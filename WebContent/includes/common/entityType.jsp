<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.bill.BillDTO"%>
<%@ page language="java"%><%@ taglib uri="../../WEB-INF/struts-bean.tld"
	prefix="bean"%><%@ taglib uri="../../WEB-INF/struts-html.tld"
	prefix="html"%><%@ taglib uri="../../WEB-INF/struts-logic.tld"
	prefix="logic"%>
<%@ page
	import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = (String) session.getAttribute("entityTypeID");
	/* if(orderTime == null)orderTime=format.format(System.currentTimeMillis()); */
	if (requested == null) {
		requested = "";
	}
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Entity Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<div class="input-group " style="width:100%;">
			<select class="form-control pull-right" name="entityTypeID" >
			<option value="" <%if (requested.equals("") ){%> selected <%}%>>All</option>
			<% for(Map.Entry<Integer,String> entity : EntityTypeConstant.entityNameMap.entrySet()){ %>
			<option
					value="<%=entity.getKey()  %>" <%if (requested.equals(Integer.toString(entity.getKey()))) {%>
					selected <%}%>><%=entity.getValue() %></option>
			<%} %>
					</select>
		</div>
	</div>
</div>
