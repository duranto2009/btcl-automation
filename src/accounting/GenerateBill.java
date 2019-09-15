package accounting;

import common.bill.BillDTO;

public interface GenerateBill {
	public void generate(BillDTO billDTO) throws Exception;
}
