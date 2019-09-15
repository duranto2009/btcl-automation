package lli.Application.BreakLongTerm;

import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import annotation.Transactional;
import common.RequestFailureException;
import lli.LLILongTermService;
import lli.Application.LLIApplication;
import lli.connection.LLIConnectionConstants;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

@TableName("at_lli_application_break_long_term")
@ForeignKeyName("parentApplicationID")
public class LLIBreakLongTermApplication extends LLIApplication{
	@PrimaryKey
	@ColumnName("extendedApplicationID")
	long extendedApplicationID;
	@ColumnName("bandwidth")
	double bandwidth;
	@ColumnName("contractID")
	long contractID;
	@ColumnName("suggestedDate")
	long suggestedDate;
	
	/**/
	public long getExtendedApplicationID() {
		return extendedApplicationID;
	}
	public void setExtendedApplicationID(long extendedApplicationID) {
		this.extendedApplicationID = extendedApplicationID;
	}
	public double getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
	}
	public long getSuggestedDate() {
		return suggestedDate;
	}
	public void setSuggestedDate(long suggestedDate) {
		this.suggestedDate = suggestedDate;
	}

	public long getContractID() {
		return contractID;
	}
	public void setContractID(long contractID) {
		this.contractID = contractID;
	}
	
	
	
	
	@Override @Transactional
	public void insertApplication() throws Exception {
		this.setApplicationType(LLIConnectionConstants.BREAK_LONG_TERM);
		this.setDemandNoteNeeded(true);
		super.insertApplication();
	}
	@Override
	public void setImmutablePropertyWhileProcessing(LLIApplication lastApplicationSnapshot) throws Exception {
		if(!(lastApplicationSnapshot instanceof LLIBreakLongTermApplication)) {
			throw new RequestFailureException("");
		}
		LLIBreakLongTermApplication lliBreakLongTermApplication = (LLIBreakLongTermApplication) lastApplicationSnapshot;
		String content = this.getContent();
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliBreakLongTermApplication, this, LLIBreakLongTermApplication.class);
		setContent(content);
	}
	public void completeApplication() throws Exception {
		LLILongTermService lliLongTermService = ServiceDAOFactory.getService(LLILongTermService.class);
		
		lliLongTermService.breakLongTermByLongTermIDAndBandwidth(this.contractID, this.bandwidth, this.suggestedDate);
		
		super.completeApplication();
	}
}
