
<%@page import="net.authorize.sim.Fingerprint"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.mysql.jdbc.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="config.OnlinePaymentConfigDTO"%>
<%@page import="databasemanager.DatabaseManager"%>
<%@page import="java.sql.ResultSet"%>
<%@ include file="../includes/checkLogin.jsp" %>
<%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %><%@page import = "java.util.*" %>

<%@page import = "java.text.*" %>

<%@ page errorPage="../common/failure.jsp"%><%
String windowTitle =  "Recharge Account";
client.ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(loginDTO.getAccountID());
%>
<%

PaymentGatewayCredentialDAO paymentcredentialdao = new PaymentGatewayCredentialDAO();
String apiLoginId = request.getParameter("x_login");
//String transactionKey = paymentcredentialdao.getAuthorizedTransactionKey(clientDTO);
String transactionKey = paymentcredentialdao.getPaymentGatewayCredential(clientDTO,OnlinePaymentConfigDTO.authorizedgateway).gettransactionkey();
apiLoginId =  paymentcredentialdao.getPaymentGatewayCredential(clientDTO,OnlinePaymentConfigDTO.authorizedgateway).getApikey();
System.out.println(" The transaction key is: "+ transactionKey);
String amount  = request.getParameter("amount");
String custom  = (request.getParameter("customserverintegrate")==null)?"":request.getParameter("customserverintegrate");
String responseaddess = request.getParameter("responseaddess");

//System.out.println("The amount is "+amount);
String return_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/webpayment/authorizenetrelay.jsp";

//System.out.println(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());
String relayResponseUrl = return_url;
long sequencenumber = System.currentTimeMillis();
Random generator = new Random();
Fingerprint fingerprint = Fingerprint.createFingerprint(
	    apiLoginId,
	    transactionKey,
	    sequencenumber,  // random sequence used for creating the finger print
	    amount);

long x_fp_sequence = fingerprint.getSequence();
long  x_fp_timestamp = fingerprint.getTimeStamp();
String x_fp_hash = fingerprint.getFingerprintHash();

String action = "../rechargecard/RechargeAccount.jsp";

{
	action = "https://test.authorize.net/gateway/transact.dll";
}
%>
<script language="JavaScript" src="../scripts/util.js"></script>
<script language="javascript">
var test = "test";

function validate()
{
	return true;
}

function init()
{	
	document.getElementById("paymentauthorized").submit();
	//alert("hi");
	
};

</script>

<html><head><html:base/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Recharge Account</title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css"></head>
<body class="body_center_align" onload="init();">
<table border="0" cellpadding="0" cellspacing="0" width="1024">
  <tr style="display:none"><td width="100%"><%@ include file="../includes/header.jsp"%></td></tr>
  <tr><td width="100%">
    <table border="0" cellpadding="0" cellspacing="0" width="1024">
      <tr><td width="1024" valign="top" class="td_main_authorized" style="background-color:white;" align="center">

  <table width="100%"><tr><td width="100%">
    <table width="100%" align="right" style="font-family: Arial; font-size: 10pt; ; color: #000000 " border="0"></table></td></tr>
<tr><td>


<!-- Authorized.net Add By Joyanta -->

<div id="rechargeByAuthrizednet">
<table width="400"  style="font-family: Arial; font-size: 10pt; ; color: #000000" align="center">
  <tr>
    </tr>
</table>
<div id="recharge_option_four">
<!-- In the case of production change the action url to "https://secure.authorize.net/gateway/transact.dll" -->
<form id="paymentauthorized"  action="<%=action%>" method="post" >
<table width="400"  style="font-family: Arial; font-size: 10pt; ; color: #000000"; align="center">
        <!--DWLayoutTable-->
        <tr>
  		  <td>
		
		  </td>
		  
        </tr>
        <tr>
          <td width="200" height="30" align="center">
          		
		        <INPUT TYPE='HIDDEN' NAME='x_login' VALUE='<%=apiLoginId%>'>
			    <INPUT TYPE='HIDDEN' NAME='x_fp_sequence' id ='x_fp_sequence' VALUE='<%=x_fp_sequence%>'>
			    <INPUT TYPE='HIDDEN' NAME='x_fp_timestamp' id='x_fp_timestamp' VALUE='<%=x_fp_timestamp%>'>
			     <INPUT TYPE='HIDDEN' NAME='x_relay_url' VALUE='<%=relayResponseUrl%>'>
			     <% System.out.println("The relay response url is-------------: "+ relayResponseUrl); %>
			    <INPUT TYPE='HIDDEN' NAME='x_fp_hash' id='x_fp_hash' VALUE='<%=x_fp_hash%>'>
			    <INPUT TYPE='HIDDEN' NAME='x_version' VALUE='3.1'>
			    <INPUT TYPE='HIDDEN' NAME='x_method' VALUE='CC'>
			    <INPUT TYPE='HIDDEN' NAME='x_type' VALUE='AUTH_CAPTURE'>
			    <INPUT TYPE='HIDDEN' NAME='x_amount' id='x_amount' VALUE='<%=amount%>'>
			    <INPUT TYPE='HIDDEN' NAME='x_show_form' VALUE='payment_form'>
			    <INPUT TYPE='HIDDEN' NAME='x_test_request' VALUE='FALSE'>
			    <INPUT TYPE='HIDDEN' NAME='customserverintegrate' VALUE='<%=custom%>'>
			    <INPUT TYPE="HIDDEN" NAME="responseaddess" VALUE='<%=responseaddess%>'>
			      <% System.out.println("The relay response address is-------------: "+ responseaddess); %>
			
			    <!-- Edited For The Enable To Recharge joyanta  -->
			    
			</td>
          
        </tr>
        <tr>
        <td width="200" height="30" align="left" style="display:none"><input type="button" onclick="init()" value="Submit form"></td>
        </tr>
     
      </table>
	  </form>  
</div></div>
<!--  -->

</td></tr></table></td></tr></table></td></tr>
</table>

</body></html>
