package crm;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts.action.ActionForm;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.SearchFieldFromReferenceTable;
import annotation.TableName;
import common.ClientDTO;
import crm.inventory.CRMInventoryItem;
import report.CrmPriorityConverter;
import report.CrmStatusConverter;
import report.DateConvertor;
import report.Display;
import report.ReportCriteria;
import report.SubqueryBuilderForDate;

@TableName("at_crm_common_pool")
public class CrmCommonPoolDTO{
	
	public static int PORTAL_TYPE_EMAIL = 1;
	public static int PORTAL_TYPE_SMS = 2;
	public static int PORTAL_TYPE_WEB = 3;
	
	public static Map<Integer, String> mapOfPortalIDToPortalName = new HashMap<Integer,String>(){{
		put(null,"All");
		put(PORTAL_TYPE_EMAIL, "<label class='label label-warning'>Email</label>");
		put(PORTAL_TYPE_SMS,"<label class='label label-info'>Sms</label>");
		put(PORTAL_TYPE_WEB, "<label class='label label-success'>Web Portal</label>");
	}};

	public static String getComplainStatusString(int status) {
		switch (status){
			case CrmComplainDTO.COMPLETED:
				return "Completed";
			case CrmComplainDTO.ON_GOING:
				return "On Going";
			case CrmComplainDTO.STARTED:
				return "Started";
			case CrmComplainDTO.REJECTED_BY_ADMIN:
				return "Rejected By Admin";
			case CrmComplainDTO.CANCELLED_BY_CLIENT:
				return "Cancelled By Client";
		}
		return "Unknown Type";
	}
	@PrimaryKey
	@ColumnName("id")
	long ID;
	@ColumnName("portalType")
	int portalType;
	@ColumnName("clientID")
	@SearchFieldFromReferenceTable(entityClass=ClientDTO.class,entityColumnName="loginName", operator = "like")
	Long clientID;
	@ColumnName("clientComplain")
	String clientComplain;
	@ColumnName("clientMobile")
	String clientMobile;
	@ColumnName("clientEmail")
	String clientEmail;
	@ColumnName("nocEmployeeID")
	Long nocEmployeeID;
	@ColumnName("creatorEmployeeID")
	Long creatorEmployeeID;
	@ColumnName("feedbackOfNoc")
	String feedbackOfNoc;
	@Display(DateConvertor.class)
	@ReportCriteria(value = SubqueryBuilderForDate.class, moduleID = 0)
	@CurrentTime
	@ColumnName("submissionTime")
	long submissionTime;
	@Display(CrmStatusConverter.class)
	@ColumnName("status")
	int status;
	@ColumnName("isDeleted")
	boolean isDeleted;
	@CurrentTime
	@ColumnName("lastModificationTime")
	long lastModificationTime;
	@Display(CrmPriorityConverter.class)
	@ColumnName("priority")
	int priority;
	@ColumnName("entityTypeID")
	Integer entityTypeID;
	@ColumnName("entityID")
	Long entityID;
	@ColumnName("entityName")
	String name;
	@ColumnName("subject")
	String subject;
	@ColumnName("frequency")
	Integer frequency;
	@ColumnName("timeOfIncident")
	Long timeOfIncident;
	@ColumnName("crmCmnPlIsBlocked")
	boolean isBlocked;
	
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public int getPortalType() {
		return portalType;
	}
	public void setPortalType(int portalType) {
		this.portalType = portalType;
	}
	public Long getClientID() {
		return clientID;
	}
	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}
	public String getClientComplain() {
		return clientComplain;
	}
	public void setClientComplain(String clientComplain) {
		this.clientComplain = clientComplain;
	}
	public String getClientMobile() {
		return clientMobile;
	}
	public void setClientMobile(String clientMobile) {
		this.clientMobile = clientMobile;
	}
	public String getClientEmail() {
		return clientEmail;
	}
	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}
	public Long getNocEmployeeID() {
		return nocEmployeeID;
	}
	public void setNocEmployeeID(Long nocEmployeeID) {
		this.nocEmployeeID = nocEmployeeID;
	}
	public Long getCreatorEmployeeID() {
		return creatorEmployeeID;
	}
	public void setCreatorEmployeeID(Long creatorEmployeeID) {
		this.creatorEmployeeID = creatorEmployeeID;
	}
	public String getFeedbackOfNoc() {
		return feedbackOfNoc;
	}
	public void setFeedbackOfNoc(String feedbackOfNoc) {
		this.feedbackOfNoc = feedbackOfNoc;
	}
	public long getSubmissionTime() {
		return submissionTime;
	}
	public void setSubmissionTime(long submissionTime) {
		this.submissionTime = submissionTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Integer getEntityTypeID() {
		return entityTypeID;
	}
	public void setEntityTypeID(Integer entityTypeID) {
		this.entityTypeID = entityTypeID;
	}
	public Long getEntityID() {
		return entityID;
	}
	public void setEntityID(Long entityID) {
		this.entityID = entityID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Integer getFrequency() {
		return frequency;
	}
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	public Long getTimeOfIncident() {
		return timeOfIncident;
	}
	public void setTimeOfIncident(Long timeOfIncident) {
		this.timeOfIncident = timeOfIncident;
	}
	
	public String sanitizeClientComplainWithEscape() {
		StringBuilder sb = new StringBuilder(this.clientComplain.length() * 2 + 1);
		
		for(int i=0;i<this.clientComplain.length();i++) {
			if(this.clientComplain.charAt(i) == '"' || this.clientComplain.charAt(i) == '\'') {
				sb.append("\\").append(this.clientComplain.charAt(i));
			}else {
				sb.append(this.clientComplain.charAt(i));
			}
		}
		return sb.toString();
	}
	@Override
	public String toString() {
		return "CrmCommonPoolDTO [ID=" + ID + ", portalType=" + portalType + ", clientID=" + clientID
				+ ", clientComplain=" + clientComplain + ", clientMobile=" + clientMobile + ", clientEmail="
				+ clientEmail + ", nocEmployeeID=" + nocEmployeeID + ", creatorEmployeeID=" + creatorEmployeeID
				+ ", feedbackOfNoc=" + feedbackOfNoc + ", submissionTime=" + submissionTime + ", status=" + status
				+ ", isDeleted=" + isDeleted + ", lastModificationTime=" + lastModificationTime + ", priority="
				+ priority + ", entityTypeID=" + entityTypeID + ", entityID=" + entityID + ", name=" + name
				+ ", subject=" + subject + ", frequency=" + frequency + ", timeOfIncident=" + timeOfIncident
				+ ", isBLocked=" + isBlocked + "]";
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
		CrmCommonPoolDTO other = (CrmCommonPoolDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	public boolean isBlocked() {
		return isBlocked;
	}
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	

}
