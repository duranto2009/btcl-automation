<%@page import="client.ClientDTO"%>
<%@page import="config.OnlinePaymentConfigDTO"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%@ page import="net.authorize.*" %>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="sessionmanager.SessionConstants"%>
<%@ page import="login.LoginDTO"%>
<%@ page import="util.ConformationMessage"%> 
<%@ include file="../includes/checkLogin.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Authorize.net Response</title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>
<script language="javascript">
function init()
{
	document.getElementById("billingpayment").submit();
}
</script>
</head>
<body class="body_center_align" onload="init();">
<table border="0" cellpadding="0" cellspacing="0" width="1024">
<%
String windowTitle =  "Response Authrize.net";
String responseaddress = request.getParameter("responseaddess");
//client.ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(loginDTO.getAccountID());
%>
<%
Logger logger = Logger.getLogger( "Authorize-reply.JSP" );
logger.debug("Response from Authorize.net "+responseaddress);


Map<String, String[]> parameters = request.getParameterMap();
logger.debug(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());
for(String parameter : parameters.keySet()) {

	 logger.debug("("+parameter+")");
}
if(request.getParameter("customserverintegrate")!=null)
{
	 logger.debug("The Custom id is "+request.getParameter("customserverintegrate"));
}

String login_info[]=request.getParameter("customserverintegrate").split(",");

String PIN=login_info[0];
String user_name = login_info[1];
String source_ip   = login_info[2];
String transaction_id = "";
long accountID=Long.parseLong(PIN);

LoginDTO login_dto_for_pin=new LoginDTO();
login_dto_for_pin.setAccountID(accountID);
login_dto_for_pin.setUsername(user_name);
login_dto_for_pin.setLoginSourceIP(source_ip);
client.ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(loginDTO.getAccountID());

PaymentGatewayCredentialDAO paymentcredentialdao = new PaymentGatewayCredentialDAO();

OnlinePaymentConfigDTO apiinfo = paymentcredentialdao.getPaymentGatewayCredential(clientDTO, OnlinePaymentConfigDTO.authorizedgateway);
String receiptPageUrl = "../home/index.jsp";
double tax=apiinfo.getTaxAmount();
String apiLoginId = apiinfo.getApikey();
String MD5HashKey = apiinfo.getsignaturekey();

StringBuffer receiptUrlBuffer = new StringBuffer(receiptPageUrl);
net.authorize.sim.Result result = net.authorize.sim.Result.createResult(apiLoginId,MD5HashKey, request.getParameterMap());
if(result != null) {
  receiptUrlBuffer.append("?");
  receiptUrlBuffer.append(ResponseField.RESPONSE_CODE.getFieldName()).append("=");
  receiptUrlBuffer.append(result.getResponseCode().getCode());
  receiptUrlBuffer.append("&");
  receiptUrlBuffer.append(ResponseField.RESPONSE_REASON_CODE.getFieldName()).append("=");
  receiptUrlBuffer.append(result.getReasonResponseCode().getResponseReasonCode());
  receiptUrlBuffer.append("&");
  receiptUrlBuffer.append(ResponseField.RESPONSE_REASON_TEXT.getFieldName()).append("=");
  receiptUrlBuffer.append(result.getResponseMap().get(
    ResponseField.RESPONSE_REASON_TEXT.getFieldName()));
  receiptUrlBuffer.append("&");
  receiptUrlBuffer.append(ResponseField.AMOUNT.getFieldName()).append("=");
  receiptUrlBuffer.append(result.getResponseMap().get(
    ResponseField.AMOUNT.getFieldName()));

  if(result.isApproved()) {
    receiptUrlBuffer.append("&").append(ResponseField.TRANSACTION_ID.getFieldName()
      ).append("=");
    receiptUrlBuffer.append(result.getResponseMap().get(ResponseField.TRANSACTION_ID.getFieldName()));
    transaction_id = result.getResponseMap().get(ResponseField.TRANSACTION_ID.getFieldName());
  }
  logger.debug("result is not NUll");
}
else
{
	logger.debug("result is  NUll");
}
logger.debug(receiptUrlBuffer.toString());
int  response_code = result.getResponseCode().getCode();

String paymentAmount = result.getResponseMap().get(ResponseField.AMOUNT.getFieldName());

String desc="Account Recharged by Authorized.net";
double amount=Double.parseDouble(paymentAmount);
if(tax>0)
{
	 amount-=(amount*tax)/100.0;
	 desc+="after deducting tax "+tax+"%";
}

String response_reason_text = result.getResponseMap().get(ResponseField.RESPONSE_REASON_TEXT.getFieldName());

String response_text_home_page = "";
if(response_code==1 && result.isApproved())
{
	LoginDTO login_dto_for_paypal=new LoginDTO();
	login_dto_for_paypal.setAccountID(accountID);
	login_dto_for_paypal.setUsername(user_name);
	login_dto_for_paypal.setLoginSourceIP(source_ip);
	
	
	response_text_home_page = "Congratulation,response from Authorized.net is "+response_reason_text+", your account will be rechared by USD "+amount;
	
%>

<%
}
else
{
	response_text_home_page ="Sorry,your Account can not be recharged,response from Authorized.net is "+response_reason_text;
}
%>

<tr>
<div id="redirect_to_billing_page">
<form id="billingpayment"  action="<%=request.getParameter("responseaddess") %>" method="post" >
<table width="400"  style="font-family: Arial; font-size: 10pt; ; color: #000000"; align="center">
        <!--DWLayoutTable-->
        <tr>
  		  <td>
		
		  </td>
		  
        </tr>
        <tr>
          <td width="200" height="30" align="center">
		     
			    <INPUT TYPE='HIDDEN' NAME='customserverintegrate' VALUE='<%=request.getParameter("customserverintegrate")%>'>
			    <INPUT TYPE="HIDDEN" NAME="responseText" VALUE="<%=response_text_home_page%>">
			</td>
          
        </tr>
        <tr>
        <td width="200" height="30" align="left" style="display:none"><input type="button" onclick="init()" value="Redirect Home"></td>
        </tr>
     
      </table>
	  </form>  
</div>
</tr>
</table>
</body>
</html>