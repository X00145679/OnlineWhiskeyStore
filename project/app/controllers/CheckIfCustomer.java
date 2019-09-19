package controllers;

import play.mvc.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

import models.users.*;

/* - Docs -
https://alexgaribay.com/2014/06/16/authentication-in-play-framework-using-java/
https://www.playframework.com/documentation/2.2.x/JavaActionsComposition
*/

// Check if this is an admin user (before permitting an action)
public class CheckIfCustomer extends Action.Simple {

    
    public CompletionStage<Result> call(Http.Context ctx) {

        
        String id = ctx.session().get("email");
        if (id != null) {
            User u = User.getUserById(id);
            if ("customer".equals(u.getRole())) {

                return delegate.call(ctx);
            }
        }
       
        ctx.flash().put("error", "Login Required.");
        return CompletableFuture.completedFuture(redirect(routes.LoginController.login()));
    }
}