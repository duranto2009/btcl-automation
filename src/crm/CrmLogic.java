package crm;

import com.google.common.base.Objects;

import crm.repository.CrmAllEmployeeRepository;
import login.LoginDTO;

public class CrmLogic {
	// doubts
	public static boolean canReject(LoginDTO loginDTO, CrmComplainDTO crmComplainDTO){

		if(!canTakeAction(loginDTO, crmComplainDTO)){
			return false;
		}
		
		
		
		
		CrmEmployeeDTO assignerEmployeeDTO =  CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(crmComplainDTO.getAssignerID());
		
		if( assignerEmployeeDTO==null || Objects.equal(assignerEmployeeDTO.getUserID(), loginDTO.getUserID() ) ){
			return false;
		}
		return true;
	}
	
	
	// doubts
	public static boolean canSendFeedback(LoginDTO loginDTO, CrmComplainDTO crmComplainDTO)
	{
		if(!canTakeAction(loginDTO, crmComplainDTO)){
			return false;
		}
		
		if(crmComplainDTO.getComplainResolverID() == crmComplainDTO.getAssignerID()){
			return false;
		}
		
		return  true;
	}
	
	
	// no action can be taken on completed complain
	public static boolean canTakeAction(LoginDTO loginDTO, CrmComplainDTO crmComplainDTO){
		
		CrmEmployeeDTO crmEmployeeDTO = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(crmComplainDTO.getComplainResolverID());
		if(crmEmployeeDTO == null||crmEmployeeDTO.getUserID()==null){
			return false;
		}
		if(! Objects.equal( crmEmployeeDTO.getUserID(),loginDTO.getUserID())){
			return false;
		}
		if(crmComplainDTO.getCurrentStatus() == CrmComplainDTO.COMPLETED){ 
			return false;
		}
		return true;
	}

}
