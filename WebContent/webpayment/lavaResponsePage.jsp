<%@page import="webpayment.LavaCrypt"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@page import="java.util.Map"%>
<%@page import = "webpayment.CreditCardPaymentHistory" %>
<%@page import="config.OnlinePaymentConfigDTO"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>
<%@ page import="login.LoginDTO"%>
<%@ page import="rechargecard.RechargeCardBatchDAO"%>


<%

Logger logger = Logger.getLogger( "lavaResponsePage.jsp" );

boolean problemFound = true;

String returnurl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/rechargecard/RechargeAccount.jsp";
logger.debug(returnurl);

Map<String, String[]> parameters = request.getParameterMap();

for(String parameter : parameters.keySet()) {

	 logger.debug("("+parameter+")");
}



String transactionID = "";
String paymentDateTime = "";
String amount = "";
String paymentMode = "";
String status = "";
String statusCode = "";
String statusMessage = "";
String paymentTransactionID = "";
String approvalCode = "";
String checkSum = "";

String receiptNo ="";
String merchantCode = "";
String merchantOrderNo = "";
String buyerBank = "";
String buyerName = "";
String custom = "";
String clientRequestFrom = "";

String response_text_home_page = "";


transactionID = request.getParameter("TRANS_ID");
paymentDateTime = request.getParameter("PAYMENT_DATETIME");
amount = request.getParameter("AMOUNT");
paymentMode = request.getParameter("PAYMENT_MODE");
status = request.getParameter("STATUS");
statusCode = request.getParameter("STATUS_CODE");
statusMessage = request.getParameter("STATUS_MESSAGE");
paymentTransactionID = request.getParameter("PAYMENT_TRANS_ID");
approvalCode = request.getParameter("APPROVAL_CODE");
checkSum = request.getParameter("CHECKSUM");

receiptNo = request.getParameter("RECEIPT_NO");
merchantCode = request.getParameter("MERCHANT_CODE");
merchantOrderNo = request.getParameter("MERCHANT_ORDER_NO");
buyerBank = request.getParameter("BUYER_BANK");
buyerName = request.getParameter("BUYER_NAME");
custom = request.getParameter("custom");

logger.debug("TransactionID : "+ transactionID);
logger.debug("Payment datetime : "+ paymentDateTime);
logger.debug("Amount : " + amount);
logger.debug("Payment mode : "+ paymentMode);
logger.debug("Status : " + status);
logger.debug("Status code : " + statusCode);
logger.debug("Status message : "+ statusMessage);
logger.debug("Payment transaction ID : " + paymentTransactionID);
logger.debug("Approval code : " + approvalCode);
logger.debug("Checksum : "+ checkSum);
logger.debug("Custom : "+ custom);

LavaCrypt lavaCrypt = new LavaCrypt();


try{
	
	if(status.equals("1") && statusCode.equals("00|00")){
	
		
		CreditCardPaymentHistory ccPaymentHistory = lavaCrypt.getPaymentHistory(transactionID);
		
		if(ccPaymentHistory != null){
			
			if(custom != null){
				clientRequestFrom =""+custom.charAt(0);
				custom = custom.substring(1);
			}else {
				logger.debug("Custom value is null...Ignoring response.");
				response_text_home_page ="Sorry,your Account can not be recharged By LavaPay";
				lavaCrypt.updateLavaPayPaymentStatus(transactionID, 0);
				
				return;
			}
						
			if( !amount.equals(lavaCrypt.makeAmountLavaPayFormat(""+ccPaymentHistory.amount))){
				logger.debug("Request  & Response amount is mismatch:");
				logger.debug("Request amount : " + ccPaymentHistory.amount );
				logger.debug("Response amount : "+ amount);
				
				response_text_home_page ="Sorry,your Account can not be recharged By LavaPay";
				lavaCrypt.updateLavaPayPaymentStatus(transactionID, 0);
				
				return;
			}else if(!paymentMode.equals(ccPaymentHistory.currency)){
				logger.debug("Request  & Response payment mode is mismatch:");
				logger.debug("Request amount : " + ccPaymentHistory.currency );
				logger.debug("Response amount : "+ paymentMode);
				
				response_text_home_page ="Sorry,your Account can not be recharged By LavaPay";
				lavaCrypt.updateLavaPayPaymentStatus(transactionID, 0);
				
				return;
			}
		
			client.ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(ccPaymentHistory.clientAccountID);
			if(clientDTO == null){
				logger.debug("No client found of accountID : " + ccPaymentHistory.clientAccountID);
				
				response_text_home_page ="Sorry,your Account can not be recharged By LavaPay";
				lavaCrypt.updateLavaPayPaymentStatus(transactionID, 0);
				
				return;
			}
			
			PaymentGatewayCredentialDAO paymentcredentialdao = new PaymentGatewayCredentialDAO();
			OnlinePaymentConfigDTO OnlinePaymentConfigvalues = paymentcredentialdao.getPaymentGatewayCredential(clientDTO,OnlinePaymentConfigDTO.LAVAPAYGATEWAY);
			
			String MERCHANT_CODE = null;
			String PASSPHRASE = null;
			String API_KEY = null;
			double tax = 0;
			String localCheckSum = null;
			
			MERCHANT_CODE = OnlinePaymentConfigvalues.getApikey();
			PASSPHRASE = OnlinePaymentConfigvalues.gettransactionkey();
			API_KEY = OnlinePaymentConfigvalues.getsignaturekey();
			tax = OnlinePaymentConfigvalues.getTaxAmount();
			
			
			logger.debug("MERCHANT_CODE : " + MERCHANT_CODE);
			logger.debug("PASSPHRASE : " + PASSPHRASE);
			logger.debug("API_KEY : " + API_KEY);
			logger.debug("Tax : " + tax);
			
			String value = transactionID + ccPaymentHistory.currency+ lavaCrypt.makeAmountLavaPayFormat(""+ccPaymentHistory.amount)  + MERCHANT_CODE + API_KEY;
			
			localCheckSum = lavaCrypt.getCheckSum(value, PASSPHRASE);
			
			logger.debug("Locally calculated checkSum is : "+ localCheckSum);
			logger.debug("Checksum got from lavapay is : "+ checkSum);
			
			if(localCheckSum == null || !localCheckSum.equals(checkSum)){
				logger.debug("Local & response checksum mismatch");
				
				response_text_home_page ="Sorry,your Account can not be recharged By LavaPay";
				lavaCrypt.updateLavaPayPaymentStatus(transactionID, 0);
				
				return;
			}
			
			String locallyCalulatedXorCheckSum = lavaCrypt.getXorOfValue(lavaCrypt.makeAmountLavaPayFormat(""+ccPaymentHistory.amount), ccPaymentHistory.currency, PASSPHRASE, ""+ccPaymentHistory.time, transactionID, clientDTO.getCustomerID(), clientDTO.getBillingPassword());
			
			if(!locallyCalulatedXorCheckSum.equals(custom)){
				logger.debug("Locally calculated checksum & lavapay response checksum is mismatch");
				logger.debug("Fraudulent request");
				
				response_text_home_page ="Sorry,your Account can not be recharged By LavaPay";
				lavaCrypt.updateLavaPayPaymentStatus(transactionID, 0);
				
				return;
			}
			//check payment status from lavapay 
			
			String paymentStatusResponse = lavaCrypt.getPaymentStatus(transactionID, amount, PASSPHRASE, MERCHANT_CODE, API_KEY);
			
			logger.debug("Response from lavapay after staus check request with transactionID : "+transactionID );
			logger.debug("Response : "+ paymentStatusResponse);
			
			if(paymentStatusResponse == null){
				logger.debug("No payment status record response received with transactionID : "+ transactionID);
				
				response_text_home_page ="Sorry,your Account can not be recharged By LavaPay";
				lavaCrypt.updateLavaPayPaymentStatus(transactionID, 0);
				
				return;
			}
			
			String psTrans_id = null; 
			String psAmount = null;
			String psStatus = null;
			
			psTrans_id = lavaCrypt.getNodeValue(paymentStatusResponse, "trans_id");
			psAmount = lavaCrypt.getNodeValue(paymentStatusResponse, "amount");
			psStatus = lavaCrypt.getNodeValue(paymentStatusResponse, "status");
			
			logger.debug("trans_id : "+ psTrans_id);
			logger.debug("amount : "+ psAmount);
			logger.debug("status : "+ psStatus);
			
			
			if(psTrans_id == null || psAmount == null || psStatus == null){
				logger.debug("Some required values are null ");
				
				response_text_home_page ="Sorry,your Account can not be recharged By LavaPay";
				lavaCrypt.updateLavaPayPaymentStatus(transactionID, 0);
				
				return;
			}
			
			if(psTrans_id.equals(transactionID) && psAmount.equals(amount)){
				logger.debug("TransactionID & amount are matched");
				if(psStatus.equals("1")){
					logger.debug("Payment successful in lavapay side.");
					problemFound = false;
				}else if(psStatus.equals("2")){
					logger.debug("Payment failed in lavapay side.");
				}else if(psStatus.equals("3")){
					logger.debug("Payment request is still pending in lavapay side.");
					response_text_home_page ="Your payment request still in pending. Please check later.";
					
					return;
				}else if(psStatus.equals("4")){
					logger.debug("Payment request cancel in lavapay side");
					response_text_home_page ="Sorry,your Account can not be recharged By LavaPay";
					lavaCrypt.updateLavaPayPaymentStatus(transactionID, 0);
					
					return;
				}else if (psStatus.equals("0")){
					logger.debug("Attempt payment request but didn't processed fully.");
					response_text_home_page ="Sorry,your Account can not be recharged By LavaPay";
					lavaCrypt.updateLavaPayPaymentStatus(transactionID, 0);
					
					return;
				}else{
					logger.debug("Unknown payment status.");
				}
			}else{
				logger.debug("TransactionID & amount are mismatched.");
			}
			
			//if no problem found
			
			if(!problemFound){
					
				logger.debug("All security check passed successfully...");
				
				LoginDTO lavaPayLonginDTO = new LoginDTO();
				
				lavaPayLonginDTO.setAccountID(clientDTO.getAccountID());
				lavaPayLonginDTO.setUsername(clientDTO.getCustomerID());
				
				String desc="Account Recharged by LavaPay";
				
				RechargeCardBatchDAO dao=new RechargeCardBatchDAO();
				
				double AccumulatedAmount = Double.parseDouble(amount);
				if(tax>0.0)
				{
					AccumulatedAmount = Double.parseDouble(amount)-((Double.parseDouble(amount)*tax)/100);
				}
				
				dao.rechargeAccountByOnlinePaymentGateway(lavaPayLonginDTO,AccumulatedAmount,paymentTransactionID,desc);
				
				response_text_home_page = "Congratulation,Payment at LavaPay is successful,your account will be recharged with <b>"+AccumulatedAmount+"</b> MYR,including "+tax+"% Tax";
				
				lavaCrypt.updateLavaPayPaymentStatus(transactionID, 1);
			}else{
				response_text_home_page ="Sorry,your Account can not be recharged By LavaPay";
				lavaCrypt.updateLavaPayPaymentStatus(transactionID, 0);
			}
		
		
		}else {
			logger.debug("No pending or no Payment request found with transactionID : "+ transactionID);
			response_text_home_page ="Sorry,your Account can not be recharged By LavaPay";
			
			return;
		}
	}else {
		logger.debug("LavaPay payment is failed");
		response_text_home_page ="Sorry,your Account can not be recharged By LavaPay";
		
	}
}catch(Exception e){
	logger.fatal("Exception occured while processing response of lava payment ", e);
}finally{
	lavaCrypt = null;
}


%>





<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>LavaPay Response</title>
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
	onload="<%if (!clientRequestFrom.equals("1")) {%> init(); <%}%>">

	<%
		if (clientRequestFrom.equals("1")) {
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
							<td width="200" height="30" align="center"> <INPUT
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






