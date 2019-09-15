package vpn.monthlyUsage;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lli.monthlyBill.ConnectionBandwidthBreakDown;
import lombok.Data;
import util.JsonUtils;
import vpn.monthlyBill.VPNLocalLoopBreakDown;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName("vpn_monthly_usage_link")
public class VPNMonthlyUsageByLink {

    @PrimaryKey
    @ColumnName("id")
    Long id;

    @ColumnName("clientId")
    long clientId;

    @ColumnName("monthlyUsageByClientId")
    long monthlyUsageByClientId;

    @ColumnName("linkId")
    long linkId;

    @ColumnName("linkName")
    String linkName="";

    @ColumnName("linkType")
    int linkType;

    @ColumnName("link_distance")
    long linkDistance;

    @ColumnName("linkAddress")
    String address = "";

    @ColumnName("mbpsCost")
    double mbpsCost = 0.0;

    @ColumnName("localEndLoopCost")
    double localEndLoopCost = 0.0;

    @ColumnName("remoteEndLoopCost")
    double remoteEndLoopCost = 0.0;

    @ColumnName("totalLoopCost")
    double totalLoopCost = 0.0;

    @ColumnName("createdDate")
    long createdDate;

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

    @ColumnName("totalCost")
    double totalCost = 0.0;

    @ColumnName("localLoopBreakDownsContentLocalEnd")
    String localLoopBreakDownsContentLocalEnd;

    @ColumnName("localLoopBreakDownsContentRemoteEnd")
    String localLoopBreakDownsContentRemoteEnd;

    @ColumnName("linkBandwidthBreakDownsContent")
    String linkBandwidthBreakDownsContent;

    @ColumnName("remark")
    String remark = "";

    List<VPNLocalLoopBreakDown> localLoopBreakDowns =new ArrayList<>();

    List<VPNLocalLoopBreakDown> localLoopBreakDownsLocalEnd = new ArrayList<>();
    List<VPNLocalLoopBreakDown> localLoopBreakDownsRemoteEnd = new ArrayList<>();

    List<ConnectionBandwidthBreakDown> linkBandwidthBreakDowns = new ArrayList<>();


    void setConnectionBandwidthBreakDownsContent() {
        this.linkBandwidthBreakDownsContent = JsonUtils.getJsonStringFromList(this.linkBandwidthBreakDowns);
    }

    void setLocalLoopBreakDownsContent() {
        this.localLoopBreakDownsContentLocalEnd = JsonUtils.getJsonStringFromList(this.localLoopBreakDownsLocalEnd);
        this.localLoopBreakDownsContentRemoteEnd = JsonUtils.getJsonStringFromList(this.localLoopBreakDownsRemoteEnd);
    }

    void setDataFromDBContent() {
        if (this.localLoopBreakDownsContentLocalEnd != null)
            this.localLoopBreakDownsLocalEnd = JsonUtils.getObjectListByJsonString(this.localLoopBreakDownsContentLocalEnd, VPNLocalLoopBreakDown.class);
        if (this.localLoopBreakDownsContentRemoteEnd != null)
            this.localLoopBreakDownsRemoteEnd = JsonUtils.getObjectListByJsonString(this.localLoopBreakDownsContentRemoteEnd, VPNLocalLoopBreakDown.class);
        if (this.linkBandwidthBreakDownsContent != null)
            this.linkBandwidthBreakDowns = JsonUtils.getObjectListByJsonString(this.linkBandwidthBreakDownsContent, ConnectionBandwidthBreakDown.class);
    }
}
