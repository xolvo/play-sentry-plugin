package play.modules.sentry;

import net.kencochrane.raven.Raven;
import net.kencochrane.raven.event.Event;
import net.kencochrane.raven.event.Event.Level;
import net.kencochrane.raven.event.EventBuilder;
import net.kencochrane.raven.event.interfaces.ExceptionInterface;
import play.Logger;
import play.modules.sentry.helpers.User;
import play.modules.sentry.helpers.Utils;
import play.modules.sentry.interfaces.PlayHttpRequestInterface;
import play.modules.sentry.interfaces.UserInterface;
import play.mvc.Http.Request;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class SentryLogger {

	private Raven client;
	private EventBuilder builder;
	private Throwable exception;
	
	protected SentryLogger(Raven client) {
		this.client = client;
		this.builder = new EventBuilder();
	}
	
	/**
	 * Set exception and message from this exception
	 * 
	 * @param e exception
	 * @return {@link SentryLogger}
	 */
	public SentryLogger setException(Throwable e) {
		if(e != null) {
			this.exception = e;
			
			builder
				.withCulprit(Utils.determineCulprit(e))
				.withMessage(e.getMessage())
				.withSentryInterface(new ExceptionInterface(e));
		}
		
		return this;
	}
	
	/**
	 * Set only message.
	 * No need to call it after exception was set
	 * 
	 * @param message the message
	 */
	public SentryLogger setMessage(String message) {
		builder.withMessage(message);
		return this;
	}
	
	public SentryLogger setCulprit(StackTraceElement frame) {
		if(frame != null)
			builder.withCulprit(frame.getClassName() + "." + frame.getMethodName());
		
		return this;
	}
	
	public SentryLogger setCulprit(String culprit) {
		builder.withCulprit(culprit);
		return this;
	}
	
	public SentryLogger setUser(User user) {
		if(user != null)
			builder.withSentryInterface(new UserInterface(user));
		
		return this;
	}
	
	public SentryLogger setRequest(Request request) {
		builder.withSentryInterface(new PlayHttpRequestInterface(request));
		return this;
	}
	
	public SentryLogger setLevel(Level level) {
		builder.withLevel(level);
		return this;
	}
	
	public SentryLogger setLogger(String logger) {
		builder.withLogger(logger);
		return this;
	}
	
	public SentryLogger addTags(Map<String, String> tags) {
		if(tags != null) {
			for(String tag : tags.keySet()) {
				String v = tags.get(tag);
				
				if(v != null)
					builder.withTag(tag, v);
			}
		}
		
		return this;
	}
	
	public SentryLogger addTag(String tag, String value) {
		builder.withTag(tag, value);
		return this;
	}
	
	public SentryLogger addExtras(Map<String, Object> extras) {
		if(extras != null) {
			for(String extra : extras.keySet()) {
				Object v = extras.get(extra);
				
				if(v != null)
					builder.withExtra(extra, v);
			}
		}
		
		return this;
	}
	
	public SentryLogger addExtra(String extra, Object value) {
		if(extra != null && value != null)
			builder.withExtra(extra, value);
		
		return this;
	}
	
	public void log() {
		try {
			builder.withServerName(InetAddress.getLocalHost().getHostName());
		} catch(UnknownHostException e) {
			Logger.warn(e, "Can not set SERVER_NAME for Raven client");
		}
		
		Event sentry_event = builder.build();
		if(SentryPlugin.isEnabled())
			client.sendEvent(sentry_event);
		
		logWithNativeLogger(sentry_event);
	}
	
	private void logWithNativeLogger(Event sentry_event) {
		Level level = sentry_event.getLevel();
		String message = sentry_event.getMessage();
		
		if(level == null)
			level = Level.ERROR;
		
		switch (level) {
		case FATAL:
			Logger.fatal(exception, message);
			break;
			
		case ERROR:
			Logger.error(exception, message);
			break;
			
		case WARNING:
			Logger.warn(exception, message);
			break;
			
		case INFO:
			Logger.info(exception, message);
			break;
			
		case DEBUG:
			Logger.debug(exception, message);
			break;

		default:
			break;
		}
	}
	
	private static void log(Level level, Throwable e, String culprit, String message, Object... args) {
		SentryLogger logger =
			getInstance()
			.setLevel(level)
			.setException(e)
			.setMessage(Utils.format(message, args));
		
		if(e == null && (culprit == null || culprit.isEmpty())) {
			StackTraceElement frame =
			Utils.getMeaningfullStackTraceElement(
				Thread.currentThread().getStackTrace());
			
			logger.setCulprit(frame);
		} else {
			logger.setCulprit(culprit);
		}
		
		logger.log();
	}
	
	private static void log(Level level, String message, Object... args) {
		log(level, null, null, message, args);
	}
	
	private static void log(Level level, String culprit, String message, Object... args) {
		log(level, null, culprit, message, args);
	}
	
	private static void log(Level level, Throwable e, String message, Object... args) {
		log(level, e, null, message, args);
	}
	
	public static SentryLogger getInstance() {
		return new SentryLogger(SentryPlugin.raven());
	}
	
	public static void info(String message, Object... args) {
		log(Level.INFO, message, args);
	}
	
	/**
	 * Send event with info level.
	 * 
	 * @param culprit can be defined here and mean header for message
	 * @param message string with format patterns like <code>%s, %d, %f</code>
	 * @param args for message formatting
	 */
	public static void info(String culprit, String message, Object... args) {
		log(Level.INFO, culprit, message, args);
	}
	
	public static void info(Throwable e, String message, Object... args) {
		log(Level.INFO, e, message, args);
	}
	
	public static void debug(String message, Object... args) {
		log(Level.DEBUG, message, args);
	}
	
	public static void debug(String culprit, String message, Object... args) {
		log(Level.DEBUG, culprit, message, args);
	}
	
	public static void debug(Throwable e, String message, Object... args) {
		log(Level.DEBUG, e, message, args);
	}
	
	public static void warn(String message, Object... args) {
		log(Level.WARNING, message, args);
	}
	
	public static void warn(String culprit, String message, Object... args) {
		log(Level.WARNING, culprit, message, args);
	}
	
	public static void warn(Throwable e, String message, Object... args) {
		log(Level.WARNING, e, message, args);
	}
	
	public static void error(String message, Object... args) {
		log(Level.ERROR, message, args);
	}
	
	public static void error(String culprit, String message, Object... args) {
		log(Level.ERROR, culprit, message, args);
	}
	
	public static void error(Throwable e, String message, Object... args) {
		log(Level.ERROR, e, message, args);
	}
	
	public static void fatal(String message, Object... args) {
		log(Level.FATAL, message, args);
	}
	
	public static void fatal(String culprit, String message, Object... args) {
		log(Level.FATAL, culprit, message, args);
	}
	
	public static void fatal(Throwable e, String message, Object... args) {
		log(Level.FATAL, e, message, args);
	}

}
