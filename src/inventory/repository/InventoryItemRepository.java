package inventory.repository;

import java.util.HashMap;

import org.apache.log4j.Logger;

import inventory.InventoryItem;
import inventory.InventoryService;
import repository.Repository;

class InventoryItemRepository implements Repository{
	Logger logger = Logger.getLogger(getClass());
	private static InventoryItemRepository instance = null;
	HashMap<Long,InventoryItem> mapOfInventoryItemToItemID;
	InventoryService inventoryService= new InventoryService();
	
	public synchronized InventoryItemRepository getInstance(){
		if(instance == null){
			instance = new InventoryItemRepository();
		}
		return instance;
	}
	@Override
	public void reload(boolean realoadAll) {
		// TODO Auto-generated method stub
		//inventoryService.getInventoryItemByItemID();
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}


}
