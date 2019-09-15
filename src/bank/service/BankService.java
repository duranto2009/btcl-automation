package bank.service;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import annotation.Transactional;
import bank.BankDTO;
import bank.dao.BankDAO;
import login.LoginDTO;
import util.NavigationService;

public class BankService implements NavigationService{
	
	BankDAO bankDAO = new BankDAO(); 
	
	@Transactional
	public void insertBank(BankDTO bankDTO, LoginDTO loginDTO) throws Exception{
		bankDAO.insertBank(bankDTO);
	}
	@Transactional
	public void deleteBank(long bankID,LoginDTO loginDTO) throws Exception{
		bankDAO.deleteBank(bankID);
	}
	@Transactional
	public BankDTO editBank(BankDTO bankDTO, LoginDTO loginDTO) throws Exception{
		bankDAO.updateBank(bankDTO);
		return bankDTO;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public BankDTO getBankDTOByID(long bankID) throws Exception{
		return bankDAO.getBankDTOByID(bankID);
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<BankDTO> getBankDTOListByBankPartialName(String partialBankName) throws Exception{
		return bankDAO.getBankDTOListByPartialBankName(partialBankName);
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	@Override
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		return bankDAO.getBankIDList();
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	@Override
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
			throws Exception {
		return bankDAO.getBankIDListByCriteria(searchCriteria);
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		return bankDAO.getBankDTOList(recordIDs);
	}

}
