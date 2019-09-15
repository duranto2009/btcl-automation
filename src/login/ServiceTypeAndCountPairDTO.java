package login;

import annotation.ColumnName;

public class ServiceTypeAndCountPairDTO {
	@ColumnName("serviceTypeID")
	long serviceTypeID;
	@ColumnName("serviceCount")
	int serviceCount;
}
