package entity.facade;

import annotation.Transactional;
import application.ApplicationService;
import com.google.gson.JsonArray;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillDTO;
import common.bill.BillService;
import common.payment.PaymentDTO;
import common.payment.PaymentService;
import common.payment.constants.PaymentConstants;
import common.pdf.AsyncPdfService;
import global.GlobalService;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import nix.document.NIXAdviceNote;
import officialLetter.OfficialLetterService;
import officialLetter.RecipientElement;
import requestMapping.Service;
import util.ServiceDAOFactory;
import util.TimeConverter;
import vpn.adviceNote.DemandNotePayment;
import vpn.adviceNote.VPNLinkAdviceNote;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationLink;

import java.util.List;
import java.util.stream.Collectors;

@Log4j
public class AdviceNoteGenerationFacade {

    @Service private GlobalService globalService;
    @Service private ApplicationService applicationService;
    @Service private OfficialLetterService officialLetterService;
    @Service private BillService billService;
    @Service private PaymentService paymentService;

    @Transactional
    public void generateGenericAdviceNote (int moduleId, LoginDTO loginDTO, Object... objects) throws Exception {
        if(moduleId == ModuleConstants.Module_ID_VPN) {
            if(objects == null ||  objects.length != 2){
                throw new RequestFailureException("Invalid Parameters Found to generate VPN Advice Note; " +
                        "Try invoking method with VPNApplication as 2nd Param. " +
                        "JsonArray as the 3rd Param."
                );
            }
            generateVPNLinkAdviceNote((VPNApplication) objects[0], ( JsonArray )objects[1], loginDTO);
        }
    }

    private void generateVPNLinkAdviceNote(VPNApplication vpnApplication, JsonArray jsonArray, LoginDTO loginDTO) throws Exception {

        List<RecipientElement> recipientElements = officialLetterService.getAllCCAndToList(jsonArray,
                vpnApplication.getVpnApplicationLinks().get(0).getLinkState().getState()); // null check ; TODO
        VPNLinkAdviceNote adviceNote = new VPNLinkAdviceNote(vpnApplication);

        officialLetterService.saveOfficialLetterTransactionalIndividual(adviceNote, recipientElements, loginDTO.getUserID());
        // for link in links : setAdviceNoteId = adviceNote.getID
        // introduce new column in link named advice note id
        // update advice note id here ; set it as adviceNote.getID() // Here it updates in Only RAM; the caller function will update it to db.

        vpnApplication.setVpnApplicationLinks(
            // I know ! This is really bad coding :(
            vpnApplication.getVpnApplicationLinks()
                .stream()
                .peek(t-> t.setAdviceNoteId(adviceNote.getId()))
                .collect(Collectors.toList())

        );
        AsyncPdfService.getInstance().accept(adviceNote);
    }

    public DemandNotePayment getDemandNotePaymentInfoByInvoiceId( Long demandNoteId ){
        if(demandNoteId == null)throw new RequestFailureException("Invalid Invoice ID");
        try {
            BillDTO billDTO = billService.getBillDTOVerified(demandNoteId);
            PaymentDTO paymentDTO = paymentService.getPaymentDTObyID(billDTO.getPaymentID());

            return DemandNotePayment.builder()
                    .id(String.valueOf(billDTO.getID()))
                    .amount(String.valueOf(billDTO.getNetPayable()))
                    .generationdate(TimeConverter.getDateTimeStringByMillisecAndDateFormat(billDTO.getGenerationTime(), "dd/MM/yyyy"))
                    .status(String.valueOf(BillDTO.demandNoteStatusMap.get(billDTO.getPaymentStatus())))
                    .paymentdate(paymentDTO == null ? "N/A" : TimeConverter.getDateTimeStringByMillisecAndDateFormat(paymentDTO.getPaymentTime(), "dd/MM/yyyy"))
                    .medium(String.valueOf(paymentDTO == null ? "N/A" : PaymentConstants.paymentGatewayIDNameMap.get(paymentDTO.getPaymentGatewayType())))
                    .bank(String.valueOf(paymentDTO == null ? "N/A" : paymentDTO.getBankName()))
                    .branch(String.valueOf(paymentDTO == null ? "N/A" : paymentDTO.getBankBranchName()))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DemandNotePayment.builder().build();
    }
}
