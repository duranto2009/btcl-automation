package lli.monthlyBill;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lli.LLIConnectionInstance;
import lli.connection.LLIConnectionConstants;
import lombok.Data;
import oracle.net.aso.a;
import util.JsonUtils;

@Data
@TableName("at_lli_monthly_bill_connection")
public class LLIMonthlyBillByConnection {

	@PrimaryKey
	@ColumnName("id")
	Long id;

	@ColumnName("monthlyBillByClientId")
	long monthlyBillByClientId;

	
	@ColumnName("clientId")
	long clientId;
	
	@ColumnName("connectionId")
	long connectionId;
	
	@ColumnName("type")
	int type = 0;
	
	@ColumnName("status")
	int status;
	
	@ColumnName("name")
	String name = "";
	
	@ColumnName("address")
	String address = "";
	
	
	@ColumnName("createdDate")
	long createdDate;
	
	@ColumnName("totalMbps")
	double totalMbps = 0.0;
	
	@ColumnName("mbpsRate")
	double mbpsRate = 0.0;
	
	@ColumnName("mbpsCost")
	double mbpsCost = 0.0;
	
	@ColumnName("loopCost")
	double loopCost = 0.0;
	
	@ColumnName("feesContent")
	String feesContent;
	
	@ColumnName("totalCost")
	double totalCost = 0.0;
	
	@ColumnName("remark")
	String remark = "";
		
	
	//before discount
	@ColumnName("grandCost")
	double grandCost = 0.0;	//new
	
	@ColumnName("discountRate")
	double discountRate = 0.0;	//new
	
	@ColumnName("discount")
	double discount = 0.0;	//new
	
	@ColumnName("vatRate")
	double vatRate = 0.0;
	
	@ColumnName("vat")
	double vat = 0.0;
	
	@ColumnName("localLoopBreakDownsContent")
	String localLoopBreakDownsContent;
	
	String connectionType;
	
	String connectionStatus;
	
	List<Fee> monthlyBillFees = new ArrayList<>();
	
	List<LLILocalLoopBreakDown> localLoopBreakDowns = new ArrayList<>();
	
	double totalMonthlyFees;
	String concatenatedRemark;
	
	void setTotalMonthlyFees() {
		this.totalMonthlyFees = this.monthlyBillFees.stream().mapToDouble(a->a.cost).sum();
	}
	
	void setConcatenatedRemark() {
		this.concatenatedRemark = this.monthlyBillFees.stream().map(a->a.remark == null ? "" : a.remark).collect(Collectors.joining(", "));
		this.concatenatedRemark = this.concatenatedRemark.isEmpty() ? "N/A" : this.concatenatedRemark;
	}
	

	void setConnectionType()
	{
		
		if(type == LLIConnectionConstants.CONNECTION_TYPE_REGULAR)
			connectionType = "Regular";
		else if(type == LLIConnectionConstants.CONNECTION_TYPE_CACHE)
			connectionType = "Cache";
		else if(type == LLIConnectionConstants.CONNECTION_TYPE_REGULAR_LONG)
			connectionType = "Long Trem Regular";
		else if(type == LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY)
			connectionType = "Temporary";
		else
			connectionType = "";
	}
	
	void setConnectionStatus()
	{
		if(status == LLIConnectionConstants.STATUS_ACTIVE)
			connectionStatus = "Active";
		else if(status == LLIConnectionConstants.STATUS_TD)
			connectionStatus = "Temporary Disconnected";
		else if(status == LLIConnectionConstants.STATUS_CLOSED)
			connectionStatus = "Closed";
		else
			connectionStatus = "";
	}
	
	
	
	public List<Fee> getMonthlyBillFee()
	{
		this.monthlyBillFees = JsonUtils.getObjectListByJsonString(this.feesContent, Fee.class);
		return this.monthlyBillFees;
	}
	
	void setMonthlyBillFee(List<Fee> monthlyBillFees)
	{
		this.feesContent = JsonUtils.getJsonStringFromList(monthlyBillFees);
	}

	void setLocalLoopBreakDownsContent()
	{
		this.localLoopBreakDownsContent = JsonUtils.getJsonStringFromList(this.localLoopBreakDowns);
	}

	//use this after fetching data from db
	void setDataFromDBContent()
	{
		setConnectionStatus();
		setConnectionType();
		
		if(this.feesContent != null)
			this.monthlyBillFees = JsonUtils.getObjectListByJsonString(this.feesContent, Fee.class);
		
		if(this.localLoopBreakDownsContent != null)
			this.localLoopBreakDowns = JsonUtils.getObjectListByJsonString(this.localLoopBreakDownsContent, LLILocalLoopBreakDown.class);
	}
}
