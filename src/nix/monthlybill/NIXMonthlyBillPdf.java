package nix.monthlybill;

import api.ClientAPI;
import api.FileAPI;
import common.ModuleConstants;
import common.Month;
import common.bill.BillConstants;
import common.pdf.PdfMaterial;
import file.FileTypeConstants;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import nix.common.NIXClientService;
import nix.monthlybillsummary.NIXMonthlyBillSummaryByClient;
import util.EnglishNumberToWords;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NIXMonthlyBillPdf implements PdfMaterial {

    NIXMonthlyBillByClient bill;

    NIXMonthlyBillSummaryByClient billSummary;

    public NIXMonthlyBillPdf(NIXMonthlyBillByClient bill) {
        this.bill = bill;
    }

    public NIXMonthlyBillPdf(NIXMonthlyBillByClient bill, NIXMonthlyBillSummaryByClient billSummary) {
        this.bill = bill;
        this.billSummary = billSummary;
    }


    public NIXMonthlyBillByClient getBill() {
        return this.bill;
    }

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("logo", "../../images/common/BTCL-small-Logo.png");
        params.put("footerLeft", "Powered By Reve Systems");
        params.put("footerRight", "Bangladesh Telecommunications Company Limited");
        params.put("last_portion", "../../images/common/last_portion.png");
        KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair = ClientAPI.getInstance()
                .getPairOfClientDetailsAndClientContactDetails(
                        billSummary.getClientId(), ModuleConstants.Module_ID_NIX, ClientContactDetailsDTO.BILLING_CONTACT
                );
        ClientContactDetailsDTO contactDetailsDTO = pair.value;

        Map<String, String> map = ServiceDAOFactory.getService(NIXClientService.class).getClientDetailsByClient(billSummary.getClientId());
        params.put("customerName", contactDetailsDTO.getRegistrantsName() + " " + contactDetailsDTO.getRegistrantsLastName());
        params.put("customerAddress", contactDetailsDTO.getAddress());
        params.put("customerType", map.get("clientType") + " (" + map.get("registrantType") + ")");
        params.put("customerCategory", map.get("registrantCategory"));
        params.put("customerID", billSummary.getClientId() + "");

        params.put("invoiceID", billSummary.getId() + "");
        params.put("billMonth", getBillTimePeriod(billSummary.getMonth(), billSummary.getYear()));
        params.put("billIssueDate", "23-01-2019");
        params.put("billLastPaymentDate", "22-02-2019");

        params.put("subTotalAmount", billSummary.getGrandTotal() + " Tk");
        params.put("adjustableAmount", billSummary.getAdjustmentAmount() + " Tk");
        params.put("payableAmount", (billSummary.getTotalPayable()) + " Tk");
        params.put("VAT", billSummary.getVAT() + " Tk");
        params.put("netPayable", billSummary.getNetPayable() + " Tk");

        params.put("amountInWords", EnglishNumberToWords.convert((long) Math.ceil(billSummary.getNetPayable())) + " Tk Only");

        List<FeeByPortTypeForClient> ports = bill.getFeeByPortTypeForClients();

        params.put("FE_PORT_COUNT", "N/A");
        params.put("FE_PORT_CHARGE", "N/A");
        params.put("GE_PORT_COUNT", "N/A");
        params.put("GE_PORT_CHARGE", "N/A");
        params.put("10GE_PORT_COUNT", "N/A");
        params.put("10GE_PORT_CHARGE", "N/A");

        for (int i = 0; i < ports.size(); i++) {
            String type = ports.get(i).getPortType().toUpperCase();
            if (type.equalsIgnoreCase("fe")) {
                params.put("FE_PORT_COUNT", Integer.toString(ports.get(i).getPortCount()));
                params.put("FE_PORT_CHARGE", Double.toString(ports.get(i).getPortCost()));
            } else if (type.equalsIgnoreCase("ge")) {

                params.put("GE_PORT_COUNT", Integer.toString(ports.get(i).getPortCount()));
                params.put("GE_PORT_CHARGE", Double.toString(ports.get(i).getPortCost()));
            } else if (type.equalsIgnoreCase("10ge")) {
                params.put("10GE_PORT_COUNT", Integer.toString(ports.get(i).getPortCount()));
                params.put("10GE_PORT_CHARGE", Double.toString(ports.get(i).getPortCost()));
            }
        }


        List<KeyValuePair<String, String>> particularList = new ArrayList<>();

        for (int i = 0; i < billSummary.getNixMonthlyBillSummaryByItems().size(); i++) {
            String key = billSummary.getNixMonthlyBillSummaryByItems().get(i).getBillType();
            String value = Double.toString(billSummary.getNixMonthlyBillSummaryByItems().get(i).getGrandCost());
            particularList.add(new KeyValuePair<>(key, value));
        }
        params.put("particulars", particularList);
        return params;
    }

    @Override
    public String getResourceFile() {
        return BillConstants.NIX_MONTHLY_BILL_TEMPLATE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        String proposedFileName = "monthly-bill-" + this.bill.getClientId() + "-" + Month.getMonthNameById(this.bill.getMonth()) + ".pdf";

        StringBuilder sb = new StringBuilder();
        sb.append(FileTypeConstants.BASE_PATH);
        sb.append(FileTypeConstants.NIX_BILL_DIRECTORY);
        sb.append(File.separatorChar);
        sb.append(File.separatorChar);

        File file = FileAPI.getInstance().createDirectory(sb.toString());
        return file.getPath() + File.separatorChar + proposedFileName;
    }

    private static String getBillTimePeriod(int month, int year) {
        String ans = "";
        switch (month) {
            case 0:
                ans += "January";
                break;
            case 1:
                ans += "February";
                break;
            case 2:
                ans += "March";
                break;
            case 3:
                ans += "April";
                break;
            case 4:
                ans += "May";
                break;
            case 5:
                ans += "June";
                break;
            case 6:
                ans += "July";
                break;
            case 7:
                ans += "August";
                break;
            case 8:
                ans += "September";
                break;
            case 9:
                ans += "October";
                break;
            case 10:
                ans += "November";
                break;
            case 11:
                ans += "December";
                break;
            default:
                ans += "";
                break;

        }
        return ans += " " + year;
    }
}
