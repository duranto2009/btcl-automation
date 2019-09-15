package permission;

import org.apache.struts.action.ActionForm;

import annotation.ColumnName;
import annotation.TableName;

@TableName("at_action_state")
public class ActionStateDTO extends ActionForm
{
	@ColumnName("asActionTypeID")
	int actionTypeID;
	@ColumnName("asParentActionTypeID")
	Integer parentActionTypeID;
	@ColumnName("asRootActionTypeID")
	int rootActionTypeID;
	@ColumnName("asDescription")
	String description;
	@ColumnName("asType")
	String type;
	@ColumnName("asNextStateID")
	int nextStateID;
	@ColumnName("asIsSystemAction")
	boolean isSystemAction;
	@ColumnName("asIsClientAction")
	boolean isClientAction;
	@ColumnName("asIsVisibleToSystem")
	boolean isVisibleToSystem;
	@ColumnName("asIsVisibleToClient")
	boolean isVisibleToClient;
	@ColumnName("asEntityTypeID")
	int entityTypeID;
	@ColumnName("asIsCreationRequest")
	boolean isCreationRequest;
	@ColumnName("asIsColumnID")
	int columnID;
	@ColumnName("asRevertable")
	boolean isRevertable;
	@ColumnName("asNextPreferableActionTypeID")
	int nextPreferableActionTypeID;
	/*@ColumnName("asNotificationTemplate")
	String notificationTemplate;*/
	
	
	String className;
	
	public String getClassName(){
		return className;
	}
	 
	public Integer getParentActionTypeID() {
		return parentActionTypeID;
	}
	public void setParentActionTypeID(Integer parentActionTypeID) {
		this.parentActionTypeID = parentActionTypeID;
	}

	public boolean isCreationRequest() {
		return isCreationRequest;
	}
	public void setCreationRequest(boolean isCreationRequest) {
		this.isCreationRequest = isCreationRequest;
	}
	public int getNextStateID() {
		return nextStateID;
	}
	public void setNextStateID(int nextStateID) {
		this.nextStateID = nextStateID;
	}

	public boolean isSystemAction() {
		return isSystemAction;
	}
	public void setSystemAction(boolean isSystemAction) {
		this.isSystemAction = isSystemAction;
	}
	public int getActionTypeID() {
		return actionTypeID;
	}
	public void setActionTypeID(int actionTypeID) {
		this.actionTypeID = actionTypeID;
	}
	public boolean isVisibleToClient() {
		return isVisibleToClient;
	}
	public void setVisibleToClient(boolean isVisibleToClient) {
		this.isVisibleToClient = isVisibleToClient;
	}
	public boolean isRootAction() {
		return (actionTypeID == rootActionTypeID);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getEntityTypeID() {
		return entityTypeID;
	}
	public void setEntityTypeID(int entityTypeID) {
		this.entityTypeID = entityTypeID;
	}
	

	public int getRootActionTypeID() {
		return rootActionTypeID;
	}
	public void setRootActionTypeID(int rootActionTypeID) {
		this.rootActionTypeID = rootActionTypeID;
	}
	
	public boolean isClientAction() {
		return isClientAction;
	}
	public void setClientAction(boolean isClientAction) {
		this.isClientAction = isClientAction;
	}
	
	public boolean isVisibleToSystem() {
		return isVisibleToSystem;
	}
	public void setVisibleToSystem(boolean isVisibleToSystem) {
		this.isVisibleToSystem = isVisibleToSystem;
	}
	
	public int getColumnID() {
		return columnID;
	}
	public void setColumnID(int columnID) {
		this.columnID = columnID;
	}
	
	public boolean isRevertable() {
		return isRevertable;
	}
	public void setRevertable(boolean isRevertable) {
		this.isRevertable = isRevertable;
	}
	
	public int getNextPreferableActionTypeID() {
		return nextPreferableActionTypeID;
	}
	public void setNextPreferableActionTypeID(int nextPreferableActionTypeID) {
		this.nextPreferableActionTypeID = nextPreferableActionTypeID;
	}
	@Override
	public String toString() {
		return "ActionStateDTO [actionTypeID=" + actionTypeID + ", parentActionTypeID=" + parentActionTypeID
				+ ", rootActionTypeID=" + rootActionTypeID + ", description=" + description + ", type=" + type
				+ ", nextStateID=" + nextStateID + ", isSystemAction=" + isSystemAction + ", isClientAction="
				+ isClientAction + ", isVisibleToSystem=" + isVisibleToSystem + ", isVisibleToClient="
				+ isVisibleToClient + ", entityTypeID=" + entityTypeID + ", isCreationRequest=" + isCreationRequest
				+ ", columnID=" + columnID + ", isRevertable=" + isRevertable + "]";
	}


	
/*	public String getNotificationTemplate() {
		return notificationTemplate;
	}
	public void setNotificationTemplate(String notificationTemplate) {
		this.notificationTemplate = notificationTemplate;
	}*/
	

	
	
}
