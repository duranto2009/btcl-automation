package request;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

import annotation.*;
import common.EntityTypeEntityDTO;
import common.ModuleConstants;
import report.CompletionStatusConverter;
import report.DateConvertor;
import report.Display;
import report.ReportCriteria;
import report.SubqueryBuilderForDate;

@TableName("at_req")
public class CommonRequestDTO extends ActionForm {

	/**
	 * 
	 */
	String receiverName;
	private static final long serialVersionUID = 1L;
	@ColumnName("arID")
	@PrimaryKey
	long reqID;
	@ColumnName(value="arRequestTypeID",editable=false)
	int requestTypeID;
	@ColumnName("arEntityTypeID")
	int entityTypeID;
	@ColumnName("arEntityID")
	long entityID;
	@ColumnName("arRootRequestID")
	Long rootReqID;
	@ColumnName("arParentRequestID")
	Long parentReqID;
	@ColumnName("arClientID")
	long clientID;
	@Display(DateConvertor.class)
	@ReportCriteria(value = SubqueryBuilderForDate.class, moduleID = 0)
	@ColumnName("arReqTime")
	long requestTime;
	@ColumnName("arRequestedByAccountID")
	long requestByAccountID;
	
	@ColumnName("arRequestedToAccountID")
	Long requestToAccountID;// NULL for all // ZERO > for client // ZERO < for admin 
	
	@ColumnName("arPriority")
	int priority;
	@ColumnName("arExpireTime")
	long expireTime;
	@ColumnName("arDescription")
	String description;
	@CurrentTime
	@ColumnName("arLastModificationTime")
	long lastModificationTime;
	@ColumnName("arIsDeleted")
	boolean isDeleted;
	@Display(CompletionStatusConverter.class)
	@ColumnName("arCompletionStatus")
	int completionStatus;
	@ColumnName("arSourceRequestID")
	String sourceRequestID = "0";
	
	@ColumnName("arIP")
	String IP;
	
	String actionName;
	
	int state;
	int moduleID;
	boolean visibleOnly = true;
	boolean rootActionsOnly = true;
	String clientName;
	String entityName;
	String expireTimeFormated;
	long billID;
	
	long rootEntityID;
	int rootEntityTypeID;
	
	ArrayList<EntityTypeEntityDTO> relatedEntityList;
	
	public CommonRequestDTO(){
		
	}
	public CommonRequestDTO(int entityType, long entityID, int actionTypeID, int stateID, long clientID){
		this.entityTypeID=entityType;
		this.entityID=entityID;
		this.requestTypeID=actionTypeID;
		this.state=stateID;
		this.clientID=clientID;
	}
	
	
	public long getBillID() {
		return billID;
	}
	public void setBillID(long billID) {
		this.billID = billID;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public int getModuleIDFromThisDTO(){
		 moduleID= Math.abs(requestTypeID)/ModuleConstants.MULTIPLIER;
		return moduleID;
	}
	public Long getParentReqID() {
		return parentReqID;
	}
	public void setParentReqID(Long parentReqID) {
		this.parentReqID = parentReqID;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Long getRootReqID() {
		return rootReqID;
	}
	public void setRootReqID(Long rootReqID) {
		this.rootReqID = rootReqID;
	}
	public Long getReqID() {
		return reqID;
	}
	public void setReqID(long requestID) {
		this.reqID = requestID;
	}
	public long getClientID() {
		return clientID;
	}
	public void setClientID(long clientID) {
		this.clientID = clientID;
	}
	public long getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
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
	public int getRequestTypeID() {
		return requestTypeID;
	}
	
	public void setRequestTypeID(int requestTypeID) {
		this.requestTypeID = requestTypeID;
	}
	
	public long getRequestByAccountID() {
		return requestByAccountID;
	}
	public void setRequestByAccountID(long requestByAccountID) {
		this.requestByAccountID = requestByAccountID;
	}
	public Long getRequestToAccountID() {
		return requestToAccountID;
	}
	public void setRequestToAccountID(Long requestToAccountID) {
		this.requestToAccountID = requestToAccountID;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	public boolean isPending(){
		if(1==1){
			throw new RuntimeException("CommonRequestDTO.isPending() not implemented yet");
		}
		return false;
	}
	
	public int getModuleID() {
		return moduleID;
	}
	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}
	

	public boolean isVisibleOnly() {
		return visibleOnly;
	}
	public void setVisibleOnly(boolean visibleOnly) {
		this.visibleOnly = visibleOnly;
	}
	
	

	public int getCompletionStatus() {
		return completionStatus;
	}
	public void setCompletionStatus(int completionStatus) {
		this.completionStatus = completionStatus;
	}
	public boolean isRootActionsOnly() {
		return rootActionsOnly;
	}
	public void setRootActionsOnly(boolean rootActionsOnly) {
		this.rootActionsOnly = rootActionsOnly;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	public ArrayList<EntityTypeEntityDTO> getRelatedEntityList() {
		return relatedEntityList;
	}
	public void setRelatedEntityList(ArrayList<EntityTypeEntityDTO> relatedEntityList) {
		this.relatedEntityList = relatedEntityList;
	}
	
	
	public String getSourceRequestID() {
		return sourceRequestID;
	}
	public void setSourceRequestID(String sourceRequestID) {
		this.sourceRequestID = sourceRequestID;
	}

	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	

	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getExpireTimeFormated() {
		return expireTimeFormated;
	}
	public void setExpireTimeFormated(String expireTimeFormated) {
		this.expireTimeFormated = expireTimeFormated;
	}


	
	public long getRootEntityID() {
		return rootEntityID;
	}
	public void setRootEntityID(long rootEntityID) {
		this.rootEntityID = rootEntityID;
	}

	public int getRootEntityTypeID() {
		return rootEntityTypeID;
	}
	public void setRootEntityTypeID(int rootEntityTypeID) {
		this.rootEntityTypeID = rootEntityTypeID;
	}
	@Override
	public String toString() {
		return "CommonRequestDTO [receiverName=" + receiverName + ", reqID=" + reqID + ", requestTypeID="
				+ requestTypeID + ", entityTypeID=" + entityTypeID + ", entityID=" + entityID + ", rootReqID="
				+ rootReqID + ", parentReqID=" + parentReqID + ", clientID=" + clientID + ", requestTime=" + requestTime
				+ ", requestByAccountID=" + requestByAccountID + ", requestToAccountID=" + requestToAccountID
				+ ", priority=" + priority + ", expireTime=" + expireTime + ", description=" + description
				+ ", lastModificationTime=" + lastModificationTime + ", isDeleted=" + isDeleted + ", completionStatus="
				+ completionStatus + ", sourceRequestID=" + sourceRequestID + ", IP=" + IP + ", actionName="
				+ actionName + ", state=" + state + ", moduleID=" + moduleID + ", visibleOnly=" + visibleOnly
				+ ", rootActionsOnly=" + rootActionsOnly + ", clientName=" + clientName + ", entityName=" + entityName
				+ ", expireTimeFormated=" + expireTimeFormated + ", billID=" + billID + ", rootEntityID=" + rootEntityID
				+ ", relatedEntityList=" + relatedEntityList + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (reqID ^ (reqID >>> 32));
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
		CommonRequestDTO other = (CommonRequestDTO) obj;
		if (reqID != other.reqID)
			return false;
		return true;
	}
	
}
