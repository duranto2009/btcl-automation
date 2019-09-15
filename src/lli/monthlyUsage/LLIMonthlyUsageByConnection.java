package lli.monthlyUsage;

import java.util.ArrayList;
import java.util.List;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lli.connection.LLIConnectionConstants;
import lli.monthlyBill.ConnectionBandwidthBreakDown;
import lli.monthlyBill.Fee;
import lli.monthlyBill.LLILocalLoopBreakDown;
import lombok.Data;
import util.JsonUtils;

@Data
@TableName("at_lli_monthly_usage_connection")
public class LLIMonthlyUsageByConnection {
	
	@PrimaryKey
	@ColumnName("id")
	Long id;

	@ColumnName("monthlyUsageByClientId")
	long monthlyUsageByClientId;
	
	@ColumnName("clientId")
	long clientId;
	
	@ColumnName("connectionId")
	long connectionId;
	
	@ColumnName("type")
	int type = 0;
	
	@ColumnName("name")
	String name = "";
	
	@ColumnName("address")
	String address = "";
	
	@ColumnName("createdDate")
	long createdDate;
	
	@ColumnName("remark")
	String remark = "";
	
	@ColumnName("mbpsCost")
	double mbpsCost = 0.0;
	
	@ColumnName("loopCost")
	double loopCost = 0.0;
	
	@ColumnName("totalCost")
	double totalCost = 0.0;
	
	@ColumnName("localLoopBreakDownsContent")
	String localLoopBreakDownsContent;
	
	@ColumnName("connectionBandwidthBreakDownsContent")
	String connectionBandwidthBreakDownsContent;
	
	@ColumnName("feesContent")
	String feesContent;
	
	//before discount
	@ColumnName("grandCost")
	double grandCost = 0.0;
	
	@ColumnName("discountRate")
	double discountRate = 0.0;
	
	@ColumnName("discount")
	double discount = 0.0;
	
	@ColumnName("vatRate")
	double vatRate = 0.0;
	
	@ColumnName("vat")
	double vat = 0.0;
	
	String connectionType;
	
	List<LLILocalLoopBreakDown> localLoopBreakDowns = new ArrayList<>();
	List<ConnectionBandwidthBreakDown> connectionBandwidthBreakDowns = new ArrayList<>();
	
	List<Fee> fees = new ArrayList<>();
	
	
	
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
	
	void setLocalLoopBreakDownsContent()
	{
		this.localLoopBreakDownsContent = JsonUtils.getJsonStringFromList(this.localLoopBreakDowns);
	}

	void setConnectionBandwidthBreakDownsContent()
	{
		this.connectionBandwidthBreakDownsContent = JsonUtils.getJsonStringFromList(this.connectionBandwidthBreakDowns);
	}
	
	void setFeesContent()
	{
		this.feesContent = JsonUtils.getJsonStringFromList(this.fees);
	}

	void setDataFromDBContent()
	{
		if(this.feesContent != null)
			this.fees = JsonUtils.getObjectListByJsonString(this.feesContent, Fee.class);
		
		if(this.localLoopBreakDownsContent != null)
			this.localLoopBreakDowns = JsonUtils.getObjectListByJsonString(this.localLoopBreakDownsContent, LLILocalLoopBreakDown.class);
		
		if(this.connectionBandwidthBreakDownsContent != null)
			this.connectionBandwidthBreakDowns = JsonUtils.getObjectListByJsonString(this.connectionBandwidthBreakDownsContent, ConnectionBandwidthBreakDown.class);
	}

}
