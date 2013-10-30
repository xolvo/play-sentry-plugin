package controllers;

import models.User;
import play.modules.sentry.SentryLogger;
import play.mvc.Controller;

public class Application extends Controller {
    public static void index() {
    	User user = new User();
    	user.id = 3L;
    	user.username = "testuser";
    	
    	SentryLogger.getInstance()
    		.setException(new RuntimeException("Oops.. Test exception occured"))
    		.setUser(user)
    		.setRequest(request)
    		.addExtra("order_id", 234234)
    		.addExtra("test", null) // will not be logged (no NPE)
    		.log();
    	
    	renderText("Exception was logged");
    }
}
