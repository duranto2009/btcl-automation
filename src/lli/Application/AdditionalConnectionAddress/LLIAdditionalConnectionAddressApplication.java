package lli.Application.AdditionalConnectionAddress;

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

@TableName("at_lli_application_additional_connection_address")
@ForeignKeyName("parentApplicationID")
public class LLIAdditionalConnectionAddressApplication extends LLIApplication implements SingleConnectionApplication{
	@PrimaryKey
	@ColumnName("extendedApplicationID")
	long extendedApplicationID;
	@ColumnName("connectionID")
	long connectionID;
	@ColumnName("address")
	String address;
	@ColumnName("loopProvider")
	int loopProvider;
	@ColumnName("suggestedDate")
	long suggestedDate;
	
	/**/
	public long getExtendedApplicationID() {
		return extendedApplicationID;
	}
	public void setExtendedApplicationID(long extendedApplicationID) {
		this.extendedApplicationID = extendedApplicationID;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getLoopProvider() {
		return loopProvider;
	}
	public void setLoopProvider(int loopProvider) {
		this.loopProvider = loopProvider;
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
		this.setApplicationType(LLIConnectionConstants.ADDITIONAL_CONNECTION_ADDRESS);
		this.setDemandNoteNeeded(true);
		super.insertApplication();
	}
	@SuppressWarnings("static-access")
	@Override
	public void setImmutablePropertyWhileProcessing(LLIApplication lastApplicationSnapshot) throws Exception {
		if(!(lastApplicationSnapshot instanceof LLIAdditionalConnectionAddressApplication)) {
			throw new RequestFailureException("");
		}
		LLIConnectionInstance connectionFromContent = ServiceDAOFactory.getService(LLIApplicationService.class).getLLIConnectionFromApplicationContent(this);
		ServiceDAOFactory.getService(LLIConnectionService.class).validateLLIConnectionInstance(connectionFromContent);

		LLIAdditionalConnectionAddressApplication lliAdditionalConnectionAddressApplication = (LLIAdditionalConnectionAddressApplication) lastApplicationSnapshot;
		String content = this.getContent();
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliAdditionalConnectionAddressApplication, this, LLIAdditionalConnectionAddressApplication.class);
		setContent(content);
	}
	@SuppressWarnings("static-access")
	@Override
	@Transactional
	public void completeApplication() throws Exception {
		LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);
		LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);
		
		LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(this);
		lliConnectionInstance.setIncident(LLIConnectionConstants.ADDITIONAL_CONNECTION_ADDRESS);
		lliConnectionService.reviseConnection(lliConnectionInstance);
		
		super.completeApplication();
	}
	
	
	
	
	
}

