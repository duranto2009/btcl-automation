package lli.monthlyBill;

import java.util.List;

import org.apache.log4j.Logger;

import lli.monthlyBillSummary.LLIMonthlyBillSummaryByClient;
import common.pdf.AsyncPdfService;
import common.pdf.PdfMaterial;

public class LLIMonthlyBillNotificationService {

	static Logger logger = Logger.getLogger( LLIMonthlyBillNotificationService.class );
	
	public void notifyUsers(List<LLIMonthlyBillSummaryByClient> bills)
	{
		try {
			
			for(LLIMonthlyBillSummaryByClient monthlyBill: bills) {
				PdfMaterial pdfMaterial = (PdfMaterial) new LLIMonthlyBillPdf(monthlyBill);
				AsyncPdfService.getInstance().accept(pdfMaterial);
			}
			
		} catch (Exception e) {
			logger.fatal("exception in notifyUsers; " + e);
		}
	}

	
}
