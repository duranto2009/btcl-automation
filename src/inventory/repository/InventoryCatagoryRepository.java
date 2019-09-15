package inventory.repository;
import java.util.*;
import inventory.InventoryCatagoryType;
import static util.SqlGenerator.*;
import java.util.HashMap;

import org.apache.log4j.Logger;

import connection.DatabaseConnection;

import repository.Repository;
import repository.RepositoryManager;
import util.SqlGenerator;

class InventoryCatagoryRepository implements Repository{
	Logger logger = Logger.getLogger(getClass());
	private static InventoryCatagoryRepository instance = null;
	public HashMap<Integer,InventoryCatagoryType> mapOfInventoryCatagoryTypeToCatagoryTypeID;
	
	private InventoryCatagoryRepository(){
		mapOfInventoryCatagoryTypeToCatagoryTypeID = new HashMap<Integer, InventoryCatagoryType>();
		RepositoryManager.getInstance().addRepository(this);
	}
	
	public static synchronized InventoryCatagoryRepository getInstance(){
		if(instance == null){
			instance = new InventoryCatagoryRepository();
		}
		return instance;
	}
	@Override
	public void reload(boolean isFirstReload) {
		try{
			DatabaseConnection databaseConnection = new DatabaseConnection();
			databaseConnection.dbOpen();
			List<InventoryCatagoryType> inventoryCatagoryTypes = (List<InventoryCatagoryType>)getAllObjectForRepository(InventoryCatagoryType.class, databaseConnection, isFirstReload);
			for(InventoryCatagoryType inventoryCatagoryType : inventoryCatagoryTypes){
				mapOfInventoryCatagoryTypeToCatagoryTypeID.put(inventoryCatagoryType.getID(), inventoryCatagoryType);
			}
			databaseConnection.dbClose();
		}catch(Exception ex){
			logger.debug("FATAL",ex);
		}
		logger.debug("mapOfInventoryCatagoryTypeToCatagoryTypeID"+mapOfInventoryCatagoryTypeToCatagoryTypeID);
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		String tableName = "";
		try{
			tableName = SqlGenerator.getTableName(InventoryCatagoryType.class);
		}catch(Exception ex){
			
		}
		return tableName;
	}


}
