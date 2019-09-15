package nix.ifr;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.ValidationService;

import java.util.ArrayList;

public class NIXIFRDeserializer {
    public ArrayList<NIXIFR> deserialize_custom(JsonElement jsonElements) {
        JsonObject jsonObject = jsonElements.getAsJsonObject();
        ArrayList<NIXIFR>lists=new ArrayList<>();
        JsonArray jsonArray=jsonObject.getAsJsonArray("pops").size()>0
                ?jsonObject.getAsJsonArray("pops")
                :jsonObject.getAsJsonArray("ifr");
        if(jsonArray!=null) {
            for (JsonElement jsonElement : jsonArray) {
                NIXIFR ifr = new NIXIFR();
                JsonObject object = jsonElement.getAsJsonObject();
                JsonElement ID = object.get("id");
                ifr.setId(ID != null ? ID.getAsLong() : 0);
                ifr.setApplication(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
                if(object.get("popID")==null){
                    ValidationService.validateNonEmptyID(object.getAsJsonObject("pop"), "ID", "POP");
                }
                else ValidationService.validateNonEmptyID(object, "popID", "POP");
                JsonElement popID = object.get("popID")==null?object.getAsJsonObject("pop").get("ID"):object.get("popID");
                ifr.setPop(popID != null ? popID.getAsLong() : 0);
                JsonElement isReply = object.get("isReplied");
                ifr.setReplied(isReply != null ? isReply.getAsLong() : 0);
                JsonElement submissionDate = object.get("submissionDate");
                ifr.setSubmissionDate(submissionDate != null ? submissionDate.getAsLong() :
                        java.lang.System.currentTimeMillis());
                JsonElement isSelected = object.get("selected");
                ifr.setSelected(isSelected != null ? isSelected.getAsInt() : 0);
                long officeId = -1;
                if (object.get("officeId") != null) {
                    ValidationService.validateNonEmptyID(object, "officeId", "Office");
                    officeId = object.get("officeId").getAsLong();
                } else if(object.get("officeID") != null){
                    ValidationService.validateNonEmptyID(object, "officeID", "Office");
                    officeId = object.get("officeID").getAsLong();
                } else {
                    ValidationService.validateNonEmptyID(object.getAsJsonObject("office"), "id", "Office");
                    officeId = object.getAsJsonObject("office").get("id").getAsLong();
                }
                ifr.setOffice(officeId);
                lists.add(ifr);
            }
        }
        return lists;
    }

    public ArrayList<NIXIFR> deserialize_custom_ifr_update(JsonElement jsonElements) {
        //Receive JSON
        JsonObject jsonObject = jsonElements.getAsJsonObject();
        ArrayList<NIXIFR>lists=new ArrayList<>();
        //long appID=Integer.parseInt(String.valueOf(jsonObject.get("applicationID")));
        JsonArray jsonArray=jsonObject.getAsJsonArray("ifr");
        for (JsonElement jsonElement:jsonArray)
        {
            NIXIFR ifr=new NIXIFR();
            JsonObject object=jsonElement.getAsJsonObject();
            JsonElement ID= object.get("id");
            ifr.setId(ID != null ? ID.getAsLong() : 0);
            ifr.setApplication(jsonObject.get("applicationID") != null
                    ? jsonObject.get("applicationID").getAsLong() : 0);
            JsonElement popID= object.getAsJsonObject("pop").get("ID");
            ifr.setPop(popID != null ? popID.getAsLong() : 0);
            //JsonElement isReply=object.get("isReplied");
            //ifr.setReplied(isReply != null ? isReply.getAsLong() : 0);
            JsonElement submissionDate=object.get("submissionDate");
            ifr.setSubmissionDate(submissionDate != null ? submissionDate.getAsLong() : java.lang.System.currentTimeMillis());
            JsonElement isSelected=object.get("isSelected")!=null?object.get("isSelected"):object.get("selected");
            ifr.setSelected(isSelected != null ? isSelected.getAsInt() : 0);
            ifr.setOffice(object.getAsJsonObject("office").get("id") != null
                    ? object.getAsJsonObject("office").get("id").getAsLong() : 0 );
            lists.add(ifr);
        }


        return lists;
    }
}
