package parkingControl;

import java.sql.Time;

public class TimePrice {
	Time timeAmount;
	int price;
	
	
	public TimePrice(Time timeAmount, int price) {
		super();
		this.timeAmount = timeAmount;
		this.price = price;
	}
	public Time getTimeAmount() {
		return timeAmount;
	}
	public void setTimeAmount(Time timeAmount) {
		this.timeAmount = timeAmount;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}
