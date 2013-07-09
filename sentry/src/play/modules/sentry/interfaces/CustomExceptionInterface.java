package play.modules.sentry.interfaces;

import net.kencochrane.raven.event.interfaces.ExceptionInterface;

public class CustomExceptionInterface extends ExceptionInterface {
	public CustomExceptionInterface(Throwable throwable) {
		super(throwable);
	}
	
	@Override
    public String getInterfaceName() {
        return "exception";
    }	
}
