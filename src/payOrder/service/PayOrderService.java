package payOrder.service;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import annotation.Transactional;
import connection.DatabaseConnection;
import payOrder.PayOrderDTO;
import payOrder.dao.PayOrderDAO;
import login.LoginDTO;
import util.NavigationService;

public class PayOrderService implements NavigationService{
	
	PayOrderDAO payOrderDAO = new PayOrderDAO(); 
	
	@Transactional
	public void insertPayOrder(PayOrderDTO payOrderDTO, LoginDTO loginDTO) throws Exception{
		payOrderDTO.setPayDraftRemainingAmount(payOrderDTO.getPayDraftTotalAmount());
		payOrderDAO.insertPayOrder(payOrderDTO);
	}
	@Transactional
	public void deletePayOrder(long payOrderID,LoginDTO loginDTO) throws Exception{
		payOrderDAO.deletePayOrder(payOrderID);
	}
	@Transactional
	public PayOrderDTO editPayOrder(PayOrderDTO payOrderDTO, LoginDTO loginDTO) throws Exception{
		payOrderDAO.updatePayOrder(payOrderDTO);
		return payOrderDTO;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public PayOrderDTO getPayOrderDTOByID(long payOrderID) throws Exception{
		return payOrderDAO.getPayOrderDTOByID(payOrderID);
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public PayOrderDTO getPayOrderDTOByPayDraftNumber(String payDraftNumber) throws Exception{
		return payOrderDAO.getPayOrderDTOByPayDraftNumber(payDraftNumber);
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	@Override
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		return payOrderDAO.getPayOrderIDList();
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	@Override
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
			throws Exception {
		return payOrderDAO.getPayOrderIDListByCriteria(searchCriteria);
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		return payOrderDAO.getPayOrderDTOList(recordIDs);
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<PayOrderDTO> getPayOrderListByPayorderTypeAndPartialMatch(int payDraftType,
			String partialPayDraftNumber) throws Exception {
		
		return payOrderDAO.getPayOrderListByPayorderTypeAndPartialMatch(payDraftType,partialPayDraftNumber);
	}
	
	public void editPayOrder(PayOrderDTO payOrderDTO, DatabaseConnection databaseConnection, LoginDTO loginDTO) throws Exception {
		payOrderDAO.updatePayOrder(payOrderDTO,databaseConnection);
		
	}

}
