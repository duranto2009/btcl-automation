package lli.monthlyBill;

import lombok.Data;

@Data
public class ConnectionBandwidthBreakDown {


	double value;
	//double rate;
	double cost;
	String remark;

	//int status;
	long fromDate;
	long toDate;
	
}
