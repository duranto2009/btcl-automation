<%@page import="vpn.clientContactDetails.ClientContactDetailsDTO"%>
<%@page import="vpn.client.ClientService"%>
<%@page import="common.ClientConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="domain.DomainPackageDetailsRepository"%>
<%@page import="domain.DomainNameDTO"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="common.CommonDAO"%>
<%@page import="util.SOP"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="domain.DomainDTO"%>
<%@page contentType="text/html;charset=utf-8" %>
<%@page import="file.FileTypeConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="domain.DomainPackage"%>
<%@page import="java.util.List"%>
<%@page import="domain.DomainPackageDetails"%>
<%@page import="domain.DomainService"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>

<link href="${context}assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css" />
<link href="${context}assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css" />
<link href="domain-query-buy.css" rel="stylesheet" type="text/css"/>

<%
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	Logger logger = Logger.getLogger(this.getClass());
	String message = (String) request.getAttribute("message");
	Long domainClientID = (Long) request.getAttribute("domainClientID");
	DomainDTO domainDTO = (DomainDTO) request.getAttribute("domainDTO");
	Boolean available = (Boolean) request.getAttribute("available");
	Boolean isFileRequired = (Boolean) request.getAttribute("isFileRequired");
	
%>

<%
/*
locally used variable
*/
List<DomainPackageDetails> domainPackageList = null;
String domainExt = DomainNameDTO.BD_EXT+"";
String domainName = null;

%>

					
<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption"><i class="fa fa-search-plus" aria-hidden="true"> </i>Domain Checker</div>
	</div>
	<!-- Main Body -->
	<div class="portlet-body" style="min-height: 450px;">
		<form method="post" action="../../DomainAction.do?mode=checkDomain" id="submit_for_check">
			<input type="hidden" name="csrfPreventionSalt" value="${csrfPreventionSalt}"/>
			<%@ include file="page-ext/domain-checker.jsp"%>
		
			<%if((domainDTO != null) && (available !=null)){ %>
			<div class="domainBuy" style="padding-top: 5px;">
				<div <%if(available){ %>class="note note-success"<%}else{ %>class="note note-danger"<%} %> style="text-align: center;">
					<h4 class="block">
						<%=message %>
					</h4>
				</div>
				
				<%if(!available && domainDTO.getID() > 0){ %>
			 		<%
					 CommonDAO commonDAO = new CommonDAO();
			 		 ClientService clientService= new ClientService();
					 List<ClientContactDetailsDTO> contactList=clientService.getVpnContactDetailsListByClientID( AllClientRepository.getInstance().getVpnClientByClientID(  domainDTO.getDomainClientID(),ModuleConstants.Module_ID_DOMAIN).getId());
				
				 	%>
					<h3>Information</h3>
					<div class="table-responsive">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th class="text-center" colspan="4"><h3><b><%=domainDTO.getDomainAddress() %></b>'s Information</h3></th>
								</tr>
							</thead>
							<tbody>
									<tr>
										<th scope="row">Domain</th>
										<td><%=domainDTO.getDomainAddress() %></td>
									</tr>
									<%if(loginDTO.getIsAdmin()){ %>
									<tr>
										<th scope="row">Username</th>
										<td><%=AllClientRepository.getInstance().getClientByClientID(domainDTO.getDomainClientID()).getLoginName() %></td>
									</tr>
									<%} %>
									<tr>
										<th scope="row">Domain Status</th>
										<td><%=commonDAO.getActivationStatusName(domainDTO.getCurrentStatus()) %> </td>
									</tr>
									<tr>
										<th scope="row">Probable Release Date</th>
										<td><%if(domainDTO.getExpiryDate()>0){%><%=TimeConverter.getTimeStringFromLong(domainDTO.getExpiryDate()) %><%}else{ %>N/A<%} %></td>
									</tr>
								</tbody>
						</table>
						
					</div>
				<%} %>
			</div>	
			<%} %>
			
			<%if(domainDTO != null && available && domainDTO.getPackageID() > 0){
				domainPackageList = DomainPackageDetailsRepository.getInstance().getPackageDetailsList(domainDTO.getPackageID());
			%>
				<!-- Domain Buy Procedure -->
				<%@ include file="page-ext/domain-buy-fields.jsp"%>		
				<!-- /Domain Buy Procedure -->
				
				<!-- Modal -->
				<%@ include file="page-ext/client-agreement-modal-for-domain-buy.jsp"%>
				<!-- /Modal -->
			<%} %>
		</form>
	</div>
	<!-- /Main Body -->
</div>
<script src="${context}assets/global/plugins/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript"></script>
<script src="${context}assets/global/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript"></script>
<script src="${context}domain/client/domainClient.js" type="text/javascript"></script>

<script src="domain-buy-validation.js" type="text/javascript"></script>
<%@ include file="page-ext/domain-query-for-buy-js.jsp"%>

