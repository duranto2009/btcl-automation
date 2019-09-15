<%@page import="user.UserDTO"%>
<%@page import="user.UserRepository"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="lli.constants.LliRequestTypeConstants"%>
<%@page import="vpn.constants.VpnRequestTypeConstants"%>
<%@page import="java.util.List"%>
<%@page import="common.bill.BillConstants.BillConfiguration"%>
<%@page import="common.note.CommonNoteConstants"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.bill.BillConstants.BillConfiguration"%>
<%@page import="vpn.bill.BillConfigurationDTO"%>
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
	
	BillService billService = new BillService();
	
	int configurationTypeId = CommonNoteConstants.FR_RESPONSE.get( commonRequestDTO.getRequestTypeID() );
	
	List<BillConfigurationDTO> billConfigurationDTOs = null;
	if( configurationTypeId > 0 )
		billConfigurationDTOs = billService.getBillConfiguration( commonRequestDTO.getEntityTypeID()/EntityTypeConstant.MULTIPLIER2, configurationTypeId );
	
	CommonSelector selector = new CommonSelector();
	selector.moduleID = commonRequestDTO.getEntityTypeID()/EntityTypeConstant.MULTIPLIER2;

	BillConfigurationDTO footer1 = billService.getBillConfigurationFromBillConfigListByHeaderFooterID( billConfigurationDTOs, BillConfiguration.FOOTER_TEXT_1 );
	BillConfigurationDTO footer2 = billService.getBillConfigurationFromBillConfigListByHeaderFooterID( billConfigurationDTOs, BillConfiguration.FOOTER_TEXT_2 );
	BillConfigurationDTO footer3 = billService.getBillConfigurationFromBillConfigListByHeaderFooterID( billConfigurationDTOs, BillConfiguration.FOOTER_TEXT_3 );
			
	BillConfigurationDTO header1 = billService.getBillConfigurationFromBillConfigListByHeaderFooterID( billConfigurationDTOs, BillConfiguration.HEADER_TEXT_1 );
	BillConfigurationDTO header2 = billService.getBillConfigurationFromBillConfigListByHeaderFooterID( billConfigurationDTOs, BillConfiguration.HEADER_TEXT_2 );
	
	clientDTO = AllClientRepository.getInstance().getClientByClientID( commonRequestDTO.getClientID() );
	ClientDetailsDTO vpnClient = new ClientService().getClient( clientDTO.getClientID(), loginDTO, selector);
	ClientContactDetailsDTO contactDetails = vpnClient.getVpnContactDetails().get(ClientConstants.DETAILS_TYPE_REGISTRANT);
	
	Long requestToAccntID = commonRequestDTO.getRequestToAccountID();
	UserDTO requestToUser = null;
	
	if( requestToAccntID != null ){
		
		 if( requestToAccntID < 0 ) requestToAccntID = -requestToAccntID;
		 
		 requestToUser = UserRepository.getInstance().getUserDTOByUserID( requestToAccntID );
	}

	if(commonNote != null)
	{				
		boolean hasPermission = true;

%>

<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<title>BTCL - Advice Note #<%=id %></title>
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
					</tr>
				</tbody>
			</table>
			
			<% String adviceNoteName = CommonNoteConstants.REQUEST_TYPE_TO_ADVICE_NOTE_NAME.getOrDefault(commonRequestDTO.getRequestTypeID(), "" ); %>
			<%if( !adviceNoteName.equals("") ){ %> <h3><%=adviceNoteName %></h3> <%} %>
			
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
				<span class="">Advice Note ID: <%=commonNote.getId() %></span><br/>
				<%
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy"); 
				 	String strDate= format.format( commonNote.getLastModificationTime() ); 
				%>
				Date: <%=strDate %> 
				<%if( header1 != null && requestToUser != null ){%>
					<br/><br/>
					<%=header1.getText().replace( "{{toUser}}", requestToUser.getUsername() ) %>
				<%} 
				else if( header1 != null ){%>
					<%=header1.getText() %>
				<%} %>
			</div>
			<%=commonNote.getNoteBody() %>
			<br>
		</div>
		
		<% if( billConfigurationDTOs != null ){ %>
		<div>
			<div style="float:left; width:50%">
				<%if( footer1 != null ){%><%=footer1.getText() %><%} %>
			</div>
			<div style="float:left; width:50%; text-align:right">
				<%if( footer1 != null ){%><%=footer2.getText() %><%} %>
			</div>
						
		</div>
		<div style="text-align:center">
			<%if( footer1 != null ){%><%=footer3.getText() %><%} %>
		</div>
		<%} %>
	
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
