
package crm.inventory;


import connection.DatabaseConnection;
import crm.inventory.repository.CRMInventoryRepository;

import static util.SqlGenerator.*;

import java.sql.*;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import common.RequestFailureException;
import util.CurrentTimeFactory;
import util.DatabaseConnectionFactory;
import util.KeyValuePair;
public class CRMInventoryDAO {
	Logger logger = Logger.getLogger(getClass());
	private static final Integer mutexLock = new Integer(0);
	/*
	 * Return type is the primaryKey of InventoryCatagory
	 * 
	 * */
	
	public boolean isAncestor(long ancestorID, long descendantID) throws Exception{
		Class classObject = CRMInventoryItem.class;
		String sql = "SELECT 1 FROM " + getTableName(classObject) + " WHERE "+ getColumnName(classObject, "pathFromRootToParent") + " LIKE ? AND " + getColumnName(classObject, "ID") + " = ?" ;
		PreparedStatement ps = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewPrepareStatement(sql);
		ps.setString(1, "%/"+ancestorID+"/%");
		ps.setLong(2, descendantID);
		ResultSet rs = ps.executeQuery();
		return rs.next();
	} 
	
	public long addInventoryCatagory(CRMInventoryCatagoryType inventoryCatagoryType,DatabaseConnection databaseConnection) throws Exception{
		insert(inventoryCatagoryType, inventoryCatagoryType.getClass(), databaseConnection,false);
		return inventoryCatagoryType.getID();
	}
	public void updateInventoryCatagory(CRMInventoryCatagoryType inventoryCatagoryType,DatabaseConnection databaseConnection) throws Exception{
		updateEntity(inventoryCatagoryType, inventoryCatagoryType.getClass(), databaseConnection,false,false);
	}
	/*
	 * Returns the primaryKey of CRMInventoryAttributeName
	 * */
	public long addCRMInventoryAttributeName(CRMInventoryAttributeName inventoryAttributeName,DatabaseConnection databaseConnection) throws Exception{
		insert(inventoryAttributeName,inventoryAttributeName.getClass(),databaseConnection,false);
		return inventoryAttributeName.getID();
	} 
	
		
	public void deleteInventoryAttributeByCatagoryID(long catagoryID,DatabaseConnection databaseConnection,long lastModificationTime) throws Exception{
		
		String sql = "update "+getTableName(CRMInventoryAttributeName.class)+" set "+getColumnName(CRMInventoryAttributeName.class, "isDeleted")+" = 1 , "+getColumnName(CRMInventoryAttributeName.class,"lastModificationTime" )+"="+lastModificationTime+" where "+getColumnName(CRMInventoryAttributeName.class, "inventoryCatagoryTypeID")+" = "+catagoryID;
		Statement stmt = databaseConnection.getNewStatement();
		stmt.execute(sql);
		
		updateSequencerTable(CRMInventoryAttributeName.class, databaseConnection, lastModificationTime);
	}
	
	public void updateCRMInventoryAttributeName(CRMInventoryAttributeName inventoryAttributeName,DatabaseConnection databaseConnection) throws Exception{
		updateEntity(inventoryAttributeName,inventoryAttributeName.getClass(),databaseConnection,false,false);
	}
	/*
	 * Returns primary key of CRMInventoryItem 
	 * */
	public long addCRMInventoryItem(CRMInventoryItem inventoryItem,DatabaseConnection databaseConnection) throws Exception{
		insert(inventoryItem,inventoryItem.getClass(),databaseConnection,false);
		return inventoryItem.getID();
	}
	
	public int updateCRMInventoryItem(CRMInventoryItem inventoryItem,DatabaseConnection databaseConnection) throws Exception{
		return updateEntity(inventoryItem,inventoryItem.getClass(),databaseConnection,false,false);
	}
	/*
	 * Returns primary key CRMInventoryAttributeValue
	 * */
	public long addInventoryValue(CRMInventoryAttributeValue inventoryAttributeValue,DatabaseConnection databaseConnection) throws Exception{
		System.out.println("before inserting inventoryAttribute value ");
		insert(inventoryAttributeValue, CRMInventoryAttributeValue.class, databaseConnection,false);
		System.out.println("after inserting inventoryAttribute value ");
		return inventoryAttributeValue.getID();
	}
	
	public void updateInventoryValue(CRMInventoryAttributeValue inventoryAttributeValue,DatabaseConnection databaseConnection) throws Exception{
		updateEntity(inventoryAttributeValue, inventoryAttributeValue.getClass(), databaseConnection,false,false);
	}
	
	
	
	
	public void deleteCRMInventoryCatagoryType(CRMInventoryCatagoryType inventoryCatagoryType,DatabaseConnection databaseConnection) throws Exception{
		deleteEntity(inventoryCatagoryType, inventoryCatagoryType.getClass(), databaseConnection,false);
	}
	public void deleteInventoryCategoryTypeByCategoryID(long categoryID,long lastModificationTime) throws Exception{
		deleteEntityByID(CRMInventoryCatagoryType.class, categoryID, lastModificationTime, DatabaseConnectionFactory.getCurrentDatabaseConnection());
	}
	/*
	 * This only deletes the item. It does not delete its child(ren)
	 * */
	public void deleteCRMInventoryItem(CRMInventoryItem inventoryItem,DatabaseConnection databaseConnection) throws Exception{
		deleteEntity(inventoryItem, inventoryItem.getClass(), databaseConnection,false);
	}
	public void deleteCRMInventoryAttributeValue(CRMInventoryAttributeValue inventoryAttributeValue,DatabaseConnection databaseConnection) throws Exception{
		deleteEntity(inventoryAttributeValue, inventoryAttributeValue.getClass(), databaseConnection,false);
	}
	public void deleteCRMInventoryAttributeValueByItemID(long inventoryItemID,DatabaseConnection databaseConnection,long lastModificationTime) throws Exception{
		String sql = "update "+getTableName(CRMInventoryAttributeValue.class)+" set "+getColumnName(CRMInventoryAttributeValue.class, "isDeleted")+ " = 1 where "+getColumnName(CRMInventoryAttributeValue.class, "inventoryItemID")+" = "+inventoryItemID;
		Statement stmt = databaseConnection.getNewStatement();
		stmt.execute(sql);
		updateSequencerTable(CRMInventoryAttributeValue.class, databaseConnection, lastModificationTime);
	}
	public void deleteCRMInventoryAttributeName(CRMInventoryAttributeName inventoryAttributeName,DatabaseConnection databaseConnection) throws Exception{
		deleteEntity(inventoryAttributeName, inventoryAttributeName.getClass(), databaseConnection,false);
	}
	/*
	public long getAnsestorID(long itemID,int ansestorLevel) throws Exception{
		if(ansestorLevel<0){
			throw new Exception("Negative ansestor level not allowed");
		} 
		if(ansestorLevel==0){
			return itemID;
		}
		CRMInventoryItem inventoryItem = CRMInventoryRepository.getInstance().getCRMInventoryItemByItemID(itemID);
		return getAnsestorID(inventoryItem.getParentID(), ansestorLevel-1);
	}*/
	private List<Long> getItemIDListByLevelAndCatagory(long catagoryID,DatabaseConnection databaseConnection) throws Exception{
		List<Long> IDList = new ArrayList();
		String tableName = getTableName(CRMInventoryItem.class);
		String IDColumnName = getColumnName(CRMInventoryItem.class, "ID");
		String catagoryIDColumnName  =getColumnName(CRMInventoryItem.class,"inventoryCatagoryTypeID");
		String sql = "select "+IDColumnName+" from "+tableName+" where "+catagoryIDColumnName + " = "+catagoryID;
				
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			IDList.add(rs.getLong(1));
		}
		return IDList;
	}/*
	public List<CRMInventoryItem> getCRMInventoryItemListOfAnsestor(CRMInventoryItem ansestorItem,
			CRMInventoryCatagoryType inventoryCatagoryType,DatabaseConnection databaseConnection) throws Exception{
		List<CRMInventoryItem> inventoryItems = new ArrayList<CRMInventoryItem>();
		List<Long> decendantItemIDList = getItemIDListByLevelAndCatagory(inventoryCatagoryType.getID(), databaseConnection);
		int ansestorLevel = CRMInventoryRepository.getInstance().getInventoryCatagoryByCatagoryID(ansestorItem.getCRMInventoryCatagoryTypeID()).getLevel();
		for(Long decendantItemID: decendantItemIDList){
			if(ansestorItem.getID()== getAnsestorID(decendantItemID, ansestorLevel-inventoryCatagoryType.getLevel())){
				inventoryItems.add(CRMInventoryRepository.getInstance().getCRMInventoryItemByItemID(decendantItemID));
			}
		}
		return inventoryItems;
	}*/
	public List<CRMInventoryAttributeValue> getCRMInventoryAttributeValueListByItemID(long itemID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString =  " where "+getColumnName(CRMInventoryAttributeValue.class, "inventoryItemID")+" = "+itemID+ " and "+getColumnName(CRMInventoryAttributeValue.class, "isDeleted")+" = 0";
		return (List<CRMInventoryAttributeValue>)getAllObjectList(CRMInventoryAttributeValue.class, databaseConnection, conditionString);
	}
	public CRMInventoryCatagoryType getInventoryCatagoryByCatagoryID(long inventoryCatagoryID,DatabaseConnection databaseConnection) throws Exception{
		return (CRMInventoryCatagoryType)getObjectByID(CRMInventoryCatagoryType.class, inventoryCatagoryID, databaseConnection);
	}
	
	public List<CRMInventoryItem> getChildItemListByParentItemID(long parentItemID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = " where "+getColumnName(CRMInventoryItem.class, "parentID")+" = "+parentItemID + " and "+ getColumnName(CRMInventoryItem.class, "isDeleted") + " = 0";
		return (List<CRMInventoryItem>)getAllObjectList(CRMInventoryItem.class, databaseConnection, conditionString);
	}
	
	public List<CRMInventoryAttributeValue> getCRMInventoryAttributeValuesByAttributeNameID(long inventoryAttributeNameID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = " where "+getColumnName(CRMInventoryAttributeValue.class, "")+" = "+inventoryAttributeNameID;
		return (List<CRMInventoryAttributeValue>)getAllObjectList(CRMInventoryAttributeValue.class, databaseConnection, conditionString);
	}
	public List<CRMInventoryItem> getCRMInventoryItemLisByCatagoryID(long catagoryID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString =  " where "+getColumnName(CRMInventoryItem.class, "")+ " = "+catagoryID;
		return (List<CRMInventoryItem>)getAllObjectList(CRMInventoryItem.class, databaseConnection, conditionString);
	}
	public CRMInventoryItem getCRMInventoryItemByItemID(long itemID,DatabaseConnection databaseConnection) throws Exception{
		return (CRMInventoryItem)getObjectByID(CRMInventoryItem.class, itemID, databaseConnection);
	}
	public List<CRMInventoryCatagoryType> getChildItemCatagoryListByParentItemID(long parentItemID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = " where "+getColumnName(CRMInventoryCatagoryType.class, "parentCatagoryTypeID")+" = "+parentItemID + " and "+ getColumnName(CRMInventoryCatagoryType.class, "isDeleted") + " = 0" ;
		return (List<CRMInventoryCatagoryType>)getAllObjectList(CRMInventoryCatagoryType.class, databaseConnection, conditionString);
	}
	public List<CRMInventoryItem> getItemListByParentItemID(long parentItemID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = " where "+getColumnName(CRMInventoryItem.class, "parentID")+" = "+parentItemID;
		return (List<CRMInventoryItem>)getAllObjectList(CRMInventoryItem.class, databaseConnection, conditionString);
	}
	public List<CRMInventoryItem> getCRMInventoryItemListByParentItemAndCatagoryID(Long parentID,int catagoryID,String partialItemName,DatabaseConnection databaseConnection) throws Exception{
		String parentItemColumnName = getColumnName(CRMInventoryItem.class, "parentID"); 
		Class classObject = CRMInventoryItem.class;
		String conditionString = " where "+
				    parentItemColumnName +  (parentID!=null?    " = "+parentID  :" is null")+
					" and "+getColumnName(classObject, "inventoryCatagoryTypeID")+" = "+catagoryID+
					" and "+getColumnName(classObject, "name")+" like '%"+parentID+"%'";
		
		//System.out.println("conditionString : "+conditionString);
		return (List<CRMInventoryItem>)getAllObjectList(CRMInventoryItem.class, databaseConnection, conditionString);
	}
	
	public List<Long> getCRMInventoryItemIDListByParentItemAndCatagoryID(Long parentID,int categoryID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = getConditionStringForCRMInventoryItemByParentItemListAndCategoryID(parentID, categoryID, databaseConnection);
		Class classObject = CRMInventoryItem.class;
		boolean hasChild = hasChildOfInventoryCategory(categoryID, databaseConnection);
		if(!hasChild){
			String isUsedColumName = getColumnName(classObject, "isUsed");
			conditionString+=(" and "+isUsedColumName+" = 0");
		}
		
		return (List<Long>)getAllIDList(classObject, databaseConnection, conditionString);
	}
	
	public List<CRMInventoryItem> getCRMInventoryItemListByParentItemAndCatagoryID(Long parentID,int catagoryID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = getConditionStringForCRMInventoryItemByParentItemListAndCategoryID(parentID, catagoryID, databaseConnection);
		return (List<CRMInventoryItem>)getAllObjectList(CRMInventoryItem.class, databaseConnection, conditionString);
	}
	
	private String getConditionStringForCRMInventoryItemByParentItemListAndCategoryID(Long parentID,int catagoryID,DatabaseConnection databaseConnection) throws Exception{
		Class classObject = CRMInventoryItem.class;
		String parentItemColumnName = getColumnName(classObject, "parentID"); 
		
		String conditionString = " where "+ parentItemColumnName +  (parentID!=null?    " = "+parentID  :" is null")+
					" and "+getColumnName(CRMInventoryItem.class, "inventoryCatagoryTypeID")+" = "+catagoryID+" and "+
				getColumnName(classObject, "isDeleted")+" = 0";
		return conditionString;
	}
	
	public List<Long> getCRMInventoryItemIDListByParentItemListAndCatagoryID(List<Long> parentIDList,long catagoryID,DatabaseConnection databaseConnection) throws Exception{
		if(parentIDList.isEmpty()){
			return new ArrayList<Long>();
		}
		Class classObject = CRMInventoryItem.class;
		String parentItemColumnName = getColumnName(classObject, "parentID"); 
		
		String conditionString = " where "+ 
					" and "+getColumnName(CRMInventoryItem.class, "inventoryCatagoryTypeID")+" = "+catagoryID+" and "+
				getColumnName(classObject, "isDeleted")+" = 0 "+parentItemColumnName+" in ( ";
		
		
		for(int i =0;i<parentIDList.size();i++){
			if(i!=0){
				conditionString+=",";
			}
			conditionString+=parentIDList.get(i);
		}
		conditionString+=")";
		
		return (List<Long>)getAllIDList(CRMInventoryItem.class, databaseConnection, conditionString);
	}
	
	public List<CRMInventoryAttributeName> getCRMInventoryAttributeNameListByCatagoryID(long catagoryID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = " where "+getColumnName(CRMInventoryAttributeName.class, "inventoryCatagoryTypeID")+ " = " + catagoryID;
		return (List<CRMInventoryAttributeName>)getAllObjectList(CRMInventoryAttributeName.class, databaseConnection, conditionString);
	}
	public void deleteCRMInventoryItemByItemID(long inventoryItemID,DatabaseConnection databaseConnection) throws Exception{

		deleteEntityByID(CRMInventoryItem.class,  inventoryItemID,CurrentTimeFactory.getCurrentTime(),databaseConnection);

	}
	public boolean checkCRMInventoryItemByCatagoryID(long catagoryID,DatabaseConnection databaseConnection) throws Exception{
		String sql = "select "+getColumnName(CRMInventoryItem.class, "ID")+" from "+getTableName(CRMInventoryItem.class)+
				" where "+getColumnName(CRMInventoryItem.class, "inventoryCatagoryTypeID")+" = "+catagoryID+" and "+getColumnName(CRMInventoryItem.class, "isDeleted")+" = 0";
		ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);
		if(rs.next()){
			return true;
		}else{
			return false;
		}
		
	}
	public boolean hasAnyCRMInventoryItemOfCategory(Integer categoryID,DatabaseConnection databaseConnection) throws Exception{
		Class classObject = CRMInventoryItem.class;
		String sql = "select exists ( select 1 from "+getTableName(classObject)+" where "+getColumnName(classObject, "inventoryCatagoryTypeID")+" = "+categoryID+" and "+ getColumnName(classObject, "isDeleted")+ " = 0" +")";;
		ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);
		rs.next();
		return rs.getInt(1)==1;
	}
	public boolean hasChildOfCRMInventoryItem(long itemID,DatabaseConnection databaseConnection) throws Exception{
		
		String sql = "select exists (select 1 from "+getTableName(CRMInventoryItem.class)+" where "+getColumnName(CRMInventoryItem.class, "parentID")+ " = "+itemID+" and "+ getColumnName(CRMInventoryItem.class, "isDeleted")+ " = 0" +")";
		ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);
		rs.next();
		return rs.getInt(1)==1;
	}

	public boolean hasChildOfInventoryCategory(long categoryID,DatabaseConnection databaseConnection) throws Exception{
		Class classObject = CRMInventoryCatagoryType.class;
		String sql = "select exists (select 1 from "+getTableName(classObject)+" where "+getColumnName(classObject, "parentCatagoryTypeID")+ " = "+categoryID+" and "+ getColumnName(classObject, "isDeleted")+ " = 0" +")";
		System.out.println("has child sql: "+sql);
		ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);
		rs.next();
		return rs.getInt(1)==1;
	}
	
	// if validItemIDList is null then this search will return all matched items otherwise the the items will be filtered by the IDList
	
	private List<Long> getItemIDListByPartialAttributeValueAndValidItemIDList(List<Long> validItemIDList,long attributeNameID,String partialAttributeValue,DatabaseConnection databaseConnection ) throws Exception{
		
		if(validItemIDList!=null && validItemIDList.isEmpty()){
			return new ArrayList<Long>();
		}
		String tableName = getTableName(CRMInventoryAttributeValue.class);
		String inventoryItemIDColumnName = getColumnName(CRMInventoryAttributeValue.class, "inventoryItemID");
		String inventoryNameIDCoulmnName = getColumnName(CRMInventoryAttributeValue.class, "inventoryAttributeNameID");
		String inventoryValueColumnName = getColumnName(CRMInventoryAttributeValue.class, "value");
		String sql = "select "+inventoryItemIDColumnName +" from "+tableName+" where "+inventoryNameIDCoulmnName + " = "+attributeNameID+
				(partialAttributeValue.trim().equals("")?"":" and "+inventoryValueColumnName+" like "+" '%"+partialAttributeValue+"%' ");
				
		
		
		StringBuilder tmp = new StringBuilder();
		if(validItemIDList!=null){
			sql+=" and "+inventoryItemIDColumnName+ " IN ( ";
			for(int i=0;i<validItemIDList.size();i++){
				if(i!=0){
					tmp.append(",");
				}
				tmp.append(validItemIDList.get(i));
			}
			tmp.append(") and " ).append(getColumnName(CRMInventoryAttributeValue.class, "isDeleted")).append("=0");
		}
		sql = sql + tmp.toString();
		System.out.println("sql: "+sql);
		ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);
		List<Long> IDList = new ArrayList<Long>();
		while(rs.next()){
			IDList.add(rs.getLong(1));
		}
		
		return IDList;
	}
	private List<Long> intersection(List<List<Long>> listOfIDList){
		if(listOfIDList.isEmpty()){
			return new ArrayList<Long>();
		}else if(listOfIDList.size()==1){
			return listOfIDList.get(0);
		}
		Set<Long> idSet = new HashSet<Long>(listOfIDList.get(0));
		for(int i =1 ;i<listOfIDList.size();i++){
			Set<Long> tmpIDSet = new HashSet<Long>();
			List<Long> IDList = listOfIDList.get(i);
			for(Long ID: IDList){
				if(idSet.contains(ID)){
					tmpIDSet.add(ID);
				}
			}
			idSet = tmpIDSet;
		}
		List<Long> returnIDList = new ArrayList<Long>();
		returnIDList.addAll(idSet);
		return returnIDList;
	}
	public List<Long> getCRMInventoryItemIDsWIthSearchCriteria(Hashtable searchCriteria,DatabaseConnection databaseConnection) throws Exception{
		String parentItemIDString = (String)searchCriteria.get("parentItemID");
		String categoryIDString = (String)searchCriteria.get("categoryID");
		String itemName = (String)searchCriteria.get("itemName");
		List<List<Long>> listOfIDlist = new ArrayList<List<Long>>();
		Enumeration keys = searchCriteria.keys();
		
		long parentItemID = (parentItemIDString==null?null:Long.parseLong(parentItemIDString));
		int categoryID = Integer.parseInt(categoryIDString);
		
		List<CRMInventoryItem> inventoryItems = getCRMInventoryItemListByParentItemAndCatagoryID( parentItemID, categoryID,itemName, databaseConnection);
		
		List<Long> itemList = new ArrayList<Long>();
		for(CRMInventoryItem inventoryItem: inventoryItems){
			itemList.add(inventoryItem.getID());
		}
		
		System.out.println("valid item id list: "+itemList);
		while(keys.hasMoreElements()){
			Object key = keys.nextElement();
			if(key.toString().equals("parentItemID")|| key.toString().equals("categoryID")){
				continue;
			}
			
			
			List<Long> IDList = getItemIDListByPartialAttributeValueAndValidItemIDList(itemList,
					Long.parseLong((String)key),(String)searchCriteria.get(key), databaseConnection);
			
			listOfIDlist.add(IDList);
		}
		return intersection(listOfIDlist);
	}
	
	public List<CRMInventoryItem> getCRMInventoryItemListForAutoComplete(Long parentItemID,int categoryID,String partialName,DatabaseConnection databaseConnection) throws Exception{
		Class classObject = CRMInventoryItem.class;
		String conditionString = " where "+getColumnName(classObject, "parentID")+
				(parentItemID==null?" is null ":" = "+parentItemID)+
				" and "+getColumnName(classObject, "inventoryCatagoryTypeID")+" = "+categoryID+
				" and "+getColumnName(classObject, "name")+" like '%"+partialName+"%' ";
		return (List<CRMInventoryItem>)getAllObjectList(classObject, databaseConnection, conditionString);
	}
	
	
	// if parentItemList is null then this method will not filter the search result otherwise the result will be filtered by the parentItemList
	private List<Long> getUnusedCRMInventoryItemIDListByParentItemListAndCategoryID(List<Long> parentItemList,int categoryID,String itemPartialName,DatabaseConnection databaseConnection) throws Exception{
		if(parentItemList!=null && parentItemList.isEmpty()){
			return new ArrayList<Long>();
		}
		
		Class classObject = CRMInventoryItem.class;
		String parentItemColumnName = getColumnName(classObject, "parentID");
		String itemNameColumnName = getColumnName(classObject, "name");
		StringBuilder conditionStringBuider = new StringBuilder();
		conditionStringBuider.append(" where ")
							 .append(getColumnName(CRMInventoryItem.class, "inventoryCatagoryTypeID"))
							 .append(" = ")
							 .append(categoryID);
		if(!StringUtils.isBlank(itemPartialName)){
			conditionStringBuider.append(" and ")
								 .append(itemNameColumnName)
								 .append(" like ")
								 .append("'%")
								 .append(itemPartialName)
								 .append("%'");	
		}

		boolean hasChild = hasChildOfInventoryCategory(categoryID, databaseConnection);
		if(!hasChild){
			String isUsedColumName = getColumnName(classObject, "isUsed");
			conditionStringBuider.append(" and ").append(isUsedColumName).append(" = 0");
		}
		if(parentItemList!=null){
			conditionStringBuider.append(" and ").append(parentItemColumnName).append(" in ( ");
			
			for(int i=0;i<parentItemList.size();i++){
				if(i!=0){
					conditionStringBuider.append(",");
				}
				conditionStringBuider.append(parentItemList.get(i));
			}
			conditionStringBuider.append(")");
			
		}
		conditionStringBuider.append(" and ").append(getColumnName(classObject, "isDeleted")).append(" = 0");
		return getAllIDList(CRMInventoryItem.class, databaseConnection, conditionStringBuider.toString());
	
	}
	
	public List<Long> getUnusedDescendantIDListByAnsestorIDAndDescendantCategoryID(Long ansestorID,int descendantCategoryID,DatabaseConnection databaseConnection) throws Exception{
		List<Long> IDList = new ArrayList<Long>();
		CRMInventoryItem ansestorItem = (CRMInventoryItem)getObjectByID(CRMInventoryItem.class, ansestorID, databaseConnection);
		if(ansestorItem.getInventoryCatagoryTypeID() == descendantCategoryID){
			 IDList.add(ansestorID);
			 return IDList;
		}
		List<Integer> categoryPathIDList = CRMInventoryRepository.getInstance().getIDListPathFromAnsestorToDescendant(ansestorItem.getInventoryCatagoryTypeID(), descendantCategoryID); 

		List<Long> itemIDList = getCRMInventoryItemIDListByParentItemAndCatagoryID(ansestorID, categoryPathIDList.get(1), databaseConnection);
		
		for(int i = 2;i<categoryPathIDList.size();i++){
			itemIDList = getUnusedCRMInventoryItemIDListByParentItemListAndCategoryID(itemIDList, categoryPathIDList.get(i),"", databaseConnection);
		}
		return itemIDList;
	}
	public int makeAllItemAsChildItemByCategoryID(int categoryID,DatabaseConnection databaseConnection) throws Exception{
		Class classObject = CRMInventoryItem.class;
		String tablename = getTableName(classObject);
		String categoryIDColumnName = getColumnName(classObject, "");
		String isChildItemColumnName = getColumnName(classObject, "");
		String isUsedColumnName = getColumnName(classObject, "isUsed");
		
		String sql = " update "+tablename+" set "+isChildItemColumnName+" = 1 and "+isUsedColumnName+" = 1 where "+
					categoryIDColumnName+" = "+categoryID;
		int numOfUpdatedRows = databaseConnection.getNewStatement().executeUpdate(sql);
		return numOfUpdatedRows;
	}
	public int makeAllItemAsNonChildItemByCategoryID(int categoryID,DatabaseConnection databaseConnection) throws Exception{
		Class classObject = CRMInventoryItem.class;
		String tablename = getTableName(classObject);
		String categoryIDColumnName = getColumnName(classObject, "");
		String isChildItemColumnName = getColumnName(classObject, "");
		String isUsedColumnName = getColumnName(classObject, "isUsed");
		
		String sql = " update "+tablename+" set "+isChildItemColumnName+" = 0 and "+isUsedColumnName+" = null where "+
					categoryIDColumnName+" = "+categoryID;
		int numOfUpdatedRows = databaseConnection.getNewStatement().executeUpdate(sql);
		return numOfUpdatedRows;
	}
	public List<Long> getCRMInventoryItemIDList(List<KeyValuePair<Integer,String> >rootToItemPath,List<KeyValuePair<Long,String>> attributeNameContraintList,DatabaseConnection databaseConnection) throws Exception{
		
		
		List<Long> validItemIDList = null;
		
		for(KeyValuePair<Integer, String> categoryIDPartialNameValuePair: rootToItemPath){
			int categoryID = categoryIDPartialNameValuePair.key;
			String itemPartialName = categoryIDPartialNameValuePair.value;
			validItemIDList = getUnusedCRMInventoryItemIDListByParentItemListAndCategoryID(validItemIDList, categoryID,itemPartialName, databaseConnection);
		}
		
		for(KeyValuePair< Long, String> attributeNameValuePair: attributeNameContraintList){
			Long attributeNameID = attributeNameValuePair.key;
			String attributePartialValue = attributeNameValuePair.value;
			validItemIDList = getItemIDListByPartialAttributeValueAndValidItemIDList(validItemIDList, attributeNameID, attributePartialValue, databaseConnection);
		}
		
		return validItemIDList;
	}
	
	public List<Long> getAllCRMInventoryItemIDListByCategoryID(int categoryID,DatabaseConnection databaseConnection) throws Exception{
		
		Class classObject = CRMInventoryItem.class;
		String categoryIDColumnName = getColumnName(classObject, "inventoryCatagoryTypeID");
		String isDeletedColumnName = getColumnName(classObject, "isDeleted");
		String conditionString = " where "+categoryIDColumnName+" = "+categoryID+" and "+isDeletedColumnName+" = 0";
		return getAllIDList(classObject, databaseConnection, conditionString);
	}
	
	public int delteCRMInventoryAttributeNameByAttributeNameID(List<Long> inventoryAttributeNameIDList,long lastModificationTime,DatabaseConnection databaseConnection) throws Exception{
		Class classObject = CRMInventoryAttributeName.class;
		String IDColumnName = getColumnName(classObject, "");
		String tableName = getColumnName(classObject, "");
		String isDeletedColumnName = getColumnName(classObject, "isDeleted");
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("update ")
					.append(tableName)
					.append(" set ")
					.append(isDeletedColumnName)
					.append(" = 1 where ")
					.append(IDColumnName)
					.append(" IN ( ");
		for(int i = 0;i<inventoryAttributeNameIDList.size();i++){
			if(i != 0){
				sqlBuilder.append(",");
			}
			sqlBuilder.append(inventoryAttributeNameIDList.get(i));
		}
		
		sqlBuilder.append(")");

		updateSequencerTable(classObject, databaseConnection, lastModificationTime);
		return databaseConnection.getNewStatement().executeUpdate(sqlBuilder.toString());
		
	}
	
	public Map<Long,String> getCRMInventoryItemNameListByItemIDList(List<Long> IDList,DatabaseConnection databaseConnection) throws Exception{
		List<String> resultList = new ArrayList<String>();
		Map<Long,String> mapOfCRMInventoryItemNameToItemID =  new HashMap<Long, String>();
		if(IDList.isEmpty()){
			return mapOfCRMInventoryItemNameToItemID;
		}
		Class classObject = CRMInventoryItem.class;
		String IDColumnName = getColumnName(classObject, "ID");
		String nameColumName = getColumnName(classObject, "name");
		String sql = "select  "+IDColumnName+" , "+nameColumName+" from "+getTableName(classObject)+" where "+IDColumnName+" IN ";
		StringBuilder conditionBuilder = new StringBuilder("(");
		for(int i=0;i<IDList.size();i++){
			if(i!=0){
				conditionBuilder.append(",");
			}
			conditionBuilder.append(IDList.get(i));
		}
		conditionBuilder.append(")");
		sql = sql+conditionBuilder.toString();
		
		ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);
		while(rs.next()){
			long ID = rs.getLong(1);
			String name = rs.getString(2);
			mapOfCRMInventoryItemNameToItemID.put(ID, name);
		}
		return mapOfCRMInventoryItemNameToItemID;
	}
	public void markCRMInventoryItemAsUsedByItemID(long inventoryItemID,DatabaseConnection databaseConnection) throws Exception{
		
		
		if(hasChildOfCRMInventoryItem(inventoryItemID, databaseConnection)){
			throw new RequestFailureException("Inventory item with item id "+inventoryItemID+" can not be used because it has child item(s)");
		}
		
		
		int numOfAffectedRows = 0;
		
		synchronized (mutexLock) {

			CRMInventoryItem inventoryItem = getCRMInventoryItemByItemID(inventoryItemID, databaseConnection);
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
			numOfAffectedRows = updateCRMInventoryItem(inventoryItem, databaseConnection);
		}
		
		
		if(numOfAffectedRows==0){
			throw new RequestFailureException("Inventory item with ID "+inventoryItemID+" can not be used");
		}
		
		
	}
	
	public boolean isAnsestor(long ansestorID,long descendantID){ // implement this
		return true;
	}
	
	public List<Long> allocateItemAndReturnItemIDList(long ansestorID,int descendantCategoryID,int numberOfItems,DatabaseConnection databaseConnection) throws Exception{
		List<Long> unusedItemIDList = getUnusedDescendantIDListByAnsestorIDAndDescendantCategoryID(ansestorID, descendantCategoryID, databaseConnection);
		List<Long> allocatedIDList = new ArrayList<Long>();
		if(unusedItemIDList.size()<numberOfItems){
			throw new RequestFailureException("");
		}
		if(numberOfItems<1){
			throw new RequestFailureException("Invalid request. Number of item allocation should be greater than 1");
		}
		for(long inventoryItemID: unusedItemIDList){
			try{
				markCRMInventoryItemAsUsedByItemID(inventoryItemID, databaseConnection);
				allocatedIDList.add(inventoryItemID);
				if(allocatedIDList.size()==numberOfItems){
					break;
				}
			}catch(Exception ex){
				logger.debug("fatal",ex);
			}
		}
		
		return allocatedIDList;
	}
}
