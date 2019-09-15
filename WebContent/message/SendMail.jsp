<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="mail.form.MailForm"%>
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
	String submitCaption = "Send Mail";
	
%>

<html>
<head>
<html:base />
<title>Send Mail</title>
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
	
	
	function init()
	{
		if(form.clientID.value != 0)
		{
			changeClient(form.adslPhone.value,form.areaCode.value);
		}
	}
	



	
</script>

</head>

<body class="body_center_align"
	onload="init();">
	&nbsp;
	<table border="0" cellpadding="0" cellspacing="0" width="1024">
		<tr>
			<td width="100%"><%@ include file="../includes/header.jsp"%></td>
		</tr>
		<tr>
			<td width="100%">
				<table border="0" cellpadding="0" cellspacing="0" width="1024">
					<tr>
						
						<td width="600" valign="top" class="td_main" align="center">

							<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td width="100%" align="right"
										style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
										<div class="div_title">Send Mail</div>
									</td>
								</tr>
								<tr>
									<td width="100%" align="center"><br /> 
									<html:form action="/sendMail" method="POST">
										<table border="0" cellpadding="1" class="form1" width="534">
											<%
												String msg = null;
													if ((msg = (String) session
															.getAttribute(util.ConformationMessage.MAIL_SENT)) != null) {
														session.removeAttribute(util.ConformationMessage.MAIL_SENT);
											%>
											<tr>
												<td width="530" colspan="2" align="center" valign="top"
													height="50"><b><%=msg%></b></td>
											</tr>
											<%
												}
											%>
											
											
											
										<tr>
                        <td width="194" style="padding-left: 70px" height="21" align = "left">Area Code</td>
                        <td width="292" height="22"><div id = "area"  align = "left" style="padding-left: 70px"><html:select property="areaCode" onchange ="changeHandle();">                           
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
												<td width="200" style="padding-left: 70px; font-size: 8pt" align = "left">ADSL Phone Number</td>
												<td width="430" height="22" align = "left" style="padding-left: 70px"><html:text property="adslPhone" size="10" onchange="changeHandle()"/><label style="color:red; vertical-align: middle;" >*</label></td>
											</tr>
											  <tr>
													<td width="150" align = "left" style="padding-left: 70px"></td>
													<td width="430" align = "left" style="padding-left: 70px"><html:errors property="adslPhone" /></td>
												</tr>
											
											 <tr>
											 <td width="200" style="padding-left: 70px; font-size: 8pt" align = "left">Client</td>
												<td width="430" height="22" align = "left" style="padding-left: 70px"><div id = "previous"><html:hidden property="clientID"/><label id ="client"></label></div>
												</td>
											</tr>
										
										
										<tr>
												<td width="200" style="padding-left: 70px;font-size: 8pt" align = "left">Subject</td>
												<td width="430" height="22" align = "left" style="padding-left: 70px"><html:text property="subject" /><label style="color:red; vertical-align: middle;" >*</label> </td>
											</tr>
											<tr>
													<td width="150" align = "left" style="padding-left: 70px"></td>
													<td width="430" align = "left" style="padding-left: 70px"><html:errors property="subject" /></td>
												</tr>
											<tr>
												<td width="200" style="padding-left: 70px;font-size: 8pt" align = "left">Message</td>
												<td width="430" height="22" align = "left" style="padding-left: 70px"><html:textarea property="msg" /><label style="color:red; vertical-align: middle;" >*</label> </td>
											</tr>
											<tr>
													<td width="150" align = "left" style="padding-left: 70px"></td>
													<td width="430" align = "left" style="padding-left: 70px"><html:errors property="msg" /></td>
												</tr>
											
										</table>
										<table border="0" cellpadding="0" cellspacing="0" width="89%" class="form1">
										
											<tr>
												<td width="100%" height="31" align = "left"><br></td>
											</tr>
											<tr>
												<td width="500" align="left" style="padding-left: 250px">
												<input type="reset" value="Reset" name="B1">
												<input type="submit" value="Send Mail">
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
