/**
 * 
 */
package client.classification;

import annotation.*;

/**
 * @author Touhid
 *
 */
@TableName("at_client_type_map")
public class ClientTypeMapDTO {
	
	@PrimaryKey
	@ColumnName("id")
	long id;
	
	@ColumnName("moduleID")
	int moduleId;
	
	@ColumnName("regType")
	int registrantType;
	
	@ColumnName("regCategory")
	int registrantCategory;
	
	@ColumnName("tariffCategory")
	int tariffCategory;
	
	@ColumnName("mandatoryDocuments")
	String mandatoryDocuments;
	
	@ColumnName("optionalDocuments")
	String optionalDocuments;
	
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

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public int getRegistrantType() {
		return registrantType;
	}

	public void setRegistrantType(int registrantType) {
		this.registrantType = registrantType;
	}

	public int getRegistrantCategory() {
		return registrantCategory;
	}

	public void setRegistrantCategory(int registrantCategory) {
		this.registrantCategory = registrantCategory;
	}

	public int getTariffCategory() {
		return tariffCategory;
	}

	public void setTariffCategory(int tariffCategory) {
		this.tariffCategory = tariffCategory;
	}

	public String getMandatoryDocuments() {
		return mandatoryDocuments;
	}

	public void setMandatoryDocuments(String mandatoryDocuments) {
		this.mandatoryDocuments = mandatoryDocuments;
	}

	public String getOptionalDocuments() {
		return optionalDocuments;
	}

	public void setOptionalDocuments(String optionalDocuments) {
		this.optionalDocuments = optionalDocuments;
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
		return "ClientTypeMapDTO [id=" + id + ", moduleId=" + moduleId + ", registrantType=" + registrantType
				+ ", registrantCategory=" + registrantCategory + ", tariffCategory=" + tariffCategory
				+ ", mandatoryDocuments=" + mandatoryDocuments + ", optionalDocuments=" + optionalDocuments
				+ ", isDeleted=" + isDeleted + ", lastModificationTime=" + lastModificationTime + "]";
	}
	
	

}
