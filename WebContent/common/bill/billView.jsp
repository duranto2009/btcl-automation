<%@page import="common.ModuleConstants"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="common.payment.constants.PaymentConstants"%>
<%@page import="common.RequestFailureException"%>
<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="javax.swing.text.html.parser.DTD"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.IOException"%>
<%@page import="java.net.MalformedURLException"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.net.URL"%>
<%@page import="com.gargoylesoftware.htmlunit.WebResponse"%>
<%@page import="com.gargoylesoftware.htmlunit.Page"%>
<%@page import="com.gargoylesoftware.htmlunit.WebClient"%>
<%@page import="common.bill.BillService"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="vpn.clientContactDetails.ClientContactDetailsDTO"%>
<%@page import="common.CommonSelector"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="vpn.client.ClientDetailsDTO"%>
<%@page import="vpn.client.ClientService"%>
<%@page import="common.bill.BillDTO"%>
<%@page import="common.payment.PaymentService"%>
<html lang="en">
<head>
<%
String ID = request.getParameter("id");
%>

<%

	String reqIDStr = request.getParameter("reqID");
	long reqID = -1;

	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	long id = 0;
	long clientID = 0;
	int moduleID = 0;
	boolean showBill = false;
	BillDTO billDTO = null;
	double totalBill = 0;
	//PaymentService paymentService = new PaymentService();
	BillService billService = new BillService();
	if (ID != null && !ID.equals("")) {
		id = Long.parseLong(ID);
		if(id > 0)
		{
			billDTO = billService.getBillByBillID(id);						
		}
	}
	else if(reqIDStr != null && !reqIDStr.equals(""))
	{
		reqID = Long.parseLong(reqIDStr);
		if(reqID > 0)
		{
			billDTO = billService.getBillByReqID(reqID);
			
		}
	}

	if(billDTO != null)
	{				
		clientID = billDTO.getClientID();
		
		boolean hasPermission = false;
		int permission;

		if(loginDTO.getUserID()>0)
		{
		    if((loginDTO.getMenuPermission(PermissionConstants.BILL) !=-1) &&(loginDTO.getMenuPermission(PermissionConstants.BILL_SEARCH) >= PermissionConstants.PERMISSION_READ))
		    {
		        hasPermission=true;
		    }
		}
		else if( loginDTO.getAccountID() >0 ){
			
			if( clientID == loginDTO.getAccountID() ){
				
				hasPermission = true;
			}
		}

		if( !hasPermission )
		{	
			//throw new RequestFailureException("You don't have permission to see this page" );
			CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();	
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
			response.sendRedirect("../");
			return;
		}
		
		moduleID = EntityTypeConstant.entityModuleIDMap.get(billDTO.getEntityTypeID());
		totalBill = billDTO.getNetPayable();

		System.out.println("ClientID : " + clientID + " Module :" + moduleID);
		CommonSelector commonSelector = new CommonSelector();
		commonSelector.moduleID = moduleID;
		ClientService service = new ClientService();
		ClientDetailsDTO clientDTO = service.getClient(clientID, loginDTO, commonSelector);

		ClientContactDetailsDTO regContactDetailsDTO = clientDTO.getVpnContactDetails().get(0);
		ClientContactDetailsDTO billiContactDetailsDTO = clientDTO.getVpnContactDetails().get(1);
		DecimalFormat formatter = new DecimalFormat("#0.0");
%>

<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>BTCL - Billing Invoice #<%=ID %></title>
<link href="<%=request.getContextPath() %>/stylesheets/invoice.css" rel="stylesheet">
<style type="text/css">
.NB, .teletalk{
	font-size: 12px;
	font-family: times roman;
}
</style>
</head>

<body>
	<div class="wrapper" id="printContent">
		<div class="topView">
		<div class="backView"></div>
			<div style="width:40%;float:right">
				<%if(!billDTO.isPaid()){ %>
					<img width="40%"  style="position: relative; text-align: right; margin-right: -30px; margin-top: -25px;float:right" src="<%=request.getContextPath() %>/assets/images/unpaid.png" title="BTCL - Billing Dept.">
				<%}else{%>					
					<img width="40%" style="position: relative; text-align: right; margin-right: -30px; margin-top: -25px;float:right" src="<%=request.getContextPath() %>/assets/images/paid.png" title="BTCL - Billing Dept.">				
				<% } %>			
			</div>			
			<div style="width:60%;float:left">
				<table class="header">
					<tbody>
						<tr>
							<td width="50%" style="margin:0px;padding:0px">
								
									<img src="<%=request.getContextPath() %>/images/common/logo.png"
										title="BTCL - Billing Dept.">
								
							</td>
							<%-- <%if(!billDTO.isPaid()){ %>
							<td width="50%" align="center">
								<img width="40%"  style="position: relative; left: 141px; bottom: 39px;" src="<%=request.getContextPath() %>/assets/images/unpaid.png" title="BTCL - Billing Dept.">
							</td>
							<%}else{%>
								<td width="50%" align="center">
								<img width="40%" style="position: relative; left: 139px; bottom: 32px; height: 100px" src="<%=request.getContextPath() %>/assets/images/paid.png" title="BTCL - Billing Dept.">
							</td>
							<% } %> --%>
						</tr>
					</tbody>
				</table>
				</div>
				<table class="items">
					<tbody>
						<tr>
							<td width="100%">

								<div class="addressbox">

									<strong>Invoiced To</strong><br>Customer ID : <%=clientID %><br>Name :
									<%=regContactDetailsDTO.getFullName()%>
									<br>Customer User Name: <%=AllClientRepository.getInstance().getClientByClientID(clientID).getLoginName()%>
									
									<br>Address
									:
									<%=billiContactDetailsDTO.getAddress() == null ? "": billiContactDetailsDTO.getAddress()%>, <br>
									<%=billiContactDetailsDTO.getCity() == null ? "": billiContactDetailsDTO.getCity()%>
									-
									<%=billiContactDetailsDTO.getPostCode() == null ? "": billiContactDetailsDTO.getPostCode()%>,Bangladesh<br>

								</div>

							</td>
						</tr>
					</tbody>
				</table>
				<div class="row" style="margin:10px 0">
					<span class="title">Invoice ID: <%=billDTO.getID()%></span><br> Invoice Date:
					<%=TimeConverter.getTimeStringFromLong(billDTO.getGenerationTime())%><br>
					Due Date:
					<%
					if(billDTO.getEntityTypeID() / EntityTypeConstant.MULTIPLIER2 == ModuleConstants.Module_ID_DOMAIN)
					{
						billDTO.setLastPaymentDate(billDTO.getGenerationTime() + (15 * 86400000L));
					}
					%>
					<%=TimeConverter.getTimeStringFromLong(billDTO.getLastPaymentDate())%>
				</div>
				<%=billDTO.getDescription()%>
				<div><b>Teletalk Payment Procedure</b></div><br>
				<table class="items">
				<tr class="title">
				<td>Total Amount</td>
				<td>Teletalk service charge</td>
				<td>Total payable using teletalk</td>
				</tr>
				<tr class="title">
				<td><%=billDTO.getNetPayable()%></td>
				<%
				double teletalkTotalPayable = Math.ceil(billDTO.getNetPayable() /( 1 - PaymentConstants.TELETALK.TELETALK_CHARGE));
				double teletalkCharge =  teletalkTotalPayable - billDTO.getNetPayable();
				%>				
				<td><%=formatter.format(teletalkCharge)%></td>
				<td width="30%"><%=formatter.format(teletalkTotalPayable)%></td>
				</tr>				
				<tr><td colspan="3">
				&#9755 <span style="color:red">At most <b>BDT 8900</b> is allowed to pay using Teletalk</span>
					
					<br><br><b>Payment steps</b>
					<br>&#9755 Recharge necessary amount at Teletalk prepaid SIM.
					<br>&#9755 Type <b>BTCL</b>&ltspace&gt<b><%=billDTO.getID()%></b> & then send to 16222.
					<br>&#9755 To confirm, type <b>BTCL</b>&ltspace&gt<b>YES</b>&ltspace&gt<b>8 digit pin number from return SMS</b> & send to 16222.
					</td>
					</tr>
				</table>
				<br><b>Manual bank payment procedure</b><br><br>
				<table class="items">
				<tr>
				<td>
					&#9755 Pay the cash amount, at Social Islami Bank Limited (SIBL), Eskaton branch, Moghbazar, Dhaka.
					<br>&#9755 After payment, bring a photocopy of money receipt to Senior Accounts Officer, Moghbazar Telephone bhaban, Dhaka-1217.
					<br>&#9755 Only cheque (For Govt. offices) and Pay order (For non Govt. offices & others organizations) should be in favor of "SAO, Telephone Revenue, North, Dhaka"".
					<br><br><b> Address to send cheque/Pay Order:</b> 
					<br>&#9755 Senior Account Officer, Telephone Revenue, BTCL, Moghbazar Telephone Bhaban, Dhaka-1217.				
				
				</td>
				</tr>
				</table>				
				<p
					style="width: 100%; text-align: right; color: blue; font-weight: 300;margin-bottom:0px;">
					Powered By : Reve Systems</p>
			</div>
		
	</div>
	<p align="center">
		<a href="<%=request.getHeader("referer")%>"> Back to Previous Page</a> | 
		<a href="javascript:;" id="printBtn">Download</a> | 
		<a href="<%=request.getContextPath()%>">Dashboard</a>
	</p>
	<script src="<%=request.getContextPath() %>/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath() %>/assets/scripts/printThis.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(document).ready(function(){
	    $("#printBtn").click(function(){
			$("#printContent").printThis();
	    })
	})
	</script>
</body>
<%
	} else {
%>No, Data found.<%
	}
%>
</html>
