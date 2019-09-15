<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="user.UserDTO, java.util.ArrayList"%>    
<%-- <%
ArrayList<UserDTO> adminList  =  (ArrayList<UserDTO> )request.getAttribute("adminList");
%> --%> 
<%-- <div class="form-group">
	<select class="form-control select2" name="requestToAccountID" style="width: 100%">
		<%for( UserDTO user: adminList) {%>
			<option value='-<%= user.getUserID()%>'><%=user.getUsername() %></option>
		<%} %>
	</select>
</div> --%>

<div class="form-group">
	<label for="cnName">Client Name</label>
	     <input id="clientIdStr" type="text" class="form-control" name="clientIdStr" >
		 <input id="clientId" type="hidden" class="form-control" name="domainClientID" >
</div>

<script>
$(document).ready(function() {
	$("#clientIdStr").autocomplete({
		source : function(request, response) {
			$("#clientId").val(-1);
			var term = request.term;
			var url = '../../AutoComplete.do?moduleID='+CONFIG.get('module','domain')+'&need=client';
			var formData = {};
			formData['name'] = term;
			callAjax(url, formData, response, "GET");
		},
		minLength : 1,
		select : function(e, ui) {
			$('#clientIdStr').val(ui.item.data);
			$('#clientId').val(ui.item.id);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		/* console.log(item); */
		return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);
	};
});
</script>