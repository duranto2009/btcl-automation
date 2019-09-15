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
import lli.Application.LLIApplicationService;
import lli.connection.LLIConnectionConstants;
import common.pdf.PdfMaterial;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import util.ServiceDAOFactory;
import validator.annotation.NonNegative;

@AccountingLogic(LLICloseConnectionDemandNoteBusinessLogic.class)
@TableName("at_lli_closing_dn")
@ForeignKeyName("lli_closing_dn_parent_bill_id")
public class LLICloseConnectionDemandNote extends BillDTO implements PdfMaterial{
//	Logger logger = Logger.getLogger(getClass());
	public LLICloseConnectionDemandNote() {
		setClassName(getClass().getCanonicalName());
		setBillType(BillConstants.DEMAND_NOTE);
		setEntityTypeID(LLIConnectionConstants.ENTITY_TYPE);
	}
	
	@PrimaryKey
	@ColumnName("lli_closing_dn_id")
	long closingDemandNoteID;
	@ColumnName("lli_closing_dn_otc")
	@NonNegative("closing OTC")
	double closingOTC;
	@ColumnName("lli_closing_dn_other")
	@NonNegative("other cost")
	double otherCost;

	public long getClosingDemandNoteID() {
		return closingDemandNoteID;
	}
	public void setClosingDemandNoteID(long closingDemandNoteID) {
		this.closingDemandNoteID = closingDemandNoteID;
	}
	public double getClosingOTC() {
		return closingOTC;
	}
	public void setClosingOTC(double closingOTC) {
		this.closingOTC = closingOTC;
	}
	public double getOtherCost() {
		return otherCost;
	}
	public void setOtherCost(double otherCost) {
		this.otherCost = otherCost;
	}
	@Override
	public Map<String, Object> getPdfParameters() throws Exception {
//		logger.info("Parameter collection");
		Map<String, Object> params = new HashMap<>();

		LLIDemandNoteService lliDemandNoteService =ServiceDAOFactory.getService(LLIDemandNoteService.class);

		lliDemandNoteService.populateFooterInfoForPDF(params);
		lliDemandNoteService.populateClientInfoForPDF(params, super.getClientID());
		lliDemandNoteService.populateBillInfoForPDF(params, this);

		params.put("closingOTC", String.format("%.2f", this.getClosingOTC()));
		params.put("otherCost", String.format("%.2f", this.getOtherCost()));

		LLIApplication lliApplication = ServiceDAOFactory.getService(LLIApplicationService.class).getNewFlowLLIApplicationByDemandNoteID(this.getID());
		if(lliApplication == null) {
			throw new RequestFailureException("No LLI Application found with demand note id " + this.getID());
		}
		lliDemandNoteService.populateLLIApplicationInfoForPDF(params, lliApplication, getID());
		
		return params;
	}
	@Override
	public String getResourceFile() {
		return BillConstants.LLI_DEMAND_NOTE_CLOSE_CONNECTION_TEMPLATE;
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
