package payment.api;
import static common.StringUtils.*;
import java.util.*;

import org.apache.log4j.Logger;

import common.bill.BankTransactionHistory;
import common.bill.BankTransactionHistoryDAO;
import common.bill.BillDAO;
import common.bill.BillDTO;
import common.payment.PaymentDAO;
import connection.DatabaseConnection;
import requestMapping.RequestParameter;
import util.TimeConverter;


public class PaymentAPIService {
	BankTransactionHistoryDAO bankTransactionHistoryDAO = new BankTransactionHistoryDAO();
	PaymentAPITokenRepository paymentAPITokenRepository = PaymentAPITokenRepository.getInstance();
	PaymentAPIDAO paymentAPIDAO = new PaymentAPIDAO();
	BillDAO billDAO = new BillDAO();
	PaymentDAO paymentDAO = new PaymentDAO();
	Logger logger = Logger.getLogger(getClass());
	public String GetTokenByUser(String userId,String password){
		String bankCode = null;
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			bankCode = paymentAPIDAO.getBankCode(userId, password, databaseConnection);
		}catch(Exception ex){if(ex instanceof PaymentApiException){
				throw (PaymentApiException)ex;
			}else{
				throw new PaymentApiException(ResponseCode.SERVER_ERROR, "Server Error");
			}
		}finally{
			databaseConnection.dbClose();
		}
		if(bankCode == null){
			throw new PaymentApiException(ResponseCode.INVALID_CREDENTIAL, "Invalid user name or password");
		}
		String token = PaymentAPITokenRepository.getInstance().getNewToken(bankCode);
		
		return token;
	}
	
	public String  ChangePasswordRequest(String userId,String token,String oldPassword,
			String newPassword){
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			String bankCode = paymentAPIDAO.getBankCode(userId, oldPassword, databaseConnection);
			String bankCodeFromRepository = paymentAPITokenRepository.getBankCode(token);
			if(!isEqual(bankCode, bankCodeFromRepository)){
				throw new PaymentApiException(ResponseCode.INVALID_CREDENTIAL, "Invalid username or old password or token");
			}
			paymentAPIDAO.changePassword(userId, oldPassword, newPassword, databaseConnection);
			bankCode = paymentAPIDAO.getBankCode(userId, newPassword, databaseConnection);
			String newToken = paymentAPITokenRepository.getNewToken(bankCode);
			databaseConnection.dbTransationEnd();
			return newToken;
			
		}catch(Exception ex){
			try{
				databaseConnection.dbTransationRollBack();
			}catch(Exception e){}
			if(ex instanceof PaymentApiException){
				throw (PaymentApiException)ex;
			}else{
				throw new PaymentApiException(ResponseCode.SERVER_ERROR, "Server Error");
			}
		}
	}
	
	
	public void payBill(long billID,long billToken,long billPaymentAmount,String bankCode,String branchCode){
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			
			paymentAPIDAO.validateBranchCodeWithBankCode(bankCode, branchCode, databaseConnection);
			
			BillDTO billDTO  = billDAO.getBillDTOByBillID(billID, databaseConnection);
			if(billDTO == null||billDTO.isDeleted()){
				throw new PaymentApiException(ResponseCode.INVALID_REQUEST, "No bill exists with ID "+billID);
			}
			if(!billDTO.isPayable()){
				throw new PaymentApiException(ResponseCode.INVALID_REQUEST,"Bill is already paid");
			}
			if(billDTO.getNetPayableCeiled()!=billPaymentAmount){
				throw new PaymentApiException(ResponseCode.INVALID_REQUEST,"Bill amount does not match "
						+ "with requested bill amount");
			}
			if(billDTO.getLastModificationTime()!=billToken){
				throw new PaymentApiException(ResponseCode.RESOURCE_NOT_FOUND, "Bill token does not match "
						+ "with current bill's token. Please the bill againg to get the latest bill token");
			}
			
			
			long currentTime = System.currentTimeMillis();
			
		
			//billDAO.markBillAsPaid(billID, billToken, currentTime, paymentDTO.getdatabaseConnection);
			billDTO.setPaymentStatus(BillDTO.PAID_UNVERIFIED);
			billDTO.setPaymentPortal(BillDTO.PAYMENT_GATEWAY_BANK);
			billDTO.setLastModificationTime(billToken);
			int numOfAffectedRows = billDAO.updateConcurrentBill(billDTO,currentTime, databaseConnection);
			if(numOfAffectedRows != 1){
				throw new PaymentApiException(ResponseCode.SERVER_ERROR, "Bill payment failed because of "
						+ "optimictic locking issue. Please try again.");
			}
			
			BankTransactionHistory bankTransactionHistory = new BankTransactionHistory();
			bankTransactionHistory.setBankCode(bankCode);
			bankTransactionHistory.setBillID(billID);
			bankTransactionHistory.setBranchCode(branchCode);
			bankTransactionHistory.setTime(currentTime);
			bankTransactionHistory.setTxType(BankTransactionHistory.BANK_PAYMENT);
			
			bankTransactionHistoryDAO.insertBankTransaction(bankTransactionHistory, databaseConnection);
			
			databaseConnection.dbTransationEnd();
			
		}catch(Exception ex){
			try{
				databaseConnection.dbTransationRollBack();
			}catch(Exception ex2){}
			if(ex instanceof PaymentApiException){
				throw (PaymentApiException)ex;
			}else{
				throw new PaymentApiException(ResponseCode.SERVER_ERROR, "Server Error");
			}
		}finally{
			databaseConnection.dbClose();
		}
	}
	
	
	
	private BankTransactionHistory createPaymentCancelTransactionHistory(long billID,String bankCode,String branchCode,long currentTime){
		BankTransactionHistory paymentCancelTransactionHistory = new BankTransactionHistory();
		paymentCancelTransactionHistory.setBankCode(bankCode);
		paymentCancelTransactionHistory.setBillID(billID);
		paymentCancelTransactionHistory.setBranchCode(branchCode);
		paymentCancelTransactionHistory.setTime(currentTime);
		paymentCancelTransactionHistory.setTxType(BankTransactionHistory.PAYMENT_CANCEL);
		
		return paymentCancelTransactionHistory;
	}
	
	public void cancelBillPayment(long billID,String bankCode,String branchCode){
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			paymentAPIDAO.validateBranchCodeWithBankCode(bankCode, branchCode, databaseConnection);
			
			// if bill payment is approved the throw exception
			
			
			BillDTO billDTO  = billDAO.getBillDTOByBillID(billID, databaseConnection);
			if(billDTO == null||billDTO.isDeleted()){
				throw new PaymentApiException(ResponseCode.INVALID_REQUEST, "No bill found with ID "+billID);
			}
			
			
			
			if(billDTO.getPaymentStatus() == BillDTO.UNPAID){
				throw new PaymentApiException(ResponseCode.INVALID_REQUEST,"Bill can not be cancelled. Because bill not payed yet");
			}

			if(billDTO.getPaymentPortal()!=BillDTO.PAYMENT_GATEWAY_BANK){
				throw new PaymentApiException(ResponseCode.INVALID_REQUEST, "Bill can not be cancelled. "
						+ "Because bill was not paid by bank payment api.");
			}
			
			
			if(billDTO.getPaymentStatus() == BillDTO.PAID_VERIFIED){
				throw new PaymentApiException(ResponseCode.INVALID_REQUEST,"Bill can not be cancelled. Because bill "
						+ "payment has already been verified");
			}
			
			
			
			
			BankTransactionHistory lastBankTransactionHistory = bankTransactionHistoryDAO.getLastBankTransactionHistoryByBillID(billID, databaseConnection);
			
			
			

			long currentTime = System.currentTimeMillis();
			if(lastBankTransactionHistory!=null){
				if(lastBankTransactionHistory.getTxType() != BankTransactionHistory.BANK_PAYMENT){
					throw new PaymentApiException(ResponseCode.INVALID_REQUEST, "No bill payment histroy found");
				}
				if(!isEqual(bankCode, lastBankTransactionHistory.getBankCode())){
					throw new PaymentApiException(ResponseCode.INVALID_REQUEST, "This bill was not paid by your bank");
				}/*
				if(!isEqual(branchCode, lastBankTransactionHistory.getBranchCode())){
					throw new PaymentApiException(ResponseCode.INVALID_REQUEST, "This bill was not paid by your branch");
				}*/
				if(TimeConverter.getStartTimeOfTheDay(lastBankTransactionHistory.getTime())!=TimeConverter.getStartTimeOfTheDay(currentTime)){
					throw new PaymentApiException(ResponseCode.INVALID_REQUEST,"Bill can not be cancelled. It was paid in previous date");
				}
			}else{
				throw new PaymentApiException(ResponseCode.SERVER_ERROR, "No bill previouspayment History found");
			}
			
			
			billDTO.setPaymentPortal(null);
			billDTO.setPaymentStatus(BillDTO.UNPAID);
			
//			billDTO.setPaymentDate(null);
			
			
			billDTO.setPaymentID(null);
			int numOfAffectedRows = billDAO.updateConcurrentBill(billDTO, currentTime, databaseConnection);
			if(numOfAffectedRows!=1){
				throw new PaymentApiException(ResponseCode.SERVER_ERROR,"Bill cancel request failed.Please try again");
			}

			BankTransactionHistory paymentCancelTransactionHistory = createPaymentCancelTransactionHistory(billID, bankCode, branchCode, currentTime);
			
			bankTransactionHistoryDAO.insertBankTransaction(paymentCancelTransactionHistory, databaseConnection);
			databaseConnection.dbTransationEnd();
		}catch(Exception ex){
			try{
				databaseConnection.dbTransationRollBack();
			}catch(Exception ex2){}
			if(ex instanceof PaymentApiException){
				throw (PaymentApiException)ex;
			}else{
				throw new PaymentApiException(ResponseCode.SERVER_ERROR, "Server Error");
			}
		}finally{
			databaseConnection.dbClose();
		}
	}
	
	
	public BankTransactionHistory getTransactionHistoryByID(@RequestParameter("ID") long ID,@RequestParameter("bankCode") String bankCode){
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			BankTransactionHistory bankTransactionHistory =bankTransactionHistoryDAO.getBankTransactionHistoryByID(ID, databaseConnection); 
			if(bankTransactionHistory!=null&& bankTransactionHistory.getBankCode()!=null&&!bankTransactionHistory.getBankCode().equals(bankCode)){
				throw new PaymentApiException(ResponseCode.INVALID_REQUEST, "This trasaction was not done by your bank");
			}
			return bankTransactionHistory;
		}catch(Exception ex){
			try{
				databaseConnection.dbTransationRollBack();
			}catch(Exception ex2){}
			if(ex instanceof PaymentApiException){
				throw (PaymentApiException)ex;
			}else{
				throw new PaymentApiException(ResponseCode.SERVER_ERROR, "Server Error");
			}
		}finally{
			databaseConnection.dbClose();
		}
	}
	public Reconciliation getReconsutationAmount(Map<String,String> criteriaMap){
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			
			if(criteriaMap.containsKey("branchCode")){
				paymentAPIDAO.validateBranchCodeWithBankCode(criteriaMap.get("bankCode"), 
						criteriaMap.get("branchCode"), databaseConnection);
			}
			
			return bankTransactionHistoryDAO.getReconsulation(criteriaMap, databaseConnection);
			
		}catch(Exception ex){
			try{
				databaseConnection.dbTransationRollBack();
			}catch(Exception ex2){}
			if(ex instanceof PaymentApiException){
				throw (PaymentApiException)ex;
			}else{
				throw new PaymentApiException(ResponseCode.SERVER_ERROR, "Server Error");
			}
		}finally{
			databaseConnection.dbClose();
		}
	}
	
	public BillDTOForBankPayment getBillForBankPayment(long billID) throws Exception{
		
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			BillDTOForBankPayment billDTOForBankPayment = (BillDTOForBankPayment)billDAO.getBillDTOByBillID(billID, BillDTOForBankPayment.class,
					databaseConnection);
			if(billDTOForBankPayment == null || billDTOForBankPayment.isDeleted()){
				throw new PaymentApiException(ResponseCode.RESOURCE_NOT_FOUND, "No valid bill found with bill id "+billID);
			}
			BankTransactionHistory lastTransactionHistory = bankTransactionHistoryDAO.getLastBankTransactionHistoryByBillID(billID, databaseConnection);
			
			if(lastTransactionHistory != null && lastTransactionHistory.getTxType() == BankTransactionHistory.BANK_PAYMENT){
				billDTOForBankPayment.setBankCode(lastTransactionHistory.getBankCode());
				billDTOForBankPayment.setBranchCode(lastTransactionHistory.getBranchCode());
				billDTOForBankPayment.setPaymentTime(lastTransactionHistory.getTime());
			}
			
			return billDTOForBankPayment;
		}catch(Exception ex){
			try{
				databaseConnection.dbTransationRollBack();
			}catch(Exception ex2){}
			if(ex instanceof PaymentApiException){
				throw (PaymentApiException)ex;
			}else{
				throw new PaymentApiException(ResponseCode.SERVER_ERROR, "Server Error");
			}
		}finally{
			databaseConnection.dbClose();
		}
		
		
	}
	
	public List<BillDTOForBankPayment> getPaidBills(Map<String,String> criteriaMap){
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			
			
			databaseConnection.dbOpen();
			
			if(criteriaMap.containsKey("branchCode")){
				paymentAPIDAO.validateBranchCodeWithBankCode(criteriaMap.get("bankCode"), 
						criteriaMap.get("branchCode"), databaseConnection);
			}
			
			
			List<BankTransactionHistory> bankTransactionHistoryList = 
					bankTransactionHistoryDAO.getPaidBankTransactionList(criteriaMap, databaseConnection);
			if(bankTransactionHistoryList.isEmpty()){
				return Collections.emptyList();
			}
			Map<Long,BankTransactionHistory> mapOfBankTransactionHistoryToBillID = new HashMap<>();
			List<Long> billIDs = new ArrayList<>();
			for(BankTransactionHistory bankTransactionHistory: bankTransactionHistoryList){
				mapOfBankTransactionHistoryToBillID.put(bankTransactionHistory.getBillID(), bankTransactionHistory);
				billIDs.add(bankTransactionHistory.getBillID());
			}
			
			List<BillDTO> billDTOs = billDAO.getBillDTOListByIDList(billIDs,BillDTOForBankPayment.class, databaseConnection);
			
			
			List<BillDTOForBankPayment> bankPaidBills = new ArrayList<>();
			
			for(BillDTO billDTO: billDTOs){
				BillDTOForBankPayment bankPaidBill = (BillDTOForBankPayment)billDTO;
				
				BankTransactionHistory bankTransactionHistory = mapOfBankTransactionHistoryToBillID.get(billDTO.getID());
				
				bankPaidBill.setBankCode(bankTransactionHistory.getBankCode());
				bankPaidBill.setBranchCode(bankTransactionHistory.getBranchCode());
				bankPaidBill.setPaymentTime(bankTransactionHistory.getTime());
				
				bankPaidBills.add(bankPaidBill);
			}
			
			return bankPaidBills;
		}catch(Exception ex){
			try{
				databaseConnection.dbTransationRollBack();
			}catch(Exception ex2){}
			if(ex instanceof PaymentApiException){
				throw (PaymentApiException)ex;
			}else{
				throw new PaymentApiException(ResponseCode.SERVER_ERROR, "Server Error");
			}
		}finally{
			databaseConnection.dbClose();
		}
	}
	
	
}
