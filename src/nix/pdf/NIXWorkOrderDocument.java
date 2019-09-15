package nix.pdf;

import api.FileAPI;
import common.bill.BillConstants;
import common.client.ClientDTO;
import common.pdf.PdfMaterial;
import file.FileTypeConstants;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import nix.efr.NIXEFR;
import nix.efr.NIXEFRBean;
import officialLetter.OfficialLetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import user.UserDTO;

import java.util.*;

public class NIXWorkOrderDocument extends OfficialLetter implements PdfMaterial {
    private static final String GAP_STRING = "............................";

    private static final Logger logger = LoggerFactory.getLogger(NIXWorkOrderDocument.class);

    private static final String NEW_CONNECTION_TEMPLATE = "nix/new-connection/work-order-vpn.jasper";

    private static final String CLOSE_CONNECTION_TEMPLATE = "nix/close-connection/work-order-vpn.jasper";

    private static final String BTCL_LOGO_HEADING = "../../images/common/btcl_logo_heading.png";

    private static final String NIX_NEW_CONNECTION_WORK_ORDER_DIR = "nix/new-connection/work-order/";

    private static final String NIX_CLOSE_CONNECTION_WORK_ORDER_DIR = "nix/close-connection/work-order/";
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
    List<NIXEFRBean> efrBeanList;

    public NIXWorkOrderDocument(int applicationType, long applicationId, long vendorId) throws Exception {
        this.resourcePath = BillConstants.NIX_WORK_ORDER;

        this.outputFilePath = (String) NIXWorkOrderDocument.generateFilePath(applicationId, vendorId);
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

    public List<NIXEFRBean> getEfrBeanList() {
        return efrBeanList;
    }

    public void setEfrBeanList(List<NIXEFRBean> efrBeanList) {
        this.efrBeanList = efrBeanList;
    }

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("btcl_logo_heading", NIXWorkOrderDocument.BTCL_LOGO_HEADING);
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
        params.put("ccList", this.ccList);

        params.put("efrBeanList", this.efrBeanList);

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

    public void populateData(ClientDTO clientDTO, UserDTO userDTO, List<NIXEFR> efrs, UserDTO vendorUserDTO) {
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

    public static String generateFilePath(long applicationId, long vendorId) throws Exception {
        return FileAPI.getInstance().getFilePath(
                FileTypeConstants.NIX_BILL_DIRECTORY,
                "nix-work-order_" + applicationId +"_" +vendorId+".pdf",System.currentTimeMillis());
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
        public List<NIXEFRBean> fromEFR(List<NIXEFR> efrs) {
            List<NIXEFRBean> efrBeanList = new ArrayList<>();

            for (NIXEFR efr : efrs) {
                efrBeanList.add(
                        new NIXEFRBean(
                                efr.getSource(),
                                NIXEFR.TERMINAL.get(efr.getSourceType()),
                                efr.getDestination(),
                                NIXEFR.TERMINAL.get(efr.getDestinationType()),
                                efr.getProposedDistance()
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
