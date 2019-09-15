package lli.outsourceBill;

import annotation.DAO;
import annotation.Transactional;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class LLIMonthlyOutsourceBillByConnectionService {

    static Logger logger = Logger.getLogger(LLIMonthlyOutsourceBillByConnectionService.class);

    @DAO
    LLIMonthlyOutsourceBillByConnectionDAO outsourceBillByConnectionDAO;

    @Transactional
    public void save(LLIMonthlyOutsourceBillByConnection object) {

        try {
            if(object.getId() == null || object.getId() == 0)
                outsourceBillByConnectionDAO.insertItem(object);
            else
                outsourceBillByConnectionDAO.updateItem(object);
        } catch (Exception e) {
            logger.error("error while saving ", e);
        }
    }

    @Transactional(transactionType=util.TransactionType.READONLY)
    public LLIMonthlyOutsourceBillByConnection getById(long id) {

        try {
            return  outsourceBillByConnectionDAO.getItem(id);

        } catch (Exception e) {
        }
        return null;
    }

    @Transactional(transactionType=util.TransactionType.READONLY)
    public List<LLIMonthlyOutsourceBillByConnection> getByOutsourceBillId(long outsourceBillId) {

        try {
            return outsourceBillByConnectionDAO.getByOutsourceBillId(outsourceBillId);

        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

}
