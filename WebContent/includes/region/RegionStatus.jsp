<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,regiontype.*" %>
<%String rStatus = (String)session.getAttribute("rStatus");
if(rStatus == null)rStatus="-1";
%>
<tr >

<%-- <html:hidden property="rStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="rStatus">
 <option value="-1" <%=(rStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=RegionConstants.REGION_STATUS_ACTIVE%>" <%=(rStatus.equals(""+RegionConstants.REGION_STATUS_ACTIVE)?"selected=\"selected\"":"")%>>Active</option> 
<option value="<%=RegionConstants.REGION_STATUS_DELETED%>" <%=(rStatus.equals(""+RegionConstants.REGION_STATUS_DELETED)?"selected=\"selected\"":"")%>>Deleted</option> 

  
</select>
</td>
</tr>