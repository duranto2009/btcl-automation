<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*,dslm.*" %>
<%String clDslmNo = (String)session.getAttribute("clDslmNo");
if(clDslmNo == null)clDslmNo="-1";
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Dslm Name</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="clDslmNo">
  <option value="-1" <%=(clDslmNo.equals("-1")?"selected=\"selected\"":"")%>>All</option>
  <%
	  DslmService dslmService = new DslmService();
	  ArrayList list = (ArrayList)dslmService.getIDs(loginDTO);
	  for(int i = 0; i<list.size();i++)
	  {
		DslmDTO dslmDTO = dslmService.getDslm(""+list.get(i));
		%>
       <option value="<%=dslmDTO.getDslmName()%>" <%=(clDslmNo.equals(""+dslmDTO.getDslmName())?"selected=\"selected\"":"")%>> <%=dslmDTO.getDslmName()%> </option>
 <%
	  }
  %>
  
 
</select>
</td>
</tr>