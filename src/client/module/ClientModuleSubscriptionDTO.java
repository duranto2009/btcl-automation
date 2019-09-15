package client.module;

import annotation.*;

/**
 * @author Touhid
 *
 */

@TableName("at_client_subscription")
public class ClientModuleSubscriptionDTO {

	@PrimaryKey
	@ColumnName("id")
	long id;

	@ColumnName("clientID")
	long clientId;

	@ColumnName("moduleID")
	long moduleId;

	@ColumnName("isDeleted")
	boolean isDeleted;

	@ColumnName("lastModificationTime")
	long lastModificationTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public long getModuleId() {
		return moduleId;
	}

	public void setModuleId(long moduleId) {
		this.moduleId = moduleId;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public long getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	@Override
	public String toString() {
		return "ClientModuleSubscriptionDTO [id=" + id + ", clientId=" + clientId + ", moduleId=" + moduleId
				+ ", isDeleted=" + isDeleted + ", lastModificationTime=" + lastModificationTime + "]";
	}
	

}
