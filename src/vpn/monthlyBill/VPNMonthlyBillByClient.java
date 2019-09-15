package vpn.monthlyBill;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lli.monthlyBill.BillingRangeBreakDown;
import lli.monthlyBill.MbpsBreakDown;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName("vpn_monthly_bill_client")
public class VPNMonthlyBillByClient
{

	@PrimaryKey
	@ColumnName("id")
	Long id;
	
	@ColumnName("clientId")
	long clientId;

	@ColumnName("mbpsBreakDownContent")
	String mbpsBreakDownContent;
	
	@ColumnName("billingRangeBreakDownContent")
	String billingRangeBreakDownContent;

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
	
	
	List<VPNMonthlyBillByLink> monthlyBillByLinks = new ArrayList<>();
	
	
	MbpsBreakDown totalMbpsBreakDown;
	BillingRangeBreakDown billingRangeBreakDown;

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
