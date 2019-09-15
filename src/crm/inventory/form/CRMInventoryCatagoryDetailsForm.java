package crm.inventory.form;
import java.util.*;

import org.apache.struts.action.*;

import crm.inventory.CRMInventoryAttributeName;
import crm.inventory.CRMInventoryCatagoryType;

public class CRMInventoryCatagoryDetailsForm extends ActionForm{
	private static final long serialVersionUID = 1L;
	private CRMInventoryCatagoryType inventoryCatagoryType;
	private List<CRMInventoryAttributeName> inventoryAttributeNames;
	
	public CRMInventoryCatagoryType getCRMInventoryCatagoryType() {
		return inventoryCatagoryType;
	}
	public void setCRMInventoryCatagoryType(CRMInventoryCatagoryType inventoryCatagoryType) {
		this.inventoryCatagoryType = inventoryCatagoryType;
	}
	public List<CRMInventoryAttributeName> getCRMInventoryAttributeNames() {
		return inventoryAttributeNames;
	}
	public void setCRMInventoryAttributeNames(
			List<CRMInventoryAttributeName> inventoryAttributeNames) {
		this.inventoryAttributeNames = inventoryAttributeNames;
	}
	
}
