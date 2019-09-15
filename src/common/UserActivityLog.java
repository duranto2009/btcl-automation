package common;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_user_activity_log")
public class UserActivityLog {
	@PrimaryKey
	@ColumnName("usrActLgID")
	long ID;
	@ColumnName("usrActLgUserID")
	long userID;
	@ColumnName("usrActLgIpAddress")
	String IPAddress;
	@ColumnName("usrActLgURI")
	String URI;
	@ColumnName("usrActLgRequestMethod")
	String requestMethod;
	@ColumnName("usrActLgTime")
	long time;
	@ColumnName("usrActLgHeaders")
	String headers;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getUserID() {
		return userID;
	}
	public void setUserID(long userID) {
		this.userID = userID;
	}
	public String getIPAddress() {
		return IPAddress;
	}
	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}
	public String getURI() {
		return URI;
	}
	public void setURI(String uRI) {
		URI = uRI;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	public String getHeaders() {
		return headers;
	}
	public void setHeaders(String headers) {
		this.headers = headers;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
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
		UserActivityLog other = (UserActivityLog) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "UserActivityLog [ID=" + ID + ", userID=" + userID + ", IPAddress=" + IPAddress + ", URI=" + URI
				+ ", requestMethod=" + requestMethod + ", time=" + time + ", headers=" + headers + "]";
	}

	
}
