<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%String tld = (String)session.getAttribute("TLD");
if(tld == null)tld="-1";
%>


<%-- <html:hidden property="bonusStatus" value ="1"/> --%>

<div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">TLD</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
<select class="form-control" size="1" name="TLD">
 <option value="-1" <%=(tld.equals("-1")?"selected=\"selected\"":"")%>>All</option>
<%for(int i = 0; i < HostingConstants.TLD.length; i++){ %>
<option value="<%=HostingConstants.TLD[i]%>" <%=(tld.equals(""+HostingConstants.TLD[i])?"selected=\"selected\"":"")%>><%=HostingConstants.TLD[i]%></option> 
<%}%>

</select>
</div></div>