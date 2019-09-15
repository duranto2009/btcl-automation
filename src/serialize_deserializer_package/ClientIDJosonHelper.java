package serialize_deserializer_package;

import com.google.gson.JsonObject;

import common.ClientDTO;
import common.repository.AllClientRepository;
import lli.LLIDropdownPair;
import util.JsonProcessorHelper;

public class ClientIDJosonHelper implements JsonProcessorHelper{

	@Override
	public Object deserialize(JsonObject jsonObject) {
		try{
			return jsonObject.get("ID").getAsLong();
		}catch(Exception ex){
			return 0;
		}
	}

	@Override
	public Object serialize(Object object) {
		
		Long clientID = (Long) object;
		ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(clientID);
		return new LLIDropdownPair(clientID, clientDTO!=null?clientDTO.getLoginName(): "");
	}

}
