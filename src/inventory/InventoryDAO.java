
package inventory;


import common.ClientDTOConditionBuilder;
import common.client.ClientDTO;
import connection.DatabaseConnection;
import global.GlobalService;
import inventory.repository.InventoryRepository;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationConditionBuilder;
import net.sf.jasperreports.web.servlets.ReportOutputServlet;

import static util.SqlGenerator.*;

import java.sql.*;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.healthmarketscience.jackcess.Database;

import common.CategoryConstants;
import common.EntityTypeConstant;
import common.RequestFailureException;
import util.*;


class RouterSwitchDTO{
	public long ID;
	public String type;
	public Set<Long> connectedDeviceIDList = new HashSet<>();
	public Set<Integer> usedVLanList = new HashSet<>();
}


public class InventoryDAO {
	Logger logger = Logger.getLogger(getClass());
	private static final Integer mutexLock = new Integer(0);
	/*
	 * Return type is the primaryKey of InventoryCatagory
	 * 
	 * */
	
	public List<InventoryItem> getInventoryItemDetailsFromRootByItemID(long itemID,DatabaseConnection databaseConnection) throws Exception{
		String sql = "select at_inventory_item.* from at_inventory_item join (select  @p id from (select @p := "+itemID+" union select 1 union select 2 union select 3 union select 4 union select 5  union select 6 union select 7 union select 8 union select 9 union select 10) tmp   where @p := (select invitParentItemID from at_inventory_item where invitID = @p)    ) idSet on (invitID = id) order by invitID";
		ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);
		return SqlGenerator.getObjectListByResultSet(InventoryItem.class, rs);
	}
	
	
	public List<InventoryItem> getInventoryItemListByItemNameCategoryIDParentID(String itemName,int categoryID,Long parentID) throws Exception{
		
		
		InventoryCatagoryType inventoryCatagoryType = InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(categoryID);
		
		if(inventoryCatagoryType == null) {
			throw new RequestFailureException("Invalid Category");
		}
		boolean hasChildCategory = hasChildOfInventoryCategory(categoryID, DatabaseConnectionFactory.getCurrentDatabaseConnection());
		
		return getAllObjectList(InventoryItem.class, DatabaseConnectionFactory.getCurrentDatabaseConnection(), 
				new InventoryItemConditionBuilder()
				.Where()
				.inventoryCatagoryTypeIDEquals(categoryID)
				.parentIDEquals(parentID)
				.nameBothLike(itemName)
				.isDeleted(false)
				.isUsed(hasChildCategory?null:false)
				.getCondition());
	}
	
	
	public void validateDuplicateItemnameOfSiblingsForUpdate(Long parentID,int categoryID,String itemName
			,long itemID,DatabaseConnection databaseConnection) throws Exception{
		
		Class<?> classObject = InventoryItem.class;
		String sql = "Select exists (select * from  "+getTableName(classObject)
		+"  where "+getColumnName(classObject, "inventoryCatagoryTypeID")+" = "+categoryID
		+"  and  "+getColumnName(classObject, "isDeleted")+" = 0  "
				+" and "+getColumnName(classObject, "name")+"= ? "
				+ (parentID==null?"":" and "+getColumnName(classObject, "parentID")+" = "+parentID)
				+  " and "+getPrimaryKeyColumnName(classObject)+"!="+itemID+ "  )";
		
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ps.setObject(1, StringUtils.trim(itemName));
		
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		boolean doesDuplicateEntryExists =  rs.getBoolean(1);
		if(doesDuplicateEntryExists){
			throw new RequestFailureException("An item with same name exists");
		}
	
	
	}
	
	
	

	public void validateItemForUpdate(Long parentID,int categoryID,String itemName,long itemID
			,String[] inventoryAttributeValues,long[] inventoryAttributeNameIDs,DatabaseConnection databaseConnection) throws Exception{
		
		List<Object> objects = new ArrayList<>();
		String innerSql = "";
		for(int i=0;i<inventoryAttributeValues.length;i++){
			if(i!=0){
				innerSql+=" OR ";
			}
			innerSql+="(invatrvalInvAttrNameID = ? and  invatrvalVal = ?)";
			objects.add(inventoryAttributeNameIDs[i]);
			objects.add(inventoryAttributeValues[i]);
		}

		
		String sql = "select tmp.invitName from at_inventory_item  tmp where tmp.invitParentItemID "+ (parentID == null?" IS NULL ":"="+parentID) +" and tmp.invitInvCatTypeID = ? and tmp.invitIsDeleted = 0"
					+" and tmp.invitID!="+itemID+"  and exists ("
					+" select tmp1.invitID from at_inventory_item tmp1 where tmp1.invitID = tmp.invitID"
					+" and tmp1.invitName = ?"
					+( inventoryAttributeNameIDs.length !=0 ?
						" and "+inventoryAttributeNameIDs.length+" = ("
						+" select count(tmp2.invatrvalID) from at_inv_attr_value tmp2 where tmp2.invatrvalInvItmID = tmp1.invitID and tmp2.invatrvalIsDeleted = 0"
						+" and ( "+innerSql+")"
						+")":""
					)	
					+")";
		
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		int index = 1;
		ps.setObject(index++, categoryID);
		ps.setObject(index++, itemName);
		
		for(Object object:objects){
			ps.setObject(index++, object);
		}
		logger.debug(ps);
		
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			throw new RequestFailureException(rs.getString(1)+" already exists in the inventory ");
		}
	}
	
	
	
	
	
	
	public void validateItemForInsert(Long parentID,int categoryID,String[] itemNames
			,String[] inventoryAttributeValues,long[] inventoryAttributeNameIDs,DatabaseConnection databaseConnection) throws Exception{
		
		List<Object> objects = new ArrayList<>();
		String innerSql = "";
		for(int i=0;i<inventoryAttributeValues.length;i++){
			if(i!=0){
				innerSql+=" OR ";
			}
			innerSql+="(invatrvalInvAttrNameID = ? and  invatrvalVal = ?)";
			objects.add(inventoryAttributeNameIDs[i]);
			objects.add(inventoryAttributeValues[i]);
		}
		String tmp = "";
		for(int i = 0;i<itemNames.length;i++){
			if(i!=0){
				tmp+=",";
			}
			tmp+="?";
		}
		
		String sql = "select tmp.invitName from at_inventory_item  tmp where tmp.invitParentItemID "+ (parentID == null?" IS NULL ":"="+parentID) +" and tmp.invitInvCatTypeID = ? and tmp.invitIsDeleted = 0"
					+" and exists ("
					+" select tmp1.invitID from at_inventory_item tmp1 where tmp1.invitID = tmp.invitID"
					+" and tmp1.invitName in ("+tmp+")"
					+( inventoryAttributeNameIDs.length!=0?
						" and "+inventoryAttributeNameIDs.length+" = ("
						+" select count(tmp2.invatrvalID) from at_inv_attr_value tmp2 where tmp2.invatrvalInvItmID = tmp1.invitID and tmp2.invatrvalIsDeleted = 0"
						+" and ( "+innerSql+")"
						+")":""
					)
					+")";
		
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		int index = 1;
		ps.setObject(index++, categoryID);
		for(String itemName: itemNames){
			ps.setObject(index++, itemName);
		}
		for(Object object:objects){
			ps.setObject(index++, object);
		}
		logger.debug(ps);
		
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			throw new RequestFailureException(rs.getString(1)+" already exists in the inventory ");
		}
	}
	
	
	public void validateDuplicateItemNameOfSiblings(Long parentID,int categoryID,String itemName
			,DatabaseConnection databaseConnection) throws Exception{
		Class<?> classObject = InventoryItem.class;
		String sql = "Select exists (select * from  "+getTableName(classObject)
		+"  where "+getColumnName(classObject, "inventoryCatagoryTypeID")+" = "+categoryID
		+"  and  "+getColumnName(classObject, "isDeleted")+" = 0  "
				+ (parentID==null?"":" and "+getColumnName(classObject, "parentID")+" = "+parentID)
				+" and "+getColumnName(classObject, "name")+"=?  )";
		
		
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ps.setObject(1, StringUtils.trim(itemName));
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		boolean doesDuplicateEntryExists =  rs.getBoolean(1);
		if(doesDuplicateEntryExists){
			throw new RequestFailureException("'" + itemName + "' already exists.");
		}
	}
	
	public boolean isAncestor(long ancestorID, long descendantID) throws Exception{
		Class<?> classObject = InventoryItem.class;
		String sql = "SELECT 1 FROM " + getTableName(classObject) + " WHERE "+ getColumnName(classObject, "pathFromRootToParent") + " LIKE ? AND " + getColumnName(classObject, "ID") + " = ?" ;
		PreparedStatement ps = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewPrepareStatement(sql);
		ps.setString(1, "%/"+ancestorID+"/%");
		ps.setLong(2, descendantID);
		ResultSet rs = ps.executeQuery();
		return rs.next();
	} 
	
	public long addInventoryCatagory(InventoryCatagoryType inventoryCatagoryType,DatabaseConnection databaseConnection) throws Exception{
		insert(inventoryCatagoryType, inventoryCatagoryType.getClass(), databaseConnection,false);
		return inventoryCatagoryType.getID();
	}
	public void updateInventoryCatagory(InventoryCatagoryType inventoryCatagoryType,DatabaseConnection databaseConnection) throws Exception{
		updateEntity(inventoryCatagoryType, inventoryCatagoryType.getClass(), databaseConnection,false,false);
	}
	/*
	 * Returns the primaryKey of InventoryAttributeName
	 * */
	public long addInventoryAttributeName(InventoryAttributeName inventoryAttributeName,DatabaseConnection databaseConnection) throws Exception{
		insert(inventoryAttributeName,inventoryAttributeName.getClass(),databaseConnection,false);
		return inventoryAttributeName.getID();
	} 
	
		
	public void deleteInventoryAttributeByCatagoryID(long catagoryID,DatabaseConnection databaseConnection,long lastModificationTime) throws Exception{
		
		String sql = "update "+getTableName(InventoryAttributeName.class)+" set "+getColumnName(InventoryAttributeName.class, "isDeleted")+" = 1 , "+getColumnName(InventoryAttributeName.class,"lastModificationTime" )+"="+lastModificationTime+" where "+getColumnName(InventoryAttributeName.class, "inventoryCatagoryTypeID")+" = "+catagoryID;
		Statement stmt = databaseConnection.getNewStatement();
		stmt.execute(sql);
		
		updateSequencerTable(InventoryAttributeName.class, databaseConnection, lastModificationTime);
	}
	
	public void updateInventoryAttributeName(InventoryAttributeName inventoryAttributeName,DatabaseConnection databaseConnection) throws Exception{
		updateEntity(inventoryAttributeName,inventoryAttributeName.getClass(),databaseConnection,false,false);
	}
	/*
	 * Returns primary key of InventoryItem 
	 * */
	public long addInventoryItem(InventoryItem inventoryItem,DatabaseConnection databaseConnection) throws Exception{
		insert(inventoryItem,inventoryItem.getClass(),databaseConnection,false);
		return inventoryItem.getID();
	}
	
	public int updateInventoryItem(InventoryItem inventoryItem,DatabaseConnection databaseConnection) throws Exception{
		return updateEntity(inventoryItem,inventoryItem.getClass(),databaseConnection,false,false);
	}
	/*
	 * Returns primary key InventoryAttributeValue
	 * */
	public long addInventoryValue(InventoryAttributeValue inventoryAttributeValue,DatabaseConnection databaseConnection) throws Exception{
		System.out.println("before inserting inventoryAttribute value ");
		insert(inventoryAttributeValue, InventoryAttributeValue.class, databaseConnection,false);
		System.out.println("after inserting inventoryAttribute value ");
		return inventoryAttributeValue.getID();
	}
	
	public void updateInventoryValue(InventoryAttributeValue inventoryAttributeValue,DatabaseConnection databaseConnection) throws Exception{
		updateEntity(inventoryAttributeValue, inventoryAttributeValue.getClass(), databaseConnection,false,false);
	}
	
	public void deleteInventoryCatagoryType(InventoryCatagoryType inventoryCatagoryType,DatabaseConnection databaseConnection) throws Exception{
		deleteEntity(inventoryCatagoryType, inventoryCatagoryType.getClass(), databaseConnection,false);
	}
	public void deleteInventoryCategoryTypeByCategoryID(long categoryID,long lastModificationTime) throws Exception{
		deleteEntityByID(InventoryCatagoryType.class, categoryID, lastModificationTime, DatabaseConnectionFactory.getCurrentDatabaseConnection());
	}
	/*
	 * This only deletes the item. It does not delete its child(ren)
	 * */
	public void deleteInventoryItem(InventoryItem inventoryItem,DatabaseConnection databaseConnection) throws Exception{
		deleteEntity(inventoryItem, inventoryItem.getClass(), databaseConnection,false);
	}
	public void deleteInventoryAttributeValue(InventoryAttributeValue inventoryAttributeValue,DatabaseConnection databaseConnection) throws Exception{
		deleteEntity(inventoryAttributeValue, inventoryAttributeValue.getClass(), databaseConnection,false);
	}
	public void deleteInventoryAttributeValueByItemID(long inventoryItemID,DatabaseConnection databaseConnection,long lastModificationTime) throws Exception{
		String sql = "update "+getTableName(InventoryAttributeValue.class)+" set "+getColumnName(InventoryAttributeValue.class, "isDeleted")+ " = 1 where "+getColumnName(InventoryAttributeValue.class, "inventoryItemID")+" = "+inventoryItemID;
		Statement stmt = databaseConnection.getNewStatement();
		stmt.execute(sql);
		updateSequencerTable(InventoryAttributeValue.class, databaseConnection, lastModificationTime);
	}
	public void deleteInventoryAttributeName(InventoryAttributeName inventoryAttributeName,DatabaseConnection databaseConnection) throws Exception{
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
		InventoryItem inventoryItem = InventoryRepository.getInstance().getInventoryItemByItemID(itemID);
		return getAnsestorID(inventoryItem.getParentID(), ansestorLevel-1);
	}*/
	private List<Long> getItemIDListByLevelAndCatagory(long catagoryID,DatabaseConnection databaseConnection) throws Exception{
		List<Long> IDList = new ArrayList<Long>();
		String tableName = getTableName(InventoryItem.class);
		String IDColumnName = getColumnName(InventoryItem.class, "ID");
		String catagoryIDColumnName  =getColumnName(InventoryItem.class,"inventoryCatagoryTypeID");
		String sql = "select "+IDColumnName+" from "+tableName+" where "+catagoryIDColumnName + " = "+catagoryID;
				
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			IDList.add(rs.getLong(1));
		}
		return IDList;
	}/*
	public List<InventoryItem> getInventoryItemListOfAnsestor(InventoryItem ansestorItem,
			InventoryCatagoryType inventoryCatagoryType,DatabaseConnection databaseConnection) throws Exception{
		List<InventoryItem> inventoryItems = new ArrayList<InventoryItem>();
		List<Long> decendantItemIDList = getItemIDListByLevelAndCatagory(inventoryCatagoryType.getID(), databaseConnection);
		int ansestorLevel = InventoryRepository.getInstance().getInventoryCatagoryByCatagoryID(ansestorItem.getInventoryCatagoryTypeID()).getLevel();
		for(Long decendantItemID: decendantItemIDList){
			if(ansestorItem.getID()== getAnsestorID(decendantItemID, ansestorLevel-inventoryCatagoryType.getLevel())){
				inventoryItems.add(InventoryRepository.getInstance().getInventoryItemByItemID(decendantItemID));
			}
		}
		return inventoryItems;
	}*/
	public List<InventoryAttributeValue> getInventoryAttributeValueListByItemID(long itemID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString =  " where "+getColumnName(InventoryAttributeValue.class, "inventoryItemID")+" = "+itemID+ " and "+getColumnName(InventoryAttributeValue.class, "isDeleted")+" = 0";
		return (List<InventoryAttributeValue>)getAllObjectList(InventoryAttributeValue.class, databaseConnection, conditionString);
	}
	
	
	public List<InventoryAttributeValue> getInventoryAttributeValueListByItemIDList(List<Long> itemIDList,DatabaseConnection databaseConnection) throws Exception{
		if(itemIDList.isEmpty()){
			return Collections.emptyList();
		}
		String conditionString =  " where "+getColumnName(InventoryAttributeValue.class, "inventoryItemID")+" in "+common.StringUtils.getCommaSeparatedString(itemIDList)+ " and "+getColumnName(InventoryAttributeValue.class, "isDeleted")+" = 0";
		return (List<InventoryAttributeValue>)getAllObjectList(InventoryAttributeValue.class, databaseConnection, conditionString);
	}
	
	public InventoryCatagoryType getInventoryCatagoryByCatagoryID(long inventoryCatagoryID,DatabaseConnection databaseConnection) throws Exception{
		return (InventoryCatagoryType)getObjectByID(InventoryCatagoryType.class, inventoryCatagoryID, databaseConnection);
	}
	
	public List<InventoryItem> getChildItemListByParentItemID(long parentItemID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = " where "+getColumnName(InventoryItem.class, "parentID")+" = "+parentItemID + " and "+ getColumnName(InventoryItem.class, "isDeleted") + " = 0";
		return (List<InventoryItem>)getAllObjectList(InventoryItem.class, databaseConnection, conditionString);
	}
	
	public List<InventoryAttributeValue> getInventoryAttributeValuesByAttributeNameID(long inventoryAttributeNameID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = " where "+getColumnName(InventoryAttributeValue.class, "")+" = "+inventoryAttributeNameID;
		return (List<InventoryAttributeValue>)getAllObjectList(InventoryAttributeValue.class, databaseConnection, conditionString);
	}
	public List<InventoryItem> getInventoryItemLisByCatagoryID(long catagoryID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString =  " where "+getColumnName(InventoryItem.class, "inventoryCatagoryTypeID")+ " = "+catagoryID;
		return (List<InventoryItem>)getAllObjectList(InventoryItem.class, databaseConnection, conditionString);
	}
	public InventoryItem getInventoryItemByItemID(long itemID,DatabaseConnection databaseConnection) throws Exception{
		return (InventoryItem)getObjectByID(InventoryItem.class, itemID, databaseConnection);
	}
	public List<InventoryCatagoryType> getChildItemCatagoryListByParentItemID(long parentItemID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = " where "+getColumnName(InventoryCatagoryType.class, "parentCatagoryTypeID")+" = "+parentItemID + " and "+ getColumnName(InventoryCatagoryType.class, "isDeleted") + " = 0" ;
		return (List<InventoryCatagoryType>)getAllObjectList(InventoryCatagoryType.class, databaseConnection, conditionString);
	}
	@SuppressWarnings("unchecked")
	public List<InventoryItem> getItemListByParentItemID(long parentItemID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = " where "+getColumnName(InventoryItem.class, "parentID")+" = "+parentItemID;
		return (List<InventoryItem>)getAllObjectList(InventoryItem.class, databaseConnection, conditionString);
	}
	@SuppressWarnings("unchecked")
	public List<InventoryItem> getInventoryItemListByParentItemAndCatagoryID(Long parentID,int catagoryID,String partialItemName,DatabaseConnection databaseConnection) throws Exception{
		String parentItemColumnName = getColumnName(InventoryItem.class, "parentID"); 
		Class<?> classObject = InventoryItem.class;
		String conditionString = " where "+
				    parentItemColumnName +  (parentID!=null?    " = "+parentID  :" is null")+
					" and "+getColumnName(classObject, "inventoryCatagoryTypeID")+" = "+catagoryID+
					" and "+getColumnName(classObject, "name")+" like '%"+partialItemName+"%'"+
					" and "+getColumnName(classObject, "isDeleted") + " = 0";
		
		return (List<InventoryItem>)getAllObjectList(InventoryItem.class, databaseConnection, conditionString);
	}
	
	public List<Long> getInventoryItemIDListByParentItemAndCatagoryID(Long parentID,int categoryID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = getConditionStringForInventoryItemByParentItemListAndCategoryID(parentID, categoryID, databaseConnection);
		Class<?> classObject = InventoryItem.class;
		boolean hasChild = hasChildOfInventoryCategory(categoryID, databaseConnection);
		if(!hasChild){
			String isUsedColumName = getColumnName(classObject, "isUsed");
			conditionString+=(" and "+isUsedColumName+" = 0");
		}
		
		return (List<Long>)getAllIDList(classObject, databaseConnection, conditionString);
	}
	
	public List<InventoryItem> getInventoryItemListByParentItemAndCatagoryID(Long parentID,int catagoryID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = getConditionStringForInventoryItemByParentItemListAndCategoryID(parentID, catagoryID, databaseConnection);
		return (List<InventoryItem>)getAllObjectList(InventoryItem.class, databaseConnection, conditionString);
	}

	public List<InventoryItem> getUnusedInventoryItemListByParentItemAndCatagoryID(Long parentID,int catagoryID) throws Exception{
		List<InventoryItem> inventoryItems= ModifiedSqlGenerator.getAllObjectList(InventoryItem.class,
				new InventoryItemConditionBuilder()
						.Where()
						.parentIDEquals(parentID)
						.inventoryCatagoryTypeIDEquals(catagoryID)
						.isUsed(false)
						.isDeleted(false)
//
						.getCondition()
		);
		return inventoryItems;
	}
	
	private String getConditionStringForInventoryItemByParentItemListAndCategoryID(Long parentID,int catagoryID,DatabaseConnection databaseConnection) throws Exception{
		Class<?> classObject = InventoryItem.class;
		String parentItemColumnName = getColumnName(classObject, "parentID"); 
		
		String conditionString = " where "+ parentItemColumnName +  (parentID!=null?    " = "+parentID  :" is null")+
					" and "+getColumnName(InventoryItem.class, "inventoryCatagoryTypeID")+" = "+catagoryID+" and "+
				getColumnName(classObject, "isDeleted")+" = 0";
		return conditionString;
	}

	private String getUnusedConditionStringForInventoryItemByParentItemListAndCategoryID(Long parentID,int catagoryID,DatabaseConnection databaseConnection) throws Exception{
		Class<?> classObject = InventoryItem.class;
		String parentItemColumnName = getColumnName(classObject, "parentID");

		String conditionString = " where "+ parentItemColumnName +  (parentID!=null?    " = "+parentID  :" is null")+
				" and "+getColumnName(InventoryItem.class, "inventoryCatagoryTypeID")+" = "+catagoryID+" and "+
				getColumnName(classObject, "isDeleted")+" = 0" +" and  invitIsUsed= 0 ";
		return conditionString;
	}
	
	public List<Long> getInventoryItemIDListByParentItemListAndCatagoryID(List<Long> parentIDList,long catagoryID,DatabaseConnection databaseConnection) throws Exception{
		if(parentIDList.isEmpty()){
			return new ArrayList<Long>();
		}
		Class<?> classObject = InventoryItem.class;
		String parentItemColumnName = getColumnName(classObject, "parentID"); 
		
		String conditionString = " where "+ 
					" and "+getColumnName(InventoryItem.class, "inventoryCatagoryTypeID")+" = "+catagoryID+" and "+
				getColumnName(classObject, "isDeleted")+" = 0 "+parentItemColumnName+" in ( ";
		
		
		for(int i =0;i<parentIDList.size();i++){
			if(i!=0){
				conditionString+=",";
			}
			conditionString+=parentIDList.get(i);
		}
		conditionString+=")";
		
		return (List<Long>)getAllIDList(InventoryItem.class, databaseConnection, conditionString);
	}
	
	public List<InventoryAttributeName> getInventoryAttributeNameListByCatagoryID(long catagoryID,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = " where "+getColumnName(InventoryAttributeName.class, "inventoryCatagoryTypeID")+ " = " + catagoryID;
		return (List<InventoryAttributeName>)getAllObjectList(InventoryAttributeName.class, databaseConnection, conditionString);
	}
	public void deleteInventoryItemByItemID(long inventoryItemID,DatabaseConnection databaseConnection) throws Exception{

		deleteEntityByID(InventoryItem.class,  inventoryItemID,CurrentTimeFactory.getCurrentTime(),databaseConnection);

	}
	public boolean checkInventoryItemByCatagoryID(long catagoryID,DatabaseConnection databaseConnection) throws Exception{
		String sql = "select "+getColumnName(InventoryItem.class, "ID")+" from "+getTableName(InventoryItem.class)+
				" where "+getColumnName(InventoryItem.class, "inventoryCatagoryTypeID")+" = "+catagoryID+" and "+getColumnName(InventoryItem.class, "isDeleted")+" = 0";
		ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);
		if(rs.next()){
			return true;
		}else{
			return false;
		}
		
	}
	public boolean hasAnyInventoryItemOfCategory(Integer categoryID,DatabaseConnection databaseConnection) throws Exception{
		Class<?> classObject = InventoryItem.class;
		String sql = "select exists ( select 1 from "+getTableName(classObject)+" where "+getColumnName(classObject, "inventoryCatagoryTypeID")+" = "+categoryID+" and "+ getColumnName(classObject, "isDeleted")+ " = 0" +")";;
		ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);
		rs.next();
		return rs.getInt(1)==1;
	}
	public boolean hasChildOfInventoryItem(long itemID,DatabaseConnection databaseConnection) throws Exception{
		
		String sql = "select exists (select 1 from "+getTableName(InventoryItem.class)+" where "+getColumnName(InventoryItem.class, "parentID")+ " = "+itemID+" and "+ getColumnName(InventoryItem.class, "isDeleted")+ " = 0" +")";
		ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);
		rs.next();
		return rs.getInt(1)==1;
	}

	public boolean hasChildOfInventoryCategory(int categoryID,DatabaseConnection databaseConnection) throws Exception{
		/*Class<?> classObject = InventoryCatagoryType.class;
		String sql = "select exists (select 1 from "+getTableName(classObject)+" where "+getColumnName(classObject, "parentCatagoryTypeID")+ " = "+categoryID+" and "+ getColumnName(classObject, "isDeleted")+ " = 0" +")";
		System.out.println("has child sql: "+sql);
		ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);
		rs.next();
		return rs.getInt(1)==1;*/
		return InventoryRepository.getInstance().hasChildByCategoryID(categoryID);
	}
	
	// if validItemIDList is null then this search will return all matched items otherwise the the items will be filtered by the IDList
	
	private List<Long> getItemIDListByPartialAttributeValueAndValidItemIDList(List<Long> validItemIDList,long attributeNameID,String partialAttributeValue,DatabaseConnection databaseConnection ) throws Exception{
		
		if(validItemIDList!=null && validItemIDList.isEmpty()){
			return new ArrayList<Long>();
		}
		String tableName = getTableName(InventoryAttributeValue.class);
		String inventoryItemIDColumnName = getColumnName(InventoryAttributeValue.class, "inventoryItemID");
		String inventoryNameIDCoulmnName = getColumnName(InventoryAttributeValue.class, "inventoryAttributeNameID");
		String inventoryValueColumnName = getColumnName(InventoryAttributeValue.class, "value");
		String partialAttributeValueSQLString = "";
		if(("").equals(partialAttributeValue)) {
			partialAttributeValueSQLString += " LIKE '%" + partialAttributeValue + "%'";
		}else {
			partialAttributeValueSQLString += " = '" + partialAttributeValue + "'";
		}
		
		String sql = "select "+inventoryItemIDColumnName +" from "+tableName+" where "+inventoryNameIDCoulmnName + " = "+attributeNameID+
				(partialAttributeValue.trim().equals("")?"":" and "+inventoryValueColumnName+ partialAttributeValueSQLString);
				
		
		
		StringBuilder tmp = new StringBuilder();
		if(validItemIDList!=null){
			sql+=" and "+inventoryItemIDColumnName+ " IN ( ";
			for(int i=0;i<validItemIDList.size();i++){
				if(i!=0){
					tmp.append(",");
				}
				tmp.append(validItemIDList.get(i));
			}
			tmp.append(") and " ).append(getColumnName(InventoryAttributeValue.class, "isDeleted")).append("=0");
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
	public List<Long> getInventoryItemIDsWIthSearchCriteria(Hashtable<?, ?> searchCriteria,DatabaseConnection databaseConnection) throws Exception{
		String parentItemIDString = (String)searchCriteria.get("parentItemID");
		String categoryIDString = (String)searchCriteria.get("categoryID");
		String itemName = (String)searchCriteria.get("itemName");
		List<List<Long>> listOfIDlist = new ArrayList<List<Long>>();
		Enumeration<?> keys = searchCriteria.keys();
		
		long parentItemID = (parentItemIDString==null?null:Long.parseLong(parentItemIDString));
		int categoryID = Integer.parseInt(categoryIDString);
		
		List<InventoryItem> inventoryItems = getInventoryItemListByParentItemAndCatagoryID( parentItemID, categoryID,itemName, databaseConnection);
		
		List<Long> itemList = new ArrayList<Long>();
		for(InventoryItem inventoryItem: inventoryItems){
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
	
	public List<InventoryItem> getInventoryItemListForAutoComplete(Long parentItemID,Integer categoryID,String partialName,DatabaseConnection databaseConnection) throws Exception{
		Class<?> classObject = InventoryItem.class;
		String conditionString = " where "+getColumnName(classObject, "parentID")+
				(parentItemID==null?" is null ":" = "+parentItemID)+
				" and "+getColumnName(classObject, "inventoryCatagoryTypeID")+" = "+categoryID+
				" and "+getColumnName(classObject, "name")+" like '%"+partialName+"%' "+
				" and " + getColumnName(classObject, "isDeleted") + "=0 "+
				" and (invitIsUsed is NULL or invitIsUsed = 0)";
		return (List<InventoryItem>)getAllObjectList(classObject, databaseConnection, conditionString);
	}
	
	
	// if parentItemList is null then this method will not filter the search result otherwise the result will be filtered by the parentItemList
	private List<Long> getInventoryItemIDListByParentItemListAndCategoryID(List<Long> parentItemList,int categoryID
			,String itemPartialName,Boolean isUsed,  DatabaseConnection databaseConnection) throws Exception{
		if(parentItemList!=null && parentItemList.isEmpty()){
			return new ArrayList<Long>();
		}
		List<Object> valueList = new ArrayList<>();
		Class<?> classObject = InventoryItem.class;
		String parentItemColumnName = getColumnName(classObject, "parentID");
		String itemNameColumnName = getColumnName(classObject, "name");
		StringBuilder conditionStringBuider = new StringBuilder();
		conditionStringBuider.append(" where ")
							 .append(getColumnName(InventoryItem.class, "inventoryCatagoryTypeID"))
							 .append(" = ")
							 .append(categoryID);
		if(!StringUtils.isBlank(itemPartialName)){
			conditionStringBuider.append(" and ")
								 .append(itemNameColumnName)
								 .append(" like ? ");
			valueList.add("%"+itemPartialName+"%");
		}

		boolean hasChild = hasChildOfInventoryCategory(categoryID, databaseConnection);
		if(!hasChild && isUsed!=null){
			
			String isUsedColumName = getColumnName(classObject, "isUsed");
			conditionStringBuider.append(" and ").append(isUsedColumName).append(" = ").append(isUsed);
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
	//	return StringUtils.isBlank(itemPartialName) ?getAllIDList(InventoryItem.class, databaseConnection, conditionStringBuider.toString()):
		
		valueList.add(0, conditionStringBuider.toString());
		
		return 	getAllIDList(InventoryItem.class, databaseConnection, valueList.toArray());
	
	}
	
	public List<Long> getDescendantIDListByAnsestorIDAndDescendantCategoryID(Long ansestorID,int descendantCategoryID
			 ,Boolean isUsed  ,DatabaseConnection databaseConnection) throws Exception{
		List<Long> IDList = new ArrayList<Long>();
		InventoryItem ansestorItem = (InventoryItem)getObjectByID(InventoryItem.class, ansestorID, databaseConnection);
		if(ansestorItem.getInventoryCatagoryTypeID() == descendantCategoryID){
			 IDList.add(ansestorID);
			 return IDList;
		}
		List<Integer> categoryPathIDList = InventoryRepository.getInstance().getIDListPathFromAnsestorToDescendant(ansestorItem.getInventoryCatagoryTypeID(), descendantCategoryID); 

		List<Long> itemIDList = getInventoryItemIDListByParentItemAndCatagoryID(ansestorID, categoryPathIDList.get(1), databaseConnection);
		
		for(int i = 2;i<categoryPathIDList.size();i++){
			itemIDList = getInventoryItemIDListByParentItemListAndCategoryID(itemIDList, categoryPathIDList.get(i),"",
					isUsed,databaseConnection);
		}
		return itemIDList;
	}
	public int makeAllItemAsChildItemByCategoryID(int categoryID,DatabaseConnection databaseConnection) throws Exception{
		Class<?> classObject = InventoryItem.class;
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
		Class<?> classObject = InventoryItem.class;
		String tablename = getTableName(classObject);
		String categoryIDColumnName = getColumnName(classObject, "");
		String isChildItemColumnName = getColumnName(classObject, "");
		String isUsedColumnName = getColumnName(classObject, "isUsed");
		
		String sql = " update "+tablename+" set "+isChildItemColumnName+" = 0 and "+isUsedColumnName+" = null where "+
					categoryIDColumnName+" = "+categoryID;
		int numOfUpdatedRows = databaseConnection.getNewStatement().executeUpdate(sql);
		return numOfUpdatedRows;
	}
	public List<Long> getInventoryItemIDList(List<KeyValuePair<Integer,String> >rootToItemPath
			,List<KeyValuePair<Long,String>> attributeNameContraintList,Boolean isUsed
			,DatabaseConnection databaseConnection) throws Exception{
		
		
		List<Long> validItemIDList = null;
		
		for(KeyValuePair<Integer, String> categoryIDPartialNameValuePair: rootToItemPath){
			int categoryID = categoryIDPartialNameValuePair.key;
			String itemPartialName = categoryIDPartialNameValuePair.value;
			validItemIDList = getInventoryItemIDListByParentItemListAndCategoryID(validItemIDList, categoryID,itemPartialName
					, isUsed ,databaseConnection);
		}
		
		for(KeyValuePair< Long, String> attributeNameValuePair: attributeNameContraintList){
			Long attributeNameID = attributeNameValuePair.key;
			String attributePartialValue = attributeNameValuePair.value;
			if(StringUtils.isBlank(attributePartialValue)){
				continue;
			}
			validItemIDList = getItemIDListByPartialAttributeValueAndValidItemIDList(validItemIDList, attributeNameID, attributePartialValue, databaseConnection);
		}
		
		return validItemIDList;
	}
	
	public List<Long> getAllInventoryItemIDListByCategoryID(int categoryID,DatabaseConnection databaseConnection) throws Exception{
		
		Class<?> classObject = InventoryItem.class;
		String categoryIDColumnName = getColumnName(classObject, "inventoryCatagoryTypeID");
		String isDeletedColumnName = getColumnName(classObject, "isDeleted");
		String conditionString = " where "+categoryIDColumnName+" = "+categoryID+" and "+isDeletedColumnName+" = 0";
		return getAllIDList(classObject, databaseConnection, conditionString);
	}
	
	public int delteInventoryAttributeNameByAttributeNameID(List<Long> inventoryAttributeNameIDList,long lastModificationTime,DatabaseConnection databaseConnection) throws Exception{
		Class<?> classObject = InventoryAttributeName.class;
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
	
	public Map<Long,String> getInventoryItemNameListByItemIDList(List<Long> IDList,DatabaseConnection databaseConnection) throws Exception{
		List<String> resultList = new ArrayList<String>();
		Map<Long,String> mapOfInventoryItemNameToItemID =  new HashMap<Long, String>();
		if(IDList.isEmpty()){
			return mapOfInventoryItemNameToItemID;
		}
		Class<?> classObject = InventoryItem.class;
		String IDColumnName = getColumnName(classObject, "ID");
		String nameColumName = getColumnName(classObject, "name");
		String isDeletedColumnName = getColumnName(classObject, "isDeleted");
		String sql = "select  "+IDColumnName+" , "+nameColumName+" from "+getTableName(classObject)+" where "+IDColumnName+" IN ";
		StringBuilder conditionBuilder = new StringBuilder("(");
		for(int i=0;i<IDList.size();i++){
			if(i!=0){
				conditionBuilder.append(",");
			}
			conditionBuilder.append(IDList.get(i));
		}
		conditionBuilder.append(")");
		conditionBuilder.append(" and ").append(isDeletedColumnName).append(" = 0;") ;
		sql = sql+conditionBuilder.toString();
		
		ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);
		while(rs.next()){
			long ID = rs.getLong(1);
			String name = rs.getString(2);
			mapOfInventoryItemNameToItemID.put(ID, name);
		}
		return mapOfInventoryItemNameToItemID;
	}
	public void markInventoryItemAsUsedByItemID(long inventoryItemID,long occupierEntityID,int occupierEntityTypeID, long occupierClientID, DatabaseConnection databaseConnection) throws Exception{
		
		String inventoryCategoryTypeName =  getInventoryCategoryTypeByCategoryTypeID(getInventoryItemByItemID(inventoryItemID, databaseConnection).getInventoryCatagoryTypeID(), databaseConnection).getName();
		
		if(hasChildOfInventoryItem(inventoryItemID, databaseConnection)){
			throw new RequestFailureException(inventoryCategoryTypeName +" (" + inventoryItemID	+ ") can not be used because it has child item(s)");
		}
		
		
		int numOfAffectedRows = 0;
		
		synchronized (mutexLock) {

			InventoryItem inventoryItem = getInventoryItemByItemID(inventoryItemID, databaseConnection);
			if(inventoryItem==null || inventoryItem.isDeleted()){
				throw new RequestFailureException("No inventory item found with ID "+inventoryItemID);
			}
			if(!inventoryItem.isChildItem()){
				throw new RequestFailureException(inventoryCategoryTypeName +" is not a child item. Hence it can not be executed");
			}
			if(inventoryItem.getIsUsed()){
				throw new RequestFailureException(inventoryCategoryTypeName +" (" + getInventoryItemByItemID(inventoryItemID, databaseConnection).getName()	+ ") is already used");
			}
			inventoryItem.setOccupierEntityTypeID(occupierEntityTypeID);
			inventoryItem.setOccupierEntityID(occupierEntityID);
			inventoryItem.setOccupierClientID(occupierClientID);
			inventoryItem.setLastModificationTime(CurrentTimeFactory.getCurrentTime());
			inventoryItem.setIsUsed(true);
			numOfAffectedRows = updateInventoryItem(inventoryItem, databaseConnection);
		}
		
		
		if(numOfAffectedRows==0){
			throw new RequestFailureException(inventoryCategoryTypeName +" (" + inventoryItemID	+ ") can not be used");
		}
		
		
	}
	
	public void markInventoryItemAsUnusedByItemID(long inventoryItemID, DatabaseConnection databaseConnection) throws Exception{
		String inventoryCategoryTypeName =  getInventoryCategoryTypeByCategoryTypeID(getInventoryItemByItemID(inventoryItemID, databaseConnection).getInventoryCatagoryTypeID(), databaseConnection).getName(); 
		
		if(hasChildOfInventoryItem(inventoryItemID, databaseConnection)){
			logger.debug(inventoryCategoryTypeName +" (" + getInventoryItemByItemID(inventoryItemID, databaseConnection).getName()	+ ") can not be unused because it has child item(s)");
		}
		int numOfAffectedRows = 0;
		
		synchronized (mutexLock) {
			InventoryItem inventoryItem = getInventoryItemByItemID(inventoryItemID, databaseConnection);
			if(inventoryItem==null || inventoryItem.isDeleted()){
				throw new RequestFailureException("No inventory item found with ID "+inventoryItemID);
			}
			if(!inventoryItem.isChildItem()){
				logger.debug(inventoryCategoryTypeName +"  is not a child item. Hence it can not be executed");
			}
			if(inventoryItem.getIsUsed()){
				logger.debug(inventoryCategoryTypeName +" (" + getInventoryItemByItemID(inventoryItemID, databaseConnection).getName() 	+ ") is already used");
			}
			inventoryItem.setOccupierEntityTypeID(null);
			inventoryItem.setOccupierEntityID(null);
			inventoryItem.setOccupierClientID(null);
			inventoryItem.setLastModificationTime(CurrentTimeFactory.getCurrentTime());
			inventoryItem.setIsUsed(false);
			numOfAffectedRows = updateInventoryItem(inventoryItem, databaseConnection);
		}
		if(numOfAffectedRows==0){
			logger.debug(inventoryCategoryTypeName +" (" + inventoryItemID	+ ") can not be unused");
		}
	}
	
	public boolean isAnsestor(long ansestorID,long descendantID){ // implement this
		return true;
	}
	/*
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
				markInventoryItemAsUsedByItemID(inventoryItemID, databaseConnection);
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
	*/
	@SuppressWarnings("unchecked")
	public Map<Integer,Map<Long,InventoryItem>> getMapOfInventoryItemToInventoryItemIDToCategoryTypeID(List<Integer> categoryIDs) throws Exception{
		Map<Integer, Map<Long,InventoryItem>> resultMap = new HashMap<>();
		String conditionString = " where "+getColumnName(InventoryItem.class, "inventoryCatagoryTypeID")+" IN "+common.StringUtils.getCommaSeparatedString(categoryIDs);
		List<InventoryItem> inventoryItems = (List<InventoryItem>)getAllObjectList(InventoryItem.class, DatabaseConnectionFactory.getCurrentDatabaseConnection(), conditionString);
		for(InventoryItem inventoryItem: inventoryItems){
			if(!resultMap.containsKey(inventoryItem.getInventoryCatagoryTypeID())){
				resultMap.put(inventoryItem.getInventoryCatagoryTypeID(), new HashMap<>());
			}
			resultMap.get(inventoryItem.getInventoryCatagoryTypeID()).put(inventoryItem.getID(), inventoryItem);
		}
		return resultMap;
	}
	@SuppressWarnings("unchecked")
	public static Map<Integer,Map<Long,InventoryItem>> getMapOfInventoryItemToInventoryItemIDToCatTypeID(List<Integer> categoryIDs) throws Exception{
		Map<Integer, Map<Long,InventoryItem>> resultMap = new HashMap<>();
		String conditionString = " where "+getColumnName(InventoryItem.class, "inventoryCatagoryTypeID")+" IN "+common.StringUtils.getCommaSeparatedString(categoryIDs);
		List<InventoryItem> inventoryItems = (List<InventoryItem>)getAllObjectList(InventoryItem.class, DatabaseConnectionFactory.getCurrentDatabaseConnection(), conditionString);
		for(InventoryItem inventoryItem: inventoryItems){
			if(!resultMap.containsKey(inventoryItem.getInventoryCatagoryTypeID())){
				resultMap.put(inventoryItem.getInventoryCatagoryTypeID(), new HashMap<>());
			}
			resultMap.get(inventoryItem.getInventoryCatagoryTypeID()).put(inventoryItem.getID(), inventoryItem);
		}
		return resultMap;
	}


	public InventoryCatagoryType getInventoryCategoryTypeByCategoryTypeID(int categoryTypeID, DatabaseConnection databaseConnection) throws Exception {
		return (InventoryCatagoryType) SqlGenerator.getObjectByID(InventoryCatagoryType.class, categoryTypeID, databaseConnection);
	}


	@SuppressWarnings("unchecked")
	public List<InventoryItem> getInventoryItemListForAutoComplete(Integer categoryID, String partialName, DatabaseConnection databaseConnection) throws Exception {
		Class<?> classObject = InventoryItem.class;
		String conditionString = " where "+getColumnName(classObject, "inventoryCatagoryTypeID")+" = "+categoryID+
				" and "+getColumnName(classObject, "name")+" like '%"+partialName+"%' "+
				" and " + getColumnName(classObject, "isDeleted") + "=0 "+
				" and (invitIsUsed is NULL or invitIsUsed = 0)";
		return (List<InventoryItem>)getAllObjectList(classObject, databaseConnection, conditionString);
	}


	public boolean isItemUsed(long inventoryItemID, DatabaseConnection databaseConnection) throws Exception {
		InventoryItem inventoryItem = (InventoryItem)SqlGenerator.getObjectByID(InventoryItem.class, inventoryItemID, databaseConnection);
		
		if(InventoryRepository.getInstance().getInventoryCatagoryTreeNodeByCatagoryID(inventoryItem.getInventoryCatagoryTypeID()).getInventoryCatagoryDetailsChildNodes().size()>0) {
			return false;
		}else {
			return inventoryItem.getIsUsed();
		}
	}

	@SuppressWarnings("unchecked")
	public long getAttributeNameIDByCategoryIDAndAttributeNameName(int categoryID, String attributeName, DatabaseConnection databaseConnection) throws Exception {
		List<InventoryAttributeName> inventoryAttributeNameList = (ArrayList<InventoryAttributeName>) SqlGenerator.getAllObjectList(InventoryAttributeName.class, databaseConnection, " where "+SqlGenerator.getColumnName(InventoryAttributeName.class, "inventoryCatagoryTypeID")+" = "+categoryID+
				" and "+SqlGenerator.getColumnName(InventoryAttributeName.class, "name") + " = '"+attributeName+"'");
		if(inventoryAttributeNameList.size()>0) {
			return inventoryAttributeNameList.get(0).getID();
		}
		return 0;
	}


	@SuppressWarnings("unchecked")
	public List<Long> getInventoryItemIDListByAttributeNameIDAndAttributeValue(long inventoryNameID, String attributeValue, DatabaseConnection databaseConnection) throws Exception {
		List<InventoryAttributeValue> inventoryAttributeValueList = (ArrayList<InventoryAttributeValue>) SqlGenerator.getAllObjectList(InventoryAttributeValue.class, databaseConnection, 
				" where "+SqlGenerator.getColumnName(InventoryAttributeValue.class, "inventoryAttributeNameID")+" = "+inventoryNameID+" and "+SqlGenerator.getColumnName(InventoryAttributeValue.class, "value") + " = '"+attributeValue+"'");
		
		List<Long> inventoryItemIDList = new ArrayList<Long>();
		for(InventoryAttributeValue inventoryAttributeValue : inventoryAttributeValueList) {
			inventoryItemIDList.add(inventoryAttributeValue.getInventoryItemID());
		}
		return inventoryItemIDList;
	}


	@SuppressWarnings("unchecked")
	public List<InventoryItem> getInventoryItemListByInventoryItemIDList(List<Long> inventoryItemIDList, DatabaseConnection databaseConnection) throws Exception {
		return (List<InventoryItem>) SqlGenerator.getObjectListByIDList(InventoryItem.class, inventoryItemIDList, databaseConnection);
	}


	// TODO: 12/26/2018 the below method is not working check later
	public List<InventoryItem> getInventoryItemListByInventoryItemIDList
			(List<Long> inventoryItemIDList,Long parentId, DatabaseConnection databaseConnection) throws Exception {
		List<InventoryItem>inventoryItems = SqlGenerator.getAllObjectList(InventoryItem.class, databaseConnection, new InventoryItemConditionBuilder()
										.Where()
										.IDIn(inventoryItemIDList)
										.parentIDEquals(parentId)
										.getCondition());

		return inventoryItems;

	}


	public List<InventoryItem> getInventoryItemListForAutocomplete(Integer categoryID, Long parentItemID, String partialName, Boolean onlyUnused, Boolean isParentNeeded, DatabaseConnection databaseConnection) throws Exception {
		String condition = " where " + getColumnName(InventoryItem.class, "inventoryCatagoryTypeID") + " = " + categoryID + " ";
		condition += " and " + getColumnName(InventoryItem.class, "isDeleted") + "=0 ";
		
		if(parentItemID != null) {
			condition += " and " + getColumnName(InventoryItem.class, "parentID") + " = " + parentItemID + " ";
		}
		if(!StringUtils.isBlank(partialName)) {
			condition += " and " + getColumnName(InventoryItem.class, "name") + " LIKE '%" + partialName + "%' ";
		}
		if(onlyUnused) {
			condition += " and ( " + getColumnName(InventoryItem.class, "isUsed") + " = 0 or " + getColumnName(InventoryItem.class, "isUsed") + " is NULL ) ";
		}
		if((isParentNeeded && parentItemID == null) && !(categoryID == CategoryConstants.CATEGORY_ID_DISTRICT) ) {
			condition += " and 1=0 ";
		}
		condition += " order by "  + getColumnName(InventoryItem.class , "name") + " ASC";
		return (List<InventoryItem>) SqlGenerator.getAllObjectList(InventoryItem.class, databaseConnection, condition);
	}




	public void makeResourceFreeByOccupierID(long entityID, int entityTypeID, DatabaseConnection databaseConnection) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "update at_inventory_item set invitIsUsed = 0, invitOccupierClientID = null, invitOccupierEntityID = null, invitOccupierEntityTypeID = null where invitOccupierEntityTypeID = "+ entityTypeID +" and invitOccupierEntityID = " + entityID; 
		databaseConnection.getNewStatement().execute(sql);
	}
	
	public void makeResourceFreeByInventoryItemID(long itemID, DatabaseConnection databaseConnection) throws Exception {
		// TODO Auto-generated method stub
		String sql = "update at_inventory_item set invitIsUsed = null, invitOccupierClientID = null, invitOccupierEntityID = null, invitOccupierEntityTypeID = null where " + getPrimaryKeyColumnName(InventoryItem.class) + " = " + itemID; 
		databaseConnection.getNewStatement().execute(sql);		
	}

	public void makeResourceFreeByInventoryItemIDs(String itemIDs, DatabaseConnection databaseConnection) throws Exception {
		// TODO Auto-generated method stub
		String sql = "update at_inventory_item set invitIsUsed = null, invitOccupierEntityID = null, invitOccupierEntityTypeID = null where " + getPrimaryKeyColumnName(InventoryItem.class) + " in ( " + itemIDs + ")"; 
		databaseConnection.getNewStatement().execute(sql);		
	}
	
	
	
	
	public Map<Long,RouterSwitchDTO> getNetworkGraph() throws Exception{
		Map<Long,RouterSwitchDTO> mapOfRoutersToRouterID = new HashMap<>();
		String sql = "select tmp1.invitID,(select tmp5.invatrvalVal from at_inv_attr_value tmp5 where tmp5.invatrvalInvAttrNameID = 73010 and tmp5.invatrvalInvItmID = tmp1.invitID) routerType,(select group_concat(tmp3.invitParentItemID)   	from at_inventory_item tmp3 where tmp3.invitID in   (select tmp2.invitOccupierEntityID from at_inventory_item 	  tmp2 where  tmp2.invitParentItemID = tmp1.invitID and tmp2.invitOccupierEntityTypeID = 9999 and tmp2.invitIsUsed=1  ) )routers   from at_inventory_item tmp1 where invitInvCatTypeID = 5";
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);
		while(rs.next()){
			RouterSwitchDTO routerSwitchDTO = new RouterSwitchDTO();
			routerSwitchDTO.ID = rs.getLong("invitID");
			routerSwitchDTO.type = rs.getString("routerType");
			String connectedIDList = StringUtils.trim(rs.getString("routers"));
			
			for(String IDString:connectedIDList.split(",")){
				try{
					routerSwitchDTO.connectedDeviceIDList.add(Long.parseLong(IDString));
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			mapOfRoutersToRouterID.put(routerSwitchDTO.ID, routerSwitchDTO);
		}
		return mapOfRoutersToRouterID;
	}

}
