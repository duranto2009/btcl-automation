<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*" %>
<%String valuePaymentType = (String)session.getAttribute("clPaymentType");
if(valuePaymentType == null)valuePaymentType="-1";
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Package Type</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="clPaymentType">
  <option value="-1" <%=(valuePaymentType.equals("-1")?"selected=\"selected\"":"")%>>All</option>
 <%					          
   
    for(int i=0;i<ClientConstants.PACKAGETYPE_NAME.length;i++)
    {
     
 %>
               <option value="<%=ClientConstants.PACKAGETYPE_VALUE[i]%>" <%=(valuePaymentType.equals(""+ClientConstants.PACKAGETYPE_VALUE[i])?"selected=\"selected\"":"")%>><%=ClientConstants.PACKAGETYPE_NAME[i]%></option>
 <% } %>
 
 
</select>
</td>
</tr>