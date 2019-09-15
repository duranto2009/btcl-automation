package lli.monthlyBill;

import lombok.Data;

@Data
public class LLILocalLoopBreakDown {
	
	Long id;
	Long officeId;
	Long connectionId;
	Long portID;
	Long vendorId;

	String name;
	int fiberCableType;

	double value;
	double btclLength;
	double vendorLength;
	double rate;
	double cost;
	String remark;

	long fromDate;
	long toDate;
	
}
