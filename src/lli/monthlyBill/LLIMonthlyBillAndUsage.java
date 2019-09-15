//package lli.monthlyBill;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import common.ModuleConstants;
//import common.Month;
//import common.RequestFailureException;
//import common.bill.BillConstants;
//import common.bill.BillDTO;
//import common.repository.AllClientRepository;
//import file.FileDTO;
//import file.FileService;
//import file.FileTypeConstants;
//import lli.connection.LLIConnectionConstants;
//import lli.monthlyUsage.LLIMonthlyUsageByClient;
//import common.pdf.PdfMaterial;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import net.sf.jasperreports.engine.JRDataSource;
//import net.sf.jasperreports.engine.JREmptyDataSource;
//import util.CurrentTimeFactory;
//import util.ServiceDAOFactory;
//import util.TimeConverter;
//import vpn.clientContactDetails.ClientContactDetailsDTO;
//import vpn.client.ClientDetailsDTO;
//import vpn.client.ClientService;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class LLIMonthlyBillAndUsage implements PdfMaterial{
//	LLIMonthlyBillByClient monthlyBill;
//	LLIMonthlyUsageByClient monthlyUsage;
//	@Override
//	public Map<String, Object> getPdfParameters() throws Exception {
//		Map<String, Object>params = new HashMap<>();
//		params.put("logo", "../../images/common/btcl_logo.jpg");
//		params.put("footerLeft", "Powered By <font color=blue>Reve Systems</font>");
//		params.put("footerRight", "Bangladesh Telecommunications Company Limited");
//		long clientId = monthlyBill.getClientId();
//		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().
//				getVpnClientByClientID( clientId, ModuleConstants.Module_ID_LLI );
//		if(clientDetailsDTO == null) {
//			throw new RequestFailureException("No Client Details Found with client id "+ clientId);
//		}
//		List<ClientContactDetailsDTO> clientContactDetailsDTOs =  ServiceDAOFactory.getService(ClientService.class)
//																		.getVpnContactDetailsListByClientID( clientDetailsDTO.getId() );
//		
//		if(clientContactDetailsDTOs == null || clientContactDetailsDTOs.isEmpty()) {
//			throw new RequestFailureException("No Client Contact Details Found with client id "+ clientId);
//		}	
//		Optional<ClientContactDetailsDTO> optional = clientContactDetailsDTOs.stream().filter(a->a.getDetailsType()==ClientContactDetailsDTO.BILLING_CONTACT).findFirst();
//		
//		if(!optional.isPresent()) {
//			throw new RequestFailureException("No Billing Address Found");
//		}
//		ClientContactDetailsDTO contactDetailsDTO = optional.get();
//		
//		
//		params.put( "clientFullName", contactDetailsDTO.getRegistrantsName() + " " + contactDetailsDTO.getRegistrantsLastName());
//		params.put( "clientAddress", contactDetailsDTO.getAddress() );
//		params.put( "clientEmail", contactDetailsDTO.getEmail() !=null ?contactDetailsDTO.getEmail():"N/A");
//		params.put( "clientLoginName", clientDetailsDTO.getLoginName() );
//		
//		params.put("billForMonth", Month.getMonthNameById(monthlyBill.getMonth()));
//		params.put("totalBW", monthlyBill.getTotalMbpsBreakDown().getValue() + " Mbps");
//		params.put("billingRangeBW", "(" +
//							monthlyBill.getBillingRangeBreakDown().getFromValue() + "-" + monthlyBill.getBillingRangeBreakDown().getToValue() + ") Mbps");
//		params.put("billingRate", monthlyBill.getBillingRangeBreakDown().getRate() + " taka");
//		params.put("longTermBW", monthlyBill.getLongTermContractBreakDown().getValue() + " Mbps");
//		params.put("longTermRate", monthlyBill.getLongTermContractBreakDown().getRate() + " taka");
//		params.put("longTermAdjustment", monthlyBill.getLongTermContructAdjustment() + " taka");
//		params.put("grandTotal", monthlyBill.getGrandTotal() + " taka");
//		params.put("discountAndPercentage", monthlyBill.getDiscount() + " taka (" + monthlyBill.getDiscountPercentage() + " %)");
//		params.put("totalPayable", monthlyBill.getTotalPayable() + " taka");
//		params.put("vatAndPercentage", monthlyBill.getVAT() + " taka (" + monthlyBill.getVatPercentage() + " %)");
//		params.put("adjustmentAmount", monthlyBill.getAdjustmentAmount() + " taka");
//		params.put("netPayable", monthlyBill.getNetPayable() + " taka");
//		params.put("bills", monthlyBill.getMonthlyBillByConnections());
//		return params;
//	}
//	@Override
//	public String getResourceFile() {
//		return BillConstants.LLI_MONTHLY_BILL_TEMPLATE;
//	}
//	@Override
//	public JRDataSource getJasperDataSource() {
//		return new JREmptyDataSource();
//	}
//	@Override
//	public String getOutputFilePath() throws Exception {
//		String proposedFileName = "monthly-bill-" + monthlyBill.getClientId() + "-" + Month.getMonthNameById(monthlyBill.getMonth()) + ".pdf";
//		return getPDFAbsolutePath(monthlyBill, proposedFileName);
//	}
//	
//	private String getPDFAbsolutePath(BillDTO billDTO, String proposedFileName) throws Exception{
//		FileDTO fileDTO = getInsertedFileDTO (billDTO, proposedFileName);
//		return fileDTO.getDocDirectoryPath() + File.separatorChar + fileDTO.getDocActualFileName();
//	}
//	private FileDTO getInsertedFileDTO(BillDTO billDTO, String proposedFileName) throws Exception {
//		FileDTO fileDTO = createFileDTO(billDTO, proposedFileName);
//		ServiceDAOFactory.getService(FileService.class).insertFile(fileDTO);
//		return fileDTO;
//	}
//	private FileDTO createFileDTO(BillDTO billDTO, String fileName) throws Exception {
//		FileDTO fileDTO = new FileDTO();
//		fileDTO.setDocOwner( billDTO.getClientID() );
//		fileDTO.setDocEntityTypeID( LLIConnectionConstants.ENTITY_TYPE);
//		fileDTO.setDocEntityID( billDTO.getEntityID() );
//		
//		fileDTO.setLastModificationTime( CurrentTimeFactory.getCurrentTime() );
//		File directory = getDirectory(billDTO);
//		File file = new File(directory.getPath()+fileName);
//     	fileDTO.setDocTypeID( FileTypeConstants.GLOBAL.BILL + "" );
//		fileDTO.setDocActualFileName( fileName );
//		fileDTO.setDocLocalFileName( fileName );
//		fileDTO.setDocSize( (int)file.length() );
//		fileDTO.setDocDirectoryPath( directory.getPath() );
//		return fileDTO;
//	}
//	
//	private File getDirectory(BillDTO billDTO) throws Exception {
//		StringBuilder sb = new StringBuilder();
//		sb.append( FileTypeConstants.BASE_PATH );
//		sb.append( FileTypeConstants.LLI_BILL_DIRECTORY );
//		sb.append( TimeConverter.getYear(billDTO.getGenerationTime()) + "/" + TimeConverter.getMonth(billDTO.getGenerationTime()) + "/" );
//		File dir = new File( sb.toString() );
//		createDirectory(dir, sb);
//		return dir;
//	}
//	
//	private void createDirectory(File dir, StringBuilder sb) throws Exception {
//		if (!dir.exists()) {
//            if (!dir.mkdirs()) {
//            	throw new Exception( "Directory for bill pdf can't be created" );
//            }
//     	}
//	}
//}
