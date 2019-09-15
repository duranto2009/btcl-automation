package lli;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
@TableName("at_lli_connection_application")
public class LLIApplicationType {
	
	@PrimaryKey
	@ColumnName("ID")
	long ID;
	@ColumnName("name")
	String name;
	@ColumnName("fields")
	String fields;
	
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}
	
}
