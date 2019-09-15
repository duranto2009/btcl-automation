package crm.inventory.form;

import java.util.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.struts.action.ActionForm;

public class CRMInventoryItemDetailsForm extends ActionForm{
	private static final long serialVersionUID = 1L;
	
	long[] attributeNameIDs;
	String[] attributeValues;
	int categoryID;
	Long parentItemID;
	public long[] getAttributeNameIDs() {
		return attributeNameIDs;
	}
	public void setAttributeNameIDs(long[] attributeNameIDs) {
		this.attributeNameIDs = attributeNameIDs;
	}
	public String[] getAttributeValues() {
		return attributeValues;
	}
	public void setAttributeValues(String[] attributeValues) {
		this.attributeValues = attributeValues;
	}
	public int getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}
	public Long getParentItemID() {
		return parentItemID;
	}
	public void setParentItemID(Long parentItemID) {
		this.parentItemID = parentItemID;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\nattributeNameIDs "+ Arrays.toString(attributeNameIDs));
		builder.append("\n,attributeValues "+ Arrays.toString(attributeValues));
		builder.append("\ncategoryID "+ categoryID);
		builder.append("\nparentItemID "+ parentItemID);
		return builder.toString();
	}
	
	
}
