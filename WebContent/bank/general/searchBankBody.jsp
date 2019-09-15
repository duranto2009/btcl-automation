<%@page import="bank.BankDTO"%>
<%@page import="common.StringUtils"%>

<%@page import="login.LoginDTO"%>

<%@page import="org.apache.log4j.Logger"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<style type="text/css">
	button.dt-button, div.dt-button, a.dt-button {
		color: white !important;
		border-color: white !important;
	}
</style>
<%
	String msg = null;
	String url = "BankSearch/Banks";
	String navigator = SessionConstants.NAV_BANK;
	String context = "../../.." + request.getContextPath() + "/";
	Logger searchLogger = Logger.getLogger(getClass());
	LoginDTO localLoginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<link
	href="<%=context%>/assets/global/plugins/datatables/datatables.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=context%>/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<link href="<%=context%>/domain/domainQueryForBuy/domain-query-buy.css"
	rel="stylesheet" type="text/css" />
<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>

<%
	try {
	String message = (String)request.getAttribute("msg");
%>
<%if(StringUtils.isNotBlank(message) || message !=null){ %>
	 <div class="note note-success">
         <h4 class="block">Success! </h4>
         <p> Bank deleted successfully </p>
     </div>
     <%}else{
    	 message="";
     } %>

<div class="portlet box">
	<div class="portlet-body">
			<div class="table">
				<table id="tableId"
					class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th class="text-center">Bank Name</th>
							<th class="text-center">Bank Code</th>
							<th class="text-center"></th>
						</tr>
					</thead>
					<tbody>
						<%
							ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_BANK);

								if (data != null) {
									int size = data.size();
									for (int i = 0; i < size; i++) {
										try {
											BankDTO row = (BankDTO) data.get(i);
						%>
						<tr>

							<td class="text-center"><a
								href="<%=context%>Bank/GetBank.do?bankID=<%=row.getID()%>"><%=row.getBankName() %>
							</a></td>
							<td class="text-center"><%=row.getPaclBankCode() %></td>
							<td class="text-center"><a
								href="<%=context%>Bank/DeleteBank.do?bankID=<%=row.getID()%>">Delete
							</a></td>
							<%
								} catch (Exception ex) {
												ex.printStackTrace();
											}
										}
							%>
						</tr>
						<%
							}
							session.removeAttribute(SessionConstants.VIEW_BANK);
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


