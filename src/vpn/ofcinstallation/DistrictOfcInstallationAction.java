package vpn.ofcinstallation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.RequestFailureException;
import login.LoginDTO;
import login.PermissionConstants;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;

@ActionRequestMapping("DistrictOfcInstallation")
public class DistrictOfcInstallationAction extends AnnotatedRequestMappingAction {

	@Service
	DistrictOfcInstallationService districtOfcInstallationService;

	static org.apache.log4j.Logger logger = Logger.getLogger(DistrictOfcInstallationAction.class);


	@RequestMapping(mapping = "/UpdateDistrictOfcInstallationCost", requestMethod = RequestMethod.GET)
	public ActionForward getUpdateDistrictOfcInstallationCost(ActionMapping mapping, HttpServletRequest request){
		LoginDTO loginDTO = (login.LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		boolean hasPermission = false;
		if( loginDTO.getUserID()>0 )
		{
			if(loginDTO.getMenuPermission( PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST) != -1     		
					&&( loginDTO.getMenuPermission( PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST )>=PermissionConstants.PERMISSION_MODIFY ) )    	
				hasPermission=true;    
		}
		if( !hasPermission ){	
			throw new RequestFailureException("You have No Permission to do this.");
		}

		return mapping.findForward("GetUpdateDistrictOfcInstallationCost");
	}

	@RequestMapping(mapping = "/UpdateDistrictOfcInstallationCost", requestMethod = RequestMethod.POST)
	public void updateDistrictOfcInstallationCost(ActionMapping mapping, HttpServletRequest request) throws Exception {

		LoginDTO loginDTO = (login.LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		boolean hasPermission = false;
		if( loginDTO.getUserID()>0 )
		{
			if(loginDTO.getMenuPermission( PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST) != -1     		
					&&( loginDTO.getMenuPermission( PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST )>=PermissionConstants.PERMISSION_MODIFY ) )    	
				hasPermission=true;    
		}

		if( !hasPermission ){	
			throw new RequestFailureException("You have No Permission to do this.");
		}


		List<String> parameterNames = new ArrayList<String>(request.getParameterMap().keySet());
		List<DistrictOfcInstallationDTO> districtOfcInstallationDTOListToBeUpdated= new ArrayList<DistrictOfcInstallationDTO>();


		for(String key:parameterNames){
			if(key.startsWith("installationcost_in_")){
				DistrictOfcInstallationDTO districtOfcInstallationDTO=new DistrictOfcInstallationDTO();

				Double installationCost = Double.parseDouble(request.getParameter(key));
				districtOfcInstallationDTO.setInstallationCost(installationCost);

				Long districtID=Long.valueOf(key.replace("installationcost_in_",""));
				districtOfcInstallationDTO.setDistrictID(districtID);

				districtOfcInstallationDTOListToBeUpdated.add(districtOfcInstallationDTO);
			}
		}
		logger.debug(districtOfcInstallationDTOListToBeUpdated);
		districtOfcInstallationService.updateAllDistrictOfcInstallationCost(districtOfcInstallationDTOListToBeUpdated);

	}


	//Added By Jami

	@RequestMapping(mapping = "/UpdateCommonCharge", requestMethod = RequestMethod.POST)
	public void updateCommonCharge(ActionMapping mapping, HttpServletRequest request) throws Exception {

				
		LoginDTO loginDTO = (login.LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		boolean hasPermission = false;
		if( loginDTO.getUserID()>0 )
		{
			if(loginDTO.getMenuPermission( PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST) != -1     		
					&&( loginDTO.getMenuPermission( PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST )>=PermissionConstants.PERMISSION_MODIFY ) )    	
				hasPermission=true;    
		}

		if( !hasPermission ){	
			throw new RequestFailureException("You have No Permission to do this.");
		}
		
		List<String> parameterNames = new ArrayList<String>(request.getParameterMap().keySet());		

		for(String key:parameterNames){
			logger.debug("parammeters for installation : "+key);
		}
	}




	//end


	@RequestMapping(mapping = "/RefreshInstallationCosts", requestMethod = RequestMethod.GET)
	public List<DistrictOfcInstallationDTO> getAllDistrictOfcInstallationCosts(ActionMapping mapping, HttpServletRequest request) throws Exception {
		return districtOfcInstallationService.getAllDistrictOfcInstallationCosts();
	}
}
