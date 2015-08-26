package play.modules.sentry.helpers;

import java.util.Map;

/**
 * Factory to create {@link User} instances.
 *
 * @author Alexander Lomov
 * @since 1.0
 */
public final class Users {

    private Users() {}

    private static User newInstance(
            Object id, String username, String email, String ipAddress, Map<String, Object> extra) {

        return new UserImpl(id, username, email, ipAddress, extra);
    }

    public static User from(Object id) {
        return newInstance(id, null, null, null, null);
    }

    public static User from(Object id, Map<String, Object> extra) {
        return newInstance(id, null, null, null, extra);
    }

    public static User from(Object id, String username) {
        return newInstance(id, username, null, null, null);
    }

    public static User from(Object id, String username, Map<String, Object> extra) {
        return newInstance(id, username, null, null, extra);
    }

    public static User from(Object id, String username, String email) {
        return newInstance(id, username, email, null, null);
    }

    public static User from(Object id, String username, String email, Map<String, Object> extra) {
        return newInstance(id, username, email, null, extra);
    }

    private static User from(Object id, String username, String email, String ipAddress) {
        return newInstance(id, username, email, ipAddress, null);
    }

    private static User from(Object id, String username, String email, String ipAddress, Map<String, Object> extra) {
        return newInstance(id, username, email, ipAddress, extra);
    }

}
