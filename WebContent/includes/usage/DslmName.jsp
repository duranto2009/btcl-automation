<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*,dslm.*" %>
<%String clDslmNo = (String)session.getAttribute("cdrDslmName");
if(clDslmNo == null)clDslmNo="";
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Dslm Name</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="cdrDslmName">
  <option value="" <%=(clDslmNo.equals("")?"selected=\"selected\"":"")%>>All</option>
  <%
	  
	  ArrayList list = DslmRepository.getInstance().getDslmList();
	  for(int i = 0; i<list.size();i++)
	  {
		DslmDTO dslmDTO = (DslmDTO)list.get(i);
		%>
       <option value="<%=dslmDTO.getDslmName()%>" <%=(clDslmNo.equals(""+dslmDTO.getDslmName())?"selected=\"selected\"":"")%>> <%=dslmDTO.getDslmName()%> </option>
 <%
	  }
  %>
  
 
</select>
</td>
</tr>