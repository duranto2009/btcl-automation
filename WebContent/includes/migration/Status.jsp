<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,migration.*" %>
<%String migrationStatus = (String)session.getAttribute("migrationStatus");
if(migrationStatus == null)migrationStatus="-1";
%>
<tr >

<%-- <html:hidden property="migrationStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Migration Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="migrationStatus">
 <option value="-1" <%=(migrationStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=MigrationConstants.MIGRATION_ACTIVE%>" <%=(migrationStatus.equals(""+MigrationConstants.MIGRATION_ACTIVE)?"selected=\"selected\"":"")%>>Active</option> 
<option value="<%=MigrationConstants.MIGRATION_DELETED%>" <%=(migrationStatus.equals(""+MigrationConstants.MIGRATION_DELETED)?"selected=\"selected\"":"")%>>Deleted</option> 

  
</select>
</td>
</tr>