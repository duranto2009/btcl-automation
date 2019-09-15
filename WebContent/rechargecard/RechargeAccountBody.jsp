<%@page import="client.ClientDTO"%>
<%@page import="creditcardpayment.client.PaymentStatusCheck"%>
<%@page import="config.OnlinePaymentConfigDTO"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>
<%@page import="client.ClientRepository"%>
<%@ include file="../includes/checkLogin.jsp" %>

<%@page import="org.apache.log4j.Logger"%>


<%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import = "java.util.*" %>
<%@page import = "java.text.*" %>
<%@ page errorPage="../common/failure.jsp"%>

<%
Logger logger = Logger.getLogger( "Recharge Account JSP" );

String showMenu="false";

String response_text = null;
String customerID = null;

boolean hasAuthorizeApiValue 	= true;
boolean haseprocessingvalue 	= false;
boolean mustDisablePaypal 		= false;

/*******Added For cashu ***************/
boolean hascashuApiValue  		= false;
String merchant_id 				= null;
String encryption_key          	= null;

/*******Added For paymentWall  ************/
boolean haspaymentwallApiValue  = false;
String application_key 			= null;

/*******Added For ingenico(Ogone) *********/
boolean hasingenicoApiValue  = true;

/*******Added For BrainTree ***************/
boolean hasBrainTreeApiValue  = true;
/******** Added for lavapay *********/
boolean hasLavapayApiValue = false;
String paymentRequestFrom = "2" ;
int rechargeCardAuthType =1;


response_text = (String)session.getAttribute("CARD_RECHARGE");
String rTypeParam = request.getParameter( "rType" );
if( rTypeParam != null && rTypeParam.equals( "cancel" )){
	response_text = "Payment has been cancelled." ;
}
if(response_text != null) {
	session.removeAttribute("CARD_RECHARGE");
} 

/*************Added  for authorized.net****************/
if(response_text==null){
  response_text = (request.getParameter("responseText")==null)?"":request.getParameter("responseText");
}

String response_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/rechargecard/RechargeAccount.jsp";
String response_from_online_payment = (request.getParameter("responseText")==null)?"":request.getParameter("responseText");
// vejal
//ClientDTO clientDTO = ClientRepository.getInstance().getClient(loginDTO.getAccountID() ) ;
client.ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(loginDTO.getAccountID());
	

String api_key = "";
String transaction_key = "";

short usertype = -1;
long accountid = -1;
String sql = "";

String receiverEmail=null;
String apiLoginId = null;
String transactionKey = null;
String relayResponseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/webpayment/authorizenetrelay.jsp";

String eprocessingAccountid = null;
String eprocssingResponseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/webpayment/eprocessing_response.jsp";

PaymentGatewayCredentialDAO paymentcredentialdao = new PaymentGatewayCredentialDAO();
ArrayList<OnlinePaymentConfigDTO> OnlinePaymentConfigvalues = paymentcredentialdao.getOnlinePaymentValues(clientDTO);

String paypalUsername = "";
String paypalPassword = "";
String paypalSignature = "";
double tax=0;
boolean allPayPalConfigured = true;
int isLivePaymentWall=0;
int isTestCashu=1;

for(int i=0;i<OnlinePaymentConfigvalues.size();i++)
{
	
	tax = OnlinePaymentConfigvalues.get(i).getTaxAmount();
	if(OnlinePaymentConfigvalues.get(i).getpaymentgatewaytype() == OnlinePaymentConfigDTO.authorizedgateway){
		apiLoginId = OnlinePaymentConfigvalues.get(i).getApikey();
		transactionKey = OnlinePaymentConfigvalues.get(i).gettransactionkey();
	
		hasAuthorizeApiValue = true;
	}
	else if(OnlinePaymentConfigvalues.get(i).getpaymentgatewaytype() == OnlinePaymentConfigDTO.eprocessinggateway){
		eprocessingAccountid = OnlinePaymentConfigvalues.get(i).getApikey();
		haseprocessingvalue = true;
	}
	else if(OnlinePaymentConfigvalues.get(i).getpaymentgatewaytype() == OnlinePaymentConfigDTO.paypalgateway){
		receiverEmail = OnlinePaymentConfigvalues.get(i).getpaypalID();
		paypalUsername = OnlinePaymentConfigvalues.get(i).getApikey(); 
		paypalPassword = OnlinePaymentConfigvalues.get(i).gettransactionkey();
		paypalSignature = OnlinePaymentConfigvalues.get(i).getsignaturekey();	

		if( paypalUsername != null && !paypalUsername.isEmpty() && paypalPassword != null && !paypalPassword.isEmpty() && paypalSignature != null && !paypalSignature.isEmpty()){
			allPayPalConfigured = true;
			System.out.println("Paypal ----------------- ok now");
		}
	}
	/*********Added For Cashu  ***********************/	
	
	else if(OnlinePaymentConfigvalues.get(i).getpaymentgatewaytype() == OnlinePaymentConfigDTO.cashugateway){
		merchant_id = OnlinePaymentConfigvalues.get(i).getApikey();
		hascashuApiValue = false;	
		isTestCashu=(OnlinePaymentConfigvalues.get(i).isLiveAccount==1)?0:1;
	}
	
	/*********Added For PaymentWall  ***********************/	
	else if(OnlinePaymentConfigvalues.get(i).getpaymentgatewaytype() == OnlinePaymentConfigDTO.paymentwall){
		application_key = OnlinePaymentConfigvalues.get(i).getApikey();
		haspaymentwallApiValue = false;		
		isLivePaymentWall=OnlinePaymentConfigvalues.get(i).isLiveAccount;
	}
	
	/*********Added For Ingenico  ***********************/
	
	else if(OnlinePaymentConfigvalues.get(i).getpaymentgatewaytype() == OnlinePaymentConfigDTO.ingenico){
		hasingenicoApiValue = true;		
	}
	
	/*********Added For BrainTree  ***********************/
	
	else if(OnlinePaymentConfigvalues.get(i).getpaymentgatewaytype() == OnlinePaymentConfigDTO.braintree){
		hasBrainTreeApiValue = true;
	//	System.out.println("Inside Braintreee------------------------------");
		customerID =""+ clientDTO.getUniqueID();
	}
	
	 /*********Added For LavaPay  ***********************/	
		else if(OnlinePaymentConfigvalues.get(i).getpaymentgatewaytype() == OnlinePaymentConfigDTO.LAVAPAYGATEWAY){
			hasLavapayApiValue = true ;
		}
		
}

customerID =""+ clientDTO.getUniqueID();
//System.out.println("Customer Id----------------"+customerID);
String amount = "0.0";
long x_fp_sequence = 0;
long x_fp_timestamp =0;
String x_fp_hash = "";
String custom=loginDTO.getUserID()+","+loginDTO.getUsername()+","+loginDTO.getLoginSourceIP();


/****************End added  for Authorized.net******************************/
if(request.getParameter("showMenu")!=null){
	showMenu="true";
}

	String retunurl=  request.getRequestURL().toString();
	 int indexOfRechargeCard=retunurl.indexOf("rechargecard/");
	 if(indexOfRechargeCard>-1){
		 retunurl=retunurl.substring(0,indexOfRechargeCard);
		 retunurl=retunurl+"webpayment/paypal-reply.jsp";
	 }
	
	String windowTitle =  "Recharge Account";
	String currencyName= "USD";
	boolean disablePaypal=true; 
	
	
	
	
	
	
	//Recharge status checking;
		/****************************************************/
		  String msg = "";
		  String transactionID = null;
		  int paymentStatus = -1;
		  int secondCount = 0;
		  String checkPaymentStatus = null; ;
		
		  String currency = "USD" ; 
		  
		  
		  

		  try
		  {
			checkPaymentStatus = (String)session.getAttribute("checkPaymentStatus");
			
			if(checkPaymentStatus!=null && "yes".equals(checkPaymentStatus)){
				
				transactionID = (String) session.getAttribute("transactionID");
				if(transactionID != null){
					
					PaymentStatusCheck paymentStatusCheck = new PaymentStatusCheck();
					do{					
						paymentStatus =paymentStatusCheck.getPaymentStatus(transactionID);
						Thread.sleep(1000);
						if(secondCount++ == 60){
							break;
						}
					}while(paymentStatus != 1);
					
					
					if(paymentStatus == 1){
						response_text = "Congratulation! Your Account is "+ paymentStatusCheck.getPaymentDescription(clientDTO);
					}else if(paymentStatus == 0){
						response_text = "Sorry...Payment failed";
					}else if(paymentStatus == 2){
						response_text = "Payment is pending...";
					}
				}
				
				session.removeAttribute("checkPaymentStatus");
			}
			
		  }
		  catch(Exception ex)
		  {
				logger.fatal("Problem occured while payment status checking", ex);
		  }
		//  logger.debug("Status check");
		 
%>

<script language="JavaScript" src="../scripts/util.js"></script>
<script language="JavaScript">
var enableauthorize = false;
var enableeprocess = false;
var enablecashu   = false;
var enablepaymentwall = false;
var enableingenico = false;
var enableBrainTree = false;

var contextPath = "<%= request.getContextPath() %>";

var isRefPaymentConfigured = <%= allPayPalConfigured  %>;

var disablePaypal = false;
var paymentType=1;

	function generate_year()                    
    {
        for (var i = 2014; i <= 2030; i++){
            document.write ("<option value='" + i + "'>" + i + "</option>");
        }   
    }

    function val_cc () 
    {          
    	var expiry_month = document.getElementById("expiry_month").value;
        var expiry_year = document.getElementById("expiry_year").value;
        var today = new Date();
        var expiry_date = today.setFullYear(expiry_year, expiry_month);

        if (today.getTime() > expiry_date.getTime()){
            alert ("Expiry month and year cannot be blank/Expiry month and year is before today month and year.");
            return false;
        }
	}
        
	function checkforonlinecheck(val)
	{
		paymentType=val;
		
		
		 if(paymentType==2){
			document.getElementById("onlineRecharge").style.display="";
			document.getElementById("recharge_option_rechargeByPIN").style.display="none";
			document.getElementById("rechargeCardInfo").style.display="none";
			document.getElementById("rechargeByCreditCard").style.display="none";
			}
		else if(paymentType==3){
			document.getElementById("recharge_option_rechargeByPIN").style.display="";			
			document.getElementById("onlineRecharge").style.display="none";
			document.getElementById("rechargeCardInfo").style.display="none";
			document.getElementById("rechargeByCreditCard").style.display="none";
			}	
		
	}
	
	function onlinepaymentaction()
	{		
		var frm = document.getElementById('payment') ;
		if(frm){
			
			
			
		   try{
			   <%if(!mustDisablePaypal){%>
			   if(document.getElementById('paypalcheckedin').checked)
			   {
				   enableauthorize = false;
				   enableeprocess = false;
				   frm.action = 'https://www.sandbox.paypal.com/cgi-bin/webscr' ;
				   document.getElementById("onlinepaymentparams").innerHTML= "";
				   
				   var innerhtmlcode = 
				   '<input type="hidden" name="cmd" value="_xclick">'+
				   '<input type="hidden" name="business" value="<%=receiverEmail%>">'+
				   '<input type="hidden" name="item_name" value="Recharge Account">'+
				   '<input type="hidden" name="quantity" value="1">'+
				   '<input type="hidden" name="recurring" value="1">'+
				   '<input type="hidden" name="return" value="<%=request.getRequestURL()%>">'+
				   '<input type="hidden" name="notify_url" value="<%=retunurl%>">'+
				   '<input type="hidden" name="shipping" value="0.00">'+
				   '<input type="hidden" name="no_shipping" value="1">'+
				   '<input type="hidden" name="no_note" value="1">'+
				   '<input type="hidden" name="currency_code" value="<%=currencyName%>">'+
				   '<input type="hidden" name="tax" value="0.0">'+
				   '<input type="hidden" name="lc" value="GB">'+
				   '<input type="hidden" name="bn" value="PP-BuyNowBF">'+
				   '<input type="hidden" name="src" value="1">'+
				   '<input type="hidden" name="sra" value="1">';
				   
				   document.getElementById("onlinepaymentparams").innerHTML = innerhtmlcode;				   
				   
				   <%if(disablePaypal && ( ! allPayPalConfigured  ) )
				   {%>
				   document.getElementById("submit_online_payment").disabled = "disabled";
				   <%}%>
					   
			   }
			   
		   <%} %>
			   
		   }catch(e){}
		   
		   
		   <%if(hasAuthorizeApiValue){%>
		   
			   if(document.getElementById('authorizecheckedin').checked)
			   {
				   enableauthorize = true;
				   enableeprocess = false;
				   enablecashu    = false;
				   enablepaymentwall = false;
				   frm.action = '../webpayment/authorizedformsubmit.jsp' ;
				   document.getElementById("onlinepaymentparams").innerHTML= "";
				   var innerhtmlcode = 
					   '<INPUT TYPE="HIDDEN" NAME="x_login" VALUE="<%=apiLoginId%>">'+
					   '<INPUT TYPE="HIDDEN" NAME="x_fp_sequence" id ="x_fp_sequence" VALUE="<%=x_fp_sequence%>">'+
					   '<INPUT TYPE="HIDDEN" NAME="x_fp_timestamp" id="x_fp_timestamp" VALUE="<%=x_fp_timestamp%>">'+
					   '<INPUT TYPE="HIDDEN" NAME="x_relay_url" VALUE="<%=relayResponseUrl%>">'+
					   '<INPUT TYPE="HIDDEN" NAME="x_relay_response" VALUE="TRUE"/>'+
					   '<INPUT TYPE="HIDDEN" NAME="x_fp_hash" id="x_fp_hash" VALUE="<%=x_fp_hash%>">'+
					   '<INPUT TYPE="HIDDEN" NAME="x_version" VALUE="3.1">'+
					   '<INPUT TYPE="HIDDEN" NAME="x_method" VALUE="CC">'+
					   '<INPUT TYPE="HIDDEN" NAME="x_type" VALUE="AUTH_CAPTURE">'+
					   '<INPUT TYPE="HIDDEN" NAME="x_amount" id="x_amount" VALUE="<%=amount%>">'+
					   '<INPUT TYPE="HIDDEN" NAME="x_show_form" VALUE="payment_form">'+
					   '<INPUT TYPE="HIDDEN" NAME="x_test_request" VALUE="FALSE">'+
					   '<INPUT TYPE="HIDDEN" NAME="customserverintegrate" VALUE="<%=custom%>">'+
					   '<INPUT TYPE="HIDDEN" NAME="responseaddess" VALUE="<%=response_url%>">';					   
					   
					   document.getElementById("onlinepaymentparams").innerHTML = innerhtmlcode;
			   }
			   else{
				   enableauthorize = false;				   
				}
		   <%}%>
		   
		   <%if(hascashuApiValue){%>
		   if(document.getElementById('cashucheckedin').checked){
			   enableauthorize = false;
			   enableeprocess = false;
			   enablecashu    = true;
			   enablepaymentwall = false;
			   frm.action = '../webpayment/cashuformsubmit.jsp' ;
			   document.getElementById("onlinepaymentparams").innerHTML= "";
			   var innerhtmlcode = 
					'<input type="hidden" name="merchant_id" value="<%=merchant_id%>">'+
					'<input type="hidden" name="display_text" value="Recharge By CASHU">'+
					'<input type="hidden" name="currency" value="<%=currencyName%>">'+
					'<input type="hidden" name="language" value="en">'+
					'<input type="hidden" name="session_id" value="<%=System.currentTimeMillis() %>">'+
					'<input type="hidden" name="customserverintegrate" value="<%=custom%>">'+
					'<input type="hidden" name="test_mode" value="<%=isTestCashu%>">'+
					'<input type="hidden" name="customserverintegrate" value ="<%=custom%>">';			   
				   
				   document.getElementById("onlinepaymentparams").innerHTML = innerhtmlcode;			  
	
		   }
		   else{
			   enablecashu = false; 
			}
		   
		   <%}%>
		   
		   <%if(haseprocessingvalue){%>
		   
			   if(document.getElementById('eprocessingcheckedin').checked){
				   enableeprocess = true;
				   enableauthorize = false;
				   enablecashu     = false;
				   enablepaymentwall = false;
				   var amount = document.getElementById("amount").value;
				   //alert(amount);
				   frm.action = 'https://www.eProcessingNetwork.com/cgi-bin/dbe/order.pl' ;
				   document.getElementById("onlinepaymentparams").innerHTML= "";
				   var innerhtmlcode = 
					   '<INPUT TYPE="HIDDEN" NAME="ePNAccount" VALUE="<%=eprocessingAccountid%>">'+
					   '<INPUT TYPE="HIDDEN" NAME="Total" VALUE='+amount+'>'+
					   '<INPUT TYPE="HIDDEN" NAME="ID" VALUE="1234567890">'+
					   '<INPUT TYPE="HIDDEN" NAME="ReturnApprovedURL" VALUE="<%=eprocssingResponseUrl%>">'+
					   '<INPUT TYPE="HIDDEN" NAME="ReturnDeclinedURL" VALUE="<%=eprocssingResponseUrl%>">'+
					   '<INPUT TYPE="HIDDEN" NAME="BackgroundColor" VALUE="white">'+
					   '<INPUT TYPE="HIDDEN" NAME="TextColor" VALUE="black">'+
					   '<INPUT TYPE="HIDDEN" NAME="Redirect" VALUE="1">'+
					   '<INPUT TYPE="HIDDEN" NAME="custom" VALUE="<%=custom%>">'+
					   '<INPUT TYPE="HIDDEN" NAME="responseaddess" VALUE="<%=response_url%>">';
					   
					   
					   document.getElementById("onlinepaymentparams").innerHTML = innerhtmlcode;
			   }
			   else{
				   enableeprocess = false;
			   }
		   <%}%>
		   
		   <%if(haspaymentwallApiValue){%>
		   
		   if(document.getElementById('paymentwallcheckedin').checked){			   
			   enableeprocess = false;
			   enableauthorize = false;
			   enablecashu     = false;
			   enablepaymentwall = true;
			   var amount = document.getElementById("amount").value;
			   //alert(amount);
			   frm.action = '../webpayment/paymentwallformsubmit.jsp' ;
			   document.getElementById("onlinepaymentparams").innerHTML= "";
			 
			   var innerhtmlcode = 
				   '<INPUT TYPE="HIDDEN" NAME="ag_type" VALUE="fixed">'+
				   '<input type="hidden" name="ag_name" value="Product">'+
				   '<INPUT TYPE="HIDDEN" NAME="sign_version" VALUE="2">'+
				   '<INPUT TYPE="HIDDEN" NAME="test_mode" VALUE="<%=isLivePaymentWall%>">'+
				   '<INPUT TYPE="HIDDEN" NAME="success_url" VALUE="<%=response_url%>">'+
				   '<INPUT TYPE="HIDDEN" NAME="evaluation" VALUE="1">'+
				   '<INPUT TYPE="HIDDEN" NAME="customserverintegrate" VALUE="<%=custom%>">'+
				   '<input type="hidden" name="currency" value="<%=currencyName%>">';			   
				   
				   document.getElementById("onlinepaymentparams").innerHTML = innerhtmlcode;
		   }
		   else{
			   enablepaymentwall = false;
		   }
	   <%}%>
	   
	   <%if(hasingenicoApiValue){%>				   
		if(document.getElementById('ingenicocheckedin').checked){					   
	   		enableeprocess = false;
		   	enableauthorize = false;
		   	enablecashu     = false;
		   	enablepaymentwall = false;
		   	enableingenico = true;
		   	
		   	//alert(amount);
		   	frm.action = '../webpayment/ingenicoPaymentSubmit.jsp' ;
		  	document.getElementById("onlinepaymentparams").innerHTML= "";
		 
		   	var innerhtmlcode = 
		   		'<INPUT type="hidden" name="CURRENCY" value="<%=currencyName%>">'+
				'<INPUT type="hidden" name="customserverintegrate" value="<%=custom%>">';
		   					   
			document.getElementById("onlinepaymentparams").innerHTML = innerhtmlcode;
	   }
	   else{
		   enableingenico = false;
	   }
	  <%}%>	
	  
	  <%if(hasBrainTreeApiValue){%>				   
		if(document.getElementById('brainTreecheckedin').checked){					   
	   		enableeprocess = false;
		   	enableauthorize = false;
		   	enablecashu     = false;
		   	enablepaymentwall = false;
		   	enableingenico = false;
		   	enableBrainTree = true;
		   	
		   	var action = "../api/braintree.jsp";
		   	frm.action = action.toString();
		  	document.getElementById("onlinepaymentparams").innerHTML= "";
		 
		   	var innerhtmlcode = 
		   		'<INPUT type="hidden" name="from" value="web">'+
		   		'<INPUT type="hidden" name="q" value="token">'+
		   		'<INPUT type="hidden" name="CustomerID" value="<%=customerID%>">'+
		   		'<INPUT type="hidden" name="currency" value="<%=currencyName%>">'+
				'<INPUT type="hidden" name="customserverintegrate" value="<%=custom%>">';
		   					   
			document.getElementById("onlinepaymentparams").innerHTML = innerhtmlcode;
	   }
	   else{
		   enableBrainTree = false;
	   }
	  <%}%>
	   <%if(hasLavapayApiValue){%>
	   if(document.getElementById('lavapaycheckedin').checked){
		   enableauthorize = false;
		   enableeprocess = false;
		   enablecashu    = false;
		   enablepaymentwall = false;
		   enablelavapay = true;
		   frm.action = '../webpayment/lavaRequestPage.jsp' ;
		   document.getElementById("onlinepaymentparams").innerHTML= "";
		   var innerhtmlcode = 
				'<input type="hidden" name="amount" value="<%=amount%>">'+		
				'<input type="hidden" name="paymentRequestFrom" value="<%=paymentRequestFrom%>">'+
				'<input type="hidden" name="currency" value="<%=currencyName%>">';
				
			   
			   document.getElementById("onlinepaymentparams").innerHTML = innerhtmlcode;			  

	   }
	   else{
		   enablelavapay = false; 
		}
	   
	   <%}%>  
		   //frm.action = '../rechargecard/authorizedformsubmit.jsp' ;
		}
	}
	
	function setdefaultvalue()
	{
		document.getElementsByName("onlinepaymentOption")[0].checked = true;
	}
	/*************************************************/


	function init() {
		/*********Added  ********************/ 
		try{
			setdefaultvalue();
			onlinepaymentaction();
		}catch(e) {

		}
		checkforonlinecheck();
		/**********************************************/
		
		document.all.paymentOption[0].click();
		
		<%if(clientDTO!=null )
		{%>
		try{
			document.getElementById("rechargeByRechargeCard").style.display="none";
			document.getElementById("rechargeByPapPal").style.display.disabled="true";
		}catch(e) {}
		<%}%>

		

	}

	function validate() {

		if (document.all.paymentOption[1].checked) {

			// PayPal
			//alert("This is 1");
			var ob = document.all.amount.options[document.all.amount.selectedIndex];
			if (isEmpty(ob.value)) {

				alert("Please enter Recharge Amount");
				ob.value = "";
				ob.focus();
				return false;
			}

			if (!isNum(ob.value)) {
				alert("Rechage Amount Must be a Number");
				ob.value = "";
				ob.focus();
				return false;
			}

			if (ob.value <= 0) {
				alert("Rechage Amount Must be a  Positive Number");
				ob.value = "";
				ob.focus();
				return false;
			}


			if(enableeprocess || enableauthorize || enablecashu || enablepaymentwall || enableingenico || enableBrainTree) return true;
			if (isRefPaymentConfigured &&  isRefPaymentEnabled && ( ! isRefPaymentActivated )  ) {
			
				// Set Up Auto Recharge
				window.location = contextPath +  "/setReferencePayment.do?amount=" + ob.value ;
				return false;
			}else if ( disablePaypal )  {
				window.location = contextPath +  "/setReferencePayment.do?amount=" + ob.value + "&refNotRequired=true" ;
				alert( "Ref Not Required" )	;
				return false;
			
			}

		}else{
			
		}

		return true; 
	}

</script>


<html:base/>
<div class="col-md-12">
<div class="box">
<div class="box-header with-border text-center"><span style="vertical-align: sub;">Payment Option</span></div>
<div class="box-body">
<table border="0" cellpadding="0" cellspacing="0" width="1024">
 
 	<tr>
 	 	<td width="100%">
 	 		<table border="0" cellpadding="0" cellspacing="0" width="1024" id="AutoNumber2">
 	 			<tr>
 	 			
	             
	              <td width="820" valign="top" class="td_main" align="center">
	                 
	              
	              <td width="1024" valign="top" class="td_main" align="center">
	             
	       
	            <td width="820" valign="top" class="td_main" align="center">
	           
                      			
          		<!--main -->
          			<table border="0" cellpadding="0" cellspacing="0" width="100%">
          			 	<tr><td width="100%" align="center">
          			 		<table border="0" cellpadding="0" cellspacing="0" class="form1"  width="700" align="center">
                        		<tr>
                      				<td width="100%" align="center" style="padding-bottom: 20px;">
                        			</td>
                    			</tr>
                    			<%if (response_text!=null){ %>
                    			<tr>
                      				<td width="100%" align="center" style="padding-bottom: 20px;">
                        				<div class="div_title"><span style="vertical-align: sub;"><%=(response_text)%></span></div>
                      				</td>
                    			</tr>
                    			<%} %>
							

								<tr id="rechargeByPapPal">
								<td>
									<table width="600"  style="font-family: Arial; font-size: 10pt; ; color: #000000" align="center">
  										<tr>
    										<td>
      										<label><input type="radio" name="paymentOption" onClick="checkforonlinecheck(2)" checked="checked" value="2" >
       										Online Payment
       										</label>
       										</td>
    									</tr>
									</table>

									<div id="onlineRecharge"  style="display: block">
										<%
										String actionform = null;
										int index=0;
										for(index=0;index<OnlinePaymentConfigvalues.size();index++)
										{
											if(!mustDisablePaypal)
											{
											//	System.out.println("Hellllllllllllllo Paypal");
												actionform = "https://www.sandbox.paypal.com/cgi-bin/webscr";
												break;
											}
											else if(hasBrainTreeApiValue)
											{
												actionform = "../api/braintree.jsp?from=web&q=token&CustomerID=" + customerID;
												break;
											}
											else if(hasAuthorizeApiValue)
											{
												actionform = "../webpayment/authorizedformsubmit.jsp";
												break;
											}
											else if(haseprocessingvalue)
											{
												actionform = "https://www.eProcessingNetwork.com/cgi-bin/dbe/order.pl";
												break;
											}
											else if(hascashuApiValue)
											{
												actionform = "../webpayment/cashuformsubmit.jsp.jsp";
												break;
											}
											else if(haspaymentwallApiValue)
											{
												actionform = "../webpayment/paymentwallformsubmit.jsp";
												break;
											}
											else if(hasingenicoApiValue){
												actionform = "../webpayment/ingenicoPaymentSubmit.jsp";
												break;
											}
										else if(hasLavapayApiValue){
												
												actionform = "../webpayment/lavaRequestPage.jsp";
												break;
											}
										}
										%>
									
										<form id="payment" onSubmit="return validate();" action="<%=actionform %>" method="post" target="_blank" >
											<table width="500"  style="font-family: Arial; font-size: 10pt; ; color: #000000"; align="center">
										        <!--DWLayoutTable-->
										        <tr>
										          <td width="150">Recharge Amount(<%=currencyName %>)</td>
										          <td width="100" >
												  <select name="amount" id="amount" onChange="onlinepaymentaction();"><%for(int i=1;i<=6;++i){%>
												 <option <%if(i==1){%>selected="selected" <%} %> value="<%=i*5%>"><%=i*5%></option>           
											 <%}%></select>  
												  <% %>
												  <input type="hidden" name="custom" value="<%=custom%>" /></td>
										        </tr>
										        
										        
										        <tr>
										          <td width="200" height="30" align="center">
										          		 
										           		<div class="onlinepaymentparams" id="onlinepaymentparams">
									           			<%
										           			for(index=0;index<OnlinePaymentConfigvalues.size();index++)
										           			{
										           				
										           				if(!mustDisablePaypal)
										           				{
										           					//System.out.println("Hi hellllllllllllllllo");
										           		%>
										           				<input type="hidden" name="cmd" value="_xclick">
												                <input type="hidden" name="business" value="<%=receiverEmail%>">
												                <input type="hidden" name="item_name" value="Recharge Account">
												                <input type="hidden" name="quantity" value="1">
												                <input type="hidden" name="recurring" value="1">
												                <INPUT TYPE="hidden" NAME="return" value="<%=request.getRequestURL()%>">
												                <input type="hidden" name="notify_url" value="<%=retunurl%>">
												                <input type="hidden" name="shipping" value="0.00">
												                <input type="hidden" name="no_shipping" value="1">
												                <input type="hidden" name="no_note" value="1">
												                <input type="hidden" name="currency_code" value="<%=currencyName%>">
												                <input type="hidden" name="tax" value="<%=tax %>>">
												                <input type="hidden" name="lc" value="GB">
												                <input type="hidden" name="bn" value="PP-BuyNowBF">
												                <input type="hidden" name="src" value="1">
												                <input type="hidden" name="sra" value="1">	
										           		<%		
										           					break;
										           				}
										           				else if(hasAuthorizeApiValue)
										           				{
										           		%>
										           				<INPUT TYPE="HIDDEN" NAME="x_login" VALUE="<%=apiLoginId%>">
															    <INPUT TYPE="HIDDEN" NAME="x_fp_sequence" id ="x_fp_sequence" VALUE="<%=x_fp_sequence%>">
															    <INPUT TYPE="HIDDEN" NAME="x_fp_timestamp" id="x_fp_timestamp" VALUE="<%=x_fp_timestamp%>">
															    <INPUT TYPE="HIDDEN" NAME="x_relay_url" VALUE="<%=relayResponseUrl%>">
															    <INPUT TYPE="HIDDEN" NAME="x_relay_response" VALUE="TRUE"/>
															    <INPUT TYPE="HIDDEN" NAME="x_fp_hash" id="x_fp_hash" VALUE="<%=x_fp_hash%>">
															    <INPUT TYPE="HIDDEN" NAME="x_version" VALUE="3.1">
															    <INPUT TYPE="HIDDEN" NAME="x_method" VALUE="CC">
															    <INPUT TYPE="HIDDEN" NAME="x_type" VALUE="AUTH_CAPTURE">
															    <INPUT TYPE="HIDDEN" NAME="x_amount" id="x_amount" VALUE="<%=amount%>">
															    <INPUT TYPE="HIDDEN" NAME="x_show_form" VALUE="payment_form">
															    <INPUT TYPE="HIDDEN" NAME="x_test_request" VALUE="FALSE">
															    <INPUT TYPE="HIDDEN" NAME="customserverintegrate" VALUE="<%=custom%>">
															    <INPUT TYPE="HIDDEN" NAME="responseaddess" VALUE="<%=response_url%>">
										           		<%
										           					break;
										           				}
										           				else if(haseprocessingvalue)
										           				{
										           		%>
										           				<INPUT TYPE="HIDDEN" NAME="ePNAccount" VALUE="<%=eprocessingAccountid%>">
																<INPUT TYPE="HIDDEN" NAME="Total" VALUE="12.34">
																<INPUT TYPE="HIDDEN" NAME="ID" VALUE="1234567890">
																<INPUT TYPE="HIDDEN" NAME="ReturnApprovedURL" VALUE="<%=eprocssingResponseUrl%>">
																<INPUT TYPE="HIDDEN" NAME="ReturnDeclinedURL" VALUE="<%=eprocssingResponseUrl%>">
																<INPUT TYPE="HIDDEN" NAME="BackgroundColor" VALUE="white">
																<INPUT TYPE="HIDDEN" NAME="TextColor" VALUE="black">
																<INPUT TYPE="HIDDEN" NAME="Redirect" VALUE="1">	
																<INPUT TYPE="HIDDEN" NAME="custom" VALUE="<%=custom%>">
																<INPUT TYPE="HIDDEN" NAME="responseaddess" VALUE="<%=response_url%>">	
										           		<%		
										           					break;
										           				}
										           				else if(hascashuApiValue)
										           				{
										           		%>
										           				<input type="hidden" name="merchant_id" value="<%=merchant_id%>">
																<input type="hidden" name="display_text" value="Recharge By CASHU">
																<input type="hidden" name="currency" value="USD">
																<input type="hidden" name="language" value="en">
																<input type="hidden" name="session_id" value="<%=System.currentTimeMillis() %>">
																<input type="hidden" name="customserverintegrate" value="<%=custom%>">
																<input type="hidden" name="test_mode" value="1">
										           		<%			
										           					break;
										           				}
										           				else if(haspaymentwallApiValue){%>
										           				<input type="hidden" name="widget" value="p10_1">
																<input type="hidden" name="ag_name" value="Product">
																<input type="hidden" name="ag_type" value="fixed">
																<input type="hidden" name="sign_version" value="2">
																<input type="hidden" name="test_mode" value="1">
																<input type="hidden" name="success_url" value="<%=response_url%>">
																<input type="hidden" name="currency" value="USD">
																<input type="hidden" name="customserverintegrate" value ="<%=custom%>">
										           				<%break;
										           				}
										           				else if(hasingenicoApiValue){%>
									           					<input type="hidden" name="CATALOGURL" value="">
																<input type="hidden" name="HOMEURL" value="">
																<input type="hidden" name="ACCEPTURL" value="">
																<input type="hidden" name="DECLINEURL" value="">
																<input type="hidden" name="EXCEPTIONURL" value="">
																<input type="hidden" name="CANCELURL" value="">
																<input type="hidden" name="customserverintegrate" value="<%=custom%>">
									           					<%break;
									           					}
										           				
															else if(hasLavapayApiValue){   /******** Added for lavapay *********/
										           					
										           					%>
										           					    <input type = "hidden" name = "amount" value = "<%=amount %>" >
										           					    <input type = "hidden" name = "currency" value = "<%=currencyName %>"> 
										           					    <input type = "hidden" name = "paymentRequestFrom" value = "<%=paymentRequestFrom %>"> 
										           					    
										           					   
										           					
										           					<% 
										           				 	break;
										           					
										           				}
										           			}
										           		
										           		%>
										                </div>
													</td>
										          <td width="200" height="30" align="left"></td>
										        </tr>
										        
										        <!-- Added By Joynata -->
										        <%if(!mustDisablePaypal){%>
											    	<tr>
													 	<td width="150"></td>
													 	<td width="100" height="50" align="left">
												      		<label>
												      		<input type="radio" name="onlinepaymentOption" value="<%=OnlinePaymentConfigDTO.paypalgateway %>" id="paypalcheckedin" checked = "checked" onClick="onlinepaymentaction();">
												      		<img src="../images/paypal.gif" alt="PayPal - The safer, easier way to pay online." align="middle" /> 
												      		</label>
											    	 	</td>
											    	</tr>
											  	<%}
										        if(hasBrainTreeApiValue){%>
									    		 <tr>
									    	 		<td width="150"></td>
									         		<td width="100" height="50" align="left">
										      			<label>
										      			<input type="radio" name="onlinepaymentOption" value="<%=OnlinePaymentConfigDTO.braintree %>" id="brainTreecheckedin" onClick="onlinepaymentaction();">
										      			<img src="../images/Braintree.png" alt="Braintree - A Paypal Company" width="170px" height="42px" align="middle" /> 
										      			</label>
									    	 		</td>
								       		 	</tr>
									    		<%}
									          	if(hasAuthorizeApiValue){%>
									    		 <tr>
									    	 		<td width="150"></td>
									         		<td width="100" height="50" align="left">
										      			<label>
										      			<input type="radio" name="onlinepaymentOption" value="<%=OnlinePaymentConfigDTO.authorizedgateway %>" id="authorizecheckedin" onClick="onlinepaymentaction();">
										      			<img src="../images/Authorizenet.gif" alt="Authorize.net - The safer way to pay online." align="middle" /> 
										      			</label>
									    	 		</td>
								       		 	</tr>
									    		<%}
									        	if(haseprocessingvalue){ %>
									    		<tr>
										    	 <td width="150"></td>
										         <td width="100" height="50" align="left">
										      		<label>
										      		<input type="radio" name="onlinepaymentOption" value="<%=OnlinePaymentConfigDTO.eprocessinggateway %>" id="eprocessingcheckedin"  onclick="onlinepaymentaction();">
										      		<img src="../images/eProcessing.jpg" alt="eprocessingnetwork - The safer way to pay online." align="middle" /> 
										      		</label>
										    	 </td>
										          
										        </tr>
									    		<%}
									        	if(hascashuApiValue){%>
									    		<tr>
										    	 <td width="150"></td>
										         <td width="100" height="50" align="left">
										      		<label>
										      		<input type="radio" name="onlinepaymentOption" value="<%=OnlinePaymentConfigDTO.cashugateway %>" id="cashucheckedin" onClick="onlinepaymentaction();">
										      		<img src="../images/cashu.jpg" alt="CASHU - Pay Your Way" align="middle" /> 
										      		</label>
										    	 </td>
										          
										        </tr>
									    		<%}
									        	if(haspaymentwallApiValue){%>
									    		<tr>
										    	 <td width="150"></td>
										         <td width="100" height="50" align="left">
										      		<label>
										      		<input type="radio" name="onlinepaymentOption" value="<%=OnlinePaymentConfigDTO.paymentwall %>" id="paymentwallcheckedin" onClick="onlinepaymentaction();">
										      		<img src="../images/paysafecard.png" alt="Paysafe - Pay Cash, Pay Safe" align="middle" /> 
										      		</label>
										    	 </td>
										          
										        </tr>
									    		<%}
									        	if(hasingenicoApiValue){%>
									        	<tr>
										    	 <td width="150"></td>
										         <td width="100" height="50" align="left">
										      		<label>
										      		<input type="radio" name="onlinepaymentOption" value="<%=OnlinePaymentConfigDTO.ingenico %>" id="ingenicocheckedin" onClick="onlinepaymentaction();">
										      		<img src="../images/ingenico.jpg" alt="Ingenico - Paiment securies par" width="170px" height="42px" align="middle" /> 			
										      		</label>
										    	 </td>
										        </tr>								
												<%}%>
													
													
									        	<%if(hasLavapayApiValue){%>   
												<tr>
													<td width="150"></td>
													<td width="100" height="50" align="left"><label>
															<input type="radio" name="onlinepaymentOption"
															value="<%=OnlinePaymentConfigDTO.LAVAPAYGATEWAY %>"
															id="lavapaycheckedin" onClick="onlinepaymentaction();">
															<img src="../images/Lavapay.PNG"
															alt="Lava - Pay Your Way" align="middle" />
													</label></td>

												</tr>
												<%}%>					    	 
										        <tr>										          
										         <td width="150"></td>
										      	 <td width="100" height="20" align="right"></td>
										    	 
										        </tr>
										        <tr>
										          
										         <td width="150"></td>
										      	 <td width="100" height="30" align="left"><input name="submit" type="image" id="submit_online_payment" src="../images/onlinesubmit.png" onClick="checkforonlinecheck(2);" alt="Pay Now Online."  border="0"></td>
										    	 
										        </tr>
										        <!-- End Add  -->
										     
											</table>
										</form>  
									</div>

								</td>
								</tr>
							



	<tr id="CreditCardInfo" >
									<td>
									<table width="600"  style="font-family: Arial; font-size: 10pt; ; color: #000000;" align="center">
									  <tr>
									    <td>
									      <label> <input type="radio" name="paymentOption"
												onClick="checkforonlinecheck(4)" 
												checked="checked"  value="4"> By Credit Card
												
											</label>
									    </tr>
									</table>
									
									<div id="rechargeByCreditCard">
									
									<html:form method="POST" onsubmit="return validate();" action="/CreditCardPayment">
									
									<table width="400" style="font-family: Arial; font-size: 10pt; ; color: #000000"; align="center">
									
										<tr>
										<td width="150">Credit Card Number</td>
										<td width="100">
											<input type="text"  data-encrypted-name="cardNumber"  autocomplete="off" name="cardNumber" />
										</td>
										</tr>									
										
										<tr>
										<td width="150">CVV</td>
										<td width="100">
											<input type="text" style="width: 20%" data-encrypted-name="cvv" autocomplete="off" name="cvv" />
										</td>
										</tr>
										
										<tr>
										<td width="150">Expiration Date</td>
										<td width="100">
											<input type="text" style="width: 15%" data-encrypted-name="month" name="month" /> /
											<input type="text" style="width: 22%" data-encrypted-name="year" name="year" />
										</td>
										</tr>
										
										<tr>
										<td width="150">Credit Amount</td>
										<td width="100"><input type="text" style="width: 25%"  name="amount" id="amount" />(<%=currencyName%>)
											
											
										</td>
										</tr>
										
				
										<tr>
										<td width="150"></td><td align="center" colspan="2" height="10" style="">
											<input type="reset" id="button_card" onClick="init1();"
											value="Reset" />
											<input type="submit" id="submit"
											value="Recharge Account" /></td>
										 </tr>				
										 
										 </table>					
									</html:form>
									
									</div>
									
									</td>
								
								
								</tr>

<tr id="SSL" >
									<td>
									<table width="600"  style="font-family: Arial; font-size: 10pt; ; color: #000000;" align="center">
									  <tr>
									    <td>
									      <label> <input type="radio" name="paymentOption"
												
												 value="5"> By SSLCommerz
												
											</label>
									    </tr>
									</table>
									
									<div id="rechargeByBkash">
								
								<form id='payment_gw' name='payment_gw' method='POST' action='https://sandbox.sslcommerz.com/gwprocess/v3/process.php'>	
								<table width="400" style="font-family: Arial; font-size: 10pt; ; color: #000000"; align="center">									
								<tr>
								<td width="150">Credit Amount</td>
								<td width="100"><input type="text" name="total_amount" style="width: 45%"  /> BDT</td>
							<td>
									
							<input type="hidden" name="store_id" value='ekusheywap1'/>
							<input type="hidden" name="tran_id" value='ORDID123456789'/>
							<input type="hidden" name="success_url" value='http://localhost:8080/BTCL_Automation/webpayment/sslCommerzReply.jsp'/>
							<input type="hidden" name="fail_url" value='http://localhost:8080/BTCL_Automation/rechargecard/RechargeAccount.jsp'/>
							<input type="hidden" name="cancel_url" value='http://localhost:8080/BTCL_Automation/rechargecard/RechargeAccount.jsp'/>				
							
							
							<input type="hidden" name="show_all_gw" value="1"/>
							<!-- SUBMIT REQUEST !-->
							</td>
										
										</tr>
										
				
										<tr>
										<td width="150"></td><td align="center" colspan="2" height="10" style="">
											<input type="reset" id="button_card" onClick="init1();"
											value="Reset" /> </td>
											<td>
											<input type="submit" id="submit"
											value="Recharge By SSLCommerz" /></td>
										 </tr>				
										 
										 </table>					
									</form>
									
									</div>
									
									</td>
								
								
								</tr>

								
                    		</table>
                    		</td>
                    	</tr>
          			</table>
 	 			</tr>
 	 		</table>
 	 	</td>
 	</tr>
	
</table>
</div>
</div>
</div>
</html>

