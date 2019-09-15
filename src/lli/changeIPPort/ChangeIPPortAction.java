package lli.changeIPPort;

import annotation.ForwardedAction;
import annotation.JsonPost;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import inventory.InventoryService;
import ip.IPService;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.FlowConnectionManager.LLIFlowConnectionService;
import lli.Application.LocalLoop.LocalLoopService;
import lli.Application.Office.OfficeService;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;


@ActionRequestMapping("lli/changeIPPort")
public class ChangeIPPortAction extends AnnotatedRequestMappingAction {


    @Service
    IPService ipService;

    @Service
    LLIFlowConnectionService lliFlowConnectionService;

    @Service
    ChangeIPPortService changeIPPortService;

    @ForwardedAction
    @RequestMapping(mapping = "/get-change-ip-port", requestMethod = RequestMethod.GET)
    public String getChagneIPPort() throws Exception {
        return "get-change-ip-port";
    }


    @RequestMapping(mapping = "/get-connection", requestMethod = RequestMethod.All)
    public JsonObject getActions(@RequestParameter("connectionID") long connectionID,
                                    LoginDTO loginDTO) throws Exception {
        LLIConnection lliConnection =  lliFlowConnectionService.getConnectionByID(connectionID);
        JsonArray jsonArray = ipService.getIPBlocksByConnectionId(connectionID);
        JsonObject jsonObject = changeIPPortService.deserializeInfo(lliConnection);
        jsonObject.add("ipByConId",jsonArray);
        return jsonObject;
    }


    @RequestMapping(mapping = "/get-inventory-data", requestMethod = RequestMethod.GET)
    public JsonObject getInventoryData(@RequestParameter("popId") long popId) throws Exception {
        return changeIPPortService.getInventoryData(popId);
    }
    @RequestMapping(mapping = "/get-switches", requestMethod = RequestMethod.All)
    public JsonArray getSwitchs(@RequestParameter("popID") long popId,
                                 LoginDTO loginDTO) throws Exception {

        return changeIPPortService.getswitchByPop(popId);
    }
    @RequestMapping(mapping = "/get-ports", requestMethod = RequestMethod.All)
    public JsonArray getPorts(@RequestParameter("switchId") long switchId,
                                 LoginDTO loginDTO) throws Exception {
        return changeIPPortService.getPortBySwitch(switchId);

    }

    @RequestMapping(mapping = "/get-vlans", requestMethod = RequestMethod.All)
    public JsonArray getVlans(@RequestParameter("switchId") long switchId,
                                 LoginDTO loginDTO) throws Exception {
        return changeIPPortService.getVlans(switchId);

    }

    @JsonPost
    @RequestMapping(mapping = "/updatelocalloop", requestMethod = RequestMethod.POST)
    public String updatePortInfo( @RequestParameter(isJsonBody = true, value = "data") String portInfo,
                                     LoginDTO loginDTO) throws Exception {
        JsonElement jelement = new JsonParser().parse(portInfo);
        JsonObject jsonObject = jelement.getAsJsonObject();
        changeIPPortService.updatePortInfoBatchOperation(jsonObject,loginDTO);
        return "oka";

    }


    @JsonPost
    @RequestMapping(mapping = "/updateIP", requestMethod = RequestMethod.POST)
    public String updateIPInfo( @RequestParameter(isJsonBody = true, value = "data") String ipInfo,
                                  LoginDTO loginDTO) throws Exception {
        JsonElement jelement = new JsonParser().parse(ipInfo);
        JsonObject jsonObject = jelement.getAsJsonObject();
        long userId = loginDTO.getUserID()>0?loginDTO.getUserID():loginDTO.getAccountID();
        long connectionId = jsonObject.get("connectionId") != null ? jsonObject.get("connectionId").getAsLong() : 0;
        long usageId = jsonObject.get("usageId") != null ? jsonObject.get("usageId").getAsLong() : 0;
        changeIPPortService.deallocationAndAllocation(usageId,jsonObject,connectionId,userId);
        return "oka";

    }


}
