package payOrder.dao;

import static util.ModifiedSqlGenerator.*;
import static util.SqlGenerator.*;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.input.ClassLoaderObjectInputStream;

import payOrder.PayOrderDTO;
import util.ModifiedSqlGenerator;
import util.SqlGenerator;
import common.StringUtils;
import connection.DatabaseConnection;


public class PayOrderDAO {
	
	public void insertPayOrder(PayOrderDTO payOrderDTO) throws Exception{
		insert(payOrderDTO, PayOrderDTO.class, false);
	}
	
	public void updatePayOrder(PayOrderDTO payOrderDTO) throws Exception{
		updateEntity(payOrderDTO,PayOrderDTO.class, false, false);
	}
	
	public void deletePayOrder(long payOrderID) throws Exception{
		deleteEntityByID(PayOrderDTO.class, payOrderID);
	}
	
	public PayOrderDTO getPayOrderDTOByID(long payOrderID) throws Exception{
		return (PayOrderDTO) getObjectFullyPopulatedByID(PayOrderDTO.class, payOrderID);	
	}

	public Collection getPayOrderDTOList(Collection recordIDs) throws Exception {
		return (List<PayOrderDTO>) getObjectListByIDList(PayOrderDTO.class, recordIDs);
	}

	public Collection getPayOrderIDList() throws Exception {
		String conditionString = " WHERE "+ ModifiedSqlGenerator.getColumnName(PayOrderDTO.class, "isDeleted") + " = 0";
		 return (List<Long>) getAllIDList(PayOrderDTO.class, conditionString);
	}
	
	public Collection getPayOrderIDListByCriteria(Hashtable criteriaMap) throws Exception {
		criteriaMap.remove("isDeleted");
		criteriaMap.put("isDeleted", "0");
		
		String[] keys = new String[]          {"payDraftBankID","payDraftNumber","payDraftClientID","isDeleted"};
		String[] operators = new String[]     {"="             ,"="             ,"="               ,"="        };		
		String[] dtoColumnNames = new String[]{"payDraftBankID","payDraftNumber","payDraftClientID","isDeleted"};
		Class classObject = PayOrderDTO.class;
		
		List<Long> IDList = (List<Long>)getIDListFromSearchCriteria(classObject, keys, operators, dtoColumnNames, criteriaMap, "");		
		return IDList;
	}

	public List<PayOrderDTO> getPayOrderListByPayorderTypeAndPartialMatch(int payDraftType,
			String partialPayDraftNumber) throws Exception {
		Class classObject = PayOrderDTO.class;
		String conditionString = " WHERE "+ModifiedSqlGenerator.getColumnName(classObject, "payDraftType") + " = " + payDraftType + " AND "+ ModifiedSqlGenerator.getColumnName(classObject, "payDraftNumber") 
		+ " LIKE '%"+StringUtils.trim(partialPayDraftNumber) +"%' "+ " AND "+ ModifiedSqlGenerator.getColumnName(classObject, "isDeleted") + " = " + 0;
		return (List<PayOrderDTO>) getAllObjectList(classObject, conditionString);
	}

	public PayOrderDTO getPayOrderDTOByPayDraftNumber(String payDraftNumber) throws Exception {
		/*Class classObject = PayOrderDTO.class;
		String conditionString = " WHERE " +getColumnName(classObject, "payDraftNumber") 
		+ " = '"+StringUtils.trim(payDraftNumber) +"' "+ " AND "+ getColumnName(classObject, "isDeleted") + " = " + 0;*/
		return null;
	}

	public void updatePayOrder(PayOrderDTO payOrderDTO, DatabaseConnection databaseConnection) throws Exception {
		SqlGenerator.updateEntity(payOrderDTO,PayOrderDTO.class,databaseConnection, false, false);
		
	}
}
