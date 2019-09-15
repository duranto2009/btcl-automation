<%@page import="client.ClientRepository"%>
<%@page import="client.ClientDTO"%>
<%@ page import="java.net.*" %>
<%@ page import="org.apache.log4j.Logger"%>
 <%@ include file="../includes/checkLogin.jsp" %>
<%@page import="creditcardpayment.client.*"%>


<%
	Logger logger = Logger.getLogger("Recharge Account jsp");
	ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(loginDTO.getAccountID());
	String tran_id = request.getParameter("tran_id");
	String error = request.getParameter("error");
	
	double rechargedAmount = Double.parseDouble(request.getParameter("amount"));
	double sentAmount = Double.parseDouble(request.getParameter("amount"));
	WebPaymentClient wbPaymentClient = new WebPaymentClient("" + clientDTO.getUniqueID(), "", "USD", " ", "",
			rechargedAmount + "");

	
	wbPaymentClient.makePayment();
	logger.debug("Recharged Amount : " + rechargedAmount);
	logger.debug("error : " +error);
	request.getSession().setAttribute("CARD_RECHARGE", "Recharged by SSL Commerz by amount : "+ rechargedAmount +" BDT");
	response.sendRedirect("../rechargecard/RechargeAccount.jsp");
	
%>
