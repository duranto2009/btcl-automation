package lli.demandNote;

import annotation.DAO;
import annotation.Transactional;
import api.ClientAPI;
import api.FileAPI;
import client.ClientTypeService;
import client.RegistrantTypeConstants;
import client.classification.ClientClassificationService;
import client.classification.RegistrantTypeDTO;
import com.google.gson.Gson;
import common.ClientDTO;
import common.ModuleConstants;
import common.RequestFailureException;
import common.UniversalDTOService;
import common.bill.BillDTO;
import common.bill.BillService;
import common.client.ClientService;
import common.pdf.AsyncPdfService;
import common.pdf.PdfService;
import common.repository.AllClientRepository;
import costConfig.CostConfigService;
import costConfig.TableDTO;
import entity.facade.DemandNoteAutofillFacade;
import entity.facade.DemandNoteGenerationFacade;
import file.FileTypeConstants;
import flow.entity.FlowState;
import flow.repository.FlowRepository;
import global.GlobalService;
import inventory.InventoryConstants;
import inventory.InventoryItem;
import inventory.InventoryService;
import lli.Application.AdditionalConnectionAddress.LLIAdditionalConnectionAddressApplication;
import lli.Application.AdditionalIP.LLIAdditionalIP;
import lli.Application.AdditionalIP.LLIAdditionalIPService;
import lli.Application.AdditionalPort.AdditionalPort;
import lli.Application.AdditionalPort.LLIAdditionalPortService;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRService;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.FlowConnectionManager.LLIFlowConnectionService;
import lli.Application.IFR.IFR;
import lli.Application.IFR.IFRConditionBuilder;
import lli.Application.IFR.IFRService;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopService;
import lli.Application.NewLocalLoop.NewLocalLoop;
import lli.Application.NewLocalLoop.NewLocalLoopService;
import lli.Application.Office.Office;
import lli.Application.Office.OfficeService;
import lli.Application.ReviseClient.ReviseDTO;
import lli.Application.ReviseClient.ReviseService;
import lli.Application.ShiftAddress.LLIShiftAddressApplication;
import lli.Application.ShiftPop.LLIShiftPopApplication;
import lli.Application.ownership.LLIOnProcessConnection;
import lli.Application.ownership.LLIOnProcessConnectionService;
import lli.Application.ownership.LLIOwnerChangeService;
import lli.Application.ownership.LLIOwnerShipChangeApplication;
import lli.*;
import lli.configuration.LLICostConfigurationService;
import lli.configuration.LLIFixedCostConfigurationDTO;
import lli.configuration.ofc.cost.OfcInstallationCostDTO;
import lli.configuration.ofc.cost.OfcInstallationCostService;
import lli.connection.LLIConnectionConstants;
import lli.demandNote.adjustment.DNAdjustStatus;
import lli.demandNote.adjustment.LLIDemandNoteAdjustment;
import lli.demandNote.adjustment.LLIDemandNoteAdjustmentService;
import lli.longTerm.LLILongTermBenefitService;
import login.LoginDTO;
import officialLetter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import requestMapping.Service;
import user.UserDTO;
import user.UserService;
import util.*;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;
import vpn.ofcinstallation.DistrictOfcInstallationService;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/*
import lli.Application.ChangeOwnership.LLIChangeOwnershipApplication;
*/

public class LLIDemandNoteService {

    private static final Logger logger = LoggerFactory.getLogger(LLIDemandNoteService.class);
    private static final int NUMBER_OF_FIRST_X_IP = 4;

    //TODO: move this config to DB
    // if a private client has >= premium margin, it is a Premium Client
    @Service private LLIApplicationService lliApplicationService;
    @Service private BillService billService;
    @Service private LLICostConfigurationService lliCostConfigurationService;
    @Service private OfcInstallationCostService ofcInstallationCostService;
    @Service private LLIConnectionService lliConnectionService;
    @Service private InventoryService inventoryService;
    @Service private PdfService pdfService;
    @Service private LLINotificationService lliNotificationService;
    @Service private LLIDemandNoteAdjustmentService lliDemandNoteAdjustmentService;
    @Service private DistrictOfcInstallationService districtOfcInstallationService;
    @Service private UniversalDTOService universalDTOService;
    @DAO private LLILongTermContractDAO lliLongTermContractDAO;


    LocalLoopService localLoopService = ServiceDAOFactory.getService(LocalLoopService.class);
    LLIFlowConnectionService lliFlowConnectionService = ServiceDAOFactory.getService(LLIFlowConnectionService.class);
    ReviseService reviseService = ServiceDAOFactory.getService(ReviseService.class);
    ClientService clientService = ServiceDAOFactory.getService(ClientService.class);
    OfficialLetterService officialLetterService = ServiceDAOFactory.getService(OfficialLetterService.class);
    UserService userService = ServiceDAOFactory.getService(UserService.class);
    LLILongTermBenefitService lliLongTermBenefitService = ServiceDAOFactory.getService(LLILongTermBenefitService.class);
    IFRService ifrService = ServiceDAOFactory.getService(IFRService.class);
    EFRService efrService = ServiceDAOFactory.getService(EFRService.class);
    LLIAdditionalPortService lliAdditionalPortService = ServiceDAOFactory.getService(LLIAdditionalPortService.class);
    LLIAdditionalIPService lliAdditionalIPService = ServiceDAOFactory.getService(LLIAdditionalIPService.class);

    /***
     * insert Demand Notes
     */

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    void insertDemandNote(BillDTO billDTO, long appId, LoginDTO loginDTO) throws Exception {
        billService.insertBill(billDTO);
        long demandNoteId = billDTO.getID();
        reviseService.setDemandNote(appId, demandNoteId);


        saveDemandNoteAsOfficialLetter(reviseService.getappById(appId), loginDTO, ReviseService.class.getCanonicalName());
    }


    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    void insertDemandNote(BillDTO billDTO) throws Exception {
        billService.insertBill(billDTO);
    }

    @Transactional
    long insertNewConnectionDN(LLINewConnectionDemandNote lliNewConnectionDN, long appId, long state, LoginDTO loginDTO) throws Exception {
        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appId);

        populateLLINewConnectionDemandNote(lliApplication, lliNewConnectionDN);
        DemandNoteGenerationFacade.isDemandNoteAlreadyGenerated(lliApplication.getDemandNoteID(), true);

        insertDemandNote(lliNewConnectionDN);
        updateLLIApplication(lliApplication, lliNewConnectionDN,(int)state, loginDTO);
        saveDemandNoteAsOfficialLetter(lliApplication, loginDTO, LLIApplicationService.class.getCanonicalName());
        AdjustDemandNoteAdjustment(lliNewConnectionDN, appId);

        lliApplicationService.sendNotification(lliApplication, (int)state, loginDTO);
        AsyncPdfService.getInstance().accept( lliNewConnectionDN);
        return lliNewConnectionDN.getID();
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    void updateLLIApplication(LLIApplication lliApplication, BillDTO billDTO, int state, LoginDTO loginDTO) throws Exception {
        if (!lliApplication.isDemandNoteNeeded()) {
            throw new RequestFailureException(
                    "No demand note is needed for application with application ID " + lliApplication.getApplicationID());
        }
        if (lliApplication.getDemandNoteID() != null) {
            throw new RequestFailureException(
                    "Demand note has already been created for application with application ID " + lliApplication.getApplicationID());
        }
        lliApplication.setState( state);
        lliApplication.setBillID(billDTO.getID());
        lliApplication.setDemandNoteID(billDTO.getID());
        lliApplication.setStatus(LLIConnectionConstants.STATUS_DEMAND_NOTE_GENERATED);

        //manual manipulation for downgrade
        if(lliApplication.getApplicationType()==LLIConnectionConstants.DOWNGRADE_BANDWIDTH
                && lliApplication.getSkipPayment()==0
        ){
            lliApplication.setState(LLIConnectionConstants.WITHOUT_LOOP_DEMAND_NOTE);
        }

        lliApplicationService.updateApplicaton(lliApplication);

    }

    @Transactional
    long insertReconnectConnectionDN(LLIReconnectConnectionDemandNote lliReconnectConnectionDN, long appId, LoginDTO loginDTO) throws Exception {
        ReviseDTO reviseDTO = reviseService.getappById(appId);
        validateApplication(reviseDTO, LLIConnectionConstants.RECONNECT);
        populateLLIReconnectConnectionDemandNote(reviseDTO, lliReconnectConnectionDN);
        long demandNoteId = lliReconnectConnectionDN.getID();
        if (demandNoteId <= 0) {
            billService.insertBill(lliReconnectConnectionDN);
            demandNoteId = lliReconnectConnectionDN.getID();
            reviseService.setReconnectionDemandNote(appId, demandNoteId);
        }
        AsyncPdfService.getInstance().accept( lliReconnectConnectionDN);
        saveDemandNoteAsOfficialLetter(reviseService.getappById(appId), loginDTO, ReviseService.class.getCanonicalName());
        return demandNoteId;
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    void saveDataInDb(LLIOwnerShipChangeDemandNote lliOwnershipChangeDN,
                      LLIOwnerShipChangeApplication lliApplication,
                      int nextState,LoginDTO loginDTO) throws Exception {
        billService.insertBill(lliOwnershipChangeDN);

        if (lliApplication.getDemandNote() != 0L) {
            throw new RequestFailureException(
                    "Demand note has already been created for application with application ID " + lliApplication.getId());
        }
        lliApplication.setDemandNote(lliOwnershipChangeDN.getID());
        lliApplication.setStatus(LLIConnectionConstants.STATUS_DEMAND_NOTE_GENERATED);
        lliApplication.setState(nextState);
        ServiceDAOFactory.getService(LLIOwnerChangeService.class).updateApplication(lliApplication);



        //lliApplicationService.setDemandNoteId(lliOwnershipChangeDN.getID(), appId);
        officialLetterService.saveDemandNoteAsOfficialLetter(LLIOwnerShipChangeDemandNote.class,
                ModuleConstants.Module_ID_LLI,
                lliApplication.getId(),
                lliApplication.getSrcClient(),
                loginDTO.getUserID());
    }
    @Transactional
    long insertOwnershipChangeDN(LLIOwnerShipChangeDemandNote lliOwnershipChangeDN,  long appId,int nextState, LoginDTO loginDTO) throws Exception {
        LLIOwnerShipChangeApplication application = populateLLIOwnershipChangeDemandNote(appId, lliOwnershipChangeDN);
        saveDataInDb(lliOwnershipChangeDN, application,nextState,  loginDTO);
        AsyncPdfService.getInstance().accept( lliOwnershipChangeDN);

        return lliOwnershipChangeDN.getID();
    }



    @Transactional
    long insertLongTermSB(LLIBreakLongTermDemandNote lliBreakLongTermDN, long appId, LoginDTO loginDTO) throws Exception {
        ReviseDTO reviseDTO = reviseService.getappById(appId);
        validateApplication(reviseDTO, LLIConnectionConstants.BREAK_LONG_TERM);
        populateBreakLongTermShortBill(reviseDTO, lliBreakLongTermDN);
        insertDemandNote(lliBreakLongTermDN, appId, loginDTO);
        AsyncPdfService.getInstance().accept( lliBreakLongTermDN );


        return lliBreakLongTermDN.getID();
    }

    @Transactional
    long insertCloseConnectionDN(LLICloseConnectionDemandNote lliCloseConnectionDN, long appId,long state, LoginDTO loginDTO) throws Exception {

        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appId);
        DemandNoteGenerationFacade.isDemandNoteAlreadyGenerated(lliApplication.getDemandNoteID(), true);
        populateCloseConnectionDemandNote(lliApplication, lliCloseConnectionDN);

        billService.insertBill(lliCloseConnectionDN);
        lliApplicationService.setDemandNoteId(
                lliCloseConnectionDN.getID(), appId
        );
        AsyncPdfService.getInstance().accept( lliCloseConnectionDN);
        saveDemandNoteAsOfficialLetter(lliApplication, loginDTO, LLIApplicationService.class.getCanonicalName());

        long billID = lliCloseConnectionDN.getID();

        //added by bony


        lliApplication.setState((int) state);

        lliApplication.setBillID(lliCloseConnectionDN.getID());
        lliApplication.setDemandNoteID(billID);
        lliApplication.setStatus(LLIConnectionConstants.STATUS_DEMAND_NOTE_GENERATED);
        lliApplicationService.updateApplicaton(lliApplication);
        lliApplicationService.sendNotification(lliApplication, (int)state, loginDTO);


        return lliCloseConnectionDN.getID();
    }

    @Transactional
    long insertCommonConnectionDN(LLISingleConnectionCommonDemandNote lliReviseConnectionDN, long appId, long nextState, LoginDTO loginDTO) throws Exception {
        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appId);
        DemandNoteGenerationFacade.isDemandNoteAlreadyGenerated(lliApplication.getDemandNoteID(), true);
        populateReviseConnectionDemandNote(lliApplication, lliReviseConnectionDN);



        insertDemandNote(lliReviseConnectionDN);
        updateLLIApplication(lliApplication, lliReviseConnectionDN,(int) nextState, loginDTO);
        saveDemandNoteAsOfficialLetter(lliApplication, loginDTO, LLIApplicationService.class.getCanonicalName());
        if (lliReviseConnectionDN.getBandwidthCharge() > 0 ||
                lliReviseConnectionDN.getCoreCharge() > 0) {
            AdjustDemandNoteAdjustment(lliReviseConnectionDN, appId);
        }
        lliApplicationService.sendNotification(lliApplication, (int)nextState, loginDTO);
        AsyncPdfService.getInstance().accept( lliReviseConnectionDN);
        return lliReviseConnectionDN.getID();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    LLINewConnectionDemandNote getAutoFillDataNewConnection(long applicationID) throws Exception {
        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(applicationID);
        validateApplication(lliApplication, LLIConnectionConstants.NEW_CONNECTION);
        LLIFixedCostConfigurationDTO lliFixedCostConfigurationDTO = lliCostConfigurationService.getCurrentActiveLLI_FixedCostConfigurationDTO();
        int numberOfBTCLProvidedFiberCount = lliConnectionService.getBTCLProvidedLocalLoopCount(localLoopService.getLocalLoop(applicationID));
        LLINewConnectionDemandNote lliNewConnectionDemandNote = new LLINewConnectionDemandNote();
        if(lliApplication.getApplicationType() == LLIConnectionConstants.SHIFT_BANDWIDTH_NEW_CONNECTION){
            return getShiftBWNewConnectionDN(lliApplication, lliNewConnectionDemandNote, lliFixedCostConfigurationDTO, numberOfBTCLProvidedFiberCount);
        }

        //Fixed Information
        lliNewConnectionDemandNote.setClientID(lliApplication.getClientID());
        lliNewConnectionDemandNote.setAdvanceAdjustment(0);
        lliNewConnectionDemandNote.setDescription("Demand Note of application ID " + applicationID);
        lliNewConnectionDemandNote.setRegistrationFee(lliFixedCostConfigurationDTO.getRegistrationCharge());
        lliNewConnectionDemandNote.setVatPercentage(lliFixedCostConfigurationDTO.getMaximumVatPercentage());
        lliNewConnectionDemandNote.setFibreOTC(numberOfBTCLProvidedFiberCount * lliFixedCostConfigurationDTO.getFiberCharge());
        lliNewConnectionDemandNote.setDiscountPercentage(0);

        //Calculated Information
        double totalBWCharge = getNewConnectionApplicationBandwidthMRC(lliApplication);
        lliNewConnectionDemandNote.setBwMRC(totalBWCharge);

        List<LocalLoop> localLoops = localLoopService.getLocalLoop(lliApplication.getApplicationID());
        lliNewConnectionDemandNote.setLocalLoopCharge(calculateLocalLoopCharge(localLoops));
//        List<KeyValuePair<LocalLoop , Double>> localLoopCharges = calculateLocalLoopChargeNew(localLoops);
//        double totalLocalLoopCharge = localLoopCharges.stream()
//                .mapToDouble(KeyValuePair::getValue)
//                .sum();
//        lliNewConnectionDemandNote.setLocalLoopCharge(totalLocalLoopCharge);

        ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(lliApplication.getClientID(), ModuleConstants.Module_ID_LLI);
        RegistrantTypeDTO registrantType = ServiceDAOFactory.getService(ClientClassificationService.class).getClientRegistrantTypeByRegistrantTypeId(clientDetailsDTO.getRegistrantType());


        if(registrantType.getRegTypeId() != RegistrantTypeConstants.GOVT){
            lliNewConnectionDemandNote.setSecurityMoney(lliApplication.getConnectionType() == LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY ? 0 : totalBWCharge);
        }
        return lliNewConnectionDemandNote;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    LLICloseConnectionDemandNote getAutoFillDataCloseConnection(long applicationID) throws Exception {
        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(applicationID);
        validateApplication(lliApplication, LLIConnectionConstants.CLOSE_CONNECTION);
        LLIFixedCostConfigurationDTO lliFixedCostConfigurationDTO = lliCostConfigurationService.getCurrentActiveLLI_FixedCostConfigurationDTO();
        LLICloseConnectionDemandNote lliCloseConnectionDemandNote = new LLICloseConnectionDemandNote();
        //Fixed Information
        lliCloseConnectionDemandNote.setClientID(lliApplication.getClientID());
        lliCloseConnectionDemandNote.setClosingOTC(lliFixedCostConfigurationDTO.getInstantClosingCharge());
        lliCloseConnectionDemandNote.setOtherCost(0);
        lliCloseConnectionDemandNote.setDescription("Demand Note of application ID " + applicationID);
        lliCloseConnectionDemandNote.setVatPercentage(lliFixedCostConfigurationDTO.getMaximumVatPercentage());
        lliCloseConnectionDemandNote.setDiscountPercentage(0);
        return lliCloseConnectionDemandNote;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    LLISingleConnectionCommonDemandNote getAutoFillDataCommonConnection(long applicationId) throws Exception {
        LLIApplication lliApplication = lliApplicationService.getLLIApplicationByApplicationID(applicationId);
        int applicationType = lliApplication.getApplicationType();
        validateApplication(lliApplication, LLIConnectionConstants.REVISE_CONNECTION);

        LLIConnectionInstance existingLLIConnectionInstance = lliConnectionService.getLLIConnectionByConnectionID(lliApplication.getConnectionId());
        LLIFixedCostConfigurationDTO lliFixedCostConfigurationDTO = lliCostConfigurationService.getCurrentActiveLLI_FixedCostConfigurationDTO();

        LLISingleConnectionCommonDemandNote lliCommonConnectionDemandNote = new LLISingleConnectionCommonDemandNote();
        // bill part
        lliCommonConnectionDemandNote.setDiscountPercentage(existingLLIConnectionInstance.getDiscountRate());
        lliCommonConnectionDemandNote.setClientID(lliApplication.getClientID());
        lliCommonConnectionDemandNote.setDescription("Demand Note of application ID " + applicationId);
        lliCommonConnectionDemandNote.setVatPercentage(lliFixedCostConfigurationDTO.getMaximumVatPercentage());



        // lli common dn part
        lliCommonConnectionDemandNote.setAdvancedAmount(0);

        double downgradeCharge = 0, shiftCharge = 0;
        switch (applicationType) {
            case LLIConnectionConstants.DOWNGRADE_BANDWIDTH:
                downgradeCharge = lliFixedCostConfigurationDTO.getDowngradeCharge();
                break;
            case LLIConnectionConstants.SHIFT_CONNECTION_ADDRESS:
            case LLIConnectionConstants.SHIFT_POP:
                shiftCharge = lliFixedCostConfigurationDTO.getShiftingCharge();
                break;
            default:
                break;
        }

        if(lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP
                || lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT){
            return additionalLoopPortDN(lliApplication, existingLLIConnectionInstance, lliFixedCostConfigurationDTO);
        }


        lliCommonConnectionDemandNote.setDowngradeCharge(downgradeCharge);
        lliCommonConnectionDemandNote.setShiftCharge(shiftCharge);


        boolean isDowngradeOrShiftingApplication =
                (applicationType == LLIConnectionConstants.DOWNGRADE_BANDWIDTH
                        || applicationType == LLIConnectionConstants.SHIFT_BANDWIDTH);

        //Calculated Information

        double bwCharge = getBandwidthMRC(applicationId, existingLLIConnectionInstance);
        int portCount = 0;
        double coreCharge = 0;
        long fibreCount = 0;
        if(lliApplication.getApplicationType() == LLIConnectionConstants.UPGRADE_BANDWIDTH) {
            if(lliApplication.isNewLoop()) {
                List<NewLocalLoop> newLocalLoops  = ServiceDAOFactory.getService(NewLocalLoopService.class).getLocalLoop(lliApplication.getApplicationID());
                coreCharge = ServiceDAOFactory.getService(DemandNoteAutofillFacade.class).calculateLocalLoopCostFromLLIApplicationLocalLoop(newLocalLoops);
                fibreCount = newLocalLoops.stream()
                        .filter(t->t.getBTCLDistances() + t.getOCDistances() > 0)
                        .count();
                portCount = newLocalLoops.size();

            }
        }else {
            coreCharge = getLocalLoopCharge(applicationId, existingLLIConnectionInstance);
            fibreCount = getFiberCount(applicationId, existingLLIConnectionInstance);
            portCount = getPortCount(applicationId, existingLLIConnectionInstance);
        }


        lliCommonConnectionDemandNote.setFibreOTC(fibreCount * lliFixedCostConfigurationDTO.getFiberCharge());
        lliCommonConnectionDemandNote.setCoreCharge(coreCharge);
        lliCommonConnectionDemandNote.setPortCharge(portCount * lliFixedCostConfigurationDTO.getPortCharge());
        if(!isDowngradeOrShiftingApplication){
            lliCommonConnectionDemandNote.setBandwidthCharge(bwCharge);
            ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance()
                    .getModuleClientByClientIDAndModuleID(lliApplication.getClientID(), ModuleConstants.Module_ID_LLI);
            RegistrantTypeDTO registrantType = ServiceDAOFactory.getService(ClientClassificationService.class)
                    .getClientRegistrantTypeByRegistrantTypeId(clientDetailsDTO.getRegistrantType());


            if(registrantType.getRegTypeId() != RegistrantTypeConstants.GOVT){
                lliCommonConnectionDemandNote.setSecurityMoney(
                        lliApplication.getConnectionType() == LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY
                                ? 0
                                : lliCommonConnectionDemandNote.getBandwidthCharge()
                );
            }

        }else {
            lliCommonConnectionDemandNote.setBandwidthCharge(0);
            lliCommonConnectionDemandNote.setSecurityMoney(0);
        }



        if(lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_IP){
            return additionalIPDN(lliApplication, lliCommonConnectionDemandNote, lliFixedCostConfigurationDTO);
        }
        return lliCommonConnectionDemandNote;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public LLIBreakLongTermDemandNote getAutoFillDataBreakLongTerm(long applicationID) throws Exception {
        ReviseDTO reviseDTO = reviseService.getappById(applicationID);
        validateApplication(reviseDTO, LLIConnectionConstants.BREAK_LONG_TERM);
        LLIFixedCostConfigurationDTO lliFixedCostConfigurationDTO = lliCostConfigurationService.getCurrentActiveLLI_FixedCostConfigurationDTO();
        LLIBreakLongTermDemandNote lliBreakLongTermDemandNote = new LLIBreakLongTermDemandNote();
        lliBreakLongTermDemandNote.setVatPercentage(lliFixedCostConfigurationDTO.getMaximumVatPercentage());
        lliBreakLongTermDemandNote.setDiscountPercentage(0);
        lliBreakLongTermDemandNote.setOtherCost(0);

        Double benefits = lliLongTermBenefitService.getTotalBenefitsByContractId(reviseDTO.getReferenceContract());

        //Say previously it was 60 MB and total benefit 6000 taka
        //Now break contract of 20 MB so new contract is 40 MB
        //So charge is 2000 taka and rest 4000 taka will be forward to benefitted amount for that 40 MB
        LLILongTermContract currentContract = lliLongTermContractDAO.getLLILongTermContractByContractID(reviseDTO.getReferenceContract());
        if(currentContract.getBandwidth() - reviseDTO.getBandwidth()  < 0)
            throw  new Exception("long term break BW can't be greater than that contract's BW");

        if(benefits != null){
            double amount = benefits * reviseDTO.getBandwidth() /currentContract.getBandwidth();
            lliBreakLongTermDemandNote.setContractBreakingFine( NumberUtils.formattedValue(amount));
        }else{
            logger.error(" [ XX ] Long term benefits: null. Putting 0  in place of null");
            lliBreakLongTermDemandNote.setContractBreakingFine(0);
        }
        return lliBreakLongTermDemandNote;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public LLIReconnectConnectionDemandNote getAutoFillDataReconnect(long applicationID) throws Exception {
        ReviseDTO reviseDTO = reviseService.getappById(applicationID);
        validateApplication(reviseDTO, LLIConnectionConstants.RECONNECT);
        LLIFixedCostConfigurationDTO lliFixedCostConfigurationDTO = lliCostConfigurationService.getCurrentActiveLLI_FixedCostConfigurationDTO();
        LLIReconnectConnectionDemandNote lliReconnectDN = new LLIReconnectConnectionDemandNote();
        lliReconnectDN.setVatPercentage(lliFixedCostConfigurationDTO.getMaximumVatPercentage());
        lliReconnectDN.setDiscountPercentage(0);
        lliReconnectDN.setOtherCost(0);
        lliReconnectDN.setReconnectionCharge(lliFixedCostConfigurationDTO.getReconnectionCharge());
        return lliReconnectDN;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public LLIOwnerShipChangeDemandNote getAutoFillDataOwnershipChange(long applicationID) throws Exception {
        LLIOwnerShipChangeApplication lliApplication = ServiceDAOFactory.getService(LLIOwnerChangeService.class).getApplicationById(applicationID);
        LLIFixedCostConfigurationDTO lliFixedCostConfigurationDTO = lliCostConfigurationService.getCurrentActiveLLI_FixedCostConfigurationDTO();
        LLIOwnerShipChangeDemandNote lliOwnershipChangeDN = new LLIOwnerShipChangeDemandNote();
        lliOwnershipChangeDN.setVatPercentage(lliFixedCostConfigurationDTO.getMaximumVatPercentage());
        lliOwnershipChangeDN.setDiscountPercentage(0);
        lliOwnershipChangeDN.setOtherCosts(0);
        List<LLIOnProcessConnection> list = ServiceDAOFactory.getService(LLIOnProcessConnectionService.class).getConnectionByAppId(lliApplication.getId());
        lliOwnershipChangeDN.setOwnerShipChangeCharge(list.size() * 5000);
        return lliOwnershipChangeDN;
    }

    private void validateApplication(LLIApplication lliApplication, int applicationType) {
        if (lliApplication == null) {
            throw new RequestFailureException("No application found");
        }
    }

    private void validateApplication(ReviseDTO reviseDTO, int applicationType) {
        if (reviseDTO == null) {
            throw new RequestFailureException("No revise application found");
        } else {
            if (reviseDTO.getApplicationType() != applicationType) {
                throw new RequestFailureException("The application for this demand note is not a " + LLIConnectionConstants.applicationTypeNameMap.get(applicationType) + " application");
            }
            DemandNoteGenerationFacade.isDemandNoteAlreadyGenerated(reviseDTO.getDemandNoteID(), false);
        }

    }

    private void checkOtherItemCostsForNegativeAmount(List<ItemCost> itemCosts) throws Exception {
        Optional<ItemCost> result = itemCosts.stream().filter(itemCost -> itemCost.cost < 0).findAny();
        if (result.isPresent()) {
            throw new RequestFailureException(result.get().item + " must be a non negative number");
        }
    }

    private void populateLLINewConnectionDemandNote(LLIApplication lliApplication,
                                                    LLINewConnectionDemandNote lliNewConnectionDN) throws Exception {

        validateApplication(lliApplication, LLIConnectionConstants.NEW_CONNECTION);
        lliNewConnectionDN.setClientID(lliApplication.getClientID());
        double vatCalculable = lliNewConnectionDN.getRegistrationFee()
                + lliNewConnectionDN.getBwMRC()
                + lliNewConnectionDN.getTotalItemCost()
                + lliNewConnectionDN.getFibreOTC()
                + lliNewConnectionDN.getLocalLoopCharge()
                + lliNewConnectionDN.getAdvanceAdjustment();

        double discount = Math.floor(lliNewConnectionDN.getBwMRC() * lliNewConnectionDN.getDiscountPercentage() / 100.);
        vatCalculable -= discount;
        double vat = Math.ceil(vatCalculable * lliNewConnectionDN.getVatPercentage() / 100.0);
        // discount = bw charge * disc % in order to calculate vat properly.
        logger.info("VAT Calculable: " + vatCalculable + ", discount: " + discount + ", VAT: " + vat);

        double grandTotal = vatCalculable + lliNewConnectionDN.getSecurityMoney();
        // security also holds actual security

        double updatedBWCharge = lliNewConnectionDN.getBwMRC() - discount;

        // if security > 0 we will make bw charge = actual bw charge and security = actual security - discount
        // else  bw charge = actual bw charge - discount and security = actual security,
        // this is done in order to incorporate with accounting.
        // note that db will not always reflect the actual bw charge ( before discount deduction value will be shown ) where security > 0

        if(lliNewConnectionDN.getSecurityMoney() > 0 ) {
            // this means for PVT clients
            // so bw charge will only be same when the client is not GOVT i.e. actual security money.
            lliNewConnectionDN.setSecurityMoney(updatedBWCharge);
        }

        double totalPayable = grandTotal - (lliNewConnectionDN.getSecurityMoney() > 0 ? discount : 0  );
        double netPayable = totalPayable + vat;
        lliNewConnectionDN.setVAT(vat);
        lliNewConnectionDN.setDiscount(discount);
        lliNewConnectionDN.setGrandTotal(grandTotal);
        lliNewConnectionDN.setTotalPayable(totalPayable);
        lliNewConnectionDN.setNetPayable(netPayable);
        List<ItemCost> itemCosts = lliNewConnectionDN.getItemCosts();
        if (itemCosts == null) {
            itemCosts = new ArrayList<>();
        }
        checkOtherItemCostsForNegativeAmount(itemCosts);
        String itemCostContent = new Gson().toJson(lliNewConnectionDN.getItemCosts());
        lliNewConnectionDN.setItemCostContent(itemCostContent);
        lliNewConnectionDN.setLastPaymentDate(TimeConverter.getLastDateBeforeNMonth(1));
    }

    private void populateReviseConnectionDemandNote(LLIApplication lliApplication,
                                                    LLISingleConnectionCommonDemandNote lliReviseConnectionDN) throws Exception {
        validateApplication(lliApplication, LLIConnectionConstants.REVISE_CONNECTION);
        lliReviseConnectionDN.setClientID(lliApplication.getClientID());
        double vatCalculable = lliReviseConnectionDN.getBandwidthCharge()
                + lliReviseConnectionDN.getDowngradeCharge()
                + lliReviseConnectionDN.getPortCharge()
                + lliReviseConnectionDN.getFibreOTC()
                + lliReviseConnectionDN.getCoreCharge()
                + lliReviseConnectionDN.getTotalItemCost()
                + lliReviseConnectionDN.getFirstXIpCost()
                + lliReviseConnectionDN.getNextYIpCost()
                + lliReviseConnectionDN.getShiftCharge()
                + lliReviseConnectionDN.getAdvancedAmount();

        double discount = Math.floor(lliReviseConnectionDN.getBandwidthCharge() * lliReviseConnectionDN.getDiscountPercentage() / 100.0);
        vatCalculable -= discount;

        double vat = Math.ceil(vatCalculable * lliReviseConnectionDN.getVatPercentage() / 100.0);
        double grandTotal = vatCalculable + lliReviseConnectionDN.getSecurityMoney();
        double updatedBWCharge = lliReviseConnectionDN.getBandwidthCharge() - discount;

        if(lliReviseConnectionDN.getSecurityMoney() > 0 ) {
        // this means for PVT clients
        // so bw charge will only be same when the client is not GOVT i.e. actual security money.
            lliReviseConnectionDN.setSecurityMoney(updatedBWCharge);
        }
        double totalPayable = grandTotal - (lliReviseConnectionDN.getSecurityMoney() > 0 ? discount : 0  );
        double netPayable = totalPayable + vat;
        lliReviseConnectionDN.setVAT(vat);
        lliReviseConnectionDN.setGrandTotal(grandTotal);
        lliReviseConnectionDN.setTotalPayable(totalPayable);
        lliReviseConnectionDN.setNetPayable(netPayable);
        lliReviseConnectionDN.setDiscount(discount);
        List<ItemCost> itemCosts = lliReviseConnectionDN.getItemCosts();
        if (itemCosts == null) {
            itemCosts = new ArrayList<>();
        }
        checkOtherItemCostsForNegativeAmount(itemCosts);
        String itemCostContent = new Gson().toJson(lliReviseConnectionDN.getItemCosts());
        lliReviseConnectionDN.setItemCostContent(itemCostContent);
        lliReviseConnectionDN.setLastPaymentDate(TimeConverter.getLastDateBeforeNMonth(1));
    }

    private void populateLLIReconnectConnectionDemandNote(ReviseDTO reviseDTO, LLIReconnectConnectionDemandNote lliReconnectConnectionDN) throws Exception {


        lliReconnectConnectionDN.setReviseDTO(reviseDTO);
        lliReconnectConnectionDN.setClientID(reviseDTO.getClientID());

        double vatCalculable = lliReconnectConnectionDN.getReconnectionCharge() + lliReconnectConnectionDN.getOtherCost();
        double vat = vatCalculable * lliReconnectConnectionDN.getVatPercentage() / 100.0;
        double grandTotal = vatCalculable;
        double discount = 0 * lliReconnectConnectionDN.getDiscountPercentage() / 100.0;
        double totalPayable = grandTotal - discount;
        double netPayable = totalPayable + vat;
        lliReconnectConnectionDN.setVAT(vat);
        lliReconnectConnectionDN.setDiscount(discount);
        lliReconnectConnectionDN.setGrandTotal(grandTotal);
        lliReconnectConnectionDN.setTotalPayable(totalPayable);
        lliReconnectConnectionDN.setNetPayable(netPayable);
        lliReconnectConnectionDN.setLastPaymentDate(TimeConverter.getLastDateBeforeNMonth(1));

        long demandNoteID = reviseDTO.getDemandNoteID();
        if (demandNoteID > 0) lliReconnectConnectionDN.setID(demandNoteID);


        List<FlowState> flowStates = FlowRepository.getInstance().getNextStatesByCurrentState((int) reviseDTO.getState());
        if (flowStates != null) {
            if (!flowStates.isEmpty()) {
                reviseService.updateApplicatonState(reviseDTO.getId(), flowStates.get(0).getId());
            }
        }
    }

    private LLIOwnerShipChangeApplication populateLLIOwnershipChangeDemandNote(long appId,
                                                      LLIOwnerShipChangeDemandNote lliOwnershipChangeDN) throws Exception {
        LLIOwnerShipChangeApplication lliApplication = ServiceDAOFactory.getService(LLIOwnerChangeService.class).getApplicationById(appId);
        if(lliApplication == null ){
            throw new RequestFailureException(" No Ownership Change Application Found with Application id: " + appId);
        }

        DemandNoteGenerationFacade.isDemandNoteAlreadyGenerated(lliApplication.getDemandNote(), false);

        lliOwnershipChangeDN.setClientID(lliApplication.getDstClient()); // destination client will pay the bill.

        double vatCalculable = lliOwnershipChangeDN.getOwnerShipChangeCharge() + lliOwnershipChangeDN.getOtherCosts();
        double vat = vatCalculable * lliOwnershipChangeDN.getVatPercentage() / 100.0;
        double grandTotal = vatCalculable;
        double discount = 0 * lliOwnershipChangeDN.getDiscountPercentage() / 100.0;
        double totalPayable = grandTotal - discount;
        double netPayable = totalPayable + vat;
        lliOwnershipChangeDN.setVAT(vat);
        lliOwnershipChangeDN.setDiscount(discount);
        lliOwnershipChangeDN.setGrandTotal(grandTotal);
        lliOwnershipChangeDN.setTotalPayable(totalPayable);
        lliOwnershipChangeDN.setNetPayable(netPayable);
        lliOwnershipChangeDN.setEntityID(-1);
        lliOwnershipChangeDN.setLastPaymentDate(TimeConverter.getLastDateBeforeNMonth(1));
        return lliApplication;
    }

    private void populateBreakLongTermShortBill(ReviseDTO reviseDTO, LLIBreakLongTermDemandNote lliBreakLongTermDN) throws Exception {

        lliBreakLongTermDN.setReviseDTO(reviseDTO);
        lliBreakLongTermDN.setClientID(reviseDTO.getClientID());
        double vatCalculable = lliBreakLongTermDN.getContractBreakingFine() + lliBreakLongTermDN.getOtherCost();
        double vat = vatCalculable * lliBreakLongTermDN.getVatPercentage() / 100.0;
        double grandTotal = vatCalculable;
        double discount = 0 * lliBreakLongTermDN.getDiscountPercentage() / 100.0;
        double totalPayable = grandTotal - discount;
        double netPayable = totalPayable + vat;
        lliBreakLongTermDN.setVAT(vat);
        lliBreakLongTermDN.setDiscount(discount);
        lliBreakLongTermDN.setGrandTotal(grandTotal);
        lliBreakLongTermDN.setTotalPayable(totalPayable);
        lliBreakLongTermDN.setNetPayable(netPayable);
        lliBreakLongTermDN.setLastPaymentDate(TimeConverter.getLastDateBeforeNMonth(1));
    }

    private void populateCloseConnectionDemandNote(LLIApplication lliApplication,
                                                   LLICloseConnectionDemandNote lliCloseConnectionDN) throws Exception {

        validateApplication(lliApplication, LLIConnectionConstants.CLOSE_CONNECTION);
        lliCloseConnectionDN.setClientID(lliApplication.getClientID());
        double vatCalculable = lliCloseConnectionDN.getClosingOTC() + lliCloseConnectionDN.getOtherCost();
        double vat = vatCalculable * lliCloseConnectionDN.getVatPercentage() / 100.0;
        double grandTotal = vatCalculable;
        double discount = 0 * lliCloseConnectionDN.getDiscountPercentage() / 100.0;
        double totalPayable = grandTotal - discount;
        double netPayable = totalPayable + vat;
        lliCloseConnectionDN.setVAT(vat);
        lliCloseConnectionDN.setGrandTotal(grandTotal);
        lliCloseConnectionDN.setTotalPayable(totalPayable);
        lliCloseConnectionDN.setNetPayable(netPayable);
        lliCloseConnectionDN.setLastPaymentDate(TimeConverter.getLastDateBeforeNMonth(1));
    }

    private double getNewConnectionApplicationBandwidthMRC(LLIApplication lliNewConnectionApplication) throws Exception {
        double cacheConnectionRate = lliCostConfigurationService.getCurrentActiveLLI_FixedCostConfigurationDTO().getCacheServiceFlatRate();

        if (lliNewConnectionApplication.getConnectionType() == LLIConnectionConstants.CONNECTION_TYPE_CACHE) {
            return lliNewConnectionApplication.getBandwidth() * cacheConnectionRate;
        }

        long clientID = lliNewConnectionApplication.getClientID();
        int tariffCategory = ClientTypeService.getClientCategoryByModuleIDAndClientID(ModuleConstants.Module_ID_LLI, clientID);

        // TODO:logic change hard coded column index
        int costChartColumnValue = lliNewConnectionApplication.getConnectionType() == LLIConnectionConstants.CONNECTION_TYPE_REGULAR_LONG ? 5 : 4;

        // TODO:logic all connections are being collected        
        List<LLIConnectionInstance> lliConnectionListByClientID = lliConnectionService.getLLIConnectionInstanceListByClientID(clientID);
        // TODO:logic also the applied Bandwidth is being added, why ???
        double aggregatedBandwidth = lliNewConnectionApplication.getBandwidth();
        for (LLIConnectionInstance lliConnectionInstance : lliConnectionListByClientID) {
            // TODO:logic check whether the connections are active and regular        
            aggregatedBandwidth += lliConnectionInstance.getBandwidth();
        }

        TableDTO tableDTO = ServiceDAOFactory.getService(CostConfigService.class).getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(System.currentTimeMillis(), ModuleConstants.Module_ID_LLI, tariffCategory);
        double bwChargeMonth =  tableDTO.getCostAfterAggregationByNewBandwidthAndAggregatedBandwidthAndColumnValue(lliNewConnectionApplication.getBandwidth(), aggregatedBandwidth, costChartColumnValue);

        if(lliNewConnectionApplication.getConnectionType() != LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY){
            return bwChargeMonth;
        }

        // Calculations for Temporary new connection
        LLIOTC lliotc = universalDTOService.get(LLIOTC.class);
        long duration = lliNewConnectionApplication.getDuration();
        if(duration > lliotc.getTempConnectionMaximumLimit_Days()){
            throw new Exception(" [ X ] Temporary connection duration must not exceed " + lliotc.getTempConnectionMaximumLimit_Days() + " days.");
        }

        int clientType = clientService.getClientInfoFromClientID(clientID).getRegType();
        boolean isPrivileged;
        switch(clientType){
            case RegistrantTypeConstants.GOVT:
            case RegistrantTypeConstants.MILITARY:
                isPrivileged = true;
                break;
            default:
                isPrivileged = false;
        }

        // TODO: check total regular bandwidth
        boolean isPremium = lliConnectionService.getTotalExistingRegularBWByClientID(clientID) >= lliotc.getPremiumBWMargin_Mbps();
        if(isPrivileged || isPremium){
            return Math.max(lliotc.getTempConnectionMinimumLimit_Days(), duration) * (bwChargeMonth / 30.0);
        }

        return bwChargeMonth;
    }

    private boolean isLocalLoopFresh(List<LocalLoop> localLoops, long localLoopID) {
        boolean status = true;
        if (localLoops != null) {
            for (LocalLoop localLoop : localLoops) {
                if (localLoop.getId() == localLoopID) {
                    status = false;
                    break;
                }
            }
        }

        return status;
    }

    private LocalLoop getLLILocalLoopByIDFromLocalLoopList(long localLoopID, List<LocalLoop> list) {
        for (LocalLoop localLoop : list) {
            if (localLoop.getId() == localLoopID) {
                return localLoop;
            }
        }
        throw new RequestFailureException("Invalid Local Loop Information");
    }

    private List<LocalLoop> getLocalLoopWithOld(long applicationId, LLIConnectionInstance existingConnection) {
        List<LocalLoop> existingLocalLoops = null;
        List<LocalLoop> appliedLocalLoops = null;
        try {
            existingLocalLoops = localLoopService.getLocalLoopByCon(existingConnection.getID());
            appliedLocalLoops = localLoopService.getLocalLoop(applicationId);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }

        List<LocalLoop> oldLocalLoops = new ArrayList<LocalLoop>();
        if (appliedLocalLoops != null) {
            for (LocalLoop lliLocalLoop : appliedLocalLoops) {
                if (!isLocalLoopFresh(existingLocalLoops, lliLocalLoop.getId())) {
                    oldLocalLoops.add(lliLocalLoop);
                }
            }
        }
        return oldLocalLoops;
    }

    private double getExtraLocalLoopCharge(long applicationId, LLIConnectionInstance existingConnection) throws Exception {
        List<LocalLoop> existingLocalLoops = localLoopService.getLocalLoopByCon(existingConnection.getID());
        List<LocalLoop> possiblyChangedLocalLoops = getLocalLoopWithOld(applicationId, existingConnection);

        int localLoopMinimumCost = 0;
        int localLoopDistanceCheckpoint = 0;

        OfcInstallationCostDTO ofcInstallationCostDTO = ofcInstallationCostService.getLatestByDate(DateUtils.getCurrentTime());

        if (ofcInstallationCostDTO != null) {
            localLoopMinimumCost = ofcInstallationCostDTO.getFiberCost();
            localLoopDistanceCheckpoint = ofcInstallationCostDTO.getFiberLength();
        }

        double totalLocalLoopCharge = 0;
        for (LocalLoop localLoop : possiblyChangedLocalLoops) {
            double extraChargeForThisLocalLoopCharge = 0;

            LocalLoop oldLocalLoop = getLLILocalLoopByIDFromLocalLoopList(localLoop.getId(), existingLocalLoops);
            double oldBillableDistance = oldLocalLoop.getBTCLDistances() + oldLocalLoop.getOCDistances();
            double newBillableDistance = localLoop.getBTCLDistances() + localLoop.getOCDistances();
            long oldOfcType = oldLocalLoop.getOfcType();
            long newOfcType = localLoop.getOfcType();

            if (oldBillableDistance >= newBillableDistance && oldOfcType >= newOfcType) {
                continue;
            } else {
                newBillableDistance -= localLoopDistanceCheckpoint;
                if (oldBillableDistance == 0) {
                    extraChargeForThisLocalLoopCharge = localLoopMinimumCost;
                }
                double extraDistance = oldBillableDistance <= localLoopDistanceCheckpoint ? newBillableDistance : newBillableDistance - (oldBillableDistance - localLoopDistanceCheckpoint);
                extraDistance = Math.max(extraDistance, 0);

                //long districtID = inventoryService.getInventoryParentItemPathMapUptoRootByItemID(localLoop.getVlanID()).get(InventoryConstants.CATEGORY_ID_DISTRICT).getID();
                //double ofcInstallationCostByDistrictID = new DistrictOfcInstallationService().getOfcInstallationCostByDistrictID(districtID);

                // TODO: Cost of unit length of fiber. Fiber cost varies from district to district. (It's mostly 1.5 and in 3-4 cases 1.95)
                double ofcInstallationCostByDistrictID = 1.5; // need to change; TODO raihan
                extraChargeForThisLocalLoopCharge += extraDistance * ofcInstallationCostByDistrictID;

                extraChargeForThisLocalLoopCharge *= localLoop.getOfcType();
                // TODO: (from Dhrubo) OFC Type bere gele ki hobe, ota korinai.
            }
            totalLocalLoopCharge += extraChargeForThisLocalLoopCharge;
        }

        return totalLocalLoopCharge;
    }

    private List<LocalLoop> getFreshLocalLoops(long applicationId, LLIConnectionInstance existingConnection) {
        List<LocalLoop> appliedLocalLoops = null;
        try {
            appliedLocalLoops = localLoopService.getLocalLoop(applicationId);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }

        return appliedLocalLoops;
    }

    private int getFiberCount(long applicationId, LLIConnectionInstance existingConnection) {
        List<LocalLoop> freshLocalLoops = getFreshLocalLoops(applicationId, existingConnection);
        int billableLocalLoopCount = 0;

        if (freshLocalLoops != null) {
            for (LocalLoop localLoop : freshLocalLoops) {
                if (localLoop.getBTCLDistances() + localLoop.getOCDistances() > 0) {
                    billableLocalLoopCount++;
                }
            }
        }
        return billableLocalLoopCount;
    }

    private int getPortCount(long applicationId, LLIConnectionInstance existingConnection) {
        List<LocalLoop> freshLocalLoops = getFreshLocalLoops(applicationId, existingConnection);
        return freshLocalLoops != null ? freshLocalLoops.size() : 0;
    }

    private double getBandwidthMRC(long applicationId, LLIConnectionInstance existingConnection) throws Exception {
        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(applicationId);
        double extraBandwidth = lliApplication.getBandwidth();// - existingConnection.getBandwidth();
        if (extraBandwidth <= 0) return 0;

        double aggregatedBandwidth = lliConnectionService.getExistingTotalBandwidthByClientID(lliApplication.getClientID()) + extraBandwidth;
        return extraBandwidth * getBandwidthMRCRate(
                existingConnection.getClientID(),
                existingConnection.getConnectionType(),
                aggregatedBandwidth,
                false
                );
    }

    private double getBandwidthMRCRate(
            long clientId,
            int connectionType,
            double bandwidth,
            boolean isLongTerm) throws Exception
    {

        if (connectionType == LLIConnectionConstants.CONNECTION_TYPE_CACHE) {
            return lliCostConfigurationService.getCurrentActiveLLI_FixedCostConfigurationDTO().getCacheServiceFlatRate();
        }

        int tariffCategory = ClientTypeService.getClientCategoryByModuleIDAndClientID(ModuleConstants.Module_ID_LLI, clientId);

        return ServiceDAOFactory.getService(CostConfigService.class)
                .getMRCRate(
                        System.currentTimeMillis(),
                        ModuleConstants.Module_ID_LLI,
                        tariffCategory,
                        bandwidth,
                        isLongTerm
                );
    }

    @Transactional(transactionType = TransactionType.READONLY)
    List<Map<String, String>> getBreakdownData(long appId) throws Exception {
        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appId);
        boolean additionalLoopApplication = lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP;
        boolean additionalPortApplication = lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT;
        boolean upgradeApplication = lliApplication.getApplicationType() == LLIConnectionConstants.UPGRADE_BANDWIDTH;

        boolean isNewLoop = lliApplication.isNewLoop();
        boolean upgradeAndNewLoop = upgradeApplication && isNewLoop;
        if(additionalLoopApplication
                || upgradeAndNewLoop
                ||additionalPortApplication
        ) {
            List<NewLocalLoop> newLocalLoops  = ServiceDAOFactory.getService(NewLocalLoopService.class).getLocalLoop(lliApplication.getApplicationID());
            return calculateLocalLoopChargeNewForNewLoopCase(newLocalLoops);
        }else {
            List<LocalLoop> localLoops = localLoopService.getLocalLoop(lliApplication.getApplicationID());
            return calculateLocalLoopChargeNew(localLoops);
        }

    }

    private List <Map<String, String>> calculateLocalLoopBreakdown(List<LoopBreakdownEssentialData> breakdownEssentialData) {
        List<Map<String, String>> list = new ArrayList<>();
        breakdownEssentialData.forEach(t-> {
            try {
                Map<String, String> map = new HashMap<>();
                long districtId = DemandNoteAutofillFacade.getDistrictIdByPopId(t.getPopID());
                double factor = DemandNoteAutofillFacade.getFactorForLocalLoopCostByDistrictId(districtId);
                OfcInstallationCostDTO ofcInstallationCostDTO = DemandNoteAutofillFacade.getOfcInstallationCost();
                String popName = ServiceDAOFactory.getService(InventoryService.class)
                        .getInventoryItemByItemID(t.getPopID()).getName();



                long afterDistances = t.getBTCLDistances() + t.getOCDistances();
                long beforeDistances = t.getBTCLDistances() + t.getOCDistances() - t.getAdjustedBTClDistance() - t.getAdjustedOCDistance();
                double after_cost = DemandNoteAutofillFacade.calculateLocalLoopCost(afterDistances,
                        ofcInstallationCostDTO.getFiberLength(), ofcInstallationCostDTO.getFiberCost(), t.getOfcType(), factor
                );

                double before_cost = DemandNoteAutofillFacade.calculateLocalLoopCost(beforeDistances,
                        ofcInstallationCostDTO.getFiberLength(), ofcInstallationCostDTO.getFiberCost(), t.getOfcType(), factor
                );
                // after distance is fixed
                map.putIfAbsent("popName", popName);
                map.putIfAbsent("ofcType", (t.getOfcType() == 1 ? "Single" : (t.getOfcType() == 2 ? "Double" : "N/A")));
                map.putIfAbsent("factor", String.valueOf(factor));
                map.putIfAbsent("after_btclDistance", String.valueOf(t.getBTCLDistances()));
                map.putIfAbsent("after_ocDistance", String.valueOf(t.getOCDistances()));
                map.putIfAbsent("after_totalDistance", String.valueOf(afterDistances));
                map.putIfAbsent("after_totalCost", String.valueOf(after_cost));

                map.putIfAbsent("before_btclDistance", String.valueOf(t.getBTCLDistances() - t.getAdjustedBTClDistance()));
                map.putIfAbsent("before_ocDistance", String.valueOf(t.getOCDistances() - t.getAdjustedOCDistance()));
                map.putIfAbsent("before_totalDistance", String.valueOf(beforeDistances));
                map.putIfAbsent("before_totalCost", String.valueOf(before_cost));

                map.putIfAbsent("minimumLength", String.valueOf(ofcInstallationCostDTO.getFiberLength()));
                map.putIfAbsent("minimumCost", String.valueOf(ofcInstallationCostDTO.getFiberCost()));
                list.add(map);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        return list;
    }
    private List<Map<String, String>> calculateLocalLoopChargeNewForNewLoopCase (List<NewLocalLoop> localLoops) {
        List<LoopBreakdownEssentialData> essentialData = localLoops.stream()
                .map(t->
                        LoopBreakdownEssentialData.builder()
                       .popID(t.getPopID())
                       .ofcType((int) t.getOfcType())
                       .BTCLDistances(t.getBTCLDistances())
                       .OCDistances(t.getOCDistances())
                       .adjustedBTClDistance(t.getAdjustedBTClDistance())
                       .adjustedOCDistance(t.getAdjustedOCDistance())
                       .build())
                .collect(Collectors.toList());
        return calculateLocalLoopBreakdown(essentialData);
    }
    private List<Map<String, String>> calculateLocalLoopChargeNew (List<LocalLoop> localLoops) {
        List<LoopBreakdownEssentialData> essentialData = localLoops.stream()
                .map(t->
                        LoopBreakdownEssentialData.builder()
                                .popID(t.getPopID())
                                .ofcType((int) t.getOfcType())
                                .BTCLDistances(t.getBTCLDistances())
                                .OCDistances(t.getOCDistances())
                                .adjustedBTClDistance(t.getAdjustedBTClDistance())
                                .adjustedOCDistance(t.getAdjustedOCDistance())
                                .build())
                .collect(Collectors.toList());
        return calculateLocalLoopBreakdown(essentialData);
    }

    private double calculateLocalLoopCharge(List<LocalLoop> localLoops) throws Exception {
        return Math.round(localLoops.stream()
                .mapToDouble(lliLocalLoop -> {
                    try {
                        return DemandNoteAutofillFacade.calculateLocalLoopCost(lliLocalLoop);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info("Returning 0");
                        return 0;
                    }
                })
                .sum());
    }



    private double getLocalLoopCharge(long applicationId, LLIConnectionInstance existingConnection) throws Exception {
        List<LocalLoop> freshLocalLoops = getFreshLocalLoops(applicationId, existingConnection);

        double extraLocalLoopChargeForExistingLocalLoops = getExtraLocalLoopCharge(applicationId, existingConnection);
        double totalFreshLocalLoopCharge = calculateLocalLoopCharge(freshLocalLoops);
        return extraLocalLoopChargeForExistingLocalLoops + totalFreshLocalLoopCharge;
    }

    private LLINewConnectionDemandNote getShiftBWNewConnectionDN(LLIApplication lliApplication, LLINewConnectionDemandNote demandNote,
                                                                 LLIFixedCostConfigurationDTO costDTO, int numberOfBTCLProvidedFiberCount) throws Exception {
        // info
        demandNote.setClientID(lliApplication.getClientID());
        demandNote.setAdvanceAdjustment(0);
        demandNote.setDescription("Demand Note of application ID " + lliApplication.getApplicationID());

        // charge
        demandNote.setSecurityMoney(0);
        demandNote.setRegistrationFee(0);
        demandNote.setBwMRC(0);
        demandNote.setVatPercentage(costDTO.getMaximumVatPercentage());
        demandNote.setDiscountPercentage(0);

        // fibre and local loop charge applicable
        demandNote.setFibreOTC(numberOfBTCLProvidedFiberCount * costDTO.getFiberCharge());
        List<LocalLoop> localLoops = localLoopService.getLocalLoop(lliApplication.getApplicationID());
        double localLoopCharge = calculateLocalLoopCharge(localLoops);
//        List<KeyValuePair<LocalLoop, Double>> localLoopCharges = calculateLocalLoopChargeNew(localLoops);
//        double totalLocalLoopCharge = localLoopCharges.stream()
//                .mapToDouble(KeyValuePair::getValue)
//                .sum();
//        demandNote.setLocalLoopCharge(totalLocalLoopCharge);

        demandNote.setLocalLoopCharge(localLoopCharge);

        return demandNote;
    }

    private void AdjustDemandNoteAdjustment(BillDTO demandNote, long appId) throws Exception {

        LLIApplication lliApplication = null;
        try {
            lliApplication = lliApplicationService.getLLIApplicationByApplicationID(appId);
        } catch (Exception e) {
        }

        if(lliApplication == null)
            return;

        int connectionType = lliApplication.getConnectionType();

        if(connectionType == LLIConnectionConstants.CONNECTION_TYPE_REGULAR_LONG)
            connectionType = LLIConnectionConstants.CONNECTION_TYPE_REGULAR;

        if(! (  connectionType == LLIConnectionConstants.CONNECTION_TYPE_CACHE
                || connectionType == LLIConnectionConstants.CONNECTION_TYPE_REGULAR)){
            return;
        }


        double localLoopCharge = 0, bwCharge = 0;
        long billId = demandNote.getID();

        if(demandNote instanceof LLINewConnectionDemandNote)
        {
            bwCharge = ((LLINewConnectionDemandNote)demandNote).getBwMRC();
            localLoopCharge = ((LLINewConnectionDemandNote)demandNote).getLocalLoopCharge();
        }
        else if(demandNote instanceof LLISingleConnectionCommonDemandNote)
        {
            bwCharge = ((LLISingleConnectionCommonDemandNote)demandNote).getBandwidthCharge();
            localLoopCharge = ((LLISingleConnectionCommonDemandNote)demandNote).getCoreCharge();
        }


        //insert
        LLIDemandNoteAdjustment dnAdjust = null;

        if(lliApplication.getSkipPayment() == 1)
        {
            dnAdjust = LLIDemandNoteAdjustment.builder()
                    .clientId(demandNote.getClientID())
                    .billId(billId)
                    .connectionType(connectionType)
                    .bandWidthCharge(bwCharge)
                    .bandWidthDiscount(demandNote.getDiscount())
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
            dnAdjust = LLIDemandNoteAdjustment.builder()
                    .clientId(demandNote.getClientID())
                    .billId(billId)
                    .connectionType(connectionType)
                    .bandWidthCharge(bwCharge)
                    .bandWidthDiscount(demandNote.getDiscount())
                    .loopCharge(localLoopCharge)
                    .status(DNAdjustStatus.PENDING)
                    .build();
        }


        lliDemandNoteAdjustmentService.save(dnAdjust);

    }

    public String getFilePath(String proposedName, long generationTime) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(FileTypeConstants.BASE_PATH)
                .append(FileTypeConstants.LLI_BILL_DIRECTORY)
                .append(TimeConverter.getYear(generationTime))
                .append(File.separatorChar)
                .append(TimeConverter.getMonth(generationTime))
                .append(File.separatorChar);

        File file = FileAPI.getInstance().createDirectory(sb.toString());
        return file.getPath() + File.separatorChar + proposedName;
    }

    public void populateClientInfoForPDF(Map<String, Object> params, long clientId) throws Exception {
        KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair =
                ClientAPI.getInstance().getPairOfClientDetailsAndClientContactDetails(
                        clientId, ModuleConstants.Module_ID_LLI, ClientContactDetailsDTO.BILLING_CONTACT);
        ClientDetailsDTO clientDetailsDTO = pair.key;
        ClientContactDetailsDTO contactDetailsDTO = pair.value;

        params.put("clientFullName", contactDetailsDTO.getRegistrantsName() + " " + contactDetailsDTO.getRegistrantsLastName());
        params.put("clientAddress", contactDetailsDTO.getAddress());
        params.put("clientEmail", contactDetailsDTO.getEmail() != null ? contactDetailsDTO.getEmail() : "N/A");
        params.put("clientLoginName", clientDetailsDTO.getLoginName());

    }

    public void populateFooterInfoForPDF(Map<String, Object> params) {

        params.put("footerLeft", "Powered By Reve Systems");
        params.put("footerRight", "Bangladesh Telecommunications Company Limited");
        params.put("NB", "NB: ( Client will arrange and maintain local loops from BTCL switch to his offices on his own ).");
    }

    public void populateBillInfoForPDF(Map<String, Object> params, BillDTO billDTO) {
        params.put("billGenerationDate", TimeConverter.getDateTimeStringByMillisecAndDateFormat(billDTO.getGenerationTime(), "dd/MM/yyyy"));
        params.put("billLastPaymentDate", TimeConverter.getDateTimeStringByMillisecAndDateFormat(billDTO.getLastPaymentDate(), "dd/MM/yyyy"));
        params.put("invoiceID", billDTO.getID() + "");
        params.put("discount", String.format("%.2f", billDTO.getDiscount()));
        params.put("VAT", String.format("%.2f", billDTO.getVAT()));
        params.put("total", String.format("%.2f", billDTO.getNetPayable()));
        params.put("demandedAmount", EnglishNumberToWords.convert((long) Math.ceil(billDTO.getNetPayable())));

    }

    public void populateLLIOwnershipChangeApplicationInfoForPDF(Map<String, Object> params, LLIOwnerShipChangeApplication lliApplication) throws Exception {
        boolean isSkipped = lliApplication.getSkipPayment() == 1L;
        params.put("skipped", isSkipped);
        params.put("logo", "../../images/common/btcl_logo_heading.png");


        String sourceClientName = AllClientRepository.getInstance().getClientByClientID(lliApplication.getSrcClient()).getLoginName();
        String destinationClientName = AllClientRepository.getInstance().getClientByClientID(lliApplication.getDstClient()).getLoginName();

        String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
        String demandNoteCause = "In the context of your application in <b>"
                + clientSuggestedDate + "</b> demand note of changing ownership from <b>"
                + sourceClientName + "</b> to <b>"
                + destinationClientName
                + "</b> has been issued.";
        params.put("sourceClient", sourceClientName);
        params.put("destinationClient", destinationClientName);
        params.put("demandNoteCause", demandNoteCause);
    }
    public void populateLLIApplicationInfoForPDF(Map<String, Object> params, LLIApplication lliApplication, long billId) throws Exception {

        boolean isSkipped = lliApplication.getSkipPayment() == 1L;
        params.put("skipped", isSkipped);
        params.put("logo", !isSkipped ? "../../images/common/btcl_logo_heading.png" : "../../images/common/btcl_logo_heading_skipped.png");

        //updated
        int lliApplicationType = lliApplication.getApplicationType();
        String demandNoteCause = "";
        String clientSuggestedBW = "";
        String clientSuggestedDate = "";
        LLIConnectionInstance lliConnectionInstance = lliConnectionService.getLLIConnectionByConnectionID(lliApplication.getConnectionId());
        if(lliConnectionInstance == null) {
            long appId = lliApplication.getApplicationID();
            Office office =  ServiceDAOFactory.getService(OfficeService.class).getOffice(
                    lliApplication.getApplicationID()).stream().findFirst().orElseThrow(()->new RequestFailureException("No Office Found for LLI Application Id " + appId)
            );
            params.put("connectionAddress", office.getOfficeAddress());
            params.put("connectionName",  "Not Provided" );
        }else {
            params.put("connectionAddress", lliConnectionInstance.getLliOffices().get(0).getAddress());
            params.put("connectionName", lliConnectionInstance.getName().isEmpty() ? "Not Provided" : lliConnectionInstance.getName());
        }

        if (lliApplicationType == LLIConnectionConstants.NEW_CONNECTION) {
            clientSuggestedDate += TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
            clientSuggestedBW += lliApplication.getBandwidth() + " Mbps";
            demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of <b>"
                    + clientSuggestedBW + "</b> Duplex Leased Line Internet Connection has been issued.";

            params.put("demandNoteCause", demandNoteCause);
        }  else if (lliApplicationType == LLIConnectionConstants.CLOSE_CONNECTION) {
            LLIConnection connection = lliFlowConnectionService.getConnectionByID(lliApplication.getConnectionId());
            clientSuggestedDate += TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
            demandNoteCause += "In the context of your application in <b>"
                    + clientSuggestedDate + "</b> Demand Note for" +
                    " instant closing of connection <b>" + connection.getName() + "</b> "
                    + "has been issued.";
            params.put("demandNoteCause", demandNoteCause);
        } /*else if (lliApplicationType == LLIConnectionConstants.CHANGE_OWNERSHIP) {

            LLIChangeOwnershipApplication lliChangeOwnershipApplication = (LLIChangeOwnershipApplication) lliApplication;
            String sourceClientName = AllClientRepository.getInstance().getClientByClientID(lliChangeOwnershipApplication.getClientID()).getLoginName();
            String destinationClientName = AllClientRepository.getInstance().getClientByClientID(lliChangeOwnershipApplication.getDestinationClientID()).getLoginName();

            clientSuggestedDate += TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
            demandNoteCause += "In the context of your application in <b>"
                    + clientSuggestedDate + "</b> demand note of changing lli.Application.ownership from <b>"
                    + sourceClientName + "</b> to <b>"
                    + destinationClientName
                    + "</b> has been issued.";
            params.put("sourceClient", sourceClientName);
            params.put("destinationClient", destinationClientName);
            params.put("demandNoteCause", demandNoteCause);

        } */else if (lliApplicationType == LLIConnectionConstants.SHIFT_BANDWIDTH_NEW_CONNECTION) {
            clientSuggestedDate += TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
            clientSuggestedBW += lliApplication.getBandwidth() + " Mbps";

//            demandNoteCause += "<b>" + clientSuggestedDate + "</b>: Demand Note for <b>"
//                    + clientSuggestedBW + "</b> Shift Bandwidth New Connection";
            demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of <b>"
                    + clientSuggestedBW + "</b> Duplex Leased Line Internet Connection has been issued.";
            params.put("connectionName", "");
            params.put("demandNoteCause", demandNoteCause);
        } else if (lliApplicationType == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP) {

            EFR efr = efrService.getSelected(lliApplication.getApplicationID()).stream().findFirst().orElse(new EFR());
            InventoryItem inventoryItem = ServiceDAOFactory.getService(InventoryService.class).getInventoryItemByItemID(efr.getPopID());
            if (inventoryItem.getID() == 0) {
                throw new RequestFailureException("No Pop Found");
            }

            demandNoteCause += "<b>" + clientSuggestedDate + "</b> Demand Note for" + " Additional Local Loop from <b>" + inventoryItem.getName() + "</b> POP<b>";
            params.put("demandNoteCause", demandNoteCause);
        } else if (lliApplicationType == LLIConnectionConstants.ADDITIONAL_PORT) {

            IFR ifr = ifrService.getSelectedIFRByAppID(lliApplication.getApplicationID()).stream().findFirst().orElse(new IFR());

            InventoryItem inventoryItem = ServiceDAOFactory.getService(InventoryService.class).getInventoryItemByItemID(ifr.getPopID());
            if (inventoryItem.getID() == 0) {
                throw new RequestFailureException("No Pop Found");
            }

            demandNoteCause += "<b>" + clientSuggestedDate + "</b> Demand Note for" + " Additional Local Loop from <b>" + inventoryItem.getName() + "</b> POP<b>";
            params.put("demandNoteCause", demandNoteCause);
        } else if (lliApplicationType == LLIConnectionConstants.ADDITIONAL_IP) {
            LLIAdditionalIP lliAdditionalIP = lliAdditionalIPService.getAdditionalIPByApplication(lliApplication.getApplicationID());
            int ipCount = lliAdditionalIP == null ? 0 : lliAdditionalIP.getIpCount();
            demandNoteCause += " <b>" + clientSuggestedDate + "</b> Demand Note for <b>" + ipCount + " additional IPs </b>";
            params.put("demandNoteCause", demandNoteCause);
            params.put("firstIPCount", ipCount <= 4 ? String.valueOf(ipCount): "4");
            params.put("nextIPCount", ipCount >4 ? String.valueOf(ipCount-4): "0");
        } else {


            clientSuggestedDate += TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");

            LLIOffice office = lliConnectionInstance.getLliOffices().stream().findFirst().orElse(null);
            String clientOfficeAddress = (office != null ? office.getAddress() : "client address");

            clientSuggestedBW += lliApplication.getBandwidth() + " Mbps";
            String existingBW = lliConnectionInstance.getBandwidth() + " Mbps";

            if (lliApplicationType == LLIConnectionConstants.UPGRADE_BANDWIDTH) {
                String finalBW = (lliConnectionInstance.getBandwidth() + lliApplication.getBandwidth()) + " Mbps";
                demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of additional <b>"
                        + clientSuggestedBW + "</b> along with <b>" + existingBW + "</b> i.e. <b>" + finalBW + " Mbps</b>"
                        + " Duplex Leased Line Internet Connection in <b>" + clientOfficeAddress + "</b> has been issued.";

            } else if (lliApplicationType == LLIConnectionConstants.DOWNGRADE_BANDWIDTH) {
                String finalBW = (lliConnectionInstance.getBandwidth() - lliApplication.getBandwidth()) + " Mbps";
                demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of downgrading <b>"
                        + clientSuggestedBW + "</b> from <b>" + existingBW + "</b> i.e. <b>" + finalBW + " Mbps</b>"
                        + " Duplex Leased Line Internet Connection in <b>" + clientOfficeAddress + "</b> has been issued.";

            } else if (lliApplicationType == LLIConnectionConstants.TEMPORARY_UPGRADE_BANDWIDTH) {
                String finalBW = (lliConnectionInstance.getBandwidth() + lliApplication.getBandwidth()) + " Mbps";
                demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of additional <b>"
                        + clientSuggestedBW + "</b> along with <b>" + existingBW + "</b> i.e. <b>" + finalBW + " Mbps</b>"
                        + " for <b>" + lliApplication.getDuration() + "</b> days"
                        + " in <b>" + clientOfficeAddress + "</b> has been issued.";

            } else if (lliApplicationType == LLIConnectionConstants.ADDITIONAL_CONNECTION_ADDRESS) {
                // TODO:maruf: inheritence needs to be fixed, might not work
                LLIAdditionalConnectionAddressApplication lliAdditionalConnectionAddressApplication = (LLIAdditionalConnectionAddressApplication) lliApplication;
                demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of"
                        + " additional connection address in <b>" + lliAdditionalConnectionAddressApplication.getAddress()
                        + "</b> has been issued.";

            } else if (lliApplicationType == LLIConnectionConstants.SHIFT_CONNECTION_ADDRESS) {
                // TODO:maruf: inheritence needs to be fixed, might not work
                LLIShiftAddressApplication lliShiftAddressApplication = (LLIShiftAddressApplication) lliApplication;
                LLIOffice lliOffice = lliConnectionInstance.getLliOffices()
                        .stream()
                        .filter(o -> o.getID() == lliShiftAddressApplication.getOfficeID())
                        .findFirst()
                        .orElse(null);
                if (lliOffice == null) {
                    throw new RequestFailureException("No Existing Office Found");
                }

                demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of"
                        + " shifting connection address in <b>" + lliShiftAddressApplication.getAddress()
                        + "</b> has been issued.";

            } else if (lliApplicationType == LLIConnectionConstants.SHIFT_POP) {
                // TODO:maruf: inheritence needs to be fixed, might not work
                LLIShiftPopApplication lliShiftPOPApplication = (LLIShiftPopApplication) lliApplication;
                InventoryItem inventoryItem = ServiceDAOFactory.getService(InventoryService.class).getInventoryItemByItemID(lliShiftPOPApplication.getPopID());
                if (inventoryItem.getID() == 0) {
                    throw new RequestFailureException("No Pop Found");
                }

                demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of"
                        + " shift POP to <b>" + inventoryItem.getName()
                        + "</b> has been issued.";

            }
            params.put("connectionName", lliConnectionInstance.getName().isEmpty() ? "Not Provided" : lliConnectionInstance.getName());
            params.put("demandNoteCause", demandNoteCause);
        }
    }

    public void populateLLIApplicationInfoForPDF(Map<String, Object> params, ReviseDTO reviseDTO, long billId) throws Exception {
        if (reviseDTO == null) {
            reviseDTO = reviseService.getApplicationByDemandNoteId(billId);
        }

        params.put("skipped", false);

        params.put("logo", "../../images/common/btcl_logo_heading.png");

        int lliApplicationType = reviseDTO.getApplicationType();
        String demandNoteCause = "";
        String clientSuggestedDate = "";

        switch (lliApplicationType){
            case LLIConnectionConstants.BREAK_LONG_TERM:
                clientSuggestedDate += TimeConverter.getDateTimeStringByMillisecAndDateFormat(reviseDTO.getSuggestedDate(), "dd/MM/yyyy");
                demandNoteCause += "In the context of your application in <b>"
                        + clientSuggestedDate + "</b> Short Bill for breaking long term contract ("
                        + "contract id: " + reviseDTO.getReferenceContract() + ") "
                        + "has been issued.";

                params.put("contractID", reviseDTO.getReferenceContract() + "");
                params.put("brokenBandwidth", reviseDTO.getBandwidth() + " Mbps");
                params.put("demandNoteCause", demandNoteCause);
                params.put("connectionAddress", "N/A");
                break;
            case LLIConnectionConstants.RECONNECT:
                clientSuggestedDate += TimeConverter.getDateTimeStringByMillisecAndDateFormat(reviseDTO.getSuggestedDate(), "dd/MM/yyyy");
                demandNoteCause += "In the context of your application in <b>"
                        + clientSuggestedDate + "</b> demand note of reconnect connection for <b>"
                        + AllClientRepository.getInstance().getClientByClientID(reviseDTO.getClientID()).getLoginName()
                        + "</b> has been issued.";
                params.put("connectionName", "");
                params.put("demandNoteCause", demandNoteCause);
                params.put("connectionAddress", "N/A");
                break;
        }
    }

    private LLISingleConnectionCommonDemandNote additionalLoopPortDN(LLIApplication lliApplication, LLIConnectionInstance existingConn, LLIFixedCostConfigurationDTO costDTO) throws Exception {
        LLISingleConnectionCommonDemandNote demandNote = new LLISingleConnectionCommonDemandNote();
        demandNote.setDiscountPercentage(existingConn.getDiscountRate());
        demandNote.setClientID(lliApplication.getClientID());
        demandNote.setDescription("Demand Note of application ID " + lliApplication.getApplicationID());

        List<EFR> efrs = efrService.getSelected(lliApplication.getApplicationID());
        AdditionalPort additionalPort = lliAdditionalPortService.getAdditionalPortByApplication(lliApplication.getApplicationID());

        int portCount = ServiceDAOFactory.getService(GlobalService.class).getAllObjectListByCondition(
                IFR.class, new IFRConditionBuilder()
                        .Where()
                        .applicationIDEquals(lliApplication.getApplicationID())
                        .isSelectedEquals(1)
                        .getCondition()
        ).size();
        
        demandNote.setPortCharge(portCount * costDTO.getPortCharge());
        demandNote.setVatPercentage(costDTO.getMaximumVatPercentage());

        List<NewLocalLoop> newLocalLoops  = ServiceDAOFactory.getService(NewLocalLoopService.class).getLocalLoop(lliApplication.getApplicationID());
        double loopCharge = ServiceDAOFactory.getService(DemandNoteAutofillFacade.class).calculateLocalLoopCostFromLLIApplicationLocalLoop(newLocalLoops);
        demandNote.setCoreCharge( loopCharge );
        demandNote.setFibreOTC(
                newLocalLoops.stream()
                .filter(t->t.getOCDistances() + t.getBTCLDistances()>0)
                .count() * costDTO.getFiberCharge()
        );

        return demandNote;
    }

    // TODO: verify calculation
    private LLISingleConnectionCommonDemandNote additionalIPDN(LLIApplication lliApplication, LLISingleConnectionCommonDemandNote demandNote, LLIFixedCostConfigurationDTO costDTO) throws Exception {
        LLIAdditionalIP lliAdditionalIP = lliAdditionalIPService.getAdditionalIPByApplication(lliApplication.getApplicationID());
        int ipCount = lliAdditionalIP == null ? 0 : lliAdditionalIP.getIpCount();
        if(ipCount<=costDTO.getFixedIpCount()) {
            demandNote.setFirstXIpCost(ipCount * costDTO.getFixedIpCharge());
            demandNote.setNextYIpCost(0);
        }else {
            demandNote.setFirstXIpCost(costDTO.getFixedIpCount() * costDTO.getFixedIpCharge());
            demandNote.setNextYIpCost(Math.max((ipCount - costDTO.getFixedIpCount()) * costDTO.getVariableIpCharge(), 0));
        }

        return demandNote;
    }

    private void saveDemandNoteAsOfficialLetter(LLIApplication lliApplication, LoginDTO loginDTO, String className) throws Exception {
        if(lliApplication == null){
            logger.error(" [ XX ] Saving demand note as official letter failed. LLIApplication is NULL.");
            return;
        }

        OfficialLetter officialLetter = new OfficialLetter();
        officialLetter.setOfficialLetterType(OfficialLetterType.DEMAND_NOTE);
        officialLetter.setClassName(className);
        officialLetter.setModuleId(ModuleConstants.Module_ID_LLI);
        officialLetter.setApplicationId(lliApplication.getApplicationID());
        officialLetter.setClientId(lliApplication.getClientID());
        officialLetter.setLastModificationTime(System.currentTimeMillis());

        long clientUserId = -99;
        try {
//            clientUserId = clientService.getClientInfoFromApplicationId(lliApplication.getApplicationID()).getUserId();
            clientUserId = clientService.getClientInfoFromApplicationId(lliApplication.getApplicationID()).getClientId();
        }catch(Exception ex){
            logger.error(" [ XX ] " + ex.toString());
        }

//        UserDTO clientUserDTO = userService.getUserDTOByUserID(clientUserId);
        ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(clientUserId);
        if(clientDTO == null){
            throw new RequestFailureException("No Client Found by id " + clientUserId);
        }
        UserDTO cdgmUserDTO = userService.getCDGMUserDTO();
        UserDTO loggedInUserDTO = userService.getUserDTOByUserID(loginDTO.getUserID());

        List<RecipientElement> recipientElements = new ArrayList<>();
        if(clientDTO != null) recipientElements.add(RecipientElement.getRecipientElementFromClientAndReferType(clientDTO, ReferType.TO));
        if(cdgmUserDTO != null) recipientElements.add(RecipientElement.getRecipientElementFromUserAndReferType(cdgmUserDTO, ReferType.CC));
        if(loggedInUserDTO != null) recipientElements.add(RecipientElement.getRecipientElementFromUserAndReferType(loggedInUserDTO, ReferType.CC));

        officialLetterService.saveOfficialLetterTransactionalDefault(officialLetter, recipientElements, loginDTO.getUserID());
    }

    private void saveDemandNoteAsOfficialLetter(ReviseDTO reviseDTO, LoginDTO loginDTO, String className) throws Exception {
        if(reviseDTO == null){
            logger.error(" [ XX ] Saving demand note as official letter failed. ReviseDTO is NULL.");
            return;
        }

        OfficialLetter officialLetter = new OfficialLetter();
        officialLetter.setOfficialLetterType(OfficialLetterType.DEMAND_NOTE);
        officialLetter.setClassName(className);
        officialLetter.setModuleId(ModuleConstants.Module_ID_LLI);
        officialLetter.setApplicationId(reviseDTO.getId());
        officialLetter.setClientId(reviseDTO.getClientID());
        officialLetter.setLastModificationTime(System.currentTimeMillis());

        long clientUserId = -99;
        try {
            clientUserId = clientService.getClientInfoByReviseApplication(reviseDTO.getId()).getClientId();
        }catch(Exception ex){
            logger.error(" [ XX ] " + ex.toString());
        }

//        UserDTO clientUserDTO = userService.getUserDTOByUserID(clientUserId);
        ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(clientUserId);
        UserDTO cdgmUserDTO = userService.getCDGMUserDTO();
        UserDTO loggedInUserDTO = userService.getUserDTOByUserID(loginDTO.getUserID());

        List<RecipientElement> recipientElements = new ArrayList<>();
        if(clientDTO != null) recipientElements.add(RecipientElement.getRecipientElementFromClientAndReferType(clientDTO, ReferType.TO));
        if(cdgmUserDTO != null) recipientElements.add(RecipientElement.getRecipientElementFromUserAndReferType(cdgmUserDTO, ReferType.CC));
        if(loggedInUserDTO != null) recipientElements.add(RecipientElement.getRecipientElementFromUserAndReferType(loggedInUserDTO, ReferType.CC));

        officialLetterService.saveOfficialLetterTransactionalDefault(officialLetter, recipientElements, loginDTO.getUserID());
    }
}
