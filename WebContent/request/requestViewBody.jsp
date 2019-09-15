<%@page import="common.repository.AllClientRepository"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="common.EntityTypeEntityDTO"%>
<%@page import="request.RequestUtilService"%>
<%@page import="login.LoginDTO"%>
<%@page import="permission.StateActionDTO"%>
<%@page import="common.ClientRepository"%>
<%@page import="request.CommonRequestDTO"%>
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
	// 	Long id = (Long) request.getAttribute("id");
	//ArrayList<ActionStateDTO> actionStateDTOs = (ArrayList<ActionStateDTO>) request.getAttribute("actions"); 
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	Logger logger = Logger.getLogger("req_view_jsp");
	String context = "../../.." + request.getContextPath() + "/";
	Long id = (Long) request.getAttribute("id");
	ArrayList<StateActionDTO> stateActionDTOs = (ArrayList<StateActionDTO>) request
			.getAttribute("stateActions");
	// 	String actionName = "../../VpnLinkAction.do?id=" + id;
	// 	String detailsActionName = actionName + "&getMode=showDetails";
	// 	String editActionName = actionName
	// 	+ "&getMode=showEditPage&isEditable";

	CommonRequestDTO comDTO = (CommonRequestDTO) request.getAttribute("commonRequestDTO");
	String actionName = context + EntityTypeConstant.entityStrutsActionMap.get(comDTO.getEntityTypeID())
			+ ".do?id=" + id;
	String detailsActionName = actionName + "&getMode=showDetails";
	String actionName2 = context + EntityTypeConstant.entityStrutsActionMapForEdit.get(comDTO.getEntityTypeID())
			+ ".do?id=" + id;
	String editActionName = actionName2 + "&getMode=showEditPage&isEditable";
	RequestUtilService utilservice = new RequestUtilService();
	logger.debug("common " + utilservice);

	request.setAttribute("entityID", comDTO.getEntityID());
	request.setAttribute("entityTypeID", comDTO.getEntityTypeID());
	request.setAttribute("clientID", comDTO.getClientID());
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
			<i class="fa fa-gift"></i>Request View
		</div>
		<div class="tools">
			<a class="collapse" href="javascript:;" data-original-title="" title=""> </a>
		</div>
	</div>
	<div class="portlet-body">
		<div class="tabbable-custom ">
			<ul class="nav nav-tabs ">
				<li class="active"><a data-toggle="tab" href="#tab_5_1" aria-expanded="true"> Summary </a></li>
				<li class=""><a data-toggle="tab" href="#tab_5_2" aria-expanded="false"> Comments </a></li>
				<li class=""><a data-toggle="tab" href="#tab_5_3" aria-expanded="false"> History </a></li>
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
								<%
									String rootReqTypeStr = "";
									int rootReqType = RequestActionStateRepository.getInstance()
											.getActionStateDTOActionTypeID(comDTO.getRequestTypeID()).getRootActionTypeID();
									rootReqTypeStr += RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(rootReqType)
											.getDescription();
								%>
								<h4 class="profile-username text-center bold">
									<span style="font-weight: bold"> Request :</span>:
									<%=(RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(comDTO.getRequestTypeID())
							.getDescription() + " (" + rootReqTypeStr + ")")%>
								</h4>
								<p class="text-center">
									<span style="font-weight: bold;"> Entity : </span> : <a
										href='<%=context%><%=EntityTypeConstant.entityStrutsActionMap.get(comDTO.getEntityTypeID())%>.do?entityID=<%=comDTO.getEntityID()%>'><%=EntityTypeConstant.entityNameMap.get(comDTO.getEntityTypeID())%></a>
								</p>
								<p class="text-center">
									<span style="font-weight: bold;"> Client ID : </span> : <a href="<%=context%>GetClientForView.do?id=<%=comDTO.getClientID()%>"><%=AllClientRepository.getInstance().getClientByClientID(comDTO.getClientID()).getLoginName()%></a>
								</p>
							</div>

							<!-- /Summary -->

							<!--  Date -->
							<div class="col-md-6">
								<table class="table table-hover">
									<thead>
										<tr style="background-color: #e0ebf9">
											<th colspan="2" class="caption"><i class="icon-calendar"></i> <span class="caption-subject font-dark bold uppercase"> Dates</span></th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>Request Date</td>
											<td><c:choose>
													<c:when test="${commonRequestDTO.requestTime  > 0 }">
														<jsp:setProperty name="date" property="time" value="${request.requestTime }" />
														<fmt:formatDate value="${date}" pattern="dd/MM/yyyy hh:mm a" />
													</c:when>
													<c:otherwise>
												No Date Found
											</c:otherwise>
												</c:choose></td>
										</tr>
										<tr>
											<td>Last Modification Date</td>
											<td><c:choose>
													<c:when test="${commonRequestDTO.lastModificationTime > 0}">
														<jsp:setProperty name="date" property="time" value="${commonRequestDTO.lastModificationTime}" />
														<fmt:formatDate value="${date}" pattern="dd/MM/yyyy hh:mm a" />
													</c:when>
													<c:otherwise>
												No Date Found
											</c:otherwise>
												</c:choose></td>
										</tr>
									</tbody>
								</table>
							</div>
							<!-- /Date -->

							<!-- Status -->
							<div class="col-md-6">
								<table class="table table-hover">
									<thead>
										<tr style="background-color: #e0ebf9">
											<th colspan="2" class="caption"><i class="icon-globe"></i> <span class="caption-subject font-dark bold uppercase"> Status</span></th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>Priority</td>
											<td><%=comDTO.getPriority()%></td>
										</tr>

									</tbody>
								</table>
							</div>
							<!-- /Status -->
							<!-- Action List -->
							<%-- <%@include file="../common/actionList.jsp"%> --%>
							<!-- /Action List -->
						</div>
					</div>
					<!-- </div> -->
					<!-- /Financial Info -->
					<!-- Action List -->
					<%-- <%@include file="../../common/actionList.jsp"%> --%>
					<!-- /Action List -->
					<!-- 					</div> -->

				</div>
				<!-- /.col -->
				<div id="tab_5_2" class="tab-pane">
					<jsp:include page="../common/comment_column.jsp" flush="true">
						<jsp:param name="moduleID" value="<%=ModuleConstants.Module_ID_VPN%>" />
						<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.REQUEST%>" />
						<jsp:param name="entityID" value="<%=id%>" />
					</jsp:include>
				</div>
				<div id="tab_5_3" class="tab-pane ">
					<%@include file="../../common/history_column.jsp"%>
				</div>
				<!-- /.col -->
			</div>

		</div>
	</div>
</div>
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
<script src="${context}scripts/common/commentAndHistory.js" type="text/javascript"></script>
