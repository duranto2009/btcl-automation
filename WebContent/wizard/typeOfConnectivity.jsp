<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="packages.PackageService"%>
<%@page import="report.ReportConfigurationDTO"%>

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
 function checkPair(id1,id2)
 {
 	if(id1.checked == true)
 	{
 		id2.checked = true;	
 	}
 	else
 	{
 		id2.checked = false;
 	}
 }
function generateReport()
{
	var option=document.createElement("option");
	 option.text = "Type Of Connection";
	 option.value = "Type Of Connection";
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
	 option2.text = "No. Of Customers";
	 option2.value = "No. Of Customers";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option2,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option2,null);
	 }
	 /*var option3=document.createElement("option");
	 option3.text = "Online Registered";
	 option3.value = "Online Registered";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option3,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option3,null);
	 }
	 var option4=document.createElement("option");
	 option4.text = "System Registered";
	 option4.value = "System Registered";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option4,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option4,null);
	 }
	 var option5=document.createElement("option");
	 option5.text = "Renewed With Discount";
	 option5.value = "Renewed With Discount";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option5,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option5,null);
	 }
	 var option6=document.createElement("option");
	 option6.text = "Renewed Without Discount";
	 option6.value = "Renewed Without Discount";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option6,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option6,null);
	 }
	 var option7=document.createElement("option");
	 option7.text = "Bonus Granted";
	 option7.value = "Bonus Granted";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option7,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option7,null);
	 }*/
	 var option8=document.createElement("option");
	 option8.text = "New Customer";
	 option8.value = "New Customer";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option8,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option8,null);
	 }
	/* var option9=document.createElement("option");
	 option9.text = "Renewed Customer";
	 option9.value = "Renewed Customer";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option9,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option9,null);
	 }
	*/
	for (var i = 0; i< 3; i++)
	{
		document.getElementsByName("columnsSelected")[0].options[i].selected = true;
	}
	/*document.getElementsByName("typesOfConnection")[0].checked = true;
	document.getElementsByName("typesOfConnectionFrom")[0].checked = true;
	document.getElementsByName("typesOfConnectionTo")[0].checked = true;*/
}
function validate()
{
	var reportTitle = document.getElementById("reportTitle");  
	var startDate = document.getElementById("typesOfConnectionFromSelect");
	var endDate = document.getElementById("typesOfConnectionToSelect");
    
    if(!validateRequired(reportTitle.value))
    {
      alert("Please Enter Report Title");
      reportTitle.value = "";
      reportTitle.focus();
      return false;
    }
    if(!validateRequired(startDate.value))
	{
		alert("Please select a starting date");
		return false;
	}
    if(!validateRequired(endDate.value))
	{
		alert("Please select an ending date");
		return false;
	}
	return true;
}
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Type Of Connectivity</title>
<%SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy"); %>
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
                      					<div class="div_title">Create Report on Type Of Connection Services </div>
            						</td>
            					</tr>
								<tr>
									<td width="100%" align="center" style="padding-left: 100px"><br/>
									<html:form action="/SingleReport" method="POST" onsubmit = "return validate();">
										<html:select property = "columnsSelected" size="10" multiple = "true" style="display:none;width: 100%">
			                        								</html:select> 
			
											<table width="450" border="0" cellpadding="0" cellspacing="0" id = "AutoNumber3">
												<tr>
													<td align="left" height="25"><h4>Report Title  </h4></td>
													
                									<td align="left" height="25">
                									<input type= "text" name ="reportTitle" id="reportTitle"/>
                									</td>
            									</tr>
            									<tr>
            						
													<td align="left" height="25"><h4>Report Type  </h4></td>
            									 	<td align="left" height="25">
            									 	<input type = "text" name="infoSectionSelected" style="display:none" value="Type of Connectivity Services"/> 
            									 		Type Of Connectivity Services
			                        				</td>
			                        			</tr>
			        
                    							<tr>
                    							<td align="left" height="25"><h4>Filters</h4></td>
                    							<td></td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "typesOfConnection" />
                    							Type Of Connection 
                    							</td>
                    								<td align = "left" height="25">
                    								<html:select property = "typesOfConnectionSelect" size="1">
                    								<html:option value="All" >All</html:option>
                    								<%for(int i = 0;i<ClientConstants.TYPEOFCONNECTION_VALUE.length;i++)
                    									{%>
                    									<html:option value="<%=Integer.toString(ClientConstants.TYPEOFCONNECTION_VALUE[i])%>" ><%=ClientConstants.TYPEOFCONNECTION_NAME[i] %></html:option>
			                        				<%} %>
			                        				</html:select> 
                    								</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "typesOfConnectionFrom" onclick = "checkPair(typesOfConnectionFrom,typesOfConnectionTo)"/>
                    							
                    							From 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="typesOfConnectionFromSelect" id="typesOfConnectionFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								
                    								'controlname' : 'typesOfConnectionFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "typesOfConnectionTo" onclick = "checkPair(typesOfConnectionTo,typesOfConnectionFrom)"/>
                    							
                    							To 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="typesOfConnectionToSelect" id="typesOfConnectionToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'typesOfConnectionToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    						
                    								<td align="left" height="25" style="padding-left: 100px" colspan="2">
                    								<input type="submit" name="submit" value = "Generate Report >" onclick = "generateReport()"/>
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
