package play.modules.sentry.interfaces;

import net.kencochrane.raven.event.interfaces.SentryInterface;
import play.modules.sentry.helpers.User;

public class UserInterface implements SentryInterface {
	private final User user;
	
	public UserInterface(User user) {
		this.user = user;
	}
	
	@Override
	public String getInterfaceName() {
		return "user";
	}

	public User getUser() {
		return user;
	}
}
