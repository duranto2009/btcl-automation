package nix.efr;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import inventory.InventoryItem;
import nix.application.office.NIXApplicationOffice;
import nix.application.office.NIXApplicationOfficeService;
import util.ServiceDAOFactory;

import java.util.ArrayList;

import static vpn.link.request.VpnLinkRequestService.inventoryService;

public class NIXEFRDeserializer {

    NIXApplicationOfficeService nixApplicationOfficeService = ServiceDAOFactory.getService(NIXApplicationOfficeService.class);

    public ArrayList<NIXEFR> deserialize_custom(JsonElement jsonElements) {
        JsonObject jsonObject = jsonElements.getAsJsonObject();
        ArrayList<NIXEFR> lists = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray("pops");
        if (jsonArray != null) {
            for (JsonElement jsonElement : jsonArray) {
                NIXEFR efr = new NIXEFR();
                JsonObject object = jsonElement.getAsJsonObject();
                JsonElement ID = object.get("id");
                efr.setId(ID != null ? ID.getAsLong() : 0);
                efr.setApplication(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
                JsonElement popID = object.get("popID");
                efr.setPop(popID != null ? popID.getAsLong() : object.get("pop").getAsLong());
                JsonElement source = object.get("sourceType");
                efr.setSourceType(source!=null ? source.getAsInt() : 0);
                JsonElement sourceName = object.get("source");
               if (efr.getSourceType() == 1) {
                    InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(efr.getPop());
                    efr.setSource(inventoryItem.getName());
                } else {
                    efr.setSource(sourceName != null ? sourceName.getAsString() : null);
                }
                JsonElement dest = object.get("destinationType");
                efr.setDestinationType(dest != null ? dest.getAsInt() : 0);
                JsonElement officeid = object.get("officeId") != null ? object.get("officeId") : object.get("office");
                efr.setOffice(officeid != null ? officeid.getAsLong() : 0);
                if (efr.getDestinationType() == 3) {
                    try {
                        long officeId= efr.getOffice();
                        NIXApplicationOffice office = nixApplicationOfficeService.getOfficeById(officeId);
                        efr.setDestination(office.getAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    JsonElement destName = object.get("destination");
                    efr.setDestination(destName != null ? destName.getAsString() : null);
                }
                JsonElement proposedLoopDistance = object.get("proposedDistance");
                efr.setProposedDistance(proposedLoopDistance != null ? proposedLoopDistance.getAsLong() : 0);
                JsonElement actualDistance = object.get("actualDistance");
                efr.setActualDistance(actualDistance != null ? actualDistance.getAsLong() : 0);
                // TODO: 12/24/2018 the below json get might need to change  
                JsonElement approvedDistance = object.get("approvedDistance");
                efr.setApprovedDistance(approvedDistance != null ? approvedDistance.getAsLong() : 0);
                JsonElement vendorID = object.get("vendorID");
                efr.setVendor(vendorID != null ? vendorID.getAsLong() : object.get("vendor").getAsLong());
                JsonElement vendorType = object.get("vendorType");
                efr.setVendorType(vendorType != null ? vendorType.getAsInt() : 0);
                JsonElement workGiven = object.get("workGiven");
                efr.setWorkGiven(workGiven != null ? workGiven.getAsInt() : 0);
                JsonElement workCompleted = object.get("workCompleted");
                efr.setWorkCompleted(workCompleted != null ? workCompleted.getAsLong() : 0);

                JsonElement isForwarded = object.get("isForwarded");
                efr.setIsForwarded(isForwarded != null ? isForwarded.getAsInt() : 0);

                JsonElement quotationStatus = object.get("quotationStatus");
                efr.setQuotationStatus(quotationStatus != null ? quotationStatus.getAsInt() : 0);
                JsonElement isSelected = object.get("isSelected");
                efr.setIsSelected(isSelected != null ? isSelected.getAsInt() : 0);

                JsonElement ofc = object.get("ofcType");
                efr.setOfcType(ofc != null ? ofc.getAsInt() : 0);
                JsonElement loopId = object.get("loopId");
                efr.setLoopId(loopId != null ? loopId.getAsLong() : 0);
                lists.add(efr);
            }
        }
        return lists;
    }
}
