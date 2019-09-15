<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="exchange.ExchangeDTO"%>
<%@page import="exchange.ExchangeRepository"%>
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
<head>
<html:base/>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Traffic Details</title>
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
	 option.text = "Customer ID";
	 option.value = "Customer ID";
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
	 option2.text = "Caller ID";
	 option2.value = "Caller ID";
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
	 option3.text = "From(Date)";
	 option3.value = "From(Date)";
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
	 option4.text = "To(Date)";
	 option4.value = "To(Date)";
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
	 option5.text = "No. Of Login";
	 option5.value = "No. Of Login";
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
	 option6.text = "Total Upload(MB)";
	 option6.value = "Total Upload(MB)";
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
	 option7.text = "Total Download(MB)";
	 option7.value = "Total Download(MB)";
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
	 option8.text = "Average Upload(MB)";
	 option8.value = "Average Upload(MB)";
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
	 option9.text = "Average Download(MB)";
	 option9.value = "Average Download(MB)";
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
	 option10.text = "Total Data Transfer";
	 option10.value = "Total Data Transfer";
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
	 option11.text = "Average Data Transfer(Per Login)";
	 option11.value = "Average Data Transfer(Per Login)";
	 try
	 {
	  // for IE earlier than version 8
	  	document.getElementsByName("columnsSelected")[0].add(option11,document.getElementsByName("columnsSelected")[0].options[null]);
	 }
	 catch (e)
	 {
		document.getElementsByName("columnsSelected")[0].add(option11,null);
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
	
	for (var i = 0; i< 12; i++)
	{
		document.getElementsByName("columnsSelected")[0].options[i].selected = true;
	}
	for (var i = 0; i< document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].options.length; i++)
	{
		document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].options[i].selected = true;
	}
	/*document.getElementsByName("trafficDetailsFrom")[0].checked = true;
	document.getElementsByName("trafficDetailsTo")[0].checked = true;
	document.getElementsByName("exchangeNoForTrafficDetails")[0].checked = true;*/
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
    if(document.getElementsByName("exchangeNoForTrafficDetails")[0].checked == true && document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].options.length <= 0)
	{
	
	alert("Please add exchange");
	
    return false;
	}
	return true;
}
function addOneExchange()
{
    var index1 = document.getElementsByName("exchangeNoForTrafficDetailsSelect")[0].selectedIndex;
    var index2 = document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].selectedIndex;
    var matched = false;
    var val = document.getElementsByName("exchangeNoForTrafficDetailsSelect")[0].options[index1].value;
    var inner = document.getElementsByName("exchangeNoForTrafficDetailsSelect")[0].options[index1].text;
    var matchedIndex = -1;
    	for(var i=0;i<document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].options.length;i++)
	    	 {
	    		 if(document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].options[i].value == val)
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
		    	  	document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].add(option,document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].options[null]);
		    	 }
		    	 catch (e)
		    	 {
		    		document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].add(option,null);
		    	 }
	       		 var lastIndex = document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].options.length -1;
	       		document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].options[lastIndex].value = val;
	       		 index2 = lastIndex;
	       		 index1 = 0;
	       		 //addFilter();
	         }
	       	 else
	       	 {
	       		document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].selectedIndex = matchedIndex;
	       	 }     
}
function removeOneExchange()
{
	var index2 = document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].selectedIndex;
	document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].options.remove(index2);
}
function addAllExchange()
{
	var matched;
	for(var i=0;i<document.getElementsByName("exchangeNoForTrafficDetailsSelect")[0].options.length;i++)
	    {
			matched = false;
	      	var val = document.getElementsByName("exchangeNoForTrafficDetailsSelect")[0].options[i].value;
	      	var inner = document.getElementsByName("exchangeNoForTrafficDetailsSelect")[0].options[i].text;
	      	for(var j = 0;j<document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].options.length;j++)
	      	{
	      		if(document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].options[j].value == val)
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
		    	  	document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].add(option,document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].options[null]);
		    	 }
		    	 catch (e)
		    	 {
		    		document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].add(option,null);
		    	 }
	      		document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].options[j].value = val;
	      		document.getElementsByName("exchangeNoForTrafficDetailsSelect")[0].selectedIndex = 0;
	      	}
	    }
}
function removeAllExchange()
{
	var len = document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].options.length - 1;
	 for(var i = len; i>=0;i--)
	 {
		 document.getElementsByName("exchangeNoForTrafficDetailsSelected")[0].options.remove(i);
	 	//removeFilter();
	 }
}
</script>

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
                      					<div class="div_title">Create Report on Traffic Details </div>
            						</td>
            					</tr>
								<tr>
									<td width="100%" align="center" style="padding-left: 170px"><br/>
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
            									 	<input type = "text" name="infoSectionSelected" style="display:none" value="Traffic Details"/> 
            									 		Traffic Details
			                        				</td>
			                        			</tr>
			        
                    							<tr>
                    							<td align="left" height="25"><h4>Filters</h4></td>
                    							<td></td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "exchangeNoForTrafficDetails"/>
                    							Exchange 
                    							</td>
                    								<td align = "left" height="25">
                    								<html:select property="exchangeNoForTrafficDetailsSelect" size="10" multiple="true">
                        							
                        									<%
                        									
                        									ArrayList<ExchangeDTO> exchanges = (ArrayList<ExchangeDTO>)ExchangeRepository.getInstance().getExchangeList();
                        									for(int i = 0;i<exchanges.size();i++)
                        									{
                        										//int exchangeID = dslmService.getDslm(dslms.get(i).toString()).getDslmExchangeNo();
                        										//System.out.println("   : "+exchangeID);
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
                    								<html:select property="exchangeNoForTrafficDetailsSelected" size="10" multiple = "true">
                        								
                        								</html:select>
                    								</td>
                    								
                    							</tr>
                    							<tr>
                    								<td align="left" height="25">
	                    							<input type= "checkbox" name = "trafficDetailsFrom" onclick = "checkPair(trafficDetailsFrom,trafficDetailsTo)" />
	                    							
	                    							From 
	                    							</td>
	                    							<td align="left" height="25">
	                    							<input type = "text" name="trafficDetailsFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
	                    								'formname' : 'singleReportForm',
	                    								'controlname' : 'trafficDetailsFromSelect'
	                    							});</script>
	                    							</td>
	                    						</tr>
	                    						<tr>
	                    							<td align="left" height="25">
	                    							<input type= "checkbox" name = "trafficDetailsTo" onclick = "checkPair(trafficDetailsTo,trafficDetailsFrom)" />
	                    							
	                    							To 
	                    							</td>
	                    							<td align="left" height="25">
	                    							<input type = "text" name="trafficDetailsToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
	                    								'formname' : 'singleReportForm',
	                    								'controlname' : 'trafficDetailsToSelect'
	                    							});</script>
	                    							</td>
	                    						</tr>
                    						<tr>
                    								<td align="left" height="25" colspan="2" style="padding-left: 80px">
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
