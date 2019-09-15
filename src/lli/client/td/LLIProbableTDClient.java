package lli.client.td;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_lli_probable_td_client")
public class LLIProbableTDClient {
	
	public LLIProbableTDClient() {
	}
	public LLIProbableTDClient(long clientID, long tdDate) {
		this.clientID = clientID;
		this.tdDate = tdDate;
	}

	
	
	@PrimaryKey
	@ColumnName("id")
	long ID;
	@ColumnName("clientID")
	long clientID;
	@ColumnName("tdDate")
	long tdDate;

	
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
	public long getTdDate() {
		return tdDate;
	}
	public void setTdDate(long tdDate) {
		this.tdDate = tdDate;
	}
	
}
