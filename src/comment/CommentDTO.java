package comment;

import java.util.Arrays;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

import file.FileDTO;
import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;


@TableName("at_comment")
public class CommentDTO extends ActionForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@ColumnName("cID")
	long ID;
	@ColumnName("cTime")
	long lastModificationTime;
	@ColumnName("cHeading")
	String heading;
	@ColumnName("cDescription")
	String description;
	@ColumnName("cEntityTypeID")
	int entityTypeID;
	@ColumnName("cEntityID")
	long entityID;
	@ColumnName("cMemberID")
	long memberID;
	
	FormFile document;
	String[] documents;
	
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
	public long getLastModificationTime() {
		return lastModificationTime;
	}
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	public String getHeading() {
		return heading;
	}
	public void setHeading(String heading) {
		this.heading = heading;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getEntityTypeID() {
		return entityTypeID;
	}
	public void setEntityTypeID(int entityTypeID) {
		this.entityTypeID = entityTypeID;
	}
	public long getEntityID() {
		return entityID;
	}
	public void setEntityID(long entityID) {
		this.entityID = entityID;
	}
	public long getMemberID() {
		return memberID;
	}
	public void setMemberID(long memberID) {
		this.memberID = memberID;
	}
	
	public String[] getDocuments() {
		return documents;
	}
	public void setDocuments(String[] documents) {
		this.documents = documents;
	}
	
	@Override
	public String toString() {
		return "CommentDTO [ID=" + ID + ", lastModificationTime=" + lastModificationTime + ", heading=" + heading
				+ ", description=" + description + ", entityTypeID=" + entityTypeID + ", entityID=" + entityID
				+ ", memberID=" + memberID + ", document=" + document + ", documents=" + Arrays.toString(documents)
				+ "]";
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
		CommentDTO other = (CommentDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
}
