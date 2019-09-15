package vpn.monthlyOutsourceBill;

import annotation.ForwardedAction;
import common.ClientDTO;
import common.repository.AllClientRepository;
import global.GlobalService;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import vpn.network.VPNNetworkLink;
import vpn.network.VPNNetworkLinkService;

import java.util.List;


@ActionRequestMapping("vpn/monthly-outsource-bill")
public class VPNMonthlyOutsourceBillAction extends AnnotatedRequestMappingAction {

    @Service
    GlobalService globalService;

    @Service
    VPNMonthlyOutsourceBillByLinkService vpnMonthlyOutsourceBillByLinkService;
    @Service
    VPNMonthlyOutsourceBillService vpnMonthlyOutsourceBillService;

    @ForwardedAction
    @RequestMapping(mapping = "/search", requestMethod = RequestMethod.All)
    public String getMonthlyOutSourceBillSummaryPage() {
        return "vpn-monthly-outsourcing-bill-page";
    }

    @RequestMapping(mapping = "/summary", requestMethod = RequestMethod.GET)
    public VPNMonthlyOutsourceBill getMonthlyOutSourceBillSummary(
            @RequestParameter("vendor") long vendor,
            @RequestParameter("month") int month,
            @RequestParameter("year") int year) throws Exception {

        return vpnMonthlyOutsourceBillService.getByVendorIdByMonthByYear(vendor, month, year);
    }

    @RequestMapping(mapping = "/details", requestMethod = RequestMethod.GET)
    public List<VPNMonthlyOutsourceBillByLink> getMonthlyOutSourceBillDetails(
            @RequestParameter("vendor") long vendor,
            @RequestParameter("month") int month,
            @RequestParameter("year") int year) throws Exception {

        VPNMonthlyOutsourceBill vpnMonthlyOutsourceBill = vpnMonthlyOutsourceBillService.getByVendorIdByMonthByYear(vendor, month, year);
        long outSourceBillId = vpnMonthlyOutsourceBill.getId();

        List<VPNMonthlyOutsourceBillByLink> vpnMonthlyOutsourceBillByLinks =
                vpnMonthlyOutsourceBillByLinkService.getByOutsourceBillId(outSourceBillId);


        for (VPNMonthlyOutsourceBillByLink vpnMonthlyOutsourceBillByLink : vpnMonthlyOutsourceBillByLinks) {
            long linkId = vpnMonthlyOutsourceBillByLink.getLinkId();
            VPNNetworkLink vpnNetworkLink = globalService.findByPK(VPNNetworkLink.class, linkId);

            long clientId = vpnNetworkLink.getClientId();
            ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(clientId);
            vpnMonthlyOutsourceBillByLink.setClientName(clientDTO.getName());
            vpnMonthlyOutsourceBillByLink.setActiveFrom(vpnNetworkLink.getActiveFrom());
        }
        return vpnMonthlyOutsourceBillByLinks;
    }
}
