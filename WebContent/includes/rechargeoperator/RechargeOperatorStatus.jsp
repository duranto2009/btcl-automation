<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,operator.*,rechargeoperator.*"%>
<%String rechargeStatus = (String)session.getAttribute("rechargeStatus");
if(rechargeStatus == null)rechargeStatus="-1";
%>
<tr >

<%-- <html:hidden property="rechargeStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="rechargeStatus">
 <option value="-1" <%=(rechargeStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=OperatorConstants.OPERATOR_ACTIVE%>" <%=(rechargeStatus.equals(""+OperatorConstants.OPERATOR_ACTIVE)?"selected=\"selected\"":"")%>>Active</option> 
<option value="<%=OperatorConstants.OPERATOR_DELETED%>" <%=(rechargeStatus.equals(""+OperatorConstants.OPERATOR_DELETED)?"selected=\"selected\"":"")%>>Deleted</option>

  
</select>
</td>
</tr>