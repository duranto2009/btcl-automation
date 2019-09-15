package coLocation.demandNote;

import annotation.*;
import api.FileAPI;
import coLocation.CoLocationConstants;
import coLocation.accounts.CoLocationDemandNoteBusinessLogic;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.application.CoLocationApplicationService;
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
import util.ServiceDAOFactory;
import validator.annotation.NonNegative;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@Data
@ForeignKeyName("parent_bill_id")
@TableName("colocation_dn")
@AccountingLogic(CoLocationDemandNoteBusinessLogic.class)
public class CoLocationDemandNote extends BillDTO implements PdfMaterial {

    public CoLocationDemandNote() {
        setClassName(this.getClass().getCanonicalName());
        setBillType(BillConstants.DEMAND_NOTE);
        setEntityTypeID(CoLocationConstants.ENTITY_TYPE);
    }

    @ColumnName("id")
    @PrimaryKey
    long demandNoteID;

    @ColumnName("rack_cost") @NonNegative
    double rackCost;

    @ColumnName("ofc_cost") @NonNegative
    double ofcCost;

    @ColumnName("power_cost") @NonNegative
    double powerCost;

    @ColumnName("floor_space_cost") @NonNegative
    double floorSpaceCost;

    @ColumnName("advance_adjustment") @NonNegative
    double advanceAdjustment;

    @ColumnName("upgrade_cost") @NonNegative
    double upgradeCost;

    @ColumnName("downgrade_cost") @NonNegative
    double downgradeCost;

    @ColumnName("reconnect_cost") @NonNegative
    double reconnectCost;

    @ColumnName("closing_cost") @NonNegative
    double closingCost;

    @ColumnName("is_yearly_demand_note")
    boolean isYearlyDemandNote;

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {
        Map<String, Object> params = new HashMap<>();

        CoLocationApplicationDTO colocationApplication =
                ServiceDAOFactory.getService(CoLocationApplicationService.class).getColocationApplicationByDemandNoteId(this.getID());

        PdfParameter.populateDemandNoteCommonInfo(params, this, ModuleConstants.Module_ID_COLOCATION);
        PdfParameter.populateCoLocationDemandNoteInfo(params, this, colocationApplication);


        return params;
    }

    @Override
    public String getResourceFile() throws Exception {
        return BillConstants.COLOCATION_DEMAND_NOTE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        String proposedFileName = "demand-note-" + super.getID() +".pdf";
        String folderName = FileTypeConstants.COLOCATION_BILL_DIRECTORY;
        return FileAPI.getInstance().getFilePath(folderName, proposedFileName, this.getGenerationTime());
    }
}