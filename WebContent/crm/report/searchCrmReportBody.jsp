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
	String url = "CrmReportSearch/Reports";
	String navigator = SessionConstants.NAV_CRM_REPORT;
	String context = "../../.." + request.getContextPath() + "/";
	Logger searchLogger = Logger.getLogger(getClass());
	LoginDTO localLoginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	List<CrmEmployeeDTO> rootCrmEmployeeDTOList = CrmAllEmployeeRepository.getInstance().getRootEmployeeList();
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
%>

<div class="portlet box">
	<div class="portlet-body">

	</div>
</div>
<%
	} catch (Exception ex) {
		searchLogger.debug("Exception ", ex);
	}
%>




