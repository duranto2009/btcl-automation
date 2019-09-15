<%@page import="lli.link.request.LliLinkCloseRequestService"%>
<%@page import="util.TimeConverter"%>
<%@page import="lli.link.request.LliLinkCloseRequestService"%>
<%@page import="lli.link.LliLinkCloseRequestDTO"%>
<%@page import="lli.link.LliLinkDTO"%>
<%@page import="lli.link.LliLinkService"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="java.util.List"%>
<%@page import="common.ModuleConstants"%>
<%@page import="inventory.InventoryCatagoryType"%>
<%@page import="util.SOP"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="inventory.InventoryItemDetails"%>
<%@page import="inventory.InventoryItem"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="java.util.HashMap, java.util.Map"%>
<%@page import="vpn.constants.EndPointConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="inventory.InventoryService"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="vpn.link.VpnNearEndDTO"%>
<%@page import="common.CategoryConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="login.LoginDTO"%>

<%
	//Service
	LliLinkService lliLinkService = ServiceDAOFactory.getService(LliLinkService.class);
	LliLinkCloseRequestService lliLinkCloseRequestService = ServiceDAOFactory.getService(LliLinkCloseRequestService.class);
    Logger logger = Logger.getLogger(this.getClass());
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	
	LliLinkCloseRequestDTO lliLinkCloseRequestDTO = lliLinkCloseRequestService.getRequestDTOByPrimaryKey(Long.parseLong(request.getParameter("id")));
	LliLinkDTO lliLinkDTO = lliLinkService.getLliLinkByLliLinkID(lliLinkCloseRequestDTO.getEntityID());
	try{
%>
<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-link"></i>Connection Close Request Preview
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
									<h3>Connection Close Request Preview </h3>
								</th >
							</tr>
						</thead>
						<tbody>
							
							<tr>
								<th scope="row">Connection Name</th>
								<td class="text-muted"><%=lliLinkDTO.getName() %></td>
							</tr>
							<tr>
								<th scope="row">Client Name</th>
								<td class="text-muted"><%=AllClientRepository.getInstance().getClientByClientID(lliLinkDTO.getClientID()).getLoginName() %></td>
							</tr>
							
							<tr>
								<th scope="row">Expected Closing Date</th>
								<td class="text-muted"><%=TimeConverter.getDateTimeStringFromDateTime(lliLinkCloseRequestDTO.getExpectedClosingDate()) %></td>
							</tr>
						
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>

<%} catch (Exception ex) {
	logger.debug("General Error " + ex);
}
%>
<script src="${context}assets/scripts/printThis.js"></script>
<script>

$(document).ready(function(){
    $("#printBtn").click(function(){
		$("#printContent").printThis();
    })
})

</script>
