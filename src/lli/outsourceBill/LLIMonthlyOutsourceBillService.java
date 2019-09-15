package lli.outsourceBill;

import annotation.DAO;
import annotation.Transactional;
import org.apache.log4j.Logger;
import util.ModifiedSqlGenerator;
import util.SqlPair;
import util.TransactionType;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LLIMonthlyOutsourceBillService {

    public static Logger logger = Logger.getLogger(LLIMonthlyOutsourceBillService.class);

    @DAO
    LLIMonthlyOutsourceBillDAO monthlyOutsourceBillDAO;


    @Transactional
    public void save(LLIMonthlyOutsourceBill object) {

        try {
            if(object.getId() == null || object.getId() == 0)
                monthlyOutsourceBillDAO.insertItem(object);
            else
                monthlyOutsourceBillDAO.updateItem(object);
        } catch (Exception e) {
            logger.error("error while saving ", e);
        }
    }


    @Transactional(transactionType=util.TransactionType.READONLY)
    public LLIMonthlyOutsourceBill getById(long id) {

        try {
            return  monthlyOutsourceBillDAO.getItem(id);

        } catch (Exception e) {
        }
        return null;
    }


    @Transactional(transactionType=util.TransactionType.READONLY)
    public LLIMonthlyOutsourceBill getByVendorIdByMonthByYear(long vendorId, int month, int year) {

        try {
            return monthlyOutsourceBillDAO.getByVendorIdByMonthByYear(vendorId, month, year);

        } catch (Exception e) {
        }
        return null;
    }

    @Transactional(transactionType=util.TransactionType.READONLY)
    public List<LLIMonthlyOutsourceBill> getByMonthByYear(int month, int year) {

        try {
            return monthlyOutsourceBillDAO.getByMonthByYear(month, year);

        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public void getAggregatedBillByVendors(Map<Long, Double> map) throws Exception {
        LLIMonthlyOutsourceBillConditionBuilder conditionBuilder = new LLIMonthlyOutsourceBillConditionBuilder();
        String selectPart = "SELECT SUM("+  conditionBuilder.getTotalPayableColumnName() + ") as sum, " + conditionBuilder.getVendorIdColumnName() + " as vendor" ;

        String groupByPart = " GROUP BY " + conditionBuilder.getVendorIdColumnName();
        SqlPair sqlPair = conditionBuilder
                .fromLLIMonthlyOutsourceBill()
                .Where()
                .statusLike(OutsourceBillStatus.ACTIVE.name())
                .getSqlPair();

        sqlPair.sql = selectPart + sqlPair.sql +  groupByPart;
        ResultSet rs = ModifiedSqlGenerator.getResultSetBySqlPair(sqlPair);
        while(rs.next()) {
            long vendorId = rs.getLong(2);
            double sum = rs.getDouble(1);
            if(map.containsKey(vendorId)) {
                map.put(vendorId , map.get(vendorId) + sum );
            }else {
                map.put(vendorId, sum);
            }

        }
    }

}
