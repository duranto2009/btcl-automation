<%@ page language="java"%>
<%@page import="config.OnlinePaymentConfigDTO"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>
<%@page import="org.apache.log4j.Logger"%>

<%@ include file="../includes/checkLogin.jsp" %>

<%
Logger logger = Logger.getLogger( "BrainTreePaymentForm JSP" );
String token  = null;

token = request.getSession(true).getAttribute("clientToken").toString();
if(token != null){
	request.getSession(true).removeAttribute("clientToken");
}
logger.debug(token);

if(token == null){
	//Show Error message
}
String CustomerID  = request.getParameter("CustomerID");
String amount  = "" + (Double.parseDouble(request.getParameter("amount")));
String currency  = request.getParameter("currency");
String customserverintegrate = request.getParameter("customserverintegrate");
String info = "q=payment&from=web" + "&CustomerID=" + CustomerID + "&amount=" + amount + "&currency=" + currency +"&customserverintegrate=" + customserverintegrate;

String acceptURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+ "/api/braintree.jsp?" + info;
logger.debug(acceptURL);
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" >
		<script src="../scripts/jquery-1.4.4.min.js"></script>
		<title>BrainTree Payment</title>
		
		 <style type="text/css">
			.div-box {
			    border: medium solid scrollbar;
			    border-radius: 7px;
			    width: 600px;
			    margin-left: auto;
    			margin-right: auto;
			}
			.pay{
				margin: 15px 15px 15px 70%;
				border-radius: 3px;
			}
			.header{
				 margin: auto;
				 text-align: center;
			}
    	</style>
	</head>
<body>
	<div class="div-box" style="min-height: 250px">
		<form id="checkout" method="post" action="<%= acceptURL %>">
			<div id="dropin" class="header" style="min-height: 2px;width: 100%"> 
				<h1>Payment Confirmation</h1>
			  	<p style="font-weight:bold;">
			  		<label>Total Charge :</label>
			  		<%=amount %>&nbsp;<%=currency%>
			  	</p>
		 	</div>
		 	<input id='myForm' class='pay' type='submit' value='Yes, I confirm Payment'>
		</form> 
	</div>	
   <script src="https://js.braintreegateway.com/v2/braintree.js"></script>
   <script>
  		 braintree.setup("<%=token %>", "dropin", {container: "dropin"});   
   </script>
  
  </body>
</html>