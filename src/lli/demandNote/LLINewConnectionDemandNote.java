package lli.demandNote;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import annotation.AccountingLogic;
import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import common.RequestFailureException;
import common.bill.BillConstants;
import common.bill.BillDTO;
import config.GlobalConfigConstants;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.connection.LLIConnectionConstants;
import common.pdf.PdfMaterial;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import util.JsonUtils;
import util.ServiceDAOFactory;
import validator.annotation.NonNegative;

@AccountingLogic(LLINewConnectionDemandNoteBusinessLogic.class)
@TableName("at_lli_new_connection_dn")
@ForeignKeyName("lli_nc_dn_parent_bill_id")
public class LLINewConnectionDemandNote extends BillDTO implements PdfMaterial{
//	Logger logger = Logger.getLogger(getClass());
	public LLINewConnectionDemandNote() {
		setClassName(getClass().getCanonicalName());
		setBillType(BillConstants.DEMAND_NOTE);
		setEntityTypeID(LLIConnectionConstants.ENTITY_TYPE);
	}
	
	@ColumnName("lli_nc_dn_id")
	@PrimaryKey
	long newConnectionDemandNoteID;
	
	@ColumnName("lli_nc_dn_sc_money")
	@NonNegative("security money")
	double securityMoney;
	
	@ColumnName("lli_nc_dn_reg_fee")
	@NonNegative("registration charge")
	double registrationFee;
	
	@ColumnName("lli_nc_dn_bw_mrc")
	@NonNegative("bandwidth MRC")
	double bwMRC;
	List<ItemCost> itemCosts;
	
	@ColumnName("lli_nc_dn_item_cost_content")
	String itemCostContent;
	
	@ColumnName("lli_nc_dn_fibre_otc")
	@NonNegative("Fiber Charge")
	double fibreOTC;
	
	@ColumnName("lli_nc_dn_local_loop_charge")
	@NonNegative("Local Loop Charge")
	double localLoopCharge;
	
	@ColumnName("lli_nc_dn_advance_adjustment")
	@NonNegative("Advance Adjustment")
	double advanceAdjustment;

	
	public long getNewConnectionDemandNoteID() {
		return newConnectionDemandNoteID;
	}
	public void setNewConnectionDemandNoteID(long newConnectionDemandNoteID) {
		this.newConnectionDemandNoteID = newConnectionDemandNoteID;
	}
	public double getSecurityMoney() {
		return securityMoney;///1.15;
	}
	public void setSecurityMoney(double securityMoney) {
		this.securityMoney = securityMoney;
	}
	public double getRegistrationFee() {
		return registrationFee;
	}
	public void setRegistrationFee(double registrationFee) {
		this.registrationFee = registrationFee;
	}
	public double getBwMRC() {
		return bwMRC;///1.15;
	}
	public void setBwMRC(double bwMRC) {
		this.bwMRC = bwMRC;
	}
	public String getItemCostContent() {
		return itemCostContent;
	}
	public void setItemCostContent(String itemCostContent) {
		this.itemCostContent = itemCostContent;
	}
	public double getFibreOTC() {
		return fibreOTC;
	}
	public void setFibreOTC(double fibreOTC) {
		this.fibreOTC = fibreOTC;
	}
	public double getLocalLoopCharge() {
		return localLoopCharge;
	}
	public void setLocalLoopCharge(double localLoopCharge) {
		this.localLoopCharge = localLoopCharge;
	}
	public double getAdvanceAdjustment() {
		return advanceAdjustment;///1.15;
	}
	public void setAdvanceAdjustment(double advanceAdjustment) {
		this.advanceAdjustment = advanceAdjustment;
	}

	
	public double getTotalItemCost(){
		double totalItemCost = 0;
		for(ItemCost itemCost: getItemCosts()){
			totalItemCost+=itemCost.cost;
		}
		return totalItemCost;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(advanceAdjustment);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(bwMRC);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		
		temp = Double.doubleToLongBits(fibreOTC);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((itemCostContent == null) ? 0 : itemCostContent.hashCode());
		temp = Double.doubleToLongBits(localLoopCharge);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (newConnectionDemandNoteID ^ (newConnectionDemandNoteID >>> 32));
		temp = Double.doubleToLongBits(registrationFee);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(securityMoney);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LLINewConnectionDemandNote other = (LLINewConnectionDemandNote) obj;
		if (Double.doubleToLongBits(advanceAdjustment) != Double.doubleToLongBits(other.advanceAdjustment))
			return false;
		if (Double.doubleToLongBits(bwMRC) != Double.doubleToLongBits(other.bwMRC))
			return false;
		if (Double.doubleToLongBits(fibreOTC) != Double.doubleToLongBits(other.fibreOTC))
			return false;
		if (itemCostContent == null) {
			if (other.itemCostContent != null)
				return false;
		} else if (!itemCostContent.equals(other.itemCostContent))
			return false;
		if (Double.doubleToLongBits(localLoopCharge) != Double.doubleToLongBits(other.localLoopCharge))
			return false;
		if (newConnectionDemandNoteID != other.newConnectionDemandNoteID)
			return false;
		if (Double.doubleToLongBits(registrationFee) != Double.doubleToLongBits(other.registrationFee))
			return false;
		if (Double.doubleToLongBits(securityMoney) != Double.doubleToLongBits(other.securityMoney))
			return false;
		return true;
	}
	public List<ItemCost> getItemCosts() {
		
		if(itemCosts==null){
			itemCosts = JsonUtils.getObjectListByJsonString(this.itemCostContent, ItemCost.class);
		}
		
		return itemCosts;
	}
	public void setItemCosts(List<ItemCost> itemCosts) {
		this.itemCosts = itemCosts;
	}

	@Override
	public double getVAT(){
		return super.getVAT();//+(this.bwMRC+this.transferCharge+this.securityMoney)*.15/1.15;
	}
	@Override
	public Map<String, Object> getPdfParameters() throws Exception {
//		logger.info("Parameter collection");
		LLIApplication lliApplication =  ServiceDAOFactory.getService(LLIApplicationService.class).getNewFlowLLIApplicationByDemandNoteID(this.getID());
		if(lliApplication == null) {
			throw new RequestFailureException("No LLI Application found with demand note id " + this.getID());
		}
		Map<String, Object> params = new HashMap<>();


		LLIDemandNoteService lliDemandNoteService =
				ServiceDAOFactory.getService(LLIDemandNoteService.class);

		lliDemandNoteService.populateFooterInfoForPDF(params);
		lliDemandNoteService.populateClientInfoForPDF(params, super.getClientID());
		lliDemandNoteService.populateBillInfoForPDF(params, this);
		
		
		params.put("securityMoney", String.format("%.2f", this.getSecurityMoney()));
		params.put("registrationCharge", String.format("%.2f", this.getRegistrationFee()));
		params.put("bwMRC", String.format("%.2f", this.getBwMRC()));

		params.put("instantDegradationCharge", "0.00");
		String otherItems = "";
		String otherItemsCharge = "";
		List<ItemCost> itemCosts = this.getItemCosts();
		for(ItemCost itemCost : itemCosts) {
			otherItems += itemCost.item + "(other)<br>";
			otherItemsCharge += String.format("%.2f", itemCost.cost) + "<br>";
		}
		params.put("otherItems", otherItems);
		params.put("otherCharge", otherItemsCharge);
		
		params.put("advancedAmount", String.format("%.2f", this.getAdvanceAdjustment()));
		params.put("localLoopCharge", String.format("%.2f", this.getLocalLoopCharge()));
		params.put("fibreCharge", String.format("%.2f", this.getFibreOTC()));


		params.put("bwMRCMinusDiscount", String.format("%.2f", this.getBwMRC() - this.getDiscount()));
		double subTotal = this.getTotalPayable();
		params.put("vatCalculableWOSecurity", String.format("%.2f",
				subTotal - this.getSecurityMoney()
		));


		params.put("subTotal", String.format("%.2f", subTotal));
		params.put("vatPercentage", String.format("%.2f", this.getVatPercentage()));
		params.put("discountPercentage", String.format("%.2f", this.getDiscountPercentage()));


		String context = GlobalConfigConstants.contextMap.get("context");
		if (context == null) {
			throw new RequestFailureException("Context Path Not Set");
		}
		params.put("actualLink", context + "lli/dn/get-local-loop-breakdown.do?appId="+ lliApplication.getApplicationID() );
		params.put("linkLocalLoop", "<span style='color:blue'>" + context + "lli/dn/get-local-loop-breakdown.do?appId="+ lliApplication.getApplicationID()+ "</span>");


		lliDemandNoteService.populateLLIApplicationInfoForPDF(params, lliApplication, getID());
		return params;


	}
	@Override
	public String getResourceFile() {
		return BillConstants.LLI_DEMAND_NOTE_NEW_CONNECTION_TEMPLATE;
	}
	@Override
	public JRDataSource getJasperDataSource() {
		return new JRBeanCollectionDataSource( Arrays.asList( this ), false );
	}
	@Override
	public String getOutputFilePath() throws Exception {
		String proposedFileName = "demand-note-" + super.getID() +".pdf"; 
		return ServiceDAOFactory.getService(LLIDemandNoteService.class).getFilePath(proposedFileName, super.getGenerationTime());
	}

}
