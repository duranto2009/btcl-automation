<%@page import="common.ModuleConstants"%>
<%@page import="common.ModuleContactDetailsService"%>
<%@page import="common.CommonService"%>
<%@page import="common.EntityDTO"%>
<%@page import="util.SqlGenerator"%>
<%@page import="common.CommonSelector"%>
<%@page import="common.payment.PaymentService"%>
<%@page import="common.payment.PaymentDTO"%>
<%@page import="common.payment.constants.PaymentConstants"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="common.ClientConstants"%>
<%@page import="vpn.clientContactDetails.ClientContactDetailsDTO"%>
<%@page import="vpn.client.ClientService"%>
<%@page import="vpn.client.ClientDetailsDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.bill.BillDTO"%>
<%@page import="common.bill.BillService"%>
<%@page import="common.bill.BillDAO"%>
<%@page import="java.net.InetAddress"%>

<%@page import="util.MD5"%> 
<%@page language="java"%>
<%@page errorPage="../common/failure.jsp"%>


<%@taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>


<%@page	import="java.util.ArrayList, sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="util.DAOResult"%>
<%@page import="java.util.*"%>
<%@page import="org.json.simple.*"%>
<%@page import="java.text.DecimalFormat"%>



<%@page import="org.apache.log4j.Logger" %>
<%@page import="java.nio.charset.Charset"%>

<%!
Logger logger=Logger.getLogger("pinBlock");
String username = null;
String userNonce = null;
String password = null;
//String validIP[]= {"127.0.0.1","0:0:0:0:0:0:0:1","114.130.64.36","180.234.212.98","43.240.101.66"};
String validIP[]= {"103.230.104.206"};
String validSubnet="";
String teletalkUsername = "teletalk";
String teletalkPassword = "teletalk";

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>

<%
	logger.debug("request.getRemoteAddr() " + request.getRemoteAddr());
	boolean fromValidIP=false;
	String fromIP=null;

	for(int i=0;i<validIP.length;i++) {	
		try{		
			String requestIP= InetAddress.getByName(validIP[i]).getHostAddress();	
			if(request.getRemoteAddr().equals(requestIP)){
				fromValidIP=true;
				fromIP=validIP[i];
				break;
			}
			
		} catch (Exception ex) {}
	}

	if(!fromValidIP)
	{
		if(validSubnet.length() > 0 && request.getRemoteAddr().startsWith(validSubnet))
		{
			fromValidIP=true;
			fromIP=request.getRemoteAddr();
		}	
	}

	if(!fromValidIP)
	{
		showError("request from invalid ip", out);
		return;			
	}

	
	
	//String countryID = null;
	String userID = null;
	String remoteIP = null;

	
	

	String module = null;// (domain, hosting etc)
	String entity = null;//(abc.com.bd, myWebHosting etc)
	String request_type = null;//(validate, invalidate)
	String customer_name = null;// (Md. Rahman)
	String customer_id = null;//(12563256)
	String payment_token = null;// (654654654688)
	String payment_amount = null;//(5000)
	String payment_date = null;//(2016-04-11 09:30:00)
	String payment_end_date = null;//(2016-04-11, 2016-06-11 etc)
	String mobile_no = null;// (8801911123653)


	int module_parsed = -10;// (domain, hosting etc)
	long customer_id_parsed = -10;//(12563256)
	long payment_token_parsed = -10;// (654654654688)
	double payment_amount_parsed = -10;//(5000)
	long payment_date_parsed = -10;
	long payment_end_date_parsed = -10;
	//rechargeDTO = new RechargeDTO();
	long currentTime = System.currentTimeMillis();
	//logger.debug("This is test : " + rechargeDTO);
	
	 
	
	try{
		username = request.getParameter("user");
		if(username == null || username.length() == 0)
		{
			showError("user missing", out);
			return;
		}
		userNonce = request.getParameter("nonce");
		password = request.getParameter("password");		
		if(password == null || password.length() == 0)
		{
			showError("password missing", out);
			return;
		}		
		
		request_type = request.getParameter("request_type");
		if(request_type == null || request_type.length() == 0)
		{
			showError("request_type missing", out);
			return;
		}
		
		payment_token = request.getParameter("payment_token");
		if(payment_token == null || payment_token.length() == 0)
		{
			showError("payment_token missing", out);
			return;
		}
		else
		{
			payment_token_parsed = Long.parseLong(payment_token);
		}

		if(!teletalkUsername.equals(username))
		{
			showError("username doesn't match", out);
			return;
		}
		if(!teletalkPassword.equals(password))
		{
			showError("password doesn't match", out);
			return;
		}	
		BillService billService = new BillService();
		BillDTO billDTO = billService.getBillByBillID(Long.parseLong(payment_token));


		if(request_type.equals("tokenRecheck"))
		{
			if(billDTO == null || billDTO.isDeleted())
			{
				showError("INVALID TOKEN", out);
				return;
			}
			if(billDTO.isPaid())
			{
				showError("INVALID TOKEN", out);
				return;
			}	
			if(billDTO.getLastPaymentDate() < currentTime)
			{
				showError("INVALID TOKEN", out);
				return;
			}
		}
		else
		{
			if(billDTO == null || billDTO.isDeleted())
			{
				showError("INVALID TOKEN", out);
				return;
			}
			if(billDTO.isPaid())
			{
				showError("ALREADY PAID", out);
				return;
			}	
			if(billDTO.getLastPaymentDate() < currentTime)
			{
				showError("EXPIRED TOKEN", out);
				return;
			}
		}
		long customer_id_db =  billDTO.getClientID();
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setAccountID(customer_id_db);	
		//double payment_amount_db = Math.ceil(((billDTO.getTotal())/( 1 - PaymentConstants.TELETALK.TELETALK_CHARGE)));//(5000)
		double payment_amount_db = Math.ceil(billDTO.getNetPayable() /( 1 - PaymentConstants.TELETALK.TELETALK_CHARGE));//(5000)
		
		if(request_type.equals("tokenCheck"))
		{							
			int module_db = billDTO.getEntityTypeID() / EntityTypeConstant.MULTIPLIER2;// (domain, hosting etc)
			CommonService commonService = new CommonService();
			EntityDTO entityDTO = commonService.getEntityDTOByEntityIDAndEntityTypeID(billDTO.getEntityTypeID(), billDTO.getEntityID());
			String entity_db = entityDTO.getName();
			AllClientRepository.getInstance().getVpnClientByClientID(customer_id_db, module_db);
			ClientService clientService = new ClientService();
	
			CommonSelector commonSelector = new CommonSelector();
			commonSelector.moduleID = module_db;
			ClientDetailsDTO clientDetailsDTO = clientService.getClient(customer_id_db, loginDTO, commonSelector);
			ClientContactDetailsDTO clientContactDetailsDTO = clientDetailsDTO.getVpnContactDetails().get(ClientConstants.DETAILS_TYPE_REGISTRANT);
			String firstName = clientContactDetailsDTO.getRegistrantsName();
			String lastName = clientContactDetailsDTO.getRegistrantsLastName();
			String customer_name_db = firstName + " " + lastName;				

			logger.debug("payment_amount_db " + payment_amount_db);
			String payment_end_date_db = sdf.format(billDTO.getLastPaymentDate());
			logger.debug("payment_end_date_db " + payment_end_date_db);
			String mobile_no_db = clientContactDetailsDTO.getPhoneNumber();
			
			
			remoteIP = request.getRemoteAddr();
		
			logger.debug("remoteIP " + remoteIP);

			out.print("<reply>");
			String message = "SUCCESS|" + ModuleConstants.ModuleMap.get(1) + "|" + entity_db + "|" + payment_amount_db + "|" + payment_end_date_db + "|" + customer_id_db + "|" + customer_name_db + "|" + mobile_no_db;
			out.print(message);
			out.println("</reply>");
			return;
		}
		else if(request_type.equals("pay"))
		{
			
			payment_amount = request.getParameter("payment_amount");
			if(payment_amount == null || payment_amount.length() == 0)
			{
				showError("payment_amount missing", out);
				return;
			}		
			else
			{
				payment_amount_parsed = Double.parseDouble(payment_amount);
			}
			
			payment_date = request.getParameter("payment_date");
			if(payment_date == null || payment_date.length() == 0)
			{
				showError("payment_date missing", out);
				return;
			}
			else
			{
				payment_date_parsed = sdf.parse(payment_date).getTime();
			}			
			
			if(payment_amount_parsed != payment_amount_db)
			{
				showError("payment_amount doesn't match", out);
				return;
			}
			PaymentDTO paymentDTO = new PaymentDTO();
			//paymentDTO.setBillID(payment_token_parsed);
			paymentDTO.setDescription("Teletalk Payment");
			long paymentTime = currentTime;
			/* paymentDTO.setPaymentTime(payment_date_parsed); */
			paymentDTO.setPaymentTime(currentTime);
			long[] billIDs = new long[1];
			billIDs[0] = billDTO.getID();
			paymentDTO.setBillIDs(billIDs);
			
			paymentDTO.setLastModificationTime(currentTime);
			paymentDTO.setPayableAmount(billDTO.getNetPayable());
			paymentDTO.setBtclAmount(billDTO.getTotalPayable());
			paymentDTO.setVatAmount(billDTO.getVAT());
			paymentDTO.setPaidAmount(payment_amount_db);
			paymentDTO.setVatIncluded(1);
			paymentDTO.setPaymentStatus(PaymentConstants.PAYMENT_STATUS_APPROVED);
			
			paymentDTO.setPaymentGatewayType(PaymentConstants.PAYEMENT_GATEWAY_TYPE_TELETALK);
			paymentDTO.setClientID(customer_id_db);
			
			//PaymentService paymentService = new PaymentService();
			PaymentService paymentService = ServiceDAOFactory.getService(PaymentServiceDomain.class);
			paymentService.insertTeletalkPayment(paymentDTO, loginDTO);
			
			showSuccess(out);
		}
		else if(request_type.equals("tokenRecheck"))
		{

			out.println("<reply>VALID TOKEN</reply>");
			return;
		}
		else
		{
			showError("INVALID REQUEST", out);
			return;
		}
	 
	} catch(Exception ex){
		logger.debug("Fatal", ex);
		showError("System internal error", out); 
/* 		out.println(jsonShowError);
		return; */	
						
	}%>

<%!
public void showError(String message, JspWriter out) throws Exception
{
	/* JSONObject jsonShowError = new JSONObject();
	jsonShowError.put("FAILED",	message);
	out.println("<reply>");
	out.println("FAILED - "+ message);
	//out.println(jsonShowError);
	out.println("</reply>"); */
	out.println("<reply>"+message+"</reply>");
	return;
}

public void showSuccess(JspWriter out) throws Exception
{
/* 	JSONObject jsonShowError = new JSONObject();
	jsonShowError.put("SUCCESS","");
	out.println("<reply>");
	out.println("SUCCESS");
	//out.println(jsonShowError);
	out.println("</reply>"); */
	out.println("<reply>SUCCESS</reply>");
	return;
}
%>