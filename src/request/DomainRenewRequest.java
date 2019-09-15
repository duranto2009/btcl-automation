package request;

import annotation.*;


@TableName("at_domain_req")
@ForeignKeyName("domrnwrqReqID")
public class DomainRenewRequest extends CommonRequestDTO{
	 
	
	@PrimaryKey
	@ColumnName("domrnwrqID")
	long ID;
	@ColumnName("domrnwrqRenewYear")
	int year;
	@ColumnName("domrnwrqRequestType")
	int buyType;
	@ColumnName("domrnwrqPackageID")
	long packageID;
	@ColumnName("domrnwrqDomainID")
	long domainID;
	
	@ColumnName("domrnwrqLastModificationTime")
	long lastModificationTime;
	
	public int getBuyType() {
		return buyType;
	}
	public void setBuyType(int buyType) {
		this.buyType = buyType;
	}
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public long getPackageID() {
		return packageID;
	}
	public void setPackageID(long packageID) {
		this.packageID = packageID;
	}
	public long getLastModificationTime() {
		return lastModificationTime;
	}
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	public long getDomainID() {
		return domainID;
	}
	public void setDomainID(long domainID) {
		this.domainID = domainID;
	}
	@Override
	public String toString() {
		return "DomainRenewRequest [ID=" + ID + ", year=" + year + ", buyType=" + buyType + ", packageID=" + packageID
				+ ", domainID=" + domainID + ", lastModificationTime=" + lastModificationTime + "]";
	}

	
}
