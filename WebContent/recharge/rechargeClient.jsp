<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="recharge.form.RechargeClientForm"%>
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
<%@ page import="java.util.*,sessionmanager.SessionConstants,java.io.*,packages.*"%>
<%
	String submitCaption = "Recharge Client";
	RechargeClientForm sform = (RechargeClientForm)request.getAttribute("rechargeClientForm");
	if(sform == null)
	{
		sform = new RechargeClientForm();
	}
%>

<%@page errorPage="../common/failure.jsp" %>

<html>
<head>
<html:base />
<title>Recharge Client</title>
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
		  //	alert(url);
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
	   
		  
	   }
	   
	   else
	 	 {
	 	 	//alert("Request failed.");
	 	 }
	}
	



	function validate(form){
		var ob = form.clientID;
		if(isEmpty(ob.value)){
			alert("Please select a client.");
			ob.focus();
			return false;
		}
		
		ob=form.rechargeAmount;
		if(isEmpty(ob.value)){
			ob.value=0.0;
			return false;
		} else if(!isNum(ob.value) || ob.value <0){
			alert("Recharge Amount must be a positive number.");
			ob.value="";
			ob.focus();
			return false;
		}
		
		ob=form.voucherNo;
		if(isEmpty(ob.value)){
			ob.value="";
			ob.focus();
			alert("Please Enter a voucher no.");
			return false;
		}
		return true;
	}
</script>

</head>

<body class="body_center_align"
	>
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
										<div class="div_title">Recharge Client</div>
									</td>
								</tr>
								<tr>
									<td width="100%" align="center"><br /> 
									<html:form action="/rechargeClient" method="POST" onsubmit="return validate(this);">
										<table border="0" cellpadding="1" class="form1" width="534">
											<%
												String msg = null;
													if ((msg = (String) session
															.getAttribute(util.ConformationMessage.RECHARGE_CLIENT)) != null) {
														session.removeAttribute(util.ConformationMessage.RECHARGE_CLIENT);
											%>
											<tr>
												<td width="530" colspan="2" align="center" valign="top"
													height="50"><b><%=msg%></b></td>
											</tr>
											<%
												}
											%>
											
											<!-- <tr>
											<td>test</td>
											<td>
											<input type="text" name="text" autocomplete="off">

											<div class="shadow" id="shadow">
											<div class="output" id="output">
											</div>
											</div>
											</td>
											</tr>
 -->
											<%-- <tr>
												<td width="150" style="padding-right: 0">Client Id</td>
												<td width="430" height="22"><html:select property="clientID" size="1" >
												<%
												ClientRepository cr= ClientRepository.getInstance();
												ArrayList cList= cr.getClientList();
												for(int i=0;i<cList.size();i++){
													if(((ClientDTO)cList.get(i)).getAccountStatus() != ClientDTO.CUSTOMER_STATUS_REMOVED){
												%>
													<html:option value="<%=Long.toString(((ClientDTO)cList.get(i)).getUniqueID()) %>"><%=((ClientDTO)cList.get(i)).getUserName() %></html:option>
												<%
													}
												}
												%>
												</html:select></td>
											</tr> --%>
											
										<tr>
                        <td width="194" style="padding-left: 70px" height="21" align = "left">Area Code</td>
                        <td width="292" height="22"><div id = "area"  align = "left" style="padding-left: 70px"><select name="areaCode" onchange ="changeHandle();">                           
                            <%
					          RegionService rService = new RegionService();
					          ArrayList regionList = rService.getAllRegion();
					          for(int i=0;i<regionList.size();i++)
					        	
					          {
					            RegionDTO regionDTO = (RegionDTO)regionList.get(i);
					            if(regionDTO.getStatus() == RegionConstants.REGION_STATUS_ACTIVE)
					            {
					            %>
                            <option value="<%=regionDTO.getRegionDesc()%>"><%=regionDTO.getRegionName()%></option>
                            <%} } %>
                          </select></div> </td>
                      </tr>
                      	
											
										
										<tr>
												<td width="200" style="padding-left: 70px; font-size: 8pt" align = "left">ADSL Phone Number</td>
												<td width="430" height="22" align = "left" style="padding-left: 70px"><html:text property="adslPhone" size="10" onchange="changeHandle()"/><label style="color:red; vertical-align: middle;" >*</label></td>
											</tr>
											  <tr>
													<td width="150" align = "left" style="padding-left: 70px"></td>
													<td width="430" align = "left" style="padding-left: 70px"><html:errors property="rechargeClientID" /></td>
												</tr>
											
											 <tr>
											 <td width="200" style="padding-left: 70px; font-size: 8pt" align = "left">Client</td>
												<td width="430" height="22" align = "left" style="padding-left: 70px"><div id = "previous"><html:hidden property="clientID"/><label id ="client"><%=(sform.getClientUserName() == null ||sform .getClientUserName().equals(""))?"":sform.getClientUserName()%></label></div>
												</td>
											</tr>
										
										
										
											<%-- <tr>
												<td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt">Recharge Amount</td>
												<td width="430" height="22"><html:text property="rechargeAmount" size="10" /> taka</td>
											</tr>
											  <tr>
													<td width="150"></td>
													<td width="430"><html:errors property="rechargeAmount" /></td>
												</tr>
											<tr> --%>
												<td width="200" style="padding-left: 70px"; font-size: 8pt" align = "left">Invoice/Bill No</td>
												<td width="430" height="22" align = "left" style="padding-left: 70px"><html:text property="voucherNo" size="10" /><label style="color:red; vertical-align: middle;" >*</label></td>
											</tr>
											<tr>
													<td width="150" align = "left" style="padding-left: 70px"></td>
													<td width="430" align = "left" style="padding-left: 70px"><html:errors property="rechargeInvoiceNo" /></td>
												</tr>
											<tr>
												<td width="200" style="padding-left: 70px;font-size: 8pt" align = "left">Comment</td>
												<td width="430" height="22" align = "left" style="padding-left: 70px"><html:textarea property="comment" /> </td>
											</tr>
											<tr>
													<td width="150" align = "left" style="padding-left: 70px"></td>
													<td width="430" align = "left" style="padding-left: 70px"><html:errors property="rechargeComment" /></td>
												</tr>
											
										</table>
										<table border="0" cellpadding="0" cellspacing="0" width="89%" class="form1">
										
											<tr>
												<td width="100%" height="31" align = "left"><br></td>
											</tr>
											<tr>
												<td width="500" align="left" style="padding-left: 250px">
												<input type="reset" value="Reset" name="B1">
												<input type="submit" value="Recharge Client">
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
