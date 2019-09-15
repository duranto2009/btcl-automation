package crm.repository;

import crm.CrmEmployeeNode;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CrmRepository{
	private static CrmRepository instance = null;
	Map<Long,CrmEmployeeNode> mapOfItemIdToEmployeeNode;
	Map<Long,Set<CrmEmployeeNode>> mapOfParentItemIdToSetOfChildEmployeeNode; // also populate this map
	Set<CrmEmployeeNode> rootEmployeeNodeSet;
	Set<Long> dirtyNodeSet = new HashSet<>();
	
	
	void setDirty(long id){
		dirtyNodeSet.add(id);
	}
	
	void removeFromDirty(long id){
		dirtyNodeSet.remove(id);
	}

	private CrmRepository() {
		mapOfItemIdToEmployeeNode = new ConcurrentHashMap<>();
		mapOfParentItemIdToSetOfChildEmployeeNode = new ConcurrentHashMap<>();
		rootEmployeeNodeSet = new HashSet<>();
	}
	
	
	void buildTreeByItemID(long itemID){
		
		if(!dirtyNodeSet.contains(itemID)){
			return;
		}
		// remove from dirty set
		removeFromDirty(itemID);
	
		CrmEmployeeNode rootEmployeeNode = getCrmEmployeeNodeByInventoryItemID(itemID);
		
		for (CrmEmployeeNode childCrmEmployeeNode : mapOfParentItemIdToSetOfChildEmployeeNode.get(rootEmployeeNode.getID())) {
				buildTreeByItemID(childCrmEmployeeNode.getID());
				if(isEdgeExist(itemID,childCrmEmployeeNode.getID())){ // if no edge exists
					rootEmployeeNode.addChildCrmEmployeeNode(childCrmEmployeeNode);
				}
		}
	}
	
	boolean isEdgeExist(long parentNode, long childNode){
		return mapOfParentItemIdToSetOfChildEmployeeNode.get(parentNode).contains(childNode);
	}
	
	
	CrmEmployeeNode getCrmEmployeeNodeByInventoryItemID(long inventoryItemID){
		return mapOfItemIdToEmployeeNode.get(inventoryItemID);
	}
	

	public static synchronized CrmRepository getInstance() {
		if (instance == null) {
			instance = new CrmRepository();
		}
		return instance;
	}

}
