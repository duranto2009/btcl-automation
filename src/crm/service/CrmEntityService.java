package crm.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import annotation.Transactional;
import coLocation.CoLocationConstants;
import coLocation.connection.CoLocationConnectionDTO;
import coLocation.connection.CoLocationConnectionDTOConditionBuilder;
import global.GlobalService;
import lli.LLIConnectionInstance;
import lli.LLIConnectionInstanceConditionBuilder;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import nix.connection.NIXConnection;
import nix.connection.NIXConnectionConditionBuilder;
import nix.constants.NIXConstants;
import requestMapping.Service;
import util.KeyValuePair;
import vpn.VPNConstants;
import vpn.network.VPNNetworkLink;
import vpn.network.VPNNetworkLinkConditionBuilder;

public class CrmEntityService {
	
	@Service private GlobalService globalService;
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<KeyValuePair<Long, String>> getEntityDTOListByEntityTypeId(Integer entityTypeID, Long clientID,String entityName, LoginDTO loginDTO) throws Exception {
//		// TODO Auto-generated method stub
//		return crmEntityDAO.getEntityDTOListByEntityTypeId(entityTypeID,clientID,entityName, loginDTO);

		long actualClientId = loginDTO.getIsAdmin() ?clientID : loginDTO.getAccountID();
		switch (entityTypeID) {
			case VPNConstants
					.ENTITY_TYPE:
				return globalService.getAllObjectListByCondition(
						VPNNetworkLink.class, new VPNNetworkLinkConditionBuilder()
						.Where()
						.clientIdEquals(actualClientId)
						.linkNameBothLike(entityName)
						.getCondition()
				).stream()
				.map(t->new KeyValuePair<>(t.getId(), t.getLinkName()))
				.collect(Collectors.toList());

			case LLIConnectionConstants
					.ENTITY_TYPE:
				return globalService.getAllObjectListByCondition(
						LLIConnectionInstance.class, new LLIConnectionInstanceConditionBuilder()
						.Where()
						.clientIDEquals(actualClientId)
						.nameBothLike(entityName)
						.getCondition()
				).stream()
						.map(t->new KeyValuePair<>(t.getID(), t.getName()))
						.collect(Collectors.toList());

			case NIXConstants
					.ENTITY_TYPE:
				return globalService.getAllObjectListByCondition(
						NIXConnection.class, new NIXConnectionConditionBuilder()
						.Where()
						.clientEquals(actualClientId)
						.nameBothLike(entityName)
						.getCondition()
				).stream()
						.map(t->new KeyValuePair<>(t.getId(), t.getName()))
						.collect(Collectors.toList());

			case CoLocationConstants
					.ENTITY_TYPE:
				return globalService.getAllObjectListByCondition(
						CoLocationConnectionDTO.class, new CoLocationConnectionDTOConditionBuilder()
						.Where()
						.clientIDEquals(actualClientId)
						.nameBothLike(entityName)
						.getCondition()
				).stream()
						.map(t->new KeyValuePair<>(t.getID(), t.getName()))
						.collect(Collectors.toList());
			default:
				return Collections.emptyList();
		}
	}

}
