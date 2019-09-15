package report;

public class BillTypeConverter  implements ColumnConvertor {

	public String convert(Object status) {
		// return (String) status;
		String statusString = "N/A";
		if(status == null) return statusString;
		int statusInt = (int)status;
		switch (statusInt) {
		case 0:
			statusString = "Demand Note";
			break;
		case 2:
			statusString = "Monthly";
			break;
		default:
			statusString = "Unknown";
			break;
		}

		return statusString;
	}

}
