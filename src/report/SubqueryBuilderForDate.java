package report;


import common.RequestFailureException;
import common.StringUtils;
import util.TimeConverter;

public class SubqueryBuilderForDate implements SubqueryBuilder{

	@Override
	public String createSubquery(String value,  Integer... moduleID) throws RuntimeException{
		value = StringUtils.trim(value);
		if(!value.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")){
			throw new RequestFailureException("Invalid date format: "+value);
		}
		value = ""+TimeConverter.getTimeFromString(value);
		return value;
	}

}
