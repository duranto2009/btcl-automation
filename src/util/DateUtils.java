package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
 
public class DateUtils {
	private static Logger logger = Logger.getLogger(DateUtils.class);
	private static SimpleDateFormat dayMonthYearDateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
	
	public static int getDaysInCurrentMonth() {
		
		Calendar c = Calendar.getInstance();
		
		return getDaysInMonth( c.get(Calendar.MONTH), c.get(Calendar.YEAR) );
	}
	
	public static boolean isOnSameDay(long time1, long time2) throws Exception {
		return TimeConverter.getStartTimeOfTheDay(time1) == TimeConverter.getStartTimeOfTheDay(time2);
	}
	
	
	public static int getDaysInMonth(int month,int year){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		int numDays = calendar.getActualMaximum(Calendar.DATE);
		logger.info("number of days in month "+month+" and year "+year+" is "+numDays);
		return numDays;
	}
	
	public static int getDaysInMonth(long dateTime){
		Date date = new Date(dateTime);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static long getStartTimeOfMonth(int month,int year){
		
		//jan means 0, feb means 1
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);
		return calendar.getTimeInMillis();
	}
	
	public static long getEndTimeOfMonth(int month,int year){
		int numberOfDaysInMonth = getDaysInMonth(month,year);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, numberOfDaysInMonth);
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND,59);
		calendar.set(Calendar.MILLISECOND,999);
		return calendar.getTimeInMillis();
	}
	/**
	 * @author Alam
	 * @return 
	 * @throws ParseException 
	 */
	public static int getMonthFromDateString( String dateStr ) throws ParseException {
		
		Date date = dayMonthYearDateFormat.parse( dateStr );
		
		Calendar c = Calendar.getInstance();
		c.setTime( date );
		return c.get( Calendar.MONTH );
	}
	
	public static int getMonthFromDate( long dateVal ) {
		
		Date date = new Date(dateVal);
		
		Calendar c = Calendar.getInstance();
		c.setTime( date );
		return c.get( Calendar.MONTH );
	}
	
	public static int getYearFromDateString( String dateStr ) throws ParseException {
		
		Date date = dayMonthYearDateFormat.parse( dateStr );
		
		Calendar c = Calendar.getInstance();
		c.setTime( date );
		return c.get( Calendar.YEAR );
	}
	
	public static int getYearFromDate( long dateVal ) {
		
		Date date = new Date(dateVal);
		
		Calendar c = Calendar.getInstance();
		c.setTime( date );
		return c.get( Calendar.YEAR );
	}
	/**
	 * @author Alam
	 * @param parameter
	 * @return 
	 * @throws ParseException 
	 */
	public static long getTimestampFromDateStr(String dateStr) throws ParseException {
		
		return dayMonthYearDateFormat.parse(dateStr).getTime();
	}
	
	public static long getEndTimeOfDayByDateString(String dateString) throws Exception{
		long timeInMills = getTimestampFromDateStr(dateString);
		timeInMills+=TimeConverter.MILLS_IN_A_DAY;
		timeInMills--;
		return timeInMills;
	}
	public static double getMultiplierConstantByFromToDate(long fromDate, long toDate) {
		Calendar fromCalendar = getCalendarByMillis(fromDate);
		Calendar toCalendar = getCalendarByMillis(toDate);
		int daysInFromDateMonth = DateUtils.getDaysInMonth(fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.YEAR));
		int diffInYear = getDiffInYear(toCalendar, fromCalendar);
		int diffInMonth = getDiffInMonth(toCalendar, fromCalendar);
		int diffInDays = getDiffInDays(toCalendar, fromCalendar, daysInFromDateMonth);
		logger.info("diffInYear " + diffInYear + " diffInMonth " + diffInMonth + " diffInDays " + diffInDays);
		return (12*diffInYear+diffInMonth)+diffInDays*1.0/daysInFromDateMonth;
	}
	public static Calendar getCalendarByMillis(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return calendar;
	}
	private static int getDiffInDays(Calendar toCalendar, Calendar fromCalendar, int daysInFromDateMonth) {
		int diffInDays = 0;
		if(toCalendar.get(Calendar.DAY_OF_MONTH)>=fromCalendar.get(Calendar.DAY_OF_MONTH)){
			diffInDays = toCalendar.get(Calendar.DAY_OF_MONTH)-fromCalendar.get(Calendar.DAY_OF_MONTH);
		}else{
			diffInDays = daysInFromDateMonth-fromCalendar.get(Calendar.DAY_OF_MONTH)+toCalendar.get(Calendar.DAY_OF_MONTH);
		}
		return diffInDays;
	}




	private static int getDiffInMonth(Calendar toCalendar, Calendar fromCalendar) {
		int diffInMonth = (toCalendar.get(Calendar.MONTH)-fromCalendar.get(Calendar.MONTH)+12)%12;
		fromCalendar.add(Calendar.MONTH, diffInMonth);
		if(fromCalendar.getTimeInMillis()>toCalendar.getTimeInMillis()){
			diffInMonth--;
			fromCalendar.add(Calendar.MONTH, -1);
		}
		return diffInMonth;
	}




	private static int getDiffInYear(Calendar toCalendar, Calendar fromCalendar) {
		int diffInYear = toCalendar.get(Calendar.YEAR)-fromCalendar.get(Calendar.YEAR);
		fromCalendar.add(Calendar.YEAR, diffInYear);
		if(fromCalendar.getTimeInMillis()>toCalendar.getTimeInMillis()){
			diffInYear--;
			fromCalendar.add(Calendar.YEAR, -1);
		}
		return diffInYear;
	}
	

	public static long getCurrentTime() {
		
		Calendar c = Calendar.getInstance();
		
		return c.getTimeInMillis();
	}
	
	public static Date getCurrentDateTime() {
		
		Calendar c = Calendar.getInstance();
		
		return c.getTime();
	}
	
	public static Date getFirstDayOfMonth(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		return c.getTime();
	}
	
	public static Date getLastDayOfMonth(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}


	/**
	 * 
	 * @param backValue count of month you want to go back
	 * @return
	 */
	public static Date getFirstDayOfMonth(int backValue)
	{
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, backValue);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		return c.getTime();
	}
	
	/**
	 * 
	 * @param backValue count of month you want to go back
	 * @return
	 */
	public static Date getLastDayOfMonth(int backValue)
	{
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, backValue);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		
		return c.getTime();
	}
	
	public static int getDiffInDays(long fromDate, long toDate)
	{
		long diffInMillies = Math.abs(fromDate - toDate) + 1;
	    return (int)TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}
	
	public static int getDayOfMonth(long dateTime)
	{
		Date date = new Date(dateTime);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}
	
	public static long getNthDayFromDate(long dateTime, int n)
	{
		Date date = new Date(dateTime);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, n);
		
		return c.getTime().getTime();
	}
	public static long getNthDayFirstHourFromDate(long dateTime, int n)
	{
		Date date = new Date(dateTime);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, n);
		setStartOfTheDate(c);
		
		return c.getTime().getTime();
	}
	public static long getNthDayLastHourFromDate(long dateTime, int n)
	{
		Date date = new Date(dateTime);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, n);
		setEndOfTheDate(c);
		
		return c.getTime().getTime();
	}

	public static long getFirstHourFromDate(long dateTime)
	{
		Date date = new Date(dateTime);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		setStartOfTheDate(c);

		return c.getTime().getTime();
	}
	public static long getLastHourFromDate(long dateTime)
	{
		Date date = new Date(dateTime);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		setEndOfTheDate(c);

		return c.getTime().getTime();
	}

	private static void  setStartOfTheDate(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}
	
	private static void setEndOfTheDate(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
	}
	public static List<DateRange> getMonthWiseDates(long fromDate, long toDate) {
		List <DateRange> list = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(fromDate);
		
		setStartOfTheDate(cal);
		return list;
	}

}
