package complain;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_complaint_history")
public class ComplainHistoryDTO extends ActionForm implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@ColumnName("chID")
	long ID;
	@ColumnName("chComplainID")
	long complainID;
	@ColumnName("chAccountID")
	long accountID;
	@ColumnName("chStatus")
	int status;
	@ColumnName("chMessage")
	String message;
	@ColumnName("chNote")
	String note;
	@ColumnName("chTime")
	long lastModificationTime;
	
	FormFile document;

	public FormFile getDocument() {
		return document;
	}
	public void setDocument(FormFile document) {
		this.document = document;
	}
	
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getComplainID() {
		return complainID;
	}
	public void setComplainID(long complainID) {
		this.complainID = complainID;
	}
	public long getAccountID() {
		return accountID;
	}
	public void setAccountID(long accountID) {
		this.accountID = accountID;
	}
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public long getLastModificationTime() {
		return lastModificationTime;
	}
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	@Override
	public String toString() {
		return "ComplainHistoryDTO [ID=" + ID + ", complainID=" + complainID + ", accountID=" + accountID + ", status="
				+ status + ", message=" + message + ", note=" + note + ", lastModificationTime=" + lastModificationTime
				+ ", document=" + document + "]";
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
		ComplainHistoryDTO other = (ComplainHistoryDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	
}
