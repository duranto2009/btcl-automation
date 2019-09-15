<%@page import="client.ClientDTO"%>
<%@page import="TokenGenerator.generatepaymentwallToken"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="config.OnlinePaymentConfigDTO"%>
<%@page import="config.PaymentGatewayCredentialDAO"%>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%@ page import="net.authorize.*" %>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="sessionmanager.SessionConstants"%>
<%@ page import="login.LoginDTO"%>
<%@ page import="util.ConformationMessage"%> 
<%@ page import="rechargecard.RechargeCardBatchDAO"%>

<%
Logger logger = Logger.getLogger( "paymentwallReply" );
//logger.debug("Response from Authorize.net "+responseaddress);
String errors = "";

ArrayList<String> validips =  new ArrayList<String>();

validips.add("174.36.92.186");
validips.add("174.36.96.66");
validips.add("174.36.92.187");
validips.add("174.36.92.192");
validips.add("174.37.14.28");

boolean valid = false;
logger.debug("Request From "+request.getRemoteHost().toString());

String requestfromip = request.getRemoteHost().toString();

Map<String, String[]> parameters = request.getParameterMap();
logger.debug(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());
for(String parameter : parameters.keySet()) {

	 logger.debug("("+parameter+")"+request.getParameter(parameter));
}


if(request.getParameter("uid")!=null && request.getParameter("goodsid")!=null)
{
	 logger.debug("Not Null");
	 String login_info[]=request.getParameter("goodsid").split("\\|");
	 
	 //logger.debug("Size of ligin_info "+login_info.length);
	 
	 String PIN=login_info[0];
	 String user_name = request.getParameter("uid");
	 String source_ip   = login_info[1];
	 String paid_money = login_info[2];
	 
	 logger.debug(PIN+" "+user_name+" "+source_ip+" "+paid_money);
	 
	 long accountID=Long.parseLong(PIN);
	 LoginDTO login_dto_for_pin=new LoginDTO();
	 login_dto_for_pin.setAccountID(accountID);
	 login_dto_for_pin.setUsername(user_name);
	 login_dto_for_pin.setLoginSourceIP(source_ip);
	 
	 client.ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(login_dto_for_pin.getAccountID());
	 
	 PaymentGatewayCredentialDAO paymentcredentialdao = new PaymentGatewayCredentialDAO();
	 
	 OnlinePaymentConfigDTO apiinfo = paymentcredentialdao.getPaymentGatewayCredential(clientDTO, OnlinePaymentConfigDTO.paymentwall);
	 
	 String secretkey = apiinfo.getsignaturekey();
	 
	 double tax = apiinfo.getTaxAmount();
	 
	 String requestsignature = request.getParameter("sig");
	 
	 String parameterlist = "uid="+request.getParameter("uid")+"goodsid="+request.getParameter("goodsid")+"slength="+request.getParameter("slength")+"speriod="+request.getParameter("speriod")+"type="+request.getParameter("type")+"ref="+request.getParameter("ref")+secretkey;
	 
	 String md5encryptionvalue = generatepaymentwallToken.md5Java(parameterlist);
	 
	 
	 if(md5encryptionvalue.equals(requestsignature) && validips.contains(requestfromip))
	 {
		 valid = true;
		 logger.debug("Match Found");
		 
		 
		    LoginDTO login_dto_for_paymentwall=new LoginDTO();
			
			login_dto_for_paymentwall.setAccountID(accountID);
			
			login_dto_for_paymentwall.setUsername(user_name);
			
			login_dto_for_paymentwall.setLoginSourceIP(source_ip);
			
			//
			logger.debug("The amount to user "+user_name);
			logger.debug("The id is "+accountID);
			
			String transaction_id = request.getParameter("ref");
			
			String desc="Account Recharged by PaymentWall";
			
			RechargeCardBatchDAO dao=new RechargeCardBatchDAO();
			
			//double amount = Double.parseDouble(paid_money);
			
			double amount = Double.parseDouble(paid_money);
			
			logger.debug("The amount to pay "+amount);
			
			
			if(tax>0)
			{
				 amount-=(amount*tax)/100.0;
				 desc+="after deducting tax "+tax+"%";
			}
			if( clientDTO.getCustomerTypeID()==ClientDTO.CUSTOMER_TYPE_RESELLER&& clientDTO.getResellerType()==ClientDTO.RESELLER_TYPE_COMMISION_BASED)
			{
				 amount=amount*(1+clientDTO.getCommission()/100);
				 desc+=" with comission   "+clientDTO.getCommission()+"%";
			}
			
			dao.rechargeAccountByOnlinePaymentGateway(login_dto_for_paymentwall,amount,transaction_id,desc);
		 
	 }
	 else
	 {
		 errors+= "Invalid Request ";
		 
		 logger.debug("Match Not Found");
	 }
	 
	 
	 
}
else
{
	 logger.debug("It is Null");
	errors+= "Missing Parameters ";
}
if(!valid)
{
	out.write(errors);
}
else
{
	out.write("OK");
}

%>