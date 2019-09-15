package lli.monthlyBill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MbpsBreakDown {

	
	double value;
	
	long fromDate;
	long toDate;
	
}
