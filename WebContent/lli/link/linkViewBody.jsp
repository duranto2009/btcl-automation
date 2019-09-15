<%@page import="lli.constants.EndPointConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="util.TimeConverter"%>
<%@page import="language.LliLanguageConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="permission.StateActionDTO"%>
<%@page import="lli.link.LliLinkDTO"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="permission.ActionStateDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="login.LoginDTO"%>
<%@page import="request.RequestStatus"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.ClientRepository"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="vpn.client.ClientDetailsDTO,lli.link.LliLinkService"%>
<%@page import="java.util.*,java.text.*"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<jsp:useBean id="date" class="java.util.Date" />
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<script src="${context}/scripts/bootstrap-datepicker.js"></script>
<%
Logger logger = Logger.getLogger("link_view_jsp");
LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
String context = "../../.." + request.getContextPath() + "/";
Long id = (Long) request.getAttribute("entityID");
if (id == null || id == 0) {
	id = Long.parseLong(request.getParameter("entityID"));
}
LliLinkDTO lliLinkDTO = (LliLinkDTO) request.getAttribute("lliLink");
ArrayList<StateActionDTO> stateActionDTOs = (ArrayList<StateActionDTO>) request.getAttribute("stateActions");
logger.debug("stateActions " + stateActionDTOs);

String action = "LliLinkAction";
String actionName = "../../LliLinkAction.do?entityID=" + id+ "&entityTypeID="+EntityTypeConstant.LLI_LINK;
String detailsActionName = actionName + "&getMode=details";
String editActionName = actionName + "&getMode=edit";
request.setAttribute("entityTypeID", EntityTypeConstant.LLI_LINK);
request.setAttribute("entityID", id);

if(lliLinkDTO==null){
	lliLinkDTO =new LliLinkService().getLliLinkByLliLinkID(id);
}

request.setAttribute("rootEntityID", lliLinkDTO.getID());
request.setAttribute("rootEntityTypeID", EntityTypeConstant.LLI_LINK);

ClientDTO clientLLIDTO = AllClientRepository.getInstance().getClientByClientID(lliLinkDTO.getClientID());

CommonDAO commonDAO = new CommonDAO();
String currentStatusStr = commonDAO.getActivationStatusName(lliLinkDTO.getCurrentStatus());
String latestStatusStr = StateRepository.getInstance().getStateDTOByStateID(lliLinkDTO.getLatestStatus()).getName();

String tabActivated = "1";
if (request.getParameter("currentTab") != null) {
	tabActivated = request.getParameter("currentTab");
}
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-link" aria-hidden="true"></i> LLI Connection View
		</div>
		<ul class="nav nav-tabs ">
			<li class="<%if (tabActivated.equals("1")) {%> <%="active"%><%}%>">
				<a href="../../LliLinkAction.do?entityID=<%=id%>&entityTypeID=<%=EntityTypeConstant.LLI_LINK%>&currentTab=1" aria-expanded="true"> Summary </a>
			</li>
			<li class="<%if (tabActivated.equals("2")) {%> <%="active"%><%}%>">
				<a
				href="../../LliLinkAction.do?entityID=<%=id%>&entityTypeID=<%=EntityTypeConstant.LLI_LINK%>&currentTab=2"
				aria-expanded="true"> Comments </a>
			</li>
			<li class="<%if (tabActivated.equals("3")) {%> <%="active"%><%}%>">
				<a
				href="../../LliLinkAction.do?entityID=<%=id%>&entityTypeID=<%=EntityTypeConstant.LLI_LINK%>&currentTab=3"
				aria-expanded="true"> History </a>
			</li>
		</ul>
	</div>
	<div class="portlet-body">
		<div class="tab-content">
			<%if (tabActivated.equals("1")) {%>
			<div id="tab_5_1" class="tab-pane active">
				<h3 class="">Information</h3>
				<!-- /.box-header -->
				<div class="table-responsive">
					<table class="table table-bordered table-striped">
						<tbody>
							<tr>
								<th scope="row">Client Name</th>
								<td colspan="2">
									<a href="../../GetClientForView.do?entityID=<%=clientLLIDTO.getClientID()%>&entityTypeID=<%=EntityTypeConstant.LLI_CLIENT%>&moduleID=<%=ModuleConstants.Module_ID_LLI%>">
										<%=clientLLIDTO.getName()%>
									</a>
								</td>
							</tr>
							<tr>
								<th scope="row">Connection Name</th>
								<td colspan="2">
									<a href="<%=detailsActionName%>">
										${lliLink.linkName}
									</a>
								</td>
							</tr>
							<tr>
								<th scope="row">Description</th>
								<td colspan="2">${lliLink.linkDescription}</td>
							</tr> 
							<tr>
								<th scope="row">Link Status</th>
								<td><%=currentStatusStr %></td>
							</tr>
							<tr>
								<th scope="row">Bandwidth</th>
								<td colspan="2">${lliLink.lliBandwidth} <%=EntityTypeConstant.linkBandwidthTypeMap.get(lliLinkDTO.getLliBandwidthType()) %>&nbsp;</td>
							</tr>

							<%-- <tr>
								<th scope="row"><%=LliLanguageConstants.REMOTE_END %> </th>
								<td class="text-muted"> <a href='${context}lli/link/endPointPreview.jsp?farEndPointID=${farEnd.ID}' target="_blank" >${farEnd.vepName } </a></td>
							</tr> --%>

							<%if( lliLinkDTO.getConnectionType() == EndPointConstants.CONNECTION_TYPE_TEMPORARY_){ %>
								<tr>
									<th scope="row">Temporary Connection Range</th>
									<td colspan="2"><%=lliLinkDTO.getTemporaryConnectionRange()%> days</td>
								</tr>
								<tr>
									<th scope="row">Requested Starting Date</th>
									<td colspan="2"><%=TimeConverter.getTimeStringByDateFormat(lliLinkDTO.getTemporaryConnectionFromDate(), "dd/MM/yyyy")%></td>
								</tr>
							<%}%>
							
							<tr>
								<th scope="row">Activation Date</th>
								<td><b><%if(lliLinkDTO.getActivationDate()>0){%><%=TimeConverter.getTimeStringFromLong(lliLinkDTO.getActivationDate()) %><%}else{ %>N/A<%} %></b></td>
							</tr>
							
						</tbody>
					</table>
				</div>
		
				
				<!-- Action List -->
				<%@include file="../../common/actionListLLI.jsp"%>
				<!-- /Action List -->
			</div>
			<%
			} 
			if (tabActivated.equals("2")) {
			%>
			<div id="tab_5_2" class="tab-pane active">
				<%
					String url = "LliLinkAction";
					String navigator = SessionConstants.NAV_COMMENT;
					String entityID = request.getParameter("entityID");
					String currentTab = request.getParameter("currentTab");
				%>
				<jsp:include page="../../common/comment_column.jsp" flush="true">
					<jsp:param name="moduleID" value="<%=ModuleConstants.Module_ID_LLI%>" />
					<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.LLI_LINK%>" />
					<jsp:param name="entityID" value="<%=entityID%>" />
					<jsp:param name="currentTab" value="<%=currentTab%>" />
				</jsp:include>
			</div>

			<jsp:include page="../../common/ajaxfileUploadTemplate.jsp" />

			<jsp:include page="../../includes/navComment.jsp" flush="true">
				<jsp:param name="moduleID"
					value="<%=ModuleConstants.Module_ID_LLI%>" />
				<jsp:param name="entityTypeID"
					value="<%=EntityTypeConstant.LLI_LINK%>" />
				<jsp:param name="entityID" value="<%=entityID%>" />
				<jsp:param name="url" value="<%=url%>" />
				<jsp:param name="navigator" value="<%=navigator%>" />
				<jsp:param name="currentTab" value="<%=currentTab%>" />
			</jsp:include>


			<%
				}
			%>
			<%if (tabActivated.equals("3")) { %>
			<div id="tab_5_3" class="tab-pane active">
				<%
					String url = "LliLinkAction";
					String navigator = SessionConstants.NAV_COMMON_REQUEST;
					String entityID = request.getParameter("entityID");
					String currentTab = request.getParameter("currentTab");
				%>
				<jsp:include page="../../includes/navCommonRequestTab.jsp"
					flush="true">
					<jsp:param name="moduleID"	value="<%=ModuleConstants.Module_ID_LLI%>" />
					<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.LLI_LINK%>" />
					<jsp:param name="entityID" value="<%=entityID%>" />
					<jsp:param name="url" value="<%=url%>" />
					<jsp:param name="navigator" value="<%=navigator%>" />
					<jsp:param name="currentTab" value="<%=currentTab%>" />
				</jsp:include>
			</div>
			<%}%>
		</div>
	</div>
</div>