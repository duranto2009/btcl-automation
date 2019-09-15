package lli;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import lli.connection.LLIConnectionConstants;
import user.UserRepository;

public class LLILocalLoopSerizalizer implements JsonSerializer<LLILocalLoop>{

	@Override
	public JsonElement serialize(LLILocalLoop lliLocalLoop, Type arg1, JsonSerializationContext context){
		JsonObject jsonObject = new JsonObject();
		
		jsonObject.addProperty("ID", lliLocalLoop.getID());
		
		jsonObject.addProperty("clientDistance", lliLocalLoop.getClientDistance());
		jsonObject.addProperty("btclDistance", lliLocalLoop.getBtclDistance());
		jsonObject.addProperty("OCDistance", lliLocalLoop.getOCDistance());
		
		if(lliLocalLoop.getOCID() != null && lliLocalLoop.getOCID()!=0) {
			jsonObject.add("OC", context.serialize(new LLIDropdownPair(lliLocalLoop.getOCID(), UserRepository.getInstance().getUserDTOByUserID(lliLocalLoop.getOCID()).getUsername() )) );
		}
		
		jsonObject.addProperty("popID", lliLocalLoop.getPopID());
		jsonObject.addProperty("router_switchID", lliLocalLoop.getRouter_switchID());
		jsonObject.addProperty("portID", lliLocalLoop.getPortID());
		jsonObject.addProperty("vlanID", lliLocalLoop.getVlanID());

		jsonObject.add("ofcType", context.serialize(new LLIDropdownPair(lliLocalLoop.getOfcType(), LLIConnectionConstants.ofcTypeMap.get(lliLocalLoop.getOfcType()) )));
		
		
		return jsonObject;
	}

}
