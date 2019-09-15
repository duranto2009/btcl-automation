<%@page import="common.CategoryConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="vpn.constants.EndPointConstants"%>
<%@page import="vpn.link.LinkUtils"%>
<%@page import="vpn.link.VpnEndPointDetailsDTO"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="vpn.link.VpnEndPointDTO"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="file.FileDTO"%>
<%@page import="util.SOP"%>
<%@page import="file.FileDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="common.ModuleConstants"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String context = "" + request.getContextPath() + "/";
LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
VpnLinkService vpnLinkService = new VpnLinkService();
VpnEndPointDTO endPoint=null;
if(request.getParameter("nearEndPointID")!=null){
	long id = Long.parseLong(request.getParameter("nearEndPointID"));
	endPoint=vpnLinkService.getNearEndByNearEndID(id);
}else if (request.getParameter("farEndPointID")!=null){
	long id = Long.parseLong(request.getParameter("farEndPointID"));
	endPoint=vpnLinkService.getFarEndByFarEndID(id);
}

if(endPoint==null){
	throw new NullPointerException();
}else{
	VpnEndPointDetailsDTO dto=LinkUtils.getEndPointDTODetails(endPoint);
	request.setAttribute("endPoint", dto);
%>
<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-link"></i>Link End Point
		</div>
		<div class="actions">
			<input type="button" id="printBtn" class="btn btn-default " style="padding-left: 15px; padding-right: 15px;" value="Print" />   
		</div>
	</div>
	<div class="portlet-body" id="printContent">
		<div class="row">
			<div class="col-md-12">
				<div class="table-responsive">
					<table class="table table-bordered table-striped">
						<thead>
							<tr>
								<th colspan="3" class="text-center">
									<h3> End Point Details </h3>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<th scope="row">Name</th>
								<td class="text-muted">${endPoint.vepName}</td>
							</tr>
							<tr>
								<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %></th>
								<td class="text-muted">${endPoint.districtName }</td>
							</tr>
							<tr>
							<tr>
								<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_UPAZILA).getName() %></th>
								<td class="text-muted">${endPoint.upazilaName }</td>
							</tr>
							<tr>
								<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_UNION).getName() %></th>
								<td class="text-muted">${endPoint.unionName }</td>
							</tr>
							<tr>
								<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_POP).getName() %></th>
								<td class="text-muted">${endPoint.popName }</td>
							</tr>
							<tr>
								<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_ROUTER).getName() %></th>
								<td class="text-muted">${endPoint.routerName }</td>
							</tr>
							<tr>
								<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_VLAN).getName() %></th>
								<td class="text-muted">${endPoint.mandatoryVlanName }</td>
							</tr>
							
							<tr>
								<th scope="row">Port Type</th>
								<td class="text-muted">${endPoint.portCateogryTypeName }</td>
							</tr>
							<%if(loginDTO.getIsAdmin()){ %>
								<tr>
									<th scope="row">Port ID</th>
									<td class="text-muted">${endPoint.portID }</td>
								</tr>
							<%} %>
							<tr>
								<th scope="row">Address</th>
								<td class="text-muted">${endPoint.address}</td>
							</tr>
							<tr>
								<th scope="row">Local Loop Distance</th>
								<td class="text-muted"> ${endPoint.localLoopDistance }</td>
							</tr>
							<tr>
								<th scope="row">OFC Type</th>
								<td class="text-muted"><%=EndPointConstants.coreTypeMap.get(dto.getCoreType()) %></td>
							</tr>
							<tr>
								<th scope="row">Loop Provider</th>
								<td class="text-muted"><%=EndPointConstants.providerOfOFC.get(dto.getOfcProviderTypeID()) %></td>
							</tr>
							<tr>
								<th scope="row">Terminal Device Provider</th>
								<td class="text-muted"><%=EndPointConstants.terminalDeviceProvider.get(dto.getTerminalDeviceProvider()) %></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="${context}assets/scripts/printThis.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function(){
	    $("#printBtn").click(function(){
			$("#printContent").printThis();
	    })
	})
</script>
<%} %>

