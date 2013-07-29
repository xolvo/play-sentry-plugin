package models;

import play.db.jpa.Model;
import play.modules.sentry.helpers.UserModel;

public class User extends Model implements UserModel {
	public String username;

	@Override
	public String getGroupName() {
		return "Test Group";
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public Object _getId() {
		return id;
	}
}
