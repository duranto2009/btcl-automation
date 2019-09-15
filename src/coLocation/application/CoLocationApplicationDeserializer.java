package coLocation.application;

import com.google.gson.*;
import common.RequestFailureException;
import login.LoginDTO;
import util.ServiceDAOFactory;

import java.lang.reflect.Type;

public class CoLocationApplicationDeserializer implements JsonDeserializer<CoLocationApplicationDTO> {

    CoLocationApplicationService coLocationApplicationService = ServiceDAOFactory.getService(CoLocationApplicationService.class);

    @Override
    public CoLocationApplicationDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }


    public CoLocationApplicationDTO deserialize_custom(JsonElement jsonElement, LoginDTO loginDTO) throws JsonParseException {
        CoLocationApplicationDTO coLocationApplicationDTO = new CoLocationApplicationDTO();

        JsonObject coLocationApplicationJsonObject = jsonElement.getAsJsonObject();

        //validate the application
        validate(coLocationApplicationJsonObject);

        //set connection ID if applicable
        Integer connectionID = (Integer) parseFromJsonObject(coLocationApplicationJsonObject, "connectionID", Integer.class);
        coLocationApplicationDTO.setConnectionId(connectionID==null?0:connectionID);


        //get all value and set in the DTO
        if(loginDTO.getUserID()>0){
            coLocationApplicationDTO.setUserID(loginDTO.getUserID());
        }

        coLocationApplicationDTO.setClientID(coLocationApplicationJsonObject.get("client").getAsJsonObject().get("key").getAsLong());//get client
//        coLocationApplicationDTO.setConnectionType(coLocationApplicationJsonObject.get("connectionType")!=null?coLocationApplicationJsonObject.get("connectionType").getAsInt():coLocationApplicationJsonObject.get("connectionType").getAsJsonObject().get("id").getAsInt());//get connectionType
        coLocationApplicationDTO.setConnectionType(coLocationApplicationJsonObject.get("connectionType").getAsJsonObject().get("id")!=null?coLocationApplicationJsonObject.get("connectionType").getAsJsonObject().get("id").getAsInt():coLocationApplicationJsonObject.get("connectionType").getAsInt());

        boolean isSkipPayment= coLocationApplicationJsonObject.get("skipPayment")!=null?coLocationApplicationJsonObject.get("skipPayment").isJsonObject():false;
        if(isSkipPayment)coLocationApplicationDTO.setSkipPayment(coLocationApplicationJsonObject.get("skipPayment").getAsJsonObject().get("id").getAsInt());


        coLocationApplicationDTO.setFiberNeeded(coLocationApplicationJsonObject.get("fiberNeeded").getAsBoolean()); // is fiber needed
        if (coLocationApplicationDTO.isFiberNeeded()) {
            boolean fiberType= coLocationApplicationJsonObject.get("fiberType").isJsonObject();
            if(fiberType){
                coLocationApplicationDTO.setFiberType(coLocationApplicationJsonObject.get("fiberType").getAsJsonObject().get("id").getAsInt()
                );
            }else{
                coLocationApplicationDTO.setFiberType(coLocationApplicationJsonObject.get("fiberType").getAsInt());
            }

//            coLocationApplicationDTO.setFiberCore(coLocationApplicationJsonObject.get("fiberCore").getAsJsonObject().get("id").getAsInt());
            coLocationApplicationDTO.setFiberCore(coLocationApplicationJsonObject.get("fiberCore").getAsInt());
        }

        coLocationApplicationDTO.setPowerNeeded(coLocationApplicationJsonObject.get("powerNeeded").getAsBoolean());//getpowerneeded
        if (coLocationApplicationDTO.isPowerNeeded()) {
            coLocationApplicationDTO.setPowerAmount(coLocationApplicationJsonObject.get("powerAmount").getAsInt());//get power amount
            boolean powerType= coLocationApplicationJsonObject.get("powerType").isJsonObject();
            if(powerType){
                coLocationApplicationDTO.setPowerType(coLocationApplicationJsonObject.get("powerType").getAsJsonObject().get("id").getAsInt());
            }else{
                coLocationApplicationDTO.setPowerType(coLocationApplicationJsonObject.get("powerType").getAsInt());
            }
        }


        coLocationApplicationDTO.setRackNeeded(coLocationApplicationJsonObject.get("rackNeeded").getAsBoolean());
        if (coLocationApplicationDTO.isRackNeeded()) {

            boolean rackType= coLocationApplicationJsonObject.get("rackSize").isJsonObject();
            if(rackType){
                coLocationApplicationDTO.setRackTypeID(coLocationApplicationJsonObject.get("rackSize").getAsJsonObject().get("id").getAsInt()
                                );
            }else{
                coLocationApplicationDTO.setRackTypeID(coLocationApplicationJsonObject.get("rackSize").getAsInt());
            }
            coLocationApplicationDTO.setRackSpace(coLocationApplicationJsonObject.get("rackSpace").getAsJsonObject().get("id").getAsInt());
        }

        //if floor space needed
        coLocationApplicationDTO.setFloorSpaceNeeded(coLocationApplicationJsonObject.get("floorSpaceNeeded").getAsBoolean());
        if (coLocationApplicationDTO.isFloorSpaceNeeded()) {
            boolean floorSpaceType= coLocationApplicationJsonObject.get("floorSpaceType").isJsonObject();
            if(floorSpaceType){
                coLocationApplicationDTO.setFloorSpaceType(coLocationApplicationJsonObject.get("floorSpaceType").getAsJsonObject().get("id").getAsInt()
                );
            }else{
                coLocationApplicationDTO.setFloorSpaceType(coLocationApplicationJsonObject.get("floorSpaceType").getAsInt());
            }

//            coLocationApplicationDTO.setFiberCore(coLocationApplicationJsonObject.get("fiberCore").getAsJsonObject().get("id").getAsInt());
            coLocationApplicationDTO.setFloorSpaceAmount(coLocationApplicationJsonObject.get("floorSpaceAmount").getAsDouble());
        }
        //end of floorspace needed


//        coLocationApplicationDTO.setComment(coLocationApplicationJsonObject.get("comment").getAsString());
        coLocationApplicationDTO.setSubmissionDate(System.currentTimeMillis());
        coLocationApplicationDTO.setDescription((String)parseFromJsonObject(coLocationApplicationJsonObject,"description",String.class));
        coLocationApplicationDTO.setComment((String) parseFromJsonObject(coLocationApplicationJsonObject, "comment", String.class));
        coLocationApplicationDTO.setSuggestedDate(coLocationApplicationJsonObject.get("suggestedDate")!=null?coLocationApplicationJsonObject.get("suggestedDate").getAsLong():0);



        return coLocationApplicationDTO;
    }

    public CoLocationApplicationDTO deserialize_revise_connection(JsonElement jsonElement, LoginDTO loginDTO) throws JsonParseException {
        CoLocationApplicationDTO coLocationApplicationDTO = new CoLocationApplicationDTO();

        JsonObject coLocationApplicationJsonObject = jsonElement.getAsJsonObject();

        //validate the application
//        validate(coLocationApplicationJsonObject);

        //set connection ID if applicable
        Integer connectionID = (Integer) parseFromJsonObject(coLocationApplicationJsonObject, "connectionID", Integer.class);
        coLocationApplicationDTO.setConnectionId(connectionID==null?0:connectionID);


        //get all value and set in the DTO
        if(loginDTO.getUserID()>0){
            coLocationApplicationDTO.setUserID(loginDTO.getUserID());
        }

        coLocationApplicationDTO.setClientID(coLocationApplicationJsonObject.get("client").getAsJsonObject().get("ID").getAsLong());//get client
//        coLocationApplicationDTO.setConnectionType(coLocationApplicationJsonObject.get("connectionType")!=null?coLocationApplicationJsonObject.get("connectionType").getAsInt():coLocationApplicationJsonObject.get("connectionType").getAsJsonObject().get("id").getAsInt());//get connectionType
//

        coLocationApplicationDTO.setFiberCore(coLocationApplicationJsonObject.get("fiberCore")!=null?coLocationApplicationJsonObject.get("fiberCore").getAsInt():0);
        coLocationApplicationDTO.setPowerAmount(coLocationApplicationJsonObject.get("powerAmount")!=null?coLocationApplicationJsonObject.get("powerAmount").getAsInt():0);
        coLocationApplicationDTO.setRackSpace(0);

//        coLocationApplicationDTO.setComment(coLocationApplicationJsonObject.get("comment").getAsString());
        coLocationApplicationDTO.setSubmissionDate(System.currentTimeMillis());
        coLocationApplicationDTO.setDescription((String)parseFromJsonObject(coLocationApplicationJsonObject,"description",String.class));
        coLocationApplicationDTO.setComment((String) parseFromJsonObject(coLocationApplicationJsonObject, "comment", String.class));
        coLocationApplicationDTO.setSuggestedDate(coLocationApplicationJsonObject.get("suggestedDate")!=null?coLocationApplicationJsonObject.get("suggestedDate").getAsLong():0);



        return coLocationApplicationDTO;
    }

    //validation goes here
    private void validate(JsonObject jsonObject) {
        if (jsonObject.get("client").getAsJsonObject() == null) {
            throw new RequestFailureException("Client must be selected");
        }
//        if (jsonObject.get("connectionType").getAsJsonObject() == null) {
//            throw new RequestFailureException("Connection Type must be selected");
//        }
        if ((Double) parseFromJsonObject(jsonObject, "suggestedDate", Double.class) == null) {
            throw new RequestFailureException("Suggested Date must be provided");
        }
    }

    private Object parseFromJsonObject(JsonObject jsonObject, String propertyName, Class<?> cls) {
        if (jsonObject.get(propertyName) == null) {
            return null;
        }
        if (jsonObject.get(propertyName).isJsonNull()) {
            return null;
        }
        if (cls == String.class) {
            return jsonObject.get(propertyName).getAsString();
        }
        if (cls == Integer.class) {
            return jsonObject.get(propertyName).getAsInt();
        }
        if (cls == Double.class) {
            return jsonObject.get(propertyName).getAsDouble();
        }
        return null;
    }
}
