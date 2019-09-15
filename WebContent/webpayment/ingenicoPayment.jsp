<%@page language="java"%>
<%@page import="ingenico.GenerateSHA"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="config.OnlinePaymentConfigDTO"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>
<%@page import="java.util.*,report.*, client.*, login.*,sessionmanager.*, util.*,config.OnlinePaymentConfigDTO" %>
<%@page import="creditcardpayment.client.*"%>

<%@ include file="../includes/checkLogin.jsp" %>

<%
Logger logger = Logger.getLogger( "ingenicoPayment JSP" );

String response_text_home_page = "";
String transaction_id = "";

String paid_money  = request.getParameter("amount");
String currency  = request.getParameter("currency");
String ncError  = request.getParameter("NCERROR");
String orderID  = request.getParameter("orderID");
String payID  = request.getParameter("PAYID");
String status  = request.getParameter("STATUS");
String approved_string  = request.getParameter("SHASIGN");

logger.debug("amount: " + paid_money);
logger.debug("currency: " + currency);
logger.debug("NCERROR: " + ncError);
logger.debug("orderID: " + orderID);
logger.debug("payID: " + payID);
logger.debug("status: " + status);
logger.debug("approved_string: " + approved_string);


Map<String, String[]> parameters = request.getParameterMap();
String returnurl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/rechargecard/RechargeAccount.jsp";
logger.debug(returnurl);


if(request.getParameter("text1")!=null)
{
	 logger.debug("The Custom id is "+request.getParameter("text1"));
}

String login_info[] = request.getParameter("text1").split(",");
logger.debug(request.getParameter("text1"));
String PIN=login_info[0];
String user_name = login_info[1];
String source_ip   = login_info[2];
long accountID = Long.parseLong(PIN);

LoginDTO login_dto_for_pin = new LoginDTO();
login_dto_for_pin.setAccountID(accountID);
login_dto_for_pin.setUsername(user_name);
login_dto_for_pin.setLoginSourceIP(source_ip);

client.ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(loginDTO.getAccountID());

try
{	
	PaymentGatewayCredentialDAO paymentcredentialdao = new PaymentGatewayCredentialDAO();	
	OnlinePaymentConfigDTO apiinfo = paymentcredentialdao.getPaymentGatewayCredential(clientDTO, OnlinePaymentConfigDTO.ingenico);
	logger.debug(" api info" +apiinfo );
	String sign = apiinfo.getsignaturekey();
	
	
	String token = "AMOUNT="+ paid_money + sign + "CURRENCY=" + currency + sign + "NCERROR=" + ncError + sign +"ORDERID=" + orderID + sign + "PAYID=" + payID + sign + "STATUS=" + status + sign;
	logger.debug("token: " + token);
	GenerateSHA sha = new GenerateSHA();
	token = sha.SHA1(token);
	logger.debug("token: " + token);
	
	if(token.equalsIgnoreCase(approved_string)){
		double tax = apiinfo.getTaxAmount();
		
		LoginDTO login_dto_for_ingenico = new LoginDTO();
		login_dto_for_ingenico.setAccountID(accountID);		
		login_dto_for_ingenico.setUsername(user_name);		
		login_dto_for_ingenico.setLoginSourceIP(source_ip);
		
		transaction_id = payID;
		
		String desc="Account Recharged by Ingenico";
		
		System.out.println("before payment");
		
		
		double amount = Double.parseDouble(paid_money);
		if(tax>0)
		{
			 amount-=(amount*tax)/100.0;
			 desc+="after deducting tax "+tax+"%";
		}
		WebPaymentClient wbPaymentClient = new WebPaymentClient(""
				+ clientDTO.getUniqueID(), "", currency,
				" ", "",
				amount+"");
		
		logger.debug("AccumulatedAmount : " + amount );
		wbPaymentClient.makePayment();	
		logger.debug("after payment");
		response_text_home_page = "Congratulation,Payment at Ingenico is successful,your account will be recharged with <b>"+amount+"</b>" + currency + ",including "+tax+"% Tax";
	}
	else{
		response_text_home_page ="Sorry,your Account can not be recharged By Ingenico";
	}
	
}
catch(Exception e){
	logger.error("Exception at Ingenico Payment "+e);
}

%>

<script language="JavaScript" src="../scripts/util.js"></script>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Ingenico response submit</title>
	</head>
	
	<body>
		<form id="IngenicoResponse" action="<%=returnurl %>" method="post" >
			<INPUT TYPE='HIDDEN' NAME='customserverintegrate' VALUE='<%=request.getParameter("txt1")%>'>
			<INPUT TYPE="HIDDEN" NAME="responseText" VALUE="<%=response_text_home_page%>">
  		</form>	
		
		<script type="text/javascript">
			function init(){	
				document.getElementById("IngenicoResponse").submit();
				//alert("hi");	
			}
			init();
		</script>
	</body>
</html>