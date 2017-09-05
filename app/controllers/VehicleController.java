package controllers;

import static akka.pattern.Patterns.ask;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;

import com.google.inject.Inject;

import actors.SimulatorProtocol.*;

import akka.actor.ActorRef;
import akka.util.Timeout;

import controllers.forms.UpdateVehicleForm;
import model.schema.Coordinate;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import static play.libs.Json.toJson;

import scala.compat.java8.FutureConverters;

public class VehicleController extends Controller {

	private final Timeout timeout = new Timeout(2, TimeUnit.SECONDS);
	
    private final ActorRef actor;
    
    private final FormFactory formFactory;

    @Inject public VehicleController(@Named("simulator-actor") ActorRef simulatorActor, FormFactory formFactory) {
        this.actor = simulatorActor;
        this.formFactory = formFactory;
    }

    public CompletionStage<Result> get() {
    	return FutureConverters.toJava(ask(actor, new Retrieve(), timeout))
                .thenApply( response -> ok( toJson(response)));
    }
    
    public CompletionStage<Result> create() {
    	return FutureConverters.toJava(ask(actor, new Create(1), timeout)) // this should be get from HTTP Request
                .thenApply( response -> created( toJson(response)));
    }

    public CompletionStage<Result> update() {
 	   Form<UpdateVehicleForm> form = formFactory.form(UpdateVehicleForm.class).bindFromRequest();
    	
    	if(form.hasErrors()){
    		return CompletableFuture.supplyAsync(()	-> badRequest());
    	} else {
    		// Exceptions are handled by ErrorHandler
    		UpdateVehicleForm request = form.get();
    		Coordinate coordinate = new Coordinate(request.getLatitude(), request.getLongitude());
    		Update msg = new Update(request.getId(), coordinate);
    		
    		return FutureConverters.toJava(ask(actor, msg, timeout))
                .thenApply( response -> created( toJson(response)));
    	}    	
    }

}
