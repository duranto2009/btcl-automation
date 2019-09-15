<%@page import="config.OnlinePaymentConfigDTO"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>
<%@page import="client.IVRCurrencyRepository"%>
<%@page import="autorecharge.PayPalAPICredentialsDTO"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="autorecharge.AutoRechargeService"%>
<%@ include file="../includes/checkLogin.jsp" %><%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %><%@page import = "java.util.*" %>
<%@page import = "java.text.*" %><%@ page errorPage="../common/failure.jsp"%><%

Logger logger = Logger.getLogger( "Recharge Account JSP" );
boolean mustDisablePaypal = false;
String showMenu="false";
String response_text = null;
response_text = (String)session.getAttribute(util.ConformationMessage.CARD_RECHARGE);
String rTypeParam = request.getParameter( "rType" );
if( rTypeParam != null && rTypeParam.equals( "cancel" ) ) {
	response_text = "Payment has been cancelled." ;
}
if(response_text != null) {
	session.removeAttribute(util.ConformationMessage.CARD_RECHARGE);
} 
/*************Added By Joyanta for authorized.net****************/
if(response_text==null)
{
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
boolean hasAuthorizeApiValue = false;
String receiverEmail=null;
String apiLoginId = null;
String transactionKey = null;
String relayResponseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/rechargecard/authorizenetrelay.jsp";
String eprocessingAccountid = null;
boolean haseprocessingvalue = false;
String eprocssingResponseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/rechargecard/eprocessing_response.jsp";
PaymentGatewayCredentialDAO paymentcredentialdao = new PaymentGatewayCredentialDAO();
ArrayList<OnlinePaymentConfigDTO> OnlinePaymentConfigvalues = paymentcredentialdao.getOnlinePaymentValues(clientDTO);
String paypalUsername = "";
String paypalPassword = "";
String paypalSignature = "";

boolean allPayPalConfigured = false;

for(int i=0;i<OnlinePaymentConfigvalues.size();i++)
{
	if(OnlinePaymentConfigvalues.get(i).getpaymentgatewaytype() == OnlinePaymentConfigDTO.authorizedgateway)
	{
		apiLoginId = OnlinePaymentConfigvalues.get(i).getApikey();
		transactionKey = OnlinePaymentConfigvalues.get(i).gettransactionkey();
		hasAuthorizeApiValue = true;
		
	}
	else if(OnlinePaymentConfigvalues.get(i).getpaymentgatewaytype() == OnlinePaymentConfigDTO.eprocessinggateway)
	{
		eprocessingAccountid = OnlinePaymentConfigvalues.get(i).getApikey();
		haseprocessingvalue = true;
	}
	else if(OnlinePaymentConfigvalues.get(i).getpaymentgatewaytype() == OnlinePaymentConfigDTO.paypalgateway)
	{
		receiverEmail = OnlinePaymentConfigvalues.get(i).getpaypalID();
		paypalUsername = OnlinePaymentConfigvalues.get(i).getApikey(); 
		paypalPassword = OnlinePaymentConfigvalues.get(i).gettransactionkey();
		paypalSignature = OnlinePaymentConfigvalues.get(i).getsignaturekey();
		
		if( paypalUsername != null && !paypalUsername.isEmpty() && paypalPassword != null && !paypalPassword.isEmpty() && paypalSignature != null && !paypalSignature.isEmpty() ) {
			allPayPalConfigured = true;
		}
		
	}
	
}
String amount = "0.0";
long x_fp_sequence = 0;
long x_fp_timestamp =0;
String x_fp_hash = "";
String custom=loginDTO.getAccountID()+","+loginDTO.getUsername()+","+loginDTO.getLoginSourceIP();
/****************End added By Joyanta for Authorized.net******************************/
if(request.getParameter("showMenu")!=null)
{	
	showMenu=request.getParameter("showMenu");
}
	String retunurl=  request.getRequestURL().toString();
	 int indexOfRechargeCard=retunurl.indexOf("rechargecard/");
	 if(indexOfRechargeCard>-1)
	 {
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
<html><head><html:base/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Recharge Account</title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>
<script language="JavaScript">
	var enableauthorize = false;
	
	var enableeprocess = false;

	var contextPath = "<%= request.getContextPath() %>";
	
	var isRefPaymentConfigured = <%= allPayPalConfigured  %>;
	var isRefPaymentEnabled = <%= ppDTO.isEnabledForClient() %>;
	var isRefPaymentActivated = <%= ppDTO.isActivatedForClient() %>;
	var disablePaypal = false;
	
	//alert( isRefPaymentEnabled + ";" + isRefPaymentActivated );
	/*************Added By Joyanta ********************/
	function checkforonlinecheck()
	{
		try{
			if (document.all.paymentOption[1].checked){
				document.getElementById("submit_online_payment").removeAttribute("disabled");
				
			}
			else
			{
				document.getElementById("submit_online_payment").disabled = "disabled";
				 
			}
		}catch(e){
			
		}
		
	}
	function onlinepaymentaction()
	{
		
	
		var frm = document.getElementById('payment') || null;
		if(frm) 
		{
		   try{
			
			   <%if(!mustDisablePaypal){%>
			   if(document.getElementById('paypalcheckedin').checked)
			   {
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
				   '<input type="hidden" name="tax" value="0.00">'+
				   '<input type="hidden" name="lc" value="GB">'+
				   '<input type="hidden" name="bn" value="PP-BuyNowBF">'+
				   '<input type="hidden" name="src" value="1">'+
				   '<input type="hidden" name="sra" value="1">';
				   
				   document.getElementById("onlinepaymentparams").innerHTML = innerhtmlcode;
				   
				   
				   <%if(disablePaypal && ( !ppDTO.isEnabledForClient() || ! allPayPalConfigured  ) )
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
			   
				   frm.action = '../rechargecard/authorizedformsubmit.jsp' ;
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
			   else
				{
				   enableauthorize = false;
				   
				}
		   <%}%>
		   
		   
		   <%if(haseprocessingvalue){%>
		   
			   if(document.getElementById('eprocessingcheckedin').checked)
			   {
				   enableeprocess = true;
				   enableauthorize = false;
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
			   else
			   {
				   enableeprocess = false;
			   }
		   <%}%>
		   
		   //frm.action = '../rechargecard/authorizedformsubmit.jsp' ;
		}
	}
	/*************************************************/
	function init() {
		/*********Added By Joyanta ********************/ 
		try{
			onlinepaymentaction();
		}catch(e) {
			
		}
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
			if( disablePaypal ) {
				%>
				disablePaypal = true;
				<%
				if( ! allPayPalConfigured || ! ppDTO.isEnabledForClient() ) {
					%>
					//alert( "No PayPal email is provided. Info about Auto Recharge is - configured: " + ppDTO.isConfigured() + ", enabled for this client: " + ppDTO.isEnabledForClient() );
					try  {
						//document.getElementById("rechargeByPapPal").disabled=true;
					} catch(e) {}
					<%
				}else{
					%> //alert( "not disabling paypal" ); <%
				}
			}else{
				%> //alert( "not disabling paypal because disablePaypal is false" ); <%
			}
		
		%>
		try {
		<% if(clientDTO.getCustomerTypeID()==ClientDTO.CUSTOMER_TYPE_RESELLER){%>
		document.getElementById("rechargeByPIN").style.display="none";
		<%} else {%>
		document.getElementById("rechargeByPIN").style.display="block";
		<%}%>
		}catch (e) {}
		// document.getElementById("rechargeByRechargeCard").style.display="none";
		// document.getElementById("rechargeByPapPal").style.display="none";
		
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
			
			//alert( "my one" );
			if(enableeprocess || enableauthorize) return true;
			if (isRefPaymentConfigured &&  isRefPaymentEnabled && ( ! isRefPaymentActivated )  ) {
				//alert( "Will do AR" );
				// Set Up Auto Recharge
				window.location = contextPath +  "/setReferencePayment.do?amount=" + ob.value ;
				return false;
			}else if ( disablePaypal )  {
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
<body class="body_center_align" onload="init();checkforonlinecheck();">
<table border="0" cellpadding="0" cellspacing="0" width="1024">
  	<tr><td width="100%"><%@ include file="../includes/header.jsp"%></td></tr>
 	<tr>
 	 	<td width="100%">
 	 		<table border="0" cellpadding="0" cellspacing="0" width="1024" id="AutoNumber2">
 	 			<tr>
 	 			<%
            	//reyans change
	            //if(loginDTO.getAccountID()!=-1 && loginDTO.getRoleID()==-1)
	            if(loginDTO.getAccountID()>0 && loginDTO.getRoleID()==-1)
	            {
	            	if (clientDTO.getCustomerTypeID()==client.ClientDTO.CUSTOMER_TYPE_RESELLER ){%>
	              <td class="td_menu"><%@ include file="../includes/resMenu.jsp"%>&nbsp;</td>
	              <td width="820" valign="top" class="td_main" align="center">
	              <%} else {
	              	if(menuAtLeftForPin)
	              	{
	                    %>
	              <td class="td_menu"><%@ include file="../includes/menu_pin.jsp"%>&nbsp;</td>
	              <td width="820" valign="top" class="td_main" align="center">
	                    <%	
	              	} else {%>
	              	
	              <td width="1024" valign="top" class="td_main" align="center">
	              <% }}
	            	}
	            	else { %>
	            <td class="td_menu"><%@ include file="../includes/menu.jsp"%>&nbsp;</td>
	            <td width="820" valign="top" class="td_main" align="center">
	            <%}%>
          		<!--main -->
          			<table border="0" cellpadding="0" cellspacing="0" width="100%">
          			 	<tr><td width="100%" align="center">
          			 		<table border="0" cellpadding="0" cellspacing="0" class="form1"  width="700" align="center">
                        		<tr>
                      				<td width="100%" align="center" style="padding-bottom: 20px;">
                        				<div class="div_title"><span style="vertical-align: sub;">Payment Option</span></div>
                      				</td>
                    			</tr>
                    			<div id="rechargeByRechargeCard">
									<table width="600"  style="font-family: Arial; font-size: 10pt; ; color: #000000;" align="center">
										<tr>
									         <td width="100%" align="center" valign="top" height="25" colspan="2" class="green_text"><b><%=response_text %></b></td>
									    </tr>
									  	<tr>
									    <td>
									      <label>
									        <input type="radio" name="paymentOption" onclick="checkforonlinecheck()"  checked="checked" value="1">
									         By Recharge Card</label>
									    </tr>
									</table>

									<div id="recharge_option_one" style="display:block">
										<html:form method="POST" onsubmit="return validate();" action="/RechargeAccountByCard">
										<table width="400" style="font-family: Arial; font-size: 10pt; ; color: #000000"; align="center">
										
										<tr><td width="150">Recharge Card Serial</td><td width="100"><input type="text" name="cardserial"/></td></tr>
										<tr><td width="150">Recharge Card No </td><td width="100"><input type="text" name="cardno"/></td></tr>
										<tr><td colspan="2" align="center"><html:errors property="serialcard"/></td></tr>
										<tr><td width="150"><td align="center" colspan="2" height="10" style="">
										<input type="button" id="button_card"  onClick="init();" value="<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.Global_reset,loginDTO)%>"/>
										<input type="submit" id="submit_card" value="Recharge Account"/></td>
										</tr>
										</table>
										</html:form>
									</div> 							
								</div>
								<!-- INSERT HERE THE PAYMENT GATWEAY CODE-->
								
								
								
								
								
								
								<%
								if( disablePaypal ){
									if( ! allPayPalConfigured || ! ppDTO.isEnabledForClient() ){
										mustDisablePaypal = true;
									}
								}

								if(hasAuthorizeApiValue || !mustDisablePaypal || haseprocessingvalue){ %>
								
								
								
								
								<div id="rechargeByPapPal">
									<table width="600"  style="font-family: Arial; font-size: 10pt; ; color: #000000" align="center">
  										<tr>
    										<td>
      										<label><input type="radio" name="paymentOption" onclick="checkforonlinecheck()" value="2" >
       										Online Payment
       										</label>
       										</td>
    									</tr>
									</table>

									<div id="recharge_option_two">
										<%String actionform = (!mustDisablePaypal)?"https://www.paypal.com/cgi-bin/webscr":((hasAuthorizeApiValue)?"../rechargecard/authorizedformsubmit.jsp":"https://www.eProcessingNetwork.com/cgi-bin/dbe/order.pl"); %>
										<form id="payment" onSubmit="return validate();" action="<%=actionform %>" method="post" target="_blank" >
											<table width="400"  style="font-family: Arial; font-size: 10pt; ; color: #000000"; align="center">
										        <!--DWLayoutTable-->
										        <tr>
										          <td width="150">Recharge Amount(<%=currencyName %>)</td>
										          <td width="100" >
												  <select name="amount" id="amount" onchange="onlinepaymentaction();"><%for(int i=1;i<=6;++i){%>
												 <option <%if(i==1){%>selected="selected" <%} %> value="<%=i*5%>"><%=i*5%></option>           
											 <%}%></select>  
												  <%
												 
												  
												  
												  
												  %>
												  <input type="hidden" name="custom" value="<%=custom%>" /></td>
										        </tr>
										        <tr>
										          <td width="200" height="30" align="center">
										          		 
										           		<div class="onlinepaymentparams" id="onlinepaymentparams">
										           		<%if(!mustDisablePaypal){ %>
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
											                <input type="hidden" name="tax" value="0.00">
											                <input type="hidden" name="lc" value="GB">
											                <input type="hidden" name="bn" value="PP-BuyNowBF">
											                <input type="hidden" name="src" value="1">
											                <input type="hidden" name="sra" value="1">
											              <%}else{
											            	if(hasAuthorizeApiValue){%>
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
											              	}
											            	else{%>
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
											            	}
											              }%>
										                </div>
													</td>
										          <td width="200" height="30" align="left"></td>
										        </tr>
										        <!-- Added By Joynata -->
										        <%if(haseprocessingvalue){ %>
										    	 <tr>
										    	 <td width="150"></td>
										         <td width="100" height="50" align="left">
										      		<label>
										      		<input type="radio" name="onlinepaymentOption" value="3" id="eprocessingcheckedin" checked="checked" onclick="onlinepaymentaction();">
										      		<img src="../images/eProcessing.jpg" alt="eprocessingnetwork - The safer way to pay online." align="middle" /> 
										      		</label>
										    	 </td>
										          
										        </tr>
										        <%} %>
										        <%if(hasAuthorizeApiValue){ %>
										    	 <tr>
										    	 <td width="150"></td>
										         <td width="100" height="50" align="left">
										      		<label>
										      		<input type="radio" name="onlinepaymentOption" value="2" id="authorizecheckedin" checked="checked" onclick="onlinepaymentaction();">
										      		<img src="../images/Authorizenet.gif" alt="Authorize.net - The safer way to pay online." align="middle" /> 
										      		</label>
										    	 </td>
										          
										        </tr>
										        <%} %>
										        <%if(!mustDisablePaypal){ %>
										        <tr>
												 <td width="150"></td>
												 <td width="100" height="50" align="left">
										      		<label>
										      		<input type="radio" name="onlinepaymentOption" value="1" id="paypalcheckedin" checked="checked" onclick="onlinepaymentaction();">
										      		<img src="../images/paypal.gif" alt="PayPal - The safer, easier way to pay online." align="middle" /> 
										      		</label>
										    	 </td>
										    	 </tr>
										    	 <%} %>
										    	 
										        <tr>
										          
										         <td width="150"></td>
										      	 <td width="100" height="20" align="right"></td>
										    	 
										        </tr>
										        <tr>
										          
										         <td width="150"></td>
										      	 <td width="100" height="30" align="left"><input name="submit" type="image" id="submit_online_payment" src="../images/onlinesubmit.png" onclick="checkforonlinecheck();" alt="Pay Now Online."  border="0"></td>
										    	 
										        </tr>
										        <!-- End Add By Joyanta -->
										     
											</table>
										</form>  
									</div>

								</div>
								
								
								
								
								
								<%} %>
								
								
								
								
								
								
								
								
								
								
								
								
								<div id="rechargeByPIN">
									<table width="600"  style="font-family: Arial; font-size: 10pt; ; color: #000000;" align="center">
									  <tr>
									    <td>
									      <label>
									        <input type="radio" name="paymentOption" onclick="checkforonlinecheck()"  value="3">
									         By PIN </label>
									    </tr>
									</table>
									<div id="recharge_option_othree" style="display:block">
										<html:form method="POST" onsubmit="return validate();" action="/RechargeAccountByPIN">
										<table width="400" style="font-family: Arial; font-size: 10pt; ; color: #000000"; align="center">
										<%String message = (String)session.getAttribute("PIN_RECHARGE");
										if(message != null)
										{
										  session.removeAttribute("PIN_RECHARGE");
										  %><tr><td colspan="2" align="center"><b><%=message%></b></td></tr><%
										}
										
										%>
										<tr><td width="150">Pin No</td><td width="100"><input type="text" name="pinNo"/></td></tr>
										<tr><td width="150">Pin Password </td><td width="100"><input type="text" name="password"/></td></tr>
										<tr><td colspan="2" align="center"><html:errors property="pinPassword"/></td></tr>
										<tr><td width="150"></td><td align="center" colspan="2" height="10" style="">
										<input type="button" id="button_card"  onClick="init();" value="<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.Global_reset,loginDTO)%>"/>
										<input type="submit" id="submit_card" value="Recharge Account"/></td>
										</tr>
										</table>
										</html:form>
									</div>
								</div>
                    		</table>
                    		</td>
                    	</tr>
          			</table>
 	 			</tr>
 	 		</table>
 	 	</td>
 	</tr>
	<tr><td width="100%"><%@ include file="../includes/footer.jsp"%></td></tr>
</table>
</body>
</html>

