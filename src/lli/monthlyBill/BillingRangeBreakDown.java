package lli.monthlyBill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingRangeBreakDown {

	double fromValue;
	double toValue;
	double rate;
	
	long fromDate;
	long toDate;
	
}