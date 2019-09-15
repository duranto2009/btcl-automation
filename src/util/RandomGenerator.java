package util;

import java.util.Random;

public class RandomGenerator {
	
	private RandomGenerator()
	{}
	
	public static RandomGenerator getInstance()
	{
		if(randomGenerator== null)
		{
			randomGenerator = new RandomGenerator();
		}
		
		return randomGenerator;
	}
	
	
	public String randomString(int length)
	{
	   String rand = "";
	   for(int i = 0; i<length;i++)
	   {
		   
		   String str = ""+Math.abs(new Random().nextLong());
		   rand = rand + str.charAt(new Random().nextInt(str.length()-1));
		   //System.out.println("str:"+str);
	   }
	   //System.out.println("rand:"+rand);
	   return rand;
	}
	
	public static RandomGenerator randomGenerator;

}
