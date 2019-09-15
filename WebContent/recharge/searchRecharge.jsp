<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="client.ClientRepository"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="recharge.RechargeClientDTO"%>
<%@page import="packages.PackageConstants"%>
<%@page import="packages.PackageDTO"%>
<%@page errorPage="../common/failure.jsp" %>
<%@ include file="../includes/checkLogin.jsp"%><%@ page language="java"%><%@ taglib
	uri="../WEB-INF/struts-bean.tld" prefix="bean"%><%@ taglib
	uri="../WEB-INF/struts-html.tld" prefix="html"%><%@ taglib
	uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="java.util.ArrayList,sessionmanager.SessionConstants,packages.*"%>

<html>
<head>
<title>Search Recharge Info</title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">

<link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css">
 <script  src="../scripts/util.js" type="text/javascript"></script>
 
<script type="text/javascript"></script>
</head>

<body class="body_center_align" >
	<table border="0" cellpadding="0" cellspacing="0" width="780">
		<tr>
			<td width="100%"><%@ include file="../includes/header.jsp"%></td>
		</tr>

		<tr>
			<td width="100%">
				<table border="0" cellpadding="0" cellspacing="0" width="780">
					<tr>
						
						<td width="600" valign="top" class="td_main" align="center">
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td width="100%" align="right"
										style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
										<div class="div_title">Search Recharge Info</div>
									</td>
								</tr>
								<tr>
									<td width="100%" align="center"><br /> <%
									 	String url = "viewRechargeInfo";
									 	String navigator = SessionConstants.NAV_RECHARGE_INFO;
									 %> <jsp:include page="../includes/nav.jsp" flush="true">
											<jsp:param name="url" value="<%=url%>" />
											<jsp:param name="navigator" value="<%=navigator%>" />
										</jsp:include> 
											<table width="550" class="table_view">
												<tr>
													<td class="td_viewheader" align="center" width="172">ADSL Phone No</td>
													<td class="td_viewheader" align="center" width="138">Recharge Amount</td>
													<td class="td_viewheader" align="center" width="138">Invoice/Bill No</td>
													<td class="td_viewheader" align="center" width="138">Recharge Date</td>
													<td class="td_viewheader" align="center" width="138">Comment</td>	
													<td class="td_viewheader" align="center" width="138">Print</td>													
												</tr>
												<%
												ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_RECHARGE_INFO);
												SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
												if (data != null) {
													ClientRepository cr= ClientRepository.getInstance();
													int size = data.size();
													for (int i = 0; i < size; i++) {
														RechargeClientDTO row = (RechargeClientDTO) data.get(i);
												%>
												<tr align="center">
													<td class="td_viewdata1"><%=cr.getClient(row.getClientID()).getPhoneNo()%></td>
													<td class="td_viewdata2"><%=row.getRechargeAmount()%></td>
													<td class="td_viewdata1"><%=row.getVoucherNo()%></td>
													<td class="td_viewdata2"><%=format.format(new Date(row.getRechargeDate()))%></td>
													<td class="td_viewdata1"><%=row.getComment()%></td>
													<td class="td_viewdata2" align="center" width="170" height="15"><a target="_blank" href="../recharge/PrintableRechargeVoucher.jsp?id=<%=row.getRechargeID() %>">Print</a></td>
                      
												</tr>
												<%
													}
												}
												%>
											</table>
										 <br /></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td width="100%"><%@ include file="../includes/footer.jsp"%></td>
		</tr>
	</table>
</body>
</html>
