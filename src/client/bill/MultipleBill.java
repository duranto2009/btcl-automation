package client.bill;

import accounting.CancelBill;
import accounting.GenerateBill;
import accounting.VerifyPayment;

public interface MultipleBill extends GenerateBill,VerifyPayment,CancelBill{
	
}
