package vpn.monthlyOutsourceBill;

import annotation.DAO;
import annotation.Transactional;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class VPNMonthlyOutsourceBillByLinkService {

    static Logger logger = Logger.getLogger(VPNMonthlyOutsourceBillByLinkService.class);

    @DAO
    VPNMonthlyOutsourceBillByLinkDAO outsourceBillByConnectionDAO;

    @Transactional
    public void save(VPNMonthlyOutsourceBillByLink object) {

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
    public VPNMonthlyOutsourceBillByLink getById(long id) {

        try {
            return  outsourceBillByConnectionDAO.getItem(id);

        } catch (Exception e) {
        }
        return null;
    }

    @Transactional(transactionType=util.TransactionType.READONLY)
    public List<VPNMonthlyOutsourceBillByLink> getByOutsourceBillId(long outsourceBillId) {

        try {
            return outsourceBillByConnectionDAO.getByOutsourceBillId(outsourceBillId);

        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

}
