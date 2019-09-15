package util;

public class DateRange {
	public long fromDate;
	public long toDate;

	public static DateRange getDateRangeByFromToDate(long fromDate, long toDate) {
		DateRange dateRange = new DateRange();
		dateRange.fromDate = fromDate;
		dateRange.toDate = toDate;
		return dateRange;
	}

	@Override
	public String toString() {
		return "DateRange [fromDate=" + TimeConverter.getDateTimeStringByMillisecAndDateFormat(fromDate, "dd/MM/yyyy") +
				", toDate=" + TimeConverter.getDateTimeStringByMillisecAndDateFormat(toDate, "dd/MM/yyyy")+ "]";
	}
}
