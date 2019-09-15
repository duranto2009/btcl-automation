
<%@page import="common.ClientDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="autorecharge.AutoRechargeDAO"%>
<%@page import="autorecharge.PayPalAPICredentialsDTO"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.util.*,report.*, client.*, login.*,sessionmanager.*, util.*,config.OnlinePaymentConfigDTO" %>
<%@page import="brainTree.*"%>
<%@page import="sessionmanager.SessionConstants" %>
<%@page import="java.nio.charset.Charset"%>
<%@page import="util.*"%>
<%@page import="creditcardpayment.client.*"%>
<%@ include file="../includes/checkLogin.jsp" %>
<%
Logger logger = Logger.getLogger("BrainTree JSP");

String braintreenonce = null;

String device_data = "";
long clientAcountID = -1;
String autoRechargeOption =null;
String username="BrainTreeAPI";
boolean successfullyUpdate = false;


String systemNonce = null;

ClientDTO  clientDTO = null;
String systemPassword = null;
/*
 * user parameters
 */
String result = "";
String user = null;
String password = null;
String nonce = null;
String type = null;

try{
/*
 * api inputs
 */
user = request.getParameter("user");
password = request.getParameter("password");
nonce = request.getParameter("nonce");
type = request.getParameter("type");

String paymentBy=request.getParameter("paymentBy");



/*
 * user authentication
 */
if(nonce == null ||nonce.trim().length()==0){

	result = "status="+MD5.NEW_NONCE_CREATED+",";
	result += "nonce="+MD5.getNonce();
	logger.debug("nonce="+result);
	 out.println(result);
	 return;
}

	if(nonce.equals(MD5.getNonce()))
	{
	systemNonce = MD5.getNonce();
	}
	else if(nonce.equals(MD5.getOldNonce()))
	{
	systemNonce = MD5.getOldNonce();
	}
 	 if(systemNonce==null||systemNonce.trim().length()==0)
	{
	result = "status="+ MD5.INVALID_NONCE;
	out.println(result);
	 return;	
	}   
 
if (user == null||user.length()==0){
	result = "status="+ MD5.INVALID_USER;
	out.println(result);
	 return;
}



	 clientDTO = AllClientRepository.getInstance().getClientByClientID(loginDTO.getAccountID());
	systemPassword = new String(new MD5().hash((nonce + user + clientDTO.getLoginPassword()).getBytes(Charset.forName("UTF8"))));
	logger.debug("nonce="+nonce);
	logger.debug("password="+systemPassword);

 if(password == null || !password.equals(systemPassword)){

	result = "status="+ MD5.INVALID_PASSWORD;
	out.println(result);
	return;
}



String merchant_ID = ""; 
String merchant_Account_ID = ""; 
String public_Key = "";
String private_Key = "";


config.OnlinePaymentConfigDTO ppCredentials = new config.PaymentGatewayCredentialDAO().getPaymentGatewayCredential(clientDTO, OnlinePaymentConfigDTO.braintree );
if(ppCredentials!=null)
{
	merchant_ID = ppCredentials.getpaypalID();
	merchant_Account_ID = ppCredentials.getApikey();
	public_Key = ppCredentials.gettransactionkey();
	private_Key = ppCredentials.getsignaturekey();
	
	logger.debug("info from Database: " + merchant_ID + ":" + merchant_Account_ID  + ":"  + public_Key  + ":" + private_Key);
}
else{		
	logger.fatal("ppcredential not found in braintreeAPI jsp");
}

if(type.equals("token"))
 {
	String token = null;
	BraintreeApp app = new BraintreeApp();
	try{
		token = app.GenerateToken(ppCredentials.isLiveAccount, merchant_ID, public_Key, private_Key, ""+clientAcountID);
		logger.debug("token : " + token);
		
	}
	catch(Exception e){
		logger.debug("Exception: " + e);
	}
	
	if(token != null){
		String amount = request.getParameter("amount");
		String currency = request.getParameter("currency");
		String customserverintegrate = request.getParameter("customserverintegrate");
		result = "status="+ MD5.SUCCESS+"<br/>";
		result+= "token="+ token;
	}
	else{
		result = "status="+ MD5.FAILED;
		
	}
	out.println(result);
	return;
	}
	autoRechargeOption=request.getParameter("autoRechargeStatus");
	String cardNumber=request.getParameter("creditCardNumber");
if(type.equals("payment")){
	
	braintreenonce = request.getParameter("braintreenonce");
	
	if(braintreenonce==null ||braintreenonce.trim().length()==0)
	{
		result = "status="+ MD5.INVALID_PARAMETER;
		out.println(result);
		return;	
	}
	
	
	String amountStr = request.getParameter("amount");
	double amount=0;
	if(amountStr==null|| amountStr.trim().length()==0)
	{
		result = "status="+ MD5.INVALID_PARAMETER;
		out.println(result);
		return;			
	}
	
	try
	{
		amount=Double.parseDouble(amountStr);
	}
	catch(Exception e)
	{
		result = "status="+ MD5.INVALID_PARAMETER;
		out.println(result);
		return;	
	}
	device_data = request.getParameter("device_data");

	logger.debug("device_data: " + device_data);
	logger.debug("nonce: " + braintreenonce);
	BraintreeApp app = new BraintreeApp();
	String paymentMethodToken = "";
	String[] paymentresult =  app.doPayment(ppCredentials.isLiveAccount, merchant_ID, merchant_Account_ID, public_Key, private_Key, braintreenonce, amountStr, device_data);

	if(paymentresult != null){
		String trans_ID = paymentresult[0];
		String vaultTokenFinal = paymentresult[1];
		String paymentState = paymentresult[2];
		String refundText = "";
		logger.debug("got paymentState is:"+paymentState);
		if(!(paymentState.equals("SUBMITTED_FOR_SETTLEMENT") || paymentState.equals("SETTLING") || paymentState.equals("SETTLED")))
		{
			
			result = "status="+ MD5.FAILED;
			out.println(result);
			return; 
			
		}
		
		logger.debug("at braintree payment done ...so now rcharging  the client ");
		String []accountIDs = new String[1];
		accountIDs[0] = "" + clientDTO.getUniqueID();
		String []rechargeAmounts = new String[1];
		rechargeAmounts[0] = amountStr;
		
		String[] rechargeDescriptions = new String[1];
		rechargeDescriptions[0] = trans_ID;
	
		if(paymentBy!=null && paymentBy.equalsIgnoreCase("creditcard"))
		{
			username ="BrainTree by credit card";
		}
		loginDTO.setUsername(username);
		loginDTO.setLoginSourceIP(request.getRemoteAddr());
		String creditAmounts[]=null;
		WebPaymentClient wbPaymentClient = new WebPaymentClient(""
				+ clientDTO.getUniqueID(), "", "USD",
				" ", "",
				amount+"");
		
		logger.debug("AccumulatedAmount : " + amount );
		wbPaymentClient.makePayment();	boolean isSuccessful=false;
		double currentBalance=clientDTO.getBalance()+ amount;
	
			result = "status="+ MD5.SUCCESS+"<br/>";
			result+="balance="+(currentBalance+amount)+"<br/>";
			isSuccessful=true;
		

		
				
	
		
		
	}
	
	
	

}
}
catch(Exception ex)
{
	
	logger.debug(ex.getStackTrace());
	result = "status=" + MD5.INTERNAL_SERVER_ERROR;
	out.println(result);
	return;
}

%>
