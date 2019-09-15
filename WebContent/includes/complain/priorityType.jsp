<%@page import="complain.ComplainConstants"%>
<%@page import="vpn.constants.*"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%String serviceType = (String)session.getAttribute("cmPriority");
if(serviceType == null)serviceType="";
%>


<%-- <html:hidden property="bonusStatus" value ="1"/> --%>

<div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Priority</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
<select class="form-control" size="1" name="cmPriority">
 <option value="" <%=(serviceType.equals("")?"selected=\"selected\"":"")%>>All</option>
 
<%for(int key:ComplainConstants.PRIORITY_MAP.keySet() ){ %>
<option value="<%=key%>" <%=(serviceType.equals(key + "")?"selected=\"selected\"":"")%>><%=ComplainConstants.PRIORITY_MAP.get(key)%></option> 
<%}%>
</select>
</div>
</div>