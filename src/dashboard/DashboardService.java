package dashboard;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import common.bill.BillDTO;
import crm.CrmCommonPoolDTO;
import crm.CrmComplainDTO;
import global.GlobalService;
import lli.LLIConnectionInstance;
import lombok.extern.log4j.Log4j;
import requestMapping.Service;
import util.ModifiedSqlGenerator;
import util.TimeConverter;
import vpn.network.VPNNetworkLink;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Log4j
class DashboardService {

    @Service private GlobalService globalService;
    @Service private DashboardDataBuilder dashboardDataBuilder;

    JsonObject getCRMTicketData() throws Exception {
        globalService.getAllObjectListByCondition(
                CrmCommonPoolDTO.class, " WHERE " + ModifiedSqlGenerator.getColumnName(CrmCommonPoolDTO.class, "status")
                + " = " + CrmComplainDTO.COMPLETED
        );
                return null;
    }

    String soothingValue( double value) {
        if(String.valueOf(value).toLowerCase().contains("e")) {
            String [] ara = String.valueOf(Math.floor(value)).toLowerCase().split("e", -1);
            String secondPart = ara[1];
            int secondPartInt = Integer.valueOf(secondPart);
            if(secondPartInt >=7 ){
                int multiplier = secondPartInt - 7;
                String [] ara2 = ara[0].split("\\.", -1);
                return ( ara2[0] + ara2[1].substring(0, multiplier) + " crore+");
            }else if(secondPartInt >= 5) {
                int multiplier = secondPartInt - 5;
                String [] ara2 = ara[0].split("\\.", -1);
                return ( ara2[0] + ara2[1].substring(0, multiplier) + " lakh+");
            }
        }else {
            String firstPart = String.valueOf(Math.floor(value)).split("\\.", -1)[0];
            int length = firstPart.length();
            if(length>7){
                return ( firstPart.substring(0, length-7) + " crore+");
            }else if(length > 5) {
               return ( firstPart.substring(0, length-5) + " lakh+");
            }else {
                StringBuilder s = new StringBuilder(firstPart.charAt(0)+"");
                for(int i=0;i<length-1;i++){
                    s.append("0");
                }
                return ( s.toString() + "+");
            }
        }
        return "";

    }

    JsonObject createJson(List<JsonObject> objects, String title, String value) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", title);

        jsonObject.addProperty("value", value);

        jsonObject.addProperty("children", gson.toJson(objects));
        return jsonObject;
    }

    JsonObject createJson(List<JsonObject> objects, String title, Number value) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", title);

        jsonObject.addProperty("value", value);

        jsonObject.addProperty("children", gson.toJson(objects));
        return jsonObject;
    }

    String parseStringForBillTimeline(BillDTO billDTO) {
        return "[bold]"
                + TimeConverter.getDateTimeStringByMillisecAndDateFormat(billDTO.getLastPaymentDate(), "dd/MM/yyyy")
                + "[/]" + "\n"
                +  billDTO.getModule() + " :: " + billDTO.getBillTypeStr() + "\n"
                + "[bold]"
                + "Invoice ID: " + billDTO.getID() + ", Amount: " + billDTO.getNetPayable()
                + "[/]";
    }

    JsonObject getBWUsage(List<LLIConnectionInstance> lliList, List<VPNNetworkLink> listVPN) {
        double bwLLI = lliList.stream()
                .mapToDouble(LLIConnectionInstance::getBandwidth)
                .sum();
        double bwVPN = listVPN.stream()
                .mapToDouble(VPNNetworkLink::getLinkBandwidth)
                .sum();

        JsonObject jsonObjectLLI = new JsonObject();
        jsonObjectLLI.addProperty("category", "LLI");
        jsonObjectLLI.addProperty("value", bwLLI);


        JsonObject jsonObjectVPN = new JsonObject();
        jsonObjectVPN.addProperty("category", "VPN");
        jsonObjectVPN.addProperty("value", bwVPN);

        return createJson(Stream.of(jsonObjectLLI, jsonObjectVPN).collect(toList()), "BW Consumption", bwLLI + bwVPN);

    }

}
