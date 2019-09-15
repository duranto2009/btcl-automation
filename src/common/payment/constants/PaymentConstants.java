package common.payment.constants;

import java.util.HashMap;

public class PaymentConstants {
	public static final int PAYEMENT_GATEWAY_TYPE_TELETALK = 1;
	public static final int PAYEMENT_GATEWAY_TYPE_BANK = 2;
	public static final int PAYEMENT_GATEWAY_TYPE_SSL_COMMARZ = 3;
	
	public static HashMap<Integer, String> paymentGatewayIDNameMap = new HashMap<Integer, String>();
	static
	{
		paymentGatewayIDNameMap.put(PAYEMENT_GATEWAY_TYPE_TELETALK, "Teletalk");
		paymentGatewayIDNameMap.put(PAYEMENT_GATEWAY_TYPE_BANK, "Bank");
		paymentGatewayIDNameMap.put(PAYEMENT_GATEWAY_TYPE_SSL_COMMARZ, "ssl commerz");
	}
	
	
	
	public static class TELETALK
	{
		public static final String MODULE_NAME = "MODULE_NAME";
		public static final String ENTITY_NAME = "ENTITY_NAME";		
		public static final String REQUEST_TYPE = "REQUEST_TYPE";
		public static final String CUSTOMER_ID = "CUSTOMER_ID";
		public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
		public static final String END_DATE = "END_DATE";
		public static final String MOBILE_NO = "MOBILE_NO";
		public static String BTCL_TO_TELETALK_ACCEPT = "http://114.130.64.36:9999/btcl_dom/btclsend2teletalk_new.php";
		
		public static final double TELETALK_CHARGE = 0.08;
	}
	
	public static final int PAYMENT_STATUS_UNPAID = 0;
	public static final int PAYMENT_STATUS_VERIFIED = 1;
	public static final int PAYMENT_STATUS_APPROVED = 2;
	public static final int PAYMENT_STATUS_REJECTED = 5;
	
	public static HashMap<Integer, String> paymentStatus = new HashMap<Integer, String>();
	static
	{
		paymentStatus.put(PAYMENT_STATUS_UNPAID, "<label class='label label-info'>Unpaid</label>");
		paymentStatus.put(PAYMENT_STATUS_VERIFIED, "<label class='label label-warning'>Unverified Paid</label>");
		paymentStatus.put(PAYMENT_STATUS_APPROVED, "<label class='label label-success'>Paid</label>");
		paymentStatus.put(PAYMENT_STATUS_REJECTED, "<label class='label label-danger'>Rejected</label>");
	}


	public static HashMap<Integer, String> paymentStatusMap = new HashMap<Integer, String>();
	static
	{
		paymentStatusMap.put(PAYMENT_STATUS_UNPAID, "UNPAID");
		paymentStatusMap.put(PAYMENT_STATUS_VERIFIED, "UNVERIFIED PAID");
		paymentStatusMap.put(PAYMENT_STATUS_APPROVED, "PAID");
		paymentStatusMap.put(PAYMENT_STATUS_REJECTED, "REJECTED");
	}



	
	public static final int IT_DEUCTION_NONE = 0;
	public static final int IT_DEUCTION_TYPE1 = 1;
	public static final int IT_DEUCTION_TYPE2 = 2;

	public static HashMap<Integer, String> itDeductionMap = new HashMap<Integer, String>();
	static
	{
		itDeductionMap.put(IT_DEUCTION_NONE, "None ");
		itDeductionMap.put(IT_DEUCTION_TYPE1, "Type1 ");
		itDeductionMap.put(IT_DEUCTION_TYPE2, "Type 2");
	}
	
	public static final int PAYMENT_TYPE_CHECK = 1;
	public static final int PAYMENT_TYPE_PAYORDER = 2;
	public static final int PAYMENT_TYPE_RECEIPT = 3;

	public static HashMap<Integer, String> paymentTypeMap = new HashMap<Integer, String>();
	static
	{
		paymentTypeMap.put(PAYMENT_TYPE_CHECK, "Cheque");
		paymentTypeMap.put(PAYMENT_TYPE_PAYORDER, "Pay Order");
		paymentTypeMap.put(PAYMENT_TYPE_RECEIPT, "Receipt");
	}
}
