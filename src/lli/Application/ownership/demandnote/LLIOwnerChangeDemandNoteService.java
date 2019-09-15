package lli.Application.ownership.demandnote;

import annotation.Transactional;
import common.ClientDTO;
import common.ModuleConstants;
import common.bill.BillDTO;
import common.bill.BillService;
import common.repository.AllClientRepository;
import lli.Application.ownership.LLIOwnerChangeService;
import lli.Application.ownership.LLIOwnerShipChangeApplication;
import officialLetter.*;
import user.UserDTO;
import user.UserService;
import util.ServiceDAOFactory;
import util.TimeConverter;
import util.TransactionType;

import java.util.ArrayList;
import java.util.List;

public class LLIOwnerChangeDemandNoteService {
    BillService billService = ServiceDAOFactory.getService(BillService.class);
    OfficialLetterService officialLetterService = ServiceDAOFactory.getService(OfficialLetterService.class);
    UserService userService = ServiceDAOFactory.getService(UserService.class);

    @Transactional(transactionType = TransactionType.READONLY)
    public LLIOwnerChangeDemandNote newDNData(long appId) throws Exception {
        LLIOwnerShipChangeApplication lliOwnerShipChangeApplication = ServiceDAOFactory.getService(LLIOwnerChangeService.class)
                .getApplicationById(appId);
        LLIOwnerChangeDemandNote demandNote = new LLIOwnerChangeDemandNote();
        demandNote.setTransferCharge(0);
        demandNote.setClientID(lliOwnerShipChangeApplication.getDstClient());
        demandNote.setDiscountPercentage(0);
        demandNote.setDescription("Demand Note of application ID " + lliOwnerShipChangeApplication.getId());
        return demandNote;
    }

    @Transactional
    public void handleDemandNoteGenerationRequest(LLIOwnerChangeDemandNote demandNote, long appId, int nextState, long senderId) throws Exception {
        LLIOwnerShipChangeApplication lliOwnerShipChangeApplication = ServiceDAOFactory.getService(LLIOwnerChangeService.class)
                            .getApplicationById(appId);
        if (lliOwnerShipChangeApplication.getSkipPayment() == 1) {
            demandNote.setPaymentStatus(BillDTO.SKIPPED);
            //TODO raihan; do skip related accounting change;
        } else {
            demandNote.setPaymentStatus(BillDTO.UNPAID);
        }

        populateNewConnectionDN(lliOwnerShipChangeApplication, demandNote);
        insertDNBill(demandNote, lliOwnerShipChangeApplication, senderId);
        updateApplicationStatus(lliOwnerShipChangeApplication, nextState, demandNote.getID());
      //  AsyncPdfService.getInstance().accept(demandNote);

    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    public void insertDNBill(BillDTO billDTO, LLIOwnerShipChangeApplication applicationDTO, long senderId) throws Exception {
        billService.insertBill(billDTO);
        saveDemandNoteAsOfficialLetter(applicationDTO, senderId);
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    private void updateApplicationStatus(LLIOwnerShipChangeApplication lliOwnerShipChangeApplication, int nextState, long demandNoteId) throws Exception {

        lliOwnerShipChangeApplication.setState(nextState);
        lliOwnerShipChangeApplication.setDemandNote(demandNoteId);
        ServiceDAOFactory.getService(LLIOwnerChangeService.class).updateApplication(lliOwnerShipChangeApplication);
    }


    private void populateNewConnectionDN(LLIOwnerShipChangeApplication lliOwnerShipChangeApplication, LLIOwnerChangeDemandNote demandNote) throws Exception {


        demandNote.setClientID(lliOwnerShipChangeApplication.getDstClient());
        double vatCalculable = 0;
        double vat = vatCalculable * demandNote.getVatPercentage() / 100.0;

        double grandTotal = vatCalculable;

        double discount = vatCalculable * demandNote.getDiscountPercentage() / 100.0;
        double totalPayable = grandTotal - discount;
        double netPayable = totalPayable + vat;
        demandNote.setVAT(vat);
        demandNote.setDiscount(discount);
        demandNote.setGrandTotal(grandTotal);
        demandNote.setTotalPayable(totalPayable);
        demandNote.setNetPayable(netPayable);
        demandNote.setLastPaymentDate(TimeConverter.getLastDateBeforeNMonth(1));
    }

    private void saveDemandNoteAsOfficialLetter(LLIOwnerShipChangeApplication lliOwnerShipChangeApplication, long senderId) throws Exception {

        // TODO: 2/7/2019 the canonical name might needed later
        OfficialLetter officialLetter = OfficialLetter.builder()
                .officialLetterType(OfficialLetterType.DEMAND_NOTE)
                .className(LLIOwnerShipChangeApplication.class.getCanonicalName())
                .moduleId(ModuleConstants.Module_ID_LLI)
                .applicationId(lliOwnerShipChangeApplication.getId())
                .clientId(lliOwnerShipChangeApplication.getDstClient())
                .build();


        ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(lliOwnerShipChangeApplication.getDstClient());
        UserDTO cdgmUserDTO = userService.getCDGMUserDTO();
        UserDTO loggedInUserDTO = userService.getUserDTOByUserID(senderId);
        // no need to check if dtos are null, should they be null, common layers will throw exception.

        List<RecipientElement> recipientElements = new ArrayList<>();
        recipientElements.add(RecipientElement.getRecipientElementFromClientAndReferType(clientDTO, ReferType.TO));
        recipientElements.add(RecipientElement.getRecipientElementFromUserAndReferType(cdgmUserDTO, ReferType.CC));
        recipientElements.add(RecipientElement.getRecipientElementFromUserAndReferType(loggedInUserDTO, ReferType.CC));

        officialLetterService.saveOfficialLetterTransactionalDefault(officialLetter, recipientElements, senderId);
    }
}
