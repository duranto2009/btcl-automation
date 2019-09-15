package scheduler.scheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

import annotation.ColumnName;
import annotation.ParseDateToMillisecond;
import annotation.PrimaryKey;
import annotation.TableName;
import org.apache.log4j.Logger;

@TableName("at_scheduler_configuration")
public class SchedulerConfiguration {
	public final static int INTERVAL_TYPE_MONTHLY = 1;
	public final static int INTERVAL_TYPE_WEEKLY = 2;
	
	
	@PrimaryKey
	@ColumnName(value = "ID",editable=false)
	long ID;
	@ColumnName(value = "schedulerName",editable=false)
	String schedulerName;
	@ParseDateToMillisecond(dateFormat="dd MMMM yyyy - hh:mm a")
	@ColumnName("lastRunningTime")
	long lastRunningTime;
	@ParseDateToMillisecond(dateFormat="dd MMMM yyyy - hh:mm a")
	@ColumnName("nextRunningTime")
	long nextRunningTime;
	@ColumnName("ClassName")
	String className;
	@ColumnName("methodName")
	String methodName;

	@ColumnName("mails")
	String mails;
	@ColumnName("result")
	String result;
	@ColumnName("allowExecution")
	boolean allowExecution;
	@ColumnName("summary")
	String summary;
	
	@ColumnName(value = "summaryUpdateTime")
	long summaryUpdateTime;
	@ColumnName("intervalType")
	int intervalType;
	@ColumnName("intervalOrder")
	String intervalOrder;
	@ColumnName("schedulingHour")
	int schedulingHour;
	@ColumnName("schedulingMinute")
	int schedulingMinute;
	
	public long getSummaryUpdateTime() {
		return summaryUpdateTime;
	}
	public void setSummaryUpdateTime(long summaryUpdateTime) {
		this.summaryUpdateTime = summaryUpdateTime;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public String getSchedulerName() {
		return schedulerName;
	}
	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}
	public long getLastRunningTime() {
		return lastRunningTime;
	}
	public void setLastRunningTime(long lastRunningTime) {
		this.lastRunningTime = lastRunningTime;
	}
	public long getNextRunningTime() {
		return nextRunningTime;
	}
	public void setNextRunningTime(long nextRunningTime) {
		this.nextRunningTime = nextRunningTime;
	}
	public String getMails() {
		return mails;
	}
	public void setMails(String mails) {
		this.mails = mails;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public boolean isAllowExecution() {
		return allowExecution;
	}
	public void setAllowExecution(boolean allowExecution) {
		this.allowExecution = allowExecution;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public int getIntervalType() {
		return intervalType;
	}
	public void setIntervalType(int intervalType) {
		this.intervalType = intervalType;
	}
	public String getIntervalOrder() {
		return intervalOrder;
	}
	public void setIntervalOrder(String intervalOrder) {
		this.intervalOrder = intervalOrder;
	}
	public int getSchedulingHour() {
		return schedulingHour;
	}
	public void setSchedulingHour(int schedulingHour) {
		this.schedulingHour = schedulingHour;
	}
	public int getSchedulingMinute() {
		return schedulingMinute;
	}
	public void setSchedulingMinute(int schedulingMinute) {
		this.schedulingMinute = schedulingMinute;
	}
	@Override
	public String toString() {
		return "SchedulerConfiguration [ID=" + ID + ", schedulerName=" + schedulerName + ", lastRunningTime="
				+ lastRunningTime + ", nextRunningTime=" + nextRunningTime + ", className=" + className
				+ ", methodName=" + methodName + ", mails=" + mails + ", result=" + result + ", allowExecution="
				+ allowExecution + ", summary=" + summary + ", summaryUpdateTime=" + summaryUpdateTime
				+ ", intervalType=" + intervalType + ", intervalOrder=" + intervalOrder + ", schedulingHour="
				+ schedulingHour + ", schedulingMinute=" + schedulingMinute + "]";
	}
	
	public List<Integer> getIntervalOrders(){

		List<Integer> intervalList = new ArrayList<>();
		String[] intervals = intervalOrder.split(",",-1);
		for(String interval: intervals){
			if(interval.matches("[0-9]+")){
				intervalList.add(Integer.parseInt(interval));
			}
		}
		
		return intervalList;
	}
	public long calculateNextRunningTime(){
		long nextRunningTime = Long.MAX_VALUE;
		try{
			Calendar calendar = Calendar.getInstance();
			
			if(intervalType == INTERVAL_TYPE_MONTHLY){
				
				int currentDate = calendar.get(Calendar.DAY_OF_MONTH);
				
				TreeSet<Integer> set = new TreeSet<>();
				
				for(int allowedDate:getIntervalOrders()){
					if(allowedDate>0 && allowedDate<32){
						set.add(allowedDate);
					}
				}
				
				if(set.isEmpty()){
					return Long.MAX_VALUE;
				}else if(set.higher(currentDate)!=null){
					int nextDate = set.higher(currentDate);
					calendar.set(Calendar.DAY_OF_MONTH,nextDate);
				}else{
					calendar.add(Calendar.MONTH, 1);
					int nextDateOfNextMonth = set.first();//(set.lower(currentDate)!=null?set.lower(currentDate):currentDate);
					calendar.set(Calendar.DAY_OF_MONTH, nextDateOfNextMonth);
				}
				
				
			}else{
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
				
				TreeSet<Integer> set = new TreeSet<>();
				
				for(int allowedDate:getIntervalOrders()){
					if(allowedDate>0 && allowedDate<8){
						set.add(allowedDate);
					}
				}
				
				if(set.isEmpty()){
					return Long.MAX_VALUE;
				}else if(set.higher(dayOfWeek)!=null){
					calendar.set(Calendar.DAY_OF_WEEK, set.higher(dayOfWeek));
				}else{
					calendar.add(Calendar.DATE, 7);
					int nextDayOfNextWeek =   set.first();  //(set.lower(dayOfWeek)==null?dayOfWeek:set.lower(dayOfWeek));
					calendar.set(Calendar.DAY_OF_WEEK, nextDayOfNextWeek);
				}
				
				
			}
			calendar.set(Calendar.HOUR_OF_DAY, getSchedulingHour());
			calendar.set(Calendar.MINUTE,getSchedulingMinute());
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND, 0);
			nextRunningTime = calendar.getTimeInMillis();
		}catch(Throwable th){
			Logger.getLogger(getClass()).debug("fatal",th);;
		}finally{
			
		}
		return nextRunningTime;
	}
}
