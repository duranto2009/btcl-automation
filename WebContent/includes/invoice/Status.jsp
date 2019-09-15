<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,invoice.*" %>
<%String invoiceStatus = (String)session.getAttribute("invoiceStatus");
if(invoiceStatus == null)invoiceStatus="-1";
%>
<tr >

<%-- <html:hidden property="invoiceStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Invoice Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="invoiceStatus">
 <option value="-1" <%=(invoiceStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=InvoiceConstants.INVOICE_USED%>" <%=(invoiceStatus.equals(""+InvoiceConstants.INVOICE_USED)?"selected=\"selected\"":"")%>>Used</option> 
<option value="<%=InvoiceConstants.INVOICE_UNUSED%>" <%=(invoiceStatus.equals(""+InvoiceConstants.INVOICE_UNUSED)?"selected=\"selected\"":"")%>>Unused</option> 

  
</select>
</td>
</tr>