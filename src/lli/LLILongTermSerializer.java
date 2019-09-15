package lli;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import common.repository.AllClientRepository;

public class LLILongTermSerializer implements JsonSerializer<LLILongTermContract>{

	@Override
	public JsonElement serialize(LLILongTermContract lliLongTermContract, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		
		jsonObject.addProperty("historyID", lliLongTermContract.getHistoryID());
		jsonObject.addProperty("ID", lliLongTermContract.getID());
		
		jsonObject.addProperty("bandwidth", lliLongTermContract.getBandwidth());
		jsonObject.add("status", context.serialize(new LLIDropdownPair(lliLongTermContract.getStatus(), LLILongTermContract.statusMap.get(lliLongTermContract.getStatus()) )));
			
		jsonObject.add("client", context.serialize( new LLIDropdownPair(lliLongTermContract.getClientID(), AllClientRepository.getInstance().getClientByClientID(lliLongTermContract.getClientID()).getLoginName() ) ));
		
		jsonObject.addProperty("contractStartDate", lliLongTermContract.getContractStartDate());
		jsonObject.addProperty("contractEndDate", lliLongTermContract.getContractEndDate());
		
		jsonObject.addProperty("activeFrom", lliLongTermContract.getActiveFrom());
		jsonObject.addProperty("activeTo", lliLongTermContract.getActiveTo());
		jsonObject.addProperty("validFrom", lliLongTermContract.getValidFrom());
		jsonObject.addProperty("validTo", lliLongTermContract.getValidTo());
		
		return jsonObject;
	}

}
