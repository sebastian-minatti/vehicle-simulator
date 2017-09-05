package factory;

import core.Direction;
import core.Foward;
import model.schema.Car;
import model.schema.Coordinate;
import model.schema.Vehicle;

/**
 * Factory for create Car instances. 
 *
 */
public class CarFactory implements Factory {

	private final Coordinate defaultCoordinate = new Coordinate((double)-34.44,(double)-58.61);
	
	private final Direction defaultDirection = new Foward((double)12.5, 90);
	
	public CarFactory() {}
	
	@Override
	public Vehicle create() {
		return new Car(defaultCoordinate, defaultDirection);
	}

	@Override
	public Vehicle create(Coordinate coordinate, Direction direction) {
		return new Car(coordinate, direction);
	}

}
