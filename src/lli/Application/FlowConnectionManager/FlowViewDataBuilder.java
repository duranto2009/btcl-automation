package lli.Application.FlowConnectionManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.bill.BillService;
import inventory.InventoryItem;
import inventory.InventoryService;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRService;
import lli.Application.IFR.IFR;
import lli.Application.IFR.IFRService;
import lli.Application.LLIApplication;
import lli.Application.LocalLoop.LocalLoopService;
import lli.Application.Office.Office;
import lli.Application.Office.OfficeService;
import lli.Comments.CommentsService;
import location.ZoneService;
import login.LoginDTO;
import user.UserDTO;
import user.UserRepository;
import util.ServiceDAOFactory;

import java.util.*;

public class FlowViewDataBuilder {

    IFRService ifrService = ServiceDAOFactory.getService(IFRService.class);
    EFRService efrService = ServiceDAOFactory.getService(EFRService.class);
    OfficeService officeService = ServiceDAOFactory.getService(OfficeService.class);
    FlowDataBuilder flowDataBuilder = ServiceDAOFactory.getService(FlowDataBuilder.class);
    LocalLoopService localLoopService = ServiceDAOFactory.getService(LocalLoopService.class);
    BillService billService = ServiceDAOFactory.getService(BillService.class);

    ZoneService zoneService = ServiceDAOFactory.getService(ZoneService.class);

    CommentsService commentsService = ServiceDAOFactory.getService(CommentsService.class);
    LLIFlowConnectionService lliFlowConnectionService = ServiceDAOFactory.getService(LLIFlowConnectionService.class);


    InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);


    public JsonObject getCommonPart_new(LLIApplication lliApplication, JsonObject jsonObject, LoginDTO loginDTO) throws Exception{
        //Serialize Common LLI Application
        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();
        LLIConnection lliConnection = new LLIConnection();

//        Map session = ActionContext.get("session");
        try {
            jsonObject = basicDataBuilder(jsonObject, lliApplication,loginDTO);
            if (lliApplication.getConnectionId() > 0) {
                lliConnection = lliFlowConnectionService.getConnectionByID(lliApplication.getConnectionId());

                if (lliConnection.getID() > 0) {
                    JsonElement jsonElement = gson.toJsonTree(lliConnection);
                    jsonObject.add("connection", jsonElement);
                }

            }
            JsonObject viewObject = viewObjectBuilder(lliApplication, lliConnection, jsonObject,loginDTO);

            jsonArray = UILabelBuilder(viewObject);

            jsonObject.add("formElements", jsonArray);


            if(loginDTO.getUserID()>0){
                UserDTO userDTO=UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());
                if(userDTO.isBTCLPersonnel()){
                    jsonObject.add("ifr", ifrArrayBuilder(lliApplication, false));
                    jsonObject.add("efr", efrArrayBuilder(lliApplication, true,loginDTO));
                }
                else if(!userDTO.isBTCLPersonnel()){

                    jsonObject.add("efr_vendor", efrArrayBuilder(lliApplication, false, loginDTO));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            List<Office> offices;
            if (lliApplication.getConnectionId() > 0) {


                offices = officeService.getOfficeByCON(lliApplication.getConnectionId());
            } else {
                offices = officeService.getOffice(lliApplication.getApplicationID());
            }

            JsonArray jsonArray1 = new JsonArray();
            for (Office office : offices) {

                JsonElement jsonElement = gson.toJsonTree(office);
                JsonObject jsonObject1 = (JsonObject) jsonElement;

                jsonArray1.add(jsonObject1);

            }
            jsonObject.add("officeList", jsonArray1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public JsonObject basicDataBuilder(JsonObject jsonObject, LLIApplication lliApplication,LoginDTO loginDTO) {

        jsonObject = flowDataBuilder.basicDataBuilder(jsonObject, lliApplication,loginDTO);
        return jsonObject;

    }

    public JsonObject viewObjectBuilder(LLIApplication lliApplication, LLIConnection lliConnection, JsonObject jsonObject,LoginDTO loginDTO) throws Exception{

        JsonObject viewObject = flowDataBuilder.viewObjectBuilder(lliApplication, lliConnection, jsonObject,loginDTO);

        return viewObject;


    }

    public JsonObject commentBuilder(LLIApplication lliApplication, JsonObject viewObject) {
        viewObject = flowDataBuilder.commentBuilder(lliApplication, viewObject);
        return viewObject;
    }

    public JsonArray efrArrayBuilder(LLIApplication lliApplication, boolean isBTCL, LoginDTO loginDTO) {


        JsonArray efrArray = new JsonArray();
        Gson gson = new Gson();

        List<EFR> efrlist = new ArrayList<>();
        try {
            if (isBTCL) {
                efrlist = efrService.getAllEFR(lliApplication.getApplicationID());

            } else {
                efrlist = efrService.getVendorWiseEFR(lliApplication.getApplicationID(), loginDTO.getUserID());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        UserDTO userDTO = new UserDTO();

        for (EFR efr : efrlist) {

            try {
                userDTO = UserRepository.getInstance().getUserDTOByUserID(efr.getVendorID());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JsonElement jsonElement = gson.toJsonTree(efr);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonObject1.addProperty("vendorName", userDTO.getFullName().equals("") ? userDTO.getUsername() : userDTO.getFullName());

            InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(efr.getPopID());
            jsonObject1.addProperty("popName", inventoryItem.getName());
//            JsonArray array=UILabelBuilder(jsonObject1);

            efrArray.add(jsonObject1);

        }

        return efrArray;


    }

    public JsonArray UILabelBuilder(JsonObject jsonObject) {
        JsonArray jsonArray = flowDataBuilder.UILabelBuilder(jsonObject);

        return jsonArray;

    }

    public JsonArray ifrArrayBuilder(LLIApplication lliApplication, boolean getSelected) throws Exception {

        JsonArray ifrArray = new JsonArray();


        Gson gson = new Gson();


        List<IFR> lists = new ArrayList<>();
        try {
            lists = ifrService.getIFRByAppID(lliApplication.getApplicationID());


        } catch (Exception e) {
            e.printStackTrace();
        }
        for (IFR ifr : lists) {

            JsonElement jsonElement = gson.toJsonTree(ifr);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(ifr.getPopID());
            jsonObject1.addProperty("popName", inventoryItem.getName());
            jsonObject1.addProperty("officeName",officeService.getOfficeById(ifr.getOfficeID()).get(0).getOfficeName());
            ifrArray.add(jsonObject1);

        }


        return ifrArray;


    }

}
