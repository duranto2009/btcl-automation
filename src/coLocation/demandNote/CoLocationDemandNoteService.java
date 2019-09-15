package coLocation.demandNote;

import annotation.DAO;
import annotation.Transactional;
import coLocation.CoLocationConstants;
import coLocation.accounts.VariableCost.VariableCostService;
import coLocation.accounts.commonCost.AllVariableUnitCharge;
import coLocation.accounts.commonCost.CommonCostDTO;
import coLocation.accounts.commonCost.CommonCostService;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.application.CoLocationApplicationService;
import coLocation.connection.CoLocationConnectionDTO;
import coLocation.connection.CoLocationConnectionService;
import common.ClientDTO;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillDTO;
import common.bill.BillService;
import common.client.ClientService;
import common.pdf.AsyncPdfService;
import common.repository.AllClientRepository;
import entity.facade.DemandNoteGenerationFacade;
import lli.configuration.LLICostConfigurationService;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import officialLetter.*;
import user.UserDTO;
import user.UserService;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.TimeConverter;
import util.TransactionType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
@Log4j
public class CoLocationDemandNoteService {

    private CoLocationApplicationService applicationService = ServiceDAOFactory.getService(CoLocationApplicationService.class);
    private BillService billService = ServiceDAOFactory.getService(BillService.class);
    private ClientService clientService = ServiceDAOFactory.getService(ClientService.class);
    private OfficialLetterService officialLetterService = ServiceDAOFactory.getService(OfficialLetterService.class);
    private UserService userService = ServiceDAOFactory.getService(UserService.class);
    private CommonCostService commonCostService=ServiceDAOFactory.getService(CommonCostService.class);
    private VariableCostService variableCostService=ServiceDAOFactory.getService(VariableCostService.class);
    private LLICostConfigurationService lliCostConfigurationService=ServiceDAOFactory.getService(LLICostConfigurationService.class);

    @DAO private CoLocationDemandNoteAdjustmentDAO adjustmentDAO;

    @Transactional
    public void saveDemandNoteAdjustment(CoLocationConnectionDTO newConnection, CoLocationConnectionDTO oldConnection) throws Exception {
        double adjustedAmount = CoLocationBillUtilityService.calculateAdjustmentForYearlyBill(oldConnection, newConnection);
        CoLocationDemandNoteAdjustment adjustment = CoLocationDemandNoteAdjustment.builder()
                .adjustmentAmount(adjustedAmount)
                .connection_history_id(newConnection.getHistoryID())
                .connection_id(newConnection.getID())
                .build();
        adjustmentDAO.insert(adjustment);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public double getTotalAdjustmentAmountByConnectionIds(List<Long> connectionIds) throws Exception {
        return adjustmentDAO.getCoLocationAdjustmentByConnectionIds(connectionIds)
                .stream()
                .mapToDouble(CoLocationDemandNoteAdjustment::getAdjustmentAmount)
                .map(Math::abs)
                .sum();
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
     void insertDNBill(BillDTO billDTO, CoLocationApplicationDTO applicationDTO, long senderId) throws Exception {
        billService.insertBill(billDTO);
        saveDemandNoteAsOfficialLetter(applicationDTO, senderId);
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    private void updateApplicationStatus(CoLocationApplicationDTO coLocationApplicationDTO, int nextState, long demandNoteId,LoginDTO loginDTO) throws Exception {

        coLocationApplicationDTO.setState(nextState);
        coLocationApplicationDTO.setDemandNoteID(demandNoteId);
        applicationService.updateApplicaton(coLocationApplicationDTO,loginDTO);
    }


     @Transactional
     void handleDemandNoteGenerationRequest(CoLocationDemandNote demandNote, long appId, int nextState, long senderId, LoginDTO loginDTO) throws Exception {
         CoLocationApplicationDTO coLocationApplicationDTO = applicationService.getColocationApplication(appId);
         DemandNoteGenerationFacade.isDemandNoteAlreadyGenerated(coLocationApplicationDTO.getDemandNoteID(), true);
         //null checking not necessary as common layer will throw exception if not found.
         if (coLocationApplicationDTO.getSkipPayment() == 1) {

             billService.skipBill(demandNote);
             demandNote.setPaymentStatus(BillDTO.SKIPPED);
         }

        populateNewConnectionDN(coLocationApplicationDTO, demandNote);
        insertDNBill(demandNote, coLocationApplicationDTO, senderId);
        updateApplicationStatus(coLocationApplicationDTO, nextState, demandNote.getID(),loginDTO);
        AsyncPdfService.getInstance().accept(demandNote);

    }

    @Transactional(transactionType = TransactionType.READONLY)
     CoLocationDemandNote newConnectionDNData(long appId) throws Exception {
        CoLocationApplicationDTO coLocationApplicationDTO = applicationService.getColocationApplication(appId);


        // LLIFixedCostConfigurationDTO lliFixedCostConfigurationDTO = lliCostConfigurationService.getCurrentActiveLLI_FixedCostConfigurationDTO();
        CoLocationDemandNote demandNote = new CoLocationDemandNote();
        demandNote.setClientID(coLocationApplicationDTO.getClientID());

        CommonCostDTO coLocationCommonCostVat = commonCostService.getAllFixedCostOfApplication(CoLocationConstants.MAXIMUM_VAT_PERCENTAGE_TYPE);
        demandNote.setVatPercentage(coLocationCommonCostVat.getPrice());

        demandNote.setDescription("Demand Note of application ID " + coLocationApplicationDTO.getApplicationID());



        AllVariableUnitCharge newCharge = variableCostService.getAllVariableChargeByCoLocationApplication(coLocationApplicationDTO);
        double monthFactor = 12.;

        if(coLocationApplicationDTO.getConnectionId() != 0L) {
            CoLocationConnectionDTO connectionDTO = ServiceDAOFactory.getService(CoLocationConnectionService.class).getColocationConnection(coLocationApplicationDTO.getConnectionId());
            AllVariableUnitCharge oldCharge = variableCostService.getAllVariableUnitChargeByCoLocation(connectionDTO);

            monthFactor = CoLocationBillUtilityService.getMonthFactorByConnectionWithRespectToNextBillDate(connectionDTO);
            double additionalPowerCharge = newCharge.getPowerCharge().getPrice() - oldCharge.getPowerCharge().getPrice();
            double additionalFiberCharge = newCharge.getFiberCharge().getPrice() - oldCharge.getFiberCharge().getPrice();
            double additionalRackCharge = newCharge.getRackCharge().getPrice() - oldCharge.getRackCharge().getPrice();
            double additionalFloorSpaceCharge = newCharge.getFloorSpaceCharge().getPrice() - oldCharge.getFloorSpaceCharge().getPrice();
            if(newCharge.getTotalCost() < oldCharge.getTotalCost()) {
                log.info("OLD Settings Cost: " + oldCharge.toString());
                log.info("NEW Settings Cost: " + newCharge.toString());
                throw new RequestFailureException("No Demand Note For Downgrade Request Check Configuration Cost");

            }
            demandNote.setPowerCost(BillService.round(additionalPowerCharge * monthFactor, 2));
            demandNote.setOfcCost(BillService.round(additionalFiberCharge * monthFactor, 2 ));
            demandNote.setRackCost(BillService.round(additionalRackCharge * monthFactor, 2));
            demandNote.setFloorSpaceCost(BillService.round(additionalFloorSpaceCharge * monthFactor, 2));

        }else {
            demandNote.setPowerCost(BillService.round(newCharge.getPowerCharge().getPrice()*monthFactor, 2));
            demandNote.setOfcCost(BillService.round(newCharge.getFiberCharge().getPrice()*monthFactor, 2));
            demandNote.setRackCost(BillService.round(newCharge.getRackCharge().getPrice()*monthFactor, 2));
            demandNote.setFloorSpaceCost(BillService.round(newCharge.getFloorSpaceCharge().getPrice() * monthFactor, 2 ));
        }

        return demandNote;
    }

    



    private void populateNewConnectionDN(CoLocationApplicationDTO coLocationApplicationDTO, CoLocationDemandNote demandNote) {

        demandNote.setClientID(coLocationApplicationDTO.getClientID());
        double vatCalculable;
        vatCalculable = ServiceDAOFactory.getService(DemandNoteGenerationFacade.class).getVatCalculableAmountGeneric(demandNote);
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

    private void saveDemandNoteAsOfficialLetter(CoLocationApplicationDTO coLocationApplicationDTO, long senderId) throws Exception {

        OfficialLetter officialLetter = OfficialLetter.builder()
                .officialLetterType(OfficialLetterType.DEMAND_NOTE)
                .className(CoLocationDemandNote.class.getCanonicalName())
                .moduleId(ModuleConstants.Module_ID_COLOCATION)
                .applicationId(coLocationApplicationDTO.getApplicationID())
                .clientId(coLocationApplicationDTO.getClientID())
                .build();


        ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(coLocationApplicationDTO.getClientID());
        UserDTO cdgmUserDTO = userService.getCDGMUserDTO();
        UserDTO loggedInUserDTO = userService.getUserDTOByUserID(senderId);
        // no need to check if dtos are null, should they be null, common layers will throw exception.

        List<RecipientElement> recipientElements = new ArrayList<>();
        recipientElements.add(RecipientElement.getRecipientElementFromClientAndReferType(clientDTO, ReferType.TO));
        recipientElements.add(RecipientElement.getRecipientElementFromUserAndReferType(cdgmUserDTO, ReferType.CC));
        recipientElements.add(RecipientElement.getRecipientElementFromUserAndReferType(loggedInUserDTO, ReferType.CC));

        officialLetterService.saveOfficialLetterTransactionalDefault(officialLetter, recipientElements, senderId);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public CoLocationYearlyDemandNote getYearlyDemandNoteByParentDemandNoteId(long demandNoteID) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(CoLocationYearlyDemandNote.class, " WHERE parent_demand_note_id = " + demandNoteID)
                .stream()
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No Yearly Bill Found with coLocation demand note id " + demandNoteID));
    }
}