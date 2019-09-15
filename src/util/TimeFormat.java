package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeFormat {

    public TimeFormat() {
    }
    
    public static Long MILLIS_IN_A_DAY = 86400000L;

    public static String formatDuration(long timeInSecond) {
        String duration = null;
        long minute = timeInSecond / 60L;
        long second = timeInSecond % 60L;
        String min = Long.toString(minute);
        String sec = Long.toString(second);
        if (second < 10L) {
            sec = '0' + sec;
        }
        duration = min + ":" + sec;
        return duration;
    }

    public static long getDateFromSystemTime() {
    	DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    	return TimeFormat.getSpecificDateInMilis(df.format(System.currentTimeMillis()));
    			
    }
    
    public static String getBillFileDate(){       
        String date="";
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();        
        calendar.setTimeInMillis(System.currentTimeMillis());
        date = formatter.format(calendar.getTime());        
        return date;
    }
    
    public static String getBillFileDate(long time){       
        String date="";
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();        
        calendar.setTimeInMillis(System.currentTimeMillis());
        date = formatter.format(time);        
        return date;
    }

    public static String getCurrentMonthStart() {
        long time = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        month++;
        String date = "01-" + ((month + "").length() == 1 ? ("0" + month) : month) + "-" + year;

        return date;
    }

    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    public static int lastDayOfMonth(int month, int year) {
        int endOfMonth = -1;
        // alert("M="+month);

        switch (month) {
            case 0://January
            case 2://March
            case 4://May
            case 6://July
            case 7://August
            case 9://October
            case 11://December
                endOfMonth = 31;
                break;

            case 1://February
                endOfMonth = isLeapYear(year) ? 29 : 28;
                break;

            case 3://April
            case 5://June
            case 8://September
            case 10://November
                endOfMonth = 30;
                break;
        }
        return endOfMonth;
    }

    public static String getCurrentMonthFinish() {
        long time = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        int day = lastDayOfMonth(month, year);

        month++;
        String date = ""+day+"-" + ((month + "").length() == 1 ? ("0" + month) : month) + "-" + year;

        return date;
    }

    public static String getValidityDate(int day) {
        long time = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        String date;
        month += 2;
        if (month <= 12) {
            date = ((day + "").length() == 1 ? ("0" + day) : day) + "-" + ((month + "").length() == 1 ? ("0" + month) : month) + "-" + year;
        } else {
            year++;
            date = ((day + "").length() == 1 ? ("0" + day) : day) + "-01-" + year;
        }

        return date;
    }
    
    public static String getLastDateofPayment(int day)
    {
    	long time = TimeFormat.getStartTimeOfDayInMilis(System.currentTimeMillis());
    	time += day*TimeFormat.MILLIS_IN_A_DAY;
    	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    	return format.format(new Date(time));
    }

    public static long getCurrentMonthStartInMilis() {
        long time = 0;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        try {
            time = df.parse(getCurrentMonthStart()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static long getSpecificDateInMilis(String date) {
        long time = 0;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        try {
            time = df.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }
    
    public static String getSpecificDate(long time) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(time);
    }
    
    
    public static long getStartTimeOfDayInMilis(long time_in_milis) {
        long time=0;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time_in_milis);
        String date = formatter.format(calendar.getTime());
        
        try {
            time = formatter.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }
    
    public static long getLastTimeOfMonthInMilis(long time_in_milisec) {
        long lastDayMili = 0;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time_in_milisec);
        String date = formatter.format(calendar.getTime());
        String parse[] = date.split("-");
        ////System.out.println("Day:"+parse[0]+":month:"+parse[1]+":year:"+parse[2]);
        int days_in_month = getDaysInMonth(parse[2], parse[1]);
        ////System.out.println("Days in month:"+days_in_month);
        date = Integer.toString(days_in_month) + "-" + parse[1] + "-" + parse[2];
        ////System.out.println("Modified Date:"+date);
        lastDayMili = getStartTimeOfDayInMilis(date);
        return lastDayMili + MILLIS_IN_A_DAY - 1000;
    }
    public static int getDaysInMonth(String year, String month) {
        
        GregorianCalendar calendar = new GregorianCalendar();
        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);
        monthInt = monthInt - 1;
        calendar.set(yearInt, monthInt, 1);
        int dayInt = calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return dayInt;
    }
    
    public static long getStartTimeOfDayInMilis(String date) {
        long time = 0;
        DateFormat df = new SimpleDateFormat("d-M-yyyy");
        try {
            time = df.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }
    
    public static long getStartTimeOfDayInMilisFromBillFileDate(String date){
        long time = 0;
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            time = df.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
        
    }
    
    public static long getEndTimeOfDayInMilis(long time_in_milis) {
        long time=0;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time_in_milis);
        String date = formatter.format(calendar.getTime());
        
        try {
            time = formatter.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return time+UtilityConstants.MILI_SECONDS_IN_A_DAY-1000;
    }
    
    

    

    public static String getCurrentDateWithFormat(long time, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(time);
    }
    public static String getCurrentDateWithFormat( String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(System.currentTimeMillis());
    }
}