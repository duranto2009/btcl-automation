	<%
	response.setContentType("application/octet-stream");
	String filename = request.getParameter("filename");
	//response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + filename);
	
	%>