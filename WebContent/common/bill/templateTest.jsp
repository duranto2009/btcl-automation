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
		totalBill = billDTO.getBtclAmount()+billDTO.getVAT();

		System.out.println("ClientID : " + clientID + " Module :" + moduleID);
		CommonSelector commonSelector = new CommonSelector();
		commonSelector.moduleID = moduleID;
		ClientService service = new ClientService();
		ClientDetailsDTO clientDTO = service.getClient(clientID, loginDTO, commonSelector);

		ClientContactDetailsDTO billiContactDetailsDTO = clientDTO.getVpnContactDetails().get(1);
%>

<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>BTCL - Billing Invoice #<%=ID %></title>
<link href="<%=request.getContextPath() %>/stylesheets/invoice.css" rel="stylesheet">
<style type="text/css">
.NB, .teletalk{
	font-size: 10px;
	font-family: times roman;
}
</style>
</head>

<body>
	<div class="wrapper" id="printContent">
		<div class="topView">
			<div class="backView"></div>
				<table class="header">
					<tbody>
						<tr>
							<td width="50%" nowrap="">
								<p>
									<img src="<%=request.getContextPath() %>/images/common/logo.png"
										title="BTCL - Billing Dept.">
								</p>
							</td>
							<%if(!billDTO.isPaid()){ %>
							<td width="50%" align="center">
								<img  style="position: relative; left: 141px; bottom: 39px;" src="<%=request.getContextPath() %>/assets/images/unpaid.png" title="BTCL - Billing Dept.">
							</td>
							<%}else{%>
								<td width="50%" align="center">
								<img  style="position: relative; left: 139px; bottom: 32px; height: 100px" src="<%=request.getContextPath() %>/assets/images/paid.png" title="BTCL - Billing Dept.">
							</td>
							<% } %>
						</tr>
					</tbody>
				</table>
				<table class="items">
					<tbody>
						<tr>
							<td width="100%">

								<div class="addressbox">

									<strong>Invoiced To</strong><br>Customer ID : <%=clientID %><br>Name :
									<%=billiContactDetailsDTO.getRegistrantsName() +" "+billiContactDetailsDTO.getRegistrantsLastName() %>
									<br>Customer User Name: <%=AllClientRepository.getInstance().getClientByClientID(clientID).getLoginName()%>
									
									<br>Address
									:
									<%=billiContactDetailsDTO.getAddress()%>, <br>
									<%=billiContactDetailsDTO.getCity()%>
									-
									<%=billiContactDetailsDTO.getPostCode()%>,Bangladesh<br>

								</div>

							</td>
						</tr>
					</tbody>
				</table>
				<div class="row">
					<span class="title">Invoice ID: <%=billDTO.getID()%></span><br> Invoice Date:
					<%=TimeConverter.getTimeStringFromLong(billDTO.getGenerationTime())%><br>
					Due Date:
					<%=TimeConverter.getTimeStringFromLong(billDTO.getLastPaymentDate())%>
				</div>
				
				
				<jsp:include page="../../vpn/link/linkDetails.jsp">
					<jsp:param value="67001" name="id"/>
				</jsp:include>
				
				<jsp:include page="../../vpn/link/endPointDetails.jsp">
					<jsp:param value="66001" name="nearEndID"/>
					<jsp:param value="66001" name="farEndID"/>
				</jsp:include>
				
				<br>
				<table class="items">
				<tr class="title">
				<td>Pay via</td>
				<td>Additinal Charge</td>
				<td>Total</td>
				</tr>
				<tr class="title">
				<td>Teletalk</td>
				<td><%=((billDTO.getTotal() / .92) - billDTO.getTotal()) %></td>
				<td width="30%"><%=(billDTO.getTotal() / .92)%></td>
				</tr>				
				</table>
				<div class="NB">
					<br>
					<strong>N.B:</strong>
					<br>• Please pay the cash amount at Social Islami Bank Limited (S.I B.L.), Eskaton branch, Moghbazar, Dhaka.
					<br>• Only Cheque ( For Govt. offices ) and Pay order ( For non Govt. offices & others organizations ) should be in favor of “SAO, TELEPHONE REVENUE, NORTH, DHAKA.
					<br><br>Address to send Cheque/Pay Order in the following office:
					<br>Account Officer, TELEPHONE REVENUE, Room#407(3rd Floor), BTCL, Moghbazar Telephone Bhaban, Dhaka-1217.
				</div>

				<p
					style="width: 100%; text-align: right; color: blue; font-weight: 300;">
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
