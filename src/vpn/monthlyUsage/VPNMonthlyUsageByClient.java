package vpn.monthlyUsage;

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
@TableName("vpn_monthly_usage_client")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VPNMonthlyUsageByClient {

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

    @ColumnName(value = "month", editable = false)
    int month = 0;
    @ColumnName("year")
    int year = 0;
    @ColumnName("isDeleted")
    boolean isDeleted;

    @ColumnName("grandTotal")
    double grandTotal = 0.0;    //after discount

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

    @ColumnName(value = "description", editable = false)
    String description = "";


    List<VPNMonthlyUsageByLink> monthlyUsageByLinks = new ArrayList<>();


    List<MbpsBreakDown> totalMbpsBreakDowns;
    List<BillingRangeBreakDown> billingRangeBreakDowns;


    void setMbpsBreakDownsContent() {
        this.mbpsBreakDownContent = JsonUtils.getJsonStringFromList(this.totalMbpsBreakDowns);
    }

    void setBillingRangeBreakDownsContent() {
        this.billingRangeBreakDownContent = JsonUtils.getJsonStringFromList(this.billingRangeBreakDowns);
    }


    public List<MbpsBreakDown> getTotalMbpsBreakDown() {
        this.totalMbpsBreakDowns = JsonUtils.getObjectListByJsonString(this.mbpsBreakDownContent, MbpsBreakDown.class);
        return this.totalMbpsBreakDowns;
    }

    public void setTotalMbpsBreakDown(List<MbpsBreakDown> mbpsBreakDowns) {
        this.totalMbpsBreakDowns = mbpsBreakDowns;
        this.mbpsBreakDownContent = JsonUtils.getJsonStringFromObject(mbpsBreakDowns);
    }

    public List<BillingRangeBreakDown> getBillingRangeBreakDown() {
        this.billingRangeBreakDowns = JsonUtils.getObjectListByJsonString(this.billingRangeBreakDownContent, BillingRangeBreakDown.class);
        return this.billingRangeBreakDowns;
    }

    public void setBillingRangeBreakDown(List<BillingRangeBreakDown> billingRangeBreakDowns) {
        this.billingRangeBreakDowns = billingRangeBreakDowns;
        this.billingRangeBreakDownContent = JsonUtils.getJsonStringFromObject(billingRangeBreakDowns);
    }


    void setDataFromDBContent() {
//        if (this.mbpsBreakDownContent != null)
//            this.totalMbpsBreakDowns = JsonUtils.getObjectListByJsonString(this.mbpsBreakDownContent, MbpsBreakDown.class);
//
//        if (this.billingRangeBreakDownContent != null)
//            this.billingRangeBreakDowns = JsonUtils.getObjectListByJsonString(this.billingRangeBreakDownContent, BillingRangeBreakDown.class);

    }

}
