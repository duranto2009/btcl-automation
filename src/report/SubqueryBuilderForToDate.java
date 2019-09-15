package report;

import java.util.Calendar;

import common.RequestFailureException;
import common.StringUtils;
import util.TimeConverter;

public class SubqueryBuilderForToDate implements SubqueryBuilder {
	@Override
	public String createSubquery(String value,  Integer... moduleID) throws RuntimeException{
		value = StringUtils.trim(value);
		if(!value.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")){
			throw new RequestFailureException("Invalid date format: "+value);
		}
		long timeInMills = TimeConverter.getTimeFromString(value);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeInMills);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		value = ""+(calendar.getTimeInMillis()-1);
		return value;
	}
}
