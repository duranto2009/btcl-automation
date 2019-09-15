package permission;

import java.util.HashSet;
import java.util.Set;

import request.CommonRequestDTO;
 
public class StateActionDTO
{
	int saID;
	int stateID;
	Set<Integer> ActionTypeIDs;
	String description;
	CommonRequestDTO commonRequestDTO;
	
	
	public int getSaID() {
		return saID;
	}
	public void setSaID(int saID) {
		this.saID = saID;
	}
	public int getStateID() {
		return stateID;
	}
	public void setStateID(int stateID) {
		this.stateID = stateID;
	}

	public Set<Integer> getActionTypeIDs() {
		return ActionTypeIDs;
	}
	public void setActionTypeIDs(Set<Integer> actionTypeIDs) {
		ActionTypeIDs = actionTypeIDs;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public CommonRequestDTO getCommonRequestDTO() {
		return commonRequestDTO;
	}
	public void setCommonRequestDTO(CommonRequestDTO commonRequestDTO) {
		this.commonRequestDTO = commonRequestDTO;
	}
	public StateActionDTO getCopy()
	{
		StateActionDTO sadto = new StateActionDTO();
		sadto.setSaID(saID);
		sadto.setStateID(stateID);
		sadto.setDescription(description);
		if(this.getActionTypeIDs() != null)
		{
			Set<Integer> actionTypeIDs = new HashSet<Integer>();
			actionTypeIDs.addAll(this.getActionTypeIDs());
			sadto.setActionTypeIDs(actionTypeIDs);
		}
		return sadto;
		
	}
	@Override
	public String toString() {
		return "StateActionDTO [saID=" + saID + ", stateID=" + stateID + ", ActionTypeIDs=" + ActionTypeIDs
				+ ", description=" + description + ", commonRequestDTO=" + commonRequestDTO + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + saID;
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
		StateActionDTO other = (StateActionDTO) obj;
		if (saID != other.saID)
			return false;
		return true;
	}
	
	

	
}