<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,exchange.*"%>
<%String exStatus = (String)session.getAttribute("exStatus");
if(exStatus == null)exStatus="-1";
%>
<tr >

<%-- <html:hidden property="exStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="exStatus">
 <option value="-1" <%=(exStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=ExchangeConstants.EXCHANGE_ACTIVE%>" <%=(exStatus.equals(""+ExchangeConstants.EXCHANGE_ACTIVE)?"selected=\"selected\"":"")%>><%=ExchangeConstants.ACTIVE %></option> 
<option value="<%=ExchangeConstants.EXCHANGE_DELETED%>" <%=(exStatus.equals(""+ExchangeConstants.EXCHANGE_DELETED)?"selected=\"selected\"":"")%>><%=ExchangeConstants.DELETED %></option>

  
</select>
</td>
</tr>