package lli.monthlyBill;

import annotation.*;
import api.ClientAPI;
import api.FileAPI;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillConstants;
import common.bill.BillDTO;
import common.pdf.PdfMaterial;
import file.FileTypeConstants;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import lli.connection.LLIConnectionConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import util.EnglishNumberToWords;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import util.TimeConverter;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.io.File;
import java.util.*;
import java.util.Map;

@TableName("lli_manual_bill")
@Data
@ForeignKeyName("bill_id")
@EqualsAndHashCode(callSuper=false)
@AccountingLogic(LLIManualBillBusinessLogic.class)
public class LLIManualBill extends BillDTO implements PdfMaterial{
	public LLIManualBill() {
		setClassName(this.getClass().getName());
		setBillType(BillConstants.MANUAL_BILL);
		setPaymentStatus(BillDTO.UNPAID);
		setEntityTypeID(LLIConnectionConstants.ENTITY_TYPE);
		setDeleted(false);
		setDiscountPercentage(0);
	}
	@PrimaryKey
	@ColumnName("id")
	long id;
	@ColumnName("connection_Id")
	Long connectionId;
	@ColumnName("total_cost")
	double totalCost;
	List<ItemForManualBill> listOfFactors = new ArrayList<>();
	public void setTotalCost() {
		Optional<ItemForManualBill> result = this.listOfFactors.stream().filter(itemCost->itemCost.cost < 0 ).findAny();
		if(result.isPresent()) {
			throw new RequestFailureException (result.get().item + " must be a positive number");
		}
		this.totalCost = this.listOfFactors.stream().mapToDouble(a->a.cost).sum();
	}
	@Override
	public Map<String, Object> getPdfParameters() throws Exception {
		Map<String, Object>params = new HashMap<>();
		params.put("logo", "../../images/common/btcl_logo_heading.png");
		params.put("footerLeft", "Powered By Reve Systems");
		params.put("footerRight", "Bangladesh Telecommunications Company Limited");
		long clientId = super.getClientID();
		
		KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO>  pair =
				ClientAPI.getInstance().getPairOfClientDetailsAndClientContactDetails(
						clientId, ModuleConstants.Module_ID_LLI, ClientContactDetailsDTO.BILLING_CONTACT);
		ClientDetailsDTO clientDetailsDTO = pair.key;
		ClientContactDetailsDTO contactDetailsDTO = pair.value;
		
		params.put( "clientFullName", contactDetailsDTO.getRegistrantsName() + " " + contactDetailsDTO.getRegistrantsLastName());
		params.put( "clientAddress", contactDetailsDTO.getAddress() );
		params.put( "clientEmail", contactDetailsDTO.getEmail() !=null ?contactDetailsDTO.getEmail():"N/A");
		params.put( "clientLoginName", clientDetailsDTO.getLoginName() );
		LLIConnectionService service = ServiceDAOFactory.getService(LLIConnectionService.class);
		if(this.getConnectionId() != null ) {
			LLIConnectionInstance connection = service.getLLIConnectionByConnectionID(this.getConnectionId());
			params.put("connectionName", connection.getName());
		}else {
			params.put("connectionName", "N/A");
		}
		
		params.put("billGenerationDate", TimeConverter.getDateTimeStringByMillisecAndDateFormat(super.getGenerationTime(), "dd/MM/yyyy"));
		params.put("billLastPaymentDate", TimeConverter.getDateTimeStringByMillisecAndDateFormat(super.getLastPaymentDate(), "dd/MM/yyyy"));
		params.put("invoiceID", super.getID()+"");
		params.put("VAT", String.format("%.2f", super.getVAT()));
		params.put("total", String.format("%.2f", super.getNetPayable()));
		params.put("demandedAmount", EnglishNumberToWords.convert((long)Math.ceil(super.getNetPayable())));
		
		String otherItems = "";
		String otherItemsCharge = "";
		for(ItemForManualBill itemCost : this.getListOfFactors()) {
			otherItems += itemCost.item + "<br>";
			otherItemsCharge += String.format("%.2f", itemCost.cost) + "<br>";
		}
		params.put("otherItems", otherItems);
		params.put("otherCharge", otherItemsCharge);
		return params;
	}
	@Override
	public String getResourceFile() {
		return BillConstants.LLI_MANUAL_BILL_TEMPLATE;
	}
	
	@Override
	public JRDataSource getJasperDataSource() {
		return new JREmptyDataSource();
	}
	
	@Override
	public String getOutputFilePath() throws Exception {
		String proposedFileName = "manual-bill-" + super.getID()+ ".pdf";
		
		StringBuilder sb = new StringBuilder();
		sb.append( FileTypeConstants.BASE_PATH );
		sb.append( FileTypeConstants.LLI_BILL_DIRECTORY );
		sb.append( TimeConverter.getYear(super.getGenerationTime()) );
		sb.append(File.separatorChar);
		sb.append(TimeConverter.getMonth(super.getGenerationTime()));
		sb.append(File.separatorChar);
		
		File file = FileAPI.getInstance().createDirectory(sb.toString());
		return file.getPath() + File.separatorChar + proposedFileName;
	}
}
