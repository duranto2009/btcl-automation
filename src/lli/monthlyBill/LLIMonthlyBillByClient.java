package lli.monthlyBill;

import java.util.ArrayList;
import java.util.List;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import util.JsonUtils;

@Data
@TableName("at_lli_monthly_bill_client")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class LLIMonthlyBillByClient
{

	@PrimaryKey
	@ColumnName("id")
	Long id;	
	
	@ColumnName("clientId")
	long clientId;
	
	@ColumnName("ltcAdjustment")
	double longTermContructAdjustment;		//after discount
	
	@ColumnName("ltcDiscountAdjustment")
	double longTermContructDiscountAdjustment;
	
	@ColumnName("mbpsBreakDownContent")
	String mbpsBreakDownContent;
	
	@ColumnName("billingRangeBreakDownContent")
	String billingRangeBreakDownContent;
	
	@ColumnName("mbpsBreakDownForCacheContent")
	String mbpsBreakDownForCacheContent;
	
	@ColumnName("billingRangeBreakDownForCacheContent")
	String billingRangeBreakDownForCacheContent;
	
	@ColumnName("longTermContractBreakDownContent")
	String longTermContractBreakDownContent;
	
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
	
	
	List<LLIMonthlyBillByConnection> monthlyBillByConnections = new ArrayList<>();
	
	
	MbpsBreakDown totalMbpsBreakDown;
	BillingRangeBreakDown billingRangeBreakDown;
	LLILongTermContractBreakDown longTermContractBreakDown;
	
	MbpsBreakDown totalMbpsBreakDownForCache;
	BillingRangeBreakDown billingRangeBreakDownForCache;

	public MbpsBreakDown getTotalMbpsBreakDown()
	{
		this.totalMbpsBreakDown = JsonUtils.getObjectByJsonString(this.mbpsBreakDownContent, MbpsBreakDown.class);
		return this.totalMbpsBreakDown;
	}
	
	public void setTotalMbpsBreakDown(MbpsBreakDown mbpsBreakDown)
	{
		this.totalMbpsBreakDown = mbpsBreakDown;
		this.mbpsBreakDownContent = JsonUtils.getJsonStringFromObject(mbpsBreakDown);
	}
	
	public BillingRangeBreakDown getBillingRangeBreakDown()
	{
		this.billingRangeBreakDown = JsonUtils.getObjectByJsonString(this.billingRangeBreakDownContent, BillingRangeBreakDown.class);
		return this.billingRangeBreakDown;
	}
	
	public void setBillingRangeBreakDown(BillingRangeBreakDown billingRangeBreakDown)
	{
		this.billingRangeBreakDown = billingRangeBreakDown;
		this.billingRangeBreakDownContent = JsonUtils.getJsonStringFromObject(billingRangeBreakDown);
	}
	
	public LLILongTermContractBreakDown getLongTermContractBreakDown()
	{
		this.longTermContractBreakDown = JsonUtils.getObjectByJsonString(this.longTermContractBreakDownContent, LLILongTermContractBreakDown.class);
		return this.longTermContractBreakDown;
	}
	
	public void setLongTermContractBreakDown(LLILongTermContractBreakDown longTermContractBreakDown)
	{
		this.longTermContractBreakDown = longTermContractBreakDown;
		this.longTermContractBreakDownContent = JsonUtils.getJsonStringFromObject(longTermContractBreakDown);
	}

	public MbpsBreakDown getTotalMbpsBreakDownForCache()
	{
		this.totalMbpsBreakDownForCache = JsonUtils.getObjectByJsonString(this.mbpsBreakDownForCacheContent, MbpsBreakDown.class);
		return this.totalMbpsBreakDownForCache;
	}
	
	public void setTotalMbpsBreakDownForCache(MbpsBreakDown mbpsBreakDown)
	{
		this.totalMbpsBreakDownForCache = mbpsBreakDown;
		this.mbpsBreakDownForCacheContent = JsonUtils.getJsonStringFromObject(mbpsBreakDown);
	}
	
	public BillingRangeBreakDown getBillingRangeBreakDownForCache()
	{
		this.billingRangeBreakDownForCache = JsonUtils.getObjectByJsonString(this.billingRangeBreakDownForCacheContent, BillingRangeBreakDown.class);
		return this.billingRangeBreakDownForCache;
	}
	
	public void setBillingRangeBreakDownForCache(BillingRangeBreakDown billingRangeBreakDown)
	{
		this.billingRangeBreakDownForCache = billingRangeBreakDown;
		this.billingRangeBreakDownForCacheContent = JsonUtils.getJsonStringFromObject(billingRangeBreakDown);
	}
	
	
	void setDataFromDBContent()
	{
		if(this.mbpsBreakDownContent != null)
			this.totalMbpsBreakDown = JsonUtils.getObjectByJsonString(this.mbpsBreakDownContent, MbpsBreakDown.class);
		
		if(this.billingRangeBreakDownContent != null)
			this.billingRangeBreakDown = JsonUtils.getObjectByJsonString(this.billingRangeBreakDownContent, BillingRangeBreakDown.class);
		
		if(this.mbpsBreakDownForCacheContent != null)
			this.totalMbpsBreakDownForCache = JsonUtils.getObjectByJsonString(this.mbpsBreakDownForCacheContent, MbpsBreakDown.class);
		
		if(this.billingRangeBreakDownForCacheContent != null)
			this.billingRangeBreakDownForCache = JsonUtils.getObjectByJsonString(this.billingRangeBreakDownForCacheContent, BillingRangeBreakDown.class);
		
		if(this.longTermContractBreakDownContent != null)
			this.longTermContractBreakDown = JsonUtils.getObjectByJsonString(this.longTermContractBreakDownContent, LLILongTermContractBreakDown.class);
	}
	
}
