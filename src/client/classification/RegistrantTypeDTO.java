/**
 * 
 */
package client.classification;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import annotation.Unique;

/**
 * @author Touhid
 *
 */
@TableName("at_client_reg_type")
public class RegistrantTypeDTO {

	@PrimaryKey
	@ColumnName("id")
	long Id;

	@Unique
	@ColumnName("regTypeId")
	long regTypeId;

	@ColumnName("name")
	String name;

	@ColumnName("isDeleted")
	boolean isDeleted;

	@ColumnName("lastModificationTime")
	long lastModificationTime;

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public long getRegTypeId() {
		return regTypeId;
	}

	public void setRegTypeId(long regTypeId) {
		this.regTypeId = regTypeId;
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
		return "RegistrantTypeDTO [Id=" + Id + ", regTypeId=" + regTypeId + ", name=" + name + ", isDeleted="
				+ isDeleted + ", lastModificationTime=" + lastModificationTime + "]";
	}

}
