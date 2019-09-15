package lli.bill;

import java.util.List;

import common.bill.BillDTO;
import lli.LLIMonthlyBillUnit;

public class LLIMonthlyBillDTO extends BillDTO{
	double sumOfBillUnits;
	double adjustmentAmountOfPrevMonth;
	double amountDeductedFromAdjustmentAccount;
	List<LLIMonthlyBillUnit>lliMonthlyBillUnits;
	public double getSumOfBillUnits() {
		return sumOfBillUnits;
	}
	public void setSumOfBillUnits(double sumOfBillUnits) {
		this.sumOfBillUnits = sumOfBillUnits;
	}
	public double getAdjustmentAmountOfPrevMonth() {
		return adjustmentAmountOfPrevMonth;
	}
	public void setAdjustmentAmountOfPrevMonth(double adjustmentAmountOfPrevMonth) {
		this.adjustmentAmountOfPrevMonth = adjustmentAmountOfPrevMonth;
	}
	public double getAmountDeductedFromAdjustmentAccount() {
		return amountDeductedFromAdjustmentAccount;
	}
	public void setAmountDeductedFromAdjustmentAccount(double amountDeductedFromAdjustmentAccount) {
		this.amountDeductedFromAdjustmentAccount = amountDeductedFromAdjustmentAccount;
	}
	public List<LLIMonthlyBillUnit> getLliMonthlyBillUnits() {
		return lliMonthlyBillUnits;
	}
	public void setLliMonthlyBillUnits(List<LLIMonthlyBillUnit> lliMonthlyBillUnits) {
		this.lliMonthlyBillUnits = lliMonthlyBillUnits;
	}
	@Override
	public String toString() {
		return "LLIMonthlyBillDTO [sumOfBillUnits=" + sumOfBillUnits + ", adjustmentAmountOfPrevMonth="
				+ adjustmentAmountOfPrevMonth + ", amountDeductedFromAdjustmentAccount="
				+ amountDeductedFromAdjustmentAccount + ", lliMonthlyBillUnits=" + lliMonthlyBillUnits + "]";
	}
}
