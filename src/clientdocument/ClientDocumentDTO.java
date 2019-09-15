package clientdocument;

/**
 * @author Touhid
 *
 */
import java.sql.Blob;
import java.util.Arrays;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_client_doc")
public class ClientDocumentDTO {
	@PrimaryKey
	@ColumnName("id")
	long ID;
	@ColumnName("clientID")
	long clientID;
	@ColumnName("doc")
	Blob document;
	@ColumnName("description")
	String description;
	@ColumnName("isCommentDoc")
	boolean isCommentDocument;
	@ColumnName("isDeleted")
	boolean isDeleted;
	@ColumnName("lastModificationTime")
	long lastModificationTime;

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public long getClientID() {
		return clientID;
	}

	public void setClientId(long clientId) {
		this.clientID = clientId;
	}

	public Blob getDocument() {
		return document;
	}

	public void setDocument(Blob document) {
		this.document = document;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCommentDocument() {
		return isCommentDocument;
	}

	public void setCommentDocument(boolean isCommentDocument) {
		this.isCommentDocument = isCommentDocument;
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
		return "ClientDocumentDTO [ID=" + ID + ", clientID=" + clientID + ", document=" + document + ", description="
				+ description + ", isCommentDocument=" + isCommentDocument + ", isDeleted=" + isDeleted
				+ ", lastModificationTime=" + lastModificationTime + "]";
	}

}
