<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="packages.PackageService"%>
<%@page import="report.ReportConfigurationDTO"%>
<%@page import="report.ReportOptions"%>
<%@ include file="../includes/checkLogin.jsp" %>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page errorPage="../common/failure.jsp" %>
<%@ page import="java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*" %>

<html>
 <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
  <script  src="../scripts/util.js" type="text/javascript"></script>
  <link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css">
    
 <script language="JavaScript">
function generateReport()
{
	var option=document.createElement("option");
	 option.text = "Login Time";
	 option.value = "Login Time";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option,null);
	 }
	 var option2=document.createElement("option");
	 option2.text = "Login Status";
	 option2.value = "Login Status";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option2,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option2,null);
	 }
	
	for (var i = 0; i< document.getElementsByName("columnsSelected")[0].options.length; i++)
	{
		document.getElementsByName("columnsSelected")[0].options[i].selected = true;
	}
	document.getElementsByName("clientPhnNo")[0].checked = true;
	document.getElementsByName("loginStatusFrom")[0].checked = true;
	document.getElementsByName("loginStatusTo")[0].checked = true;
}
function validate()
{
	var reportTitle = document.getElementById("reportTitle");  
    if(!validateRequired(reportTitle.value))
    {
      alert("Please Enter Report Title");
      reportTitle.value = "";
      reportTitle.focus();
      return false;
    }
	return true;
}
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Status</title>
<%SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

ArrayList<ClientDTO> clients = (ArrayList<ClientDTO>)ClientRepository.getInstance().getClientList();
%>
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
                      					<div class="div_title">Create Report on Login Status </div>
            						</td>
            					</tr>
								<tr>
									<td width="100%" align="center" style="padding-left: 140px"><br/>
									<html:form action="/SingleReport" method="POST" onsubmit = "return validate();">
										<html:select property = "columnsSelected" size="10" multiple = "true" style="display:none;width: 100%">
			                        								</html:select> 
			
											<table width="500" border="0" cellpadding="0" cellspacing="0" id = "AutoNumber3">
												<tr>
													<td align="left" height="25"><h4>Report Title  </h4></td>
													
                									<td align="left" height="25">
                									<input type= "text" name ="reportTitle" id="reportTitle"/>
                									</td>
            									</tr>
            									<tr>
            						
													<td align="left" height="25"><h4>Report Type  </h4></td>
            									 	<td align="left" height="25">
            									 	<input type = "text" name="infoSectionSelected" style="display:none" value="Login Status"/> 
            									 		Login Status
			                        				</td>
			                        			</tr>
			        
                    							<tr>
                    							<td align="left" height="25"><h4>Filters</h4></td>
                    							<td></td>
                    							</tr>
          
                    							<tr>
                    								<td align = "left" height="25"><input type="checkbox" name="clientPhnNo" style="display:none">
                    								Select Client ADSL <br/>Phone No 
                    								</td>
                    								<td align = "left" height="25">
                    								<html:select property = "clientPhnNoSelect" size="1">
                    								
                    								<% 
                    								GetClientDAO clientDAO = new GetClientDAO();
                    								
                    									for(int i = 0;i<clients.size();i++)
                    									{
                    								%>
                    									<html:option value="<%=Long.toString(clients.get(i).getPhoneNo()) %>" ><%=Long.toString(clients.get(i).getPhoneNo()) %></html:option>
                    								<%} %>
			                        				</html:select> 
                    								</td>
                    								<td>
                    								&nbsp;
                    								</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "loginStatusFrom" style="display:none">
                    							
                    							Start From 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="loginStatusFromSelect" value="<%=format.format(new Date()) %>"><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'loginStatusFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "loginStatusTo" style="display:none">
                    							
                    							To 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="loginStatusToSelect" value="<%=format.format(new Date()) %>"><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'loginStatusToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						
                    						<tr>

                    								<td align="left" height="25" colspan="2" style="padding-left: 100px">
                    								<input type="submit" name="submit" value = "Generate Report >" onclick = "generateReport()">
                    								</td>
                    							</tr>
                    						</table>
                    				</html:form>
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
