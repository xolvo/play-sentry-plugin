package play.modules.sentry;

import net.kencochrane.raven.Dsn;
import net.kencochrane.raven.Raven;
import play.Play;
import play.PlayPlugin;

public class SentryPlugin extends PlayPlugin {
	private static Raven raven;
	
	@Override
	public void onApplicationStart() {
		String dsn_string = Play.configuration.getProperty("sentry.dsn");
		Dsn dsn = new Dsn(dsn_string);
		
    	raven = CustomRavenFactory.ravenInstance(dsn);
	}
	
	public static Raven raven() {
		return raven;
	}
}
