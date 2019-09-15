<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@page import="vpn.client.ClientForm"%>
<%@page import="common.EntityTypeConstant"%>
<%@ page language="java"%><%@ taglib uri="../../WEB-INF/struts-bean.tld"
	prefix="bean"%><%@ taglib uri="../../WEB-INF/struts-html.tld"
	prefix="html"%><%@ taglib uri="../../WEB-INF/struts-logic.tld"
	prefix="logic"%>
<%@ page
	import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = (String) session.getAttribute("vclClientType");
	if (requested == null) {
		requested = "";
	}
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Client Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control pull-right" name="vclClientType" >
			<option value="" <%if (requested.equals("") ){%> selected <%}%>>All</option>
			<option value="<%=ClientForm.CLIENT_TYPE_INDIVIDUAL %>" <%if (requested.equals(""+ClientForm.CLIENT_TYPE_INDIVIDUAL) ){%> selected <%}%>><%=ClientForm.CLIENT_TYPE_STR.get(ClientForm.CLIENT_TYPE_INDIVIDUAL) %></option>
			<option value="<%=ClientForm.CLIENT_TYPE_COMPANY %>" <%if (requested.equals(""+ClientForm.CLIENT_TYPE_COMPANY) ){%> selected <%}%>><%=ClientForm.CLIENT_TYPE_STR.get(ClientForm.CLIENT_TYPE_COMPANY) %></option>
		</select>
	</div>
</div>