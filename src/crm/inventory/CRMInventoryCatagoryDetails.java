package crm.inventory;

import java.util.*;

public class CRMInventoryCatagoryDetails {
	private CRMInventoryCatagoryType CRMInventoryCatagoryType;
	List<CRMInventoryAttributeName> CRMInventoryAttributeNameList;
	public CRMInventoryCatagoryType getCRMInventoryCatagoryType() {
		return CRMInventoryCatagoryType;
	}
	public void setCRMInventoryCatagoryType(CRMInventoryCatagoryType CRMInventoryCatagoryType) {
		this.CRMInventoryCatagoryType = CRMInventoryCatagoryType;
	}
	public List<CRMInventoryAttributeName> getCRMInventoryAttributeNameList() {
		return CRMInventoryAttributeNameList;
	}
	public void setCRMInventoryAttributeNameList(
			List<CRMInventoryAttributeName> CRMInventoryAttributeNameList) {
		this.CRMInventoryAttributeNameList = CRMInventoryAttributeNameList;
	}
	@Override
	public String toString() {
		
		
		String description = "CRMInventoryCatagoryDetails [CRMInventoryCatagoryType="
				+ CRMInventoryCatagoryType ;
		
		for(CRMInventoryAttributeName CRMInventoryAttributeName: CRMInventoryAttributeNameList){
			description+="\n";
			description+="->"+CRMInventoryAttributeName;
		}
		return description;
	}
	
}
