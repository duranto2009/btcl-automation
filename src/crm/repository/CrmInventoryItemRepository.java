package crm.repository;

import static util.SqlGenerator.getAllObjectForRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.log4j.Logger;

import connection.DatabaseConnection;
import crm.inventory.CRMInventoryItem;
import crm.service.CrmEmployeeService;
import repository.Repository;
import repository.RepositoryManager;
import util.DatabaseConnectionFactory;
import util.SqlGenerator;

public class CrmInventoryItemRepository implements Repository{

	Logger logger = Logger.getLogger(this.getClass());
	CrmEmployeeService crmService = new CrmEmployeeService();
	private static CrmInventoryItemRepository instance = null;
	
	Map<Long, CRMInventoryItem> mapOfInventoryItemDTOToInventoryItemID;
	Map<Long,CRMInventoryItem> mapOfRootInventoryItemDTOToInventoryItemID;
	Map<Long,Map<Long,CRMInventoryItem>> mapOfChildInventoryItemDTOMapToInventoryItemIDToParentInventoryItemID;
	Map<Integer,Map<Long,CRMInventoryItem>> mapOfInventoryItemToInventoryItemIdMapToInventoryCategoryID;
	
	
	private CrmInventoryItemRepository() {
		mapOfInventoryItemDTOToInventoryItemID = new ConcurrentHashMap<>();
		mapOfRootInventoryItemDTOToInventoryItemID = new ConcurrentHashMap<>();
		mapOfChildInventoryItemDTOMapToInventoryItemIDToParentInventoryItemID = new ConcurrentHashMap<>();
		mapOfInventoryItemToInventoryItemIdMapToInventoryCategoryID = new ConcurrentHashMap<>();
		RepositoryManager.getInstance().addRepository(this);
	}
	

	public static synchronized CrmInventoryItemRepository getInstance() {
		if (instance == null) {
			instance = new CrmInventoryItemRepository();
		}
		return instance;
	}

	@Override
	public void reload(boolean isFirstReload){
		DatabaseConnection databaseConnection = new DatabaseConnection();
		List<CRMInventoryItem> intentoryItemList;
		try {
			databaseConnection.dbOpen();
			intentoryItemList = (List<CRMInventoryItem>)getAllObjectForRepository(CRMInventoryItem.class, databaseConnection, isFirstReload," exists (select 1 from at_crm_employee where inventoryItemID = invitID)");
			for(CRMInventoryItem invItem : intentoryItemList){
				
				
				CRMInventoryItem oldCrmInventoryItem = mapOfInventoryItemDTOToInventoryItemID.get(invItem.getID());
				if(oldCrmInventoryItem!=null && oldCrmInventoryItem.getLastModificationTime()==invItem.getLastModificationTime()){
					continue;
				}
				
				if(oldCrmInventoryItem!=null){
					if(oldCrmInventoryItem.getParentID()!=null){
						// since oldCrmInventoryItem is not null , the maps will contain the dto(s)
						mapOfChildInventoryItemDTOMapToInventoryItemIDToParentInventoryItemID.
						get(oldCrmInventoryItem.getParentID()).remove(oldCrmInventoryItem.getID());
					}else{
						mapOfRootInventoryItemDTOToInventoryItemID.remove(oldCrmInventoryItem.getID());
					}
					
					if(oldCrmInventoryItem.getInventoryCatagoryTypeID()!=null){
						Map<Long,CRMInventoryItem> mapOfCrmInventoryItemToCrmInventoryCategoryID = mapOfInventoryItemToInventoryItemIdMapToInventoryCategoryID.get(oldCrmInventoryItem.getInventoryCatagoryTypeID());
						if(mapOfCrmInventoryItemToCrmInventoryCategoryID!=null){
							mapOfCrmInventoryItemToCrmInventoryCategoryID.remove(oldCrmInventoryItem.getInventoryCatagoryTypeID());
						}
						if(mapOfCrmInventoryItemToCrmInventoryCategoryID.isEmpty()){
							mapOfInventoryItemToInventoryItemIdMapToInventoryCategoryID
							.remove(oldCrmInventoryItem.getInventoryCatagoryTypeID());
						}
					}
					
					
				}
				mapOfInventoryItemDTOToInventoryItemID.remove(invItem.getID());
				if(!invItem.isDeleted()){
					mapOfInventoryItemDTOToInventoryItemID.put(invItem.getID(), invItem);
				}
				
				// parent id may change or set to null
				
				if(invItem.getParentID() == null){
					
					mapOfRootInventoryItemDTOToInventoryItemID.remove(invItem.getID());
					if(!invItem.isDeleted()){
						mapOfRootInventoryItemDTOToInventoryItemID.put(invItem.getID(),invItem);
					}

				}else {
					if(mapOfChildInventoryItemDTOMapToInventoryItemIDToParentInventoryItemID.containsKey(invItem.getParentID())){
						Map<Long, CRMInventoryItem> mapOfInventoryDTOToInventoryItemID  = mapOfChildInventoryItemDTOMapToInventoryItemIDToParentInventoryItemID.get(invItem.getParentID());
						mapOfInventoryDTOToInventoryItemID.remove(invItem.getID());
						if(!invItem.isDeleted()){
							mapOfInventoryDTOToInventoryItemID.put(invItem.getID(), invItem);
						}
						if(mapOfInventoryItemDTOToInventoryItemID.isEmpty()){
							mapOfChildInventoryItemDTOMapToInventoryItemIDToParentInventoryItemID.remove(invItem.getParentID());
						}
					}else{
						Map<Long, CRMInventoryItem> mapOfInventoryDTOToInventoryItemID  = new ConcurrentHashMap<>();
						if(!invItem.isDeleted()){
							mapOfInventoryDTOToInventoryItemID.put(invItem.getID(), invItem);
							mapOfChildInventoryItemDTOMapToInventoryItemIDToParentInventoryItemID.put(invItem.getParentID(), mapOfInventoryDTOToInventoryItemID);
						}
					}
				}
				
				if( !invItem.isDeleted() && invItem.getInventoryCatagoryTypeID()!=null){
					
					if(!mapOfInventoryItemToInventoryItemIdMapToInventoryCategoryID.containsKey(invItem.getInventoryCatagoryTypeID())){
						mapOfInventoryItemToInventoryItemIdMapToInventoryCategoryID.put(invItem.getInventoryCatagoryTypeID(), new HashMap<>());
					}
					
					Map<Long, CRMInventoryItem> mapOfInventoryDTOToInventoryItemID  = mapOfInventoryItemToInventoryItemIdMapToInventoryCategoryID.get(invItem.getInventoryCatagoryTypeID());
					mapOfInventoryDTOToInventoryItemID.put(invItem.getID(), invItem);
				}
			}
		
		} catch (Exception e) {
			logger.error("Error", e);
		}finally {
			databaseConnection.dbClose();
		}
		
	}
	

	@Override
	public String getTableName() {
		String tableName = "";
		try{
			tableName = SqlGenerator.getTableName(CRMInventoryItem.class);
		}catch(Exception ex){}
		return tableName;
	}

}
