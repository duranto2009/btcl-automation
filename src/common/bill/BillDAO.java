package common.bill;

import accounting.BillPaymentDTOForLedger;
import accounting.BillPaymentForLedgerPrev;
import annotation.Transactional;
import coLocation.CoLocationConstants;
import common.*;
import connection.DatabaseConnection;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import nix.constants.NIXConstants;
import payment.api.BillDTOForBankPayment;
import request.CommonRequestDTO;
import request.StateDTO;
import request.StateRepository;
import util.*;
import vpn.VPNConstants;
import vpn.bill.BillConfigurationDTO;
import vpn.bill.VpnBillDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static common.StringUtils.isBlank;
import static util.SqlGenerator.*;
public class BillDAO {
	
	private static Logger logger = Logger.getLogger(BillDAO.class);
	
	Class<BillDTO> classObject = BillDTO.class;
	
	public long insertVpnBill(VpnBillDTO vpnBillDTO,DatabaseConnection databaseConnection) throws Exception{
		insert(vpnBillDTO, VpnBillDTO.class, databaseConnection, true);
		return vpnBillDTO.getVpnBillID();
	}
	
//	public long insertLliBill(LliBillDTO lliBillDTO,DatabaseConnection databaseConnection) throws Exception{
//		insert(lliBillDTO, LliBillDTO.class, databaseConnection, true);
//		return lliBillDTO.getLliBillID();
//	}
	
	public long insertBill(BillDTO billDTO,DatabaseConnection databaseConnection) throws Exception{
		insert(billDTO, databaseConnection);
		return billDTO.getID();
	}	
	public void updateBill(BillDTO BillDTO,DatabaseConnection databaseConnection) throws Exception{
		updateEntity(BillDTO, BillDTO.class, databaseConnection, false,false);
	}
	public int updateConcurrentBill(BillDTO billDTO,long currentTime,DatabaseConnection databaseConnection) throws Exception{
		return updateEntity(billDTO, BillDTO.class, databaseConnection, false, true, currentTime);
	}
	public void deleteBillDTOByBillDTO(BillDTO billDTO,DatabaseConnection databaseConnection) throws Exception{
		deleteEntity(billDTO, databaseConnection);
	}
	
	public void deleteDemanNote(long BillID,DatabaseConnection databaseConnection,long lastModificationTime) throws Exception{
		deleteEntityByID(BillDTO.class, BillID, lastModificationTime,databaseConnection);
	}
	public void markBillAsPaid(long billID,long prevLastModificationTime,long currentTime,DatabaseConnection databaseConnection) throws Exception{
		String updateSql = "update at_bill set blPaymentDate = ? ,blLastModificationTime = ?  where blID = ? and blLastModificationTime = ? ";
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(updateSql);
		ps.setObject(1, currentTime);
		ps.setObject(2, currentTime);
		ps.setObject(3, billID);
		ps.setObject(4, prevLastModificationTime);
		int numOfAffectedRows = ps.executeUpdate();
		if(numOfAffectedRows != 1){
			throw new RequestFailureException("Bill payment failed");
		}
	}
	public List<BillDTO> getBillDTOListByClientID(long clientID,DatabaseConnection databaseConnection) throws Exception{
		String clientIDColumnName = getColumnName(classObject, "clientID");
		String conditionString = " where "+clientIDColumnName+" = "+clientID;
		List<BillDTO> billDTOs = (List<BillDTO>)getAllObjectList(classObject, databaseConnection, conditionString);
		return billDTOs;
	}
	
	public List<BillDTO> getUnPaidBillDTOListByModuleIDAndClientID(int moduleID, long clientID,DatabaseConnection databaseConnection) throws Exception{
		String clientIDColumnName = getColumnName(classObject, "clientID");
		String paymentID=getColumnName(classObject, "paymentID");
		String entityTypeID=getColumnName(BillDTO.class,"entityTypeID");
		String isDeleted=getColumnName(classObject, "isDeleted"); 
		String conditionString = " where "+clientIDColumnName+" = "+clientID;
		conditionString += " and  floor(("+entityTypeID +"/ "+EntityTypeConstant.MULTIPLIER2+") + 0.001) = "+moduleID;
		conditionString += " and ("+paymentID +" = 0 or " + paymentID + " is null)" ;
		conditionString += " and "+isDeleted+" = 0";
			
		List<BillDTO> billDTOs = (List<BillDTO>)getAllObjectList(classObject, databaseConnection, conditionString);
		return billDTOs;
	}
	
	public BillDTO getBillDTOByBillID(long billID,DatabaseConnection databaseConnection) throws Exception{
		BillDTO billDTO = getBillDTOByBillID(billID, classObject, databaseConnection);
		if(billDTO == null||billDTO.isDeleted()){
			return null;
		}
		if(billDTO.getClassName()==null || billDTO.getClassName().trim().isEmpty()){
			return billDTO;
		}
		BillDTO subBillDTO = (BillDTO)Class.forName(billDTO.getClassName()).getConstructors()[0].newInstance();
		
		populateObjectFromOtherObject(subBillDTO, BillDTO.class, billDTO, BillDTO.class);
		return subBillDTO;
	}
	
	public VpnBillDTO getVpnBillDTOByVpnBillID( long vpnBillID ) throws Exception{
		
		VpnBillDTO vpnBillDTO = (VpnBillDTO)ModifiedSqlGenerator.getObjectFullyPopulatedByID( VpnBillDTO.class ,vpnBillID );
		return vpnBillDTO;
	}

	
	public VpnBillDTO getVpnBillDTOByBillID( long billID, DatabaseConnection databaseConnection) throws Exception{
		
		String conditionString = " where billvpnBillID = " + billID;
		ArrayList<VpnBillDTO> vpnBillDTOs = (ArrayList<VpnBillDTO>)SqlGenerator.getAllObjectListFullyPopulated(VpnBillDTO.class, databaseConnection, conditionString);
		
		if( vpnBillDTOs.size() == 1 ) return vpnBillDTOs.get(0);
		else throw new RequestFailureException( "Multiple or no vpn bill dto found with the given ID " + billID );
	}
	
//	public LliBillDTO getLliBillDTOByBillID( long billID, DatabaseConnection databaseConnection ) throws Exception{
//
//		String conditionString = " where billID = " + billID;
//		ArrayList<LliBillDTO> lliBillDTOs = (ArrayList<LliBillDTO>)SqlGenerator.getAllObjectListFullyPopulated( LliBillDTO.class, databaseConnection, conditionString );
//
//		if( lliBillDTOs.size() == 1 )
//			return lliBillDTOs.get(0);
//
//		else
//			throw new RequestFailureException( "Multiple or no lli bill dto found with the given ID " + billID );
//	}
	
	public BillDTO getBillByReqID(long reqID, DatabaseConnection databaseConnection) throws Exception {
		BillDTO billDTO = null;
		ArrayList<BillDTO> billList = (ArrayList<BillDTO>)getAllObjectListFullyPopulated(BillDTO.class, databaseConnection, " where blReqID = " + reqID + " and blIsDeleted = 0");
		if(billList != null && billList.size() > 0)
		{
			billDTO = billList.iterator().next();
		}

		return billDTO;
	}
	
	public BillDTO getChildBillDTOByParentBillID(long billID,Class<? extends BillDTO> classObject,DatabaseConnection databaseConnection) throws Exception{
		
		String conditionString = " where "+getForeignKeyColumnName(classObject)+" = "+billID;
		
		List<? extends BillDTO> billDTOList = getAllObjectList(classObject, databaseConnection, conditionString);
		
		return billDTOList.isEmpty()?null: billDTOList.get(0);
	}
	
	public BillDTO getBillDTOByBillID(long billID,Class<? extends BillDTO> classObject,DatabaseConnection databaseConnection ) throws Exception{
		return getObjectByID(BillDTO.class,classObject, billID, databaseConnection);
	}
	
	public List<BillDTO> getBillDTOListByIDList(List<Long> IDList,Class<? extends BillDTO> subClassObject,DatabaseConnection databaseConnection) throws Exception{
		Class<BillDTO> classObject = BillDTO.class;
		String conditionString =  getColumnName(classObject, "isDeleted")+" = 0 ";
		
		List<BillDTO> billDTOs = getObjectListByIDList(classObject,subClassObject, IDList, databaseConnection,conditionString);
		return billDTOs;
	}
	
	public List<BillDTO> getBillDTOListByIDList(List<Long> IDList, DatabaseConnection databaseConnection) throws Exception{
		return getBillDTOListByIDList(IDList, false, databaseConnection);
	}
	
	public List<BillDTO> getBillDTOListByIDList(List<Long> IDList, boolean considerSubBillDTO, DatabaseConnection databaseConnection) throws Exception{
		Class<BillDTO> classObject = BillDTO.class;
		String conditionString =  getColumnName(classObject, "isDeleted")+" = 0 ";
		
		List<BillDTO> billDTOs = getObjectListByIDList(classObject, IDList, databaseConnection,conditionString); 
		if(considerSubBillDTO == true)
		{
			return getSubBillDTOsByBillDTOs(billDTOs);
		}
		return billDTOs;
		
	}
	
	@Transactional
	public List<BillDTO> getBillDTOListByIDList2(List<Long> IDList, DatabaseConnection databaseConnection) throws Exception
	{
		return getBillDTOListByIDList2(IDList, DatabaseConnectionFactory.getCurrentDatabaseConnection());
	}
	
	
	public List<BillDTO> getSubBillDTOsByBillDTOs(List<BillDTO> billDTOs) throws Exception{		
		int index = 0;
		for(BillDTO billDTO: billDTOs)
		{
			if(billDTO == null||billDTO.isDeleted()){
				continue;
			}
			if(billDTO.getClassName()==null || billDTO.getClassName().trim().isEmpty()){
				continue;
			}

			BillDTO subBillDTO = (BillDTO)Class.forName(billDTO.getClassName()).getConstructors()[0].newInstance();		
			populateObjectFromOtherObject(subBillDTO, BillDTO.class, billDTO, BillDTO.class);
			
			billDTOs.set(index++, subBillDTO);
		}
		return billDTOs;
	}	
	public List<Long> getAllBillIDList(LoginDTO loginDTO,int moduleID,DatabaseConnection databaseConnection) throws Exception{
		Class<BillDTO> classObject = BillDTO.class;
		String conditionString = " where "+getColumnName(classObject, "isDeleted")+" = 0 and floor(("+ getColumnName(classObject,"entityTypeID")+"/ "+EntityTypeConstant.MULTIPLIER2+") + 0.001) = "+moduleID;
		if(!loginDTO.getIsAdmin()){
			conditionString += " and "+getColumnName(classObject, "clientID")+" = "+loginDTO.getAccountID();
		}
		//added by bony for filtering multiple bill;
		conditionString+=" and " +getColumnName(classObject,"isMultiple") + " != "+BillConstants.MULTIPLE_BILL_CHILD;
		logger.debug(conditionString);
		return getAllIDList(classObject, databaseConnection, conditionString);
	}
	public List<Long> getBillIDListBySearchCriteria(LoginDTO loginDTO,int moduleID,Hashtable<String, String> criteriaMap,DatabaseConnection databaseConnection) throws Exception{

		
		String clientLoginName = criteriaMap.get("clientName");
		List<Long> clientIDList = new ArrayList<Long>();
		if(!isBlank(clientLoginName)){
			Class<?> firstClassObject = ClientDTO.class;
			String firstConditionString = " where "+getColumnName(firstClassObject, "loginName")+" like "+"'%"+SQLInjectionEscaper.escapeString(clientLoginName, true)+"%'";
			clientIDList = getAllIDList(firstClassObject, databaseConnection, firstConditionString);
			if(clientIDList.isEmpty()){
				clientIDList.add(new Long(-1));
			}
		}
		
		if(!loginDTO.getIsAdmin()){
			criteriaMap.remove("clientID");
			criteriaMap.put("clientID", ""+loginDTO.getAccountID());
		}
		criteriaMap.remove("isDeleted");
		criteriaMap.put("isDeleted", "0");
		
		String generationToDateInputName = "generationTimeTo";
		if(!StringUtils.isBlank(criteriaMap.get(generationToDateInputName))){
			criteriaMap.put(generationToDateInputName, ""+DateUtils.getEndTimeOfDayByDateString(criteriaMap.get(generationToDateInputName)));
		}
		
		String lastPaymentToDateInputName = "lastPaymentDateTo";
		if(!StringUtils.isBlank(criteriaMap.get(lastPaymentToDateInputName))){
			criteriaMap.put(lastPaymentToDateInputName, ""+DateUtils.getEndTimeOfDayByDateString(criteriaMap.get(lastPaymentToDateInputName)));
		}
		String paymentToDateInputName = "paymentDateTo";
		if(!StringUtils.isBlank(criteriaMap.get(paymentToDateInputName))){
			criteriaMap.put(paymentToDateInputName, ""+DateUtils.getEndTimeOfDayByDateString(criteriaMap.get(paymentToDateInputName)));
		}
		
		
		String[] keys = new String[]          {"billID","generationTimeFrom","generationTimeTo","lastPaymentDateFrom","lastPayementDateTo","totalFrom"		,"totalTo"	,"billType"	,"isDeleted"	,"client"   ,"paymentStatus"	,"billActionType"};
		String[] operators = new String[]     {"="     ,">="                ,"<="              ,">="                 ,"<="                ,">="	      		,"<="	    ,"="       	,"="        	,"in"       ,"="          		,"="};
		String[] dtoColumnNames = new String[]{"ID"    ,"generationTime"    ,"generationTime"  ,"lastPaymentDate"    ,"lastPaymentDate"   ,"paymentDate"  	,"total"    ,"billType"		,"isDeleted","clientID" ,"paymentStatus"	,"className"};

		Class<?> classObject = BillDTO.class;
		String fixedCondition = "floor(("+ getColumnName(BillDTO.class,"entityTypeID")+"/ "+EntityTypeConstant.MULTIPLIER2+") + 0.001) = "+moduleID;
		
		if(!loginDTO.getIsAdmin()){
			fixedCondition += " and "+getColumnName(classObject, "clientID")+" = "+loginDTO.getAccountID();
		}else{
			if(!isBlank(clientLoginName) ){
				fixedCondition += " and "+getColumnName(classObject, "clientID")+" in  "+StringUtils.getCommaSeparatedString(clientIDList);
			}
		}
		fixedCondition+=" and " +getColumnName(classObject,"isMultiple") + " != "+BillConstants.MULTIPLE_BILL_CHILD;
		
		List<Long> IDList = (List<Long>)SqlGenerator.getIDListFromSearchCriteria(classObject, keys, operators, dtoColumnNames, criteriaMap, fixedCondition, databaseConnection);
		if (IDList != null) {
			Collections.sort(IDList);
			Collections.reverse(IDList);
		}
		return IDList;
	}
	public void extendBillLastDate(long billID,int days,long currentTime,DatabaseConnection databaseConnection) throws Exception{
		long extensionTime = days*24*60*60*1000;
		Class<?> classObject = BillDTO.class;
		String IDColumnName = getColumnName(classObject, "ID");
		String lastPaymentDateColumnName = getColumnName(classObject, "lastPaymentDate");
		String lastModificationTimeColumnName = getLastModificationTimeColumnName(classObject);
		String sql = "update "+getTableName(classObject)+" set "+lastPaymentDateColumnName+" = "+lastPaymentDateColumnName+"+"+extensionTime+
					" and "+lastModificationTimeColumnName+" = "+currentTime+" where "+IDColumnName+" = "+billID;
		logger.debug("sql:"+sql);
		int numOfAffectedRows = databaseConnection.getNewStatement().executeUpdate(sql);
		if(numOfAffectedRows!=1){
			throw new RequestFailureException("No billDTO found with ID "+billID);
		}
		updateSequencerTable(classObject, databaseConnection, currentTime);
	} 
	public void deleteBillDTOByIDList(List<Long> IDList,long currentTime,DatabaseConnection databaseConnection) throws Exception{
		Class<?> classObject = BillDTO.class;
		String sql = "update "+getTableName(classObject)+" set "+getColumnName(classObject, "isDeleted")+
					" = 1 , "+getLastModificationTimeColumnName(classObject)+" = "+currentTime+" where "+
				getColumnName(classObject, "ID")+" IN "+StringUtils.getCommaSeparatedString(IDList);
		logger.debug("sql:"+sql);
		databaseConnection.getNewStatement().executeUpdate(sql);
	}
	public void setNewExpiryDate(BillDTO billDTO,DatabaseConnection databaseConnection) throws Exception{
		updateEntityByPropertyList(billDTO, BillDTO.class, databaseConnection, false, false, new String[]{"lastPaymentDate"});
	}
	public List<BillDTO> getBillDTOsByEntityAndEntityType(long entityID, int entityTypeID, DatabaseConnection databaseConnection) throws Exception {
		return getBillDTOsByEntityAndEntityType(entityID, entityTypeID, databaseConnection, false);
	}
	public List<BillDTO> getBillDTOsByEntityAndEntityType(long entityID, int entityTypeID, DatabaseConnection databaseConnection, boolean pendingOnly) throws Exception {
		
		String conditionString = " where "+getColumnName(classObject, "entityID")+" = "+entityID;
		conditionString += " and " + getColumnName(classObject, "entityTypeID") + " = " + entityTypeID;
		conditionString += " and (" + getColumnName(classObject, "billType") + " = "+BillConstants.PREPAID+" or " + getColumnName(classObject, "billType") +  " = " + BillConstants.PREPAID_AND_POSTPAID + ")";
		if(pendingOnly)
		{
			conditionString += " and " + getColumnName(classObject, "paymentID") + " IS NULL";
		}
		List<BillDTO> billDTOs = (List<BillDTO>)getAllObjectList(classObject, databaseConnection, conditionString);
		return billDTOs;
	}
	public List<BillDTO> getBillDTOsByPaymentID(long paymentID, DatabaseConnection databaseConnection) throws Exception {
		String conditionString = " where "+getColumnName(classObject, "paymentID")+" = "+paymentID;
		List<BillDTO> billDTOs = getAllObjectList(classObject, databaseConnection, conditionString);
		return billDTOs;
	}

	/**
	 * @author Alam
	 * @param moduleID
	 * @param billTypeID
	 * @throws Exception 
	 */
	public static void deleteBillConfigurationData(int moduleID, int billTypeID, DatabaseConnection databaseConnection ) throws Exception {
		
		String sql = "update " + SqlGenerator.getTableName( BillConfigurationDTO.class)
				+ " set " + SqlGenerator.getColumnName( BillConfigurationDTO.class, "isDeleted" ) + " = 1"
				+ " where " + SqlGenerator.getColumnName( BillConfigurationDTO.class, "moduleID") + " = " + moduleID
				+ " and " + SqlGenerator.getColumnName( BillConfigurationDTO.class, "billTypeID" ) + " = " + billTypeID;
		
		SqlGenerator.runUpdateQuery( sql, databaseConnection );
	}

	public int cancelBillsByBillIDs(List<Long> billIDs, DatabaseConnection databaseConnection ) throws Exception {
		int count = 0;
		for( long billID: billIDs ) {
			count += cancelSingleBillByBillID(billID,  databaseConnection);
		}
		
		return count;
	}
	

	private int cancelSingleBillByBillID( long billID, DatabaseConnection databaseConnection ) {
		return cancelSingleBillByBillID(billID, BillConstants.MONTHLY_BILL, databaseConnection);
	}
	private int cancelSingleBillByBillID( long billID, int billType, DatabaseConnection databaseConnection ) {
		long currentTime = System.currentTimeMillis();
		try {
			BillDTO billDTO = null;
			if(billType == -1)
			{
				billDTO = SqlGenerator.getObjectByID( BillDTO.class, billID, databaseConnection );
			}
			else
			{
				String conditionString = " where " + getColumnName(BillDTO.class, "billType") + " = " + billType + " and " + getPrimaryKeyColumnName(BillDTO.class) + " = " + billID;
				List<BillDTO> billList = SqlGenerator.getAllObjectListFullyPopulated(BillDTO.class, databaseConnection, conditionString);
				if(billList.size() > 0)
				{
					billDTO = billList.get(0);
				}
			}
			
			
			if(billDTO == null)
				return 0;
			if(!isSafeToDelete( billDTO, databaseConnection ))
				return 0;
			
			Class classObject = EntityTypeConstant.entityBillClassMap.get(billDTO.getEntityTypeID());
			billDTO.setDeleted(true);
			billDTO.setLastModificationTime(currentTime);
			updateEntityByPropertyList(billDTO, classObject, databaseConnection, true, false, new String[]{"lastModificationTime"});
			
			if(billDTO.getBillType() == BillConstants.MONTHLY_BILL)//Advanced Monthly bill will be skipped
			{
				SqlGenerator.deleteEntityByID(CommonRequestDTO.class, billDTO.getReqID(), currentTime, databaseConnection);
			}
			EntityDTO entityDTO = new CommonDAO().getEntityDTOByEntityIDAndEntityTypeID(billDTO.getEntityTypeID(), billDTO.getEntityID(), databaseConnection);
			double currentBalance = entityDTO.getBalance();
			currentBalance += billDTO.getAdjustmentAmount();
			entityDTO.setBalance(currentBalance);
			entityDTO.setLastModificationTime(currentTime);
			updateEntity(entityDTO, classObject, databaseConnection, true, false);	
			
			return 1;
		}
		catch( Exception e ) {			
			logger.debug("Exception ", e);			
		}
		return 0;
	}


	
	public void cancelBillByTimeRange( long from, long to, DatabaseConnection databaseConnection ) throws Exception {
		String generationTimeColumnName = SqlGenerator.getColumnName( BillDTO.class,  "generationTime" );
		String conditionString = " where " + generationTimeColumnName + " >= " + from + " and " + generationTimeColumnName + " <= " + to; 
		List<Long> billIDList = SqlGenerator.getAllIDList( BillDTO.class, databaseConnection, conditionString );		
		cancelBillsByBillIDs( billIDList, databaseConnection );
	}
	
	public boolean isSafeToDelete(BillDTO billDTO, DatabaseConnection databaseConnection) throws Exception {

		logger.debug( "is bill safe to delete called" );
		boolean isSafeToDelete = true;

		if( billDTO.getPaymentID() == null || billDTO.getPaymentID() <= 0 ) 
		{
			return false;
		}
		
		if(billDTO.getBillType() == BillConstants.MONTHLY_BILL)
		{
			
		}
		else if(billDTO.getBillType() == BillConstants.MONTHLY_BILL_ADVANCED)
		{
			
		}	
		else
		{
			return false;
		}
		
		EntityDTO entity = new CommonDAO().getEntityDTOByEntityIDAndEntityTypeID(billDTO.getEntityTypeID(), billDTO.getEntityID(), databaseConnection);
		StateDTO currentState = StateRepository.getInstance().getStateDTOByStateID( entity.getCurrentStatus() );
		if( currentState.getActivationStatus() != EntityTypeConstant.STATUS_ACTIVE ) {//these bills need to rollback instead of cancel
			return false;			
		}

		return isSafeToDelete;
	}
	
	public List<BillDTO> getAdvancedPaidBills(long entityID, int entityTypeID, DatabaseConnection databaseConnection) throws Exception	
	{
		String conditionString = " where ";
		conditionString += getColumnName(BillDTO.class,  "entityID") + "= "+entityID;
		conditionString += " and " + getColumnName(BillDTO.class, "entityTypeID") + "="+entityTypeID;
		conditionString += " and " + getColumnName(BillDTO.class,"paymentStatus") + " is not null && " + getColumnName(BillDTO.class,"paymentStatus") + " > 0";
		conditionString += " and " + getColumnName(BillDTO.class,"isDeleted") + " = 0";
		conditionString += " and " + getColumnName(BillDTO.class,"billType") + " = " + BillConstants.MONTHLY_BILL_ADVANCED;
		
		List<BillDTO> billList = SqlGenerator.getAllObjectList(BillDTO.class, databaseConnection, conditionString);
		return billList;
	}
	
	public void insertBillCancelRequest(int entityTypeID, int entityID, LoginDTO loginDTO, DatabaseConnection databaseConnection)
	{
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
	}
	
	public <T> T getExtendedBillByRootReqID( Class<T> classObject, long rootRequestID , DatabaseConnection databaseConnection ) throws Exception{
		String conditionString = " where " + SqlGenerator.getColumnName(CommonRequestDTO.class, "requestTypeID") + " like '%11' and " + SqlGenerator.getColumnName(CommonRequestDTO.class, "rootReqID") + " = " + rootRequestID + " order by " + SqlGenerator.getPrimaryKeyColumnName(CommonRequestDTO.class) + " desc limit 1";
		List<CommonRequestDTO> billRequestDTOList = SqlGenerator.getAllUndeletedObjectList(CommonRequestDTO.class, databaseConnection, conditionString);
		if(billRequestDTOList == null || billRequestDTOList.size() == 0)
		{
			return null;
		}
		CommonRequestDTO billRequestDTO = billRequestDTOList.get(0);
		 
		BillDTO billDTO = new BillDAO().getBillByReqID(billRequestDTO.getReqID(), databaseConnection );
		
		conditionString = " where " + SqlGenerator.getForeignKeyColumnName( classObject ) + " = " + billDTO.getID();
		
		List<T> bills = (List<T>) SqlGenerator.getAllObjectListFullyPopulated( classObject, databaseConnection, conditionString );
		
		T extendedBill = null;
		
		if( !bills.isEmpty() )
			extendedBill = bills.get(0);
		
		return extendedBill;
	}
	
	public List<BillDTO> getBillsByRootReqID( long rootReqID, DatabaseConnection databaseConnection ) throws Exception{
		
		List<CommonRequestDTO> demnadNoteGenerationReqst = (ArrayList<CommonRequestDTO>)SqlGenerator.getAllObjectList(CommonRequestDTO.class, databaseConnection,
				" where (" + SqlGenerator.getColumnName( CommonRequestDTO.class, "rootReqID" ) + " = " + rootReqID
				+ " or " + SqlGenerator.getPrimaryKeyColumnName(CommonRequestDTO.class) + " = " + rootReqID + ")"
				+ " and " + SqlGenerator.getColumnName( CommonRequestDTO.class, "requestTypeID" ) + " like '%11' "
				+ " and " + SqlGenerator.getColumnName( CommonRequestDTO.class, "isDeleted" ) + " = 0" 
				+ " order by " + SqlGenerator.getColumnName( CommonRequestDTO.class, "reqID" ) + " desc limit 1" );
		
		if(demnadNoteGenerationReqst == null || demnadNoteGenerationReqst.size() == 0)
		{
			return new ArrayList<BillDTO>();
		}
		long billReqstID = demnadNoteGenerationReqst.get(0).getReqID();
		
		List<BillDTO> billList = (ArrayList<BillDTO>)SqlGenerator.getAllObjectList(BillDTO.class, databaseConnection,
					" where " + SqlGenerator.getColumnName( BillDTO.class, "reqID" ) + " = " + billReqstID
					+ " and " + SqlGenerator.getColumnName( BillDTO.class, "isDeleted" ) + " = 0 ");
		
		return billList; 
	}

	public List<DateRange> getDateRangesOfAlreadyCreatedMonthlyBills(Long clientID, int entityTypeID, long queryFromDate, long queryToDate) throws Exception {
		SqlPair sqlPair = new BillDTOConditionBuilder()
		.selectActivationTimeFrom()
		.selectActivationTimeTo()
		.fromBillDTO()
		.Where()
		.clientIDEquals(clientID)
		.entityTypeIDEquals(entityTypeID)
		.activationTimeFromLessThanEquals(queryToDate)
		.activationTimeToGreaterThanEquals(queryFromDate)
		.isDeleted(false)
		.orderByactivationTimeFromAsc()
		.getSqlPair();

		ResultSet rs = ModifiedSqlGenerator.getResultSetBySqlPair(sqlPair);
		return getDateRangeListByResultSet(rs);
	}

	private List<DateRange> getDateRangeListByResultSet(ResultSet rs) throws Exception {
		List<DateRange> dateRanges = new ArrayList<>();
		while(rs.next()) {
			DateRange dateRange = new DateRange();
			dateRange.fromDate = rs.getLong(1);
			dateRange.toDate = rs.getLong(2);
			dateRanges.add(dateRange);
		}
		return dateRanges;
	}


    public List<BillDTO> getUnPaidBillDTOListByEntityTypeIDAndClientIDAndBillType(int entityType, long clientId, int billType) throws Exception {

		List<BillDTO> unpaidBills = ModifiedSqlGenerator.getAllObjectList(BillDTO.class,
				" Where " +
						getColumnName(BillDTO.class, "entityTypeID") +
						" = " +
						entityType +
						" and " +
						getColumnName(BillDTO.class, "clientID") +
						" = " +
						clientId +
						" and " +
						getColumnName(BillDTO.class, "billType") +
						" = " +
						billType
				);

		return unpaidBills;
    }

    List<BillPaymentDTOForLedger> getSubscriberLedgerReport(long clientID, Long fromDate, Long toDate, int moduleId, DatabaseConnection databaseConnection) throws Exception {


    	StringBuilder sb = new StringBuilder();
    	int entityTypeId =( moduleId == ModuleConstants.Module_ID_LLI ?
				LLIConnectionConstants.ENTITY_TYPE :
						moduleId == ModuleConstants.Module_ID_VPN ?
								VPNConstants.ENTITY_TYPE :
								moduleId == ModuleConstants.Module_ID_NIX ?
										NIXConstants.ENTITY_TYPE :
										moduleId == ModuleConstants.Module_ID_COLOCATION ?
												CoLocationConstants.ENTITY_TYPE : 0);
    	if(entityTypeId == 0) {
    		throw new RequestFailureException("Invalid Entity Type ID, Please Select Appropriate Module");
		}


		sb.append("SELECT  b.blID,")
				.append(" b.blType,")
				.append(" b.blTotalPayable,")
				.append(" b.blVAT,")
				.append(" b.blNetPayable,")
				.append(" b.blGenerationTime,")
				.append(" p.pBankName,")
				.append(" p.pBankBranchName,")
				.append(" p.pPaymentTime,")
				.append(" p.pPaidAmount,")
				.append(" b.blPaymentID")
				.append(" from at_bill b")
				.append(" LEFT JOIN at_payment p")
				.append(" ON b.blPaymentID = p.pID")
				.append(" WHERE b.blClientID = ?")
		        .append(" AND b.blIsMultiple <> 1")
                .append(" AND b.blEntityTypeID = " + entityTypeId);


		if(fromDate != null) {
    		sb.append(" AND b.blGenerationTime >= ?");
		}
    	if(toDate != null) {
    		sb.append(" AND b.blGenerationTime <= ?");
		}
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sb.toString());

    	ps.setLong(1, clientID);
    	if(fromDate!=null && toDate != null){
    		ps.setLong(2, fromDate);
    		ps.setLong(3, toDate);
		}else if(fromDate == null && toDate != null) {
    		ps.setLong(2, toDate);
		}else if(fromDate != null){
    		ps.setLong(2, fromDate);
		}
		logger.info(ps);
    	ResultSet rs = ps.executeQuery();
    	List<BillPaymentDTOForLedger> list = new ArrayList<>();
    	int serial = 1;

    	double btclBl = 0, vatBl = 0, totalBl = 0;
    	while (rs.next()) {
			String invoiceId = String.valueOf(rs.getLong(1));
			String billType = BillDTO.getBillTypeStr(rs.getInt(2));

    		String btclAmount = String.valueOf(rs.getDouble(3));
    		String vat = String.valueOf(rs.getDouble(4));
    		String totalAmount = String.valueOf(rs.getDouble(5));

    		long generationTime = rs.getLong(6);
    		String generationTimeFirst = TimeConverter.getDateTimeStringByMillisecAndDateFormat(generationTime, "dd/MM/yyyy");
    		String generationTimeSecond = TimeConverter.getDateTimeStringByMillisecAndDateFormat(generationTime, "MMM-yyyy");

			String bank = rs.getString(7);
			if(rs.wasNull()) {
				bank = "N/A";
			}

			String branch = rs.getString(8);
			if(rs.wasNull()) {
				branch = "N/A";
			}
			long paymentTime = rs.getLong(9);
			if(rs.wasNull()) {
				paymentTime = 0;
			}
			double paidAmount = rs.getDouble(10);
			if(rs.wasNull()) {
				paidAmount = 0;
			}

			long paymentId = rs.getLong(11);
			if(rs.wasNull()){
				paymentId = 0;
			}


			if(paymentId!=0) {
				// 2 entry insert dr and cr
				String btclInitialAmount = String.valueOf(btclBl);
				String vatInitialAmount = String.valueOf(vatBl);
				String totalInitialAmount = String.valueOf(totalBl);

				btclBl += Double.valueOf(btclAmount);
				vatBl += Double.valueOf(vat);
				totalBl += Double.valueOf(totalAmount);

				String drOrCr = "-";
				if(totalBl > 0) {
					drOrCr = "Dr";
				}else if(totalBl < 0) {
					drOrCr = "Cr";
				}

				BillPaymentDTOForLedger dto1 = BillPaymentDTOForLedger.builder()
						.generationOrPaymentDate(generationTimeFirst)
						.invoiceId(invoiceId)
						.billTypeMonthYear(billType + " " + generationTimeSecond)
						.btclAmountDr(btclAmount)
						.vatDr(vat)
						.totalAmountDr(totalAmount)
						.btclAmountCr("-")
						.btclAmountCr("-")
						.btclAmountCr("-")
						.btclAmountBl(String.valueOf(btclBl))
						.vatBl(String.valueOf(vatBl))
						.totalAmountBl(String.valueOf(totalBl))
						.drOrCr(drOrCr)
						.bankName("-")
						.branchName("-")
						.build();

				btclBl = Double.valueOf(btclInitialAmount);
				vatBl = Double.valueOf(vatInitialAmount);
				totalBl = Double.valueOf(totalInitialAmount);

				drOrCr = "-";
				if(totalBl > 0) {
					drOrCr = "Dr";
				}else if(totalBl < 0) {
					drOrCr = "Cr";
				}

				String paymentTimeStr = TimeConverter.getDateTimeStringByMillisecAndDateFormat(paymentTime, "dd/MM/yyyy");
				BillPaymentDTOForLedger dto2 = BillPaymentDTOForLedger.builder()
						.generationOrPaymentDate(paymentTimeStr)
						.invoiceId(invoiceId)
						.billTypeMonthYear(billType + " " + generationTimeSecond)
						.btclAmountDr("-")
						.vatDr("-")
						.totalAmountDr("-")
						.btclAmountCr(btclAmount)
						.vatCr(vat)
						.totalAmountCr(totalAmount)
						.btclAmountBl(btclInitialAmount)
						.vatBl(vatInitialAmount)
						.totalAmountBl(totalInitialAmount)
						.drOrCr(drOrCr)
						.bankName(bank)
						.branchName(branch).build();

				list.add(dto1);
				list.add(dto2);


			}else{
				// 1 entry insert dr.
				btclBl += (Double.valueOf(btclAmount));
				vatBl += (Double.valueOf(vat));
				totalBl += (Double.valueOf(totalAmount)); // - 0

				String drOrCr = "-";
				if(totalBl > 0) {
					drOrCr = "Dr";
				}else if(totalBl < 0) {
					drOrCr = "Cr";
				}
				BillPaymentDTOForLedger dto = BillPaymentDTOForLedger.builder()
						.generationOrPaymentDate(generationTimeFirst)
						.invoiceId(invoiceId)
						.billTypeMonthYear(billType + " " + generationTimeSecond)
						.btclAmountDr(btclAmount)
						.vatDr(vat)
						.totalAmountDr(totalAmount)
						.btclAmountCr("-")
						.vatCr("-")
						.totalAmountCr("-")
						.btclAmountBl(String.valueOf(btclBl))
						.vatBl(String.valueOf(vatBl))
						.totalAmountBl(String.valueOf(totalBl))
						.drOrCr(drOrCr)
						.bankName("-")
						.branchName("-").build();

				list.add(dto);
			}



		}

    	// sort



//		list = list.stream()
//				.sorted((t1, t2)-> {
//					try {
//						long date1 = TimeConverter.getTimeInMilliSec(t1.getGenerationOrPaymentDate(), "dd/MM/yyyy");
//						long date2 = TimeConverter.getTimeInMilliSec(t2.getGenerationOrPaymentDate(), "dd/MM/yyyy");
//						return Long.compare(date1, date2);
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//					return Long.compare(0, 0);
//				}).collect(Collectors.toList());

    	return list;

	}


	List<BillPaymentForLedgerPrev> getSubscriberLedgerReportPrev(long clientID, Long fromDate, Long toDate, DatabaseConnection databaseConnection) throws Exception {


		StringBuilder sb = new StringBuilder();
		sb.append("SELECT b.blMonth,")
				.append(" b.blYear,")
				.append(" b.blTotalPayable,")
				.append(" b.blVAT,")
				.append(" b.blNetPayable,")
				.append(" b.blPaymentStatus,")
				.append( "p.pPaidAmount,")
				.append(" p.pPaymentGatewayType,") // Bank // Teletalk // SSL
				.append(" p.pPaymentType,") // Check // Order // Receipt
				.append(" p.pBankName,")
				.append(" p.pBankBranchName,")
				.append(" p.pPaymentTime,")
				.append(" b.blID")
				.append(" from at_bill b")
				.append(" LEFT JOIN at_payment p")
				.append(" ON b.blPaymentID = p.pID")
				.append(" WHERE b.blClientID = ?");

		if(fromDate != null) {
			sb.append(" AND b.blGenerationTime >= ?");
		}
		if(toDate != null) {
			sb.append(" AND b.blGenerationTime <= ?");
		}
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sb.toString());

		ps.setLong(1, clientID);
		if(fromDate!=null && toDate != null){
			ps.setLong(2, fromDate);
			ps.setLong(3, toDate);
		}else if(fromDate == null && toDate != null) {
			ps.setLong(2, toDate);
		}else if(fromDate != null){
			ps.setLong(2, fromDate);
		}
		logger.info(ps);
		ResultSet rs = ps.executeQuery();
		List<BillPaymentForLedgerPrev> list = new ArrayList<>();
		int serial = 1;
		while (rs.next()) {
			String month = Month.getMonthNameById(rs.getInt(1));
			String year = 	String.valueOf(rs.getInt(2));
			String btclAmount = String.valueOf(rs.getDouble(3));
			String vat = String.valueOf(rs.getDouble(4));
			String totalAmount = String.valueOf(rs.getDouble(5));

			double paidAmount = rs.getDouble(7);
			if(rs.wasNull()){
				paidAmount = 0;
			}
			String bank = rs.getString(10);
			if(rs.wasNull()) {
				bank = "N/A";
			}

			String branch = rs.getString(11);
			if(rs.wasNull()) {
				branch = "N/A";
			}
			long paymentTime = rs.getLong(12);
			if(rs.wasNull()) {
				paymentTime = 0;
			}

			String invoiceId = String.valueOf(rs.getLong(13));
			BillPaymentForLedgerPrev dto = BillPaymentForLedgerPrev.builder()
					.serial(String.valueOf(serial++))
					.monthYear(month + " " + year)
					.btclAmount(btclAmount)
					.vat(vat)
					.totalAmount(totalAmount)
					.bankName(bank)
					.branchName(branch)
					.paymentDate(paymentTime==0?"N/A":TimeConverter.getDateTimeStringByMillisecAndDateFormat(paymentTime, "dd/MM/yyyy"))
					.paidAmount( paidAmount==0 ? "N/A" : String.valueOf(paidAmount))
					.invoiceId(invoiceId)
					.build();

//			jsonObject.addProperty("paymentStatus", rs.getInt(6));

//			int gatewayType = rs.getInt(8);
//			if(rs.wasNull()) {
//				gatewayType = 0;
//			}
//			int paymentType = rs.getInt(9);
//			if(rs.wasNull()) {
//				paymentType = 0;
//			}

			list.add(dto);


		}

		return list;

	}
}

