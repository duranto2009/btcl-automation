package common.payment;

import accounting.AccountType;
import accounting.AccountingEntryService;
import annotation.Transactional;
import application.Application;
import application.ApplicationState;
import client.bill.CommonBillService;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.application.CoLocationApplicationService;
import common.*;
import common.bill.BillConstants;
import common.bill.BillDAO;
import common.bill.BillDTO;
import common.bill.BillService;
import common.payment.constants.PaymentConstants;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import exception.NoDataFoundException;
import file.FileDTO;
import file.FileService;
import file.FileTypeConstants;
import flow.entity.FlowState;
import flow.repository.FlowRepository;
import global.GlobalService;
import lli.Application.EFR.EFRService;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.FlowConnectionManager.LLIFlowConnectionService;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopService;
import lli.Application.ReviseClient.ReviseDTO;
import lli.Application.ReviseClient.ReviseService;
import lli.Application.ownership.LLIOwnerChangeService;
import lli.Application.ownership.LLIOwnerShipChangeApplication;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import nix.application.NIXApplication;
import nix.application.NIXApplicationService;
import nix.revise.NIXReviseDTO;
import nix.revise.NIXReviseService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import util.DatabaseConnectionFactory;
import util.NavigationService;
import util.ServiceDAOFactory;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationLink;
import vpn.application.VPNApplicationLinkConditionBuilder;
import vpn.application.VPNApplicationService;
import vpn.client.ClientDetailsDTO;
import vpn.client.ClientForm;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

public class PaymentService implements NavigationService {
	int moduleID ;

	BillDAO billDAO = new BillDAO();
	BillService billService = ServiceDAOFactory.getService(BillService.class);
	LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);
	LocalLoopService localLoopService = ServiceDAOFactory.getService(LocalLoopService.class);
	LLIFlowConnectionService lliFlowConnectionService=ServiceDAOFactory.getService(LLIFlowConnectionService.class);
	CoLocationApplicationService coLocationApplicationService = ServiceDAOFactory.getService(CoLocationApplicationService.class);
	NIXApplicationService nIXApplicationService = ServiceDAOFactory.getService(NIXApplicationService.class);
	NIXReviseService nixReviseService=ServiceDAOFactory.getService(NIXReviseService.class);
	VPNApplicationService vpnApplicationService = ServiceDAOFactory.getService(VPNApplicationService.class);
	GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);
	CommonBillService commonBillService=ServiceDAOFactory.getService(CommonBillService.class);

	AccountingEntryService accountingEntryService=ServiceDAOFactory.getService(AccountingEntryService.class); ;


	ReviseService reviseService = ServiceDAOFactory.getService(ReviseService.class);
    EFRService efrService = ServiceDAOFactory.getService(EFRService.class);
	Logger logger = Logger.getLogger(getClass());
	static Integer lockingObject = new Integer(1);
	PaymentDAO paymentDAO = new PaymentDAO();
	public int getModuleID() {
		return moduleID;
	}

	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}

	@Transactional
	public void insertNewBankPayment(PaymentDTO paymentDTO, LoginDTO loginDTO, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();

		long currentTime=System.currentTimeMillis();
		ArrayList<Long> recordIDs=  (ArrayList<Long>) Arrays.stream(paymentDTO.getBillIDs()).boxed().collect(Collectors.toList());
		ArrayList<BillDTO> billDTOs = (ArrayList<BillDTO>)billDAO.getBillDTOListByIDList(recordIDs, databaseConnection);

		int moduleID = billDTOs.iterator().next().getEntityTypeID() / EntityTypeConstant.MULTIPLIER2;
		paymentDTO.setModuleID(moduleID);

		boolean isValidPayment=true;
		String invalidityMsg="";

		double totalBtclAmount=0.0;
		double totalVatAmount=0.0;
		double totalPayableAmount=0.0;
		double itDeduction = 0.0;

		long clientID=billDTOs.get(0).getClientID();

		long billClientID = -1;
		int entityTypeID = -1;
		/*long entityID = -1;*/
		boolean isInitValidation = true;

		//bony for multiple bill
		List<BillDTO> tempDTOS=new ArrayList<>();
		tempDTOS.addAll(billDTOs);

		List<BillDTO> multipleBillDtos=new ArrayList<>();

		double security = 0;
		boolean isSecurityNeeded=false;
		for(BillDTO billDTO:tempDTOS){
			if(billDTO.getBillType()==BillConstants.MULTIPLE_MONTH_BILL||billDTO.getBillType()==BillConstants.FINAL_BILL){
				List<BillDTO> childBillDto=commonBillService.getIndividualBillFromSummedBill(billDTO);
				if(childBillDto!=null && childBillDto.size()>0){
					billDTOs.remove(billDTO);
					multipleBillDtos.add(billDTO);
					recordIDs.remove(billDTO.getID());
					billDTOs.addAll(childBillDto);
					for (BillDTO billDTO1:childBillDto){

						recordIDs.add(billDTO1.getID());
					}
				}

				if(billDTO.getBillType()==BillConstants.FINAL_BILL){
					isSecurityNeeded=true;
					security = (accountingEntryService
							.getBalanceByClientIDAndAccountID(billDTO.getClientID()
									, AccountType.SECURITY.getID()));
				}

			}
		}


		for(BillDTO billDTO: billDTOs){
			totalBtclAmount+=billDTO.getTotalPayable();
			totalVatAmount+=( paymentDTO.getVatIncluded() == 1 )?billDTO.getVAT():0.0;
			totalPayableAmount += billDTO.getNetPayable();
			/*validation*/
			if(billClientID != -1 && billClientID != billDTO.getClientID()){
				throw new RequestFailureException("Client ID violation");
			}
			if(billDTO.getPaymentStatus() != BillDTO.UNPAID && billDTO.getPaymentStatus() != BillDTO.SKIPPED){
				throw new RequestFailureException("All bill should be unpaid or skipped");
			}
			if(entityTypeID != -1 && entityTypeID != billDTO.getEntityTypeID()){
				throw new RequestFailureException("All bill should have same Entity Type ID");
			}
			/*if(entityID != -1 && entityID != billDTO.getEntityID()){
					throw new RequestFailureException("All bill should have same Entity ID");
				}*/
			if(isInitValidation){
				billClientID = billDTO.getClientID();
				entityTypeID = billDTO.getEntityTypeID();
				//entityID = billDTO.getEntityID();
				isInitValidation = false;
			}
			/*validation*/

			if(billDTO.getPaymentID()!=null && billDTO.getPaymentID()>0){
				isValidPayment=false;
				invalidityMsg="Please check one of the bill is already paid";
				throw new RequestFailureException(invalidityMsg);
				//break;

			}

			if(clientID!=billDTO.getClientID()){
				isValidPayment=false;
				invalidityMsg="All bills should of same client";
				throw new RequestFailureException(invalidityMsg);
				//break;
			}

			//TODO: ADDEED BY BONY FOR CHANGE STATE


			if(billDTO.getBillType() == BillConstants.DEMAND_NOTE){

				flowStateUpdater(billDTO,loginDTO);
			}



		}

		itDeduction = paymentDTO.getItDeductionAmount();


//		if()

		if(paymentDTO.isItDeductionIncluded()){
			totalPayableAmount -= itDeduction;
		}

		if(isSecurityNeeded){

			if(Math.round(paymentDTO.getPayableAmount()+security)!=Math.round(totalPayableAmount)){

				logger.debug("paymentDTO.getTotalAmount(): "+ paymentDTO.getPayableAmount() +" totalPayableAmount:"+totalPayableAmount );
				isValidPayment=false;
				invalidityMsg="Something  wrong in calculating total payment.";
			}

		}else{
			if(Math.round(paymentDTO.getPayableAmount())!=Math.round(totalPayableAmount)){

				logger.debug("paymentDTO.getTotalAmount(): "+ paymentDTO.getPayableAmount() +" totalPayableAmount:"+totalPayableAmount );
				isValidPayment=false;
				invalidityMsg="Something  wrong in calculating total payment.";
			}
		}

		logger.debug("paymentDTO.getPaidAmount() " + paymentDTO.getPaidAmount());
		logger.debug("totalPayableAmount " + totalPayableAmount);

		if(isSecurityNeeded){

			if (Math.round(paymentDTO.getPaidAmount()+security) != Math.round(totalPayableAmount)) {

				logger.debug("paymentDTO.getPaidAmount(): " + paymentDTO.getPaidAmount() + " totalPayableAmount:" + totalPayableAmount);
				isValidPayment = false;
				invalidityMsg = "Paid amount is not equal to Total payable amount";
			}

		}else {
			if (Math.round(paymentDTO.getPaidAmount()) != Math.round(totalPayableAmount)) {

				logger.debug("paymentDTO.getPaidAmount(): " + paymentDTO.getPaidAmount() + " totalPayableAmount:" + totalPayableAmount);
				isValidPayment = false;
				invalidityMsg = "Paid amount is not equal to Total payable amount";
			}
		}

		if(!isValidPayment){

			new CommonActionStatusDTO().setErrorMessage(invalidityMsg, false, request);
			throw new Exception(invalidityMsg);
		}


		// by bony update billIDs

		long[] billIds=new long[recordIDs.size()];
//		Long[] billIds=recordIDs.toArray(new Long[recordIDs.size()]);
		for (int i=0;i<recordIDs.size();i++){
			billIds[i]=recordIDs.get(i);
		}

		paymentDTO.setBillIDs(billIds);

		if(isSecurityNeeded){
            paymentDTO.setBtclAmount(totalBtclAmount-security);
            paymentDTO.setPayableAmount(totalPayableAmount-security);

        }else{

            paymentDTO.setBtclAmount(totalBtclAmount);
            paymentDTO.setPayableAmount(totalPayableAmount);

        }
		paymentDTO.setVatAmount(totalVatAmount);
		paymentDTO.setItDeductionAmount(itDeduction);
		paymentDTO.setPaymentTime(paymentDTO.getPaymentTime());
		paymentDTO.setPaymentGatewayType(PaymentConstants.PAYEMENT_GATEWAY_TYPE_BANK);
		paymentDTO.setPaymentStatus(PaymentConstants.PAYMENT_STATUS_VERIFIED);
		paymentDTO.setVerificationTime(currentTime);
		paymentDTO.setVerifiedBy(loginDTO.getUserID());
		paymentDTO.setVerificationMessage(StringUtils.trimToEmpty( paymentDTO.getVerificationMessage()));
		paymentDTO.setLastModificationTime(currentTime);
		paymentDTO.setDescription("");
		logger.debug(paymentDTO);

		logger.debug(billDTOs);
		paymentDAO.payBillFromAPI(paymentDTO, loginDTO);

		//paymentDAO.insertNewPayment(paymentDTO, databaseConnection); //testing purpose by sharif

		for(BillDTO billDTO: multipleBillDtos){
			billDTO.setPaymentStatus(BillDTO.PAID_UNVERIFIED);
			billDTO.setPaymentID(paymentDTO.getID());
			billDTO.setLastModificationTime(currentTime);

			billDAO.updateBill(billDTO, databaseConnection);



		}


		if(paymentDTO.getDocument()!=null && paymentDTO.getDocument().getFileData().length>0){
			uploadDocument(paymentDTO,paymentDTO.getDocument(),FileTypeConstants.GLOBAL.BANK_PAYMENT_VAT, loginDTO, databaseConnection);
		}

		if(paymentDTO.getDocumentIT()!=null && paymentDTO.getDocumentIT().getFileData().length>0){
			uploadDocument(paymentDTO,paymentDTO.getDocumentIT(),FileTypeConstants.GLOBAL.BANK_PAYMENT_IT, loginDTO, databaseConnection);
		}
	}

	@Transactional
	public CommonActionStatusDTO approveBankPayment(PaymentDTO paymentDTO, LoginDTO loginDTO) throws Exception {
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
		long currentTime = System.currentTimeMillis();

		PaymentDTO oldPaymentDTO = paymentDAO.getPaymentDTObyID(paymentDTO.getID(), databaseConnection);
		logger.debug("oldPaymentDTO"+ oldPaymentDTO);
		if(oldPaymentDTO.getPaymentStatus() == PaymentConstants.PAYMENT_STATUS_APPROVED){
			commonActionStatusDTO.setMessage("Payment Already Approved");
			return commonActionStatusDTO;
		}

		ArrayList<BillDTO> billDTOs = (ArrayList<BillDTO>) billDAO.getBillDTOsByPaymentID( paymentDTO.getID(), databaseConnection);
		logger.debug(billDTOs);

		//remove bill for verification
		ArrayList<BillDTO> tempBills=new ArrayList<>();
		ArrayList<BillDTO> multipleBills=new ArrayList<>();
		tempBills.addAll(billDTOs);
		for (BillDTO billDTO:tempBills) {


			if (billDTO.getBillType() == BillConstants.MULTIPLE_MONTH_BILL
					|| billDTO.getBillType() == BillConstants.FINAL_BILL)
			{
				billDTOs.remove(billDTO);
				multipleBills.add(billDTO);

			}
		}

		oldPaymentDTO.setPaymentStatus(paymentDTO.getPaymentStatus());
		oldPaymentDTO.setApprovedBy(loginDTO.getUserID());
		oldPaymentDTO.setLastModificationTime(currentTime);

		if(paymentDTO.getPaymentStatus() == PaymentConstants.PAYMENT_STATUS_APPROVED){
			oldPaymentDTO.setDescription(StringUtils.trimToEmpty( paymentDTO.getDescription()));
		}else if (paymentDTO.getPaymentStatus() == PaymentConstants.PAYMENT_STATUS_REJECTED){
			oldPaymentDTO.setDescription(StringUtils.trimToEmpty( paymentDTO.getDescription()));
		}

		paymentDAO.updatePaymentDTO(oldPaymentDTO, databaseConnection);

		boolean approved = false;
		if (paymentDTO.getPaymentStatus()==PaymentConstants.PAYMENT_STATUS_APPROVED){
			approved = true;
		}

		for(BillDTO billDTO: billDTOs){
			billDTO.setLastModificationTime(currentTime);
			if(!approved){
				//TODO what to do if reject after payment?
			}
			else{
				billService.verifyPaymentByBillID(billDTO.getID(),paymentDTO.getID());

				//TODO: changed by bony check if needed
				if(billDTO.getBillType() == BillConstants.DEMAND_NOTE) {

					flowStateUpdater(billDTO,loginDTO);
				}
			}
		}

		if(approved){
			if(multipleBills!=null&& multipleBills.size()>0) {
				for (BillDTO billDTO : multipleBills) {
					billDTO.setPaymentStatus(PaymentConstants.PAYMENT_STATUS_APPROVED);
					billDTO.setLastModificationTime(currentTime);
					globalService.update(billDTO);
				}
			}
			commonActionStatusDTO.setMessage("payment is accepted");
		}else {
			commonActionStatusDTO.setMessage("payment is rejected");
		}

		return commonActionStatusDTO;
	}

	@Transactional
	private void flowStateUpdater(BillDTO billDTO,LoginDTO loginDTO) throws Exception {
		if((billDTO.getEntityTypeID()/100)==ModuleConstants.Module_ID_LLI) {
			updateFlowStateForLLI(billDTO,loginDTO);
		}else if((billDTO.getEntityTypeID()/100)==ModuleConstants.Module_ID_COLOCATION){
			updateFlowStateForCoLocation(billDTO,loginDTO);
		}else if((billDTO.getEntityTypeID()/100)==ModuleConstants.Module_ID_NIX){
			updateFlowStateForNIX(billDTO,loginDTO);
		}else if((billDTO.getEntityTypeID()/100)==ModuleConstants.Module_ID_VPN){
			updateFlowStateForVPN(billDTO,loginDTO);
		}
	}

	private void updateFlowStateForLLI(BillDTO billDTO,LoginDTO loginDTO) throws Exception {
		LLIApplication lliApplication = lliApplicationService.getNewFlowLLIApplicationByDemandNoteID(billDTO.getID());
		if (lliApplication != null) {
			if(lliApplication.getApplicationType() == LLIConnectionConstants.CLOSE_CONNECTION){
				if(lliApplication.getState()==LLIConnectionConstants.CLOSE_CONNECTION_PAYMENT_DONE_STATE){
					LLIConnection lliConnection= lliFlowConnectionService.getConnectionByID(lliApplication.getConnectionId());
					List<LocalLoop> localLoops=localLoopService.getLocalLoopByCon(lliConnection.getID());
					int distances=0;
					for (LocalLoop localLoop:localLoops
						 ) {
						distances+=localLoop.getBTCLDistances()+localLoop.getOCDistances();


					}
					if(distances==0){
						lliApplication.setState(LLIConnectionConstants.CLOSE_CONNECTION_WO_BYPASS);
						lliApplicationService.updateApplicatonState(lliApplication.getApplicationID(),lliApplication.getState());

					}else {

						lliApplicationService.updateApplicatonState(lliApplication.getApplicationID(), getNextStateFromCurrentState(lliApplication.getState()));
					}
				}else{
					lliApplicationService.updateApplicatonState(lliApplication.getApplicationID(), getNextStateFromCurrentState(lliApplication.getState()));

				}

			}else{

				lliApplicationService.updateApplicatonState(lliApplication.getApplicationID(), getNextStateFromCurrentState(lliApplication.getState()));
			}
			lliApplicationService.sendNotification(lliApplication,getNextStateFromCurrentState(lliApplication.getState()),loginDTO);


		} else {
			try {
				ReviseDTO reviseDTO = reviseService.getApplicationByDemandNoteId(billDTO.getID());
				reviseService.updateApplicatonState(reviseDTO.getId(), getNextStateFromCurrentState((int)reviseDTO.getState()));
				reviseService.sendNotification(reviseDTO,getNextStateFromCurrentState((int) reviseDTO.getState()),loginDTO);

			}catch (NoDataFoundException e) {
				logger.fatal(e.getMessage());

				LLIOwnerShipChangeApplication lliOwnerShipChangeApplication = ServiceDAOFactory.getService(LLIOwnerChangeService.class)
						.getApplicationByDemandNoteId(billDTO.getID());
				ServiceDAOFactory.getService(LLIOwnerChangeService.class)
						.updateApplicatonState(lliOwnerShipChangeApplication.getId(), getNextStateFromCurrentState(lliOwnerShipChangeApplication.getState()));
			}
		}
	}

	private void updateFlowStateForNIX(BillDTO billDTO,LoginDTO loginDTO) throws Exception {

		try {
			NIXApplication nixApplication= nIXApplicationService.
					getNIXApplicationByDemandNoteId(billDTO.getID());
			nixApplication.setState(getNextStateFromCurrentState(nixApplication.getState()));

			nIXApplicationService.updateApplicaton(nixApplication);
			nIXApplicationService.sendNotification(nixApplication,nixApplication.getState(),loginDTO);
		}catch (NoDataFoundException e){

			logger.fatal(e.getMessage());
			NIXReviseDTO reviseDTO = ServiceDAOFactory.getService(NIXReviseService.class).getNIXReviseClientApplicationByDemandNoteId(billDTO.getID());
			nixReviseService.updateApplicatonState(reviseDTO.getId(), getNextStateFromCurrentState((int)reviseDTO.getState()));
			nixReviseService.sendNotification(reviseDTO, (int) reviseDTO.getState(),loginDTO);
		}
	}

	private void updateFlowStateForCoLocation(BillDTO billDTO,LoginDTO loginDTO) throws Exception{
		CoLocationApplicationDTO coLocationApplicationDTO = coLocationApplicationService.getColocationApplicationByDemandNoteId(billDTO.getID());
		coLocationApplicationDTO.setState(getNextStateFromCurrentState(coLocationApplicationDTO.getState()));
		coLocationApplicationService.updateApplicaton(coLocationApplicationDTO,loginDTO);
		coLocationApplicationService.sendNotification(coLocationApplicationDTO,getNextStateFromCurrentState(coLocationApplicationDTO.getState()),loginDTO);
	}

	private void updateFlowStateForVPN(BillDTO billDTO,LoginDTO loginDTO) throws Exception {
		List<VPNApplicationLink> vpnApplicationLinks = globalService.getAllObjectListByCondition(
				VPNApplicationLink.class, new VPNApplicationLinkConditionBuilder()
						.Where()
						.demandNoteIdEquals(billDTO.getID())
						.getCondition()
		);
		VPNApplication vpnApplication=globalService.findByPK(VPNApplication.class,vpnApplicationLinks.get(0).getVpnApplicationId());
		Application application=globalService.findByPK(Application.class,vpnApplication.getApplicationId());
		vpnApplication.setClientId(application.getClientId());
		if (vpnApplicationLinks.isEmpty()) {
			throw new NoDataFoundException("No Link found with demand note ID : " + billDTO.getID());
		}
		int nextState = getNextStateFromCurrentState(vpnApplicationLinks.get(0).getLinkState().getState());
		vpnApplicationLinks.forEach(t -> {

			t.setLinkState(ApplicationState.getApplicationStateByStateId(nextState));
			globalService.update(t);
			try {
				vpnApplicationService.sendNotification(vpnApplication,t,loginDTO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private int getNextStateFromCurrentState(int state) {
		List<FlowState> flowStates = FlowRepository.getInstance().getNextStatesByCurrentState(state);
		return flowStates.stream()
				.findFirst()
				.orElseThrow(()->new NoDataFoundException("No next state found for current state id: "+ state))
				.getId();
	}

	protected void uploadDocument(PaymentDTO paymentDTO,FormFile doc,int fileTypeConstant, LoginDTO loginDTO,  DatabaseConnection databaseConnection) throws Exception {
		// TODO Auto-generated method stub
		FileService fileService = new FileService();

		int entityTypeID = EntityTypeConstant.VPN_LINK;
		long entityID = paymentDTO.getID();

		FileDTO fileDTO = new FileDTO();
		fileDTO.setDocOwner(paymentDTO.getClientID());
		fileDTO.setDocEntityTypeID(entityTypeID);
		fileDTO.setDocEntityID(entityID);
		fileDTO.setLastModificationTime(System.currentTimeMillis());
		fileDTO.setDocument(doc);

		fileDTO.createLocalFileUsingFormFile(fileTypeConstant);

		fileService.insert(fileDTO, databaseConnection);
	}
	public void skipMultipleBill(PaymentDTO paymentDTO, int moduleID, LoginDTO loginDTO, HttpServletRequest request ) throws Exception{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			System.currentTimeMillis();
			ArrayList<Long> recordIDs=  (ArrayList<Long>) Arrays.stream(paymentDTO.getBillIDs()).boxed().collect(Collectors.toList());
			ArrayList<BillDTO> billDTOs = (ArrayList<BillDTO>)billDAO.getBillDTOListByIDList(recordIDs, databaseConnection);

			boolean isValidPayment=true;
			String invalidityMsg="";

			long clientID=billDTOs.get(0).getClientID();
			for(BillDTO billDTO: billDTOs){
				if(clientID!=billDTO.getClientID()){
					isValidPayment=false;
					invalidityMsg="All bills should of same client";
					throw new RequestFailureException(invalidityMsg);
				}
				if(billDTO.getBillType() == BillDTO.PREPAID_BILL || billDTO.getBillType() == BillDTO.PREPAID_AND_POSTPAID_BILL)
				{

				}
				else
				{
					throw new RequestFailureException("Sorry you don't have permission to skip bill with ID " + billDTO.getID() + " as it is not a demand note");
				}
			}

			ClientDetailsDTO clientDetailsDTO=AllClientRepository.getInstance().getVpnClientByClientID(clientID, moduleID);
			logger.debug(clientDetailsDTO );
			logger.debug(clientID);

			if(clientDetailsDTO.getClientCategoryType() == ClientForm.CLIENT_TYPE_COMPANY &&
			(clientDetailsDTO.getRegistrantType()!=RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON || clientDetailsDTO.getRegistrantType()!=RegistrantTypeConstants.REGISTRANT_TYPE_MILITARY))
			{

			}
			else
			{
				isValidPayment=false;
				invalidityMsg="Client must be govt type";
			}

			if(!isValidPayment){
				new CommonActionStatusDTO().setErrorMessage(invalidityMsg, false, request);
			}

			paymentDAO.skipBillPayment(billDTOs, loginDTO, databaseConnection);
			databaseConnection.dbTransationEnd();

		} catch (Exception ex) {
			logger.fatal(ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				logger.fatal(ex2);
			}
			logger.fatal("Exception ", ex);
			throw ex;
		} finally {
			databaseConnection.dbClose();
		}
	}
	public void updatePayment(PaymentDTO paymentDTO, LoginDTO loginDTO) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			paymentDAO.updateNewPayment(paymentDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.fatal("fatal", ex);
		} finally {
			databaseConnection.dbClose();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		List<Long> IDList = new ArrayList<Long>();
		try {
			databaseConnection.dbOpen();
			/*int moduleID = 0;
			if (objects.length > 0) {
				moduleID = (Integer) objects[0];
			}*/

			IDList=paymentDAO.getAllPaymentID(loginDTO, moduleID, databaseConnection);
		} catch (Exception ex) {
			logger.debug("fatal", ex);
		} finally {
			databaseConnection.dbClose();
		}
		return IDList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		List<Long> IDList = new ArrayList<Long>();
		try {
			databaseConnection.dbOpen();
			IDList = paymentDAO.getPaymentIDListByCriteriaMap(loginDTO, moduleID ,searchCriteria, databaseConnection);

		} catch (Exception ex) {
			logger.debug("fatal", ex);
		} finally {
			databaseConnection.dbClose();
		}
		return IDList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {

		DatabaseConnection databaseConnection = new DatabaseConnection();
		List<PaymentDTO> statusHistories = new ArrayList<PaymentDTO>();

		try {

			databaseConnection.dbOpen();
			statusHistories = paymentDAO.getPaymentByIDList((List<Long>) recordIDs, databaseConnection);
		} catch (Exception ex) {
			logger.debug("fatal", ex);
		} finally {
			databaseConnection.dbClose();
		}
		return statusHistories;

	}

	@Transactional
	public void payBillFromAPI(PaymentDTO paymentDTO, LoginDTO loginDTO) throws Exception
	{
		paymentDAO.payBillFromAPI(paymentDTO, loginDTO);
	}
	public PaymentDTO getPaymentDTObyID(long paymentID) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		PaymentDTO paymentDTO = null;
		try {
			databaseConnection.dbOpen();
			paymentDTO= paymentDAO.getPaymentDTObyID(paymentID, databaseConnection);
		} catch (Exception ex) {
			logger.debug("fatal", ex);
		} finally {
			databaseConnection.dbClose();
		}
		return paymentDTO;
	}

	public void insertTeletalkPayment(PaymentDTO paymentDTO, LoginDTO loginDTO) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Not implemented");
	}



}
