package lli.monthlyBill;

import api.ClientAPI;
import api.FileAPI;
import common.ModuleConstants;
import common.Month;
import common.bill.BillConstants;
import common.pdf.PdfMaterial;
import file.FileTypeConstants;
import lli.LLIClientService;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryByClient;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryByItem;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryType;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import util.EnglishNumberToWords;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import util.TimeConverter;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LLIMonthlyBillPdf implements PdfMaterial {
	
//	Logger logger = Logger.getLogger(getClass());
	LLIMonthlyBillSummaryByClient billSummary;
	
	public LLIMonthlyBillPdf(LLIMonthlyBillSummaryByClient bill ) {
		this.billSummary = bill;
	} 
	
	public LLIMonthlyBillSummaryByClient getBillSummary() {
		return billSummary;
	}
	
	@Override
	public Map<String, Object> getPdfParameters() throws Exception {
//		logger.info("parameter collection");
		Map<String, Object>params = new HashMap<>();
		params.put("logo", "../../images/common/BTCL-small-Logo.png");
		params.put("footerLeft", "Powered By Reve Systems");
		params.put("footerRight", "Bangladesh Telecommunications Company Limited");
		params.put("last_portion", "../../images/common/last_portion.png");
//		params.put("last_portion", "C:/Users/Acer/Desktop/last_portion.png");
		KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair = ClientAPI.getInstance()
			.getPairOfClientDetailsAndClientContactDetails(
					billSummary.getClientID(),ModuleConstants.Module_ID_LLI, ClientContactDetailsDTO.BILLING_CONTACT
					);
		ClientContactDetailsDTO contactDetailsDTO = pair.value;
		
		
		Map<String, String> map = ServiceDAOFactory.getService(LLIClientService.class).getClientDetailsByClient(billSummary.getClientID());
		params.put( "customerName", contactDetailsDTO.getRegistrantsName() + " " + contactDetailsDTO.getRegistrantsLastName());
		params.put( "customerAddress", contactDetailsDTO.getAddress() );
		params.put( "customerType", map.get("clientType") + " (" + map.get("registrantType") +")");
		params.put( "customerCategory", map.get("registrantCategory"));
		params.put( "customerID", billSummary.getClientID() + "" );
		
		params.put("invoiceID", billSummary.getID()+"");
		params.put("billMonth", getBillTimePeriod(billSummary.getMonth(), billSummary.getYear()));
		params.put("billIssueDate", TimeConverter.getDateTimeStringByMillisecAndDateFormat(billSummary.getGenerationTime(), "dd/MM/yyyy"));
		params.put("billLastPaymentDate", TimeConverter.getDateTimeStringByMillisecAndDateFormat(billSummary.getLastPaymentDate(), "dd/MM/yyyy"));
		params.put("totalBW", billSummary.getTotalMbpsBreakDown().getValue() + " Mbps");
		params.put("billingRange", "(" +
							billSummary.getBillingRangeBreakDown().getFromValue() + "-" + billSummary.getBillingRangeBreakDown().getToValue() + ") Mbps");
		params.put("mrcPerBW", billSummary.getBillingRangeBreakDown().getRate() + " Tk");
		params.put("LTBW", billSummary.getLongTermContractBreakDown().getValue() ==0? "":billSummary.getLongTermContractBreakDown().getValue()+ " Mbps");
		params.put("mrcPerLTBW", billSummary.getLongTermContractBreakDown().getRate() ==0?"":billSummary.getLongTermContractBreakDown().getRate() + " Tk");
		params.put("cacheBW", billSummary.getTotalMbpsBreakDownForCache().getValue()==0?"":billSummary.getTotalMbpsBreakDownForCache().getValue() + " Mbps");
		params.put("mrcPerCacheBW", billSummary.getBillingRangeBreakDownForCache().getRate()==0?"":billSummary.getBillingRangeBreakDownForCache().getRate() + " Tk");
		params.put("subTotalAmount", billSummary.getGrandTotal()+ " Tk");
		params.put("adjustableAmount", billSummary.getAdjustmentAmount() + " Tk");
		params.put("payableAmount", (billSummary.getTotalPayable()) + " Tk");
		params.put("VAT", billSummary.getVAT()+" Tk");
		params.put("netPayable", billSummary.getNetPayable() + " Tk");
		//params.put( "bills", billSummary.getLliMonthlyBillSummaryByItems() );
		params.put("amountInWords", EnglishNumberToWords.convert((long)Math.ceil(billSummary.getNetPayable())) + " Tk Only");
		List<LLIMonthlyBillSummaryByItem>list = billSummary.getLliMonthlyBillSummaryByItems();
		list.sort((o1, o2)->Integer.compare(o1.getType(), o2.getType()));
		
		double sum = list.stream()
			.filter(t-> t.getType() == LLIMonthlyBillSummaryType.REGULAR_ADJUSTMENT  	
						|| t.getType() == LLIMonthlyBillSummaryType.CACHE_ADJUSTMENT
						|| t.getType() == LLIMonthlyBillSummaryType.LOCAL_LOOP_ADJUSTMENT
			).mapToDouble(LLIMonthlyBillSummaryByItem::getTotalCost).sum();
		params.put("previousMonthAdjustment", sum==0?"-":String.format("%.2f Tk", sum));
		List<KeyValuePair<String, String>> particularList = new ArrayList<>();
		
		list.forEach(t->{
			if(t.getType() == LLIMonthlyBillSummaryType.REGULAR) {
				params.put("regular",  String.format("%.2f Tk", t.getTotalCost()));
			}else if(t.getType() == LLIMonthlyBillSummaryType.CACHE) {
				params.put("cache", t.getGrandCost() == 0 ? "-" : String.format("%.2f Tk", t.getTotalCost()));
			}else if(t.getType() == LLIMonthlyBillSummaryType.LOCAL_LOOP) {
				params.put("local_loop", String.format("%.2f Tk", t.getTotalCost()));
			}else if(t.getType() == LLIMonthlyBillSummaryType.DEMANDNOTE_ADJUSTMENT) {
				params.put("demand_note", t.getGrandCost() == 0 ? "-" : String.format("%.2f Tk", t.getTotalCost()));
			}
		});
		String regular = (String)params.get("regular");
		String cache = (String)params.get("cache");
		String local_loop = (String)params.get("local_loop");
		String adjustment = (String)params.get("previousMonthAdjustment");
		String demandNoteAdjustment = (String)params.get("demand_note");
		
		if(regular != null)
			particularList.add(new KeyValuePair<String, String>("Bandwidth Charge", regular));
		
		if(cache != null && cache.charAt(0)!='-')
			particularList.add(new KeyValuePair<String, String>("Transmission BW for Cache Charge", cache));
		
		if(local_loop != null)
			particularList.add(new KeyValuePair<String, String>("Local Loop Charge", local_loop));
		
		if(adjustment != null && adjustment.charAt(0)!='-')
			particularList.add(new KeyValuePair<String, String>("Adjustment", adjustment));
		
		if(demandNoteAdjustment != null && demandNoteAdjustment.charAt(0)!='-')
			particularList.add(new KeyValuePair<String, String>("Other Charges (Demand Note Transfer)", demandNoteAdjustment));
		
		params.put("particulars", particularList);
		return params;
	}

	@Override
	public String getResourceFile() {
		return BillConstants.LLI_MONTHLY_BILL_TEMPLATE;
	}

	@Override
	public JRDataSource getJasperDataSource() {
		return new JREmptyDataSource();
	}

	@Override
	public String getOutputFilePath() throws Exception {
		String proposedFileName = "monthly-bill-" + this.billSummary.getClientID() + "-" + Month.getMonthNameById(this.billSummary.getMonth()) + ".pdf";
		
		StringBuilder sb = new StringBuilder();
		sb.append( FileTypeConstants.BASE_PATH );
		sb.append( FileTypeConstants.LLI_BILL_DIRECTORY );
		sb.append( TimeConverter.getYear(this.billSummary.getGenerationTime()) );
		sb.append(File.separatorChar);
		sb.append(TimeConverter.getMonth(this.billSummary.getGenerationTime()) );
		sb.append(File.separatorChar);
		
		File file = FileAPI.getInstance().createDirectory(sb.toString());
		return file.getPath() + File.separatorChar + proposedFileName;
	}
	
	private static String getBillTimePeriod(int month, int year) {
		String ans = "";
		switch(month) {
			case 0: ans +="January";break;
			case 1: ans +="February";break;
			case 2: ans +="March";break;
			case 3: ans +="April";break;
			case 4: ans +="May";break;
			case 5: ans +="June";break;
			case 6: ans +="July";break;
			case 7: ans +="August";break;
			case 8: ans +="September";break;
			case 9: ans +="October";break;
			case 10: ans +="November";break;
			case 11: ans +="December";break;
			default: ans +="";break;
			
		}
		return ans += " "+year;
	}
}
