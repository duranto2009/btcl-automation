<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%String tld = (String)session.getAttribute("TLD");
if(tld == null)tld="-1";
%>
<tr >

<%-- <html:hidden property="bonusStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">TLD</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="TLD">
 <option value="-1" <%=(tld.equals("-1")?"selected=\"selected\"":"")%>>All</option>
<%for(int i = 0; i < HostingConstants.TLD.length; i++){ %>
<option value="<%=HostingConstants.TLD[i]%>" <%=(tld.equals(""+HostingConstants.TLD[i])?"selected=\"selected\"":"")%>><%=HostingConstants.TLD[i]%></option> 
<%}%>

</select>
</td>
</tr>