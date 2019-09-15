package nix.revise;

import annotation.JsonPost;
import lli.Comments.CommentsService;
import login.LoginDTO;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class NIXClientTDAction extends AnnotatedRequestMappingAction {
    @Service
    NIXClientTDService nixClientTDService;
    @Service
    NIXProbableTDClientService nixProbableTDClientService;

    @Service
    NIXReviseService reviseService;


    @Service
    CommentsService commentsService;


    @RequestMapping(mapping="search", requestMethod= RequestMethod.All)
    public ActionForward getSearchDNSDomainPage(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_NIX_PROBABLE_TD_CLIENT, request, nixProbableTDClientService,
                SessionConstants.VIEW_NIX_PROBABLE_TD_CLIENT,
                SessionConstants.SEARCH_NIX_PROBABLE_TD_CLIENT
        );
        recordNavigationManager.doJob(loginDTO);
        return mapping.findForward("search-client-td-status");
    }
    @JsonPost
    @RequestMapping(mapping="td", requestMethod=RequestMethod.POST)
    public void TDByClientID(@RequestParameter(isJsonBody=true, value="id") long clientID) throws Exception {
        nixClientTDService.tempDisconnectClientByClientID(clientID,System.currentTimeMillis());
    }

    @RequestMapping(mapping="get-client-td-history", requestMethod=RequestMethod.All)
    public List<NIXClientTD> getTDHistoryByClientID(@RequestParameter("id") long clientID) throws Exception {
        return nixClientTDService.getTDHistoryByClientID(clientID);
    }

    @RequestMapping(mapping="get-probable-td-date", requestMethod=RequestMethod.All)
    public long getProbableTDDateByClientID(@RequestParameter("id") long clientID) throws Exception {
        return nixClientTDService.getProbableTDDateByClientID(clientID);
    }
    @RequestMapping(mapping="createview", requestMethod=RequestMethod.All)
    public ActionForward getTDCreateView(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_NIX_PROBABLE_TD_CLIENT, request, nixProbableTDClientService,
                SessionConstants.VIEW_NIX_PROBABLE_TD_CLIENT,
                SessionConstants.SEARCH_NIX_PROBABLE_TD_CLIENT
        );
        recordNavigationManager.doJob(loginDTO);
        return mapping.findForward("td-create-view");
    }

    @RequestMapping(mapping="view", requestMethod=RequestMethod.All)
    public ActionForward getTDView(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_NIX_PROBABLE_TD_CLIENT, request, nixProbableTDClientService,
                SessionConstants.VIEW_NIX_PROBABLE_TD_CLIENT,
                SessionConstants.SEARCH_NIX_PROBABLE_TD_CLIENT
        );
        recordNavigationManager.doJob(loginDTO);
        return mapping.findForward("td-view");
    }

}
