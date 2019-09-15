package vpn.monthlyBillSummary;

import annotation.*;
import common.bill.BillConstants;
import common.bill.BillDTO;
import lli.monthlyBill.BillingRangeBreakDown;
import lli.monthlyBill.MbpsBreakDown;
import lombok.Data;
import lombok.EqualsAndHashCode;
import util.JsonUtils;
import vpn.VPNConstants;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName("vpn_monthly_bill_summary_client")
@ForeignKeyName("bill_id")
@EqualsAndHashCode(callSuper=false)
@AccountingLogic(VPNMonthlyBillSummaryByClientBusinessLogic.class)
public class VPNMonthlyBillSummaryByClient extends BillDTO {


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

	@ColumnName("createdDate")
	long createdDate;


	List<VPNMonthlyBillSummaryByItem> vpnMonthlyBillSummaryByItems = new ArrayList<>();

	MbpsBreakDown totalMbpsBreakDown;
	BillingRangeBreakDown billingRangeBreakDown;

	public VPNMonthlyBillSummaryByClient()
	{
		setClassName(this.getClass().getName());
		setBillType(BillConstants.MONTHLY_BILL);
		setPaymentStatus(BillDTO.UNPAID);
		setEntityTypeID(VPNConstants.ENTITY_TYPE);
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

	void setDataFromDBContent()
	{
		if(this.mbpsBreakDownContent != null)
			this.totalMbpsBreakDown = JsonUtils.getObjectByJsonString(this.mbpsBreakDownContent, MbpsBreakDown.class);
		
		if(this.billingRangeBreakDownContent != null)
			this.billingRangeBreakDown = JsonUtils.getObjectByJsonString(this.billingRangeBreakDownContent, BillingRangeBreakDown.class);
	}
}
