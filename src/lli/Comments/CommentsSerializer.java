package lli.Comments;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class CommentsSerializer implements JsonSerializer {
    @Override
    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    public static JsonObject getCommonPart(Comments comments, JsonObject jsonObject, JsonSerializationContext context) {
        //Serialize Common LLI Application


        jsonObject.addProperty("id",comments.getId());
        jsonObject.addProperty("applicationID", comments.getApplicationID());
        jsonObject.addProperty("stateID",comments.getStateID());
        jsonObject.addProperty("sequenceID",comments.getSequenceID());
        jsonObject.addProperty("comment",comments.getComments());
        jsonObject.addProperty("submissionDate",comments.getSubmissionDate());
        return jsonObject;
    }
}
