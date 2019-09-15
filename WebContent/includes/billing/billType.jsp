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
	String requested = (String) session.getAttribute("billType");
	/* if(orderTime == null)orderTime=format.format(System.currentTimeMillis()); */
	if (requested == null) {
		requested = "";
	}
	
	String locaModuleID = request.getParameter("moduleID");
	if(locaModuleID != null){
%>
<% if(!locaModuleID.equals(ModuleConstants.Module_ID_VPN+"") && !locaModuleID.equals(ModuleConstants.Module_ID_LLI+"") ) {%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Bill Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control pull-right" name="billType" >
		<option value="" <%if (requested.equals("") ){%> selected <%}%>>All</option>
		<option
				value="<%=BillDTO.PREPAID_BILL  %>" <%if (requested.equals(Integer.toString(BillDTO.PREPAID_BILL))) {%>
				selected <%}%>>Prepaid</option>
				<option
				value="<%=BillDTO.POSTPAID_BILL  %>" <%if (requested.equals(Integer.toString(BillDTO.POSTPAID_BILL))) {%>
				selected <%}%>>Postpaid</option>
		</select>
	</div>
</div>
<%}else{ %>
	<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Bill Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control pull-right" name="billType" >
		<option value="" <%if (requested.equals("") ){%> selected <%}%>>All</option>
		<option
				value="<%=BillDTO.PREPAID_AND_POSTPAID_BILL  %>" <%if (requested.equals(Integer.toString(BillDTO.PREPAID_AND_POSTPAID_BILL))) {%>
				selected <%}%>>Demand Note</option>
				<option
				value="<%=BillDTO.POSTPAID_BILL  %>" <%if (requested.equals(Integer.toString(BillDTO.POSTPAID_BILL))) {%>
				selected <%}%>>Monthly Bill</option>
				<option
				value="<%=BillConstants.MONTHLY_BILL_ADVANCED  %>" <%if (requested.equals(Integer.toString(BillConstants.MONTHLY_BILL_ADVANCED ))) {%>
				selected <%}%>>Monthly Bill Advance</option>
				<option
				value="<%=BillConstants.MANUAL_BILL  %>" <%if (requested.equals(Integer.toString(BillConstants.MANUAL_BILL ))) {%>
				selected <%}%>>Manual Bill</option>
		</select>
	</div>
</div>	
<%}} %>
