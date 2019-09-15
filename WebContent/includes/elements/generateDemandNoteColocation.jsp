<%@page import="coLocation.ColocationDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	ColocationDTO colocationDTO = (ColocationDTO)request.getAttribute("colocationDTO");
%>
<br/>
<div class="form-group">
	<a class="btn btn-submit-btcl" href="<%=request.getContextPath() %>/coLocation/colocation/generateDemandNote.jsp?clientID=<%=colocationDTO.getColocationClientID()%>&colocationID=<%=colocationDTO.getColocationID() %>"> Generate </a>
</div>