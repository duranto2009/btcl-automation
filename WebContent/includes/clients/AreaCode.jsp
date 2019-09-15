<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*,regiontype.*" %>
<%String valueAreaCode = (String)session.getAttribute("clAreaCode");
if(valueAreaCode == null)valueAreaCode="-1";
%>
<div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Area Code</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
<select class="form-control" size="1" name="clAreaCode">
  <option value="-1" <%=(valueAreaCode.equals("-1")?"selected=\"selected\"":"")%>>All</option>
  <%
	  RegionService regionService = new RegionService();
	  ArrayList list = (ArrayList)regionService.getAllRegion();
	  for(int i = 0; i<list.size();i++)
	  {
		RegionDTO regionDTO = (RegionDTO)list.get(i);
		%>
       <option value="<%=regionDTO.getRegionDesc()%>" <%=(valueAreaCode.equals(""+regionDTO.getRegionName())?"selected=\"selected\"":"")%>> <%=regionDTO.getRegionName()%> </option>
 <%
	  }
  %>
  
 
</select>
</div>
</div>