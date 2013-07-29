package play.modules.sentry;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.kencochrane.raven.Raven;
import net.kencochrane.raven.event.Event;
import net.kencochrane.raven.event.EventBuilder;
import play.Logger;
import play.Play;
import play.modules.sentry.helpers.UserModel;
import play.modules.sentry.interfaces.CustomExceptionInterface;
import play.modules.sentry.interfaces.PlayHttpRequestInterface;
import play.modules.sentry.interfaces.UserInterface;
import play.mvc.Http.Request;

@Deprecated
public class ExceptionTracker {
	private Raven client;
	
	private Throwable e;
	private Map<String, Object> extras = new HashMap<String, Object>();
	
	private UserModel user;
	
	private static final boolean enabled = Boolean.parseBoolean(Play.configuration.getProperty("sentry.enabled", "true"));
	
	protected ExceptionTracker(Raven client) {
		this.client = client;
	}
	
	public ExceptionTracker addExtra(String key, Object value) {
		extras.put(key, value);
		return this;
	}
	
	public static ExceptionTracker getInstance() {
		return new ExceptionTracker(SentryPlugin.raven());
	}
	
	public static UUID log(Throwable e) {
		return getInstance().setException(e).log();
	}
	
	public ExceptionTracker setException(Throwable e) {
		this.e = e;
		return this;
	}
	
	public ExceptionTracker setUser(UserModel user) {
		this.user = user;
		return this;
	}
	
	private static String determineCulprit(Throwable throwable) {
        Throwable currentThrowable = throwable;
        String culprit = null;
        
        while (currentThrowable != null) {
            StackTraceElement[] elements = currentThrowable.getStackTrace();
            if (elements.length > 0) {
                StackTraceElement trace = elements[0];
                culprit = trace.getClassName() + "." + trace.getMethodName();
            }
            currentThrowable = currentThrowable.getCause();
        }
        
        return culprit;
    }
	
	public UUID log() {
		if(!enabled || e == null)
			return null;
		
		String message = e.getMessage();
		if(message == null)
			message = e.getClass().getSimpleName();
		
		EventBuilder builder = new EventBuilder()
			.setMessage(message)
			.setCulprit(determineCulprit(e))
			.addSentryInterface(new CustomExceptionInterface(e));
		
		try {
			builder.setServerName(InetAddress.getLocalHost().getHostName());
		} catch(UnknownHostException e) {
			Logger.warn(e, "Can not set SERVER_NAME for Raven client");
		}
		
		Request request = Request.current();
		if(request != null)
			builder.addSentryInterface(
				new PlayHttpRequestInterface(request));
		
		if(user != null)
			builder.addSentryInterface(
				new UserInterface(user));
		
		if(!extras.isEmpty()) {
			for(String key : extras.keySet()) {
				Object object = extras.get(key);
				
				if(object != null)
					builder.addExtra(key, object);
			}
		}
		
		Event event = builder.build();
		client.sendEvent(event);
		
		return event.getId();
	}
}
