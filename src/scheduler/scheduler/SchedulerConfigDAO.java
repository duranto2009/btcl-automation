package scheduler.scheduler;

import connection.DatabaseConnection;
import org.apache.log4j.Logger;
import util.SqlGenerator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.ModifiedSqlGenerator.getAllObjectList;
import static util.ModifiedSqlGenerator.getObjectByID;





class AsyschronousQueryTemplate implements Runnable{

	String query;
	
	boolean isRawQuery;
	Object object;
	
	public AsyschronousQueryTemplate(String sql) {
		this.query = sql;
		isRawQuery = true;
	}
	public AsyschronousQueryTemplate(Object object) {
		this.object = object;
		isRawQuery = false;
	}
	
	@Override
	public void run() {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			if(isRawQuery){
				databaseConnection.getNewStatement().execute(query);
			}else{
				SqlGenerator.updateEntity(object, object.getClass(), databaseConnection, false, false);
			}
		}catch(Throwable th){
			Logger.getLogger(getClass()).debug("fatal",th);
		}finally{
			databaseConnection.dbClose();
		}
	}
	
}


public class SchedulerConfigDAO {
	static private ExecutorService executorService = Executors.newSingleThreadExecutor();
	public List<SchedulerConfiguration> getSchedulerConfigs(long currentTime) throws Exception{
		return getAllObjectList(SchedulerConfiguration.class, " where nextRunningTime<= "+currentTime+" and allowExecution = 1");

	}
	public void updateSchedulerLastRunningTime(long lastRunningTime){
		String sql = "update at_scheduler_configuration set lastRunningTime = "+lastRunningTime+ " where  allowExecution = 1 and nextRunningTime <= "+lastRunningTime;
		executorService.execute(new AsyschronousQueryTemplate(sql));
	}
	
	public void update(SchedulerConfiguration schedulerConfiguration){
		executorService.execute(new AsyschronousQueryTemplate(schedulerConfiguration));
	}
	
	public List< SchedulerConfiguration> getAllSchedulers() throws Exception {
		Class < SchedulerConfiguration> classObject =  SchedulerConfiguration.class;
		return getAllObjectList(classObject, "");
	}
	public SchedulerConfiguration getSchedulerBySchedulerID(long iD) throws Exception {
		return getObjectByID( SchedulerConfiguration.class, iD);
	}
	public void updateSchedulerConfig(String schedulerName,long nextRunningTime){
		String sql = "update at_scheduler_configuration set nextRunningTime ="+nextRunningTime+",summary='Running',summaryUpdateTime = "+System.currentTimeMillis()+" where schedulerName = '"+schedulerName+"'";
		executorService.execute(new AsyschronousQueryTemplate(sql));
	}
	public void updateSchedulerConfig(String schedulerName,String summary){
		String sql = "update at_scheduler_configuration set summary='"+summary+"',summaryUpdateTime = "+System.currentTimeMillis()+" where schedulerName = '"+schedulerName+"'";
		executorService.execute(new AsyschronousQueryTemplate(sql));
	}

}
