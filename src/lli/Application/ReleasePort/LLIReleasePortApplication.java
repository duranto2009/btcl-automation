package lli.Application.ReleasePort;

import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import annotation.Transactional;
import common.RequestFailureException;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.Application.SingleConnectionApplication;
import lli.connection.LLIConnectionConstants;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

@TableName("at_lli_application_release_port")
@ForeignKeyName("parentApplicationID")
public class LLIReleasePortApplication extends LLIApplication implements SingleConnectionApplication{
	@PrimaryKey
	@ColumnName("extendedApplicationID")
	long extendedApplicationID;
	@ColumnName("connectionID")
	long connectionID;
	@ColumnName("officeID")
	long officeID;
	@ColumnName("portCount")
	int portCount;
	
	@ColumnName("suggestedDate")
	long suggestedDate;
	
	/**/
	public long getExtendedApplicationID() {
		return extendedApplicationID;
	}
	public void setExtendedApplicationID(long extendedApplicationID) {
		this.extendedApplicationID = extendedApplicationID;
	}
	
	public long getConnectionID() {
		return connectionID;
	}

	public long getOfficeID() {
		return officeID;
	}
	public void setConnectionID(long connectionID) {
		this.connectionID = connectionID;
	}
	public void setOfficeID(long officeID) {
		this.officeID = officeID;
	}
	public long getSuggestedDate() {
		return suggestedDate;
	}
	public void setSuggestedDate(long suggestedDate) {
		this.suggestedDate = suggestedDate;
	}
	
	
	public int getPortCount() {
		return portCount;
	}
	public void setPortCount(int portCount) {
		this.portCount = portCount;
	}
	@Override
	@Transactional
	public void insertApplication() throws Exception {
		this.setApplicationType(LLIConnectionConstants.RELEASE_PORT);
		this.setDemandNoteNeeded(false);
		super.insertApplication();
	}
	@SuppressWarnings("static-access")
	@Override
	public void setImmutablePropertyWhileProcessing(LLIApplication lastApplicationSnapshot) throws Exception {
		if(!(lastApplicationSnapshot instanceof LLIReleasePortApplication)) {
			throw new RequestFailureException("");
		}
		LLIConnectionInstance connectionFromContent = ServiceDAOFactory.getService(LLIApplicationService.class).getLLIConnectionFromApplicationContent(this);
		ServiceDAOFactory.getService(LLIConnectionService.class).validateLLIConnectionInstance(connectionFromContent);

		LLIReleasePortApplication lliReleasePortApplication = (LLIReleasePortApplication) lastApplicationSnapshot;
		String content = this.getContent();
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliReleasePortApplication, this, LLIReleasePortApplication.class);
		setContent(content);
	}
	@SuppressWarnings("static-access")
	@Override
	@Transactional
	public void completeApplication() throws Exception {
		LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);
		LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);
		
		LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(this);
		lliConnectionInstance.setIncident(LLIConnectionConstants.RELEASE_PORT);
		lliConnectionService.reviseConnection(lliConnectionInstance);
		
		super.completeApplication();
	}

}
