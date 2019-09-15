package vpn.demandNote;

import annotation.*;
import api.FileAPI;
import common.ApplicationGroupType;
import common.ModuleConstants;
import common.bill.BillConstants;
import common.bill.BillDTO;
import common.pdf.PdfMaterial;
import common.pdf.PdfParameter;
import entity.annotations.ExcludeFromVATCalculation;
import file.FileTypeConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import requestMapping.Service;
import util.EnglishNumberToWords;
import util.ServiceDAOFactory;
import validator.annotation.NonNegative;
import vpn.VPNConstants;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationService;

import java.time.LocalDate;
import java.util.*;
import java.util.Map;

@TableName("vpn_dn")
@Data
@EqualsAndHashCode(callSuper = false)
@ForeignKeyName("parent_bill_id")
@AccountingLogic(VPNDemandNoteBusinessLogic.class)
public class VPNDemandNote extends BillDTO implements PdfMaterial {


    public VPNDemandNote() {
        setClassName(getClass().getCanonicalName());
        setBillType(BillConstants.DEMAND_NOTE);
        setEntityTypeID(VPNConstants.ENTITY_TYPE);
    }

    @PrimaryKey
    @ColumnName("id") long id;
    @ColumnName("security_charge") @NonNegative  @ExcludeFromVATCalculation double securityCharge;
    @ColumnName("registration_charge") @NonNegative double registrationCharge;
    @ColumnName("otc_local_loop_BTCL") @NonNegative double otcLocalLoopBTCL;
    @ColumnName("bandwidth_charge") @NonNegative double bandwidthCharge;
    @ColumnName("local_loop_charge") @NonNegative double localLoopCharge;
    @ColumnName("degradation_charge") @NonNegative double degradationCharge;
    @ColumnName("reconnect_charge") @NonNegative double reconnectCharge;
    @ColumnName("closing_charge") @NonNegative double closingCharge;
    @ColumnName("shifting_charge") @NonNegative double shiftingCharge;
    @ColumnName("ownership_change_charge") @NonNegative double ownershipChangeCharge;
    @ColumnName("other_charge") @NonNegative double otherCharge;
    @ColumnName("advance") @NonNegative double advance;
    @ColumnName("application_group") ApplicationGroupType applicationGroupType;

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {

        Map<String, Object> params = new HashMap<>();
        PdfParameter.populateDemandNoteCommonInfo(params, this, ModuleConstants.Module_ID_VPN);

        VPNApplication vpnApplication = ServiceDAOFactory.getService(VPNApplicationService.class).getVPNApplicationByDemandNoteId(getID());
        PdfParameter.populateVPNLinkDemandNoteInfo(params, this, vpnApplication);

        return params;
    }

    @Override
    public String getResourceFile() {
        return BillConstants.VPN_DEMAND_NOTE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        String proposedFileName = "demand-note-" + super.getID() +".pdf";
        return FileAPI.getInstance().getFilePath(FileTypeConstants.VPN_BILL_DIRECTORY, proposedFileName, super.getGenerationTime());
    }

}
