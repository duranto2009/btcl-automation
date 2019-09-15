package payment.api;

public class Token {
	public int ResponseCode;
	public String TokenNumber;
	@Override
	public String toString() {
		return "Token [ResponseCode=" + ResponseCode + ", TokenNumber="
				+ TokenNumber + "]";
	}
	
}
