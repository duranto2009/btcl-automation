/**
 * 
 */
package client.module;

import annotation.*;

/**
 * @author Touhid
 *
 */
@TableName("at_client_module")
public class ClientModuleDTO {
	
	@PrimaryKey
	@ColumnName("id")   
	long id;
	
	@Unique
	@ColumnName("moduleId")
	long moduleId;
	
	@ColumnName("name")
	String name;
	
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
	
	public long getModuleId() {
		return moduleId;
	}

	public void setModuleId(long moduleId) {
		this.moduleId = moduleId;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return "ClientModuleDTO [id=" + id + ", moduleId=" + moduleId + ", name=" + name + ", isDeleted=" + isDeleted
				+ ", lastModificationTime=" + lastModificationTime + "]";
	}

}
