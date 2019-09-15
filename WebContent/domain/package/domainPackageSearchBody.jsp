<%@page import="org.apache.log4j.Logger"%>
<%@page import="domain.DomainPackageTypeDTO"%>
<%@page import="java.util.HashMap"%>
<%@page import="domain.DomainService"%>
<%@page import="util.TimeConverter"%>
<%@page import="domain.DomainPackage"%>
<%@page import="domain.DomainDTO"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants"%>
<%
	String msg = null;
	String url = "SearchDomainPackage";
	String navigator = SessionConstants.NAV_DOMAIN_PACKAGE;
	String context = "../../.." + request.getContextPath() + "/";
%>
<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>

<%
DomainService domainService = new DomainService();
HashMap<Long,DomainPackageTypeDTO> domanPackageTypeMap = domainService.getDomainPackageTypeMap();
Logger loggerDomainPack = Logger.getLogger("domainPackage");
loggerDomainPack.debug("domanPackageTypeMap " + domanPackageTypeMap);
%>
<%try{ %>
<div class="portlet box">
	<div class="portlet-body">
		<html:form action="/SearchDomainPackage" method="POST">
			<div class="table-responsive">
				<table id="example1" class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>Package Name</th>
							<th>Package Type</th>
							<th>Activation Date</th>
						</tr>
					</thead>
					<tbody>
						<%
							ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_DOMAIN_PACKAGE);

								if (data != null) {
									int size = data.size();
									for (int i = 0; i < size; i++) {
										DomainPackage row = (DomainPackage) data.get(i);
										loggerDomainPack.debug("row " + row);
						%>
						<tr>
							<td><a href="<%=context%>domain/package/updateDomainPackage.jsp?id=<%=row.getID() %>"><%=row.getPackageName() %></a></td>
							<td><%=domanPackageTypeMap.get(row.getPackageTypeID()).getPackageName() %></td><!-- package type name -->
							<td><%=TimeConverter.getTimeStringFromLong( row.getActivationDate()) %></td>
							<%
								}
							%>
						</tr>
						<%
							}
						%>
					</tbody>
				</table>

			</div>
		</html:form>
	</div>
</div>
<%}catch(Exception ex){
	loggerDomainPack.debug("Exception", ex);
}
%>