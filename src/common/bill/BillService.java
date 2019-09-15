package common.bill;

import accounting.*;
import annotation.AccountingLogic;
import annotation.Transactional;
import coLocation.demandNote.CoLocationDemandNote;
import coLocation.demandNote.CoLocationDemandNoteService;
import com.google.gson.JsonObject;
import common.RequestFailureException;
import common.payment.PaymentDTO;
import common.payment.PaymentService;
import common.pdf.AsyncPdfService;
import connection.DatabaseConnection;
import global.GlobalService;
import lli.LLIActionButton;
import lli.monthlyBill.ItemForManualBill;
import lli.monthlyBill.ItemForManualBillConditionBuilder;
import lli.monthlyBill.LLIManualBill;
import login.LoginDTO;
import org.apache.log4j.Logger;
import request.CommonRequestDTO;
import util.*;
import vpn.bill.BillConfigurationDTO;
import vpn.bill.Installment;
import vpn.bill.VpnBillDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BillService {
    public static Logger logger = Logger.getLogger(BillService.class);

    BillDAO billDAO = new BillDAO();

    @Transactional(transactionType = TransactionType.READONLY)
    public KeyValuePair<BillDTO, PaymentDTO> getBillPaymentInfoByBillId(long billId) {
        try {
            BillDTO billDTO = getBillByBillID(billId);
            if(billDTO == null) {
                return new KeyValuePair<>(null, null);
            }else {
                PaymentDTO paymentDTO = ServiceDAOFactory.getService(PaymentService.class).getPaymentDTObyID(billDTO.getPaymentID());
                return new KeyValuePair<>(billDTO, paymentDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new KeyValuePair<>(null, null);
        }
    }

    public BillDTO getBillByReqID(long reqID) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        BillDTO billDTO = null;
        try {
            databaseConnection.dbOpen();
            return billDAO.getBillByReqID(reqID, databaseConnection);
        } catch (Exception ex) {
            logger.debug("fatal", ex);
        } finally {
            databaseConnection.dbClose();
        }
        return billDTO;
    }


    @SuppressWarnings("unchecked")
    @Transactional
    public BillDTO getBillByBillID(long billID) throws Exception {
        BillDTO billDTO = billDAO.getBillDTOByBillID(billID, DatabaseConnectionFactory.getCurrentDatabaseConnection());

        if (billDTO == null) {
            return billDTO;
        }

        Class<? extends BillDTO> classObject = (Class<? extends BillDTO>) Class.forName(billDTO.getClassName());
        BillDTO childBillDTO = billDAO.getChildBillDTOByParentBillID(billID, classObject, DatabaseConnectionFactory.getCurrentDatabaseConnection());
        ModifiedSqlGenerator.populateObjectFromOtherObject(billDTO, childBillDTO, BillDTO.class);

        return childBillDTO;
    }

    @Transactional
    public VpnBillDTO getVpnBillByVpnBillID(long vpnBillID) throws Exception {

        return billDAO.getVpnBillDTOByVpnBillID(vpnBillID);
    }


    @Transactional
    public BillDTO getVpnBillByBillID(long billID) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        VpnBillDTO vpnBillDTO = null;
        try {
            databaseConnection.dbOpen();
            return billDAO.getVpnBillDTOByBillID(billID, databaseConnection);
        } catch (Exception ex) {
            logger.debug("fatal", ex);
        } finally {
            databaseConnection.dbClose();
        }
        return vpnBillDTO;
    }
	/*
	@Transactional
	public LliBillDTO getLliBillByBillID( long billID ) throws Exception{
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		LliBillDTO lliBillDTO = null;
		try {
			databaseConnection.dbOpen();
			return billDAO.getLliBillDTOByBillID( billID, databaseConnection);
		} catch (Exception ex) {
			logger.debug("fatal", ex);
		} finally {
			databaseConnection.dbClose();
		}
		return lliBillDTO;
	}
	*/


    public void updateBill(BillDTO BillDTO) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            billDAO.updateBill(BillDTO, databaseConnection);
        } catch (Exception ex) {
            logger.debug("fatal", ex);
        } finally {
            databaseConnection.dbClose();
        }
    }

    @SuppressWarnings("rawtypes")
    public Collection getUnPaidBillDTOListByModuleIDAndClientID(int modduleID, int clientID)
            throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        List<BillDTO> billList = new ArrayList<BillDTO>();
        try {
            databaseConnection.dbOpen();
            billList = billDAO.getUnPaidBillDTOListByModuleIDAndClientID(modduleID, clientID, databaseConnection);
        } catch (Exception ex) {
            logger.debug("fatal", ex);
        } finally {
            databaseConnection.dbClose();
        }
        return billList;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<BillDTO> getUnPaidBillDTOListByEntityTypeIDAndClientIDAndBillType(int entityType, long clientId, int billType) throws Exception {
        return billDAO.getUnPaidBillDTOListByEntityTypeIDAndClientIDAndBillType(entityType, clientId, billType);
    }

    public HashMap<Long, CommonRequestDTO> getRequestIDtoObject(ArrayList<Long> reqList) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        HashMap<Long, CommonRequestDTO> idToObject = new HashMap<Long, CommonRequestDTO>();
        try {
            databaseConnection.dbOpen();
            ArrayList<CommonRequestDTO> list = (ArrayList<CommonRequestDTO>) SqlGenerator.getObjectListByIDList(CommonRequestDTO.class, reqList, databaseConnection);
            for (CommonRequestDTO dto : list) {
                idToObject.put(dto.getReqID(), dto);
            }
        } catch (Exception ex) {
            logger.debug("fatal", ex);
        } finally {
            databaseConnection.dbClose();
        }
        return idToObject;
    }

    /*public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        List<BillDTO> statusHistories = new ArrayList<BillDTO>();
        try {
            databaseConnection.dbOpen();
            statusHistories = billDAO.getBillDTOListByIDList((List<Long>) recordIDs, databaseConnection);
        } catch (Exception ex) {
            logger.debug("fatal", ex);
        } finally {
            databaseConnection.dbClose();
        }
        return statusHistories;
    }*/
    public String getBillHtmlStringByBillID(HttpServletRequest request, HttpServletResponse response, long billID) throws Exception {

        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response) {
            private final StringWriter sw = new StringWriter();

            @Override
            public PrintWriter getWriter() throws IOException {
                return new PrintWriter(sw);
            }

            @Override
            public String toString() {
                return sw.toString();
            }
        };

        request.getRequestDispatcher("common/bill/billView.jsp?id=16002").include(request, responseWrapper);
        String content = responseWrapper.toString();
        System.out.println("Output : " + content);
        return content;
    }

    public List<BillDTO> getBillIDListByPaymentID(long paymentID, Object... objects) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        List<BillDTO> billDTOs = new ArrayList<BillDTO>();
        try {
            databaseConnection.dbOpen();
            billDTOs = billDAO.getBillDTOsByPaymentID(paymentID, databaseConnection);
        } catch (Exception ex) {
            logger.debug("fatal", ex);
        } finally {
            databaseConnection.dbClose();
        }
        return billDTOs;
    }

    @SuppressWarnings("static-access")
    public static void main(String args[]) throws Exception {
//		BillService bs = ServiceDAOFactory.getService(BillService.class);
//		BillDTO b = bs.getBillByBillID(325002L);
//		LLIManualBill manualBill= (LLIManualBill) b;
//		manualBill.setListOfFactors(bs.get(manualBill.getId()));
//		b.toString();
    }

    public static void insertBillConfigurationData(int moduleID, int billTypeID, HttpServletRequest request) throws Exception {

        DatabaseConnection databaseConnection = new DatabaseConnection();

        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            BillDAO.deleteBillConfigurationData(moduleID, billTypeID, databaseConnection);

            for (String fieldName : BillConstants.formFieldName.keySet()) {

                BillConfigurationDTO billConfigurationDTO = new BillConfigurationDTO();

                String text = request.getParameter(fieldName);
                String fontSizeStr = request.getParameter(fieldName + "_font");
                int fontSize = 10;

                if (fontSizeStr != null)
                    fontSize = Integer.parseInt(fontSizeStr);

                billConfigurationDTO.setBillTypeID(billTypeID);
                billConfigurationDTO.setModuleID(moduleID);
                billConfigurationDTO.setHeaderFooterID(BillConstants.formFieldName.get(fieldName));
                billConfigurationDTO.setDeleted(false);
                billConfigurationDTO.setText(text);
                billConfigurationDTO.setFontSize(fontSize);

                SqlGenerator.insert(billConfigurationDTO, databaseConnection);
            }

            databaseConnection.dbTransationEnd();
        } catch (Exception e) {

            e.printStackTrace();
            logger.fatal(e.getMessage());
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception e2) {

                e2.printStackTrace();
            }
            databaseConnection.dbClose();
            throw e;
        } finally {
            databaseConnection.dbClose();
        }
    }

    /**
     * @param moduleID
     * @param billTypeID
     * @throws Exception
     * @author Alam
     */
    public static List<BillConfigurationDTO> getBillConfiguration(int moduleID, int billTypeID) throws Exception {


        DatabaseConnection databaseConnection = new DatabaseConnection();

        List<BillConfigurationDTO> billConfigurationDTOs = null;

        try {
            databaseConnection.dbOpen();

            String conditionString = " where " + SqlGenerator.getColumnName(BillConfigurationDTO.class, "moduleID") + " = " + moduleID
                    + " and " + SqlGenerator.getColumnName(BillConfigurationDTO.class, "billTypeID") + " = " + billTypeID
                    + " and " + SqlGenerator.getColumnName(BillConfigurationDTO.class, "isDeleted") + " = 0";

            billConfigurationDTOs = (List<BillConfigurationDTO>) SqlGenerator.getAllObjectList(BillConfigurationDTO.class, databaseConnection, conditionString);
        } catch (Exception e) {

            e.printStackTrace();
            logger.fatal(e.getMessage());
            throw e;
        } finally {
            databaseConnection.dbClose();
        }

        return billConfigurationDTOs;
    }

    public static BillConfigurationDTO getBillConfigurationFromBillConfigListByHeaderFooterID(List<BillConfigurationDTO> configs, int headerFooterID) {

        if (configs == null) return null;

        for (BillConfigurationDTO config : configs) {

            if (config.getHeaderFooterID() == headerFooterID) {

                return config;
            }
        }

        return null;
    }

    /**
     * @param ids
     * @param installments
     * @throws Exception
     * @author Alam
     */
    public BillDTO mergeBillsAndInsertSplittedBills(Long[] ids, Installment[] installments) throws Exception {

        DatabaseConnection databaseConnection = new DatabaseConnection();
        BillDTO mergedBill = null;

        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            List<BillDTO> billDTOs = billDAO.getBillDTOListByIDList(Arrays.asList(ids), databaseConnection);

            if (billDTOs == null || billDTOs.size() == 0)
                throw new ArrayIndexOutOfBoundsException();

            mergedBill = makeMergedBillFromDueBills(billDTOs);

            SqlGenerator.insert(mergedBill, databaseConnection);

            List<BillDTO> splittedBills = splitMergedBillIntoInstallments(installments, mergedBill);

            insertMultipleBillFromList(splittedBills, databaseConnection);

            deleteOldBills(billDTOs, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception e) {

            databaseConnection.dbTransationRollBack();

            e.printStackTrace();
            logger.fatal(e.getMessage());

            throw e;
        } finally {

            databaseConnection.dbClose();
        }

        return mergedBill;
    }

    private void deleteOldBills(List<BillDTO> billDTOs, DatabaseConnection databaseConnection) throws Exception {

        for (BillDTO bill : billDTOs) {

            SqlGenerator.deleteEntity(bill, BillDTO.class, databaseConnection, false);
        }
    }

    private void insertMultipleBillFromList(List<BillDTO> splittedBills, DatabaseConnection databaseConnection) throws Exception {

        for (BillDTO bill : splittedBills) {

            SqlGenerator.insert(bill, databaseConnection);
        }
    }

    private List<BillDTO> splitMergedBillIntoInstallments(Installment[] installments, BillDTO mergedBill) {

        List<BillDTO> splittedBills = new ArrayList<BillDTO>();

        for (Installment currentInstallment : installments) {

            BillDTO billDTO = new BillDTO();

            billDTO.setClientID(mergedBill.getClientID());
            billDTO.setEntityID(mergedBill.getEntityID());
            billDTO.setEntityTypeID(mergedBill.getEntityTypeID());

            billDTO.setMonth(currentInstallment.getMonth());
            billDTO.setYear(currentInstallment.getYear());

            billDTO.setActivationTimeFrom(DateUtils.getStartTimeOfMonth(currentInstallment.getMonth(), currentInstallment.getYear()));
            billDTO.setLastPaymentDate(DateUtils.getEndTimeOfMonth(currentInstallment.getMonth(), currentInstallment.getYear()));

            billDTO.setBillType(BillConstants.POSTPAID);

            billDTO.setNetPayable(currentInstallment.getInstallment());

            billDTO.setTotalPayable(currentInstallment.getBtclAmount());
            billDTO.setGrandTotal(currentInstallment.getBtclAmount());
            billDTO.setVAT(currentInstallment.getVat());

            billDTO.setLastModificationTime(System.currentTimeMillis());

            billDTO.setClassName(BillDTO.class.getCanonicalName());

            splittedBills.add(billDTO);
        }

        return splittedBills;
    }

    /**
     * @param billDTOs
     * @return
     * @author Alam
     */
    private BillDTO makeMergedBillFromDueBills(List<BillDTO> billDTOs) {

        BillDTO billDTO = new BillDTO();

        int entityTypeID = billDTOs.get(billDTOs.size() - 1).getEntityTypeID();
        long entityID = billDTOs.get(billDTOs.size() - 1).getEntityID();
        long clientID = billDTOs.get(billDTOs.size() - 1).getClientID();

        double totalNetPayable = 0.0;
        double totalVat = 0.0;
        double totalPayable = 0.0;
        double grandTotal = 0.0;
        double lateFee = 0.0;
        double discount = 0.0;

        StringBuilder sb = new StringBuilder();

        for (BillDTO bill : billDTOs) {

            totalPayable += bill.getTotalPayable();
            totalVat += bill.getVAT();
            totalNetPayable += bill.getNetPayable();
            grandTotal += bill.getGrandTotal();
            lateFee += bill.getLateFee();
            discount += bill.getDiscount();

            sb.append(bill.getID() + ",");
        }

        sb.setLength(sb.length() - 1);
        billDTO.setDescription(sb.toString());

        billDTO.setTotalPayable(totalPayable);
        billDTO.setVAT(totalVat);
        billDTO.setNetPayable(totalNetPayable);
        billDTO.setGrandTotal(grandTotal);
        billDTO.setLateFee(lateFee);
        billDTO.setDiscount(discount);

        billDTO.setDeleted(true);

        billDTO.setEntityID(entityID);
        billDTO.setEntityTypeID(entityTypeID);
        billDTO.setLastModificationTime(System.currentTimeMillis());
        billDTO.setClientID(clientID);

        billDTO.setBillType(BillConstants.MERGED_UNPAID_BILL);

        return billDTO;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public boolean isBillSkipableByBillID(long billID) throws Exception {
        BillDTO billDTO = getBillByBillID(billID);
        return isBillSkipableByBillDTO(billDTO);

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public boolean isBillSkipableByBillDTO(BillDTO billDTO) throws Exception {
        if (billDTO == null) {
            throw new RequestFailureException("No bill found with invoice ID " + billDTO);
        }
        if (billDTO.isDeleted()) {
            return false;
        }
        AccountingLogic accountingLogic = billDTO.getClass().getAnnotation(AccountingLogic.class);

        if (accountingLogic == null || !(accountingLogic.value().newInstance() instanceof Skip)) {
            return false;
        }
        Skip skip = (Skip) ServiceDAOFactory.getService(accountingLogic.value());
        return skip.isSkipable(billDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public boolean isBillUnskippable(long billID) throws Exception {
        BillDTO billDTO = getBillByBillID(billID);
        return isBillUnskipableByBillDTO(billDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public boolean isBillUnskipableByBillDTO(BillDTO billDTO) throws Exception {

        if (billDTO == null) {
            throw new RequestFailureException("No bill found with invoice ID " + billDTO);
        }
        if (billDTO.isDeleted()) {
            return false;
        }
        AccountingLogic accountingLogic = billDTO.getClass().getAnnotation(AccountingLogic.class);

        if (accountingLogic == null || !(accountingLogic.value().newInstance() instanceof Skip)) {
            return false;
        }
        Skip skip = (Skip) ServiceDAOFactory.getService(accountingLogic.value());
        return skip.isUnskipable(billDTO);
    }

    @Transactional
    public void unverifiedPayBillByBillID(long billID, long paymentID) throws Exception {
        BillDTO billDTO = getBillByBillID(billID);
        if (billDTO == null) {
            throw new RequestFailureException("No bill found with invoice ID " + billID);
        }
        int billPaymentStatus = billDTO.getPaymentStatus();
        if (billPaymentStatus != BillDTO.UNPAID && billPaymentStatus != BillDTO.SKIPPED) {
            throw new RequestFailureException("Bill with invoice ID " + billID + " can not be temp paid.A "
                    + "bill can only be paid temporalily from UNPAID OR SKIPPED state.");
        }
        if (billPaymentStatus == BillDTO.UNPAID) {
            // Normal Flow
            billDTO.setPaymentStatus(BillDTO.PAID_UNVERIFIED);
        } else if (billPaymentStatus == BillDTO.SKIPPED) {
            billDTO.setPaymentStatus(BillDTO.PAID_UNVERIFIED_FROM_SKIPPED);
        }
        billDTO.setPaymentID(paymentID);
        billDAO.updateBill(billDTO, DatabaseConnectionFactory.getCurrentDatabaseConnection());

    }


    public void skipBill(BillDTO billDTO) throws Exception {

        AccountingLogic accountingLogic = getAccountingLogic(billDTO, true);

        Skip skip = (Skip) ServiceDAOFactory.getService(accountingLogic.value());

        skip.skip(billDTO);
    }

    private AccountingLogic getAccountingLogic(BillDTO billDTO, boolean forSkip) throws Exception {

        AccountingLogic accountingLogic = billDTO.getClass().getAnnotation(AccountingLogic.class);
        if (forSkip) {
            if (accountingLogic == null || !(accountingLogic.value().newInstance() instanceof Skip)) {
                throw new RequestFailureException("Bill with invoice ID " + billDTO.getID() + " is not skip able");
            }
            if (billDTO.getPaymentStatus() != BillDTO.UNPAID) {
                throw new RequestFailureException("Bill with invoice ID " + billDTO.getID() + " can not be skipped "
                        + "as it is not in unpaid status.");
            }

        } else {
            if (accountingLogic == null || !(accountingLogic.value().newInstance() instanceof Skip)) {
                throw new RequestFailureException("Bill with invoice ID " + billDTO.getID() + " is not skip able");
            }
            if (billDTO.getPaymentStatus() != BillDTO.SKIPPED) {
                throw new RequestFailureException("Bill with invoice ID " + billDTO.getID() + " can not be unskipped "
                        + "as it is not in skipped status.");
            }
        }

        return accountingLogic;
    }


    @Transactional
    public void cancelBillPaymentByBillID(long billID) throws Exception {
        BillDTO billDTO = getBillByBillID(billID);
        if (billDTO == null) {
            throw new RequestFailureException("No bill found with bill ID " + billID);
        }
        if (billDTO.getPaymentStatus() != BillDTO.PAID_UNVERIFIED) {
            throw new RequestFailureException("Bill payment of bill with invoice ID " + billID + "can only be cancelled from Temp Paid status.");
        }
        billDTO.setPaymentID(null);
        billDTO.setPaymentStatus(BillDTO.UNPAID);
        billDAO.updateBill(billDTO, DatabaseConnectionFactory.getCurrentDatabaseConnection());
    }


    @Transactional
    public void verifyPaymentByBillID(long billID, long paymentID) throws Exception {
        BillDTO billDTO = getBillByBillID(billID);
        if (billDTO == null) {
            throw new RequestFailureException("No bill found with invoice ID " + billID);
        }

        AccountingLogic accountingLogic = billDTO.getClass().getAnnotation(AccountingLogic.class);
		/*
		if(billDTO.getPaymentStatus() != BillDTO.PAID_UNVERIFIED){
			throw new RequestFailureException("Bill with invoice ID "+billDTO.getID()
			+" is not in unpaid verified state. So this bill's payment can not be verified.");
		}
		*/

        if (accountingLogic != null && accountingLogic.value().newInstance() instanceof VerifyPayment) {
            VerifyPayment payBill = (VerifyPayment) ServiceDAOFactory.getService(accountingLogic.value());
            payBill.verifyPayment(billDTO);
        }
        billDTO.setPaymentID(paymentID);
        billDTO.setPaymentStatus(BillDTO.PAID_VERIFIED);
        billDAO.updateBill(billDTO, DatabaseConnectionFactory.getCurrentDatabaseConnection());
        billDTO.payBill(DatabaseConnectionFactory.getCurrentDatabaseConnection());
    }


    @Transactional
    public void cancelBillByBillID(long billID) throws Exception {
        BillDTO billDTO = getBillByBillID(billID);
        if (billDTO == null) {
            throw new RequestFailureException("No such bill found with ID " + billID);
        }
        int status = billDTO.getPaymentStatus();
        if (status == BillDTO.PAID_VERIFIED) {
            throw new RequestFailureException("Invoice " + billID + " is already paid and verified. You can not cancel this bill");
        }
        if (status == BillDTO.PAID_UNVERIFIED) {
            throw new RequestFailureException("Invoice " + billID + " is already paid but not verified. You can not cancel this bill");
        }
        if (status == BillDTO.PAID_UNVERIFIED_FROM_SKIPPED) {
            throw new RequestFailureException("Invoice " + billID + " is already paid from skipped situation but not verified. You can not cancel this bill");
        }
        if (status == BillDTO.SKIPPED) {
            throw new RequestFailureException("Invoice " + billID + " is skipped. You can not cancel this bill");
        }
        //only acceptable step will be UNPAID
        billDTO.setPaymentID(null);
        //
        AccountingLogic accountingLogic = billDTO.getClass().getAnnotation(AccountingLogic.class);

        if (accountingLogic != null && accountingLogic.value().newInstance() instanceof CancelBill) {
            CancelBill cancelBill = (CancelBill) ServiceDAOFactory.getService(accountingLogic.value());
            cancelBill.cancelBill(billDTO);
        }
        // update the bill status
        billDAO.updateBill(billDTO, DatabaseConnectionFactory.getCurrentDatabaseConnection());

    }


    public void unskipBillByBillID(BillDTO billDTO) throws Exception {

        AccountingLogic accountingLogic = getAccountingLogic(billDTO, false);


        Skip skip = (Skip) ServiceDAOFactory.getService(accountingLogic.value());

        skip.unskip(billDTO);
//		billDTO.setPaymentStatus(BillDTO.UNPAID);
//
//		billDAO.updateBill(billDTO, DatabaseConnectionFactory.getCurrentDatabaseConnection());

    }


    public int cancelBillsByBillIDs(List<Long> ids, LoginDTO loginDTO) throws Exception {

        logger.debug("Cancel bill is called in BillService");
        DatabaseConnection databaseConnection = new DatabaseConnection();
        int numOfRowDeleted = 0;

        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            numOfRowDeleted = billDAO.cancelBillsByBillIDs(ids, databaseConnection);
            databaseConnection.dbTransationEnd();
        } catch (Exception e) {

            e.printStackTrace();
            logger.debug("Exception " + e.getStackTrace());

            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            throw new Exception("Error with database connection");
        } finally {

            databaseConnection.dbClose();
        }

        return numOfRowDeleted;
    }

    @Transactional
    public void insertBill(BillDTO billDTO) throws Exception {
        billDAO.insertBill(billDTO, DatabaseConnectionFactory.getCurrentDatabaseConnection());
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    public void insertBillIndividualTransaction(BillDTO billDTO) throws Exception {
        billDAO.insertBill(billDTO, DatabaseConnectionFactory.getCurrentDatabaseConnection());
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public boolean isBillCancellableByBillID(long billID) throws Exception {
        BillDTO billDTO = getBillByBillID(billID);
        return isBillCancellableByBillDTO(billDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public boolean isBillCancellableByBillDTO(BillDTO billDTO) throws Exception {
        if (billDTO == null) {
            throw new RequestFailureException("No bill found with invoice ID " + billDTO);
        }
        return ((!billDTO.isDeleted()) && (billDTO.getPaymentStatus() == BillDTO.UNPAID));
    }

//	@Transactional(transactionType=TransactionType.READONLY)
//	public BillButtonStatus getBillButtonStatus(long billID) throws Exception {
//		BillButtonStatus billButtonStatus = new BillButtonStatus();
//		BillDTO billDTO = getBillByBillID(billID);
//		billButtonStatus.skippable = isBillSkipableByBillDTO(billDTO);
//		billButtonStatus.unSkippable = isBillUnskipableByBillDTO(billDTO);
//		billButtonStatus.cancellable = isBillCancellableByBillDTO(billDTO);
//		return billButtonStatus;
//	}

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIActionButton> getAvailableActions(long billID, LoginDTO loginDTO) throws Exception {
        List<LLIActionButton> list = new ArrayList<>();
        BillDTO billDTO = getBillByBillID(billID);
        if (isBillSkipableByBillDTO(billDTO)) {
            list.add(new LLIActionButton("Skip", "lli/dn/skip.do", false));
        }
        if (isBillUnskipableByBillDTO(billDTO)) {
            list.add(new LLIActionButton("Unskip", "lli/dn/unSkip.do", false));
        }
        if (isBillCancellableByBillDTO(billDTO)) {
            list.add(new LLIActionButton("Cancel", "lli/dn/cancel.do", false));
        }
        return list;
    }


    @Transactional
    public void generateManualBill(BillDTO bill) throws Exception {

        LLIManualBill billDTO = (LLIManualBill) bill;
        billDTO.setTotalCost();
        double vatCalculable = billDTO.getTotalCost();
        double vat = vatCalculable * billDTO.getVatPercentage() / 100.0;
        billDTO.setVatPercentage(billDTO.getVatPercentage());
        billDTO.setVAT(vat);
        billDTO.setDiscount(0);
        billDTO.setGrandTotal(vatCalculable);
        billDTO.setTotalPayable(vatCalculable);
        billDTO.setNetPayable(vatCalculable + vat);
        billDTO.setLastPaymentDate(TimeConverter.getLastDateBeforeNMonth(1));
        insertBill(billDTO);
        long billId = billDTO.getId();
        for (ItemForManualBill item : billDTO.getListOfFactors()) {
            item.setManualBillId(billId);
            ModifiedSqlGenerator.insert(item);
        }
        AccountingLogic accountingLogic = billDTO.getClass().getAnnotation(AccountingLogic.class);

        if (accountingLogic != null && accountingLogic.value().newInstance() instanceof GenerateBill) {
            GenerateBill generateMonthlyBill = (GenerateBill) ServiceDAOFactory.getService(accountingLogic.value());
            generateMonthlyBill.generate(billDTO);
        }

        //generating pdf
        AsyncPdfService.getInstance().accept(billDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public BillDTO getManualBillByBillId(long billId) throws Exception {
        BillDTO billDTO = getBillByBillID(billId);
        if (billDTO == null) {
            throw new RequestFailureException("No bill found with bill id " + billId);
        }
        LLIManualBill bill = (LLIManualBill) billDTO;
        bill.setListOfFactors(getItemsByManualBillId(bill.getId()));
        return bill;
    }

    private List<ItemForManualBill> getItemsByManualBillId(long id) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(ItemForManualBill.class,
                new ItemForManualBillConditionBuilder()
                        .Where()
                        .manualBillIdEquals(id)
                        .getCondition());

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public BillDTO getBillDTOVerified(long billId) throws Exception {
        BillDTO billDTO = getBillByBillID(billId);
        if (billDTO == null) {
            throw new RequestFailureException("No Bill Found with ID " + billId);
        }

        //exception case
        if (billDTO instanceof LLIManualBill) {
            LLIManualBill bill = (LLIManualBill) billDTO;
            bill.setListOfFactors(getItemsByManualBillId(bill.getId()));
            return bill;
        } else if (billDTO instanceof CoLocationDemandNote) {
            CoLocationDemandNote bill = (CoLocationDemandNote) billDTO;
            if (bill.isYearlyDemandNote()) {
                return ServiceDAOFactory.getService(CoLocationDemandNoteService.class).getYearlyDemandNoteByParentDemandNoteId(bill.getDemandNoteID());
            }
        }
        return billDTO;
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public List<BillPaymentDTOForLedger> getSubscriberLedgerReport(long clientID, Long fromDate, Long toDate, int moduleId) throws Exception {
        DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
        return billDAO.getSubscriberLedgerReport(clientID, fromDate, toDate, moduleId, databaseConnection);
    }
}
