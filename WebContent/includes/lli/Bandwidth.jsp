<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%String bandwidth = (String)session.getAttribute("LLBANDWIDTH");
if(bandwidth == null)bandwidth="-1";
%>

<%-- <html:hidden property="bonusStatus" value ="1"/> --%>

<div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Bandwidth</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
<select class="form-control" size="1" name="LLBANDWIDTH">
 <option value="-1" <%=(bandwidth.equals("-1")?"selected=\"selected\"":"")%>>All</option>
<%for(int i = 0; i < HostingConstants.LLI_BANDWIDTH.length; i++){ %>
<option value="<%=HostingConstants.LLI_BANDWIDTH[i]%>" <%=(bandwidth.equals(""+HostingConstants.LLI_BANDWIDTH_VAL[i])?"selected=\"selected\"":"")%>><%=HostingConstants.LLI_BANDWIDTH[i]%></option> 
<%}%>

</select>
</div></div>