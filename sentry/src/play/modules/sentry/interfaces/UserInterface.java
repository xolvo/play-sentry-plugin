package play.modules.sentry.interfaces;

import net.kencochrane.raven.event.interfaces.SentryInterface;
import play.modules.sentry.helpers.UserModel;

public class UserInterface implements SentryInterface {
	private final UserModel user;
	
	public UserInterface(UserModel user) {
		this.user = user;
	}
	
	@Override
	public String getInterfaceName() {
		return "user";
	}

	public UserModel getUser() {
		return user;
	}
}
