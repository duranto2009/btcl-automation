<%@page import="inventory.InventoryService"%>
<%@page import="inventory.InventoryDAO"%>
<%@page import="common.note.CommonNoteService"%>
<%@page import="common.note.CommonNote"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<html lang="en">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>BTCL -  Note </title>
<link href="<%=request.getContextPath() %>/stylesheets/invoice.css" rel="stylesheet">
<style type="text/css">
	.NB, .teletalk{
		font-size: 10px;
		font-family: times roman;
	}
</style>
</head>

<%
try{
InventoryService invtService= new InventoryService();
String districtName=invtService.getInventoryItemByItemID(Integer.parseInt(request.getParameter("districtId"))).getName();
String popName=invtService.getInventoryItemByItemID(Integer.parseInt(request.getParameter("popId"))).getName();

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
									<img src="<%=request.getContextPath() %>/images/common/logo.png" title="BTCL - Billing Dept.">
								</p>
							</td>
						</tr>
					</tbody>
				</table>
				<table class="items">
					<tbody>
						<tr>
							<td width="100%">

								<div class="addressbox">

									<strong>Adviced To</strong><br>Customer ID : 1120<br>
									Name : H M A Alam
									<br>Customer User Name: Alam
									
									<br>Address
									:
									Meradia, <br>
									Gulshan
									-
									1212,Bangladesh<br>

								</div>

							</td>
						</tr>
					</tbody>
				</table>
				<div class="row">
					<span class="title">FR Report</span>
					<br> 
					Invoice Date: 12/12/2012<br>
					Due Date:
					Don't know
				</div>
				
				<table class="items">
				<thead>
					<tr class="title textcenter"><td width="70%">Distance Type</td><td width="30%">Meters</td></tr>
				</thead>
				<tbody>
					<tr><td>District Name</td><td class="textcenter"><%=districtName %></td></tr>
					<tr><td>Pop Name</td><td class="textcenter"><%=popName %></td></tr>
					<tr><td>Distance covered by btcl</td><td class="textcenter"><%=request.getParameter("btclDistance") %></td></tr>
					<tr><td>Distance covered by O/C</td><td class="textcenter"><%=request.getParameter("ocDistance") %></td></tr>
					<tr><td>Distance covered by Customer</td><td class="textcenter"><%=request.getParameter("cusDistance") %></td></tr>
					<tr><td>Total Distance</td><td class="textcenter"><%=request.getParameter("totalDistance") %></td></tr>
					<tr><td>Description</td><td class="textcenter"><%=request.getParameter("description") %></td></tr>
					</tbody>
				</table>
				<br>
				<div class="NB">
					<br>
					<strong>N.B:</strong>
					<br>â¢ Please pay the cash amount at Social Islami Bank Limited (S.I B.L.), Eskaton branch, Moghbazar, Dhaka.
					<br>â¢ Only Cheque ( For Govt. offices ) and Pay order ( For non Govt. offices & others organizations ) should be in favor of âSAO, TELEPHONE REVENUE, NORTH, DHAKA.
					<br><br>Address to send Cheque/Pay Order in the following office:
					<br>Account Officer, TELEPHONE REVENUE, Room#407(3rd Floor), BTCL, Moghbazar Telephone Bhaban, Dhaka-1217.
				</div>
				<p style="width: 100%; text-align: right; color: blue; font-weight: 300;"> Powered By : Reve Systems</p>
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
<%}catch(Exception ex){ %>
	<%="Error" %>
<%} %>
</body>

</html>
