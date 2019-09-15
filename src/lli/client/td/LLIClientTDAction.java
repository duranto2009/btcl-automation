package lli.client.td;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lli.Application.ReviseClient.ReviseService;
import lli.Comments.Comments;
import lli.Comments.CommentsDeserializer;
import lli.Comments.CommentsService;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import annotation.JsonPost;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;
@ActionRequestMapping("lli/td/")
public class LLIClientTDAction extends AnnotatedRequestMappingAction {
	@Service
	LLIClientTDService lliClientTDService;
	@Service
	LLIProbableTDClientService lliProbableTDClientService;

	@Service
	ReviseService reviseService;


	@Service
	CommentsService commentsService;
	
	
	@RequestMapping(mapping="search", requestMethod=RequestMethod.All)
	public ActionForward getSearchDNSDomainPage(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
		RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
				SessionConstants.NAV_LLI_PROBABLE_TD_CLIENT, request, lliProbableTDClientService, 
				SessionConstants.VIEW_LLI_PROBABLE_TD_CLIENT, 
				SessionConstants.SEARCH_LLI_PROBABLE_TD_CLIENT
		);
		recordNavigationManager.doJob(loginDTO);
		return mapping.findForward("search-client-td-status");
	}
	@JsonPost
	@RequestMapping(mapping="td", requestMethod=RequestMethod.POST)
	public void TDByClientID(@RequestParameter(isJsonBody=true, value="id") long clientID) throws Exception {
		lliClientTDService.tempDisconnectClientByClientID(clientID,System.currentTimeMillis());
	}

	@RequestMapping(mapping="get-client-td-history", requestMethod=RequestMethod.All)
	public List<LLIClientTD> getTDHistoryByClientID(@RequestParameter("id") long clientID) throws Exception {
		return lliClientTDService.getTDHistoryByClientID(clientID);
	}
	
	@RequestMapping(mapping="get-probable-td-date", requestMethod=RequestMethod.All)
	public long getProbableTDDateByClientID(@RequestParameter("id") long clientID) throws Exception {
		return lliClientTDService.getProbableTDDateByClientID(clientID);
	}
	@RequestMapping(mapping="createview", requestMethod=RequestMethod.All)
	public ActionForward getTDCreateView(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
		RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
				SessionConstants.NAV_LLI_PROBABLE_TD_CLIENT, request, lliProbableTDClientService,
				SessionConstants.VIEW_LLI_PROBABLE_TD_CLIENT,
				SessionConstants.SEARCH_LLI_PROBABLE_TD_CLIENT
		);
		recordNavigationManager.doJob(loginDTO);
		return mapping.findForward("td-create-view");
	}

    @RequestMapping(mapping="view", requestMethod=RequestMethod.All)
    public ActionForward getTDView(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_LLI_PROBABLE_TD_CLIENT, request, lliProbableTDClientService,
                SessionConstants.VIEW_LLI_PROBABLE_TD_CLIENT,
                SessionConstants.SEARCH_LLI_PROBABLE_TD_CLIENT
        );
        recordNavigationManager.doJob(loginDTO);
        return mapping.findForward("td-view");
    }

}
