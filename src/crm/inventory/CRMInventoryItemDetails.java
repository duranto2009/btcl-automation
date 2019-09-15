package crm.inventory;
import java.util.*;

import common.RequestFailureException;


public class CRMInventoryItemDetails {
	String parentItemName;
	CRMInventoryItem inventoryItem;
	List<CRMInventoryAttributeValuePair> attributeNameValuePairList = new ArrayList<CRMInventoryAttributeValuePair>();
	List<CRMInventoryAttributeValue> inventoryAttributeValues = new ArrayList<CRMInventoryAttributeValue>();
	List<CRMInventoryAttributeName> inventoryAttributeNames = new ArrayList<CRMInventoryAttributeName>();
	private Map<Long,CRMInventoryAttributeValue> mapOfCRMInventoryAttributeValueToAttributeNameID = new HashMap<Long, CRMInventoryAttributeValue>();
	private Map<String,CRMInventoryAttributeName> mapOfCRMInventoryAttributeNameDTOToName = new HashMap<String, CRMInventoryAttributeName>();
	
	public String getParentItemName() {
		return parentItemName;
	}

	public void setParentItemName(String parentItemName) {
		this.parentItemName = parentItemName;
	}

	public CRMInventoryItem getCRMInventoryItem() {
		return inventoryItem;
	}

	public void setCRMInventoryItem(CRMInventoryItem inventoryItem) {
		this.inventoryItem = inventoryItem;
	}

	public List<CRMInventoryAttributeValuePair> getAttributeValuePairList(){
		return attributeNameValuePairList;
	}
	
	public void settAttributeValuePairList(List<CRMInventoryAttributeValuePair> attributeValuePairs){
		this.attributeNameValuePairList = attributeValuePairs;
	}
	
	public void addAttributeValuePair(CRMInventoryAttributeValuePair inventoryAttributeValuePair){
		attributeNameValuePairList.add(inventoryAttributeValuePair);
	}
	public List<CRMInventoryAttributeValue> getCRMInventoryAttributeValues() {
		return inventoryAttributeValues;
	}
	public void setCRMInventoryAttributeValues(
			List<CRMInventoryAttributeValue> inventoryAttributeValues) {
		this.inventoryAttributeValues = inventoryAttributeValues;
		for(CRMInventoryAttributeValue inventoryAttributeValue: inventoryAttributeValues){
			mapOfCRMInventoryAttributeValueToAttributeNameID.put(inventoryAttributeValue.getInventoryAttributeNameID(), inventoryAttributeValue);
		}
	}
	

	public List<CRMInventoryAttributeName> getCRMInventoryAttributeNames() {
		return inventoryAttributeNames;
	}

	public void setCRMInventoryAttributeNames(
			List<CRMInventoryAttributeName> inventoryAttributeNames) {
		this.inventoryAttributeNames = inventoryAttributeNames;
		for(CRMInventoryAttributeName inventoryAttributeName: inventoryAttributeNames){
			mapOfCRMInventoryAttributeNameDTOToName.put(inventoryAttributeName.getName(), inventoryAttributeName);
		}
	}
	public String getValueByAttributeName(String attributeName){
		System.out.println("name map: "+mapOfCRMInventoryAttributeNameDTOToName.keySet());
		CRMInventoryAttributeName inventoryAttributeName = mapOfCRMInventoryAttributeNameDTOToName.get(attributeName);
		if(inventoryAttributeName == null){
			throw new RequestFailureException("No Attribute Found with Name :"+attributeName);
		}
		CRMInventoryAttributeValue inventoryAttributeValue = mapOfCRMInventoryAttributeValueToAttributeNameID.get(inventoryAttributeName.getID()); 
		if(inventoryAttributeValue == null){
			throw new RequestFailureException("No Attribute value found with name :"+attributeName);
		}
		return inventoryAttributeValue.getValue();
		
	}
	@Override
	public String toString() {
		return "CRMCRMInventoryItemDetails [inventoryItem=" + inventoryItem
				+ ", inventoryAttributeValues=" + inventoryAttributeValues
				+ "]";
	}
	
}
