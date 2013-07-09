package play.modules.sentry.bindings;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import net.kencochrane.raven.event.interfaces.ImmutableThrowable;
import net.kencochrane.raven.event.interfaces.StackTraceInterface;
import net.kencochrane.raven.marshaller.json.InterfaceBinding;
import play.Logger;
import play.modules.sentry.interfaces.CustomExceptionInterface;

import com.fasterxml.jackson.core.JsonGenerator;

public class CustomExceptionInterfaceBinding implements InterfaceBinding<CustomExceptionInterface> {
    private static final String TYPE_PARAMETER = "type";
    private static final String VALUE_PARAMETER = "value";
    private static final String MODULE_PARAMETER = "module";
    private static final String STACKTRACE_PARAMETER = "stacktrace";
    private final InterfaceBinding<StackTraceInterface> stackTraceInterfaceBinding;

    public CustomExceptionInterfaceBinding(InterfaceBinding<StackTraceInterface> stackTraceInterfaceBinding) {
        this.stackTraceInterfaceBinding = stackTraceInterfaceBinding;
    }

    @Override
    public void writeInterface(JsonGenerator generator, CustomExceptionInterface exceptionInterface) throws IOException {
        Set<ImmutableThrowable> dejaVu = new HashSet<ImmutableThrowable>();
        ImmutableThrowable throwable = exceptionInterface.getThrowable();
        StackTraceElement[] enclosingStackTrace = new StackTraceElement[0];
        
        generator.writeStartObject();
        while (throwable != null) {
            dejaVu.add(throwable);

            generator.writeStringField(TYPE_PARAMETER, throwable.getActualClass().getName());
            generator.writeStringField(VALUE_PARAMETER, throwable.getMessage());
            
            if(throwable.getActualClass().getPackage() != null)
            	generator.writeStringField(MODULE_PARAMETER, throwable.getActualClass().getPackage().getName());
            else
            	generator.writeStringField(MODULE_PARAMETER, "");
            
            generator.writeFieldName(STACKTRACE_PARAMETER);
            stackTraceInterfaceBinding.writeInterface(generator,
                    new StackTraceInterface(throwable.getStackTrace(), enclosingStackTrace));
            
            enclosingStackTrace = throwable.getStackTrace();
            throwable = throwable.getCause();

            if (dejaVu.contains(throwable)) {
                Logger.warn("Exiting a circular referencing exception!");
                break;
            }
        }
        generator.writeEndObject();
    }
}
