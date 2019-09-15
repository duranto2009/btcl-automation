package vpn.bill;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_bill_configuration")
public class BillConfigurationDTO {

	@PrimaryKey
	@ColumnName("id")
	private long id;
	
	@ColumnName("moduleID")
	private int moduleID;
	
	@ColumnName("billTypeID")
	private int billTypeID;
	
	@ColumnName("headerFooterID")
	private int headerFooterID;
	
	@ColumnName("text")
	private String text;
	
	@ColumnName("isDeleted")
	private boolean isDeleted;
	
	@ColumnName("fontSize")
	private int fontSize;

	public int getBillTypeID() {
		return billTypeID;
	}

	public void setBillTypeID(int billTypeID) {
		this.billTypeID = billTypeID;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getModuleID() {
		return moduleID;
	}

	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}

	public int getHeaderFooterID() {
		return headerFooterID;
	}

	public void setHeaderFooterID(int headerFooterID) {
		this.headerFooterID = headerFooterID;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "BillConfigurationDTO [moduleID=" + moduleID + ", billTypeID=" + billTypeID + ", headerFooterID="
				+ headerFooterID + ", text=" + text + ", isDeleted=" + isDeleted + "]";
	}
}
