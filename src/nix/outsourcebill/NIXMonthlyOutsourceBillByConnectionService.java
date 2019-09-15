package nix.outsourcebill;

import annotation.DAO;
import annotation.Transactional;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class NIXMonthlyOutsourceBillByConnectionService {

    static Logger logger = Logger.getLogger(NIXMonthlyOutsourceBillByConnectionService.class);

    @DAO
    NIXMonthlyOutsourceBillByConnectionDAO outsourceBillByConnectionDAO;

    @Transactional
    public void save(NIXMonthlyOutsourceBillByConnection object) {

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
    public NIXMonthlyOutsourceBillByConnection getById(long id) {

        try {
            return  outsourceBillByConnectionDAO.getItem(id);

        } catch (Exception e) {
        }
        return null;
    }

    @Transactional(transactionType=util.TransactionType.READONLY)
    public List<NIXMonthlyOutsourceBillByConnection> getByOutsourceBillId(long outsourceBillId) {

        try {
            return outsourceBillByConnectionDAO.getByOutsourceBillId(outsourceBillId);

        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

}
