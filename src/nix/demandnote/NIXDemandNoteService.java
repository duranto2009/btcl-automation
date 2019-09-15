package nix.demandnote;

import annotation.Transactional;
import client.RegistrantTypeConstants;
import client.classification.ClientClassificationService;
import client.classification.RegistrantTypeDTO;
import common.*;
import common.bill.BillDTO;
import common.bill.BillService;
import common.pdf.AsyncPdfService;
import common.repository.AllClientRepository;
import entity.facade.DemandNoteAutofillFacade;
import entity.facade.DemandNoteGenerationFacade;
import global.GlobalService;
import lli.configuration.LLICostConfigurationService;
import lli.configuration.LLIFixedCostConfigurationDTO;
import lli.demandNote.adjustment.DNAdjustStatus;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import nix.application.NIXApplication;
import nix.application.NIXApplicationService;
import nix.application.close.NIXCloseApplication;
import nix.application.close.NIXCloseApplicationService;
import nix.application.downgrade.NIXDowngradeApplication;
import nix.application.downgrade.NIXDowngradeApplicationService;
import nix.application.localloop.NIXApplicationLocalLoop;
import nix.application.office.NIXApplicationOffice;
import nix.application.upgrade.NIXUpgradeApplication;
import nix.application.upgrade.NIXUpgradeApplicationService;
import nix.constants.NIXConstants;
import nix.ifr.NIXIFR;
import nix.ifr.NIXIFRConditionBuilder;
import nix.nixportconfig.NIXPortConfig;
import nix.nixportconfig.NIXPortConfigService;
import nix.revise.NIXReviseDTO;
import nix.revise.NIXReviseService;
import officialLetter.*;
import user.UserDTO;
import user.UserService;
import util.ServiceDAOFactory;
import util.TimeConverter;
import util.TransactionType;
import vpn.client.ClientDetailsDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Log4j
public class NIXDemandNoteService {

    //TODO raihan; remove duplicate demand note code in generation time;
    
    NIXApplicationService nixApplicationService = ServiceDAOFactory.getService(NIXApplicationService.class);
    BillService billService = ServiceDAOFactory.getService(BillService.class);
    OfficialLetterService officialLetterService = ServiceDAOFactory.getService(OfficialLetterService.class);
    UserService userService = ServiceDAOFactory.getService(UserService.class);
    NIXReviseService reviseService = ServiceDAOFactory.getService(NIXReviseService.class);


    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    private void updateApplicationStatus(NIXApplication application, int nextState, long demandNoteId) throws Exception {

        application.setState(nextState);
        application.setDemandNote(demandNoteId);
        nixApplicationService.updateApplicaton(application);
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    private void updateApplicationStatus(NIXReviseDTO application, int nextState, long demandNoteId) throws Exception {

        application.setState(nextState);
        application.setDemandNoteID(demandNoteId);
        reviseService.updateApplicaton(application);
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    public void insertDNBill(BillDTO billDTO, long appId, long clientId, long senderId) throws Exception {
        billService.insertBill(billDTO);
        saveDemandNoteAsOfficialLetter(appId, clientId, senderId);
    }



    private double getLocalLoopCharge(NIXApplication nixApplication) {
        List<NIXApplicationLocalLoop> listOfLocalLoop = nixApplication.getNixApplicationOffices()
                .stream()
                .map(NIXApplicationOffice::getLoops)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return listOfLocalLoop.stream()
                .mapToDouble(nixApplicationLocalLoop -> {
                    try {
                        return DemandNoteAutofillFacade.calculateLocalLoopCost(nixApplicationLocalLoop);
                    } catch (Exception e) {
                        log.fatal(e.getMessage());
                        log.fatal("Returning 0");
                        return 0;
                    }
                })
                .sum();
    }

    private void populateNewConnectionDN(long clientId, NIXDemandNote NIXDemandNote) {

        NIXDemandNote.setClientID(clientId);

        double vatCalculable;
        vatCalculable = NIXDemandNote.getRegistrationFee()
                + NIXDemandNote.getNixPortCharge()
                + NIXDemandNote.getLocalLoopCharge()
                + NIXDemandNote.getNixPortDowngradeCharge()
                + NIXDemandNote.getNixPortUpgradeCharge()
                + NIXDemandNote.getReconnectCharge()
                + NIXDemandNote.getClosingCharge()
                + NIXDemandNote.getAdvanceAdjustment();

        double discount = Math.floor(NIXDemandNote.getNixPortCharge() * NIXDemandNote.getDiscountPercentage() / 100.0);
        vatCalculable = vatCalculable -discount;
        double vat = Math.ceil(vatCalculable * NIXDemandNote.getVatPercentage() / 100.0); // as vat calculable holds actual port charge, we need to deduct
        // discount = port charge * disc % in order to calculate vat properly.
        log.info("VAT Calculable: " + vatCalculable + ", discount: " + discount + ", VAT: " + vat);
        double grandTotal = vatCalculable + NIXDemandNote.getSecurityMoney();
        // security also holds actual security
        double updatedPortCharge = NIXDemandNote.getNixPortCharge() - discount;

        // if security > 0 we will make port charge = actual port charge and security = actual security - discount
        // else  port charge = actual port charge - discount and security = actual security,
        // this is done in order to incorporate with accounting.
        // note that db will not always reflect the actual port charge ( before discount deduction value will be shown ) where security > 0

        if(NIXDemandNote.getSecurityMoney()>0){
            // this means for PVT clients
            // so bw charge will only be same when the client is not GOVT i.e. actual security money.

            NIXDemandNote.setSecurityMoney(updatedPortCharge);
        }


        double totalPayable = grandTotal - (NIXDemandNote.getSecurityMoney() > 0 ? discount : 0  );
        double netPayable = totalPayable + vat;
        NIXDemandNote.setVAT(vat);
        NIXDemandNote.setDiscount(discount);
        NIXDemandNote.setGrandTotal(grandTotal);
        NIXDemandNote.setTotalPayable(totalPayable);
        NIXDemandNote.setNetPayable(netPayable);
        NIXDemandNote.setLastPaymentDate(TimeConverter.getLastDateBeforeNMonth(1));
    }

    private void saveDemandNoteAsOfficialLetter(long appId, long clientId, long senderId) throws Exception {
        OfficialLetter officialLetter = OfficialLetter.builder()
                .officialLetterType(OfficialLetterType.DEMAND_NOTE)
                .className(NIXDemandNote.class.getCanonicalName())
                .moduleId(ModuleConstants.Module_ID_NIX)
                .applicationId(appId)
                .clientId(clientId)
                .build();

        ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(clientId);
        UserDTO cdgmUserDTO = userService.getCDGMUserDTO();
        UserDTO loggedInUserDTO = userService.getUserDTOByUserID(senderId);
        List<RecipientElement> recipientElements = new ArrayList<>();
        recipientElements.add(RecipientElement.getRecipientElementFromClientAndReferType(clientDTO, ReferType.TO));
        recipientElements.add(RecipientElement.getRecipientElementFromUserAndReferType(cdgmUserDTO, ReferType.CC));
        recipientElements.add(RecipientElement.getRecipientElementFromUserAndReferType(loggedInUserDTO, ReferType.CC));

        officialLetterService.saveOfficialLetterTransactionalDefault(officialLetter, recipientElements, senderId);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public NIXDemandNote getDNAutofilled(long appId, ApplicationGroupType appGroupType) throws Exception{
        if(appGroupType == ApplicationGroupType.NIX_CONNECTION_APPLICATION){
            return getNIXConnectionDemandNoteAutofilled(appId, appGroupType);
        }else if(appGroupType == ApplicationGroupType.NIX_CLIENT_APPLICATION) {
            return getNIXClientDemandNoteAutofilled(appId, appGroupType);
        }
        throw new RequestFailureException("Invalid Application Group Type: " + appGroupType.name());
    }

    private NIXDemandNote getNIXClientDemandNoteAutofilled(long appId, ApplicationGroupType groupType)throws Exception {
        NIXReviseDTO reviseDTO = reviseService.getappById(appId);

        NIXDemandNote NIXDemandNote = new NIXDemandNote();

        NIXDemandNote.setApplicationGroupType(groupType);
        NIXDemandNote.setClientID(reviseDTO.getClientID());

        NIXOTC nixotc = ServiceDAOFactory.getService(UniversalDTOService.class).get(NIXOTC.class);

        NIXDemandNote.setVatPercentage(nixotc.getMaxVATPercentage());
        NIXDemandNote.setDescription("Demand Note of application ID " + reviseDTO.getId());
        NIXDemandNote.setReconnectCharge(nixotc.getReconnectCharge());
        return NIXDemandNote;
    }

    private void getCommonChargesOfDN(NIXDemandNote NIXDemandNote, NIXApplication nixApplication, NIXPortConfig nixPortConfig, RegistrantTypeDTO registrantTypeDTO) throws Exception {
        int portCount = ServiceDAOFactory.getService(GlobalService.class).getAllObjectListByCondition(
                NIXIFR.class, new NIXIFRConditionBuilder()
                        .Where()
                        .applicationEquals(nixApplication.getId())
                        .selectedEquals(1)
                        .getCondition()
        ).size();
        double totalPortCharge = nixPortConfig.getPortCharge() * portCount;
        NIXDemandNote.setNixPortCharge(totalPortCharge);

        if(nixApplication.getLoopProvider() != NIXConstants.LOOP_PROVIDER_CLIENT
                &&nixApplication.getType()==NIXConstants.NEW_CONNECTION_APPLICATION
        ){
            NIXDemandNote.setLocalLoopCharge(getLocalLoopCharge(nixApplication));
        }

        if(registrantTypeDTO.getRegTypeId() != RegistrantTypeConstants.GOVT){
            NIXDemandNote.setSecurityMoney(totalPortCharge);
        }
        if(nixApplication.getType()==NIXConstants.NEW_CONNECTION_APPLICATION){

            NIXDemandNote.setRegistrationFee(nixPortConfig.getRegistrationCharge());
        }
    }

    private NIXDemandNote getNIXConnectionDemandNoteAutofilled(long appId, ApplicationGroupType groupType) throws Exception {
        NIXApplication nixApplication = nixApplicationService.getApplicationById(appId);

        NIXDemandNote NIXDemandNote = new NIXDemandNote();
        NIXDemandNote.setApplicationGroupType(groupType);
        NIXDemandNote.setClientID(nixApplication.getClient());

        ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(NIXDemandNote.getClientID(), ModuleConstants.Module_ID_NIX);
        RegistrantTypeDTO registrantType = ServiceDAOFactory.getService(ClientClassificationService.class).getClientRegistrantTypeByRegistrantTypeId(clientDetailsDTO.getRegistrantType());


        NIXPortConfig nixPortConfig;
        if(nixApplication.getType() == NIXConstants.NEW_CONNECTION_APPLICATION){
            nixPortConfig = ServiceDAOFactory.getService(NIXPortConfigService.class).getPortConfigByPortType(nixApplication.getPortType());
            getCommonChargesOfDN(NIXDemandNote, nixApplication, nixPortConfig, registrantType);
        }
        else if(nixApplication.getType() == NIXConstants.NIX_UPGRADE_APPLICATION){
            NIXUpgradeApplication nixUpgradeApplication = ServiceDAOFactory.getService(NIXUpgradeApplicationService.class)
                    .getApplicationByParent(nixApplication.getId());
            nixPortConfig = ServiceDAOFactory.getService(NIXPortConfigService.class).getPortConfigByPortType(nixUpgradeApplication.getNewPortType());
            NIXDemandNote.setNixPortUpgradeCharge(nixPortConfig.getUpgradeCharge());
            getCommonChargesOfDN(NIXDemandNote, nixApplication, nixPortConfig, registrantType);
        }
        else if(nixApplication.getType() == NIXConstants.NIX_DOWNGRADE_APPLICATION){
            NIXDowngradeApplication nixDowngradeApplication = ServiceDAOFactory.getService(NIXDowngradeApplicationService.class)
                    .getApplicationByParent(nixApplication.getId());
            nixPortConfig = ServiceDAOFactory.getService(NIXPortConfigService.class).getPortConfigByPortType(nixDowngradeApplication.getNewPortType());
            double totalPortCharge = nixPortConfig.getPortCharge() * nixApplication.getPortCount();
            NIXDemandNote.setNixPortDowngradeCharge(nixPortConfig.getDowngradeCharge());
            NIXDemandNote.setNixPortCharge(totalPortCharge);
        }

        else if(nixApplication.getType() == NIXConstants.NIX_CLOSE_APPLICATION){
            NIXCloseApplication nixCloseApplication = ServiceDAOFactory.getService(NIXCloseApplicationService.class).getApplicationByParent(nixApplication.getId());
            nixPortConfig = ServiceDAOFactory.getService(NIXPortConfigService.class).getPortConfigByPortType(nixCloseApplication.getPortType());
            NIXDemandNote.setClosingCharge(nixPortConfig.getCloseCharge());

        }
        LLIFixedCostConfigurationDTO lliFixedCostConfigurationDTO = ServiceDAOFactory.getService(LLICostConfigurationService.class).getCurrentActiveLLI_FixedCostConfigurationDTO();
        NIXDemandNote.setVatPercentage(lliFixedCostConfigurationDTO.getMaximumVatPercentage());
        NIXDemandNote.setDescription("Demand Note of application ID " + nixApplication.getId());
        return NIXDemandNote;
    }

    public void generateDemandNote(NIXDemandNote nixDemandNote, long appId, int next, ApplicationGroupType applicationGroupType, LoginDTO loginDTO) throws Exception {

        nixDemandNote.setApplicationGroupType(applicationGroupType);

        if(applicationGroupType == ApplicationGroupType.NIX_CONNECTION_APPLICATION){
            generateNIXConnectionDemandNote(nixDemandNote, appId, next, loginDTO);
        }else if(applicationGroupType == ApplicationGroupType.NIX_CLIENT_APPLICATION) {
            generateNIXClientDemandNote(nixDemandNote, appId, next, loginDTO);
        }
    }

    @Transactional
    void generateNIXClientDemandNote(NIXDemandNote demandNote, long appId, int next, LoginDTO loginDTO) throws Exception {

        NIXReviseDTO application = reviseService.getappById(appId);
        DemandNoteGenerationFacade.isDemandNoteAlreadyGenerated(application.getDemandNoteID(), false);
        long clientId = application.getClientID();
        populateNewConnectionDN(clientId, demandNote);
        insertDNBill(demandNote, appId, clientId, loginDTO.getUserID());
        updateApplicationStatus(application, next, demandNote.getID());
        ServiceDAOFactory.getService(NIXReviseService.class).sendNotification(application,next,loginDTO);
        AsyncPdfService.getInstance().accept( demandNote );

    }
    private void AdjustDemandNoteAdjustment(BillDTO demandNote, long appId)throws Exception{

        NIXApplication nixApplication = null;
        try {
            nixApplication = ServiceDAOFactory.getService(NIXApplicationService.class).getApplicationById(appId);
        } catch (Exception e) {
        }

        if(nixApplication == null)
            return;

        //int connectionType = lliApplication.get();

     /*   if(connectionType == LLIConnectionConstants.CONNECTION_TYPE_REGULAR_LONG)
            connectionType = LLIConnectionConstants.CONNECTION_TYPE_REGULAR;

        if(! (  connectionType == LLIConnectionConstants.CONNECTION_TYPE_CACHE
                || connectionType == LLIConnectionConstants.CONNECTION_TYPE_REGULAR))
            return;*/

        double localLoopCharge = 0;
        long billId = demandNote.getID();

       /* if(demandNote instanceof LLINewConnectionDemandNote)
        {
            bwCharge = ((LLINewConnectionDemandNote)demandNote).getBwMRC();
            localLoopCharge = ((LLINewConnectionDemandNote)demandNote).getLocalLoopCharge();
        }
        else if(demandNote instanceof LLISingleConnectionCommonDemandNote)
        {
            bwCharge = ((LLISingleConnectionCommonDemandNote)demandNote).getBandwidthCharge();
            localLoopCharge = ((LLISingleConnectionCommonDemandNote)demandNote).getCoreCharge();
        }*/


        //insert
        NIXDemandNoteAdjustment dnAdjust = null;

        if(nixApplication.getSkipPayment() == 1)
        {
            dnAdjust = NIXDemandNoteAdjustment.builder()
                    .clientId(demandNote.getClientID())
                    .billId(billId)
                    //.connectionType(connectionType)
                    .loopCharge(localLoopCharge)
                    .status(DNAdjustStatus.ACTIVE)
                    .totalDue(demandNote.getTotalPayable())
                    .vatRate(demandNote.getVatPercentage())
                    .vat(demandNote.getVAT())
                    .build();
            billService.skipBill(demandNote);
            demandNote.setPaymentStatus(BillDTO.SKIPPED);
            billService.updateBill(demandNote);
        }
        else
        {
            dnAdjust = NIXDemandNoteAdjustment.builder()
                    .clientId(demandNote.getClientID())
                    .billId(billId)
                   // .connectionType(connectionType)
                    //.bandWidthCharge(bwCharge)
                    //.bandWidthDiscount(demandNote.getDiscount())
                    .loopCharge(localLoopCharge)
                    .status(DNAdjustStatus.PENDING)
                    .build();
        }
        ServiceDAOFactory.getService(NIXDemandNoteAdjustmentService.class).save(dnAdjust);
    }
    @Transactional
    void generateNIXConnectionDemandNote(NIXDemandNote demandNote, long appId, int nextState, LoginDTO loginDTO) throws Exception {
        NIXApplication application = nixApplicationService.getApplicationById(appId);
        DemandNoteGenerationFacade.isDemandNoteAlreadyGenerated(application.getDemandNote(), false);

        populateNewConnectionDN(application.getClient(), demandNote);
        insertDNBill(demandNote,  application.getId(),application.getClient(), loginDTO.getUserID());
        updateApplicationStatus(application, nextState, demandNote.getID());
        ServiceDAOFactory.getService(NIXApplicationService.class).sendNotification(application,nextState,loginDTO);
        AsyncPdfService.getInstance().accept(demandNote);
        AdjustDemandNoteAdjustment(demandNote,application.getId());
    }
}