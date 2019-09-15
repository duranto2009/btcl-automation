package vpn.monthlyOutsourceBill;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class VPNMonthlyOutsourceBillByLinkDAO {

    Class<VPNMonthlyOutsourceBillByLink> classObject = VPNMonthlyOutsourceBillByLink.class;

    public void insertItem(VPNMonthlyOutsourceBillByLink object) throws Exception{
        ModifiedSqlGenerator.insert(object);
    }

    public VPNMonthlyOutsourceBillByLink getItem(long id) throws Exception{
        return ModifiedSqlGenerator.getObjectByID(classObject, id);
    }

    public void updateItem(VPNMonthlyOutsourceBillByLink object) throws Exception{
        ModifiedSqlGenerator.updateEntity(object);
    }


    public List<VPNMonthlyOutsourceBillByLink> getByOutsourceBillId(long outsourceBillId) throws Exception {
        return getAllObjectList(classObject, new VPNMonthlyOutsourceBillByLinkConditionBuilder()
                .Where()
                .outsourceBillIdEquals(outsourceBillId)
                .getCondition());
    }


}
