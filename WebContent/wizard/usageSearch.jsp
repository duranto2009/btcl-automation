<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="clientmodule.usagehistory.UsageHistory"%>
<%@page import="client.ClientRepository"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="recharge.RechargeClientDTO"%>
<%@page import="packages.PackageConstants"%>
<%@page import="packages.PackageDTO"%>
<%@ include file="../includes/checkLogin.jsp"%><%@ page language="java"%><%@ taglib
	uri="../WEB-INF/struts-bean.tld" prefix="bean"%><%@ taglib
	uri="../WEB-INF/struts-html.tld" prefix="html"%><%@ taglib
	uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="java.util.ArrayList,sessionmanager.SessionConstants,packages.*"%>

<html>
<head>
<title>Search Usage Info</title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">

<link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css">
 <script  src="../scripts/util.js" type="text/javascript"></script>
 
<script type="text/javascript"></script>
</head>

<body class="body_center_align">
	<table border="0" cellpadding="0" cellspacing="0" width="1024">
		<tr>
			<td width="100%"><%@ include file="../includes/header.jsp"%></td>
		</tr>

		<tr>
			<td width="100%">
				<table border="0" cellpadding="0" cellspacing="0" width="1024">
					<tr>
						
						<td width="1024" valign="top" class="td_main" align="center">
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td width="100%" align="right"
										style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
										<div class="div_title">Search Usage Info</div>
									</td>
								</tr>
								<tr>
									<td width="100%" align="center"><br />  
									<%
					                  String url = "ViewUsage";
					                  String navigator = SessionConstants.NAV_USAGE;
					                %> 
					                <jsp:include page="../includes/nav.jsp" flush="true">
											<jsp:param name="url" value="<%=url%>" />
											<jsp:param name="navigator" value="<%=navigator%>" />
										</jsp:include> 
											<table width="550" class="table_view">
												<tr>
													<td class="td_viewheader" align="center" valign="top" width="59" height="25">
                          <!--Customer ID-->
						Client Username</td>
                       
						 <td class="td_viewheader" align="center" valign="top" width="59" height="25">
                          <!--Name-->
						DSLM Port</td>
						 <td class="td_viewheader" align="center" valign="top" width="59" height="25">
                          <!--Name-->
						MAC Address</td>
						 <td class="td_viewheader" align="center" valign="top" width="59" height="25">
                          <!--Name-->
						IP Address</td>
						 <td class="td_viewheader" align="center" valign="top" width="59" height="25">
                          <!--Name-->
						Phone No</td>
						<td class="td_viewheader" align="center" valign="top" width="59" height="25">
                          <!--Name-->
						Connect Time</td>
						<td class="td_viewheader" align="center" valign="top" width="59" height="25">
                          <!--Name-->
						Disconnect Time</td>
						<td class="td_viewheader" align="center" valign="top" width="59" height="25">
                          <!--Name-->
						Upload (kB)</td>
						<td class="td_viewheader" align="center" valign="top" width="59" height="25">
                          <!--Name-->
						Download (kB)</td>								
												</tr>
												 <%
                      ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_USAGE);
                      if (data != null) {
                        int size = data.size();
                        System.out.println("size:"+size);
                        
                        for (int i = 0; i < size; i++)
						{
                          UsageHistory row = (UsageHistory) data.get(i);
                    %>
                      <tr align="center">
                        <td class="td_viewdata1" align="center" width="110" height="15"><%=ClientRepository.getInstance().getClient(row.getClientID()).getUserName() %> </td>
                        
                         <td class="td_viewdata1" align="center" width="110" height="15"><%=row.getDslmPort() %> </td>
                        <td class="td_viewdata2" align="center" width="170" height="15"><%=row.getMacAddress() %> </td>
                         <td class="td_viewdata1" align="center" width="110" height="15"><%=row.getIpAddress() %> </td>
                        <td class="td_viewdata2" align="center" width="170" height="15"><%=row.getPhoneNo() %> </td>
                         <td class="td_viewdata1" align="center" width="110" height="15"><%=row.getFrom() %> </td>
                        <td class="td_viewdata2" align="center" width="170" height="15"><%=row.getTo() %> </td>
                         <td class="td_viewdata1" align="center" width="110" height="15"><%=row.getOutBytes() %> </td>
                        <td class="td_viewdata2" align="center" width="170" height="15"><%=row.getInBytes() %> </td>
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

