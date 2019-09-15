package lli;

import annotation.ParseDateToMillisecond;

public class LliMonthlyBillProperty {
	@ParseDateToMillisecond(dateFormat="dd MMMM yyyy - hh:mm")
	long fromDate;
	@ParseDateToMillisecond(dateFormat="dd MMMM yyyy - hh:mm")
	long toDate;
	public long getFromDate() {
		return fromDate;
	}
	public void setFromDate(long fromDate) {
		this.fromDate = fromDate;
	}
	public long getToDate() {
		return toDate;
	}
	public void setToDate(long toDate) {
		this.toDate = toDate;
	}
	@Override
	public String toString() {
		return "LliMonthlyBillProperty [fromDate=" + fromDate + ", toDate=" + toDate + "]";
	}
	
}
