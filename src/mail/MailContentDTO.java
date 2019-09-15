package mail;
public class MailContentDTO {
	
	public static final int DISK_USAGE = 1;
	public static final int MEMORY_USAGE = 2;
	public static final int CPU_USAGE = 3;
	public static final int ASR_ALARM = 4;
	public static final int ACD_ALARM = 5;
	public static final int PDD_ALARM = 6;
	public static final int CLIENT_WISE_UNUSUAL_TRAFFIC = 7;
	public static final int DESTWISE_UNUSUAL_TRAFFIC = 8;
	public static final int CONCURRENT_CALL_FOR_A_PIN = 9;
	public static final int EXPENSIVE_ROUTE = 10;
	public static final int AMT_RECHARGE_IN_LAST_24HRS = 11;
	public static final int TERMINATION_GW_DOWN_MORETHAN = 12;
	public static final int CLIENT_WISE_DUR_INC = 13;
	public static final int DESTWISE_DUR_INC = 14;
	public static final int LOW_BALANCE_ALERT = 15;
	public static final int FORGET_PASSWORD = 16;
	public static final int UNAUTHORIZED_IP = 17;
	
	//kayesh
	public static final int OFFER_RATE = 24;
	public static final int WITHDRAW_REQUEST = 25;
	public static final int PAYPAL_ID_VERIFICATION = 35;
	public String mailSubject;
	public String mailContent;

}