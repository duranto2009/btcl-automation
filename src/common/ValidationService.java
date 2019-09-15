package common;

import client.RegistrantTypeConstants;
import com.google.gson.JsonObject;
import common.repository.AllClientRepository;
import lli.client.td.LLIClientTDService;
import lli.connection.LLIConnectionConstants;
import util.ServiceDAOFactory;
import vpn.client.ClientDetailsDTO;

import java.util.stream.IntStream;

public class ValidationService {

    public static void validateExistingClient(JsonObject application) {
        validateMandatoryDropdown(application, "client", "Client");
        ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(application.get("client").getAsJsonObject().get("ID").getAsLong(), ModuleConstants.Module_ID_LLI);
        if (clientDetailsDTO == null) {
            throw new RequestFailureException("This client does not exist");
        }

    }

    public static void validateConnectionType(JsonObject application) {
        validateExistingClient(application);
        validateMandatoryDropdown(application, "connectionType", "Connection Type");
        int connectionType = application.get("connectionType").getAsJsonObject().get("ID").getAsInt();

        int[] connectionTypesValidForPrivateClients = {LLIConnectionConstants.CONNECTION_TYPE_REGULAR, LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY, LLIConnectionConstants.CONNECTION_TYPE_CACHE};
        int[] connectionTypesValidForGovtClients = {LLIConnectionConstants.CONNECTION_TYPE_REGULAR, LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY, LLIConnectionConstants.CONNECTION_TYPE_CACHE, LLIConnectionConstants.CONNECTION_TYPE_REGULAR_LONG};

        ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(application.get("client").getAsJsonObject().get("ID").getAsLong(), ModuleConstants.Module_ID_LLI);

        if (clientDetailsDTO.getRegistrantType() == RegistrantTypeConstants.GOVT) {
            if (!IntStream.of(connectionTypesValidForGovtClients).anyMatch(x -> x == connectionType)) {
                throw new RequestFailureException("Connection Type is invalid");
            }
        } else {
            if (!IntStream.of(connectionTypesValidForPrivateClients).anyMatch(x -> x == connectionType)) {
                throw new RequestFailureException("Connection Type is invalid");
            }
        }
    }

    public static void validateNonEmptyString(JsonObject application, String name, String title) {
        validateExistence(application, name, title);
        if (application.get(name).getAsString().trim().length() <= 0) {
            throw new RequestFailureException(title + " cannot be empty");
        }
    }

    public static void validateNonEmptyID(JsonObject application, String name, String title) {
        validateExistence(application, name, title);
        if (application.get(name).getAsLong() == 0L) {
            throw new RequestFailureException(title + " cannot be empty");
        }
    }

    public static void validateExistence(JsonObject application, String name, String title) {
        if (application.get(name) == null) {
            throw new RequestFailureException(title + " is mandatory");
        }
    }

    public static void validatePositiveNumber(JsonObject application, String name, String title) {
        validateExistence(application, name, title);
        validateNumeric(application, name, title);

        if (application.get(name).getAsLong() <= 0) {
            throw new RequestFailureException(title + " should be positive");
        }
    }

    public static void validateMandatoryDropdown(JsonObject application, String name, String title) {
        validateExistence(application, name, title);

        if (!(application.get(name).isJsonObject() && !application.get(name).getAsJsonObject().get("ID").isJsonNull())) {
            throw new RequestFailureException(title + "is not valid");
        }
    }

    public static void validateMandatoryDropdownZone(JsonObject application, String name, String title) {
        validateExistence(application, name, title);

        if (!(application.get(name).isJsonObject() && !application.get(name).getAsJsonObject().get("id").isJsonNull())) {
            throw new RequestFailureException(title + "is not valid");
        }
    }

    @SuppressWarnings("unused")
    public static void validateNumeric(JsonObject application, String name, String title) {
        validateExistence(application, name, title);

        try {
            double number = Double.parseDouble(application.get(name).getAsString());
        } catch (NumberFormatException numberFormatException) {
            throw new RequestFailureException(title + " should be numeric");
        }
    }

    public static void validateTDClient(JsonObject application) {
        validateExistingClient(application);
        try {
            if (!ServiceDAOFactory.getService(LLIClientTDService.class).isClientTemporarilyDisconnected(application.get("client").getAsJsonObject().get("ID").getAsLong())) {
                throw new RequestFailureException("The Client is not temporarily disconnected");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RequestFailureException("The Client is not temporarily disconnected");
        }
    }

    public static void validate(JsonObject object, String fieldName, String fieldTitle) {
        if (object.get(fieldName) == null) {
            throw new RequestFailureException(fieldTitle + " is mandatory");
        }
    }


}
