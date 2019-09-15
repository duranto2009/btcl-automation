package dataConverter.DateTime;

import dataConverter.IDataConverter;
import util.DateUtils;

public class DateToStartOfTheDayConverter implements IDataConverter {

    @Override
    public Long convert(Object data) {
        if(data instanceof Long)
        {
            Long dateTime = (Long) data;
            Long bigDateTime = 32503679999000L; //31-12-2999
            if(dateTime > bigDateTime)
                return dateTime;
            return DateUtils.getFirstHourFromDate(dateTime);
        }
        return 0L;
    }
}
