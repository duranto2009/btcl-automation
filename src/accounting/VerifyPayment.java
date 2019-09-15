package accounting;

import common.bill.BillDTO;

public interface VerifyPayment {
	 void verifyPayment(BillDTO billDTO) throws Exception;
}
