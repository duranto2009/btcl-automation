package request;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import permission.ActionStateDTO;

@TableName("at_state")
public class StateDTO
{
	
	@PrimaryKey
	@ColumnName("stID")
	int id;
	@ColumnName("stName")
	String name;
	@ColumnName("stRootRequestTypeID")
	int rootRequestTypeIDSameBranch;
	int rootRequestTypeIDMain;
	@ColumnName("stDurationInMillis")
	long durationInMillis;
	@ColumnName("stActivationStatus")
	int activationStatus;
	@ColumnName("stIsVisibleToSystem")
	boolean isVisibleToSystem;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRootRequestTypeID() {
		return getRootRequestTypeID(false);
	}
	public int getRootRequestTypeID(boolean sameBranch) {
		if(rootRequestTypeIDSameBranch == -1)
		{
			return rootRequestTypeIDSameBranch;
		}
		if(!sameBranch)
		{
			ActionStateDTO asdto = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(rootRequestTypeIDSameBranch);
			if(asdto == null)
				return -1;
			if(asdto.getRootActionTypeID() == rootRequestTypeIDSameBranch)
				return rootRequestTypeIDSameBranch;
			
			rootRequestTypeIDMain = rootRequestTypeIDSameBranch;
			while(asdto.getActionTypeID() != asdto.getRootActionTypeID())
			{
				asdto = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(rootRequestTypeIDMain);
				if(asdto == null)
				{
					return -1;
				}
				rootRequestTypeIDMain = asdto.getRootActionTypeID();
			}
			return rootRequestTypeIDMain;
		}
		return rootRequestTypeIDSameBranch;
	}
	public void setRootRequestTypeID(int rootRequestTypeIDSameBranch) {
		this.rootRequestTypeIDSameBranch = rootRequestTypeIDSameBranch;
	}
	public void setRootRequestTypeID(int rootRequestTypeID, boolean sameBranch) {
		if(sameBranch)
		{
			this.rootRequestTypeIDSameBranch = rootRequestTypeID;
		}
		else
		{
			this.rootRequestTypeIDMain = rootRequestTypeID;
		}
	}
	
	public long getDurationInMillis() {
		return durationInMillis;
	}
	public void setDurationInMillis(long durationInMillis) {
		this.durationInMillis = durationInMillis;
	}
	
	public int getActivationStatus() {
		return activationStatus;
	}
	public void setActivationStatus(int activationStatus) {
		this.activationStatus = activationStatus;
	}
	
	
	public boolean isVisibleToSystem() {
		return isVisibleToSystem;
	}
	public void setVisibleToSystem(boolean isVisibleToSystem) {
		this.isVisibleToSystem = isVisibleToSystem;
	}
	@Override
	public String toString() {
		return "StateDTO [id=" + id + ", name=" + name + ", rootRequestTypeIDSameBranch=" + rootRequestTypeIDSameBranch
				+ ", rootRequestTypeIDMain=" + rootRequestTypeIDMain + ", durationInMillis=" + durationInMillis
				+ ", activationStatus=" + activationStatus + ", isVisibleToSystem=" + isVisibleToSystem + "]";
	}

	

	
	
}
