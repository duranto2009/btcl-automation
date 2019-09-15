<%@page import="websecuritylog.WebSecurityConstant"%>
<%@page import="login.LoginDTO"%>
<%@page import="common.ModuleConstants"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="vpn.constants.*"%>
<%@ page language="java" %>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>

<%String attemptTypeStr = (String)session.getAttribute("awslAttemptType");
int attemptType = -1;
if(attemptTypeStr != null){
	attemptType= Integer.parseInt(attemptTypeStr);
}
%>

<div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Attempt Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control select2" size="1" name="awslAttemptType" >
		 	<option value="-1" <%=((attemptType == -1)?"selected=\"selected\"":"")%>> All</option>
			 <%
			HashMap<Integer, String> attemptTypeMap = WebSecurityConstant.attemptTypeMap;
				for (Integer key : attemptTypeMap.keySet()) {
			%>
			
				<option value="<%=key%>" <%=((attemptType == key)?"selected=\"selected\"":"")%>> <%=attemptTypeMap.get(key) %></option> 
			<%}%>
		</select>
	</div>
</div>

