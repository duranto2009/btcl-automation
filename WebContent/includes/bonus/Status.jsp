<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,bonus .*" %>
<%String bonusStatus = (String)session.getAttribute("bonusStatus");
if(bonusStatus == null)bonusStatus="-1";
%>
<tr >

<%-- <html:hidden property="bonusStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Bonus Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="bonusStatus">
 <option value="-1" <%=(bonusStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=BonusConstants.BONUS_STATUS_ACTIVE%>" <%=(bonusStatus.equals(""+BonusConstants.BONUS_STATUS_ACTIVE)?"selected=\"selected\"":"")%>>Active</option> 
<option value="<%=BonusConstants.BONUS_STATUS_DELETED%>" <%=(bonusStatus.equals(""+BonusConstants.BONUS_STATUS_DELETED)?"selected=\"selected\"":"")%>>Deleted</option> 

  
</select>
</td>
</tr>