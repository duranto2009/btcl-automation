<%@page import="crm.CrmDepartmentDTO"%>
<%@ page import="login.*, util.*, common.*, common.repository.*, java.util.*, sessionmanager.*"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>


<style type="text/css">
	button.dt-button, div.dt-button, a.dt-button{
		color: white !important;
		border-color: white !important;
	}
	td, th{
		text-align: center;
	}
</style>
<%
	String url = "CrmDesignation/department/getSearchDepartment";
	String navigator = SessionConstants.NAV_CRM_DEPT;
	String context = "../.." + request.getContextPath();
	Logger logger = Logger.getLogger(getClass());
	LoginDTO localLoginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	String notApplicable = "N/A";
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>



<div class="portlet box">
	<div class="portlet-body">
		<form id="tableForm">
			<div class="table-responsive">
				<table id="tableData" class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>Department</th>
							<th>District</th>
							<th>Upazila</th>
							<th>Union</th>
							<th>is NOC</th>
							<th><input type="submit" class="btn btn-xs btn-danger" value="Delete" /></th>
						</tr>
					</thead>
					<tbody>
					<%
					
					ArrayList<CrmDepartmentDTO> data = (ArrayList<CrmDepartmentDTO>) session.getAttribute(SessionConstants.VIEW_CRM_DEPT);
					if(data != null){
						for(CrmDepartmentDTO dept: data){
							%>
								<tr>
									<td><a href="${context }CrmEmployee/getDepartmentView.do?departmentID=<%=dept.getID()%>" target=_blank><%=dept.getDepartmentName() %></td>
									<td><%=dept.getDistrictNameString() %></td>
									<td><%=dept.getUpazilaNameString() %></td>
									<td><%=dept.getUnionNameString() %></td>
									
									<td><%=dept.isNOCString()%></td>
									<td><input type="checkbox" id="<%=dept.getID()%>" name="deleteIDs" value="<%=dept.getID()%>"></td>
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
		</form>
	</div>
</div>

<!-- END PAGE LEVEL PLUGINS -->
<script src="../../assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script type="text/javascript">
   $(document).ready(function(){
	   $('#tableForm').submit(function(e) {
		   e.preventDefault(); 
		   	var currentForm = this;
		    var selected=false;
		    var set = $('#tableData').find('tbody > tr > td:last-child input[type="checkbox"]');
		    $(set).each(function() {
		    	if($(this).prop('checked')){
		    		selected=true;
		    	}
		    });
		    if(!selected){
		    	 bootbox.alert("Select a Department to delete!", function() { });
		    }else{
		    	 bootbox.confirm("Are you sure you want to delete the department(s)?", function(result) {
		            if (result) {
		            	var url = "../../CrmDesignation/department/deleteDepartment.do"
		            	var formData = $("#tableForm").serialize();
						callAjax(url, formData, function(data){
								if(data['responseCode'] == 1){
									toastr.success(data['msg']);
									window.setTimeout(function(){
									   location.reload();
									}, 1000);
								}else {
									toastr.error(data['msg']);
								}
							}, "POST");
		             }
		         });
		    }
		});
    });
    
    </script>