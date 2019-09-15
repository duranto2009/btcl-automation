<%@ include file="../includes/checkLogin.jsp"%>
<%@page import="client.ClientDTO"%>
<%@page import="client.ClientRepository"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
String amountStr = request.getParameter("amount");
boolean isPin = false;

boolean haspermission = false;
client.ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(loginDTO.getAccountID());

if(clientDTO!=null && (clientDTO.getAuthByPIN() || clientDTO.getCustomerTypeID() == clientDTO.CUSTOMER_TYPE_RESELLER))
{
	haspermission = true;
}
if(!haspermission)
{
	response.sendRedirect("../");
}

boolean isRechargeAble = false;
if(isAgent)
{	
if(clientDTO.getParentAccountNo()==-1)
	isRechargeAble=true;
else
{	
	double amount=Double.parseDouble(amountStr);
	ClientDTO cdto = client.ClientRepository.getInstance().getClient(clientDTO.getParentAccountNo());
	if(cdto.getCustomerTypeID()==ClientDTO.CUSTOMER_TYPE_RESELLER&& cdto.getResellerType()==ClientDTO.RESELLER_TYPE_COMMISION_BASED)
	{	
	double totalAvailableBalance = cdto.getBalance()+cdto.getCreditLimit();
	
	 double amountWithComission=amount*(1-cdto.getCommission()/100.0);
		if(totalAvailableBalance >=amountWithComission )
			isRechargeAble= true;
		else isRechargeAble= false;
	}
	else isRechargeAble=true;
}
}
else
{
	isRechargeAble=true;
}
if(isRechargeAble)
{
%>
<span>OK</span>
<%
}
else
{
%>	
<span>NOT OK</span>;
<%
}
%>