package common;

public class ClientStatusDTO {
	int applicationStatus = ClientStatusConstants.APPLICATION_STATUS_NOT_APPLIED;
	int status = ClientStatusConstants.STATUS_NOT_REGISTERED;
	private long moduleClientID;
	
	public int getApplicationStatus() {
		return applicationStatus;
	}
	public void setApplicationStatus(int applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getModuleClientID() {
		return moduleClientID;
	}
	public void setModuleClientID(long moduleClientID) {
		this.moduleClientID = moduleClientID;
	}
	@Override
	public String toString() {
		return "ClientStatusDTO [applicationStatus=" + applicationStatus + ", status=" + status + ", moduleClientID="
				+ moduleClientID + "]";
	}
	
}
