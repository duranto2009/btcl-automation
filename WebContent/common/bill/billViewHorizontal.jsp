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
	String reqIDStr = request.getParameter("reqID");
	String[] billIDs = request.getParameterValues( "billIDs" );
	BillDTO[] billDTOs = null;
	
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
	
	if( billIDs != null ){
		billDTOs = new BillDTO[ billIDs.length ];
		for( int i=0;i<billIDs.length;i++){
			
			billDTOs[i] = billService.getBillByBillID( Long.parseLong( billIDs[i] ) );
		}
	}
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

	if(billDTO != null || billDTOs !=null )
	{				
		if( billDTO == null )
			billDTO = billDTOs[0];
		
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
	<title>BTCL - Billing Invoice </title>
	<link href="<%=request.getContextPath() %>/stylesheets/invoice.css" rel="stylesheet">
	<style type="text/css">
	.NB, .teletalk{
		font-size: 10px;
		font-family: times roman;
	}
	</style>
</head>

<body>
	<div class="wrapper" id="printContent" style="width:1400px">
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
								<img  style="position: relative; left: 330px; bottom: 39px;" src="<%=request.getContextPath() %>/assets/images/unpaid.png" title="BTCL - Billing Dept.">
							</td>
							<%}else{%>
								<td width="50%" align="center">
								<img  style="position: relative; left: 330px; bottom: 32px; height: 100px" src="<%=request.getContextPath() %>/assets/images/paid.png" title="BTCL - Billing Dept.">
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
				
				<br/>	
				
				<!-- <table class='items billTable'>
					<thead>
						<tr class='title textcenter'>
							<td width='5%'>Invoice ID</td>
							<td width='5%'>Link Name</td>
							<td width='5%'>BW Connection Charge</td>
							<td width='5%'>OFC Installation Charge</td>
							<td width='5%'>OFC Laying Cost</td>
							<td width='5%'>Establishment Cost</td>
							<td width='5%'>Media Converter Cost</td>
							<td width='5%'>SFP Module Cost</td>
							<td width='5%'>Others</td>
							<td width='5%'>Security Charge</td>
							<td width='5%'>OFC Charge</td>
							<td width='5%'>Bandwidth Charges</td>
							<td width='5%'>Grand Total</td>
							<td width="5%">Discount</td>
							<td width="5%">Total Payable</td>
							<td width='5%'>Vat(15%)</td>
							<td width='5%'>Net Payable</td>
						</tr>
					</thead>
					<tbody> -->
					
						<%
						if( billDTOs != null ){
							
							totalBill = 0;
							for( BillDTO bill : billDTOs ){
								
								totalBill += bill.getBtclAmount() - bill.getDiscount();
							%>
							
								<%=bill.getDescription().replaceAll( "XXXXX", Long.toString( billDTO.getID() ) ) %>
								<br/>
							<%}%>
						<%} else if( billDTO != null ){ 
							totalBill = billDTO.getBtclAmount() - billDTO.getDiscount(); %>
							<b>Invoid ID: <%=billDTO.getID() %></b> &nbsp 
							<%=billDTO.getDescription().replaceAll( "XXXXX", Long.toString( billDTO.getID() ) ) %>
						<%} %>
					<!-- </tbody>
				</table> -->
				<br/>
				<table class="items">
					<tr class="title textcenter">
						<td id="totalPayable">Total : <%=totalBill %></td>
						<td id="vat">VAT : <%=BillDTO.setDecimalPlaces( totalBill * 0.15, 2 ) %></td>
						<td id="netPayable">Net Payable : <%=totalBill + BillDTO.setDecimalPlaces( totalBill * 0.15, 2 ) %></td>
					</tr>
				</table>

					<br />
					<br>

					<div class="NB">
						<br> <strong>N.B:</strong> <br>• Please pay the cash
						amount at Social Islami Bank Limited (S.I B.L.), Eskaton branch,
						Moghbazar, Dhaka. <br>• Only Cheque ( For Govt. offices )
						and Pay order ( For non Govt. offices & others organizations )
						should be in favor of “SAO, TELEPHONE REVENUE, NORTH, DHAKA. <br>
						<br>Address to send Cheque/Pay Order in the following office:
						<br>Account Officer, TELEPHONE REVENUE, Room#407(3rd Floor),
						BTCL, Moghbazar Telephone Bhaban, Dhaka-1217.
					</div>

					<p
						style="width: 100%; text-align: right; color: blue; font-weight: 300;">
						Powered By : Reve Systems</p></div>
		
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
	    });
	    
	    var totalPayables = $(".billTable tr td:nth-last-child(3)");
	    var vat = 0.0;
	    var netPayable = 0.0;
	    
	    var totalPayable = 0.0;
	    for( var i = 1;i<totalPayables.length; i++){
	    	
	    	totalPayable += parseFloat( totalPayables[i].innerText );
	    }
	    
	    vat = totalPayable * 0.15;
	    netPayable = totalPayable * 1.15;
	    
	    //$("#totalPayable")[0].innerText += " " + totalPayable.toFixed(2);
	    //$("#vat")[0].innerText += " " + vat.toFixed(2);
	    //$("#netPayable")[0].innerText += " " + netPayable.toFixed(2);
	})
	</script>
</body>
<%
	} else {
%>No, Data found.<%=billIDs %><%
	}
%>
</html>
