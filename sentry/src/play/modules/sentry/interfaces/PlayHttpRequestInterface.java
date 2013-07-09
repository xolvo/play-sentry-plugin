package play.modules.sentry.interfaces;

import net.kencochrane.raven.event.interfaces.SentryInterface;
import play.mvc.Http.Request;

public class PlayHttpRequestInterface implements SentryInterface {
	public static final String HTTP_INTERFACE = "sentry.interfaces.Http";
	private final Request request;
	
	public PlayHttpRequestInterface(Request request) {
		this.request = request;
	}

	@Override
	public String getInterfaceName() {
		return HTTP_INTERFACE;
	}
	
	public Request getRequest() {
		return request;
	}
}
