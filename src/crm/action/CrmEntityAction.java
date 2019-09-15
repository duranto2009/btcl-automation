package crm.action;

import java.util.List;

import com.google.gson.GsonBuilder;

import common.EntityDTO;
import common.StringUtils;
import crm.CrmEmployeeDTO;
import crm.CrmEmployeeNode;
import crm.adapter.CrmEntityAdapter;
import crm.adapter.EmployeeAdapter;
import crm.adapter.EmployeeDTOAdapter;
import crm.service.CrmEmployeeService;
import crm.service.CrmEntityService;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import util.KeyValuePair;

@ActionRequestMapping("CrmEntity")
public class CrmEntityAction extends AnnotatedRequestMappingAction {

	@Service
	CrmEntityService crmEntityService;
	
	@Override
	public GsonBuilder getGsonBuilder(){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeHierarchyAdapter(EntityDTO.class, new CrmEntityAdapter());
		gsonBuilder.registerTypeAdapter(EntityDTO.class, new CrmEntityAdapter());
		return gsonBuilder;
	}

	@RequestMapping(mapping = "/GetEntityListByEntityTypeID", requestMethod = RequestMethod.GET)
	public List<KeyValuePair<Long, String>> getEntityDTOListByEntityTypeId(@RequestParameter("entityTypeID") Integer entityTypeID,
															 @RequestParameter("clientID") Long clientID, @RequestParameter("entityName") String entityName, LoginDTO loginDTO) throws Exception {
		
		return crmEntityService.getEntityDTOListByEntityTypeId(entityTypeID,clientID,StringUtils.trim(entityName),loginDTO);
	}

}
