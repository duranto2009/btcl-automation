
<%@page import="billing.BillConstant"%>
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%String statusType = (String)session.getAttribute("cbStatus");
if(statusType == null)statusType="0";
%>
<tr>
	<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25">Bill Status</td>
	<td bgcolor="#deede6" align="left" height="25"> 
		<select size="1" name="cbStatus">
			<option value="" <%=(statusType.equals("")?"selected=\"selected\"":"")%>>All</option>
			<option value="<%=BillConstant.STATUS_BILL_NORMAL %>" <%=(statusType.equals(""+BillConstant.STATUS_BILL_NORMAL)?"selected=\"selected\"":"")%>>Valid</option>
			<option value="<%=BillConstant.STATUS_BILL_ROLLBACKED%>" <%=(statusType.equals(""+BillConstant.STATUS_BILL_ROLLBACKED)?"selected=\"selected\"":"")%>>Roll Backed</option>	
		</select>
	</td>
</tr>
