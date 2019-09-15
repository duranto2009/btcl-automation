package nix.monthlybillsummary;

import annotation.*;
import common.bill.BillConstants;
import common.bill.BillDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nix.constants.NIXConstants;
import java.util.ArrayList;
import java.util.List;

@Data
@TableName("nix_monthly_bill_summary_client")
@ForeignKeyName("bill_id")
@EqualsAndHashCode(callSuper=false)
@AccountingLogic(NIXMonthlyBillSummaryByClientBusinessLogic.class)
public class NIXMonthlyBillSummaryByClient extends BillDTO {


	@PrimaryKey
	@ColumnName("id")
	Long id;
	//there is another clientID in bill DTO. but we are using it here for searching
	@ColumnName("clientId")
	long clientId;

	@ColumnName("createdDate")
	long createdDate;

	List<NIXMonthlyBillSummaryByItem> nixMonthlyBillSummaryByItems = new ArrayList<>();

	public NIXMonthlyBillSummaryByClient()
	{
		setClassName(this.getClass().getName());
		setBillType(BillConstants.MONTHLY_BILL);
		setPaymentStatus(BillDTO.UNPAID);
		setEntityTypeID(NIXConstants.ENTITY_TYPE);

	}
	
	public void setClientId(long clientId)
	{
		this.clientId = clientId;
		//set for bill
		this.setClientID(clientId);
	}

	
	void setDataFromDBContent()
	{


	}
}
