package parkingControl;

import java.sql.Date;
import java.sql.Timestamp;

public class ParkedVehicleHistory extends ParkedVehicle{
	private int charge;
	private Timestamp departureTime;
	
	
	public ParkedVehicleHistory(String plate, String vehicleType, Timestamp entryTime, Date entryDate, int id,
			int charge, Timestamp departureTime) {
		super(plate, vehicleType, entryTime, id);
		this.charge = charge;
		this.departureTime = departureTime;
	}
	
	public ParkedVehicleHistory(ParkedVehicle v){
		super(v.plate, v.vehicleType, v.entryTime, v.id);
		this.charge = 0;
		this.departureTime = null;
	}
	
	public int getCharge() {
		return charge;
	}
	public void setCharge(int charge) {
		this.charge = charge;
	}
	public Timestamp getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(Timestamp departureTime) {
		this.departureTime = departureTime;
	}
	
}
