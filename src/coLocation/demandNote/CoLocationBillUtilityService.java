package coLocation.demandNote;

import coLocation.CoLocationConstants;
import coLocation.accounts.VariableCost.VariableCostService;
import coLocation.accounts.commonCost.AllVariableUnitCharge;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.application.CoLocationApplicationService;
import coLocation.connection.CoLocationConnectionDTO;
import coLocation.connection.CoLocationConnectionService;
import common.RequestFailureException;
import common.bill.BillDTO;
import common.bill.BillService;
import common.payment.PaymentDTO;
import common.payment.PaymentService;
import lombok.extern.log4j.Log4j;
import util.ServiceDAOFactory;
import util.TimeConverter;

import java.time.*;

@Log4j
public class CoLocationBillUtilityService {


    private CoLocationBillUtilityService() {}

    private static final double MONTH_DAY_COUNT = 30.;
    private static final double MONTH_COUNT = 12.;


    public static double getMonthFactorByFromDateToDate(LocalDate fromDate, LocalDate toDate) {
        Period period = Period.between(fromDate, toDate);
        log.info("Years: " + period.getYears() + " Month: " + period.getMonths() + " Days: " + period.getDays());
        double monthEstimated = (MONTH_COUNT* period.getYears()) + (period.getMonths()) + (period.getDays()/MONTH_DAY_COUNT);
        log.info("Month Estimated: " + monthEstimated);
        if(monthEstimated<0){
            throw new RequestFailureException("Currently your request cannot be processed. Server time "+ fromDate + ". You can submit application after " + toDate.plusMonths(1));
        }


        return monthEstimated;
    }


    public static double getMonthFactorByConnectionWithRespectToNextBillDate(CoLocationConnectionDTO connection) throws Exception {
        LocalDate now = LocalDate.now(ZoneId.systemDefault());
        LocalDate nextBillDate = getNextBillingDateBasedOnSystemDateAndConnectionHistoryId(connection.getHistoryID());

        double monthFactor =  getMonthFactorByFromDateToDate(now, nextBillDate);
        if(monthFactor<0){
            throw new RequestFailureException("Currently your request cannot be processed. Server time "+ now + ". You can submit application after " + nextBillDate.plusMonths(1));
        }
        return  monthFactor;
    }

    /***
     *
     * @param oldConnection should be null for new connection application
     * @param newConnection
     * @return double : adjustment of the application.
     * @throws Exception
     */
    public static double calculateAdjustmentForYearlyBill(CoLocationConnectionDTO oldConnection, CoLocationConnectionDTO newConnection) throws Exception {
        //emsure this is the last history ; the most updated one;
//        CoLocationConnectionDTO latestHistory = ServiceDAOFactory.getService(CoLocationConnectionService.class)
//                .getConnectionHistoryByHistoryID(oldConnection.getHistoryID())
//                .stream()
//                .findFirst()
//                .orElseThrow(()->new RequestFailureException("Exception in getting latest history of connection history id " + oldConnection.getHistoryID()));
//        if(latestHistory.getID() != oldConnection.getID()) {
//            throw new RequestFailureException("History Mismatch");
//        }




        double adjustment = 0;
        CoLocationApplicationDTO application = ServiceDAOFactory.getService(CoLocationApplicationService.class).getCoLocationApplicationByConnectionId(newConnection.getID());

        if(application.getApplicationType() == CoLocationConstants.NEW_CONNECTION
        || application.getApplicationType() == CoLocationConstants.UPGRADE_CONNECTION
        ) {
            Long demandNoteId = application.getDemandNoteID();
            BillDTO billDTO = ServiceDAOFactory.getService(BillService.class).getBillDTOVerified(demandNoteId);
            if(application.getSkipPayment() == 1){
                adjustment = billDTO.getNetPayable();
            }else {
                PaymentDTO paymentDTO = ServiceDAOFactory.getService(PaymentService.class).getPaymentDTObyID(billDTO.getPaymentID());
                if(paymentDTO == null){
                    throw new RequestFailureException("No Payment info found for app id " + application.getApplicationID() + " bill id: " + billDTO.getID() + " payment id: " + billDTO.getPaymentID());
                }
                LocalDate demandNotePaymentDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(paymentDTO.getPaymentTime()), ZoneId.systemDefault()).toLocalDate();
                LocalDate serviceStartDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(newConnection.getStartDate()), ZoneId.systemDefault()).toLocalDate();
                double monthEstimated = getMonthFactorByFromDateToDate(demandNotePaymentDate, serviceStartDate);


                AllVariableUnitCharge newCostSettings = ServiceDAOFactory.getService(VariableCostService.class).getAllVariableChargeByCoLocationApplication(application);

                adjustment = monthEstimated * newCostSettings.getTotalCost();

            }
        }else if(application.getApplicationType() == CoLocationConstants.DOWNGRADE_CONNECTION) {
            AllVariableUnitCharge newCharge = ServiceDAOFactory.getService(VariableCostService.class).getAllVariableUnitChargeByCoLocation(newConnection);
            AllVariableUnitCharge oldCharge = ServiceDAOFactory.getService(VariableCostService.class).getAllVariableUnitChargeByCoLocation(oldConnection);

            double monthFactor = CoLocationBillUtilityService.getMonthFactorByConnectionWithRespectToNextBillDate(newConnection);
            adjustment = monthFactor * (newCharge.getTotalCost()-oldCharge.getTotalCost());

        }
        log.info("Adjustment Amount: " + adjustment);
        return adjustment;
    }



    public static LocalDate getNextBillingDateBasedOnSystemDateAndConnectionHistoryId(long historyId) throws Exception {
        CoLocationConnectionDTO firstHistory = ServiceDAOFactory.getService(CoLocationConnectionService.class)
                .getConnectionHistoryByHistoryID(historyId)
                .stream()
                .reduce((first, second)->second)
                .orElseThrow(() -> new RequestFailureException("No History Found for connection history id " + historyId));

//        LocalDate now = LocalDate.now(ZoneId.systemDefault());
        return getNextBillingDateBasedOnSystemDateAndInitialHistory(firstHistory);

    }

    public static LocalDate getNextBillingDateBasedOnSystemDateAndInitialHistory(CoLocationConnectionDTO firstHistory) {
        return TimeConverter.getStandardNextYearDate(firstHistory.getStartDate());

    }
}
