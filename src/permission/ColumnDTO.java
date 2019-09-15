package permission;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("adcolumn")
public class ColumnDTO {

	@ColumnName("columnID")
	@PrimaryKey
	int columnID;
	@ColumnName("columnName")
	String columnName;
	@ColumnName("moduleTypeID")
	int moduleTypeID;
	
	
	public int getColumnID() {
		return columnID;
	}


	public void setColumnID(int columnID) {
		this.columnID = columnID;
	}


	public String getColumnName() {
		return columnName;
	}


	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}


	public int getModuleTypeID() {
		return moduleTypeID;
	}


	public void setModuleTypeID(int moduleTypeID) {
		this.moduleTypeID = moduleTypeID;
	}


	@Override
	public String toString() {
		return "ColumnDTO [columnID=" + columnID + ", columnName=" + columnName + ", moduleTypeID=" + moduleTypeID
				+ "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + columnID;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnDTO other = (ColumnDTO) obj;
		if (columnID != other.columnID)
			return false;
		return true;
	}
	
	
}
