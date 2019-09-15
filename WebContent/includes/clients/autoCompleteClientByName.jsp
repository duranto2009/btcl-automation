<%@page import="java.text.SimpleDateFormat"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%
//String requestTime = (String)session.getAttribute("clientID");
/* if(orderTime == null)orderTime=format.format(System.currentTimeMillis()); */
//if(requestTime == null)requestTime="";
Integer moduleID = Integer.parseInt(request.getParameter("moduleID"));
//System.out.println("request Time " + requestTime);
%>
<div class="form-group">
	<label for="" class="<%=request.getParameter("labelClass")%>">Client Name</label>
	<div class="<%=request.getParameter("inputWrapperClass")%>"> 
	    <input id="clientIdStr" type="text" class="form-control">
		<input id="clientId" type="hidden" class="form-control" name="clientID" >
		<input type="hidden" id="moduleID" value="<%=moduleID %>" />
	</div>
</div>
<script src="${context}common/ajaxAutoCompleteClientByName.js" type="text/javascript"></script>
