package config;

import java.util.*;

import util.*;
import java.io.FileInputStream;
import org.apache.log4j.*;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_global_config")
public class GlobalConfigDTO
{
	@PrimaryKey
	@ColumnName("gcID")
	int ID;
	@ColumnName("gcName")
	String Name;
	@ColumnName("gcValue")
	int value;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

}
