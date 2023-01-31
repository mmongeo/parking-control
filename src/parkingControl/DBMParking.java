package parkingControl;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.mysql.jdbc.ResultSetMetaData;


public class DBMParking {


	private DataSource ds;
	public DBMParking(){
		ds = DataSourceFactory.getMySQLDataSource();
	}
	
	public static String DIA = "dia";
	public static String LIVIANO = "liviano";
	public static String PESADO = "pesado";
	public static String MOTO = "moto";
	
	
	
	public int getValuePerHour(ParkedVehicle vehicle, String timeOfDay, String timeAmount){
		int value = 0;
		Connection con = null;
		java.sql.PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement("SELECT price FROM price "
					+ "WHERE vehicle_type = ? AND time_of_day = ? and time_amount = ?");
			stmt.setString(1, vehicle.getVehicleType());
			stmt.setString(2, timeOfDay);
			stmt.setString(3, timeAmount);
			rs = stmt.executeQuery();
			if(rs.next()){
				value = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	public ParkedVehicle getVehicleById(int id){
		ParkedVehicle v = null;
		Connection con = null;
		java.sql.PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement("select plate, entry_time, vehicle_type, id from current_vehicle WHERE id = ?");
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if(rs.next()){
				v = new ParkedVehicle();
				v.setPlate(rs.getString(1));
				v.setEntryTime(rs.getTimestamp(2));
				v.setVehicleType(rs.getString(3));
				v.setId(rs.getInt(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return v;
	}

	public List<ParkedVehicle> getCurrentVehicles(){
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<ParkedVehicle> listVehicles = new ArrayList<ParkedVehicle>();
		try {
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select plate, entry_time, vehicle_type, id from current_vehicle");
			while(rs.next()){
				ParkedVehicle v = new ParkedVehicle();
				v.setPlate(rs.getString(1));
				v.setEntryTime(rs.getTimestamp(2));
				v.setVehicleType(rs.getString(3));
				v.setId(rs.getInt(4));
				listVehicles.add(v);
			}
			printResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listVehicles;
	}
	
	public void deleteVehicle(ParkedVehicleHistory vehicle){
		Connection con = null;
		java.sql.PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement("INSERT INTO vehicle_history (plate, entry_time, "
					+ "departure_time, charge, vehicle_type) VALUES(?,?,?,?,?)");
			stmt.setString(1, vehicle.getPlate());
			stmt.setTimestamp(2, vehicle.getEntryTime());
			//java.util.Date date = new java.util.Date();
			//Timestamp timestamp = new Timestamp(date.getTime());
			stmt.setTimestamp(3, vehicle.getDepartureTime());
			stmt.setInt(4, vehicle.getCharge());
			stmt.setString(5, vehicle.getVehicleType());
			stmt.executeUpdate();
			stmt.close();
			
			stmt = con.prepareStatement("DELETE FROM current_vehicle WHERE id = ?");
			stmt.setInt(1, vehicle.getId());
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
		
	public void insertVehicle(ParkedVehicle vehicle){
		Connection con = null;
		java.sql.PreparedStatement stmt = null;
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement("INSERT INTO current_vehicle (plate, entry_time, "
					+ "vehicle_type) values (?, ?, ?)");
			stmt.setString(1, vehicle.getPlate());			
			//java.util.Date date = new java.util.Date();
			//Timestamp timestamp = new Timestamp(date.getTime());
			stmt.setTimestamp(2, vehicle.getEntryTime());
			//stmt.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
			//stmt.setDate(3, vehicle.getEntryDate());
			stmt.setString(3, vehicle.getVehicleType());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(stmt != null) stmt.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}			
	}
	
	
	
	public void printResultSet(ResultSet rs){
		ResultSetMetaData rsmd;
		try {
			rsmd = (ResultSetMetaData) rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1) System.out.print(",  ");
					String columnValue = rs.getString(i);
					System.out.print(columnValue + " " + rsmd.getColumnName(i));
				}
				System.out.println("");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

/*	public ResultSet searchVehicles(String plate){
		return getQueryResult("select plate, entry_time, entry_date, vehicle_type "
				+ "from current_vehicle WHERE plate LIKE %" + plate + "%");
	}*/

}



