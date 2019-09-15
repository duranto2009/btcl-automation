<%@page import="client.ClientDTO"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>
<%@page import="config.OnlinePaymentConfigDTO"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="databasemanager.DatabaseManager"%>
<%@ page import="sessionmanager.SessionConstants"%>
<%@ page import="login.LoginDTO"%>
<%@ page import="util.ConformationMessage"%> 
 
<%@page import="login.LoginDTO"%>
<%@page import="javax.net.ssl.HttpsURLConnection"%>
<%@page import="java.net.MalformedURLException"%>
<%@page import="java.security.cert.Certificate"%>
<%@page import="javax.net.ssl.SSLPeerUnverifiedException"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>eprocessingNetwork Response</title>
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
Logger logger = Logger.getLogger( "Eprocesssing-Reply.JSP" );
logger.debug("Response from EprocessingNetwork "+responseaddress);
Connection cn = null;
PreparedStatement ps = null;


Map<String, String[]> parameters = request.getParameterMap();
logger.debug(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());
for(String parameter : parameters.keySet()) {

	 logger.debug("("+parameter+")");
}
if(request.getParameter("customserverintegrate")!=null)
{
	 logger.debug("The Custom id is "+request.getParameter("custom"));
}

String login_info[]=request.getParameter("custom").split(",");

String PIN=login_info[0];
String user_name = login_info[1];
String source_ip   = login_info[2];
String transaction_id = "";
long accountID=Long.parseLong(PIN);

LoginDTO login_dto_for_pin=new LoginDTO();
login_dto_for_pin.setAccountID(accountID);
login_dto_for_pin.setUsername(user_name);
login_dto_for_pin.setLoginSourceIP(source_ip);
client.ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(login_dto_for_pin.getAccountID());



boolean valid_request = false;


String paid_money = request.getParameter("amount");
String approved = request.getParameter("approved");
String https_url = "https://www.eprocessingnetwork.com/cgi-bin/tdbe/transact.pl";
String response_text_home_page = "";
URL url;
try
{
	InetAddress eprocessAddress = java.net.InetAddress.getByName("www.eprocessingnetwork.com");
	String address = eprocessAddress.getHostAddress().toString();
	String request_from = request.getRemoteHost().toString();
	if(address.equals(request_from))
	{
		valid_request = true;
		if(approved.equals("Y"))
		{
			LoginDTO login_dto_for_eprocessing=new LoginDTO();
			login_dto_for_eprocessing.setAccountID(accountID);
			login_dto_for_eprocessing.setUsername(user_name);
			login_dto_for_eprocessing.setLoginSourceIP(source_ip);
			transaction_id = request.getParameter("transid");
			String desc = "Account Recharged By eprocessingNetwork";
			PaymentGatewayCredentialDAO paymentcredentialdao = new PaymentGatewayCredentialDAO();	
			OnlinePaymentConfigDTO apiinfo = paymentcredentialdao.getPaymentGatewayCredential(clientDTO, OnlinePaymentConfigDTO.eprocessinggateway);
			double amount = Double.parseDouble(paid_money);
			double tax=apiinfo.getTaxAmount();
			if(tax>0)
			{
				 amount-=(amount*tax)/100.0;
				 desc+="after deducting tax "+tax+"%";
			}
		
			
			
		//	RechargeCardBatchDAO dao=new RechargeCardBatchDAO();
			// dao.rechargeAccountByOnlinePaymentGateway(login_dto_for_eprocessing, Double.parseDouble(paid_money),transaction_id,desc);
			
			response_text_home_page = "Congratulation,Payment at eprocessingnetwork is successful,your account will be recharged with <b>"+amount+"</b> ";
		}
		//response_text_home_page = "congrat"
		//out.write("Valid request");
	}
	else
	{
		response.sendRedirect("../");
	}
}catch(Exception e)
{
	logger.error("Exception at eprocessing_response Inetaddress "+e);
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