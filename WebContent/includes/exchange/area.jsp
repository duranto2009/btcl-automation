<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,exchange.*,regiontype.*" %>
<%String exNWD = (String)session.getAttribute("exNWD");
if(exNWD == null)exNWD="-1";
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Area Code</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="exNWD">
  <option value="-1" <%=(exNWD.equals("-1")?"selected=\"selected\"":"")%>>All</option>
  <%
	  RegionService regionService = new RegionService();
	  ArrayList list = (ArrayList)regionService.getAllRegion();
	  for(int i = 0; i<list.size();i++)
	  {
		RegionDTO regionDTO = (RegionDTO)list.get(i);
		%>
       <option value="<%=regionDTO.getRegionDesc()%>" <%=(exNWD.equals(""+regionDTO.getRegionName())?"selected=\"selected\"":"")%>> <%=regionDTO.getRegionName()%> </option>
 <%
	  }
  %>
  
 
</select>
</td>
</tr>