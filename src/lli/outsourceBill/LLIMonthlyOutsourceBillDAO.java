package lli.outsourceBill;

import static util.ModifiedSqlGenerator.getAllObjectList;
import util.ModifiedSqlGenerator;

import java.util.List;

public class LLIMonthlyOutsourceBillDAO {

    Class<LLIMonthlyOutsourceBill> classObject = LLIMonthlyOutsourceBill.class;

    public void insertItem(LLIMonthlyOutsourceBill object) throws Exception{
        ModifiedSqlGenerator.insert(object);
    }

    public LLIMonthlyOutsourceBill getItem(long id) throws Exception{
        return ModifiedSqlGenerator.getObjectByID(classObject, id);
    }

    public void updateItem(LLIMonthlyOutsourceBill object) throws Exception{
        ModifiedSqlGenerator.updateEntity(object);
    }


    public LLIMonthlyOutsourceBill getByVendorIdByMonthByYear(long vendorId, int month, int year) throws Exception {
        List<LLIMonthlyOutsourceBill> list = getAllObjectList(classObject, new LLIMonthlyOutsourceBillConditionBuilder()
                .Where()
                .vendorIdEquals(vendorId)
                .monthEquals(month)
                .yearEquals(year)
                .getCondition());
        return list.size() > 0 ? list.get(list.size()-1) : null;
    }

    public List<LLIMonthlyOutsourceBill> getByMonthByYear(int month, int year) throws Exception {
        List<LLIMonthlyOutsourceBill> list = getAllObjectList(classObject, new LLIMonthlyOutsourceBillConditionBuilder()
                .Where()
                .monthEquals(month)
                .yearEquals(year)
                .getCondition());
        return list;
    }
}
