package nix.monthlyusage;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lli.monthlyBill.BillingRangeBreakDown;
import lli.monthlyBill.LLILongTermContractBreakDown;
import lli.monthlyBill.MbpsBreakDown;
import lli.monthlyUsage.LLIMonthlyUsageByConnection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nix.monthlybill.FeeByPortTypeForClient;
import util.JsonUtils;

import java.util.ArrayList;
import java.util.List;


@Data
@TableName("nix_monthly_usage_client")
@EqualsAndHashCode(callSuper=false)
public class NIXMonthlyUsageByClient {
	
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
	double grandTotal = 0.0;		//inclusive discount

	@ColumnName("discountPercentage")
	double discountPercentage = 0.0;
	@ColumnName("discount")
	double discount = 0.0;
	
	@ColumnName("totalPayable")
	double totalPayable = 0;
	
	
	@ColumnName("vatRate")
	double VatPercentage = 0.0;
	@ColumnName("vat")
	double VAT = 0.0;
	
	
	@ColumnName("netPayable")
	double netPayable = 0.0;
	
	//@ColumnName("adjustmentAmount")
	//double adjustmentAmount;
	

	@ColumnName("lateFee")
	double lateFee = 0.0;
	@ColumnName(value = "description", editable = false)
	String description = "";
	
	List<NIXMonthlyUsageByConnection> monthlyUsageByConnections = new ArrayList<>();

	List<FeeByPortTypeForClient> feeByPortTypeForClients=new ArrayList<>();

	
	void setDataFromDBContent()
	{
	}
}
