package lli.client.td;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_lli_client_td")
public class LLIClientTD {
	
	public LLIClientTD() {
	}
	public LLIClientTD(long clientID, long tdFrom, long tdTo) {
		this.clientID = clientID;
		this.tdFrom = tdFrom;
		this.tdTo = tdTo;
	}
	
	@PrimaryKey
	@ColumnName("id")
	long ID;
	@ColumnName("clientID")
	long clientID;
	@ColumnName("tdFrom")
	long tdFrom;
	@ColumnName("tdTo")
	long tdTo;

	@ColumnName("advicedDate")
	long advicedDate;
	@ColumnName("appliedDate")
	long appliedDate;
	@ColumnName("state")
	long state;
	
	/**/
	
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getClientID() {
		return clientID;
	}
	public void setClientID(long clientID) {
		this.clientID = clientID;
	}
	public long getTd() {
		return tdFrom;
	}
	public void setTdFrom(long tdFrom) {
		this.tdFrom = tdFrom;
	}
	public long getTdTo() {
		return tdTo;
	}
	public void setTdTo(long tdTo) {
		this.tdTo = tdTo;
	}
}
