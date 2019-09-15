package lli.Application.Office;

import annotation.ForwardedAction;
import annotation.JsonPost;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lli.Application.IFR.IFR;
import lli.Application.IFR.IFRService;
import lli.Application.LLIApplicationService;
import location.DistrictService;
import location.DivisionService;
import location.ZoneService;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

import javax.servlet.http.HttpServletRequest;

@ActionRequestMapping("/lli/application/office")
public class OfficeAction extends AnnotatedRequestMappingAction{

    @Service
    OfficeService officeService;
//    @Service
//    DistrictService districtService;
//    @Service
//    ZoneService zoneService;
//    @Service
//    AreaService areaService;
//
//    @Service
//    IFRService ifrService;
//
//    @Service
//    LLIApplicationService lliApplicationService ;
//
//

    //@JsonPost
    @RequestMapping(mapping="/view", requestMethod=RequestMethod.All)
    public JsonElement getDivision(@RequestParameter(value="id",required = false)long id ) throws Exception {

        Gson gson=new Gson();
        if(id>0){
            System.out.println("blah");
        }
        return gson.toJsonTree(officeService.getOffice(id));

    }

    @JsonPost
    @RequestMapping(mapping="/officeinsert", requestMethod=RequestMethod.POST)
    public String ifrData(@RequestParameter(isJsonBody = true,value = "ifr") String JsonString, LoginDTO loginDTO) throws Exception{

//        JsonElement jelement = new JsonParser().parse(JsonString);

//        JsonObject jsonObject = jelement.getAsJsonObject();
//
//        long appID=jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
//        int state=jsonObject.get("nextState").getAsInt();
//
//        lliApplicationService.updateApplicatonState(appID,state);//this need to update state
//
//        ArrayList<IFR> lists=new IFRDeserializer().deserialize_custom(jelement);
//        Comments comments=new CommentsDeserializer().deserialize_custom(jelement,loginDTO);
//        commentsService.insertComments(comments,loginDTO);
//        for ( IFR ifr:lists)
//        {
//            ifrService.insertApplication(ifr,loginDTO);
//            //parent ifr id need to be incorporated
//
//        }
        return "";//need to go search page
    }

//    //@JsonPost
//    @RequestMapping(mapping="/district", requestMethod=RequestMethod.All)
//    public JsonElement getDistrict(@RequestParameter(value="division",required = false)int division) throws Exception {
//        Gson gson=new Gson();
//        return gson.toJsonTree(districtService.getDistrict(division));
//
//    }
//
//    @RequestMapping(mapping="/zone", requestMethod=RequestMethod.All)
//    public JsonElement getZone(@RequestParameter(value="district",required = false)int district) throws Exception {
//        Gson gson=new Gson();
//        return gson.toJsonTree(zoneService.getZone(district));
//
//    }
//    @RequestMapping(mapping="/zonesearch", requestMethod=RequestMethod.All)
//    public JsonElement getZone(@RequestParameter(value="val",required = false)String search) throws Exception {
//        Gson gson=new Gson();
//        return gson.toJsonTree(zoneService.getZoneByChar(search));
//
//    }
//
//    @RequestMapping(mapping="/area", requestMethod=RequestMethod.All)
//    public JsonElement getArea(@RequestParameter(value="zone",required = false)long zone) throws Exception {
//        Gson gson=new Gson();
//        return gson.toJsonTree(areaService.getArea(zone));
//
//    }
//
//
//
//    //Application Location Page
//    @ForwardedAction
//    @RequestMapping(mapping="/location", requestMethod=RequestMethod.All)
//    public String LocationLLIConnection(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
//        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_LLI_APPLICATION, request,
//                lliApplicationService, SessionConstants.VIEW_LLI_APPLICATION, SessionConstants.SEARCH_LLI_APPLICATION);
//        rnManager.doJob(loginDTO);
//        return "location";
//    }
//
//
//    @RequestMapping(mapping="/testinsert", requestMethod=RequestMethod.GET)
//    public String insertTest(LoginDTO loginDTO) throws Exception{
////        @RequestParameter(isJsonBody = true,value = "ifr") IFR ifr, LoginDTO loginDTO
//        IFR ifr=new IFR();
//        ifr.setApplicationID(2);
//        ifr.setAvailableBW(5);
//        ifrService.insertApplication(ifr,loginDTO);
//        return "ok";
//    }
}
