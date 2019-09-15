package location;

import annotation.ForwardedAction;
import annotation.JsonPost;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lli.Application.IFR.IFR;
import lli.Application.IFR.IFRService;
import lli.Application.LLIApplicationService;
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
import java.util.List;
import java.util.stream.Collectors;

@ActionRequestMapping("location")
public class LocationAction extends AnnotatedRequestMappingAction{

    @Service
    DivisionService divisionService;
    @Service
    DistrictService districtService;
    @Service
    ZoneService zoneService;
    @Service
    AreaService areaService;

    @Service
    IFRService ifrService;

    @Service
    LLIApplicationService lliApplicationService ;



    //@JsonPost
    @RequestMapping(mapping="/division", requestMethod=RequestMethod.All)
    public JsonElement getDivision(@RequestParameter(value="id",required = false)long id ) throws Exception {

        Gson gson=new Gson();
        if(id>0){
            System.out.println("blah");
        }
        return gson.toJsonTree(divisionService.getDivision(id));

    }

    //@JsonPost
    @RequestMapping(mapping="/district", requestMethod=RequestMethod.All)
    public JsonElement getDistrict(@RequestParameter(value="division",required = false)int division) throws Exception {
        Gson gson=new Gson();
        return gson.toJsonTree(districtService.getDistrict(division));

    }


    @RequestMapping(mapping="/AllDistricts", requestMethod=RequestMethod.GET)
    public List<District> getAllDistrictsByQuery(@RequestParameter("query")String query) throws Exception {

        return districtService.getAllDistrictsByQuery(query);

    }


    @RequestMapping(mapping="/zone", requestMethod=RequestMethod.All)
    public JsonElement getZone(@RequestParameter(value="district",required = false)int district) throws Exception {
        Gson gson=new Gson();
        return gson.toJsonTree(zoneService.getZone(district));

    }

    /**
     * @author Forhad
     * @desc: get all zones from db
     * @param
     * @return json object of all zones
     * @throws Exception
     */
    @RequestMapping(mapping="/allzonesearch", requestMethod=RequestMethod.All)
    public List<Zone> getAllZone() throws Exception {
//        Gson gson=new Gson();
//        return gson.toJsonTree(zoneService.getAllZone());
        return zoneService.getAllZone();

    }
    @RequestMapping(mapping="/zoneByUser", requestMethod=RequestMethod.All)
    public List<Zone> getAllZoneByUser(@RequestParameter("id")long id) throws Exception {
//        Gson gson=new Gson();
//        return gson.toJsonTree(zoneService.getAllZone());
        return zoneService.getUserZone((int)id).stream().map(s->{
            Zone z=null;
            try {
                z = zoneService.getZonebyId(s);
            }catch (Exception e){
                e.printStackTrace();
            }
            return z;
        }).collect(Collectors.toList());

    }
    /** end: getAllZone() **/

    @JsonPost
    @RequestMapping(mapping="/addZoneByUser", requestMethod=RequestMethod.All)
    public void addAllZoneByUser(@RequestParameter(value = "userId", isJsonBody = true)long userId,
                                       @RequestParameter(value = "zoneList", isJsonBody = true)List<Zone>zoneList) throws Exception {

       zoneService.setZonesForUser(userId,zoneList);

    }


    @JsonPost
    @RequestMapping(mapping="/deleteZoneByUser", requestMethod=RequestMethod.All)
    public void deleteAllZoneByUser(@RequestParameter(value = "userId", isJsonBody = true)long userId,
                                 @RequestParameter(value = "zoneId", isJsonBody = true)long zoneId) throws Exception {

        zoneService.deleteZoneForUser(userId,zoneId);

    }


    @RequestMapping(mapping="/zonesearch", requestMethod=RequestMethod.All)
    public JsonElement getZone(@RequestParameter(value="val",required = false)String search) throws Exception {
        Gson gson=new Gson();
        return gson.toJsonTree(zoneService.getZoneByChar(search));

    }

    @RequestMapping(mapping="/area", requestMethod=RequestMethod.All)
    public JsonElement getArea(@RequestParameter(value="zone",required = false)long zone) throws Exception {
        Gson gson=new Gson();
        return gson.toJsonTree(areaService.getArea(zone));

    }



    //Application Location Page
    @ForwardedAction
    @RequestMapping(mapping="/location", requestMethod=RequestMethod.All)
    public String LocationLLIConnection(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_LLI_APPLICATION, request,
                lliApplicationService, SessionConstants.VIEW_LLI_APPLICATION, SessionConstants.SEARCH_LLI_APPLICATION);
        rnManager.doJob(loginDTO);
        return "location";
    }


    @RequestMapping(mapping="/testinsert", requestMethod=RequestMethod.GET)
    public String insertTest(LoginDTO loginDTO) throws Exception{
//        @RequestParameter(isJsonBody = true,value = "ifr") IFR ifr, LoginDTO loginDTO
        IFR ifr=new IFR();
        ifr.setApplicationID(2);
        ifr.setAvailableBW(5);
        ifrService.insertApplication(ifr,loginDTO);
        return "ok";
    }
}
