package lli.Application.ShiftPop;

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

@TableName("at_lli_application_shift_pop")
@ForeignKeyName("parentApplicationID")
public class LLIShiftPopApplication extends LLIApplication implements SingleConnectionApplication{
	@PrimaryKey
	@ColumnName("extendedApplicationID")
	long extendedApplicationID;
	@ColumnName("connectionID")
	long connectionID;
	@ColumnName("officeID")
	long officeID;
	@ColumnName("popID")
	long popID;
	@ColumnName("newPopID")
	long newPopID;
	@ColumnName("loopProvider")
	int loopProvider;
	@ColumnName("suggestedDate")
	long suggestedDate;
	
	public long getNewPopID() {
		return newPopID;
	}
	public void setNewPopID(long newPopID) {
		this.newPopID = newPopID;
	}
	/**/
	public long getExtendedApplicationID() {
		return extendedApplicationID;
	}
	public void setExtendedApplicationID(long extendedApplicationID) {
		this.extendedApplicationID = extendedApplicationID;
	}
	public long getPopID() {
		return popID;
	}
	public void setPopID(long popID) {
		this.popID = popID;
	}
	public int getLoopProvider() {
		return loopProvider;
	}
	public void setLoopProvider(int loopProvider) {
		this.loopProvider = loopProvider;
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
	public long getOfficeID() {
		return officeID;
	}
	public void setOfficeID(long officeID) {
		this.officeID = officeID;
	}
	
	
	@Override @Transactional
	public void insertApplication() throws Exception {
		this.setApplicationType(LLIConnectionConstants.SHIFT_POP);
		this.setDemandNoteNeeded(true);
		super.insertApplication();
	}
	@SuppressWarnings("static-access")
	@Override
	public void setImmutablePropertyWhileProcessing(LLIApplication lastApplicationSnapshot) throws Exception {
		if(!(lastApplicationSnapshot instanceof LLIShiftPopApplication)) {
			throw new RequestFailureException("This is not a valid Shift Pop Application");
		}
		LLIConnectionInstance connectionFromContent = ServiceDAOFactory.getService(LLIApplicationService.class).getLLIConnectionFromApplicationContent(this);
		ServiceDAOFactory.getService(LLIConnectionService.class).validateLLIConnectionInstance(connectionFromContent);
		
		LLIShiftPopApplication lliShiftPopApplication = (LLIShiftPopApplication) lastApplicationSnapshot;
		String content = this.getContent();
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliShiftPopApplication, this, LLIShiftPopApplication.class);
		setContent(content);
	}
	
	
	@Override @Transactional @SuppressWarnings("static-access") 
	public void completeApplication() throws Exception {
		LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);
		LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);
		
		LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(this);
		lliConnectionInstance.setIncident(LLIConnectionConstants.SHIFT_POP);
		lliConnectionService.reviseConnection(lliConnectionInstance);
		
		super.completeApplication();
	}
	
	
	
	
	
}
