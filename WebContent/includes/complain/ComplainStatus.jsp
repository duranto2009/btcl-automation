<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,complain.*" %>
<%String clStatus = (String)session.getAttribute("clStatus");
if(clStatus == null)clStatus="-1";
%>
<tr >

<%-- <html:hidden property="clStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Category Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="clStatus">
 <option value="-1" <%=(clStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=ComplainConstants.COMPLAIN_STATUS_ACTIVE_VALUE%>" <%=(clStatus.equals(""+ComplainConstants.COMPLAIN_STATUS_ACTIVE_VALUE)?"selected=\"selected\"":"")%>><%=ComplainConstants.COMPLAIN_STATUS_ACTIVE_NAME%></option> 
<option value="<%=0%>" <%=(clStatus.equals(""+0)?"selected=\"selected\"":"")%>><%=ComplainConstants.COMPLAIN_STATUS_DELETED_NAME%></option>
 
  
</select>
</td>
</tr>