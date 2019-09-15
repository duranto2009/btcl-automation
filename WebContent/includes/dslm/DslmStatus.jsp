<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,dslm.*"%>
<%String dslmStatus = (String)session.getAttribute("dslmStatus");
if(dslmStatus == null)dslmStatus="-1";
%>
<tr >

<%-- <html:hidden property="dslmStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="dslmStatus">
 <option value="-1" <%=(dslmStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=DslmConstants.DSLM_ACTIVE%>" <%=(dslmStatus.equals(""+DslmConstants.DSLM_ACTIVE)?"selected=\"selected\"":"")%>><%=DslmConstants.ACTIVE %></option> 
<option value="<%=DslmConstants.DSLM_DELETED%>" <%=(dslmStatus.equals(""+DslmConstants.DSLM_DELETED)?"selected=\"selected\"":"")%>><%=DslmConstants.DELETED %></option>

  
</select>
</td>
</tr>