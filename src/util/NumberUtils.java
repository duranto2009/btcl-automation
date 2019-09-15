package util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtils {
	
	private static DecimalFormat decimalFormat = new DecimalFormat("#.##");
	
	public static boolean isBetween(double a,double b,double c){
		return (Double.compare(a, b)<=0.0 && Double.compare(b, c)<=0.0);
	}
	public static double min(double a,double b){
		return a>b?b:a;
	}

	public static double formattedValue(double val)
	{
		return Double.valueOf(decimalFormat.format(val));
	}
	
	/*public static double roundedValue(double val)
	{
		BigDecimal bd = new BigDecimal(val).setScale(0, RoundingMode.HALF_DOWN);
		return bd.doubleValue();
	}*/
}
