package lli.longTerm;

import annotation.DAO;
import annotation.Transactional;
import org.apache.log4j.Logger;

import java.util.List;

public class LLILongTermBenefitService {

    public static Logger logger = Logger.getLogger(LLILongTermBenefitService.class);

    @DAO
    LLILongTermBenefitDAO longTermBenefitDAO;


    @Transactional
    public void save(LLILongTermBenefit object) {

        try {
            if(object.getId() == null || object.getId() == 0)
                longTermBenefitDAO.insertItem(object);
            else
                longTermBenefitDAO.updateItem(object);
        } catch (Exception e) {
            logger.error("error while saving ", e);
        }
        return;
    }


    @Transactional(transactionType=util.TransactionType.READONLY)
    public LLILongTermBenefit getById(long id) {

        try {
            return longTermBenefitDAO.getItem(id);
        } catch (Exception e) {
        }
        return null;
    }


    @Transactional(transactionType=util.TransactionType.READONLY)
    public List<LLILongTermBenefit> getActiveListByClientId(long clientId) {

        try {
            return longTermBenefitDAO.getActiveListByClientId(clientId);
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * get total benefits for this long term contract id
     * @param contractId
     * @return
     */
    @Transactional(transactionType=util.TransactionType.READONLY)
    public Double getTotalBenefitsByContractId(long contractId) {

        try {
            LLILongTermBenefit benefit = longTermBenefitDAO.getItemByContractId(contractId);
            return benefit == null ? 0 : benefit.getAmount();
        } catch (Exception e) {
        }
        return null;
    }


    @Transactional(transactionType=util.TransactionType.READONLY)
    public LLILongTermBenefit getByContractId(long contractId) {

        try {
            LLILongTermBenefit benefit = longTermBenefitDAO.getItemByContractId(contractId);
            return benefit;
        } catch (Exception e) {
        }
        return null;
    }
}
