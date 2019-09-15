package scheduler.scheduler;

public abstract class SchedulerService {
	public abstract long getNextRunningTime();
	private String getSchedulerName(){
		
		Class<? extends SchedulerService> clazz = getClass();
		Scheduler scheduler = clazz.getAnnotation(Scheduler.class);
		if(scheduler == null){
			throw new RuntimeException("No @Scheduler annotation found");
		}
		
		return scheduler.value();
	}
	final public void execute(){
		
	}
}
