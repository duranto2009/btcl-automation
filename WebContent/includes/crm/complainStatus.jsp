
<%@page import="common.StringUtils"%>
<%@page import="crm.CrmComplainDTO"%>
<%@ page language="java"%><%@ taglib uri="../../WEB-INF/struts-bean.tld"
	prefix="bean"%><%@ taglib uri="../../WEB-INF/struts-html.tld"
	prefix="html"%><%@ taglib uri="../../WEB-INF/struts-logic.tld"
	prefix="logic"%>
<%@ page
	import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = StringUtils.trim((String) session.getAttribute("status"));
	/* if(orderTime == null)orderTime=format.format(System.currentTimeMillis()); */
	if (requested == null) {
		requested = "";
	}
	//int status = Integer.parseInt(requested);
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Complain
		Status</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<div class="input-group " style="width: 100%;">
			<select class="form-control pull-right" name="status">
				<option value="" <%if (requested.equals("")) {%> selected <%}%>>All</option>
				<option value="<%=CrmComplainDTO.COMPLETED%>"
					<%if(!requested.equals("") && CrmComplainDTO.COMPLETED == Integer.parseInt(requested)) {%> selected <%}%>>Completed</option>
				<option value="<%=CrmComplainDTO.ON_GOING%>"
					<%if (!requested.equals("") && CrmComplainDTO.ON_GOING == Integer.parseInt(requested)) {%> selected <%}%>>On
					Going</option>
				<option value="<%=CrmComplainDTO.STARTED%>"
					<%if (!requested.equals("") && CrmComplainDTO.STARTED == Integer.parseInt(requested)) {%> selected <%}%>>Started</option>
			</select>
		</div>
	</div>
</div>
