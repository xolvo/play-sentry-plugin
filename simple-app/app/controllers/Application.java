package controllers;

import play.Logger;
import play.modules.sentry.SentryLogger;
import play.mvc.Controller;

import play.modules.sentry.helpers.Users;

public class Application extends Controller {

    public static void index() {
        SentryLogger.getInstance()
         .setException(new RuntimeException("Oops.. Test exception occured"))
         .setUser(Users.from(3, "test", "xollvo@gmail.com"))
         .setRequest(request)
         .addExtra("order_id", 234234)
         .addExtra("test", null)
         .log();
        
        // SentryLogger.debug("The test msg %s", 1);

        Logger.info("Opa!");
        
        renderText("Exception was logged");
    }

}
