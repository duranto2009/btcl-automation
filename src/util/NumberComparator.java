package util;

public class NumberComparator {
	static private double h = .000000000001d;
	public static boolean isEqual(Number a,Number b){
		return Math.abs(a.doubleValue()-b.doubleValue())<h;
	}
	public static boolean isGreaterThanOrEqual(Number a, Number b){
		return a.doubleValue()>(b.doubleValue()-h);
	}
	public static boolean isBetween(Number a,Number b,Number c){
		return isGreaterThanOrEqual(b, a) && isGreaterThanOrEqual(c, b);
	}
}
