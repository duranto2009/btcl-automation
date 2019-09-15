package nix.demandnote;

import annotation.*;
import api.FileAPI;
import common.ApplicationGroupType;
import common.ModuleConstants;
import common.bill.BillConstants;
import common.bill.BillDTO;
import common.pdf.PdfMaterial;
import common.pdf.PdfParameter;
import file.FileTypeConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import nix.accounts.NIXDemandNoteBusinessLogic;
import nix.application.NIXApplication;
import nix.application.NIXApplicationService;
import nix.constants.NIXConstants;
import nix.revise.NIXReviseDTO;
import nix.revise.NIXReviseService;
import util.ServiceDAOFactory;
import validator.annotation.NonNegative;

import java.util.HashMap;
import java.util.Map;
@EqualsAndHashCode(callSuper = false)
@Data
@AccountingLogic(NIXDemandNoteBusinessLogic.class)
@TableName("nix_demand_note")
@ForeignKeyName("lli_nc_dn_parent_bill_id")
public class NIXDemandNote extends BillDTO implements PdfMaterial {

    public NIXDemandNote() {
        setClassName(getClass().getCanonicalName());
        setBillType(BillConstants.DEMAND_NOTE);
        setEntityTypeID(NIXConstants.ENTITY_TYPE);
    }



    @ColumnName("id")
    @PrimaryKey
    long id;

    @ColumnName("security_money")
    @NonNegative("security money")
    double securityMoney;

    @ColumnName("registration_charge")
    @NonNegative("registration charge")
    double registrationFee;


    @ColumnName("port_charge")
    @NonNegative("port charge")
    double nixPortCharge;

    @ColumnName("port_upgrade_charge")
    @NonNegative("port upgrade charge")
    double nixPortUpgradeCharge;

    @ColumnName("port_downgrade_charge")
    @NonNegative("port downgrade charge")
    double nixPortDowngradeCharge;

    @ColumnName("instant_degradation_charge")
    @NonNegative("instant degradation charge")
    double instantDegradationCharge;

    @ColumnName("reconnect_charge")
    @NonNegative("reconnect charge")
    double reconnectCharge;

    @ColumnName("closing_charge")
    @NonNegative("closing charge")
    double closingCharge;

    @ColumnName("local_loop_charge")
    @NonNegative("Local Loop Charge")
    double localLoopCharge;

    @ColumnName("advance_adjustment")
    @NonNegative("Advance Adjustment")
    double advanceAdjustment;

    @ColumnName("application_group")
    ApplicationGroupType applicationGroupType;

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {
        Map<String, Object> params = new HashMap<>();
        PdfParameter.populateDemandNoteCommonInfo(params, this, ModuleConstants.Module_ID_NIX);
        if(applicationGroupType == ApplicationGroupType.NIX_CLIENT_APPLICATION){
            NIXReviseDTO reviseDTO = ServiceDAOFactory.getService(NIXReviseService.class).getNIXReviseClientApplicationByDemandNoteId(this.getID());
            PdfParameter.populateNIXDemandNoteInfo(params, this, reviseDTO);
        }else {
            NIXApplication nixApplication = ServiceDAOFactory.getService(NIXApplicationService.class).getNIXApplicationByDemandNoteId(this.getID());
            PdfParameter.populateNIXDemandNoteInfo(params, this, nixApplication);
        }
        return params;
    }

    @Override
    public String getResourceFile() {
        return BillConstants.NIX_DEMAND_NOTE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        String proposedFileName = "demand-note-" + super.getID() +".pdf";
        return FileAPI.getInstance().getFilePath(FileTypeConstants.NIX_BILL_DIRECTORY, proposedFileName, super.getGenerationTime());
    }
}