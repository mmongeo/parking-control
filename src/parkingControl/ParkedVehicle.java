package parkingControl;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ParkedVehicle {
	protected String plate;
	protected String vehicleType;
	protected Timestamp entryTime;
	protected int id;
	
	public ParkedVehicle(){
				
	}
	
	public static String[] getColumnNames(){
		String [] ColumnNames = {"Placa", "Hora Entrada", "Fecha","Tipo Vehiculo", "id"};
		return ColumnNames;
	}
	
	public String[] toRow(){

		String [] rowValues = {this.plate, new SimpleDateFormat("HH:mm:ss").format(entryTime),
				new SimpleDateFormat("MM/dd/yyyy").format(entryTime), this.vehicleType, this.id + ""};
		return rowValues;
	}
	
	public ParkedVehicle(String plate, String vehicleType, Timestamp entryTime, int id) {
		super();
		this.plate = plate;
		this.vehicleType = vehicleType;
		this.entryTime = entryTime;
		this.id = id;
	}
	public String getPlate() {
		return plate;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public Timestamp getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(Timestamp entryTime) {
		this.entryTime = entryTime;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
