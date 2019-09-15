<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,discount .*" %>
<%String discountStatus = (String)session.getAttribute("discountStatus");
if(discountStatus == null)discountStatus="-1";
%>
<tr >

<%-- <html:hidden property="discountStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Discount Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="discountStatus">
 <option value="-1" <%=(discountStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=DiscountConstants.DISCOUNT_STATE_ACTIVE%>" <%=(discountStatus.equals(""+DiscountConstants.DISCOUNT_STATE_ACTIVE)?"selected=\"selected\"":"")%>>Active</option> 
<option value="<%=DiscountConstants.DISCOUNT_STATE_DELETED%>" <%=(discountStatus.equals(""+DiscountConstants.DISCOUNT_STATE_DELETED)?"selected=\"selected\"":"")%>>Deleted</option> 

  
</select>
</td>
</tr>