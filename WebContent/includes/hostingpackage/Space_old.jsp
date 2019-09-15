<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%String space = (String)session.getAttribute("HPSPACE");
if(space == null)space="-1";
%>
<tr >

<%-- <html:hidden property="bonusStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Space</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="HPSPACE">
 <option value="-1" <%=(space.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=1024%>" <%=(space.equals(""+1024)?"selected=\"selected\"":"")%>>1 GB</option> 
<option value="<%=2048%>" <%=(space.equals(""+2048)?"selected=\"selected\"":"")%>>2 GB</option> 
<option value="<%=5120%>" <%=(space.equals(""+5120)?"selected=\"selected\"":"")%>>5 GB</option>
<option value="<%=10240%>" <%=(space.equals(""+10240)?"selected=\"selected\"":"")%>>10 GB</option>
<option value="<%=20480%>" <%=(space.equals(""+20480)?"selected=\"selected\"":"")%>>20 GB</option>

</select>
</td>
</tr>