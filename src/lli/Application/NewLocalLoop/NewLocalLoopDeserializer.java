package lli.Application.NewLocalLoop;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.Office.OfficeService;
import util.ServiceDAOFactory;

import java.util.ArrayList;

public class NewLocalLoopDeserializer {

    LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);
    OfficeService officeService = ServiceDAOFactory.getService(OfficeService.class);

    public ArrayList<NewLocalLoop> deserialize_custom(JsonObject jsonObject) throws JsonParseException {
        ArrayList<NewLocalLoop>lists=new ArrayList<>();
        JsonArray jsonArray=jsonObject.getAsJsonArray("localloops")==null?
                        jsonObject.get("application").getAsJsonObject().
                                getAsJsonArray("localloops"):jsonObject.
                                                            getAsJsonArray("localloops");
        if(jsonArray!=null){
            for (JsonElement jsonElement:jsonArray)
            {
                NewLocalLoop NewlocalLoop=new NewLocalLoop();
                JsonObject object=jsonElement.getAsJsonObject();
                JsonElement ID= object.get("id");
                NewlocalLoop.setId(ID != null ? ID.getAsLong() : 0);
                NewlocalLoop.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
                JsonElement popID= object.get("popID");
                NewlocalLoop.setPopID(popID != null ? popID.getAsLong() : 0);
                try {
                    LLIApplication lliApplication=lliApplicationService.getLLIApplicationByApplicationID(jsonObject.get("applicationID").getAsLong());
                    if(object.get("officeID").getAsLong()>0){
                        NewlocalLoop.setOfficeID(object.get("officeID").getAsLong());
                    }else{
                        NewlocalLoop.setOfficeID(officeService.getOffice(NewlocalLoop.getApplicationID()).get(0).getId());//need to change for multi office
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JsonElement BW=object.get("bandwidth");
                NewlocalLoop.setBandwidth(BW !=null?BW.getAsLong():0);
                JsonElement ofc=object.get("ofcType");
                NewlocalLoop.setOfcType(ofc !=null?ofc.getAsLong():0);
                JsonElement router_switchID=object.get("router_switchID");
                NewlocalLoop.setRouter_switchID(router_switchID !=null?router_switchID.getAsLong():0);
                JsonElement clientDistances=object.get("clientDistances");
                NewlocalLoop.setClientDistances(clientDistances != null ? clientDistances.getAsLong() : 0);
                JsonElement BTCLDistances= object.get("BTCLDistances");
                NewlocalLoop.setBTCLDistances(BTCLDistances != null ? BTCLDistances.getAsLong() : 0);
                JsonElement OCDistances=object.get("OCDistances");
                NewlocalLoop.setOCDistances(OCDistances != null ? OCDistances.getAsLong() : 0);
                JsonElement portID=object.get("portID");
                NewlocalLoop.setPortID(portID != null ? portID.getAsInt() : 0);
                JsonElement VLANID=object.get("VLANID");
                NewlocalLoop.setVLANID(VLANID != null ? VLANID.getAsInt() : 0);
                JsonElement OCID=object.get("OCID");
                NewlocalLoop.setOCID(OCID != null ? OCID.getAsLong() : 0);
                JsonElement old_loop_id=object.get("oldLoopId");
                NewlocalLoop.setOldLoopId(old_loop_id != null ? old_loop_id.getAsLong() : 0);
                JsonElement isRemovable=object.get("isRemovable");
                NewlocalLoop.setIsRemovable(isRemovable != null ? isRemovable.getAsInt() : 0);
                NewlocalLoop.setOldLoopId(old_loop_id != null ? old_loop_id.getAsLong() : 0);
                lists.add(NewlocalLoop);
            }
        }
        return lists;
    }

    public LocalLoop deserializeToLocalLoop(NewLocalLoop localLoop,long officeId) {
        LocalLoop loca = new LocalLoop();
        loca.setClientDistances(localLoop.getClientDistances());
        loca.setOCDistances(localLoop.getOCDistances());
        loca.setPopName(localLoop.getPopName());
        loca.setBandwidth(localLoop.getBandwidth());
        loca.setBTCLDistances(localLoop.getBTCLDistances());
        loca.setOfcType(localLoop.getOfcType());
        loca.setOCID(localLoop.getOCID());
        loca.setPopName(localLoop.getPopName());
        loca.setPopID(localLoop.getPopID());
        loca.setApplicationID(localLoop.getApplicationID());
        loca.setPortID(localLoop.getPortID());
        loca.setVLANID(localLoop.getVLANID());
        loca.setRouter_switchID(localLoop.getRouter_switchID());
        loca.setOfficeID(officeId);
        return  loca;

    }


}
