package vpn.demandNote;

import lli.demandNote.adjustment.DNAdjustStatus;
import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class VPNDemandNoteAdjustmentDAO {



    public List<VPNDemandNoteAdjustment> getAllByStatus(DNAdjustStatus status) throws Exception{

        List<VPNDemandNoteAdjustment> list = getAllObjectList(VPNDemandNoteAdjustment.class, new VPNDemandNoteAdjustmentConditionBuilder()
                .Where()
                .statusLike(status.name())
                .getCondition());
        return list;
    }


    public void insertItem(VPNDemandNoteAdjustment object) throws Exception{
        ModifiedSqlGenerator.insert(object);
    }

    public void updateItem(VPNDemandNoteAdjustment object) throws Exception{
        ModifiedSqlGenerator.updateEntity(object);
    }


}
