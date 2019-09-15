package lli.demandNote;

import annotation.*;
import common.bill.BillConstants;
import common.bill.BillDTO;
import lli.Application.ReviseClient.ReviseDTO;
import lli.connection.LLIConnectionConstants;
import common.pdf.PdfMaterial;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import util.ServiceDAOFactory;
import validator.annotation.NonNegative;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AccountingLogic(LLIBreakLongTermDemandNoteBusinessLogic.class)
@TableName("at_lli_break_lt_dn")
@ForeignKeyName("lli_break_lt_dn_parent_bill_id")
public class LLIBreakLongTermDemandNote extends BillDTO implements PdfMaterial {

    public LLIBreakLongTermDemandNote() {
        setClassName(this.getClass().getCanonicalName());
        setBillType(BillConstants.DEMAND_NOTE);
        setEntityTypeID(LLIConnectionConstants.ENTITY_TYPE);
    }

    @PrimaryKey
    @ColumnName("lli_break_lt_dn_id")
    long shortBillID;

    @ColumnName("lli_break_lt_dn_fine")
    @NonNegative("contract breaking fine")
    double contractBreakingFine;

    @ColumnName("lli_break_lt_dn_other")
    @NonNegative("other cost")
    double otherCost;

    ReviseDTO reviseDTO;

    public long getShortBillID() {
        return shortBillID;
    }

    public void setShortBillID(long shortBillID) {
        this.shortBillID = shortBillID;
    }

    public double getContractBreakingFine() {
        return contractBreakingFine;
    }

    public void setContractBreakingFine(double contractBreakingFine) {
        this.contractBreakingFine = contractBreakingFine;
    }

    public double getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(double otherCost) {
        this.otherCost = otherCost;
    }

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {

        Map<String, Object> params = new HashMap<>();

        LLIDemandNoteService lliDemandNoteService = ServiceDAOFactory.getService(LLIDemandNoteService.class);

        lliDemandNoteService.populateFooterInfoForPDF(params);
        lliDemandNoteService.populateClientInfoForPDF(params, super.getClientID());
        lliDemandNoteService.populateBillInfoForPDF(params, this);

        params.put("contractBreakingFine", String.format("%.2f", this.getContractBreakingFine()));
        params.put("otherCost", String.format("%.2f", this.getOtherCost()));

        lliDemandNoteService.populateLLIApplicationInfoForPDF(params, reviseDTO, getID());
        return params;
    }

    @Override
    public String getResourceFile() {
        return BillConstants.LLI_DEMAND_NOTE_BREAK_LONG_TERM_TEMPLATE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JRBeanCollectionDataSource(Arrays.asList(this), false);
    }

    @Override
    public String getOutputFilePath() throws Exception {
        String proposedFileName = "demand-note-" + super.getID() + ".pdf";
        return ServiceDAOFactory.getService(LLIDemandNoteService.class).getFilePath(proposedFileName, super.getGenerationTime());
    }

    public ReviseDTO getReviseDTO() {
        return reviseDTO;
    }

    public void setReviseDTO(ReviseDTO reviseDTO) {
        this.reviseDTO = reviseDTO;
    }
}
