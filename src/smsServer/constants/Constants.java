package smsServer.constants;


public final class Constants {

	
	public static final short MESSAGE_TYPE_LOGIN_REQUEST = 7;
	public static final short MESSAGE_TYPE_ACK_LOGIN_REQUEST = 1007;
	public static final short MESSAGE_TYPE_LOGOUT_REQUEST = 8;
	public static final short MESSAGE_TYPE_ACK_LOGOUT_REQUEST = 1008;
	public static final short MESSAGE_TYPE_LOGIN_REQUEST_STATUS = 9;
	public static final short MESSAGE_TYPE_LOGIN_ACK_REQUEST_STATUS = 1009;
	public static final short MESSAGE_TYPE_SIGNUP_REQUEST_FROM_AGENT = 10;
	public static final short COMPLAIN_MESSAGE_FROM_CLIENT = 10;
	public static final short MESSAGE_TYPE_SIGNUP_STATUS_FOR_AGENT_REQUEST = 11;
	public static final short MESSAGE_TYPE_SMS_REQUEST = 0x0101;

	
	
	public static final short ATT_TYPE_PHONE_NUMBER = 1;
	public static final short ATT_TYPE_STATUS = 3;

	public static final short ATT_TYPE_PASSWORD_NONCE = 5;
	public static final short ATT_TYPE_REPONSE = 6;
	public static final short ATT_TYPE_ENCRYPTED_PASSWORD = 8;
	public static final short ATT_TYPE_IMEI = 9;

	public static final short SMS_ATT_DESTINATION_ID = 0x0102;
	public static final short SMS_ATT_SMS_CONTENT = 0x0103;
	public static final short SMS_ATT_OPERATOR_CODE_ID = 0x0104;
	public static final short SMS_ATT_USER_NAME_ID = 0X0105;
	public static final short SMS_ATT_CALLER_ID = 0X0106;

	public static final int ATT_TYPE_SIZE = 2;
	public static final int ATT_LENGTH_SIZE = 2;
	public static final int MESSAGE_HEADER_LENGTH = 4;
	public static final int ATT_HEADER_LENGTH = 4;
	public static final int INVALID_PACKET_TYPE = -101;
	public static final String TAG = "Mkhan";
	public static final boolean DEBUG = true;
	public static final int LOGIN_REQUEST_SENDING_INTERVAL_IN_MILLS = 30000;

	public static final int LOG_MESSAGE_TYPE_LOGIN_REQUEST_WITHOUT_PASSWORD = 0;
	public static final int LOG_MESSAGE_TYPE_LOGIN_REQUEST_WITH_PASSWORD = 1;
	public static final int LOG_MESSAGE_TYPE_LOGIN_RESPONSE_WITH_NONCE = 2;
	public static final int LOG_MESSAGE_TYPE_LOGIN_RESPONSE_WITH_POSITIVE_STATUS = 3;
	public static final int LOG_MESSAGE_TYPE_LOGIN_REPONSE_WITH_NEGETIVE_STATUS = 4;
	public static final int LOG_MESSAGE_TYPE_SMS_REQUEST = 5;
	public static final int LOG_MESSAGE_TYPE_NO_CONNECTIVITY = 6;
	public static final int LOG_MESSAGE_TYPE_SIGNUP_REQUEST_FROM_AGENT = 7;
	public static final int LOG_MESSAGE_TYPE_SIGNUP_REQUEST_FROM_AGENT_STATUS = 8;
	public static final int LOG_MESSAGE_TYPE_RECEIVED_SMS=9;
	
	
	
	public static final String ID_TIME_STAMP_FORMAT_SPLITER = ":";
	public static final String IMEI_PLACEHOLDER="NOTIMEI";

	
	public static final int CREDENTIAL_TYPE_BANK = 1;
	public static final int CREDENTIAL_TYPE_COMPLAIN_APP = 2;
}
