<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,othercharge.*" %>
<%String othStatus = (String)session.getAttribute("othStatus");
if(othStatus == null)othStatus="-1";
%>
<tr >

<%-- <html:hidden property="othStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Deduction Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="othStatus">
 <option value="-1" <%=(othStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=OtherChargeConstants.OTHER_CHARGE_DEDUCTED%>" <%=(othStatus.equals(""+OtherChargeConstants.OTHER_CHARGE_DEDUCTED)?"selected=\"selected\"":"")%>>Deducted</option> 
<option value="<%=OtherChargeConstants.OTHER_CHARGE_TOBEDEDUCTED%>" <%=(othStatus.equals(""+OtherChargeConstants.OTHER_CHARGE_TOBEDEDUCTED)?"selected=\"selected\"":"")%>>To Be Deducted</option> 

  
</select>
</td>
</tr>