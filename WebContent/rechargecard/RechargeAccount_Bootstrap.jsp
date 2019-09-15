<%@page import="config.OnlinePaymentConfigDTO"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>

<%@page import="autorecharge.PayPalAPICredentialsDTO"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="autorecharge.AutoRechargeService"%>
<%@ include file="../includes/checkLogin.jsp" %>
<%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import = "java.util.*" %>
<%@page import = "java.text.*" %>
<%@ page errorPage="../common/failure.jsp"%><%

Logger logger = Logger.getLogger( "Recharge Account JSP" );

String showMenu="false";
String response_text = null;

boolean hasAuthorizeApiValue 	= false;
boolean haseprocessingvalue 	= false;
boolean mustDisablePaypal 		= false;

/*******Added For cashu ***************/
boolean hascashuApiValue  		= false;
String merchant_id 				= null;
String encryption_key          	= null;
/**************************************************/

/*******Added For paymentWall  ***************/
boolean haspaymentwallApiValue  = false;
String application_key 			= null;
/**************************************************/

/*******Added For ingenico(Ogone) ***************/
boolean hasingenicoApiValue  = false;

response_text = (String)session.getAttribute(util.ConformationMessage.CARD_RECHARGE);
String rTypeParam = request.getParameter( "rType" );
if( rTypeParam != null && rTypeParam.equals( "cancel" )){
	response_text = "Payment has been cancelled." ;
}
if(response_text != null){
	session.removeAttribute(util.ConformationMessage.CARD_RECHARGE);
} 

/*************Added  for authorized.net****************/
if(response_text==null){
  response_text = (request.getParameter("responseText")==null)?"":request.getParameter("responseText");
}

String response_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/rechargecard/RechargeAccount.jsp";
String response_from_online_payment = (request.getParameter("responseText")==null)?"":request.getParameter("responseText");

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
boolean allPayPalConfigured = false;

for(int i=0; i<OnlinePaymentConfigvalues.size(); i++){
	tax=OnlinePaymentConfigvalues.get(i).getTaxAmount();
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
		
		if( paypalUsername != null && !paypalUsername.isEmpty() && paypalPassword != null && !paypalPassword.isEmpty() && paypalSignature != null && !paypalSignature.isEmpty() ) {
			allPayPalConfigured = true;
		}
	}
	/*********Added For Cashu  ***********************/
		
	else if(OnlinePaymentConfigvalues.get(i).getpaymentgatewaytype() == OnlinePaymentConfigDTO.cashugateway){
		merchant_id = OnlinePaymentConfigvalues.get(i).getApikey();
		hascashuApiValue = true;		
	}
	/***********************************************************/
	
	/*********Added For PaymentWall  ***********************/
	
	else if(OnlinePaymentConfigvalues.get(i).getpaymentgatewaytype() == OnlinePaymentConfigDTO.paymentwall){
		application_key = OnlinePaymentConfigvalues.get(i).getApikey();
		haspaymentwallApiValue = true;		
	}
	/***********************************************************/
	
	/*********Added For Ingenico  ***********************/
	
	else if(OnlinePaymentConfigvalues.get(i).getpaymentgatewaytype() == OnlinePaymentConfigDTO.ingenico){
		//application_key = OnlinePaymentConfigvalues.get(i).getApikey();
		hasingenicoApiValue = true;		
	}
	/***********************************************************/
}
String amount = "0.0";
long x_fp_sequence = 0;
long x_fp_timestamp =0;
String x_fp_hash = "";
String custom=loginDTO.getAccountID()+","+loginDTO.getUsername()+","+loginDTO.getLoginSourceIP();
/****************End added  for Authorized.net******************************/
if(request.getParameter("showMenu")!=null){
	showMenu=request.getParameter("showMenu");
}

String retunurl=  request.getRequestURL().toString();
 int indexOfRechargeCard=retunurl.indexOf("rechargecard/");
 if(indexOfRechargeCard>-1){
	 retunurl=retunurl.substring(0,indexOfRechargeCard);
	 retunurl=retunurl+"webpayment/paypal-reply.jsp";
 }
 
/*String retunurl= "http://"+request.getServerName();
if(request.getServerPort()!=80)
{
	retunurl+=":"+request.getServerPort();
}
retunurl=retunurl+request.getContextPath()+"/webpayment/paypal-reply.jsp";
*/
String windowTitle =  "Recharge Account";
String currencyName= IVRCurrencyRepository.getInstance().getCurrencyDTOByID(Integer.parseInt(clientDTO.getIVRCurrency())).currencyName;
boolean disablePaypal=true; // Shantanu - make it to true when deploying.
/*********Joyanta has changed here ******************/
//receiverEmail = paymentcredentialdao.getPaypalEmailAddress(clientDTO);
/****************************************************/
if(receiverEmail!=null && receiverEmail.length()>0) {
	disablePaypal=false;
}
// Get Reference Payment Data
AutoRechargeService arService = new AutoRechargeService();
PayPalAPICredentialsDTO ppDTO = arService.getAutoRechargeInformation(clientDTO);

logger.debug( "Auto Recharge info obtained: isRefPaymentConfigured:" + allPayPalConfigured + "; enabled: " 
	+ ppDTO.isEnabledForClient() + "; activated: " + ppDTO.isActivatedForClient()  );

%>


<html>
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1">
		
		<title>Recharge Account</title>
		
		<link rel="stylesheet" type="text/css" href="../stylesheets/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="../stylesheets/modifiedpage.css">
		<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
		
		<script language="JavaScript" src="../scripts/util.js"></script>
		<script language="JavaScript">
			var enableauthorize = false;		
			var enableeprocess = false;		
			var enablecashu   = false;		
			var enablepaymentwall = false;
			var enableingenico = false;
			var contextPath = "<%= request.getContextPath() %>";
			
			var isRefPaymentConfigured = <%= allPayPalConfigured  %>;
			var isRefPaymentEnabled = <%= ppDTO.isEnabledForClient() %>;
			var isRefPaymentActivated = <%= ppDTO.isActivatedForClient() %>;
			var disablePaypal = false;
			var paymentType=1;
			//alert( isRefPaymentEnabled + ";" + isRefPaymentActivated );
			
			function checkforonlinecheck(val)
			{
				paymentType=val;
				
				if(paymentType==1){
					document.getElementById("rechargeCardInfo").style.display="";
					document.getElementById("onlineRecharge").style.display="none";
					document.getElementById("recharge_option_rechargeByPIN").style.display="none";
				}
				else if(paymentType==2){
					document.getElementById("onlineRecharge").style.display="";
					document.getElementById("recharge_option_rechargeByPIN").style.display="none";
					document.getElementById("rechargeCardInfo").style.display="none";
				}
				else if(paymentType==3){
					document.getElementById("recharge_option_rechargeByPIN").style.display="";			
					document.getElementById("onlineRecharge").style.display="none";
					document.getElementById("rechargeCardInfo").style.display="none";
				}				
			}
				
			function onlinepaymentaction(){				
				var frm = document.getElementById('payment') || null;
				if(frm){					
				   	try{						
						<%if(!mustDisablePaypal){%>
					   	if(document.getElementById('paypalcheckedin').checked){
							enableauthorize = false;
						   	enableeprocess = false;
						   	frm.action = 'https://www.paypal.com/cgi-bin/webscr' ;
						   	document.getElementById("onlinepaymentparams").innerHTML= "";
						   
						   	var innerhtmlcode = 
						   	'<input type="hidden" name="cmd" value="_xclick">'+
						   	'<input type="hidden" name="business" value="<%=receiverEmail%>">'+
						   	'<input type="hidden" name="item_name" value="Recharge Account">'+
						   	'<input type="hidden" name="quantity" value="1">'+
						   	'<input type="hidden" name="recurring" value="1">'+
						   	'<INPUT TYPE="hidden" NAME="return" value="<%=request.getRequestURL()%>">'+
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
						   
						   	<%if(disablePaypal && ( !ppDTO.isEnabledForClient() || ! allPayPalConfigured  ) ){%>
						   		document.getElementById("submit_online_payment").disabled = "disabled";
						   	<%}%>	   
					   	}
				   	<%} %>
					   
				   }catch(e){}

				   <%if(hasAuthorizeApiValue){%>
				   
					   if(document.getElementById('authorizecheckedin').checked){
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
								'<input type="hidden" name="currency" value="USD">'+
								'<input type="hidden" name="language" value="en">'+
								'<input type="hidden" name="session_id" value="<%=System.currentTimeMillis() %>">'+
								'<input type="hidden" name="customserverintegrate" value="<%=custom%>">'+
								'<input type="hidden" name="test_mode" value="1">'+
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
							   '<INPUT TYPE="HIDDEN" NAME="test_mode" VALUE="1">'+
							   '<INPUT TYPE="HIDDEN" NAME="success_url" VALUE="<%=response_url%>">'+
							   '<INPUT TYPE="HIDDEN" NAME="evaluation" VALUE="1">'+
							   '<INPUT TYPE="HIDDEN" NAME="customserverintegrate" VALUE="<%=custom%>">'+
							   '<input type="hidden" name="currency" value="USD">';					   
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
								'<INPUT type="hidden" name="PSPID" value="murexmobileAPP">'+
								'<INPUT type="hidden" name="customserverintegrate" value="<%=custom%>">'+
								'<INPUT type="hidden" name="USERID" value="imurex">';
						   					   
							document.getElementById("onlinepaymentparams").innerHTML = innerhtmlcode;
							
							
					   }
					   else{
						   enableingenico = false;
					   }
			  		<%}%>			   		
				   
				   //frm.action = '../rechargecard/authorizedformsubmit.jsp' ;
				}
			}
				
			function setdefaultvalue(){
				document.getElementsByName("onlinepaymentOption")[0].checked = true;
			}
			/*************************************************/

			function init(){
				/*********Added  ********************/ 
				try{
					setdefaultvalue();
					onlinepaymentaction();
				}catch(e){}
				checkforonlinecheck();
				/**********************************************/
				
				document.all.paymentOption[0].click();
				// document.all.paymentOption[2].click(); //to recharge by PIN As default
				<%if(clientDTO!=null && clientDTO.getParentAccountNo()!=-1 &&client.ClientRepository.getInstance().getClient(clientDTO.getParentAccountNo()).getMaxAllowedBalance()!=-1)
				{%>
				try{
					document.getElementById("rechargeByRechargeCard").style.display="none";
					document.getElementById("rechargeByPapPal").style.display.disabled="true";
				}catch(e) {}
				<%}%>
		
				<%
					if( disablePaypal ){
						%>
						disablePaypal = true;
						<%
						if( ! allPayPalConfigured || ! ppDTO.isEnabledForClient()){
							%>
							//alert( "No PayPal email is provided. Info about Auto Recharge is - configured: " + ppDTO.isConfigured() + ", enabled for this client: " + ppDTO.isEnabledForClient() );
							try {
								//document.getElementById("rechargeByPapPal").disabled=true;
							} catch(e){}
							<%
						}else{
							%> //alert( "not disabling paypal" ); <%
						}
					}else{
						%> //alert( "not disabling paypal because disablePaypal is false" ); <%
					}
		
				%>
				try {
					<% if(clientDTO.getCustomerTypeID()==ClientDTO.CUSTOMER_TYPE_ORIGINATING && clientDTO.getAuthByPIN()){%>
					document.getElementById("rechargeByPIN").style.display="";
				<%} else {%>
					document.getElementById("rechargeByPIN").style.display="none";
				<%}%>
				}catch (e) {}
				// document.getElementById("rechargeByRechargeCard").style.display="none";
				// document.getElementById("rechargeByPapPal").style.display="none";
			}
			
			function validate(){	
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
		
					//alert( "my one" );
					if(enableeprocess || enableauthorize || enablecashu || enablepaymentwall) return true;
					if (isRefPaymentConfigured &&  isRefPaymentEnabled && ( ! isRefPaymentActivated )){
						//alert( "Will do AR" );
						// Set Up Auto Recharge
						window.location = contextPath +  "/setReferencePayment.do?amount=" + ob.value ;
						return false;
					}else if (disablePaypal){
						window.location = contextPath +  "/setReferencePayment.do?amount=" + ob.value + "&refNotRequired=true" ;
						alert( "Ref Not Required" )	;
						return false;
						//alert( "NON AR" );
					}		
				}else{
					//alert("not checked");
				}		
				return true; 
			}		
		</script>
	</head>
	<body class="" onload="init();checkforonlinecheck();">
		<!--header-->
		<div class="container">
			<div class="col-lg-offset-3 col-xs-12 col-sm-12  col-md-12 col-lg-7 header-backgroundcolor" >
				<%@ include file="../includes/header.jsp"%>
			</div>
		</div>
		<div class="container">
			<div class="col-lg-offset-3 col-xs-12 col-sm-12 col-md-12 col-lg-7">
				<div class="col-xxs-4 col-xs-4 col-sm-4  col-md-2 col-lg-2 div_menu">
					<%  //reyans change
            			//if(loginDTO.getAccountID()!=-1 && loginDTO.getRoleID()==-1)
			            if(loginDTO.getAccountID()>0 && loginDTO.getRoleID()==-1){
			            	if (clientDTO.getCustomerTypeID()==client.ClientDTO.CUSTOMER_TYPE_RESELLER ){%>
								<%@ include file="../includes/resMenu.jsp"%>
							<%} 
			            	else {
				              	if(menuAtLeftForPin){%>
				              		<%@ include file="../includes/menu_pin.jsp"%>
				              	<%}
				            }
				         }
			           	 else {%>
			            		<%@ include file="../includes/menu.jsp"%>
			             <%}%>			
				</div>
				
				<div class="col-xxs-8 col-xs-8 col-sm-8  col-md-10 col-lg-10 div_main" >
					<div class="col-xxs-offset-0-5 col-xs-offset-0-5 col-md-offset-1 col-lg-offset-1 col-xxs-11 col-xs-11 col-sm-8 col-md-10 col-lg-10 title"  style="height:35px;">
						Payment Option
					</div>
					
					<%if(!isAgent){ %>
						<div id="rechargeByRechargeCard" class="col-xxs-offset-0-5 col-xs-offset-0-5 col-md-offset-1 col-lg-offset-1 col-xxs-11 col-xs-11 col-sm-8 col-md-10 col-lg-10">
							<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-8 col-md-10 col-lg-10" >
		   						<div style="width:100%;	text-align:center; height:25px;" class="green_text"><b><%=response_text %></b></div>
		   					</div>
							<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-8 col-md-10 col-lg-10" >
		   						<div style="width:100%; text-align:left;">
		   							<label><input type="radio" name="paymentOption" onclick="checkforonlinecheck(1)"  checked="checked" value="1">
						        		 By Recharge Card
						        	</label>
		   						</div>
		   						
		   						<div id="rechargeCardInfo">
									<html:form method="POST" onsubmit="return validate();" action="/RechargeAccountByCard">
									<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-8 col-md-10 col-lg-10" style="text-align:left;">
										<div class="row" style="margin: 0px;">
											<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-6 col-md-4 col-lg-4">
												Recharge Card Serial
											</div>
											<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-4 col-md-4 col-lg-4">
												<input type="text"  style="width:100%;" name="cardserial"/>
											</div>
										</div>
										<div class="row" style="margin: 0px;">
											<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-6 col-md-4 col-lg-4">
												Recharge Card No
											</div>
											<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-4 col-md-4 col-lg-4">
												<input type="text"  style="width:100%;" name="cardno"/>
											</div>
										</div>
										<div class="row" style="margin: 0px;">
											<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-10 col-md-10 col-lg-10">
												<html:errors property="serialcard"/>
											</div>
										</div>
										<div class="row" style="margin: 0px;">
											<div class="col-md-offset-7 col-lg-offset-7 col-xxs-12 col-xs-12 col-sm-10 col-md-5 col-lg-4">
												<input type="button" id="button_card"  onClick="init();" value="<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.Global_reset,loginDTO)%>"/>
												<input type="submit" id="submit_card" value="Recharge Account"/>
											</div>
										</div>		
										
	   								</div>
	   								</html:form>
		   						</div>
		   					</div>
						</div>
					<%} %>
					
					<%if(disablePaypal){
						if(! allPayPalConfigured || ! ppDTO.isEnabledForClient()){
							mustDisablePaypal = true;
						}
					}

					if(hasAuthorizeApiValue || !mustDisablePaypal || haseprocessingvalue || hascashuApiValue || haspaymentwallApiValue || hasingenicoApiValue){ %>
						<div id="rechargeByPapPal" class="col-xxs-offset-0-5 col-xs-offset-0-5 col-md-offset-1 col-lg-offset-1 col-xxs-11 col-xs-11 col-sm-8 col-md-10 col-lg-10">
							<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-8 col-md-10 col-lg-10" >
								<div style="width:100%; text-align:left;">
									<label><input type="radio" name="paymentOption" onclick="checkforonlinecheck(2)" <%if(isAgent){ %>checked="checked" <%} %> value="2" >
		       							Online Payment
		       						</label>
		       					</div>
		       					<div id="onlineRecharge" <%if(isAgent){%> style="display: block"<%}%>>
		       						<%
									String actionform = null;
									int index=0;
									for(index=0; index<OnlinePaymentConfigvalues.size(); index++){
										if(!mustDisablePaypal){
											actionform = "https://www.paypal.com/cgi-bin/webscr";
											break;
										}
										else if(hasAuthorizeApiValue){
											actionform = "../webpayment/authorizedformsubmit.jsp";
											break;
										}
										else if(haseprocessingvalue){
											actionform = "https://www.eProcessingNetwork.com/cgi-bin/dbe/order.pl";
											break;
										}
										else if(hascashuApiValue){
											actionform = "../webpayment/cashuformsubmit.jsp.jsp";
											break;
										}
										else if(haspaymentwallApiValue){
											actionform = "../webpayment/paymentwallformsubmit.jsp";
											break;
										}
										else if(hasingenicoApiValue){
											actionform = "../webpayment/ingenicoPaymentSubmit.jsp";
											break;
										}
									}%>
									
									<form id="payment" onSubmit="return validate();" action="<%=actionform %>" method="post" target="_blank" >
									<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-8 col-md-10 col-lg-10" style="text-align:left;">
										<div class="row" style="margin-left: 0px; margin-right: 0px; margin-bottom: 5%;">
											<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-6 col-md-4 col-lg-4">
												Recharge Amount(<%=currencyName %>)
											</div>
											<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-4 col-md-4 col-lg-4">
												<select name="amount" id="amount" onchange="onlinepaymentaction();">
											  		<%for(int i=1;i<=6;++i){%>
											 			<option <%if(i==1){%>selected="selected" <%} %> value="<%=i*5%>"><%=i*5%></option>           
										 			<%}%>
										 		</select>
										 		<input type="hidden" name="custom" value="<%=custom%>" />
											</div>
										</div>
										<div class="row onlinepaymentparams" id="onlinepaymentparams" style="margin: 0px;">
											<%for(index=0; index<OnlinePaymentConfigvalues.size(); index++){
							           			//System.out.println("Cashu Status "+hascashuApiValue);
							           			
						           				if(!mustDisablePaypal){%>
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
								           			<% break;
							           		 	}
							           			else if(hasAuthorizeApiValue){%>
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
							           				<%break;
							           			}
							           			else if(haseprocessingvalue){%>
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
							           				<%break;
							           			}
							           			else if(hascashuApiValue){%>
							           				<input type="hidden" name="merchant_id" value="<%=merchant_id%>">
													<input type="hidden" name="display_text" value="Recharge By CASHU">
													<input type="hidden" name="currency" value="USD">
													<input type="hidden" name="language" value="en">
													<input type="hidden" name="session_id" value="<%=System.currentTimeMillis() %>">
													<input type="hidden" name="customserverintegrate" value="<%=custom%>">
													<input type="hidden" name="test_mode" value="1">
							           				<%break;
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
							           		}%>

										</div>
										
										<!-- Added By Joynata -->
										<%if(!mustDisablePaypal){%>
											<div class="row" style="margin: 0px;">
												<div class="col-md-offset-6 col-lg-offset-6 col-xxs-12 col-xs-12 col-sm-8 col-md-6 col-lg-6">
													<label>
										      			<input type="radio" style="vertical-align: baseline;" name="onlinepaymentOption" value="<%=OnlinePaymentConfigDTO.paypalgateway %>" id="paypalcheckedin" checked = "checked" onclick="onlinepaymentaction();">
										      			<img src="../images/paypal.gif" alt="PayPal - The safer, easier way to pay online." align="" /> 
										      		</label>
												</div>
											</div>
										<%}
									    if(hasAuthorizeApiValue){%>
											<div class="row" style="margin-left: 0px; margin-right: 0px; margin-top: 1%;">
												<div class="col-md-offset-6 col-lg-offset-6 col-xxs-12 col-xs-12 col-sm-8 col-md-6 col-lg-6">
													<label>
										      			<input type="radio" style="vertical-align: baseline;" name="onlinepaymentOption" value="<%=OnlinePaymentConfigDTO.authorizedgateway %>" id="authorizecheckedin" onclick="onlinepaymentaction();">
										      			<img src="../images/Authorizenet.gif" alt="Authorize.net - The safer way to pay online." align="" /> 
										      		</label>
												</div>
											</div>
										 <%}
									     if(haseprocessingvalue){%>	
											<div class="row" style="margin-left: 0px; margin-right: 0px; margin-top: 1%;">
												<div class="col-md-offset-6 col-lg-offset-6 col-xxs-12 col-xs-12 col-sm-8 col-md-6 col-lg-6">
													<label>
											      		<input type="radio" style="vertical-align: baseline;" name="onlinepaymentOption" value="<%=OnlinePaymentConfigDTO.eprocessinggateway %>" id="eprocessingcheckedin"  onclick="onlinepaymentaction();">
											      		<img src="../images/eProcessing.jpg" alt="eprocessingnetwork - The safer way to pay online." align="" /> 
											      	</label>
												</div>
											</div>
										 <%}
									    
										 if(hascashuApiValue){%>
										 	<div class="row" style="margin-left: 0px; margin-right: 0px; margin-top: 1%;">
												<div class="col-md-offset-6 col-lg-offset-6 col-xxs-12 col-xs-12 col-sm-8 col-md-6 col-lg-6">	
													<label>
											      		<input type="radio" style="vertical-align: baseline;" name="onlinepaymentOption" value="<%=OnlinePaymentConfigDTO.cashugateway %>" id="cashucheckedin" onclick="onlinepaymentaction();">
											      		<img src="../images/cashu.jpg" alt="CASHU - Pay Your Way" align="" /> 
											      	</label>
												</div>
											</div>
										<%}
										if(haspaymentwallApiValue){%>
											<div class="row" style="margin-left: 0px; margin-right: 0px; margin-top: 1%;">
												<div class="col-md-offset-6 col-lg-offset-6 col-xxs-12 col-xs-12 col-sm-8 col-md-6 col-lg-6">	 	
													<label>
											      		<input type="radio" style="vertical-align: baseline;" name="onlinepaymentOption" value="<%=OnlinePaymentConfigDTO.paymentwall %>" id="paymentwallcheckedin" onclick="onlinepaymentaction();">
											      		<img src="../images/paysafecard.png" alt="Paysafe - Pay Cash, Pay Safe" align="" /> 
											      	</label>
												</div>
											</div>
										<%}
										if(hasingenicoApiValue){%>
											<div class="row" style="margin-left: 0px; margin-right: 0px; margin-top: 1%;">
												<div class="col-md-offset-6 col-lg-offset-6 col-xxs-12 col-xs-12 col-sm-8 col-md-6 col-lg-6">	 	
													<label>
											      		<input type="radio" style="vertical-align: baseline;" name="onlinepaymentOption" value="6" id="ingenicocheckedin" onclick="onlinepaymentaction();">
											      		<img src="../images/ingenico.jpg" alt="Ingenico - Paiment securies par" width="170px" height="42px" align="" /> 
											      	</label>
												</div>
											</div>
										<%}%>
										
										<div class="row" style="margin-left: 0px; margin-right: 0px; margin-top: 5%;">
											<div class="col-xxs-offset-6 col-xs-offset-6 col-md-offset-6 col-lg-offset-6 col-xxs-6 col-xs-6 col-sm-4 col-md-3 col-lg-3">	 	
												<input name="submit" type="image" id="submit_online_payment" src="../images/onlinesubmit.png" onclick="checkforonlinecheck(2);" alt="Pay Now Online."  border="0">
											</div>
										</div>
										<!-- End Add  -->
	   								</div>
	   								</form>
		   						</div>
	   						</div>	
						</div>
					<%}%>
					
					<div id="rechargeByPIN" class="col-xxs-offset-0-5 col-xs-offset-0-5 col-md-offset-1 col-lg-offset-1 col-xxs-11 col-xs-11 col-sm-8 col-md-10 col-lg-10">
						<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-8 col-md-10 col-lg-10" >
							<div style="width:100%; text-align:left;">
								</label>
									<input type="radio" name="paymentOption" onclick="checkforonlinecheck(3)" value="3">
									By PIN 
								</label>
	       					</div>
	       					<div id="recharge_option_rechargeByPIN">
	       						<html:form method="POST" onsubmit="return validate();" action="/RechargeAccountByPIN">
	       						<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-8 col-md-10 col-lg-10" style="text-align:left;">
	       							<%String message = (String)session.getAttribute("PIN_RECHARGE");
									if(message != null)
									{
										session.removeAttribute("PIN_RECHARGE");%>
										<div class="row" style="margin: 0px;">
											<b><%=message%></b>
										</div>
									<%}%>
									<div class="row" style="margin: 0px;">
										<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-6 col-md-4 col-lg-4">Pin No</div>
										<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-4 col-md-4 col-lg-4">
											<input type="text" name="pinNo"/>
										</div>
									</div>
									<div class="row" style="margin: 0px;">
										<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-6 col-md-4 col-lg-4">Pin Password</div>
										<div class="col-md-offset-1 col-lg-offset-1 col-xxs-12 col-xs-12 col-sm-4 col-md-4 col-lg-4">
											<input type="text" name="password"/>
										</div>
									</div>
									<div class="row" style="margin: 0px;">
										<html:errors property="pinPassword"/>
									</div>
									<div class="row" style="margin: 0px;">
										<div class="col-md-offset-7 col-lg-offset-7 col-xxs-12 col-xs-12 col-sm-4 col-md-4 col-lg-4">
											<input type="button" id="button_card"  onClick="init();" value="<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.Global_reset,loginDTO)%>"/>
											<input type="submit" id="submit_card" value="Recharge Account"/></td>
										</div>
									</div>		
								</div>	
								</html:form>
							</div>
						</div>	
       				</div>	
				</div>
			</div>
		</div>
		<div class="container">
			<div class="col-lg-offset-3 col-xs-12 col-sm-12 col-md-12 col-lg-7 header-backgroundcolor">
				<footer>
					<%@ include file="../includes/footer.jsp" %>
				</footer>
			</div>
		</div>  
	</body>
</html>

