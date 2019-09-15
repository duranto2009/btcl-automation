package vpn.demandNote;

import annotation.ForwardedAction;
import annotation.JsonPost;
import common.ApplicationGroupType;
import entity.facade.DemandNoteAutofillFacade;
import entity.facade.DemandNoteGenerationFacade;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import vpn.VPNConstants;

@ActionRequestMapping(VPNConstants.VPN_BASE_URL+"/dn/")
public class VPNDemandNoteAction extends AnnotatedRequestMappingAction {

    @Service
    private DemandNoteAutofillFacade demandNoteAutofillFacade;

    @Service
    private DemandNoteGenerationFacade demandNoteGenerationFacade;

    @ForwardedAction
    @RequestMapping(mapping = "preview", requestMethod = RequestMethod.GET)
    public String preview() {
        return "vpn-dn-preview";
    }

    @RequestMapping(mapping="autofill", requestMethod = RequestMethod.GET)
    public VPNDemandNote getAutofillDemandNote(@RequestParameter("id") long id,
                                               @RequestParameter("global") boolean isGlobal,
                                               @RequestParameter("appGroup") int appGroup


    ) throws Exception {
        return demandNoteAutofillFacade.autofillVPNDemandNote(id, isGlobal, ApplicationGroupType.getAppGroupTypeByOrdinal(appGroup));
    }

    @JsonPost
    @RequestMapping(mapping = "generate", requestMethod = RequestMethod.POST)
    public void generateNewConnectionDN(
            @RequestParameter(isJsonBody = true, value = "dn") VPNDemandNote demandNote,
            @RequestParameter(isJsonBody = true, value = "id") long id,
            @RequestParameter(isJsonBody = true, value = "nextState") int next,
            @RequestParameter(isJsonBody = true, value = "global")boolean isGlobal,
            LoginDTO loginDTO) throws Exception {

//        long senderId = loginDTO.getUserID();
        demandNoteGenerationFacade.generateVPNDemandNote(demandNote, id, isGlobal, next, loginDTO);
    }
}
