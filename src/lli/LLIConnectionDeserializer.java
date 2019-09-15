package lli;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.*;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import util.TimeConverter;

public class LLIConnectionDeserializer implements JsonDeserializer<LLIConnectionInstance>{

	@Override
	public LLIConnectionInstance deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context)
			throws JsonParseException {
		LLIConnectionInstance lliConnectionInstance = new LLIConnectionInstance();
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		
		lliConnectionInstance.setID( jsonObject.get("ID").getAsLong());
		lliConnectionInstance.setName(jsonObject.get("name").getAsString());
		
		//lliConnectionInstance.setBandwidth((jsonObject.get("gbBandwidth").getAsDouble() * 1024) + jsonObject.get("mbBandwidth").getAsDouble());
		lliConnectionInstance.setBandwidth(jsonObject.get("bandwidth").getAsDouble());
		
		lliConnectionInstance.setConnectionType(jsonObject.get("connectionType").getAsJsonObject().get("ID").getAsInt());
		
		lliConnectionInstance.setIncident(jsonObject.get("incident").getAsJsonObject().get("ID").getAsInt());
		
		lliConnectionInstance.setClientID(jsonObject.get("client").getAsJsonObject().get("ID").getAsLong());
		
		lliConnectionInstance.setActiveFrom(jsonObject.get("activeFrom") != null ? jsonObject.get("activeFrom").getAsLong() : 0);
		
		List<LLIOffice> lliOfficeList = new ArrayList<>();
		for(JsonElement jsonOfficeElement: jsonObject.get("officeList").getAsJsonArray()){
			LLIOffice lliOffice = context.deserialize(jsonOfficeElement, LLIOffice.class);
			lliOfficeList.add(lliOffice);
		}
		lliConnectionInstance.setLliOffices(lliOfficeList);
		
		
		
		
		return lliConnectionInstance;
	}

}
