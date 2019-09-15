package lli.Application.NewConnection;

import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import annotation.Transactional;
import common.RequestFailureException;
import common.bill.BillDTO;
import common.bill.BillService;
import lli.Application.Office.Office;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import lli.LLILongTermContract;
import lli.LLILongTermService;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.connection.LLIConnectionConstants;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

import java.util.List;

@TableName("at_lli_application_new_connection")
@ForeignKeyName("parentApplicationID")
public class LLINewConnectionApplication extends LLIApplication{
	@PrimaryKey
	@ColumnName("extendedApplicationID")
	long extendedApplicationID;
	@ColumnName("bandwidth")
	double bandwidth;
	@ColumnName("connectionType")
	int connectionType;
	@ColumnName("loopProvider")
	int loopProvider;
	@ColumnName("duration")
	int duration;
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
	public int getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(int connectionType) {
		this.connectionType = connectionType;
	}

	public int getLoopProvider() {
		return loopProvider;
	}
	public void setLoopProvider(int loopProvider) {
		this.loopProvider = loopProvider;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
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
		this.setApplicationType(LLIConnectionConstants.NEW_CONNECTION);
		this.setDemandNoteNeeded(true);
		super.insertApplication();
	}
	@SuppressWarnings("static-access")
	@Override
	public void setImmutablePropertyWhileProcessing(LLIApplication lastApplicationSnapshot) throws Exception {
		if(!(lastApplicationSnapshot instanceof LLINewConnectionApplication)) {
			throw new RequestFailureException("This is not a valid New Connection application");
		}
		LLIConnectionInstance connectionFromContent = ServiceDAOFactory.getService(LLIApplicationService.class).getLLIConnectionFromApplicationContent(this);
		ServiceDAOFactory.getService(LLIConnectionService.class).validateLLIConnectionInstance(connectionFromContent);

		LLINewConnectionApplication lliNewConnection = (LLINewConnectionApplication) lastApplicationSnapshot;
		String content = this.getContent();
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliNewConnection, this, LLINewConnectionApplication.class);
		setContent(content);
	}
	
	@SuppressWarnings("static-access")
	@Override
	@Transactional
	public void completeApplication() throws Exception {
		LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);
		LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);
		
		LLIConnectionInstance lliConnectionInstance = lliApplicationService.getLLIConnectionFromApplicationContent(this);
		lliConnectionInstance.setStartDate(lliConnectionInstance.getActiveFrom());
		
		if (lliConnectionInstance.getConnectionType() == LLIConnectionConstants.CONNECTION_TYPE_REGULAR_LONG) {
			LLILongTermContract lliLongTermContract = new LLILongTermContract();
			lliLongTermContract.setClientID(lliConnectionInstance.getClientID());
			lliLongTermContract.setBandwidth(lliConnectionInstance.getBandwidth());
			lliLongTermContract.setActiveFrom(lliConnectionInstance.getActiveFrom());
			lliLongTermContract.setContractStartDate(lliConnectionInstance.getActiveFrom());
			ServiceDAOFactory.getService(LLILongTermService.class).insertNewLongTermContract(lliLongTermContract);
			
			lliConnectionInstance.setConnectionType(LLIConnectionConstants.CONNECTION_TYPE_REGULAR);
		}
		
		BillDTO demandNote = ServiceDAOFactory.getService(BillService.class).getBillByBillID(this.getDemandNoteID());
		double discountPercentage = demandNote.getDiscountPercentage();
		
		//set demand note discount rate into connection
		lliConnectionInstance.setDiscountRate(discountPercentage);
		lliConnectionService.insertNewLLIConnection(lliConnectionInstance);
//		setEntityIDInBillDTO(this.getDemandNoteID(), lliConnectionInstance.getID());
//		setEntityIDInFileDTO(this.getDemandNoteID(), lliConnectionInstance.getID());
//		
		super.completeApplication();
	}
	
	private void setEntityIDInFileDTO(Long demandNoteID, long id) {
		// TODO
	
		// to be implemented
		
	}
	private void setEntityIDInBillDTO(Long demandNoteID, long entityID) throws Exception {
		BillService billService = ServiceDAOFactory.getService(BillService.class);
		BillDTO billDTO = billService.getBillByBillID(demandNoteID);
		if(billDTO == null) {
			throw new RequestFailureException ("Demand Note not found");
		}
		billDTO.setEntityID(entityID);
		ModifiedSqlGenerator.updateEntityByPropertyList(billDTO, BillDTO.class, true, false, new String[] {"entityID"});
		
	}
	
	
	
	
}
