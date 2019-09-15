package client.classification;

/**
 * @author Touhid
 *
 */

import annotation.*;

@TableName("at_client_reg_category")
public class RegistrantCategoryDTO {

	@PrimaryKey
	@ColumnName("id")
	long Id;

	@ColumnName("regCatId")
	long registrantCategoryId;

	@ColumnName("name")
	String name;

	@ColumnName("discount")
	double discount;

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

	public long getRegistrantCategoryId() {
		return registrantCategoryId;
	}

	public void setRegistrantCategoryId(long registrantCategoryId) {
		this.registrantCategoryId = registrantCategoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
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
		return "RegistrantCategoryDTO [Id=" + Id + ", registrantCategoryId=" + registrantCategoryId + ", name=" + name
				+ ", isDeleted=" + isDeleted + ", lastModificationTime=" + lastModificationTime + "]";
	}

}
