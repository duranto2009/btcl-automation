package upstream.flowUtils;

import application.ApplicationType;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.uwyn.jhighlight.fastutil.Hash;
import common.ModuleConstants;
import entity.comment.Comment;
import entity.comment.CommentService;
import flow.FlowService;
import flow.entity.FlowState;
import global.GlobalService;
import login.LoginDTO;
import requestMapping.Service;
import upstream.application.UpstreamApplication;
import upstream.application.UpstreamApplicationService;
import upstream.contract.UpstreamContract;
import upstream.contract.UpstreamContractService;
import upstream.inventory.UpstreamInventoryItem;
import upstream.vendor.UpstreamVendor;
import util.DateUtils;
import util.ServiceDAOFactory;
import util.TimeConverter;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class UpstreamFlowDataBuilder {

    @Service
    private UpstreamApplicationService upstreamApplicationService;
    @Service
    private UpstreamContractService upstreamContractService;

    @Service
    private GlobalService globalService;

    @Service
    private CommentService commentService;

    public JsonObject getCommonPart_new(UpstreamApplication upstreamApplication, JsonObject jsonObject, LoginDTO loginDTO) {

        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();

        try {
            jsonObject = buildBasicData(jsonObject, upstreamApplication, loginDTO);

            jsonArray = buildUILabel(jsonObject, upstreamApplication);
            jsonObject.add("formElements", jsonArray);

            JsonArray actionArray = getActions(upstreamApplication, loginDTO, gson);
            jsonObject.add("action", actionArray);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public JsonArray getActions(UpstreamApplication upstreamApplication, LoginDTO loginDTO, Gson gson) {
        List<FlowState> flowStates = upstreamApplicationService.getActionList((int) upstreamApplication.getState(), (int) loginDTO.getRoleID());

        JsonArray actionArray = new JsonArray();
        for (FlowState flowState : flowStates) {
            JsonElement action = gson.toJsonTree(flowState);
            actionArray.add(action);
        }

        return actionArray;
    }

    public JsonObject buildBasicData(JsonObject jsonObject, UpstreamApplication upstreamApplication, LoginDTO loginDTO) {

        try {
            Gson gson = new Gson();

            //TODO- not sure
            String color = ServiceDAOFactory.getService(FlowService.class).getStateById((int) upstreamApplication.getState()).getColor();

            upstreamApplication.setStateColor(color);

            jsonObject.addProperty("moduleId", ModuleConstants.Module_ID_UPSTREAM);
            jsonObject.add("applicationObject", gson.toJsonTree(upstreamApplication));
            if (upstreamApplication.getContractId() > 0) {
                UpstreamContract contract = upstreamContractService.getContractByContractId(upstreamApplication.getContractId());
                jsonObject.add("contractObject", gson.toJsonTree(contract));
                jsonObject.add("previousApplicationObject", gson.toJsonTree(upstreamApplicationService.getApplicationByApplicationId(contract.getApplicationId())));
            }

            //comment object build
            List<Comment> comments = commentService.getCommentByEntityId(ModuleConstants.Module_ID_UPSTREAM, upstreamApplication.getApplicationId());
            if (!comments.isEmpty()) {
                jsonObject.add("comments", gson.toJsonTree(comments));
            }

            //TODO- not sure
            FlowState flowState = upstreamApplicationService.getCurrentState((int) upstreamApplication.getState());

            JsonElement jsonElement = gson.toJsonTree(flowState);
            //TODO- not sure
            JsonObject flowStateObject = (JsonObject) jsonElement;
            jsonObject.add("state", flowStateObject);

            if (upstreamApplication.getOtc() > 0) jsonObject.addProperty("otc", upstreamApplication.getOtc());
            if (upstreamApplication.getBandwidthPrice() > 0)
                jsonObject.addProperty("bandwidthPrice", upstreamApplication.getBandwidthPrice());
            if (upstreamApplication.getMrc() > 0) jsonObject.addProperty("mrc", upstreamApplication.getMrc());

            jsonObject.addProperty("color", color);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    public JsonArray buildUILabel(JsonObject jsonObject, UpstreamApplication upstreamApplication) throws Exception {
        JsonArray jsonArray = new JsonArray();
        FlowState flowState = upstreamApplicationService.getCurrentState((int) upstreamApplication.getState());

        jsonArray.add(makeJsonObjectFromValues("Application Type", upstreamApplication.getApplicationType().getApplicationTypeName()));
        jsonArray.add(makeJsonObjectFromValues("Application Id", Long.toString(upstreamApplication.getApplicationId())));
        jsonArray.add(makeJsonObjectFromValues("Type of Bandwidth",
                globalService.findByPK(UpstreamInventoryItem.class, upstreamApplication.getTypeOfBandwidthId()).getItemName()));


        if (
                upstreamApplication.getApplicationType() == ApplicationType.UPSTREAM_UPGRADE ||
                        upstreamApplication.getApplicationType() == ApplicationType.UPSTREAM_DOWNGRADE
        ) {
            jsonArray.add(makeJsonObjectFromValues("Previous Bandwidth Capacity(GB)", Double.toString(upstreamContractService.getContractByContractId(upstreamApplication.getContractId()).getBandwidthCapacity())));

        }
        jsonArray.add(makeJsonObjectFromValues("Current Bandwidth Capacity(GB)", Double.toString(upstreamApplication.getBandwidthCapacity())));

        jsonArray.add(makeJsonObjectFromValues("Media Type",
                globalService.findByPK(UpstreamInventoryItem.class, upstreamApplication.getMediaId()).getItemName()));
        jsonArray.add(makeJsonObjectFromValues("BTCL Service Location",
                globalService.findByPK(UpstreamInventoryItem.class, upstreamApplication.getBtclServiceLocationId()).getItemName()));
        jsonArray.add(makeJsonObjectFromValues("Provider Location",
                globalService.findByPK(UpstreamInventoryItem.class, upstreamApplication.getProviderLocationId()).getItemName()));

        if (upstreamApplication.getSelectedProviderId() != 0) {
            jsonArray.add(makeJsonObjectFromValues("Selected Vendor",
                    globalService.findByPK(UpstreamInventoryItem.class, upstreamApplication.getSelectedProviderId()).getItemName())
//                    globalService.findByPK(UpstreamVendor.class, upstreamApplication.getSelectedProviderId()).getVendorName())

            );
        }

        if (upstreamApplication.getBandwidthPrice() != 0) {
            jsonArray.add(makeJsonObjectFromValues("Bandwidth Price", Double.toString(upstreamApplication.getBandwidthPrice())));
        }

        if (upstreamApplication.getOtc() != 0) {
            jsonArray.add(makeJsonObjectFromValues("OTC", Double.toString(upstreamApplication.getOtc())));
        }

        if (upstreamApplication.getMrc() != 0) {
            jsonArray.add(makeJsonObjectFromValues("MRC", Double.toString(upstreamApplication.getMrc())));
        }

        if (upstreamApplication.getContractDuration() != 0) {
//            Calendar c = Calendar.getInstance();
////Set time in milliseconds
//            c.setTimeInMillis(Double.valueOf(upstreamApplication.getContractDuration()).longValue());
//            int mYear = c.get(Calendar.YEAR);
//            int mMonth = c.get(Calendar.MONTH);
//            int mDay = c.get(Calendar.DAY_OF_MONTH);
//            int hr = c.get(Calendar.HOUR);
//            int min = c.get(Calendar.MINUTE);
//            int sec = c.get(Calendar.SECOND);
            long duration = Double.valueOf(upstreamApplication.getContractDuration()).longValue();
//            Map<TimeUnit, Long> time = computeDiff(new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()+duration));
            String durationString = convertMillisecondsToYearMonthDay(duration);

            jsonArray.add(makeJsonObjectFromValues("Contract Duration ",
//                    Double.toString(upstreamApplication.getContractDuration())
//                    TimeConverter.getDateTimeStringByMillisecAndDateFormat(Double.valueOf(upstreamApplication.getContractDuration()).longValue(), "dd-MM-yyyy")
                    durationString
            ));
        }

        if (upstreamApplication.getSrfDate() != 0) {
            jsonArray.add(makeJsonObjectFromValues("SRF Date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(upstreamApplication.getSrfDate(), "dd-MM-yyyy")));
        }

        if (upstreamApplication.getCircuitInfoLink() != null) {
            jsonArray.add(makeJsonObjectFromValues("Circuit Info Link", upstreamApplication.getCircuitInfoLink()));
        }

        if (upstreamApplication.getApplicationDate() != 0) {
            jsonArray.add(makeJsonObjectFromValues("Application Date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(upstreamApplication.getApplicationDate(), "dd-MM-yyyy")));
        }

        jsonArray.add(makeJsonObjectFromValues("Application Status", flowState.getViewDescription()));

        return jsonArray;

    }


    public JsonObject makeJsonObjectFromValues(String key, String value) {
        JsonObject object = new JsonObject();
        object.addProperty("Label", key);
        if (value.contains("\"")) {
            object.addProperty("Value", value.replaceAll("\"", " "));
        } else {
            object.addProperty("Value", value);
        }
        return object;
    }

    public static Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);

        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long milliesRest = diffInMillies;
        for ( TimeUnit unit : units ) {
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit,diff);
        }
        return result;
    }

    public static String convertMillisecondsToYearMonthDay(long time){
        String durationString = "";
        long y = 1L * 365L * 24L * 60L * 60L * 1000L;
        long m = 30L * 24L * 60L * 60L * 1000L ;
        long d = 24L * 60L * 60L * 1000L ;
        if(time / y> 0){
            durationString += time / y + " Years ";
            time = time % y;
        }

        if( time / m > 0){
            durationString += " " ;
            durationString += time / m + " Months";
            time = time %m;
        }

        if(time / d > 0){
            durationString += " ";
            durationString += time / d + " Days";
            time = time % d;
        }

        return durationString;
    }
}
