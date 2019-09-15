package lli.Application;

import java.io.File;
import java.util.*;

import api.FileAPI;
import common.client.ClientDTO;
import file.FileTypeConstants;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRBean;
import lli.connection.LLIConnectionConstants;
import common.pdf.PdfMaterial;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import user.UserDTO;
import util.ServiceDAOFactory;
import util.TimeConverter;

public class WorkOrderDocument implements PdfMaterial {

    private static final String GAP_STRING = "............................";

    private static final Logger logger = LoggerFactory.getLogger(WorkOrderDocument.class);

    private static final String NEW_CONNECTION_TEMPLATE = "lli/new-connection/work-order.jasper";

    private static final String CLOSE_CONNECTION_TEMPLATE = "lli/close-connection/work-order.jasper";

    private static final String BTCL_LOGO_HEADING = "../../images/common/btcl_logo_heading.png";

    private static final String LLI_NEW_CONNECTION_WORK_ORDER_DIR = "lli/new-connection/work-order/";

    private static final String LLI_CLOSE_CONNECTION_WORK_ORDER_DIR = "lli/close-connection/work-order/";

    String recipientName;
    String recipientDesignation;
    String recipientAddress;
    String clientName;
    String clientAddress;
    double bandwidth;
    String senderName;
    String senderDesignation;
    String senderAddress;
    List<String> ccList;
    String outputFilePath;
    String ref;
    String resourcePath;

    List<CCBean> ccBeanList;
    List<EFRBean> efrBeanList;

    public WorkOrderDocument(int applicationType, long applicationId, long vendorId) {
        this.resourcePath = (applicationType == LLIConnectionConstants.NEW_CONNECTION) ? NEW_CONNECTION_TEMPLATE : CLOSE_CONNECTION_TEMPLATE;
        this.outputFilePath = WorkOrderDocument.generateFilePath(applicationId, vendorId);
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public List<String> getCcList() {
        return ccList;
    }

    public void setCcList(List<String> ccList) {
        this.ccList = ccList;
    }


    public String getRecipientDesignation() {
        return recipientDesignation;
    }

    public void setRecipientDesignation(String recipientDesignation) {
        this.recipientDesignation = recipientDesignation;
    }

    public String getSenderDesignation() {
        return senderDesignation;
    }

    public void setSenderDesignation(String senderDesignation) {
        this.senderDesignation = senderDesignation;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public List<CCBean> getCcBeanList() {
        return ccBeanList;
    }

    public void setCcBeanList(List<CCBean> ccBeanList) {
        this.ccBeanList = ccBeanList;
    }

    public List<EFRBean> getEfrBeanList() {
        return efrBeanList;
    }

    public void setEfrBeanList(List<EFRBean> efrBeanList) {
        this.efrBeanList = efrBeanList;
    }

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("btcl_logo_heading", WorkOrderDocument.BTCL_LOGO_HEADING);
        params.put("recipient_name", this.recipientName);
        params.put("recipient_designation", this.recipientDesignation);
        params.put("recipient_address", this.recipientAddress);
        params.put("client_name", this.clientName);
        params.put("client_address", this.clientAddress);
        params.put("client_bandwidth", Double.toString(this.bandwidth));
        params.put("sender_name", this.senderName);
        params.put("sender_designation", this.senderDesignation);
        params.put("sender_address", this.senderAddress);
        params.put("ref", this.ref);
        //params.put("ccList", this.ccList);
        params.put("ccList", new JRBeanCollectionDataSource(this.ccList));

        params.put("efrBeanList", new JRBeanCollectionDataSource(this.efrBeanList));

        return params;
    }

    @Override
    public String getResourceFile() {
        return this.resourcePath;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        return this.outputFilePath;
    }

    public void populateData(ClientDTO clientDTO, UserDTO userDTO, List<EFR> efrs, UserDTO vendorUserDTO) {
        setRecipientName(vendorUserDTO.getFullName());
        setRecipientDesignation(vendorUserDTO.getDesignation());
        setRecipientAddress(vendorUserDTO.getMailAddress());
        setClientName(clientDTO.getName() + " " + clientDTO.getLastName());
        setClientAddress(clientDTO.getAddress());

        this.efrBeanList = new BeanList().fromEFR(efrs);

        setSenderName(userDTO.getFullName());
        setSenderDesignation(userDTO.getDesignation());
        setSenderAddress(userDTO.getMailAddress());
        // TODO: use reference
        setRef(" ");
        // TODO: set cc
        //setCcList(Arrays.asList("", "", "", "", "", "", "", "", "", ""));
        this.ccBeanList = new BeanList().fromCC(Arrays.asList("", "", "", "", "", "", "", "", "", ""));
    }

    public static String generateFilePath(long applicationId, long vendorId) {
        int applicationType = LLIConnectionConstants.NEW_CONNECTION;
        try {
            applicationType = ServiceDAOFactory.getDAO(LLIApplicationDAO.class).getApplicationTypeById(applicationId);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }

        String baseName = applicationId + "_" + vendorId;

        String proposedFileName = baseName + ".pdf";
        StringBuilder sb = new StringBuilder()
                .append(FileTypeConstants.BASE_PATH)
                .append(applicationType == LLIConnectionConstants.NEW_CONNECTION ? LLI_NEW_CONNECTION_WORK_ORDER_DIR : LLI_CLOSE_CONNECTION_WORK_ORDER_DIR)
                // TODO:maruf: change system time: old documents will not be found
                .append(TimeConverter.getYear(System.currentTimeMillis()))
                .append(File.separatorChar)
                // TODO:maruf: change system time: old documents will not be found
                .append(TimeConverter.getMonth(System.currentTimeMillis()))
                .append(File.separatorChar);

        try {
            return FileAPI.getInstance().createDirectory(sb.toString()).getPath() + File.separatorChar + proposedFileName;
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            return sb.toString() + File.separatorChar + proposedFileName;
        }
    }



    public class CCBean {
        String name;

        public CCBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    class BeanList {
        public List<EFRBean> fromEFR(List<EFR> efrs) {
            List<EFRBean> efrBeanList = new ArrayList<>();

            for (EFR efr : efrs) {
                efrBeanList.add(
                        new EFRBean(
                                efr.getSource(),
                                EFR.TERMINAL.get(efr.getSourceType()),
                                efr.getDestination(),
                                EFR.TERMINAL.get(efr.getDestinationType()),
                                efr.getProposedLoopDistance(),
                                efr.getActualLoopDistance()
                        )
                );
            }

            return efrBeanList;
        }

        public List<CCBean> fromCC(List<String> ccList) {
            List<CCBean> ccBeans = new ArrayList<>();
            for (String cc : ccList) ccBeans.add(new CCBean(cc));
            return ccBeans;
        }
    }
}
