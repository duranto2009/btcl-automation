/**
 * 
 */
package client.classification;

import annotation.*;

/**
 * @author Touhid
 *
 */
@TableName("at_req_doc_vs_category")
public class RequiredDocsInACategoryDTO {
	
	@PrimaryKey
	@ColumnName("id")
	long id;
	
	@ColumnName("regCategoryInATypeId")
	long registrantCategoryInATypeId;
	
	@ColumnName("documentId")
	long documentId;
	
	@ColumnName("isMandatory")
	boolean isMandatory;
	
	@ColumnName("isDeleted")
	boolean isDeleted;
	
	@ColumnName("lastModificationTime")
	long lastModificationTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRegistrantCategoryInATypeId() {
		return registrantCategoryInATypeId;
	}

	public void setRegistrantCategoryInATypeId(long registrantCategoryInATypeId) {
		this.registrantCategoryInATypeId = registrantCategoryInATypeId;
	}

	public long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}
	
	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public long getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	@Override
	public String toString() {
		return "RequiredDocsInACategoryDTO [id=" + id + ", registrantCategoryInATypeId=" + registrantCategoryInATypeId
				+ ", documentId=" + documentId + ", isMandatory=" + isMandatory + ", isDeleted=" + isDeleted
				+ ", lastModificationTime=" + lastModificationTime + "]";
	}

	
}
