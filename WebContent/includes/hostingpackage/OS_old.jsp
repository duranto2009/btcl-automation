<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%String os = (String)session.getAttribute("HPOS");
if(os == null)os="-1";
%>
<tr >

<%-- <html:hidden property="bonusStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Operating System</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="HPCOSTTYPE">
 <option value="-1" <%=(os.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=HostingConstants.WINDOWS%>" <%=(os.equals(""+HostingConstants.WINDOWS)?"selected=\"selected\"":"")%>>Windows</option> 
<option value="<%=HostingConstants.LINUX%>" <%=(os.equals(""+HostingConstants.LINUX)?"selected=\"selected\"":"")%>>Linux</option> 

</select>
</td>
</tr>