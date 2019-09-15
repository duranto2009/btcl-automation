package entity.efr;//package vpn.application.EFR;
//
//import com.google.gson.*;
//import common.ValidationService;
//import inventory.InventoryItem;
//import inventory.InventoryService;
//import lli.Application.Office.Office;
//import lli.Application.Office.OfficeService;
//import lli.Application.newOffice.NewOffice;
//import lli.Application.newOffice.NewOfficeService;
//import util.ServiceDAOFactory;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//
//public class EFRDeserializer implements JsonDeserializer<EFR> {
//
//    private static final int workExpireTime = 3 * 24 * 60 * 60 * 1000;//need to change accordind to expire policy
//
//    InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);
//
//
//    OfficeService officeService = ServiceDAOFactory.getService(OfficeService.class);
//
//
//    NewOfficeService newOfficeService = ServiceDAOFactory.getService(NewOfficeService.class);
//
//    @Override
//    public EFR deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//        return null;
//    }
//
//
//    public ArrayList<EFR> deserialize_custom(JsonElement jsonElements) throws JsonParseException {
//
//        //Receive JSON
//
//        JsonObject jsonObject = jsonElements.getAsJsonObject();
//        ArrayList<EFR> lists = new ArrayList<>();
//        //long appID=Integer.parseInt(String.valueOf(jsonObject.get("applicationID")));
//        JsonArray jsonArray = jsonObject.getAsJsonArray("pops");
//        if (jsonArray != null) {
//            for (JsonElement jsonElement : jsonArray) {
//                EFR efr = new EFR();
//
//
//                JsonObject object = jsonElement.getAsJsonObject();
//
//                JsonElement ID = object.get("id");
//                efr.setId(ID != null ? ID.getAsLong() : 0);
//
//                efr.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
//
//                JsonElement parID = object.get("parentID");
//                efr.setParentEFRID(parID != null ? parID.getAsLong() : 0);
//
//
//                ValidationService.validateNonEmptyID(object, "popID", "POP");
//                JsonElement popID = object.get("popID");
//                efr.setPopID(popID != null ? popID.getAsLong() : 0);
//
//
//                JsonElement bandwidth = object.get("bandwidth");
//                efr.setBandwidth(bandwidth != null ? bandwidth.getAsLong() : 0);
//
//                JsonElement source = object.get("sourceType");
//                efr.setSourceType(source != null ? source.getAsLong() : 0);
//
//                JsonElement sourceName = object.get("source");
//                if (efr.getSourceType() == 1) {
//                    InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(efr.getPopID());
//
//                    efr.setSource(inventoryItem.getName());
//                } else {
//                    efr.setSource(sourceName != null ? sourceName.getAsString() : null);
//
//                }
//
//
//                JsonElement dest = object.get("destinationType");
//                efr.setDestinationType(dest != null ? dest.getAsLong() : 0);
//
//                JsonElement officeid = object.get("officeId") != null ? object.get("officeId") : object.get("officeID");
//                efr.setOfficeID(officeid != null ? officeid.getAsLong() : 0);
//
//
//                if (efr.getDestinationType() == 3) {
//                    try {
//                        Office office = officeService.getOfficeById(efr.getOfficeID()).get(0);
//                        efr.setDestination(office.getOfficeAddress());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    JsonElement destName = object.get("destination");
//                    efr.setDestination(destName != null ? destName.getAsString() : null);
//                }
//
//
//                efr.setQuotationStatus(object.get("quotationStatus") != null ? object.get("quotationStatus").getAsLong() : 0);
//
//                efr.setQuotationDeadline(object.get("quotationDeadline") != null ? object.get("quotationDeadline").getAsLong() : System.currentTimeMillis() + workExpireTime);
////            efr.setQuotationStatus(object.get("quotation")!=null?object.get("quotation").getAsLong():0);//deadline need to do
//
////            ValidationService.validateNumeric(object,"proposedLoopDistance","Length");
//                JsonElement proposedLoopDistance = object.get("proposedLoopDistance");
//                efr.setProposedLoopDistance(proposedLoopDistance != null ? proposedLoopDistance.getAsLong() : 0);
//                JsonElement actualDistance = object.get("actualLoopDistance");
//                efr.setActualLoopDistance(actualDistance != null ? actualDistance.getAsLong() : 0);
//
//                JsonElement loopDistanceIsApproved = object.get("loopDistanceIsApproved");
//                efr.setLoopDistanceIsApproved(loopDistanceIsApproved != null ? loopDistanceIsApproved.getAsLong() : 0);
//
//                JsonElement vendorID = object.get("vendorID");
//                efr.setVendorID(vendorID != null ? vendorID.getAsLong() : 0);
//                JsonElement vendorType = object.get("vendorType");
//                efr.setVendorType(vendorType != null ? vendorType.getAsLong() : 0);
//
////            JsonElement vendorID= object.get("vendorID");
////            efr.setVendorID(vendorID != null ? vendorID.getAsLong() : 0);
//
//                JsonElement workGiven = object.get("workGiven");
//                efr.setWorkGiven(workGiven != null ? workGiven.getAsLong() : 0);
//
//                JsonElement workCompleted = object.get("workCompleted");
//                efr.setWorkCompleted(workCompleted != null ? workCompleted.getAsLong() : 0);
//                JsonElement workDeadline = object.get("workDeadline");
//                efr.setWorkDeadline(workDeadline != null ? workDeadline.getAsLong() : System.currentTimeMillis() + workExpireTime);
//
////            ValidationService.validateNonEmptyID(object, "ofcType", "OFC Type");
//                JsonElement ofc = object.get("ofcType");
//                efr.setOfcType(ofc != null ? ofc.getAsLong() : 0);
//
//                JsonElement state = jsonObject.get("nextState");
//                efr.setState(state != null ? state.getAsLong() : 0);
//
//
//                lists.add(efr);
//
//
//            }
//        }
//
//
//        return lists;
//    }
//
//    public ArrayList<EFR> deserialize_custom_new_local_loop(JsonElement jsonElements) throws JsonParseException {
//
//        //Receive JSON
//
//        JsonObject jsonObject = jsonElements.getAsJsonObject();
//        ArrayList<EFR> lists = new ArrayList<>();
//        //long appID=Integer.parseInt(String.valueOf(jsonObject.get("applicationID")));
//        JsonArray jsonArray = jsonObject.getAsJsonArray("pops");
//        if (jsonArray != null) {
//            for (JsonElement jsonElement : jsonArray) {
//                EFR efr = new EFR();
//
//
//                JsonObject object = jsonElement.getAsJsonObject();
//
//                JsonElement ID = object.get("id");
//                efr.setId(ID != null ? ID.getAsLong() : 0);
//
//                efr.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
//
//                JsonElement parID = object.get("parentID");
//                efr.setParentEFRID(parID != null ? parID.getAsLong() : 0);
//
//
//                ValidationService.validateNonEmptyID(object, "popID", "POP");
//                JsonElement popID = object.get("popID");
//                efr.setPopID(popID != null ? popID.getAsLong() : 0);
//
//                JsonElement source = object.get("sourceType");
//                efr.setSourceType(source != null ? source.getAsLong() : 0);
//
//                JsonElement sourceName = object.get("source");
//                if (efr.getSourceType() == 1) {
//                    InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(efr.getPopID());
//
//                    efr.setSource(inventoryItem.getName());
//                } else {
//                    efr.setSource(sourceName != null ? sourceName.getAsString() : null);
//
//                }
//
//
//                JsonElement dest = object.get("destinationType");
//                efr.setDestinationType(dest != null ? dest.getAsLong() : 0);
//
//                JsonElement officeid = object.get("officeId") != null ? object.get("officeId") : object.get("officeID");
//                efr.setOfficeID(officeid != null ? officeid.getAsLong() : 0);
//
//
//                if (efr.getDestinationType() == 3) {
//                    try {
//                        NewOffice office = newOfficeService.getOfficeById(efr.getOfficeID()).get(0);
//                        efr.setDestination(office.getOfficeAddress());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    JsonElement destName = object.get("destination");
//                    efr.setDestination(destName != null ? destName.getAsString() : null);
//                }
//
//
//                efr.setQuotationStatus(object.get("quotationStatus") != null ? object.get("quotationStatus").getAsLong() : 0);
//
//                efr.setQuotationDeadline(object.get("quotationDeadline") != null ? object.get("quotationDeadline").getAsLong() : System.currentTimeMillis() + workExpireTime);
////            efr.setQuotationStatus(object.get("quotation")!=null?object.get("quotation").getAsLong():0);//deadline need to do
//
////            ValidationService.validateNumeric(object,"proposedLoopDistance","Length");
//                JsonElement proposedLoopDistance = object.get("proposedLoopDistance");
//                efr.setProposedLoopDistance(proposedLoopDistance != null ? proposedLoopDistance.getAsLong() : 0);
//                JsonElement actualDistance = object.get("actualLoopDistance");
//                efr.setActualLoopDistance(actualDistance != null ? actualDistance.getAsLong() : 0);
//
//                JsonElement loopDistanceIsApproved = object.get("loopDistanceIsApproved");
//                efr.setLoopDistanceIsApproved(loopDistanceIsApproved != null ? loopDistanceIsApproved.getAsLong() : 0);
//
//                JsonElement vendorID = object.get("vendorID");
//                efr.setVendorID(vendorID != null ? vendorID.getAsLong() : 0);
//                JsonElement vendorType = object.get("vendorType");
//                efr.setVendorType(vendorType != null ? vendorType.getAsLong() : 0);
//
////            JsonElement vendorID= object.get("vendorID");
////            efr.setVendorID(vendorID != null ? vendorID.getAsLong() : 0);
//
//                JsonElement workGiven = object.get("workGiven");
//                efr.setWorkGiven(workGiven != null ? workGiven.getAsLong() : 0);
//
//                JsonElement workCompleted = object.get("workCompleted");
//                efr.setWorkCompleted(workCompleted != null ? workCompleted.getAsLong() : 0);
//                JsonElement workDeadline = object.get("workDeadline");
//                efr.setWorkDeadline(workDeadline != null ? workDeadline.getAsLong() : System.currentTimeMillis() + workExpireTime);
//
////            ValidationService.validateNonEmptyID(object, "ofcType", "OFC Type");
//                JsonElement ofc = object.get("ofcType");
//                efr.setOfcType(ofc != null ? ofc.getAsLong() : 0);
//
//                JsonElement state = jsonObject.get("nextState");
//                efr.setState(state != null ? state.getAsLong() : 0);
//
//
//                lists.add(efr);
//
//
//            }
//        }
//
//
//        return lists;
//    }
//
//}
