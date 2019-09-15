package report;


public class BillPaymentStatusConverter implements ColumnConvertor {

	public String convert(Object status) {
		// return (String) status;
		String statusString = "N/A";
		if(status == null) return statusString;
		int statusInt = (int)status;
		switch (statusInt) {
		case 0:
			statusString = "Unpaid";
			break;
		case 1:
			statusString = "Paid Unverified";
			break;
		case 2:
			statusString = "Paid Verified";
			break;
		}

		return statusString;
	}

}
