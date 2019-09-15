package vpn.demandNote;

import annotation.DAO;
import annotation.Transactional;
import lli.demandNote.adjustment.DNAdjustStatus;
import org.apache.log4j.Logger;

import java.util.List;

public class VPNDemandNoteAdjustmentService {

    Logger logger = Logger.getLogger(this.getClass());

    @DAO
    VPNDemandNoteAdjustmentDAO vpnDemandNoteAdjustmentDAO;

    @Transactional(transactionType=util.TransactionType.READONLY)
    public List<VPNDemandNoteAdjustment> getAllByStatus(DNAdjustStatus status) {

        try {
            return vpnDemandNoteAdjustmentDAO.getAllByStatus(status);

        } catch (Exception e) {
        }
        return null;
    }


    @Transactional
    public void save(VPNDemandNoteAdjustment object) {
        try {
            if(object.getId() == null || object.getId() == 0)
                vpnDemandNoteAdjustmentDAO.insertItem(object);
            else
                vpnDemandNoteAdjustmentDAO.updateItem(object);
        } catch (Exception e) {
            logger.error("error while saving ", e);
        }
        return;
    }

}
