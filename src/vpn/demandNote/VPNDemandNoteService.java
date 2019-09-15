package vpn.demandNote;

import common.ApplicationGroupType;
import common.RequestFailureException;
import common.bill.BillService;
import requestMapping.Service;
import vpn.application.VPNApplication;

import java.util.Arrays;

public class VPNDemandNoteService {

    @Service BillService billService;

    public VPNDemandNote getAutofillData(long appId, ApplicationGroupType appGroupType) throws Exception {
        if(appGroupType == ApplicationGroupType.VPN_LINK_APPLICATION){
            return getVPNLinkDemandNoteAutofilled(appId, appGroupType);
        }else if(appGroupType == ApplicationGroupType.VPN_CLIENT_APPLICATION) {
            return getVPNClientDemandNoteAutofilled(appId, appGroupType);
        }
        throw new RequestFailureException("Invalid Application Group Type: " + appGroupType.name());
    }



    public void generateDemandNote() {
        // TODO raihan
    }


    private VPNDemandNote getVPNClientDemandNoteAutofilled(long appId, ApplicationGroupType appGroupType) {
        return null;//TODO raihan
    }

    private VPNDemandNote getVPNLinkDemandNoteAutofilled(long appId, ApplicationGroupType appGroupType) throws Exception {
        VPNDemandNote demandNote = new VPNDemandNote();

        VPNApplication vpnApplication = new VPNApplication(); // TODO
        vpnApplication.setVpnApplicationLinks(
                Arrays.asList(

                )

        );
        //Bill DTO part
        demandNote.setVatPercentage(15); //TODO
        demandNote.setDiscountPercentage(0); //TODO
        demandNote.setDescription("Demand Note for Virtual Private Network(VPN) with application : " + vpnApplication.getApplicationId()); //TODO

        demandNote.setApplicationGroupType(appGroupType);
        demandNote.setClientID(vpnApplication.getClientId());
        calculateEssentialCharges(demandNote, vpnApplication);


        return demandNote;
    }

    private void calculateEssentialCharges(VPNDemandNote demandNote, VPNApplication vpnApplication) throws Exception {
//        TableDTO tableDTO = ServiceDAOFactory.getService(CostConfigService.class).getLatestTableWithCategoryID(ModuleConstants.Module_ID_VPN, 1);
//        GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);
//        globalService.getAllObjectListByCondition(LocalLoop.class,
//                " WHERE " + ModifiedSqlGenerator.getColumnName(LocalLoop.class, "applicationId") + " = " + vpnApplication.getApplicationId()
//                );
        calculateRegistrationCharge(demandNote, vpnApplication);
        calculateBWCharge(demandNote, vpnApplication);
        calculateLocalLoopCharge(demandNote, vpnApplication);
        calculateSecurityCharge(demandNote, vpnApplication);
    }

    private void calculateRegistrationCharge(VPNDemandNote demandNote, VPNApplication vpnApplication) {
        demandNote.setRegistrationCharge(10000);
        // for each link predefined value from db
        // sum it up
    }

    private void calculateSecurityCharge(VPNDemandNote demandNote, VPNApplication vpnApplication) {
        demandNote.setSecurityCharge(demandNote.getBandwidthCharge()); // without discount
    }

    private void calculateLocalLoopCharge(VPNDemandNote demandNote, VPNApplication vpnApplication) {

        // for each link after efr state this will make sense
        // let distance = link.ocDistance + link.btclDistance
        // first 500 m 1000 bdt/ minimum cost rest will be calculated by a factor defined in db
        // sum it up
        demandNote.setLocalLoopCharge(40000); //TODO
    }

    private void calculateBWCharge(VPNDemandNote demandNote, VPNApplication vpnApplication) throws Exception {
        // needs to calculate from cost chart.
        // for each link after ifr state this will make sense
        // let bw = link.bw
        // let distance = link.popToPopDistance
        // let cost = bw/distance from chart
        // let costlessDiscount = cost - discount
        // sum it up
//        ServiceDAOFactory.getService(Globa.class).getLocalLoopByApplicationId(vpnApplication.getApplicationId());
//        tableDTO
        demandNote.setBandwidthCharge(1000); // TODO
    }

}
