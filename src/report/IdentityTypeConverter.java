package report;

import file.FileTypeConstants;

public class IdentityTypeConverter implements ColumnConvertor{
	public String convert(Object status) {
		return (status.equals(0) || status == null) ? "N/A" : FileTypeConstants.TYPE_ID_NAME.get(status);
	}
}
