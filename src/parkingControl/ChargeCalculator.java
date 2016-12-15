package parkingControl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class ChargeCalculator {
	private boolean chargeFirstHour;
	private int minTimeFraction;
	private DBMParking dbm;

	public ChargeCalculator(DBMParking dbm){
		initConfigValues();
		this.dbm = dbm;
	}

	private void initConfigValues(){
		InputStream inputStream = null;
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				//error ehere 
			}
			chargeFirstHour = Boolean.parseBoolean(prop.getProperty("chargeFirstHour"));
			minTimeFraction = Integer.parseInt(prop.getProperty("minHourFraction"));
			
		} 
		catch (Exception e) {
			System.out.println("Exception: " + e);
		}
		finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
	}
	
public static long compareTwoTimeStamps(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime) {
	long milliseconds1 = oldTime.getTime();
	long milliseconds2 = currentTime.getTime();

	long diff = milliseconds2 - milliseconds1;
	long diffMinutes = diff / (60 * 1000);
	long diffHours = diff / (60 * 60 * 1000);
	long diffDays = diff / (24 * 60 * 60 * 1000);

	return diffMinutes;
}

public static String getVehicleTimeDifference(ParkedVehicleHistory v){
	long milliseconds1 = v.getEntryTime().getTime();
	long milliseconds2 = v.getDepartureTime().getTime();
	long millis = milliseconds2 - milliseconds1;
	return formatMillis(millis);
}

public static String formatMillis(long millis){

	long minute = (millis / (1000 * 60)) % 60;
	long hour = (millis / (1000 * 60 * 60)) % 24;

	return String.format("%02d:%02d:00", hour, minute, millis);

}

public ParkedVehicleHistory getVehicleWithPrice(ParkedVehicle v){
	int totalPrice = -1;
	ParkedVehicleHistory hv = new ParkedVehicleHistory(v);
	
	//assumming always day for now! 
	int pricePerHour = dbm.getValuePerHour(v, dbm.DIA, formatMillis(60* 60 * 1000));
	int pricePerMin = dbm.getValuePerHour(v, dbm.DIA, formatMillis(60 * minTimeFraction * 1000));
	java.util.Date date = new java.util.Date();
	Timestamp departureTime = new Timestamp(date.getTime());
	hv.setDepartureTime(departureTime);
	long totalMinutes = java.lang.Math.abs(compareTwoTimeStamps(v.getEntryTime(), departureTime));
	long minutes = totalMinutes;
	if(chargeFirstHour){
		totalPrice = pricePerHour;
		minutes-= 60; 
	}
	if(minutes > 0){
		totalPrice+= pricePerHour * ( minutes / 60);
		float fractions = minTimeFraction;
		fractions = (minutes % 60) / fractions;
		totalPrice+= pricePerMin * java.lang.Math.ceil(fractions);
	}
	hv.setCharge(totalPrice);
	return hv;
}

}
