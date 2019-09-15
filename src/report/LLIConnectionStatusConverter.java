package report;

import lli.connection.LLIConnectionConstants;

public class LLIConnectionStatusConverter implements ColumnConvertor{
	
	public String convert(Object status) {
		return status == null ? "Not Found" : (String) LLIConnectionConstants.connectionStatusMap.get((Integer) status);
	}

}
