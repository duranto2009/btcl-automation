package vpn.workOrder;

import annotation.TableName;
import api.ClientAPI;
import api.FileAPI;
import application.ApplicationService;
import application.ApplicationType;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillConstants;
import common.pdf.PdfMaterial;
import entity.efr.EFR;
import entity.efr.EFRBean;
import entity.efr.EFRConditionBuilder;
import file.FileTypeConstants;
import global.GlobalService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import officialLetter.OfficialLetter;
import officialLetter.OfficialLetterConcern;
import officialLetter.OfficialLetterService;
import officialLetter.OfficialLetterType;
import user.UserDTO;
import user.UserRepository;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationService;
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
public class VPNWorkOrder extends OfficialLetter implements PdfMaterial {

    long vendorId;
    List<EFR> efrs;
    public VPNWorkOrder(long applicationId, long clientId, long vendorId, List<EFR> selectedEFRs) {
        this.vendorId = vendorId;
        this.efrs = selectedEFRs;
        this.applicationId = applicationId;
        this.clientId = clientId;
        this.moduleId = ModuleConstants.Module_ID_VPN;
        this.className = this.getClass().getCanonicalName();
        this.officialLetterType = OfficialLetterType.WORK_ORDER;
        this.creationTime = System.currentTimeMillis();
        this.lastModificationTime = System.currentTimeMillis();
        this.isDeleted = false;
    }
    public VPNWorkOrder(long applicationId, long clientId) {
        setClientId(clientId);
        setApplicationId(applicationId);
        setModuleId(ModuleConstants.Module_ID_VPN);
        setClassName(this.getClass().getCanonicalName());
        setLastModificationTime(System.currentTimeMillis());
        setOfficialLetterType(OfficialLetterType.WORK_ORDER);
        setCreationTime(System.currentTimeMillis());
        setDeleted(false);
    }

    public VPNWorkOrder(OfficialLetter ol) {
        setId(ol.getId());
        setModuleId(ol.getModuleId());
        setClientId(ol.getClientId());
        setApplicationId(ol.getApplicationId());
        setOfficialLetterType(ol.getOfficialLetterType());
        setClassName(ol.getClassName());
        setCreationTime(ol.getCreationTime());
        setLastModificationTime(ol.getLastModificationTime());
        setDeleted(ol.isDeleted());

    }

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("btcl_logo_heading", "../../images/common/logo_BTCL_Aim.png");
//        map.put("btcl_logo_heading", "C:/Users/REVE/Desktop/logo_BTCL_Aim.png");
        map.put("work_order_no", String.valueOf(this.getId()));
        OfficialLetterService officialLetterService = ServiceDAOFactory.getService(OfficialLetterService.class);

        List<OfficialLetterConcern> recipientList = officialLetterService.getRecipientListByOfficialLetterId(this.getId());
        OfficialLetterConcern vendor = recipientList.stream()
                .filter(officialLetterService.workOrderToRecipient())
                .findFirst()
                .orElseThrow(() -> new RequestFailureException("No Work Order Recipient Found"));

        List<String> ccList = recipientList.stream()
                .filter(officialLetterService.ccRecipient())
                .map(t -> {
                    try {
                        UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(t.getRecipientId());
                        return userDTO.getDesignation() + ", " + userDTO.getDepartmentName();
                    } catch (Exception e) {
                        return ClientAPI.getInstance().getFullNameOfAClient(clientId, ModuleConstants.Module_ID_VPN, ClientContactDetailsDTO.BILLING_CONTACT);
                    }
                })
                .collect(Collectors.toList());

        UserDTO recipient = UserRepository.getInstance().getUserDTOByUserID(vendor.getRecipientId());
        UserDTO sender = UserRepository.getInstance().getUserDTOByUserID(vendor.getSenderId());
        map.put("recipient_name", recipient.isBTCLPersonnel() ? recipient.getDesignation() + recipient.getDepartmentName() : recipient.getFullName());
        map.put("recipient_address", recipient.getAddress() == null ? "" : recipient.getAddress());


        map.put("letter_body",
                "With regard to the mentioned subject, please take necessary measures to set up internet to the client(s) as per the table.\n" +
                        "You are requested to do this work under Agreement for Providing BTCL Quick Broadband (BQB) Service to the new\n" +
                        "customers other than Info Sarkar project with mentioned cautions.");

        map.put("sender_name", sender.getFullName());
        map.put("sender_department", sender.getDepartmentName());
        map.put("sender_designation", sender.getDesignation());


        VPNApplication application = ServiceDAOFactory.getService(VPNApplicationService.class).getApplicationByApplicationId(this.getApplicationId());

        if (application.getApplicationType() == ApplicationType.VPN_CLOSE) {
            map.put("subject", "Subject:  Work Order to close Virtual Private Network local loop.");
        } else {
            map.put("subject", "Subject:  Work Order to set up Virtual Private Network local loop.");
        }

        map.put("ccList", ccList);

        List<EFRBean> list = this.efrs
                        .stream()
                        .map(t -> new EFRBean(
                                t.getSource(),
                                EFR.TERMINAL.get((long) t.getSourceType()),
                                t.getDestination(),
                                EFR.TERMINAL.get((long) t.getDestinationType()),
                                t.getProposedLoopDistance()))
                        .collect(Collectors.toList());

        map.put("efrBeanList", list);

        return map;
    }

    @Override
    public String getResourceFile() {
        return BillConstants.VPN_WORK_ORDER;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        return FileAPI.getInstance().getFilePath(FileTypeConstants.VPN_BILL_DIRECTORY, "vpn-work-order-" + this.applicationId +"_" + this.vendorId+ ".pdf", this.getCreationTime());
    }

}
