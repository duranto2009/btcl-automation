<%@page import="lli.link.LliLinkDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	LliLinkDTO link = (LliLinkDTO)request.getAttribute("lliLink");
%>
<br/>
<div class="form-group">
	<a class="btn btn-submit-btcl" href="<%=request.getContextPath() %>/DemandNote.do?method=upgradeLinkLLI&clientID=<%=link.getClientID()%>&lliLinkID=<%=link.getID() %>"> Generate Demand Note </a>
</div>