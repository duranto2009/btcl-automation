<%@page import="common.repository.AllClientRepository"%>
<%@page import="common.CommonDAO"%>
<%@page import="util.TimeConverter"%>
<%@page import="domain.DomainSearchService"%>
<%@page import="domain.DomainService"%>
<%@page import="domain.DomainDTO"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="permission.StateActionDTO"%>
<%@page import="vpn.VpnConnectionDTO"%>
<%@page import="vpn.link.VpnLinkDTO"%>
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
<%@page import="vpn.client.ClientDetailsDTO"%>
<%@page import="java.util.*,java.text.*"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<jsp:useBean id="date" class="java.util.Date" />
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<script src="${context}/scripts/bootstrap-datepicker.js"></script>
<%
	Logger logger = Logger.getLogger(this.getClass());
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	String context = "../../.." + request.getContextPath() + "/";
	Long id = (Long) request.getAttribute("entityID");
	if(id==null || id==0){
		id=Long.parseLong(request.getParameter("entityID"));
	}
	
	DomainService domainService = new DomainService();
	DomainDTO domainDTO = domainService.getDomainByID(id);
	ArrayList<StateActionDTO> stateActionDTOs = (ArrayList<StateActionDTO>) request.getAttribute("stateActions");
	logger.debug("stateActions " + stateActionDTOs);
	String action = "ViewDomain";
	String actionName = "../../" + action + ".do?entityID=" + id;
	String editActionName = context+"domain/domainSearch/domainEdit.jsp?entityID=" + id;
	String detailsActionName = "../../"+"domain/domainSearch/domainPreview.jsp?entityID="+id+ "&getMode=showDetails";
	//String editActionName = actionName + "&edit";
	request.setAttribute("moduleID", ModuleConstants.Module_ID_DOMAIN);
	int entityTypeID = EntityTypeConstant.DOMAIN;
	request.setAttribute("entityTypeID", entityTypeID);
	request.setAttribute("entityID", id);
	request.setAttribute("domainName", domainDTO.getDomainAddress());
	request.setAttribute("domainDTO", domainDTO);

	String tabActivated = "1";
	if (request.getParameter("currentTab") != null) {
		tabActivated = request.getParameter("currentTab");
	}
	CommonDAO commonDAO = new CommonDAO();
	String currentStatusStr = "";
	String latestStatusName = "";
	try{
		latestStatusName = StateRepository.getInstance().getStateDTOByStateID(domainDTO.getLatestStatus()).getName();
		currentStatusStr = commonDAO.getActivationStatusName(domainDTO.getCurrentStatus());
	}catch(Exception ex){
		logger.debug(ex);
	}
	String clientLink = "../../GetClientForView.do?entityID="+domainDTO.getDomainClientID()+"&entityTypeID="+EntityTypeConstant.DOMAIN_CLIENT+"&moduleID="+ModuleConstants.Module_ID_DOMAIN;
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i>Domain View
		</div>
		<ul class="nav nav-tabs ">
			<li class="<%if (tabActivated.equals("1")) {%> <%="active"%><%}%>">
				<a href="../../<%=action%>.do?entityID=<%=id%>&currentTab=1"
				aria-expanded="true"> Summary </a>
			</li>
			<li class="<%if (tabActivated.equals("2")) {%> <%="active"%><%}%>">
				<a
				href="../../<%=action%>.do?entityID=<%=id %>&currentTab=2&entityTypeID=${entityTypeID}"
				aria-expanded="true"> Comments </a>
			</li>
			<li class="<%if (tabActivated.equals("3")) {%> <%="active"%><%}%>">
				<a
				href="../../<%=action%>.do?entityID=<%=id %>&currentTab=3&entityTypeID=${entityTypeID}"
				aria-expanded="true"> History </a>
			</li>
		</ul>
	</div>
	<div class="portlet-body">
		<div class="tab-content">
			<%
				if (tabActivated.equals("1")) {
			%>
			<div id="tab_5_1" class="tab-pane active">
				<h3>Information</h3>
					<div class1="well well-lg">
						<div class="table-responsive">
						<table class="table table-bordered table-striped">
							<tbody>
									<tr>
										<th scope="row"> Domain Name</th>
										<td><a href="<%=detailsActionName%>"><%=domainDTO.getDomainAddress()%></a> <a  class="pull-right " target="_blank" href="http://<%=domainDTO.getDomainAddress()%>">visit this site</a></td>
									</tr>
									<tr>
										<th scope="row"> Client Name</th>
										<td><a href="<%=clientLink%>"><%=AllClientRepository.getInstance().getClientByClientID(domainDTO.getDomainClientID()).getName()%></a></td>
									</tr>
									<tr>
										<th scope="row">Domain Status</th>
										<td><%=currentStatusStr %> <strong>( <%=latestStatusName %> )</strong></td>
									</tr>
									<tr>
										<th scope="row">Activation Date</th>
										<td><%if(domainDTO.getActivationDate()>0){%><%=TimeConverter.getTimeStringFromLong(domainDTO.getActivationDate()) %><%}else{ %>N/A<%} %></td>
									</tr>
									<tr>
										<th scope="row">Expire Date</th>
										<td><%if(domainDTO.getExpiryDate()>0){%><%=TimeConverter.getTimeStringFromLong(domainDTO.getExpiryDate()) %><%}else{ %>N/A<%} %></td>
									</tr>
									<%if(loginDTO.getIsAdmin()){ %>
									<tr>
										<th scope="row">Privilege</th>
										<td >
											<%if(domainDTO.isPrivileged()) {%> <span class=" badge badge-warning"> Yes </span><%}else{ %> <span class=" badge badge-danger"> No </span> <%} %> 
										</td>
									</tr>
									<%} %>
<%-- 									<tr>
										<th scope="row">Registration Duration(Year)</th>
										<td><%=domainDTO.getYear() %></td>
									</tr>
									<tr>
									<th scope="row">Domain Cost(BDT)</th>
									<td><%=domainDTO.getCost()%></td>
									</tr> --%>
								</tbody>
						</table>
					</div>
				
					
					</div>
				<!-- Action List -->
				<%@include file="../../common/actionListDomain.jsp"%>
				<!-- /Action List -->
			</div>
			<%
				}
			%>
			<%
				if (tabActivated.equals("2")) {
			%>
			<div id="tab_5_2" class="tab-pane <%if (tabActivated.equals("2")) {%> <%="active"%><%}%>">
				<%
					String url = action;
							String navigator = SessionConstants.NAV_COMMENT;
							//String id =  request.getParameter("id");
							String currentTab = request.getParameter("currentTab");
				%>
				<jsp:include page="../../common/comment_column.jsp" flush="true">
					<jsp:param name="moduleID" value="${moduleID }" />
					<jsp:param name="entityTypeID" value="${entityTypeID }" />
					<jsp:param name="entityID" value="<%=id%>" />
					<jsp:param name="currentTab" value="<%=currentTab%>" />
				</jsp:include>
			</div>
			
	  		<jsp:include page="../../common/ajaxfileUploadTemplate.jsp" />
			<jsp:include page="../../common/fileUploadHelper.jsp" />
			
			<jsp:include page="../../includes/navComment.jsp" flush="true">
				<jsp:param name="moduleID" value="${moduleID }" />
				<jsp:param name="entityTypeID" value="${entityTypeID }" />
				<jsp:param name="entityID" value="<%=id%>" />
				<jsp:param name="url" value="<%=url%>" />
				<jsp:param name="navigator" value="<%=navigator%>" />
				<jsp:param name="currentTab" value="<%=currentTab%>" />
			</jsp:include>
			<%
				}
			%>
			<%
				if (tabActivated.equals("3")) {
			%>
			<div id="tab_5_3"
				class="tab-pane <%if (tabActivated.equals("3")) {%> <%="active"%><%}%> ">
				<%
					String url = action;
					String navigator = SessionConstants.NAV_COMMON_REQUEST;
					//String id=request.getParameter("id");
					String currentTab = request.getParameter("currentTab");
				%>
				<jsp:include page="../../includes/navCommonRequestTab.jsp" flush="true">
					<jsp:param name="moduleID" value="${moduleID }" />
					<jsp:param name="entityTypeID" value="${entityTypeID }" />
					<jsp:param name="entityID" value="<%=id%>" />
					<jsp:param name="url" value="<%=url%>" />
					<jsp:param name="navigator" value="<%=navigator%>" />
					<jsp:param name="currentTab" value="<%=currentTab%>" />
				</jsp:include>
			</div>
			<%
				}
			%>
		</div>
	</div>
</div>


