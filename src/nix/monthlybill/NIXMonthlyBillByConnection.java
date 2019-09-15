package nix.monthlybill;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import inventory.InventoryConstants;
import lombok.Data;
import nix.constants.NIXConstants;
import util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName("nix_monthly_bill_connection")
public class NIXMonthlyBillByConnection {

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

    @ColumnName("remark")
    String remark = "";


    @ColumnName("portCount")
    int portCount;
    @ColumnName("portType")
    long portType;
    @ColumnName("portRate")
    double portRate;
    @ColumnName("portCost")
    double portCost;


    @ColumnName("loopRate")
    double loopRate = 0.0;
    @ColumnName("loopCost")
    double loopCost = 0.0;


    @ColumnName("totalCost")
    double totalCost = 0.0;


    @ColumnName("createdDate")
    long createdDate;


    //before discount
    @ColumnName("grandCost")
    double grandCost = 0.0;    //new

    @ColumnName("discountRate")
    double discountRate = 0.0;    //new

    @ColumnName("discount")
    double discount = 0.0;    //new

    @ColumnName("vatRate")
    double vatRate = 0.0;

    @ColumnName("vat")
    double vat = 0.0;

    @ColumnName("localLoopBreakDownsContent")
    String localLoopBreakDownsContent;

    String connectionType;

    String connectionStatus;

    String portTypeName;

    void setPortTypeName() {
        portTypeName = InventoryConstants.mapOfPortTypeToPortTypeString.get((int)portType);
    }


    List<NIXLocalLoopBreakDown> localLoopBreakDowns = new ArrayList<>();

    double totalMonthlyFees;
    String concatenatedRemark;


    void setConcatenatedRemark() {

        this.concatenatedRemark = this.concatenatedRemark.isEmpty() ? "N/A" : this.concatenatedRemark;
    }


    void setConnectionType() {

        if (type == NIXConstants.CONNECTION_TYPE_REGULAR)
            connectionType = "Regular";
        else
            connectionType = "";
    }

    void setConnectionStatus() {
        if (status == NIXConstants.STATUS_ACTIVE)
            connectionStatus = "Active";
        else if (status == NIXConstants.STATUS_TD)
            connectionStatus = "Temporary Disconnected";
        else if (status == NIXConstants.STATUS_CLOSED)
            connectionStatus = "Closed";
        else
            connectionStatus = "";
    }


    void setLocalLoopBreakDownsContent() {
        this.localLoopBreakDownsContent = JsonUtils.getJsonStringFromList(this.localLoopBreakDowns);
    }

    //use this after fetching data from db
    void setDataFromDBContent() {
        setConnectionStatus();
        setConnectionType();
        setPortTypeName();

        if (this.localLoopBreakDownsContent != null)
            this.localLoopBreakDowns = JsonUtils.getObjectListByJsonString(this.localLoopBreakDownsContent, NIXLocalLoopBreakDown.class);
    }
}
