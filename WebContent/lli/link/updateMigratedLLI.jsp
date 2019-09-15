<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliMigration");
  request.setAttribute("subMenu2","lliConnectionMigration");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Update Migrated LLI" /> 
	<jsp:param name="body" value="../lli/link/updateMigratedLLIBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 
