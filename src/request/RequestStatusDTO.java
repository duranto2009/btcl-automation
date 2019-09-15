package request;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
@TableName("at_req_status")
public class RequestStatusDTO {

	@PrimaryKey
	@ColumnName("rsID")
	long rsID;
	@ColumnName("rsReqID")
	long reqID;	
	@ColumnName("rsStatus")
	int reqStatus;
	long clientID;
	int entityTypeID;
	
	public long getReqID() {
		return reqID;
	}
	public void setReqID(long reqID) {
		this.reqID = reqID;
	}
	public int getReqStatus() {
		return reqStatus;
	}
	public void setReqStatus(int reqStatus) {
		this.reqStatus = reqStatus;
	}
	public long getRsID() {
		return rsID;
	}
	public void setRsID(long rsID) {
		this.rsID = rsID;
	}
	
	
}
