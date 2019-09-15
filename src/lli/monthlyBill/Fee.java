package lli.monthlyBill;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
//@TableName("at_lli_monthly_bill_connection_fee")//not connection charge
public class Fee {
	
	/*@PrimaryKey
	@ColumnName("id")
	long id;


	@ColumnName("monthlyBillByConnectionId")
	long monthlyBillByConnectionId;
	
	@ColumnName("connectionId")
	long connectionId;
	*/
	
	@ColumnName("cost")
	double cost;
	@ColumnName("remark")
	String remark;

}
