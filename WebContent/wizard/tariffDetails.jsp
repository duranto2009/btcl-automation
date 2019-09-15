<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="regiontype.RegionDTO"%>
<%@page import="regiontype.RegionService"%>
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
	 option.text = "Tariff ID";
	 option.value = "Tariff ID";
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
	 option2.text = "Package Name";
	 option2.value = "Package Name";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option2,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option2,null);
	 }
	 var option3=document.createElement("option");
	 option3.text = "Activation Date";
	 option3.value = "Activation Date";
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
	 option4.text = "Expiry Date";
	 option4.value = "Expiry Date";
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
	 option5.text = "Package Value";
	 option5.value = "Package Value";
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
	 option6.text = "Unit Rate";
	 option6.value = "Unit Rate";
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
	 option7.text = "Additional Usage Charge";
	 option7.value = "Additional Usage Charge";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option7,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option7,null);
	 }

	for (var i = 0; i<7; i++)
	{
		document.getElementsByName("columnsSelected")[0].options[i].selected = true;
	}
	var len = document.getElementsByName("packageNameForTariffDetailsSelected")[0].options.length;
	for (var i = 0; i<len; i++)
	{
		document.getElementsByName("packageNameForTariffDetailsSelected")[0].options[i].selected = true;
	}
	/*document.getElementsByName("activationDateFrom")[0].checked = true;
	document.getElementsByName("activationDateTo")[0].checked = true;
	document.getElementsByName("expirationDateFrom")[0].checked = true;
	document.getElementsByName("expirationDateTo")[0].checked = true;
	document.getElementsByName("packageNameForTariffDetails")[0].checked = true;*/

}
function validate()
{
	var reportTitle = document.getElementById("reportTitle");  
	var startActivationDate = document.getElementById("activationDateFromSelect");
	var endActivationDate = document.getElementById("activationDateToSelect");
	var startExpirationDate = document.getElementById("expirationDateFromSelect");
	var endExpirationDate = document.getElementById("expirationDateToSelect");
	
	
    
    if(!validateRequired(reportTitle.value))
    {
      alert("Please Enter Report Title");
      reportTitle.value = "";
      reportTitle.focus();
      return false;
    }
    if(!validateRequired(startActivationDate.value))
	{
		alert("Please select a starting date for activation date");
		return false;
	}
    if(!validateRequired(endActivationDate.value))
	{
		alert("Please select an ending date for activation date");
		return false;
	}
    if(!validateRequired(startExpirationDate.value))
	{
		alert("Please select a starting date for expiration date");
		return false;
	}
    if(!validateRequired(endExpirationDate.value))
	{
		alert("Please select an ending date for expiration date");
		return false;
	}
    if(document.getElementsByName("packageNameForTariffDetails")[0].checked == true && document.getElementsByName("packageNameForTariffDetailsSelected")[0].options.length <= 0)
	{
	
	alert("Please add package");
	
    return false;
	}
	return true;
}
function addOneExchange()
{
    var index1 = document.getElementsByName("packageNameForTariffDetailsSelect")[0].selectedIndex;
    var index2 = document.getElementsByName("packageNameForTariffDetailsSelected")[0].selectedIndex;
    var matched = false;
    var val = document.getElementsByName("packageNameForTariffDetailsSelect")[0].options[index1].value;
    var inner = document.getElementsByName("packageNameForTariffDetailsSelect")[0].options[index1].text;
    var matchedIndex = -1;
    	for(var i=0;i<document.getElementsByName("packageNameForTariffDetailsSelected")[0].options.length;i++)
	    	 {
	    		 if(document.getElementsByName("packageNameForTariffDetailsSelected")[0].options[i].value == val)
	    		{
	    			 matched = true;
	    			 matchedIndex = i;
	    			 break;
	    		}
	    	 }
	       	 if(matched == false)
	         {
	       		var option = document.createElement("option");
	      		option.value = inner;
	      		option.text = inner;
	      		 try
		    	 {
		    	  // for IE earlier than version 8
		    	  	document.getElementsByName("packageNameForTariffDetailsSelected")[0].add(option,document.getElementsByName("packageNameForTariffDetailsSelected")[0].options[null]);
		    	 }
		    	 catch (e)
		    	 {
		    		document.getElementsByName("packageNameForTariffDetailsSelected")[0].add(option,null);
		    	 }
	       		 var lastIndex = document.getElementsByName("packageNameForTariffDetailsSelected")[0].options.length -1;
	       		document.getElementsByName("packageNameForTariffDetailsSelected")[0].options[lastIndex].value = val;
	       		 index2 = lastIndex;
	       		 index1 = 0;
	       		 //addFilter();
	         }
	       	 else
	       	 {
	       		document.getElementsByName("packageNameForTariffDetailsSelected")[0].selectedIndex = matchedIndex;
	       	 }     
}
function removeOneExchange()
{
	var index2 = document.getElementsByName("packageNameForTariffDetailsSelected")[0].selectedIndex;
	document.getElementsByName("packageNameForTariffDetailsSelected")[0].options.remove(index2);
}
function addAllExchange()
{
	var matched;
	for(var i=0;i<document.getElementsByName("packageNameForTariffDetailsSelect")[0].options.length;i++)
	    {
			matched = false;
	      	var val = document.getElementsByName("packageNameForTariffDetailsSelect")[0].options[i].value;
	      	var inner = document.getElementsByName("packageNameForTariffDetailsSelect")[0].options[i].text;
	      	for(var j = 0;j<document.getElementsByName("packageNameForTariffDetailsSelected")[0].options.length;j++)
	      	{
	      		if(document.getElementsByName("packageNameForTariffDetailsSelected")[0].options[j].value == val)
	      		{
	      			matched = true;
	      		}
	      	}
	      	if(matched == false)
	      	{
	      		var option = document.createElement("option");
	      		option.value = inner;
	      		option.text = inner;
	      		 try
		    	 {
		    	  // for IE earlier than version 8
		    	  	document.getElementsByName("packageNameForTariffDetailsSelected")[0].add(option,document.getElementsByName("packageNameForTariffDetailsSelected")[0].options[null]);
		    	 }
		    	 catch (e)
		    	 {
		    		document.getElementsByName("packageNameForTariffDetailsSelected")[0].add(option,null);
		    	 }
	      		document.getElementsByName("packageNameForTariffDetailsSelected")[0].options[j].value = val;
	      		document.getElementsByName("packageNameForTariffDetailsSelect")[0].selectedIndex = 0;
	      	}
	    }
}
function removeAllExchange()
{
	var len = document.getElementsByName("packageNameForTariffDetailsSelected")[0].options.length - 1;
	 for(var i = len; i>=0;i--)
	 {
		 document.getElementsByName("packageNameForTariffDetailsSelected")[0].options.remove(i);
	 	//removeFilter();
	 }
}
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tariff Details</title>
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
                      					<div class="div_title">Create Report on Customer Details </div>
            						</td>
            					</tr>
								<tr>
									<td width="100%" align="center" style="padding-left: 130px"><br/>
									<html:form action="/SingleReport" method="POST" onsubmit = "return validate();">
										<html:select property = "columnsSelected" size="10" multiple = "true" style="display:none;width: 100%">
			                        								</html:select> 
			
											<table width="500" border="0" cellpadding="0" cellspacing="0" id = "AutoNumber3">
												<tr>
													<td align="left"><h4>Report Title  </h4></td>
													
                									<td align="left">
                									<input type= "text" name ="reportTitle" id="reportTitle"/>
                									</td>
            									</tr>
            									<tr>
            						
													<td align="left" height="25"><h4>Report Type </h4></td>
            									 	<td align="left" height="25">
            									 	<input type = "text" name="infoSectionSelected" style="display:none" value="Tariff Details"/> 
            									 		Tariff Details
			                        				</td>
			                        			</tr>
			        
                    							<tr>
                    							<td align="left" height="25"><h4>Filters</h4></td>
                    							<td></td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "activationDateFrom" onclick = "checkPair(activationDateFrom,activationDateTo)"/>
                    							
                    							Activation date From 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="activationDateFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'activationDateFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "activationDateTo" onclick = "checkPair(activationDateTo,activationDateFrom)"/>
                    							
                    							Activation date To
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="activationDateToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'activationDateToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "expirationDateFrom" onclick = "checkPair(expirationDateFrom,expirationDateTo)"/>
                    							
                    							Expiration date From 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="expirationDateFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'expirationDateFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "expirationDateTo" onclick = "checkPair(expirationDateTo,expirationDateFrom)"/>
                    							
                    							Expiration date To 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="expirationDateToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'expirationDateToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    							<tr>
                    							<td align="left" height="25"><input type="checkbox" name="packageNameForTariffDetails"/>Package Name </td>
                    								<td align="left" height="25">
                    									<html:select property="packageNameForTariffDetailsSelect" size="10" multiple="true">
                        									<%
                        									PackageService packageService = new PackageService();
                        									ArrayList<Long> ids = (ArrayList<Long>)packageService.getIDs(loginDTO);
                        									for(int i = 0;i<ids.size();i++)
                        									{
                        									%>
                        									<html:option value="<%=packageService.getPackageName(Long.toString(ids.get(i))) %>" ><%=packageService.getPackageName(Long.toString(ids.get(i))) %></html:option>
                        									<%} %>
                        								</html:select>
                        							</td>
                    							</tr>
                    							<tr>
                    							<td align="center" height="25">
                    							<input type="button" name="addExchangeOne" value = "Add" onclick = "addOneExchange()"/>
                    							<br/>
                    							<input type="button" name="addExchangeAll" value = "Add All" onclick = "addAllExchange()"/>
                    							<br/>
                    							<input type="button" name="removeExchangeOne" value = "Remove" onclick = "removeOneExchange()"/>
                    							<br/>
                    							<input type="button" name="removeExchangeAll" value = "Remove All" onclick = "removeAllExchange()"/>
                    							<br/>
                    							
                    							</td>
                    								<td align = "left" height="25">
                    								<html:select property="packageNameForTariffDetailsSelected" size="10" multiple = "true">
                        								
                        								</html:select>
                    								</td>
                    								
                    							</tr>
                    						
                    						<tr>
                    								<td align="left" height="25" colspan="2" style="padding-left: 110px">
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
