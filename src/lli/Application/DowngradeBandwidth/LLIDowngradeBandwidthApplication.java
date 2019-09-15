package lli.Application.DowngradeBandwidth;

import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import annotation.Transactional;
import common.RequestFailureException;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import lli.LLILongTermService;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.Application.SingleConnectionApplication;
import lli.connection.LLIConnectionConstants;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

@TableName("at_lli_application_downgrade_bandwidth")
@ForeignKeyName("parentApplicationID")
public class LLIDowngradeBandwidthApplication extends LLIApplication implements SingleConnectionApplication{
	@PrimaryKey
	@ColumnName("extendedApplicationID")
	long extendedApplicationID;
	@ColumnName("connectionID")
	long connectionID;
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
	public long getConnectionID() {
		return connectionID;
	}
	public void setConnectionID(long connectionID) {
		this.connectionID = connectionID;
	}
	
	
	
	@Override @Transactional
	public void insertApplication() throws Exception {
		this.setApplicationType(LLIConnectionConstants.DOWNGRADE_BANDWIDTH);
		this.setDemandNoteNeeded(true);
		
		LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);
		LLIConnectionInstance lliConnectionInstance = lliConnectionService.getLLIConnectionByConnectionID(this.connectionID);
		if(lliConnectionInstance ==  null){
			throw new RequestFailureException("No connection found with connection ID "+this.connectionID);
		}
		
		if(this.bandwidth>=lliConnectionInstance.getBandwidth()){
			throw new RequestFailureException("The connection don't have enough bandwidth to downgrade "+this.bandwidth+" MB bandwidth.");
		}
		
		double totalNonLongTermBW = ServiceDAOFactory.getService(LLILongTermService.class).getTotalNonLongTermRegularBWByClientID(getClientID());
		if(totalNonLongTermBW < this.bandwidth) {
			throw new RequestFailureException("Due to Long Term Contracts, system can allow only "+totalNonLongTermBW + " Mbps for downgrade");
		}
		
		super.insertApplication();
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void setImmutablePropertyWhileProcessing(LLIApplication lastApplicationSnapshot) throws Exception {
		if(!(lastApplicationSnapshot instanceof LLIDowngradeBandwidthApplication)) {
			throw new RequestFailureException("");
		}
		LLIConnectionInstance connectionFromContent = ServiceDAOFactory.getService(LLIApplicationService.class).getLLIConnectionFromApplicationContent(this);
		ServiceDAOFactory.getService(LLIConnectionService.class).validateLLIConnectionInstance(connectionFromContent);
		
		LLIDowngradeBandwidthApplication lliDowngradeBandwidthApplication = (LLIDowngradeBandwidthApplication) lastApplicationSnapshot;
		String content = this.getContent();
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliDowngradeBandwidthApplication, this, LLIDowngradeBandwidthApplication.class);
		setContent(content);
	}
	
	
	@Override @Transactional @SuppressWarnings("static-access") 
	public void completeApplication() throws Exception {
		LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);
		LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);
		
		LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(this);
		lliConnectionService.reviseConnection(lliConnectionInstance);
		
		super.completeApplication();
	}
	
	
	
	
	
	
}
