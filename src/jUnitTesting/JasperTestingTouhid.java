package jUnitTesting;

import lli.Application.WorkOrderDocument;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;

public class JasperTestingTouhid {

    public static void main(String[] args) throws Exception {
        work_order_testing();
    }

    public static void work_order_testing() throws Exception {
        InputStream resourceContent = JasperTestingTouhid.class.getClassLoader().getResourceAsStream("lli/bill/lli_work_order.jasper");
        /*OutputStream output = new FileOutputStream(new File("C:/Users/HP/Desktop/lli_work_order.pdf"));
        NIXWorkOrderDocument workOrderDocument = new NIXWorkOrderDocument();
        workOrderDocument.setRecipientName("Hasib");
        workOrderDocument.setRecipientAddress("Motijheel");
        workOrderDocument.setClientAddress("Basundhara");
        workOrderDocument.setClientName("Maruf");
        workOrderDocument.setBandwidth(50);
        workOrderDocument.setSenderName("Touhid");
        workOrderDocument.setSenderAddress("Banani");
        workOrderDocument.setCcList(Arrays.asList("ts@gmail.com", "th@gmail.com"));
        writePdfToStream(resourceContent, output, workOrderDocument);*/
        System.out.println("Done");
    }

    private static void writePdfToStream(InputStream resourceContent, OutputStream outputStream, WorkOrderDocument workOrderDocument) throws Exception {
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(resourceContent);
        Map<String, Object> params = getPdfParamMap(workOrderDocument);
        JRDataSource itemsJRBean = new JREmptyDataSource();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, itemsJRBean);
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }

    private static Map<String, Object> getPdfParamMap(WorkOrderDocument workOrderDocument) throws Exception {
        Map<String, Object> params = workOrderDocument.getPdfParameters();
        return params;
    }
}