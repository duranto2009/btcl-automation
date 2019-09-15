package coLocation.demandNote;

import annotation.ForwardedAction;
import annotation.JsonPost;
import coLocation.CoLocationConstants;
import login.LoginDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@ActionRequestMapping("co-location/dn")
public class CoLocationDemandNoteAction extends AnnotatedRequestMappingAction {

    private static final Logger logger = LoggerFactory.getLogger(CoLocationDemandNoteAction.class);

    @Service
    CoLocationDemandNoteService coLocationDemandNoteService;

    @ForwardedAction
    @RequestMapping(mapping = "/new", requestMethod = RequestMethod.GET)
    public String newConnectionView(LoginDTO loginDTO, HttpServletRequest request) {
        request.setAttribute("url", CoLocationConstants.NEW_CONNECTION_APPLICATION_DEMAND_NOTE);
        return "co-location";
    }

    @RequestMapping(mapping = "/new-connection/autofill", requestMethod = RequestMethod.GET)
    public CoLocationDemandNote getNewDNAutoFillData(@RequestParameter("appId") long appId) throws Exception {
        return coLocationDemandNoteService.newConnectionDNData(appId);
    }

    @JsonPost
    @RequestMapping(mapping = "/new-connection/generate", requestMethod = RequestMethod.POST)
    public void generateSBLongTerm(
            @RequestParameter(isJsonBody = true, value = "dn") CoLocationDemandNote demandNote,
            @RequestParameter(isJsonBody = true, value = "appId") long appId,
            @RequestParameter(isJsonBody=true, value="nextState") int next,

            LoginDTO loginDTO) throws Exception {
        coLocationDemandNoteService.handleDemandNoteGenerationRequest(demandNote, appId, next, loginDTO.getUserID(),loginDTO);
    }
}
