package crm.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import annotation.ForwardedAction;
import common.RequestFailureException;
import crm.CrmClientComplainSubjectDTO;
import crm.CrmClientComplainToCrmComplainMapper;
import crm.CrmCommonPoolDTO;
import crm.CrmComplainDTO;
import crm.service.CrmCommonPoolService;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import util.TimeConverter;

@ActionRequestMapping("CrmClientComplain")
public class CrmClientComplainAction extends AnnotatedRequestMappingAction {

	@Service
	CrmCommonPoolService crmCommonPoolService;
	
	@RequestMapping(mapping = "/GetComplainSubjectByEntityTypeID", requestMethod = RequestMethod.GET)
	public List<CrmClientComplainSubjectDTO> getComplainSubjectByEntityTypeID(@RequestParameter("complainSubject") String complainSubject,@RequestParameter("entityTypeID") Integer entityTypeID) throws Exception {
		 return crmCommonPoolService.getComplainSubjectByEntityTypeID(complainSubject,entityTypeID);
	}
	
	@ForwardedAction
	@RequestMapping(mapping = "/CreateComplain", requestMethod = RequestMethod.GET)
	public String getCreateComplainPage() throws Exception {
		return "GetCreateComplainView";
	}
	
	@ForwardedAction
	@RequestMapping(mapping = "/Complain", requestMethod = RequestMethod.GET)
	public String getClientComplain(HttpServletRequest request,@RequestParameter("id") long complainID) throws Exception {
		CrmCommonPoolDTO crmCommonPoolDTO = crmCommonPoolService.getClientComplainByComplainID(complainID);
		request.setAttribute("CrmCommonPoolDTO", crmCommonPoolDTO);
		return "GetClientComplainView";
	}
	
	@RequestMapping(mapping = "/FeedbackClientCrmComplain", requestMethod = RequestMethod.POST)
	public void sendFeedbackToClient(@RequestParameter("ID") long commonPoolID,@RequestParameter("status") int status,@RequestParameter("feedbackOfNoc") String feedbackOfNoc) throws Exception {
		 crmCommonPoolService.sendFeedbackToClient(commonPoolID,status,feedbackOfNoc);
	}
	
	@RequestMapping(mapping = "/CreateComplainViaWeb", requestMethod = RequestMethod.POST)
	public long createNewComplainViaWeb(ActionMapping mapping, CrmCommonPoolDTO crmCommonPoolDTO,
			@RequestParameter("clientID")Long clientID,HttpServletRequest request, 
			LoginDTO loginDTO, @RequestParameter("crmEmployeeID") Long crmEmployeeID) throws Exception {
		
		if(loginDTO.getIsAdmin() &&  !loginDTO.isNOC()){
			throw new RequestFailureException("No non NOC has access to this common.");
		}
		
		if(loginDTO.getIsAdmin() && clientID == null){
			throw new RequestFailureException("NOC must select a client");
		}
		
		if(!loginDTO.getIsAdmin()){
			clientID = loginDTO.getAccountID();
		}
		// so now clientID can not be null
		
		String timeOfIncidentString = request.getParameter("timeOfIncidentString");
		if(timeOfIncidentString != null && timeOfIncidentString.length() > 0){
			long timeOfIncident = TimeConverter.getDateWithTimeFromDateTimeString(timeOfIncidentString);
			crmCommonPoolDTO.setTimeOfIncident(timeOfIncident);
		}
		
		crmCommonPoolDTO.setPortalType(CrmCommonPoolDTO.PORTAL_TYPE_WEB);
		crmCommonPoolDTO.setStatus(CrmComplainDTO.STARTED);
		crmCommonPoolDTO.setFeedbackOfNoc("We try to solve your problem as soon as possible.");
	
		crmCommonPoolDTO.setClientID(clientID);
		crmCommonPoolDTO.setCreatorEmployeeID(crmEmployeeID);
		try {
			int entityTypeID = Integer.parseInt(request.getParameter("entityTypeID"));
			crmCommonPoolDTO.setEntityTypeID(entityTypeID);
		}catch(Exception e) {
			throw new RequestFailureException("Invalid Service Type");
		}

		crmCommonPoolService.addNewClientComplain(crmCommonPoolDTO,request.getParameterValues("documents")
				,loginDTO);
		return crmCommonPoolDTO.getID();
		
	}
	
	@RequestMapping(mapping = "/CreateCrmComplain", requestMethod = RequestMethod.POST)
	public void createCrmComplainFromClientComplain(CrmClientComplainToCrmComplainMapper crmClientComplainToCrmComplainMapper,LoginDTO loginDTO) throws Exception{
		crmCommonPoolService.createCrmComplainFromClientCompain(crmClientComplainToCrmComplainMapper,loginDTO);
		//crmCommonPoolService.updateClientComplain(crmCommonPoolDTO);
	} 

	
	@RequestMapping(mapping = "/ReQueryClientCrmComplain", requestMethod = RequestMethod.POST)
	public void sendFurtheQueryClientCrmComplain(CrmCommonPoolDTO crmCommonPoolDTO,HttpServletRequest request, LoginDTO loginDTO) throws Exception {
		crmCommonPoolService.updateComplainFromClient(crmCommonPoolDTO,request.getParameterValues("documents"),loginDTO);
	}
	
	@RequestMapping(mapping="/sendResponseToClient", requestMethod=RequestMethod.POST)
	public void sendResponseToClient(@RequestParameter("ID") Long commonPoolID, @RequestParameter("status") int status,
			@RequestParameter("feedback") String feedback,LoginDTO loginDTO) throws Exception{
		crmCommonPoolService.sendResponseToClient(commonPoolID, status, feedback,loginDTO);
	}
	@RequestMapping(mapping = "/changeBlockedStatus", requestMethod = RequestMethod.POST)
	public void changeBlockedStatus(@RequestParameter ("complainID") long complainID,
			 LoginDTO loginDTO) throws Exception{
		crmCommonPoolService.blockClientComplain(complainID, loginDTO);
		
	}
}
