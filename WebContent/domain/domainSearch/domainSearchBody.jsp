<%@page import="request.StateRepository"%>
<%@page import="login.ColumnPermissionConstants"%>
<%@page import="login.PermissionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.CommonDAO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="domain.DomainDTO"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants"%>

<style type="text/css">
	button.dt-button, div.dt-button, a.dt-button{
		color: white !important;
		border-color: white !important;
	}
</style>
<%
	boolean showTerminationOption = false;
	String msg = null;
	String url = "SearchDomain";
	String navigator = SessionConstants.NAV_DOMAIN;
	String context = "../../.." + request.getContextPath() + "/";
	Logger searchLogger = Logger.getLogger(getClass());
	LoginDTO loginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	boolean hasPermission=false;
	
	if(loginDTO.getUserID()>0)
	{
	    if((loginDTO.getMenuPermission(PermissionConstants.BUY_DOMAIN) !=-1)
	    		
	      &&(loginDTO.getMenuPermission(PermissionConstants.BUY_DOMAIN) >= PermissionConstants.PERMISSION_FULL)){
	    	
	        hasPermission=true;
	    }
	}
	
	
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<link href="<%=context%>assets/styles/keyboard.css" rel="stylesheet" type="text/css" />
<link href="<%=context%>/assets/global/plugins/datatables/datatables.min.css" rel="stylesheet" type="text/css" />
<link href="<%=context%>/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.css" rel="stylesheet" type="text/css" />
<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>

<%try{%>

<div class="portlet box">
	<div class="portlet-body">
		<form action="../../DomainAction.do?mode=changePrivilege" method="POST" id="tableForm">
			<%if(hasPermission){ %>
				<div class="row">
					<div class="col-md-12 clearfix" style="margin-bottom: 5px;">
					
						<%if( loginDTO.getColumnPermission( ColumnPermissionConstants.DOMAIN.CAN_TERMINATE_DOMAIN ) ){ %>
							<button id="terminateBtn" class="btn btn-sm btn-danger pull-right" type="button"> Terminate Request </button>
		        			<span class="pull-right">&nbsp; &nbsp;</span>
		        		<%} %>
	        			
	        			<button class="btn btn-sm btn-danger pull-right" type="submit" value="2"> Unprivilege </button>
	        			<button class="btn btn-sm btn-warning pull-right" type="submit" value="1">Privilege</button>
	        			<span class="pull-right">&nbsp; &nbsp;</span>
	        			<%if( loginDTO.getColumnPermission( ColumnPermissionConstants.DOMAIN.DOMAIN_RENEW_BLOCK ) ){ %>
	        			<button class="btn btn-sm btn-danger pull-right" type="submit" value="3"> Renew block </button>
	        			<%} %>
	        			<%if( loginDTO.getColumnPermission( ColumnPermissionConstants.DOMAIN.DOMAIN_RENEW_UNBLOCK ) ){ %>
	        			<button class="btn btn-sm btn-info pull-right" type="submit" value="4"> Renew Unblock</button>
	        			<%} %>
	        			
	        			<input id="privilegeAction" name="action" type="hidden" />
	        		</div>
	        	</div>
			<%} %>
			<div class="table-responsive">
				<table id="tableId" class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>Name</th>
							<!-- <th>Year</th>
							<th>Cost</th> -->
							<th>Status</th>
							<th>Client</th>
							<th>Expire Date</th>
							<th class="text-center">DNS</th>
							
							<%if(hasPermission){ %>
								<th class="text-center" >Renew Blocked?</th>
								<th class="text-center" >Privileged?</th>
								<th class="text-center">
									<input type="checkbox" name="Check_All" value="CheckAll" class="group-checkable pull-left" />Select
									<br>
								</th>
							<%} %>
						</tr>
					</thead>
					<tbody>

						<%
							CommonDAO commonDAO = new CommonDAO();
								ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_DOMAIN);

								if (data != null) {
									int size = data.size();
									for (int i = 0; i < size; i++) {
										DomainDTO row = (DomainDTO) data.get(i);
										try {
											request.setAttribute("id", row.getID());
											request.setAttribute("address", row.getDomainAddress());
											String latestStatusName = StateRepository.getInstance().getStateDTOByStateID(row.getLatestStatus()).getName();
											request.setAttribute("status", commonDAO.getActivationStatusName(row.getCurrentStatus()) + " - " + latestStatusName);
											request.setAttribute("client", AllClientRepository.getInstance().getClientByClientID(row.getDomainClientID()).getLoginName());
											request.setAttribute("time", TimeConverter.getTimeStringFromLong(row.getExpiryDate()));
											request.setAttribute("editLink", context+"domain/domainSearch/domainEdit.jsp?entityID="+row.getID()+"&entityTypeID="+EntityTypeConstant.DOMAIN+"&edit");
										} catch (Exception ex) {
											Logger logger = Logger.getLogger(this.getClass());
											logger.error("Error in " + this.getClass());
										}
									%>
									<tr>
										<td>
											<a href="<%=context%>ViewDomain.do?entityID=${id }&entityTypeID=<%=EntityTypeConstant.DOMAIN%>">${address}</a>
										</td>
										<td>${status}</td>
										<td><a href="<%=context%>GetClientForView.do?moduleID=1&entityID=<%=row.getDomainClientID()%>">${client}</a></td>
										<td>${time}</td>
										
										<td class="text-center"> 
										<%if(!row.isDeleted()){%>
											<a href="${editLink}">Edit</a>
										<%}%>
										</td>
										
										<%if(hasPermission){ %>
											<td class="text-center"> <%if(row.isRenewBlocked()) {%> <span class=" badge badge-danger "> Yes </span><%}else{ %> <span class=" badge badge-info "> No </span> <%} %> </td>
											<td class="text-center"> <%if(row.isPrivileged()) {%> <span class=" badge badge-warning "> Yes </span><%}else{ %> <span class=" badge badge-info"> No </span> <%} %> </td>
											
											<td class="text-center">
											<%if(!row.isDeleted()){%> 
												<input  class="checkboxes" type="checkbox" name="domainIDs" value="<%=row.getID() %>"  > 
											<%}%>	
												</td>
										<%} %>
									<%
										}
									%>
								</tr>
							<% } %>
					</tbody>
				</table>
			</div>
		</form>
	</div>
</div>
<%}catch(Exception ex){
	searchLogger.debug("Exception ", ex);
}
%>
<!-- END PAGE LEVEL PLUGINS -->
<script src="<%=context%>/assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
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
       
       $("#terminateBtn").on( "click", function(){
    	  
    	   var param = {};
    	   param['domainIDs'] = new Array();
    	   param['mode'] = "terminateDomain";
    	   
    	   var url = context + "DomainAction.do?";
    	   
    	   $("input[name='domainIDs']:checked").each(function(){
    		   
    		   var id = $(this).val();
    		   url += "domainIDs=" + id + "&";
    	   });
    	  
    	   callAjax( url, param, renderDomainTerminate, "POST" );
       });
       
       function renderDomainTerminate(data){
    	  
    	   if( data['responseCode'] == 1 ){
    		   
    		   toastr.success(data['msg']);
    		   window.location = context + "SearchDomain.do";
    	   }
    	   else if( data['responseCode'] == 2 ) {
    		   
    		   toastr.error(data['msg']);
    	   }
    	  
       }
    })
    
    </script>