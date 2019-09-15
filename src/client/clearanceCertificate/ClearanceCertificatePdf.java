package client.clearanceCertificate;

import api.ClientAPI;
import api.FileAPI;
import client.ClientService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import common.ModuleConstants;
import common.bill.BillConstants;
import common.bill.MonthWiseDue;
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
public class ClearanceCertificatePdf implements PdfMaterial {

    JsonObject params;

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {

        Map<String, Object> params = new HashMap<>();

//        params.put("footerLeft", "Powered By Reve Systems");
//        params.put("footerRight", "Bangladesh Telecommunications Company Limited");

        params.put("letterIssueDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
        params.put("netPayable", String
                .format("%,.2f", UtilService
                        .round(this.params
                                .get("totalDue")
                                .getAsDouble(), 2))+"");
        params.put("amountInWords", this.params.get("amountInWords").getAsString());


        KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair = ClientAPI.getInstance()
                .getPairOfClientDetailsAndClientContactDetails(
                        this.params.get("clientId").getAsLong(), this.params.get("moduleId").getAsInt(),
                        ClientContactDetailsDTO.BILLING_CONTACT
                );
        ClientContactDetailsDTO contactDetailsDTO = pair.value;


        Map<String, String> map = ServiceDAOFactory.getService(ClientService.class)
                .getClientDetailsByClientIdAndModuleId(this.params.get("clientId").getAsLong(),  this.params.get("moduleId").getAsInt());


        params.put("customerName", contactDetailsDTO.getRegistrantsName() + " " + contactDetailsDTO.getRegistrantsLastName());
        params.put("customerAddress", contactDetailsDTO.getAddress());
        params.put("customerID", this.params.get("clientId").getAsLong() + "");
        params.put("customerType", map.get("clientType") + " (" + map.get("registrantType") + ")");
        params.put("customerCategory", map.get("registrantCategory"));
        params.put("moduleName", ModuleConstants.ModuleMap.get(this.params.get("moduleId").getAsInt()));

        params.put("serviceStartDate", new SimpleDateFormat("dd/MM/yyyy").
                format(new Date(pair.key.getActivationDate())));


        List<MonthWiseDue> dues = new ArrayList<>();

        List<KeyValuePair<Integer, KeyValuePair<Integer, Double>>> dueBills = new Gson().fromJson(this.params.get("dueBills"),
                new TypeToken<List<KeyValuePair<Integer, KeyValuePair<Integer, Double>>>>() {
                }.getType()
        );

        for (int i = 0; i < dueBills.size(); i++) {
            MonthWiseDue due = new MonthWiseDue();
            due.setSerial(Integer.toString(i + 1));
            due.setMonth(getMonth(dueBills.get(i).getValue().getKey()));
            due.setYear(Integer.toString(dueBills.get(i).getKey()));
            due.setDue((String
                            .format("%,.2f", UtilService
                                    .round(dueBills
                                            .get(i)
                                            .getValue()
                                            .getValue(), 2)
                            )
                    )
            );

            dues.add(due);
        }

        MonthWiseDue due = new MonthWiseDue();

        due.setSerial("");
        due.setMonth("");
        due.setYear("Total: ");
        due.setDue(String.format("%,.2f",UtilService
                .round(this.params
                        .get("totalDue")
                        .getAsDouble(), 2)));

        dues.add(due);


        params.put("particulars", dues);

        params.put("logo", "../../images/common/BTCL-small-Logo.png");
        params.put("last_portion", "../../images/common/last_portion.png");

        params.put("clientId", this.params.get("clientId").getAsLong() + "");

        params.put("letterNumber", this.params.get("letterNumber").getAsString());

        params.put("fromDate",new SimpleDateFormat("dd/MM/yyyy")
                .format(new Date(this.params.get("fromDateMillis").getAsLong())));

        params.put("toDate",new SimpleDateFormat("dd/MM/yyyy")
                .format(new Date(this.params.get("toDateMillis").getAsLong())));

        return params;
    }

    @Override
    public String getResourceFile() {
        return BillConstants.CLEARANCE_CERTIFICATE_WITH_DUE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        String proposedFileName = "clearance-certificate" + this.params.get("clientId").getAsLong() + "-" + ".pdf";

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
