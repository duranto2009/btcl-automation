package distance.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import common.CommonActionStatusDTO;
import common.RequestFailureException;
import distance.DistanceService;
import distance.form.DistrictDistanceDTO;
import distance.form.UnionDistanceDTO;
import distance.form.UpazilaDistanceDTO;
import login.LoginDTO;
import login.PermissionConstants;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;



@ActionRequestMapping("Distance")
public class UpdateDistanceAction extends AnnotatedRequestMappingAction {

	@Service
	DistanceService distanceService;

	static org.apache.log4j.Logger logger = Logger.getLogger(UpdateDistanceAction.class);
	
	
	@RequestMapping(mapping = "/UpdateDistrictDistance", requestMethod = RequestMethod.GET)
	public ActionForward getUpdateDistrictPage(ActionMapping mapping, HttpServletRequest request){
		LoginDTO loginDTO = (login.LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		boolean hasPermission = false;
		if( loginDTO.getUserID()>0 )
		{
		    if((loginDTO.getMenuPermission(PermissionConstants.VPN_CONFIGURATION_DISTANCE) != -1  && (loginDTO.getMenuPermission( PermissionConstants.VPN_CONFIGURATION_DISTANCE )>=PermissionConstants.PERMISSION_MODIFY )) 
		    	|| (loginDTO.getMenuPermission(PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST) != -1  && (loginDTO.getMenuPermission( PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST)>=PermissionConstants.PERMISSION_MODIFY )))
		        hasPermission=true;    
		}

		if( !hasPermission ){	
			CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
			return mapping.findForward("NoPermission");
		}
		return mapping.findForward("GetUpdateDistrictPage");
	}
	
	@RequestMapping(mapping = "/UpdateDistrictDistance", requestMethod = RequestMethod.POST)
	public void updateDistrictDistance(ActionMapping mapping, HttpServletRequest request) throws Exception {
		
		LoginDTO loginDTO = (login.LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		boolean hasPermission = false;
		if( loginDTO.getUserID()>0 )
		{
			if((loginDTO.getMenuPermission(PermissionConstants.VPN_CONFIGURATION_DISTANCE) != -1  && (loginDTO.getMenuPermission( PermissionConstants.VPN_CONFIGURATION_DISTANCE )>=PermissionConstants.PERMISSION_MODIFY )) 
			    	|| (loginDTO.getMenuPermission(PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST) != -1  && (loginDTO.getMenuPermission( PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST)>=PermissionConstants.PERMISSION_MODIFY )))
			        hasPermission=true; 
		}

		if( !hasPermission ){	
			throw new RequestFailureException("You have No Permission to do this.");
		}
		

		List<String> parameterNames = new ArrayList<String>(request.getParameterMap().keySet());
		ArrayList<DistrictDistanceDTO> list= new ArrayList<DistrictDistanceDTO>();
		
		for(String key:parameterNames){
			if(key.startsWith("distance_from_")){
				DistrictDistanceDTO udf=new DistrictDistanceDTO();
				
				Double distance=Double.parseDouble(request.getParameter(key));
				udf.setDistance(distance);
				
				Integer distinationId=Integer.parseInt(key.replace("distance_from_",""));
				udf.setDestinationDistrictId(distinationId);
				
				Integer originatingDistrictId=Integer.parseInt(request.getParameter("originatingDistrictId"));
				udf.setSourceDistrictId(originatingDistrictId);
				
				if(udf.getDestinationDistrictId()==udf.getSourceDistrictId()){
					udf.setDistance(0.0);
				}
				list.add(udf);
			}
		}
		ArrayList<DistrictDistanceDTO> list2= new ArrayList<DistrictDistanceDTO>();
		for(DistrictDistanceDTO udf: list){
			DistrictDistanceDTO viceVersaUdf= new DistrictDistanceDTO();
			viceVersaUdf.setDistance(udf.getDistance());
			viceVersaUdf.setDestinationDistrictId(udf.getDestinationDistrictId());
			viceVersaUdf.setSourceDistrictId((udf.getSourceDistrictId()));
			list2.add(viceVersaUdf);
		}
		list.addAll(list2);
		
		logger.debug(list);
		
		request.getSession(true).setAttribute("originatingDistrictId", request.getParameter("originatingDistrictId"));
		
		distanceService.updateDistance(list);

	}
	
	
	
	
	@RequestMapping(mapping = "/UpdateUpazilaDistance", requestMethod = RequestMethod.GET)
	public ActionForward getUpdateUpazilaPage(ActionMapping mapping, HttpServletRequest request) {
		LoginDTO loginDTO = (login.LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		boolean hasPermission = false;
		if( loginDTO.getUserID()>0 )
		{
			if((loginDTO.getMenuPermission(PermissionConstants.VPN_CONFIGURATION_DISTANCE) != -1  && (loginDTO.getMenuPermission( PermissionConstants.VPN_CONFIGURATION_DISTANCE )>=PermissionConstants.PERMISSION_MODIFY )) 
			    	|| (loginDTO.getMenuPermission(PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST) != -1  && (loginDTO.getMenuPermission( PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST)>=PermissionConstants.PERMISSION_MODIFY )))
			        hasPermission=true;     
		}

		if( !hasPermission ){	
			CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
			return mapping.findForward("NoPermission");
		}
		return mapping.findForward("GetUpdateUpazilaPage");
	}

	@RequestMapping(mapping = "/UpdateUpazilaDistance", requestMethod = RequestMethod.POST)
	public void updateUpazilaDistance(ActionMapping mapping, HttpServletRequest request) throws Exception {
		LoginDTO loginDTO = (login.LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		boolean hasPermission = false;
		if( loginDTO.getUserID()>0 )
		{
			if((loginDTO.getMenuPermission(PermissionConstants.VPN_CONFIGURATION_DISTANCE) != -1  && (loginDTO.getMenuPermission( PermissionConstants.VPN_CONFIGURATION_DISTANCE )>=PermissionConstants.PERMISSION_MODIFY )) 
			    	|| (loginDTO.getMenuPermission(PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST) != -1  && (loginDTO.getMenuPermission( PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST)>=PermissionConstants.PERMISSION_MODIFY )))
			        hasPermission=true;   
		}

		if( !hasPermission ){	
			throw new RequestFailureException("You have No Permission to do this.");
		}
		
		List<String> parameterNames = Collections.list(request.getParameterNames());
		List<UpazilaDistanceDTO> upazilaDistanceDTOList = new ArrayList<>();
		for (int i = 2; i < parameterNames.size(); i++) {

			long upazilaId = Long.parseLong(parameterNames.get(i));
			UpazilaDistanceDTO upazilaDTO = distanceService.getUpazilaDTOByUpazilaId(upazilaId);
			int currentDistanceFromView = Integer.parseInt(request.getParameter(upazilaId + ""));
			if(upazilaDTO.getDistance() != currentDistanceFromView){
				upazilaDTO.setDistance(currentDistanceFromView);
				upazilaDistanceDTOList.add(upazilaDTO);
			}
		}
		distanceService.updateUpazilaUnderSameDistrict(upazilaDistanceDTOList);
	}

	
	
	
	@RequestMapping(mapping = "/UpdateUnionDistance", requestMethod = RequestMethod.GET)
	public ActionForward getUpdateUnionPage(ActionMapping mapping, HttpServletRequest request){
		LoginDTO loginDTO = (login.LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		boolean hasPermission = false;
		if( loginDTO.getUserID()>0 )
		{
			if((loginDTO.getMenuPermission(PermissionConstants.VPN_CONFIGURATION_DISTANCE) != -1  && (loginDTO.getMenuPermission( PermissionConstants.VPN_CONFIGURATION_DISTANCE )>=PermissionConstants.PERMISSION_MODIFY )) 
			    	|| (loginDTO.getMenuPermission(PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST) != -1  && (loginDTO.getMenuPermission( PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST)>=PermissionConstants.PERMISSION_MODIFY )))
			        hasPermission=true; 
		}

		if( !hasPermission ){	
			CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
			return mapping.findForward("NoPermission");
		}
		return mapping.findForward("GetUpdateUnionPage");
	}
	
	@RequestMapping(mapping = "/UpdateUnionDistance", requestMethod = RequestMethod.POST)
	public void updateUnionDistance(HttpServletRequest request) throws Exception {
		LoginDTO loginDTO = (login.LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		boolean hasPermission = false;
		if( loginDTO.getUserID()>0 )
		{
			if((loginDTO.getMenuPermission(PermissionConstants.VPN_CONFIGURATION_DISTANCE) != -1  && (loginDTO.getMenuPermission( PermissionConstants.VPN_CONFIGURATION_DISTANCE )>=PermissionConstants.PERMISSION_MODIFY )) 
			    	|| (loginDTO.getMenuPermission(PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST) != -1  && (loginDTO.getMenuPermission( PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST)>=PermissionConstants.PERMISSION_MODIFY )))
			        hasPermission=true; 
		}

		if( !hasPermission ){	
			throw new RequestFailureException("You have No Permission to do this.");
		}
		
		
		long districtID=Long.parseLong(request.getParameter("districtID"));
		long upazilaID=Long.parseLong(request.getParameter("upazilaID"));
		
		boolean isUpazilaUnderThisDistrict = distanceService.isUpazilaUnderThisDistrict(districtID, upazilaID);
		if (isUpazilaUnderThisDistrict){
			List<String> parameterNames =   Collections.list(request.getParameterNames());

			for(int i=4;i<parameterNames.size();i++){
				
				long unionID = Long.parseLong(parameterNames.get(i));
				UnionDistanceDTO unionDTO = distanceService.getUnionDTOByUnionId(unionID);
				unionDTO.setDistance(Integer.parseInt(request.getParameter(unionID+"")));
				distanceService.updateUnion(unionDTO);
				
			}
		}
	}
	
	
	
	
	
	
	
	
	//AutoComplete Distance
	@RequestMapping(mapping = "/PopulateDistrictToDistrictDistance", requestMethod = RequestMethod.GET)
	public List<DistrictDistanceDTO> populateDistrictToDistrictDistance(HttpServletRequest request) throws Exception {
		List<DistrictDistanceDTO> districtDistanceDTOList = new ArrayList<DistrictDistanceDTO>();
		String idStr = request.getParameter("sourceDistrictID"); // use it as source district
		String feDistrictIdStr = request.getParameter("feDistrictId");// use it as destination district
		if (StringUtils.isBlank(feDistrictIdStr)) {
			int id = Integer.parseInt(idStr);
			districtDistanceDTOList = distanceService.getDistancesFromAllDistricts(id);
		} else if (StringUtils.isNotBlank(feDistrictIdStr)) {
			int id = Integer.parseInt(idStr);
			int feDistrictId = Integer.parseInt(feDistrictIdStr);
			DistrictDistanceDTO districtDistanceDTO = distanceService.getDistanceBetweenTwoDistricts(id, feDistrictId);
			districtDistanceDTOList.add(districtDistanceDTO);
		}
    	return districtDistanceDTOList;
	}
	
	@RequestMapping(mapping = "/PopulateDistrictToUpazilaDistance", requestMethod = RequestMethod.GET)
	public List<UpazilaDistanceDTO> populateDistrictToUpazilaDistance(HttpServletRequest request) throws Exception {
    	List<UpazilaDistanceDTO> upazilaDistanceDTOListByUpazila = new ArrayList<UpazilaDistanceDTO>();
    	String districtID = request.getParameter("districtID"); 
    	upazilaDistanceDTOListByUpazila = distanceService.getUpazilaDistanceDTOListByDistrictID(districtID);
    	return upazilaDistanceDTOListByUpazila;
	}
	
	@RequestMapping(mapping = "/PopulateUpazilaToUnionDistance", requestMethod = RequestMethod.GET)
	public List<UnionDistanceDTO> populateUpazilaToUnionDistance(HttpServletRequest request) throws Exception {
    	List<UnionDistanceDTO> unionDistanceDTOListByUpazila = new ArrayList<UnionDistanceDTO>();
    	String upazilaID = request.getParameter("upazilaID");
    	System.err.println(upazilaID);
    	unionDistanceDTOListByUpazila = distanceService.getUnionDistanceDTOListByUpazilaID(upazilaID);
    	return unionDistanceDTOListByUpazila;
	}
	
	//AutoComplete Location
	@RequestMapping(mapping = "/PopulateDistrictList", requestMethod = RequestMethod.GET)
	public List<DistrictDistanceDTO> populateDistrictList(HttpServletRequest request) throws Exception {				
		List<DistrictDistanceDTO> districtList = new ArrayList<DistrictDistanceDTO>();
		String districtValueForAutoComplete = request.getParameter("districtValue");			
		districtList= distanceService.getMatchedDistricts(districtValueForAutoComplete);
		return districtList;
	}
		
	@RequestMapping(mapping = "/PopulateUpazilaList", requestMethod = RequestMethod.GET)
	public List<UpazilaDistanceDTO> populateUpazilaList(HttpServletRequest request) throws Exception {
		List<UpazilaDistanceDTO> upazilaList = new ArrayList<UpazilaDistanceDTO>();
		String parentDistrictID = request.getParameter("parentDistrictID");
    	String upazilaValueForAutoComplete = request.getParameter("upazilaValue");        	
    	upazilaList = distanceService.getMatchedUpazilas(upazilaValueForAutoComplete, parentDistrictID);
    	return upazilaList;
	}
	
	
	//Check Distance Between Two Anything
	@RequestMapping(mapping = "/GetDistanceBetweenTwoLocation", requestMethod = RequestMethod.GET)
	public double getDistanceBetweenTwoLocation(@RequestParameter("location1") long fromLocationID , @RequestParameter("location2")long toLocationID ) throws Exception {
		double distance = distanceService.getDistanceBetweenTwoLocation(fromLocationID, toLocationID);
    	logger.debug("Distance Between Two Location: "+ distance);
		return distance;
	}
	

}
