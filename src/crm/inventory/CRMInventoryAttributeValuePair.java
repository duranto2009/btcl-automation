package crm.inventory;

public class CRMInventoryAttributeValuePair {
	CRMInventoryAttributeName attributeName;
	CRMInventoryAttributeValue attributeValue;
	public CRMInventoryAttributeValuePair(CRMInventoryAttributeName attributeName,CRMInventoryAttributeValue attributeValue){
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}
	@Override
	public String toString() {
		return "CRMCRMInventoryAttributeValuePair [attributeName=" + attributeName
				+ ", attributeValue=" + attributeValue + "]";
	}
}
