package scheduler.scheduler;

import annotation.Transactional;
import common.Logger;
import common.UniversalDTOService;
import util.ServiceDAOFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommonSchedulerService {
	public static final long COMMON_SCHEDULER_SLEEP_TIME = 1000*60*2L;
	Logger logger = Logger.getLogger(CommonSchedulerService.class);
	SchedulerConfigDAO schedulerConfigDAO = new SchedulerConfigDAO();
	@Transactional(transactionType=util.TransactionType.READONLY)
	public void runPendingSchedulers() throws Exception{
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		long currentTime = System.currentTimeMillis();
		List<SchedulerConfiguration> schedulerConfigurations = schedulerConfigDAO.getSchedulerConfigs(currentTime);
		for(SchedulerConfiguration schedulerConfiguration: schedulerConfigurations){
			executorService.execute(new ScheduledTask(schedulerConfiguration));
		}
		schedulerConfigDAO.updateSchedulerLastRunningTime(currentTime);
	}
	

	public static void main(String[] args){
		UniversalDTOService universalDTOService = ServiceDAOFactory.getService(UniversalDTOService.class);
		org.apache.log4j.PropertyConfigurator.configure("log4j.properties");
		CommonSchedulerService commonSchedulerService = ServiceDAOFactory.getService(CommonSchedulerService.class);
		while(true){

			try{
				commonSchedulerService.runPendingSchedulers();
				CommonSchedulerProperty commonSchedulerProperty = new CommonSchedulerProperty();
				commonSchedulerProperty.lastRunningTime = System.currentTimeMillis();
				universalDTOService.update(commonSchedulerProperty);
				Thread.sleep(COMMON_SCHEDULER_SLEEP_TIME);
			}catch(Throwable th){
				Logger.getLogger(CommonSchedulerService.class).debug("fatal",th);
			}
		}
		
	}
}
