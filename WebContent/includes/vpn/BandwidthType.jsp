<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%
System.out.println("before include btype");
String bandwidthType = (String)session.getAttribute("VPBANDWIDTHTYPE");
if(bandwidthType == null)bandwidthType="-1";
System.out.println("after include btype");
%>

<%-- <html:hidden property="bonusStatus" value ="1"/> --%>

<div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Bandwidth Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
<select class="form-control" size="1" name="VPBANDWIDTHTYPE">
 <option value="-1" <%=(bandwidthType.equals("-1")?"selected=\"selected\"":"")%>>All</option>
<%for(int i = 0; i < HostingConstants.BADWIDTH_TYPE.length; i++){ %>
<option value="<%=HostingConstants.BADWIDTH_TYPE[i]%>" <%=(bandwidthType.equals(""+HostingConstants.BADWIDTH_TYPE_VAL[i])?"selected=\"selected\"":"")%>><%=HostingConstants.BADWIDTH_TYPE[i]%></option> 
<%}%>

</select>
</div></div>