package vpn.monthlyBillSummary;

import annotation.Transactional;
import common.bill.BillDTO;
import common.bill.BillService;
import requestMapping.Service;
import util.DateUtils;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import vpn.monthlyBill.VPNMonthlyBillByClient;
import vpn.monthlyBill.VPNMonthlyBillNotificationService;
import vpn.monthlyUsage.VPNMonthlyUsageByClient;

import java.util.ArrayList;
import java.util.List;

public class VPNMonthlyBillSummaryService {

    @Service
    VPNMonthlyBillSummaryByClientService vpnMonthlyBillSummaryByClientService;

    @Service
    VPNMonthlyBillSummaryByItemService vpnMonthlyBillSummaryByItemService;

    @Service
    VPNMonthlyBillSummaryGenerator vpnMonthlyBillSummaryGenerator;

    @Service
    BillService billService;

    public void generateSummary(List<VPNMonthlyBillByClient> vpnMonthlyBillByClients,
                                List<VPNMonthlyUsageByClient> vpnMonthlyUsageByClients) {

        List<VPNMonthlyBillSummaryByClient> lastMonthSummaries = new ArrayList<>();

        int month = DateUtils.getMonthFromDate(DateUtils.getFirstDayOfMonth(-1).getTime());
        int year = DateUtils.getYearFromDate(DateUtils.getFirstDayOfMonth(-1).getTime());

        for (VPNMonthlyBillByClient vpnMonthlyBillByClient : vpnMonthlyBillByClients)
        {
            VPNMonthlyBillSummaryByClient clientSummary = vpnMonthlyBillSummaryByClientService.getByClientIdAndDateRange(
                    vpnMonthlyBillByClient.getClientId(),
                    month,
                    year);
            if(clientSummary != null)
            {
                clientSummary.setVpnMonthlyBillSummaryByItems(vpnMonthlyBillSummaryByItemService.getListByMonthlyBillSummaryByClientId(clientSummary.getId()));
                lastMonthSummaries.add(clientSummary);
            }
        }
        List<VPNMonthlyBillSummaryByClient> summaries = vpnMonthlyBillSummaryGenerator.
                generateSummary(vpnMonthlyBillByClients, vpnMonthlyUsageByClients, lastMonthSummaries);

        ServiceDAOFactory.getService(VPNMonthlyBillNotificationService.class).notifyUsers(summaries);
    }


    @Transactional(transactionType=util.TransactionType.READONLY)
    public boolean isMonthlyBillGenerated(int month, int year)
    {
        return vpnMonthlyBillSummaryByClientService.isMonthlyBillGenerated(month, year);
    }



    public VPNMonthlyBillSummaryByClient getVPNMonthlyBillSummary(Long clientId, int month, int year) throws Exception
    {
        VPNMonthlyBillSummaryByClient clientSummary = vpnMonthlyBillSummaryByClientService.getByClientIdAndDateRange(clientId, month, year);

        if(clientSummary != null)
        {
            BillDTO billDTO = billService.getBillByBillID(clientSummary.getID());	//this is billID
            ModifiedSqlGenerator.populateObjectFromOtherObject(billDTO, clientSummary, BillDTO.class);

            List<VPNMonthlyBillSummaryByItem> list = vpnMonthlyBillSummaryByItemService.getListByMonthlyBillSummaryByClientId(clientSummary.getId());
            clientSummary.getVpnMonthlyBillSummaryByItems().addAll(list);
        }
        return clientSummary;
    }

}
