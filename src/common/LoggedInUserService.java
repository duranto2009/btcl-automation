package common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import annotation.Transactional;
import util.CurrentTimeFactory;
import util.ModifiedSqlGenerator;

public class LoggedInUserService extends Thread{
	Logger logger = Logger.getLogger(getClass());
	volatile private boolean isRunning = true;
	static private BlockingQueue<UserActivityLog> queue = new LinkedBlockingQueue<>();
	
	public LoggedInUserService(){
		start();
	}
	public void insert(UserActivityLog userActivityLog){
		userActivityLog.setTime(CurrentTimeFactory.getCurrentTime());
		queue.add(userActivityLog);
	}
	@Override
	public void run() {
		
		while(isRunning){
			try {
				UserActivityLog userActivityLog = queue.take();
				insertIntoDB(userActivityLog);
			} catch (Exception e) {
				logger.debug("fatal");
			}
		}
		
	}
	@Transactional
	public void insertIntoDB(UserActivityLog userActivityLog) throws Exception{
		CurrentTimeFactory.initializeCurrentTimeFactory();
		ModifiedSqlGenerator.insert(userActivityLog);
		CurrentTimeFactory.destroyCurrentTimeFactory();
	}

	public void kill( ) {
		this.isRunning = false;
	}
}
