<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="prepaidpayment.form.PrepaidPaymentForm"%>
<%@page import="regiontype.RegionConstants"%>
<%@page import="regiontype.RegionDTO"%>
<%@page import="regiontype.RegionService"%>
<%@page import="client.ClientStatusDTO"%>
<%@page import="client.ClientDTO"%>
<%@page import="client.ClientRepository"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="../includes/checkLogin.jsp"%>
<%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="../common/failure.jsp"%>
<%@ page import="java.util.*,sessionmanager.SessionConstants,java.io.*,packages.*,prepaidpayment.*"%>
<%
	String submitCaption = "Add Payment";
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	PrepaidPaymentForm pform = (PrepaidPaymentForm)request.getSession().getAttribute("prepaidPaymentForm");
	if(pform == null)
	{
		//out.println("What a shame!!");
		pform = new PrepaidPaymentForm();
	}
	
	

%>

<html>
<head>
<html:base />
<title>Add Payment</title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css" />
<link rel="stylesheet" type="text/css" href="../stylesheets/autocomplete.css" />
<link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css"/>

<script type="text/javascript" src="../scripts/util.js"></script>

  
  
    
<script type="text/javascript">


if (!String.prototype.trim) {
	 String.prototype.trim = function() {
	  return this.replace(/^\s+|\s+$/g,'');
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
		w = document.getElementsByName("areaCode")[0].options[document.getElementsByName("areaCode")[0].selectedIndex].value;
     	v = document.getElementsByName("adslPhone")[0].value;
     	changeClient(v,w);
	}
	
	
	 function selectAll()
     {    
		 
		// alert("I am called select all now");
   	 
   	  for (var i = 0; i< document.getElementsByName("otherChargeList")[0].options.length; i++)
   	  {
   		  document.getElementsByName("otherChargeList")[0].options[i].selected = true;
   	  }
   	  
   	 
   	  return true;
    	 
     }
	
	function changeClient(v,w)
	{
		  
		  xmlHttp = GetXmlHttpObject();
		  if(xmlHttp == null)
		  {
			  alert("Browser is not supported.");
			  return false;
		  }    	  	
		  
		  v = v.trim(v);
		
		  	var url="../recharge/GetClient.do?adslPhone="+v+"&areaCode="+w;
		  	//alert(url);
		  	xmlHttp.onreadystatechange = ClientChanged;
		  	xmlHttp.open("GET",url,false);    	  	
		  	xmlHttp.send(null);    	  	
		  	return true;
	}

	function ClientChanged() 
	{ 
	  //alert("12:"+xmlHttp.responseText);
	   if (xmlHttp.readyState==4)
	   { 
	    // alert("11:"+document.getElementById("cl").innerHTML);
	     if(xmlHttp.responseText.charAt(1) == 'i')
	     document.getElementById("previous").innerHTML=xmlHttp.responseText;
	     changeChargeList(document.getElementsByName("clientID")[0].value);
	   
		  
	   }
	   
	   else
	 	 {
	 	 	//alert("Request failed.");
	 	 }
	}
	
	function add()
    {
  	 for (var i = 0; i< document.getElementsByName("otherChargeList")[0].length; i++)
 		 {
 		 	if(document.getElementById("charges").options[document.getElementById("charges").selectedIndex].value ==  document.getElementsByName("otherChargeList")[0].options[i].value )
		 	{
 		 		alert("The bonus has already been applied to this charges.");
		 		return false;
		 	}
 		 }
  	 
  	 var option=document.createElement("option");
  	 option.text = document.getElementById("charges").options[document.getElementById("charges").selectedIndex].text;
  	 option.value = document.getElementById("charges").options[document.getElementById("charges").selectedIndex].value;
  	 try
  	 {
  	  // for IE earlier than version 8
  	  	document.getElementsByName("otherChargeList")[0].add(option,document.getElementsByName("otherChargeList")[0].options[null]);
  	 }
  	 catch (e)
  	 {
  		document.getElementsByName("otherChargeList")[0].add(option,null);
  	 }
    }
    
    function remove()
    {
  	  document.getElementsByName("otherChargeList")[0].options[document.getElementsByName("otherChargeList")[0].selectedIndex] = null;
  	  return true;
    }
    
    function addAll()
    {
  	  document.getElementsByName("otherChargeList")[0].options.length = 0;
  	  for (var i = 0; i< document.getElementById("charges").options.length; i++)
  	  {
  		  var option=document.createElement("option");
 	     	  option.text = document.getElementById("charges").options[i].text;
  	      option.value = document.getElementById("charges").options[i].value;
  	      try
  	      {
  	     	  // for IE earlier than version 8
  	     	 document.getElementsByName("otherChargeList")[0].add(option,document.getElementsByName("otherChargeList")[0].options[null]);
  	      }
  	      catch (e)
  	      {
  	     	document.getElementsByName("otherChargeList")[0].add(option,null);
  	      }
  	  }
  	  
  	  return true;
   	 
   	 
    }
    
    function removeAll()
    {
  	  document.getElementsByName("otherChargeList")[0].options.length = 0;
  	  return true;
    }
    
	
    
    
    function changeChargeList(v)
    {
    	xmlHttp = GetXmlHttpObject();
		  if(xmlHttp == null)
		  {
			  alert("Browser is not supported.");
			  return false;
		  }    	  	
		  
  	 
  	    v = v.trim();
  	  	var url="../prepaidpayment/GetCharges.do?clientID="+v;
  	  //	alert(url);
  	  	xmlHttp.onreadystatechange = ChargeChanged;
  	  	xmlHttp.open("GET",url,false);    	  	
  	  	xmlHttp.send(null); 
  	  //	alert("I am done");
  	  	return true;
    }
    
    function ChargeChanged() 
    { 
  	  //alert(xmlHttp.responseText);
       if (xmlHttp.readyState==4)
       { 
        // alert(document.getElementById("chargeDiv").innerHTML);
         document.getElementById("chargeDiv").innerHTML=xmlHttp.responseText;
    	   removeAll();
       }
       
       else
     	 {
     	 	//alert("Request failed.");
     	 }
    }


    function init()
    {
    	var f = document.forms[0];
    	//alert(f.clientID.value);
    	if(f.clientID.value != 0)
    	{
    		changeClient(f.adslPhone.value,f.areaCode.value);
    		
    	}
    }


	
</script>

</head>

<body class="body_center_align"
	onload = "return init()">
	&nbsp;
	<table border="0" cellpadding="0" cellspacing="0" width="780">
		<tr>
			<td width="100%"><%@ include file="../includes/header.jsp"%></td>
		</tr>
		<tr>
			<td width="100%">
				<table border="0" cellpadding="0" cellspacing="0" width="780">
					<tr>
						
						<td width="600" valign="top" class="td_main" align="center">

							<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td width="100%" align="right"
										style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
										<div class="div_title">Prepaid Payment</div>
									</td>
								</tr>
								<tr>
									<td width="100%" align="center"><br /> 
									<html:form action="/AddPayment" method="POST" onsubmit = "return selectAll();" >
										<table border="0" cellpadding="1" class="form1" width="534">
											<%
												String msg = null;
													if ((msg = (String) session
															.getAttribute(util.ConformationMessage.PREPAID_PAYMENT_ADD)) != null) {
														session.removeAttribute(util.ConformationMessage.RECHARGE_CLIENT);
											%>
											<tr>
												<td width="530" colspan="2" align="center" valign="top"
													height="50"><b><%=msg%></b></td>
											</tr>
											<%
												}
											%>
											
																						
										<tr>
                        <td width="194" style="padding-left: 50px" align = "left" height="21">Area Code</td>
                        <td width="292" height="22" style="padding-left: 50px" align = "left" ><div id = "area" ><html:select  property="areaCode" onchange ="changeHandle();">                           
                            <%
					          RegionService rService = new RegionService();
					          ArrayList regionList = rService.getAllRegion();
					          for(int i=0;i<regionList.size();i++)
					        	
					          {
					            RegionDTO regionDTO = (RegionDTO)regionList.get(i);
					            if(regionDTO.getStatus() == RegionConstants.REGION_STATUS_ACTIVE)
					            {
					            %>
                            <html:option value="<%=Long.toString(regionDTO.getRegionDesc())%>"><%=regionDTO.getRegionName()%></html:option>
                            <%} } %>
                          </html:select></div> </td>
                      </tr>
                      	
											
										
										<tr>
												<td width="200" style="padding-left: 50px; font-size: 8pt" align="left">ADSL Phone Number</td>
												<td width="430" height="22" style="padding-left: 50px" align = "left" ><html:text  property="adslPhone" size="10" onchange="changeHandle()"/><label style="color:red; vertical-align: middle;" >*</label></td>
											</tr>
											  <tr>
													<td width="150"></td>
													<td width="430"><html:errors property="clientID" /></td>
												</tr>
											
											 <tr>
											 	<td width="200" style="padding-left: 50px; font-size: 8pt" align="left">Client</td>
												<td width="430" height="22"><div id = "previous"><html:hidden property="clientID"/><label id ="client"></label></div>
												</td>
											</tr>
										
										
										<tr>
												<td width="430" height="22"><html:hidden property="receiverID" value = "<%=Long.toString(loginDTO.getUserID()) %>"/>
												</td>
											</tr>
										
										  <tr >
						                      <td width="195" height="59" style="padding-left: 50px" align = "left" >Unpaid Charges</td>
						                      <td width="100" height="59" style="padding-left: 50px" align = "left" > <div  id = "chargeDiv"  "><select id="charges"  size="1">                        
						                       
						                        </select></div> </td>
						                        </tr>
						                        <tr>
						                        <td width="54" rowspan="1" style="padding-left: 50px" align = "left" >
						                        <frame onload = "init();"></frame>
						                        <table   >
						                        <tr ><input class="cmd"  type="button" value="Add" name="AddCharge" onClick = "add();">&nbsp;</tr>
						                        <tr><input class="cmd"  type="button" value="Remove" name="RemoveCharge" onClick = "remove();">&nbsp;</tr>
						                        <tr><input class="cmd"  type="button" value="Add All" name="AddAll" onClick = "addAll();">&nbsp;</tr>
						                        <tr><input class="cmd"  type="button" value="Remove All" name="RemoveAll" onClick = "removeAll()">&nbsp;</tr>
						                        </table>
						                        </td>
						                        
						                        <td width = "254">
						                        <html:select property="otherChargeList" multiple = "true" >
						                        
						                        </html:select>
						                        
						                        </td>
						                    </tr>
										
										  <tr>
													<td width="150"></td>
													<td width="430"><html:errors property="othercharge" /></td>
												</tr>
											<tr> 
										
											<tr>
												<td width="200" style="padding-left: 50px; font-size: 8pt" align ="left">Payment Amount</td>
												<td width="430" height="22" style="padding-left: 50px" align = "left" ><html:text property="paymentAmount" size="10" /> taka<label style="color:red; vertical-align: middle;" >*</label></td>
											</tr>
											  <tr>
													<td width="150"></td>
													<td width="430"><html:errors property="paymentAmount" /></td>
												</tr>
											<tr> 
												<td width="200" style="padding-left: 50px; font-size: 8pt" align="left">Scroll No</td>
												<td width="430" height="22" style="padding-left: 50px" align = "left" ><html:text property="scrollNo" size="10" /><label style="color:red; vertical-align: middle;" >*</label></td>
											</tr>
											<tr>
													<td width="150"></td>
													<td width="430"><html:errors property="scrollNo" /></td>
												</tr>
											<tr>
											
											 <tr>
                            <td  style="padding-left: 50px" align = "left"  >
                            Payment Date
                            </td>
                            <td style="padding-left: 50px" align = "left"  >
                              <html:text property = "paymentTime" size="21" readonly="readonly"
                              value="<%=format.format(new Date(System.currentTimeMillis())) %>"/>
                              <script type="text/javascript" >
                              new tcal ({
                                // form name
                                'formname': 'prepaidPaymentForm',
                                // input name
                                'controlname': 'paymentTime'
                              });
                              </script>
                            </td>
                          </tr>
                       <tr>
													<td width="150"></td>
													<td width="430"><html:errors property="paymentTime" /></td>
												</tr>
												<td width="200" style="padding-left: 50px; font-size: 8pt" align="left">Comments</td>
												<td width="430" height="22" style="padding-left: 50px" align = "left" ><html:textarea property="description" /> </td>
											</tr>
											<tr>
													<td width="150"></td>
													<td width="430"><html:errors property="paymentDescripttion" /></td>
												</tr>
											
										</table>
										<table border="0" cellpadding="0" cellspacing="0" width="89%" class="form1">
										
											<tr>
												<td width="100%" height="31"><br></td>
											</tr>
											<tr>
												<td width="100%" align="center">
												<input type="reset" value="Reset" name="B1">
												<input type="submit" value="Add Payment" >
												</td>
											</tr>
										</table>
										<br>
										<br>
									</html:form> <br></td>
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
