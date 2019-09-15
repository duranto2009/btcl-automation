<%@page import="org.apache.log4j.Logger"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="user.UserDTO, java.util.ArrayList"%>    
<%
Logger adminListLogger = Logger.getLogger("adminList_jsp");
ArrayList<UserDTO> adminList  =  (ArrayList<UserDTO> )request.getAttribute("adminList");
/* adminListLogger.debug("adminList " + adminList); */
String currentActionTypeID = (String)request.getAttribute("fileRequestID");
%> 
<div class="form-group">
	<label for="cusDistance" class="control-label col-md-3">Select Admin</label>
	<div class="col-md-4">
		<select class="form-control select2" id="requestToAccountID" style="width: 100%">
			
			<option value='0'> ---Select--- </option>
			<%if(adminList != null){%>
			<%for( UserDTO user: adminList) {
				String fullName = user.getFullName();
				if(fullName != null && !fullName.isEmpty())
				{
					fullName = " ("+fullName+")";
				}
			%>
				<option value='-<%= user.getUserID()%>'><%=(user.getUsername() + fullName) %></option>
			<%} %>
			<%}%>
		</select>
	</div>
</div>
<script>
	$(document).ready(function(){
		var form = $('#fileupload<%=currentActionTypeID%>');
		$("#requestToAccountID").on("change", function(){
			$("input[name=requestToAccountID]").val($("#requestToAccountID").val());
		});
		$("#requestToAccountID").trigger("change");
		$(form).on('submit', function(){
			var select = $(this).find('select');
			if($(select).val() == '0'){
				$(select).closest('.form-group').addClass('has-error');
				toastr.error("please select an admin");
				return false;
			}
			return true;
		});
	});
</script>