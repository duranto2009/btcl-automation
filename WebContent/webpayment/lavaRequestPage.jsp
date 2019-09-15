<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import = "webpayment.LavaCrypt" %> 
<%@ page import="org.apache.log4j.Logger"%>
<%@page import="config.OnlinePaymentConfigDTO"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>
<%@page import="java.util.*"%>
<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.net.URL" %>
<%@page import="java.io.*"%>
<%@ include file="../includes/checkLogin.jsp"%>

<%@ page errorPage="../common/failure.jsp"%>
<%
Logger logger = Logger.getLogger( "lavaRequestPage.jsp" );
LavaCrypt lavaCrypt = new LavaCrypt();

String amount = request.getParameter("amount");
String currency ; //= request.getParameter("currency");
String paymentRequestFrom = request.getParameter("paymentRequestFrom");
String custom = request.getParameter("custom");

currency = "MYR";

logger.debug("Amount : " + amount);
logger.debug("Currency : " + currency);
logger.debug("Payment request from : " + paymentRequestFrom);
logger.debug("Custom : "+ custom);

if(amount == null) {
	logger.debug("Amount value is null...");
	return;
}
int indexOfDecimalPoint = -1;
indexOfDecimalPoint = amount.indexOf('.');

if((amount.length()-indexOfDecimalPoint-1) > 2){
	logger.debug("Invalid amount format.. Only two digit allowed after decimal point");
}

if(paymentRequestFrom == null && (!paymentRequestFrom.equals("1") || !paymentRequestFrom.equals("2"))){
	logger.debug("Payment request from is invalid formatted...");
	return;
}

try{
	Double.parseDouble(amount);
}catch(Exception e){
	logger.fatal("Provided amount is not a double value", e);
	return;
}

String PASSPHRASE = "";//"68605fcb2507e95d3f0c7305da38f8382b07a647";
String TRAN_ID = "";//"10066";
String PAYMENT_MODE_FPX = "fpx";
String PAYMENT_MODE_MIGS = "migs";
String AMOUNT = lavaCrypt.makeAmountLavaPayFormat(""+amount);

logger.debug("Amount in lavapay format : "+ AMOUNT);

String MERCHANT_CODE = "";//"neunode";
String API_KEY = "";//"f7b316d848ca6e9151fec4cf6bc8546c";

client.ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(loginDTO.getAccountID());

logger.debug("LoginDto accountID : "+ loginDTO.getAccountID());

PaymentGatewayCredentialDAO paymentcredentialdao = new PaymentGatewayCredentialDAO();
OnlinePaymentConfigDTO OnlinePaymentConfigvalues = paymentcredentialdao.getPaymentGatewayCredential(clientDTO,OnlinePaymentConfigDTO.LAVAPAYGATEWAY);


MERCHANT_CODE = OnlinePaymentConfigvalues.getApikey();
PASSPHRASE = OnlinePaymentConfigvalues.gettransactionkey();
API_KEY = OnlinePaymentConfigvalues.getsignaturekey();

logger.debug("Tax : " + OnlinePaymentConfigvalues.getTaxAmount());
logger.debug("MERCHANT_CODE : " + MERCHANT_CODE);
logger.debug("PASSPHRASE : " + PASSPHRASE);
logger.debug("API_KEY : " + API_KEY);




String customValueWithFPX = "";
String customValueWithMIGS = "";
String checkSumWithFPX = "";
String checkSumWithMIGS = ""; 

long timeStamp = System.currentTimeMillis();

try{
	TRAN_ID = ""+lavaCrypt.insertLavaPaymentCredential(""+clientDTO.getAccountID(), "", AMOUNT, "migs", timeStamp);
	
	logger.debug("Transaction ID : "+ TRAN_ID );
	logger.debug("AccountID : "+ clientDTO.getAccountID());
	
	String valueFPX = TRAN_ID + PAYMENT_MODE_FPX+ AMOUNT  + MERCHANT_CODE + API_KEY;
	String valueMIGS = TRAN_ID + PAYMENT_MODE_MIGS+ AMOUNT  + MERCHANT_CODE + API_KEY;
	
	checkSumWithFPX = lavaCrypt.getCheckSum(valueFPX, PASSPHRASE);
	checkSumWithMIGS = lavaCrypt.getCheckSum(valueMIGS, PASSPHRASE);
	
	customValueWithFPX = paymentRequestFrom +lavaCrypt.getXorOfValue(AMOUNT, "fpx", PASSPHRASE, ""+timeStamp, TRAN_ID, clientDTO.getCustomerID(), clientDTO.getBillingPassword());
	customValueWithMIGS = paymentRequestFrom +lavaCrypt.getXorOfValue(AMOUNT, "migs", PASSPHRASE, ""+timeStamp, TRAN_ID, clientDTO.getCustomerID(), clientDTO.getBillingPassword());
	
}catch(Exception e){
	e.printStackTrace();
}


%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lavapay Payment </title>
	<style type="text/css">
	
		form, input, button[type=submit]{
			font-size: <%
			if(paymentRequestFrom != null && paymentRequestFrom.equals("1") ){%>
				25px;
			<%}else{%>
				14px;
			<%}%> 
		}
	</style>
</head>
<body>


 

		

		
				<form id="merchantRequestId" name="merchantRequest" method="POST" action="http://epaymentstg.lava.com.my/eps/request">
				
					<table align = center>
					
						<tr>
							<td width="150"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.CREDIT_AMOUNT,loginDTO)%></td>
							<td> <span><%=currency.toUpperCase() %></span> <input type= "text" name="AMOUNT" value="<%=AMOUNT%>"  readonly  ></td>
						</tr>
					
					
						<tr> 
							<td width="150">Payment Via </td>
							<td> <input type="radio" id = "PAYMENT_MODE" name="PAYMENT_MODE" value="migs" onclick="alter1(this)" checked> Credit Card<br></td>
							
						
						</tr>
						
						<tr>
							<td> </td>
							<td> <input type="radio" id = "PAYMENT_MODE" name="PAYMENT_MODE" value="fpx" onclick="alter2(this)" > Debit Account<br></td>						
							
						</tr>
						
						<tr> 
							<td> <input type="hidden" id = "TRANS_ID" name="TRANS_ID" value="<%=TRAN_ID%>"></td>
						
						</tr>
						
						<tr> 
							<td> <input type="hidden" name="MERCHANT_CODE" value="<%=MERCHANT_CODE%>"> </td>
						
						</tr>
						
						<tr> 
							<td> <input type="hidden" id="CHECKSUM" name="CHECKSUM" value="<%=checkSumWithMIGS%>"> </td>
						
						</tr>
						
						<tr> 
							<td> <input type="hidden" id ="CUSTOM" name="CUSTOM" value ="<%=customValueWithMIGS%>"> </td>
						
						</tr>
						
						<tr> 
							<td> </td>
							<td> <input type="submit" id ="submit" value="Submit"  > </td>
						
						</tr>
					    
					    
						
						
						
						
						
						
						
					</table>
				
				</form>
		










<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<script type="text/javascript">
//document.getElementById("merchantRequestId").submit();
function alter1(){
	document.getElementById("CHECKSUM").value="<%=checkSumWithMIGS%>";
	document.getElementById("CUSTOM").value = "<%=customValueWithMIGS%>";
	document.getElementById("PAYMENT_MODE").value = "<%="migs"%>";
}
function alter2(){
	document.getElementById("CHECKSUM").value="<%=checkSumWithFPX%>";
	document.getElementById("CUSTOM").value = "<%=customValueWithFPX%>";
	document.getElementById("PAYMENT_MODE").value = "<%="fpx"%>";
}


$(document).ready(function(){
    $("#submit").click(function(e){
        $.post({
        	data:{TRANS_ID: document.getElementById("TRANS_ID").value,  PAYMENT_MODE: document.getElementById("PAYMENT_MODE").value},
        	url: "paymentModeUpdate.jsp",
        	async:false,
        	success: function(result){
        		//console.log('status is updated');
        	}
        });
    });
});



</script>
</body>
</html>