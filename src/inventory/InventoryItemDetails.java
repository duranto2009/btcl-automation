package inventory;
import java.util.*;

import common.RequestFailureException;


public class InventoryItemDetails {
	String parentItemName;
	InventoryItem inventoryItem;
	List<InventoryAttributeValuePair> attributeNameValuePairList = new ArrayList<InventoryAttributeValuePair>();
	List<InventoryAttributeValue> inventoryAttributeValues = new ArrayList<InventoryAttributeValue>();
	List<InventoryAttributeName> inventoryAttributeNames = new ArrayList<InventoryAttributeName>();
	private Map<Long,InventoryAttributeValue> mapOfInventoryAttributeValueToAttributeNameID = new HashMap<Long, InventoryAttributeValue>();
	private Map<String,InventoryAttributeName> mapOfInventoryAttributeNameDTOToName = new HashMap<String, InventoryAttributeName>();
	
	public String getParentItemName() {
		return parentItemName;
	}

	public void setParentItemName(String parentItemName) {
		this.parentItemName = parentItemName;
	}

	public InventoryItem getInventoryItem() {
		return inventoryItem;
	}

	public void setInventoryItem(InventoryItem inventoryItem) {
		this.inventoryItem = inventoryItem;
	}

	public List<InventoryAttributeValuePair> getAttributeValuePairList(){
		return attributeNameValuePairList;
	}
	
	public void settAttributeValuePairList(List<InventoryAttributeValuePair> attributeValuePairs){
		this.attributeNameValuePairList = attributeValuePairs;
	}
	
	public void addAttributeValuePair(InventoryAttributeValuePair inventoryAttributeValuePair){
		attributeNameValuePairList.add(inventoryAttributeValuePair);
	}
	public List<InventoryAttributeValue> getInventoryAttributeValues() {
		return inventoryAttributeValues;
	}
	public void setInventoryAttributeValues(
			List<InventoryAttributeValue> inventoryAttributeValues) {
		this.inventoryAttributeValues = inventoryAttributeValues;
		for(InventoryAttributeValue inventoryAttributeValue: inventoryAttributeValues){
			mapOfInventoryAttributeValueToAttributeNameID.put(inventoryAttributeValue.getInventoryAttributeNameID(), inventoryAttributeValue);
		}
	}
	

	public List<InventoryAttributeName> getInventoryAttributeNames() {
		return inventoryAttributeNames;
	}

	public void setInventoryAttributeNames(
			List<InventoryAttributeName> inventoryAttributeNames) {
		this.inventoryAttributeNames = inventoryAttributeNames;
		for(InventoryAttributeName inventoryAttributeName: inventoryAttributeNames){
			mapOfInventoryAttributeNameDTOToName.put(inventoryAttributeName.getName(), inventoryAttributeName);
		}
	}
	public String getValueByAttributeName(String attributeName){
		InventoryAttributeName inventoryAttributeName = mapOfInventoryAttributeNameDTOToName.get(attributeName);
		if(inventoryAttributeName == null){
			throw new RequestFailureException("No Attribute Found with Name :"+attributeName);
		}
		InventoryAttributeValue inventoryAttributeValue = mapOfInventoryAttributeValueToAttributeNameID.get(inventoryAttributeName.getID()); 
		if(inventoryAttributeValue == null){
			throw new RequestFailureException("No Attribute value found with name :"+attributeName);
		}
		return inventoryAttributeValue.getValue();
		
	}
	
	public InventoryAttributeValue getInventoryAttributeValueByAttributeID(long attributeID) {
		return mapOfInventoryAttributeValueToAttributeNameID.get(attributeID);
	}
	@Override
	public String toString() {
		return "InventoryItemDetails [inventoryItem=" + inventoryItem
				+ ", inventoryAttributeValues=" + inventoryAttributeValues
				+ "]";
	}
	
}
