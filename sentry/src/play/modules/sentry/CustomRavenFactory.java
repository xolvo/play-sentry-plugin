package play.modules.sentry;

import net.kencochrane.raven.DefaultRavenFactory;
import net.kencochrane.raven.Raven;
import net.kencochrane.raven.dsn.Dsn;
import net.kencochrane.raven.marshaller.Marshaller;
import net.kencochrane.raven.marshaller.json.JsonMarshaller;
import play.modules.sentry.bindings.PlayHttpRequestInterfaceBinding;
import play.modules.sentry.bindings.UserInterfaceBinding;
import play.modules.sentry.interfaces.PlayHttpRequestInterface;
import play.modules.sentry.interfaces.UserInterface;

public class CustomRavenFactory extends DefaultRavenFactory {
	public static Raven ravenInstance(Dsn dsn) {
		return new CustomRavenFactory().createRavenInstance(dsn);
	}
	
	@Override
	public Raven createRavenInstance(Dsn dsn) {
		Raven raven = new Raven();
        raven.setConnection(createConnection(dsn));
        
        return raven;
	}
	
	@Override
	protected Marshaller createMarshaller(Dsn dsn) {
		JsonMarshaller marshaller = (JsonMarshaller) super.createMarshaller(dsn);
		
		marshaller.addInterfaceBinding(PlayHttpRequestInterface.class, new PlayHttpRequestInterfaceBinding());
		marshaller.addInterfaceBinding(UserInterface.class, new UserInterfaceBinding());
		
		return marshaller;
	}
}
