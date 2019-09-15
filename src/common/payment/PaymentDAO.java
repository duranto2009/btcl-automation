package common.payment;

import annotation.Transactional;
import common.*;
import common.bill.BillConstants;
import common.bill.BillDAO;
import common.bill.BillDTO;
import common.bill.BillService;
import connection.DatabaseConnection;
import login.LoginDTO;
import org.apache.log4j.Logger;
import payment.api.PaymentApiException;
import payment.api.ResponseCode;
import permission.PermissionDAO;
import request.CommonRequestDTO;
import request.RequestActionStateRepository;
import request.RequestUtilDAO;
import util.*;

import java.util.*;
import java.util.stream.Collectors;

import static common.StringUtils.isBlank;
import static util.SqlGenerator.*;

public class PaymentDAO {
	Logger logger = Logger.getLogger(getClass());
	RequestUtilDAO requestUtilDAO = new RequestUtilDAO();
	BillService billService = ServiceDAOFactory.getService(BillService.class);
	
	public void insertNewPayment(PaymentDTO paymentDTO,DatabaseConnection databaseConnection) throws Exception{
		insert(paymentDTO, PaymentDTO.class, databaseConnection, false);
	}
	public PaymentDTO getPaymentDTObyID(long paymentID,DatabaseConnection databaseConnection) throws Exception{
		PaymentDTO paymentDTO = (PaymentDTO)getObjectByID(PaymentDTO.class, paymentID, databaseConnection);
		return paymentDTO;
	}
	
	/*public List<Long> getEntityIDListFromStatusAndEntityTypeID(int status,int entityTypeID,DatabaseConnection databaseConnection) throws Exception{
		Class classObject = PaymentDTO.class;
		String entityIDColumnName = getColumnName(classObject, "entityID");
		String entityTypeIDColumnName = getColumnName(classObject, "entityTypeID");
		String statusColumnName = getColumnName(classObject, "status");
		String sql = "select "+entityIDColumnName+" from where "+entityTypeIDColumnName+"="+entityTypeID+" and "+statusColumnName+"="+status;
		ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);
		List<Long> IDList = new ArrayList<Long>();
		while(rs.next()){
			IDList.add(rs.getLong(1));
		}
		return IDList;
	}*/
	
	
	public List<Long> getAllPaymentID(LoginDTO loginDTO,int moduleID, DatabaseConnection databaseConnection) throws Exception{
		
		String conditionString1 = " where "+getColumnName(BillDTO.class, "isDeleted")+" = 0 and floor(("+ getColumnName(BillDTO.class,"entityTypeID")+"/ "+EntityTypeConstant.MULTIPLIER2+") + 0.001) = "+moduleID;
		conditionString1+=" and ( "+ getColumnName(BillDTO.class, "paymentID")+" IS NOT NULL AND "+ getColumnName(BillDTO.class, "paymentID") +" > 0)"; 
		
		if(!loginDTO.getIsAdmin()){
			conditionString1 += " and "+getColumnName(BillDTO.class, "clientID")+" = "+loginDTO.getAccountID();
		}
		List<BillDTO> billDTOList = (List<BillDTO>) getAllObjectList (BillDTO.class, databaseConnection, conditionString1);
		List<Long> paymentIDList=new ArrayList<Long>();
		
		if(billDTOList.isEmpty()){
			return paymentIDList;
		}else{
			paymentIDList=billDTOList.stream().map(billDTO -> billDTO.getPaymentID()).collect(Collectors.toList());
		}
		String condtionString = " where "+getColumnName(PaymentDTO.class, "ID")+" IN "+StringUtils.getCommaSeparatedString(paymentIDList);
		return getAllIDList(PaymentDTO.class, databaseConnection,condtionString);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentDTO> getPaymentByIDList(List<Long> paymentIDList,DatabaseConnection databaseConnection) throws Exception{
		return (List<PaymentDTO>)getObjectListByIDList(PaymentDTO.class, paymentIDList, databaseConnection);
	}
	
	public List<Long> getPaymentIDListByCriteriaMap(LoginDTO loginDTO,int moduleID,Hashtable<String, String> criteria,DatabaseConnection databaseConnection) throws Exception{
		Class<PaymentDTO> classObject = PaymentDTO.class;
		//status is pIsVerified
		
		String toDateInputName = "paymentDateTo";
		if(!StringUtils.isBlank(criteria.get(toDateInputName))){
			criteria.put(toDateInputName, ""+DateUtils.getEndTimeOfDayByDateString(criteria.get(toDateInputName)));
		}
		
		
		String[] keys = 		  {"paymentGatewayType",	"billAmountFrom",	"billAmountTo", "paymentDateFrom" , "paymentDateTo","description" ,"paymentStatus"};
		String[] operators = 	  {"="			       ,	">="            ,	"<="    	   ,">="			  ,"<="            ,"like"        , "="};
		String[] dtoColumnNames = {"paymentGatewayType", 	"payableAmount" ,	"payableAmount", "paymentTime"    ,"paymentTime"   , "description","paymentStatus"};
		String fixedCondition = getColumnName(classObject, "isDeleted")+" = 0";
		
	
		String condtionString = " where "+getColumnName(BillDTO.class, "isDeleted")+" = 0 and floor(("+ getColumnName(BillDTO.class,"entityTypeID")+"/ "+EntityTypeConstant.MULTIPLIER2+") + 0.001) = "+moduleID;
		condtionString+=" and ( "+ getColumnName(BillDTO.class, "paymentID")+" IS NOT NULL AND "+ getColumnName(BillDTO.class, "paymentID") +" > 0)"; 

		if(!loginDTO.getIsAdmin()){
			condtionString += " and "+getColumnName(BillDTO.class, "clientID")+" = "+loginDTO.getAccountID();
		}else{
			String clientLoginName = criteria.get("clientName");
			List<Long> clientIDList = new ArrayList<Long>();
			if(!isBlank(clientLoginName)){
				Class firstClassObject = ClientDTO.class;
				String firstConditionString = " where "+getColumnName(firstClassObject, "loginName")+" like "+"'%"+SQLInjectionEscaper.escapeString(clientLoginName, true)+"%'";
				clientIDList = getAllIDList(firstClassObject, databaseConnection, firstConditionString);
				if(clientIDList.isEmpty()){
					clientIDList.add(new Long(-1));
				}
				condtionString += " and "+getColumnName(BillDTO.class, "clientID")+" in  "+StringUtils.getCommaSeparatedString(clientIDList);
			}
		}
		
		List<BillDTO> billDTOList = (List<BillDTO>) getAllObjectList (BillDTO.class, databaseConnection, condtionString);
		List<Long> paymentIDList=new ArrayList<Long>();
		
		if(billDTOList.isEmpty()){
			return paymentIDList;
		}else{
			paymentIDList=billDTOList.stream().map(billDTO -> billDTO.getPaymentID()).collect(Collectors.toList());
		}
		
		fixedCondition +=" AND "+getColumnName(PaymentDTO.class, "ID")+" IN "+StringUtils.getCommaSeparatedString(paymentIDList);
		return getIDListFromSearchCriteria(classObject, keys, operators, dtoColumnNames, criteria, fixedCondition, databaseConnection);
	}

	public List<BillDTO> getDemandNoteListByClientID(long clientID,DatabaseConnection databaseConnection) throws Exception{
		Class classObject = BillDTO.class;
		String clientIDColumnName = getColumnName(classObject, "clientID");
		String billTypeColumnName = getColumnName(classObject, "billType");
		int prepaidPaidBillType = 0;

		String conditionString = " where "+clientIDColumnName+" = "+clientID+" and "+billTypeColumnName+" = "+prepaidPaidBillType ;
		List<BillDTO> billDTOs = (List<BillDTO>)getAllObjectList(classObject, databaseConnection, conditionString);
		return billDTOs;
	}
	
	public void payBillByBillID(long billID, DatabaseConnection databaseConnection) throws Exception{
		Class classObject = BillDTO.class;

		BillDAO billDAO = new BillDAO();
		BillDTO billDTO = billDAO.getBillDTOByBillID(billID, databaseConnection);
		billDTO.payBill(databaseConnection);

		/*
		String IDColumnName = getColumnName(classObject, "ID");
		String paymentStatusColumnName = getColumnName(classObject, "paymentStatus");
		String tableName = getTableName(classObject);
		String sql = "update "+tableName+" set "+paymentStatusColumnName+" = 1 "+" where "+
					IDColumnName+" = "+billID+" and "+paymentStatusColumnName+" = 0";
		int numberOfAffectedRows = databaseConnection.getNewStatement().executeUpdate(sql);
		if(numberOfAffectedRows == 0){
			throw new Exception("");
		}*/
		
		
	}
	public void insertNewDemandNote(BillDTO billDTO,DatabaseConnection databaseConnection) throws Exception{
		insert(billDTO, BillDTO.class, databaseConnection, false);
	}
	public long getExpirationTimeForDemandNote() throws Exception{
		return 100000;
	}
	public void payBill(long billID,DatabaseConnection databaseConnection) throws Exception{
		BillDTO billDTO = (BillDTO)getObjectByID(BillDTO.class, billID, databaseConnection);
		Class classObject = Class.forName(billDTO.getClassName());
		Object object = classObject.getConstructor().newInstance();
		populateObjectFromOtherObject(object, BillDTO.class, billDTO, BillDTO.class);
		billDTO = (BillDTO)object;
		billDTO.payBill(databaseConnection);
		
	}

	@Transactional
	public void payBillFromAPIOld(PaymentDTO paymentDTO, LoginDTO loginDTO) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("payBillFromAPI");
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		
		insertNewPayment(paymentDTO, databaseConnection);

		//update bill payment time
		BillDAO billDAO= new BillDAO();
		CommonDAO commonDAO =new CommonDAO();

		long currentTime = System.currentTimeMillis();
		ArrayList<Long> recordIDs=  (ArrayList<Long>) Arrays.stream(paymentDTO.getBillIDs()).boxed().collect(Collectors.toList());
		ArrayList<BillDTO> billDTOs = (ArrayList<BillDTO>)billDAO.getBillDTOListByIDList(recordIDs, true, databaseConnection);
		for(BillDTO billDTO: billDTOs){
			CommonRequestDTO requestDTO = requestUtilDAO.getRequestDTOByReqID(billDTO.getReqID(), databaseConnection);
			//CommonRequestDTO 
			CommonRequestDTO newCommonRequestDTO = new CommonRequestDTO();
			SqlGenerator.populateObjectFromOtherObject(newCommonRequestDTO, CommonRequestDTO.class, requestDTO, CommonRequestDTO.class);
			logger.debug("commonRequestDTO " + newCommonRequestDTO);
			
			int paymentCompletedRequestTypeID = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(requestDTO.getRequestTypeID()).getNextPreferableActionTypeID();
			logger.debug("paymentCompletedRequestTypeID " + paymentCompletedRequestTypeID);
			
			newCommonRequestDTO.setClientID(paymentDTO.getClientID());
			newCommonRequestDTO.setDescription(paymentDTO.getDescription());
			newCommonRequestDTO.setModuleID(newCommonRequestDTO.getEntityTypeID() / EntityTypeConstant.MULTIPLIER2);
			newCommonRequestDTO.setRequestByAccountID(loginDTO.getIsAdmin() ? -loginDTO.getUserID() : loginDTO.getAccountID());
			newCommonRequestDTO.setRequestToAccountID(null);
			
			if(billDTO.getBillType() != BillConstants.MONTHLY_BILL)
			{
				newCommonRequestDTO.setCompletionStatus( 0 );
			}
			newCommonRequestDTO.setSourceRequestID(""+ billDTO.getReqID() );
			newCommonRequestDTO.setRequestTypeID(paymentCompletedRequestTypeID - 1);//114
		//	billDTO.setPaymentStatus(BillDTO.PAID_UNVERIFIED);
						
			logger.debug("commonRequestDTO " + newCommonRequestDTO);
			
			newCommonRequestDTO.setRootReqID(requestDTO.getRootReqID() == null ? requestDTO.getReqID() : requestDTO.getRootReqID());
			commonDAO.commonRequestSubmit(null, newCommonRequestDTO, requestDTO, null, null, loginDTO, databaseConnection, true);		
			
		//	billDTO.setPaymentID(paymentDTO.getID());
		//	billDTO.setLastModificationTime(currentTime);
			
		//	billDAO.updateBill(billDTO, databaseConnection);
		//	billDTO.payBill(databaseConnection);
			
			BillService billService = ServiceDAOFactory.getService(BillService.class);
			billService.unverifiedPayBillByBillID(billDTO.getID(), paymentDTO.getID());
			
		}
		logger.debug("payBillFromAPI done");
	}
	
	@Transactional
	public void payBillFromAPI(PaymentDTO paymentDTO, LoginDTO loginDTO) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("payBillFromAPI");
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		
		insertNewPayment(paymentDTO, databaseConnection);

		//update bill payment time
		BillDAO billDAO= new BillDAO();
		CommonDAO commonDAO =new CommonDAO();

		long currentTime = System.currentTimeMillis();
		ArrayList<Long> recordIDs=  (ArrayList<Long>) Arrays.stream(paymentDTO.getBillIDs()).boxed().collect(Collectors.toList());
		ArrayList<BillDTO> billDTOs = (ArrayList<BillDTO>)billDAO.getBillDTOListByIDList(recordIDs, true, databaseConnection);
		for(BillDTO billDTO: billDTOs){
			
			BillService billService = ServiceDAOFactory.getService(BillService.class);
			billService.unverifiedPayBillByBillID(billDTO.getID(), paymentDTO.getID());
			
			
		}
		logger.debug("payBillFromAPI done");
	}
	
	public void updateNewPayment(PaymentDTO paymentDTO, DatabaseConnection databaseConnection) {
		/*// TODO Auto-generated method stub
		ArrayList<PaymentDTO> paymentList = (ArrayList<PaymentDTO>)SqlGenerator.getAllObjectListFullyPopulated(PaymentDTO.class, databaseConnection, " where pBillID = " + paymentDTO.getBillID());
		PaymentDTO paymentDTO2 = paymentList.iterator().next();
		paymentDTO2.setDescription(paymentDTO.getDescription());
		paymentDTO2.setIs
		//SqlGenerator.updateEntityByPropertyList(paymentList.iterator().next(), PaymentDTO.class, databaseConnection, false, false, new String[]{"verifiedBy, pLastModificationTime, pIsVerified, pPaymentGatewayType, pDescription, pPaymentTime"})
		SqlGenerator.updateEntity(object, classObject, databaseConnection, allowNestedUpdate, applyOptimisticLocking, currentTimeOption);*/
	}
	
	public void updatePaymentDTO(PaymentDTO paymentDTO, DatabaseConnection databaseConnection) throws Exception {
		updateEntity(paymentDTO, PaymentDTO.class, databaseConnection, false,false);
	}
	public void deletePaymentByPaymentID(Long paymentID, long lastModificationTime, DatabaseConnection databaseConnection) throws Exception {
		int numberOfAffectedRows = deleteEntityByID(PaymentDTO.class, paymentID, lastModificationTime , databaseConnection);
		if(numberOfAffectedRows!=1){
			throw new PaymentApiException(ResponseCode.RESOURCE_NOT_FOUND,"Inconsistent data. BillDTO contains invalid payment ID");
		}
	}
	public void skipBillPayment(ArrayList<BillDTO> billDTOs, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
		// TODO Auto-generated method stub
		PermissionDAO permissionDAO = new PermissionDAO();
		for(BillDTO billDTO: billDTOs)
		{
			CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
			commonRequestDTO.setEntityID(billDTO.getEntityID());
			commonRequestDTO.setEntityTypeID(billDTO.getEntityTypeID());
			commonRequestDTO.setClientID(billDTO.getClientID());
			int moduleID = billDTO.getEntityTypeID() / EntityTypeConstant.MULTIPLIER2;
			int requestTypeID = moduleID * EntityTypeConstant.MULTIPLIER2 + 194;
			commonRequestDTO.setRequestTypeID(requestTypeID);
			
			CommonService commonService = new CommonService();
			
			CommonRequestDTO sourceRequestDTO = null;
			Set<CommonRequestDTO> bottomRequestDTOSet = commonService.getBottomRequestDTOsByEntity(commonRequestDTO);
			for(CommonRequestDTO bottomRequestDTO: bottomRequestDTOSet){
				if(bottomRequestDTO.getEntityID() == commonRequestDTO.getEntityID() && bottomRequestDTO.getEntityTypeID() == commonRequestDTO.getEntityTypeID()){
					sourceRequestDTO = bottomRequestDTO;
				}
				continue;			
			}
			
			if(sourceRequestDTO == null || sourceRequestDTO.getRequestTypeID() < 0)
			{
				throw new RequestFailureException("Demand note is not prepared to be skipped");
			}
			else
			{
				int prefix = (sourceRequestDTO.getRequestTypeID() % 1000) / 100;
				commonRequestDTO.setRequestTypeID(moduleID * ModuleConstants.MULTIPLIER +  (prefix * 100 + 94));
			}
			Set<Integer> actionSet = new RequestUtilDAO().getNextActionSetByStateAndUser(sourceRequestDTO.getRequestTypeID(), loginDTO, databaseConnection);
			if(actionSet.contains(commonRequestDTO.getRequestTypeID()))
			{
				CommonRequestDTO newCommonRequestDTO = new CommonRequestDTO();		
				SqlGenerator.populateObjectFromOtherObject(newCommonRequestDTO, CommonRequestDTO.class, sourceRequestDTO, CommonRequestDTO.class);
				newCommonRequestDTO.setSourceRequestID(""+sourceRequestDTO.getReqID());
				newCommonRequestDTO.setRequestTypeID(commonRequestDTO.getRequestTypeID());
				newCommonRequestDTO.setRequestToAccountID(commonRequestDTO.getRequestToAccountID());
				newCommonRequestDTO.setDescription(commonRequestDTO.getDescription());
				newCommonRequestDTO.setRequestByAccountID((loginDTO.getAccountID() > 0) ? loginDTO.getAccountID() : (-loginDTO.getUserID()));
				newCommonRequestDTO.setIP(loginDTO.getLoginSourceIP());
				newCommonRequestDTO.setModuleID(moduleID);
				newCommonRequestDTO.setParentReqID(null);
				newCommonRequestDTO.setRootEntityID(commonRequestDTO.getRootEntityID());
				newCommonRequestDTO.setExpireTime(commonRequestDTO.getExpireTime());
				newCommonRequestDTO.setDescription("");
				commonService.commonRequestSubmit(null, newCommonRequestDTO, sourceRequestDTO, null, null, loginDTO);
			}
			else
			{
				throw new RequestFailureException("Sorry you don't have permission to skip bill with ID " + billDTO.getID());
			}
		}
	}
}
