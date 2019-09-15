<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="shifting.form.ShiftClientForm"%>
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
<%@ page import="java.util.*,sessionmanager.SessionConstants,java.io.*,packages.*,dslm.*,regiontype.*,exchange.*"%>
<%
    String submitCaption = "Shift Client";
	ShiftClientForm sform = (ShiftClientForm)request.getAttribute("shiftClientForm");
	if(sform == null)
	{
		sform = new ShiftClientForm();
	}
%>
<%@page errorPage="../common/failure.jsp" %>
<html>
    <head>
        <html:base />
        <title>Shift Client</title>
        <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css" />
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
       		w = document.getElementsByName("oldAreaCode")[0].options[document.getElementsByName("oldAreaCode")[0].selectedIndex].value;
       		v = document.getElementsByName("oldTelephone")[0].value;
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
       		
       		  	var url="../shifting/GetClient.do?adslPhone="+v+"&areaCode="+w;
       		  //	alert(url);
       		  	xmlHttp.onreadystatechange = function(){ClientChanged(xmlHttp);};
       		  	xmlHttp.open("GET",url,false);    	  	
       		  	xmlHttp.send(null);    	  	
       		  	return true;
       	}

       	function ClientChanged(xmlHttp) 
       	{ 
       	 //  alert(xmlHttp.responseText);
       	   if (xmlHttp.readyState==4)
       	   { 
       	   //  alert(document.getElementById("previous").innerHTML);
       	     document.getElementById("previous").innerHTML=xmlHttp.responseText;
       	  	 xmlHttp.onreadystatechange = empty;
       		  
       	   }
       	   
       	   else
       	 	 {
       	 	 	//alert("Request failed.");
       	 	 }
       	}


        
        
        
        
            
            
            
            function changeCode(v)
            {
            	document.getElementById("code").innerHTML = ""+v+"";
            }
            
            function init()
            {
            	changeCode(document.getElementsByName("areaCode")[0].options[0].value);
            }
            
            
            
            
            
            
            function changeDslm(v)
            {
          	 
          	  //alert("I am Called" +v);
          	  xmlHttp = GetXmlHttpObject();
          	  if(xmlHttp == null)
          	  {
          		  alert("Browser is not supported.");
          		  return false;
          	  } 
          	  var url="../dslm/GetShiftDslms.do?exCode="+v+"&time="+new Date().getTime();
          	//  alert(url);
          	  xmlHttp.onreadystatechange = function(){DslmChanged(xmlHttp);};
          	  xmlHttp.open("GET",url,false);    	  	
          	  xmlHttp.send(null);    	  	
          	  return true;
            }
            
            function DslmChanged(xmlHttp) 
            { 
          	 
               if (xmlHttp.readyState==4)
               { 
              	 //alert(xmlHttp.responseText);
                	// alert(document.getElementById("dslm"));	
                	 
                 document.getElementById("dslm").innerHTML=xmlHttp.responseText;
            	  // removeAll();
                 xmlHttp.onreadystatechange = empty;
               }
               
               else
             	 {
             	 	//alert("Request failed.");
             	 }
            }
            
            
            function changeHandleNew()
            {
              v = document.getElementsByName("areaCode")[0].options[document.getElementsByName("areaCode")[0].selectedIndex].value;
           	  w = document.getElementsByName("newTelephone")[0].value;
           	  changeExchange(v,w);
            }
            
            
            function changeExchange(v,w)
            {
          	 
          	// alert("I am Called" +v);
          	  xmlHttp = GetXmlHttpObject();
          	  if(xmlHttp == null)
          	  {
          		  alert("Browser is not supported.");
          		  return false;
          	  } 
          	  var url="../dslm/GetExchange.do?areaCode="+v+"&adslPhone="+w;
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
                	// alert(document.getElementById("exchange"));
                 
                 if(xmlHttp.responseText.substr(0,31) == "<select name = 'dslmExchangeNo'")
                 	document.getElementById("exchange").innerHTML=xmlHttp.responseText;          
                 if(document.getElementsByName("dslmExchangeNo")[0].options.length>0)
               		changeDslm(document.getElementsByName("dslmExchangeNo")[0].options[0].value);
                  xmlHttp.onreadystatechange = empty;
            	  // removeAll();
               }
               
               else
             	 {
             	 	//alert("Request failed.");
             	 }
              
            }
            
        </script>

    </head>

    <body class="body_center_align"
          onload="init();">
        &nbsp;
        <table border="0" cellpadding="0" cellspacing="0" width="780">
            <tr>
                <td width="100%"><%@ include file="../includes/header.jsp"%></td>
            </tr>
            <tr>
                <td width="100%">
                    <table border="0" cellpadding="0" cellspacing="0" width="780">
                        <tr>
                           
                            <td width="780" valign="top" class="td_main" align="center">

                                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                    <tr>
                                        <td width="100%" align="right"
                                            style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
                                            <div class="div_title">Shift Client</div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="100%" align="center" style="padding-left:180px"><br /> 
                                            <html:form action="/shiftClient" method="POST" onsubmit="return validate(this);">
                                                <table border="0" cellpadding="1" class="form1" width="500">
                                                    <%
                                                        String msg = null;
                                                        if ((msg = (String) session
                                                                .getAttribute(util.ConformationMessage.SHIFT_CLIENT)) != null) {
                                                            session.removeAttribute(util.ConformationMessage.SHIFT_CLIENT);
                                                    %>
                                                    <tr>
                                                        <td width="530" colspan="2" align="center" valign="top"
                                                            height="50"><b><%=msg%></b></td>
                                                    </tr>
                                                    <%
                                                        }
                                                    %>
                                                  <tr>
												<td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">ADSL Phone Number</td>
												<td width="430" height="22" align="left"><html:text property="oldTelephone" size="10" onchange="changeHandle()"/><label style="color:red; vertical-align: middle;" >*</label></td>
											</tr>  
											<tr>
													<td width="150" align="left"></td>
													<td width="430" align="left"><html:errors property="shiftOldPhoneNo" /></td>
												</tr>
											
											<tr>

					<td width="150" style="padding-right: 0" height="21" align="left">Old Area Code</td>
					                        <td width="430" height="22" align="left"> <select name="oldAreaCode" onchange = "changeHandle();">                           
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
					                          </select> </td>
					                      </tr>
                                                    
                                       </table>
                                       <div id = "previous">
                                       
                                      <table border="0" cellpadding="1" class="form1" width="500">             
										
											
											
											 <tr>
												<td width="150" style="padding-right: 0" align="left">Client</td>
												<td width="430" height="22" align="left" style="padding-left: 30px"><label id ="client"><%=(sform.getClientUserName() == null ||sform .getClientUserName().equals(""))?"":sform.getClientUserName()%></label>
												</td>
											</tr>
											
										<tr>
												<td width="150" style="padding-right: 0" align="left">Old Dslm</td>
												<td width="430" height="22" align="left" style="padding-left: 30px"><label><%=(sform.getDslmName() == null ||sform .getDslmName().equals(""))?"":sform.getDslmName()%></label>
												</td>
											</tr>	
											
											
											<tr>
												<td width="150" style="padding-right: 0" align="left">Old Port</td>
												<td width="430" height="22" align="left" style="padding-left: 30px"><label><%=(sform.getOldPort()==0)?"0":sform.getOldPort()+""%></label>
												</td>
											</tr>	
											
											
											<tr>
												<td width="150" style="padding-right: 0" align="left">Previously Bounded to Port</td>
												<td width="430" height="22" align="left" style="padding-left: 30px"><label><%=(sform.getBindStatus() == null ||sform.getBindStatus().equals(""))?"":sform.getBindStatus()%></label>
												</td>
											</tr>	
											
									<tr>
												<td width="150" style="padding-right: 0" align="left">Old Exchange</td>
												<td width="430" height="22" align="left" style="padding-left: 30px"><label><%=(sform.getExchangeName() == null ||sform .getExchangeName().equals(""))?"":sform.getExchangeName()%></label>
												</td>
											</tr>	
											
											<tr>
												<td width="150" style="padding-right: 0" align="left">Old Address</td>
												<td width="430" height="22" align="left" style="padding-left: 30px"><label><%=(sform.getOldAddress() == null ||sform .getOldAddress().equals(""))?"":sform.getOldAddress()%></label>
												</td>
											</tr>	
											</table>
											</div>
											  <table border="0" cellpadding="1" class="form1" width="500">
                                                    
                                                    

                                                  <%--   <tr>
                                                        <td width="150" style="padding-right: 0">Client Id</td>
                                                        <td width="430" height="22"><html:select property="clientID" size="1" >
                                                                <%
                                                                    ClientRepository cr = ClientRepository.getInstance();
                                                                    ArrayList cList = cr.getClientList();
                                                                    for (int i = 0; i < cList.size(); i++) {
                                                                        if (((ClientDTO) cList.get(i)).getAccountStatus() != ClientDTO.CUSTOMER_STATUS_REMOVED && ((ClientDTO) cList.get(i)).getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED) {
                                                                %>
                                                                <html:option value="<%=Long.toString(((ClientDTO) cList.get(i)).getUniqueID())%>"><%=((ClientDTO) cList.get(i)).getUserName()%></html:option>
                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </html:select></td>
                                                    </tr> --%>

                                      
                                      
                                      <tr>
                                                        <td width="150" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">New Telephone</td>
                                                        <td width="430" height="22" align="left"><label id ="code"></label><html:text property="newTelephone" size="15" onchange = "changeHandleNew();" /><label style="color:red; vertical-align: middle;" >*</label></td>
                                                    </tr>            
                                                    <tr>
													<td width="150" align="left"></td>
													<td width="430" align="left"><html:errors property="shiftNewPhoneNo" /></td>
												</tr>

<tr>

					<td width="150" style="padding-right: 0" height="21" align="left">New Area Code</td>
					                        <td width="430" height="22" align="left"> <select name="areaCode" onchange = "changeCode(this.value);changeHandleNew();">                           
					                            <%
										          rService = new RegionService();
										          regionList = rService.getAllRegion();
										          for(int i=0;i<regionList.size();i++)
										        	
										          {
										            RegionDTO regionDTO = (RegionDTO)regionList.get(i);
										            if(regionDTO.getStatus() == RegionConstants.REGION_STATUS_ACTIVE)
										            {
										            %>
					                            <option value="<%=regionDTO.getRegionDesc()%>"><%=regionDTO.getRegionName()%></option>
					                            <%} } %>
					                          </select> </td>
					                      </tr>
					                      



					<tr>
                      <td width="200" height="22" align="left">Exchange Name</td>
                      <td width="292" height="22" align="left"><div id = "exchange"> <select name="dslmExchangeNo" size ="1" onchange="changeDslm(this.value);">
                      <%
                      long ac = ((RegionDTO)rService.getAllRegion().get(0)).getRegionDesc();
                     
                      	for(int i = 0 ;i < ExchangeRepository.getInstance().getExchangeList().size();i++)
                      	{
                      		ExchangeDTO row = (ExchangeDTO) ExchangeRepository.getInstance().getExchangeList().get(i);
                      		if(row.getExNWD() == ac)
                      		{
                      			
                      			if(row.getExCode()== 1000)
                      			{
                      %>
                      
                       <option value="<%=Integer.toString(row.getExCode())%>" selected = "selected"><%=row.getExName()%></option>
                      <%}else{ %>
                      <option value="<%=Integer.toString(row.getExCode())%>"><%=row.getExName()%></option>
                      <%}}} %>
                      </select></div></td>
                    </tr>       


 <tr>
                      <td width="200" height="22" align="left">New DSLM Name</td>
                      <td width="292" height="22" align="left"> <div id = "dslm"><html:select property="newDslm">
                      <%
                      
                      	for(int i = 0 ;i < DslmRepository.getInstance().getDslmList().size();i++)
                      	{
                      		DslmDTO row = (DslmDTO) DslmRepository.getInstance().getDslmList().get(i);
                      		if(row.getDslmStatus()==DslmConstants.DSLM_ACTIVE && row.getDslmExchangeNo() == 1000)
                      		{
                      %>
                      
                      <html:option value="<%=Long.toString(row.getDslmID())%>"><%=row.getDslmName() %></html:option>
                      <%}} %>
                      </html:select></div></td>
                    </tr>

 <tr>
													<td width="150" align="left"></td>
													<td width="430" align="left"><html:errors property="newDslm" /></td>
												</tr>





							<%-- <tr>
							                      <td width="150" height="22">New DSLM Name</td>
							                      <td width="430" height="22"> <div id = "dslm"></div><html:select property="newDslm">
							                      <%
							                      	for(int i = 0 ;i < DslmRepository.getInstance().getDslmList().size();i++)
							                      	{
							                      		DslmDTO row = (DslmDTO) DslmRepository.getInstance().getDslmList().get(i);
							                      		if(row.getDslmStatus()==DslmConstants.DSLM_ACTIVE)
							                      		{
							                      %>
							                      
							                      <html:option value="<%=row.getDslmName()%>"><%=row.getDslmName() %></html:option>
							                      <%}} %>
							                      </html:select></div></td>
							                    </tr> --%>
                                                   
                                                    
                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">New Port</td>
                                                        <td width="430" height="22" align="left"><html:text property="newPort" size="15"/> <label style="color:red; vertical-align: middle;" >*</label></td>
                                                    </tr>
                                                  <tr>
													<td width="150" align="left"></td>
													<td width="430" align="left"><html:errors property="shiftNewPort" /></td>
												</tr>
                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Bind to Port</td>
                                                        <td width="430" height="22" align="left"><html:checkbox property="bindToPort" /> </td>
                                                    </tr>
                                                    
                                                 
                      
                      
                        
                                                    
                                                    <tr>
                                                        <td width="150" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">New Address</td>
                                                        <td width="430" height="22" align="left"><html:textarea property="newAddress" /><label style="color:red; vertical-align: middle;" >*</label> </td>
                                                    </tr>
                                                   
                                                     <tr>
													<td width="150" align="left"></td>
													<td width="430" align="left"><html:errors property="newAddress" /></td>
												</tr>
                                                    <tr>
                                                        <td width="150" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Shifting Invoice No</td>
                                                        <td width="430" height="22" align="left"><html:text property="invoiceNo" /><label style="color:red; vertical-align: middle;" >*</label> </td>
                                                    </tr>
                                                    
                                                <tr>
													<td width="150" align="left"></td>
													<td width="430" align="left"><html:errors property="shiftInvoiceNo" /></td>
												</tr>
                                                    
                                                    <tr>
                                                        <td width="150" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Shifting Charge</td>
                                                        <td width="430" height="22" align="left"><html:text property="cost" /><label style="color:red; vertical-align: middle;" >*</label> </td>
                                                    </tr>
                                                    
                                                <tr>
													<td width="150" align="left"></td>
													<td width="430" align="left"><html:errors property="shiftCharge" /></td>
												</tr>
                                                    
                                                    <tr>
                                                        <td width="150" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Comments</td>
                                                        <td width="430" height="22" align="left"><html:textarea property="description" /> </td>
                                                    </tr>
                                                    
                                                    <tr>
													<td width="150" align="left"></td>
													<td width="430" align="left"><html:errors property="shiftComments" /></td>
												</tr>
												
												

                                                </table>
                                                <table border="0" cellpadding="0" cellspacing="0" width="89%" class="form1">

                                                    <tr>
                                                        <td width="100%" height="31" align="left"><br></td>
                                                    </tr>
                                                    <tr>
                                                        <td width="100%" align="left" style="padding-left:100px">
                                                            <input type="reset" value="Reset" name="B1">
                                                            <input type="submit" value="Shift Client">
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

