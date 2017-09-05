package actors;

import java.util.Set;
import java.util.UUID;

import model.schema.Coordinate;

public class SimulatorProtocol {

	/**
	 * Retrieve actors for simulation process.
	 *
	 */
	public static final class Retrieve {
		@Override
		public String toString() {
			return getClass().getName();
		}
	}
	
	/**
	 * The actor creation was successfully done.
	 *
	 */
	public static final class SuccessfullyRetrieved {
		private final Set<UUID> vehicles;


		public SuccessfullyRetrieved(Set<UUID> vehicles) {
			super();
			this.vehicles = vehicles;
		}
		
		public Set<UUID> getVehicles() {
			return vehicles;
		}
		
		@Override
		public String toString() {
			return getClass().getName();
		}		
	}
	
	/**
	 * Create actor for simulation process.
	 *
	 */
	public static final class Create {
		private final int quantity;
		
		public Create(int quantity) {
			super();
			this.quantity = quantity;
		}
		public int getQuantity() {
			return quantity;
		}
		@Override
		public String toString() {
			return getClass().getName();
		}
	}
	
	/**
	 * The actor creation was successfully done.
	 *
	 */
	public static final class SuccessfullyCreated {
		private final Set<UUID> vehicles;


		public SuccessfullyCreated(Set<UUID> vehicles) {
			super();
			this.vehicles = vehicles;
		}
		
		public Set<UUID> getVehicles() {
			return vehicles;
		}
		
		@Override
		public String toString() {
			return getClass().getName();
		}		
	}
	
	/**
	 * Start simulation.
	 *
	 */
    public static final class Start {    	
		@Override
		public String toString() {
			return getClass().getName();
		}    	
    }
    
	/**
	 * Update simulation process.
	 *
	 */
    public static class Update {    
    	private UUID id;
    	private Coordinate coordinate;


		public Update(UUID id, Coordinate coordinate) {
			super();
			this.id = id;
			this.coordinate = coordinate;
		}  
		
		public UUID getId() {
			return id;
		}

		public Coordinate getCoordinate() {
			return coordinate;
		}

		@Override
		public String toString() {
			return getClass().getName();
		}
  	
    }
    
	/**
	 * Failure updating simulation.
	 *
	 */
	public static final class FailureUpdating extends Update {
		
		public FailureUpdating(UUID id, Coordinate coordinate) {
			super(id, coordinate);
		}

		@Override
		public String toString() {
			return getClass().getName();
		}		
	}
    
	/**
	 * Successfully updated simulation.
	 *
	 */
	public static final class SuccessfullyUpdated extends Update {
		
		public SuccessfullyUpdated(UUID id, Coordinate coordinate) {
			super(id, coordinate);
		}

		@Override
		public String toString() {
			return getClass().getName();
		}		
	}
}
