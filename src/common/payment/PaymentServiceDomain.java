package common.payment;

import javax.servlet.http.HttpServletRequest;

import annotation.Transactional;
import common.bill.BillDAO;
import common.bill.BillDTO;
import common.payment.constants.PaymentConstants;
import connection.DatabaseConnection;
import login.LoginDTO;

public class PaymentServiceDomain extends PaymentService{

	public void insertNewBankPayment(PaymentDTO paymentDTO, LoginDTO loginDTO, HttpServletRequest request) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			synchronized (lockingObject) {

				BillDAO billDAO = new BillDAO();
				BillDTO billDTO = billDAO.getBillDTOByBillID(paymentDTO.getBillIDs()[0], databaseConnection);
				if(billDTO.isPaid())
				{
					throw new Exception("Already Paid");
				}

				
				
				int moduleID = Integer.parseInt(request.getParameter("moduleID"));
				
				paymentDTO.setVatIncluded(1);
				paymentDTO.setPaymentStatus(PaymentConstants.PAYMENT_STATUS_APPROVED);
				
				paymentDTO.setBtclAmount(billDTO.getTotalPayable());
				paymentDTO.setVatAmount(billDTO.getVAT());
				paymentDTO.setPayableAmount(billDTO.getNetPayable());
				paymentDTO.setPaidAmount(paymentDTO.getPayableAmount());
				paymentDTO.setPaymentGatewayType(PaymentConstants.PAYEMENT_GATEWAY_TYPE_BANK);
				paymentDTO.setLastModificationTime(System.currentTimeMillis());
				paymentDTO.setVerifiedBy(loginDTO.getUserID());
				paymentDTO.setDescription(paymentDTO.getDescription());

				logger.debug("paymentDTO " + paymentDTO);
				PaymentDAO paymentDAO = new PaymentDAODomain();
				paymentDAO.payBillFromAPI(paymentDTO, loginDTO);

			}
			databaseConnection.dbTransationEnd();
			
		} catch (Exception ex) {
			logger.debug(ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				logger.debug(ex2);
			}
			logger.debug("Exception ", ex);
			throw ex;
		} finally {
			databaseConnection.dbClose();
		}
	}
	
	@Override
	@Transactional
	public void insertTeletalkPayment(PaymentDTO paymentDTO, LoginDTO loginDTO) throws Exception 
	{
		PaymentDAO paymentDAO = new PaymentDAODomain();
		paymentDAO.payBillFromAPI(paymentDTO, loginDTO);		
	}

}
