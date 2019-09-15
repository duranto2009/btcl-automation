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
    Logger logger = Logger.getLogger(this.getClass());
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	
	try{
%>
<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-link"></i>Link TD Request Preview
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
									<h3>Link Close Request Preview </h3>
								</th >
							</tr>
						</thead>
						<tbody>
							
							<tr>
								<th scope="row">Link Name</th>
								<td class="text-muted">${endPoint.districtName }</td>
							</tr>
							<tr>
								<th scope="row">Date</th>
								<td class="text-muted">${endPoint.address}</td>
							</tr>
							<tr>
								<th scope="row">Comments</th>
								<td class="text-muted">${endPoint.address}</td>
							</tr>
							<tr>
								<th scope="row">Documents</th>
								<td class="text-muted">
									<jsp:include page="../../../common/fileListHelper.jsp" flush="true">
										<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.VPN_LINK%>" />
										<jsp:param name="entityID" value="22" />
										<jsp:param name="noFileMessage" value="yes" />
									</jsp:include>
								</td>
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

<script src="${context}assets/scripts/vpn/link/linkBandwidth.js" type="text/javascript"></script>
