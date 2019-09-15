package util;

import java.util.Arrays;
import java.util.Map;

/**
 * System.out.println()
 * 
 */
public class SOP {
	public static void print(String s) {
		System.out.println(s + "\n");
	}
	public static void print(Object obj) {
		obj.toString();
	}
	public static void printException(Object obj) {
		System.out.println(obj + "\n");
	}
	
	public static void printParameterMap(Map<String, String[]> parameterMap) {
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			System.out.println(entry.getKey() + "\t\t: " + Arrays.toString(entry.getValue()));
		}
	}
}