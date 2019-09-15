<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*,invoiceorder.*" %>
<%String invoiceOrderType = (String)session.getAttribute("invoiceOrderType");
if(invoiceOrderType == null)invoiceOrderType="-1";
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Package Type</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="invoiceOrderType">
  <option value="-1" <%=(invoiceOrderType.equals("-1")?"selected=\"selected\"":"")%>>All</option>
 <%					          
   
    for(int i=0;i<ClientConstants.PACKAGETYPE_NAME.length-1;i++)
    {
     
 %>
               <option value="<%=ClientConstants.PACKAGETYPE_VALUE[i]%>" <%=(invoiceOrderType.equals(""+ClientConstants.PACKAGETYPE_VALUE[i])?"selected=\"selected\"":"")%>><%=ClientConstants.PACKAGETYPE_NAME[i]%></option>
 <% } %>
 
 
</select>
</td>
</tr>