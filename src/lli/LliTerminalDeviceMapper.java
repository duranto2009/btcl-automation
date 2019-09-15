package lli;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_terminal_device_mapper")
public class LliTerminalDeviceMapper {
	@PrimaryKey
	@ColumnName("trmnldevmapID")
	long ID;
	@ColumnName("trmnldevmapEndPointID")
	long endPointID;
	@ColumnName("trmnldevmapTerminalDeviceID")
	long terminalDeviceID;
	@ColumnName("trmnldevmapIsRented")
	boolean isRented;
	@ColumnName("trmnldevmapIsMaintainedByBTCL")
	boolean isMaintanedByBTCL; 
	@ColumnName("trmnldevmapIsDeleted")
	boolean isDeleted;
	@ColumnName("trmnldevmapLastModificationTime")
	long lastModificationTime;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getEndPointID() {
		return endPointID;
	}
	public void setEndPointID(long endPointID) {
		this.endPointID = endPointID;
	}
	public long getTerminalDeviceID() {
		return terminalDeviceID;
	}
	public void setTerminalDeviceID(long terminalDeviceID) {
		this.terminalDeviceID = terminalDeviceID;
	}
	public boolean isRented() {
		return isRented;
	}
	public void setRented(boolean isRented) {
		this.isRented = isRented;
	}
	public boolean isMaintanedByBTCL() {
		return isMaintanedByBTCL;
	}
	public void setMaintanedByBTCL(boolean isMaintanedByBTCL) {
		this.isMaintanedByBTCL = isMaintanedByBTCL;
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
	
}