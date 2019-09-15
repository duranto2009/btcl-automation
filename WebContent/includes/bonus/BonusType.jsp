<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,bonus.*"%>
<%String bonusTypeID = (String)session.getAttribute("bonusTypeID");
if(bonusTypeID == null)bonusTypeID="-1";
%>
<tr >

<%-- <html:hidden property="bonusTypeID" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Bonus Type</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="bonusTypeID">
 <option value="-1" <%=(bonusTypeID.equals("-1")?"selected=\"selected\"":"")%>>All</option>
 <%					          
   
    for(int i=0;i<BonusConstants.BONUS_TYPE_VALUES.length;i++)
    {
     
 %>
               <option value="<%=BonusConstants.BONUS_TYPE_VALUES[i]%>" <%=(bonusTypeID.equals(""+BonusConstants.BONUS_TYPE_VALUES[i])?"selected=\"selected\"":"")%>><%=BonusConstants.BONUS_TYPE_NAMES[i]%></option>
 <% } %>
 
  
</select>
</td>
</tr>