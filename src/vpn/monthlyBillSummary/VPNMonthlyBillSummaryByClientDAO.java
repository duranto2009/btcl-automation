package vpn.monthlyBillSummary;

import util.ModifiedSqlGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class VPNMonthlyBillSummaryByClientDAO {

    public VPNMonthlyBillSummaryByClient getByClientIdAndDateRange(long clientId, long fromDate, long toDate) throws Exception{

        List<VPNMonthlyBillSummaryByClient> list = getAllObjectList(VPNMonthlyBillSummaryByClient.class, new VPNMonthlyBillSummaryByClientConditionBuilder()
                .Where()
                .clientIdEquals(clientId)
                .createdDateGreaterThanEquals(fromDate)
                .createdDateLessThanEquals(toDate)
                .getCondition());
        return list.size() > 0 ? list.get(list.size()-1) : null;
    }

    public void insertItem(VPNMonthlyBillSummaryByClient object) throws Exception{
        ModifiedSqlGenerator.insert(object);
    }


    public void updateItem(VPNMonthlyBillSummaryByClient object) throws Exception{
        ModifiedSqlGenerator.updateEntity(object);
    }

    public int getCountByDateRange(long fromDate, long toDate) throws Exception{

        List<VPNMonthlyBillSummaryByClient> list = getAllObjectList(VPNMonthlyBillSummaryByClient.class, new VPNMonthlyBillSummaryByClientConditionBuilder()
                .Where()
                .createdDateGreaterThanEquals(fromDate)
                .createdDateLessThanEquals(toDate)
                .getCondition());
        return list.size();
    }


    public List<Long> getClientIdsByDateRange(long fromDate, long toDate) throws Exception {

        List<VPNMonthlyBillSummaryByClient> list = getAllObjectList(VPNMonthlyBillSummaryByClient.class, new VPNMonthlyBillSummaryByClientConditionBuilder()
                .Where()
                .createdDateGreaterThanEquals(fromDate)
                .createdDateLessThanEquals(toDate)
                .getCondition());

        return list == null ? new ArrayList<>()
                :list.stream().mapToLong(x->x.getClientId()).boxed().collect(Collectors.toList());
    }


}

