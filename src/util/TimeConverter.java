package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import common.StringUtils;

public class TimeConverter {	
	static Logger logger = Logger.getLogger(TimeConverter.class);
	
	public static final String dateFormatWithTimeString = "dd/MM/yyyy hh:mm a";
	
	public static final DateFormat  dfWithTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public static final DateFormat  df = new SimpleDateFormat("dd/MM/yyyy");

	public static final DateFormat meridiemSdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a"); //comment format
	
	public static final DateFormat monthSdf = new SimpleDateFormat("MMM"); //month format
	public static final DateFormat yearSdf = new SimpleDateFormat("yyyy"); //year format
	public static final DateFormat meridiandf = new SimpleDateFormat("dd/MM/yyyy hh:mm a"); //comment format

	public static final DateFormat dateWithTimeFormat = new SimpleDateFormat("dd MMM yyyy - HH:mm");
	
	public static long MILLS_IN_A_DAY =  86400000L;	
	public static final long MILLISECONDS_IN_YEAR = MILLS_IN_A_DAY * 365;
	public static long OffsetInMillis = OffsetDateTime.now().getOffset().getTotalSeconds() * 1000;
	
	
	//28 May 2017 - 05:25
	public static String getTimeStringByDateFormat(long timeInMillis, String dateFormat) throws ParseException{
		DateFormat df = new SimpleDateFormat(dateFormat);
		df.setLenient(false);
		
		Date applicationDate = new Date(timeInMillis);
		return df.format(applicationDate).toString();
	}
	public static long getTimeInMilliSec(String timeString,String dateFormat) throws ParseException  {
		timeString = StringUtils.trim(timeString);
		if(timeString.matches("[0-9]+")) {
			return Long.parseLong(timeString);
		}else {
			DateFormat df = new SimpleDateFormat(dateFormat);
			df.setLenient(false);
			return df.parse(timeString).getTime();
		}
	}
	
	public static long getDateWithTimeFromDateTimeString(String dateWithTime){
		Date activationDate = null;
		try {
			dateWithTimeFormat.setLenient(false);
			activationDate = dateWithTimeFormat.parse(dateWithTime);
		} catch (Exception e) {
			logger.debug(e);
		}
		return activationDate.getTime();
	}
	public static String getDateTimeStringByMillisecAndDateFormat(long millisec,String dateTimeFormat){
		Date date = new Date(millisec);
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
		dateFormat.setLenient(false);
		return dateFormat.format(date);
	}
	public static String getDateTimeStringFromDateTime(long dateWithTime){
		Date applicationDate = new Date(dateWithTime);
		dateWithTimeFormat.setLenient(false);
		return dateWithTimeFormat.format(applicationDate).toString();
	}
	
	public static String getTimeStringFromLong(long time) {
		Date applicationDate = new Date(time);
		df.setLenient(false);
		return df.format(applicationDate).toString();
	}
	
	public static String getTimeWithTimeStringFromLong(long time) {
		Date applicationDate = new Date(time);
		dfWithTime.setLenient(false);
		return dfWithTime.format(applicationDate).toString();
	}

	public static long getTimeFromString(String timeString) {
		Date activationDate = null;
		try {
			df.setLenient(false);
			activationDate = df.parse(timeString);
		} catch (Exception e) {
			logger.debug(e);
		}
		return activationDate.getTime();
	}
	public static long getStartTimeOfTheDay(long time){
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
	public static long getStartTimeOfTheNextNthDay(long time,int n){
		return getStartTimeOfTheDay(time)+n*MILLS_IN_A_DAY;
	}
	public static String getToDay(){
		Date applicationDate = new Date(System.currentTimeMillis());
		return df.format(applicationDate).toString();
	}
	
	public static String getMeridiemTime(long time){
		Date applicationDate = new Date(time);
		meridiandf.setLenient(false);
		return meridiandf.format(applicationDate).toString();
	}
	
	public static Long getDateFromString(String dateString){
		
		Long dateLong = null;
		
		try{
			dateLong = df.parse(dateString).getTime();
		}catch(Exception ex){
		//	ex.printStackTrace();
		}
		return dateLong;
	}
	
	public static Long getNextDateFromString(String dateString){
		Long date = getDateFromString(dateString);
		if(date!=null){
			date+=MILLS_IN_A_DAY;
		}
		return date;
	}
	
	
	public static String getMonth(long time){
		return monthSdf.format(time).toString();
	}
	
	public static String getYear(long time) {
		return yearSdf.format(time).toString();
	}

	public static long getLastDateBeforeNMonth(int numberOfMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(getStartTimeOfTheDay(System.currentTimeMillis()));
		calendar.add(Calendar.MONTH, numberOfMonth);
		return calendar.getTimeInMillis() - 1;
	}

    public static LocalDate getStandardNextYearDate(long serviceStartTime) {
		serviceStartTime = getStartTimeOfTheDay(serviceStartTime);
		LocalDate startDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(serviceStartTime), ZoneId.systemDefault()).toLocalDate();
		LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());
		LocalDate nextCycleDate = LocalDate.of(currentDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());



		if(nextCycleDate.isBefore(currentDate) || nextCycleDate.equals(currentDate)){
			nextCycleDate = nextCycleDate.plusYears(1);
		}
		nextCycleDate = nextCycleDate.minusMonths(1);
		logger.info("Server Current Date: " + currentDate);
		logger.info("Start Date: "+ startDate);
		logger.info("Next Bill Date: " + nextCycleDate);
		return nextCycleDate;
    }
}
