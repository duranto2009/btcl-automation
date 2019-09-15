package report;

import common.RegistrantTypeConstants;

public class RegistantConverter implements ColumnConvertor{
	public String convert(Object status) {
		return (status.equals(0) || status == null) ? "N/A" : RegistrantTypeConstants.COMMON_REGISTRANT_TYPE.get(status);
	}
}
