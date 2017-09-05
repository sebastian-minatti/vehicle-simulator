package factory;

import core.Direction;
import model.schema.Coordinate;
import model.schema.Vehicle;

public interface Factory {
	
	public Vehicle create(Coordinate coordinate, Direction direction);
	
	public Vehicle create();
}
