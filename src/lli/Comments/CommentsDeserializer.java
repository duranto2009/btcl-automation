package lli.Comments;

import com.google.gson.*;
import common.ValidationService;
import login.LoginDTO;

import java.lang.reflect.Type;

public class CommentsDeserializer implements JsonDeserializer<Comments> {
    @Override
    public Comments deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }

    public Comments deserialize_custom(JsonElement jsonElements, LoginDTO loginDTO) throws JsonParseException {

        //Receive JSON

        JsonObject jsonObject = jsonElements.getAsJsonObject();
        Comments comments = new Comments();
        //long appID=Integer.parseInt(String.valueOf(jsonObject.get("applicationID")));

        comments.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);


        comments.setUserID(loginDTO.getUserID());
        comments.setStateID(jsonObject.get("nextState") != null ? jsonObject.get("nextState").getAsLong() : 0);
        comments.setSequenceID(jsonObject.get("sequenceID") != null ? jsonObject.get("sequenceID").getAsLong() : 1);

        ValidationService.validateNonEmptyString(jsonObject, "comment", "Comment");


        comments.setComments(jsonObject.get("comment") != null ? String.valueOf(jsonObject.get("comment")) : null);
        comments.setSubmissionDate(java.lang.System.currentTimeMillis());


        //parent ifr id need to be incorporated


        //Deserialize Common LLI Application

//        ifr.parentIFRID(jsonObject.get("client").getAsJsonObject().get("ID").getAsLong());
//        ifr.setPopID(jsonObject.get("popID") != null ? jsonObject.get("popID").getAsLong() : null);
//        ifr.setRequestedBW(jsonObject.get("requestedBW") != null ? jsonObject.get("requestedBW").getAsLong() : 0);
//        ifr.setRequestedBW(jsonObject.get("availableBW") != null ? jsonObject.get("availableBW").getAsLong() : 0);
//        ifr.setRequestedBW(jsonObject.get("selectedBW") != null ? jsonObject.get("selectedBW").getAsLong() : 0);
//        ifr.setRequestedBW(jsonObject.get("priority") != null ? jsonObject.get("priority").getAsLong() : 0);
//        ifr.setRequestedBW(jsonObject.get("priority") != null ? jsonObject.get("priority").getAsLong() : 0);

        return comments;
    }

    public RevisedComment deserializeReviseComment(JsonElement jsonElements, LoginDTO loginDTO) throws JsonParseException {
        JsonObject jsonObject = jsonElements.getAsJsonObject();
        RevisedComment comment = new RevisedComment();

        comment.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
        comment.setUserID(loginDTO.getUserID());
        comment.setStateID(jsonObject.get("nextState") != null ? jsonObject.get("nextState").getAsLong() : 0);
        comment.setSequenceID(jsonObject.get("sequenceID") != null ? jsonObject.get("sequenceID").getAsLong() : 1);

        comment.setComments(jsonObject.get("comment") != null ? String.valueOf(jsonObject.get("comment")) : "");
        comment.setSubmissionDate(java.lang.System.currentTimeMillis());
        return comment;
    }
}
