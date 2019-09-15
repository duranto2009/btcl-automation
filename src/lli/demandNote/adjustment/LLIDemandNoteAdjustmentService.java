package lli.demandNote.adjustment;

import java.util.List;

import org.apache.log4j.Logger;

import annotation.DAO;
import annotation.Transactional;


public class LLIDemandNoteAdjustmentService {

	Logger logger = Logger.getLogger(this.getClass());
	
	@DAO
	LLIDemandNoteAdjustmentDao lliDemandNoteAdjustmentDao;
	
	
	@Transactional
	public void save(LLIDemandNoteAdjustment object) {

		try {
			if(object.getId() == null || object.getId() == 0)
				lliDemandNoteAdjustmentDao.insertItem(object);
			else
				lliDemandNoteAdjustmentDao.updateItem(object);
		} catch (Exception e) {
			logger.error("error while saving ", e);
		}
		return;
	}
	
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public LLIDemandNoteAdjustment getById(long id) {

		try {
			LLIDemandNoteAdjustment lliDemandNoteAdjustment =  lliDemandNoteAdjustmentDao.getItem(id);
			
			return lliDemandNoteAdjustment;
		} catch (Exception e) {
		}
		return null;
	}
	
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<LLIDemandNoteAdjustment> getByClientIdAndStatus(long clientId, DNAdjustStatus status) {
		
		try {
			return lliDemandNoteAdjustmentDao.getByClientIdAndStatus(clientId, status);
			
		} catch (Exception e) {
		}
		return null;
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<LLIDemandNoteAdjustment> getAllByStatus(DNAdjustStatus status) {
		
		try {
			return lliDemandNoteAdjustmentDao.getAllByStatus(status);
			
		} catch (Exception e) {
		}
		return null;
	}

    @Transactional
    public void setStatusById(long id, DNAdjustStatus status) {

        try {
            LLIDemandNoteAdjustment adjustment = getById(id);
            if(adjustment == null)
                return;

            adjustment.setStatus(status);
            save(adjustment);

        } catch (Exception e) {
        }
    }

    @Transactional
    public void setStatusByBillId(long billId, DNAdjustStatus status) {

        try {
            LLIDemandNoteAdjustment adjustment = lliDemandNoteAdjustmentDao.getByBillId(billId);
            if(adjustment == null)
                return;

            adjustment.setStatus(status);
            save(adjustment);

        } catch (Exception e) {
        }
    }
	
}
