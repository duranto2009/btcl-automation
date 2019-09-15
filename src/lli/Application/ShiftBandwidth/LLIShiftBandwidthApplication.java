package lli.Application.ShiftBandwidth;

import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import annotation.Transactional;
import common.RequestFailureException;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import lli.Application.DoubleConnectionApplication;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.connection.LLIConnectionConstants;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

@TableName("at_lli_application_shift_bandwidth")
@ForeignKeyName("parentApplicationID")
public class LLIShiftBandwidthApplication extends LLIApplication implements DoubleConnectionApplication{
	@PrimaryKey
	@ColumnName("extendedApplicationID")
	long extendedApplicationID;
	@ColumnName("connectionID")
	long connectionID;
	@ColumnName("destinationConnectionID")
	long destinationConnectionID;
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

	public long getDestinationConnectionID() {
		return destinationConnectionID;
	}
	public void setDestinationConnectionID(long destinationConnectionID) {
		this.destinationConnectionID = destinationConnectionID;
	}
	public double getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
	}
	public long getConnectionID() {
		return connectionID;
	}
	public void setConnectionID(long connectionID) {
		this.connectionID = connectionID;
	}
	public long getSuggestedDate() {
		return suggestedDate;
	}
	public void setSuggestedDate(long suggestedDate) {
		this.suggestedDate = suggestedDate;
	}
	
	
	@Override
	@Transactional
	public void insertApplication() throws Exception {
		this.setApplicationType(LLIConnectionConstants.SHIFT_BANDWIDTH);
		this.setDemandNoteNeeded(false);
		super.insertApplication();
	}
	@Override
	public void setImmutablePropertyWhileProcessing(LLIApplication lastApplicationSnapshot) throws Exception {
		if(!(lastApplicationSnapshot instanceof LLIShiftBandwidthApplication)) {
			throw new RequestFailureException("This is not a valid Shift Bandwidth Application");
		}

		//Generating DTO from JSON
		LLIConnectionInstance sourceConnection = ServiceDAOFactory.getService(LLIApplicationService.class).getConnectionFromShiftBandwidthApplicationContent(this,0);
		LLIConnectionInstance destinationConnection = ServiceDAOFactory.getService(LLIApplicationService.class).getConnectionFromShiftBandwidthApplicationContent(this,1);
		
		//Validation
		ServiceDAOFactory.getService(LLIApplicationService.class).validateShiftBandwidthApplication(sourceConnection, destinationConnection);
		
		//Updating Content
		LLIShiftBandwidthApplication lliShiftBandwidthApplication = (LLIShiftBandwidthApplication) lastApplicationSnapshot;
		String content = this.getContent();
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliShiftBandwidthApplication, this, LLIShiftBandwidthApplication.class);
		setContent(content);
	}
	@Override
	@Transactional
	public void completeApplication() throws Exception {
		LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);
		
		//Generating DTO from JSON
		LLIConnectionInstance sourceConnection = ServiceDAOFactory.getService(LLIApplicationService.class).getConnectionFromShiftBandwidthApplicationContent(this,0);
		LLIConnectionInstance destinationConnection = ServiceDAOFactory.getService(LLIApplicationService.class).getConnectionFromShiftBandwidthApplicationContent(this,1);
		
		//Validation
		ServiceDAOFactory.getService(LLIApplicationService.class).validateShiftBandwidthApplication(sourceConnection, destinationConnection);
		
		//Updating Connections
		sourceConnection.setIncident(LLIConnectionConstants.SHIFT_BANDWIDTH);
		lliConnectionService.reviseConnection(sourceConnection);
		destinationConnection.setIncident(LLIConnectionConstants.SHIFT_BANDWIDTH);
		lliConnectionService.reviseConnection(destinationConnection);
		
		super.completeApplication();
	}
	@Override
	public long getSecondConnectionID() {
		return getDestinationConnectionID();
	}

}
