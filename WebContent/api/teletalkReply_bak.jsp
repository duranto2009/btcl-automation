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
<%@page import="recharge.RechargeDAO"%>
<%@page import="recharge.util.*"%>
<%@page import="login.LoginDTO"%>
<%@page import="recharge.*"%>
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
String validIP[]= {"127.0.0.1","0:0:0:0:0:0:0:1","114.130.64.36","180.234.212.98"};
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
		module = request.getParameter("module");
		if(module == null || module.length() == 0)
		{
			showError("module missing", out);
			return;
		}
		else
		{
			module_parsed = Integer.parseInt(module);
		}
		entity = request.getParameter("document");
		if(entity == null || entity.length() == 0)
		{
			showError("document missing", out);
			return;
		}
		
		/* request_type = request.getParameter("request_type");
		if(request_type == null || request_type.length() == 0)
		{
			showError("request_type missing", out);
			return;
		} */		
		customer_name = request.getParameter("customer_name");
		if(customer_name == null || customer_name.length() == 0)
		{
			showError("customer_name missing", out);
			return;
		}		
		customer_id = request.getParameter("customer_id");
		if(customer_id == null || customer_id.length() == 0)
		{
			showError("customer_id missing", out);
			return;
		}	
		else
		{
			customer_id_parsed = Long.parseLong(customer_id);
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
		payment_end_date = request.getParameter("payment_end_date");
		if(payment_end_date == null || payment_end_date.length() == 0)
		{
			showError("payment_end_date missing", out);
			return;
		}	
		else
		{
			payment_end_date_parsed = sdf.parse(payment_end_date).getTime();
		}
		mobile_no = request.getParameter("mobile_no");
		if(mobile_no == null || mobile_no.length() == 0)
		{
			showError("mobile_no missing", out);
			return;
		}		
		/*
		
		//////////////////////////////validity check////////////
		
		*/
		
		
		
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
		if(billDTO == null || billDTO.isDeleted())
		{
			showError("payment_token invalid", out);
			return;
		}
		if(billDTO.isPaid())
		{
			showError("already paid", out);
			return;
		}		
		long customer_id_db =  billDTO.getClientID();
		int module_db = billDTO.getEntityTypeID() / EntityTypeConstant.MULTIPLIER2;// (domain, hosting etc)
		CommonService commonService = new CommonService();
		EntityDTO entityDTO = commonService.getEntityDTOByEntityIDAndEntityTypeID(billDTO.getEntityTypeID(), billDTO.getEntityID());
		//EntityDTO entityDTO = (EntityDTO)SqlGenerator.getObjectByID(EntityTypeConstant.entityClassMap.get(billDTO.getEntityTypeID()), billDTO.getEntityID(), databaseConnection);
		String entity_db = entityDTO.getName();//billDTO.getEntityName();//(abc.com.bd, myWebHosting etc)
		logger.debug("entity_db " + entity_db);
		//String request_type = billDTO.getRequestType();//(validate, invalidate)
		AllClientRepository.getInstance().getVpnClientByClientID(customer_id_db, module_db);
		ClientService clientService = new ClientService();
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setAccountID(customer_id_db);		
		CommonSelector commonSelector = new CommonSelector();
		commonSelector.moduleID = module_db;
		ClientDetailsDTO clientDetailsDTO = clientService.getClient(customer_id_db, loginDTO, commonSelector);
		ClientContactDetailsDTO clientContactDetailsDTO = clientDetailsDTO.getVpnContactDetails().get(ClientConstants.DETAILS_TYPE_REGISTRANT);
		String firstName = clientContactDetailsDTO.getRegistrantsName();
		String lastName = clientContactDetailsDTO.getRegistrantsLastName();
		String customer_name_db = firstName + " " + lastName;				
		//double payment_amount_db = Math.ceil(((billDTO.getTotal() * 1.15)/( 1 - PaymentConstants.TELETALK.TELETALK_CHARGE)));//(5000)
		double payment_amount_db = Math.ceil(((billDTO.getTotal())/( 1 - PaymentConstants.TELETALK.TELETALK_CHARGE)));//(5000)
		logger.debug("payment_amount_db " + payment_amount_db);
		//String payment_date_db = ;//(2016-04-11 09:30:00)
/* 		long payment_end_date_db = billDTO.getLastPaymentDate();//(2016-04-11, 2016-06-11 etc)
		payment_end_date_db = Math.round(payment_end_date_db / 1000) * 1000;//ignoring the milliseconds */
		String payment_end_date_db = sdf.format(billDTO.getLastPaymentDate());
		logger.debug("payment_end_date_db " + payment_end_date_db);
		String mobile_no_db = clientContactDetailsDTO.getPhoneNumber();// (8801911123653)
		
		
		
		if(module_parsed != module_db)
		{
			showError("module doesn't match", out);
			return;
		}				
		if(!entity.equals(entity_db))
		{
			showError("document doesn't match", out);
			return;
		}
		if(!customer_name.equals(customer_name_db))
		{
			showError("customer_name doesn't match", out);
			return;
		}		
		if(customer_id_parsed != customer_id_db)
		{
			showError("customer_id doesn't match", out);
			return;
		}		
		if(payment_amount_parsed != payment_amount_db)
		{
			showError("payment_amount doesn't match", out);
			return;
		}		
		if(!payment_end_date.equals(payment_end_date_db))
		{
			logger.debug("payment_end_date " + payment_end_date);
			showError("payment_end_date doesn't match", out); 
			return;
		}		
		if(!mobile_no.equals(mobile_no_db))
		{
			showError("mobile_no doesn't match", out);
			return;
		}		
		
		
		remoteIP = request.getRemoteAddr();
	
		logger.debug("remoteIP " + remoteIP);
		//remoteIP = "127.0.0.1";
		//logger.debug("remoteIP " + remoteIP);
	
		
		
		
		boolean loginByUserPass = true;
		boolean loginByIP = true;

 
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setBillID(payment_token_parsed);
		paymentDTO.setDescription("Teletalk Payment");
		paymentDTO.setPaymentTime(payment_date_parsed);
		paymentDTO.setLastModificationTime(System.currentTimeMillis());
		paymentDTO.setPaymentAmount(payment_amount_db);
		paymentDTO.setPaymentGatewayType(PaymentConstants.PAYEMENT_GATEWAY_TYPE_TELETALK);
		paymentDTO.setClientID(customer_id_db);
		
		PaymentService paymentService = new PaymentService();
		paymentService.payBillFromAPI(paymentDTO, loginDTO);
		
		showSuccess(out);

	 
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
	out.println("<reply>"+"FAILED - "+message+"</reply>");
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