package common;

public interface EntityDTO {
	boolean isActivated();
	String getName();
	int getCurrentStatus();
	void setCurrentStatus(int currentStatus);
	int getLatestStatus();
	void setLatestStatus(int latestStatus); 
	long getLastModificationTime();
	void setLastModificationTime(long lastModificationTime);
	boolean isDeleted();
	void setDeleted(boolean isDeleted);
	long getClientID();
	long getEntityID();
	double getBalance();
	void setBalance(double balance);
}
