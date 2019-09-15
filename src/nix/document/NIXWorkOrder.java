package nix.document;

import annotation.TableName;
import api.ClientAPI;
import api.FileAPI;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillConstants;
import common.pdf.PdfMaterial;
import file.FileTypeConstants;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import nix.application.NIXApplication;
import nix.application.NIXApplicationService;
import nix.constants.NIXConstants;
import nix.efr.NIXEFR;
import nix.efr.NIXEFRService;
import officialLetter.OfficialLetter;
import officialLetter.OfficialLetterConcern;
import officialLetter.OfficialLetterService;
import officialLetter.OfficialLetterType;
import user.UserDTO;
import user.UserRepository;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@EqualsAndHashCode(callSuper = false)
@Data
@TableName("official_letter")
@NoArgsConstructor
@Log4j
public class NIXWorkOrder extends OfficialLetter implements PdfMaterial {
    long vendorId;
    List<NIXEFR> efrs;

    public NIXWorkOrder(long applicationId, long clientId) {
        setClientId(clientId);
        setApplicationId(applicationId);
        setModuleId(ModuleConstants.Module_ID_NIX);
        setClassName(this.getClass().getCanonicalName());
        setLastModificationTime(System.currentTimeMillis());
        setOfficialLetterType(OfficialLetterType.WORK_ORDER);
        setCreationTime(System.currentTimeMillis());
        setDeleted(false);
    }

    public NIXWorkOrder(long applicationId, long clientId, long vendorId, List<NIXEFR> selectedEFRs) {
        this.vendorId = vendorId;
        this.efrs = selectedEFRs;
        this.applicationId = applicationId;
        this.clientId = clientId;
        this.moduleId = ModuleConstants.Module_ID_NIX;
        this.className = this.getClass().getCanonicalName();
        this.officialLetterType = OfficialLetterType.WORK_ORDER;
        this.creationTime = System.currentTimeMillis();
        this.isDeleted = false;
    }

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("btcl_logo_heading", "../../images/common/btcl_logo_heading.png");
        long applicationId = this.getApplicationId();
//        long loggedInUserId = LoggedInUserService.

        OfficialLetterService officialLetterService = ServiceDAOFactory.getService(OfficialLetterService.class);

//        officialLetterService.getSenderByAppIdAndVendorIdAndOfficialLetterType()
//        officialLetters.stream()
//                .map()
        //populate with actual data
        List<OfficialLetterConcern> recipientList = officialLetterService.getRecipientListByOfficialLetterId(this.getId());
        OfficialLetterConcern vendor = recipientList.stream()
                .filter(officialLetterService.toRecipient())
                .findFirst()
                .orElseThrow(() -> new RequestFailureException("No Work Order Recipient Found"));
        vendorId = vendor.getRecipientId();

        List<String> ccList = recipientList.stream()
                .filter(officialLetterService.ccRecipient())
                .map(t -> {
                    try {

                        UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(t.getRecipientId());
                        return userDTO.getDesignation() + ", " + userDTO.getDepartmentName();
                    } catch (Exception e) {
                        try {
                            KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair =
                                    ClientAPI.getInstance().getPairOfClientDetailsAndClientContactDetails(
                                            this.getClientId(), this.getModuleId(), ClientContactDetailsDTO.BILLING_CONTACT);

                            ClientContactDetailsDTO contactDetailsDTO = pair.value;
                            return contactDetailsDTO.getRegistrantsName() + contactDetailsDTO.getRegistrantsLastName();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                    return "";
                })
                .collect(Collectors.toList());

        UserDTO recipient = UserRepository.getInstance().getUserDTOByUserID(vendor.getRecipientId());
        UserDTO sender = UserRepository.getInstance().getUserDTOByUserID(vendor.getSenderId());

        //no null check needed as repository checks for null already.


        map.put("recipient_name", recipient.isBTCLPersonnel() ? recipient.getDesignation() + recipient.getDepartmentName() : recipient.getFullName());
        map.put("recipient_address", recipient.getAddress() == null ? "" : recipient.getAddress());


        map.put("letter_body",
                "With regard to the mentioned subject, please take necessary measures to set up internet to the client(s) as per the table.\n" +
                        "You are requested to do this work under Agreement for Providing BTCL Quick Broadband (BQB) Service to the new\n" +
                        "customers other than Info Sarkar project with mentioned cautions.");

        map.put("sender_name", sender.getFullName());
        map.put("sender_department", sender.getDepartmentName());
        map.put("sender_designation", sender.getDesignation());


        NIXApplication nixApplication = ServiceDAOFactory.getService(NIXApplicationService.class).getApplicationById(this.getApplicationId());

        if (nixApplication.getType() == NIXConstants.NIX_CLOSE_APPLICATION) {
            map.put("subject", "Subject:  Work Order to close National Internet Exchange local loop.");
        } else {
            map.put("subject", "Subject:  Work Order to set up National Internet Exchange local loop.");
        }

        map.put("ccList", ccList);

        List<EFRBean> list =
                this.efrs
                        .stream()
                        .map(t -> new EFRBean(
                                t.getSource(),
                                EFR.TERMINAL.get((long) t.getSourceType()),
                                t.getDestination(),
                                EFR.TERMINAL.get((long) t.getDestinationType()),
                                t.getProposedDistance(),
                                t.getActualDistance()

                                )
                        )
                        .collect(Collectors.toList());

        map.put("efrBeanList", list);

        return map;
    }

    @Override
    public String getResourceFile() {
        return BillConstants.NIX_WORK_ORDER;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        return FileAPI.getInstance().getFilePath(FileTypeConstants.NIX_BILL_DIRECTORY, "nix-work-order-" + this.applicationId +"_" + this.vendorId+ ".pdf", this.getCreationTime());
    }

}
