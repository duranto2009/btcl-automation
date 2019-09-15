package lli.Application.CloseConnection;

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

@TableName("at_lli_application_close_connection")
@ForeignKeyName("parentApplicationID")
public class LLICloseConnectionApplication extends LLIApplication implements SingleConnectionApplication{
	@PrimaryKey
	@ColumnName("extendedApplicationID")
	long extendedApplicationID;
	
	@ColumnName("connectionID")
	long connectionID;
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
		this.setApplicationType(LLIConnectionConstants.CLOSE_CONNECTION);
		this.setDemandNoteNeeded(true);
		super.insertApplication();
	}
	@Override
	public void setImmutablePropertyWhileProcessing(LLIApplication lastApplicationSnapshot) throws Exception {
		if(!(lastApplicationSnapshot instanceof LLICloseConnectionApplication)) {
			throw new RequestFailureException("");
		}

		LLICloseConnectionApplication lliCloseConnectionApplication = (LLICloseConnectionApplication) lastApplicationSnapshot;
		String content = this.getContent();
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliCloseConnectionApplication, this, LLICloseConnectionApplication.class);
		setContent(content);
	}
	@SuppressWarnings({ "unused" })
	@Override
	@Transactional
	public void completeApplication() throws Exception {
		LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);
		LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);
		
		lliConnectionService.closeConnectionByConnectionID(this.getConnectionID(), this.getSuggestedDate());
		
		super.completeApplication();
	}

}
