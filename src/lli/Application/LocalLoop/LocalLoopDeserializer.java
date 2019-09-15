package lli.Application.LocalLoop;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.Application.Office.OfficeService;
import util.ServiceDAOFactory;

import java.util.ArrayList;

public class LocalLoopDeserializer {

    LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);
    OfficeService officeService = ServiceDAOFactory.getService(OfficeService.class);

    public ArrayList<LocalLoop> deserialize_custom(JsonElement jsonElements) throws JsonParseException {

        //Receive JSON

        //JsonArray jsonArray1=jsonElements.getAsJsonArray();
        JsonObject jsonObject = jsonElements.getAsJsonObject();
        ArrayList<LocalLoop> lists = new ArrayList<>();
//        long appID=Integer.parseInt(String.valueOf(jsonObject.get("applicationID")));
        String isNewLoop="";
        if(jsonObject.get("application").getAsJsonObject().has("isNewLoop")){

             isNewLoop=jsonObject.get("application").getAsJsonObject().get("isNewLoop").getAsString();
        }else{
            isNewLoop="false";

        }
        JsonArray jsonArray=new JsonArray();
        if(isNewLoop.equals("true")){
            jsonArray=jsonObject.get("application").getAsJsonObject().get("modifiedlocalloops").getAsJsonArray();

        }else {

             jsonArray = jsonObject.getAsJsonArray("localloops");
        }
        if (jsonArray != null) {


            for (JsonElement jsonElement : jsonArray) {
                LocalLoop localLoop = new LocalLoop();


                JsonObject object = jsonElement.getAsJsonObject();

                JsonElement ID = object.get("id");
                JsonElement historyID = object.get("historyID");

                localLoop.setId(ID != null ? ID.getAsLong() : 0);
                localLoop.setHistoryID(historyID != null ? historyID.getAsLong() : 0);

                localLoop.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);


                JsonElement popID = object.get("popID");
                localLoop.setPopID(popID != null ? popID.getAsLong() : 0);

                try {

                    LLIApplication lliApplication = lliApplicationService.getLLIApplicationByApplicationID(jsonObject.get("applicationID").getAsLong());
                    if (object.get("officeID").getAsLong() > 0) {

                        localLoop.setOfficeID(object.get("officeID").getAsLong());
                    } else {
                        localLoop.setOfficeID(officeService.getOffice(localLoop.getApplicationID()).get(0).getId());
                        //need to change for multi office

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                JsonElement BW = object.get("bandwidth");
                localLoop.setBandwidth(BW != null ? BW.getAsLong() : 0);

                JsonElement ofc = object.get("ofcType");
                localLoop.setOfcType(ofc != null ? ofc.getAsLong() : 0);

                JsonElement router_switchID = object.get("router_switchID");
                localLoop.setRouter_switchID(router_switchID != null ? router_switchID.getAsLong() : 0);


                JsonElement clientDistances = object.get("clientDistances");
                localLoop.setClientDistances(clientDistances != null ? clientDistances.getAsLong() : 0);

                JsonElement BTCLDistances = object.get("BTCLDistances");
                localLoop.setBTCLDistances(BTCLDistances != null ? BTCLDistances.getAsLong() : 0);

                JsonElement OCDistances = object.get("OCDistances");
                localLoop.setOCDistances(OCDistances != null ? OCDistances.getAsLong() : 0);

                JsonElement portID = object.get("portID");
                localLoop.setPortID(portID != null ? portID.getAsInt() : 0);
                JsonElement VLANID = object.get("VLANID");
                localLoop.setVLANID(VLANID != null ? VLANID.getAsInt() : 0);
                JsonElement OCID = object.get("OCID");
                localLoop.setOCID(OCID != null ? OCID.getAsLong() : 0);
                JsonElement adjustBTCL = object.get("adjustedBTClDistance");
                localLoop.setAdjustedBTClDistance(adjustBTCL != null ? adjustBTCL.getAsLong() : 0);
                JsonElement adjustOC = object.get("adjustedOCDistance");
                localLoop.setAdjustedOCDistance(adjustOC != null ? adjustOC.getAsLong() : 0);


                lists.add(localLoop);


                //parent ifr id need to be incorporated

            }
        }


        return lists;
    }
}
