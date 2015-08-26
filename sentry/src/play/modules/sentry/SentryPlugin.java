package play.modules.sentry;

import net.kencochrane.raven.Raven;
import net.kencochrane.raven.dsn.Dsn;
import play.PlayPlugin;

import static play.Play.configuration;

public class SentryPlugin extends PlayPlugin {

	private static Raven raven;
	private static boolean enabled;
	
	@Override
	public void onApplicationStart() {
        if(raven != null)
            return;

		String dsn_string = configuration.getProperty("sentry.dsn");
		Dsn dsn = new Dsn(dsn_string);
		
    	raven = CustomRavenFactory.ravenInstance(dsn);
        enabled = Boolean.parseBoolean(configuration.getProperty("sentry.enabled", "true"));
	}
	
	public static Raven raven() {
		return raven;
	}
	
	public static boolean isEnabled() {
		return enabled;
	}

}
