package actors;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import actors.SimulatorProtocol.*;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.Timeout;
import scala.compat.java8.FutureConverters;
import scala.concurrent.ExecutionContext;
import model.schema.Car;
import factory.CarFactory;
import factory.Factory;

import static akka.pattern.Patterns.ask;
import static play.libs.Json.toJson;

/**
 * Create actors for vehicle simulation and manage them.
 *
 */
public class VehicleSimulationAdmin extends AbstractActor {

	private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);	

    private final ActorSystem actorSystem;
    
    private final ExecutionContext executionContext;
	
    private final Timeout timeout = new Timeout(2, TimeUnit.SECONDS);

    private final Factory factory = new CarFactory();
    
	private final Map<UUID, ActorRef> vehicles = new ConcurrentHashMap<>();

	@Inject
	public VehicleSimulationAdmin(ActorSystem actorSystem, ExecutionContext executionContext) {
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;
	}
	
	public static Props getProps() {
        return Props.create(VehicleSimulationAdmin.class);
    }
	
	@Override
	public void preStart() throws Exception {
		logger.info("Initializing simulator process.");
		
		CompletableFuture.runAsync(() -> initialize());		
		// after this we could send message to the stored actors and check status.
		
		logger.info("Simulator process initialized.");
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Retrieve.class, msg -> {
					logger.info("Received message {}", msg);
					
					CompletableFuture<Set<UUID>> future = CompletableFuture.supplyAsync(() -> new HashSet<UUID>(vehicles.keySet()) );
					
					sender().tell(new SuccessfullyRetrieved(future.get()), self());		
				})
				.match(Create.class, create -> {
					logger.info("Received message {}", create);
					
					CompletableFuture<Set<UUID>> future = CompletableFuture.supplyAsync(() -> {
										create(create);
										return new HashSet<UUID>(vehicles.keySet());
					                 });

					sender().tell(new SuccessfullyCreated(future.get()), self());
				})
				.match(Update.class, update -> {
					logger.info("Received message {}", update);
					
					CompletableFuture<ActorRef> future = CompletableFuture.supplyAsync(() -> update(update));
					
					// could fail for nullpointer exception
				    future.thenApplyAsync( actor -> ask(actor, update, timeout) )
					      .thenAccept( response -> sender().tell(response, self()));					

				}).build();
	}
	
	/**
	 * Create one actor for starting the simulation process.
	 */
	private void initialize() {
		Car car = (Car) factory.create();
		
		ActorRef child = getContext().actorOf(VehicleSimulatorActor.getProps(car, this.actorSystem, this.executionContext));
		vehicles.put(car.getId(), child);		
	}
	
	/**
	 * Create more actors for simulation process.
	 * (TODO) we should check the quantity of process to create to avoid overflow.
	 */
	private void create(Create msg){
		for (int i = 0; i < msg.getQuantity(); i++) {
			Car car = (Car) factory.create();
			
			ActorRef child = getContext().actorOf(VehicleSimulatorActor.getProps(car, this.actorSystem, this.executionContext));
			vehicles.put(car.getId(), child);			
		}		
	}
	
	/**
	 * Retrieve the correct actor. 
	 */
	private ActorRef update(Update msg) throws NullPointerException{		
		return this.vehicles.get(msg.getId());
	}
}
