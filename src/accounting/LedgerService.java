package accounting;

import common.RequestFailureException;

public class LedgerService {
	public void getLedgerByAccountIDAndDateRange(int accountID,long fromDate,long toDate) throws Exception{
		AccountType accountType = AccountType.getAccountTypeByAccountID(accountID);
		if(accountType == null){
			throw new RequestFailureException("No Account Found With Account ID "+accountID);
		}
		
		
		
		
	}
}
