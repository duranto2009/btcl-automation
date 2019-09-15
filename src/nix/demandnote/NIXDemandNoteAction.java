package nix.demandnote;

import annotation.ForwardedAction;
import annotation.JsonPost;
import common.ApplicationGroupType;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import nix.revise.NIXReviseService;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

@ActionRequestMapping("nix/dn/")
@Log4j
public class NIXDemandNoteAction extends AnnotatedRequestMappingAction {

    @Service
    NIXDemandNoteService NIXDemandNoteService;

    @Service
    NIXReviseService nixReviseService;

    @ForwardedAction
    @RequestMapping(mapping = "preview", requestMethod = RequestMethod.All)
    public String newConnectionView() {
        return "nix-dn-preview";
    }

    @RequestMapping(mapping = "autofill", requestMethod = RequestMethod.GET)
    public NIXDemandNote getDemandNoteAutofilled(@RequestParameter("appId") long appId, @RequestParameter("appGroup") int type) throws Exception {
        ApplicationGroupType appGroupType = ApplicationGroupType.getAppGroupTypeByOrdinal(type);
        return NIXDemandNoteService.getDNAutofilled(appId, appGroupType);
    }

    @JsonPost
    @RequestMapping(mapping = "generate", requestMethod = RequestMethod.POST)
    public void generateNewConnectionDN(
            @RequestParameter(isJsonBody = true, value = "dn") NIXDemandNote NIXDemandNote,
            @RequestParameter(isJsonBody = true, value = "appId") long appId,
            @RequestParameter(isJsonBody = true, value = "nextState") int next,
            @RequestParameter(isJsonBody = true, value = "appGroup") int type,
            LoginDTO loginDTO) throws Exception {

        ApplicationGroupType applicationGroupType = ApplicationGroupType.getAppGroupTypeByOrdinal(type);
        NIXDemandNoteService.generateDemandNote(NIXDemandNote, appId, next, applicationGroupType, loginDTO);
    }
}
