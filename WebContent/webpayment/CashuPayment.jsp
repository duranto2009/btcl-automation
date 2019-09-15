
<%@page import="client.ClientDTO"%>
<%@page import="java.util.Map"%>
<%@page import="TokenGenerator.generatecashutoken"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>
<%@page import="config.OnlinePaymentConfigDTO"%>

<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="databasemanager.DatabaseManager"%>
<%@ page import="sessionmanager.SessionConstants"%>
<%@ page import="login.LoginDTO"%>
<%@ page import="util.ConformationMessage"%> 
<%@ page import="rechargecard.RechargeCardBatchDAO"%> 
<%@page import="login.LoginDTO"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Cashu Response</title>
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
	String windowTitle =  "Response Cashu";
//String responseaddress = request.getParameter("responseaddess");
//client.ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(loginDTO.getAccountID());
%>
<%
	Logger logger = Logger.getLogger( "CashuPayment.jsp" );
//logger.debug("Response from Cashu "+responseaddress);


Map<String, String[]> parameters = request.getParameterMap();
String returnurl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/rechargecard/RechargeAccount.jsp";
logger.debug(returnurl);
for(String parameter : parameters.keySet()) {

	 logger.debug("("+parameter+")");
}
if(request.getParameter("txt1")!=null)
{
	 logger.debug("The Custom id is "+request.getParameter("txt1"));
}

String login_info[]=request.getParameter("txt1").split(",");

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
String approved_string = request.getParameter("verificationString");
String trn_id = request.getParameter("trn_id");

//logger.debug("Verifcation String "+approved_string);
logger.debug("Trn Id "+trn_id);


String response_text_home_page = "";

try
{
	
	PaymentGatewayCredentialDAO paymentcredentialdao = new PaymentGatewayCredentialDAO();
	
	OnlinePaymentConfigDTO apiinfo = paymentcredentialdao.getPaymentGatewayCredential(clientDTO, OnlinePaymentConfigDTO.cashugateway);
	
	String encryptionkey = apiinfo.getsignaturekey();
	
	String merchant_id = apiinfo.getApikey();
	
	String tokenstringparameter = merchant_id+":"+trn_id+":"+encryptionkey.toLowerCase();
	
	String token = generatecashutoken.shahashvalue(tokenstringparameter);
	
	if(token.equalsIgnoreCase(approved_string))
	{
		double tax = apiinfo.getTaxAmount();
		
		LoginDTO login_dto_for_cashu=new LoginDTO();
		
		login_dto_for_cashu.setAccountID(accountID);
		
		login_dto_for_cashu.setUsername(user_name);
		
		login_dto_for_cashu.setLoginSourceIP(source_ip);
		
		transaction_id = request.getParameter("trn_id");
		
		String desc="Account Recharged by Cashu";
		
		RechargeCardBatchDAO dao=new RechargeCardBatchDAO();
		
		//double amount = Double.parseDouble(paid_money);
		
		
		
		
		double amount = Double.parseDouble(paid_money);
		
		
		if(tax>0)
		{
			 amount-=(amount*tax)/100.0;
			 desc+="after deducting tax "+tax+"%";
		}
		if( clientDTO.getCustomerTypeID()==ClientDTO.CUSTOMER_TYPE_RESELLER&& clientDTO.getResellerType()==ClientDTO.RESELLER_TYPE_COMMISION_BASED)
		{
			 amount=amount*(1+clientDTO.getCommission()/100);
			 desc+=" with comission   "+clientDTO.getCommission()+"%";
		}
		dao.rechargeAccountByOnlinePaymentGateway(login_dto_for_cashu,amount,transaction_id,desc);
		
		response_text_home_page = "Congratulation,Payment at Cashu is successful,your account will be recharged with <b>"+amount+"</b> USD,including "+tax+"% Tax";
	}
	else
	{
		response_text_home_page ="Sorry,your Account can not be recharged By Cashu";
	}
	
}catch(Exception e)
{
	logger.error("Exception at cashu Payment "+e);
}
%>

<tr>
<div id="redirect_to_billing_page">
<form id="billingpayment"  action="<%=returnurl %>" method="post" >
<table width="400"  style="font-family: Arial; font-size: 10pt; ; color: #000000"; align="center">
        <!--DWLayoutTable-->
        <tr>
  		  <td>
		
		  </td>
		  
        </tr>
        <tr>
          <td width="200" height="30" align="center">
		      <INPUT TYPE='HIDDEN' NAME='customserverintegrate' VALUE='<%=request.getParameter("txt1")%>'>
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