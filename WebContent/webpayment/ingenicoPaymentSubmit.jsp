<%@ page language="java" %>
<%@page import="ingenico.GenerateSHA"%>
<%@page import="config.OnlinePaymentConfigDTO"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>
<%@page import="org.apache.log4j.Logger"%>

<%@ include file="../includes/checkLogin.jsp" %>

<%
Logger logger = Logger.getLogger( "Recharge Account JSP" );
/*PaymentGatewayCredentialDAO paymentcredentialdao = new PaymentGatewayCredentialDAO();*/
//String key = paymentcredentialdao.getPaymentGatewayCredential(clientDTO,OnlinePaymentConfigDTO.cashugateway).getsignaturekey();



String amount  = "" + (Integer.parseInt(request.getParameter("amount")) * 100);
String currency  = request.getParameter("CURRENCY");
String customserverintegrate  = request.getParameter("customserverintegrate");

logger.debug("amount: " + amount);
logger.debug("currency: " + currency);
logger.debug("customserverintegrate: " + customserverintegrate);

client.ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(loginDTO.getAccountID());
String orderID = "" + clientDTO.getUniqueID()+ ":"+ System.currentTimeMillis();
logger.debug("orderID: " + orderID);
String acceptURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+ "/webpayment/ingenicoPayment.jsp?text1=" + customserverintegrate;
//String acceptURL = "http://localhost:8080/BillingNew2/webpayment/ingenicoPayment.jsp?" + "text1=" + customserverintegrate;

PaymentGatewayCredentialDAO paymentcredentialdao = new PaymentGatewayCredentialDAO();	
OnlinePaymentConfigDTO apiinfo = paymentcredentialdao.getPaymentGatewayCredential(clientDTO, OnlinePaymentConfigDTO.ingenico);
String sign = apiinfo.getsignaturekey();
String pspID = apiinfo.getApikey();

String sha1 = "ACCEPTURL="+ acceptURL + sign + "AMOUNT="+ amount + sign + "CURRENCY=" + currency + sign + "LANGUAGE=en_US" + sign +"ORDERID=" + orderID + sign + "PSPID=" + pspID + sign;
logger.debug("sha1: " + sha1);
GenerateSHA sha = new GenerateSHA();
sha1 = sha.SHA1(sha1);

logger.debug("sha1: " + sha1);

//If test site
String action = "https://secure.ogone.com/ncol/test/orderstandard.asp";

%>

<script language="JavaScript" src="../scripts/util.js"></script>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Ingenico form submit</title>
	</head>
	
	<body>
		<form id="paymentIngenico" action="<%=action%>" method="post" >
			<INPUT type="hidden" name="ACCEPTURL" value="<%=acceptURL%>">
			<INPUT type="hidden" NAME="AMOUNT" value="<%=amount%>">
			<INPUT type="hidden" NAME="CURRENCY" value="<%=currency%>">
			<INPUT type="hidden" NAME="LANGUAGE" value="en_US">
			<INPUT type="hidden" NAME="ORDERID" value="<%=orderID%>">
			<INPUT type="hidden" NAME="PSPID" value="<%=pspID%>">
			<input type="hidden" name="SHASIGN" value="<%=sha1%>">			
  		</form>	
		
		<script type="text/javascript">
			function init(){	
				document.getElementById("paymentIngenico").submit();
				//alert("hi");	
			}
			init();
		</script>
	</body>
</html>