<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%String costType = (String)session.getAttribute("HPCOSTTYPE");
if(costType == null)costType="-1";
%>
<tr >

<%-- <html:hidden property="bonusStatus" value ="1"/> --%>

<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Payment Inverval</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
<select class="form-control" size="1" name="HPCOSTTYPE">
 <option value="-1" <%=(costType.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=HostingConstants.COST_TYPE_MONTHLY%>" <%=(costType.equals(""+HostingConstants.COST_TYPE_MONTHLY)?"selected=\"selected\"":"")%>>Monthly</option> 
<option value="<%=HostingConstants.COST_TYPE_YEARLY%>" <%=(costType.equals(""+HostingConstants.COST_TYPE_YEARLY)?"selected=\"selected\"":"")%>>Yearly</option> 

</select>
</div>
</div>