package core;

import model.schema.Coordinate;

public class Foward implements Direction {

	private final double angle;
	private final int speed;

	public Foward(final double angle, final int speed) {
		super();
		this.angle = angle;
		this.speed = speed;
	}

	/**
	 * The formula is not correct. 
	 * 
	 * (TODO) We should improve the formula.
	 */
	@Override
	public Coordinate update(final Coordinate coordinate) {
		double longitude = coordinate.getLongitude() + this.speed * Math.cos(this.angle);
	    double latitude =  coordinate.getLatitude()+ this.speed * Math.sin(this.angle); 			
		                			
		return new Coordinate(latitude, longitude);
	}
}
