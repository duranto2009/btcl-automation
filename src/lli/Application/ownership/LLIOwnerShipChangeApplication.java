package lli.Application.ownership;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.RequestFailureException;
import lli.Application.FlowConnectionManager.LLIConnection;
import lombok.Data;
import util.ServiceDAOFactory;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName("lli_owner_change_application")
public class LLIOwnerShipChangeApplication {
    @PrimaryKey
    @ColumnName("id")
    long id;

    @ColumnName("submission_date")
    long submissionDate;
    @ColumnName("source_client")
    long srcClient;
    @ColumnName("destination_client")
    long dstClient;

    @ColumnName("state")
    int state;

    @ColumnName("status")
    int status;

    @ColumnName("demand_note")
    Long demandNote;

    @ColumnName("comment")
    String comment;
    @ColumnName("description")
    String description;

    @ColumnName("is_forwarded")
    int isForwarded;

    @ColumnName("suggested_date")
    long suggestedDate;

    @ColumnName("skip_payment")
    long skipPayment = 0;

    @ColumnName("type")
    int type;

    @ColumnName("zone")
    long zone;

    String[] documents;
    String stateDescription;
    String color;
    boolean hasPermission = false;

    public static LLIOwnerShipChangeApplication deserialize(JsonObject jsonObject) {
        LLIOwnerShipChangeApplication lliOwnerShipChangeApplication = new LLIOwnerShipChangeApplication();

        long srcClient =( jsonObject.getAsJsonObject("srcClient")!=null)?jsonObject.getAsJsonObject("srcClient").get("ID").getAsLong():-1;
        long dstClient =( jsonObject.getAsJsonObject("dstClient")!=null)?jsonObject.getAsJsonObject("dstClient").get("ID").getAsLong():-1;


        if(srcClient == -1) {
            throw new RequestFailureException("Source Client Must be Selected");
        }
        if(dstClient == -1) {
            throw new RequestFailureException("Destination Client Must be Selected");
        }

        String comment = jsonObject.get("comment")!=null ? jsonObject.get("comment").getAsString() : "";
        String description = jsonObject.get("description")!=null ? jsonObject.get("description").getAsString() : "";
        long suggestedDate = jsonObject.get("suggestedDate")==null? 0 : jsonObject.get("suggestedDate").getAsLong();
        lliOwnerShipChangeApplication.setSrcClient(srcClient);
        lliOwnerShipChangeApplication.setDstClient(dstClient);
        lliOwnerShipChangeApplication.setSuggestedDate(suggestedDate);
        lliOwnerShipChangeApplication.setComment(comment);
        lliOwnerShipChangeApplication.setDescription(description);
        lliOwnerShipChangeApplication.setSubmissionDate(System.currentTimeMillis());
        lliOwnerShipChangeApplication.setDemandNote(0L);
        return lliOwnerShipChangeApplication;
    }
}
