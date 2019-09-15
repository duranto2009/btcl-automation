<%@page import="payOrder.PayOrderDTO"%>
<%@page import="common.StringUtils"%>
<%@page import="bank.BankDTO"%>
<%@page import="common.ModuleConstants"%>
<%@page import="crm.CrmCommonPoolDTO"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.util.List"%>
<%@page import="crm.repository.CrmAllEmployeeRepository"%>
<%@page import="crm.CrmLogic"%>
<%@page import="crm.inventory.CRMInventoryItem"%>
<%@page import="crm.repository.CrmInventoryItemRepository"%>
<%@page import="crm.CrmEmployeeDTO"%>
<%@page import="crm.repository.CrmEmployeeRepository"%>
<%@page import="crm.CrmComplainDTO"%>
<%@page import="login.LoginDTO"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.CommonDAO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="domain.DomainDTO"%>
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
	String url = "PayOrderSearch/PayOrders";
	String navigator = SessionConstants.NAV_PAY_ORDER;
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
         <p> PayOrder deleted successfully </p>
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
							<th class="text-center">Draft ID</th>
							<th class="text-center">Total Amount</th>
							<th class="text-center">Remaining Amount</th>
							<th class="text-center">Draft Type</th>
							<th class="text-center">Draft Number</th>
							<th class="text-center"></th>
						</tr>
					</thead>
					<tbody>
						<%
							ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_PAY_ORDER);

								if (data != null) {
									int size = data.size();
									for (int i = 0; i < size; i++) {
										try {
											PayOrderDTO row = (PayOrderDTO) data.get(i);
						%>
						<tr>

							<td class="text-center"><a
								href="<%=context%>PayOrder/GetPayOrder.do?payOrderID=<%=row.getID()%>"><%=row.getID() %>
							</a></td>
							<td class="text-center"><%=row.getPayDraftTotalAmount() %></td>
							<td class="text-center"><%=row.getPayDraftRemainingAmount() %></td>
							<td class="text-center"><%=PayOrderDTO.MapOfDraftTypeToDraftTypeID.get(row.getPayDraftType()) %></td>
							<td class="text-center"><%=row.getPayDraftNumber() %></td>
							<td class="text-center"><a
								href="<%=context%>PayOrder/DeletePayOrder.do?payOrderID=<%=row.getID()%>">Delete
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
							session.removeAttribute(SessionConstants.VIEW_PAY_ORDER);
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


