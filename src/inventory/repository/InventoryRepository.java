package inventory.repository;
import inventory.InventoryAttributeName;
import inventory.InventoryCatagoryDetails;
import inventory.InventoryCatagoryTreeNode;
import inventory.InventoryCatagoryType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;


public class InventoryRepository {
	Logger logger = Logger.getLogger(getClass());
	private static InventoryRepository instance = null;
	
	HashMap<Integer,InventoryCatagoryType> mapOfInventoryCatagoryTypeToCatagoryTypeID;
	HashMap<Integer,Set<InventoryAttributeName>> mapOfInventoryAttributeNameListToCatagaoryID;
	HashMap<Long,InventoryAttributeName> mapOfInventoryAttributeNameToAttributeNameID;
	HashMap<Integer,InventoryCatagoryDetails> mapOfInventoryCatagoryDetailsToCatagoryID;
	HashMap<Integer,InventoryCatagoryTreeNode> mapOfInventoryCatagoryTreeNodeToCatagoryID;
	
	
	public boolean hasChildByCategoryID(int inventoryCategoryID){
		if(!mapOfInventoryCatagoryTreeNodeToCatagoryID.containsKey(inventoryCategoryID)){
			return false;
		}
		return !mapOfInventoryCatagoryTreeNodeToCatagoryID.get(inventoryCategoryID).getInventoryCatagoryDetailsChildNodes().isEmpty();
	}
	
	
	public List<InventoryCatagoryType> getAllInventoryCategory(){
		List<InventoryCatagoryType> inventoryCatagoryTypes = new ArrayList<InventoryCatagoryType>();
		for(InventoryCatagoryType inventoryCatagoryType: mapOfInventoryCatagoryTypeToCatagoryTypeID.values()){
			if(!inventoryCatagoryType.isDeleted()){
				inventoryCatagoryTypes.add(inventoryCatagoryType);
			}
		}
		return inventoryCatagoryTypes;
	}
	public InventoryCatagoryType getInventoryCatgoryTypeByCatagoryID(int catagoryID){
		if(mapOfInventoryCatagoryTypeToCatagoryTypeID.isEmpty()){
			InventoryCatagoryRepository.getInstance().reload(true);
		}
		return mapOfInventoryCatagoryTypeToCatagoryTypeID.get(catagoryID);
	}
	
	public List<InventoryAttributeName> getInventoryAttributeNameListByCatagoryID(int catagoryID){
		List<InventoryAttributeName> inventoryAttributeNames = new ArrayList<InventoryAttributeName>();
		
		
		if(mapOfInventoryAttributeNameListToCatagaoryID.containsKey(catagoryID)){
			for(InventoryAttributeName inventoryAttributeName: mapOfInventoryAttributeNameListToCatagaoryID.get(catagoryID)){
				if(!inventoryAttributeName.isDeleted()){
					inventoryAttributeNames.add(inventoryAttributeName);
				}
			}
		}
		return inventoryAttributeNames;
	}
	
	private boolean isAnsestor(InventoryCatagoryTreeNode ansestorTreeNode, long decendantCatagoryID,List<InventoryCatagoryType> catagoryPath){
		catagoryPath.add(ansestorTreeNode.getInventoryCatagoryDetailsRootNode().getInventoryCatagoryType());
		if(ansestorTreeNode.getInventoryCatagoryDetailsRootNode().getInventoryCatagoryType().getID()==decendantCatagoryID){
			return true;
		}
		for(InventoryCatagoryTreeNode childNode : ansestorTreeNode.getInventoryCatagoryDetailsChildNodes()){
			if(isAnsestor(childNode, decendantCatagoryID, catagoryPath)){
				return true;
			}
		}
		catagoryPath.remove(catagoryPath.size()-1);
		return false;
	}
	
	public  List<InventoryCatagoryType> getInventoryCatagoryTypePathFromRoot(int catagoryID){
		
		InventoryCatagoryType inventoryCatagoryType = getInventoryCatgoryTypeByCatagoryID(catagoryID);
		if(inventoryCatagoryType==null||inventoryCatagoryType.isDeleted()){
			throw new RuntimeException("No inventory catagory found with catagory ID "+catagoryID);
		}
		List<InventoryCatagoryType> pathFromRootToNode ;
		if(!inventoryCatagoryType.hasParentCatagory()){
			pathFromRootToNode = new ArrayList<InventoryCatagoryType>();
			
		}else{
			pathFromRootToNode = getInventoryCatagoryTypePathFromRoot(inventoryCatagoryType.getParentCatagoryTypeID());
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
		
		InventoryCatagoryType descendantCategoryType = getInventoryCatgoryTypeByCatagoryID(descendantCategoryID);
		returnIDList = getIDListPathFromAnsestorToDescendant(ansestorCategoryID, descendantCategoryType.getParentCatagoryTypeID());
		returnIDList.add(descendantCategoryID);
		return returnIDList;
		
	}
	
	private InventoryRepository(){
		mapOfInventoryCatagoryTypeToCatagoryTypeID = InventoryCatagoryRepository.getInstance().mapOfInventoryCatagoryTypeToCatagoryTypeID;
		mapOfInventoryAttributeNameListToCatagaoryID = InventoryCatagoryAttributeNameRepository.getInstance().mapOfInventoryAttruvbuteNameListToCatagaoryID;
		mapOfInventoryAttributeNameToAttributeNameID = InventoryCatagoryAttributeNameRepository.getInstance().mapOfInventoryAttributeNameToAttributeNameID;
		initializeCatagoryTree();
	}
	public InventoryAttributeName getInventoryAttributeNameByNameID(long inventoryAttributeNameID){
		InventoryAttributeName inventoryAttributeName = mapOfInventoryAttributeNameToAttributeNameID.get(inventoryAttributeNameID);
		return inventoryAttributeName!=null&&!inventoryAttributeName.isDeleted()?inventoryAttributeName:null;
	}
	
	public void reload(boolean isFirstReload){
		InventoryCatagoryRepository.getInstance().reload(isFirstReload);
		InventoryCatagoryAttributeNameRepository.getInstance().reload(isFirstReload);
	}
	
	private void initializeCatagoryTree(){
		mapOfInventoryCatagoryDetailsToCatagoryID = new HashMap<Integer, InventoryCatagoryDetails>();
		mapOfInventoryCatagoryTreeNodeToCatagoryID = new HashMap<Integer, InventoryCatagoryTreeNode>();
		createInventoryCatagoryDetails();
		createInventoryCatagoryTreeNodes();
		createInventoryCatagoryTreeEdges();
	}
	
	public InventoryCatagoryTreeNode getInventoryCatagoryTreeNodeByCatagoryID(int catagoryID){
		initializeCatagoryTree();
		return mapOfInventoryCatagoryTreeNodeToCatagoryID.get(catagoryID);
	}
	
	public List<InventoryCatagoryTreeNode> getInventoryCategoryTreeRootNodes(){
		initializeCatagoryTree();
		List<InventoryCatagoryTreeNode> inventoryTreeRootNodes = new ArrayList<InventoryCatagoryTreeNode>();
		for(InventoryCatagoryTreeNode inventoryCatagoryTreeNode : mapOfInventoryCatagoryTreeNodeToCatagoryID.values()){
			if(!inventoryCatagoryTreeNode.getInventoryCatagoryDetailsRootNode().getInventoryCatagoryType().hasParentCatagory()){
				inventoryTreeRootNodes.add(inventoryCatagoryTreeNode);
			}
		}
		return inventoryTreeRootNodes;
	}
	private void createInventoryCatagoryDetails(){
		for(InventoryCatagoryType inventoryCatagoryType: mapOfInventoryCatagoryTypeToCatagoryTypeID.values()){
			
			if(inventoryCatagoryType.isDeleted()){
				continue;
			}
			
			InventoryCatagoryDetails inventoryCatagoryDetails = new InventoryCatagoryDetails();
			inventoryCatagoryDetails.setInventoryCatagoryType(inventoryCatagoryType);
			List<InventoryAttributeName> inventoryAttributeNameList = new ArrayList<InventoryAttributeName>();
			if(mapOfInventoryAttributeNameListToCatagaoryID.containsKey(inventoryCatagoryType.getID()))
			{
				Collection<InventoryAttributeName> inventoryAttributeNames =  mapOfInventoryAttributeNameListToCatagaoryID.get(inventoryCatagoryType.getID());
				for(InventoryAttributeName inventoryAttributeName : inventoryAttributeNames){
					if(inventoryAttributeName.isDeleted()==false){
						inventoryAttributeNameList.add(inventoryAttributeName);
					}
				}
			}
			inventoryCatagoryDetails.setInventoryAttributeNameList(inventoryAttributeNameList);
			mapOfInventoryCatagoryDetailsToCatagoryID.put(inventoryCatagoryType.getID(),inventoryCatagoryDetails);
		}
	}
	private void createInventoryCatagoryTreeNodes(){		
		 for(InventoryCatagoryDetails inventoryCatagoryDetails : mapOfInventoryCatagoryDetailsToCatagoryID.values()){
			 if(inventoryCatagoryDetails.getInventoryCatagoryType().isDeleted()){				 
				 continue;
			 }
			 InventoryCatagoryTreeNode inventoryCatagoryTreeNode = new InventoryCatagoryTreeNode();
			 inventoryCatagoryTreeNode.setInventoryCatagoryDetailsRootNode(inventoryCatagoryDetails);
			 inventoryCatagoryTreeNode.setInventoryCatagoryDetailsChildNodes(new ArrayList<>());
			 mapOfInventoryCatagoryTreeNodeToCatagoryID.put(inventoryCatagoryDetails.getInventoryCatagoryType().getID(), inventoryCatagoryTreeNode);
		 }
	}
	private void createInventoryCatagoryTreeEdges(){		
		for(InventoryCatagoryType inventoryCatagoryType : mapOfInventoryCatagoryTypeToCatagoryTypeID.values()){
			if(inventoryCatagoryType.isDeleted()){
				continue;
			}
			if(!inventoryCatagoryType.hasParentCatagory()){
				continue;
			}
			Integer catagoryID = inventoryCatagoryType.getID();
			Integer parentID = inventoryCatagoryType.getParentCatagoryTypeID();
			InventoryCatagoryTreeNode parentInventoryTreeNode = mapOfInventoryCatagoryTreeNodeToCatagoryID.get(parentID);
			InventoryCatagoryTreeNode childInventoryTreeNode = mapOfInventoryCatagoryTreeNodeToCatagoryID.get(catagoryID);
			if(parentInventoryTreeNode == null)continue;
			if(parentInventoryTreeNode.getInventoryCatagoryDetailsChildNodes()==null){
				parentInventoryTreeNode.setInventoryCatagoryDetailsChildNodes(new ArrayList<InventoryCatagoryTreeNode>());
			}
			parentInventoryTreeNode.getInventoryCatagoryDetailsChildNodes().add(childInventoryTreeNode);
		}
	}
	
	public static synchronized InventoryRepository getInstance(){
		if(instance == null){
			instance = new InventoryRepository();
		}
		return instance;
	}
}
