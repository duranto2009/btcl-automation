package requestMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConvertor implements RequestParameterConvetor{
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
	@Override
	public Object convert(String str) {
		Date date = null;
		try {
			date =  dateFormat.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

}
