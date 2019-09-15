package vpn.monthlyOutsourceBill;
import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class VPNMonthlyOutsourceBillDAO {

    Class<VPNMonthlyOutsourceBill> classObject = VPNMonthlyOutsourceBill.class;

    public void insertItem(VPNMonthlyOutsourceBill object) throws Exception{
        ModifiedSqlGenerator.insert(object);
    }

    public VPNMonthlyOutsourceBill getItem(long id) throws Exception{
        return ModifiedSqlGenerator.getObjectByID(classObject, id);
    }

    public void updateItem(VPNMonthlyOutsourceBill object) throws Exception{
        ModifiedSqlGenerator.updateEntity(object);
    }


    public VPNMonthlyOutsourceBill getByVendorIdByMonthByYear(long vendorId, int month, int year) throws Exception {
        List<VPNMonthlyOutsourceBill> list = getAllObjectList(classObject, new VPNMonthlyOutsourceBillConditionBuilder()
                .Where()
                .vendorIdEquals(vendorId)
                .monthEquals(month)
                .yearEquals(year)
                .getCondition());
        return list.size() > 0 ? list.get(list.size()-1) : null;
    }

    public List<VPNMonthlyOutsourceBill> getByMonthByYear(int month, int year) throws Exception {
        List<VPNMonthlyOutsourceBill> list = getAllObjectList(classObject, new VPNMonthlyOutsourceBillConditionBuilder()
                .Where()
                .monthEquals(month)
                .yearEquals(year)
                .getCondition());
        return list;
    }
}
