<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@page import = "webpayment.LavaCrypt" %> 
<%@ include file="../includes/checkLogin.jsp"%>
<%
Logger logger = Logger.getLogger( "demo_test_call.jsp" );
String trans_id = "";
String payment_mode = "";
trans_id = request.getParameter("TRANS_ID");
payment_mode = request.getParameter("PAYMENT_MODE");
logger.debug("TRANS_ID : " + trans_id);
logger.debug("PAYMENT_MODE : " + payment_mode);

if(trans_id != null && payment_mode != null ){
	if(payment_mode.equals("fpx")){
		try{
			new LavaCrypt().updateLavaPayPaymentMode(trans_id, payment_mode);
			logger.debug("Lava Payment mode updated successfully...");
		}catch(Exception e){
			logger.fatal("Exception is occurred while updating payment mode", e);
		}
	}else if(payment_mode.equals("migs")){
		logger.debug("No need for payment mode update...");
	}
	
}else{
	logger.debug("Wrong formatted post request");
}

%>