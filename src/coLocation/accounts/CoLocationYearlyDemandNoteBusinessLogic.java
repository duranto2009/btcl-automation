package coLocation.accounts;


import accounting.*;
import coLocation.demandNote.CoLocationYearlyDemandNote;
import common.ModuleConstants;
import common.bill.BillDTO;
import requestMapping.Service;
import util.CurrentTimeFactory;
import util.NumberComparator;

public class CoLocationYearlyDemandNoteBusinessLogic implements YearlyBill {
    @Service
    AccountingIncidentService accountingIncidentService;

    private AccountingIncidentBuilder createBaseIncidentBuilder(BillDTO demandNote){
        return new AccountingIncidentBuilder()
                .clientID(demandNote.getClientID())
                .dateOfRecord(CurrentTimeFactory.getCurrentTime())
                .moduleID(ModuleConstants.Module_ID_COLOCATION)
                .dateOfOccurance(CurrentTimeFactory.getCurrentTime());

    }
    @Override
    public void generate(BillDTO billDTO) throws Exception {
        CoLocationYearlyDemandNote bill = (CoLocationYearlyDemandNote) billDTO;
        if(NumberComparator.isEqual(bill.getNetPayable(), 0.00)) return;

        AccountingIncident accountingIncident = createBaseIncidentBuilder(bill)

                .credit(AccountType.COLOCATION_POWER_COST, bill.getPowerCost())
                .credit(AccountType.COLOCATION_RACK_COST, bill.getRackCost())
                .credit(AccountType.COLOCATION_OFC_COST, bill.getOfcCost())
                .credit(AccountType.COLOCATION_FLOOR_SPACE_COST, bill.getFloorSpaceCost())
                .credit(AccountType.VAT_PAYABLE_TO_NBR, bill.getVAT())
                .credit(AccountType.COLOCATION_CLOSING_OTC, bill.getClosingCost())
                .credit(AccountType.COLOCATION_RECONNECT, bill.getReconnectCost())
                .credit(AccountType.COLOCATION_UPGRADE_COST, bill.getUpgradeCost())
                .credit(AccountType.COLOCATION_DOWNGRADE_COST, bill.getDowngradeCost())

                .debit(AccountType.COLOCATION_ADJUSTABLE, bill.getAdjustmentAmount()) // client paid some amount beforehand // i am not sure debit/credit
                .debit(AccountType.COLOCATION_YEARLY_ADJUSTMENT,bill.getYearlyAdjustment()) // yearly adjustment amount
                .debit(AccountType.COLOCATION_DISCOUNT, bill.getDiscount())
                .debit(AccountType.COLOCATION_ACCOUNT_RECEIVABLE_TD, bill.getNetPayable())
                .description("Yearly Bill Generated for Client " + bill.getClientID() +  " Year (" + bill.getYear() + ")")
                .createAccountingIncident();

        accountingIncidentService.insertAccountingIncident(accountingIncident);

    }

    @Override
    public void verifyPayment(BillDTO billDTO) throws Exception {
        CoLocationYearlyDemandNote bill = (CoLocationYearlyDemandNote) billDTO;
        AccountingIncident accountingIncident = createBaseIncidentBuilder(bill)
                .credit(AccountType.ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable())
                .debit(AccountType.COLOCATION_CASH, bill.getNetPayable())
                .description("Yearly Bill Verified for Client " + bill.getClientID() + " Year (" + bill.getYear() + ")")
                .createAccountingIncident();
        accountingIncidentService.insertAccountingIncident(accountingIncident);
    }
}
