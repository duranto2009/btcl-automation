package config;

public class OnlinePaymentConfigDTO {
	
	public static short paypalgateway = 1;
	public static short authorizedgateway = 2;
	public static short eprocessinggateway = 3;
	/******Added For Cashu ***********/
	public static short cashugateway = 4;
	/*********************************/
	/******Added For Cashu ***********/
	public static short paymentwall = 5;
	/*********************************/
	/******Added For Ingenico ***********/
	public static short ingenico = 6;
	/******Added For braintree ***********/
	public static short braintree = 7;
	public static short googleInAppPurchase = 8;
	public static short appleInAppPurchase = 9;
	public static short worldpay =10;
	/******Added For stripe ***********/
	public static short stripe = 11;
	public static short masterCardMIG = 14;
	public static short LAVAPAYGATEWAY = 15;
	//public static short AdminUserClientType = 1;
	//public static short ResellerClientType = 2;
	///////////Added By Joyantra later ////////////////
	public static int Clinentautorechargeenable = 2;
	public static int Clientautorechargedisable = 1;
	///////////////////////////////////////////////////
	
	private long id;
	private short paymentgatewaytype;
	private String api_key = null;
	private String transaction_key = null;
	private String signature_key = null;
	private long account_id;
	private short client_type;
	public boolean autorechargeenable = false;
	private String paypalID = null;
	private String ArAmount = null;
	private String ArRechargeAtBalance = null;
	public boolean haspaypalid = false;
	
	public boolean deletedinfo = false;
	
	/*******Added For Tax Payment *************/
	private double taxamount = 0.0;
	public int isLiveAccount=1;
	/******************************************/
	public OnlinePaymentConfigDTO()
	{
		
		
	}
	public void setid(long id)
	{
		this.id = id;
	}
	public long getid()
	{
		return this.id;
	}
	public void setpaymentgatewaytype(short type)
	{
		this.paymentgatewaytype = type;
	}
	public short getpaymentgatewaytype()
	{
		return this.paymentgatewaytype;
	}
	public void setApikey(String api_key)
	{
		this.api_key = api_key;
	}
	public String getApikey()
	{
		return this.api_key;
	}
	
	public void settransactionkey(String transaction_key)
	{
		this.transaction_key = transaction_key;
	}
	public String gettransactionkey()
	{
		return this.transaction_key;
	}
	public void setsignaturekey(String signature_key)
	{
		this.signature_key = signature_key;
	}
	public String getsignaturekey()
	{
		return this.signature_key;
	}
	
	public void setaccountid(long account_id)
	{
		this.account_id = account_id;
	}
	public long getaccountid()
	{
		return this.account_id;
	}
	
	public void setClienttype(short client_type)
	{
		this.client_type = client_type;
	}
	public short getclientType()
	{
		return this.client_type;
	}
	public void setpaypalID(String paypalID)
	{
		this.paypalID = paypalID;
	}
	public String getpaypalID()
	{
		return this.paypalID;
	}
	public void setArAmount(String ArAmount)
	{
		this.ArAmount = ArAmount;
	}
	public String getArAmount()
	{
		return this.ArAmount;
	}
	public void setArRechargeAtBalance(String ArRechargeAtBalance)
	{
		this.ArRechargeAtBalance = ArRechargeAtBalance;
	}
	public String getArRechargeAtBalance()
	{
		return this.ArRechargeAtBalance;
	}
	/*******Added For Tax Payment *************/
	public void setTaxAmount(double amount)
	{
		this.taxamount = amount;
	}
	public double getTaxAmount()
	{
		return this.taxamount;
	}
	/****************************************/
}
