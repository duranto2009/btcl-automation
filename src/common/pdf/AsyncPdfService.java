package common.pdf;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import lli.monthlyBill.LLIMonthlyBillPdf;
import util.CurrentTimeFactory;


public class AsyncPdfService extends Thread {
	
	private Logger log= Logger.getLogger(getClass());
	private static volatile AsyncPdfService pdfService = null;
	private static BlockingQueue<PdfMaterial>queue = null;
	
	private AsyncPdfService () {
		super("Async PDF Service");
		queue = new LinkedBlockingQueue<>();
		setDaemon(true);
		setPriority(MIN_PRIORITY);
		start();
	}
	
	///lazy instantiation is an overkill in EE! but whatever
	public static AsyncPdfService getInstance() {
		if(pdfService == null) {
			synchronized(AsyncPdfService.class) {
				if(pdfService == null) {
					pdfService = new AsyncPdfService();
				}
			}
		}
		return pdfService;
	}
	
	public void accept(PdfMaterial pdfMaterial) {
		log.info("Accepting PDF Material");
		queue.offer(pdfMaterial);
	}
	@Override
	public void run() {
		while(true) {
			PdfMaterial pdfMaterial = null;
			try {
				pdfMaterial = queue.take();
				processPdfMaterial(pdfMaterial);
				
			} catch (Exception e) {
				log.fatal(e.getMessage());
				e.printStackTrace();
				if(pdfMaterial instanceof LLIMonthlyBillPdf) {
					log.fatal("Async Pdf FAILURE for Client + " + ((LLIMonthlyBillPdf)pdfMaterial).getBillSummary().getClientID());
				}
			}
		}
	}
	
	private void processPdfMaterial(PdfMaterial pdfMaterial) throws Exception {
		CurrentTimeFactory.initializeCurrentTimeFactory();
		log.info("--------------STARTING PDF GENERATION--------------");
		createPDF(pdfMaterial);
		CurrentTimeFactory.destroyCurrentTimeFactory();

	}

	private void createPDF(PdfMaterial pdfMaterial) {
		try {
			JasperAPI.getInstance().createPDF(pdfMaterial);
		}catch(Exception e) {
			//log.fatal("Async Pdf Service Error!\n check jasper api or mail send.",e);
			e.printStackTrace();
		}
	}
}
