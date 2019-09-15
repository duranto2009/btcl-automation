<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="exchange.ExchangeDTO"%>
<%@page import="exchange.ExchangeRepository"%>
<%@page import="dslm.DslmService"%>
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
<%@ page import="java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*,regiontype.*" %>
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
	 option.text = "Client ID";
	 option.value = "Client ID";
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
	 option2.text = "Username";
	 option2.value = "Username";
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
	 option3.text = "Caller ID";
	 option3.value = "Caller ID";
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
	 option4.text = "From(Date)";
	 option4.value = "From(Date)";
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
	 option5.text = "To(Date)";
	 option5.value = "To(Date)";
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
	 option6.text = "Upload(MB)";
	 option6.value = "Upload(MB)";
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
	 option7.text = "Download(MB)";
	 option7.value = "Download(MB)";
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
	 option8.text = "Total Data Transfer";
	 option8.value = "Total Data Transfer";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option8,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option8,null);
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
	for (var i = 0; i< 9; i++)
	{
		document.getElementsByName("columnsSelected")[0].options[i].selected = true;
	}
	for (var i = 0; i< document.getElementsByName("exchangeNoForBwdtSelected")[0].options.length; i++)
	{
		document.getElementsByName("exchangeNoForBwdtSelected")[0].options[i].selected = true;
	}
	/*document.getElementsByName("bwdtFrom")[0].checked = true;
	document.getElementsByName("bwdtTo")[0].checked = true;
	document.getElementsByName("exchangeNoForBwdt")[0].checked = true;*/
}
function validate()
{
	var reportTitle = document.getElementById("reportTitle");  
    var startDate = document.getElementById("bwdtFromSelect");
    var endDate = document.getElementById("bwdtToSelect");
    if(!validateRequired(reportTitle.value))
    {
      alert("Please Enter Report Title");
      reportTitle.value = "";
      reportTitle.focus();
      return false;
    }
    if(document.getElementsByName("exchangeNoForBwdt")[0].checked == true && document.getElementsByName("exchangeNoForBwdtSelected")[0].options.length <= 0)
    	{
    	
    	alert("Please add exchange");
    	
        return false;
    	}
	return true;
}
function addOneExchange()
{
    var index1 = document.getElementsByName("exchangeNoForBwdtSelect")[0].selectedIndex;
    var index2 = document.getElementsByName("exchangeNoForBwdtSelected")[0].selectedIndex;
    var matched = false;
    var val = document.getElementsByName("exchangeNoForBwdtSelect")[0].options[index1].value;
    var inner = document.getElementsByName("exchangeNoForBwdtSelect")[0].options[index1].text;
    var matchedIndex = -1;
    	for(var i=0;i<document.getElementsByName("exchangeNoForBwdtSelected")[0].options.length;i++)
	    	 {
	    		 if(document.getElementsByName("exchangeNoForBwdtSelected")[0].options[i].value == val)
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
		    	  	document.getElementsByName("exchangeNoForBwdtSelected")[0].add(option,document.getElementsByName("exchangeNoForBwdtSelected")[0].options[null]);
		    	 }
		    	 catch (e)
		    	 {
		    		document.getElementsByName("exchangeNoForBwdtSelected")[0].add(option,null);
		    	 }
	       		 var lastIndex = document.getElementsByName("exchangeNoForBwdtSelected")[0].options.length -1;
	       		document.getElementsByName("exchangeNoForBwdtSelected")[0].options[lastIndex].value = val;
	       		 index2 = lastIndex;
	       		 index1 = 0;
	       		 //addFilter();
	         }
	       	 else
	       	 {
	       		document.getElementsByName("exchangeNoForBwdtSelected")[0].selectedIndex = matchedIndex;
	       	 }     
}
function removeOneExchange()
{
	var index2 = document.getElementsByName("exchangeNoForBwdtSelected")[0].selectedIndex;
	document.getElementsByName("exchangeNoForBwdtSelected")[0].options.remove(index2);
}
function addAllExchange()
{
	var matched;
	for(var i=0;i<document.getElementsByName("exchangeNoForBwdtSelect")[0].options.length;i++)
	    {
			matched = false;
	      	var val = document.getElementsByName("exchangeNoForBwdtSelect")[0].options[i].value;
	      	var inner = document.getElementsByName("exchangeNoForBwdtSelect")[0].options[i].text;
	      	for(var j = 0;j<document.getElementsByName("exchangeNoForBwdtSelected")[0].options.length;j++)
	      	{
	      		if(document.getElementsByName("exchangeNoForBwdtSelected")[0].options[j].value == val)
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
		    	  	document.getElementsByName("exchangeNoForBwdtSelected")[0].add(option,document.getElementsByName("exchangeNoForBwdtSelected")[0].options[null]);
		    	 }
		    	 catch (e)
		    	 {
		    		document.getElementsByName("exchangeNoForBwdtSelected")[0].add(option,null);
		    	 }
	      		document.getElementsByName("exchangeNoForBwdtSelected")[0].options[j].value = val;
	      		document.getElementsByName("exchangeNoForBwdtSelect")[0].selectedIndex = 0;
	      	}
	    }
}
function removeAllExchange()
{
	var len = document.getElementsByName("exchangeNoForBwdtSelected")[0].options.length - 1;
	 for(var i = len; i>=0;i--)
	 {
		 document.getElementsByName("exchangeNoForBwdtSelected")[0].options.remove(i);
	 	//removeFilter();
	 }
}
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bandwidth Consumption and Data Transfer</title>
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
                      					<div class="div_title">Create Report on Bandwidth Consumption and Data Transfer </div>
            						</td>
            					</tr>
								<tr>
									<td width="100%" align="center" style="padding-left: 130px"><br/>
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
            									 	<input type = "text" name="infoSectionSelected" style="display:none" value="Bandwidth Consumption and Data Transfer"/> 
            									 		Bandwidth Consumption <br/>and Data Transfer
			                        				</td>
			                        			</tr>
			        
                    							<tr>
                    							<td align="left" height="25"><h4>Filters</h4></td>
                    							<td></td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "exchangeNoForBwdt" />
                    							Exchange 
                    							</td>
                    								<td align = "left" height="25">
                    								<html:select property="exchangeNoForBwdtSelect" size="10" multiple = "true">
                        									
                        									<%
                        									ArrayList<ExchangeDTO> exchanges = (ArrayList<ExchangeDTO>)ExchangeRepository.getInstance().getExchangeList();
                        									
                        									for(int i = 0;i<exchanges.size();i++)
                        									{
                        										//int exchangeID = dslmService.getDslm(dslms.get(i).toString()).getDslmExchangeNo();
                        									%>
                        									<html:option value="<%=Integer.toString(exchanges.get(i).getExCode()) %>" ><%=exchanges.get(i).getExName() %></html:option>
                        									<%
                        									
                        									} %>
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
                    								<html:select property="exchangeNoForBwdtSelected" size="10" multiple = "true">
                        								
                        								</html:select>
                    								</td>
                    								
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "bwdtFrom" onclick = "checkPair(bwdtFrom,bwdtTo)"/>
                    							
                    							Start From 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="bwdtFromSelect" id="bwdtFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'bwdtFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "bwdtTo" onclick = "checkPair(bwdtTo,bwdtFrom)"/>
                    							
                    							To 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="bwdtToSelect" id="bwdtToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'bwdtToSelect'
                    							});</script>
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
