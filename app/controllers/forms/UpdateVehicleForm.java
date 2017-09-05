package controllers.forms;

import java.util.UUID;

public class UpdateVehicleForm {
	private UUID id;
	private double latitude;
	private double longitude;

	public UpdateVehicleForm() {}	

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
}
