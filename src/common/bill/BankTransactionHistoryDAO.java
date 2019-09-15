package common.bill;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import org.apache.log4j.Logger;

import common.EntityTypeConstant;
import common.RequestFailureException;

import static util.SqlGenerator.*;

import connection.DatabaseConnection;
import payment.api.Reconciliation;
public class BankTransactionHistoryDAO {
	Logger logger = Logger.getLogger(getClass());
	public void insertBankTransaction(BankTransactionHistory bankTransactionHistory,DatabaseConnection databaseConnection) throws Exception{
		insert(bankTransactionHistory, BankTransactionHistory.class, databaseConnection, true);
	}
	public List<Long> getPaidBillIDList(Map<String,String> criteriaMap,DatabaseConnection databaseConnection) throws Exception{
		String[] keys = 			{"fromDate","toDate","bankCode","branchCode"};
		String[] operators = 		{">="	   ,"<="	,"="	   ,"="};
		String[] dtoColumnNames = 	{"time"	   ,"time"	,"bankCode","branchCode"};
		
		
		//  select a.* from at_bank_transaction a join (select btBillID, max(btTime) btLastTime from at_bank_transaction group by btBillID) b on (a.btBillID = b.btBillID and a.btTime = b.btLastTime) ;
		// select btBillID from at_bank_transaction a where btTime = (select max(btTime) btLastTime from at_bank_transaction b where a.btBillID = b.btBillID)
		
		StringBuilder conditionBuilder = new StringBuilder();
		Class classObject = BankTransactionHistory.class;
		List<Object> valueList = new ArrayList<>();
		for(int i=0;i<keys.length;i++){
			String key = keys[i];
			String operator = operators[i];
			String dtoColumnName = dtoColumnNames[i];
			if(criteriaMap.containsKey(key)){
				String value = criteriaMap.get(key);
				String databaseTableColumnName = getColumnName(classObject, dtoColumnName);
				conditionBuilder.append(" and ").append(databaseTableColumnName).append(operator).append("?");
				valueList.add(value);
			}
		}
		
		String selectSql = "select btBillID from at_bank_transaction a where btTime = (select max(btTime) btLastTime from at_bank_transaction b where a.btBillID = b.btBillID) and btTxType = "+BankTransactionHistory.BANK_PAYMENT+"  "+conditionBuilder.toString();
		
		
		if(criteriaMap.containsKey("moduleID")){
			String moduleID = criteriaMap.get("moduleID");
			try{
				Integer.parseInt(moduleID);
			}catch(Exception ex){
				throw new RequestFailureException("Invalid serviceID format");
			}
			selectSql += "and exists(select * from at_bill c where  floor(("+getColumnName(BillDTO.class, "entityTypeID") +"/ "+EntityTypeConstant.MULTIPLIER2+") + 0.001) = "+moduleID+" and c.blID = a.btBillID)";
		}
		
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(selectSql);
		for(int i=0;i<valueList.size();i++){
			ps.setObject(i+1, valueList.get(i));
		}
		
		logger.debug("sql: "+ps);
		
		ResultSet rs = ps.executeQuery();
		List<Long> billIDList = new ArrayList<>();
		while(rs.next()){
			billIDList.add(rs.getLong(1));
		}
		
		return billIDList;
	} 
	
	
	
	
	public List<BankTransactionHistory> getPaidBankTransactionList(Map<String,String> criteriaMap,DatabaseConnection databaseConnection) throws Exception{
		String[] keys = 			{"fromDate","toDate","bankCode","branchCode","billID"};
		String[] operators = 		{">="	   ,"<="	,"="	   ,"="			,"="};
		String[] dtoColumnNames = 	{"time"	   ,"time"	,"bankCode","branchCode","billID"};
		
		
		//  select a.* from at_bank_transaction a join (select btBillID, max(btTime) btLastTime from at_bank_transaction group by btBillID) b on (a.btBillID = b.btBillID and a.btTime = b.btLastTime) ;
		// select btBillID from at_bank_transaction a where btTime = (select max(btTime) btLastTime from at_bank_transaction b where a.btBillID = b.btBillID)
		
		StringBuilder conditionBuilder = new StringBuilder();
		Class classObject = BankTransactionHistory.class;
		List<Object> valueList = new ArrayList<>();
		for(int i=0;i<keys.length;i++){
			String key = keys[i];
			String operator = operators[i];
			String dtoColumnName = dtoColumnNames[i];
			if(criteriaMap.containsKey(key)){
				String value = criteriaMap.get(key);
				String databaseTableColumnName = getColumnName(classObject, dtoColumnName);
				conditionBuilder.append(" and ").append(databaseTableColumnName).append(operator).append("?");
				valueList.add(value);
			}
		}
		
		
		
		

		String selectSql = "select * from at_bank_transaction a where btTime = (select max(btTime) btLastTime from at_bank_transaction b where a.btBillID = b.btBillID) and btTxType = "+BankTransactionHistory.BANK_PAYMENT+"  "+conditionBuilder.toString();
		
		
		if(criteriaMap.containsKey("moduleID")){
			String moduleID = criteriaMap.get("moduleID");
			try{
				Integer.parseInt(moduleID);
			}catch(Exception ex){
				throw new RequestFailureException("Invalid serviceID format");
			}
			selectSql += "and exists(select * from at_bill c where  floor(("+getColumnName(BillDTO.class, "entityTypeID") +"/ "+EntityTypeConstant.MULTIPLIER2+") + 0.001) = "+moduleID+" and c.blID = a.btBillID)";
		}
		
		
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(selectSql);
		for(int i=0;i<valueList.size();i++){
			ps.setObject(i+1, valueList.get(i));
		}
		
		logger.debug("sql: "+ps);
		
		ResultSet rs = ps.executeQuery();
		return (List<BankTransactionHistory>)getObjectListByResultSet(BankTransactionHistory.class, rs);
	} 
	
	
	
	public Reconciliation getReconsulation(Map<String,String> criteriaMap,DatabaseConnection databaseConnection) throws Exception{
		List<Long> billIDs = getPaidBillIDList(criteriaMap, databaseConnection);
		List<BillDTO> billDTOs = (List<BillDTO>)getObjectListByIDList(BillDTO.class, billIDs, databaseConnection);
		long totalAmount = 0;
		for(BillDTO billDTO: billDTOs){
			/*if(billDTO.getBillType()==BankTransactionHistory.BANK_PAYMENT){
				totalAmount += billDTO.getPaymentAmount();
			}else{
				totalAmount -= billDTO.getPaymentAmount();
			}*/
			totalAmount += billDTO.getNetPayableCeiled();
		}
		Reconciliation reconciliation = new Reconciliation();
		reconciliation.billCount = billDTOs.size();
		reconciliation.totalAmount = totalAmount;
		return reconciliation;
	}
	public BankTransactionHistory getBankTransactionHistoryByTransactionID(long txID,DatabaseConnection databaseConnection) throws Exception{
		List list = getObjectFullyPopulatedByString(BankTransactionHistory.class, databaseConnection, new Object[]{txID}, new String[]{"txNumber"});
		return list.isEmpty()?null:(BankTransactionHistory)list.get(0);
	}
	public BankTransactionHistory getBankTransactionHistoryByID(long ID,DatabaseConnection databaseConnection) throws Exception{
		return (BankTransactionHistory)getObjectByID(BankTransactionHistory.class, ID, databaseConnection);
	} 
	public BankTransactionHistory getLastBankTransactionHistoryByBillID(long billID, DatabaseConnection databaseConnection ) throws Exception{
		
		List<BankTransactionHistory> bankTransactionHistories = (List<BankTransactionHistory>)getObjectFullyPopulatedByString(BankTransactionHistory.class,
				databaseConnection, new Object[]{billID}, new String[]{"billID"}," order by "+getColumnName(BankTransactionHistory.class, "time")+" desc limit 1");
		
		return bankTransactionHistories.isEmpty()?null:(BankTransactionHistory)bankTransactionHistories.get(0);
	}
}
