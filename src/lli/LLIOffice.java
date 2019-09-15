package lli;

import java.util.*;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
@TableName("at_lli_office")
public class LLIOffice {
	@PrimaryKey
	@ColumnName("historyID")
	long historyID;
	@ColumnName("ID")
	long ID;
	@ColumnName("connectionHistoryID")
	long connectionHistoryID;
	@ColumnName("name")
	String name;
	@ColumnName("address")
	String address;
	List<LLILocalLoop> localLoops;
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
	public long getConnectionHistoryID() {
		return connectionHistoryID;
	}
	public void setConnectionHistoryID(long connectionHistoryID) {
		this.connectionHistoryID = connectionHistoryID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<LLILocalLoop> getLocalLoops() {
		return localLoops;
	}
	public void setLocalLoops(List<LLILocalLoop> localLoops) {
		this.localLoops = localLoops;
	}
	@Override
	public String toString() {
		return "LLIOffice [historyID=" + historyID + ", ID=" + ID + ", connectionHistoryID=" + connectionHistoryID
				+ ", name=" + name + ", localLoops=" + localLoops + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
		result = prime * result + ((localLoops == null) ? 0 : localLoops.hashCode());
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
		LLIOffice other = (LLIOffice) obj;
		if (ID != other.ID)
			return false;
		if (localLoops == null) {
			if (other.localLoops != null)
				return false;
		} else if (!new HashSet<>(this.localLoops).equals(new HashSet<>(other.getLocalLoops())))
			return false;
		return true;
	}
	
}
