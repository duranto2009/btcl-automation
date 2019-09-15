<%@page import="vpn.link.VpnLinkService"%>
<%@page import="vpn.VpnConnectionDTO"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="permission.ActionStateDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<jsp:useBean id="date" class="java.util.Date" />
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<script src="${context}/scripts/bootstrap-datepicker.js"></script>
<%
	Long id = (Long) request.getAttribute("id");
	VpnConnectionDTO vpnConnectionDTO = (VpnConnectionDTO) request.getAttribute("vpnConnection");

	//ArrayList<ActionStateDTO> actionStateDTOs = (ArrayList<ActionStateDTO>) request.getAttribute("actions");

	String actionName = "../../VpnConnectionAction.do?id=" + id;
	ArrayList<StateActionDTO> stateActionDTOs = (ArrayList<StateActionDTO>) request
			.getAttribute("stateActions");
	VpnLinkService vpnLinkService = new VpnLinkService();
	String detailsActionName = actionName + "&getMode=showDetails";
	String editActionName = actionName + "&getMode=showEditPage&isEditable";
	request.setAttribute("entityTypeID", EntityTypeConstant.VPN_CONNECTION);
	request.setAttribute("entityID", id);
	request.setAttribute("view-tab", "true");
	String tabStr = (String) request.getAttribute("view-tab");
%>
<style type="text/css">
.text-center {
	margin-bottom: 5px;;
}
</style>
<div class="portlet box green">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i>VPN Connection View
		</div>
		<div class="tools">
			<a class="collapse" href="javascript:;" data-original-title=""
				title=""> </a>
		</div>
	</div>
	<div class="portlet-body">
		<div class="tabbable-custom ">
			<ul class="nav nav-tabs ">
				<li class="active"><a data-toggle="tab" href="#tab_5_1"
					aria-expanded="true"> Summary </a></li>
				<li class=""><a data-toggle="tab" href="#tab_5_2"
					aria-expanded="false"> Comments </a></li>
				<li class=""><a data-toggle="tab" href="#tab_5_3"
					aria-expanded="false"> History </a></li>
			</ul>
			<div class="tab-content">
				<div id="tab_5_1" class="tab-pane active">
					<div class="portlet light">
						<!-- /.box-header -->
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-reorder"></i>Information
							</div>
						</div>
						<div class="portlet-body" style="min-height: 450px;">
							<div class="well well-lg">
								<h4 class="profile-username text-center bold">
									<span style="font-weight: bold"> Connection Name </span>: <a
										href="<%=detailsActionName%>"><c:out
											value="${vpnConnection.cnName}"></c:out></a>
								</h4>
								<p class="text-center">
									<span style="font-weight: bold;"> Connection Owner </span> : <a
										href="<%=detailsActionName%>"><c:out value="${clientName}"></c:out></a>
								</p>
							</div>

							<!-- /Summary -->

							<!--  Date -->
							<div class="col-md-4">
								<table class="table table-hover">
									<thead>
										<tr style="background-color: #e0ebf9">
											<th colspan="2" class="caption"><i class="icon-calendar"></i>
												<span class="caption-subject font-dark bold uppercase">
													Dates</span></th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>Request Date</td>
											<td><c:choose>
													<c:when test="${vpnConnection.lastModificationTime  > 0 }">
														<jsp:setProperty name="date" property="time"
															value="${vpnConnection.lastModificationTime}" />
														<fmt:formatDate value="${date}"
															pattern="dd/MM/yyyy hh:mm a" />
													</c:when>
													<c:otherwise>
												Not activated
											</c:otherwise>
												</c:choose></td>
										</tr>
										<tr>
											<td>Approved Date</td>
											<td><c:choose>
													<c:when test="${vpnConnection.activationDate  > 0}">
														<jsp:setProperty name="date" property="time"
															value="${vpnConnection.activationDate}" />
														<fmt:formatDate value="${date}"
															pattern="dd/MM/yyyy hh:mm a" />
													</c:when>
													<c:otherwise>
												Not activated
											</c:otherwise>
												</c:choose></td>
										</tr>
									</tbody>
								</table>
							</div>
							<!-- /Date -->

							<!-- Status -->
							<div class="col-md-8">
								<table class="table table-hover">
									<thead>
										<tr style="background-color: #e0ebf9">
											<th colspan="2" class="caption"><i class="icon-globe"></i>
												<span class="caption-subject font-dark bold uppercase">
													Status</span></th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>Total links</td>
											<td><%=vpnLinkService.getTotalLinkFromConnectionID(id)  %></td>
										</tr>
										<tr>
											<td>Active links</td>
											<td><%=vpnLinkService.getActiveLinkFromConnectionID(id)  %></td>
										</tr>
										<tr>
											<td>Inactive links</td>
											<td><%=vpnLinkService.getInactiveLinkFromConnectionID(id)  %></td>
										</tr>
									</tbody>
								</table>
							</div>
							<!-- /Status -->

							<!-- Financial Info -->
						</div>
					</div>
					
					<!-- Action List -->
					<%@include file="../../../common/actionList.jsp"%>
					<!-- /Action List -->

					

				</div>
				<div id="tab_5_2" class="tab-pane">
					<jsp:include page="../../../common/comment_column.jsp" flush="true">
						<jsp:param name="moduleID"
							value="<%=ModuleConstants.Module_ID_VPN%>" />
						<jsp:param name="entityTypeID"
							value="<%=EntityTypeConstant.VPN_CONNECTION%>" />
						<jsp:param name="entityID" value="${vpnConnection.ID}" />
						<jsp:param name="tabStr" value="${tabStr}" />
					</jsp:include>
				</div>
				<div id="tab_5_3" class="tab-pane ">
					<%@include file="../../../common/history_column.jsp"%>
				</div>
			</div>
		</div>
	</div>
</div>

<!--  -->
<!-- /.row -->
<script>
	$(document).ready(function() {

		$('#datepicker-from').datepicker({
			dateFormat : 'dd/mm/yy',
			autoclose : true
		});
		$('#datepicker-to').datepicker({
			dateFormat : 'dd/mm/yy',
			autoclose : true
		});
	});
</script>
<script src="${context}scripts/common/commentAndHistory.js"
	type="text/javascript"></script>