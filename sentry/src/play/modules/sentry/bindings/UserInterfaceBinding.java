package play.modules.sentry.bindings;

import java.io.IOException;

import net.kencochrane.raven.marshaller.json.InterfaceBinding;
import play.modules.sentry.helpers.UserModel;
import play.modules.sentry.interfaces.UserInterface;

import com.fasterxml.jackson.core.JsonGenerator;

public class UserInterfaceBinding implements InterfaceBinding<UserInterface> {

	@Override
	public void writeInterface(JsonGenerator generator, UserInterface userInterface) throws IOException {
		UserModel user = userInterface.getUser();
		
		generator.writeStartObject();
		generator.writeObjectField("id", user._getId());
		generator.writeStringField("username", user.getUsername());
		generator.writeStringField("group", user.getGroupName());
		generator.writeEndObject();
	}

}
