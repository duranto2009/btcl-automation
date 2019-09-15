package nix.outsourcebill;
import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class NIXMonthlyOutsourceBillDAO {

    Class<NIXMonthlyOutsourceBill> classObject = NIXMonthlyOutsourceBill.class;

    public void insertItem(NIXMonthlyOutsourceBill object) throws Exception{
        ModifiedSqlGenerator.insert(object);
    }

    public NIXMonthlyOutsourceBill getItem(long id) throws Exception{
        return ModifiedSqlGenerator.getObjectByID(classObject, id);
    }

    public void updateItem(NIXMonthlyOutsourceBill object) throws Exception{
        ModifiedSqlGenerator.updateEntity(object);
    }


    public NIXMonthlyOutsourceBill getByVendorIdByMonthByYear(long vendorId, int month, int year) throws Exception {
        List<NIXMonthlyOutsourceBill> list = getAllObjectList(classObject, new NIXMonthlyOutsourceBillConditionBuilder()
                .Where()
                .vendorIdEquals(vendorId)
                .monthEquals(month)
                .yearEquals(year)
                .getCondition());
        return list.size() > 0 ? list.get(list.size()-1) : null;
    }

    public List<NIXMonthlyOutsourceBill> getByMonthByYear(int month, int year) throws Exception {
        List<NIXMonthlyOutsourceBill> list = getAllObjectList(classObject, new NIXMonthlyOutsourceBillConditionBuilder()
                .Where()
                .monthEquals(month)
                .yearEquals(year)
                .getCondition());
        return list;
    }
}
