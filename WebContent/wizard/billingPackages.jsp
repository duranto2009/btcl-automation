<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="packages.PackageService"%>
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
 <script language="JavaScript">
function generateReport()
{
	var option=document.createElement("option");
	 option.text = "Package Name";
	 option.value = "Package Name";
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
	 option2.text = "Package Type";
	 option2.value = "Package Type";
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
	 option3.text = "Validity Period";
	 option3.value = "Validity Period";
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
	 option4.text = "Is Unlimited";
	 option4.value = "Is Unlimited";
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
	 option5.text = "Total Volume Limit";
	 option5.value = "Total Volume Limit";
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
	 option6.text = "Data Transfer Rate";
	 option6.value = "Data Transfer Rate";
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
	 option7.text = "Unit Type";
	 option7.value = "Unit Type";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option7,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option7,null);
	 }
	 var option8=document.createElement("option");
	 option8.text = "Vat";
	 option8.value = "Vat";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option8,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option8,null);
	 }
	 var option9=document.createElement("option");
	 option9.text = "Tariff Type";
	 option9.value = "Tariff Type";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option9,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option9,null);
	 }
	 var option10=document.createElement("option");
	 option10.text = "Costing Type";
	 option10.value = "Costing Type";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option10,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option10,null);
	 }
	 var option11=document.createElement("option");
	 option11.text = "Configuration Charge";
	 option11.value = "Configuration Charge";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option11,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option11,null);
	 }
	 var option12=document.createElement("option");
	 option12.text = "Registration Charge";
	 option12.value = "Registration Charge";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option12,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option12,null);
	 }
	 var option13=document.createElement("option");
	 option13.text = "Shifting Charge";
	 option13.value = "Shifting Charge";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option13,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option13,null);
	 }
	for (var i = 0; i< 13; i++)
	{
		document.getElementsByName("columnsSelected")[0].options[i].selected = true;
	}
	for (var i = 0; i< document.getElementsByName("packageNameForBillingPackagesSelected")[0].options.length; i++)
	{
		document.getElementsByName("packageNameForBillingPackagesSelected")[0].options[i].selected = true;
	}
	/*document.getElementsByName("packageNameForBillingPackages")[0].checked = true;
	document.getElementsByName("packageType")[0].checked = true;
	document.getElementsByName("isUnlimited")[0].checked = true;
	document.getElementsByName("unitType")[0].checked = true;
	document.getElementsByName("tariffType")[0].checked = true;
	document.getElementsByName("costingType")[0].checked = true;*/
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
    if(document.getElementsByName("packageNameForBillingPackages")[0].checked == true && document.getElementsByName("packageNameForBillingPackagesSelected")[0].options.length <= 0)
	{
	
	alert("Please add package");
	
    return false;
	}
	return true;
}
function addOneExchange()
{
    var index1 = document.getElementsByName("packageNameForBillingPackagesSelect")[0].selectedIndex;
    var index2 = document.getElementsByName("packageNameForBillingPackagesSelected")[0].selectedIndex;
    var matched = false;
    var val = document.getElementsByName("packageNameForBillingPackagesSelect")[0].options[index1].value;
    var inner = document.getElementsByName("packageNameForBillingPackagesSelect")[0].options[index1].text;
    var matchedIndex = -1;
    	for(var i=0;i<document.getElementsByName("packageNameForBillingPackagesSelected")[0].options.length;i++)
	    	 {
	    		 if(document.getElementsByName("packageNameForBillingPackagesSelected")[0].options[i].value == val)
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
		    	  	document.getElementsByName("packageNameForBillingPackagesSelected")[0].add(option,document.getElementsByName("packageNameForBillingPackagesSelected")[0].options[null]);
		    	 }
		    	 catch (e)
		    	 {
		    		document.getElementsByName("packageNameForBillingPackagesSelected")[0].add(option,null);
		    	 }
	       		 var lastIndex = document.getElementsByName("packageNameForBillingPackagesSelected")[0].options.length -1;
	       		document.getElementsByName("packageNameForBillingPackagesSelected")[0].options[lastIndex].value = val;
	       		 index2 = lastIndex;
	       		 index1 = 0;
	       		 //addFilter();
	         }
	       	 else
	       	 {
	       		document.getElementsByName("packageNameForBillingPackagesSelected")[0].selectedIndex = matchedIndex;
	       	 }     
}
function removeOneExchange()
{
	var index2 = document.getElementsByName("packageNameForBillingPackagesSelected")[0].selectedIndex;
	document.getElementsByName("packageNameForBillingPackagesSelected")[0].options.remove(index2);
}
function addAllExchange()
{
	var matched;
	for(var i=0;i<document.getElementsByName("packageNameForBillingPackagesSelect")[0].options.length;i++)
	    {
			matched = false;
	      	var val = document.getElementsByName("packageNameForBillingPackagesSelect")[0].options[i].value;
	      	var inner = document.getElementsByName("packageNameForBillingPackagesSelect")[0].options[i].text;
	      	for(var j = 0;j<document.getElementsByName("packageNameForBillingPackagesSelected")[0].options.length;j++)
	      	{
	      		if(document.getElementsByName("packageNameForBillingPackagesSelected")[0].options[j].value == val)
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
		    	  	document.getElementsByName("packageNameForBillingPackagesSelected")[0].add(option,document.getElementsByName("packageNameForBillingPackagesSelected")[0].options[null]);
		    	 }
		    	 catch (e)
		    	 {
		    		document.getElementsByName("packageNameForBillingPackagesSelected")[0].add(option,null);
		    	 }
	      		document.getElementsByName("packageNameForBillingPackagesSelected")[0].options[j].value = val;
	      		document.getElementsByName("packageNameForBillingPackagesSelect")[0].selectedIndex = 0;
	      	}
	    }
}
function removeAllExchange()
{
	var len = document.getElementsByName("packageNameForBillingPackagesSelected")[0].options.length - 1;
	 for(var i = len; i>=0;i--)
	 {
		 document.getElementsByName("packageNameForBillingPackagesSelected")[0].options.remove(i);
	 	//removeFilter();
	 }
}
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Billing Packages</title>

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
                      					<div class="div_title">Create Report on Billing Packages </div>
            						</td>
            					</tr>
								<tr>
									<td width="100%" align="center" style="padding-left: 150px"><br/>
									<html:form action="/SingleReport" method = "POST" onsubmit="return validate();">
										<html:select property = "columnsSelected" multiple = "true" style="display:none;width: 100%">
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
            									 	<input type = "text" name="infoSectionSelected" style="display:none" value="Billing Packages"/> 
            									 		Billing Packages
			                        				</td>
			                        			</tr>
			        
                    							<tr>
                    							<td align="left" height="25"><h4>Filters</h4></td>
                    							<td></td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25"><input type="checkbox" name="packageNameForBillingPackages"/>Package Name </td>
                    								<td align="left" height="25">
                    									<html:select property="packageNameForBillingPackagesSelect" size="10" multiple = "true">
                        									
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
                    								<html:select property="packageNameForBillingPackagesSelected" size="10" multiple = "true">
                        								
                        								</html:select>
                    								</td>
                    								
                    							</tr>
                    							<tr>
                    							<td align="left" height="25"><input type="checkbox" name="packageType"/>Package Type </td>
                    								<td align="left" height="25">
                    									<html:select property="packageTypeSelect" size="1"  style="width:50">
                        									<html:option value="All" >All</html:option>
                        									<html:option value="Prepaid" >Prepaid</html:option>
                        									<html:option value="Postpaid" >Postpaid</html:option>
                        									
                        								
                        								</html:select>
                        							</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25"><input type="checkbox" name="isUnlimited"/>Is Unlimited </td>
                    								<td align="left" height="25">
                    									<html:select property="isUnlimitedSelect" size="1">
                        									<html:option value="All" >All</html:option>
                        									<html:option value="Yes" >Yes</html:option>
                        									<html:option value="No" >No</html:option>
                        								</html:select>
                        							</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25"><input type="checkbox" name="unitType"/>Unit Type </td>
                    								<td align="left" height="25">
                    									<html:select property="unitTypeSelect" size="1">
                    										<html:option value="All" >All</html:option>
                        									<html:option value="Per KB" >Per KB</html:option>
                        									<html:option value="Per MB" >Per MB</html:option>
                        									<html:option value="Per Minute" >Per Minute</html:option>
                        								</html:select>
                        							</td>
                    							</tr>
                    							<tr>
                    							<td  align="left" height="25"><input type="checkbox" name="tariffType"/>Tariff Type </td>
                    								<td align="left" height="25">
                    									<html:select property="tariffTypeSelect" size="1">
                        									<html:option value="All" >All</html:option>
                        									<html:option value="Time Based" >Time Based</html:option>
                        									<html:option value="Volume Based" >Volume Based</html:option>
                        									
                        								</html:select>
                        							</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25"><input type="checkbox" name="costingType"/>Costing Type </td>
                    								<td align="left" height="25">
                    									<html:select property="costingTypeSelect" size="1">
                        									<html:option value="All" >All</html:option>
                        									<html:option value="Package Based" >Package Based</html:option>
                        									<html:option value="Unit Based" >Unit Based</html:option>
                        								</html:select>
                        							</td>
                    							</tr>
                    							<tr>
                    								
                    								<td align="left" height="25" colspan="2" style="padding-left: 100px">
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
