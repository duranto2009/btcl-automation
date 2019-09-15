package nix.application;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import nix.application.office.NIXApplicationOffice;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@TableName("nix_application")
public class NIXApplication{
    @PrimaryKey
    @ColumnName("id")
    long id;

    @ColumnName("client")
    long client;

    @ColumnName("type")
    int type;

    @ColumnName("state")
    int state;

    @ColumnName("demand_note")
    Long demandNote;

    @ColumnName("loop_provider")
    int loopProvider;

    @ColumnName("zone")
    int zone;

    @ColumnName("submission_date")
    long submissionDate;

    @ColumnName("suggested_date")
    long suggestedDate;

    @ColumnName("comment")
    String comment;

    // TODO: 1/9/2019 is skip payment is not used yet
    @ColumnName("skip_payment")
    int skipPayment;

    @ColumnName("created")
    long created;

    @ColumnName("modified")
    long modified;

    @ColumnName("port_type")
    int portType;

    @ColumnName("port_count")
    int portCount;

    @ColumnName("is_forwarded")
    int isForwarded;
    @ColumnName("is_service_started")
    int isServiceStarted;

    @ColumnName("status")
    int status;

    @ColumnName("connection")
    long connection;

    @ColumnName("second_zone")
    int secondZone;

   // @ColumnName("second_zone")
    //int secondZone;
    String stateDescription;
    String color;
    boolean hasPermission = false;

    List<NIXApplicationOffice> nixApplicationOffices ;

   // String comment;
//    String description;

    public NIXApplication desirializer(JsonObject jsonObject) {

        NIXApplication nixApplication = new NIXApplication();
        int portCount =( jsonObject.get("portCount")==null)?-1:jsonObject.get("portCount").getAsInt();
        int loopProvider = jsonObject.get("loopProvider")==null?-1:jsonObject.get("loopProvider").getAsJsonObject().get("ID").getAsInt();
        int portType = (jsonObject.get("portType")==null)?-1:jsonObject.get("portType").getAsJsonObject().get("ID").getAsInt();
        int zone = jsonObject.get("zone")==null?-1:jsonObject.get("zone").getAsJsonObject().get("id").getAsInt();
        //long clientId = jsonObject.get("client").getAsJsonObject().get("ID").getAsLong();
        long clientId = jsonObject.get("client").getAsJsonObject().get("ID")==null?jsonObject.getAsJsonObject("client").
                get("key").getAsLong():jsonObject.get("client").getAsJsonObject().get("ID").getAsLong();

        int skipPayment = jsonObject.get("skipPayment")==null?-1:jsonObject.get("skipPayment").getAsJsonObject().get("ID").getAsInt();
        String comment = jsonObject.get("comment").getAsString();
        String description = jsonObject.get("description")!=null ?jsonObject.get("description").getAsString():"";
        long suggestedDate = jsonObject.get("suggestedDate")==null?0:jsonObject.get("suggestedDate").getAsLong();
        JsonArray jsonArray = (JsonArray) ((jsonObject.get("officeList")!=null)?jsonObject.get("officeList"):null);
        List<NIXApplicationOffice> nixApplicationOffices = new ArrayList<>();
        if(jsonArray !=null){
            for (JsonElement jsonElement1 : jsonArray) {
                JsonObject officeJsonObject = jsonElement1.getAsJsonObject();
                NIXApplicationOffice nixApplicationOffice = new NIXApplicationOffice();
                nixApplicationOffice.setName(officeJsonObject.get("officeName").getAsString());
                nixApplicationOffice.setAddress(officeJsonObject.get("officeAddress").getAsString());
                nixApplicationOffice.setLastModificationTime(System.currentTimeMillis());
                nixApplicationOffice.setCreated(System.currentTimeMillis());
                nixApplicationOffices.add(nixApplicationOffice);
            }
        }
        nixApplication.setLoopProvider(loopProvider);
        nixApplication.setClient(clientId);
        nixApplication.setSuggestedDate(suggestedDate);
        nixApplication.setNixApplicationOffices(nixApplicationOffices);
        nixApplication.setModified(System.currentTimeMillis());
        nixApplication.setSubmissionDate(System.currentTimeMillis());
        nixApplication.setType(1);
        nixApplication.setPortCount(portCount);
        nixApplication.setPortType(portType);
        nixApplication.setComment(comment);
        nixApplication.setSkipPayment(skipPayment);
        nixApplication.setDemandNote(0l);
        nixApplication.setZone(zone);
        return nixApplication;
    }
    public NIXApplication desirializer_custom(JsonObject jsonObject) {

        NIXApplication nixApplication = new NIXApplication();
        long appID =( jsonObject.get("applicationID")==null)?-1:jsonObject.get("applicationID").getAsLong();
        int portCount =( jsonObject.get("portCount")==null)?-1:jsonObject.get("portCount").getAsInt();
        int loopProvider = jsonObject.get("loopProvider").getAsJsonObject().get("ID").getAsInt();
        int zone = jsonObject.get("zone")==null?-1:jsonObject.get("zone").getAsJsonObject().get("id").getAsInt();
        long clientId = jsonObject.get("client").getAsJsonObject().get("ID")==null?jsonObject.getAsJsonObject("client").
                get("key").getAsLong():jsonObject.get("client").getAsJsonObject().get("ID").getAsLong();
        int portType = (jsonObject.get("portType")==null)?-1:jsonObject.get("portType").getAsInt();
        //String comment = jsonObject.get("comment").getAsString();
        //String description = jsonObject.get("description").getAsString();
        long suggestedDate = jsonObject.get("suggestedDate").getAsLong();
        JsonArray jsonArray = (JsonArray) ((jsonObject.get("officeList")!=null)?jsonObject.get("officeList"):null);
        List<NIXApplicationOffice> nixApplicationOffices = new ArrayList<>();
        if(jsonArray !=null){
            for (JsonElement jsonElement1 : jsonArray) {
                JsonObject officeJsonObject = jsonElement1.getAsJsonObject();
                NIXApplicationOffice nixApplicationOffice = new NIXApplicationOffice();
                nixApplicationOffice.setName(officeJsonObject.get("name").getAsString());
                long id = officeJsonObject.get("id")!=null? officeJsonObject.get("id").getAsLong():0;
                nixApplicationOffice.setId(id);
                long application = officeJsonObject.get("application")!=null? officeJsonObject.get("application").getAsLong():0;
                nixApplicationOffice.setApplication(application);
                long history = officeJsonObject.get("history")!=null? officeJsonObject.get("history").getAsLong():0;
                nixApplicationOffice.setHistory(history);
                nixApplicationOffice.setAddress(officeJsonObject.get("address").getAsString());
                nixApplicationOffice.setLastModificationTime(System.currentTimeMillis());
                nixApplicationOffice.setCreated(System.currentTimeMillis());
                nixApplicationOffices.add(nixApplicationOffice);
            }
        }
        nixApplication.setId(appID);
        nixApplication.setLoopProvider(loopProvider);
        nixApplication.setClient(clientId);
        nixApplication.setSuggestedDate(suggestedDate);
        nixApplication.setNixApplicationOffices(nixApplicationOffices);
        nixApplication.setModified(System.currentTimeMillis());
        nixApplication.setSubmissionDate(System.currentTimeMillis());
        nixApplication.setType(1);
        nixApplication.setPortCount(portCount);
        nixApplication.setPortType(portType);
        nixApplication.setComment(comment);
        nixApplication.setDemandNote(0l);
        nixApplication.setZone(zone);
        return nixApplication;
    }

}