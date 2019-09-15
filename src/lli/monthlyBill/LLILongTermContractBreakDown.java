package lli.monthlyBill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LLILongTermContractBreakDown {

	double value;
	double rate;
	
	long fromDate;
	long toDate;
	
}
