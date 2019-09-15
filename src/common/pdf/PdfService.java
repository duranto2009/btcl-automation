package common.pdf;

import accounting.BillPaymentDTOForLedger;
import annotation.Transactional;
import api.ClientAPI;
import api.FileAPI;
import client.clearanceCertificate.ClearanceCertificateNoDuePdf;
import client.clearanceCertificate.ClearanceCertificatePdf;
import client.multipleMonthBill.MultipleMonthBillPdf;
import client.requestLetter.RequestLetterPdf;
import com.google.gson.JsonObject;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillDTO;
import common.bill.BillService;
import common.repository.AllClientRepository;
import entity.efr.EFR;
import entity.efr.EFRConditionBuilder;
import file.FileService;
import file.FileTypeConstants;
import global.GlobalService;
import inventory.InventoryItem;
import inventory.InventoryService;
import lli.Application.AdditionalConnectionAddress.LLIAdditionalConnectionAddressApplication;
import lli.Application.AdditionalIP.LLIAdditionalIPApplication;
import lli.Application.AdditionalLocalLoop.LLIAdditionalLocalLoopApplication;
import lli.Application.AdditionalPort.LLIAdditionalPortApplication;
import lli.Application.BreakLongTerm.LLIBreakLongTermApplication;
import lli.Application.CloseConnection.LLICloseConnectionApplication;
import lli.Application.DowngradeBandwidth.LLIDowngradeBandwidthApplication;
import lli.Application.EFR.EFRService;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.Application.NewConnection.LLINewConnectionApplication;
import lli.Application.ShiftAddress.LLIShiftAddressApplication;
import lli.Application.ShiftPop.LLIShiftPopApplication;
import lli.Application.TemporaryUpgradeBandwidth.LLITemporaryUpgradeBandwidthApplication;
import lli.Application.upgradeBandwidth.LLIUpgradeBandwidthApplication;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import lli.LLINotificationService;
import lli.LLIOffice;
import lli.connection.LLIConnectionConstants;
import lli.demandNote.*;
import lombok.extern.log4j.Log4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import nix.efr.NIXEFR;
import nix.efr.NIXEFRConditionBuilder;
import officialLetter.OfficialLetter;
import officialLetter.OfficialLetterService;
import officialLetter.OfficialLetterType;
import org.apache.commons.io.IOUtils;
import requestMapping.Service;
import util.*;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationLink;
import vpn.application.VPNApplicationService;
import vpn.client.ClientDetailsDTO;
import vpn.client.ClientService;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j
public class PdfService {

    @Service
    private BillService billService;
    @Service
    private ClientService clientService;
    @Service
    private LLIApplicationService lliApplicationService;
    @Service
    private LLIConnectionService lliConnectionService;
    @Service
    private InventoryService inventoryService;
    @Service
    private FileService fileService;
    @Service
    private LLINotificationService lliNotificationService;
    @Service
    private OfficialLetterService officialLetterService;
    @Service
    private GlobalService globalService;


    private String saveDemandNoteToFileSystemAndGetAbsolutePath(BillDTO billDTO) throws Exception {
        LLIApplication lliApplication = getLLIApplication(billDTO.getID());
        JasperPrint jasperPrint = getJasperPrintForDemandNote(billDTO, lliApplication);
        String proposedFileName = "demand-note-" + billDTO.getID() + ".pdf";
        String fileAbsolutePath = getPDFAbsolutePath(billDTO, proposedFileName);
        savePDFInSpecifiedPath(fileAbsolutePath, jasperPrint);
        return fileAbsolutePath;
    }

    private String getPDFAbsolutePath(BillDTO billDTO, String proposedFileName) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(FileTypeConstants.BASE_PATH);
        sb.append(FileTypeConstants.LLI_BILL_DIRECTORY);
        sb.append(TimeConverter.getYear(billDTO.getGenerationTime()));
        sb.append(File.separatorChar);
        sb.append(TimeConverter.getMonth(billDTO.getGenerationTime()));
        sb.append(File.separatorChar);

        File file = FileAPI.getInstance().createDirectory(sb.toString());
        return file.getPath() + File.separatorChar + proposedFileName;
    }

    private LLIApplication getLLIApplication(long billID) throws Exception {
        LLIApplication lliApplication = lliApplicationService.getNewFlowLLIApplicationByDemandNoteID(billID);
        if (lliApplication == null) {
            throw new RequestFailureException("No lli Application found with demand note id " + billID);
        }
        return lliApplication;
    }

    private JasperPrint getJasperPrintForDemandNote(BillDTO billDTO, LLIApplication lliApplication) throws Exception {
        InputStream inputStream = getInputStreamByApplicationType(lliApplication.getApplicationType());
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
        Map<String, Object> params = getPdfParameters(billDTO, lliApplication);
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(Arrays.asList(billDTO), false);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jrBeanCollectionDataSource);
        IOUtils.closeQuietly(inputStream);
        return jasperPrint;
    }

    private void savePDFInSpecifiedPath(String fileName, JasperPrint jasperPrint) throws Exception {
        log.debug("Saving in File System starts");
        JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);
        log.debug("Saving in File System done");
    }


    @Transactional(transactionType = TransactionType.READONLY)
    void viewPDFDN(long billId, OutputStream outputStream) throws Exception {
        BillDTO billDTO = billService.getBillDTOVerified(billId);
        JasperAPI.getInstance().renderPDFToBrowser((PdfMaterial) billDTO, outputStream);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    void viewPDFMonthlyBill(PdfMaterial pdf, OutputStream outputStream) throws Exception {
        JasperAPI.getInstance().renderPDFToBrowser(pdf, outputStream);
    }

    private void writePdfToBrowserStream(BillDTO billDTO, OutputStream outputStream) throws Exception {
        LLIApplication lliApplication = getLLIApplication(billDTO.getID());
        JasperPrint jasperPrint = getJasperPrintForDemandNote(billDTO, lliApplication);
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }

    private String getBillJasperTemplateLocator(int lliApplicationType) {
        return LLIConnectionConstants.mapOfDemandNoteTemplateToApplicationType.get(lliApplicationType);
    }

    private InputStream getInputStreamByApplicationType(int lliApplicationType) {
        InputStream inputStream = null;
        String billFileLocator = getBillJasperTemplateLocator(lliApplicationType);
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(billFileLocator);
        } catch (Exception e) {
            log.debug(e.getMessage());
            throw new RequestFailureException("No Bill Template Found");
        }
        return inputStream;
    }

    private Map<String, Object> getPdfParameters(BillDTO billDTO, LLIApplication lliApplication) throws Exception {
        log.info("Parameter collection");
        Map<String, Object> params = new HashMap<>();

        KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair =
                ClientAPI.getInstance().getPairOfClientDetailsAndClientContactDetails(
                        billDTO.getClientID(), ModuleConstants.Module_ID_LLI, ClientContactDetailsDTO.BILLING_CONTACT);
        ClientDetailsDTO clientDetailsDTO = pair.key;
        ClientContactDetailsDTO contactDetailsDTO = pair.value;

        getCommonInformation(params);
        getClientInformation(params, contactDetailsDTO, clientDetailsDTO);
        getBillSpecificInformation(params, billDTO);
        getConnectionSpecificInformation(params, lliApplication);
        return params;

    }

    private void getCommonInformation(Map<String, Object> params) {
        params.put("logo", "../../images/common/btcl_logo_heading.png");
        params.put("footerLeft", "Powered By <font color=blue>Reve Systems</font>");
        params.put("footerRight", "Bangladesh Telecommunications Company Limited");
        params.put("NB", "NB: ( Client will arrange and maintain local loops from BTCL switch to his offices on his own ).");
    }

    private void getClientInformation(Map<String, Object> params, ClientContactDetailsDTO contactDetailsDTO, ClientDetailsDTO clientDetailsDTO) {
        params.put("clientFullName", contactDetailsDTO.getRegistrantsName() + " " + contactDetailsDTO.getRegistrantsLastName());
        params.put("clientAddress", contactDetailsDTO.getAddress());
        params.put("clientEmail", contactDetailsDTO.getEmail() != null ? contactDetailsDTO.getEmail() : "N/A");
        params.put("clientLoginName", clientDetailsDTO.getLoginName());
    }

    private void getConnectionSpecificInformation(Map<String, Object> params, LLIApplication lliApplication) throws Exception {
        String demandNoteCause = "";
        String clientSuggestedBW = "";
        int lliApplicationType = lliApplication.getApplicationType();
        if (lliApplicationType == LLIConnectionConstants.NEW_CONNECTION) {

            LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(lliApplication);
            params.put("connectionName", lliConnectionInstance.getName().isEmpty() ? "Not Provided" : lliConnectionInstance.getName());
            String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
            String clientOfficeAddress = lliConnectionInstance.getLliOffices().get(0).getAddress();

            LLINewConnectionApplication lliNewConnectionApplication = (LLINewConnectionApplication) lliApplication;
            clientSuggestedBW += lliNewConnectionApplication.getBandwidth() + " Mbps";
            demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of <b>"
                    + clientSuggestedBW + "</b> Duplex Leased Line Internet Connection in <b>"
                    + clientOfficeAddress + "</b> has been issued.";
        } else if (lliApplicationType == LLIConnectionConstants.UPGRADE_BANDWIDTH) {
            LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(lliApplication);
            params.put("connectionName", lliConnectionInstance.getName().isEmpty() ? "Not Provided" : lliConnectionInstance.getName());
            String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
            String clientOfficeAddress = lliConnectionInstance.getLliOffices().get(0).getAddress();

            LLIUpgradeBandwidthApplication lliUpgradeBandwidthApplication = (LLIUpgradeBandwidthApplication) lliApplication;
            clientSuggestedBW += lliUpgradeBandwidthApplication.getBandwidth() + " Mbps";
            String existingBW = (lliConnectionInstance.getBandwidth() - lliUpgradeBandwidthApplication.getBandwidth()) + " Mbps";
            demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of additional <b>"
                    + clientSuggestedBW + "</b> along with <b>" + existingBW + "</b> i.e. <b>" + lliConnectionInstance.getBandwidth() + " Mbps</b>"
                    + " Duplex Leased Line Internet Connection in <b>" + clientOfficeAddress + "</b> has been issued.";
        } else if (lliApplicationType == LLIConnectionConstants.DOWNGRADE_BANDWIDTH) {
            LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(lliApplication);
            params.put("connectionName", lliConnectionInstance.getName().isEmpty() ? "Not Provided" : lliConnectionInstance.getName());
            String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
            String clientOfficeAddress = lliConnectionInstance.getLliOffices().get(0).getAddress();

            LLIDowngradeBandwidthApplication lliDowngradeBandwidthApplication = (LLIDowngradeBandwidthApplication) lliApplication;
            clientSuggestedBW += lliDowngradeBandwidthApplication.getBandwidth() + " Mbps";
            String existingBW = (lliConnectionInstance.getBandwidth() + lliDowngradeBandwidthApplication.getBandwidth()) + " Mbps";
            demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of downgrading <b>"
                    + clientSuggestedBW + "</b> from <b>" + existingBW + "</b> i.e. <b>" + lliConnectionInstance.getBandwidth() + " Mbps</b>"
                    + " Duplex Leased Line Internet Connection in <b>" + clientOfficeAddress + "</b> has been issued.";
        } else if (lliApplicationType == LLIConnectionConstants.TEMPORARY_UPGRADE_BANDWIDTH) {
            LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(lliApplication);
            params.put("connectionName", lliConnectionInstance.getName().isEmpty() ? "Not Provided" : lliConnectionInstance.getName());
            String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
            String clientOfficeAddress = lliConnectionInstance.getLliOffices().get(0).getAddress();

            List<LLIConnectionInstance> lliConnectionInstances = lliConnectionService.getLLIConnectionInstanceListByClientID(lliApplication.getClientID())
                    .stream()
                    .filter(
                            connection -> connection.getID() != lliConnectionInstance.getID()
                    ).collect(Collectors.toList());
            String restOfTheConnection = "";
            for (LLIConnectionInstance connection : lliConnectionInstances) {
                restOfTheConnection += "+ ( " + connection.getName() + " = " + connection.getBandwidth() + " Mbps )";
            }
            LLITemporaryUpgradeBandwidthApplication lliTemporaryUpgradeBandwidthApplication = (LLITemporaryUpgradeBandwidthApplication) lliApplication;
            clientSuggestedBW += lliTemporaryUpgradeBandwidthApplication.getBandwidth() + " Mbps";
            String existingBW = (lliConnectionInstance.getBandwidth() - lliTemporaryUpgradeBandwidthApplication.getBandwidth()) + " Mbps";
            demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of additional <b>"
                    + clientSuggestedBW + "</b> along with <b>" + existingBW + "</b> i.e. <b>" + lliConnectionInstance.getBandwidth() + " Mbps</b>"
                    + " for <b>" + lliTemporaryUpgradeBandwidthApplication.getDuration() + "</b> days"
                    + " in <b>" + clientOfficeAddress + "</b> has been issued.";
            // too many connection name overflow.
//					+ " Client Applied for ( " + lliConnectionInstance.getName() + " = " +lliConnectionInstance.getBandwidth() +" Mbps )" 
//					+ restOfTheConnection;
        } else if (lliApplicationType == LLIConnectionConstants.ADDITIONAL_PORT) {
            LLIAdditionalPortApplication lliAdditionalPortApplication = (LLIAdditionalPortApplication) lliApplication;
            LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(lliApplication);
            params.put("connectionName", lliConnectionInstance.getName().isEmpty() ? "Not Provided" : lliConnectionInstance.getName());
            String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
            String clientOfficeAddress = lliConnectionInstance.getLliOffices().get(0).getAddress();


            int portCount = lliAdditionalPortApplication.getPortCount();
            demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of <b>"
                    + portCount + "</b> additional ports in <b>"
                    + clientOfficeAddress + "</b> has been issued.";
        } else if (lliApplicationType == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP) {
            LLIAdditionalLocalLoopApplication lliAdditionalLocalLoopApplication = (LLIAdditionalLocalLoopApplication) lliApplication;
            LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(lliApplication);
            params.put("connectionName", lliConnectionInstance.getName().isEmpty() ? "Not Provided" : lliConnectionInstance.getName());
            String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
            String clientOfficeAddress = lliConnectionInstance.getLliOffices().get(0).getAddress();

            InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(lliAdditionalLocalLoopApplication.getPopID());
            if (inventoryItem.getID() == 0) {
                throw new RequestFailureException("No Pop Found");
            }
            demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of"
                    + " additional local loop from <b>" + inventoryItem.getName() + "</b> POP in <b>"
                    + clientOfficeAddress + "</b> has been issued.";
        } else if (lliApplicationType == LLIConnectionConstants.ADDITIONAL_CONNECTION_ADDRESS) {
            LLIAdditionalConnectionAddressApplication lliAdditionalConnectionAddressApplication = (LLIAdditionalConnectionAddressApplication) lliApplication;
            LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(lliApplication);
            params.put("connectionName", lliConnectionInstance.getName().isEmpty() ? "Not Provided" : lliConnectionInstance.getName());
            String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
            String clientOfficeAddress = lliConnectionInstance.getLliOffices().get(0).getAddress();

            demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of"
                    + " additional connection address in <b>" + lliAdditionalConnectionAddressApplication.getAddress()
                    + "</b> has been issued.";
        } else if (lliApplicationType == LLIConnectionConstants.SHIFT_CONNECTION_ADDRESS) {
            LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(lliApplication);
            params.put("connectionName", lliConnectionInstance.getName().isEmpty() ? "Not Provided" : lliConnectionInstance.getName());
            String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
            String clientOfficeAddress = lliConnectionInstance.getLliOffices().get(0).getAddress();

            LLIShiftAddressApplication lliShiftAddressApplication = (LLIShiftAddressApplication) lliApplication;
            LLIOffice lliOffice = lliConnectionInstance.getLliOffices()
                    .stream()
                    .filter(office -> office.getID() == lliShiftAddressApplication.getOfficeID())
                    .findFirst().
                            orElse(null);
            if (lliOffice == null) {
                throw new RequestFailureException("No Existing Office Found");
            }
            demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of"
                    + " shifting connection address in <b>" + lliShiftAddressApplication.getAddress()
                    + "</b> has been issued.";
        } else if (lliApplicationType == LLIConnectionConstants.SHIFT_POP) {
            LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(lliApplication);
            params.put("connectionName", lliConnectionInstance.getName().isEmpty() ? "Not Provided" : lliConnectionInstance.getName());
            String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
            String clientOfficeAddress = lliConnectionInstance.getLliOffices().get(0).getAddress();

            LLIShiftPopApplication lliShiftPOPApplication = (LLIShiftPopApplication) lliApplication;
            InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(lliShiftPOPApplication.getPopID());
            if (inventoryItem.getID() == 0) {
                throw new RequestFailureException("No Pop Found");
            }
            demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of"
                    + " shift POP to <b>" + inventoryItem.getName()
                    + "</b> has been issued.";
        } else if (lliApplicationType == LLIConnectionConstants.ADDITIONAL_IP) {
            LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(lliApplication);
            params.put("connectionName", lliConnectionInstance.getName().isEmpty() ? "Not Provided" : lliConnectionInstance.getName());
            String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
            String clientOfficeAddress = lliConnectionInstance.getLliOffices().get(0).getAddress();

            LLIAdditionalIPApplication lliAdditionalIPApplication = (LLIAdditionalIPApplication) lliApplication;
            demandNoteCause += "In the context of your application in <b>" + clientSuggestedDate + "</b> Demand Note of <b>"
                    + lliAdditionalIPApplication.getIpCount() + " additional IPs </b>"
                    + "has been issued.";
        } else if (lliApplicationType == LLIConnectionConstants.CLOSE_CONNECTION) {
            LLICloseConnectionApplication lliCloseConnectionApplication = (LLICloseConnectionApplication) lliApplication;
            LLIConnectionInstance lliConnectionInstance = lliConnectionService.getLLIConnectionByConnectionID(lliCloseConnectionApplication.getConnectionID());
            params.put("connectionName", lliConnectionInstance.getName().isEmpty() ? "Not Provided" : lliConnectionInstance.getName());
            String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");

            demandNoteCause += "In the context of your application in <b>"
                    + clientSuggestedDate + "</b> Demand Note for" +
                    " instant closing of connection <b>" + lliConnectionInstance.getName() + "</b> "
                    + "has been issued.";

        } else if (lliApplicationType == LLIConnectionConstants.BREAK_LONG_TERM) {
            LLIBreakLongTermApplication lliBreakLongTermApplication = (LLIBreakLongTermApplication) lliApplication;
            params.put("contractID", lliBreakLongTermApplication.getContractID() + "");
            String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");

            params.put("brokenBandwidth", lliBreakLongTermApplication.getBandwidth() + " Mbps");
            demandNoteCause += "In the context of your application in <b>"
                    + clientSuggestedDate + "</b> Short Bill for breaking long term contract ("
                    + "contract id: " + lliBreakLongTermApplication.getContractID() + ") "
                    + "has been issued.";
        }/*else if(lliApplicationType == LLIConnectionConstants.CHANGE_OWNERSHIP) {
			LLIChangeOwnershipApplication lliChangeOwnershipApplication = (LLIChangeOwnershipApplication) lliApplication;
			String sourceClientName = AllClientRepository.getInstance().getClientByClientID(lliChangeOwnershipApplication.getClientID()).getLoginName();
			String destinationClientName = AllClientRepository.getInstance().getClientByClientID(lliChangeOwnershipApplication.getDestinationClientID()).getLoginName();
			params.put("sourceClient", sourceClientName);
			params.put("destinationClient", destinationClientName);
			String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");
			demandNoteCause += "In the context of your application in <b>" 
					+ clientSuggestedDate + "</b> demand note of changing lli.Application.ownership from <b>"
					+ sourceClientName + "</b> to <b>"
					+ destinationClientName
					+ "</b> has been issued.";
					
		}*/ else if (lliApplicationType == LLIConnectionConstants.RECONNECT) {
            params.put("connectionName", "");
            String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd/MM/yyyy");

            demandNoteCause += "In the context of your application in <b>"
                    + clientSuggestedDate + "</b> demand note of reconnect connection for <b>"
                    + AllClientRepository.getInstance().getClientByClientID(lliApplication.getClientID()).getLoginName()
                    + "</b> has been issued.";
        } else {
            demandNoteCause += "";
        }
        params.put("demandNoteCause", demandNoteCause);
    }

    private void getBillSpecificInformation(Map<String, Object> params, BillDTO billDTO) {
        params.put("billGenerationDate", TimeConverter.getDateTimeStringByMillisecAndDateFormat(billDTO.getGenerationTime(), "dd/MM/yyyy"));
        params.put("billLastPaymentDate", TimeConverter.getDateTimeStringByMillisecAndDateFormat(billDTO.getLastPaymentDate(), "dd/MM/yyyy"));
        params.put("invoiceID", billDTO.getID() + "");
        params.put("discount", String.format("%.2f", billDTO.getDiscount()));
        params.put("VAT", String.format("%.2f", billDTO.getVAT()));
        params.put("total", String.format("%.2f", billDTO.getNetPayable()));
        params.put("demandedAmount", EnglishNumberToWords.convert((long) Math.ceil(billDTO.getNetPayable())));

        if (billDTO instanceof LLINewConnectionDemandNote) {
            LLINewConnectionDemandNote lliNewConnectionDemandNote = (LLINewConnectionDemandNote) billDTO;
            getNewConnectionDemandNoteSpecificInformation(params, lliNewConnectionDemandNote);
        } else if (billDTO instanceof LLISingleConnectionCommonDemandNote) {
            LLISingleConnectionCommonDemandNote lliSingleConnectionCommonDemandNote = (LLISingleConnectionCommonDemandNote) billDTO;
            getSingleConnectionDemandNoteSpecificInformation(params, lliSingleConnectionCommonDemandNote);
        } else if (billDTO instanceof LLIBreakLongTermDemandNote) {
            LLIBreakLongTermDemandNote lliBreakLongTermDemandNote = (LLIBreakLongTermDemandNote) billDTO;
            params.put("contractBreakingFine", String.format("%.2f", lliBreakLongTermDemandNote.getContractBreakingFine()));
            params.put("otherCost", String.format("%.2f", lliBreakLongTermDemandNote.getOtherCost()));
        } else if (billDTO instanceof LLICloseConnectionDemandNote) {
            LLICloseConnectionDemandNote lliCloseConnectionDemandNote = (LLICloseConnectionDemandNote) billDTO;
            params.put("closingOTC", String.format("%.2f", lliCloseConnectionDemandNote.getClosingOTC()));
            params.put("otherCost", String.format("%.2f", lliCloseConnectionDemandNote.getOtherCost()));
        } else if (billDTO instanceof LLIOwnerShipChangeDemandNote) {
            LLIOwnerShipChangeDemandNote lliOwnershipChangeDemandNote = (LLIOwnerShipChangeDemandNote) billDTO;
            params.put("ownershipChangeCharge", String.format("%.2f", lliOwnershipChangeDemandNote.getOwnerShipChangeCharge()));
            params.put("otherCost", String.format("%.2f", lliOwnershipChangeDemandNote.getOtherCosts()));
        } else if (billDTO instanceof LLIReconnectConnectionDemandNote) {
            LLIReconnectConnectionDemandNote lliReconnectConnectionDemandNote = (LLIReconnectConnectionDemandNote) billDTO;
            params.put("reconnectionCharge", String.format("%.2f", lliReconnectConnectionDemandNote.getReconnectionCharge()));
            params.put("otherCost", String.format("%.2f", lliReconnectConnectionDemandNote.getOtherCost()));
        } else {
            throw new RequestFailureException("This bill does not have a pdf template");
        }
    }

    private void getSingleConnectionDemandNoteSpecificInformation(Map<String, Object> params,
                                                                  LLISingleConnectionCommonDemandNote lliSingleConnectionCommonDemandNote) {
        params.put("securityMoney", String.format("%.2f", lliSingleConnectionCommonDemandNote.getSecurityMoney()));
        params.put("registrationCharge", "0.00");
        params.put("bwMRC", String.format("%.2f", lliSingleConnectionCommonDemandNote.getBandwidthCharge()));

        params.put("instantDegradationCharge", "0.00");


        String otherItems = "";
        String otherItemsCharge = "";
        List<ItemCost> itemCosts = lliSingleConnectionCommonDemandNote.getItemCosts();
        for (ItemCost itemCost : itemCosts) {
            otherItems += itemCost.item + "(other)<br>";
            otherItemsCharge += String.format("%.2f", itemCost.cost) + "<br>";
        }
        params.put("otherItems", otherItems);
        params.put("otherCharge", otherItemsCharge);

        params.put("advancedAmount", String.format("%.2f", lliSingleConnectionCommonDemandNote.getAdvancedAmount()));
        params.put("localLoopCharge", String.format("%.2f", lliSingleConnectionCommonDemandNote.getCoreCharge()));
        params.put("fibreCharge", String.format("%.2f", lliSingleConnectionCommonDemandNote.getFibreOTC()));
        params.put("firstXIpCharge", String.format("%.2f", lliSingleConnectionCommonDemandNote.getFirstXIpCost()));
        params.put("nextYIpCharge", String.format("%.2f", lliSingleConnectionCommonDemandNote.getNextYIpCost()));
        params.put("shiftCharge", String.format("%.2f", lliSingleConnectionCommonDemandNote.getShiftCharge()));
        params.put("portCharge", String.format("%.2f", lliSingleConnectionCommonDemandNote.getPortCharge()));
        params.put("downgradeCharge", String.format("%.2f", lliSingleConnectionCommonDemandNote.getDowngradeCharge()));
    }

    private void getNewConnectionDemandNoteSpecificInformation(Map<String, Object> params,
                                                               LLINewConnectionDemandNote lliNewConnectionDemandNote) {
        params.put("securityMoney", String.format("%.2f", lliNewConnectionDemandNote.getSecurityMoney()));
        params.put("registrationCharge", String.format("%.2f", lliNewConnectionDemandNote.getRegistrationFee()));
        params.put("bwMRC", String.format("%.2f", lliNewConnectionDemandNote.getBwMRC()));
        params.put("instantDegradationCharge", "0.00");
        String otherItems = "";
        String otherItemsCharge = "";
        List<ItemCost> itemCosts = lliNewConnectionDemandNote.getItemCosts();
        for (ItemCost itemCost : itemCosts) {
            otherItems += itemCost.item + "(other)<br>";
            otherItemsCharge += String.format("%.2f", itemCost.cost) + "<br>";
        }
        params.put("otherItems", otherItems);
        params.put("otherCharge", otherItemsCharge);

        params.put("advancedAmount", String.format("%.2f", lliNewConnectionDemandNote.getAdvanceAdjustment()));
        params.put("localLoopCharge", String.format("%.2f", lliNewConnectionDemandNote.getLocalLoopCharge()));
        params.put("fibreCharge", String.format("%.2f", lliNewConnectionDemandNote.getFibreOTC()));
    }

    @Transactional(transactionType = TransactionType.READONLY)
    void viewPDFAN(long appId, OutputStream outputStream, int moduleId) throws Exception {
        log.info("PDF rendering in browser starts");
        // make it generic by passing module id
        List<? extends OfficialLetter> list = officialLetterService
                .getOfficialLettersByApplicationIdAndLetterTypeAndModuleId(appId, OfficialLetterType.ADVICE_NOTE, moduleId);
        if (list.size() > 1) {
            throw new RequestFailureException("Multiple Advice Note Information found for app id " + appId + " module id "
                    + ModuleConstants.ActiveModuleMap.getOrDefault(moduleId, "N/A"));
        }


        OfficialLetter officialLetter = list.get(0);
        Class<? extends OfficialLetter> clazz = (Class<? extends OfficialLetter>) Class.forName(officialLetter.getClassName());
        Constructor<? extends OfficialLetter> constructor;
        PdfMaterial adviceNote;
        if (moduleId == ModuleConstants.Module_ID_VPN) {
            // TODO architectural change; link wise. or aggregated
            // TODO change this VPN Application will come from front end
            VPNApplication vpnApplication = ServiceDAOFactory.getService(VPNApplicationService.class).getApplicationByApplicationId(officialLetter.getApplicationId());
            constructor = clazz.getConstructor(VPNApplication.class);
            adviceNote = (PdfMaterial) constructor.newInstance(vpnApplication);
            ((OfficialLetter) adviceNote).setId(officialLetter.getId());
        } else {
            constructor = clazz.getConstructor(OfficialLetter.class);
            adviceNote = (PdfMaterial) constructor.newInstance(officialLetter);
        }
        runJasperAPI(adviceNote, outputStream);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    void viewPDFWO(long appId, int moduleId, long vendorId, OutputStream outputStream) throws Exception {
        log.info("PDF rendering in browser starts");
        OfficialLetter officialLetter = officialLetterService.getWorkOrderByApplicationVendorModule(appId, vendorId, moduleId);
        String className = officialLetter.getClassName();
        log.info("class name for official letter : " + officialLetter.getId() + " : " + className);
        Class<? extends OfficialLetter> classObject = (Class<? extends OfficialLetter>) Class.forName(className);
        Constructor<? extends OfficialLetter> constructor;
        PdfMaterial pdfMaterial;
        constructor = classObject.getConstructor(Long.TYPE, Long.TYPE, Long.TYPE, List.class);

        if (moduleId == ModuleConstants.Module_ID_LLI) {
            int appType = lliApplicationService.getApplicationTypeByApplicationID(appId);
            EFRService efrService = ServiceDAOFactory.getService(EFRService.class);
            List<lli.Application.EFR.EFR> efrs =
                    (appType == LLIConnectionConstants.CLOSE_CONNECTION)
                            ? efrService.getEFRsToClose(appId)
                            : efrService.getSelected(appId);
            List<lli.Application.EFR.EFR> vendorSpecificWO = efrs.stream()
                    .filter(t -> t.getVendorID() == vendorId)
                    .collect(Collectors.toList());
            pdfMaterial = (PdfMaterial) constructor.newInstance(appId, officialLetter.getClientId(), vendorId, vendorSpecificWO);

        } else if (moduleId == ModuleConstants.Module_ID_NIX) {
            List<NIXEFR> selectedEFRNIX = ServiceDAOFactory.getService(GlobalService.class).getAllObjectListByCondition(
                    NIXEFR.class, new NIXEFRConditionBuilder()
                            .Where()
                            .vendorEquals(vendorId)
                            .workGivenEquals(1)
                            .applicationEquals(appId)
                            .getCondition()
            );
            pdfMaterial = (PdfMaterial) constructor.newInstance(appId, officialLetter.getClientId(), vendorId, selectedEFRNIX);

        } else if (moduleId == ModuleConstants.Module_ID_VPN) {
            List<EFR> selectedEFRVPN = ServiceDAOFactory.getService(GlobalService.class).getAllObjectListByCondition(
                    EFR.class, new EFRConditionBuilder()
                            .Where()
                            .vendorIDEquals(vendorId)
                            .isSelected(true)
                            .applicationIdEquals(appId)
                            .getCondition()
            );
            pdfMaterial = (PdfMaterial) constructor.newInstance(appId, officialLetter.getClientId(), vendorId, selectedEFRVPN);

        } else {
            throw new RequestFailureException("Invalid Module For Work Order");
        }
        ((OfficialLetter) pdfMaterial).setId(officialLetter.getId());
        runJasperAPI(pdfMaterial, outputStream);

    }

    private void runJasperAPI(PdfMaterial pdfMaterial, OutputStream outputStream) {
        try {
            JasperAPI.getInstance().renderPDFToBrowser(pdfMaterial, outputStream);
        } catch (Exception e) {
            log.fatal(e.getMessage());
//			throw new RequestFailureException(e.getMessage());
        }
    }

    @Transactional(transactionType = TransactionType.READONLY)
    void viewPDFVpnLinkAN(long linkId, OutputStream outputStream) throws Exception {

        VPNApplicationLink link = ServiceDAOFactory.getService(VPNApplicationService.class).getVPNApplicationLinkByApplicationLinkId(linkId);

        OfficialLetter officialLetter = globalService.findByPK(OfficialLetter.class, link.getAdviceNoteId());
        Class<? extends OfficialLetter> clazz = (Class<? extends OfficialLetter>) Class.forName(officialLetter.getClassName());
        Constructor<? extends OfficialLetter> constructor = clazz.getConstructor(VPNApplication.class);

        VPNApplication vpnApplication = ServiceDAOFactory.getService(VPNApplicationService.class)
                .getVPNApplicationByApplicationIdWithoutVPNLinks(
                        officialLetter.getApplicationId()
                );
        vpnApplication.setVpnApplicationLinks(Arrays.asList(link));
        PdfMaterial adviceNote = (PdfMaterial) constructor.newInstance(vpnApplication);
        ((OfficialLetter) adviceNote).setId(officialLetter.getId());
        runJasperAPI(adviceNote, outputStream);
    }


    void viewPDFSubscriberLedgerReport(long clientId, List<BillPaymentDTOForLedger> dtos, OutputStream os) throws Exception {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("ledger/subscriber_ledger_report.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
        Map<String, Object> params = getPdfParametersForSubscriberLedgerReport(clientId, dtos);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
        JasperExportManager.exportReportToPdfStream(jasperPrint, os);
    }

    private Map<String, Object> getPdfParametersForSubscriberLedgerReport(long clientId, List<BillPaymentDTOForLedger> dtos) throws Exception {
        Map<String, Object> map = new HashMap<>();
        KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair = ClientAPI.getInstance().getPairOfClientDetailsAndClientContactDetails(clientId, 0, ClientContactDetailsDTO.BILLING_CONTACT );
        map.put("userName", pair.getKey().getLoginName());
        map.put("fullName", pair.getValue().getRegistrantsName() + pair.getValue().getRegistrantsLastName());
        map.put("billingAddress", pair.getValue().getAddress());
        map.put("email", pair.getValue().getEmail());
        map.put("mobile", pair.getValue().getPhoneNumber());
        map.put("logo", "../../images/common/BTCL-small-Logo.png");
        double totalBTCL = 0, totalVAT = 0, totalTotal = 0, totalPaid = 0;
//        for(BillPaymentDTOForLedger dto : dtos) {
//            totalBTCL += Double.valueOf(dto.getBtclAmount());
//            dto.setBtclAmount(String.format("%.2f", Double.valueOf(dto.getBtclAmount())));
//            totalVAT += Double.valueOf(dto.getVat());
//            dto.setVat(String.format("%.2f", Double.valueOf(dto.getVat())));
//            totalTotal += Double.valueOf(dto.getTotalAmount());
//            dto.setTotalAmount(String.format("%.2f", Double.valueOf(dto.getTotalAmount())));
//            totalPaid += dto.getPaidAmount().equals("N/A") ? 0 : Double.valueOf(dto.getPaidAmount());
//            dto.setPaidAmount(dto.getPaidAmount().equals("N/A") ? "0.00": String.format("%.2f", Double.valueOf(dto.getPaidAmount())));
//        }
        map.put("totalBTCLAmount", String.format("%.2f", totalBTCL));
        map.put("totalVAT", String.format("%.2f", totalVAT));
        map.put("totalTotalAmount", String.format("%.2f", totalTotal));
        map.put("totalPaidAmount", String.format("%.2f", totalPaid));

        map.put("list", dtos);
        return map;
    }

    void viewPDFRequestLetter(JsonObject params, OutputStream outputStream) throws Exception {

        RequestLetterPdf requestLetterPdf = new RequestLetterPdf();
        requestLetterPdf.setParams(params);
        JasperAPI.getInstance().renderPDFToBrowser(requestLetterPdf, outputStream);
    }

    public void viewPDFClearanceCertificateWithDues(JsonObject params, OutputStream outputStream) throws Exception {

        ClearanceCertificatePdf clearanceCertificatePdf = new ClearanceCertificatePdf();
        clearanceCertificatePdf.setParams(params);
        JasperAPI.getInstance().renderPDFToBrowser(clearanceCertificatePdf, outputStream);
    }

    public void viewPDFClearanceCertificateWithoutDues(JsonObject params, OutputStream outputStream) throws Exception {

        ClearanceCertificateNoDuePdf clearanceCertificateNoDuePdf = new ClearanceCertificateNoDuePdf();
        clearanceCertificateNoDuePdf.setParams(params);
        JasperAPI.getInstance().renderPDFToBrowser(clearanceCertificateNoDuePdf, outputStream);
    }

    public void viewPDFMultipleBill(JsonObject params, OutputStream outputStream) throws Exception {
        MultipleMonthBillPdf multipleMonthBillPdf = new MultipleMonthBillPdf();
        multipleMonthBillPdf.setParams(params);
        JasperAPI.getInstance().renderPDFToBrowser(multipleMonthBillPdf, outputStream);
    }

    public void viewPDFFinalBill(JsonObject params, OutputStream outputStream) throws Exception {
        MultipleMonthBillPdf multipleMonthBillPdf = new MultipleMonthBillPdf();
        multipleMonthBillPdf.setParams(params);
        JasperAPI.getInstance().renderPDFToBrowser(multipleMonthBillPdf, outputStream);
    }
}
