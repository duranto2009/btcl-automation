package coLocation.demandNote;

import annotation.*;
import api.FileAPI;
import coLocation.CoLocationConstants;
import coLocation.accounts.CoLocationYearlyDemandNoteBusinessLogic;
import coLocation.connection.CoLocationConnectionDTO;
import coLocation.connection.CoLocationConnectionService;
import common.ModuleConstants;
import common.bill.BillConstants;
import common.pdf.PdfMaterial;
import common.pdf.PdfParameter;
import file.FileTypeConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import util.ServiceDAOFactory;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@Data
@ForeignKeyName("parent_demand_note_id")
@TableName("colocation_dn_yearly")
@AccountingLogic(CoLocationYearlyDemandNoteBusinessLogic.class)
public class CoLocationYearlyDemandNote extends CoLocationDemandNote implements PdfMaterial {

    public CoLocationYearlyDemandNote(){
        setClassName(CoLocationDemandNote.class.getCanonicalName());
        setBillType(BillConstants.YEARLY_BILL);
        setEntityTypeID(CoLocationConstants.ENTITY_TYPE);
    }
    @PrimaryKey
    @ColumnName("id")
    long id;

    @ColumnName("connection_id")
    long connectionId;

    @ColumnName("yearly_adjustment")
    double yearlyAdjustment;

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {

        Map<String, Object> params = new HashMap<>();
//        CoLocation

        CoLocationConnectionDTO connection = ServiceDAOFactory.getService(CoLocationConnectionService.class).getColocationConnection(this.getConnectionId());
        PdfParameter.populateDemandNoteCommonInfo(params, this, ModuleConstants.Module_ID_COLOCATION);
        PdfParameter.populateCoLocationYearlyDemandNoteInfo(params, this, connection);

        params.put("connectionName", String.valueOf(connection.getName()));
        return params;

    }

    @Override
    public String getResourceFile()  {
        return BillConstants.COLOCATION_DEMAND_NOTE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        String proposedFileName = "yearly-demand-note-" + super.getID() + "_" +this.getClientID() + "_" + this.getYear() + ".pdf";
        String folderName = FileTypeConstants.COLOCATION_BILL_DIRECTORY;
        return FileAPI.getInstance().getFilePath(folderName, proposedFileName, this.getGenerationTime());
    }

}
