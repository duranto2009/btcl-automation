package lli.Application.Reconnect;

import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import annotation.Transactional;
import common.RequestFailureException;
import lli.Application.LLIApplication;
import lli.client.td.LLIClientTDService;
import lli.connection.LLIConnectionConstants;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

@TableName("at_lli_application_reconnect")
@ForeignKeyName("parentApplicationID")
public class LLIReconnectApplication extends LLIApplication{
	@PrimaryKey
	@ColumnName("extendedApplicationID")
	long extendedApplicationID;
	@ColumnName("suggestedDate")
	long suggestedDate;
	
	/**/
	public long getExtendedApplicationID() {
		return extendedApplicationID;
	}
	public void setExtendedApplicationID(long extendedApplicationID) {
		this.extendedApplicationID = extendedApplicationID;
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
		this.setApplicationType(LLIConnectionConstants.RECONNECT);
		this.setDemandNoteNeeded(true);
		super.insertApplication();
	}
	@Override
	public void setImmutablePropertyWhileProcessing(LLIApplication lastApplicationSnapshot) throws Exception {
		if(!(lastApplicationSnapshot instanceof LLIReconnectApplication)) {
			throw new RequestFailureException("");
		}

		LLIReconnectApplication lliReconnectApplication = (LLIReconnectApplication) lastApplicationSnapshot;
		String content = this.getContent();
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliReconnectApplication, this, LLIReconnectApplication.class);
		setContent(content);
	}
	@Override
	@Transactional
	public void completeApplication() throws Exception {
		ServiceDAOFactory.getService(LLIClientTDService.class).reconnectClientByClientID(this.getClientID(), this.getSuggestedDate());
		super.completeApplication();
	}

}
