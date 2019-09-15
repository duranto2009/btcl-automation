package scheduler.scheduler;

import common.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import util.CurrentTimeFactory;
import util.ServiceDAOFactory;

import java.lang.reflect.Method;

public class ScheduledTask implements Runnable{

	
	SchedulerConfiguration schedulerConfiguration;
	
	public ScheduledTask(SchedulerConfiguration schedulerConfiguration){
		this.schedulerConfiguration = schedulerConfiguration;
	}
	
	@Override
	final public void run() {
		
		CurrentTimeFactory.initializeCurrentTimeFactory();
		
		String returnObjectString = "";
		try{
			schedulerConfiguration.setNextRunningTime(schedulerConfiguration.calculateNextRunningTime());
			schedulerConfiguration.setSummary("Running");
			schedulerConfiguration.setSummaryUpdateTime(CurrentTimeFactory.getCurrentTime());
			schedulerConfiguration.setLastRunningTime(CurrentTimeFactory.getCurrentTime());
			new SchedulerConfigurationService().updateScheduler(schedulerConfiguration);
			
			
			
			Class<?> clazz = Class.forName(schedulerConfiguration.getClassName());
			Object serviceObject = ServiceDAOFactory.getService(clazz);
			Method method = clazz.getMethod(schedulerConfiguration.getMethodName());
			Object returnObject = method.invoke(serviceObject);
			returnObjectString = (returnObject==null?"Success":returnObject.toString());
		}catch(Throwable th){
			Logger.getLogger("Scheduler : "+schedulerConfiguration.getClassName()).debug("fatal",th);
			returnObjectString = ExceptionUtils.getStackTrace(th);
		}finally{
			schedulerConfiguration.setSummary(StringUtils.isBlank(returnObjectString)?"Succesful":returnObjectString);
			schedulerConfiguration.setSummaryUpdateTime(System.currentTimeMillis());
			new SchedulerConfigurationService().updateScheduler(schedulerConfiguration);
			CurrentTimeFactory.destroyCurrentTimeFactory();
		}
	}
	
}
