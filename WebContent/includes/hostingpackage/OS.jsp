<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%String os = (String)session.getAttribute("HPOS");
if(os == null)os="-1";
%>

<%-- <html:hidden property="bonusStatus" value ="1"/> --%>

<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Operating System</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
<select class="form-control" size="1" name="HPOS">
 <option value="-1" <%=(os.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=HostingConstants.WINDOWS%>" <%=(os.equals(""+HostingConstants.WINDOWS)?"selected=\"selected\"":"")%>>Windows</option> 
<option value="<%=HostingConstants.LINUX%>" <%=(os.equals(""+HostingConstants.LINUX)?"selected=\"selected\"":"")%>>Linux</option> 

</select>
</div>
</div>