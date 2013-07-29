package play.modules.sentry.helpers;

public class Utils {
	public static String determineCulprit(Throwable throwable) {
		Throwable currentThrowable = throwable;
        String culprit = null;
        
        while (currentThrowable != null) {
            StackTraceElement[] elements = currentThrowable.getStackTrace();
            if (elements.length > 0) {
                StackTraceElement trace = elements[0];
                culprit = trace.getClassName() + "." + trace.getMethodName();
            }
            currentThrowable = currentThrowable.getCause();
        }
        
        return culprit;
	}
	
	public static String format(String message, Object... args) {
		try {
            if(args != null && args.length > 0) {
                return String.format(message, args);
            }
            return message;
        } catch (Exception e) {
            return message;
        }
	}
	
	public static StackTraceElement getMeaningfullStackTraceElement(StackTraceElement[] elems) {
		for(StackTraceElement el : elems) {
			if((el.getClassName() + "." + el.getMethodName()).equals("java.lang.Thread.getStackTrace"))
				continue;
			
			if(!el.getClassName().equals("play.modules.sentry.SentryLogger"))
				return el;
		}
		
		return null;
	}
}
