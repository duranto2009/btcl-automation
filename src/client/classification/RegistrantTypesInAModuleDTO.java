/**
 * 
 */
package client.classification;

import annotation.*;

/**
 * @author Touhid
 *
 */
@TableName("at_reg_type_vs_module")
public class RegistrantTypesInAModuleDTO {

	@PrimaryKey
	@ColumnName("id")
	long id;

	@ColumnName("moduleId")
	long moduleId;

	@ColumnName("registrantTypeId")
	long registrantTypeId;

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

	public long getRegistrantTypeId() {
		return registrantTypeId;
	}

	public void setRegistrantTypeId(long registrantTypeId) {
		this.registrantTypeId = registrantTypeId;
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
		return "RegistrantTypesInAModuleDTO [id=" + id + ", moduleId=" + moduleId + ", registrantTypeId="
				+ registrantTypeId + ", isDeleted=" + isDeleted + ", lastModificationTime=" + lastModificationTime + "]";
	}

}
