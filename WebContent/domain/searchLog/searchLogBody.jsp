<%@page import="common.CommonDAO"%>
<%@page import="domain.DomainDTO"%>
<%@page import="domain.constants.DomainStateConstants"%>
<%@page import="util.TimeFormat"%>
<%@page import="domain.search.log.DomainSearchLogDTO"%>
<%@page import="common.StringUtils"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.util.List"%>
<%@page import="login.LoginDTO"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="util.TimeConverter"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants"%>

<style type="text/css">
button.dt-button, div.dt-button, a.dt-button {
	color: white !important;
	border-color: white !important;
}
</style>
<%
	String msg = null;
	String url = "DomainSearchLog/SearchLog";
	String navigator = SessionConstants.NAV_DOMAIN_SEARCH_LOG ;
	String context = "../../.." + request.getContextPath() + "/";
	Logger searchLogger = Logger.getLogger(getClass());
	LoginDTO localLoginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>

<%
	try {
	String message = (String)request.getAttribute("msg");
%>


<div class="portlet box">
	<div class="portlet-body">
			<div class="table">
				<table id="tableId"
					class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th class="text-center">ID</th>
							<th class="text-center">Domain Name</th>
							<th class="text-center">IP</th>
							<th class="text-center">Status</th>
							<th class="text-center">Date Time</th>
						</tr>
					</thead>
					<tbody>
						<%
							CommonDAO commonDAO = new CommonDAO();
							ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_DOMAIN_SEARCH_LOG );

								if (data != null) {
									int size = data.size();
									for (int i = 0; i < size; i++) {
										try {
											DomainSearchLogDTO row = (DomainSearchLogDTO) data.get(i);
						%>
						<tr>

							<td class="text-center"><%=row.getID() %></td>
							<td class="text-center"><%=row.getDomainName() %></td>
							<td class="text-center"><%=row.getIp() %></td>
							<td class="text-center"><%=row.isActive() ? "Avaiable" : "Unavailable" %></td>
							<td class="text-center"><%=TimeConverter.getTimeStringByDateFormat(row.getLastModificationTime(), "dd MMM yyyy hh:mm a")  %></td>
							<%
								} catch (Exception ex) {
												ex.printStackTrace();
											}
										}
							%>
						</tr>
						<%
							}
							session.removeAttribute(SessionConstants.VIEW_DOMAIN_SEARCH_LOG);
						%>
					</tbody>
				</table>
			</div>
	</div>
</div>
<%
	} catch (Exception ex) {
		searchLogger.debug("Exception ", ex);
	}
%>




