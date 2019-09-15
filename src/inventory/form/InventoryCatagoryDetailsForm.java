package inventory.form;
import java.util.*;

import inventory.InventoryAttributeName;
import inventory.InventoryCatagoryType;


import org.apache.struts.action.*;

public class InventoryCatagoryDetailsForm extends ActionForm{
	private static final long serialVersionUID = 1L;
	private InventoryCatagoryType inventoryCatagoryType;
	private List<InventoryAttributeName> inventoryAttributeNames;
	
	public InventoryCatagoryType getInventoryCatagoryType() {
		return inventoryCatagoryType;
	}
	public void setInventoryCatagoryType(InventoryCatagoryType inventoryCatagoryType) {
		this.inventoryCatagoryType = inventoryCatagoryType;
	}
	public List<InventoryAttributeName> getInventoryAttributeNames() {
		return inventoryAttributeNames;
	}
	public void setInventoryAttributeNames(
			List<InventoryAttributeName> inventoryAttributeNames) {
		this.inventoryAttributeNames = inventoryAttributeNames;
	}
	
}
