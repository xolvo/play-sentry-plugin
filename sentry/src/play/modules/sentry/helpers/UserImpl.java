package play.modules.sentry.helpers;

import java.util.Map;

/**
 * Support class for User base model.
 *
 * @author Alexander Lomov
 * @since 1.0
 */
class UserImpl implements User {

    private Object id;
    private String username;
    private String email;
    private String ipAddress;
    private Map<String, Object> extra;

    UserImpl(Object id, String username, String email, String ipAddress, Map<String, Object> extra) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.ipAddress = ipAddress;
        this.extra = extra;
    }

    @Override
    public Object id() {
        return id;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String ipAddress() {
        return ipAddress;
    }

    @Override
    public Map<String, Object> extra() {
        return extra;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

}
