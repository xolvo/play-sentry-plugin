package play.modules.sentry.bindings;

import com.fasterxml.jackson.core.JsonGenerator;
import net.kencochrane.raven.marshaller.json.InterfaceBinding;
import play.modules.sentry.helpers.User;
import play.modules.sentry.interfaces.UserInterface;

import java.io.IOException;
import java.util.Map;

public class UserInterfaceBinding implements InterfaceBinding<UserInterface> {

	@Override
	public void writeInterface(JsonGenerator generator, UserInterface userInterface) throws IOException {
		User user = userInterface.getUser();
		
		generator.writeStartObject();
		generator.writeObjectField("id", user.id());
		generator.writeStringField("username", user.username());
		generator.writeStringField("email", user.email());
		generator.writeStringField("ip_address", user.ipAddress());
        processExtra(generator, user.extra());
		generator.writeEndObject();
	}

    private void processExtra(JsonGenerator generator, Map<String, Object> extra) throws IOException {
        if(extra != null) {
            for(Map.Entry<String, Object> e : extra.entrySet()) {
                generator.writeObjectField(e.getKey(), e.getValue());
            }
        }
    }

}
