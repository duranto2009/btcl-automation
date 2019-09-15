package lli;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import common.repository.AllClientRepository;
import lli.connection.LLIConnectionConstants;

public class LLIConnectionInstanceSerizalizer implements JsonSerializer<LLIConnectionInstance>{

	@Override
	public JsonElement serialize(LLIConnectionInstance connectionInstance, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		
		jsonObject.addProperty("historyID", connectionInstance.getHistoryID());
		jsonObject.addProperty("ID", connectionInstance.getID());
		jsonObject.addProperty("name", connectionInstance.getName());
		
		jsonObject.addProperty("bandwidth", connectionInstance.getBandwidth());
		
		jsonObject.add("connectionType", context.serialize(new LLIDropdownPair(connectionInstance.getConnectionType(),  LLIConnectionConstants.connectionTypeMap.get(connectionInstance.getConnectionType())  )));
		
		if(connectionInstance.getClientID() != 0) {
			jsonObject.add("client", context.serialize(new LLIDropdownPair(connectionInstance.getClientID(),
					AllClientRepository.getInstance().getClientByClientID(connectionInstance.getClientID()).getLoginName())));
		}
		jsonObject.add("status", context.serialize(new LLIDropdownPair(connectionInstance.getStatus(), LLIConnectionInstance.statusMap.get(connectionInstance.getStatus()))) );
		jsonObject.add("incident", context.serialize(new LLIDropdownPair(connectionInstance.getIncident(), LLIConnectionConstants.applicationTypeNameMap.get(connectionInstance.getIncident()))) );
		jsonObject.add("officeList", context.serialize(connectionInstance.getLliOffices() == null ? new ArrayList<>() : connectionInstance.getLliOffices()));

		jsonObject.addProperty("activeFrom", connectionInstance.getActiveFrom());
		jsonObject.addProperty("validTo", connectionInstance.getValidTo());
		jsonObject.addProperty("startDate", connectionInstance.getStartDate());
		
		/**/
		jsonObject.add("options", context.serialize(LLIDropdownMap.map));
		return jsonObject;
	}

}
