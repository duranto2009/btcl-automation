<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%String regCat = (String)session.getAttribute("IGREGCAT");
if(regCat == null)regCat="-1";
%>


<%-- <html:hidden property="bonusStatus" value ="1"/> --%>

<div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Reg. Category</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
<select class="form-control" size="1" name="IGREGCAT">
 <option value="-1" <%=(regCat.equals("-1")?"selected=\"selected\"":"")%>>All</option>
<%for(int i = 0; i < HostingConstants.RegistrationCat.length; i++){ %>
<option value="<%=HostingConstants.RegistrationCatVal[i]%>" <%=(regCat.equals(""+HostingConstants.RegistrationCatVal[i])?"selected=\"selected\"":"")%>><%=HostingConstants.RegistrationCat[i]%></option> 
<%}%>
</select>
</div>
</div>