package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HierarchicalEntityService {

//	VpnLinkService vpnLinkService = ServiceDAOFactory.getService(VpnLinkService.class);
//	LliLinkService lliLinkService = ServiceDAOFactory.getService(LliLinkService.class);

	/**
	 * @author dhrubo
	 */
	public String getViewPageByEntityIDAndEntityTypeID(Long entityID, Integer entityTypeID, Integer... moduleID) throws Exception {
		String entityViewURL;
		Integer parentEntityTypeID = EntityTypeConstant.childEntityTypeParentEntityTypeMap.get(entityTypeID)!=null ? EntityTypeConstant.childEntityTypeParentEntityTypeMap.get(entityTypeID) : entityTypeID;

		if(parentEntityTypeID.equals(entityTypeID)) {
			entityViewURL = EntityTypeConstant.entityStrutsActionMap.get(entityTypeID) + ".do?entityID=" + entityID + "&entityTypeID=" + entityTypeID;
		} else {
			Long parentEntityID = getParentEntityIDByEntityIDAndEntityTypeID(entityID, entityTypeID);
			entityViewURL = EntityTypeConstant.entityStrutsActionMap.get(entityTypeID) + ".do?entityID=" + parentEntityID + "&entityTypeID=" + parentEntityTypeID;
		}

		if(moduleID.length > 0) {
			entityViewURL += "&moduleID=" + moduleID[0];
		}

		return entityViewURL;
	}

	/**
	 * @author dhrubo
	 */
	private Long getParentEntityIDByEntityIDAndEntityTypeID(Long entityID, Integer entityTypeID) throws Exception {

		switch (entityTypeID / 100) {
		case ModuleConstants.Module_ID_VPN:
//			entityID = vpnLinkService.getParentEntityIDByEntityIDAndEntityTypeID(entityID, entityTypeID);
//		case ModuleConstants.Module_ID_LLI:
//			entityID = lliLinkService.getParentEntityIDByEntityIDAndEntityTypeID(entityID, entityTypeID);
		}
		return entityID;
	}

	/**
	 * @author dhrubo
	 */
	public Boolean hasViewPage(Long entityID, Integer entityTypeID) throws Exception {
		return getParentEntityIDByEntityIDAndEntityTypeID(entityID, entityTypeID) != null ;
	}


	/**
	 * @author dhrubo
	 */

	public List<HashMap<String, String>> getChildEntityListByParentEntityIDAndEntityTypeID(Long entityID, Integer entityTypeID){
		List<HashMap<String, String>> childEntityListByParentEntityIDAndEntityTypeID = new ArrayList<HashMap<String, String>>();
		switch (entityTypeID / 100) {
//		case ModuleConstants.Module_ID_VPN:
//			childEntityListByParentEntityIDAndEntityTypeID = vpnLinkService.getChildEntityListByParentEntityIDAndEntityTypeID(entityID, entityTypeID);
//			break;
//		case ModuleConstants.Module_ID_LLI:
//			childEntityListByParentEntityIDAndEntityTypeID = lliLinkService.getChildEntityListByParentEntityIDAndEntityTypeID(entityID, entityTypeID);
//			break;
		default:
			HashMap<String, String> genericEntityDetails = new HashMap<String, String>();
			genericEntityDetails.put("entityID", String.valueOf(entityID));
			genericEntityDetails.put("entityTypeID", String.valueOf(entityTypeID));
			childEntityListByParentEntityIDAndEntityTypeID.add(genericEntityDetails);
		}
		return childEntityListByParentEntityIDAndEntityTypeID;
	}

}

