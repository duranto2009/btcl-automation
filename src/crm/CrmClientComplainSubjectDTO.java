package crm;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_crm_client_complaint_subject")
public class CrmClientComplainSubjectDTO {
	@PrimaryKey
	@ColumnName("id")
	long ID;
	@ColumnName("entityTypeID")
	int entityTypeID;
	@ColumnName("subject")
	String subject;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public int getEntityTypeID() {
		return entityTypeID;
	}
	public void setEntityTypeID(int entityTypeID) {
		this.entityTypeID = entityTypeID;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	@Override
	public String toString() {
		return "CrmClientComplainSubjectDTO [ID=" + ID + ", entityTypeID=" + entityTypeID + ", subject=" + subject
				+ "]";
	}
}
