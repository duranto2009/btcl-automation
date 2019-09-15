package util;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

public class MultipleTypeUtils {
	private  static final int MASK=(byte)0x01;
	private  static final int LONG_SIZE=8;
	private static final int MAXIMUM_TYPE=4;
	
	
	private MultipleTypeUtils(){
		
	}
	
	public static Set<Integer> getArrayToUniqueSet(String[] arrayOfString)
	{
		Set<Integer> uniqueSet = new HashSet<Integer>();
		for(int i = 0; i < arrayOfString.length; i++ ){
			uniqueSet.add(Integer.parseInt(arrayOfString[i]));
		}
		return uniqueSet;
	}
	
	public static String[] getUniqueSetToArray(Set<Integer> uniqueSet)
	{
		String[] arrayOfString = new String[uniqueSet.size()];
		int i = 0;
		for(int e : uniqueSet){
			arrayOfString[i++] = e+"";
		}
		return arrayOfString;
		
	}
	
	public static long getSelectedTypeAsLong(Set<Integer> selectedSet)//only the selected will come here
	{
		long selectedLong = 0;
		for(Integer selected: selectedSet)
		{
			selectedLong = selectedLong | (1L << selected);
		}		
		return selectedLong;
	}
	
	public static Set getSelectedTypeAsSet(long selectedLong)
	{
		//System.out.println("selectedLong " + selectedLong);
		byte[] result = longToBytes(selectedLong);
		Set<Integer> set = new HashSet<Integer>();
		for(int p = 0; p < result.length; p++)
		{
			//System.out.println("result[p] " + result[p]);
			byte mask = MASK;
			for (int j = 0; j < LONG_SIZE; j++)
			{
				int value = fromByteToInt((byte)(result[p] & mask));	
				if(value > 0)
				{
					//System.out.println("value " + value );
					set.add((j + 1) + ((LONG_SIZE - p - 1) *LONG_SIZE) - 1);
					//System.out.println("set " + set);
				}
				mask <<= 1;
			}
		}
		return set;
	}
	
	public static byte[] longToBytes(long l) {
	    byte[] result = new byte[LONG_SIZE];
	    for (int i = (LONG_SIZE-1); i >= 0; i--) {
	        result[i] = (byte)(l & 0xFF);
	        //System.out.println(result[i]+" "+Long.toBinaryString(result[i]));
	        l >>= LONG_SIZE;
	    }
	    return result;
	}
	
	public static long bytesToLong(byte[] b) {
	    long result = 0;
	    for (int i = 0; i < LONG_SIZE; i++) {
	        result <<= LONG_SIZE;
	        result |= (b[i] & 0xFF);
	    }
	    return result;
	}
	
	public static int fromByteToInt(byte byteData) {
		ByteBuffer dbuf = ByteBuffer.allocate(4);
		byte[] bytes = dbuf.array();
		bytes[3]=byteData;
		return ByteBuffer.wrap(bytes).getInt();
	}
	
	public static void main(String args[])
	{
		Set<Integer> set = new HashSet<Integer>();
		set.add(0);
		set.add(1);
		set.add(2);
		set.add(3);
		set.add(25);
		
		long selectedLong = getSelectedTypeAsLong(set);
		System.out.println("selectedLong " + selectedLong);
		
		Set<Integer> set2 = getSelectedTypeAsSet(selectedLong);
		System.out.println(set2);
		
	}
}
