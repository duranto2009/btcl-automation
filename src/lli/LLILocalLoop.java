package lli;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
@TableName("at_lli_local_loop")
public class LLILocalLoop {
	@PrimaryKey
	@ColumnName("historyID")
	long historyID;
	@ColumnName("ID")
	long ID;
	@ColumnName("lliOfficeHistoryID")
	long lliOfficeHistoryID;
	@ColumnName("clientDistance")
	int clientDistance;
	@ColumnName("btclDistance")
	int btclDistance;
	@ColumnName("OCDistance")
	int OCDistance;
	@ColumnName("OCID")
	Long OCID;

	@ColumnName("popID")
	long popID;
	@ColumnName("router_switchID")
	long router_switchID=0;
	@ColumnName("portID")
	long portID=0;
	@ColumnName("vlanID")
	Long vlanID;

	@ColumnName("numOfCore")
	int ofcType;


	@ColumnName("adjustedBTClDistance")
	long adjustedBTClDistance;

	@ColumnName("adjustedOCDistance")
	long adjustedOCDistance;

	@ColumnName("is_deleted")
	int is_deleted;

	public void setPopID(long popID) {
		this.popID = popID;
	}

	public void setRouter_switchID(long router_switchID) {
		this.router_switchID = router_switchID;
	}

	public void setPortID(long portID) {
		this.portID = portID;
	}

	public long getAdjustedBTClDistance() {
		return adjustedBTClDistance;
	}

	public void setAdjustedBTClDistance(long adjustedBTClDistance) {
		this.adjustedBTClDistance = adjustedBTClDistance;
	}

	public long getAdjustedOCDistance() {
		return adjustedOCDistance;
	}

	public void setAdjustedOCDistance(long adjustedOCDistance) {
		this.adjustedOCDistance = adjustedOCDistance;
	}

	public long getHistoryID() {
		return historyID;
	}
	public void setHistoryID(long historyID) {
		this.historyID = historyID;
	}
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getLliOfficeHistoryID() {
		return lliOfficeHistoryID;
	}
	public void setLliOfficeHistoryID(long lliOfficeHistoryID) {
		this.lliOfficeHistoryID = lliOfficeHistoryID;
	}
	public int getClientDistance() {
		return clientDistance;
	}
	public void setClientDistance(int clientDistance) {
		this.clientDistance = clientDistance;
	}
	public int getBtclDistance() {
		return btclDistance;
	}
	public void setBtclDistance(int btclDistance) {
		this.btclDistance = btclDistance;
	}
	public int getOCDistance() {
		return OCDistance;
	}
	public void setOCDistance(int oCDistance) {
		OCDistance = oCDistance;
	}
	public Long getOCID() {
		return OCID;
	}
	public void setOCID(Long oCID) {
		OCID = oCID;
	}

	public Long getPopID() {
		return popID;
	}
	public void setPopID(Long popID) {
		this.popID = popID;
	}
	public Long getRouter_switchID() {
		return router_switchID;
	}
	public void setRouter_switchID(Long router_switchID) {
		this.router_switchID = router_switchID;
	}
	public Long getPortID() {
		return portID;
	}
	public void setPortID(Long portID) {
		this.portID = portID;
	}
	public Long getVlanID() {
		return vlanID;
	}
	public void setVlanID(Long vlanID) {
		this.vlanID = vlanID;
	}

	public void setOfcType(int ofcType) {
		this.ofcType = ofcType;
	}
	public int getOfcType() {
		return this.ofcType;
	}

	public void setIs_deleted(int is_deleted) {
		this.is_deleted = is_deleted;
	}
	public int getIs_deleted() {
		return this.is_deleted;
	}
	
	@Override
	public String toString() {
		return "LLILocalLoop [historyID=" + historyID + ", ID=" + ID + ", lliOfficeHistoryID=" + lliOfficeHistoryID
				+ ", clientDistance=" + clientDistance + ", btclDistance=" + btclDistance + ", OCDistance=" + OCDistance
				+ ", OCID=" + OCID + ", popID=" + popID + ", ofcType=" + ofcType + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
		result = prime * result + OCDistance;
		result = prime * result + (int) (OCID ^ (OCID >>> 32));
		result = prime * result + btclDistance;
		result = prime * result + (int) (lliOfficeHistoryID ^ (lliOfficeHistoryID >>> 32));
		result = prime * result + ofcType;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LLILocalLoop other = (LLILocalLoop) obj;
		if (ID != other.ID)
			return false;
		if (OCDistance != other.OCDistance)
			return false;
		if (OCID != other.OCID)
			return false;
		if (btclDistance != other.btclDistance)
			return false;
		if (lliOfficeHistoryID != other.lliOfficeHistoryID)
			return false;
		if (ofcType != other.ofcType)
			return false;
		return true;
	}
	
	
	
}
