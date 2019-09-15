package common.payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import common.CommonDAO;
import common.EntityTypeConstant;
import common.ModuleConstants;
import common.bill.BillConstants;
import common.bill.BillDAO;
import common.bill.BillDTO;
import connection.DatabaseConnection;
import login.LoginDTO;
import request.CommonRequestDTO;
import request.RequestActionStateRepository;
import util.DatabaseConnectionFactory;
import util.SqlGenerator;
import util.UtilService;

public class PaymentDAODomain extends PaymentDAO{

	@Override
	public void payBillFromAPI(PaymentDTO paymentDTO, LoginDTO loginDTO) throws Exception {
		
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		logger.debug("payBillFromAPI");
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
			
		//	billDTO.setPaymentStatus(BillDTO.PAID_VERIFIED);
			newCommonRequestDTO.setRequestTypeID(paymentCompletedRequestTypeID);
			newCommonRequestDTO.setSourceRequestID(""+newCommonRequestDTO.getReqID());
						
			logger.debug("commonRequestDTO " + newCommonRequestDTO);
			
			newCommonRequestDTO.setRootReqID(requestDTO.getRootReqID() == null ? requestDTO.getReqID() : requestDTO.getRootReqID());
			commonDAO.commonRequestSubmit(null, newCommonRequestDTO, requestDTO, null, null, loginDTO, databaseConnection, true);
			
			commonDAO.updateStatusOfEntity(newCommonRequestDTO, loginDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST, databaseConnection);								
			
		//	billDTO.setPaymentID(paymentDTO.getID());
		//	billDTO.setLastModificationTime(currentTime);
			
		//	billDAO.updateBill(billDTO, databaseConnection);
		//	billDTO.payBill(databaseConnection);
			
			billService.verifyPaymentByBillID(billDTO.getID(),paymentDTO.getID());
		}
		logger.debug("payBillFromAPI done");
	}
}
