<%@page import="common.bill.BillConstants"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.bill.BillDTO"%>
<%@ page language="java"%><%@ taglib uri="../../WEB-INF/struts-bean.tld"
	prefix="bean"%><%@ taglib uri="../../WEB-INF/struts-html.tld"
	prefix="html"%><%@ taglib uri="../../WEB-INF/struts-logic.tld"
	prefix="logic"%>
<%@ page
	import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = (String) session.getAttribute("billActionType");
	/* if(orderTime == null)orderTime=format.format(System.currentTimeMillis()); */
	if (requested == null) {
		requested = "";
	}
	
	String locaModuleID = request.getParameter("moduleID");
	if(locaModuleID != null){
%>
<% if(locaModuleID.equals(""+ModuleConstants.Module_ID_DOMAIN )) {%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Bill Action Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control pull-right" name="billActionType" >
			<option value="" <%if (requested.equals("") ){%> selected <%}%>>All</option>
			<option
					value="domain.DomainDemandNote" <%if (requested.equals("domain.DomainDemandNote")){%>
					selected <%}%>>New Domain
			</option>
			<option
					value="domain.DomainRenewDemandNote" <%if (requested.equals("domain.DomainRenewDemandNote")){%>
					selected <%}%>>Renew Domain
			</option>
		</select>
		
		
		
		
	</div>
</div>
<%}else{ %>

<%}} %>
