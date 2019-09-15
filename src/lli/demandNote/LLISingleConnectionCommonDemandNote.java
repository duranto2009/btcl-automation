
package lli.demandNote;

import annotation.*;
import common.RequestFailureException;
import common.ServiceDAO;
import common.bill.BillConstants;
import common.bill.BillDTO;
import config.GlobalConfigConstants;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.connection.LLIConnectionConstants;
import common.pdf.PdfMaterial;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import util.*;
import validator.annotation.NonNegative;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AccountingLogic(LLISingleConnectionDemandNoteBusinessLogic.class)
@ForeignKeyName("lli_common_dn_parent_bill_id")
@TableName("at_lli_common_dn")
public class LLISingleConnectionCommonDemandNote extends BillDTO implements PdfMaterial {

    public LLISingleConnectionCommonDemandNote() {
        setClassName(this.getClass().getCanonicalName());
        setBillType(BillConstants.DEMAND_NOTE);
        setEntityTypeID(LLIConnectionConstants.ENTITY_TYPE);
    }

    @PrimaryKey
    @ColumnName("lli_common_dn_id")
    long commonDemandNoteID;
    @ColumnName("lli_common_dn_sc_money")
    @NonNegative("security money")
    double securityMoney;
    @ColumnName("lli_common_dn_bw_charge")
    @NonNegative("bandwidth charge")
    double bandwidthCharge;
    @ColumnName("lli_common_dn_advance_amount")
    @NonNegative("advanced amount")
    double advancedAmount;
    @ColumnName("lli_common_dn_downgrade_charge")
    @NonNegative("downgrade charge")
    double downgradeCharge;
    @ColumnName("lli_common_dn_port_charge")
    @NonNegative("port charge")
    double portCharge;
    @ColumnName("lli_common_dn_fibre_otc")
    @NonNegative("fiber charge")
    double fibreOTC;
    @ColumnName("lli_common_dn_core_charge")
    @NonNegative("local loop charge")
    double coreCharge;
    @ColumnName("lli_common_dn_first_x_ip_cost")
    @NonNegative("first X IP charge")
    double firstXIpCost;
    @ColumnName("lli_common_dn_next_y_ip_cost")
    @NonNegative("next Y IP charge")
    double nextYIpCost;
    @ColumnName("lli_common_dn_shift_charge")
    @NonNegative("shifting charge")
    double shiftCharge;

    @ColumnName("ip_yearly_bill_base_bill_id")
    @Getter @Setter  Long baseBillId;

    @ColumnName("lli_common_dn_item_cost_content")
    String itemCostContent;
    List<ItemCost> itemCosts;

    public String getItemCostContent() {
        return itemCostContent;
    }

    public void setItemCostContent(String itemCostContent) {
        this.itemCostContent = itemCostContent;
    }

    public long getCommonDemandNoteID() {
        return commonDemandNoteID;
    }

    public void setCommonDemandNoteID(long commonDemandNoteID) {
        this.commonDemandNoteID = commonDemandNoteID;
    }

    public double getSecurityMoney() {
        return securityMoney;
    }

    public void setSecurityMoney(double securityMoney) {
        this.securityMoney = securityMoney;
    }

    public double getBandwidthCharge() {
        return bandwidthCharge;
    }

    public void setBandwidthCharge(double bandwidthCharge) {
        this.bandwidthCharge = bandwidthCharge;
    }

    public double getAdvancedAmount() {
        return advancedAmount;
    }

    public void setAdvancedAmount(double advancedAmount) {
        this.advancedAmount = advancedAmount;
    }

    public double getDowngradeCharge() {
        return downgradeCharge;
    }

    public void setDowngradeCharge(double downgradeCharge) {
        this.downgradeCharge = downgradeCharge;
    }

    public double getPortCharge() {
        return portCharge;
    }

    public void setPortCharge(double portCharge) {
        this.portCharge = portCharge;
    }

    public double getFibreOTC() {
        return fibreOTC;
    }

    public void setFibreOTC(double fibreOTC) {
        this.fibreOTC = fibreOTC;
    }

    public double getCoreCharge() {
        return coreCharge;
    }

    public void setCoreCharge(double coreCharge) {
        this.coreCharge = coreCharge;
    }

    public double getFirstXIpCost() {
        return firstXIpCost;
    }

    public void setFirstXIpCost(double firstXIpCost) {
        this.firstXIpCost = firstXIpCost;
    }

    public double getNextYIpCost() {
        return nextYIpCost;
    }

    public void setNextYIpCost(double nextYIpCost) {
        this.nextYIpCost = nextYIpCost;
    }

    public double getShiftCharge() {
        return shiftCharge;
    }

    public void setShiftCharge(double shiftCharge) {
        this.shiftCharge = shiftCharge;
    }

    public double getTotalItemCost() {
        double totalItemCost = 0;
        for (ItemCost itemCost : getItemCosts()) {
            totalItemCost += itemCost.cost;
        }
        return totalItemCost;
    }

    public List<ItemCost> getItemCosts() {
        if (itemCosts == null) {
            itemCosts = JsonUtils.getObjectListByJsonString(this.itemCostContent, ItemCost.class);
        }

        return itemCosts;
    }

    public void setItemCosts(List<ItemCost> itemCosts) {
        this.itemCosts = itemCosts;
    }

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {
        Map<String, Object> params = new HashMap<>();
        LLIDemandNoteService lliDemandNoteService =ServiceDAOFactory.getService(LLIDemandNoteService.class);
        LLIApplication lliApplication;
        if(this.baseBillId == null) {

            lliApplication = ServiceDAOFactory.getService(LLIApplicationService.class).getNewFlowLLIApplicationByDemandNoteID(this.getID());

        }else {
            lliApplication = ServiceDAOFactory.getService(LLIApplicationService.class).getNewFlowLLIApplicationByDemandNoteID(this.getBaseBillId());
        }
        if(lliApplication == null) {
            throw new RequestFailureException("No LLI Application found with demand note id " + this.getID());
        }

        lliDemandNoteService.populateFooterInfoForPDF(params);
        lliDemandNoteService.populateClientInfoForPDF(params, super.getClientID());
        lliDemandNoteService.populateBillInfoForPDF(params, this);


        params.put("securityMoney", String.format("%.2f", this.getSecurityMoney()));
        params.put("registrationCharge", "0.00");
        params.put("bwMRC", String.format("%.2f", this.getBandwidthCharge()));

        params.put("instantDegradationCharge", "0.00");

        String otherItems = "";
        String otherItemsCharge = "";
        List<ItemCost> itemCosts = this.getItemCosts();
        for (ItemCost itemCost : itemCosts) {
            otherItems += itemCost.item + "(other)<br>";
            otherItemsCharge += String.format("%.2f", itemCost.cost) + "<br>";
        }
        params.put("otherItems", otherItems);
        params.put("otherCharge", otherItemsCharge);

        params.put("advancedAmount", String.format("%.2f", this.getAdvancedAmount()));
        params.put("localLoopCharge", String.format("%.2f", this.getCoreCharge()));
        params.put("fibreCharge", String.format("%.2f", this.getFibreOTC()));
        params.put("firstXIpCharge", String.format("%.2f", this.getFirstXIpCost()));
        params.put("nextYIpCharge", String.format("%.2f", this.getNextYIpCost()));
        params.put("shiftCharge", String.format("%.2f", this.getShiftCharge()));
        params.put("portCharge", String.format("%.2f", this.getPortCharge()));
        params.put("downgradeCharge", String.format("%.2f", this.getDowngradeCharge()));


        params.put("bwMRCMinusDiscount", String.format("%.2f", this.getBandwidthCharge() - this.getDiscount()));
        double subTotal = this.getTotalPayable();
        params.put("vatCalculableWOSecurity", String.format("%.2f",
                subTotal - this.getSecurityMoney()
        ));


        params.put("subTotal", String.format("%.2f", subTotal));
        params.put("vatPercentage", String.format("%.2f", this.getVatPercentage()));
        params.put("discountPercentage", String.format("%.2f", this.getDiscountPercentage()));


        String context = GlobalConfigConstants.contextMap.get("context");
        if (context == null) {
            throw new RequestFailureException("Context Path Not Set");
        }
        params.put("actualLink", context + "lli/dn/get-local-loop-breakdown.do?appId="+ lliApplication.getApplicationID() );
        params.put("linkLocalLoop", "<span style='color:blue'>" + context + "lli/dn/get-local-loop-breakdown.do?appId="+ lliApplication.getApplicationID()+ "</span>");


        lliDemandNoteService.populateLLIApplicationInfoForPDF(params, lliApplication, getID());

        return params;
    }

    @Override
    public String getResourceFile() {
        return BillConstants.LLI_DEMAND_NOTE_REVISE_CONNECTION_TEMPLATE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JRBeanCollectionDataSource(Arrays.asList(this), false);
    }

    @Override
    public String getOutputFilePath() throws Exception {
        String proposedFileName = "demand-note-" + super.getID() +".pdf";
        return ServiceDAOFactory.getService(LLIDemandNoteService.class).getFilePath(proposedFileName, super.getGenerationTime());
    }




}
