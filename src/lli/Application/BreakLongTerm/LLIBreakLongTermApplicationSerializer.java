package lli.Application.BreakLongTerm;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import common.RequestFailureException;
import lli.LLIDropdownPair;
import lli.LLILongTermContract;
import lli.LLILongTermService;
import lli.Application.LLIApplicationSerializer;
import util.ServiceDAOFactory;
import util.TimeConverter;

public class LLIBreakLongTermApplicationSerializer implements JsonSerializer<LLIBreakLongTermApplication>{

	LLILongTermService lliLongTermService = ServiceDAOFactory.getService(LLILongTermService.class);
	
	@Override
	public JsonElement serialize(LLIBreakLongTermApplication lliBreakLongTermApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliBreakLongTermApplication.getExtendedApplicationID());

		LLILongTermContract contract;
		try {
			contract = lliLongTermService.getLLILongTermContractByLongTermContractID(lliBreakLongTermApplication.getContractID());
			String startDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(contract.getContractStartDate(), "dd/MM/yyyy");
			String endDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(contract.getContractEndDate(), "dd/MM/yyyy");
			
			jsonObject.add("contract", 
							context.serialize(new LLIDropdownPair(lliBreakLongTermApplication.getContractID(),
							contract.getBandwidth() + " Mbps + ( " + startDate + " - " + endDate + " )"
							)));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RequestFailureException("Invalid Contract");
		}
		
		jsonObject.addProperty("bandwidth", lliBreakLongTermApplication.getBandwidth());

		jsonObject.addProperty("suggestedDate", lliBreakLongTermApplication.getSuggestedDate());
		
		//Serialize Common LLI Application
		jsonObject = LLIApplicationSerializer.getCommonPart(lliBreakLongTermApplication, jsonObject, context);
		
		return jsonObject;
	}

}
