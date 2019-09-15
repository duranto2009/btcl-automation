<%
	Long schedulerID =(Long) request.getAttribute("ID");
	String title = (schedulerID == null ) ? "View Scheduler Configuration" : "Edit Scheduler Configuration";
%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" /> 
	<jsp:param name="body" value="../scheduler_config/viewSchedulerConfigBody.jsp" />
</jsp:include> 