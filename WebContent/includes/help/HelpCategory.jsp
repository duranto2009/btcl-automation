<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,help.*,complain.*" %>
<%String hrCategoryID = (String)session.getAttribute("hrCategoryID");
if(hrCategoryID == null)hrCategoryID="-1";
%>

<%-- <html:hidden property="hrCategoryID" value ="1"/> --%>

<div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Complain Category</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
<select class="form-control" size="1" name="hrCategoryID">
<option value="-1" <%=(hrCategoryID.equals("-1")?"selected=\"selected\"":"")%>>All</option>
<%ArrayList<ComplainDTO> complains = (new ComplainService()).getComplains();
for(int i = 0; i<complains.size(); i++)
{
%>
 

<option value="<%=complains.get(i).getComplainID()%>" <%=(hrCategoryID.equals(""+complains.get(i).getComplainID())?"selected=\"selected\"":"")%>><%=complains.get(i).getComplainType()%></option> 
<%} %>
  
</select>
</div>
</div>