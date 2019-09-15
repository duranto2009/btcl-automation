<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,invoiceorder.*" %>
<%String invoiceOrderStatus = (String)session.getAttribute("invoiceOrderStatus");
if(invoiceOrderStatus == null)invoiceOrderStatus="-1";
%>
<tr >

<%-- <html:hidden property="invoiceOrderStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Complain Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="invoiceOrderStatus">
 <option value="-1" <%=(invoiceOrderStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=InvoiceOrderConstants.ORDER_STATUS_ACTIVE%>" <%=(invoiceOrderStatus.equals(""+InvoiceOrderConstants.ORDER_STATUS_ACTIVE)?"selected=\"selected\"":"")%>>Active</option> 
<option value="<%=InvoiceOrderConstants.ORDER_STATUS_DELETED%>" <%=(invoiceOrderStatus.equals(""+InvoiceOrderConstants.ORDER_STATUS_DELETED)?"selected=\"selected\"":"")%>>Deleted</option> 

  
</select>
</td>
</tr>