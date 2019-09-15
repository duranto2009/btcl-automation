package crm.inventory.repository;

import static util.SqlGenerator.getAllObjectList;
import static util.SqlGenerator.getLastModificationTimeColumnName;
import static util.SqlGenerator.populateObjectFromDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import crm.inventory.CRMInventoryAttributeName;
import crm.inventory.CRMInventoryCatagoryDetails;
import crm.inventory.CRMInventoryCatagoryTreeNode;
import crm.inventory.CRMInventoryCatagoryType;


public class CRMInventoryRepository {
	Logger logger = Logger.getLogger(getClass());
	private static CRMInventoryRepository instance = null;
	
	HashMap<Integer,CRMInventoryCatagoryType> mapOfCRMInventoryCatagoryTypeToCatagoryTypeID;
	HashMap<Integer,Set<CRMInventoryAttributeName>> mapOfInventoryAttruvbuteNameListToCatagaoryID;
	HashMap<Long,CRMInventoryAttributeName> mapOfCRMInventoryAttributeNameToAttributeNameID;
	HashMap<Integer,CRMInventoryCatagoryDetails> mapOfCRMInventoryCatagoryDetailsToCatagoryID;
	HashMap<Integer,CRMInventoryCatagoryTreeNode> mapOfCRMInventoryCatagoryTreeNodeToCatagoryID;
	public List<CRMInventoryCatagoryType> getAllInventoryCategory(){
		List<CRMInventoryCatagoryType> inventoryCatagoryTypes = new ArrayList<CRMInventoryCatagoryType>();
		for(CRMInventoryCatagoryType inventoryCatagoryType: mapOfCRMInventoryCatagoryTypeToCatagoryTypeID.values()){
			if(!inventoryCatagoryType.isDeleted()){
				inventoryCatagoryTypes.add(inventoryCatagoryType);
			}
		}
		return inventoryCatagoryTypes;
	}
	public CRMInventoryCatagoryType getInventoryCatgoryTypeByCatagoryID(int catagoryID){
		if(mapOfCRMInventoryCatagoryTypeToCatagoryTypeID.isEmpty()){
			InventoryCatagoryRepository.getInstance().reload(true);
		}
		return mapOfCRMInventoryCatagoryTypeToCatagoryTypeID.get(catagoryID);
	}
	
	public List<CRMInventoryAttributeName> getCRMInventoryAttributeNameListByCatagoryID(int catagoryID){
		List<CRMInventoryAttributeName> inventoryAttributeNames = new ArrayList<CRMInventoryAttributeName>();
		logger.debug("mapOfInventoryAttruvbuteNameListToCatagaoryID"+mapOfInventoryAttruvbuteNameListToCatagaoryID);
		for(int categoryID:mapOfInventoryAttruvbuteNameListToCatagaoryID.keySet()){
			logger.debug("categoryID="+catagoryID+""+mapOfInventoryAttruvbuteNameListToCatagaoryID.get(catagoryID));
		}
		
		if(mapOfInventoryAttruvbuteNameListToCatagaoryID.containsKey(catagoryID)){
			for(CRMInventoryAttributeName inventoryAttributeName: mapOfInventoryAttruvbuteNameListToCatagaoryID.get(catagoryID)){
				if(!inventoryAttributeName.isDeleted()){
					inventoryAttributeNames.add(inventoryAttributeName);
				}
			}
		}
		return inventoryAttributeNames;
	}
	
	private boolean isAnsestor(CRMInventoryCatagoryTreeNode ansestorTreeNode, long decendantCatagoryID,List<CRMInventoryCatagoryType> catagoryPath){
		catagoryPath.add(ansestorTreeNode.getCRMInventoryCatagoryDetailsRootNode().getCRMInventoryCatagoryType());
		if(ansestorTreeNode.getCRMInventoryCatagoryDetailsRootNode().getCRMInventoryCatagoryType().getID()==decendantCatagoryID){
			return true;
		}
		for(CRMInventoryCatagoryTreeNode childNode : ansestorTreeNode.getCRMInventoryCatagoryDetailsChildNodes()){
			if(isAnsestor(childNode, decendantCatagoryID, catagoryPath)){
				return true;
			}
		}
		catagoryPath.remove(catagoryPath.size()-1);
		return false;
	}
	/*
	public List<CRMInventoryCatagoryType> getCRMInventoryCatagoryTypePathFromRoot(long catagoryID){
		List<CRMInventoryCatagoryTreeNode> inventoryCatagoryTreeNodes = getInventoryCategoryTreeRootNodes();
		List<CRMInventoryCatagoryType> inventoryCatagoryTypes = new ArrayList<CRMInventoryCatagoryType>();
		for(CRMInventoryCatagoryTreeNode rootTreeNode: inventoryCatagoryTreeNodes){
			if(isAnsestor(rootTreeNode, catagoryID, inventoryCatagoryTypes)){
				return inventoryCatagoryTypes;
			}
		}
		
		return inventoryCatagoryTypes;
	}*/
	
	public  List<CRMInventoryCatagoryType> getCRMInventoryCatagoryTypePathFromRoot(int catagoryID){
		
		CRMInventoryCatagoryType inventoryCatagoryType = getInventoryCatgoryTypeByCatagoryID(catagoryID);
		if(inventoryCatagoryType==null||inventoryCatagoryType.isDeleted()){
			throw new RuntimeException("No inventory catagory found with catagory ID "+catagoryID);
		}
		List<CRMInventoryCatagoryType> pathFromRootToNode ;
		if(!inventoryCatagoryType.hasParentCatagory()){
			pathFromRootToNode = new ArrayList<CRMInventoryCatagoryType>();
			
		}else{
			pathFromRootToNode = getCRMInventoryCatagoryTypePathFromRoot(inventoryCatagoryType.getParentCatagoryTypeID());
		}
		pathFromRootToNode.add(inventoryCatagoryType);
		return pathFromRootToNode;
	}
	
	public List<Integer> getIDListPathFromAnsestorToDescendant(int ansestorCategoryID,int descendantCategoryID){
		List<Integer> returnIDList = new ArrayList<Integer>();
		if(ansestorCategoryID == descendantCategoryID){
			returnIDList.add(ansestorCategoryID);
			return returnIDList;
		}
		
		logger.debug(ansestorCategoryID + "  "+descendantCategoryID +"  "+mapOfCRMInventoryCatagoryTypeToCatagoryTypeID.toString());
		CRMInventoryCatagoryType descendantCategoryType = getInventoryCatgoryTypeByCatagoryID(descendantCategoryID);
		returnIDList = getIDListPathFromAnsestorToDescendant(ansestorCategoryID, descendantCategoryType.getParentCatagoryTypeID());
		returnIDList.add(descendantCategoryID);
		return returnIDList;
		
	}
	
	private CRMInventoryRepository(){
		mapOfCRMInventoryCatagoryTypeToCatagoryTypeID = InventoryCatagoryRepository.getInstance().mapOfCRMInventoryCatagoryTypeToCatagoryTypeID;
		mapOfInventoryAttruvbuteNameListToCatagaoryID = InventoryCatagoryAttributeNameRepository.getInstance().mapOfInventoryAttruvbuteNameListToCatagaoryID;
		mapOfCRMInventoryAttributeNameToAttributeNameID = InventoryCatagoryAttributeNameRepository.getInstance().mapOfCRMInventoryAttributeNameToAttributeNameID;
		
	}
	public CRMInventoryAttributeName getCRMInventoryAttributeNameByNameID(long inventoryAttributeNameID){
		CRMInventoryAttributeName inventoryAttributeName = mapOfCRMInventoryAttributeNameToAttributeNameID.get(inventoryAttributeNameID);
		return inventoryAttributeName!=null&&!inventoryAttributeName.isDeleted()?inventoryAttributeName:null;
	}
	
	public void reload(boolean isFirstReload){
		InventoryCatagoryRepository.getInstance().reload(isFirstReload);
		InventoryCatagoryAttributeNameRepository.getInstance().reload(isFirstReload);
	}
	
	private void initializeCatagoryTree(){
		mapOfCRMInventoryCatagoryDetailsToCatagoryID = new HashMap<Integer, CRMInventoryCatagoryDetails>();
		mapOfCRMInventoryCatagoryTreeNodeToCatagoryID = new HashMap<Integer, CRMInventoryCatagoryTreeNode>();
		createCRMInventoryCatagoryDetails();
		createCRMInventoryCatagoryTreeNodes();
		createInventoryCatagoryTreeEdges();
	}
	
	public CRMInventoryCatagoryTreeNode getCRMInventoryCatagoryTreeNodeByCatagoryID(int catagoryID){
		initializeCatagoryTree();
		return mapOfCRMInventoryCatagoryTreeNodeToCatagoryID.get(catagoryID);
	}
	
	public List<CRMInventoryCatagoryTreeNode> getInventoryCategoryTreeRootNodes(){
		initializeCatagoryTree();
		List<CRMInventoryCatagoryTreeNode> inventoryTreeRootNodes = new ArrayList<CRMInventoryCatagoryTreeNode>();
		for(CRMInventoryCatagoryTreeNode inventoryCatagoryTreeNode : mapOfCRMInventoryCatagoryTreeNodeToCatagoryID.values()){
			if(!inventoryCatagoryTreeNode.getCRMInventoryCatagoryDetailsRootNode().getCRMInventoryCatagoryType().hasParentCatagory()){
				inventoryTreeRootNodes.add(inventoryCatagoryTreeNode);
			}
		}
		return inventoryTreeRootNodes;
	}
	private void createCRMInventoryCatagoryDetails(){
		for(CRMInventoryCatagoryType inventoryCatagoryType: mapOfCRMInventoryCatagoryTypeToCatagoryTypeID.values()){
			
			if(inventoryCatagoryType.isDeleted()){
				continue;
			}
			
			CRMInventoryCatagoryDetails inventoryCatagoryDetails = new CRMInventoryCatagoryDetails();
			inventoryCatagoryDetails.setCRMInventoryCatagoryType(inventoryCatagoryType);
			List<CRMInventoryAttributeName> inventoryAttributeNameList = new ArrayList<CRMInventoryAttributeName>();
			if(mapOfInventoryAttruvbuteNameListToCatagaoryID.containsKey(inventoryCatagoryType.getID()))
			{
				Collection<CRMInventoryAttributeName> inventoryAttributeNames =  mapOfInventoryAttruvbuteNameListToCatagaoryID.get(inventoryCatagoryType.getID());
				for(CRMInventoryAttributeName inventoryAttributeName : inventoryAttributeNames){
					if(inventoryAttributeName.isDeleted()==false){
						inventoryAttributeNameList.add(inventoryAttributeName);
					}
				}
			}
			inventoryCatagoryDetails.setCRMInventoryAttributeNameList(inventoryAttributeNameList);
			mapOfCRMInventoryCatagoryDetailsToCatagoryID.put(inventoryCatagoryType.getID(),inventoryCatagoryDetails);
		}
	}
	private void createCRMInventoryCatagoryTreeNodes(){		
		 for(CRMInventoryCatagoryDetails inventoryCatagoryDetails : mapOfCRMInventoryCatagoryDetailsToCatagoryID.values()){
			 if(inventoryCatagoryDetails.getCRMInventoryCatagoryType().isDeleted()){				 
				 continue;
			 }
			 CRMInventoryCatagoryTreeNode inventoryCatagoryTreeNode = new CRMInventoryCatagoryTreeNode();
			 inventoryCatagoryTreeNode.setCRMInventoryCatagoryDetailsRootNode(inventoryCatagoryDetails);
			 inventoryCatagoryTreeNode.setCRMInventoryCatagoryDetailsChildNodes(new ArrayList());
			 mapOfCRMInventoryCatagoryTreeNodeToCatagoryID.put(inventoryCatagoryDetails.getCRMInventoryCatagoryType().getID(), inventoryCatagoryTreeNode);
		 }
	}
	private void createInventoryCatagoryTreeEdges(){		
		for(CRMInventoryCatagoryType inventoryCatagoryType : mapOfCRMInventoryCatagoryTypeToCatagoryTypeID.values()){
			if(inventoryCatagoryType.isDeleted()){
				continue;
			}
			if(!inventoryCatagoryType.hasParentCatagory()){
				continue;
			}
			Integer catagoryID = inventoryCatagoryType.getID();
			Integer parentID = inventoryCatagoryType.getParentCatagoryTypeID();
			CRMInventoryCatagoryTreeNode parentInventoryTreeNode = mapOfCRMInventoryCatagoryTreeNodeToCatagoryID.get(parentID);
			CRMInventoryCatagoryTreeNode childInventoryTreeNode = mapOfCRMInventoryCatagoryTreeNodeToCatagoryID.get(catagoryID);
			if(parentInventoryTreeNode == null)continue;
			if(parentInventoryTreeNode.getCRMInventoryCatagoryDetailsChildNodes()==null){
				parentInventoryTreeNode.setCRMInventoryCatagoryDetailsChildNodes(new ArrayList<CRMInventoryCatagoryTreeNode>());
			}
			parentInventoryTreeNode.getCRMInventoryCatagoryDetailsChildNodes().add(childInventoryTreeNode);
		}
	}
	
	public static synchronized CRMInventoryRepository getInstance(){
		if(instance == null){
			instance = new CRMInventoryRepository();
		}
		return instance;
	}
}
