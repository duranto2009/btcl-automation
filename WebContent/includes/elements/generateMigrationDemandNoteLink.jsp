<%@page import="vpn.link.VpnLinkDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	VpnLinkDTO link = (VpnLinkDTO)request.getAttribute("vpnLink");
%>
<br/>
<div class="form-group">
	<a class="btn btn-submit-btcl" href="<%=request.getContextPath() %>/vpn/link/generateDemandNoteMigration.jsp?clientID=<%=link.getClientID()%>&vpnLinkID=<%=link.getID() %>"> Generate </a>
</div>