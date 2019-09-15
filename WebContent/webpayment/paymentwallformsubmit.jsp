<%@page import="TokenGenerator.generatepaymentwallToken"%>
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
<%@ page errorPage="../common/failure.jsp"%><%
	String windowTitle =  "Recharge Account";
client.ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(loginDTO.getAccountID());
%>


<%
	PaymentGatewayCredentialDAO paymentcredentialdao = new PaymentGatewayCredentialDAO();
//String transactionKey = paymentcredentialdao.getAuthorizedTransactionKey(clientDTO);
OnlinePaymentConfigDTO dto = paymentcredentialdao.getPaymentGatewayCredential(clientDTO,OnlinePaymentConfigDTO.paymentwall);
String key = dto.getApikey();
String secret = dto.getsignaturekey();


String amount  = request.getParameter("amount")+".00";
//String amount  = "9.99";

String custom  = loginDTO.getAccountID()+"|"+loginDTO.getLoginSourceIP()+"|"+amount;

String uid = loginDTO.getUsername();

String currency  = request.getParameter("currency");

String ag_name = request.getParameter("ag_name");

String ag_type  = request.getParameter("ag_type");

String success_url = request.getParameter("success_url");

String test_mode = request.getParameter("test_mode");

String sign_version = request.getParameter("sign_version");

String email = dto.getpaypalID(); //change this to the merchant id

Map<String, String> params = new LinkedHashMap<String, String>();


params.put("key", key); // available in your merchant Area
params.put("uid", uid);
params.put("widget", "p10_1");

params.put("amount", amount);
params.put("currencyCode",currency);
params.put("ag_name", ag_name);
params.put("ag_external_id",custom);
params.put("ag_type", ag_type);
params.put("sign_version",sign_version);
params.put("email", "momi@revesoft.com");
params.put("test_mode",test_mode);
params.put("success_url", success_url);
params.put("evaluation", "1");
params.put("ag_period_length", "3");


String signature = generatepaymentwallToken.calculateWidgetSignature(params, secret);
params.put("sign", signature);
String paymentwallbaseurl = "https://api.paymentwall.com/api/subscription";

String url = paymentwallbaseurl+"?"+generatepaymentwallToken.urlEncodeUTF8(params);

//Map<String, String> param = new LinkedHashMap<String, String>();

//uid=27001,pinusermomigoodsid=product301length=speriod=type=0ref=t1394080205e1b444087820c5563fb643b59636527c
//goodsid=product301length=ref=t1394080205e1b444087820c5563fb643b59636527csperiod=type=0uid=27001,pinusermomi
//goodsid=gold_membershiplength=3ref=33b5949e0c26b87767a4752a276de9570speriod=monthtype=0uid=1
System.out.println("The values are "+generatepaymentwallToken.md5Java("uid=27001,pinusermomigoodsid=product301slength=speriod=type=ref=t1394020221e1b444087820c5563fb643b59636527c"));
//System.out.println("The url is "+paymentwallbaseurl+"?"+generatepaymentwallToken.urlEncodeUTF8(params));

%>






<script language="JavaScript" src="../scripts/util.js"></script>
<script language="javascript">

function validate()
{
	return true;
}

function init()
{	
	document.getElementById("paymentPaymentwall").submit();
	//alert("hi");
	
};

</script>

<html><head><html:base/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Recharge Account By PaySafeCard</title>
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


<!-- PaymentWall Add By Joyanta -->

<div id="rechargeByPaymentWall">
<table width="400"  style="font-family: Arial; font-size: 10pt; ; color: #000000" align="center">
  <tr>
    </tr>
</table>
<div>

<form id="paymentPaymentwall"  action="<%=url %>" method="post" >

</form>  
</div></div>
<!--  -->

</td></tr></table></td></tr></table></td></tr>
</table>

</body></html>
