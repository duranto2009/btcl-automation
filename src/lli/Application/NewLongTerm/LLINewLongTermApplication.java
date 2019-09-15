package lli.Application.NewLongTerm;

import java.util.Calendar;

import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import annotation.Transactional;
import common.RequestFailureException;
import lli.LLILongTermContract;
import lli.LLILongTermService;
import lli.Application.LLIApplication;
import lli.connection.LLIConnectionConstants;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

@TableName("at_lli_application_new_long_term")
@ForeignKeyName("parentApplicationID")
public class LLINewLongTermApplication extends LLIApplication{
	
	@PrimaryKey
	@ColumnName("extendedApplicationID")
	long extendedApplicationID;
	@ColumnName("bandwidth")
	double bandwidth;
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
	
	
	@Override @Transactional
	public void insertApplication() throws Exception {
		ServiceDAOFactory.getService(LLILongTermService.class).validateBandwidthOfNewLongTermApplication(this.getBandwidth(), this.getClientID());
		
		this.setApplicationType(LLIConnectionConstants.NEW_LONG_TERM);
		this.setDemandNoteNeeded(false);
		super.insertApplication();
	}
	@Override
	public void setImmutablePropertyWhileProcessing(LLIApplication lastApplicationSnapshot) throws Exception {
		if(!(lastApplicationSnapshot instanceof LLINewLongTermApplication)) {
			throw new RequestFailureException("This is not a valid Long Term Application");
		}
		LLINewLongTermApplication lliNewLongTermApplication = (LLINewLongTermApplication) lastApplicationSnapshot;
		String content = this.getContent();
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliNewLongTermApplication, this, LLINewLongTermApplication.class);
		setContent(content);
	}
	@Override @Transactional  
	public void completeApplication() throws Exception {
		LLILongTermService lliLongTermService = ServiceDAOFactory.getService(LLILongTermService.class);
		
		ServiceDAOFactory.getService(LLILongTermService.class).validateBandwidthOfNewLongTermApplication(this.getBandwidth(), this.getClientID());
		
		long activationTime = this.getSuggestedDate();
		
		LLILongTermContract lliLongTermContract = new LLILongTermContract();
		lliLongTermContract.setActiveFrom(activationTime);
		lliLongTermContract.setBandwidth(this.bandwidth);
		lliLongTermContract.setClientID(this.getClientID());
		lliLongTermContract.setContractStartDate(activationTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(activationTime);
		calendar.add(Calendar.YEAR, 5);
		lliLongTermContract.setContractEndDate(calendar.getTimeInMillis());
		lliLongTermContract.setStatus(LLILongTermContract.STATUS_ACTIVE);
		
		lliLongTermService.insertNewLongTermContract(lliLongTermContract);
		
		super.completeApplication();
	}
}
