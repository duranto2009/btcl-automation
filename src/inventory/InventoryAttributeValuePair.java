package inventory;

public class InventoryAttributeValuePair {
	InventoryAttributeName attributeName;
	InventoryAttributeValue attributeValue;
	public InventoryAttributeValuePair(InventoryAttributeName attributeName,InventoryAttributeValue attributeValue){
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}
	@Override
	public String toString() {
		return "InventoryAttributeValuePair [attributeName=" + attributeName
				+ ", attributeValue=" + attributeValue + "]";
	}
}
