<%@page import="TokenGenerator.generatecashutoken"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.mysql.jdbc.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="config.OnlinePaymentConfigDTO"%>
<%@page import="databasemanager.DatabaseManager"%>
<%@page import="java.sql.ResultSet"%>
<%@ include file="../includes/checkLogin.jsp" %><%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %><%@page import = "java.util.*" %>

<%@page import = "java.text.*" %>
<%@page import="net.authorize.sim.*" %>
<%@ page errorPage="../common/failure.jsp"%><%
	String windowTitle =  "Recharge Account";
client.ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(loginDTO.getAccountID());
%>


<%
	PaymentGatewayCredentialDAO paymentcredentialdao = new PaymentGatewayCredentialDAO();
//String transactionKey = paymentcredentialdao.getAuthorizedTransactionKey(clientDTO);
OnlinePaymentConfigDTO configDTO=paymentcredentialdao.getPaymentGatewayCredential(clientDTO,OnlinePaymentConfigDTO.cashugateway);
String key =configDTO.getsignaturekey();

int isTestCashu=0;

if(configDTO.isLiveAccount==0)
{
	isTestCashu=1;
}

String amount  = request.getParameter("amount")+".00";
String custom  = (request.getParameter("customserverintegrate")==null)?"":request.getParameter("customserverintegrate");
String merchat_id = request.getParameter("merchant_id");
String currency  = request.getParameter("currency");
//String token = generatetoken.md5("joyanta:10.00:usd:hastalavistababy");
String tokenstringparameter = merchat_id+":"+amount+":"+currency.toLowerCase()+":"+key.toLowerCase();
String token = generatecashutoken.md5(tokenstringparameter);
//System.out.println(tokenstringparameter);
//String test = generatecashutoken.shahashvalue(merchat_id+":"+"3992211"+":"+key.toLowerCase());
//System.out.println("Tokenized value "+test);
//System.out.println(merchat_id+":"+"3992211"+":"+key.toLowerCase());
%>






<script language="JavaScript" src="../scripts/util.js"></script>
<script language="javascript">

function validate()
{
	return true;
}

function init()
{	
	document.getElementById("paymentcahsu").submit();
	//alert("hi");
	
};

</script>

<html><head><html:base/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Recharge Account By Cashu</title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css"></head>
<body class="body_center_align" onload="init();">
<table border="0" cellpadding="0" cellspacing="0" width="1024">
  <tr style="display:none"><td width="100%"><%@ include file="../includes/header.jsp"%></td></tr>
  <tr><td width="100%">
    <table border="0" cellpadding="0" cellspacing="0" width="1024">
      <tr>
      <td width="1024" valign="top" class="td_main_authorized" style="background-color:white;" align="center">

  <table width="100%"><tr><td width="100%">
    <table width="100%" align="right" style="font-family: Arial; font-size: 10pt; ; color: #000000 " border="0"><%if(loginDTO.getWebRoot() != null){
      %><tr><td width="100%" align="center"><table style="font-family: Arial; font-size: 10pt; ; color: #000000 " width="90%" align="center">

</table></td></tr><%}%></table></td></tr>
<tr><td>


<!-- Authorized.net Add By Joyanta -->

<div id="rechargeByAuthrizednet">
<table width="400"  style="font-family: Arial; font-size: 10pt; ; color: #000000" align="center">
  <tr>
    </tr>
</table>
<div>

<form id="paymentcahsu"  action="https://www.cashu.com/cgi-bin/pcashu.cgi" method="post" >
<table width="400"  style="font-family: Arial; font-size: 10pt; ; color: #000000"; align="center">
        <!--DWLayoutTable-->
        <tr>
  		  <td>
		
		  </td>
		  
        </tr>
        <tr>
          <td width="200" height="30" align="center">
		        <input type="hidden" name="merchant_id" value="<%=merchat_id%>">
				<input type="hidden" name="token" value="<%=token%>">
				<input type="hidden" name="display_text" value="Bill Pay ItelBilling">
				<input type="hidden" name="currency" value="<%=currency%>">
				<input type="hidden" name="amount" value="<%=amount%>">
				<input type="hidden" name="language" value="en">
				<input type="hidden" name="session_id" value="<%=System.currentTimeMillis()%>">
				<input type="hidden" name="txt1" value="<%=custom%>">
				<input type="hidden" name="test_mode" value="<%=isTestCashu%>">
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
