package entity.facade;

import accounting.DemandNote;
import annotation.Transactional;
import application.Application;
import application.ApplicationService;
import application.ApplicationState;
import common.ApplicationGroupType;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillDTO;
import common.bill.BillService;
import common.pdf.AsyncPdfService;
import entity.annotations.ExcludeFromVATCalculation;
import global.GlobalService;
import lli.demandNote.adjustment.DNAdjustStatus;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import officialLetter.OfficialLetter;
import officialLetter.OfficialLetterService;
import officialLetter.OfficialLetterType;
import officialLetter.RecipientElement;
import requestMapping.Service;
import user.UserService;
import util.ServiceDAOFactory;
import util.TimeConverter;
import util.TransactionType;
import validator.annotation.NonNegative;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationLink;
import vpn.application.VPNApplicationService;
import vpn.demandNote.VPNDemandNote;
import vpn.demandNote.VPNDemandNoteAdjustment;
import vpn.demandNote.VPNDemandNoteAdjustmentService;
import vpn.demandNote.VPNLoopChargeDiscountEligibility;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Log4j
public class DemandNoteGenerationFacade {

    @Service private ApplicationService applicationService;
    @Service private VPNApplicationService vpnApplicationService;
    @Service private BillService billService;
    @Service private UserService userService;
    @Service private OfficialLetterService officialLetterService;
    @Service private GlobalService globalService;

    public void generateVPNDemandNote(VPNDemandNote demandNote, long id, boolean isGlobal, int next, LoginDTO loginDTO) throws Exception {

       generateVPNLinkDemandNote(demandNote, id, isGlobal, next, loginDTO);

    }

    private void generateVPNLinkDemandNote(VPNDemandNote demandNote, long id, boolean isGlobal, int nextState, LoginDTO loginDTO) throws Exception {
        VPNApplication application = vpnApplicationService.getAppropriateVPNApplication(id, isGlobal);
        isDemandNoteAlreadyGenerated(application);
        calculateInvoiceAmount(demandNote, application.getVpnApplicationLinks());
        saveData(demandNote, application, nextState, loginDTO);
        AsyncPdfService.getInstance().accept(demandNote);
        adjustDemandNote(demandNote, application);

    }

    private void isDemandNoteAlreadyGenerated(VPNApplication vpnApplication) {
        long dnAlreadyGeneratedCount = vpnApplication.getVpnApplicationLinks().stream()
                .filter(t->t.getDemandNoteId()!=null)
                .count();
        if(dnAlreadyGeneratedCount>0) {
            throw new RequestFailureException("Demand Note is already generated");
        }
    }

    // if the demand note id inside application id is declared as object LONG then defaultValueNULL = true;
    // else false
    public static void isDemandNoteAlreadyGenerated(Long demandNoteId, boolean defaultValueNULL) {
        if(defaultValueNULL) {
            if(demandNoteId != null) {
                throw new RequestFailureException("Demand Note is already generated");
            }
        }else{
            if(demandNoteId != 0) {
                throw new RequestFailureException("Demand Note is already generated");
            }
        }

    }

    private void adjustDemandNote(VPNDemandNote demandNote, VPNApplication application) throws Exception {
        VPNDemandNoteAdjustment dnAdjust;

        if (application.isSkipPayment()) {
            dnAdjust = VPNDemandNoteAdjustment.builder()
                    .clientId(demandNote.getClientID())
                    .billId(demandNote.getID())
//                    .connectionType(connectionType)
                    .bandWidthCharge(demandNote.getBandwidthCharge())
                    .bandWidthDiscount(demandNote.getDiscount())
                    .loopCharge(demandNote.getLocalLoopCharge())
                    .status(DNAdjustStatus.ACTIVE)
                    .totalDue(demandNote.getTotalPayable())
                    .vatRate(demandNote.getVatPercentage())
                    .vat(demandNote.getVAT())
                    .build();

            billService.skipBill(demandNote);
            demandNote.setPaymentStatus(BillDTO.SKIPPED);
            billService.updateBill(demandNote);

        } else {
            dnAdjust = VPNDemandNoteAdjustment.builder()
                    .clientId(demandNote.getClientID())
                    .billId(demandNote.getID())
//                    .connectionType(connectionType)
                    .bandWidthCharge(demandNote.getBandwidthCharge())
                    .bandWidthDiscount(demandNote.getDiscount())
                    .loopCharge(demandNote.getLocalLoopCharge())
                    .status(DNAdjustStatus.PENDING)
                    .build();
        }
        ServiceDAOFactory.getService(VPNDemandNoteAdjustmentService.class).save(dnAdjust);
    }


    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    void saveData(VPNDemandNote demandNote, VPNApplication application, int nextState, LoginDTO loginDTO) throws Exception {
        billService.insertBill(demandNote);
        // TODO change for individual case
        application.setDemandNoteId(demandNote.getID());
        vpnApplicationService.updateApplicaton(application);

        OfficialLetter officialLetter = officialLetterService.getOfficialLetter(VPNDemandNote.class, OfficialLetterType.DEMAND_NOTE, ModuleConstants.Module_ID_VPN,
                application.getApplicationId(), application.getClientId());

        List<RecipientElement> recipientElements = officialLetterService.getDemandNoteSpecificRecipientElement(application.getClientId(), loginDTO.getUserID());
        officialLetterService.saveOfficialLetterTransactionalDefault(officialLetter, recipientElements, loginDTO.getUserID());

        List<VPNApplicationLink> updatedLinks = application.getVpnApplicationLinks()
                .stream()
                .peek(t -> {
                    t.setLinkState(ApplicationState.getApplicationStateByStateId(nextState));
                    t.setDemandNoteId(demandNote.getID());
                    t.setDemandNoteOfficialLetterId(officialLetter.getId());
                })
                .collect(Collectors.toList());

        updatedLinks.forEach(t-> {
            try {
                vpnApplicationService.sendNotification(application, t, loginDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });



        updatedLinks.forEach(t -> globalService.update(t));
        application.setVpnApplicationLinks(updatedLinks);

        // local loop charge discount eligibility
        if (!vpnApplicationService.isLocalEndLoopChargePreviouslyCalculated(application.getVpnApplicationId())) {
            updatedLinks.forEach(t->{
                VPNLoopChargeDiscountEligibility eligibility = new VPNLoopChargeDiscountEligibility();
                eligibility.setVpnApplicationId(application.getVpnApplicationId());
                eligibility.setDemandNoteId(demandNote.getID());
                eligibility.setVpnApplicationLinkId(t.getId());
                if(application.isSkipPayment()) {
                    eligibility.setSkipped(true);
                }
                try {
                    globalService.save(eligibility);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
    }


    private Predicate<Field> getVatCalculableField = (t) -> {
        boolean hasNonNegativeAnnotation = t.getDeclaredAnnotation(NonNegative.class) != null;
        boolean doesNotHaveExcludeFromVATAnnotation = t.getDeclaredAnnotation(ExcludeFromVATCalculation.class) == null;
//        log.info(hasNonNegativeAnnotation + " " + doesNotHaveExcludeFromVATAnnotation);
        return hasNonNegativeAnnotation && doesNotHaveExcludeFromVATAnnotation;
    };

    public double getVatCalculableAmountGeneric(BillDTO demandNote) {
        return Arrays.stream(demandNote.getClass().getDeclaredFields())
                .filter(getVatCalculableField)
                .reduce(0.0, (acc, next) -> {
                    next.setAccessible(true);
                    try {
                        return acc + (Double) next.get(demandNote);
                    } catch (IllegalAccessException e) {
                        log.fatal("Error [ X ]: Calculating failed for: " + next.getName() + "[" + e.getMessage() + "] Returning 0.0 for this field");
                        return 0.0;
                    }
                }, (s1, s2) -> s1 + s2);
    }
    private double getVatCalculableAmount(VPNDemandNote demandNote) {
        return getVatCalculableAmountGeneric(demandNote);
    }

    private void setCommonInvoiceAmountByVATAndDiscount(BillDTO demandNote, double grandTotal, double VAT, double discount) {
        double totalPayable = grandTotal - (((VPNDemandNote)demandNote).getSecurityCharge() > 0 ? discount : 0);
        log.info("Total Payable: " + totalPayable);
        double netPayable = totalPayable + VAT;
        log.info("Net Payable: " + netPayable);
        demandNote.setVAT(VAT);
        demandNote.setDiscount(discount);
        demandNote.setGrandTotal(grandTotal);
        demandNote.setTotalPayable(totalPayable);
        demandNote.setNetPayable(netPayable);
        demandNote.setLastPaymentDate(TimeConverter.getLastDateBeforeNMonth(1));

    }

    private void calculateInvoiceAmount(VPNDemandNote demandNote, List<VPNApplicationLink> links) throws Exception {
        log.info("calculating invoice amount");

        // Recalculation is necessary : explanation : 2 browser widow; different preview; different dn;
        ServiceDAOFactory.getService(DemandNoteAutofillFacade.class).calculateLocalLoopCharge(demandNote, links);
        double vatCalculable = getVatCalculableAmount(demandNote);

        double discount = Math.floor(demandNote.getBandwidthCharge() * demandNote.getDiscountPercentage() / 100.0);
        vatCalculable = vatCalculable - discount; // as vatCalculable holds actual BW cost;
        log.info("vat calculable: " + vatCalculable);
        double vat = Math.ceil(vatCalculable * demandNote.getVatPercentage() / 100.0);
        log.info("VAT: " + vat);
        double grandTotal = vatCalculable + demandNote.getSecurityCharge(); // security also holds actual security we need to subtract
        log.info("Grand Total: " + grandTotal + "( vat calculable=" + vatCalculable + ", security=" + demandNote.getSecurityCharge() + ")");
        //double discount = demandNote.getBandwidthCharge() * demandNote.getDiscountPercentage() / 100.0;
        log.info("discount: " + discount);


        // now update security money
        double updateBWChargeAfterDiscountDeduct = demandNote.getBandwidthCharge() - discount;
//        demandNote.setBandwidthCharge(updateBWChargeAfterDiscountDeduct);
        // if security is involved we will make bw charge actual and security will be security - discount
        // else security = 0 , bw charge = bw - discount,
        // this is done in order to incorporate with accounting.
        // note that db will not always reflect the actual bw ( before discount deduction value will be shown ) where security is involved.

        if(demandNote.getSecurityCharge()>0){
            // this means for PVT Clients
            // so bw charge will only be same when the client is not GOVT i.e. actual security money.

            demandNote.setSecurityCharge(updateBWChargeAfterDiscountDeduct);
        }

        setCommonInvoiceAmountByVATAndDiscount(demandNote, grandTotal, vat, discount);
    }
}
