package lli;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class LLILongTermDeserializer implements JsonDeserializer<LLILongTermContract>{

	@Override
	public LLILongTermContract deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context)	throws JsonParseException {
		LLILongTermContract lliLongTermContract = new LLILongTermContract();
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		
		lliLongTermContract.setClientID(jsonObject.get("client").getAsJsonObject().get("ID").getAsLong());
		lliLongTermContract.setStatus(jsonObject.get("status") != null? jsonObject.get("status").getAsInt() : 0);
		lliLongTermContract.setBandwidth(jsonObject.get("bandwidth").getAsDouble());
		
		lliLongTermContract.setContractStartDate(jsonObject.get("contractStartDate").getAsLong());
		
		return lliLongTermContract;
	}

}
