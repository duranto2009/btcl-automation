package accounting;

import common.bill.BillDTO;

public interface CancelBill {
	 void cancelBill(BillDTO billDTO) throws Exception;
}
