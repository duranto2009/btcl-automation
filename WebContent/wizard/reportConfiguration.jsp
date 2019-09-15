<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="role.RoleService"%>
<%@page import="packages.PackageRepository"%>
<%@page import="user.UserRepository"%>
<%@page import="user.UserDTO"%>
<%@page import="exchange.ExchangeRepository"%>
<%@page import="exchange.ExchangeConstants"%>
<%@page import="regiontype.RegionRepository"%>
<%@page import="report.ReportConfigurationDTO"%>
<%@page import="report.ReportOptions"%>
<%@ include file="../includes/checkLogin.jsp" %>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page errorPage="../common/failure.jsp" %>
<%@ page import="java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*" %>

<%
ReportConfigurationDTO reportData = new ReportConfigurationDTO();
reportData = (ReportConfigurationDTO)session.getAttribute("reportConfig");
%>
<html>
 <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Report Configuration</title>

</head>
<body  class="body_center_align">
<center>
	<table border="0" cellpadding="0" cellspacing="0"  width="1024" id="AutoNumber1">
        <tr>
        	<td width="100%"><%@ include file="../includes/header.jsp"  %></td>
        </tr>
        <tr>
        	<td width="100%">
        		<table border="0" cellpadding="0" cellspacing="0"  width="1024" id="AutoNumber2">
         			 <tr>
         			 	
            			<td width="1024" valign="top" class="td_main" align="center">
                  			<table border="0" cellpadding="0" cellspacing="0" width="100%">
                    			<tr>
                    				<td width="1024" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
                      					<div class="div_title">Report Configuration</div>
            						</td>
            					</tr>
								<tr>
									<td width="100%" align="center" style="padding-left: 180px"><br/>

											<table width="500" border="0" cellpadding="0" cellspacing="0" id = "AutoNumber3">
												<tr>
													<td align="left" height="25"><h4>Report Title</h4></td>
													<td></td>
												</tr>
												<tr>
													<td></td>
                									<td align="left" height="25">
                									
                										<%=reportData.getReportTitle() %>
                									</td>
            									</tr>
            									<tr>
            						
													<td align="left" height="25"><h4>Report Type</h4></td>
													<td></td>
												</tr>
												<tr>
													<td></td>
            									 	<td align="left" height="25"> 
            									 		<%=reportData.getReportType()%>
			                        				</td>

			                        			</tr>
			                        			<tr>
			                        			<td align="left" height="25"><h4>Selected Fields</h4></td>
			                        			<td></td>
			                        			</tr>
			                        			
			                        			<tr>
			                        			<td></td>
	                        						<td align="left" height="25">
	                        							<table width="250" >
			            									 		<%
			            									 		String[] columnsSelectedArray = reportData.getReportColumnsSelected();
			            									 		System.out.println("columns size  "+columnsSelectedArray.length);
			            									 		for(int i = 0;i<columnsSelectedArray.length;i++)
			            									 		{
			            									 		%>
			            									 			<tr><td height="25">
			            									 			<%System.out.println("columns  "+columnsSelectedArray[i]); %>
			            									 			<%=columnsSelectedArray[i] %>
			            									 			</td></tr>
																	<%} %>
			                        					
			                        					</table>
	                        						</td>
                    							</tr>
                    							<%HashMap<String, String> constraints = reportData.getConstraintsSelected(); 
                    							
                    							if(constraints.size()>0)
                    							{%>
                    							<tr>
                    							
                    							<td align="left" height="25"><h4>Selected Filters</h4></td>
                    							<td></td>
                    							</tr>
                    							<tr>
                    							<td></td>
                    							<td align ="left" height="25">
                    							<table width = "250" align= "left" height="25">
                    							<%
                    							
                    							for(String key:constraints.keySet())
                    								{%>
                    								<tr>
                    									<td align="left" height="25"><%=key+" : " %></td>
                    									<%
                    									if(!constraints.get(key).equalsIgnoreCase("All"))
                    									{
                    										if(reportData.getReportType().equals("Customer Details") && key.equals("Region Name")){ %>
                    										
	                        							<td align="left" height="25">
	                        									<%=RegionRepository.getInstance().getRegionID(Long.parseLong(constraints.get(key))) %>
	                        								</td>
	                        								<%} 
	                        								else if(reportData.getReportType().equals("Customer Details") && key.equals("Type Of Connection")){ %>
	                        							<td align="left" height="25">
	                        									<%=ClientConstants.TYPEOFCONNECTION_NAME[Integer.parseInt(constraints.get(key))-1] %>
	                        								</td>
	                        								<%} 
	                        								else if(reportData.getReportType().equals("Customer Details") && key.equals("Package Name")){ %>
		                        							<td align="left" height="25">
		                        									<%=PackageRepository.getInstance().getPackage(Long.parseLong(constraints.get(key))).getPackageName() %>
		                        								</td>
		                        								<%}
	                        								else if(reportData.getReportType().equals("Customer Details") && key.equals("Operator Name")){ 
	                        								UserDTO operator = UserRepository.getInstance().getUserDTOByUserID(Long.parseLong(constraints.get(key)));
	                        								%>
		                        							<td align="left" height="25">
		                        									<%=operator.getUsername() %>
		                        								</td>
		                        								<%} 
		                        								
		                        							else if(reportData.getReportType().equals("Package Wise Customer Details") && key.equals("Package Name")){ %>
	                        							<td align="left" height="25">
	                        									<%=PackageRepository.getInstance().getPackage(Long.parseLong(constraints.get(key))).getPackageName() %>
	                        								</td>
	                        								<%}
		                        							else if(reportData.getReportType().equals("Region Wise") && key.equals("Region Name")){ 
		                        								//UserDTO operator = UserRepository.getInstance().getUserDTOByUserID(Long.parseLong(constraints.get(key)));
		                        								%>
			                        							<td align="left" height="25">
			                        									<%=RegionRepository.getInstance().getRegionID(Long.parseLong(constraints.get(key))) %>
			                        								</td>
			                        								<%} 
		                        							else if(reportData.getReportType().equals("Type of Connectivity Services") && key.equals("Type Of Connection")){ %>
		                        							<td align="left" height="25">
		                        									<%=ClientConstants.TYPEOFCONNECTION_NAME[Integer.parseInt(constraints.get(key))-1] %>
		                        								</td>
		                        								<%}
		                        							else if(reportData.getReportType().equals("System Admin") && key.equals("Role")){ 
		                        							RoleService rService = util.ServiceDAOFactory.getService(RoleService.class);%>
		                        							<td align="left" height="25">
		                        									<%=rService.getRole(constraints.get(key)).getRoleName() %>
		                        								</td>
		                        								<%}
		                        							else if(reportData.getReportType().equals("System Admin") && key.equals("Admin Name")){ 
			                        							%>
			                        							<td align="left" height="25">
			                        									<%=UserRepository.getInstance().getUserDTOByUserID(Long.parseLong(constraints.get(key))).getUsername() %>
			                        								</td>
			                        								<%}
		                        							else{ 
			                        							if(constraints.get(key).contains(":")){
			                        								%>
			                        								<td align="left" height="25">
			                        									<table>
				                        								<%
				                        								String[] constraintValues = constraints.get(key).split(":");
				                        								if(key.equals("Exchange Code"))
				                        								{
					                        								for(int i = 0; i<constraintValues.length; i++)
					                        								{%>
					                        									<tr>
					                        										<td align="left" height="25">
					                        									
					                        										<%=ExchangeRepository.getInstance().getExchange(Integer.parseInt(constraintValues[i])).getExName() %>
					                        										</td>
					                        									</tr>
					                        								<%}	
					                        							}
				                        								else if(key.equals("Package Name"))
				                        								{
					                        								for(int i = 0; i<constraintValues.length; i++)
					                        								{%>
					                        									<tr>
					                        										<td align="left" height="25">
					                        									
					                        										<%=PackageRepository.getInstance().getPackage(Long.parseLong(constraintValues[i])).getPackageName() %>
					                        										</td>
					                        									</tr>
					                        								<%}	
					                        							}
					                        							%>
			                        									</table>
			                        								</td>
			                        							<%}
			                        							else
			                        							{
			                        									if(key.equals("Exchange Code"))
				                        								{%>
				                        									<td align="left" height="25">
			                        										<%=ExchangeRepository.getInstance().getExchange(Integer.parseInt(constraints.get(key))).getExName() %>
			                        										</td>
			                        									<%}
			                        									
			                        									else if(key.equals("Package Name"))
				                        								{%>
				                        									<td align="left" height="25">
			                        										<%=PackageRepository.getInstance().getPackage(Long.parseLong(constraints.get(key))).getPackageName() %>
			                        										</td>
			                        									<%}
			                        									else
			                        									{%>
					                        								<td align="left" height="25">
					                        									<%= constraints.get(key) %>
					                        								</td>
					                        								<%
			                        									}
			                        							}
			                        						}
                       									}
                       									else
                       									{%>
	                        								<td align="left" height="25">
	                        									<%= constraints.get(key) %>
	                        								</td>
	                        								<%
                       									}%>
	                        						</tr>
	                        						<%} %>
	                        						
	                        						</table>
	                        						</td>
                    							</tr>
                    							<%} %>
                    							<tr>
                    							<td height="25"><%="    " %></td>
                    							<td height="25"><%="  " %></td>
                    							</tr>
                    							<tr><td><a href="../SingleReport.do?id=<%=reportData.getReportConfigID()%>">Generate Report</a></td></tr>
                    						<tr><td>&nbsp;</td></tr>
                    						</table>
                    				
   	 								</td>
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
   	 </center>
</body>
</html>
