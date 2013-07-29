package play.modules.sentry;

import net.kencochrane.raven.Dsn;
import net.kencochrane.raven.Raven;
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
	
	public static Raven raven() {
		return raven;
	}
	
	public static boolean isEnabled() {
		return enabled;
	}
}
