<%
  request.setAttribute("menu","lli");
  request.setAttribute("subMenu1","lliPreview");
  request.setAttribute("subMenu2","lliDemandNotePreview");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Demand Note Preview" /> 
	<jsp:param name="body" value="../lli/preview/demand-note-preview-body.jsp" />
</jsp:include> 