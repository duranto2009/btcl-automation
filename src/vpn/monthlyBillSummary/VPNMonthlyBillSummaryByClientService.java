package vpn.monthlyBillSummary;

import annotation.DAO;
import annotation.Transactional;
import org.apache.log4j.Logger;
import util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class VPNMonthlyBillSummaryByClientService {


    public static Logger logger = Logger.getLogger(VPNMonthlyBillSummaryByClientService.class);

    @DAO
    VPNMonthlyBillSummaryByClientDAO vpnMonthlyBillSummaryByClientDAO;


    @Transactional(transactionType=util.TransactionType.READONLY)
    public VPNMonthlyBillSummaryByClient getByClientIdAndDateRange(long clientId,  int month, int year) {

        long fromDate = DateUtils.getStartTimeOfMonth(month, year);
        long toDate = DateUtils.getEndTimeOfMonth(month, year);
        try {
            VPNMonthlyBillSummaryByClient client = vpnMonthlyBillSummaryByClientDAO.getByClientIdAndDateRange(clientId, fromDate, toDate);
            populateNonMappedData(client);

            return client;
        } catch (Exception e) {
        }
        return null;
    }


    private void populateNonMappedData(VPNMonthlyBillSummaryByClient client)
    {
        if(client == null)
            return;

        client.setDataFromDBContent();
    }


    @Transactional
    public void save(VPNMonthlyBillSummaryByClient object) {

        try {
            if(object.getId() == null || object.getId() == 0)
                vpnMonthlyBillSummaryByClientDAO.insertItem(object);
            else
                vpnMonthlyBillSummaryByClientDAO.updateItem(object);
        } catch (Exception e) {
            logger.error("error while saving ", e);
        }
        return;
    }



    @Transactional(transactionType=util.TransactionType.READONLY)
    public boolean isMonthlyBillGenerated(int month, int year) {

        long fromDate = DateUtils.getStartTimeOfMonth(month, year);
        long toDate = DateUtils.getEndTimeOfMonth(month, year);
        try {
            return vpnMonthlyBillSummaryByClientDAO.getCountByDateRange(fromDate, toDate) > 0 ? true : false;
        } catch (Exception e) {
            logger.fatal("error in isMonthlyBillGenerated ");
        }
        return true;
    }


    @Transactional(transactionType=util.TransactionType.READONLY)
    public List<Long> getClientIdsByDateRange(int month, int year) {

        long fromDate = DateUtils.getStartTimeOfMonth(month, year);
        long toDate = DateUtils.getEndTimeOfMonth(month, year);

        try {
            return vpnMonthlyBillSummaryByClientDAO.getClientIdsByDateRange(fromDate, toDate);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new ArrayList<>();
    }



}
