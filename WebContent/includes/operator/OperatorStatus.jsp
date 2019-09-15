<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,operator.*"%>
<%String btOperatorStatus = (String)session.getAttribute("btOperatorStatus");
if(btOperatorStatus == null)btOperatorStatus="-1";
%>
<tr >

<%-- <html:hidden property="btOperatorStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="btOperatorStatus">
 <option value="-1" <%=(btOperatorStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=OperatorConstants.OPERATOR_ACTIVE%>" <%=(btOperatorStatus.equals(""+OperatorConstants.OPERATOR_ACTIVE)?"selected=\"selected\"":"")%>>Active</option> 
<option value="<%=OperatorConstants.OPERATOR_DELETED%>" <%=(btOperatorStatus.equals(""+OperatorConstants.OPERATOR_DELETED)?"selected=\"selected\"":"")%>>Deleted</option>

  
</select>
</td>
</tr>