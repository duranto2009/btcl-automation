package util;

import java.util.Calendar;

public class CalendarUtils {
	public static int getMonth(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH)+1;
	}
	public static int getYear(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}
	public static int getMonthFromTime(long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return calendar.get(Calendar.MONTH)+1;
	}
	public static int getYearFromTime(long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return calendar.get(Calendar.YEAR);
	}
}
