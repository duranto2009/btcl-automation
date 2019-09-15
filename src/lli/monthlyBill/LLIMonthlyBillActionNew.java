package lli.monthlyBill;

import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;


@ActionRequestMapping("lli/bill/monthly/")
public class LLIMonthlyBillActionNew {

	@Service
	LLIMonthlyBillService lliMonthlyBillService;
	
}
