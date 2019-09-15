package common;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import file.FileDTO;
import file.FileTypeConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.upload.FormFile;

import java.io.Serializable;
import java.util.Arrays;

@TableName("at_client")
public class ClientDTO implements Serializable,EntityDTO {
	@PrimaryKey
	@ColumnName("clID")
	public long clientID;
	@ColumnName("clLoginName")
	public String loginName;

	@CurrentTime
	@ColumnName("clLastModificationTime")
	public long lastModificationTime;
	@ColumnName("clIsDeleted")
	public boolean isDeleted;
	@ColumnName("clBalance")
	public double balance;
	@ColumnName("clLoginPassword")
	public String loginPassword;
	@ColumnName("clActivationDate")
	long activationDate = System.currentTimeMillis();
	@ColumnName("clProfilePicturePath") //added by sharif
	String profilePicturePath;
	@ColumnName("clCurrentStatus")
	int currentStatus; // 0=booked,1=running,2=parking,3=closed
	@ColumnName("clLatestStatus")
	int latestStatus;
	@ColumnName("isCorporate")
	boolean isCorporate;

	public boolean isCorporate() {
		return isCorporate;
	}

	public void setCorporate(boolean corporate) {
		isCorporate = corporate;
	}

	public FileDTO fileDTO;


	public FileDTO getFileDTO() {
		return fileDTO;
	}
	public void setFileDTO(FileDTO fileDTO) {
		this.fileDTO = fileDTO;
	}
	public FormFile document;
	private String[] documents;

	public FormFile getDocument() {
		return document;
	}
	public void setDocument(FormFile document) {
		this.document = document;
	}
	public String[] getDocuments() {
		return documents;
	}

	public void setDocuments(String[] documents) {
		this.documents = documents;
	}

	public long getClientID() {
		return clientID;
	}
	public void setClientID(long clientID) {
		this.clientID = clientID;
	}
	public long getLastModificationTime() {
		return lastModificationTime;
	}
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public long getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(long activationDate) {
		this.activationDate = activationDate;
	}
	public String getProfilePicturePath() {
		if(StringUtils.isEmpty(profilePicturePath)){
			profilePicturePath=FileTypeConstants.DEFAULT_PROFILE_PIC;
		}
		return profilePicturePath;
	}
	public void setProfilePicturePath(String profilePicturePath) {
		this.profilePicturePath = profilePicturePath;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (clientID ^ (clientID >>> 32));
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
		ClientDTO other = (ClientDTO) obj;
		if (clientID != other.clientID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClientDTO [clientID=" + clientID + ", loginName=" + loginName + ", lastModificationTime="
				+ lastModificationTime + ", isDeleted=" + isDeleted + ", balance=" + balance + ", loginPassword="
				+ loginPassword + ", activationDate=" + activationDate + ", profilePicturePath=" + profilePicturePath
				+ ", currentStatus=" + currentStatus + ", latestStatus=" + latestStatus + ", fileDTO=" + fileDTO
				+ ", document=" + document + ", documents=" + Arrays.toString(documents) + "]";
	}

	@Override
	public boolean isActivated() {
		return activationDate!=0;
	}
	@Override
	public String getName() {
		return loginName;
	}
	@Override
	public int getCurrentStatus() {

		return this.currentStatus;
	}
	@Override
	public int getLatestStatus() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void setCurrentStatus(int currentStatus) {
		// TODO Auto-generated method stub
		this.currentStatus = currentStatus;
	}
	@Override
	public void setLatestStatus(int latestStatus) {
		// TODO Auto-generated method stub

	}
	/* (non-Javadoc)
	 * @see common.EntityDTO#getEntityID()
	 */
	@Override
	public long getEntityID() {
		return clientID;
	}
}
