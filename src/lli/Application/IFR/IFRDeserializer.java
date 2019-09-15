package lli.Application.IFR;

import com.google.gson.*;
import common.ValidationService;
import lli.Application.LLIApplicationService;
import lli.Application.Office.OfficeService;
import util.ServiceDAOFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class IFRDeserializer implements JsonDeserializer<IFR> {

    LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);
    OfficeService officeService = ServiceDAOFactory.getService(OfficeService.class);
    @Override
    public IFR deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        //Receive JSON
        IFR ifr=new IFR();
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        //Deserialize Common LLI Application
        ifr.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
//        ifr.parentIFRID(jsonObject.get("client").getAsJsonObject().get("ID").getAsLong());
        ifr.setPopID(jsonObject.get("popID") != null ? jsonObject.get("popID").getAsLong() : null);
        ifr.setRequestedBW(jsonObject.get("requestedBW") != null ? jsonObject.get("requestedBW").getAsLong() : 0);
        ifr.setRequestedBW(jsonObject.get("availableBW") != null ? jsonObject.get("availableBW").getAsLong() : 0);
        ifr.setRequestedBW(jsonObject.get("selectedBW") != null ? jsonObject.get("selectedBW").getAsLong() : 0);
        ifr.setRequestedBW(jsonObject.get("priority") != null ? jsonObject.get("priority").getAsLong() : 0);
        ifr.setRequestedBW(jsonObject.get("priority") != null ? jsonObject.get("priority").getAsLong() : 0);

        return null;
    }

    public ArrayList<IFR> deserialize_custom(JsonElement jsonElements) throws JsonParseException {

        //Receive JSON

        //JsonArray jsonArray1=jsonElements.getAsJsonArray();
        JsonObject jsonObject = jsonElements.getAsJsonObject();

        ArrayList<IFR>lists=new ArrayList<>();
        //long appID=Integer.parseInt(String.valueOf(jsonObject.get("applicationID")));
        JsonArray jsonArray=jsonObject.getAsJsonArray("pops").size()>0
                ?jsonObject.getAsJsonArray("pops")
                :jsonObject.getAsJsonArray("ifr");
        if(jsonArray!=null) {
            for (JsonElement jsonElement : jsonArray) {
                IFR ifr = new IFR();


                JsonObject object = jsonElement.getAsJsonObject();


//            start: validate


//            end: validate

                JsonElement ID = object.get("id");
                ifr.setId(ID != null ? ID.getAsLong() : 0);


                ifr.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);

                JsonElement parID = object.get("parentID");
                ifr.setParentIFRID(parID != null ? parID.getAsLong() : 0);
                ValidationService.validateNonEmptyID(object, "popID", "POP");
                JsonElement popID = object.get("popID");
                ifr.setPopID(popID != null ? popID.getAsLong() : 0);
                JsonElement requsetedBW = object.get("requestedBW");
                ifr.setRequestedBW(requsetedBW != null ? requsetedBW.getAsLong() : 0);
                JsonElement availableBW = object.get("availableBW");
                ifr.setAvailableBW(availableBW != null ? availableBW.getAsLong() : 0);
                JsonElement selectedBW = object.get("selectedBW");
                ifr.setSelectedBW(selectedBW != null ? selectedBW.getAsLong() : 0);
                ValidationService.validateNonEmptyString(object, "priority", "Priority");
                ifr.setPriority(object.get("priority") != null ? object.get("priority").getAsLong() : 0);
                JsonElement isReply = object.get("isReplied");
                ifr.setIsReplied(isReply != null ? isReply.getAsLong() : 0);
                JsonElement serverLoc = object.get("serverRoomLocation");
                ifr.setServerRoomLocationID(serverLoc != null ? serverLoc.getAsLong() : 0);
                JsonElement submissionDate = object.get("submissionDate");
                ifr.setSubmissionDate(submissionDate != null ? submissionDate.getAsLong() : java.lang.System.currentTimeMillis());
                JsonElement isSelected = object.get("isSelected");
                ifr.setIsSelected(isSelected != null ? isSelected.getAsInt() : 0);
                JsonElement state = jsonObject.get("nextState");
                ifr.setState(state != null ? state.getAsInt() : 0);
                if (object.get("officeId") != null) {
                    ValidationService.validateNonEmptyID(object, "officeId", "Office");
                } else {
                    ValidationService.validateNonEmptyID(object, "officeID", "Office");
                }

                ifr.setOfficeID(object.get("officeId") != null ?
                        object.get("officeId").getAsLong() : object.get("officeID").getAsLong());
                lists.add(ifr);

            }
        }

        return lists;
    }

    public ArrayList<IFR> deserialize_custom_local_loop(JsonElement jsonElements) throws JsonParseException {

        //Receive JSON

        //JsonArray jsonArray1=jsonElements.getAsJsonArray();
        JsonObject jsonObject = jsonElements.getAsJsonObject();

        ArrayList<IFR>lists=new ArrayList<>();
        //long appID=Integer.parseInt(String.valueOf(jsonObject.get("applicationID")));
        JsonArray jsonArray=jsonObject.getAsJsonArray("pops").size()>0
                ?jsonObject.getAsJsonArray("pops")
                :jsonObject.getAsJsonArray("ifr");
        if(jsonArray!=null) {
            for (JsonElement jsonElement : jsonArray) {
                IFR ifr = new IFR();
                JsonObject object = jsonElement.getAsJsonObject();
                JsonElement ID = object.get("id");
                ifr.setId(ID != null ? ID.getAsLong() : 0);

                ifr.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);

                JsonElement parID = object.get("parentID");
                ifr.setParentIFRID(parID != null ? parID.getAsLong() : 0);
                ValidationService.validateNonEmptyID(object, "popID", "POP");
                JsonElement popID = object.get("popID");
                ifr.setPopID(popID != null ? popID.getAsLong() : 0);

                JsonElement isReply = object.get("isReplied");
                ifr.setIsReplied(isReply != null ? isReply.getAsLong() : 0);
                JsonElement serverLoc = object.get("serverRoomLocation");
                ifr.setServerRoomLocationID(serverLoc != null ? serverLoc.getAsLong() : 0);
                JsonElement submissionDate = object.get("submissionDate");
                ifr.setSubmissionDate(submissionDate != null ? submissionDate.getAsLong() : java.lang.System.currentTimeMillis());
                JsonElement isSelected = object.get("isSelected");
                ifr.setIsSelected(isSelected != null ? isSelected.getAsInt() : 0);
                JsonElement state = jsonObject.get("nextState");
                ifr.setState(state != null ? state.getAsInt() : 0);


                if (object.get("officeId") != null) {
                    ValidationService.validateNonEmptyID(object, "officeId", "Office");
                } else {
                    ValidationService.validateNonEmptyID(object, "officeID", "Office");
                }

                ifr.setOfficeID(object.get("officeId") != null ?
                        object.get("officeId").getAsLong() : object.get("officeID").getAsLong());
                lists.add(ifr);

            }
        }

        return lists;
    }

    public ArrayList<IFR> deserialize_custom_ifr_update(JsonElement jsonElements) throws JsonParseException {

        //Receive JSON


        JsonObject jsonObject = jsonElements.getAsJsonObject();
        ArrayList<IFR>lists=new ArrayList<>();
        //long appID=Integer.parseInt(String.valueOf(jsonObject.get("applicationID")));
        JsonArray jsonArray=jsonObject.getAsJsonArray("ifr");
        for (JsonElement jsonElement:jsonArray)
        {
            IFR ifr=new IFR();
            JsonObject object=jsonElement.getAsJsonObject();
            JsonElement ID= object.get("id");
            ifr.setId(ID != null ? ID.getAsLong() : 0);
            ifr.setApplicationID(jsonObject.get("applicationID") != null
                    ? jsonObject.get("applicationID").getAsLong() : 0);
            JsonElement parID= object.get("parentID");
            ifr.setParentIFRID(parID != null ? parID.getAsLong() : 0);
            JsonElement popID= object.get("popID");
            ifr.setPopID(popID != null ? popID.getAsLong() : 0);
            JsonElement requsetedBW=object.get("requestedBW");
            ifr.setRequestedBW(requsetedBW !=null?requsetedBW.getAsLong():0);
            JsonElement availableBW=object.get("availableBW");
            ifr.setAvailableBW(availableBW !=null?availableBW.getAsLong():0);
            JsonElement selectedBW=object.get("selectedBW");
            ifr.setSelectedBW(selectedBW !=null?selectedBW.getAsLong():0);
            ifr.setPriority(object.get("priority")!=null?object.get("priority").getAsLong():0);
            JsonElement isReply=object.get("isReplied");
            ifr.setIsReplied(isReply != null ? isReply.getAsLong() : 0);
            JsonElement serverLoc= object.get("serverRoomLocation");
            ifr.setServerRoomLocationID(serverLoc != null ? serverLoc.getAsLong() : 0);
            JsonElement submissionDate=object.get("submissionDate");
            ifr.setSubmissionDate(submissionDate != null ? submissionDate.getAsLong() : java.lang.System.currentTimeMillis());
            JsonElement isSelected=object.get("isSelected");
            ifr.setIsSelected(isSelected != null ? isSelected.getAsInt() : 0);
            JsonElement state=jsonObject.get("nextState");
            ifr.setState(state != null ? state.getAsInt() : 0);
            ifr.setOfficeID(object.get("officeId") != null
                    ? object.get("officeId").getAsLong() : object.get("officeID").getAsLong() );
            lists.add(ifr);
        }


        return lists;
    }
    public ArrayList<IFR> deserialize_ifr_update_for_new_local_loop(JsonElement jsonElements) throws JsonParseException {

        //Receive JSON


        JsonObject jsonObject = jsonElements.getAsJsonObject();
        ArrayList<IFR>lists=new ArrayList<>();
        JsonArray jsonArray=jsonObject.getAsJsonArray("ifr");
        for (JsonElement jsonElement:jsonArray)
        {
            IFR ifr=new IFR();
            JsonObject object=jsonElement.getAsJsonObject();
            JsonElement ID= object.get("id");
            ifr.setId(ID != null ? ID.getAsLong() : 0);
            ifr.setApplicationID(jsonObject.get("applicationID") != null
                    ? jsonObject.get("applicationID").getAsLong() : 0);
            JsonElement parID= object.get("parentID");
            ifr.setParentIFRID(parID != null ? parID.getAsLong() : 0);
            JsonElement popID= object.get("popID");
            ifr.setPopID(popID != null ? popID.getAsLong() : 0);
            JsonElement isReply=object.get("isReplied");
            ifr.setIsReplied(isReply != null ? isReply.getAsLong() : 0);
            JsonElement serverLoc= object.get("serverRoomLocation");
            ifr.setServerRoomLocationID(serverLoc != null ? serverLoc.getAsLong() : 0);
            JsonElement submissionDate=object.get("submissionDate");
            ifr.setSubmissionDate(submissionDate != null ? submissionDate.getAsLong() : java.lang.System.currentTimeMillis());
            JsonElement isSelected=object.get("isSelected");
            ifr.setIsSelected(isSelected != null ? isSelected.getAsInt() : 0);
            JsonElement state=jsonObject.get("nextState");
            ifr.setState(state != null ? state.getAsInt() : 0);
            ifr.setOfficeID(object.get("officeId") != null
                    ? object.get("officeId").getAsLong() : object.get("officeID").getAsLong() );
            lists.add(ifr);
        }

        return lists;
    }


    public IFR deserialize_additoinal_ip(JsonElement jsonElement) {
        IFR ifr = new IFR();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement ID = jsonObject.get("id");
        ifr.setId(ID != null ? ID.getAsLong() : 0);
        ifr.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
        JsonElement parID = jsonObject.get("parentID");
        ifr.setParentIFRID(parID != null ? parID.getAsLong() : 0);
        JsonElement isReply = jsonObject.get("isReplied");
        ifr.setIsReplied(isReply != null ? isReply.getAsLong() : 0);
        JsonElement serverLoc = jsonObject.get("serverRoomLocation");
        ifr.setServerRoomLocationID(serverLoc != null ? serverLoc.getAsLong() : 0);
        JsonElement submissionDate = jsonObject.get("submissionDate");
        ifr.setSubmissionDate(submissionDate != null ? submissionDate.getAsLong() : java.lang.System.currentTimeMillis());
        JsonElement isSelected = jsonObject.get("isSelected");
        ifr.setIsSelected(isSelected != null ? isSelected.getAsInt() : 0);
        JsonElement state = jsonObject.get("nextState");
        ifr.setState(state != null ? state.getAsInt() : 0);
        return ifr;
    }
}


