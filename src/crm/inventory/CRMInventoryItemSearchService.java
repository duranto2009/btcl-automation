package crm.inventory;

import java.util.*;

import org.apache.log4j.Logger;
import common.RequestFailureException;

import connection.DatabaseConnection;
import crm.inventory.repository.CRMInventoryRepository;
import login.LoginDTO;
import sessionmanager.SessionConstants;
import util.KeyValuePair;
import util.NavigationService;
import static util.SqlGenerator.*;
public class CRMInventoryItemSearchService implements NavigationService{
	Logger logger = Logger.getLogger(getClass());
	CRMInventoryDAO inventoryDAO = new CRMInventoryDAO();
	private int categoryID;
	
	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	@Override
	public Collection getIDs(LoginDTO loginDTO, Object... objects ) throws Exception {
		
		List<Long> IDList = new ArrayList<Long>();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			Class classObject = CRMInventoryItem.class;
			IDList = inventoryDAO.getAllCRMInventoryItemIDListByCategoryID(categoryID, databaseConnection);
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.fatal("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
		} finally {
			databaseConnection.dbClose();
		}
		logger.debug("all id list: "+IDList);
		return IDList;
	}

	@Override
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria,
			LoginDTO loginDTO,Object... objects) throws Exception {

		
		
		Collection collection = new ArrayList();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			List<CRMInventoryAttributeName> inventoryAttributeNames =CRMInventoryRepository.getInstance().getCRMInventoryAttributeNameListByCatagoryID(categoryID);
			////////////////create two lists
			logger.debug("searchCriteria " + searchCriteria.size() +searchCriteria.toString() );
			List<KeyValuePair<Integer,String> >rootToItemPath=new ArrayList<KeyValuePair<Integer,String> >();;
			List<KeyValuePair<Long,String>> attributeNameContraintList=new ArrayList<KeyValuePair<Long,String>>();
			int i=0;
			for(;i<SessionConstants.SEARCH_INVENTORY_ITEM.length-inventoryAttributeNames.size();i++){
				KeyValuePair<Integer,String> pathKey=new KeyValuePair<Integer,String>();
				
				pathKey.key=Integer.parseInt(SessionConstants.SEARCH_INVENTORY_ITEM[i][1]);;
				pathKey.value=searchCriteria.get(SessionConstants.SEARCH_INVENTORY_ITEM[i][1].toString()).toString();
						
				logger.debug(pathKey.toString());
				rootToItemPath.add(pathKey);		
			}
			logger.debug(i);
			for(;i<SessionConstants.SEARCH_INVENTORY_ITEM.length;i++){
				KeyValuePair<Long,String> attrKey=new KeyValuePair<Long,String>();
				attrKey.key=Long.parseLong(SessionConstants.SEARCH_INVENTORY_ITEM[i][1]);;
				attrKey.value=searchCriteria.get(SessionConstants.SEARCH_INVENTORY_ITEM[i][1].toString()).toString();
						
				logger.debug(attrKey.toString());
				attributeNameContraintList.add(attrKey);		
			}
			logger.debug(rootToItemPath.toString()+"\n"+attributeNameContraintList.toString());
			collection = inventoryDAO.getCRMInventoryItemIDList(rootToItemPath, attributeNameContraintList, databaseConnection);
			
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.fatal("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
		} finally {
			databaseConnection.dbClose();
		}
		
		return collection;
	}

	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		Collection dtoCollection = new ArrayList<CRMInventoryItemDetails>();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			List<Long> parentItemIDList = new ArrayList<Long>();
			
			List<CRMInventoryItem> inventoryItems = (List<CRMInventoryItem>)getObjectListByIDList(CRMInventoryItem.class, (List<Long>)recordIDs, databaseConnection);
			
			Map<Long,CRMInventoryItem> mapOfCRMInventoryItemToItemID = new HashMap<Long, CRMInventoryItem>();
			
			for(CRMInventoryItem inventoryItem: inventoryItems){
				logger.debug(inventoryItem.toString());
				if(inventoryItem.getParentID()!=null){
					parentItemIDList.add(inventoryItem.getParentID());
				}
			}
			logger.debug("check1");
			Map<Long, String> mapOfInventoryParentItemToItemID =  inventoryDAO.getCRMInventoryItemNameListByItemIDList(parentItemIDList, databaseConnection);
			
			for(CRMInventoryItem inventoryItem: inventoryItems){
				CRMInventoryItemDetails inventoryItemDetails = new CRMInventoryItemDetails();
				inventoryItemDetails.setCRMInventoryItem(inventoryItem);
				List<CRMInventoryAttributeValue> inventoryAttributeValues = inventoryDAO.getCRMInventoryAttributeValueListByItemID(inventoryItem.getID(), databaseConnection);
				inventoryItemDetails.setCRMInventoryAttributeValues(inventoryAttributeValues);
				String parentItemName = mapOfInventoryParentItemToItemID.get(inventoryItem.getParentID());
				inventoryItemDetails.setParentItemName(parentItemName==null?"N/A":parentItemName);
				dtoCollection.add(inventoryItemDetails);
				logger.debug("dto with id "+inventoryItem.getID()+" is "+inventoryItemDetails);
				
			}
			
			
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.fatal("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
		} finally {
			databaseConnection.dbClose();
		}
		return dtoCollection;
	}
	
	private void validateUserInput(List<KeyValuePair<Integer,String> >rootToItemPath,
			List<KeyValuePair<Long,String>> attributeNameContraintList,DatabaseConnection databaseConnection)
					throws Exception{
		
	//validate attributeNameConstraint list
	// validate both
		if(rootToItemPath.isEmpty()){
			throw new RequestFailureException("Root to item path can not be null");
		}
		for(int i=0;i<rootToItemPath.size()-1;i++){
			Integer parentCategoryID = rootToItemPath.get(i).key;
			Integer childCateogoryID = rootToItemPath.get(i+1).key;
			CRMInventoryCatagoryType childCategory = CRMInventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(childCateogoryID); 
			if(!parentCategoryID.equals(childCategory.getParentCatagoryTypeID())){
				throw new RequestFailureException("");	
			}
		}
		
		int searchedCategoryID = rootToItemPath.get(rootToItemPath.size()-1).key;
		
		for(KeyValuePair<Long, String> attributeNameValuePair: attributeNameContraintList){
			long inventoryAttributeNameID = attributeNameValuePair.key;
			CRMInventoryAttributeName inventoryAttributeName = CRMInventoryRepository.getInstance().getCRMInventoryAttributeNameByNameID(inventoryAttributeNameID);
			if(inventoryAttributeName == null){
				throw new RequestFailureException("No inventory attribute name is found by ID "+inventoryAttributeNameID);
			}
			
			if(searchedCategoryID!=inventoryAttributeName.getInventoryCatagoryTypeID()){
				throw new RequestFailureException("The category ID of attribute name "+inventoryAttributeName.getName()+" should be "+searchedCategoryID+" while it is "+inventoryAttributeName.getID());
			}
		}
	}

}
