package nix.nixflowconnectionmanger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import inventory.InventoryItem;
import inventory.InventoryService;
import login.LoginDTO;
import nix.application.NIXApplication;
import nix.application.office.NIXApplicationOffice;
import nix.connection.NIXConnection;
import nix.efr.NIXEFR;
import nix.application.office.NIXApplicationOfficeService;
import nix.connection.NIXConnectionService;
import nix.efr.NIXEFRService;
import nix.ifr.NIXIFR;
import nix.ifr.NIXIFRService;
import user.UserDTO;
import user.UserRepository;
import util.ServiceDAOFactory;

import java.util.ArrayList;
import java.util.List;

public class NIXFlowViewDataBuilder {
    NIXIFRService ifrService = ServiceDAOFactory.getService(NIXIFRService.class);
    NIXEFRService efrService = ServiceDAOFactory.getService(NIXEFRService.class);
    NIXApplicationOfficeService officeService = ServiceDAOFactory.getService(NIXApplicationOfficeService.class);
    NIXFlowDataBuilder flowDataBuilder = ServiceDAOFactory.getService(NIXFlowDataBuilder.class);
    NIXConnectionService nixConnectionService = ServiceDAOFactory.getService(NIXConnectionService.class);
    InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);


    public JsonObject getCommonPart_nix(NIXApplication nixApplication, JsonObject jsonObject, LoginDTO loginDTO) {
        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();
        NIXConnection nixConnection = new NIXConnection();
        try {
            jsonObject = basicDataBuilder(jsonObject, nixApplication,loginDTO);
            JsonObject viewObject = viewObjectBuilder(nixApplication, jsonObject,loginDTO);
            jsonArray = UILabelBuilder(viewObject);
            jsonObject.add("formElements", jsonArray);
            if(loginDTO.getUserID()>0){
                UserDTO userDTO= UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());
                if(userDTO.isBTCLPersonnel()){
                    jsonObject.add("ifr", ifrArrayBuilder(nixApplication, false));
                    jsonObject.add("efr", efrArrayBuilder(nixApplication, true,loginDTO));
                }
                else if(!userDTO.isBTCLPersonnel()){

                    jsonObject.add("efr_vendor", efrArrayBuilder(nixApplication, false, loginDTO));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<NIXApplicationOffice> offices;
            offices = officeService.getOfficesByApplicationId(nixApplication.getId());
            JsonArray jsonArray1 = new JsonArray();
            for (NIXApplicationOffice office : offices) {
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

    public JsonObject basicDataBuilder(JsonObject jsonObject, NIXApplication nixApplication,LoginDTO loginDTO) {
        jsonObject = flowDataBuilder.basicDataBuilderNix(jsonObject, nixApplication,loginDTO);
        return jsonObject;
    }

    public JsonObject viewObjectBuilder(NIXApplication nixApplication,JsonObject jsonObject,LoginDTO loginDTO) {
        JsonObject viewObject = flowDataBuilder.viewObjectBuilderNix(nixApplication, jsonObject,loginDTO);
        return viewObject;
    }
    public JsonObject commentBuilder(NIXApplication nixApplication, JsonObject viewObject) {
        viewObject = flowDataBuilder.commentBuilderNix(nixApplication, viewObject);
        return viewObject;
    }

    public JsonArray efrArrayBuilder(NIXApplication nixApplication, boolean isBTCL, LoginDTO loginDTO) {
        JsonArray efrArray = new JsonArray();
        Gson gson = new Gson();
        List<NIXEFR> efrlist = new ArrayList<>();
        try {
            if (isBTCL) {
                efrlist = efrService.getAllEFR(nixApplication.getId());
            } else {
                efrlist = efrService.getVendorWiseEFR(nixApplication.getId(), loginDTO.getUserID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        UserDTO userDTO = new UserDTO();
        for (NIXEFR efr : efrlist) {
            try {
                userDTO = UserRepository.getInstance().getUserDTOByUserID(efr.getVendor());
            } catch (Exception e) {
                e.printStackTrace();
            }
            JsonElement jsonElement = gson.toJsonTree(efr);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonObject1.addProperty("vendorName", userDTO.getFullName().equals("") ? userDTO.getUsername() : userDTO.getFullName());
            InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(efr.getPop());
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

    public JsonArray ifrArrayBuilder(NIXApplication nixApplication, boolean getSelected) throws Exception {
        JsonArray ifrArray = new JsonArray();
        Gson gson = new Gson();
        List<NIXIFR> lists = new ArrayList<>();
        try {
            lists = ifrService.getIFRByAppID(nixApplication.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (NIXIFR ifr : lists) {
            JsonElement jsonElement = gson.toJsonTree(ifr);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(ifr.getPop());
            jsonObject1.addProperty("popName", inventoryItem.getName());
            jsonObject1.addProperty("officeName",officeService.getOfficeById(ifr.getOffice()).getName());
            ifrArray.add(jsonObject1);
        }
        return ifrArray;
    }
}
