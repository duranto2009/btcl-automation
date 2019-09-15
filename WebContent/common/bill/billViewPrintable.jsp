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
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>BTCL - Billing Invoice ID: <%=ID %></title>
<style type="text/css">
/* *****************************************************

     WHMCS Printable Invoice CSS Stylesheet
     Created: 1st September 2011
     Last Updated: 20th November 2011
     Version: 1.3

     This file is part of the WHMCS Billing Software
       http://www.whmcs.com/

***************************************************** */
body {
	margin: 30px;
	background-color: #eaeaea;
}

body, td, input, select {
	font-family: Arial;
	font-size: 13px;
	color: #000000;
	line-height: 1.4em;
}

.clear {
	clear: both;
}

form {
	margin: 0px;
}

a {
	font-size: 14px;
	color: #1E598A;
	padding: 10px;
}

a:hover {
	text-decoration: none;
}

.textcenter {
	text-align: center;
}

.textright {
	text-align: right;
}

.wrapper {
	margin: 0 auto 30px;
	padding: 30px;
	width: 640px;
	background-color: #fff;
	border: 1px solid #ccc;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
	-moz-box-shadow: 0 0 2px rgba(0, 0, 0, .1);
	-webkit-box-shadow: 0 0 2px rgba(0, 0, 0, .1);
	box-shadow: 0 0 2px rgba(0, 0, 0, .1);
	background-color: #fff;
}

.backView {
	position: absolute;
	background: url(logo.png);
	z-index: -1;
	opacity: .05;
	width: 100%;
	height: 100%;
	top: 0;
	bottom: 0;
	left: 0;
	right: 0;
	-moz-transform: rotate(-1deg);
}

.topView {
	position: relative;
	opacity: .9;
	z-index: 1;
}

.date, .duedate {
	-webkit-border-radius: 3px;
	-moz-border-radius: 3px;
	border-radius: 3px;
	background-color: #fff;
	-webkit-box-shadow: 0 1px 3px rgba(0, 0, 0, .11), inset 0 -2px 2px
		rgba(0, 0, 0, .05);
	-moz-box-shadow: 0 1px 3px rgba(0, 0, 0, .11), inset 0 -2px 2px
		rgba(0, 0, 0, .05);
	box-shadow: 0 1px 3px rgba(0, 0, 0, .11), inset 0 -2px 2px
		rgba(0, 0, 0, .05);
	border: solid 1px #cfcfcf;
	float: right;
	font-size: 120%;
	padding: 7px 12px;
	margin-left: 15px;
}

.date strong {
	color: #007cb6;
}

.duedate strong {
	color: #af0000;
}

.header {
	margin: 0 0 15px 0;
	width: 100%;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
}

table.address {
	background: #eaeaea;
	width: 100%;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
}

table.address td {
	background-color: #fff;
	border: 1px solid #ddd;
	padding: 15px;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
	vertical-align: top;
}

table.items {
	width: 100%;
	background-color: #ccc;
	border-spacing: 0;
	border-collapse: separate;
	border-left: 1px solid #ccc;
}

table.items tr.title td {
	margin: 0;
	padding: 8px 12px;
	line-height: 16px;
	border: 1px solid #ccc;
	border-bottom: 0;
	border-left: 0;
	color: #333;
	font-size: 14px;
	font-weight: bold;
	background-color: #fafafa;
	background-image: -webkit-linear-gradient(bottom, rgba(1, 2, 2, .03),
		rgba(255, 255, 255, .03));
	background-image: -moz-linear-gradient(bottom, rgba(1, 2, 2, .03),
		rgba(255, 255, 255, .03));
	background-image: -o-linear-gradient(bottom, rgba(1, 2, 2, .03),
		rgba(255, 255, 255, .03));
	background-image: -ms-linear-gradient(bottom, rgba(1, 2, 2, .03),
		rgba(255, 255, 255, .03));
	background-image: linear-gradient(to top, rgba(1, 2, 2, .03),
		rgba(255, 255, 255, .03));
}

table.items td {
	margin: 0;
	padding: 8px;
	line-height: 15px;
	background-color: #fff;
	border: 1px solid #ccc;
	border-bottom: 0;
	border-left: 0;
}

table.items tr:last-child td {
	border-bottom: 1px solid #ccc;
}

.row {
	margin: 25px 0;
}

.title {
	font-size: 24px;
	font-weight: bold;
	line-height: 35px;
}

.subtitle {
	font-size: 18px;
	font-weight: bold;
}

.unpaid {
	font-size: 16px;
	color: #cc0000;
	font-weight: bold;
}

.paid {
	font-size: 16px;
	color: #779500;
	font-weight: bold;
}

.refunded {
	font-size: 16px;
	color: #224488;
	font-weight: bold;
}

.cancelled {
	font-size: 16px;
	color: #cccccc;
	font-weight: bold;
}

.collections {
	font-size: 16px;
	color: #ffcc00;
	font-weight: bold;
}

.creditbox {
	margin: 0 auto 15px auto;
	padding: 10px;
	border: 1px dashed #cc0000;
	font-weight: bold;
	background-color: #FBEEEB;
	text-align: center;
	width: 95%;
	color: #cc0000;
}

.btn, i {
	background: #ffffff;
	border: 1px solid #cdd1d5;
	color: #455e94;
	cursor: pointer;
	display: inline-block;
	font-size: 105%;
	-webkit-box-shadow: inset 0px -1px 0px 0px rgba(250, 250, 250, 1);
	-moz-box-shadow: inset 0px -1px 0px 0px rgba(250, 250, 250, 1);
	box-shadow: inset 0px -1px 0px 0px rgba(250, 250, 250, 1);
	line-height: 21px;
	margin-bottom: 0;
	padding: 6px 13px;
	text-align: center;
	text-decoration: none;
	zoom: 1;
	border-radius: 3px;
	moz-border-radius: 3px;
	-webkit-border-radius: 3px;
}

.btn-primary, input[type=submit] {
	border-radius: 3px;
	moz-border-radius: 3px;
	-webkit-border-radius: 3px;
	padding: 6px 13px;
	background: #008db5;
	background: -moz-linear-gradient(top, #008db5 0%, #008db5 50%, #008db5 100%);
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #008db5),
		color-stop(50%, #008db5), color-stop(100%, #008db5));
	background: -webkit-linear-gradient(top, #008db5 0%, #008db5 50%, #008db5 100%);
	background: -o-linear-gradient(top, #008db5 0%, #008db5 50%, #008db5 100%);
	background: -ms-linear-gradient(top, #008db5 0%, #008db5 50%, #008db5 100%);
	background: linear-gradient(to bottom, #008db5 0%, #008db5 50%, #008db5 100%);
	filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#008db5',
		endColorstr='#008db5', GradientType=0);
	-webkit-box-shadow: inset 0px 1px 1px 0px rgba(0, 150, 187, 1), inset
		0px -1px 1px 0px rgba(0, 150, 187, 1);;
	-moz-box-shadow: inset 0px 1px 1px 0px rgba(0, 150, 187, 1), inset 0px
		-1px 1px 0px rgba(0, 150, 187, 1);;
	box-shadow: inset 0px 1px 1px 0px rgba(0, 150, 187, 1), inset 0px -1px
		1px 0px rgba(0, 150, 187, 1);;
	color: #fff;
	border: 1px solid #007697;
}

.btn-success {
	background: #28af62;
	background: -moz-linear-gradient(top, #28af62 0%, #27ae60 50%, #27ae60 100%, #27ae60
		100%);
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #28af62),
		color-stop(50%, #27ae60), color-stop(100%, #27ae60),
		color-stop(100%, #27ae60));
	background: -webkit-linear-gradient(top, #28af62 0%, #27ae60 50%, #27ae60 100%,
		#27ae60 100%);
	background: -o-linear-gradient(top, #28af62 0%, #27ae60 50%, #27ae60 100%, #27ae60
		100%);
	background: -ms-linear-gradient(top, #28af62 0%, #27ae60 50%, #27ae60 100%, #27ae60
		100%);
	background: linear-gradient(to bottom, #28af62 0%, #27ae60 50%, #27ae60 100%,
		#27ae60 100%);
	filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#28af62',
		endColorstr='#27ae60', GradientType=0);
	-webkit-box-shadow: inset 0px 1px 1px 0px rgba(42, 181, 105, 1), inset
		0px -1px 1px 0px rgba(37, 165, 91, 1);;
	-moz-box-shadow: inset 0px 1px 1px 0px rgba(42, 181, 105, 1), inset 0px
		-1px 1px 0px rgba(37, 165, 91, 1);;
	box-shadow: inset 0px 1px 1px 0px rgba(42, 181, 105, 1), inset 0px -1px
		1px 0px rgba(37, 165, 91, 1);
	border: 1px solid #25a55b;
	color: #fff;
}

.btn:hover {
	background: #eee;
}

.btn-primary:hover {
	background: #00719f;
}

.btn-success:hover {
	background: #5ba000;
}

input[type=text], input[type=password], select, textarea, input[type=email],
	input[type=file] {
	background-color: #fff;
	box-shadow: 0 2px 2px rgba(255, 255, 255, .1), inset 1px 2px 0
		rgba(207, 211, 218, .15);
	border: 1px solid #ccc;
	-moz-box-shadow: 0 2px 2px rgba(255, 255, 255, .1), inset 1px 2px 0
		rgba(207, 211, 218, .15);
	border: 1px solid #ccc;
	-webkit-box-shadow: 0 2px 2px rgba(255, 255, 255, .1), inset 1px 2px 0
		rgba(207, 211, 218, .15);
	border: 1px solid #ccc;
	padding: 6px;
	border: 1px solid #bbcad1;
	-moz-transition: -moz-box-shadow 0.3s;
	-webkit-transition: -webkit-box-shadow 0.3s;
	transition: box-shadow 0.3s;
	color: #828f9e;
}

input:focus, select:focus, textarea:focus {
	border: solid 1px #0f90c4;
	-webkit-box-shadow: 0 0 6px rgba(6, 152, 255, .35);
	-moz-box-shadow: 0 0 6px rgba(6, 152, 255, .35);
	box-shadow: 0 0 6px rgba(6, 152, 255, .35);
	outline: none;
}

input:disabled, select:disabled, textarea::disabled {
	filter: Alpha(opacity = 60);
	opacity: 0.6;
}

input[type="file"], input[type="file"]:focus {
	background: 0;
	border: 0;
	-webkit-box-shadow: none;
	-moz-box-shadow: none;
	box-shadow: none;
	padding: 0;
}

input[type="checkbox"] {
	background: none;
	border: 0;
	-webkit-border-radius: 0;
	-moz-border-radius: 0;
	border-radius: 0;
	-webkit-box-shadow: none;
	-moz-box-shadow: none;
	box-shadow: none;
	padding: 0;
	width: auto !important;
}

.NB, .teletalk {
	font-size: 10px;
	font-family: times roman;
}
</style>
</head>
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
		moduleID = EntityTypeConstant.entityModuleIDMap.get(billDTO.getEntityTypeID());
		totalBill = billDTO.getBtclAmount()+billDTO.getVAT();

		System.out.println("ClientID : " + clientID + " Module :" + moduleID);
		CommonSelector commonSelector = new CommonSelector();
		commonSelector.moduleID = moduleID;
		ClientService service = new ClientService();
		ClientDetailsDTO clientDTO = service.getClient(clientID, loginDTO, commonSelector);

		ClientContactDetailsDTO billiContactDetailsDTO = clientDTO.getVpnContactDetails().get(1);
%>
<body>
	<div class="wrapper" id="printContent">
		<div class="topView">
			<div class="backView"></div>
				<table class="header">
					<tbody>
						<tr>
							<td width="50%" nowrap="">
								<p>
									<img src="logo.png"
										title="BTCL - Billing Dept.">
								</p>
							</td>
							<%if(!billDTO.isPaid()){ %>
							<td width="50%" align="center">
								<img  style="position: relative; left: 141px; bottom: 39px;" src="unpaid.png" title="BTCL - Billing Dept.">
							</td>
							<%}else{%>
								<td width="50%" align="center">
								<img  style="position: relative; left: 139px; bottom: 32px; height: 100px" src="paid.png" title="BTCL - Billing Dept.">
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
									<%=billiContactDetailsDTO.getRegistrantsName() +" "+billiContactDetailsDTO.getRegistrantsLastName() %><br>Address
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
				<%=billDTO.getDescription()%>
				<%-- <div class="teletalk" style="font-size:15px">
					<br>
					<strong>If you pay via teletalk, extra <%=(totalBill/0.92)-totalBill %> BDT charge will be added with total amount.</strong>
				</div> --%>
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
					<br>• Please pay the cash amount at Social Investment Bank Limited (S.I B.L.), Eskaton branch, Moghbazar, Dhaka.
					<br>• Only Cheque ( For Govt. offices ) and Pay order ( For non Govt. offices & others organizations ) should be in favor of “SAO, TELEPHONE REVENUE, NORTH, DHAKA.
					<br><br>Address to send Cheque/Pay Order in the following office:
					<br>Account Officer, TELEPHONE REVENUE, Room#407(3rd Floor), BTCL, Moghbazar Telephone Bhaban, Dhaka-1217.
				</div>

				<p
					style="width: 100%; text-align: right; color: blue; font-weight: 300;">
					Powered By : Reve Systems</p>
			</div>
		
	</div>
</body>
<%
	} else {
%>No, Data found.<%
	}
%>
</html>