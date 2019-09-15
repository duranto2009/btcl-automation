package lli.monthlyUsage;

import java.util.ArrayList;
import java.util.List;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lli.monthlyBill.BillingRangeBreakDown;
import lli.monthlyBill.LLILongTermContractBreakDown;
import lli.monthlyBill.MbpsBreakDown;
import lombok.Data;
import lombok.EqualsAndHashCode;
import util.JsonUtils;



@Data
@TableName("at_lli_monthly_usage_client")
@EqualsAndHashCode(callSuper=false)
public class LLIMonthlyUsageByClient {
	
	@PrimaryKey
	@ColumnName("id")
	Long id;
	
	@ColumnName("clientId")
	long clientId;
	
	
	@ColumnName("ltcAdjustment")
	double longTermContructAdjustment;		//inclusive discount
	
	@ColumnName("ltcDiscountAdjustment")
	double longTermContructDiscountAdjustment;
	
	@ColumnName("mbpsBreakDownsContent")
	String mbpsBreakDownsContent;
	
	@ColumnName("billingRangeBreakDownsContent")
	String billingRangeBreakDownsContent;
	
	@ColumnName("mbpsBreakDownsForCacheContent")
	String mbpsBreakDownsForCacheContent;
	
	@ColumnName("billingRangeBreakDownsForCacheContent")
	String billingRangeBreakDownsForCacheContent;
	
	@ColumnName("longTermContractBreakDownsContent")
	String longTermContractBreakDownsContent;
	
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
	
	List<LLIMonthlyUsageByConnection> monthlyUsageByConnections = new ArrayList<>();
	
	List<MbpsBreakDown> totalMbpsBreakDowns = new ArrayList<>();
	List<BillingRangeBreakDown> billingRangeBreakDowns = new ArrayList<>();
	List<LLILongTermContractBreakDown> longTermContractBreakDowns = new ArrayList<>();
	
	List<MbpsBreakDown> totalMbpsBreakDownsForCache = new ArrayList<>();
	List<BillingRangeBreakDown> billingRangeBreakDownsForCache = new ArrayList<>();
	
	void setMbpsBreakDownsContent()
	{
		this.mbpsBreakDownsContent = JsonUtils.getJsonStringFromList(this.totalMbpsBreakDowns);
	}
	
	void setBillingRangeBreakDownsContent()
	{
		this.billingRangeBreakDownsContent = JsonUtils.getJsonStringFromList(this.billingRangeBreakDowns);
	}
	
	void setMbpsBreakDownsForCacheContent()
	{
		this.mbpsBreakDownsForCacheContent = JsonUtils.getJsonStringFromList(this.totalMbpsBreakDownsForCache);
	}
	
	void setBillingRangeBreakDownsForCacheContent()
	{
		this.billingRangeBreakDownsForCacheContent = JsonUtils.getJsonStringFromList(this.billingRangeBreakDownsForCache);
	}
	
	void setLongTermContractBreakDownsContent()
	{
		this.longTermContractBreakDownsContent = JsonUtils.getJsonStringFromList(this.longTermContractBreakDowns);
	}

	
	void setDataFromDBContent()
	{

		if(this.mbpsBreakDownsContent != null)
			this.totalMbpsBreakDowns = JsonUtils.getObjectListByJsonString(this.mbpsBreakDownsContent, MbpsBreakDown.class);
		
		if(this.billingRangeBreakDownsContent != null)
			this.billingRangeBreakDowns = JsonUtils.getObjectListByJsonString(this.billingRangeBreakDownsContent, BillingRangeBreakDown.class);
		
		if(this.mbpsBreakDownsForCacheContent != null)
			this.totalMbpsBreakDownsForCache = JsonUtils.getObjectListByJsonString(this.mbpsBreakDownsForCacheContent, MbpsBreakDown.class);
		
		if(this.billingRangeBreakDownsForCacheContent != null)
			this.billingRangeBreakDownsForCache = JsonUtils.getObjectListByJsonString(this.billingRangeBreakDownsForCacheContent, BillingRangeBreakDown.class);
		
		if(this.longTermContractBreakDownsContent != null)
			this.longTermContractBreakDowns = JsonUtils.getObjectListByJsonString(this.longTermContractBreakDownsContent, LLILongTermContractBreakDown.class);
	}
}
