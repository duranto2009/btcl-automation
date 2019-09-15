package report;

import util.TimeConverter;

public class DateConvertor implements ColumnConvertor{

	public String convert(Object timeInMills) {
		return (String) TimeConverter.getTimeStringFromLong((long) timeInMills);
	}

}
