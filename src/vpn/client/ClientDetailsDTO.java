package vpn.client;

import annotation.*;
import common.ClientDTO;
import common.ModuleConstants;
import common.StringUtils;
import report.*;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@TableName("at_client_details")
@ForeignKeyName("vclClientID")
@SearchFieldFromReferenceTable(entityClass = ClientDTO.class, operator = "like", entityColumnName = "loginName")

public class ClientDetailsDTO extends ClientDTO implements Serializable, Comparable<ClientDetailsDTO> {



    private static final long serialVersionUID = 1L;
    @PrimaryKey
    @ColumnName("vclID")
    long id;

    @Display(RegistantConverter.class)
    @ColumnName("vclRegType")
    int registrantType;
    @Display(ClientCategoryTypeConverter.class)
    @ColumnName("vclClientType") // will be used for individual or company
            int clientCategoryType;
    @ColumnName("vclModuleID")
    int moduleID;
    @ColumnName("vclRegistrationCategory")
    long regCategory;
    @CurrentTime
    @ColumnName("vclLastModificationTime")
    long lastModificationTime;
    @ColumnName("vclIsDeleted")
    boolean isDeleted;
    @ColumnName("vclActivationDate")
    long activationDate = System.currentTimeMillis();

    @ReportCriteria(value = SubqueryBuilderForStatus.class, moduleID = ModuleConstants.Module_ID_CLIENT)
    @Display(StatusConverter.class)
    @ColumnName("vclCurrentStatus")
    @SearchFieldFromMethod(methodName = "getCurrentStatusListFromActivationStatus", objectClass = ClientDAO.class, parameterType = Hashtable.class)
    int currentStatus;
    @ColumnName("vclLatestStatus")
    int latestStatus;

    @ColumnName("vclIdentity")
    String identity;

    @ColumnName("btrcLicenseDate")
    String btrcLicenseDate;

    @ColumnName("registrationSubCategory")
    long regSubCategory;

    public long getRegCategory() {
        return regCategory;
    }

    public void setRegCategory(long regCategory) {
        this.regCategory = regCategory;
    }

    public long getRegSubCategory() {
        return regSubCategory;
    }

    public void setRegSubCategory(long regSubCategory) {
        this.regSubCategory = regSubCategory;
    }

    public String getBtrcLicenseDate() {
        return btrcLicenseDate;
    }

    public void setBtrcLicenseDate(String btrcLicenseDate) {
        this.btrcLicenseDate = btrcLicenseDate;
    }


    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

//	String regiCategories[];

    long existingClientID; // Added by Dhrubo to register client being a client

    public List<ClientContactDetailsDTO> vpnContactDetails = new ArrayList<ClientContactDetailsDTO>();

    public long getExistingClientID() {
        return existingClientID;
    }

    public void setExistingClientID(long existingClientID) {
        this.existingClientID = existingClientID;
    }

    public int getModuleID() {
        return moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public long getClientID() {
        return clientID;
    }

    public int getClientCategoryType() {
        return clientCategoryType;
    }

    public void setClientCategoryType(int clientCategoryType) {
        this.clientCategoryType = clientCategoryType;
    }

    public void setClientID(long clientID) {
        this.clientID = clientID;
    }


    public long getRegistrantCategory() {
        return this.regCategory;
    }

    public void setRegistrantCategory(long regCategory) {
        this.regCategory = regCategory;
    }

    public int getRegistrantType() {
        return registrantType;
    }

    public void setRegistrantType(int registrantType) {
        this.registrantType = registrantType;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<ClientContactDetailsDTO> getVpnContactDetails() {
        return vpnContactDetails;
    }

    public void setVpnContactDetails(List<ClientContactDetailsDTO> vpnContactDetails) {
        this.vpnContactDetails = vpnContactDetails;
    }

    public long getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(long activationDate) {
        this.activationDate = activationDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (int) (clientID ^ (clientID >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ClientDetailsDTO other = (ClientDetailsDTO) obj;
        if (clientID != other.clientID)
            return false;
        return true;
    }

    @Override
    public boolean isActivated() {
        return activationDate != 0 && super.isActivated();

    }

    public int getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
    }

    public int getLatestStatus() {
        return latestStatus;
    }

    public void setLatestStatus(int latestStatus) {
        this.latestStatus = latestStatus;
    }

    @Override
    public String toString() {
        return "ClientDetailsDTO [id=" + id + ", registrantType=" + registrantType + ", clientCategoryType="
                + clientCategoryType + ", moduleID=" + moduleID + ", regCategory=" + regCategory
                + ", lastModificationTime=" + lastModificationTime + ", isDeleted=" + isDeleted + ", activationDate="
                + activationDate + ", currentStatus=" + currentStatus + ", latestStatus=" + latestStatus
                + ", vpnContactDetails=" + vpnContactDetails + "]";
    }

    @Override
    public int compareTo(ClientDetailsDTO o1) {
        return o1 == null ? -1 : this.getLoginName().compareToIgnoreCase(StringUtils.trim(o1.getLoginName()));
    }
}
