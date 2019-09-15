<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@page import="domain.DomainPackageTypeDTO"%>
<%@page import="domain.DomainService"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.bill.BillDTO"%>
<%@ page language="java"%><%@ taglib uri="../../WEB-INF/struts-bean.tld"
	prefix="bean"%><%@ taglib uri="../../WEB-INF/struts-html.tld"
	prefix="html"%><%@ taglib uri="../../WEB-INF/struts-logic.tld"
	prefix="logic"%>
<%@ page
	import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = (String) session.getAttribute("packageTypeID");
	DomainService domainService = new DomainService();
	List<DomainPackageTypeDTO> domainPackageTypeDTOs = domainService.getDomainPackageTypes();

	if (requested == null) {
		requested = "";
	}
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Domain Package Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control pull-right" name="packageTypeID" >
			<option value="" <%if (requested.equals("") ){%> selected <%}%>>All</option>
			<% for(DomainPackageTypeDTO domainPackageTypeDTO : domainPackageTypeDTOs){ %>
				<option value="<%=domainPackageTypeDTO.getID() %>" <%if (requested.equals(Long.toString(domainPackageTypeDTO.getID()))) {%>selected <%}%>><%=domainPackageTypeDTO.getPackageName() %></option>
			<%} %>
		</select>
	</div>
</div>