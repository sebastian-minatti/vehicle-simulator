package model.schema;

import java.util.UUID;

import core.Direction;

public final class Car implements Vehicle {
	private final UUID id;
	private Coordinate coordinate;
	private final Direction direction;
	
	public Car(final Coordinate coordinate, final Direction direction) {
		this.id = UUID.randomUUID();
		this.coordinate = coordinate;
		this.direction = direction;
	}

	public UUID getId() {
		return id;
	}

	/** I need to simulate the car 's movements so i need to update the coordinates **/
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	@Override
	public void move() {
		Coordinate coordinate = direction.update(this.coordinate);
		this.setCoordinate(coordinate);
	}

	@Override
	public void update(Coordinate coordinate) {
		this.setCoordinate(coordinate);
	}
	
	@Override
	public String toString() {
		return "The Car with id: " + this.id + 
				" with coordinate: " + this.coordinate;
	}
}
