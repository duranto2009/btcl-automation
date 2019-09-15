package lli;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.GsonBuilder;

import annotation.ForwardedAction;
import annotation.JsonPost;
import inventory.InventoryService;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

@ActionRequestMapping("lli/application")
public class LLIApplicationAction extends AnnotatedRequestMappingAction{
	
	@Service
	LLIConnectionService lliConnectionService ;
	@Service
	LLIConnectionApplicationService lliConnectionApplicationService ;
	@Service
	InventoryService inventoryService;
	
	@Override
	public GsonBuilder getGsonBuilder(){
		GsonBuilder gsonBuilder = new GsonBuilder();
		//LLI Connection
		gsonBuilder.registerTypeAdapter(LLIConnectionInstance.class, new LLIConnectionInstanceSerizalizer());
		gsonBuilder.registerTypeAdapter(LLIOffice.class, new LLIOfficeSerizalizer());
		gsonBuilder.registerTypeAdapter(LLILocalLoop.class, new LLILocalLoopSerizalizer());
		gsonBuilder.registerTypeAdapter(LLIConnectionInstance.class, new LLIConnectionDeserializer());
		gsonBuilder.registerTypeAdapter(LLIOffice.class, new LLIOfficeDeserializer());
		gsonBuilder.registerTypeAdapter(LLILocalLoop.class, new LLILocalLoopDeserializer());
		
		//LLI Connection Application
		gsonBuilder.registerTypeAdapter(LLIApplicationType.class, new LLIConnectionApplicationSerializer());
		gsonBuilder.registerTypeAdapter(LLIApplicationType.class, new LLIConnectionApplicationDeserializer());
		gsonBuilder.registerTypeAdapter(LLIApplicationInstance.class, new LLIConnectionApplicationInstanceSerializer());
		gsonBuilder.registerTypeAdapter(LLIApplicationInstance.class, new LLIConnectionApplicationInstanceDeserializer());
		return gsonBuilder;
	}
	
	
	/*Application Builder Begins*/
	@ForwardedAction
	@RequestMapping(mapping="/build", requestMethod=RequestMethod.GET)
	public String getApplicationBuilder() throws Exception {
		return "lli-application-build";
	}
	@JsonPost
	@RequestMapping(mapping="/build", requestMethod=RequestMethod.POST)
	public void postApplicationBuilder(@RequestParameter(isJsonBody = true,value = "application") LLIApplicationType lliConnectionApplication) throws Exception{
		lliConnectionApplicationService.insertNewApplication(lliConnectionApplication);
	}
	/*Application Builder Ends*/
	
	/*New Application Begins*/
	@ForwardedAction
	@RequestMapping(mapping="/new", requestMethod=RequestMethod.GET)
	public String getNewApplication() throws Exception {
		return "lli-application-new";
	}
	@JsonPost
	@RequestMapping(mapping="/new", requestMethod=RequestMethod.POST)
	public void postNewApplication(@RequestParameter(isJsonBody = true,value = "application") LLIApplicationInstance lliConnectionApplicationInstance) throws Exception{
		lliConnectionApplicationService.insertNewApplicationInstance(lliConnectionApplicationInstance);
	}
	@RequestMapping(mapping="/get-application-types", requestMethod=RequestMethod.GET)
	public List<LLIDropdownPair> getApplicationTypes() throws Exception{
		return lliConnectionApplicationService.getApplicationTypeList();
	}
	@RequestMapping(mapping="/get-application-type", requestMethod=RequestMethod.GET)
	public LLIApplicationType getApplicationType(@RequestParameter("id") Long applicationTypeID) throws Exception{
		return lliConnectionApplicationService.getApplicationTypeByID(applicationTypeID);
	}
	/*New Application Ends*/
	
	/*View Application Begins*/
	@ForwardedAction
	@RequestMapping(mapping="/view", requestMethod=RequestMethod.All)
	public String getViewApplication() throws Exception {
		return "lli-application-view";
	}
	@RequestMapping(mapping="/get-application-instance", requestMethod=RequestMethod.All)
	public LLIApplicationInstance getApplicationInstance(@RequestParameter("id") Long applicationInstanceID) throws Exception{
		return lliConnectionApplicationService.getApplicationInstanceByID(applicationInstanceID);
	}
	/*View Application Ends*/
	
	/*Search LLI Connection Begins*/
	@ForwardedAction
	@RequestMapping(mapping="/search", requestMethod=RequestMethod.All)
	public String searchLLIApplication(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
		RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_LLI_APPLICATION, request,
				lliConnectionApplicationService, SessionConstants.VIEW_LLI_APPLICATION, SessionConstants.SEARCH_LLI_APPLICATION);
		rnManager.doJob(loginDTO,request.getParameter("applicationType"));
		return "lli-application-search";
	}
	/*Search LLI Connection Ends*/
}
