
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*,packages.*" %>
<%String bcurrentPackage = (String)session.getAttribute("bcurrentPackage");
if(bcurrentPackage == null)bcurrentPackage="-1";
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Previous Package</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="bcurrentPackage">
  <option value="-1" <%=(bcurrentPackage.equals("-1")?"selected=\"selected\"":"")%>>All</option>
  <%
	  PackageService packageService = new PackageService();
	  ArrayList list = (ArrayList)packageService.getDTOs(packageService.getIDs(loginDTO));
	  for(int i = 0; i<list.size();i++)
	  {
		PackageDTO packageDTO = (PackageDTO)list.get(i);
		if(packageDTO.getPackageType() != ClientConstants.TYPEOFCONNECTION_VALUE[ClientConstants.TYPEOFCONNECTION_VALUE.length-1])
		{
		%>
       <option value="<%=packageDTO.getPackageID()%>" <%=(bcurrentPackage.equals(""+packageDTO.getPackageID())?"selected=\"selected\"":"")%>> <%=packageDTO.getPackageName()%> </option>
 <%
		}
		}
  %>
  
 
</select>
</td>
</tr>