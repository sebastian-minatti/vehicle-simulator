package actors;

import actors.SimulatorProtocol.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import model.schema.Coordinate;
import model.schema.Vehicle;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

/**
 * Simulate a vehicle 's movements
 *
 */
public class VehicleSimulatorActor extends AbstractActor {

	private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	private final Vehicle vehicle;
	
    private final ActorSystem actorSystem;
    
    private final ExecutionContext executionContext;    
    
    private Cancellable cancellable;
    
	public VehicleSimulatorActor(final Vehicle vehicle, final ActorSystem actorSystem, final ExecutionContext executionContext){
		this.vehicle = vehicle;
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;
        
        // schedule for simulate vehicle's movements.
        this.cancellable = this.actorSystem.scheduler().schedule(Duration.create(2, TimeUnit.SECONDS),
				Duration.create(2, TimeUnit.SECONDS), () -> simulate(), this.executionContext);
	}
	
	public static Props getProps(final Vehicle vehicle, final ActorSystem actorSystem, final ExecutionContext executionContext) {
		return Props.create(VehicleSimulatorActor.class, vehicle, actorSystem, executionContext);
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Update.class, update -> {
					logger.info("Received message {}", update);

					CompletableFuture<Update> result = CompletableFuture.supplyAsync(() -> {
						cancellable.cancel();							
						return checkStatus(update);	
					});		
					
					sender().tell( result.get(), self());
				}).build();
	}
	
	/**
	 * Update coordinate and start again process simulator.
	 * 
	 * @param coordinate
	 */
	private void update(Coordinate coordinate){
		vehicle.update(coordinate);
		
        this.cancellable = this.actorSystem.scheduler().schedule(Duration.create(2, TimeUnit.SECONDS),
				Duration.create(2, TimeUnit.SECONDS), () -> simulate(), this.executionContext);
	}
	
	/**
	 * Verify cancelled status.
	 * 
	 * @param msg
	 * @return
	 */
	private Update checkStatus(Update msg){
		Update message;
		
		if(this.cancellable.isCancelled()){
			update(msg.getCoordinate());
			message = new SuccessfullyUpdated(msg.getId(), msg.getCoordinate());
		} else {
			message = new FailureUpdating(msg.getId(), msg.getCoordinate());
		}
		
		return message;
	}
	
	/**
	 * Simulate process. 
	 */
	private void simulate(){
		vehicle.move();
		logger.info("Vehicle {}", vehicle);
	}
}
