package vpn.monthlyBill;

import api.ClientAPI;
import api.FileAPI;
import common.ModuleConstants;
import common.Month;
import common.bill.BillConstants;
import common.pdf.PdfMaterial;
import file.FileTypeConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import util.EnglishNumberToWords;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import vpn.VPNClientService;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;
import vpn.monthlyBillSummary.VPNMonthlyBillSummaryByClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data

public class VPNMonthlyBillPdf implements PdfMaterial {
    VPNMonthlyBillSummaryByClient billSummary;

    public VPNMonthlyBillPdf(VPNMonthlyBillSummaryByClient bill ) {
        this.billSummary = bill;
    }

    public VPNMonthlyBillSummaryByClient getBillSummary() {
        return billSummary;
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
                        billSummary.getClientId(), ModuleConstants.Module_ID_VPN, ClientContactDetailsDTO.BILLING_CONTACT
                );
        ClientContactDetailsDTO contactDetailsDTO = pair.value;

        Map<String, String> map = ServiceDAOFactory.getService(VPNClientService.class).getClientDetailsByClient(billSummary.getClientId());
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

        List<KeyValuePair<String, String>> particularList = new ArrayList<>();

        for (int i = 0; i < billSummary.getVpnMonthlyBillSummaryByItems().size(); i++) {
            String key = billSummary.getVpnMonthlyBillSummaryByItems().get(i).getBillType();
            String value = Double.toString(billSummary.getVpnMonthlyBillSummaryByItems().get(i).getGrandCost());
            particularList.add(new KeyValuePair<>(key, value));
        }
        params.put("particulars", particularList);
        return params;
    }

    @Override
    public String getResourceFile() {
        return BillConstants.VPN_MONTHLY_BILL_TEMPLATE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        String proposedFileName = "monthly-bill-" + this.billSummary.getClientId() + "-" + Month.getMonthNameById(this.billSummary.getMonth()) + ".pdf";

        StringBuilder sb = new StringBuilder();
        sb.append(FileTypeConstants.BASE_PATH);
        sb.append(FileTypeConstants.VPN_BILL_DIRECTORY);
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
