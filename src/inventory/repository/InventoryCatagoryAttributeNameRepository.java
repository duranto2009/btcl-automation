package inventory.repository;

import inventory.InventoryAttributeName;

import java.util.*;


import org.apache.log4j.Logger;
import connection.DatabaseConnection;
import static util.SqlGenerator.*;
import repository.Repository;
import repository.RepositoryManager;
import util.SqlGenerator;

class InventoryCatagoryAttributeNameRepository implements Repository{
	Logger logger = Logger.getLogger(getClass());
	static private InventoryCatagoryAttributeNameRepository instance = null; 
	
	public  HashMap<Integer,Set<InventoryAttributeName>> mapOfInventoryAttruvbuteNameListToCatagaoryID;
	public HashMap<Long,InventoryAttributeName> mapOfInventoryAttributeNameToAttributeNameID;
	
	private InventoryCatagoryAttributeNameRepository(){
		mapOfInventoryAttruvbuteNameListToCatagaoryID = new HashMap<Integer, Set<InventoryAttributeName>>();
		mapOfInventoryAttributeNameToAttributeNameID = new HashMap<Long, InventoryAttributeName>();
		RepositoryManager.getInstance().addRepository(this);
	}
	
	
	
	public static synchronized InventoryCatagoryAttributeNameRepository getInstance(){
		if(instance == null){
			instance = new InventoryCatagoryAttributeNameRepository();
		}
		return instance;
	}
	@Override
	public void reload(boolean isFirstReload) {
		List<InventoryAttributeName> inventoryAttributeNames = new ArrayList<InventoryAttributeName>();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			inventoryAttributeNames = (List<InventoryAttributeName>)getAllObjectForRepository(InventoryAttributeName.class, databaseConnection, isFirstReload);
			databaseConnection.dbClose();
		}catch(Exception ex){
			logger.debug("FATAL",ex);
		}
		for(InventoryAttributeName inventoryAttributeName : inventoryAttributeNames){
			mapOfInventoryAttributeNameToAttributeNameID.put(inventoryAttributeName.getID(), inventoryAttributeName);
			if(!mapOfInventoryAttruvbuteNameListToCatagaoryID.containsKey(inventoryAttributeName.getInventoryCatagoryTypeID())){
				mapOfInventoryAttruvbuteNameListToCatagaoryID.put(inventoryAttributeName.getInventoryCatagoryTypeID(), new HashSet());
			}
			Set<InventoryAttributeName> inventoryAttributeNameSet = mapOfInventoryAttruvbuteNameListToCatagaoryID.get(inventoryAttributeName.getInventoryCatagoryTypeID()); 
			inventoryAttributeNameSet.remove(inventoryAttributeName);
			inventoryAttributeNameSet.add(inventoryAttributeName);
		}
		
		//logger.debug("mapOfInventoryAttruvbuteNameListToCatagaoryID"+mapOfInventoryAttruvbuteNameListToCatagaoryID);
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		String tableName = "";
		try{
			tableName = SqlGenerator.getTableName(InventoryAttributeName.class);
		}catch(Exception ex){}
		return tableName;
	}



}
