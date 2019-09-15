<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliLink");
  request.setAttribute("subMenu2","searchLliLink");
%>
<!-- BEGIN PAGE LEVEL STYLES -->
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="LLI Link View" /> 
	<jsp:param name="body" value="../lli/link/generateDemandNoteBody.jsp" />
	<jsp:param name="css" value="assets/global/plugins/odometer/odometer.css" />
	<jsp:param name="js" value="assets/global/plugins/odometer/odometer.js" />
</jsp:include> 
