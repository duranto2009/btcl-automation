package inventory;

import java.util.*;

public class InventoryCatagoryDetails {
	private InventoryCatagoryType inventoryCatagoryType;
	List<InventoryAttributeName> inventoryAttributeNameList;
	public InventoryCatagoryType getInventoryCatagoryType() {
		return inventoryCatagoryType;
	}
	public void setInventoryCatagoryType(InventoryCatagoryType inventoryCatagoryType) {
		this.inventoryCatagoryType = inventoryCatagoryType;
	}
	public List<InventoryAttributeName> getInventoryAttributeNameList() {
		return inventoryAttributeNameList;
	}
	public void setInventoryAttributeNameList(
			List<InventoryAttributeName> inventoryAttributeNameList) {
		this.inventoryAttributeNameList = inventoryAttributeNameList;
	}
	@Override
	public String toString() {
		
		
		String description = "InventoryCatagoryDetails [inventoryCatagoryType="
				+ inventoryCatagoryType ;
		
		for(InventoryAttributeName inventoryAttributeName: inventoryAttributeNameList){
			description+="\n";
			description+="->"+inventoryAttributeName;
		}
		return description;
	}
	
}
