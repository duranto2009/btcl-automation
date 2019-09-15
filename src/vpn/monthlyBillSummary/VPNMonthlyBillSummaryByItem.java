package vpn.monthlyBillSummary;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import util.DateUtils;

@Data
@TableName("vpn_monthly_bill_summary_item")
@EqualsAndHashCode(callSuper=false)
public class VPNMonthlyBillSummaryByItem {

	@PrimaryKey
	@ColumnName("id")
	Long id;

	@ColumnName("monthlyBillSummaryByClientId")
	long monthlyBillSummaryByClientId;

	@ColumnName("type")
	int type;

	@ColumnName("grandCost")
	double grandCost = 0.0;

	@ColumnName("discount")
	double discount = 0.0;

	@ColumnName("totalCost")
	double totalCost = 0.0;

	@ColumnName("vatRate")
	double vatRate = 0.0;

	@ColumnName("vat")
	double vat = 0.0;

	@ColumnName("netCost")
	double netCost = 0.0;

	@ColumnName("remark")
	String remark = "";

	@ColumnName("createdDate")
	long createdDate;

	String billType = "";

	public void setType()
	{
		switch(type)
		{
		case VPNMonthlyBillSummaryType.BANDWIDTH:
				billType = "Regular Bandwidth Charge";
			break;

		case VPNMonthlyBillSummaryType.LOCAL_LOOP:
				billType = "Local Loop Charge";
			break;

		case VPNMonthlyBillSummaryType.BANDWIDTH_ADJUSTMENT:
				billType = "Adjustment of Regular BW Usage";
			break;

		case VPNMonthlyBillSummaryType.LOCAL_LOOP_ADJUSTMENT:
				billType = "Adjustment of Local Loop Usage";
			break;

		case VPNMonthlyBillSummaryType.DEMANDNOTE_ADJUSTMENT:
				billType = "Demand Note Charge";
			break;

		default :
				billType = "";
			break;

		}

	}


	//use this after fetching data from db
	void setDataFromDBContent()
	{
		setType();
	}


	public VPNMonthlyBillSummaryByItem() {
		super();
		this.createdDate = DateUtils.getCurrentTime();
	}
}
