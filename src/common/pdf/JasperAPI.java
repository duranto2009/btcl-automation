package common.pdf;

import common.UniversalDTOService;
import common.bill.BillDTO;
import configuration.MailConfiguration;
import lli.LLINotificationService;
import lli.monthlyBill.LLIMonthlyBillPdf;
import lombok.extern.log4j.Log4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.io.IOUtils;
import util.ServiceDAOFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@Log4j
public class JasperAPI {

    private static final JasperAPI jasperAPI = new JasperAPI();

    public static JasperAPI getInstance() {
        return jasperAPI;
    }

    public void createPDF(PdfMaterial pdfMaterial) throws Exception {
        String jasperFile = pdfMaterial.getResourceFile();
        Map<String, Object> parameters = pdfMaterial.getPdfParameters();
        JRDataSource dataSource = pdfMaterial.getJasperDataSource();
        String outputFilePath = pdfMaterial.getOutputFilePath();
        try {
            generatePDF(jasperFile, parameters, dataSource, outputFilePath);
            //TODO:maruf/raihan : mail / notification
            MailConfiguration mailConfiguration = ServiceDAOFactory.getService(UniversalDTOService.class).get(MailConfiguration.class);
			if(mailConfiguration.isLliInvoiceMail()){
			    notify(pdfMaterial, outputFilePath);
            }
        } catch (JRException ex) {
            log.error("Error Generating Jasper Report", ex);
        }
        log.info("--------------DONE PDF GENERATION--------------");
    }

    public void renderPDFToBrowser(PdfMaterial pdfMaterial, OutputStream os) throws Exception {
        log.info( "PDF rendering in browser starts" );
        String jasperFile = pdfMaterial.getResourceFile();
        Map<String, Object> parameters = pdfMaterial.getPdfParameters();
        JRDataSource dataSource = pdfMaterial.getJasperDataSource();
        try {
            renderPDF(jasperFile, parameters, dataSource, os);
        } catch (JRException ex) {
            log.error("Error Generating Jasper Report", ex);
        }

        log.info( "PDF rendering in browser done" );
    }

    private void notify(PdfMaterial pdfMaterial, String fileName) throws Exception {
        BillDTO billDTO = null;
        if (pdfMaterial instanceof LLIMonthlyBillPdf) {
            billDTO = (BillDTO) ((LLIMonthlyBillPdf) pdfMaterial).getBillSummary();
        } else {
            billDTO = (BillDTO) pdfMaterial;
        }
        ServiceDAOFactory.getService(LLINotificationService.class).sendMail(billDTO, fileName);
        log.info("Done for Client + " + billDTO.getClientID());
    }

    private void generatePDF(String jasperFile, Map<String, Object> parameters, JRDataSource dataSource, String outputFilePath) throws JRException {
        JasperPrint jasperPrint = getJasperPrint(jasperFile, parameters, dataSource);
        exportToPDF(jasperPrint, outputFilePath);
    }

    private void renderPDF(String jasperFile, Map<String, Object> parameters, JRDataSource dataSource, OutputStream os) throws Exception {
        JasperPrint jasperPrint = getJasperPrint(jasperFile, parameters, dataSource);
        exportToStream(jasperPrint, os);
    }

    private JasperPrint getJasperPrint(String jasperFile, Map<String, Object> parameters, JRDataSource dataSource) throws JRException {
        InputStream inputStream = getPdfResourcePath(jasperFile);
        JasperReport jasperReport = getJasperReport(inputStream);
        IOUtils.closeQuietly(inputStream);
        return populateReport(jasperReport, parameters, dataSource);
    }

    private JasperReport getJasperReport(InputStream inputStream) throws JRException {
        return (JasperReport) JRLoader.loadObject(inputStream);
    }

    private InputStream getPdfResourcePath(String jasperFile) {
        InputStream inputStream = getClass().getResourceAsStream(jasperFile);
        if (inputStream == null) {
            inputStream = getClass().getClassLoader().getResourceAsStream(jasperFile);
            if (inputStream == null) {
                inputStream = JasperAPI.getInstance().getClass().getResourceAsStream(jasperFile);
                if (inputStream == null) {
                    inputStream = JasperAPI.getInstance().getClass().getClassLoader().getResourceAsStream(jasperFile);
                    if (inputStream == null) {
                        try {
                            inputStream = new FileInputStream(JasperAPI.class.getProtectionDomain().getCodeSource().getLocation().getPath() + jasperFile);
                        } catch (FileNotFoundException fnf) {
                            log.error(fnf.getMessage(), fnf);
                        }
                    }
                }
            }
        }

        return inputStream;
    }

    private JasperPrint populateReport(JasperReport jasperReport, Map<String, Object> parameters, JRDataSource jrDataSource) throws JRException {
//        JRElementsVisitor.visitReport(jasperReport, );

        return JasperFillManager.fillReport(jasperReport, parameters, jrDataSource);
    }

    private void exportToPDF(JasperPrint jasperPrint, String outputFilePath) throws JRException {
        JasperExportManager.exportReportToPdfFile(jasperPrint, outputFilePath);
    }

    private void exportToStream(JasperPrint jasperPrint, OutputStream os) throws JRException {
        JasperExportManager.exportReportToPdfStream(jasperPrint, os);
    }
}
