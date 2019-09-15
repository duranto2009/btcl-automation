<%@page import="java.util.List"%>
<%@page import="urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType"%>
<%@ page import="urn.ebay.apis.eBLBaseComponents.ErrorType"%>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page errorPage="../common/failure.jsp"%>

<%
	
	// Check for doExpressCheckout call response
	
	Boolean isDoEC = ( Boolean ) request.getAttribute( "doEC" ) ;
	
	if( isDoEC != null && isDoEC ) {
		// DO EXPRESS CHECKOUT ////////////////////////////////
		request.getSession( true ).setAttribute("CARD_RECHARGE", "Auto Rechage activated successfully!" ) ;
		response.sendRedirect( request.getContextPath() + "/rechargecard/RechargeAccount.jsp?rType=billAggrmntRecvd" ) ;
		return;
	}

	// SET EXPRESS CHECKOUT	///////////////////////////////////
	
	SetExpressCheckoutResponseType setExpressCheckoutResponse = ( SetExpressCheckoutResponseType ) request.getAttribute("setExpressCheckoutResponse") ;
	
	response.sendRedirect("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" + setExpressCheckoutResponse.getToken() ); // TODO Sandbox...
	return;  // is it necessary?

%>