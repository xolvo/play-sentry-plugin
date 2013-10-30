package play.modules.sentry;

import java.io.IOException;

import net.kencochrane.raven.Raven;
import net.kencochrane.raven.dsn.Dsn;
import play.Logger;
import play.Play;
import play.PlayPlugin;

public class SentryPlugin extends PlayPlugin {
	private static Raven raven;
	private static boolean enabled;
	
	@Override
	public void onApplicationStart() {
		String dsn_string = Play.configuration.getProperty("sentry.dsn");
		Dsn dsn = new Dsn(dsn_string);
		
    	raven = CustomRavenFactory.ravenInstance(dsn);
    	enabled = Boolean.parseBoolean(Play.configuration.getProperty("sentry.enabled", "true"));
	}
	
	@Override
	public void onApplicationStop() {
		try {
			if(raven != null) {
				raven.getConnection().close();
				raven = null;
			}
		} catch (IOException e) {
			Logger.error(e, "Error while closing connection to Sentry");
		}
	}
	
	public static Raven raven() {
		return raven;
	}
	
	public static boolean isEnabled() {
		return enabled;
	}
}
