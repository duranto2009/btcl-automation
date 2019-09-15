<%@ include file="../checkLogin.jsp" %><%@ page language="java" %>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,user.*,login.*,role.*" %>
<%
	String status = (String)session.getAttribute("usStatus");
	if(status == null)status="-1";
%>
<div class="form-group">
	<label class="control-label col-md-4 col-sm-4">Status</label>
	<div class="col-md-8 col-sm-8">
		<select class="form-control" name="usStatus">
 			<option value="" <%=(status.equals("-1")?"selected=\"selected\"":"")%>>All</option> 
            <option value="<%=UserDTO.STATUS_ACTIVE%>" <%=(status.equals(""+UserDTO.STATUS_ACTIVE)?"selected=\"selected\"":"")%> >
            	Active
            </option>
 			<option value="<%=UserDTO.STATUS_BLOCKED%>" <%=(status.equals(""+UserDTO.STATUS_BLOCKED)?"selected=\"selected\"":"")%> >
 				Blocked
 			</option>
		</select>
	</div>		
</div>
