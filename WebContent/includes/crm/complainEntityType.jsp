
<%@page import="java.util.Map.Entry"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.StringUtils"%>
<%@page import="crm.CrmComplainDTO"%>
<%@ page language="java"%><%@ taglib uri="../../WEB-INF/struts-bean.tld"
	prefix="bean"%><%@ taglib uri="../../WEB-INF/struts-html.tld"
	prefix="html"%><%@ taglib uri="../../WEB-INF/struts-logic.tld"
	prefix="logic"%>
<%@ page
	import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = StringUtils.trim((String) session.getAttribute("entityTypeID"));
	/* if(orderTime == null)orderTime=format.format(System.currentTimeMillis()); */
	if (requested == null) {
		requested = "";
	}
	HashMap<Integer, String> modules = EntityTypeConstant.mapOfModuleNameToMainEntityTypeIdForCrm;
	request.setAttribute("modules", modules);
	//int status = Integer.parseInt(requested);
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Service Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<div class="input-group " style="width: 100%;">
			<select class="form-control pull-right" name="entityTypeID">
				<option value="" <%if (requested.equals("")) {%> selected <%}%>>All</option>
				<%
				for(Entry<Integer, String> entity : modules.entrySet()){
					Integer entityTypeID = entity.getKey(); 
				%>
					<option value='<%=entityTypeID %>' 
						<%if(!requested.equals("") && entityTypeID == Integer.parseInt(requested)) {%> selected <%}%> >
							<%=entity.getValue()%>
					</option>
				<%} %>
			</select>
		</div>
	</div>
</div>
