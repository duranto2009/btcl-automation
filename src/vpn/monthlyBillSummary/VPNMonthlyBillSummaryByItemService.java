package vpn.monthlyBillSummary;

import annotation.DAO;
import annotation.Transactional;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class VPNMonthlyBillSummaryByItemService {

    static Logger logger = Logger.getLogger( VPNMonthlyBillSummaryByItemService.class );


    @DAO
    VPNMonthlyBillSummaryByItemDAO vpnMonthlyBillSummaryByItemDAO;

    @Transactional(transactionType=util.TransactionType.READONLY)
    public List<VPNMonthlyBillSummaryByItem> getListByMonthlyBillSummaryByClientId(long idOfmonthlyBillSummaryByClient) {

        try {
            List<VPNMonthlyBillSummaryByItem> list = vpnMonthlyBillSummaryByItemDAO.getListByMonthlyBillSummaryByClientId(idOfmonthlyBillSummaryByClient);
            populateNonMappedData(list);
            return list;
        } catch (Exception e) {

        }
        return new ArrayList<>();
    }

    private void populateNonMappedData(List<VPNMonthlyBillSummaryByItem> items)
    {
        for(VPNMonthlyBillSummaryByItem item : items)
        {
            populateNonMappedData(item);
        }
    }

    private void populateNonMappedData(VPNMonthlyBillSummaryByItem item)
    {
        if(item == null)
            return;

        item.setDataFromDBContent();
    }


    @Transactional
    public void save(VPNMonthlyBillSummaryByItem object) {

        try {
            if(object.getId() == null || object.getId() == 0)
                vpnMonthlyBillSummaryByItemDAO.insertItem(object);
            else
                vpnMonthlyBillSummaryByItemDAO.updateItem(object);
        } catch (Exception e) {
            logger.error("error while saving SummaryByConnection", e);
        }
        return;
    }

}
