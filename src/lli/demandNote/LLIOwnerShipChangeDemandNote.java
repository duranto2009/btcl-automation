package lli.demandNote;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import annotation.AccountingLogic;
import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import common.RequestFailureException;
import common.bill.BillConstants;
import common.bill.BillDTO;
import lli.Application.LLIApplication;
import lli.Application.ownership.LLIOwnerChangeService;
import lli.Application.ownership.LLIOwnerShipChangeApplication;
import lli.connection.LLIConnectionConstants;
import common.pdf.PdfMaterial;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import util.ServiceDAOFactory;
import validator.annotation.NonNegative;

@AccountingLogic(LLIOwnerShipChageDemandNoteBusinessLogic.class)
@TableName("at_lli_ownership_change_dn")
@ForeignKeyName("lli_oc_dn_parent_bill_id")
public class LLIOwnerShipChangeDemandNote extends BillDTO implements PdfMaterial{
//	Logger logger = Logger.getLogger(getClass());
	
	public LLIOwnerShipChangeDemandNote() {
		setClassName(this.getClass().getCanonicalName());
		setBillType(BillConstants.DEMAND_NOTE);
		setEntityTypeID(LLIConnectionConstants.ENTITY_TYPE);
	}
	
	
	@PrimaryKey
	@ColumnName("lli_oc_dn_id")
	long ownerShipChangeDemandNoteID;
	@ColumnName("lli_oc_dn_oc_charge")
	@NonNegative("ownership charge")
	double ownerShipChangeCharge;
	@ColumnName("lli_oc_dn_other_cost")
	@NonNegative("other cost")
	double otherCosts;

	
	public long getOwnerShipChangeDemandNoteID() {
		return ownerShipChangeDemandNoteID;
	}
	public void setOwnerShipChangeDemandNoteID(long ownerShipChangeDemandNoteID) {
		this.ownerShipChangeDemandNoteID = ownerShipChangeDemandNoteID;
	}
	public double getOwnerShipChangeCharge() {
		return ownerShipChangeCharge;
	}
	public void setOwnerShipChangeCharge(double ownerShipChangeCharge) {
		this.ownerShipChangeCharge = ownerShipChangeCharge;
	}
	public double getOtherCosts() {
		return otherCosts;
	}
	public void setOtherCosts(double otherCosts) {
		this.otherCosts = otherCosts;
	}
	@Override
	public Map<String, Object> getPdfParameters() throws Exception {
//		logger.info("Parameter collection");
		Map<String, Object> params = new HashMap<>();

		LLIDemandNoteService lliDemandNoteService =ServiceDAOFactory.getService(LLIDemandNoteService.class);

		lliDemandNoteService.populateFooterInfoForPDF(params);
		lliDemandNoteService.populateClientInfoForPDF(params, super.getClientID());
		lliDemandNoteService.populateBillInfoForPDF(params, this);

		params.put("ownershipChangeCharge", String.format("%.2f", this.getOwnerShipChangeCharge()));
		params.put("otherCost", String.format("%.2f", this.getOtherCosts()));
		LLIOwnerShipChangeApplication lliApplication = ServiceDAOFactory.getService(LLIOwnerChangeService.class).getApplicationByDemandNoteId(this.getID());
		if(lliApplication == null ){
			throw new RequestFailureException(" No Ownership Change Application Found with Demand Note id: " + this.getID());
		}
		params.put("connectionAddress", "N/A");
		lliDemandNoteService.populateLLIOwnershipChangeApplicationInfoForPDF(params, lliApplication);
		return params;
	}
	@Override
	public String getResourceFile() {
		return BillConstants.LLI_DEMAND_NOTE_CHANGE_OWNERSHIP_TEMPLATE;
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
