package crm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.StringUtils;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

@ActionRequestMapping("CrmActivityLog")
public class CrmActivityLogAction extends AnnotatedRequestMappingAction{
	@Service
	CrmActivityLogService crmActivityService;
	
	@RequestMapping (mapping="/getCrmActivityLogSearch", requestMethod = RequestMethod.All)
	public ActionForward getSearchCrmActivityLogpage(ActionMapping actionMapping, HttpServletRequest p_request) throws Exception{
		String target = "getCrmActivityLogSearch";
		LoginDTO loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        RecordNavigationManager rnManager = new RecordNavigationManager(
        			SessionConstants.NAV_CRM_ACTIVITY, p_request, crmActivityService,
        			SessionConstants.VIEW_CRM_ACTIVITY,
        			SessionConstants.SEARCH_CRM_ACTIVITY
        );
        
        rnManager.doJob(loginDTO);
        return (actionMapping.findForward(target));
	}
	@RequestMapping (mapping="/getAllCrmEmployee", requestMethod=RequestMethod.GET)
	public List<CrmEmployeeWithDesignationAndDepartment> getAllCrmEmployeeByPartialName(@RequestParameter("partialName") String partialName) throws Exception{
		return crmActivityService.getAllCrmEmployeeByPartialName(StringUtils.trim(partialName));
	}
}
