package payment.api;

import java.util.List;

public class PaidBillDetailsDTO {
	List<BillDTOForBankPayment> billDTOForBankPayment;
	double total;
	public List<BillDTOForBankPayment> getBillDTOForBankPayment() {
		return billDTOForBankPayment;
	}
	public void setBillDTOForBankPayment(List<BillDTOForBankPayment> billDTOForBankPayment) {
		this.billDTOForBankPayment = billDTOForBankPayment;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	
}
