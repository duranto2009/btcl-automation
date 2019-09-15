package crm.action;

import java.util.*;

import org.apache.struts.action.*;

import annotation.ForwardedAction;
import common.RequestFailureException;
import common.StringUtils;
import crm.CrmComplainDTO;
import crm.service.CrmComplainService;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.*;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

@ActionRequestMapping("CrmComplain")
public class CrmComplainAction extends AnnotatedRequestMappingAction {

	@Service
	CrmComplainService complainService;
	
	@ForwardedAction
	@RequestMapping(mapping = "/CreateComplain", requestMethod = RequestMethod.GET)
	public String getCreateComplainPage() throws Exception {
		return "GetCreateComplainView";
	}

	@RequestMapping(mapping = "/Feedback", requestMethod = RequestMethod.POST)
	public void feedBackComplain(@RequestParameter("complainID") long complainID,@RequestParameter("feedback") String feedback,
			LoginDTO loginDTO) throws Exception {
		if(feedback == null){
			throw new RequestFailureException("Please provide a feedback message");
		}
		complainService.sendFeedBack(complainID, feedback,loginDTO.getUserID());
	}
	
	@ForwardedAction
	@RequestMapping(mapping = "/Feedbacks", requestMethod = RequestMethod.POST)
	public String feedBackComplain(@RequestParameter("complainID") List<Long> complainIDList,@RequestParameter("feedback") String feedback, LoginDTO loginDTO) throws Exception {;
		if(feedback == null){
			throw new RequestFailureException("Please provide a feedback message");
		}
		complainService.sendFeedBackList(complainIDList, feedback,loginDTO.getUserID());
		return "GetComplainSearch";
	}

	@ForwardedAction
	@RequestMapping(mapping = "/PassComplains", requestMethod = RequestMethod.POST)
	public String passComplainList(@RequestParameter("complainID") List<Long> complainIDList
			,@RequestParameter("complainResolverID") long passedToEmployeeID
			,@RequestParameter("passingMessage")String passingMessage,LoginDTO loginDTO) throws Exception {
		complainService.passComplainList(complainIDList, passedToEmployeeID,loginDTO.getUserID(),passingMessage);
		return "GetComplainSearch";
	}

	@RequestMapping(mapping = "/RejectComplain", requestMethod = RequestMethod.POST)
	public void rejectComplain(@RequestParameter("complainID") long complainID,@RequestParameter("rejectionCause") String rejectionCause, LoginDTO loginDTO) throws Exception{
		if(rejectionCause == null){
			throw new RequestFailureException("Please provide a rejection cause.");
		}
		complainService.rejectComplain(complainID, rejectionCause, loginDTO.getUserID());
	}

	@RequestMapping(mapping = "/ChangeComplainStatus", requestMethod = RequestMethod.POST)
	public void updateComplainStatus(LoginDTO loginDTO,@RequestParameter("currentStatus") int status
			, @RequestParameter("complainID")long complainID, @RequestParameter("resolverMsg") String resolverMsg) throws Exception{
		complainService.changeStatusOfComplain(complainID, status, loginDTO.getUserID(),resolverMsg,loginDTO);
	}
	
	@RequestMapping(mapping = "/CompleteComplain", requestMethod = RequestMethod.POST)
	public void completeComplain(LoginDTO loginDTO, @RequestParameter("complainID")long complainID, 
			@RequestParameter("resolverMsg")String resolverMsg, LoginDTO loginDTO2) throws Exception{
		complainService.completeComplain(complainID, loginDTO.getUserID(), resolverMsg, loginDTO);
	}
	
	@RequestMapping(mapping = "/PassComplain", requestMethod = RequestMethod.POST)
	public void passComplain(@RequestParameter("complainID")long complainID,@RequestParameter("complainResolverID")long newComplainResolverID
			,@RequestParameter("passingMessage")String passingMessage,LoginDTO loginDTO) throws Exception{		
		passingMessage = StringUtils.trim(passingMessage);
		if(newComplainResolverID == -1){
			throw new RequestFailureException("Please select a valid Resolver.");
		}
		
		complainService.passComplain(complainID, newComplainResolverID, loginDTO.getUserID(),passingMessage);
	}
	@RequestMapping(mapping = "/changeBlockedStatus", requestMethod = RequestMethod.POST)
	public void changeBlockedStatus(@RequestParameter ("complainID") long complainID, LoginDTO loginDTO) throws Exception{
		complainService.blockCrmComplain(complainID, loginDTO);
	}

}
