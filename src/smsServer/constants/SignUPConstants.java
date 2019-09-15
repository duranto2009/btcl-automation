package smsServer.constants;

public class SignUPConstants
{
public static final byte MESSAGE_TYPE_SIGNUP=1;
public static final byte MESSAGE_TYPE_PASSWORD_BY_CALL=2;
public static final byte MESSAGE_TYPE_PASSWORD_VERIFICATION=3;
public static final int SIGN_UP_STATUS = 4;

//by kayesh
public static final byte ACK_MESSAGE_TYPE_SIGNUP=11;
public static final byte ACK_MESSAGE_TYPE_PASSWORD_BY_CALL=12;
public static final byte ACK_MESSAGE_TYPE_PASSWORD_VERIFICATION=4;

public static final byte PHONE_NUMBER=1;
public static final byte PASSWORD=2;
public static final int STATUS = 3;
public static final int OPERATOR_CODE = 4;

public static final int SMS_REQUEST = 0x0101;
public static final int DESTINATION_ID = 0x0102;
public static final int SMS_ID = 0x0103;
public static final int OPERATOR_CODE_ID = 0x0104;
public static final int USER_NAME_ID = 0X0105;
public static final int CALLER_ID = 0X0106;


public static final String PLAY_PIN_PROMPT="your-application-pin-is";
public static final String SILENCE="silence";

public static final String iTelPinCreationSuccessMsg = "OK";

}
