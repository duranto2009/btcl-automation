<%@page import="request.RequestStatus"%>
<%@page import="login.LoginDTO"%>
<%@page import="common.ModuleConstants"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="vpn.constants.*"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%
LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
String status = (String)session.getAttribute("arCompletionStatus");
if(status == null)status="-1";
%>


<%-- <html:hidden property="bonusStatus" value ="1"/> --%>

<div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Request Status</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
<select class="form-control" size="1" name="arCompletionStatus">
 <option value="-1" <%=(status.equals("-1")?"selected=\"selected\"":"")%>>All</option>
 
 
<%for(int key:RequestStatus.reqStatusMap.keySet() ){ 
/* if(loginDTO.getAccountID() > 0 && key == RequestStatus.SEMI_PROCESSED)continue; */
%>
<option value="<%=key%>" <%=(status.equals(key + "")?"selected=\"selected\"":"")%>><%=RequestStatus.reqStatusMap.get(key)%></option> 
<%}%>
</select>
</div>
</div>