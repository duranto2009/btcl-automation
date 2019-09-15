<%@page import="common.CommonDAO"%>
<%@page import="util.TimeConverter"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.RequestFailureException"%>
<%@page import="domain.DomainService"%>
<%@page import="domain.DomainDTO"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%
DomainService domainService = new DomainService();
String context = "../../.." + request.getContextPath() + "/";
Logger logger = Logger.getLogger(this.getClass());
try{	
	int id = Integer.parseInt(request.getParameter("entityID"));
	String actionName = "../../domain/domainSearch/domainEdit.jsp?entityID=" + id;
	String back = "../../ViewDomain.do?entityID=" + id;
	
	DomainDTO domainDTO = domainService.getDomainByID(id);
	CommonDAO commonDAO = new CommonDAO();

%>
<style type="text/css">
	.custom th{
		width: 50%;
	}
</style>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption"><i class="fa fa-user"></i>Info</div>
		
		<div class="actions">
			<a href="<%=back%>" class="btn btn-back-btcl" style="padding-left: 15px; padding-right: 15px; "> Back </a>
		 	<a href="<%=actionName%>&edit" class="btn btn-edit-btcl" style="padding-left: 15px; padding-right: 15px; "> Edit </a>
		 </div>
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
							<td><a href="../../ViewDomain.do?entityID=<%=id %>&entityTypeID=<%=EntityTypeConstant.DOMAIN %>"><%=domainDTO.getDomainAddress() %></a></td>
						</tr>
						<tr>
							<th scope="row">Client</th>
							<td><%=AllClientRepository.getInstance().getClientByClientID(domainDTO.getDomainClientID()).getLoginName() %></td>
						</tr>
						
						<tr>
							<th scope="row">Domain Status</th>
							<td><%=commonDAO.getActivationStatusName(domainDTO.getCurrentStatus()) %></td>
							
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
				<jsp:param name="entityID" value="<%=id %>" />
			</jsp:include>
					
		</div>
	</div>
</div>
<%}catch(Exception e){
	logger.debug("Error: ",e);
} %>
