package util;

class ColumnNameValuePair{
	String columnName;
	Object value;
	public ColumnNameValuePair(){}
	public ColumnNameValuePair(String columnName,Object value){
		this.columnName = columnName;
		this.value = value;
	}
	@Override
	public String toString() {
		return "ColumnNameValuePair [columnName=" + columnName + ", value="
				+ value + "]";
	}
	
}
