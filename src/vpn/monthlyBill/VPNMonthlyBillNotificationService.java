package vpn.monthlyBill;

import common.pdf.AsyncPdfService;
import common.pdf.PdfMaterial;
import org.apache.log4j.Logger;
import vpn.monthlyBillSummary.VPNMonthlyBillSummaryByClient;

import java.util.List;

public class VPNMonthlyBillNotificationService {

    static Logger logger = Logger.getLogger(VPNMonthlyBillNotificationService.class);

    public void notifyUsers(List<VPNMonthlyBillSummaryByClient> bills) {
        try {

            for (VPNMonthlyBillSummaryByClient monthlyBill : bills) {
                //TODO -  handle after preparing VPN MB pdf
//				PdfMaterial pdfMaterial = (PdfMaterial) new LLIMonthlyBillPdf(monthlyBill);
//				AsyncPdfService.getInstance().accept(pdfMaterial);
            }

        } catch (Exception e) {
            logger.fatal("exception in notifyUsers; " + e);
        }
    }


}
