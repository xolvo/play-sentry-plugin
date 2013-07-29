package controllers;

import net.kencochrane.raven.event.Event.Level;
import models.User;
import play.Logger;
import play.modules.sentry.SentryLogger;
import play.mvc.Controller;

public class Application extends Controller {
    public static void index() {
    	User user = new User();
    	user.id = 1L;
    	user.username = "testuser";
    	
    	SentryLogger.getInstance()
    		.setLevel(Level.INFO)
    		.setCulprit("Йа заголовог!")
    		.setMessage("Пыщ пыщ ололо. Я мессага ;)")
    		.setUser(user)
    		.addExtra("order_id", 234234)
    		.log();
    	
    	renderText("Exception was logged");
    }
}