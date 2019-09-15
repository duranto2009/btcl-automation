<%@page import="domain.request.DomainRenewDomainRequestService"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="domain.DomainDTO"%>
<%@page import="domain.constants.DomainRequestTypeConstants"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="domain.DomainService"%>
<%@page import="common.CommonService"%>
<%@page import="request.DomainRenewRequest"%>
<%

Long id=Long.parseLong(request.getParameter("id"));

DomainService domService= new DomainService();
CommonService commonService = new CommonService();
DomainRenewDomainRequestService domainRenewDomainRequestService = ServiceFactory.getService(DomainRenewDomainRequestService.class);

DomainRenewRequest domainRenewRequest = domainRenewDomainRequestService.getRequestDTOByPrimaryKey(id);
DomainDTO domainDTO=domService.getDomainByID(domainRenewRequest.getEntityID());
%>
<style type="text/css">
	.custom th{
		width: 50%;
	}
</style>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption"><i class="fa fa-user"></i>Domain Renewal Application</div>
	</div>
	<div class="portlet-body ">
		<div class="table-responsive">
			<table class="table table-bordered table-striped">
				<thead>
					<tr>
						<th class="text-center" colspan="4"><h3><b><%=domainDTO.getDomainAddress() %></b>'s Information</h3></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th scope="row" width="50%">Domain</th>
						<td><a href="../../ViewDomain.do?entityID=<%=domainDTO.getID()%>&entityTypeID=<%=EntityTypeConstant.DOMAIN %>"><%=domainDTO.getDomainAddress() %></a></td>
					</tr>
					<tr>
						<th scope="row">Client</th>
						<td><%=AllClientRepository.getInstance().getClientByClientID(domainDTO.getDomainClientID()).getLoginName() %></td>
					</tr>
					<tr>
						<th scope="row">Domain Status</th>
						<td><%=commonService.getActivationStatusName(domainDTO.getCurrentStatus()) %></td>
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
						<th class="text" colspan="4"><h4>Domain Renewal</h4></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th>Extension</th>
						<td><%=domainRenewRequest.getYear() + " year(s)"%></td>
					</tr>
					<tr>
						<th>Buy Type</th>
						<td><%=domainRenewRequest.getBuyType()%></td>
					</tr>
				</tbody>
			</table>
				
			<table class="table table-bordered table-striped">
				<thead>
					<tr>
						<th class="text" colspan="4"><h4>Server Info</h4></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th scope="row" width="15%">Primary DNS</th>
						<td width="35%"><%=domainDTO.getPrimaryDNS()%></td>
						<th scope="row" width="15%">Primary IP</th>
						<td width="35%"><%=domainDTO.getPrimaryDnsIP()%></td>
					</tr>
					<tr>
						<th scope="row">Secondary DNS</th>
						<td><%=domainDTO.getSecondaryDNS()%></td>
						<th scope="row">Secondary IP</th>
						<td><%=domainDTO.getSecondaryDnsIP()%></td>
					</tr>
					<tr>
						<th scope="row">Tertiary DNS</th>
						<td><%=domainDTO.getTertiaryDNS()%></td>
						<th scope="row">Tertiary IP</th>
						<td><%=domainDTO.getTertiaryDnsIP()%></td>
					</tr>
				</tbody>
			</table>
	
			<jsp:include page="../../common/fileListHelper.jsp" flush="true">
				<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.DOMAIN %>" />
				<jsp:param name="entityID" value="<%=domainDTO.getID()%>" />
			</jsp:include>
					
		</div>
	</div>
</div>