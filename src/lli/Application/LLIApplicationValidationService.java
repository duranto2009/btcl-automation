package lli.Application;

import client.RegistrantTypeConstants;
import com.google.gson.JsonObject;
import common.ModuleConstants;
import common.RequestFailureException;
import common.repository.AllClientRepository;
import lli.client.td.LLIClientTDService;
import lli.connection.LLIConnectionConstants;
import util.ServiceDAOFactory;
import vpn.client.ClientDetailsDTO;

import java.util.stream.IntStream;

public class LLIApplicationValidationService {
	
	public void validateExistingClient(JsonObject application) {
		validateMandatoryDropdown(application, "client", "Client");
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(application.get("client").getAsJsonObject().get("ID").getAsLong(), ModuleConstants.Module_ID_LLI);
		if(clientDetailsDTO == null) {
			throw new RequestFailureException("This client does not exist");
		}
		
	}
	public void validateConnectionType(JsonObject application) {
		validateExistingClient(application);
		validateMandatoryDropdown(application,"connectionType","Connection Type");
		 int connectionType;
		if(application.get("connectionType").isJsonObject()) {
			 connectionType = application.get("connectionType").getAsJsonObject().get("ID").getAsInt();
		}else{

			connectionType=application.get("connectionType").getAsInt();
		}
		int[] cocnnectionTypesValidForPrivateClients = {LLIConnectionConstants.CONNECTION_TYPE_REGULAR, LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY, LLIConnectionConstants.CONNECTION_TYPE_CACHE};
		int[] connectionTypesValidForGovtClients = {LLIConnectionConstants.CONNECTION_TYPE_REGULAR, LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY, LLIConnectionConstants.CONNECTION_TYPE_CACHE, LLIConnectionConstants.CONNECTION_TYPE_REGULAR_LONG};
		
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(application.get("client").getAsJsonObject().get("ID").getAsLong(), ModuleConstants.Module_ID_LLI);
		
		if(clientDetailsDTO.getRegistrantType() == RegistrantTypeConstants.GOVT) {
			if(!IntStream.of(connectionTypesValidForGovtClients).anyMatch(x -> x == connectionType)) {
				throw new RequestFailureException("Connection Type is invalid");
			}
		} else {
			if(!IntStream.of(cocnnectionTypesValidForPrivateClients).anyMatch(x -> x == connectionType)) {
				throw new RequestFailureException("Connection Type is invalid");
			}
		}
	}
	public void validateNonEmptyString(JsonObject application, String name, String title) {
		validateExistence(application,name,title);
		if(application.get(name).getAsString().trim().length() <= 0) {
			throw new RequestFailureException(title + " cannot be empty");
		}
	}
	public void validateExistence(JsonObject application, String name, String title) {
		if(application.get(name) == null) {
			throw new RequestFailureException(title + " is mandatory");
		}
	}
	public void validatePositiveNumber(JsonObject application, String name, String title) {
		validateExistence(application,name,title);
		validateNumeric(application, name, title);
		
		if(application.get(name).getAsLong() <= 0) {
			throw new RequestFailureException(title + " should be positive");
		}
	}
	public void validateMandatoryDropdown(JsonObject application, String name, String title) {
		validateExistence(application,name,title);
		
		if(! (application.get(name).isJsonObject() && !application.get(name).getAsJsonObject().get("ID").isJsonNull()) ) {
			throw new RequestFailureException(title + "is not valid");
		}
	}
	public void validateMandatoryDropdownZone(JsonObject application, String name, String title) {
		validateExistence(application,name,title);

		if(! (application.get(name).isJsonObject() && !application.get(name).getAsJsonObject().get("id").isJsonNull()) ) {
			throw new RequestFailureException(title + "is not valid");
		}
	}
	@SuppressWarnings("unused")
	public void validateNumeric(JsonObject application, String name, String title) {
		validateExistence(application,name,title);
		
		try {
			double number = Double.parseDouble(application.get(name).getAsString());
		} catch(NumberFormatException numberFormatException) {
			throw new RequestFailureException(title + " should be numeric");
		}
	}
	public void validateTDClient(JsonObject application){
		validateExistingClient(application);
		try {
			if(!ServiceDAOFactory.getService(LLIClientTDService.class).isClientTemporarilyDisconnected(application.get("client").getAsJsonObject().get("ID").getAsLong())) {
				throw new RequestFailureException("The Client is not temporarily disconnected");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RequestFailureException("The Client is not temporarily disconnected");
		}
	}
	
}
