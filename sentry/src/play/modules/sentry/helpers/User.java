package play.modules.sentry.helpers;

import java.util.Map;

public interface User {

    /**
     * The unique ID of the user.
     */
	Object id();

    /**
     * The username of the user
     */
	String username();

    /**
     * The email address of the user.
     */
    String email();

    /**
     * The IP of the user.
     */
    String ipAddress();

    /**
     * Any other key-valued extra information about the user.
     */
    Map<String, Object> extra();

}
