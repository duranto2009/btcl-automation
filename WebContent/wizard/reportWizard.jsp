<%@ include file="../includes/checkLogin.jsp" %>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page errorPage="../common/failure.jsp" %>
<%@ page import="java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.0 Transitional//EN" "http://www.w3.org/TR/html5/loose.dtd">
<html>
 <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript">
var add = 1;
var addColumns = 1;
function addOneInfoSection()
{
	 var form = document.forms[0];
     var index1 = form.infoSection.selectedIndex;
     var index2 = form.infoSectionSelected.selectedIndex;
     var matched = false;
     var val = form.infoSection[index1].value;
     var inner = form.infoSection[index1].innerHTML;
     var matchedIndex = -1;
     if(add == 1)
	 {
     	if(index1 != -1)
     	{
    	 	for(var i=0;i<form.infoSectionSelected.length;i++)
	    	 {
	    		 if(form.infoSectionSelected[i].value == val)
	    		{
	    			 matched = true;
	    			 matchedIndex = i;
	    			 break;
	    		}
	    	 }
	       	 if(matched == false)
	         {
	       		 form.infoSectionSelected.add(new Option(inner));
	       		 var lastIndex = form.infoSectionSelected.length -1;
	       		 form.infoSectionSelected[lastIndex].value = val;
	         }
	       	 else
	       	 {
	       		form.infoSectionSelected.selectedIndex = matchedIndex;
	       	 }
     	}      
     }
     else
     {
    	 if(index2 != -1)
    	 {
    	 	form.infoSectionSelected.options.remove(index2);
    	 }
     }
   	
     //form.infoSectionSelected.selectedIndex = -1;
}
function addAllInfoSection()
{
	var matched;
	var form = document.forms[0];
	 if(add == 1)
	 {
 
		for(var i=0;i<form.infoSection.length;i++)
	    {
			matched = false;
	      	var val = form.infoSection[i].value;
	      	var inner = form.infoSection[i].innerHTML;
	      	for(var j = 0;j<form.infoSectionSelected.length;j++)
	      	{
	      		if(form.infoSectionSelected[j].value == val)
	      		{
	      			matched = true;
	      		}
	      	}
	      	if(matched == false)
	      	{
	      		form.infoSectionSelected.add(new Option(inner));
	      		 form.infoSectionSelected[j].value = val;
	      	}
	    }
	 }
     else
     {
    	 var len = form.infoSectionSelected.length - 1;
    	 for(var i = len; i>=0;i--)
    	 {
    	 	form.infoSectionSelected.options.remove(i);
    	 }
    	 var len2 = form.columns.length - 1;
    	 for(var i = len2; i>=0;i--)
    	 {
    	 	form.columns.options.remove(i);
    	 }
    	 var len3 = form.columnsSelected.length - 1;
    	 for(var i = len3; i>=0;i--)
    	 {
    	 	form.columnsSelected.options.remove(i);
    	 }
     }
}

function refreshFields()
{
	var form = document.forms[0];
	var index = form.infoSectionSelected.selectedIndex;
	var val = form.infoSectionSelected[index].value;
	var ind;
	switch(val)
	{
	case "Customer Details":
		if(form.columns.length>0)
		{
			var len = form.columns.length - 1;
			for(var i = len; i>=0;i--)
			{
				form.columns.options.remove(i);
			}
		}
		form.columns.add(new Option("Client ID"));
		ind = form.columns.length-1;
		form.columns[ind].value = "Client ID";
		
		form.columns.add(new Option("Name"));
		form.columns[form.columns.length-1].value = "Name";
		
		form.columns.add(new Option("Company"));
		form.columns[form.columns.length-1].value = "Company";
		
		form.columns.add(new Option("Phone"));
		form.columns[form.columns.length-1].value = "Phone";
		
		form.columns.add(new Option("Email"));
		form.columns[form.columns.length-1].value = "Email";
		
		break;
	case "User Details":
		if(form.columns.length>0)
		{
			var len = form.columns.length - 1;
			for(var i = len; i>=0;i--)
			{
				form.columns.options.remove(i);
			}
		}
		form.columns.add(new Option("Full Name"));
		ind = form.columns.length-1;
		form.columns[ind].value = "Full Name";
		
		form.columns.add(new Option("Designation"));
		form.columns[form.columns.length-1].value = "Designation";
		
		form.columns.add(new Option("Address"));
		form.columns[form.columns.length-1].value = "Address";
		break;
	}
	//alert("inside refresh fields" + val);
}
function addOneColumn()
{
	var form = document.forms[0];
    var index1 = form.columns.selectedIndex;
    var index2 = form.columnsSelected.selectedIndex;
    var matched = false;
    var val = form.columns[index1].value;
    var inner = form.columns[index1].innerHTML;
    var matchedIndex = -1;
    if(addColumns == 1)
	 {
    	if(index1 != -1)
    	{
   	 		for(var i=0;i<form.columnsSelected.length;i++)
	    	 {
	    		 if(form.columnsSelected[i].value == val)
	    		{
	    			 matched = true;
	    			 matchedIndex = i;
	    			 break;
	    		}
	    	 }
	       	 if(matched == false)
	         {
	       		 form.columnsSelected.add(new Option(inner));
	       		 var lastIndex = form.columnsSelected.length -1;
	       		 form.columnsSelected[lastIndex].value = val;
	         }
	       	 else
	       	 {
	       		form.columnsSelected.selectedIndex = matchedIndex;
	       	 }
    	}      
    }
    else
    {
	   	 if(index2 != -1)
	   	 {
	   	 	form.columnsSelected.options.remove(index2);
	   	 }
    }
}
function addAllColumn()
{
	var matched;
	var form = document.forms[0];
	 if(addColumns == 1)
	 {
		for(var i=0;i<form.columns.length;i++)
	    {
			matched = false;
	      	var val = form.columns[i].value;
	      	var inner = form.columns[i].innerHTML;
	      	for(var j = 0;j<form.columnsSelected.length;j++)
	      	{
	      		if(form.columnsSelected[j].value == val)
	      		{
	      			matched = true;
	      		}
	      	}
	      	if(matched == false)
	      	{
	      		form.columnsSelected.add(new Option(inner));
	      		 form.columnsSelected[j].value = val;
	      	}
	    }
	 }
     else
     {
    	 var len = form.columnsSelected.length - 1;
    	 for(var i = len; i>=0;i--)
    	 {
    	 	form.columnsSelected.options.remove(i);
    	 }
     }
}
function changeButtonName(name)
{
	var form = document.forms[0];
	if(name == "infoSectionSelected")
	{	
		form.addTypeOne.value = "<";
		form.addTypeAll.value = "<<";
		add = 0;
	}
	else if(name == "infoSection")
	{
		form.addTypeOne.value = ">";
		form.addTypeAll.value = ">>";
		add = 1;
	}
	else if(name == "columns")
	{
		form.addColumnOne.value = ">";
		form.addColumnAll.value = ">>";
		addColumns = 1;
	}
	else
	{
		form.addColumnOne.value = "<";
		form.addColumnAll.value = "<<";
		addColumns = 0;
	}
}

function LTrim(value)
{
	var re = /\s*((\S+\s*)*)/;
  	return value.replace(re, "$1");
}

function RTrim(value)
{
	var re = /((\s*\S+)*)\s*/;
  	return value.replace(re, "$1");
}
function trim(value) 
{
		return LTrim(RTrim(value));
}
function GetXmlHttpObject()
{
   var xmlHttp=null;
   try
   {
      xmlHttp=new XMLHttpRequest();
   }
   catch (e)
   {
      try
      {
         xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
      }
      catch (e)
      {
         xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
      }
   }
   return xmlHttp;
}	
function StateChanged() 
{ 
	var form = document.forms[0];
	form.reportTitle.innerHTML = "hello";
   if (xmlHttp.readyState==4)
   {
	   
   }
}
function generateReport()
{
	var form = document.forms[0];
	for (var i = 0; i< form.infoSectionSelected.length; i++)
	{
		form.infoSectionSelected[i].selected = true;
	}
	for (var i = 0; i< form.columnsSelected.length; i++)
	{
		form.columnsSelected[i].selected = true;
	}
	//sendDataToAction("generateReport");
}
function addOptionsToDatabase() 
{
	sendDataToAction("saveOptions");
}
function sendDataToAction(buttonName)
{
	var form = document.forms[0];
	var title = form.reportTitle.value;
	var types = "";
	
	for(var i = 0; i<form.infoSectionSelected.length;i++)
	{
		types += form.infoSectionSelected[i].innerHTML;
		types += ";";
	}
	var fields = "";
	for(var i = 0; i<form.columnsSelected.length;i++)
	{
		fields += form.columnsSelected[i].innerHTML;
		fields += ";";
	}
	var xmlHttp=GetXmlHttpObject();
  	if (xmlHttp==null)
  	{
     	alert ("Your browser does not support AJAX!");
     	return false;
  	}    
    
  	var url="../report/ReportWizard.do?title="+title+"&types="+types+"&fields="+fields+"&option="+buttonName+"&rand="+ Math.random();
  	alert(url);
  	xmlHttp.onreadystatechange=StateChanged;
  	xmlHttp.open("GET",url,true);
  	xmlHttp.send(null);
  	return true;
}
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Report Wizard</title>

</head>
<body  class="body_center_align" onLoad="init();">

	<table border="0" cellpadding="0" cellspacing="0"  width="1024" id="AutoNumber1">
        <tr>
        	<td width="100%"><%@ include file="../includes/header.jsp"  %></td>
        </tr>
        <tr>
        	<td width="100%">
        		<table border="0" cellpadding="0" cellspacing="0"  width="1024" id="AutoNumber2">
         			 <tr>
         			 	<td class="td_menu"><%@ include file="../includes/menu.jsp"  %>&nbsp;</td>
            			<td width="600" valign="top" class="td_main" align="center">
                  			<table border="0" cellpadding="0" cellspacing="0" width="100%">
                    			<tr>
                    				<td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
                      					<div class="div_title">Create Report</div>
            						</td>
            					</tr>
								<tr>
									<td width="100%" align="center"><br/>
										<html:form action ="/ReportWizard" method = "POST" onsubmit= "generateReport() ">
											<table width="500" border="0" cellpadding="0" cellspacing="0" class="form1">
												<tr>
													<td width="250" height="22">Title of Report</td>
                									<td width="250" height="22"><input type = "text" name ="reportTitle"></td>
            									</tr>
            									<tr>
													<td width="250" height="22">Select Report Type</td>
                									<td width="250" height="22">Select Fields</td>
            									</tr>
            									 <tr>
            									 	<td>
            									 		<table width="250" >
            									 			<tr>
			            									 	<td width="100" height="22"> 
			            									 		<html:select property="infoSection" size="10" style="width: 100%" onfocus = "changeButtonName('infoSection')" multiple = "true">
			                        									<html:option value="Type of Connectivity Services" >Type of Connectivity Services</html:option>
			                        									<html:option value="Bandwidth consumption and Data Transfer" >Bandwidth consumption and Data Transfer</html:option>
			                        									<html:option value="Billing Packages" >Billing Packages</html:option>
			                        									<html:option value="Customer Details" >Customer Details</html:option>
			                        									<html:option value="User Details" >User Details</html:option>
			                        								</html:select> 
			                        							</td>
			                        							<td width="25" height="22">
			                        								<table>
			                        									<tr><td><input type="button" name="addTypeOne" value = ">" onclick = "addOneInfoSection()"></td></tr>
			                        									<tr><td><input type="button" name="addTypeAll" value = ">>" onclick = "addAllInfoSection()"></td></tr>
			                        								</table>
			                        							</td>
			                        						
			                        							<td width="100" height="22"> 
			            									 		<html:select property="infoSectionSelected" size="10" style="width: 100%" onchange = "refreshFields()" onfocus = "changeButtonName('infoSectionSelected')" multiple = "true">
			                        					
			                        								</html:select> 
			                        							</td>
			                        						</tr>
			                        					</table>
                        							</td>
	                        						<td>
	                        							<table width="250" >
	                        								<tr>
			                        							<td width="100" height="22"> 
			            									 		<html:select property = "columns" size="10" style="width: 100%" onfocus = "changeButtonName('columns')" multiple = "true">

			                        								</html:select> 
			                        							</td>
			                        							<td width="25" height="22">
			                        								<table>
			                        									<tr>
			                        										<td><input type="button" name="addColumnOne" value = ">" onclick = "addOneColumn()"></td>
			                        									</tr>
			                        									<tr>
			                        										<td><input type="button" name="addColumnAll" value = ">>" onclick = "addAllColumn()"></td>
			                        									</tr>
			                        								</table>
			                        							</td>
			                        							<td width="100" height="22"> 
			            									 		<html:select property = "columnsSelected" size="10" style="width: 100%" onfocus = "changeButtonName('columnsSelected')" multiple = "true">
			                        								</html:select> 
			                        							</td>
			                        						</tr>
			                        					</table>
	                        						</td>
                    							</tr>
                    							<tr>
                    							</tr>
                    							<tr>
                    							</tr>
                    							<tr>
													<td align = "left"><input type="submit" name="saveOptions" value = "Save Options" onclick = "generateReport()"></td>
                									<td  align = "right"><input type="submit" name="submit" value = "Next >" onclick = "generateReport()"></td>
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
</body>
</html>