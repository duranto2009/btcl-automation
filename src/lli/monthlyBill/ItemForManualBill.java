package lli.monthlyBill;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("item_for_manual_bill")
public class ItemForManualBill {
	@PrimaryKey
	@ColumnName("id")
	long id;
	@ColumnName("manual_bill_id")
	long manualBillId;
	@ColumnName("item")
	String item;
	@ColumnName("cost")
	double cost;
}
