<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%String bandwidth = (String)session.getAttribute("VPBANDWIDTH");
if(bandwidth == null)bandwidth="-1";
%>

<%-- <html:hidden property="bonusStatus" value ="1"/> --%>

<div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Bandwidth</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
<select class="form-control" size="1" name="VPBANDWIDTH">
 <option value="-1" <%=(bandwidth.equals("-1")?"selected=\"selected\"":"")%>>All</option>
<%for(int i = 0; i < HostingConstants.VPNBandWidth.length; i++){ %>
<option value="<%=HostingConstants.VPNBandWidth[i]%>" <%=(bandwidth.equals(""+HostingConstants.VPNBandWidthVal[i])?"selected=\"selected\"":"")%>><%=HostingConstants.VPNBandWidth[i]%></option> 
<%}%>

</select>
</div></div>