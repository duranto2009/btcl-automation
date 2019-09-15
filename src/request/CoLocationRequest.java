package request;

import annotation.*;
 
@TableName("at_colocation_req")
@ForeignKeyName("colocrqReqID")
public class CoLocationRequest extends CommonRequestDTO{
	@PrimaryKey
	@ColumnName("colocrqID")
	long ID;
	@ColumnName("colocrqYear")
	long year;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getYear() {
		return year;
	}
	public void setYear(long year) {
		this.year = year;
	}
	@Override
	public String toString() {
		return "CoLocationRequest [ID=" + ID + ", year=" + year + "]";
	}
	
	
}
