package crm.inventory.repository;

import java.util.*;


import org.apache.log4j.Logger;
import connection.DatabaseConnection;
import crm.inventory.CRMInventoryAttributeName;

import static util.SqlGenerator.*;
import repository.Repository;
import repository.RepositoryManager;
import util.SqlGenerator;

class InventoryCatagoryAttributeNameRepository implements Repository{
	Logger logger = Logger.getLogger(getClass());
	static private InventoryCatagoryAttributeNameRepository instance = null; 
	
	public  HashMap<Integer,Set<CRMInventoryAttributeName>> mapOfInventoryAttruvbuteNameListToCatagaoryID;
	public HashMap<Long,CRMInventoryAttributeName> mapOfCRMInventoryAttributeNameToAttributeNameID;
	
	private InventoryCatagoryAttributeNameRepository(){
		mapOfInventoryAttruvbuteNameListToCatagaoryID = new HashMap<Integer, Set<CRMInventoryAttributeName>>();
		mapOfCRMInventoryAttributeNameToAttributeNameID = new HashMap<Long, CRMInventoryAttributeName>();
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
		List<CRMInventoryAttributeName> inventoryAttributeNames = new ArrayList<CRMInventoryAttributeName>();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			inventoryAttributeNames = (List<CRMInventoryAttributeName>)getAllObjectForRepository(CRMInventoryAttributeName.class, databaseConnection, isFirstReload);
			databaseConnection.dbClose();
		}catch(Exception ex){
			logger.debug("FATAL",ex);
		}
		for(CRMInventoryAttributeName inventoryAttributeName : inventoryAttributeNames){
			mapOfCRMInventoryAttributeNameToAttributeNameID.put(inventoryAttributeName.getID(), inventoryAttributeName);
			if(!mapOfInventoryAttruvbuteNameListToCatagaoryID.containsKey(inventoryAttributeName.getInventoryCatagoryTypeID())){
				mapOfInventoryAttruvbuteNameListToCatagaoryID.put(inventoryAttributeName.getInventoryCatagoryTypeID(), new HashSet());
			}
			Set<CRMInventoryAttributeName> inventoryAttributeNameSet = mapOfInventoryAttruvbuteNameListToCatagaoryID.get(inventoryAttributeName.getInventoryCatagoryTypeID()); 
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
			tableName = SqlGenerator.getTableName(CRMInventoryAttributeName.class);
		}catch(Exception ex){}
		return tableName;
	}



}
