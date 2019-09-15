<%@page import="common.EntityTypeConstant"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.CommonSelector"%>
<%@page import="common.ClientConstants"%>
<%@page import="vpn.client.ClientDetailsDTO"%>
<%@page import="vpn.client.ClientService"%>
<%@page import="common.bill.BillDTO"%>
<%@page import="common.bill.BillService"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="vpn.clientContactDetails.ClientContactDetailsDTO"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="connection.DatabaseConnection"%>
<%@page import="util.SqlGenerator"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="common.note.CommonNoteDAO"%>
<%@page import="common.note.CommonNoteService"%>
<%@page import="common.note.CommonNote"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<html lang="en">
<%
	String IDStr = request.getParameter("id");
	long id = -1;
	if( IDStr != null ){
		id = Long.parseLong( IDStr );
	}

	String reqIDStr = request.getParameter("reqID");
	long reqID = -1;
	if( reqIDStr != null ){
		reqID = Long.parseLong( reqIDStr );
	}

	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	CommonNoteService commonNoteService = new CommonNoteService();
	
	CommonNote commonNote = commonNoteService.getById( id );
	
	CommonRequestDTO commonRequestDTO = null;
	ClientDTO clientDTO = null;
	
	reqID = commonNote.getReqID();
		
	DatabaseConnection databaseConnection = new DatabaseConnection();
	
	databaseConnection.dbOpen();
	commonRequestDTO = (CommonRequestDTO)SqlGenerator.getObjectByID( CommonRequestDTO.class, reqID, databaseConnection );
	databaseConnection.dbClose();
	
	CommonSelector selector = new CommonSelector();
	selector.moduleID = (int)commonNote.getEntityTypeId() / EntityTypeConstant.MULTIPLIER2;

	clientDTO = AllClientRepository.getInstance().getClientByClientID( commonRequestDTO.getClientID() );
	ClientDetailsDTO vpnClient = new ClientService().getClient( clientDTO.getClientID(), loginDTO, selector);
	ClientContactDetailsDTO contactDetails = vpnClient.getVpnContactDetails().get(ClientConstants.DETAILS_TYPE_REGISTRANT);

	if(commonNote != null)
	{				
		boolean hasPermission = true;

%>

<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<title>BTCL - External Fr Response  #<%=id %></title>
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
								<img src="<%=request.getContextPath() %>/images/common/logo.png" title="BTCL - Billing Dept.">
							</p>
						</td>
						<td width="50%" nowrap="" align="right">
							Date: <b> <%=TimeConverter.getTimeStringFromLong (commonNote.getLastModificationTime()) %> </b>
						</td>
					</tr>
				</tbody>
			</table>
			<table class="items">
				<tbody>
					<tr>
						<td width="100%">

							<div class="addressbox">

								<strong>Client Info:</strong>
								<br>
								Customer ID : <%=clientDTO.getClientID() %><br>
								Name : <%=clientDTO.getName() %>
								<br>
								Customer User Name: <%=clientDTO.getLoginName() %>
								<br/>
								Phone No: <%=contactDetails.getPhoneNumber() %>
								<br/>
								Email: <%=contactDetails.getEmail() %>
							</div>

						</td>
					</tr>
				</tbody>
			</table>
			<div class="row">
				<h2 class="text-center"> External FR Response Note ID: <%=commonNote.getId() %></h2>
			</div>
				<%=commonNote.getNoteBody() %>
			<br>
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

<% } else {
%>No, Data found.<%
	}
%>

</html>
