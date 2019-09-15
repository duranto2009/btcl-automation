package lli.monthlyBillSummary;

import annotation.*;
import common.bill.BillConstants;
import common.bill.BillDTO;
import lli.connection.LLIConnectionConstants;
import lli.monthlyBill.BillingRangeBreakDown;
import lli.monthlyBill.LLILongTermContractBreakDown;
import lli.monthlyBill.MbpsBreakDown;
import lombok.Data;
import lombok.EqualsAndHashCode;
import util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName("at_lli_monthly_bill_summary_client")
@ForeignKeyName("bill_id")
@EqualsAndHashCode(callSuper=false)
@AccountingLogic(LLIMonthlyBillSummaryByClientBusinessLogic.class)
public class LLIMonthlyBillSummaryByClient extends BillDTO {

	
	@PrimaryKey
	@ColumnName("id")
	Long id;	
	
	//there is another clientID in bill DTO. but we are using it here for searching 
	@ColumnName("clientId")
	long clientId;
	
	
	@ColumnName("mbpsBreakDownContent")
	String mbpsBreakDownContent;
	
	@ColumnName("billingRangeBreakDownContent")
	String billingRangeBreakDownContent;
	
	@ColumnName("longTermContractBreakDownContent")
	String longTermContractBreakDownContent;
	
	
	@ColumnName("mbpsBreakDownForCacheContent")
	String mbpsBreakDownForCacheContent;
	
	@ColumnName("billingRangeBreakDownForCacheContent")
	String billingRangeBreakDownForCacheContent;
	
	
	@ColumnName("createdDate")
	long createdDate;
	
	
	List<LLIMonthlyBillSummaryByItem> lliMonthlyBillSummaryByItems = new ArrayList<>();
	
	MbpsBreakDown totalMbpsBreakDown;
	BillingRangeBreakDown billingRangeBreakDown;
	LLILongTermContractBreakDown longTermContractBreakDown;
	
	MbpsBreakDown totalMbpsBreakDownForCache;
	BillingRangeBreakDown billingRangeBreakDownForCache;
	
	public LLIMonthlyBillSummaryByClient()
	{
		setClassName(this.getClass().getName());
		setBillType(BillConstants.MONTHLY_BILL);
		setPaymentStatus(BillDTO.UNPAID);
		setEntityTypeID(LLIConnectionConstants.ENTITY_TYPE);

	}
	
	public void setClientId(long clientId)
	{
		this.clientId = clientId;
		//set for bill
		this.setClientID(clientId);
	}


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
		
		if(this.longTermContractBreakDownContent != null)
			this.longTermContractBreakDown = JsonUtils.getObjectByJsonString(this.longTermContractBreakDownContent, LLILongTermContractBreakDown.class);
		
		if(this.mbpsBreakDownForCacheContent != null)
			this.totalMbpsBreakDownForCache = JsonUtils.getObjectByJsonString(this.mbpsBreakDownForCacheContent, MbpsBreakDown.class);
		
		if(this.billingRangeBreakDownForCacheContent != null)
			this.billingRangeBreakDownForCache = JsonUtils.getObjectByJsonString(this.billingRangeBreakDownForCacheContent, BillingRangeBreakDown.class);
		
	}
}
