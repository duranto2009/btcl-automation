package crm.inventory;



import java.util.*;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.apache.log4j.lf5.viewer.categoryexplorer.CategoryExplorerTree;

import util.DatabaseConnectionFactory;
import util.SqlGenerator;

import com.mysql.fabric.xmlrpc.base.Array;


import common.RequestFailureException;

import connection.DatabaseConnection;
import crm.inventory.repository.CRMInventoryRepository;


public class CRMInventoryService {
	Logger logger = Logger.getLogger(getClass());
	static Integer mutexLock = new Integer(0);
	CRMInventoryDAO inventoryDAO = new CRMInventoryDAO();

	private CRMInventoryAttributeName createCRMInventoryAttributeName(Integer catagoryID,String attributeName,int index,long lastModificationTime){
		CRMInventoryAttributeName inventoryAttributeName = new CRMInventoryAttributeName();
		inventoryAttributeName.setName(attributeName);
		inventoryAttributeName.setInventoryCatagoryTypeID(catagoryID);
		inventoryAttributeName.setDeleted(false);
		inventoryAttributeName.setLastModificationTime(lastModificationTime);
		inventoryAttributeName.setIndexNumber(index);
		return inventoryAttributeName;
	} 
	
	private boolean matches(CRMInventoryItemDetails inventoryItemDetails,Map<Long,String> mapOfMatchingStringToAttributeValueID){
		
		for(CRMInventoryAttributeValue inventoryAttributeValue:inventoryItemDetails.getCRMInventoryAttributeValues()){
			String matchingString = mapOfMatchingStringToAttributeValueID.get(inventoryAttributeValue.getID());
			if(matchingString!=null && !matchingString.trim().equals("") && !inventoryAttributeValue.value.contains(matchingString)){
				return false;
			}
		}
		
		return true;
	}
	public List<CRMInventoryItemDetails> getInvenotryItemDetailsByParentItemIDAndCatagoryID(Long parentItemID,int catagoryID){
		return  getInvenotryItemDetailsByParentItemIDAndCatagoryIDWithMatchingCriteria(parentItemID,catagoryID,new HashMap<Long,String>());
	}
	public List<CRMInventoryItemDetails> getInvenotryItemDetailsByParentItemIDAndCatagoryIDWithMatchingCriteria(Long parentItemID,int catagoryID,Map<Long,String> mapOfMatchingStringToAttributeValueID){
		List<CRMInventoryItemDetails> inventoryItemDetailsList = new ArrayList<CRMInventoryItemDetails>();
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			
			List<CRMInventoryItem> inventoryItems = inventoryDAO.getCRMInventoryItemListByParentItemAndCatagoryID(parentItemID, catagoryID, databaseConnection);
			
			for(CRMInventoryItem inventoryItem: inventoryItems){
				List<CRMInventoryAttributeValue> inventoryAttributeValues = inventoryDAO.getCRMInventoryAttributeValueListByItemID(inventoryItem.getID(), databaseConnection);
				CRMInventoryItemDetails inventoryItemDetails = new CRMInventoryItemDetails();
				inventoryItemDetails.setCRMInventoryAttributeValues(inventoryAttributeValues);
				inventoryItemDetails.setCRMInventoryItem(inventoryItem);
				if(matches(inventoryItemDetails, mapOfMatchingStringToAttributeValueID)){
					inventoryItemDetailsList.add(inventoryItemDetails);
				}
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
		
		return inventoryItemDetailsList;
	}
	private List<Long> getCRMInventoryAttributeNameIDListForDeletion(List<CRMInventoryAttributeName> inventoryAttributeNames){
		Set<Long> inventoryAttributeNameIDSet = new HashSet<Long>();
		
		for(CRMInventoryAttributeName inventoryAttributeName:inventoryAttributeNames){
			inventoryAttributeNameIDSet.add(inventoryAttributeName.getID());
		}
		int categoryID = inventoryAttributeNames.get(0).getInventoryCatagoryTypeID();
		List<CRMInventoryAttributeName> existingCRMInventoryAttributeNames = CRMInventoryRepository.getInstance().getCRMInventoryAttributeNameListByCatagoryID(categoryID);
		
		List<Long> IDListForDeletion = new ArrayList<Long>();
		
		for(CRMInventoryAttributeName inventoryAttributeName: existingCRMInventoryAttributeNames){
			Long existingAttributeNameID = inventoryAttributeName.getID();
			if(!inventoryAttributeNameIDSet.contains(existingAttributeNameID)){
				IDListForDeletion.add(existingAttributeNameID);
			}
		}
		
		return IDListForDeletion;
	}
	
	private void deleteAttributeNameListByIDList(List<Long> attributeNameIDList,DatabaseConnection databaseConnection) throws Exception{
		// delete attribute name 
		
		// delete attribute value
	}
	
	private void addCRMInventoryAttributeName(CRMInventoryAttributeName inventoryAttributeName,DatabaseConnection databaseConnection) throws Exception{
		// add attribute name 
		inventoryDAO.addCRMInventoryAttributeName(inventoryAttributeName, databaseConnection);
		// add attribute value with defaul value if any inventory item exists of that type
	}
	public void updateInventoryCategoryDetails(List<CRMInventoryAttributeName> inventoryAttributeNames){
		
		for(int i=1;i<inventoryAttributeNames.size();i++){
			CRMInventoryAttributeName prevAttributeName = inventoryAttributeNames.get(i-1);
			CRMInventoryAttributeName nextAttributeName = inventoryAttributeNames.get(i);
			if(!prevAttributeName.getInventoryCatagoryTypeID().equals(nextAttributeName.getInventoryCatagoryTypeID())){
				throw new RequestFailureException("Invalid input. Category ID of all attribute names do not match");
			}
		}
		
		long currentTime = System.currentTimeMillis();
		
		for(CRMInventoryAttributeName inventoryAttributeName:inventoryAttributeNames){
			inventoryAttributeName.setLastModificationTime(currentTime);
			inventoryAttributeName.setDeleted(false);
		}
		
		
		
		//add attribute name and attribute values with default value if specified
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			//find delete attribute name ids
			List<Long> attributeNameIDListForDeletetion = getCRMInventoryAttributeNameIDListForDeletion(inventoryAttributeNames);
			//delete those attribute names and corresponding attribute values 
			deleteAttributeNameListByIDList(attributeNameIDListForDeletetion,databaseConnection);
			//update attribute names
			
			for(CRMInventoryAttributeName inventoryAttributeName: inventoryAttributeNames){
				if(inventoryAttributeName.getID()==0){
					addCRMInventoryAttributeName(inventoryAttributeName, databaseConnection);
				}else {
					inventoryDAO.updateCRMInventoryAttributeName(inventoryAttributeName, databaseConnection);
				}
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
	}
	public void updateCRMInventoryAttributeName(Integer catagoryID,List<String> nameList) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			List<CRMInventoryAttributeName> inventoryAttributeNames = new ArrayList<CRMInventoryAttributeName>();
			long lastModificationTime = System.currentTimeMillis();
			
			inventoryDAO.deleteInventoryAttributeByCatagoryID(catagoryID, databaseConnection, lastModificationTime);
			
			for(int i=0;i<nameList.size();i++){
				inventoryAttributeNames.add(createCRMInventoryAttributeName(catagoryID, nameList.get(i), i,lastModificationTime));
			}
			
			for(CRMInventoryAttributeName inventoryAttributeName: inventoryAttributeNames){
				inventoryDAO.addCRMInventoryAttributeName(inventoryAttributeName, databaseConnection);
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
	}
	
	
	public void addCatagoryList(
			List<CRMInventoryCatagoryType> inventoryCatagoryTypeList) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			for (CRMInventoryCatagoryType inventoryCatagoryType : inventoryCatagoryTypeList) {
				inventoryDAO.addInventoryCatagory(inventoryCatagoryType,
						databaseConnection);
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
	}
	private boolean isUsedAsChildCategory(Integer inventoryCategoryID,DatabaseConnection databaseConnection) throws Exception{
		if(inventoryCategoryID == null){
			return false;
		}
		
		if(inventoryDAO.hasChildOfInventoryCategory(inventoryCategoryID, databaseConnection)){
			return false;
		}
		if(inventoryDAO.hasAnyCRMInventoryItemOfCategory(inventoryCategoryID, databaseConnection)){
			return false;
		}
		
		return true;
	}
	public void addCatagory(CRMInventoryCatagoryType inventoryCatagoryType) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			Integer parentCategoryTypeID = inventoryCatagoryType.getParentCatagoryTypeID();
			
			 
			
			if(isUsedAsChildCategory(parentCategoryTypeID, databaseConnection)){
				throw new RequestFailureException("New category additon failed bacause parent category is already used as child category");
			}
			
			inventoryDAO.addInventoryCatagory(inventoryCatagoryType,databaseConnection);

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
	}
	
	private void isValidAddRequest(int categoryID,Long parentItemID,String[] attributeValues,long[] attributeNameIDs,String itemName,
			DatabaseConnection databaseConnection) throws Exception{
		
		if(attributeValues.length!=attributeNameIDs.length){
			throw new RequestFailureException("Number of attribuetValues ("+attributeValues.length+") and number of attribute name id("+attributeNameIDs.length+") does not match");
		}
		
		
		CRMInventoryCatagoryType inventoryCatagoryType = CRMInventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(categoryID);
		if(inventoryCatagoryType == null||inventoryCatagoryType.isDeleted()){
			throw new RequestFailureException("Not inventory category found with categoryID = "+categoryID);
		}
		
		CRMInventoryItem parentItem = null;
		if(parentItemID == null && inventoryCatagoryType.getParentCatagoryTypeID()!=null){
			throw new RequestFailureException("Invalid parent item is selected");
		}
		else if(parentItemID != null){
			parentItem = inventoryDAO.getCRMInventoryItemByItemID(parentItemID, databaseConnection);
		}
		
		
		
		
		if(parentItemID!=null &&     (parentItem==null || parentItem.isDeleted()) ) {
			throw new RequestFailureException("No parent item found with ID "+parentItemID);
		}
		
		
		if(parentItem != null){			
			if(!parentItem.getInventoryCatagoryTypeID().equals(inventoryCatagoryType.getParentCatagoryTypeID())){
				throw new RequestFailureException("Parent item with id = "+parentItemID+" should be a item of category with category id = "+inventoryCatagoryType.getParentCatagoryTypeID()+" while it is of category id "+parentItem.getInventoryCatagoryTypeID());
			}
		}
		int numberOfAttribueInCategory =CRMInventoryRepository.getInstance().getCRMInventoryAttributeNameListByCatagoryID(categoryID).size(); 
		
		if(numberOfAttribueInCategory!=attributeNameIDs.length){
			throw new RequestFailureException("Number of attribue name should be "+numberOfAttribueInCategory+" while the number is "+attributeNameIDs.length);
		}
		for(long inventoryAttributeNameID: attributeNameIDs){
			CRMInventoryAttributeName inventoryAttributeName = CRMInventoryRepository.getInstance().getCRMInventoryAttributeNameByNameID(inventoryAttributeNameID);
			if(inventoryAttributeName == null || inventoryAttributeName.isDeleted()){
				throw new Exception("No valid attribute name found with ID "+inventoryAttributeNameID);
			}
			
			if(inventoryAttributeName.getInventoryCatagoryTypeID()!=categoryID){
				throw new RequestFailureException("The requested attribue name shoud be a attribue of a category with id "+categoryID+" while it is "+inventoryAttributeName.getInventoryCatagoryTypeID());
			}
		}
	}
	private List<CRMInventoryAttributeValue> createCRMInventoryAttributeValueList(String[] attributeValues,long[] attributeNameIDs,long lastModificationTime,long inventoryItemID){
		List<CRMInventoryAttributeValue> inventoryAttributeValues = new ArrayList<CRMInventoryAttributeValue>();
		int ind = 0;
		for(long attributeNameID: attributeNameIDs){
			CRMInventoryAttributeValue inventoryAttributeValue = new CRMInventoryAttributeValue();
			inventoryAttributeValue.setInventoryAttributeNameID(attributeNameID);
			inventoryAttributeValue.setValue(attributeValues[ind++]);
			inventoryAttributeValue.setLastModificationTime(lastModificationTime);
			inventoryAttributeValue.setInventoryItemID(inventoryItemID);
			//inventoryAttributeValues.add()
		}
		return inventoryAttributeValues;
	}
	public void updateCRMInventoryItem(long inventoryItemID,String itemName,String[] attributeValues,long[] attributeNameIDs){
		long lastModificationTime = System.currentTimeMillis();
		// check attribute name ids with item ID

		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			
			CRMInventoryItem inventoryItem = new CRMInventoryItem();
			inventoryItem.setID(inventoryItemID);
			inventoryItem.setName(itemName);
			inventoryItem.setLastModificationTime(lastModificationTime);
			SqlGenerator.updateEntityByPropertyList(inventoryItem, CRMInventoryItem.class, databaseConnection, false, false, new String[]{"name","lastModificationTime"});
			
			
			
			for(int i=0;i<attributeNameIDs.length;i++){
				long attributeNameID = attributeNameIDs[i];
				String attributeVale = attributeValues[i];
				CRMInventoryAttributeValue inventoryAttributeValue = new CRMInventoryAttributeValue();
				inventoryAttributeValue.setID(attributeNameID);
				inventoryAttributeValue.setValue(attributeVale);
				inventoryAttributeValue.setLastModificationTime(lastModificationTime);
				SqlGenerator.updateEntityByPropertyList(inventoryAttributeValue, CRMInventoryAttributeValue.class, databaseConnection, false, false, new String[]{"value","lastModificationTime"});
			}
			
			databaseConnection.dbTransationEnd();
		}catch(Exception ex){
			logger.debug("fatal",ex);
			try{
				databaseConnection.dbTransationRollBack();
			}catch(Exception ex2){}
		}finally{
			databaseConnection.dbClose();
		}
		
		
	}

	
	
	public void addCRMInventoryItem(int catagoryID, Long parentItemID,String[] attributeValues, long[] attributeNameIDs,String itemName){
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			if(attributeNameIDs==null){
				attributeNameIDs = new long[0];
			}
			if(attributeValues==null){
				attributeValues = new String[0];
			}
			isValidAddRequest(catagoryID, parentItemID, attributeValues, attributeNameIDs, itemName, databaseConnection);
			long currentTime = System.currentTimeMillis();
			CRMInventoryItem inventoryItem = new CRMInventoryItem();
			inventoryItem.setInventoryCatagoryTypeID(catagoryID);
			inventoryItem.setParentID(parentItemID);
			inventoryItem.setDeleted(false);
			inventoryItem.setLastModificationTime(currentTime);
			inventoryItem.setName(itemName);
			if(!inventoryDAO.hasChildOfInventoryCategory(catagoryID, databaseConnection)){
				inventoryItem.setChildItem(true);
				inventoryItem.setIsUsed(false);
			}else{
				inventoryItem.setChildItem(false);
				inventoryItem.setIsUsed(null);
			}
			
			if (inventoryItem.getParentID() != null) {
				CRMInventoryItem parentCRMInventoryItem = inventoryDAO.getCRMInventoryItemByItemID(inventoryItem.getParentID(),
						DatabaseConnectionFactory.getCurrentDatabaseConnection());
				inventoryItem.setPathFromRootToParent(parentCRMInventoryItem.getPathFromRootToParent() + "/" + parentCRMInventoryItem.getID());
			} else {
				inventoryItem.setPathFromRootToParent("/");
			}
			
			inventoryDAO.addCRMInventoryItem(inventoryItem, databaseConnection);
			
			List<CRMInventoryAttributeValue> inventoryAttributeValues = new ArrayList<CRMInventoryAttributeValue>();
			long itemID = inventoryItem.getID();
			int ind = 0;
			for(String attributeValue: attributeValues){
				CRMInventoryAttributeValue inventoryAttributeValue = new CRMInventoryAttributeValue();
				long inventoryAttributeNameID = attributeNameIDs[ind++];
				inventoryAttributeValue.setInventoryAttributeNameID(inventoryAttributeNameID);
				inventoryAttributeValue.setInventoryItemID(itemID);
				inventoryAttributeValue.setDeleted(false);
				inventoryAttributeValue.setLastModificationTime(currentTime);
				inventoryAttributeValue.setValue(attributeValue);
				
				
				inventoryAttributeValues.add(inventoryAttributeValue);
			}
			
			for(CRMInventoryAttributeValue inventoryAttributeValue: inventoryAttributeValues){
				inventoryDAO.addInventoryValue(inventoryAttributeValue, databaseConnection);
			}
			
			
			databaseConnection.dbTransationEnd();
			// catch
		}catch(RequestFailureException ex){
			throw ex;
		}catch (Exception ex) {
			logger.debug("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
			throw new RequestFailureException("Inventory item add failed");
		} finally {
			databaseConnection.dbClose();
		}
		
	}
	public void updateCatagory(CRMInventoryCatagoryType inventoryCatagoryType) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			inventoryDAO.updateInventoryCatagory(inventoryCatagoryType,
					databaseConnection);

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
	}
	
	private boolean isDeleteable(CRMInventoryCatagoryTreeNode treeNode,DatabaseConnection databaseConnection) throws Exception{
		
		
		long catagoryID = treeNode.getCRMInventoryCatagoryDetailsRootNode().getCRMInventoryCatagoryType().getID();
		if(inventoryDAO.checkCRMInventoryItemByCatagoryID(catagoryID, databaseConnection)){
			return false; 
		}
		for(CRMInventoryCatagoryTreeNode childTreeNode: treeNode.getCRMInventoryCatagoryDetailsChildNodes()){			
			if(!isDeleteable(childTreeNode, databaseConnection)){
				return false;
			}
		}
		return true;
		
	}
	
	private void deleteTreeNode(CRMInventoryCatagoryTreeNode treeNode,DatabaseConnection databaseConnection,long lastModificationTime) throws Exception{
		CRMInventoryCatagoryType inventoryCatagoryType = treeNode.getCRMInventoryCatagoryDetailsRootNode().getCRMInventoryCatagoryType(); 
		inventoryCatagoryType.isDeleted = true;
		inventoryCatagoryType.lastModificationTime = lastModificationTime;
		inventoryDAO.updateInventoryCatagory(inventoryCatagoryType, databaseConnection);
		for(CRMInventoryAttributeName inventoryAttributeName:treeNode.getCRMInventoryCatagoryDetailsRootNode().getCRMInventoryAttributeNameList()){
			inventoryAttributeName.isDeleted = true;
			inventoryAttributeName.lastModificationTime = lastModificationTime;
			inventoryDAO.updateCRMInventoryAttributeName(inventoryAttributeName, databaseConnection);
		}
		for(CRMInventoryCatagoryTreeNode childNode: treeNode.getCRMInventoryCatagoryDetailsChildNodes()){
			deleteTreeNode(childNode, databaseConnection, lastModificationTime);
		}
	}
	
	public void deleteTreeNodeInCasecadeModeByCatagoryID(int catagoryID) throws Exception{
		CRMInventoryCatagoryTreeNode treeNode = CRMInventoryRepository.getInstance().getCRMInventoryCatagoryTreeNodeByCatagoryID(catagoryID);
		if(treeNode == null){
			throw new Exception("No catagory found by catagory ID "+catagoryID);
		}
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			if(!isDeleteable(treeNode, databaseConnection)){
				throw new Exception("Delete request is not possible . Because inventory item exists.");
			}
			long lastModificationTime = System.currentTimeMillis();
			deleteTreeNode(treeNode, databaseConnection, lastModificationTime);
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.fatal("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
			throw new Exception("Delete request is not possible . Because inventory item exists.");
		} finally {
			databaseConnection.dbClose();
		}
	}
	
	
	public void addItemList(List<CRMInventoryItem> inventoryItemList) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			for (CRMInventoryItem inventoryItem : inventoryItemList) {
				inventoryDAO
						.addCRMInventoryItem(inventoryItem, databaseConnection);
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
	}

	public void addCRMInventoryItemDetails(
			CRMInventoryItemDetails inventoryItemDetails) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			inventoryDAO.addCRMInventoryItem(
					inventoryItemDetails.getCRMInventoryItem(),
					databaseConnection);
			for (CRMInventoryAttributeValue inventoryAttributeValue : inventoryItemDetails
					.getCRMInventoryAttributeValues()) {
				inventoryDAO.addInventoryValue(inventoryAttributeValue,
						databaseConnection);
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
	}

	public void updateCRMInventoryItemDetails(
			CRMInventoryItemDetails inventoryItemDetails) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			inventoryDAO.updateCRMInventoryItem(
					inventoryItemDetails.getCRMInventoryItem(),
					databaseConnection);
			for (CRMInventoryAttributeValue inventoryAttributeValue : inventoryItemDetails
					.getCRMInventoryAttributeValues()) {
				inventoryDAO.updateInventoryValue(inventoryAttributeValue,
						databaseConnection);
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
	}

	public void updateItem(CRMInventoryItem inventoryItem) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			inventoryDAO.updateCRMInventoryItem(inventoryItem, databaseConnection);

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
	}

	public void addAttributeNameList(
			List<CRMInventoryAttributeName> inventoryAttributeNames) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			for (CRMInventoryAttributeName inventoryAttributeName : inventoryAttributeNames) {
				inventoryDAO.addCRMInventoryAttributeName(inventoryAttributeName,
						databaseConnection);
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
	}

	public void updateAttributeName(
			CRMInventoryAttributeName inventoryAttributeName) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			inventoryDAO.updateCRMInventoryAttributeName(inventoryAttributeName,
					databaseConnection);

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
	}

	public void addAttributeValueList(
			List<CRMInventoryAttributeValue> inventoryAttributeValues) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			for (CRMInventoryAttributeValue inventoryAttributeValue : inventoryAttributeValues) {
				inventoryDAO.addInventoryValue(inventoryAttributeValue,
						databaseConnection);
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
	}

	public void updateAttributeValue(
			CRMInventoryAttributeValue inventoryAttributeValue) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			inventoryDAO.updateInventoryValue(inventoryAttributeValue,
					databaseConnection);

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
	}
/*
	public void deleteCatagoryType(CRMInventoryCatagoryType inventoryCatagoryType) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			deleteCRMInventoryCatagoryType(inventoryCatagoryType,
					databaseConnection);

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
	}*/
/*
	public void deleteCRMInventoryItem(long inventoryItemID) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			CRMInventoryItem inventoryItem = inventoryDAO.getCRMInventoryItemByItemID(inventoryItemID, databaseConnection);

			if(inventoryDAO.hasChildOfCRMInventoryItem(inventoryItemID,databaseConnection)){
				throw new Exception("Inventory item deletion failed bacause of child Item(s).");
			}
			
			inventoryDAO.deleteCRMInventoryAttributeValueByItemID(inventoryItemID, databaseConnection);
			inventoryDAO.deleteCRMInventoryItemByItemID(inventoryItemID, databaseConnection);
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
	}
	
	
	*/
/*
	public void deleteCRMInventoryItem(CRMInventoryItem inventoryItem) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			deleteCRMInventoryItem(inventoryItem, databaseConnection);

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
	}*/
/*
	public void deleteAttributeName(
			CRMInventoryAttributeName inventoryAttributeName) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			deleteInventoryAttrivuteName(inventoryAttributeName,
					databaseConnection);

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
	}*/

	public void deleteAttrivuteValue(
			CRMInventoryAttributeValue inventoryAttributeValue) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			deleteCRMInventoryAttributeValue(inventoryAttributeValue,
					databaseConnection);

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
	}
/*
	private void deleteCRMInventoryCatagoryType(
			CRMInventoryCatagoryType inventoryCatagoryType,
			DatabaseConnection databaseConnection) throws Exception {
		
		List<CRMInventoryItem> inventoryItems = inventoryDAO.getCRMInventoryItemLisByCatagoryID(inventoryCatagoryType.getID(), databaseConnection);
		
		for (CRMInventoryItem inventoryItem : inventoryItems) {
			deleteCRMInventoryItem(inventoryItem);
		}
		inventoryDAO.deleteCRMInventoryCatagoryType(inventoryCatagoryType,
				databaseConnection);
	}*/

	/*
	 * deletes inventory item , its attribute values and child items
	 * */
	
	
	
	
	/*
	private void deleteCRMInventoryItem(long inventoryItemID,
			DatabaseConnection databaseConnection) throws Exception {

		
		if(inventoryDAO.hasChildOfCRMInventoryItem(inventoryItemID,databaseConnection)){
			throw new Exception("Inventory item deletion failed bacause of child Item(s).");
		}
		
		inventoryDAO.deleteCRMInventoryAttributeValueByItemID(inventoryItemID, databaseConnection);
		inventoryDAO.deleteCRMInventoryItemByItemID(inventoryItemID, databaseConnection);
	}*/
/*
	private void deleteInventoryAttrivuteName(
			CRMInventoryAttributeName inventoryAttributeName,
			DatabaseConnection databaseConnection) throws Exception {
		CRMInventoryRepository inventoryRepository = CRMInventoryRepository
				.getInstance();
		List<CRMInventoryAttributeValue> inventoryAttributeValueList = inventoryRepository.getCRMInventoryAttributeValuesByAttributeNameID(inventoryAttributeName.getID());
		for (CRMInventoryAttributeValue inventoryAttributeValue : inventoryAttributeValueList) {
			deleteAttrivuteValue(inventoryAttributeValue);
		}
		inventoryDAO.deleteCRMInventoryAttributeName(inventoryAttributeName,
				databaseConnection);
	}*/

	private void deleteCRMInventoryAttributeValue(
			CRMInventoryAttributeValue inventoryAttributeValue,
			DatabaseConnection databaseConnection) throws Exception {
		inventoryDAO.deleteCRMInventoryAttributeValue(inventoryAttributeValue,
				databaseConnection);
	}
/*
	public List<CRMInventoryItem> getDescendantItemList(long ansestorItemID,
			long inventoryCatagoryTypeID) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		List<CRMInventoryItem> inventoryItems = new ArrayList<CRMInventoryItem>();
		try {
			CRMInventoryItem ansestorItem = inventoryDAO.getCRMInventoryItemByItemID(ansestorItemID, databaseConnection);
				
			CRMInventoryCatagoryType inventoryCatagoryType = CRMInventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(inventoryCatagoryTypeID);

			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			inventoryItems = inventoryDAO.getCRMInventoryItemListOfAnsestor(
					ansestorItem, inventoryCatagoryType, databaseConnection);

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
		return inventoryItems;
	}*/
	public CRMInventoryItemDetails getCRMInventoryItemDetailsByItemID(long inventoryItemID){
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		CRMInventoryItemDetails inventoryItemDetails = null;
		try {

			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			inventoryItemDetails = new CRMInventoryItemDetails();
			CRMInventoryItem inventoryItem = inventoryDAO.getCRMInventoryItemByItemID(inventoryItemID, databaseConnection);
			inventoryItemDetails.setCRMInventoryItem(inventoryItem);
			List<CRMInventoryAttributeValue> inventoryAttributeValueList = inventoryDAO.getCRMInventoryAttributeValueListByItemID(inventoryItemID, databaseConnection);
			inventoryItemDetails.setCRMInventoryAttributeValues(inventoryAttributeValueList);
			List<CRMInventoryAttributeName> inventoryAttributeNames =  CRMInventoryRepository.getInstance().getCRMInventoryAttributeNameListByCatagoryID(inventoryItem.getInventoryCatagoryTypeID());
			inventoryItemDetails.setCRMInventoryAttributeNames(inventoryAttributeNames);
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
		return inventoryItemDetails;
	}/*
	public void updateOrInsertCRMInventoryItemDetails(CRMInventoryItemDetails inventoryItemDetails){
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {

			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			if(inventoryDAO.getCRMInventoryItemByItemID(inventoryItemDetails.getCRMInventoryItem().getID(), databaseConnection)!=null){
				inventoryDAO.updateCRMInventoryItem(inventoryItemDetails.getCRMInventoryItem(), databaseConnection);
			}else{
				inventoryDAO.addCRMInventoryItem(inventoryItemDetails.getCRMInventoryItem(), databaseConnection);
			}
			inventoryDAO.deleteCRMInventoryAttributeValueByItemID(inventoryItemDetails.getCRMInventoryItem().getID(), databaseConnection);
			for(CRMInventoryAttributeValue inventoryAttributeValue: inventoryItemDetails.getCRMInventoryAttributeValues()){
				inventoryAttributeValue.setCRMInventoryItemID(inventoryItemDetails.getCRMInventoryItem().getID());
				inventoryDAO.addInventoryValue(inventoryAttributeValue, databaseConnection);
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
	}
	
	public void deleteCRMInventoryItemDetails(long inventoryItemID){
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {

			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			inventoryDAO.deleteCRMInventoryAttributeValueByItemID(inventoryItemID, databaseConnection);
			inventoryDAO.deleteCRMInventoryItemByItemID(inventoryItemID, databaseConnection);

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
	}
	
	*/

	public List<CRMInventoryItemDetails> getCRMInventoryItemDetailsListByCatagoryIDAndParentItemID(int catagoryID,long parentItemID){
		List<CRMInventoryItemDetails> inventoryItemDetailsList = new ArrayList<CRMInventoryItemDetails>();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {

			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			/*
			List<CRMInventoryItem> inventoryItemList = inventoryDAO.getCRMInventoryItemListByParentItemAndCatagoryID(parentItemID, catagoryID, databaseConnection);
			
			for(CRMInventoryItem inventoryItem: inventoryItemList){
				CRMInventoryItemDetails inventoryItemDetails = new CRMInventoryItemDetails();
				List<CRMInventoryAttributeValue> inventoryAttributeValues = inventoryDAO.getCRMInventoryAttributeValueListByItemID(inventoryItem.getID(), databaseConnection);
				inventoryItemDetails.setCRMInventoryItem(inventoryItem);
				inventoryItemDetails.setCRMInventoryAttributeValues(inventoryAttributeValues);
				inventoryItemDetailsList.add(inventoryItemDetails);
			}
			*/
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
		return inventoryItemDetailsList;
	}

	public List<CRMInventoryItem> getCRMInventoryItemForAutoComplete(int categoryID,Long parentItemID,String partialName){
		List<CRMInventoryItem> inventoryItems = new ArrayList<CRMInventoryItem>();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {

			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			inventoryItems = inventoryDAO.getCRMInventoryItemListForAutoComplete(parentItemID, categoryID, partialName, databaseConnection);
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
		return inventoryItems;
}











public List<CRMInventoryItem> getCRMInventoryItemListByCatagoryIDAndParentItemID(int catagoryID,long parentItemID){
	List<CRMInventoryItem> inventoryItemList = new ArrayList<CRMInventoryItem>();

	DatabaseConnection databaseConnection = new DatabaseConnection();
	try {

		databaseConnection.dbOpen();
		databaseConnection.dbTransationStart();

		
		inventoryItemList = inventoryDAO.getCRMInventoryItemListByParentItemAndCatagoryID(parentItemID, catagoryID, databaseConnection);

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
	return inventoryItemList;
}

	public HashMap<String, List<Long>> getAvailableChildItemsByAncestoryIDAndCategoryID(int categoryID, long ancestorItemID)
	{
		logger.debug("categoryID "+ categoryID +" "+ ancestorItemID);
		HashMap<String, List<Long>> catItemMap = new HashMap<String, List<Long>>();
		
		CRMInventoryCatagoryTreeNode catTreeNode = CRMInventoryRepository.getInstance().getCRMInventoryCatagoryTreeNodeByCatagoryID(categoryID);
		List<CRMInventoryCatagoryTreeNode> childList = catTreeNode.getCRMInventoryCatagoryDetailsChildNodes();
		for(CRMInventoryCatagoryTreeNode child: childList)
		{
			CRMInventoryCatagoryDetails categoryDetails = child.getCRMInventoryCatagoryDetailsRootNode();
			int childCategoryID = categoryDetails.getCRMInventoryCatagoryType().getID();
			List<Long> list = getDescendantIDListByAnsestorIDAndDescendantCategoryID(ancestorItemID, childCategoryID);
			if(list!=null && list.size() > 0)
			{
				catItemMap.put(categoryDetails.getCRMInventoryCatagoryType().getName(), list);
			}
		}
		return catItemMap;
	}
	public List<Long> getDescendantIDListByAnsestorIDAndDescendantCategoryID(Long ansestorItemID,int descendantCategoryID){
		List<Long> returnIDList = new ArrayList<Long>();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {

			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			returnIDList = inventoryDAO.getUnusedDescendantIDListByAnsestorIDAndDescendantCategoryID(ansestorItemID, descendantCategoryID, databaseConnection);

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
		return returnIDList;
	}
	

	public CRMInventoryItem getCRMInventoryItemByItemID(long inventoryItemID){
		CRMInventoryItem itemDTO=null;
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {

			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			itemDTO=inventoryDAO.getCRMInventoryItemByItemID(inventoryItemID, databaseConnection);
			databaseConnection.dbTransationEnd();
		}catch(RequestFailureException ex){ 
			throw ex;
		}catch (Exception ex) {
			logger.fatal("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
		} finally {
			databaseConnection.dbClose();
		}
		if(itemDTO==null){
			itemDTO= new CRMInventoryItem();
		}
		return itemDTO;
	}
	
	
	public List<Long> deleteCRMInventoryItemByItemID(long inventoryItemID){
		List<Long> returnIDList = new ArrayList<Long>();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {

			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			if(inventoryDAO.hasChildOfCRMInventoryItem(inventoryItemID, databaseConnection)){
				throw new RequestFailureException("Inventory item with id "+inventoryItemID+" can not be deleted because it has child item(s)");
			}
			long lastModificationTime = System.currentTimeMillis();
			inventoryDAO.deleteCRMInventoryAttributeValueByItemID(inventoryItemID, databaseConnection, lastModificationTime);
			inventoryDAO.deleteCRMInventoryItemByItemID(inventoryItemID, databaseConnection);
			
			databaseConnection.dbTransationEnd();
		}catch(RequestFailureException ex){ 
			throw ex;
		}catch (Exception ex) {
			logger.fatal("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
		} finally {
			databaseConnection.dbClose();
		}
		return returnIDList;
	}
	


	
	
	public void markCRMInventoryItemAsUsed(long inventoryItemID){
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {

			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			if(inventoryDAO.hasChildOfCRMInventoryItem(inventoryItemID, databaseConnection)){
				throw new RequestFailureException("Inventory item with item id "+inventoryItemID+" can not be used because it has child item(s)");
			}
			
			
			int numOfAffectedRows = 0;
			
			synchronized (mutexLock) {

				CRMInventoryItem inventoryItem = inventoryDAO.getCRMInventoryItemByItemID(inventoryItemID, databaseConnection);
				if(inventoryItem==null || inventoryItem.isDeleted()){
					throw new RequestFailureException("No inventory item found with ID "+inventoryItemID);
				}
				if(!inventoryItem.isChildItem()){
					throw new RequestFailureException("The item is not a child item. Hence it can not be executed");
				}
				if(inventoryItem.getIsUsed()){
					throw new RequestFailureException("The item with id "+inventoryItemID+" is already used");
				}
				inventoryItem.setLastModificationTime(System.currentTimeMillis());
				inventoryItem.setIsUsed(true);
				numOfAffectedRows = inventoryDAO.updateCRMInventoryItem(inventoryItem, databaseConnection);
			}
			
			
			if(numOfAffectedRows==0){
				throw new RequestFailureException("Inventory item with ID "+inventoryItemID+" can not be used");
			}
			databaseConnection.dbTransationEnd();
		}catch(RequestFailureException ex){ 
			throw ex;
		}catch (Exception ex) {
			logger.fatal("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
			throw new RequestFailureException("Item with ID "+inventoryItemID+" can not be set as used");
		} finally {
			databaseConnection.dbClose();
		}
	}
	public void forceFullyAddCategory(){
		
	}
	public void forceFullyDeleteCategory(){
		
	}

}