<%@page import="common.ModuleConstants"%>
<%@page import="vpn.constants.*"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%String serviceType = (String)session.getAttribute("cmModuleID");
if(serviceType == null)serviceType="-1";
%>


<div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Module</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
<select class="form-control" size="1" name="cmModuleID">
 <option value="-1" <%=(serviceType.equals("-1")?"selected=\"selected\"":"")%>>All</option>
 
<%for(int key:ModuleConstants.ModuleMap.keySet() ){ %>
<option value="<%=key%>" <%=(serviceType.equals(key + "")?"selected=\"selected\"":"")%>><%=ModuleConstants.ModuleMap.get(key)%></option> 
<%}%>
</select>
</div>
</div>