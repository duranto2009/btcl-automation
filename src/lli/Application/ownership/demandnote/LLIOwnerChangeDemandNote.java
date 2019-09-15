package lli.Application.ownership.demandnote;


import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import common.bill.BillConstants;
import common.bill.BillDTO;
import common.pdf.PdfMaterial;
import lli.Application.ownership.LLIOwnerChangeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sf.jasperreports.engine.JRDataSource;

import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@Data
@ForeignKeyName("parent_bill_id")
@TableName("lli_owner_change_dn")
@Deprecated
public class LLIOwnerChangeDemandNote extends BillDTO implements PdfMaterial {

    public LLIOwnerChangeDemandNote() {
        setClassName(this.getClass().getCanonicalName());
        setBillType(BillConstants.DEMAND_NOTE);
        setEntityTypeID(LLIOwnerChangeConstant.ENTITY_TYPE);
    }

    @ColumnName("id")
    @PrimaryKey
    long demandNoteID;

    @ColumnName("transfer_charge")
    double transferCharge;

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {
        return null;
    }

    @Override
    public String getResourceFile() throws Exception {
        return null;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return null;
    }

    @Override
    public String getOutputFilePath() throws Exception {
        return null;
    }
}
