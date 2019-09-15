package lli;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.GsonBuilder;

import annotation.ForwardedAction;
import annotation.JsonPost;
import lli.Options.LLIOptionsService;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

import java.util.List;

// TODO: maruf: purge this class (after testing)
@ActionRequestMapping("lli/longterm")
public class LLILongTermAction extends AnnotatedRequestMappingAction {

    @Service
    LLILongTermService lliLongTermService;
    @Service
    LLIOptionsService lliOptionsService;

    @Override
    public GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //LLI Long Term
        gsonBuilder.registerTypeAdapter(LLILongTermContract.class, new LLILongTermSerializer());
        gsonBuilder.registerTypeAdapter(LLILongTermContract.class, new LLILongTermDeserializer());
        return gsonBuilder;
    }


    // TODO: maruf: purge this method
    @ForwardedAction
    @RequestMapping(mapping = "/new", requestMethod = RequestMethod.GET)
    public String getLongTermNew() throws Exception {
        return "lli-longterm-new";
    }

    // TODO: maruf: purge this method
    @JsonPost
    @RequestMapping(mapping = "/new", requestMethod = RequestMethod.POST)
    public void postLongTermNew(@RequestParameter(isJsonBody = true, value = "contract") LLILongTermContract lliLongTermContract) throws Exception {
        lliLongTermService.insertNewLongTermContract(lliLongTermContract);
    }

    // TODO: maruf: purge this method
    @ForwardedAction
    @RequestMapping(mapping = "/search", requestMethod = RequestMethod.All)
    public String searchLongTermContract(HttpServletRequest request) throws Exception {
        LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        RecordNavigationManager rnManager = new RecordNavigationManager(
                SessionConstants.NAV_LONG_TERM, request, lliLongTermService,
                SessionConstants.VIEW_LONG_TERM,
                SessionConstants.SEARCH_LONG_TERM
        );

        rnManager.doJob(loginDTO);
        return "lli-longterm-search";
    }

    // TODO: maruf: purge this method
    @ForwardedAction
    @RequestMapping(mapping = "/view", requestMethod = RequestMethod.GET)
    public String getLongTermView() throws Exception {
        return "lli-longterm-view";
    }

    // TODO: maruf: move the APIs to different location
    /*Long Term API Begins*/
    @RequestMapping(mapping = "/get-longterm", requestMethod = RequestMethod.All)
    public LLILongTermContract getLongTermContract(@RequestParameter("id") long longTermContractID) throws Exception {
        return lliLongTermService.getLLILongTermContractByLongTermContractID(longTermContractID);
    }

    @RequestMapping(mapping = "/get-longterm-by-client-id", requestMethod = RequestMethod.GET)
    public List<LLIDropdownPair> getLLILongTermContractListByClientID(@RequestParameter("id") long clientID) throws Exception {
        return lliOptionsService.getLongTermContractListClientID(clientID);
    }

    @RequestMapping(mapping = "/get-contract-history-list-by-contract-id", requestMethod = RequestMethod.GET)
    public List<LLILongTermContract> getLLILongTermContractHistoryListByLongTermContractID(@RequestParameter("id") long contractID) throws Exception {
        return lliLongTermService.getLLILongTermHistoryByContractID(contractID);
    }

    @RequestMapping(mapping = "/get-contract-by-history-id", requestMethod = RequestMethod.GET)
    public LLILongTermContract getLLILongTermContractByHistoryID(@RequestParameter("id") long historyID) throws Exception {
        return lliLongTermService.getLLILongTermContractInstanceByContractHistoryID(historyID);
    }

    @RequestMapping(mapping = "/total", requestMethod = RequestMethod.GET)
    public double getTotalLT(@RequestParameter("clientId") long clientId) throws Exception {
        return lliLongTermService.getTotalActiveLongTermBandwidthByClientID(clientId);
    }
    /*Long Term API Ends*/
}

