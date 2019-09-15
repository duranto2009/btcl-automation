<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,help.*" %>
<%String hrStatus = (String)session.getAttribute("hrStatus");
if(hrStatus == null)hrStatus="-1";
%>


<%-- <html:hidden property="hrStatus" value ="1"/> --%>

<div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Complain Status</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
<select class="form-control" size="1" name="hrStatus">
 <option value="-1" <%=(hrStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=HelpConstants.HELP_STATUS_VALUE_OPEN%>" <%=(hrStatus.equals(""+HelpConstants.HELP_STATUS_VALUE_OPEN)?"selected=\"selected\"":"")%>><%=HelpConstants.HELP_STATUS_NAME_OPEN%></option> 
<option value="<%=HelpConstants.HELP_STATUS_VALUE_CLOSED%>" <%=(hrStatus.equals(""+HelpConstants.HELP_STATUS_VALUE_CLOSED)?"selected=\"selected\"":"")%>><%=HelpConstants.HELP_STATUS_NAME_CLOSED%></option> 

  
</select>
</div>
</div>