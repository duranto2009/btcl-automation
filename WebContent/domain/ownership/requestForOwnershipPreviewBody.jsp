<%@page import="common.ClientDTO"%>
<%@page import="request.OwnerShipChangeRequest"%>
<%@page import="common.CommonService"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="request.StateDTO"%>
<%@page import="request.StateRepository"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="vpn.clientContactDetails.ClientContactDetailsDTO"%>
<%@page import="vpn.client.ClientService"%>
<%@page import="common.CommonDAO"%>
<%@page import="common.ModuleConstants"%>
<%@page import="vpn.client.ClientDetailsDTO"%>
<%@page import="domain.constants.DomainRequestTypeConstants"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="util.SOP"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="domain.DomainDTO"%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@page import="file.FileTypeConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="domain.DomainPackage"%>
<%@page import="java.util.List"%>
<%@page import="domain.DomainPackageDetails"%>
<%@page import="domain.DomainService"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<link href="<%=request.getContextPath() %>/assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath() %>/assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css" />
<%
LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

try{
CommonRequestDTO commonRequestDTO= new CommonRequestDTO();
CommonService comService=new CommonService();
DomainService domService= new DomainService();

long requestID=Long.parseLong(request.getParameter("requestID"));
commonRequestDTO.setReqID(requestID);
commonRequestDTO.setRequestTypeID(DomainRequestTypeConstants.REQUEST_DOMAIN_OWNERSHIP_CHANGE.CLIENT_APPLY);
OwnerShipChangeRequest oscrDTO=(OwnerShipChangeRequest) comService.getExtendedRequest(commonRequestDTO);
DomainDTO domainDTO=domService.getDomainByID(oscrDTO.getEntityID());


%>
<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">  <i class="fa  fa-exchange"> </i>Ownership change Request Preview</div>
	</div>
	<!-- Main Body -->
	<div class="portlet-body" style="min-height: 450px;">
		<%if(domainDTO!=null && domainDTO.getDomainClientID()>0 ){
			 CommonDAO commonDAO = new CommonDAO();
	 		 ClientService clientService= new ClientService();
			 List<ClientContactDetailsDTO> contactList=clientService.getVpnContactDetailsListByClientID( AllClientRepository.getInstance().getVpnClientByClientID(  domainDTO.getDomainClientID(),ModuleConstants.Module_ID_DOMAIN).getId());
			 ClientDTO fromClientDTO= AllClientRepository.getInstance().getClientByClientID(oscrDTO.getOldClientID());
			 ClientDTO toClientDTO= AllClientRepository.getInstance().getClientByClientID(oscrDTO.getClientID());
			 %>
		 	 <div class="table-responsive">
			 	<table class="table table-bordered table-striped">
					<thead>
						<tr>
							<th class="text-center" colspan="4"><h3>Ownership Info</h3></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<th scope="row"></th>
							<th scope="row">From </th>
							<th scope="row">To</th>
						</tr>
						<tr>
							<th scope="row">Username</th>
							<td><%=fromClientDTO.getLoginName() %></td>
							<td><%=toClientDTO.getName() %></td>
						</tr>
						<tr>
							<th scope="row">Registrant Name</th>
							<td><%=fromClientDTO.getLoginName() %></td>
							<td><%=toClientDTO.getName() %></td>
						</tr>
					</tbody>
				</table>
			</div>
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
						<tr>
							<th scope="row">Username</th>
							<td><%=AllClientRepository.getInstance().getClientByClientID(domainDTO.getDomainClientID()).getLoginName() %></td>
						</tr>
						<%if(contactList!=null && contactList.size()>0){ %>
							<tr>
								<th scope="row">Registrant's Name</th>
								<td><%=contactList.get(0).getRegistrantsName() +"&nbsp;"+contactList.get(0).getRegistrantsLastName() %></td>
							</tr>
							<tr>
								<th scope="row">Email</th>
								<td><%=contactList.get(0).getEmail() %></td>
							</tr>
						<%} %>

						<tr>
							<th scope="row">Domain Status</th>
							<td><%=commonDAO.getActivationStatusName(domainDTO.getCurrentStatus()) %> </td>
						</tr>
						<tr>
							<th scope="row">Activation Date</th>
							<td><%if(domainDTO.getActivationDate()>0){%><%=TimeConverter.getTimeStringFromLong(domainDTO.getActivationDate()) %><%}else{ %>N/A<%} %></td>
						</tr>
						<tr>
							<th scope="row">Expire Date</th>
							<td><%if(domainDTO.getExpiryDate()>0){%><%=TimeConverter.getTimeStringFromLong(domainDTO.getExpiryDate()) %><%}else{ %>N/A<%} %></td>
						</tr>
					</tbody>
				</table>
				<table class="table table-bordered table-striped">
					<thead>
						<tr>
							<th class="text-center" colspan="4"><h3>Server Info</h3></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<th scope="row">Primary DNS</th>
							<td><%=domainDTO.getPrimaryDNS() %></td>
							<th scope="row">Primary IP</th>
							<td><%=domainDTO.getPrimaryDnsIP() %></td>
						</tr>
						<tr>
							<th scope="row">Secondary DNS</th>
							<td><%=domainDTO.getSecondaryDNS() %></td>
							<th scope="row">Secondary IP</th>
							<td><%=domainDTO.getSecondaryDnsIP() %></td>
						</tr>
						<tr>
							<th scope="row">Tertiary DNS</th>
							<td><%=domainDTO.getTertiaryDNS() %></td>
							<th scope="row">Tertiary  IP</th>
							<td><%=domainDTO.getTertiaryDnsIP() %></td>
						</tr>
				</tbody>
			</table>
		</div>
		<jsp:include page="../../common/fileListHelper.jsp" flush="true">
				<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.DOMAIN %>" />
				<jsp:param name="entityID" value="<%=domainDTO.getID() %>" />
		</jsp:include>
	<%} %>
	</div>
	<!-- Main Body -->
</div>
<%}catch( Exception ex){ %>
	<div class="alert alert-danger alert-dismissable">
   		<button type="button" class="close" data-dismiss="alert" aria-hidden="true"></button>
   		<strong>Error!</strong> please check the error(s)<br>
   		 Something is wrong
   	</div>
<%} %>

	
