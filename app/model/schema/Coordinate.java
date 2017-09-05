package model.schema;

public final class Coordinate {
	private final double latitude;
	private final double longitude;
	
	public Coordinate(final double latitude, final double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "latitude: " + this.latitude +
			   " longitude: " + this.longitude;
	}

}
