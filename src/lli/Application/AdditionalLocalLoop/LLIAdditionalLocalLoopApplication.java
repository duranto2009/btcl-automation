package lli.Application.AdditionalLocalLoop;

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
import lombok.Data;
import lombok.EqualsAndHashCode;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("at_lli_application_additional_local_loop")
@ForeignKeyName("parentApplicationID")
public class LLIAdditionalLocalLoopApplication extends LLIApplication implements SingleConnectionApplication{
	@PrimaryKey
	@ColumnName("extendedApplicationID")
	long extendedApplicationID;
	@ColumnName("connectionID")
	long connectionID;
	@ColumnName("officeID")
	long officeID;
	@ColumnName("popID")
	long popID;
	@ColumnName("suggestedDate")
	long suggestedDate;



	@Override
	@Transactional
	public void insertApplication() throws Exception {
		this.setApplicationType(LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP);
		this.setDemandNoteNeeded(true);
		super.insertApplication();
	}
	@SuppressWarnings("static-access")
	@Override
	public void setImmutablePropertyWhileProcessing(LLIApplication lastApplicationSnapshot) throws Exception {
		if(!(lastApplicationSnapshot instanceof LLIAdditionalLocalLoopApplication)) {
			throw new RequestFailureException("");
		}
		LLIConnectionInstance connectionFromContent = ServiceDAOFactory.getService(LLIApplicationService.class).getLLIConnectionFromApplicationContent(this);
		ServiceDAOFactory.getService(LLIConnectionService.class).validateLLIConnectionInstance(connectionFromContent);

		LLIAdditionalLocalLoopApplication lliAdditionalLocalLoopApplication = (LLIAdditionalLocalLoopApplication) lastApplicationSnapshot;
		String content = this.getContent();
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliAdditionalLocalLoopApplication, this, LLIAdditionalLocalLoopApplication.class);
		setContent(content);
	}
	@SuppressWarnings("static-access")
	@Override
	@Transactional
	public void completeApplication() throws Exception {
		LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);
		LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);
		
		LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(this);
		lliConnectionInstance.setIncident(LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP);
		lliConnectionService.reviseConnection(lliConnectionInstance);
		
		super.completeApplication();
	}

}
