package crm.inventory.repository;
import java.util.*;
import static util.SqlGenerator.*;
import java.util.HashMap;

import org.apache.log4j.Logger;

import connection.DatabaseConnection;
import crm.inventory.CRMInventoryCatagoryType;
import repository.Repository;
import repository.RepositoryManager;
import util.SqlGenerator;

class InventoryCatagoryRepository implements Repository{
	Logger logger = Logger.getLogger(getClass());
	private static InventoryCatagoryRepository instance = null;
	public HashMap<Integer,CRMInventoryCatagoryType> mapOfCRMInventoryCatagoryTypeToCatagoryTypeID;
	
	private InventoryCatagoryRepository(){
		mapOfCRMInventoryCatagoryTypeToCatagoryTypeID = new HashMap<Integer, CRMInventoryCatagoryType>();
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
			List<CRMInventoryCatagoryType> inventoryCatagoryTypes = (List<CRMInventoryCatagoryType>)getAllObjectForRepository(CRMInventoryCatagoryType.class, databaseConnection, isFirstReload," not exists (select 1 from at_crm_designation_type where inventoryCategoryID = invctID)");
			for(CRMInventoryCatagoryType inventoryCatagoryType : inventoryCatagoryTypes){
				mapOfCRMInventoryCatagoryTypeToCatagoryTypeID.put(inventoryCatagoryType.getID(), inventoryCatagoryType);
			}
			databaseConnection.dbClose();
		}catch(Exception ex){
			logger.debug("FATAL",ex);
		}
		logger.debug("mapOfCRMInventoryCatagoryTypeToCatagoryTypeID"+mapOfCRMInventoryCatagoryTypeToCatagoryTypeID);
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		String tableName = "";
		try{
			tableName = SqlGenerator.getTableName(CRMInventoryCatagoryType.class);
		}catch(Exception ex){
			
		}
		return tableName;
	}


}
