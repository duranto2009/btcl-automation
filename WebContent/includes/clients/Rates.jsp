
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*,packages.*" %>
<%String valuePackageID = (String)session.getAttribute("clRateID");
if(valuePackageID == null)valuePackageID="-1";
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Package</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="clRateID">
  <option value="-1" <%=(valuePackageID.equals("-1")?"selected=\"selected\"":"")%>>All</option>
  <%
	  PackageService packageService = new PackageService();
	  ArrayList list = (ArrayList)packageService.getDTOs(packageService.getIDs(loginDTO));
	  for(int i = 0; i<list.size();i++)
	  {
		PackageDTO packageDTO = (PackageDTO)list.get(i);
		%>
       <option value="<%=packageDTO.getPackageID()%>" <%=(valuePackageID.equals(""+packageDTO.getPackageID())?"selected=\"selected\"":"")%>> <%=packageDTO.getPackageName()%> </option>
 <%
	  }
  %>
  
 
</select>
</td>
</tr>