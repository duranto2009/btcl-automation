<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*,invoice.*" %>
<%String invoiceOrderType = (String)session.getAttribute("invoiceOrderType");
if(invoiceOrderType == null)invoiceOrderType="-1";
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Invoice Type</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="invoiceOrderType">
  <option value="-1" <%=(invoiceOrderType.equals("-1")?"selected=\"selected\"":"")%>>All</option>
 <%					          
   
    for(int i=0;i<InvoiceConstants.INVOICE_TYPE_NAME.length;i++)
    {
     
 %>
               <option value="<%=InvoiceConstants.INVOICE_TYPE_VALUE[i]%>" <%=(invoiceOrderType.equals(""+InvoiceConstants.INVOICE_TYPE_VALUE[i])?"selected=\"selected\"":"")%>><%=InvoiceConstants.INVOICE_TYPE_NAME[i]%></option>
 <% } %>
 
 
</select>
</td>
</tr>