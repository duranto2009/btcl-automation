package payment.api;

public class BillLedgerResponse {
	public int responseCode;
	public String TXNNumber;
	public String ExchnageCode;
	public String PhoneNumbe;
	public String BillDetails;
	@Override
	public String toString() {
		return "BillLedgerResponse [responseCode=" + responseCode
				+ ", BillDetails=" + BillDetails
				+ "]";
	}
	
}
