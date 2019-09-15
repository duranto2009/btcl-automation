<%@page import="java.util.Locale"%>
<%@page import="common.CommonDAO"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="vpn.clientContactDetails.ClientContactDetailsDTO"%>
<%@page import="vpn.client.ClientForm"%>
<%@page import="request.RequestUtilService"%>
<%@page import="request.RequestUtilDAO"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="common.ClientRepository"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="java.util.HashMap"%>
<jsp:useBean id="date" class="java.util.Date" />
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%
	Logger logger = Logger.getLogger("clientView_jsp");
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	String context = "../../.." + request.getContextPath() + "/";
	Long id = (Long) request.getAttribute("entityID");
	String moduleIDStr = request.getParameter("moduleID");
	int moduleID = -1;
	if(moduleIDStr != null)
	{
		moduleID = Integer.parseInt(moduleIDStr);
	}
	
	String entityTypeIDStr = request.getParameter("entityTypeID");
	int entityTypeID = -1;
	if(entityTypeIDStr != null)
	{
		entityTypeID = Integer.parseInt(entityTypeIDStr);
	}
	else
	{
		entityTypeID = EntityTypeConstant.moduleIDClientTypeIDMap.get(moduleID);
	}
	String actionName = "../../GetClientForEdit.do?entityID=" + id+"&moduleID="+moduleID ;
	String editActionName = actionName + "&edit";
	
	
	/* ArrayList<ActionStateDTO> actionStateDTOs = (ArrayList<ActionStateDTO>) request.getAttribute("actions"); */
	ArrayList<StateActionDTO> stateActionDTOs = (ArrayList<StateActionDTO>) request.getAttribute("stateActions");
	String detailsActionName = actionName + "&getMode=showDetails";
	
	
	request.setAttribute("entityTypeID", entityTypeID);
	request.setAttribute("entityID", id);
	request.setAttribute("clientID", id);
	request.setAttribute("moduleID", moduleID);
	
	ArrayList<CommonRequestDTO> pendingRequestList = (ArrayList<CommonRequestDTO>) request.getAttribute("pendingRequestList");
	
	String tabActivated="1";
	if(request.getParameter("currentTab")!=null){
		tabActivated=request.getParameter("currentTab");
	}
	ClientForm  clientForm = (ClientForm)request.getAttribute("form");
	ClientContactDetailsDTO  registrantContactDetails = clientForm.getRegistrantContactDetails();
	CommonDAO commonDAO = new CommonDAO();
	logger.debug("clientForm.getClientDetailsDTO().getCurrentStatus() " + clientForm.getClientDetailsDTO().getCurrentStatus());
	String currentStatusStr = "";
	if(tabActivated.equals("1"))
	{
		currentStatusStr = commonDAO.getActivationStatusName(clientForm.getClientDetailsDTO().getCurrentStatus());
		if(StateRepository.getInstance().getStateDTOByStateID(clientForm.getClientDetailsDTO().getCurrentStatus()).getActivationStatus()!=EntityTypeConstant.STATUS_ACTIVE && loginDTO.getAccountID()>0){
			currentStatusStr+="&nbsp;<i>You will be notified whenever admin takes an action</i>";
		}

	}
	
	%>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-user"></i>Client View
		</div>
		<ul class="nav nav-tabs ">
				 <li class="<%if(tabActivated.equals("1")){%> <%="active" %><%}%>">
				 	<a  href="../../GetClientForView.do?moduleID=<%=moduleID %>&entityID=<%=id %>&currentTab=1&entityTypeID=<%=entityTypeID %>" aria-expanded="true"> Summary </a>
			 	</li>
				 <li class="<%if(tabActivated.equals("2")){%> <%="active" %><%}%>">
				 	<a  href="../../GetClientForView.do?moduleID=<%=moduleID %>&entityID=<%=id %>&currentTab=2&entityTypeID=<%=entityTypeID %>" aria-expanded="false"> Comments </a>
				 </li>
				 <li class="<%if(tabActivated.equals("3")){%> <%="active" %><%}%>">
				 	<a  href="../../GetClientForView.do?moduleID=<%=moduleID %>&entityID=<%=id %>&currentTab=3&entityTypeID=<%=entityTypeID %>" aria-expanded="false"> History </a>
				 </li>
			</ul>
	</div>
	<div class="portlet-body">
		<div class="tab-content">
			<%if(tabActivated.equals("1")){ %>
				<div id="tab_5_1" class="tab-pane active">
					<h3>Information</h3>
					<div class1="well well-lg">
						<div class="table-responsive">
						<table class="table table-bordered table-striped">
							<tbody>
								<tr>
									<th scope="row">Username</th>
									<td><a target="_blank" href="<%=detailsActionName%>">${form.clientDetailsDTO.loginName}</a></td>
								</tr>
								<%if(clientForm.getClientDetailsDTO().getClientCategoryType()==ClientForm.CLIENT_TYPE_INDIVIDUAL){%>
								<tr>
									<th scope="row">First Name</th>
									<td>${form.registrantContactDetails.registrantsName}</td>
								</tr>
								<tr>	
									<th scope="row">Last Name</th>
									<td>${form.registrantContactDetails.registrantsLastName}</td>
								</tr>
								
								<%}else{ %>
								<tr>
									<th scope="row">Company Name</th>
									<td>${form.registrantContactDetails.registrantsName}</td>
								</tr>
								
								<%} %>
								
								<tr>
									<th scope="row">Email</th>
 									<td>
 										${form.registrantContactDetails.email} 
 										<%if(clientForm.getClientDetailsDTO().getVpnContactDetails().get(0).getIsEmailVerified()==0){ %>
 											<button class="btn btn-xs btn-warning-btcl" title="Please verify your email" id="sendVerificationMail"> verify </button>
 										<%}else{ %>
 											<label class="badge badge-success" title="Email is verified"> <i class="fa fa-check"></i> </label>
 										<%} %>
 									</td>
								</tr>
								
								<tr>
									<th scope="row">Country</th>
									<td><%=new Locale("",clientForm.getClientDetailsDTO().getVpnContactDetails().get(0).getCountry()).getDisplayCountry()%></td>
								</tr>
								<tr>
									<th scope="row">Mobile</th>
									<td>${form.registrantContactDetails.phoneNumber}</td>
								</tr>
								<tr>
									<th scope="row"><%=FileTypeConstants.TYPE_ID_NAME.get(clientForm.getClientDetailsDTO().getIdentityType()) %> No</th>
									<td>${form.clientDetailsDTO.identityNo}</td>
								</tr>
								<tr>
									<th scope="row">Client Status</th>
									<td><%=currentStatusStr %></td>
								</tr>
							</tbody>
						</table>
					</div>
				
					
					</div>

					<!-- Action List -->
					<%@include file="../../common/actionListClient.jsp"%>
					<!-- /Action List -->

				</div>
				<%}%>
				<!-- /.col -->
				<%if(tabActivated.equals("2")){ 
					String url = "GetClientForView";
					String navigator = SessionConstants.NAV_COMMENT;
				%>
				<div id="tab_5_2" class="tab-pane <%if(tabActivated.equals("2")){%> <%="active" %><%}%> ">

					<jsp:include page="../../common/comment_column.jsp" flush="true">
						<jsp:param name="moduleID" value="<%=moduleID%>" />
						<jsp:param name="entityTypeID" value="<%=entityTypeID%>" />
						<jsp:param name="entityID" value="${clientID}" />						
						<jsp:param name="currentTab" value="<%=tabActivated%>" />
					</jsp:include>

				</div>
 				<jsp:include page="../../common/ajaxfileUploadTemplate.jsp" />
				<jsp:include page="../../common/fileUploadHelper.jsp" />	   
				        
               	<jsp:include page="../../includes/navComment.jsp" flush="true">
					<jsp:param name="moduleID" value="<%=moduleID%>" />
					<jsp:param name="entityTypeID" value="<%=entityTypeID%>" />
					<jsp:param name="entityID" value="<%=id%>" />	
					<jsp:param name="url" value="<%=url%>" />
					<jsp:param name="navigator" value="<%=navigator%>" />	
					<jsp:param name="currentTab" value="<%=tabActivated%>" />	
				</jsp:include>				
				<%}%>
				<%if(tabActivated.equals("3")){ %>
	               <div id="tab_5_3" class="tab-pane <%if(tabActivated.equals("3")){%> <%="active" %><%}%> ">
		                <%
							String url = "GetClientForView";
							String navigator = SessionConstants.NAV_COMMON_REQUEST;
							String entityID=request.getParameter("entityID");
							String currentTab=request.getParameter("currentTab");
						%>
	                 	<jsp:include page="../../includes/navCommonRequestTab.jsp" flush="true">
							<jsp:param name="moduleID" value="<%=moduleID%>" />
							<jsp:param name="entityTypeID" value="<%=entityTypeID%>" />
							<jsp:param name="entityID" value="<%= entityID%>" />	
							<jsp:param name="url" value="<%=url%>" />
							<jsp:param name="navigator" value="<%=navigator%>" />	
							<jsp:param name="currentTab" value="<%=currentTab%>" />	
						</jsp:include>
						
	               </div>
	               

               <%} %>
				<!-- /.col -->
		</div>
	</div>
</div>
<!-- /.row -->