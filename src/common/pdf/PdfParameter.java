package common.pdf;

import api.ClientAPI;
import application.ApplicationType;
import client.classification.ClientClassificationService;
import coLocation.accounts.VariableCost.VariableCostDTO;
import coLocation.accounts.VariableCost.VariableCostService;
import coLocation.accounts.commonCost.AllVariableUnitCharge;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.connection.CoLocationConnectionDTO;
import coLocation.connection.CoLocationConnectionService;
import coLocation.demandNote.CoLocationDemandNote;
import coLocation.demandNote.CoLocationYearlyDemandNote;
import coLocation.inventory.CoLocationInventoryTemplateDTO;
import coLocation.inventory.CoLocationInventoryTemplateService;
import common.ApplicationGroupType;
import common.ModuleConstants;
import common.RequestFailureException;
import common.UniversalDTOService;
import common.bill.BillDTO;
import common.bill.BillService;
import common.payment.PaymentDTO;
import common.payment.PaymentService;
import common.payment.constants.PaymentConstants;
import costConfig.CostConfigService;
import costConfig.TableDTO;
import entity.facade.AdviceNoteGenerationFacade;
import entity.facade.DemandNoteAutofillFacade;
import entity.facade.WorkOrderGenerationFacade;
import entity.localloop.LocalLoop;
import exception.NoDataFoundException;
import global.GlobalService;
import inventory.InventoryConstants;
import inventory.InventoryItem;
import inventory.InventoryService;
import ip.IPBlockLLI;
import ip.IPConstants;
import ip.IPService;
import ip.IPUtility;
import ip.ipUsage.IPBlockUsage;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRBean;
import lli.Application.EFR.EFRService;
import lli.Application.LLIApplication;
import lli.Application.Office.Office;
import lli.Application.Office.OfficeService;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import lli.LLILongTermService;
import lli.LLIOffice;
import lli.connection.LLIConnectionConstants;
import lombok.extern.log4j.Log4j;
import nix.application.NIXApplication;
import nix.application.close.NIXCloseApplication;
import nix.application.close.NIXCloseApplicationService;
import nix.application.downgrade.NIXDowngradeApplication;
import nix.application.downgrade.NIXDowngradeApplicationService;
import nix.application.office.NIXApplicationOffice;
import nix.application.office.NIXApplicationOfficeService;
import nix.application.upgrade.NIXUpgradeApplication;
import nix.application.upgrade.NIXUpgradeApplicationService;
import nix.connection.NIXConnection;
import nix.connection.NIXConnectionService;
import nix.constants.NIXConstants;
import nix.demandnote.NIXDemandNote;
import nix.revise.NIXReviseDTO;
import officialLetter.OfficialLetter;
import officialLetter.OfficialLetterConcern;
import officialLetter.OfficialLetterService;
import officialLetter.ReferType;
import user.UserDTO;
import user.UserRepository;
import util.EnglishNumberToWords;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import util.TimeConverter;
import vpn.VPNConstants;
import vpn.VPNOTC;
import vpn.adviceNote.DemandNotePayment;
import vpn.adviceNote.InventoryInfo;
import vpn.adviceNote.LinkInfo;
import vpn.adviceNote.WorkOrderInfo;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationLink;
import vpn.application.VPNApplicationService;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;
import vpn.demandNote.RemoteEndInfo;
import vpn.demandNote.VPNDemandNote;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j
public class PdfParameter {
    private static final String logoWithDowrySloganPath = "../../images/common/btcl_logo_heading.png";
    private static final String logoWithBTCLAim = "../../images/common/logo_BTCL_Aim.png";
//    private static final String logoWithBTCLAim = "C:/Users/REVE/Desktop/logo_BTCL_Aim.png";

    public static void populateCoLocationAdviceTopInfo(Map<String, Object> map) {

        map.put("an_title", "Co-Location Advice Note");
        map.put("an_letter_body", "Therefore, you are requested to allocate/transfer/extend and/or"
                + " take measure and back the report accordingly."
        );
    }

    public static void populateNIXAdiceNoteTopInfo(Map<String, Object> map) {
        map.put("an_title", "Lease Line Internet Advice Note");
        map.put("an_letter_body", "Therefore, you are requested to allocate/transfer/extend and/or"
                + " take measure and back the report accordingly.");
    }

    public static void populateLLITDReconnectAdviceNoteTopInfo(Map<String, Object> map) {
        map.put("an_title", "Lease Line Internet Advice Note");
        map.put("an_letter_body", "Therefore, you are requested to allocate/transfer/extend and/or"
                + " take measure and back the report accordingly.");
    }

    public static void populateLLIOwnershipChangeAdviceNoteTopInfo(Map<String, Object> map) {
        map.put("an_title", "Lease Line Internet Advice Note");
        map.put("an_letter_body", "Therefore, you are requested to allocate/transfer/extend and/or"
                + " take measure and back the report accordingly.");
    }


    public static void populateAdviceNoteTopInfo(Map<String, Object> map, OfficialLetter officialLetter, String applicationType, boolean isServiceStarted) {
        map.put("logo", logoWithDowrySloganPath);
        map.put("an_number", String.valueOf(officialLetter.getId()));
        map.put("an_date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(officialLetter.getCreationTime(), "dd/MM/yyyy"));
        map.put("an_status", isServiceStarted ? "Completed" : "Generated"); // ToDo needs solution.
        map.put("app_id", String.valueOf(officialLetter.getApplicationId()));
        map.put("app_type", applicationType);

    }

    public static void populateLLIAdviceNoteTopInfo(Map<String, Object> map,
                                                    LLIApplication lliApplication, 
                                                    LLIConnectionInstance dest,
                                                    LLIConnectionInstance src) throws Exception {

        String date = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSuggestedDate(),"dd-MM-yyy");
        
        map.put("an_title", "Lease Line Internet Advice Note");

        if(lliApplication.getApplicationType() == LLIConnectionConstants.CLOSE_CONNECTION) {
            LLIOffice lliOffice = dest.getLliOffices().stream().findFirst().orElse(null);
            if(lliOffice != null) {
                map.put("an_letter_body",
                        "You are requested to close the LLI connection " +
                                "at address " +
                        lliOffice.getAddress() +
                        " preferably within " +
                        date +
                        " and back the report accordingly."
                );
            }
            return;

        }
        Office office = ServiceDAOFactory.getService(OfficeService.class)
                .getOffice(lliApplication.getApplicationID())
                .stream()
                .findFirst()
                .orElse(null);


        if(office == null) {
            office = ServiceDAOFactory.getService(OfficeService.class)
                    .getOfficeByCON(dest.getID())
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RequestFailureException("No Office info found by both app id " + lliApplication.getApplicationID() + " and connection id "
                            + dest.getID()));
        }


        switch (lliApplication.getApplicationType()) {

            case LLIConnectionConstants.NEW_CONNECTION:
                map.put("an_letter_body",
                        "You are requested to allocate a new LLI connection of " +
                                lliApplication.getBandwidth() +
                                " Mbps bandwidth " +
                                "at address " +
                                office.getOfficeAddress() +
                                " preferably within " +
                                date +
                                " and back the report accordingly."
                );
                break;

            case LLIConnectionConstants.UPGRADE_BANDWIDTH:
                map.put("an_letter_body",
                        "You are requested to upgrade the bandwidth of the LLI connection at address " +
                                office.getOfficeAddress() +
                                " from " +
                                dest.getBandwidth() +
                                " Mbps to " +
                                (dest.getBandwidth() + lliApplication.getBandwidth()) +
                                " Mbps (" + dest.getBandwidth() + "+" + lliApplication.getBandwidth() + "=" + (dest.getBandwidth() + lliApplication.getBandwidth()) + ")" +
                                " preferably within " +
                                date +
                                " and back the report accordingly."
                );
                break;

            case LLIConnectionConstants.DOWNGRADE_BANDWIDTH:
                map.put("an_letter_body",
                        "You are requested to downgrade the bandwidth of the LLI connection at address " +
                                office.getOfficeAddress() +
                                " from " +
                                dest.getBandwidth() +
                                " Mbps to " +
                                (dest.getBandwidth() - lliApplication.getBandwidth()) +
                                " Mbps (" + dest.getBandwidth() + "-" + lliApplication.getBandwidth() + "=" + (dest.getBandwidth() - lliApplication.getBandwidth()) + ")" +
                                " preferably within " +
                                date +
                                " and back the report accordingly."
                );
                break;

            case LLIConnectionConstants.TEMPORARY_UPGRADE_BANDWIDTH:
                map.put("an_letter_body",
                        "You are requested to upgrade the bandwidth temporarily of the LLI connection at address " +
                                office.getOfficeAddress() +
                                " from " +
                                dest.getBandwidth() +
                                " Mbps to " +
                                (dest.getBandwidth() + lliApplication.getBandwidth()) +
                                " Mbps (" + dest.getBandwidth() + "+" + lliApplication.getBandwidth() + "=" + (dest.getBandwidth() + lliApplication.getBandwidth()) + ")" +
                                " preferably within " +
                                date +
                                " and back the report accordingly."
                );
                break;


            case LLIConnectionConstants.SHIFT_BANDWIDTH_NEW_CONNECTION:
                if (src == null) {
                    throw new RequestFailureException("No source connection found");
                }
                map.put("an_letter_body",
                        "You are requested to Shift " +
                                lliApplication.getBandwidth() +
                                " Mbps from connection " +
                                src.getName() +
                                " at address " +
                                office.getOfficeAddress() +
                                " to a new connection " +
                                " preferably within " +
                                date +
                                " and back the report accordingly."
                );
                break;
            case LLIConnectionConstants.SHIFT_BANDWIDTH:
                if (src == null) {
                    throw new RequestFailureException("No source connection found");
                }
                map.put("an_letter_body",
                        "You are requested to Shift " +
                                lliApplication.getBandwidth() +
                                " Mbps from connection " +
                                src.getName() +
                                " at address " +
                                office.getOfficeAddress() +
                                (dest == null ? " and form a new LLI Connection" : " to connection " + dest.getName()) +
                                " preferably within " +
                                date +
                                " and back the report accordingly."
                );
                break;


            case LLIConnectionConstants.RECONNECT:
                map.put("an_letter_body",
                        "You are requested to reconnect the LLI connection " +
                                "at address " +
                                office.getOfficeAddress() +
                                " preferably within " +
                                date +
                                " and back the report accordingly."
                );
                break;


            case LLIConnectionConstants.CLOSE_CONNECTION:
                map.put("an_letter_body",
                        "You are requested to close the LLI connection " +
                                "at address " +
                                office.getOfficeAddress() +
                                " preferably within " +
                                date +
                                " and back the report accordingly."
                );
                break;
            case LLIConnectionConstants.TD:
                map.put("an_letter_body",
                        "You are requested to temporarily disconnect the LLI connection " +
                                "at address " +
                                office.getOfficeAddress() +
                                " preferably within " +
                                date +
                                " and back the report accordingly."
                );
                break;

            case LLIConnectionConstants.ADDITIONAL_PORT:

            case LLIConnectionConstants.RELEASE_PORT:

            case LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP:

            case LLIConnectionConstants.RELEASE_LOCAL_LOOP:

            case LLIConnectionConstants.ADDITIONAL_IP:

            case LLIConnectionConstants.RELEASE_IP:

            case LLIConnectionConstants.ADDITIONAL_CONNECTION_ADDRESS:

            case LLIConnectionConstants.SHIFT_CONNECTION_ADDRESS:

            case LLIConnectionConstants.RELEASE_CONNECTION_ADDRESS:

            case LLIConnectionConstants.SHIFT_POP:

            case LLIConnectionConstants.NEW_LONG_TERM:

            case LLIConnectionConstants.BREAK_LONG_TERM:

            case LLIConnectionConstants.CHANGE_BILLING_ADDRESS:

            case LLIConnectionConstants.CHANGE_OWNERSHIP:

            default:
                map.put("an_letter_body", "Therefore, you are requested to allocate/transfer/extend and/or"
                        + " take measure and back the report accordingly.");
                break;

        }

    }

    public static void populateOfficialLetterInfo(Map<String, Object> map, OfficialLetter officialLetter) throws Exception {

        List<OfficialLetterConcern> concernList = ServiceDAOFactory.getService(OfficialLetterService.class).getRecipientListByOfficialLetterId(officialLetter.getId());
        List<UserDTO> users = concernList.stream()
                .filter(t -> t.getReferType() == ReferType.CC)
                .map(t -> UserRepository.getInstance().getUserDTOByUserID(t.getRecipientId()))
                .collect(Collectors.toList());
        AtomicInteger atomicInteger = new AtomicInteger(0);
        List<String> ccList = users.stream()
                .map(t -> {
                    atomicInteger.getAndIncrement();
                    return atomicInteger.toString() + ". " + t.getFullName() + "," + t.getDesignation() + ", " + t.getDepartmentName();
                })
                .collect(Collectors.toList());

        UserDTO sender = UserRepository.getInstance().getUserDTOByUserID(
                concernList.stream()
                        .findFirst()
                        .orElseThrow(() -> new RequestFailureException("No Concern List Found"))
                        .getSenderId()
        );

        if(ccList.isEmpty()){
            ccList = new ArrayList<>();
            ccList.add("N/A");
        }
        map.put("an_cc_list", ccList);
        map.put("an_to", "Server Room");
        if (sender == null) {
            throw new RequestFailureException("No Sender Found");
        }
        map.put("an_sender_name", sender.getFullName());
        map.put("an_sender_designation", sender.getDesignation() + ", " + sender.getDepartmentName());

    }

    public static void populateBillInfoForAdviceNote(Map<String, Object> map, Long demandNoteId) throws Exception {
        BillDTO billDTO = null;
        if (demandNoteId != null) {
            billDTO = ServiceDAOFactory.getService(BillService.class).getBillByBillID(demandNoteId);
        }
        PdfParameter.populateDemandNoteInfoForAdviceNote(map, billDTO);
    }

    public static void populateWorkOrderInfoForLLIAdviceNote(Map<String, Object> map, long applicationId) throws Exception {
        List<EFRBean> workOrderList = ServiceDAOFactory.getService(EFRService.class).getSelected(applicationId)
                .stream()
                .map(t -> new EFRBean(
                                t.getSource(),
                                EFR.TERMINAL.get(t.getSourceType()),
                                t.getDestination(),
                                EFR.TERMINAL.get(t.getDestinationType()),
                                t.getProposedLoopDistance(),
                                t.getActualLoopDistance()
                        )
                ).collect(Collectors.toList());
        workOrderList.forEach(log::info);
        map.put("work_order_list", workOrderList);
    }

    public static void populateIPInfoForLLIAdviceNote(Map<String, Object> map, long connectionId, IPConstants.Purpose purpose) throws Exception {
        List<IPBlockUsage> blockUsagesMandatory = ServiceDAOFactory.getService(IPService.class)
                .getIPBlockUsageByConnectionIdAndUsageType(connectionId, LLIConnectionConstants.IPUsageType.MANDATORY, purpose);
        blockUsagesMandatory.forEach(log::info);


        List<IPBlockUsage> blockUsagesAdditional = ServiceDAOFactory.getService(IPService.class)
                .getIPBlockUsageByConnectionIdAndUsageType(connectionId, LLIConnectionConstants.IPUsageType.ADDITIONAL, purpose);
        blockUsagesAdditional.forEach(log::info);

        List<IPBlockLLI> ip_list = Stream.concat(
                blockUsagesMandatory.stream()
                        .map(t -> new IPBlockLLI(t.getFromIP(), t.getToIP(), LLIConnectionConstants.IPUsageType.MANDATORY.name()))
                ,
                blockUsagesAdditional.stream()
                        .map(t -> new IPBlockLLI(t.getFromIP(), t.getToIP(), LLIConnectionConstants.IPUsageType.ADDITIONAL.name()))
        ).collect(Collectors.toList());
        long mandatoryCount = getIPCount(blockUsagesMandatory);
        long additionalCount = getIPCount(blockUsagesAdditional);

        map.put("ip_list", ip_list);
        map.put("mandatory_ip_count", String.valueOf(mandatoryCount));
        map.put("additional_ip_count", String.valueOf(additionalCount));
    }

    private static long getIPCount(List<IPBlockUsage> list) {
        return list.stream()
                .mapToLong(t -> {
                    try {
                        return IPUtility.getIPCountByRange(t.getFromIP(), t.getToIP());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return 0;
                }).sum();
    }

    public static void populateDemandNoteInfoForAdviceNote(Map<String, Object> map, BillDTO billDTO) {
        if (billDTO == null) {
            map.put("dn_id", "N/A");
            map.put("dn_generation_date", "N/A");
            map.put("dn_amount", "N/A");
            map.put("dn_status", "N/A");
            PdfParameter.populatePaymentInfoForAdviceNote(map, null);
        } else {
            map.put("dn_id", String.valueOf(billDTO.getID()));
            map.put("dn_generation_date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(billDTO.getGenerationTime(), "dd/MM/yyyy"));
            map.put("dn_amount", new BigDecimal(billDTO.getNetPayable()).toPlainString());
            map.put("dn_status", String.valueOf(BillDTO.demandNoteStatusMap.get(billDTO.getPaymentStatus())));

            PaymentDTO paymentDTO = ServiceDAOFactory.getService(PaymentService.class).getPaymentDTObyID(billDTO.getPaymentID());
            PdfParameter.populatePaymentInfoForAdviceNote(map, paymentDTO);
        }

    }

    public static void populatePaymentInfoForAdviceNote(Map<String, Object> map, PaymentDTO paymentDTO) {
        if (paymentDTO == null) {
            map.put("payment_id", "N/A");
            map.put("payment_date", "N/A");
            map.put("payment_amount", "N/A");
            map.put("payment_type", "N/A");
            map.put("payment_medium", "N/A");
            map.put("payment_bank", "N/A");
            map.put("payment_branch", "N/A");
            map.put("is_bank_payment", false);
        } else {
            map.put("payment_id", String.valueOf(paymentDTO.getID()));
            map.put("payment_date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(paymentDTO.getPaymentTime(), "dd/MM/yyyy"));
            map.put("payment_amount", new BigDecimal(paymentDTO.getPaidAmount()).toPlainString());
            map.put("payment_type", String.valueOf(PaymentConstants.paymentTypeMap.get(paymentDTO.getPaymentType())));
            map.put("payment_medium", String.valueOf(PaymentConstants.paymentGatewayIDNameMap.get(paymentDTO.getPaymentGatewayType())));
            map.put("payment_bank", String.valueOf(paymentDTO.getBankName()));
            map.put("payment_branch", String.valueOf(paymentDTO.getBankBranchName()));
            map.put("is_bank_payment", paymentDTO.getPaymentGatewayType() == PaymentConstants.PAYEMENT_GATEWAY_TYPE_BANK);
        }
    }

    public static void populateClientInfoForAdviceNote(Map<String, Object> map, OfficialLetter officialLetter, int moduleId) throws Exception {
        KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair =
                ClientAPI.getInstance().getPairOfClientDetailsAndClientContactDetails(
                        officialLetter.getClientId(), moduleId, ClientContactDetailsDTO.BILLING_CONTACT);
        ClientDetailsDTO clientDetailsDTO = pair.key;
        ClientContactDetailsDTO contactDetailsDTO = pair.value;
        map.put("client_full_name", contactDetailsDTO.getRegistrantsName() + contactDetailsDTO.getRegistrantsLastName());
        map.put("client_user_name", clientDetailsDTO.getLoginName());
        map.put("client_mobile", contactDetailsDTO.getPhoneNumber());
        map.put("client_billing_address", contactDetailsDTO.getAddress());
        //TODO
        ClientClassificationService clientClassificationService = ServiceDAOFactory.getService(ClientClassificationService.class);

        map.put("client_type", clientClassificationService.getClientTypeById((long) clientDetailsDTO.getRegistrantType()));
        map.put("client_category", clientClassificationService.getClientCategoryById(clientDetailsDTO.getRegistrantCategory()));
        map.put("client_isp_license_type", clientClassificationService.getClientSubCategoryById(clientDetailsDTO.getRegSubCategory()));
    }

    public static void populateDemandNoteCommonInfo(Map<String, Object> params, BillDTO billDTO, int moduleId) throws Exception {
        populateClientInfo(params, billDTO, moduleId);
        populateBillInfo(params, billDTO);
        populateDemandNoteFooterInfo(params);
    }

    private static void populateClientInfo(Map<String, Object> params, BillDTO billDTO, int moduleId) throws Exception {
        KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair =
                ClientAPI.getInstance().getPairOfClientDetailsAndClientContactDetails(
                        billDTO.getClientID(), moduleId, ClientContactDetailsDTO.BILLING_CONTACT);
        ClientDetailsDTO clientDetailsDTO = pair.key;
        ClientContactDetailsDTO contactDetailsDTO = pair.value;

        params.put("clientFullName", contactDetailsDTO.getRegistrantsName() + " " + contactDetailsDTO.getRegistrantsLastName());
        params.put("clientAddress", contactDetailsDTO.getAddress());
        params.put("clientEmail", contactDetailsDTO.getEmail() != null ? contactDetailsDTO.getEmail() : "N/A");
        params.put("clientLoginName", clientDetailsDTO.getLoginName());
    }

    private static void populateBillInfo(Map<String, Object> params, BillDTO billDTO) {
        params.put("billGenerationDate", TimeConverter.getDateTimeStringByMillisecAndDateFormat(billDTO.getGenerationTime(), "dd/MM/yyyy"));
        params.put("billLastPaymentDate", TimeConverter.getDateTimeStringByMillisecAndDateFormat(billDTO.getLastPaymentDate(), "dd/MM/yyyy"));
        params.put("invoiceID", billDTO.getID() + "");
        params.put("discount", String.format("%.2f", billDTO.getDiscount()));
        params.put("vat", String.format("%.2f", billDTO.getVAT()));
        params.put("total", String.format("%.2f", billDTO.getNetPayable()));
        params.put("demandedAmount", EnglishNumberToWords.convert((long) Math.ceil(billDTO.getNetPayable())));
    }

    private static void populateDemandNoteFooterInfo(Map<String, Object> params) {
        params.put("footerLeft", "Powered By Reve Systems");
        params.put("footerRight", "Bangladesh Telecommunications Company Limited");
        params.put("NB", "NB: ( Client will arrange and maintain local loops from BTCL switch to his offices on his own ).");

    }

    public static void populateCoLocationYearlyDemandNoteInfo(Map<String, Object> params, CoLocationYearlyDemandNote demandNote, CoLocationConnectionDTO connection) throws Exception {
        populateLogo(params, false, logoWithDowrySloganPath);
        params.put("demandNoteCause", demandNote.getDescription());

        AllVariableUnitCharge allVariableUnitCharge = ServiceDAOFactory.getService(VariableCostService.class).getAllVariableUnitChargeByCoLocation(connection);
        Map<Long, CoLocationInventoryTemplateDTO> inventoryTemplateMap = ServiceDAOFactory.getService(CoLocationInventoryTemplateService.class).getCoLocationInventoryTemplateMap();
        VariableCostDTO rackUnitCharge = allVariableUnitCharge.getRackCharge();
        VariableCostDTO fiberUnitCharge = allVariableUnitCharge.getFiberCharge();
        VariableCostDTO powerUnitCharge = allVariableUnitCharge.getPowerCharge();
        VariableCostDTO floorSpaceUnitCharge = allVariableUnitCharge.getFloorSpaceCharge();

        CoLocationInventoryTemplateDTO rackSize = inventoryTemplateMap.getOrDefault((long)connection.getRackSize(), null);
        CoLocationInventoryTemplateDTO rackSpace = inventoryTemplateMap.getOrDefault((long)connection.getRackSpace(), null);
        CoLocationInventoryTemplateDTO coreType = inventoryTemplateMap.getOrDefault((long)connection.getFiberType(), null);
        CoLocationInventoryTemplateDTO powerType = inventoryTemplateMap.getOrDefault((long)connection.getPowerType(), null);
        CoLocationInventoryTemplateDTO floorSpaceType = inventoryTemplateMap.getOrDefault((long)connection.getFloorSpaceType(), null);
        if(connection.isRackNeeded()) {

            params.put("rackSize", String.valueOf(rackSize !=null ? rackSize.getValue() : "N/A"));
            params.put("rackSpace", String.valueOf(rackSpace != null ? rackSpace.getValue() : "N/A"));
            params.put("rackCharge", String.valueOf(rackUnitCharge.getPrice()));
        }else {
            params.put("rackSize","N/A");
            params.put("rackSpace", "N/A");
            params.put("rackCharge", "N/A");
        }


        if (connection.isFiberNeeded()) {
            params.put("totalCore", String.valueOf(connection.getFiberCore()));
            params.put("ofcTypeCharge", String.valueOf(fiberUnitCharge.getPrice() / connection.getFiberCore()));
            params.put("coreType", String.valueOf(coreType != null ?coreType.getValue() : "N/A"));
        } else {
            params.put("totalCore", "N/A");
            params.put("ofcTypeCharge", "N/A");
            params.put("coreType", "N/A");
        }


        if (connection.isPowerNeeded()) {
            params.put("powerType", String.valueOf(powerType !=null ? powerType.getValue() : "N/A"));
            params.put("powerUnit", String.valueOf(connection.getPowerAmount()));
            params.put("powerCharge", String.valueOf(powerUnitCharge.getPrice() / connection.getPowerAmount()));
        }
        else{
            params.put("powerType","N/A");
            params.put("powerUnit", "N/A");
            params.put("powerCharge", "N/A");
        }

        if (connection.isFloorSpaceNeeded()) {
            params.put("floorSpaceType", String.valueOf(floorSpaceType !=null ?floorSpaceType.getValue() : "N/A"));
            params.put("floorSpaceAmount", String.valueOf(connection.getFloorSpaceAmount()));
            params.put("floorSpaceCharge", String.valueOf(floorSpaceUnitCharge.getPrice() / connection.getFloorSpaceAmount()));
        }
        else{
            params.put("floorSpaceType","N/A");
            params.put("floorSpaceAmount", "N/A");
            params.put("floorSpaceCharge", "N/A");
        }


        params.put("yearlyRackCharge", String.valueOf(demandNote.getRackCost()));
        params.put("yearlyOFCCharge", String.valueOf(demandNote.getOfcCost()));
        params.put("yearlyPowerCharge", String.valueOf(demandNote.getPowerCost()));

        params.put("upgradeCharge", String.valueOf(demandNote.getUpgradeCost()));
        params.put("downgradeCharge", String.valueOf(demandNote.getDowngradeCost()));
        params.put("closingCharge", String.valueOf(demandNote.getClosingCost()));
        params.put("reconnectCharge", String.valueOf(demandNote.getReconnectCost()));

        params.put("yearly_adjustment", String.valueOf(demandNote.getYearlyAdjustment()));

    }

    public static String printWithoutScientificNotation(double cost) {
        return new BigDecimal(cost).toPlainString();
    }

    public static void populateCoLocationDemandNoteInfo(Map<String, Object> params, CoLocationDemandNote demandNote, CoLocationApplicationDTO application) throws Exception {

        populateLogo(params, application.getSkipPayment() == 1, logoWithDowrySloganPath);
        params.put("month_factor", "");

        params.put("demandNoteCause", "In the context of your application in "
                + TimeConverter.getDateTimeStringByMillisecAndDateFormat(application.getSubmissionDate(), "dd/MM/yyyy")
                + " Demand Note for CoLocation Connection"
                + " has been issued.");

        AllVariableUnitCharge allVariableUnitCharge = ServiceDAOFactory.getService(VariableCostService.class).getAllVariableChargeByCoLocationApplication(application);
        Map<Long, CoLocationInventoryTemplateDTO> inventoryTemplateMap = ServiceDAOFactory.getService(CoLocationInventoryTemplateService.class).getCoLocationInventoryTemplateMap();
//        Map<Integer, Map<Integer, List<VariableCostDTO>>> mapOfVariableCostsToQuantityToType
//                = ServiceDAOFactory.getService(VariableCostService.class).getVariableCostConfigMap();

        VariableCostDTO rackUnitCharge = allVariableUnitCharge.getRackCharge();
        VariableCostDTO fiberUnitCharge = allVariableUnitCharge.getFiberCharge();
        VariableCostDTO powerUnitCharge = allVariableUnitCharge.getPowerCharge();
        VariableCostDTO floorSpaceUnitCharge = allVariableUnitCharge.getFloorSpaceCharge();


        if(application.isRackNeeded()) {
            CoLocationInventoryTemplateDTO rackSize = inventoryTemplateMap.getOrDefault((long)application.getRackTypeID(), null);
            CoLocationInventoryTemplateDTO rackSpace = inventoryTemplateMap.getOrDefault((long)application.getRackSpace(), null);

            params.put("rackSize", String.valueOf(rackSize !=null ? rackSize.getValue() : "N/A"));
            params.put("rackSpace", String.valueOf(rackSpace != null ? rackSpace.getValue() : "N/A"));
            params.put("rackCharge", String.valueOf(rackUnitCharge.getPrice()));
        }else {
            params.put("rackSize","N/A");
            params.put("rackSpace", "N/A");
            params.put("rackCharge", "N/A");
        }


        if (application.isFiberNeeded()) {
            CoLocationInventoryTemplateDTO coreType = inventoryTemplateMap.getOrDefault((long)application.getFiberType(), null);
            params.put("totalCore", String.valueOf(application.getFiberCore()));
            params.put("ofcTypeCharge", String.valueOf(fiberUnitCharge.getPrice() / application.getFiberCore()));
            params.put("coreType", String.valueOf(coreType != null ?coreType.getValue() : "N/A"));
        } else {
            params.put("totalCore", "N/A");
            params.put("ofcTypeCharge", "N/A");
            params.put("coreType", "N/A");
        }


        if (application.isPowerNeeded()) {
            CoLocationInventoryTemplateDTO powerType = inventoryTemplateMap.getOrDefault((long)application.getPowerType(), null);
            params.put("powerType", String.valueOf(powerType !=null ? powerType.getValue() : "N/A"));
            params.put("powerUnit", String.valueOf(application.getPowerAmount()));
            params.put("powerCharge", String.valueOf(powerUnitCharge.getPrice() / application.getPowerAmount()));
        }
        else{
            params.put("powerType","N/A");
            params.put("powerUnit", "N/A");
            params.put("powerCharge", "N/A");
        }

        if (application.isFloorSpaceNeeded()) {
            CoLocationInventoryTemplateDTO floorSpaceType = inventoryTemplateMap.getOrDefault((long)application.getFloorSpaceType(), null);
            params.put("floorSpaceType", String.valueOf(floorSpaceType !=null ?floorSpaceType.getValue() : "N/A"));
            params.put("floorSpaceAmount", String.valueOf(application.getFloorSpaceAmount()));
            params.put("floorSpaceCharge", String.valueOf(floorSpaceUnitCharge.getPrice() / application.getFloorSpaceAmount()));
        }
        else{
            params.put("floorSpaceType","N/A");
            params.put("floorSpaceAmount", "N/A");
            params.put("floorSpaceCharge", "N/A");
        }

        params.put("yearlyRackCharge",printWithoutScientificNotation(demandNote.getRackCost()));
        params.put("yearlyOFCCharge", printWithoutScientificNotation(demandNote.getOfcCost()));
        params.put("yearlyPowerCharge", printWithoutScientificNotation(demandNote.getPowerCost()));
        params.put("yearlyFloorSpaceCharge", printWithoutScientificNotation(demandNote.getFloorSpaceCost()));

        params.put("upgradeCharge", printWithoutScientificNotation(demandNote.getUpgradeCost()));
        params.put("downgradeCharge", printWithoutScientificNotation(demandNote.getDowngradeCost()));
        params.put("closingCharge", printWithoutScientificNotation(demandNote.getClosingCost()));
        params.put("reconnectCharge", printWithoutScientificNotation(demandNote.getReconnectCost()));
        //TODO remove and assign appropriate connection name;

        if (application.getConnectionId() != 0) {
            CoLocationConnectionDTO connection = ServiceDAOFactory.getService(CoLocationConnectionService.class).getColocationConnection(application.getConnectionId());
            params.put("connectionName", connection.getName());
        } else {
            params.put("connectionName", "Not Assigned");
        }

    }

    private static void populateLogo(Map<String, Object> params, boolean isSkipped, String logoPath) {
        params.put("skipped", isSkipped);
        params.put("logo", logoPath);
    }

    public static void populateNIXDemandNoteInfo(Map<String, Object> params, NIXDemandNote nixDemandNote, NIXReviseDTO nixClientApplication) throws Exception {
        populateLogo(params, false, logoWithDowrySloganPath);
        params.put("connectionName", "N/A");
        if (nixClientApplication.getApplicationType() == NIXConstants.NIX_RECONNECT) {
            params.put("demandNoteCause", "In the context of your "
                    + NIXConstants.nixapplicationTypeNameMap.get(nixClientApplication.getApplicationType()) + " in "
                    + TimeConverter.getDateTimeStringByMillisecAndDateFormat(nixClientApplication.getSuggestedDate(), "dd/MM/yyyy")
                    + " Demand Note for National Internet Exchange (NIX) Connection"
                    + " has been issued.");

        }
        params.put("securityCharge", String.valueOf(nixDemandNote.getSecurityMoney()));
        params.put("registrationCharge", String.valueOf(nixDemandNote.getRegistrationFee()));
        params.put("nixPortCharge", String.valueOf(nixDemandNote.getNixPortCharge()));
        params.put("nixPortUpgradeCharge", String.valueOf(nixDemandNote.getNixPortUpgradeCharge()));
        params.put("nixPortDowngradeCharge", String.valueOf(nixDemandNote.getNixPortDowngradeCharge()));
        params.put("instantDegradationCharge", String.valueOf(nixDemandNote.getInstantDegradationCharge()));
        params.put("localLoopCharge", String.valueOf(nixDemandNote.getLocalLoopCharge()));
        params.put("closingCharge", String.valueOf(nixDemandNote.getClosingCharge()));
        params.put("reconnectCharge", String.valueOf(nixDemandNote.getReconnectCharge()));
        params.put("advanceAdjustment", String.format("%.2f", nixDemandNote.getAdvanceAdjustment()));
        params.put("portChargeMinusDiscount", String.format("%.2f", nixDemandNote.getNixPortCharge() - nixDemandNote.getDiscount()));
        double subTotal = nixDemandNote.getTotalPayable();
        params.put("vatCalculableWOSecurity", String.format("%.2f",
                subTotal - nixDemandNote.getSecurityMoney()
        ));


        params.put("subTotal", String.format("%.2f", subTotal));
        params.put("vatPercentage", String.format("%.2f", nixDemandNote.getVatPercentage()));
        params.put("discountPercentage", String.format("%.2f", nixDemandNote.getDiscountPercentage()));

    }

    public static void populateNIXDemandNoteInfo(Map<String, Object> params, NIXDemandNote nixDemandNote, NIXApplication nixApplication) throws Exception {

        boolean isSkipped = nixApplication.getSkipPayment() == 1;
        populateLogo(params, isSkipped, logoWithDowrySloganPath);


        NIXApplicationOffice nixOffice = ServiceDAOFactory.
                getService(NIXApplicationOfficeService.class).getOfficesByApplicationId(nixApplication.getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RequestFailureException("No Office Found With Application Id" + nixApplication.getId()));
        String portTypeName;
        String prevPortTypeName;
        String newPortTypeName;
        if (nixApplication.getType() == NIXConstants.NEW_CONNECTION_APPLICATION) {
            portTypeName = InventoryConstants.mapOfPortTypeToPortTypeString.get(nixApplication.getPortType());
            params.put("demandNoteCause", "In the context of your "
                    + NIXConstants.nixapplicationTypeNameMap.get(nixApplication.getType()) + " in "
                    + TimeConverter.getDateTimeStringByMillisecAndDateFormat(nixApplication.getSubmissionDate(), "dd/MM/yyyy")
                    + " Demand Note for National Internet Exchange (NIX) Connection in "
                    + nixOffice.getAddress()
                    + " has been issued. "
                    + "Requested Port Type: " + portTypeName + ". "
                    + "Requested Port: " + nixApplication.getPortCount() + ". ");

        } else if (nixApplication.getType() == NIXConstants.NIX_UPGRADE_APPLICATION) {
            NIXUpgradeApplication upgradeApplication = ServiceDAOFactory.getService(NIXUpgradeApplicationService.class)
                    .getApplicationByParent(nixApplication.getId());
            prevPortTypeName = InventoryConstants.mapOfPortTypeToPortTypeString.get(upgradeApplication.getOldPortType());
            newPortTypeName = InventoryConstants.mapOfPortTypeToPortTypeString.get(upgradeApplication.getNewPortType());
            params.put("demandNoteCause", "In the context of your "
                    + NIXConstants.nixapplicationTypeNameMap.get(nixApplication.getType()) + " in "
                    + TimeConverter.getDateTimeStringByMillisecAndDateFormat(nixApplication.getSubmissionDate(), "dd/MM/yyyy")
                    + " Demand Note for National Internet Exchange (NIX) Connection in "
                    + nixOffice.getAddress()
                    + " has been issued. "
                    + "Requested New Port Type: " + newPortTypeName + ". "
                    + "Old Port Type: " + prevPortTypeName + ". ");


        } else if (nixApplication.getType() == NIXConstants.NIX_DOWNGRADE_APPLICATION) {
            NIXDowngradeApplication downgradeApplication = ServiceDAOFactory.getService(NIXDowngradeApplicationService.class)
                    .getApplicationByParent(nixApplication.getId());
            prevPortTypeName = InventoryConstants.mapOfPortTypeToPortTypeString.get(downgradeApplication.getOldPortType());
            newPortTypeName = InventoryConstants.mapOfPortTypeToPortTypeString.get(downgradeApplication.getNewPortType());
            params.put("demandNoteCause", "In the context of your "
                    + NIXConstants.nixapplicationTypeNameMap.get(nixApplication.getType()) + " in "
                    + TimeConverter.getDateTimeStringByMillisecAndDateFormat(nixApplication.getSubmissionDate(), "dd/MM/yyyy")
                    + " Demand Note for National Internet Exchange (NIX) Connection in "
                    + nixOffice.getAddress()
                    + " has been issued. "
                    + "Requested New Port Type: " + newPortTypeName + ". "
                    + "Old Port Type: " + prevPortTypeName + ". ");
        } else if (nixApplication.getType() == NIXConstants.NIX_CLOSE_APPLICATION) {
            NIXCloseApplication closeApplication = ServiceDAOFactory.getService(NIXCloseApplicationService.class)
                    .getApplicationByParent(nixApplication.getId());

            params.put("demandNoteCause", "In the context of your "
                    + NIXConstants.nixapplicationTypeNameMap.get(nixApplication.getType()) + " in "
                    + TimeConverter.getDateTimeStringByMillisecAndDateFormat(nixApplication.getSubmissionDate(), "dd/MM/yyyy")
                    + " Demand Note for National Internet Exchange (NIX) Connection in "
                    + nixOffice.getAddress()
                    + " has been issued.");

        }


        params.put("securityCharge", String.format("%.2f", nixDemandNote.getSecurityMoney()));
        params.put("registrationCharge", String.format("%.2f", nixDemandNote.getRegistrationFee()));
        params.put("nixPortCharge", String.format("%.2f", nixDemandNote.getNixPortCharge()));
        params.put("nixPortUpgradeCharge", String.format("%.2f", nixDemandNote.getNixPortUpgradeCharge()));
        params.put("nixPortDowngradeCharge", String.format("%.2f", nixDemandNote.getNixPortDowngradeCharge()));
        params.put("instantDegradationCharge", String.format("%.2f", nixDemandNote.getInstantDegradationCharge()));
        params.put("localLoopCharge", String.format("%.2f", nixDemandNote.getLocalLoopCharge()));
        params.put("closingCharge", String.format("%.2f", nixDemandNote.getClosingCharge()));
        params.put("reconnectCharge", String.format("%.2f", nixDemandNote.getReconnectCharge()));
        params.put("advanceAdjustment", String.format("%.2f", nixDemandNote.getAdvanceAdjustment()));
        params.put("portChargeMinusDiscount", String.format("%.2f", nixDemandNote.getNixPortCharge() - nixDemandNote.getDiscount()));
        double subTotal = nixDemandNote.getTotalPayable();
        params.put("vatCalculableWOSecurity", String.format("%.2f",
                subTotal - nixDemandNote.getSecurityMoney()
        ));


        params.put("subTotal", String.format("%.2f", subTotal));
        params.put("vatPercentage", String.format("%.2f", nixDemandNote.getVatPercentage()));
        params.put("discountPercentage", String.format("%.2f", nixDemandNote.getDiscountPercentage()));


        if (nixApplication.getConnection() != 0L) {
            NIXConnection nixConnection = ServiceDAOFactory.getService(NIXConnectionService.class)
                    .getLatestNIXConnectionByConnectionHistoryId(nixApplication.getConnection());

            params.put("connectionName", nixConnection.getName());
        } else {
            params.put("connectionName", "Not Assigned");
        }
    }

    public static void populateNIXAdviceNoteTopInfo(Map<String, Object> map) {
        map.put("an_title", "National Internet Exchange (NIX) Advice Note");
        map.put("an_letter_body", "Therefore, you are requested to allocate/transfer/extend and/or"
                + " take measure and back the report accordingly."
        );
    }

    public static void populateVPNAdviceNoteTopInfo(Map<String, Object> map) {
        map.put("an_title", "Virtual Private Network (VPN) Advice Note");
        map.put("an_letter_body", "Therefore, you are requested to allocate/transfer/extend and/or"
                + " take measure and back the report accordingly."
        );
    }


    public static void populateVPNSpecificAdviceNote(Map<String, Object> map, List<VPNApplicationLink> links) throws Exception {

        AdviceNoteGenerationFacade adviceNoteGenerationFacade = ServiceDAOFactory.getService(AdviceNoteGenerationFacade.class);
        // Bill & Payment Info

        List<DemandNotePayment> demandNotePayments = links.stream()
                .map(VPNApplicationLink::getDemandNoteId)
                .filter(Objects::nonNull)
                .distinct()
                .map(adviceNoteGenerationFacade::getDemandNotePaymentInfoByInvoiceId)
                .collect(Collectors.toList());

        map.put("dn_payment_list", demandNotePayments);

        // TODO better performance// Adjusted LocalDistance Remote Distance
        // TODO vendor
        List<LinkInfo> linkInfos = new ArrayList<>();
        for (VPNApplicationLink link : links) {
            String localOCName = "N/A";
            String remoteOCName = "N/A";
            LocalLoop localEnd = link.getLocalOffice().getLocalLoops().get(0);
            LocalLoop remoteEnd = link.getRemoteOffice().getLocalLoops().get(0);
            try {
                UserDTO localOC = UserRepository.getInstance().getUserDTOByUserID(localEnd.getVendorId());
                localOCName = localOC.getUsername();
            }catch (NoDataFoundException e) {
                log.fatal(e.getMessage());
            }

            try {
                UserDTO remoteOC = UserRepository.getInstance().getUserDTOByUserID(remoteEnd.getVendorId());
                remoteOCName = remoteOC.getUsername();
            }catch (NoDataFoundException e){
                log.fatal(e.getMessage());
            }
            linkInfos.add(
                LinkInfo.builder()
                    .name(link.getLinkName())
                    .bw(String.valueOf(link.getLinkBandwidth()))
                    .p2p(String.valueOf(ServiceDAOFactory.getService(DemandNoteAutofillFacade.class).getPopToPopDistance(localEnd.getPopId(), remoteEnd.getPopId())))
                    .lprovider(localEnd.getLoopProvider() == 1? "BTCL" : "Client")
                    .rprovider(remoteEnd.getLoopProvider() == 1 ? "BTCL" : "Client")
                    .loc(localOCName)
                    .roc(remoteOCName)
                    .ldistance(localEnd.getBtclDistance() + " + " +localEnd.getOcDistance())
                    .rdistance(remoteEnd.getBtclDistance() + " + " + remoteEnd.getOcDistance())
                    .build()
            );
        }
        map.put("link_info", linkInfos);

        // inventory info
        List<InventoryInfo> inventoryInfos = new ArrayList<>();
        InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);
        for (VPNApplicationLink link : links) {
            LocalLoop localEnd = link.getLocalOffice().getLocalLoops().get(0);
            LocalLoop remoteEnd = link.getRemoteOffice().getLocalLoops().get(0);
            Map<Long, InventoryItem> inventoryMap = inventoryService.getMapOfInventoryItemToInventoryIDByInventoryIds(Arrays.asList(localEnd.getPopId(),
                    localEnd.getPortId(),
                    localEnd.getRouterOrSwitchId(),
                    localEnd.getVlanId(),
                    remoteEnd.getPopId(),
                    remoteEnd.getPortId(),
                    remoteEnd.getRouterOrSwitchId(),
                    remoteEnd.getVlanId()

            ));
            inventoryInfos.add(
                InventoryInfo.builder()
                    .name(link.getLinkName())
                    .localPop(inventoryMap.getOrDefault(localEnd.getPopId(), new InventoryItem()).getName())
                    .remotePop(inventoryMap.getOrDefault(remoteEnd.getPopId(), new InventoryItem()).getName())
                    .localPort(inventoryMap.getOrDefault(localEnd.getPortId(), new InventoryItem()).getName())
                    .remotePort(inventoryMap.getOrDefault(remoteEnd.getPortId(), new InventoryItem()).getName())
                    .localRouterSwitch(inventoryMap.getOrDefault(localEnd.getRouterOrSwitchId(), new InventoryItem()).getName())
                    .remoteRouterSwitch(inventoryMap.getOrDefault(remoteEnd.getRouterOrSwitchId(), new InventoryItem()).getName())
                    .localVlan(inventoryMap.getOrDefault(localEnd.getVlanId(), new InventoryItem()).getName())
                    .remoteVlan(inventoryMap.getOrDefault(remoteEnd.getVlanId(), new InventoryItem()).getName())

                    .build()
            );
        }

        map.put("inventory_info", inventoryInfos);

        List<WorkOrderInfo> workOrderInfos = new ArrayList<>();
        List<Long> officeIds = ServiceDAOFactory.getService(WorkOrderGenerationFacade.class).getDistinctOfficeIds(links);
        List<entity.efr.EFR> efrs;
        try {
             efrs = ServiceDAOFactory.getService(WorkOrderGenerationFacade.class).getSelectedEFRs(officeIds);
        }catch (NoDataFoundException e) {
            efrs = new ArrayList<>();
        }

        for (entity.efr.EFR efr : efrs) {
            UserDTO vendor = null;
            try {
                vendor = UserRepository.getInstance().getUserDTOByUserID(efr.getVendorID());
            }catch(NoDataFoundException e) {
                log.fatal(e.getMessage());
            }

            entity.office.Office office = ServiceDAOFactory.getService(GlobalService.class)
                    .findByPK(entity.office.Office.class, efr.getOfficeId());
            workOrderInfos.add(
                WorkOrderInfo.builder()
                    .woNumber(String.valueOf(efr.getWorkOrderNumber())) // TODO change it to actual wo number;
                    .vendorName(vendor==null?"N/A": vendor.getUsername()) // TODO change it to actual name;
                    .source(efr.getSource()== null ? "N/A" : efr.getSource())
                    .sourceType(EFR.TERMINAL.getOrDefault(efr.getSourceType(), "N/A"))
                    .destination(efr.getDestination() == null ? "N/A" : efr.getDestination())
                    .destinationType(EFR.TERMINAL.getOrDefault(efr.getDestinationType(), "N/A"))
                    .distance(String.valueOf(efr.getProposedLoopDistance()))
                    .officeAddress(office == null ? "N/A" : office.getOfficeAddress()) // TODO change it to actual name;
                    // TODO status of work order.
                    .build()
            );
        }

        map.put("wo_info", workOrderInfos);

        // for ip
        // TODO IP later
        map.put("isLayer3", false);
    }


    public static void populate_bandwidth_information(Map<String, Object> map, OfficialLetter officialLetter) throws Exception {

        Map<String, Double> bwStatistics =  ServiceDAOFactory.getService(LLIConnectionService.class).getBandwidthStatisticsByClientId(officialLetter.getClientId());

        map.put("total_regular_bw", String.valueOf(bwStatistics.getOrDefault("regular", 0.0)));
        map.put("total_long_term_bw", String.valueOf(bwStatistics.getOrDefault("LT", 0.0)));
        map.put("total_cache_bw", String.valueOf(bwStatistics.getOrDefault("cache", 0.0)));
    }

    public static void populateVPNLinkDemandNoteInfo(Map<String, Object> params, VPNDemandNote vpnDemandNote, VPNApplication vpnApplication) throws Exception {
        boolean isSkipped = vpnApplication.isSkipPayment();
        populateLogo(params, isSkipped, logoWithBTCLAim);

        List<VPNApplicationLink> links = vpnApplication.getVpnApplicationLinks();
        params.put("localEndAddress", links.get(0).getLocalOffice().getOfficeAddress()); // TODO NULL Check;

        params.put("demandNoteCause", "In the context of your application id: " + vpnApplication.getApplicationId()
                + " in " + TimeConverter.getDateTimeStringByMillisecAndDateFormat(vpnApplication.getSubmissionDate(), "dd/MM/yyyy")
                + " Demand Note for "+ vpnApplication.getApplicationType().getApplicationTypeName() + " for "
                + links.size() + " Remote Ends has been issued."
        );


        params.put("registrationCharge", String.format("%.2f", vpnDemandNote.getRegistrationCharge()));
        params.put("otcLocalLoopBTCL", String.format("%.2f", vpnDemandNote.getOtcLocalLoopBTCL()));
        params.put("bandwidthCharge", String.format("%.2f", vpnDemandNote.getBandwidthCharge() ));
        params.put("bandwidthChargeMinusDiscount", String.format("%.2f",
                vpnDemandNote.getBandwidthCharge() - vpnDemandNote.getDiscount() ));

        params.put("securityCharge", String.format("%.2f", vpnDemandNote.getSecurityCharge()));
        params.put("localLoopCharge", String.format("%.2f", vpnDemandNote.getLocalLoopCharge()));
        params.put("instantDegradationCharge", String.format("%.2f", vpnDemandNote.getDegradationCharge()));
        params.put("reconnectCharge", String.format("%.2f", vpnDemandNote.getReconnectCharge()));
        params.put("closingCharge", String.format("%.2f", vpnDemandNote.getClosingCharge()));
        params.put("shiftingCharge", String.format("%.2f", vpnDemandNote.getShiftingCharge()));
        params.put("ownershipChangeCharge", String.format("%.2f", vpnDemandNote.getOwnershipChangeCharge()));
        params.put("otherCharge", String.format("%.2f", vpnDemandNote.getOtherCharge()));
        params.put("advance", String.format("%.2f", vpnDemandNote.getAdvance()));
        params.put("subTotal", String.format("%.2f", vpnDemandNote.getTotalPayable()));
        params.put("discountPercentage", String.format("%.2f", vpnDemandNote.getDiscountPercentage()));
        params.put("vatPercentage", String.format("%.2f", vpnDemandNote.getVatPercentage()));
        params.put("vatCalculableWOSecurity", String.format("%.2f", vpnDemandNote.getTotalPayable() - vpnDemandNote.getSecurityCharge()));
        params.put("demandedAmount", EnglishNumberToWords.convert((long) Math.ceil(vpnDemandNote.getNetPayable())));


        // TODO null check ; Reference bottleneck removal

        params.put("ownerChange", false);
        params.put("reconnectAndTD", false);
        if(vpnDemandNote.getApplicationGroupType() == ApplicationGroupType.VPN_LINK_APPLICATION) {
            populateVPNLinkDemandNoteSpecificInfo(params, vpnDemandNote, vpnApplication);
        }else if(vpnDemandNote.getApplicationGroupType() == ApplicationGroupType.VPN_LINK_CLIENT_APPLICATION ) {

            KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair = ClientAPI.getInstance().
                    getPairOfClientDetailsAndClientContactDetails(vpnApplication.getSecondClient(), ModuleConstants.Module_ID_VPN, ClientContactDetailsDTO.BILLING_CONTACT);
            ClientDetailsDTO clientDetailsDTO = pair.getKey();
            ClientContactDetailsDTO contactDetailsDTO = pair.getValue();
            params.put("clientFullNameDest", contactDetailsDTO.getRegistrantsName() + " " + contactDetailsDTO.getRegistrantsLastName());
            params.put("clientAddressDest", contactDetailsDTO.getAddress());
            params.put("clientEmailDest", contactDetailsDTO.getEmail() != null ? contactDetailsDTO.getEmail() : "N/A");
            params.put("clientLoginNameDest", clientDetailsDTO.getLoginName());
            params.put("linkInfo", links.stream()
                    .map(t->new KeyValuePair<>(t.getLinkName(), String.valueOf(t.getLinkBandwidth())))
                    .collect(Collectors.toList())
            );
            params.put("ownerChange", true);
        }else if(vpnDemandNote.getApplicationGroupType() == ApplicationGroupType.VPN_CLIENT_APPLICATION){
            params.put("reconnectAndTD", true);
            params.put("linkInfo", links.stream()
                    .map(t->new KeyValuePair<>(t.getLinkName(), String.valueOf(t.getLinkBandwidth())))
                    .collect(Collectors.toList())
            );
        }
    }

    private static void populateVPNLinkDemandNoteSpecificInfo(Map<String, Object> params, VPNDemandNote vpnDemandNote, VPNApplication vpnApplication) throws Exception {
        DemandNoteAutofillFacade autofillFacade = ServiceDAOFactory.getService(DemandNoteAutofillFacade.class);
        VPNApplicationService vpnApplicationService = ServiceDAOFactory.getService(VPNApplicationService.class);


        List<VPNApplicationLink> links = vpnApplication.getVpnApplicationLinks();
        LocalLoop localEnd = links.get(0).getLocalOffice().getLocalLoops().get(0);
        params.put("localEndBTCLDistance", localEnd.getBtclDistance() + " (m)");
        params.put("localEndOCDistance", localEnd.getOcDistance() + " (m)");
        params.put("localEndLoopProvider", localEnd.getLoopProvider()== VPNConstants.LOOP_CLIENT ? "Client" : "BTCL");
        double localEndLoopCharge = 0;
        double totalLLCost = 0;
        if(vpnApplication.getApplicationType() == ApplicationType.VPN_NEW_CONNECTION ) {
             localEndLoopCharge =  !vpnApplicationService.getLocalLoopDiscountEligibilityByInvoiceId(vpnDemandNote.getID()).isEmpty() ?
                     DemandNoteAutofillFacade.calculateLocalLoopCost(localEnd.getDistrictId(),
                             localEnd.getBtclDistance() + localEnd.getOcDistance(),
                             localEnd.getOfcType()
                     ): 0;
             totalLLCost = autofillFacade.calculateRemoteEndLoopCost(links);

        }
        params.put("localEndLoopCharge", String.format("%.2f",localEndLoopCharge));
        params.put("totalLLCost", String.format("%.2f",totalLLCost));
        double totalBWCostWithoutDiscount = vpnDemandNote.getBandwidthCharge() - vpnDemandNote.getDiscount();
        params.put("totalBWCost", String.format("%.2f", vpnDemandNote.getBandwidthCharge()));
        params.put("totalDiscountCost", String.format("%.2f", vpnDemandNote.getDiscount()));
        params.put("totalBWMinusDiscountCost", String.format("%.2f", totalBWCostWithoutDiscount));

        params.put("totalRegCharge", String.format("%.2f", vpnDemandNote.getRegistrationCharge()));
        double totalSubTotal = totalBWCostWithoutDiscount + totalLLCost + vpnDemandNote.getRegistrationCharge();
        params.put("totalSubTotal", String.format("%.2f",totalSubTotal));
        List<RemoteEndInfo> remoteEndInfos = getRemoteEndInfo(vpnApplication, vpnDemandNote.getDiscountPercentage());
        params.put("remoteEndInfo", remoteEndInfos);
    }

    private static List<RemoteEndInfo> getRemoteEndInfo(VPNApplication vpnApplication, double discountPercentage) throws Exception {
        List<VPNApplicationLink> links = vpnApplication.getVpnApplicationLinks();
        AtomicInteger atomicInteger = new AtomicInteger(1);
        TableDTO tableDTO = ServiceDAOFactory.getService(CostConfigService.class)
                .getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(System.currentTimeMillis(), ModuleConstants.Module_ID_VPN, 1);
        VPNOTC vpnotc = ServiceDAOFactory.getService(UniversalDTOService.class).get(VPNOTC.class);
        boolean isNewConnectionApplication = vpnApplication.getApplicationType() == ApplicationType.VPN_NEW_CONNECTION;
        boolean isUpgradeApplication = vpnApplication.getApplicationType() == ApplicationType.VPN_UPGRADE_CONNECTION;
        boolean isBWChargeCalculationNeeded = isNewConnectionApplication || isUpgradeApplication;
        return links.stream()
                .map(t-> {
                    try {
                        return getRemoteInfo(t, atomicInteger.getAndIncrement(), tableDTO, vpnotc, discountPercentage, 
                                isNewConnectionApplication, isBWChargeCalculationNeeded,vpnApplication.getLayerType()

                        );
                    } catch (Exception e) {
                        log.fatal("[ X ]: " + e.getMessage());
                        log.fatal("returning null");
                        return null;
                    }
                })
                .collect(Collectors.toList());

    }

    private static RemoteEndInfo getRemoteInfo(VPNApplicationLink link, int serial, TableDTO costChart,
                                               VPNOTC vpnotc, double discountPercentage,
                                               boolean isNewConnectionApplication,
                                                boolean isBWChargeNeeded,
                                               int layerType
    ) throws Exception {
        DemandNoteAutofillFacade demandNoteAutofillFacade = ServiceDAOFactory.getService(DemandNoteAutofillFacade.class);
        LocalLoop remoteEndLoop = link.getRemoteOffice().getLocalLoops().get(0);
        LocalLoop localEndLoop = link.getLocalOffice().getLocalLoops().get(0);
        double loopCost = 0;
        double registrationCost = 0;
        if(isNewConnectionApplication ){
            loopCost = ServiceDAOFactory.getService(DemandNoteAutofillFacade.class).
                    calculateLocalLoopCost( remoteEndLoop, remoteEndLoop.isCompleted() );
            registrationCost = vpnotc.getRegistrationChargePerLink();
        }
        KeyValuePair<Double, Double> pairOfBWCostAndDiscount = null;
        if(isBWChargeNeeded) {
            pairOfBWCostAndDiscount= demandNoteAutofillFacade.calculateBWCostOfALinkForVPN(link, costChart, discountPercentage, vpnotc, layerType);
        }
        double bwCost = 0;
        double discount = 0;
        double subTotal = 0;
        if(pairOfBWCostAndDiscount != null ){
            bwCost = pairOfBWCostAndDiscount.getKey();
            discount = pairOfBWCostAndDiscount.getValue();
            subTotal = Stream.of(loopCost, bwCost-discount, registrationCost)
                    .mapToDouble(t -> t)
                    .sum();
        }
        return RemoteEndInfo.builder()
                .serial(String.valueOf(serial))
                .address(link.getRemoteOffice().getOfficeAddress())
                .popToPopDistance(
                        String.valueOf(
                                demandNoteAutofillFacade.getPopToPopDistance(
                                        localEndLoop.getPopId(),remoteEndLoop.getPopId()
                                )
                        )
                )
                .bw(String.valueOf(link.getLinkBandwidth()))
                .btclDistance(String.valueOf(remoteEndLoop.getBtclDistance()))
                .ocDistance(String.valueOf(remoteEndLoop.getOcDistance()))
                .localLoopLength(String.valueOf(remoteEndLoop.getBtclDistance()
                        + remoteEndLoop.getOcDistance()))
                .localLoopCost(String.format("%.2f",loopCost))
                .bwCost(String.format("%.2f",bwCost))
                .discount(String.format("%.2f",discount))
                .bwMinusDiscount(String.format("%.2f", bwCost - discount))
                .registrationCharge(String.format("%.2f",registrationCost))
                .subTotal(String.format("%.2f",subTotal))
                .build();
    }
}
