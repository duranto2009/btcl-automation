package accounting;

import common.bill.BillDTO;
// TODO raihan:: reuse code in skip, unskip, isSkippable, isUnskippable
public interface Skip {
	 void skip(BillDTO billDTO) throws Exception;
	
	 void unskip(BillDTO billDTO) throws Exception ;
	
	 boolean isSkipable(BillDTO billDTO) throws Exception;
	
	 boolean isUnskipable(BillDTO billDTO) throws Exception;
	
}
