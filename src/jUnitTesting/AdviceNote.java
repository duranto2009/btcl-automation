package jUnitTesting;

import ip.IPConstants;
import ip.IPService;
import ip.ipUsage.IPBlockUsage;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRBean;
import lli.Application.EFR.EFRService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopService;
import lli.Application.LocalLoop.LocalLoopStringProjection;
import lli.connection.LLIConnectionConstants;
import common.pdf.PdfMaterial;
import lombok.extern.log4j.Log4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import util.ServiceDAOFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j
public class AdviceNote implements PdfMaterial {
    @Override
    public Map<String, Object> getPdfParameters() throws Exception {
        Map<String, Object> map = new HashMap<>();
        populate_letter_info(map);
        populate_client_info(map);
        populate_advice_note_info(map);
        populate_sub_reports(map);

        return map;
    }

    private void populate_advice_note_info(Map<String, Object> map) throws Exception {
        map.put("app_id", String.valueOf("1234"));
        map.put("app_type", String.valueOf("New Connection"));


        map.put("dn_id", String.valueOf("211212"));
        map.put("dn_generation_date", String.valueOf("12/12/2019"));
        map.put("dn_amount", String.valueOf("5101010"));
        map.put("dn_status", String.valueOf("PAID"));


        map.put("payment_id", String.valueOf("211212"));
        map.put("payment_date", String.valueOf("12/12/2019"));
        map.put("payment_amount", String.valueOf("5101010"));
        map.put("payment_type", String.valueOf("Bank/Online/Teletalk"));
        map.put("payment_bank", String.valueOf("SIBL"));
        map.put("payment_branch", String.valueOf("Dhaka"));


        map.put("connection_name", String.valueOf("Connection 1"));
        map.put("connection_address", String.valueOf("DCC Kha-56, Gulshan-2, Dhaka-1213"));
        map.put("connection_activation_date", String.valueOf("23/12/2018"));

        List<LocalLoop> listOfLocalLoop = ServiceDAOFactory.getService(LocalLoopService.class).getLocalLoop(209001);
        listOfLocalLoop.forEach(log::info);
        List<LocalLoopStringProjection> listOfLocalLoopStringProjection = listOfLocalLoop.stream().map(LocalLoopService::getLocalLoopProjection).collect(Collectors.toList());
        map.put("connection_1_local_loops", listOfLocalLoopStringProjection);

        map.put("connection_2_name", String.valueOf("Connection 1"));
        map.put("connection_2_address", String.valueOf("DCC Kha-56, Gulshan-2, Dhaka-1213"));
        map.put("connection_2_activation_date", String.valueOf("23/12/2018"));

        List<LocalLoop> listOfLocalLoop2 = ServiceDAOFactory.getService(LocalLoopService.class).getLocalLoop(209001);
        listOfLocalLoop.forEach(log::info);
        List<LocalLoopStringProjection> listOfLocalLoopStringProjection2 = listOfLocalLoop2.stream().map(LocalLoopService::getLocalLoopProjection).collect(Collectors.toList());
        map.put("connection_2_local_loops", listOfLocalLoopStringProjection2);

//        List<IPBlockUsage> blockUsagesMandatory = ServiceDAOFactory.getService(IPService.class)
//                .getIPBlockUsageByConnectionIdAndUsageType(110001L, LLIConnectionConstants.IPUsageType.MANDATORY);
//        blockUsagesMandatory.forEach(log::info);


        List<IPBlockUsage> blockUsagesAdditional = ServiceDAOFactory.getService(IPService.class)
                .getIPBlockUsageByConnectionIdAndUsageType(110001L, LLIConnectionConstants.IPUsageType.ADDITIONAL, IPConstants.Purpose.LLI_CONNECTION);
        blockUsagesAdditional.forEach(log::info);

//        List<IPBlockLLI> ip_list = Stream.concat(
//            blockUsagesMandatory.stream()
//                    .map(t->new IPBlockLLI(t.getFromIP(), t.getToIP(), LLIConnectionConstants.IPUsageType.MANDATORY.name()))
//                ,
//            blockUsagesAdditional.stream()
//                    .map(t->new IPBlockLLI(t.getFromIP(), t.getToIP(), LLIConnectionConstants.IPUsageType.ADDITIONAL.name()))
//        ).collect(Collectors.toList());
//
//        map.put("ip_list", ip_list);
//        map.put("mandatory_ip_count", String.valueOf(blockUsagesMandatory.size()));
//        map.put("additional_ip_count", String.valueOf(blockUsagesAdditional.size()));


        List<EFRBean>workOrderList  = ServiceDAOFactory.getService(EFRService.class).getSelected(197001)
                .stream()
                .map(t->new EFRBean(
                        t.getSource(),
                        EFR.TERMINAL.get(t.getSourceType()),
                        t.getDestination(),
                        EFR.TERMINAL.get(t.getDestinationType()),
                        t.getProposedLoopDistance(),
                        t.getActualLoopDistance()
                    )
                ).collect(Collectors.toList());
            workOrderList.forEach(log::info);
            map.put("work_order_list", workOrderList);

    }

    private void populate_client_info(Map<String, Object> map) {
        map.put("client_full_name", String.valueOf("M.M, Monirul Islam"));
        map.put("client_user_name", String.valueOf("monirul87"));
        map.put("client_mobile", String.valueOf("01811456089"));
        map.put("client_billing_address", String.valueOf("18, Kawran Bazar C/A Dhaka-1215 12312122121 raihan "));
        map.put("client_type", String.valueOf("Private"));
        map.put("client_category", String.valueOf("ISP"));
        map.put("client_isp_license_type", String.valueOf("ISP national"));
    }

    private void populate_sub_reports(Map<String, Object> map) throws JRException {
        JasperReport sub1 = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("lli/sub1.jasper"));
        JasperReport sub2_connection_1 = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("lli/sub2-connection-1.jasper"));
        JasperReport sub2_connection_2 = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("lli/sub2-connection-2.jasper"));
        JasperReport sub3_ip = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("lli/sub3-ip.jasper"));
        JasperReport sub4_work_order = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("lli/sub4-work-order.jasper"));

        map.put("sub1", sub1);
        map.put("sub2_connection_1", sub2_connection_1);
        map.put("sub2_connection_2", sub2_connection_2);
        map.put("sub3_ip", sub3_ip);
        map.put("sub4_work_order", sub4_work_order);


        map.put("print_sub2_connection_2", true);
    }

    private void populate_letter_info(Map<String, Object> map) {
        map.put("an_number", String.valueOf(1234123));
        map.put("an_status", String.valueOf("Generated"));
        map.put("an_date", String.valueOf("12/12/2018"));
        map.put("an_title", String.valueOf("Lease Line Internet Advice Note"));
        map.put("list", Arrays.asList(
                "1. Mostafa Khalid Raihan, Assitant Manager, LLI Department",
                "2.Md. Forhad Hossain Methun, Assistant Manager, VPN Department",
                "2.Md. Forhad Hossain Methun, Assistant Manager, VPN Department"

        ));
        map.put("an_to", "Server Room");
        map.put("an_sender_name", "Basir Ul Leon");
        map.put("an_sender_designation", "BTCL Team, Reve Systems");
        map.put("an_letter_body", "Threfore, you are requested to allocate/transfer/extend and/or"
                +" take measure and back the report accordingly."
                +" take measure and back the report accordingly."
                +" take measure and back the report accordingly."
                +" take measure and back the report accordingly."
                +" take measure and back the report accordingly."


        );
    }

    @Override
    public String getResourceFile() throws Exception {
        return "lli/advice-note.jasper";
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        return null;
    }
}
