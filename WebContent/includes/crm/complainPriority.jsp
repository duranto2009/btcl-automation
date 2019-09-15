
<%@page import="common.StringUtils"%>
<%@page import="crm.CrmComplainDTO"%>
<%@ page language="java"%><%@ taglib uri="../../WEB-INF/struts-bean.tld"
	prefix="bean"%><%@ taglib uri="../../WEB-INF/struts-html.tld"
	prefix="html"%><%@ taglib uri="../../WEB-INF/struts-logic.tld"
	prefix="logic"%>
<%@ page
	import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = StringUtils.trim((String) session.getAttribute("priority"));
	/* if(orderTime == null)orderTime=format.format(System.currentTimeMillis()); */
	if (requested == null) {
		requested = "";
	}
	//int status = Integer.parseInt(requested);
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Complain
		Priority</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<div class="input-group " style="width: 100%;">
			<select class="form-control pull-right" name="priority">
				<option value="" <%if (requested.equals("")) {%> selected <%}%>>All</option>
				<option value="<%=CrmComplainDTO.LOW%>"
					<%if(!requested.equals("") && CrmComplainDTO.LOW == Integer.parseInt(requested)) {%> selected <%}%>>Low</option>
				<option value="<%=CrmComplainDTO.NORMAL%>"
					<%if (!requested.equals("") && CrmComplainDTO.NORMAL == Integer.parseInt(requested)) {%> selected <%}%>>Normal
					</option>
				<option value="<%=CrmComplainDTO.HIGH%>"
					<%if (!requested.equals("") && CrmComplainDTO.HIGH == Integer.parseInt(requested)) {%> selected <%}%>>High</option>
			</select>
		</div>
	</div>
</div>
