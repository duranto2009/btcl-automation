<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="report.FileWalker"%>
<%@page import="java.io.File"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@ include file="../includes/checkLogin.jsp" %>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page errorPage="../common/failure.jsp" %>
<%@ page import="java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*" %>
<html>
 <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
 <link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css">
  <script  src="../scripts/util.js" type="text/javascript"></script>
    
 <script language="JavaScript">



</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Download Daily Reports</title>
<%SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy"); 


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
                      					<div class="div_title">Download Daily Reports</div>
            						</td>
            					</tr>
								<tr>
									<td width="100%" align="center" style="padding-left: 180px"><br/>
									<html:form action="/Download" method="POST" onsubmit = "return validate();">
									
			
											<table width="500" border="0" cellpadding="0" cellspacing="0" id = "AutoNumber3">
												 <%
                                                        String msg = null;
                                                        if ((msg = (String) session.getAttribute("cdrDownloadedSuccessfully")) != null) {
                                                            session.removeAttribute("cdrDownloadedSuccessfully");
                                                    %>
                                                    <tr>
                                                        <td colspan="2" align="left" valign="top" height="50"><b><%=msg%></b></td>
                                                    </tr>
                                                    <%}%>
                                                    
                                                   <tr>
                    							<td align="left" height="25">
                    							
                    							
                    							Start Time
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="startFromCdr" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'downloadForm',
                    								'controlname' : 'startFromCdr'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							
                    							
                    							End Time
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="startToCdr" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'downloadForm',
                    								'controlname' : 'startToCdr'
                    							});</script>
                    							</td>
                    						</tr>
                    						                    						
                    						
                    				<tr>
                    							
                    								<td align="left" height="25" colspan="2" style="padding-left: 80px">
                    								<input type="submit" name="downloadReports" value = "Download"/>
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
