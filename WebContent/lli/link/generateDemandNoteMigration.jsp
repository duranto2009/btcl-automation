<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliMigration");
  request.setAttribute("subMenu2","lliBillMigration");
%>
<!-- BEGIN PAGE LEVEL STYLES -->
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="LLI Bill Migration" /> 
	<jsp:param name="body" value="../lli/link/generateDemandNoteMigrationBody.jsp" />
	<jsp:param name="css" value="assets/global/plugins/odometer/odometer.css" />
	<jsp:param name="js" value="assets/global/plugins/odometer/odometer.js" />
</jsp:include> 
