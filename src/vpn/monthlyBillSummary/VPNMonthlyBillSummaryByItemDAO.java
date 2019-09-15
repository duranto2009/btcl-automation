package vpn.monthlyBillSummary;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class VPNMonthlyBillSummaryByItemDAO {

    public List<VPNMonthlyBillSummaryByItem> getListByMonthlyBillSummaryByClientId(long monthlyBillByClientId) throws Exception {

        return getAllObjectList(VPNMonthlyBillSummaryByItem.class, new VPNMonthlyBillSummaryByItemConditionBuilder()
                .Where()
                .monthlyBillSummaryByClientIdEquals(monthlyBillByClientId)
                .getCondition());

    }

    public void insertItem(VPNMonthlyBillSummaryByItem object) throws Exception{
        ModifiedSqlGenerator.insert(object);
    }

    public void updateItem(VPNMonthlyBillSummaryByItem object) throws Exception{
        ModifiedSqlGenerator.updateEntity(object);
    }

}
