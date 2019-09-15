package inventory;

import inventory.repository.InventoryRepository;

import java.util.*;

import org.apache.log4j.Logger;
import common.RequestFailureException;

import connection.DatabaseConnection;

import login.LoginDTO;
import sessionmanager.SessionConstants;
import util.KeyValuePair;
import util.NavigationService;
import static util.SqlGenerator.*;
public class InventoryItemSearchService implements NavigationService{
	Logger logger = Logger.getLogger(getClass());
	InventoryDAO inventoryDAO = new InventoryDAO();
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
			IDList = inventoryDAO.getAllInventoryItemIDListByCategoryID(categoryID, databaseConnection);
		} catch (Exception ex) {
			logger.fatal("Fatal", ex);
			
		} finally {
			databaseConnection.dbClose();
		}
		return IDList;
	}

	@Override
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria,
			LoginDTO loginDTO,Object... objects) throws Exception {

		
		
		Collection collection = new ArrayList();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();

			List<KeyValuePair<Integer,String> >rootToItemPath=new ArrayList<KeyValuePair<Integer,String> >();;
			List<KeyValuePair<Long,String>> attributeNameContraintList=new ArrayList<KeyValuePair<Long,String>>();
			int i=0;
			
			while(i<SessionConstants.SEARCH_INVENTORY_ITEM.length-1
					&& searchCriteria.get(SessionConstants.SEARCH_INVENTORY_ITEM[i][1].toString()).toString().trim().equals("")){
				i++;
			}
			
			
			for(;i<SessionConstants.SEARCH_INVENTORY_ITEM.length;i++){
				KeyValuePair<Integer,String> pathKey=new KeyValuePair<Integer,String>();
				
				pathKey.key=Integer.parseInt(SessionConstants.SEARCH_INVENTORY_ITEM[i][1]);;
				pathKey.value=searchCriteria.get(SessionConstants.SEARCH_INVENTORY_ITEM[i][1].toString()).toString();
						
				rootToItemPath.add(pathKey);		
			}

			
			for(InventoryAttributeName inventoryAttributeName: InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(categoryID)) {
				KeyValuePair<Long,String> attrKey=new KeyValuePair<Long,String>();
				attrKey.key=inventoryAttributeName.getID();
				attrKey.value=searchCriteria.get(""+inventoryAttributeName.getID()).toString();
				attributeNameContraintList.add(attrKey);			
			}
			
			
			
			Boolean isUsed = null;
			
			if(searchCriteria.containsKey("isUsed")){
				String stringValueOfIsUsed = (String)searchCriteria.get("isUsed");
				if(stringValueOfIsUsed.trim().equals("0")){
					isUsed = false;
				}else if(stringValueOfIsUsed.trim().equals("1")){
					isUsed = true;
				}
			}
			
			collection = inventoryDAO.getInventoryItemIDList(rootToItemPath, attributeNameContraintList
					,isUsed,databaseConnection);
			
		} catch (Exception ex) {
			logger.fatal("Fatal", ex);
			throw ex;
		} finally {
			databaseConnection.dbClose();
		}
		
		return collection;
	}

	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		Collection dtoCollection = new ArrayList<InventoryItemDetails>();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			
			List<Long> parentItemIDList = new ArrayList<Long>();
			
			List<InventoryItem> inventoryItems = (List<InventoryItem>)getObjectListByIDList(InventoryItem.class, (List<Long>)recordIDs, databaseConnection);
			
			Map<Long,InventoryItem> mapOfInventoryItemToItemID = new HashMap<Long, InventoryItem>();
			
			for(InventoryItem inventoryItem: inventoryItems){
				if(inventoryItem.getParentID()!=null){
					parentItemIDList.add(inventoryItem.getParentID());
				}
			}
			Map<Long, String> mapOfInventoryParentItemToItemID =  inventoryDAO.getInventoryItemNameListByItemIDList(parentItemIDList, databaseConnection);
			
			List<InventoryAttributeValue> inventoryAttributeValues = inventoryDAO.getInventoryAttributeValueListByItemIDList((List<Long>) recordIDs, databaseConnection);// inventoryDAO.getInventoryAttributeValueListByItemID(inventoryItem.getID(), databaseConnection);
			
			Map<Long,List<InventoryAttributeValue>> mapOfInventoryAttributeValueListToInventoryItemID = new HashMap<>();
			
			for(InventoryAttributeValue inventoryAttributeValue: inventoryAttributeValues){
				if(!mapOfInventoryAttributeValueListToInventoryItemID.containsKey(inventoryAttributeValue
						.getInventoryItemID())){
					mapOfInventoryAttributeValueListToInventoryItemID.put(inventoryAttributeValue
							.getInventoryItemID(), new ArrayList<>());
				}
				mapOfInventoryAttributeValueListToInventoryItemID.get(inventoryAttributeValue
						.getInventoryItemID()).add(inventoryAttributeValue);
			}
			
			
			
			for(InventoryItem inventoryItem: inventoryItems){
				InventoryItemDetails inventoryItemDetails = new InventoryItemDetails();
				inventoryItemDetails.setInventoryItem(inventoryItem);
				inventoryItemDetails.setInventoryAttributeValues(
						mapOfInventoryAttributeValueListToInventoryItemID.containsKey(inventoryItem.getID())?  mapOfInventoryAttributeValueListToInventoryItemID.get(inventoryItem.getID()):new ArrayList<>()
					);
				List<InventoryAttributeName> inventoryAttributeNames =  InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(inventoryItem.getInventoryCatagoryTypeID());
				inventoryItemDetails.setInventoryAttributeNames(inventoryAttributeNames);
				
				String parentItemName = mapOfInventoryParentItemToItemID.get(inventoryItem.getParentID());
				inventoryItemDetails.setParentItemName(parentItemName==null?"N/A":parentItemName);
				
				dtoCollection.add(inventoryItemDetails);
				
			}
			
			
		} catch (Exception ex) {
			logger.fatal("Fatal", ex);
			
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
			InventoryCatagoryType childCategory = InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(childCateogoryID); 
			if(!parentCategoryID.equals(childCategory.getParentCatagoryTypeID())){
				throw new RequestFailureException("");	
			}
		}
		
		int searchedCategoryID = rootToItemPath.get(rootToItemPath.size()-1).key;
		
		for(KeyValuePair<Long, String> attributeNameValuePair: attributeNameContraintList){
			long inventoryAttributeNameID = attributeNameValuePair.key;
			InventoryAttributeName inventoryAttributeName = InventoryRepository.getInstance().getInventoryAttributeNameByNameID(inventoryAttributeNameID);
			if(inventoryAttributeName == null){
				throw new RequestFailureException("No inventory attribute name is found by ID "+inventoryAttributeNameID);
			}
			
			if(searchedCategoryID!=inventoryAttributeName.getInventoryCatagoryTypeID()){
				throw new RequestFailureException("The category ID of attribute name "+inventoryAttributeName.getName()+" should be "+searchedCategoryID+" while it is "+inventoryAttributeName.getID());
			}
		}
	}

}
