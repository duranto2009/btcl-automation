package client.multipleMonthBill;

import accounting.AccountType;
import accounting.AccountingEntryService;
import api.ClientAPI;
import api.FileAPI;
import client.ClientService;
import client.bill.CommonBillService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import common.MiscDocumentType;
import common.ModuleConstants;
import common.bill.BillConstants;
import common.bill.BillDTO;
import common.pdf.PdfMaterial;
import file.FileTypeConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import util.UtilService;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipleMonthBillPdf implements PdfMaterial {

    JsonObject params;

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {

        Map<String, Object> params = new HashMap<>();

//        params.put("footerLeft", "Powered By Reve Systems");
//        params.put("footerRight", "Bangladesh Telecommunications Company Limited");

        //TODO
        params.put("invoiceId", this.params.get("invoiceId").getAsLong() + "");

        params.put("letterIssueDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
        params.put("netPayable", String
                .format("%,.2f", UtilService
                        .round(this.params
                                .get("netPayable")
                                .getAsDouble(), 2)) + " Tk");


        KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair = ClientAPI.getInstance()
                .getPairOfClientDetailsAndClientContactDetails(
                        this.params.get("clientId").getAsLong(), this.params.get("moduleId").getAsInt(),
                        ClientContactDetailsDTO.BILLING_CONTACT
                );
        ClientContactDetailsDTO contactDetailsDTO = pair.value;


        Map<String, String> map = ServiceDAOFactory.getService(ClientService.class)
                .getClientDetailsByClientIdAndModuleId(this.params.get("clientId").getAsLong(), this.params.get("moduleId").getAsInt());


        params.put("customerName", contactDetailsDTO.getRegistrantsName() + " " + contactDetailsDTO.getRegistrantsLastName());
        params.put("customerAddress", contactDetailsDTO.getAddress());
        params.put("customerID", this.params.get("clientId").getAsLong() + "");
        params.put("customerType", map.get("clientType") + " (" + map.get("registrantType") + ")");
        params.put("customerCategory", map.get("registrantCategory"));
        params.put("moduleName", ModuleConstants.ModuleMap.get(this.params.get("moduleId").getAsInt()));

        params.put("serviceStartDate", new SimpleDateFormat("dd/MM/yyyy").
                format(new Date(pair.key.getActivationDate())));


        List<MultipleMonthBillRow> dues = new ArrayList<>();

        List<BillDTO> dueBills = new Gson().fromJson(this.params.get("dueBills"),
                new TypeToken<List<BillDTO>>() {
                }.getType()
        );

        double totalBTCLAmount = 0, totalVat = 0, totalPayable = 0;

        for (int i = 0; i < dueBills.size(); i++) {
            BillDTO bill = dueBills.get(i);

            MultipleMonthBillRow due = new MultipleMonthBillRow();

            due.setSerial(Integer.toString(i + 1));
            due.setBillId(Long.toString(bill.getID()));
            due.setBillIssueDate(new SimpleDateFormat("dd/MM/yyyy")
                    .format(new Date(bill.getGenerationTime())));
            due.setMonth(getMonth(bill.getMonth()));
            due.setYear(Integer.toString(bill.getYear()));

            due.setBtclAmount(Double.toString(bill.getTotalPayable()));
            totalBTCLAmount += bill.getTotalPayable();

            due.setVat(Double.toString(bill.getVAT()));
            totalVat += bill.getVAT();

            due.setNetCharge(Double.toString(bill.getNetPayable()));
            totalPayable += bill.getNetPayable();

            dues.add(due);
        }

        //final dummy row for showing totals :start
        MultipleMonthBillRow due = new MultipleMonthBillRow();

        due.setSerial("");
        due.setBillId("");
        due.setBillIssueDate("");
        due.setMonth("");
        due.setYear("Total: ");
        due.setBtclAmount(Double.toString(UtilService.round(totalBTCLAmount, 2)));
        due.setVat(Double.toString(UtilService.round(totalVat, 2)));
        due.setNetCharge(Double.toString(UtilService.round(totalPayable, 2)));

        dues.add(due);

        //end

        params.put("particulars", dues);

        params.put("logo", "../../images/common/BTCL-small-Logo.png");
        params.put("last_portion", "../../images/common/last_portion.png");

        params.put("clientId", this.params.get("clientId").getAsLong() + "");

        int type = this.params.get("type").getAsInt();

        MiscDocumentType documentType = MiscDocumentType.getMiscDocumentTypeByTypeId(type);

        if (documentType == MiscDocumentType.MULTIPLE_BILL) {
            params.put("heading", "Multiple Month Duplicate Bill for Customer ID- " + this.params.get("clientId").getAsLong() + "");
            params.put("isFinalBill", false);

            params.put("fromDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date(
                    this.params.get("fromDate").getAsLong()
            )));

            params.put("toDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date(
                    this.params.get("toDate").getAsLong()
            )));

            params.put("amountInWords", this.params.get("amountInWords").getAsString());


        } else if (documentType == MiscDocumentType.FINAL_BILL) {
            params.put("heading", "Final Bill for Customer ID- " + this.params.get("clientId").getAsLong() + "");
            params.put("isFinalBill", true);

            AccountingEntryService accountingEntryService = ServiceDAOFactory.getService(AccountingEntryService.class);

            double securityMoney = (accountingEntryService
                    .getBalanceByClientIDAndAccountID(this.params.get("clientId").getAsLong()
                            , AccountType.SECURITY.getID()));

            double netPayable = UtilService
                    .round(this.params
                            .get("netPayable")
                            .getAsDouble(), 2);

            params.put("securityDeposit", String.valueOf(securityMoney));

            params.put("finalPayable", String
                    .format("%,.2f", netPayable - securityMoney));

            params.put("amountInWords", ServiceDAOFactory
                    .getService(CommonBillService.class)
                    .convertTotalToWords(netPayable - securityMoney));

        }


        params.put("lastPaymentDate", this.params.get("lastPaymentDate").getAsString());


        return params;
    }

    @Override
    public String getResourceFile() {
        return BillConstants.MULTIPLE_BILL;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        String proposedFileName = "multiple-monthly-bill" + this.params.get("clientId").getAsLong() + "-" + ".pdf";

        StringBuilder sb = new StringBuilder();
        sb.append(FileTypeConstants.BASE_PATH);
        sb.append(FileTypeConstants.VPN_BILL_DIRECTORY);
        sb.append(File.separatorChar);
        sb.append(File.separatorChar);

        File file = FileAPI.getInstance().createDirectory(sb.toString());
        return file.getPath() + File.separatorChar + proposedFileName;
    }

    private static String getMonth(int month) {
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
        return ans;
    }
}
