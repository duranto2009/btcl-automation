package scheduler.scheduler;

import annotation.Transactional;
import common.RequestFailureException;

import java.util.List;



public class SchedulerConfigurationService {
	SchedulerConfigDAO schedulerConfigDAO = new SchedulerConfigDAO();
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<SchedulerConfiguration> getAllSchedulers() throws Exception {
		return schedulerConfigDAO.getAllSchedulers();
		 
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	public SchedulerConfiguration getSchedulerBySchedulerID(long iD) throws Exception {
		SchedulerConfiguration schedulerConfiguration = schedulerConfigDAO.getSchedulerBySchedulerID(iD);
		if(schedulerConfiguration == null) {
			throw new RequestFailureException("No such scheduler found");
		}
		return schedulerConfiguration;
	}

	public void updateScheduler(SchedulerConfiguration schedulerConfiguration){
		schedulerConfigDAO.update(schedulerConfiguration);
	}

	public void updateSchedulerEntry(String schedulerName,String schedulerSummary){
		schedulerConfigDAO.updateSchedulerConfig(schedulerName,  schedulerSummary);
	}

	public void updateSchedulerEntry(String schedulerName,long nextRunningTime){
		schedulerConfigDAO.updateSchedulerConfig(schedulerName, nextRunningTime);
	}
	

}
