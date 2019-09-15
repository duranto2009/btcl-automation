<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="exchange.ExchangeDTO"%>
<%@page import="exchange.ExchangeRepository"%>
<%@page import="user.UserRepository"%>
<%@page import="role.RoleService"%>
<%@page import="dslm.DslmDTO"%>
<%@page import="dslm.DslmService"%>
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
  
   <script  src="../scripts/util.js" type="text/javascript"></script>
   <link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css">
    
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
	 option.text = "User Name";
	 option.value = "User Name";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option,null);
	 }
	 var option111=document.createElement("option");
	 option111.text = "Login Password";
	 option111.value = "Login Password";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option111,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option111,null);
	 }
	 var option1=document.createElement("option");
	 option1.text = "ADSL Phone";
	 option1.value = "ADSL Phone";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option1,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option1,null);
	 }
	 var option251=document.createElement("option");
	 option251.text = "Full Name";
	 option251.value = "Full Name";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option251,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option251,null);
	 }
	 var option351=document.createElement("option");
	 option351.text = "Address";
	 option351.value = "Address";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option351,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option351,null);
	 }
	 
	 var option3=document.createElement("option");
	 option3.text = "Web Username";
	 option3.value = "Web Username";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option3,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option3,null);
	 }
	 var option112=document.createElement("option");
	 option112.text = "Web Password";
	 option112.value = "Web Password";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option112,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option112,null);
	 }
	 var option4=document.createElement("option");
	 option4.text = "Type Of Connection";
	 option4.value = "Type Of Connection";
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
	 option5.text = "Payment Type";
	 option5.value = "Payment Type";
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
	 option6.text = "Application Status";
	 option6.value = "Application Status";
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
	 option7.text = "Account Status";
	 option7.value = "Account Status";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option7,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option7,null);
	 }
	 var option71=document.createElement("option");
	 option71.text = "Connection Status";
	 option71.value = "Connection Status";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option71,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option71,null);
	 }
	 var option8=document.createElement("option");
	 option8.text = "Package";
	 option8.value = "Package";
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
	 option9.text = "Region";
	 option9.value = "Region";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option9,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option9,null);
	 }
	  var option11=document.createElement("option");
	 option11.text = "DSLAM";
	 option11.value = "DSLAM";
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
	 option12.text = "Port";
	 option12.value = "Port";
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
	 option13.text = "Added On";
	 option13.value = "Added On";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option13,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option13,null);
	 }
	 var option14=document.createElement("option");
	 option14.text = "Verified On";
	 option14.value = "Verified On";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option14,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option14,null);
	 }
	 var option15=document.createElement("option");
	 option15.text = "Approved On";
	 option15.value = "Approved On";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option15,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option15,null);
	 }
	 var option16=document.createElement("option");
	 option16.text = "Exchange Name";
	 option16.value = "Exchange Name";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option16,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option16,null);
	 }
	 var option17=document.createElement("option");
	 option17.text = "Operator";
	 option17.value = "Operator";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option17,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option17,null);
	 }
	 var option18=document.createElement("option");
	 option18.text = "Blacklist Status";
	 option18.value = "Blacklist Status";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option18,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option18,null);
	 }
	 /*var option19=document.createElement("option");
	 option19.text = "Registration";
	 option19.value = "Registration";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option19,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option19,null);
	 }*/
	 var option20=document.createElement("option");
	 option20.text = "Bonus";
	 option20.value = "Bonus";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option20,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option20,null);
	 }
	 var option22=document.createElement("option");
	 option22.text = "Bonus Category";
	 option22.value = "Bonus Category";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option22,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option22,null);
	 }
	 var option21=document.createElement("option");
	 option21.text = "Discount";
	 option21.value = "Discount";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option21,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option21,null);
	 }
	 var option23=document.createElement("option");
	 option23.text = "Discount Category";
	 option23.value = "Discount Category";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option23,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option23,null);
	 }
	for (var i = 0; i< 25; i++)
	{
		document.getElementsByName("columnsSelected")[0].options[i].selected = true;
	}
	var len = document.getElementsByName("exchangeNoSelected")[0].options.length;
	//alert("len is : "+len);
	for (var i = 0; i< len; i++)
	{
		//alert("len is : "+len);
		document.getElementsByName("exchangeNoSelected")[0].options[i].selected = true;
	}
	//alert("everythings ok");
	/*document.getElementsByName("addedDateFrom")[0].checked = true;
	document.getElementsByName("addedDateTo")[0].checked = true;
	document.getElementsByName("verifiedDateFrom")[0].checked = true;
	document.getElementsByName("verifiedDateTo")[0].checked = true;
	document.getElementsByName("approvedDateFrom")[0].checked = true;
	document.getElementsByName("approvedDateTo")[0].checked = true;
	document.getElementsByName("paymentType")[0].checked = true;
	document.getElementsByName("applicationStatus")[0].checked = true;
	document.getElementsByName("name")[0].checked = true;
	document.getElementsByName("packageName")[0].checked = true;
	document.getElementsByName("typeOfConnection")[0].checked = true;
	document.getElementsByName("accountStatus")[0].checked = true;
	document.getElementsByName("regionName")[0].checked = true;
	document.getElementsByName("exchangeNo")[0].checked = true;
	document.getElementsByName("clientPhoneNo")[0].checked = true;
	document.getElementsByName("operatorName")[0].checked = true;
	document.getElementsByName("blackListStatus")[0].checked = true;
	document.getElementsByName("bonusGrantedFrom")[0].checked = true;
	document.getElementsByName("bonusGrantedTo")[0].checked = true;
	document.getElementsByName("discountOfferedFrom")[0].checked = true;
	document.getElementsByName("discountOfferedTo")[0].checked = true;*/
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
    //alert("length is "+document.getElementsByName("exchangeNoSelected")[0].options.length);
    if(document.getElementsByName("exchangeNo")[0].checked == true && document.getElementsByName("exchangeNoSelected")[0].options.length <= 0)
	{
		alert("Please add exchange");
    	return false;
	}
	return true;
}

function addOneExchange()
{
    var index1 = document.getElementsByName("exchangeNoSelect")[0].selectedIndex;
    var index2 = document.getElementsByName("exchangeNoSelected")[0].selectedIndex;
    var matched = false;
    var val = document.getElementsByName("exchangeNoSelect")[0].options[index1].value;
    var inner = document.getElementsByName("exchangeNoSelect")[0].options[index1].text;
    var matchedIndex = -1;
    	for(var i=0;i<document.getElementsByName("exchangeNoSelected")[0].options.length;i++)
	    	 {
	    		 if(document.getElementsByName("exchangeNoSelected")[0].options[i].value == val)
	    		{
	    			 matched = true;
	    			 matchedIndex = i;
	    			 break;
	    		}
	    	 }
	       	 if(matched == false)
	         {
	       		var option = document.createElement("option");
	      		option.value = val;
	      		option.text = inner;
	      		 try
		    	 {
		    	  // for IE earlier than version 8
		    	  	document.getElementsByName("exchangeNoSelected")[0].add(option,document.getElementsByName("exchangeNoSelected")[0].options[null]);
		    	 }
		    	 catch (e)
		    	 {
		    		document.getElementsByName("exchangeNoSelected")[0].add(option,null);
		    	 }
	       		 var lastIndex = document.getElementsByName("exchangeNoSelected")[0].options.length -1;
	       		document.getElementsByName("exchangeNoSelected")[0].options[lastIndex].value = val;
	       		 index2 = lastIndex;
	       		 index1 = 0;
	       		 //addFilter();
	         }
	       	 else
	       	 {
	       		document.getElementsByName("exchangeNoSelected")[0].selectedIndex = matchedIndex;
	       	 }     
}
function removeOneExchange()
{
	var index2 = document.getElementsByName("exchangeNoSelected")[0].selectedIndex;
	document.getElementsByName("exchangeNoSelected")[0].options.remove(index2);
}
function addAllExchange()
{
	var matched;
	for(var i=0;i<document.getElementsByName("exchangeNoSelect")[0].options.length;i++)
	    {
			matched = false;
	      	var val = document.getElementsByName("exchangeNoSelect")[0].options[i].value;
	      	var inner = document.getElementsByName("exchangeNoSelect")[0].options[i].text;
	      	for(var j = 0;j<document.getElementsByName("exchangeNoSelected")[0].options.length;j++)
	      	{
	      		if(document.getElementsByName("exchangeNoSelected")[0].options[j].value == val)
	      		{
	      			matched = true;
	      		}
	      	}
	      	if(matched == false)
	      	{
	      		var option = document.createElement("option");
	      		option.value = val;
	      		option.text = inner;
	      		 try
		    	 {
		    	  // for IE earlier than version 8
		    	  	document.getElementsByName("exchangeNoSelected")[0].add(option,document.getElementsByName("exchangeNoSelected")[0].options[null]);
		    	 }
		    	 catch (e)
		    	 {
		    		document.getElementsByName("exchangeNoSelected")[0].add(option,null);
		    	 }
	      		document.getElementsByName("exchangeNoSelected")[0].options[j].value = val;
	      		document.getElementsByName("exchangeNoSelect")[0].selectedIndex = 0;
	      	}
	    }
}
function removeAllExchange()
{
	var len = document.getElementsByName("exchangeNoSelected")[0].options.length - 1;
	 for(var i = len; i>=0;i--)
	 {
		 document.getElementsByName("exchangeNoSelected")[0].options.remove(i);
	 	//removeFilter();
	 }
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
function changeHandle()
{
    var v = document.getElementsByName("regionNameSelect")[0].options[document.getElementsByName("regionNameSelect")[0].selectedIndex].value;
    var w = document.getElementsByName("clientPhoneNoSelect")[0].value;
   // alert("v and w is "+v+"  "+w);
    changeExchange(v,w);
}


function changeExchange(v,w)
{
	var xmlHttp = null;
    // alert("I am Called" +v);
    if(v==null || w==null)
    {
        return false;
    }
    xmlHttp = GetXmlHttpObject();
    if(xmlHttp == null)
    {
        alert("Browser is not supported.");
        return false;
    } 
    var url="../GetExchanges.do?areaCode="+v+"&adslPhone="+w;
     // alert(url);
    xmlHttp.onreadystatechange = function(){ExchangeChanged(xmlHttp);};
    xmlHttp.open("GET",url,false);    	  	
    xmlHttp.send(null);    	  	
    return true;
}

function ExchangeChanged(xmlHttp) 
{ 

    if (xmlHttp.readyState==4)
    { 
       // alert(xmlHttp.responseText);
        //alert(xmlHttp.responseText.substr(0,6));

	//alert(xmlHttp.responsetext);
	 //alert( document.getElementById("exchange").innerHTML);
        //if(xmlHttp.responseText.substr(0,31) == "<select name = \'exchangeNoSelect\'")
            document.getElementById("exchange").innerHTML=xmlHttp.responseText;
    }

    else
    {
        //alert("Request failed.");
    }
}
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Customer Details</title>
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
                      					<div class="div_title">Create Report on Customer Details </div>
            						</td>
            					</tr>
								<tr>
									<td width="100%" align="center" style="padding-left: 120px"><br/>
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
            									 	<input type = "text" name="infoSectionSelected" style="display:none" value="Customer Details"/> 
            									 		Customer Details
			                        				</td>
			                        			</tr>
			        
                    							<tr>
                    							<td align="left" height="25"><h4>Filters</h4></td>
                    							<td></td>
                    							</tr>
                    							<tr>
                    								<td align = "left"><input type="checkbox" name="clientPhoneNo" />
                    								Client ADSL Phone No 
                    								</td>
                    								<td align = "left">
                    								<input type = "text" name="clientPhoneNoSelect" value = ""/>
                    								</td>
                    								<td>
                    								&nbsp;
                    								</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "addedDateFrom" onclick = "checkPair(addedDateFrom,addedDateTo)"/>
                    							
                    							Added date From 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="addedDateFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'addedDateFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "addedDateTo" onclick = "checkPair(addedDateTo,addedDateFrom)"/>
                    							
                    							Added date To 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="addedDateToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'addedDateToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "verifiedDateFrom" onclick = "checkPair(verifiedDateFrom,verifiedDateTo)"/>
                    							
                    							Verified date From 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="verifiedDateFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">
        
                    							new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'verifiedDateFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "verifiedDateTo" onclick = "checkPair(verifiedDateTo,verifiedDateFrom)"/>
                    							
                    							Verified date To 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="verifiedDateToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'verifiedDateToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "approvedDateFrom" onclick = "checkPair(approvedDateFrom,approvedDateTo)"/>
                    							
                    							Approved date From 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="approvedDateFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'approvedDateFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "approvedDateTo" onclick = "checkPair(approvedDateTo,approvedDateFrom)"/>
                    							
                    							Approved date To 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="approvedDateToSelect" value = "<%=format.format(new Date(System.currentTimeMillis())) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'approvedDateToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "paymentType"/>
                    							Payment Type 
                    							</td>
                    								<td align = "left" height="25">
                    								<html:select property = "paymentTypeSelect" size="1">
	                    								<html:option value="All" >All</html:option>
	                    								<html:option value="Prepaid" >Prepaid</html:option>
	                    								<html:option value="Postpaid" >Postpaid</html:option>
	                    								
			                        				</html:select> 
                    								</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "applicationStatus"/>
                    							Application Status 
                    							</td>
                    								<td align = "left" height="25">
                    								<html:select property = "applicationStatusSelect" size="1">
                    								<html:option value="All" >All</html:option>
                    								<html:option value="Submitted" >Submitted</html:option>
                    								<html:option value="Verified" >Verified</html:option>
                    								<html:option value="Approved" >Approved</html:option>
                    								<html:option value="Denied" >Denied</html:option>
			                        				</html:select> 
                    								</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "name"/>
                    							Username 
                    							</td>
                    								<td align = "left" height="25">
                    								<input type = "text" name="nameSelect" value = ""/>
                    								</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25"><input type="checkbox" name="packageName"/>Package Name :</td>
                    								<td align="left" height="25">
                    									<html:select property="packageNameSelect" size="1">
                        									<html:option value="All" >All</html:option>
                        									<%
                        									PackageService packageService = new PackageService();
                        									ArrayList<Long> ids = (ArrayList<Long>)packageService.getIDs(loginDTO);
                        									for(int i = 0;i<ids.size();i++)
                        									{
                        									%>
                        									<html:option value="<%=Long.toString(ids.get(i)) %>" ><%=packageService.getPackageName(Long.toString(ids.get(i))) %></html:option>
                        									<%} %>
                        								</html:select>
                        							</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "typeOfConnection"/>
                    							Type Of Connection 
                    							</td>
                    								<td align = "left" height="25">
                    								<html:select property = "typeOfConnectionSelect" size="1">
                    								<html:option value="All" >All</html:option>
                    								<%for(int i = 0;i<ClientConstants.TYPEOFCONNECTION_VALUE.length;i++)
                    									{%>
                    									<html:option value="<%=Long.toString(ClientConstants.TYPEOFCONNECTION_VALUE[i]) %>" ><%=ClientConstants.TYPEOFCONNECTION_NAME[i] %></html:option>
			                        				<%} %>
			                        				</html:select> 
                    								</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "accountStatus"/>
                    							Account Status 
                    							</td>
                    								<td align = "left" height="25">
                    								<html:select property = "accountStatusSelect" size="1">
                    								<html:option value="All" >All</html:option>
                    								<html:option value="Active" >Active</html:option>
                    								<html:option value="Disabled" >Disabled</html:option>
                    								<html:option value="Renewed" >Renewed</html:option>
                    								<html:option value="Expired" >Expired</html:option>
                    								<html:option value="Removed" >Removed</html:option>
                    								
			                        				</html:select> 
<!--                     								</td> -->
<!--                     							<td align="left" height="25"> -->

                    							</td>                    							
                    							</tr>
                    							<tr>
                    							<td align="right" height="25">From:
                    							</td>
                    							<td>
                    							<input type = "text" name="clientStatusFrom" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'clientStatusFrom'
                    							});</script>
                    							
                    							</td>
                    							</tr>
                    							<tr>
                    							<td align="right" height="25">To:
                    							</td>
                    							<td>
                    							                    
                    							<input type = "text" name="clientStatusTo" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'clientStatusTo'
                    							});</script>
                    							</td>
                    							</tr>                    							
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "blackListStatus" />
                    							
                    							Blacklist Status
                    							</td>
                    								<td align = "left" height="25">
                    								<html:select property = "blackListStatusSelect" size="1">
                    								<html:option value="All" >All</html:option>
                    								<html:option value="Yes" >Yes</html:option>
                    								<html:option value="No" >No</html:option>
                    							
                    								
			                        				</html:select> 
                    								</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "regionName" />
                    							Region 
                    							</td>
                    								<td align = "left" height="25">
                    								<html:select property="regionNameSelect" size="1" onchange="changeHandle()">
                        									<html:option value="All" >All</html:option>
                        									<%
                        									RegionService regionService = new RegionService();
                        									ArrayList<RegionDTO> regions = (ArrayList<RegionDTO>)regionService.getAllRegion();
                        									for(int i = 0;i<regions.size();i++)
                        									{
                        									%>
                        									<html:option value="<%=Long.toString(regions.get(i).getRegionDesc()) %>" ><%=regions.get(i).getRegionName() %></html:option>
                        									<%} %>
                        								</html:select>
                    								</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "exchangeNo"/>
                    							Exchange 
                    							</td>
                    								<td align = "left" height="25"><div id="exchange">
                    								<html:select property="exchangeNoSelect" size="10" multiple = "true">
                        									
                        									<%
                        									ArrayList<ExchangeDTO> exchanges = (ArrayList<ExchangeDTO>)ExchangeRepository.getInstance().getExchangeList();
                        									for(int i = 0;i<exchanges.size();i++)
                        									{
                        										//int exchangeID = dslmService.getDslm(dslms.get(i).toString()).getDslmExchangeNo();
                        									%>
                        									<html:option value="<%=Integer.toString(exchanges.get(i).getExCode()) %>" ><%=exchanges.get(i).getExName() %></html:option>
                        									<%
                        									
                        									} %>
                        								</html:select></div>
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
                    								<html:select property="exchangeNoSelected" size="10" multiple = "true">
                        								
                        								</html:select>
                    								</td>
                    								
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "operatorName"/>
                    							Operator 
                    							</td>
                    								<td align = "left" height="25">
                    								<html:select property="operatorNameSelect" size="1">
                        									<html:option value="All" >All</html:option>
                        									<%
                        									RoleService rservice = util.ServiceDAOFactory.getService(RoleService.class);
                          							          ArrayList operatorList = UserRepository.getInstance().getUserList();
                        							          for(int i=0;i<operatorList.size();i++)
                        							          {
                        							           user.UserDTO operatorDTO = (user.UserDTO)operatorList.get(i);
                        							           
                        							            if(rservice.getRole(""+operatorDTO.getRoleID()).getRestrictedtoOwn())
                        							            {
                        							            %>
                        		                          <html:option value="<%=Long.toString(operatorDTO.getUserID())%>"><%=operatorDTO.getUsername()%></html:option>
                        		                          <% }} %>
                        									
                        								</html:select>
                    								</td>
                    							</tr>
                    							
                    							<!-- <tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "registration" style="display:none"/>
                    							Registration 
                    							</td>
                    								<td align = "left" height="25">
                    								<html:select property="registrationSelect" size="1">
                        									<html:option value="All" >All</html:option>
                        									<html:option value="Online" >Online Registered</html:option>
                        									<html:option value="Offline" >Offline Registered</html:option>
                        									
                        								</html:select>
                    								</td>
                    							</tr>-->
                    							
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "bonusGrantedFrom" onclick = "checkPair(bonusGrantedFrom,bonusGrantedTo)"/>
                    							
                    							Bonus Granted From 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="bonusGrantedFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'bonusGrantedFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "bonusGrantedTo" onclick = "checkPair(bonusGrantedTo,bonusGrantedFrom)"/>
                    							
                    							Bonus Granted To 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="bonusGrantedToSelect" value = "<%=format.format(new Date(System.currentTimeMillis())) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'bonusGrantedToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "discountOfferedFrom" onclick = "checkPair(discountOfferedFrom,discountOfferedTo)"/>
                    							
                    							Discount Offered From 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="discountOfferedFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'discountOfferedFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "discountOfferedTo" onclick = "checkPair(discountOfferedTo,discountOfferedFrom)"/>
                    							
                    							Discount Offered To 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="discountOfferedToSelect" value = "<%=format.format(new Date(System.currentTimeMillis())) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'discountOfferedToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "connection" />
                    							Connection Status 
                    							</td>
                    								<td align = "left" height="25">
                    								<html:select property="connectionSelect" size="1">
                        									<html:option value="All" >All</html:option>
                        									<html:option value="Not Connected" >Not Connected</html:option>
                        									<html:option value="Connected" >Connected</html:option>
                        									
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
