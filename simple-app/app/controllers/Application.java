package controllers;

import play.*;
import play.modules.sentry.ExceptionTracker;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
    	User user = new User();
    	user.id = 1L;
    	user.username = "testuser";
    	
    	UUID exception_id = ExceptionTracker.getInstance()
    		.setException(new RuntimeException())
    		.setUser(user)
    		.log();
    	
    	renderText("Exception logged with id " + exception_id);
    }

}