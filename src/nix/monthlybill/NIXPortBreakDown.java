package nix.monthlybill;

import lombok.Data;

@Data
public class NIXPortBreakDown {
	
	Long id;
	Long officeId;
	Long connectionId;
	Long portID;
	String name;
	int portType;
	double rate;
	double cost;
	String remark;
	long fromDate;
	long toDate;
	
}
