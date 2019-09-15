package complain;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_complaint_subject")
public class ComplainSubjectDTO extends ActionForm implements Serializable{
	
	@PrimaryKey
	@ColumnName("csID")
	int csID;
	@ColumnName("csName")
	String csName;
	@ColumnName("csCreatedBy")
	long csCreatedBy;
	
	public int getCsID() {
		return csID;
	}
	public void setCsID(int csID) {
		this.csID = csID;
	}
	public String getCsName() {
		return csName;
	}
	public void setCsName(String csNameString) {
		this.csName = csNameString;
	}
	public long getCsCreatedBy() {
		return csCreatedBy;
	}
	public void setCsCreatedBy(long csCreatedBy) {
		this.csCreatedBy = csCreatedBy;
	}
	@Override
	public String toString() {
		return "ComplainSubjectDTO [csID=" + csID + ", csName=" + csName + ", csCreatedBy=" + csCreatedBy + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (csID ^ (csID >>> 32));
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
		ComplainSubjectDTO other = (ComplainSubjectDTO) obj;
		if (csID != other.csID)
			return false;
		return true;
	}

}
