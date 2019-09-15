package lli.workOrder;

import annotation.TableName;
import api.ClientAPI;
import api.FileAPI;
import application.ApplicationType;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillConstants;
import common.pdf.PdfMaterial;
import file.FileTypeConstants;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRBean;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.connection.LLIConnectionConstants;
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
import util.ServiceDAOFactory;
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
public class LLIWorkOrder extends OfficialLetter implements PdfMaterial {
    long vendorId;
    List<EFR> efrs;

    public LLIWorkOrder (long applicationId, long clientId, long vendorId, List<EFR> selectedEFRs) {
        this.vendorId = vendorId;
        this.efrs = selectedEFRs;
        this.applicationId = applicationId;
        this.clientId = clientId;
        this.moduleId = ModuleConstants.Module_ID_LLI;
        this.className = this.getClass().getCanonicalName();
        this.officialLetterType = OfficialLetterType.WORK_ORDER;
        this.creationTime = System.currentTimeMillis();
        this.lastModificationTime = System.currentTimeMillis();
        this.isDeleted = false;
    }

    public LLIWorkOrder(long applicationId, long clientId) {
        setClientId(clientId);
        setApplicationId(applicationId);
        setModuleId(ModuleConstants.Module_ID_LLI);
        setClassName(this.getClass().getCanonicalName());
        setLastModificationTime(System.currentTimeMillis());
        setOfficialLetterType(OfficialLetterType.WORK_ORDER);
        setCreationTime(System.currentTimeMillis());
        setDeleted(false);
    }

    public LLIWorkOrder(OfficialLetter ol) {
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
                        return ClientAPI.getInstance().getFullNameOfAClient(clientId, ModuleConstants.Module_ID_LLI,
                                ClientContactDetailsDTO.BILLING_CONTACT);
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


        LLIApplication application = ServiceDAOFactory.getService(LLIApplicationService.class).getLLIApplicationByApplicationID(
                this.getApplicationId()
        );

        if (application.getApplicationType() == LLIConnectionConstants.CLOSE_CONNECTION) {
            map.put("subject", "Subject:  Work Order to close Lease Line Internet local loop.");
        } else {
            map.put("subject", "Subject:  Work Order to set up Lease Line Internet local loop.");
        }

        map.put("ccList", ccList);

        List<EFRBean> list = this.efrs
                .stream()
                .map(t -> new EFRBean(
                        t.getSource(),
                        EFR.TERMINAL.get((long) t.getSourceType()),
                        t.getDestination(),
                        EFR.TERMINAL.get((long) t.getDestinationType()),
                        t.getProposedLoopDistance(),
                        t.getActualLoopDistance()
                ))
                .collect(Collectors.toList());

        map.put("efrBeanList", list);

        return map;
    }

    @Override
    public String getResourceFile() throws Exception {
        return BillConstants.VPN_WORK_ORDER;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        return FileAPI.getInstance().getFilePath(
                FileTypeConstants.LLI_BILL_DIRECTORY,
                "lli-work-order-" + this.applicationId
                + "_" + this.vendorId + ".pdf", this.getCreationTime()
                );
    }
}
