package eeSearch;

import annotation.ForwardedAction;
import annotation.JsonPost;
import application.ApplicationService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import login.LoginDTO;
import notification.NotificationService;
import officialLetter.OfficialLetter;
import officialLetter.OfficialLetterService;
import officialLetter.RecipientElement;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;
import util.ServiceDAOFactory;
import vpn.VPNConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ActionRequestMapping("eeSearch/")
public class EESearchAction extends AnnotatedRequestMappingAction {

    @Service
    OfficialLetterService officialLetterService;


    @ForwardedAction
    @RequestMapping(mapping = "official-letter", requestMethod = RequestMethod.All)
    public  String getOfficialLetterSearchPage(HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_EE_OFFICIAL_LETTER, request, officialLetterService,
                SessionConstants.VIEW_EE_OFFICIAL_LETTER,
                SessionConstants.SEARCH_EE_OFFICIAL_LETTER
        );
        recordNavigationManager.doJob(loginDTO);
        return "eeSearch-official-letter";
    }

    @JsonPost
    @RequestMapping(mapping = "forward-letter", requestMethod = RequestMethod.All)
    public  void forwardOfficialLetter(@RequestParameter(isJsonBody = true,value = "data")String forwadInfor,
                                         LoginDTO loginDTO) throws Exception {

        JsonElement jsonElement = new JsonParser().parse(forwadInfor);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray userArray = jsonObject.getAsJsonArray("userList");
        long officialLetterId = jsonObject.get("officialLetterId") != null ? jsonObject.get("officialLetterId").getAsLong() : 0;
        //int moduleId = jsonObject.get("module") != null ? jsonObject.get("module").getAsInt() : 0;
        OfficialLetter officialLetter = officialLetterService.getofficialLetterById(officialLetterId);
        List<RecipientElement> recipientElements =  officialLetterService.getCCRecipientElements(userArray);
        long senderId = loginDTO.getUserID()>0?loginDTO.getUserID():loginDTO.getAccountID();
        officialLetterService.saveOfficialLetterTransactionalDefault(officialLetter,  recipientElements, senderId);
        //ServiceDAOFactory.getService(NotificationService.class).se 
        // TODO: 6/24/2019 latter official letter send notification

    }
 }
