package crm.inventory.repository;

import java.util.HashMap;

import org.apache.log4j.Logger;

import crm.inventory.CRMInventoryItem;
import crm.inventory.CRMInventoryService;
import repository.Repository;

class CRMInventoryItemRepository implements Repository{
	Logger logger = Logger.getLogger(getClass());
	private static CRMInventoryItemRepository instance = null;
	HashMap<Long,CRMInventoryItem> mapOfCRMInventoryItemToItemID;
	CRMInventoryService inventoryService= new CRMInventoryService();
	
	public synchronized CRMInventoryItemRepository getInstance(){
		if(instance == null){
			instance = new CRMInventoryItemRepository();
		}
		return instance;
	}
	@Override
	public void reload(boolean realoadAll) {
		// TODO Auto-generated method stub
		//inventoryService.getCRMInventoryItemByItemID();
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}


}
