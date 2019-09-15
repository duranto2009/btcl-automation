<%@page import="common.repository.AllClientRepository"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="login.LoginDTO"%>
<%@page import="request.RequestStatus"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.ClientRepository"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="vpn.client.ClientDetailsDTO"%>
<%@page import="java.util.*,java.text.*"%>
<%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants"%>


<html:base />

			<%
			LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
				String msg = null;
				String url = "CommonRequestSearch";
				String navigator = SessionConstants.NAV_COMMON_REQUEST;
				String context = "../../.." + request.getContextPath() + "/";
			%>
			<jsp:include page="../includes/navCommonRequest.jsp" flush="true">
				<jsp:param name="url" value="<%=url%>" />
				<jsp:param name="navigator" value="<%=navigator%>" />
			</jsp:include>


	<div class="portlet box">
		<div class="portlet-body">
			<html:form action="/DropClient" method="POST">				
					<div class="table-responsive">
						<table id="example1" class="table table-bordered table-striped">
							<thead>
								<tr>

<!-- 									<th>Request Name</th> -->
									<th>Entity</th>
									<th>Description</th>
									<th>Client Name</th>
									<th>Request Date</th>
									<th>Status</th>					
								</tr>
							</thead>
							<tbody>
								<%
									ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_COMMON_REQUEST);

										if (data != null) {
											int size = data.size();
											for (int i = 0; i < size; i++) {
												CommonRequestDTO row = (CommonRequestDTO) data.get(i);
												ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(row.getClientID());
												if (clientDTO == null)
													continue;
												String clientName = clientDTO.getLoginName();
												Date requestDate = new Date(row.getRequestTime());
												DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
												String requestDateString = df.format(requestDate).toString();
								%>
								<tr>
									<%
									
									/*RequestActionStateRepository actionRepo = RequestActionStateRepository.getInstance();
									String rootRequestName = "";
									if(row.getRootReqID() == null)
									{
										
									}*/
									%>
<%-- 									<td><a href="<%=context%>GetRequestForView.do?id=<%=row.getReqID()%>"><%//RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(row.getRequestTypeID()).getDescription()%></a></td> --%>
									<td><%=EntityTypeConstant.entityNameMap.get(row.getEntityTypeID())%></td>	
									<td><%=row.getDescription()%></td>																	
									<td><%=clientName%></td>
									<td><%=requestDateString%></td>
									<%
									int status = row.getCompletionStatus();
									if(loginDTO.getAccountID() > 0){
										if(status == RequestStatus.SEMI_PROCESSED)
											status = RequestStatus.PENDING;
									%>
									<%}%>
									<td><%=RequestStatus.reqStatusMap.get(status)%></td>
								</tr>

								<%
									}
								%>
							</tbody>
							<%
								}
							%>

						</table>
					</div>
			</html:form>
		</div>
	</div>
</div>