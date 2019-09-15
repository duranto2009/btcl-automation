package websecuritylog;

import org.apache.struts.action.ActionForm;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_web_security")
public class WebSecurityLogDTO extends ActionForm {
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@ColumnName("awslID")
	long ID;
	@ColumnName("awslRemoteUrl")
	String url;
	@ColumnName("awslRemotePort")
	String port;
	@ColumnName("awslUsername")
	String username;
	@ColumnName("awslPassword")
	String password;
	@ColumnName("awslAttemptType")
	int attemptType;
	@ColumnName("awslDescription")
	String description;
	@ColumnName("awslActionTaken")
	int actionTaken;
	@ColumnName("awslLastModificationTime")
	long lastModificationTime;
	@ColumnName("awslAttemptTime")
	long attemptTime;
	@ColumnName("awslIsDeleted")
	boolean isDeleted;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getAttemptType() {
		return attemptType;
	}
	public void setAttemptType(int attempType) {
		this.attemptType = attempType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getActionTaken() {
		return actionTaken;
	}
	public void setActionTaken(int actionTaken) {
		this.actionTaken = actionTaken;
	}
	public long getLastModificationTime() {
		return lastModificationTime;
	}
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	public long getAttemptTime() {
		return attemptTime;
	}
	public void setAttemptTime(long attempTime) {
		this.attemptTime = attempTime;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	@Override
	public String toString() {
		return "WebSecurityLogDTO [ID=" + ID + ", url=" + url + ", port=" + port + ", username=" + username
				+ ", attempType=" + attemptType + ", description=" + description + ", actionTaken=" + actionTaken
				+ ", lastModificationTime=" + lastModificationTime + ", attempTime=" + attemptTime + ", isDeleted="
				+ isDeleted + "]";
	}
	
	

}