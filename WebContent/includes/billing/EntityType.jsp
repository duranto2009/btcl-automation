<%@page import="java.util.Map.Entry"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.bill.BillDTO"%>
<%@ page language="java"%><%@ taglib uri="../../WEB-INF/struts-bean.tld"
	prefix="bean"%><%@ taglib uri="../../WEB-INF/struts-html.tld"
	prefix="html"%><%@ taglib uri="../../WEB-INF/struts-logic.tld"
	prefix="logic"%>
<%@ page
	import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = (String) session.getAttribute("entityID");
String moduleID = request.getParameter("moduleID");
	/* if(orderTime == null)orderTime=format.format(System.currentTimeMillis()); */
	if (requested == null) {
		requested = "";
	}
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Entity Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control pull-right" name="entityID" >
		<option value="" <%if (requested.equals("") ){%> selected <%}%>>All</option>
		<% Map<Integer, String>entityNameMap = EntityTypeConstant.entityNameMap; 
		for(Entry<Integer, String>entityNameMapItem : entityNameMap.entrySet()){
		 int moduleIDCalculated = entityNameMapItem.getKey()/ EntityTypeConstant.MULTIPLIER2;
		 if( Integer.parseInt(moduleID) == moduleIDCalculated){
		%>
		<option
				value="<%=entityNameMapItem.getKey()  %>" <%if (requested.equals(Integer.toString(entityNameMapItem.getKey()))) {%>
				selected <%}%>><%=entityNameMapItem.getValue() %></option>
		<%}} %>
		</select>
	</div>
</div>