<%@page import="common.ClientDTO"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="crm.CrmCommonPoolDTO"%>
<%@page import="crm.CrmComplainDTO"%>
<%@page import="crm.CrmEmployeeDTO"%>
<%@page import="crm.repository.CrmAllEmployeeRepository"%>
<%@page import="login.LoginDTO"%>

<%@page import="org.apache.log4j.Logger"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="util.TimeConverter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<style type="text/css">
	button.dt-button, div.dt-button, a.dt-button {
		color: white !important;
		border-color: white !important;
	}

</style>
<%
	String msg = null;
	String url = "CrmClientComplainSearch/Complains";
	String navigator = SessionConstants.NAV_CRM_CLIENT_COMPLAIN;
	String context = "../../.." + request.getContextPath() + "/";
	Logger searchLogger = Logger.getLogger(getClass());
	LoginDTO localLoginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	List<CrmEmployeeDTO> rootCrmEmployeeDTOList = CrmAllEmployeeRepository.getInstance().getRootEmployeeList();
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<link
	href="<%=context%>/assets/global/plugins/datatables/datatables.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=context%>/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<%-- <link href="<%=context%>/domain/domainQueryForBuy/domain-query-buy.css" --%>
<!-- 	rel="stylesheet" type="text/css" /> -->
<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>

<%
	try {
%>

<div class="portlet box">
	<div class="portlet-body">
			<div class="table-responsive">
				<table id="tableId"
					class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th class="text-center">Token ID</th>
							<th class="text-center"><%= localLoginDTO.isNOC() ? "Client-Given Priority" : "Priority" %></th>
							<th class="text-center">Service Type</th>
							<th class="text-center">Status</th>	
							<%if(localLoginDTO.isNOC()){ %>
								<th class="text-center">Client Name</th>
								<th class="text-center">NOC Name</th>
								<th class="text-center">Creator Name</th>
							<%} %>
							<th class="text-center">Submission Time</th>
							<th class="text-center">Block</th>
					</thead>
					<tbody>
						<%
							ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_CRM_CLIENT_COMPLAIN);

								if (data != null) {
									int size = data.size();
									for (int i = 0; i < size; i++) {
										try {
											CrmCommonPoolDTO row = (CrmCommonPoolDTO) data.get(i);
											
						%>
						<tr>

							<td class="text-center"><a
								href="<%=context%>CrmClientComplain/Complain.do?id=<%=row.getID()%>">Tok<%=row.getID()%>
							</a></td>
							<td class="text-center"><%=CrmComplainDTO.mapComplainPriorityStringToPriorityID.get(row.getPriority()) %></td>
							<td class="text-center"><%= (row.getEntityTypeID() != null && row.getEntityTypeID() != 0)?  
	                            		EntityTypeConstant.mapOfModuleNameToMainEntityTypeIdForCrm.get(row.getEntityTypeID()) : "" %></td>					
							<td class="text-center"><%=CrmComplainDTO.mapComplainStatusStringToStatusID.get(row.getStatus())%></td>
							<%if(localLoginDTO.isNOC()){ %>
							<!--  <td class="text-center">CrmCommonPoolDTO.mapOfPortalIDToPortalName.get(row.getPortalType())</td>-->
							<td class="text-center">
								<%
									ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(row.getClientID());
								%>
							
								<a target="_blank" href="${context }GetClientForView.do?moduleID=<%=row.getEntityTypeID()/100%>
										&entityID=<%=row.getClientID()%>">
								<%
								
								ClientDTO clientDTOFromRepository = AllClientRepository.getInstance().getClientByClientID(row.getClientID());
								if(clientDTOFromRepository== null) {
								%>
								<%="" %>
								<%
								}else {
								%>
								<%=clientDTOFromRepository.getName() %>
								<%	
								}
								%>
								</a>
							
							</td>
							
							
							<td class="text-center">
								<%
									if(row.getNocEmployeeID() == null){
								%>
										N/A
								<%		
									}else {
								%>
										<a target="_blank" href="${context }CrmEmployee/getEmployeeView.do?employeeID=<%=row.getNocEmployeeID()%>">
											<%=CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(row.getNocEmployeeID()).getName()%>		
										</a>
								<%
									}
								%>
								</td>
								<td class="text-center">
									<%
										if(row.getCreatorEmployeeID() == null){
									%>
											N/A
									<%		
										}else {
									%>
											<a target="_blank" href="${context }CrmEmployee/getEmployeeView.do?employeeID=<%=row.getCreatorEmployeeID()%>">
											<%=CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(row.getCreatorEmployeeID()).getName()%>
								
											</a>
									<%		
										}
									%>
									
								</td>
								<%} %>	
							
							<td class="text-center"><%=TimeConverter.getMeridiemTime(row.getSubmissionTime())%></td>
							<%if(localLoginDTO.isNOC()){ %>
                            <td class="text-center">
                            	<button class="btn btn-danger <%=row.isBlocked() == false ? "active" : " disabled" %> btn-block" 
                            		data-complain-id = "<%=row.getID()%>"  type="button">
                            		Block
                            	</button>
                            </td>
                            

							<%}
							
								} catch (Exception ex) {
												ex.printStackTrace();
											}
										}
							%>
						</tr>
						<%
							}

								session.removeAttribute(SessionConstants.VIEW_CRM_CLIENT_COMPLAIN);
						%>
					</tbody>
				</table>
			</div>
	</div>
</div>
<%
	} catch (Exception ex) {
		searchLogger.debug("Exception ", ex);
	}
%>
<script>
var blockBtn = $('.btn-block');
blockBtn.click(function(event){
	var url = context + "CrmClientComplain/changeBlockedStatus.do";
	var param = {};
	var self = $(event.target);
	if(self.hasClass('disabled')){
		return false;
	}
	bootbox.confirm("Are you sure you want to block this complain? This complain and all subcomplains of this complain will be blocked. Once blocked it can not be undone.", function(result){
		if(result){
			param.complainID = self.data('complain-id');
			callAjax(url, param, function(data){
				if(data.responseCode == 1){
					toastr.success(data.msg);
					self.removeClass('active');
					self.addClass('disabled');
				}else {
					toastr.error(data.msg);
				}
			}, "POST");		
		}
	});
	
	return false;
});

</script>
<script src="../../assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>