
<%@page import="client.ClientDTO"%>
<%@page import="util.GlobalConstants"%>
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
<%@page import="webpayment.*"%>
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
String receivedTx2FromCashu = request.getParameter("txt2");
int lengthCount = login_info.length;

String PIN = null; 
String user_name = null; 
String source_ip = null;
String referenceID = null;
int  fromDialer =0; 
if(lengthCount > 0){
	PIN = login_info[0];
	lengthCount--;
	logger.debug("Pin : "+ PIN );
}
if(lengthCount > 0){
	user_name = login_info[1];
	lengthCount--;
	logger.debug("User Name : "+ user_name);
}
if(lengthCount > 0){
	source_ip = login_info[2];
	lengthCount--;
	logger.debug("source IP : "+ source_ip);
}
if(lengthCount > 0){
	referenceID = login_info[3];
	lengthCount--;
	logger.debug("Reference ID : " +referenceID);
}
if(lengthCount > 0){
	String dialer = login_info[4];
	lengthCount--;
	fromDialer=Integer.parseInt(dialer);
	logger.debug("dialer :" + dialer);
}

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
String currency = request.getParameter("currency");

//logger.debug("Verifcation String "+approved_string);
logger.debug("Trn Id "+trn_id);


String response_text_home_page = "";
boolean foundAnyProblem = false;

try
{
	
	PaymentGatewayCredentialDAO paymentcredentialdao = new PaymentGatewayCredentialDAO();
	
	OnlinePaymentConfigDTO apiinfo = paymentcredentialdao.getPaymentGatewayCredential(clientDTO, OnlinePaymentConfigDTO.cashugateway);
	
	String encryptionkey = apiinfo.getsignaturekey();
	
	String merchant_id = apiinfo.getApikey();
	
	String tokenstringparameter = merchant_id+":"+trn_id+":"+encryptionkey.toLowerCase();
	
	String token = generatecashutoken.shahashvalue(tokenstringparameter);
	DataBaseAndOthersOperation dbOperation=new DataBaseAndOthersOperation();
	if(token.equalsIgnoreCase(approved_string))
	{
		//dialer changes
		
		
		if(fromDialer==1){
			logger.debug("Request from dialer...");
			String tokenValueReceivedFromCashu = request.getParameter("token");
			
			logger.debug("Received Amount from Cashu : "  + paid_money);
			logger.debug("Received Currency from Cashu : " + currency);
		
		
			CashuPaymentRequestHistory cashuPay = dbOperation.checkReferenceID(referenceID);
			
			if(cashuPay == null){
				logger.debug("No pending request is found for referenceID : " + referenceID);
				foundAnyProblem = true;
				response_text_home_page ="Sorry,your Account can not be recharged By Cashu";
				
			}else{
		
		
				double originalAmount = cashuPay.amount;
				String originalCurrency = cashuPay.currency;
				
				logger.debug("Requested Amount from Dialer : " + originalAmount);
				logger.debug("Requested Currency from Dialer : " + originalCurrency);
				
				
				String amountValue = dbOperation.buildValidCashuAmountFormat(originalAmount);
	
				logger.debug("Amount after making desired cashu format : " + amountValue);
				
				String calculateToken = dbOperation.getMD5Value((merchant_id.toLowerCase())+":"+(amountValue)+":"+(originalCurrency.toLowerCase())+":"+(encryptionkey.toLowerCase()));
				
				logger.debug("Actual send token : " + calculateToken );
				logger.debug("Received token from cashu : " + tokenValueReceivedFromCashu);
						
			
				if(!calculateToken.equals(tokenValueReceivedFromCashu)){
					logger.debug("Caculate Token & received token from cashu mismatch");
					foundAnyProblem = true;
					response_text_home_page ="Sorry,your Account can not be recharged By Cashu";
						
					dbOperation.UpdateCashuPaymentStatus(referenceID, 0); ///failure = 0 
			
				}
				if(!dbOperation.checktxt2WithOriginalRequest(amountValue, originalCurrency, receivedTx2FromCashu, clientDTO)){
					logger.debug("Calculated xor value is mismatched with txt2 value received from cashue" );
					foundAnyProblem = true;
					response_text_home_page ="Sorry,your Account can not be recharged By Cashu";
				}
			}
		}
			// dialer chagers
		
		if(!foundAnyProblem){
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
			if(tax>0.0)
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
			
			if(fromDialer==1){
				dbOperation.UpdateCashuPaymentStatus(referenceID, 1); ///success =1 
			}
			
				}
			}
			else
			{
				response_text_home_page ="Sorry,your Account can not be recharged By Cashu";	
			}
	
}catch(Exception e)
{
	response_text_home_page ="Sorry,your Account can not be recharged By Cashu";
	
	logger.error("Exception at cashu Payment "+e);
}
%>



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
<body class="body_center_align"
	onload="<%if (!dialer.equals(""+GlobalConstants.CASHUDAILERREQUSTCONFIRMATION)) {%> init(); <%}%>">

	<%
		if (dialer.equals("1")) {
	%>
	<div style="text-align: center; color: green">		
		<%=response_text_home_page%>
	</div>
	<%
		} else {
	%>
	<table border="0" cellpadding="0" cellspacing="0" width="1024">

		<tr>
			<div id="redirect_to_billing_page">
				<form id="billingpayment" action="<%=returnurl%>" method="post">
					<table width="400"
						style="font-family: Arial; font-size: 10pt;; color: #000000"
						; align="center">
						<!--DWLayoutTable-->
						<tr>
							<td></td>

						</tr>
						<tr>
							<td width="200" height="30" align="center"><INPUT
								TYPE='HIDDEN' NAME='customserverintegrate'
								VALUE='<%=request.getParameter("txt1")%>'> <INPUT
								TYPE="HIDDEN" NAME="responseText"
								VALUE="<%=response_text_home_page%>"></td>

						</tr>
						<tr>
							<td width="200" height="30" align="left" style="display: none"><input
								type="button" onclick="init()" value="Redirect Home"></td>
						</tr>

					</table>
				</form>
			</div>
		</tr>
	</table>
	<%
		}
	%>
</body>
</html>