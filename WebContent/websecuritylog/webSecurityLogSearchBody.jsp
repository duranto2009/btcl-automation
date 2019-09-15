<%@page import="common.ClientDTO"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="login.LoginDTO"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="util.TimeConverter"%>
<%@page import="websecuritylog.WebSecurityConstant"%>
<%@page import="websecuritylog.WebSecurityLogDTO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<%
	Logger searchLogger = Logger.getLogger(getClass());
	String url = "WebSecurityLogSearch";
	String navigator = SessionConstants.NAV_WEB_SECURITY_LOG;
	String context = "../../.." + request.getContextPath() + "/";
	
	LoginDTO localLoginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<jsp:include page="../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>

<%try{%>

<div class="portlet box">
	<div class="portlet-body">
		
			<%if(localLoginDTO.getIsAdmin()){ %>
				<div class="row">
					<div class="col-md-12 clearfix" style="margin-bottom: 5px;">
	        			<button class="btn btn-sm btn-danger pull-right disabled" type="submit" value="2"> Block IP (System)</button>
	        			<span class="pull-right">&nbsp; &nbsp;</span>
	        			<button class="btn btn-sm btn-danger pull-right disabled" type="submit" value="2"> Block IP (firewall) </button>
	        			<span class="pull-right">&nbsp; &nbsp;</span>
	        			<button class="btn btn-sm btn-warning pull-right disabled" type="submit" value="3"> Disable User </button>
	        			<span class="pull-right">&nbsp; &nbsp;</span>
	        			<button class="btn btn-sm btn-info pull-right disabled" type="submit" value="1">Send Mail</button>
	        			<span class="pull-right">&nbsp; &nbsp;</span>
	        			<button class="btn btn-sm btn-info pull-right disabled" type="submit" value="1">Send SMS</button>
	        			<input id="privilegeAction" name="action" type="hidden" />
	        		</div>
	        	</div>
			<%} %>
			<div class="table-responsive">
				<table id="tableId" class="table table-bordered table-striped table-hover">
					<thead>
						<tr>

							<th class="text-center">Username</th>
							<th class="text-center">URL/IP</th>
							<th class="text-center">Port</th>
							<th class="text-center">Attempt Type</th>
							<th>Description</th>
							<th class="text-center">Attempt Time</th>
							<th class="text-center">Action Taken</th>
						</tr>
					</thead>
					<tbody>

						<%
							ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_WEB_SECURITY_LOG);

								if (data != null) {
									int size = data.size();
									for (int i = 0; i < size; i++) {
										WebSecurityLogDTO row = (WebSecurityLogDTO) data.get(i);
										try{
											ClientDTO clientDTO= new ClientDTO();;
											if(!row.getUsername().equalsIgnoreCase("n/a")){
												clientDTO=AllClientRepository.getInstance().getClientByClientLoginName(row.getUsername());
											}else{
												clientDTO.setLoginName("N/A");
											}
										%>
										<tr>
											<%if(clientDTO.getClientID()==0){ %>
												<td class="text-center">N/A</td>
											<%}else{ %>
												<td class="text-center" ><a target="_blank" href="${context}GetClientForView.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN%>&entityID=<%=clientDTO.getClientID()%>"> <%=row.getUsername() %> </a> </td>
											<%} %>
											<td class="text-center"><%=row.getUrl() %></td>
											<td class="text-center"><%=row.getPort() %></td>
											<td class="text-center"><%=WebSecurityConstant.attemptTypeMap.get(row.getAttemptType()) %></td>
											<td><%=row.getDescription() %></td>
											<td class="text-center"><%=TimeConverter.getTimeWithTimeStringFromLong( row.getAttemptTime()) %></td>
											<td>None</td>
										</tr>
									<%}catch(Exception ex ){ %>
										<tr><td colspan="10" class="text-center "><span class="font-red-mint font-lg bold uppercase">Something wrong in this record.id (<%=row.getID() %>)</span></td></tr>
									<%} %>
								<% }
							}%>
					</tbody>
				</table>
			</div>
	</div>
</div>
<%}catch(Exception ex){
	searchLogger.debug("Exception ", ex);
}
%>
<!-- END PAGE LEVEL PLUGINS -->
<script src="<%=context%>assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script type="text/javascript">
   $(document).ready(function(){
       var table=$("#tableId");
       var currentForm = $("#tableForm");
       
       $('.group-checkable', table).change(function() {
           var set = table.find('tbody > tr > td:last-child input[type="checkbox"]');
           var checked = $(this).prop("checked");
           $(set).each(function() {
               $(this).prop("checked", checked);
           });
           $.uniform.update(set);
       });
       $('#tableForm button[type=submit]').click(function(e) {
           var currentSubmit = this;
           var selected=false;
           e.preventDefault();
           var set = table.find('tbody > tr > td:last-child input[type="checkbox"]');
           $(set).each(function() {
           	if($(this).prop('checked')){
           		selected=true;
           	}
           });
           if(!selected){
           	 bootbox.alert("Select Domain  to take this action  !", function() { });
           }else{
           	 bootbox.confirm("Are you sure to take this action ?", function(result) {
                    if (result) {
                    	$('#privilegeAction').val($(currentSubmit).val());
                        currentForm.submit();
                    }
                });
           }
       });
    })
    
    </script>