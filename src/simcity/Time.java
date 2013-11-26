package simcity;

public class Time
{
	int hour;
	int minute;
	Day day;
	
	public Time(Day d, int h, int m) {
		day = d;
		hour = h;
		minute = m;
	}
	
	public int getHour() {
		return hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public Day getDay() {
		return day;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	public void setDay(Day day) {
		this.day = day;
	}
	
	public Time plus(int minutes) {
		Day d = day;
		int h = hour;
		int m = minute;
		if (m + minutes >= 60) {
			h++;
			m = minutes - (60 - m);
			if (h == 24) {
				h = 0;
				d = d.next();
			}
		} else {
			m += minutes;
		}
		return new Time(d, h, m);
	}
	
	public boolean greaterThanOrEqualTo(Time t) {
		if (hour >= t.hour) {
			return true;
		}
		if (hour == t.hour && minute >= t.minute) {
			return true;
		}
		return false;
	}
	
	public boolean isEqualTo(Time t) {
		if (day == t.day && hour == t.hour && minute == t.minute) {
			return true;
		}
		return false;
	}
	
}
