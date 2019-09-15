package util;

import common.RequestFailureException;

public class CurrentTimeFactory {
	private static ThreadLocal<Long> currentTime = new ThreadLocal<Long>();
	
	public static void initializeCurrentTimeFactory(){
		currentTime.set(System.currentTimeMillis());
	}
	public static void destroyCurrentTimeFactory(){
		currentTime.remove();
	}
    
    public static long getCurrentTime(){
    	Long currentTimeInMills = currentTime.get();
    	if(currentTimeInMills == null){
//    		throw new RequestFailureException("No current time found in factory");
			initializeCurrentTimeFactory();
			currentTimeInMills = currentTime.get();
    	}
    	destroyCurrentTimeFactory();
    	return currentTimeInMills;
    }
}
