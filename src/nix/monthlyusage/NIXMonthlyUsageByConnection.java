package nix.monthlyusage;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

import lombok.Data;
import nix.monthlybill.NIXLocalLoopBreakDown;
import nix.monthlybill.NIXPortBreakDown;
import util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName("nix_monthly_usage_connection")
public class NIXMonthlyUsageByConnection {
	
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
	
	@ColumnName("portCost")
	double portCost = 0.0;
	
	@ColumnName("loopCost")
	double loopCost = 0.0;
	
	@ColumnName("totalCost")
	double totalCost = 0.0;
	
	@ColumnName("localLoopBreakDownsContent")
	String localLoopBreakDownsContent;

	@ColumnName("portBreakDownsContent")
	String portBreakDownsContent;

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
	
	List<NIXLocalLoopBreakDown> localLoopBreakDowns = new ArrayList<>();
	List<NIXPortBreakDown> nixPortBreakDowns = new ArrayList<>();
//	List<ConnectionBandwidthBreakDown> connectionBandwidthBreakDowns = new ArrayList<>();
	
//	List<Fee> fees = new ArrayList<>();
	
	
	
	void setConnectionType()
	{

			connectionType = "Regular";
	}
	
	void setLocalLoopBreakDownsContent()
	{
		this.localLoopBreakDownsContent = JsonUtils.getJsonStringFromList(this.localLoopBreakDowns);
	}

//	void setConnectionBandwidthBreakDownsContent()
//	{
//		this.connectionBandwidthBreakDownsContent = JsonUtils.getJsonStringFromList(this.connectionBandwidthBreakDowns);
//	}
	
//	void setFeesContent()
//	{
//		this.feesContent = JsonUtils.getJsonStringFromList(this.fees);
//	}

	void setDataFromDBContent()
	{
//		if(this.feesContent != null)
//			this.fees = JsonUtils.getObjectListByJsonString(this.feesContent, Fee.class);
		
//		if(this.localLoopBreakDownsContent != null)
//			this.localLoopBreakDowns = JsonUtils.getObjectListByJsonString(this.localLoopBreakDownsContent, LLILocalLoopBreakDown.class);
//
//		if(this.connectionBandwidthBreakDownsContent != null)
//			this.connectionBandwidthBreakDowns = JsonUtils.getObjectListByJsonString(this.connectionBandwidthBreakDownsContent, ConnectionBandwidthBreakDown.class);
	}

}
