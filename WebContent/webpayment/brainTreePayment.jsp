<%@ page language="java" %>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.util.*,report.*, client.*, login.*,sessionmanager.*, util.*,config.OnlinePaymentConfigDTO" %>
<%@page import="config.OnlinePaymentConfigDTO"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>

<%@page import="brainTree.*"%>

<%
Logger logger = Logger.getLogger("BrainTreePayment JSP");
String paymentState = "";
String paymentCurrency = "";
double paymentAmount = 0;
String[] verifiedData = new String[3];


String response_text_home_page = "Sorry,your Account can not be recharged By BrainTree";
String customerID=request.getParameter("CustomerID");
if(customerID==null){
	response_text_home_page += "INSUFFICIENT PARAMETER";
	return;
}

ClientDTO clientDTO = ClientRepository.getInstance().getClient(customerID);
if(clientDTO==null){
	response_text_home_page += "INVALID PIN";
	return;
}

String trans_id = request.getParameter("trans_ID");
String amount = request.getParameter("amount");
String currency = request.getParameter("currency");

double amountTorecharge=0;

try{ 
	amountTorecharge=Double.parseDouble(amount);
 }
catch(Exception ee){
	response_text_home_page += "INVALID PARAMETER AMOUNT";
	return;
}

String merchant_ID = ""; 
String merchant_Account_ID = ""; 
String public_Key = "";
String private_Key = "";
String refundText = "";

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

	request.getSession(true).setAttribute(SessionConstants.FAILURE_MESSAGE, "The information of the payment Receipient Account is not settled.");
	response.sendRedirect("../common/failure.jsp");
	return;
}

try {
	BraintreeApp app = new BraintreeApp();
	
	verifiedData = app.verifyPayment(ppCredentials.isLiveAccount, trans_id, merchant_ID, public_Key, private_Key);
	
	paymentState = verifiedData[0];
	paymentAmount = Double.parseDouble(verifiedData[1]);
	paymentCurrency = verifiedData[2];
	
	logger.debug("Output Here: " +paymentState+ paymentCurrency + paymentAmount);
	
	if(paymentState.equals("SUBMITTED_FOR_SETTLEMENT") || paymentState.equals("SETTLING") || paymentState.equals("SETTLED")){
		if(paymentAmount != amountTorecharge){
			refundText = app.refund(ppCredentials.isLiveAccount, trans_id, merchant_ID, public_Key, private_Key, paymentState);
			response_text_home_page += "PAYMENT AMOUNT UNMATCHED" + refundText;
			return;
		}
	
		if(!paymentCurrency.equals(currency)){
			refundText = app.refund(ppCredentials.isLiveAccount, trans_id, merchant_ID, public_Key, private_Key, paymentState);
			response_text_home_page += "PAYMENT CURRENCY UNMATCHED" + refundText;
			return;
		}
	}
	else{
		response_text_home_page += "UNAUTHORIZED PAYMENT";
		return;
	}
}
catch (Exception e) {
	System.out.println("Exception ee:" + e.toString());
	logger.fatal("Exception at getting Payment Information:", e);
	response_text_home_page += "INTERNAL SERVER ERROR";
	return;
}

String returnurl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/rechargecard/RechargeAccount.jsp";
logger.debug("inside brain tree payment jsp: the return url is "+returnurl);

try
{	
	String login_info[] = request.getParameter("customserverintegrate").split(",");
	String PIN = login_info[0];
	String user_name = login_info[1];
	String source_ip   = login_info[2];
	long accountID = Long.parseLong(PIN);
		
	LoginDTO login_dto_for_brainTree = new LoginDTO();
	
	login_dto_for_brainTree.setAccountID(accountID);
	login_dto_for_brainTree.setUsername(user_name);
	login_dto_for_brainTree.setLoginSourceIP(source_ip);
	
	double TaxAmount = ppCredentials.getTaxAmount();
	logger.debug("TaxAmount: " + TaxAmount);	
	
	String desc="Account Recharged by BrainTree";
	
	
	double AccumulatedAmount = paymentAmount;
	
	if(TaxAmount>0.0){
		AccumulatedAmount = paymentAmount-((paymentAmount*TaxAmount)/100);
		desc+=" after deducting tax " + TaxAmount + "%";
	}
	
	
	{
		response_text_home_page = "Congratulation,Payment at BrainTree is successful,your account will be recharged with <b>"+AccumulatedAmount+"</b>" + currency + ", including "+TaxAmount+"% Tax";
	}
}	
catch (Exception e) {
	System.out.println("Exception ee:" + e.toString());
	logger.fatal("Exception at getting Payment Information:", e);
	response_text_home_page += "INTERNAL SERVER ERROR";
	return;
}	
	

%>

<script language="JavaScript" src="../scripts/util.js"></script>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" >
		<title>BrainTree response submit</title>
	</head>
	
	<body>
		<form id="BrainTreeResponse" action="<%=returnurl %>" method="post" >
			<INPUT TYPE='HIDDEN' NAME='customserverintegrate' VALUE='<%=request.getParameter("customserverintegrate")%>'>
			<INPUT TYPE="HIDDEN" NAME="responseText" VALUE="<%=response_text_home_page%>">
  		</form>	
		
		<script type="text/javascript">
			function init(){	
				document.getElementById("BrainTreeResponse").submit();
				//alert("hi");	
			}
			init();
		</script>
	</body>
</html>