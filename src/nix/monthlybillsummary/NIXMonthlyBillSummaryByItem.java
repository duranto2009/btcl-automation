package nix.monthlybillsummary;
import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import util.DateUtils;

@Data
@TableName("nix_monthly_bill_summary_item")
@EqualsAndHashCode(callSuper = false)
public class NIXMonthlyBillSummaryByItem {


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

    public void setType() {
        switch (type) {
            case NIXMonthlyBillSummaryType.REGULAR:
                billType = "Regular Charge";
                break;


            case NIXMonthlyBillSummaryType.LOCAL_LOOP:
                billType = "Local Loop Charge";
                break;

            case NIXMonthlyBillSummaryType.PORT:
                billType = "Port Charge";
                break;



            case NIXMonthlyBillSummaryType.LOCAL_LOOP_ADJUSTMENT:
                billType = "Adjustment of Local Loop Usage";
                break;
            case NIXMonthlyBillSummaryType.PORT_ADJUSTMENT:
                billType = "Adjustment of Port Usage";
                break;

            case NIXMonthlyBillSummaryType.DEMAND_NOTE_ADJUSTMENT:
                billType = "Demand Note Charge";
                break;

            default:
                billType = "";
                break;

        }

    }


    //use this after fetching data from db
    void setDataFromDBContent() {
        setType();
    }


    public NIXMonthlyBillSummaryByItem() {
        super();
        this.createdDate = DateUtils.getCurrentTime();
    }
}
