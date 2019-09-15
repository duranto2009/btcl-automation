<%@page import="lli.link.request.LliLinkBandwidthChangeRequestService"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="lli.link.LinkUtils"%>
<%@page import="lli.link.LliEndPointDetailsDTO"%>
<%@page import="lli.link.LliFarEndDTO"%>
<%@page import="lli.link.LliLinkDTO"%>
<%@page import="lli.constants.LliRequestTypeConstants"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="common.CommonService"%>
<%@page import="lli.link.LliBandWidthChangeRequestDTO"%>
<%@page import="java.util.List"%>
<%@page import="common.ModuleConstants"%>
<%@page import="inventory.InventoryCatagoryType"%>
<%@page import="util.SOP"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="inventory.InventoryItemDetails"%>
<%@page import="inventory.InventoryItem"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="java.util.HashMap, java.util.Map"%>
<%@page import="lli.constants.EndPointConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="inventory.InventoryService"%>
<%@page import="lli.link.LliLinkService"%>
<%@page import="common.CategoryConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="login.LoginDTO"%>

<%
Long lliLinkBandwidthChangeRequestID = Long.parseLong(request.getParameter("id"));	
if(lliLinkBandwidthChangeRequestID==null){
	response.sendRedirect(request.getContextPath());
}
LliLinkBandwidthChangeRequestService lliLinkBandwidthChangeRequestService = ServiceDAOFactory.getService(LliLinkBandwidthChangeRequestService.class);
LliBandWidthChangeRequestDTO lliBandWidthChangeRequestDTO = lliLinkBandwidthChangeRequestService.getRequestDTOByPrimaryKey(lliLinkBandwidthChangeRequestID);
LliLinkDTO lliLinkDTO = new LliLinkService().getLliLinkByLliLinkID(lliBandWidthChangeRequestDTO.getLinkID());
	

%>
<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-link"></i>Connection Upgrade/Degrade Request
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
								<th class="text-center" colspan="4"><h3>Connection Upgrade/Degrade </h3></th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<th scope="row">Client Name</th>
								<td class="text-muted" colspan="2"><%=AllClientRepository.getInstance().getClientByClientID(lliBandWidthChangeRequestDTO.getClientID()).getName() %></td>
							</tr>
							<tr>
								<th scope="row">Connection Name</th>
								<td class="text-muted" colspan="2"><%=lliLinkDTO.getName() %></td>
							</tr>
						</tbody>
						<tbody>
							<tr>
								<th scope="row"></th>
								<th scope="row">Current  </th>
								<th scope="row">New </th>
							</tr>
							<tr>
								<th scope="row">Bandwidth</th>
								<td scope="row"><%=lliLinkDTO.getLliBandwidth()%></td>
								<td scope="row"><%= lliBandWidthChangeRequestDTO.getNewBandwidth()%> </td>
							</tr>
							<tr>
								<th scope="row">Bandwidth Type</th>
								<td scope="row"><%=lliLinkDTO.getLliBandwidthType()==1?"Mb":"Gb"%></td>
								<td scope="row"><%= lliBandWidthChangeRequestDTO.getNewBandwidthType()==1?"Mb":"Gb"%> </td>
							</tr>							
							<%-- <tr>
								<th scope="row">Far End Port</th>
								<td scope="row"><%=lliBandWidthChangeRequestDTO.getOldFarPortID()%></td>
								<td scope="row"><%=lliBandWidthChangeRequestDTO.getNewFarPortID() != null ? lliBandWidthChangeRequestDTO.getNewFarPortID() : "n/a"%></td>
							</tr>
							<tr>
								<th scope="row">Far End Port Type</th>
								<td scope="row"><%=lliBandWidthChangeRequestDTO.getOldFarPortType()%></td>
								<td scope="row"><%=lliBandWidthChangeRequestDTO.getNewFarPortType() != null ? lliBandWidthChangeRequestDTO.getNewFarPortType() : "n/a"%></td>
							</tr> --%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>

