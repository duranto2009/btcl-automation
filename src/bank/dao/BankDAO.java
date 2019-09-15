package bank.dao;

import static util.ModifiedSqlGenerator.*;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import bank.BankDTO;
import common.StringUtils;


public class BankDAO {
	
	public void insertBank(BankDTO bankDTO) throws Exception{
		insert(bankDTO, BankDTO.class, false);
	}
	
	public void updateBank(BankDTO bankDTO) throws Exception{
		updateEntity(bankDTO,BankDTO.class, false, false);
	}
	
	public void deleteBank(long bankID) throws Exception{
		deleteEntityByID(BankDTO.class, bankID);
	}
	
	public BankDTO getBankDTOByID(long bankID) throws Exception{
		return (BankDTO) getObjectFullyPopulatedByID(BankDTO.class, bankID);	
	}
	
	public List<BankDTO> getBankDTOListByPartialBankName(String partialBankName) throws Exception{
		String conditionString = " WHERE "+ getColumnName(BankDTO.class, "bankName") + " LIKE '%"+ StringUtils.trim(partialBankName)+"%' AND "+ getColumnName(BankDTO.class, "isDeleted") + " = 0";
		return (List<BankDTO>) getAllObjectList(BankDTO.class, conditionString);
		
	}

	public Collection getBankDTOList(Collection recordIDs) throws Exception {
		return (List<BankDTO>) getObjectListByIDList(BankDTO.class, recordIDs);
	}

	public Collection getBankIDList() throws Exception {
		String conditionString = " WHERE "+ getColumnName(BankDTO.class, "isDeleted") + " = 0";
		 return (List<Long>) getAllIDList(BankDTO.class, conditionString);
	}
	
	public Collection getBankIDListByCriteria(Hashtable criteriaMap) throws Exception {
		criteriaMap.remove("isDeleted");
		criteriaMap.put("isDeleted", "0");
		
		String[] keys = new String[]          {"bankName","isDeleted"};
		String[] operators = new String[]     {"like"    , "="};		
		String[] dtoColumnNames = new String[]{"bankName","isDeleted"};
		Class classObject = BankDTO.class;
		
		List<Long> IDList = (List<Long>)getIDListFromSearchCriteria(classObject, keys, operators, dtoColumnNames, criteriaMap, "");		
		return IDList;
	}
}
