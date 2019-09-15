<%@page import="common.repository.AllClientRepository"%>
<%@page import="common.ClientDTO"%>
<%@page import="autorecharge.AutoRechargeDAO"%>
<%@page import="autorecharge.PayPalAPICredentialsDTO"%>
<%@page language="java"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.util.*,report.*, login.*,sessionmanager.*, util.*,config.OnlinePaymentConfigDTO" %>
<%@page import="brainTree.*"%>
<%@ include file="../includes/checkLogin.jsp" %>

<%
Logger logger = Logger.getLogger("BrainTree JSP");
String failiureReason="";
String action = null;
String nonce = null;
String query = request.getParameter("q");
String device_data = "";
String clientAcountID = "";
String autoRechargeOption =null;
boolean found = false;
boolean successfullyUpdate = false;

String customerID = request.getParameter("CustomerID");
if(customerID == null){
	failiureReason="errorCode="+MD5.ERROR_INSUFFICIENT_PARAMETER;
	out.println(failiureReason);
	return;
}

ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(loginDTO.getAccountID());


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
	
}

if(query!=null &&query.equals("token")){
	String token = null;
	BraintreeApp app = new BraintreeApp();
	try{
		token = app.GenerateToken(ppCredentials.isLiveAccount, merchant_ID, public_Key, private_Key, clientAcountID);
		logger.debug("token : " + token);
		
	}
	catch(Exception e){
		logger.debug("Exception: " + e);
	}
	
	if(token != null){
		action = request.getParameter("from");
		String amount = request.getParameter("amount");
		String currency = request.getParameter("currency");
		String customserverintegrate = request.getParameter("customserverintegrate");
		if(action != null && action.equals("web")){
			request.getSession(true).setAttribute("clientToken", token);
			logger.debug("Token is: "+token);
			response.sendRedirect("../webpayment/brainTreePaymentForm.jsp?customserverintegrate=" + customserverintegrate +  "&CustomerID=" + customerID + "&amount=" + amount + "&currency=" + currency);
			
		}
		else{
			out.println(token);
			return;
		}
	}
	else{
		request.getSession(true).setAttribute(SessionConstants.FAILURE_MESSAGE, "The information of the payment Receipient Account is invalid");
		response.sendRedirect("../common/failure.jsp");
	}
}
autoRechargeOption=request.getParameter("autoRechargeStatus");
String cardNumber=request.getParameter("creditCardNumber");
if(query!=null &&query.equals("payment")){
	action = request.getParameter("from");
	if(action != null && action.equals("web")){
		nonce  = request.getParameter("payment_method_nonce");
		device_data = request.getParameter("device_data");
		logger.debug("nonce: " + nonce);
	}
	else{
		nonce = request.getParameter("nonce");
		device_data = request.getParameter("device_data");
		logger.debug("device_data: " + device_data);
		logger.debug("nonce: " + nonce);
	}
	
	String amount = request.getParameter("amount");
	String currency = request.getParameter("currency");
	
	BraintreeApp app = new BraintreeApp();
	String paymentMethodToken = "";
	String trans_ID =  app.PaymentWithNonce(ppCredentials.isLiveAccount, merchant_ID, merchant_Account_ID, public_Key, private_Key, nonce, amount, device_data);
	if(trans_ID == null){
	
		trans_ID= "failed";
	}
	if(action != null && action.equals("web")){
		customerID = clientDTO.getUserName();
		String customserverintegrate = request.getParameter("customserverintegrate");
		logger.debug("Inside api/braintree.jsp:  the customserverIntegrate is : "+ customserverintegrate);
		logger.debug("Checking Redirect address: "+"../webpayment/brainTreePayment.jsp?trans_ID=" + trans_ID + "&customserverintegrate=" + customserverintegrate + "&CustomerID=" + customerID + "&amount=" + amount + "&currency=" + currency);
		response.sendRedirect("../webpayment/brainTreePayment.jsp?trans_ID=" + trans_ID + "&customserverintegrate=" + customserverintegrate + "&CustomerID=" + customerID + "&amount=" + amount + "&currency=" + currency);	
	}
	else{
		out.println(trans_ID);
		return;
	}
}

%>
