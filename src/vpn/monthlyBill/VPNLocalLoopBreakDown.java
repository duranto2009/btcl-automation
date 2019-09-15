package vpn.monthlyBill;

import lombok.Data;

@Data
public class VPNLocalLoopBreakDown {
	
	Long id;
	Long officeId;
	Long linkId;
	Long portID;
	Long vendorId;
	Long disctrictId;

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
