package lli.demandNote;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import lli.Application.LocalLoop.LocalLoop;
import lli.Application.ReviseClient.ReviseDTO;
import lli.Application.ReviseClient.ReviseService;

import annotation.ForwardedAction;
import annotation.JsonPost;
import common.RequestFailureException;
import common.bill.BillDTO;
import common.bill.BillService;
import lli.Application.ownership.LLIOwnerChangeConstant;
import lli.Application.ownership.demandnote.LLIOwnerChangeDemandNote;
import lli.Application.ownership.demandnote.LLIOwnerChangeDemandNoteService;
import lli.LLIActionButton;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import util.KeyValuePair;
import util.ServiceDAOFactory;

import javax.servlet.http.HttpServletRequest;


@ActionRequestMapping("lli/dn/")
public class LLIDemandNoteAction extends AnnotatedRequestMappingAction {
	public static final Logger logger = LoggerFactory.getLogger(LLIDemandNoteAction.class);
	
	@Service private LLIDemandNoteService lliDemandNoteService;
	@Service private LLIApplicationService lliApplicationService;
	@Service private BillService billService;
	@Service private ReviseService reviseService;
	@Service private LLIOwnerChangeDemandNoteService lliOwnerChangeDemandNoteService;

	@RequestMapping(mapping="change-owner-dn-page", requestMethod= RequestMethod.GET)
	@ForwardedAction
	public String getGenerateDNPage(
			LoginDTO loginDTO,
			HttpServletRequest request)throws Exception {
		request.setAttribute("url", LLIOwnerChangeConstant.APPLICATION_DEMAND_NOTE);
		return "owner-change";
	}

	@JsonPost
	@RequestMapping(mapping = "owner-change/generate", requestMethod = RequestMethod.POST)
	public void generateSBLongTerm(
			@RequestParameter(isJsonBody = true, value = "dn") LLIOwnerChangeDemandNote demandNote,
			@RequestParameter(isJsonBody = true, value = "appId") long appId,
			@RequestParameter(isJsonBody=true, value="nextState") int next,
			LoginDTO loginDTO) throws Exception {
		lliOwnerChangeDemandNoteService.handleDemandNoteGenerationRequest(demandNote, appId, next, loginDTO.getUserID());
	}

	@RequestMapping(mapping="new", requestMethod=RequestMethod.GET) 
	@ForwardedAction
	public String getGenerateDNPage(@RequestParameter("id") long appId) throws Exception {
		int applicationType = lliApplicationService.getFlowLLIApplicationByApplicationID(appId).getApplicationType();
		return getAppropriateViewName(applicationType);
	}

	@RequestMapping(mapping="new-revise", requestMethod=RequestMethod.GET)
	@ForwardedAction
	public String getGenerateDNRevise(@RequestParameter("id") long appId) throws Exception {
		ReviseDTO reviseDTO = reviseService.getappById(appId);
		if(reviseDTO != null){
			return getAppropriateViewName(reviseDTO.getApplicationType());
		}

		logger.error(" -  [ X ] Could not find the revise application from the id");
		return "";
	}
	
	@JsonPost
	@RequestMapping(mapping="new-connection/generate", requestMethod=RequestMethod.POST) 
	public long generateDNNewConnection(
			@RequestParameter(isJsonBody=true, value="dn") LLINewConnectionDemandNote newConnectionDN, 
			@RequestParameter(isJsonBody=true, value="appId") long appId,@RequestParameter(isJsonBody=true, value="nextState") long next, LoginDTO loginDTO) throws Exception {
		return lliDemandNoteService.insertNewConnectionDN(newConnectionDN, appId,next, loginDTO);
	}
	
	@JsonPost
	@RequestMapping(mapping="close/generate", requestMethod=RequestMethod.POST) 
	public long generateDNClosing(
			@RequestParameter(isJsonBody=true, value="dn") LLICloseConnectionDemandNote closeConnectionDN,
			@RequestParameter(isJsonBody=true, value="appId") long appId,@RequestParameter(isJsonBody=true, value="nextState") long next, LoginDTO loginDTO) throws Exception {
		return lliDemandNoteService.insertCloseConnectionDN(closeConnectionDN, appId,next, loginDTO);
	}
	
	@JsonPost
	@RequestMapping(mapping="common/generate", requestMethod=RequestMethod.POST) 
	public long generateDNCommon(
			@RequestParameter(isJsonBody=true, value="dn") LLISingleConnectionCommonDemandNote commonConnectionDN,
			@RequestParameter(isJsonBody=true, value="appId") long appId,@RequestParameter(isJsonBody=true, value="nextState") long next, LoginDTO loginDTO) throws Exception {
		return lliDemandNoteService.insertCommonConnectionDN(commonConnectionDN, appId,next, loginDTO);
	}
	
	@JsonPost
	@RequestMapping(mapping="short-bill/generate", requestMethod=RequestMethod.POST) 
	public long generateSBLongTerm(
			@RequestParameter(isJsonBody=true, value="dn") LLIBreakLongTermDemandNote breakLongTermDN,
			@RequestParameter(isJsonBody=true, value="appId") long appId, LoginDTO loginDTO) throws Exception {
		return lliDemandNoteService.insertLongTermSB(breakLongTermDN, appId, loginDTO);
	}
	
	@JsonPost
	@RequestMapping(mapping="reconnect/generate", requestMethod=RequestMethod.POST)
	public long generateDNReconnect(
			@RequestParameter(isJsonBody=true, value="dn") LLIReconnectConnectionDemandNote lliReconnectConnectionDN,
			@RequestParameter(isJsonBody=true, value="appId") long appId, LoginDTO loginDTO) throws Exception {
		return lliDemandNoteService.insertReconnectConnectionDN(lliReconnectConnectionDN, appId, loginDTO);
	}
	
	@JsonPost
	@RequestMapping(mapping="ownership-change/generate", requestMethod=RequestMethod.POST)
	public long generateDNOwnershipChange(
			@RequestParameter(isJsonBody=true, value="dn") LLIOwnerShipChangeDemandNote lliOwnershipChangeDN,
			@RequestParameter(isJsonBody=true, value="appId") long appId,
			@RequestParameter(isJsonBody = true,value = "nextState") int nextState,
			LoginDTO loginDTO) throws Exception {
		return lliDemandNoteService.insertOwnershipChangeDN(lliOwnershipChangeDN, appId, nextState,loginDTO);
	}

	

	

	
	@JsonPost
	@RequestMapping(mapping="cancel", requestMethod=RequestMethod.POST) 
	public void cancelDN(@RequestParameter(isJsonBody=true, value="billID") long billID) throws Exception {
		billService.cancelBillByBillID(billID);
	}

//	@RequestMapping(mapping="local-loop-breakdown-data", requestMethod = RequestMethod.GET)
//	public List<KeyValuePair<KeyValuePair<LocalLoop, Double>, String>> getLLILocalLoopBreakdownData(@RequestParameter("appId") long appId) throws Exception {
//		return lliDemandNoteService.getBreakdownData(appId);
//	}

	@RequestMapping(mapping="local-loop-breakdown-data", requestMethod = RequestMethod.GET)
	public List<Map<String, String>> getLLILocalLoopBreakdownData(@RequestParameter("appId") long appId) throws Exception {
		return lliDemandNoteService.getBreakdownData(appId);
	}

	@ForwardedAction
	@RequestMapping(mapping = "get-local-loop-breakdown", requestMethod = RequestMethod.GET)
	public String getLocalLoopBreakdown(@RequestParameter("appId") long appId) {
		return "get-local-loop-breakdown";
	}
	
	@RequestMapping(mapping="new-connection/autofill", requestMethod=RequestMethod.GET) 
	public LLINewConnectionDemandNote getNewConnectionDNAutoFillData(@RequestParameter("appId") long applicationID) throws Exception {
		return lliDemandNoteService.getAutoFillDataNewConnection(applicationID);
	}
	
	@RequestMapping(mapping="close/autofill", requestMethod=RequestMethod.GET) 
	public LLICloseConnectionDemandNote getCloseConnectionDNAutoFillData(@RequestParameter("appId") long applicationID) throws Exception {
		return lliDemandNoteService.getAutoFillDataCloseConnection(applicationID);
	}
	
	@RequestMapping(mapping="common/autofill", requestMethod=RequestMethod.GET) 
	public LLISingleConnectionCommonDemandNote getCommonConnectionDNAutoFillData(@RequestParameter("appId") long applicationID) throws Exception {
		return lliDemandNoteService.getAutoFillDataCommonConnection(applicationID);
	}
	
	@RequestMapping(mapping="break-long-term/autofill", requestMethod=RequestMethod.GET) 
	public LLIBreakLongTermDemandNote getBreakLongTermDNAutoFillData(@RequestParameter("appId") long applicationID) throws Exception {
		return lliDemandNoteService.getAutoFillDataBreakLongTerm(applicationID);
	}
	
	@RequestMapping(mapping="reconnect/autofill", requestMethod=RequestMethod.GET) 
	public LLIReconnectConnectionDemandNote getReconnectDNAutoFillData(@RequestParameter("appId") long applicationID) throws Exception {
		return lliDemandNoteService.getAutoFillDataReconnect(applicationID);
	}
	
	@RequestMapping(mapping="owner-change/autofill", requestMethod=RequestMethod.GET)
	public LLIOwnerShipChangeDemandNote getOwnershipChangeDNAutoFillData(@RequestParameter("appId") long applicationID) throws Exception {
		return lliDemandNoteService.getAutoFillDataOwnershipChange(applicationID);
	}
	
	@RequestMapping(mapping="getBill", requestMethod=RequestMethod.GET) 
	public BillDTO getBill(@RequestParameter("id") long billID) throws Exception {
		BillDTO billDTO = billService.getBillByBillID(billID);
		if(billDTO == null) {
			throw new RequestFailureException("No Bill Found with ID " + billID);
		}
		try {
			Method method = billDTO.getClass().getDeclaredMethod("getItemCosts");
			if(method != null) {
				method.invoke(billDTO);
			}
		}catch(Exception e) {
			logger.debug(e.getMessage(), e);
		}
		return billDTO;
	}
	
	
	@ForwardedAction
	@RequestMapping(mapping="view", requestMethod=RequestMethod.GET)
	public String getDNView(@RequestParameter("id") long billID) throws Exception {
		LLIApplication lliApplication =  lliApplicationService.getLLIApplicationByDemandNoteID(billID);
		if(lliApplication == null) {
			return "404";
		}
		int applicationType = lliApplication.getApplicationType();
		if(
				applicationType == LLIConnectionConstants.SHIFT_BANDWIDTH ||
				applicationType == LLIConnectionConstants.CHANGE_BILLING_ADDRESS
			) {
			return "404";
		} 
		return getAppropriateViewName(applicationType) + "-view";
	}
	
	@RequestMapping(mapping="get-actions", requestMethod=RequestMethod.GET)
	public List<LLIActionButton> getActions(@RequestParameter("id") long billID, LoginDTO loginDTO) throws Exception{
		return billService.getAvailableActions(billID, loginDTO);
	}
	
	
	/***
	 * Private Methods
	 */
	private String getAppropriateViewName(int applicationType) {
		return "lli-dn-" + LLIConnectionConstants.applicationTypeHyphenSeperatedMap.get(applicationType);
	}

}
