package test.junit;

import common.ApplicationGroupType;
import entity.facade.DemandNoteAutofillFacade;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import util.ServiceDAOFactory;
import vpn.demandNote.VPNDemandNote;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@Log4j
public class VPNDemandNoteTest {
    private DemandNoteAutofillFacade demandNoteAutofillFacade = ServiceDAOFactory.getService(DemandNoteAutofillFacade.class);
    @Test
    public void vpnLinkDemandNoteAutofillTest() {
        double actualRegistrationCharge = 0;
        double actualVatPercentage = 15;
        double actualLocalLoopCharge = 0;
        double actualBandwidthCharge = 0;
        double actualSecurityCharge = 0;
        ApplicationGroupType groupType = ApplicationGroupType.VPN_LINK_APPLICATION;
        VPNDemandNote vpnDemandNote=null;
        long applicationId = -1; // TODO; Change it
        try{
            vpnDemandNote = demandNoteAutofillFacade.autofillVPNDemandNote(applicationId, true, ApplicationGroupType.VPN_LINK_APPLICATION);
        }catch (Exception e){
            log.fatal(e.getMessage());
        }

        assertNotNull("VPN Demand Not NULL: ", vpnDemandNote);
        assertEquals("Reg. Charge: ", vpnDemandNote.getRegistrationCharge(), actualRegistrationCharge);
        assertEquals("Security Charge: " , vpnDemandNote.getSecurityCharge(), actualSecurityCharge);
        assertEquals("Bandwidth Charge: " , vpnDemandNote.getBandwidthCharge(), actualBandwidthCharge);
        assertEquals("Local Loop Charge: " , vpnDemandNote.getSecurityCharge(), actualLocalLoopCharge);
        assertEquals("VAT Percentage : " , vpnDemandNote.getVatPercentage(), actualVatPercentage);

    }
}
