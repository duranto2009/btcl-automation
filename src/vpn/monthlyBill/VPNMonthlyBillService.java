package vpn.monthlyBill;

import common.RequestFailureException;
import global.GlobalService;
import login.LoginDTO;
import org.apache.log4j.Logger;
import requestMapping.Service;
import util.DateUtils;
import util.NavigationService;
import vpn.VPNClientService;
import vpn.client.ClientDetailsDTO;
import vpn.monthlyBillSummary.VPNMonthlyBillSummaryByClient;
import vpn.monthlyBillSummary.VPNMonthlyBillSummaryByClientService;
import vpn.monthlyBillSummary.VPNMonthlyBillSummaryService;
import vpn.monthlyUsage.VPNMonthlyUsageByClient;
import vpn.monthlyUsage.VPNMonthlyUsageGenerator;
import vpn.monthlyUsage.VPNMonthlyUsageService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class VPNMonthlyBillService implements NavigationService {

    static Logger logger = Logger.getLogger(VPNMonthlyBillService.class);

    @Service
    GlobalService globalService;

    @Service
    VPNClientService vpnClientService;

    @Service
    VPNMonthlyUsageService vpnMonthlyUsageService;

    @Service
    VPNMonthlyBillByClientService vpnMonthlyBillByClientService;

    @Service
    VPNMonthlyBillByLinkService vpnMonthlyBillByLinkService;

    @Service
    VPNMonthlyUsageGenerator vpnMonthlyUsageGenerator;

    @Service
    VPNMonthlyBillGenerator vpnMonthlyBillGenerator;

    @Service
    VPNMonthlyBillSummaryService vpnMonthlyBillSummaryService;

    @Service
    VPNMonthlyBillSummaryByClientService vpnMonthlyBillSummaryByClientService;


    public void generateCurrentVPNMonthlyBill(List<Long> clientIds) throws Exception {
        List<ClientDetailsDTO> clientsToGenerateUsage = new ArrayList<>();
        List<ClientDetailsDTO> clientsToGenerateBill = new ArrayList<>();

        List<Long> clientsToFetchUsage = new ArrayList<>();
        List<Long> clientsToFetchBill = new ArrayList<>();

        for (Long clientId : clientIds) {
            ClientDetailsDTO clientDetailsDTO = vpnClientService.getVPNClient(clientId);
            if (clientDetailsDTO == null) {
                logger.warn("vpn clientId " + clientId + " not found for generating monthly bill individual");
                continue;
            } else {
                if (vpnMonthlyUsageService.isCurrentMonthlyUsageGenerated(clientId) == false)
                    clientsToGenerateUsage.add(clientDetailsDTO);
                else
                    clientsToFetchUsage.add(clientId);

                if (isCurrentMonthlyBillGenerated(clientId) == false)
                    clientsToGenerateBill.add(clientDetailsDTO);
                else
                    clientsToFetchBill.add(clientId);
            }
        }
        List<VPNMonthlyUsageByClient> vpnMonthlyUsageByClients = new ArrayList<>();
        if (clientsToGenerateUsage.size() > 0)
            vpnMonthlyUsageByClients = vpnMonthlyUsageGenerator.generateMonthlyUsage(clientsToGenerateUsage);

        int month = DateUtils.getMonthFromDate(DateUtils.getFirstDayOfMonth(-1).getTime());
        int year = DateUtils.getYearFromDate(DateUtils.getFirstDayOfMonth(-1).getTime());

        for (long clientId : clientsToFetchUsage) {
            vpnMonthlyUsageByClients.add(vpnMonthlyUsageService.getVPNMonthlyUsageByClient(clientId, month, year));
        }

        //usage ends
        //bill starts
        List<VPNMonthlyBillByClient> vpnMonthlyBillByClients = new ArrayList<>();
        if(clientsToGenerateBill.size() > 0)
            vpnMonthlyBillByClients = vpnMonthlyBillGenerator.generateMonthlyBill(clientsToGenerateBill);

        month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
        year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());

        for(long clientId : clientsToFetchBill)
        {
            vpnMonthlyBillByClients.add(getVPNMonthlyBillByClient(clientId, month, year));
        }

        vpnMonthlyBillSummaryService.generateSummary(vpnMonthlyBillByClients, vpnMonthlyUsageByClients);

    }


    public VPNMonthlyBillByClient  getVPNMonthlyBillByClient(long clientId, int month, int year) throws Exception
    {
        VPNMonthlyBillByClient vpnMonthlyBillByClient = vpnMonthlyBillByClientService.getByClientIdAndDateRange(clientId, month, year);

        if(vpnMonthlyBillByClient != null)
        {
            List<VPNMonthlyBillByLink> list = vpnMonthlyBillByLinkService.getListByMonthlyBillByClientId(vpnMonthlyBillByClient.getId());
            vpnMonthlyBillByClient.getMonthlyBillByLinks().addAll(list);
        }
        else {
            throw new RequestFailureException("No data found for the month of "+month);
        }
        return vpnMonthlyBillByClient;
    }



    public void generateCurrentVPNMonthlyBillForAll() throws Exception
    {
        List<VPNMonthlyUsageByClient> vpnMonthlyUsageByClients = new ArrayList<>();
        List<VPNMonthlyBillByClient> vpnMonthlyBillByClients = new ArrayList<>();

        if(isCurrentMonthlyBillSummaryGenerated())
            return;

        if(vpnMonthlyUsageService.isCurrentMonthlyUsageGenerated() == false)
        {
            vpnMonthlyUsageByClients = vpnMonthlyUsageGenerator.generateMonthlyUsage();
        }
        //TODO else fetch generated usages

        if(isCurrentMonthlyBillGenerated() == false)
        {
            vpnMonthlyBillByClients = vpnMonthlyBillGenerator.generateMonthlyBill();
        }

        vpnMonthlyBillSummaryService.generateSummary(vpnMonthlyBillByClients, vpnMonthlyUsageByClients);
    }


    public boolean isCurrentMonthlyBillGenerated() throws Exception
    {
        int month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
        int year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());

        return vpnMonthlyBillByClientService.isMonthlyBillGenerated(month, year);

    }

    public boolean isCurrentMonthlyBillGenerated(long clientId) throws Exception {
        int month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
        int year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());

        VPNMonthlyBillByClient vpnMonthlyBillByClient = vpnMonthlyBillByClientService.getByClientIdAndDateRange(clientId, month, year);

        return vpnMonthlyBillByClient == null ? false : true;

    }

    public boolean isCurrentMonthlyBillSummaryGenerated() throws ParseException
    {
        int month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
        int year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());

        return vpnMonthlyBillSummaryService.isMonthlyBillGenerated(month, year);
    }

    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
    }

    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        List<ClientDetailsDTO> lliClients = vpnClientService.getAllVPNClient();

        String dateStr = (String) searchCriteria.get("Month");
        int month=-1,year=-1;
        if(dateStr!=null) {
            SimpleDateFormat dayMonthYearDateFormat = new SimpleDateFormat( "yyyy-MM" );
            Date date = dayMonthYearDateFormat.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);
        }
        else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);
        }
        String clientId= (String) searchCriteria.get("clientID");

        ArrayList<Long> ids = new ArrayList<>();


        if(clientId!=null && clientId.length()>0) {
            long cId = Long.parseLong(clientId);
            VPNMonthlyBillSummaryByClient clientSummary = vpnMonthlyBillSummaryByClientService.getByClientIdAndDateRange(cId, month, year);

            if(clientSummary == null) {
                ids.add(cId);
            }
        }
        else {
            List<Long> clientIdsOfMonthlyBillSummary = vpnMonthlyBillSummaryByClientService.getClientIdsByDateRange(month, year);

            for (ClientDetailsDTO lliClient : lliClients)
            {
                if(clientIdsOfMonthlyBillSummary.contains(lliClient.getClientID()) == false)
                {
                    ids.add(lliClient.getClientID());
                }
            }
        }
        return ids;
    }

    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        return vpnClientService.getVPNClients(recordIDs);
    }
}
