package payment.api;

import common.RequestFailureException;

public class PaymentApiException extends RequestFailureException{
	int errorCode;
	public PaymentApiException(int errorCode,String msg) {
		super(msg);
		this.errorCode = errorCode;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
