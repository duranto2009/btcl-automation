package nix.monthlybill;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lli.monthlyBill.BillingRangeBreakDown;
import lli.monthlyBill.LLILongTermContractBreakDown;
import lli.monthlyBill.LLIMonthlyBillByConnection;
import lli.monthlyBill.MbpsBreakDown;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName("nix_monthly_bill_client")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class NIXMonthlyBillByClient
{

	@PrimaryKey
	@ColumnName("id")
	Long id;
	@ColumnName("clientId")
	long clientId;
	@ColumnName("createdDate")
	long createdDate;
	@ColumnName(value = "month",editable = false)
	int month = 0;
	@ColumnName("year")
	int year = 0;
	@ColumnName("isDeleted")
	boolean isDeleted;
	@ColumnName("grandTotal")
	double grandTotal = 0.0;	//after discount
	@ColumnName("discountRate")
	double discountPercentage = 0;
	@ColumnName("discount")
	double discount = 0.0;
	@ColumnName("totalPayable")
	double totalPayable = 0;
	@ColumnName("vatRate")
	double VatPercentage = 0;
	@ColumnName("vat")
	double VAT = 0.0;
	@ColumnName("netPayable")
	double netPayable = 0.0;
	List<NIXMonthlyBillByConnection> monthlyBillByConnections = new ArrayList<>();

	List<FeeByPortTypeForClient> feeByPortTypeForClients=new ArrayList<>();
	
}
