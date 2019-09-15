<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@page import="common.EntityTypeConstant"%>
<%@ page language="java"%><%@ taglib uri="../../WEB-INF/struts-bean.tld"
	prefix="bean"%><%@ taglib uri="../../WEB-INF/struts-html.tld"
	prefix="html"%><%@ taglib uri="../../WEB-INF/struts-logic.tld"
	prefix="logic"%>
<%@ page
	import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = (String) session.getAttribute("status");
	if (requested == null) {
		requested = "";
	}
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Status</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<div class="input-group " style="width:100%;">
			<select class="form-control pull-right" name="status" >
				<option value="" <%if (requested.equals("") ){%> selected <%}%>>All</option>
				<option value="<%=EntityTypeConstant.STATUS_NOT_ACTIVE %>" <%if (requested.equals(""+EntityTypeConstant.STATUS_NOT_ACTIVE) ){%> selected <%}%>>Not active</option>
				<option value="<%=EntityTypeConstant.STATUS_ACTIVE %>" <%if (requested.equals(""+EntityTypeConstant.STATUS_ACTIVE) ){%> selected <%}%>>Approved</option>
				<option value="<%=EntityTypeConstant.STATUS_SEMI_ACTIVE %>" <%if (requested.equals(""+EntityTypeConstant.STATUS_SEMI_ACTIVE) ){%> selected <%}%>>Processing</option>
				
			</select>
					
		</div>
	</div>
</div>