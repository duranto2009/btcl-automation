<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page errorPage="../common/failure.jsp" %>

<%@page import="remove.form.RemoveClientForm"%>
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
    String submitCaption = "Remove Client";
	RemoveClientForm sform = (RemoveClientForm)request.getAttribute("removeClientForm");
	if(sform == null)
	{
		sform = new RemoveClientForm();
	}
%>

<html>
    <head>
        <html:base />
        <title>Remove Client</title>
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
    	  // alert("12:"+xmlHttp.responseText);
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
                                            <div class="div_title">Remove Client</div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="100%" align="center" style="padding-left:210px"><br /> 
                                            <html:form action="/removeClient" method="POST" >
                                                <table border="0" cellpadding="1" class="form1" width="534">
                                                    <%
                                                        String msg = null;
                                                        if ((msg = (String) session
                                                                .getAttribute(util.ConformationMessage.REMOVE_CLIENT)) != null) {
                                                            session.removeAttribute(util.ConformationMessage.REMOVE_CLIENT);
                                                    %>
                                                    <tr>
                                                        <td width="530" colspan="2" align="center" valign="top"
                                                            height="50"><b><%=msg%></b></td>
                                                    </tr>
                                                    <%
                                                        }
                                                    %>
                                                    
                                                    				<tr>
                        <td width="194" style="padding-right: 0" height="21" align="left">Area Code</td>
                        <td width="292" height="22" align="left"><div id = "area" ><select name="areaCode" onchange ="changeHandle();">                           
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
												<td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">ADSL Phone Number</td>
												<td width="430" height="22" align="left"><html:text property="adslPhone" size="10" onchange="changeHandle()"/><label style="color:red; vertical-align: middle;" >*</label></td>
											</tr>
											
											<tr>
													<td width="150" align="left"></td>
													<td width="430" align="left"><html:errors property="removeClientID" /></td>
												</tr>
											 <tr>
												<td width="150" style="padding-right: 0" align="left">Client</td>
												<td width="430" height="22" align="left"><div id = "previous"><html:hidden property="clientID"/><label id ="client"><%=(sform.getClientUserName()== null ||sform .getClientUserName().equals(""))?"":sform.getClientUserName()%></label></div>
												</td>
											</tr>
										

                                                    <%-- <tr>
                                                        <td width="150" style="padding-right: 0">Client Id</td>
                                                        <td width="430" height="22"><html:select property="accountNo" size="1" >
                                                                <%
                                                                    ClientRepository cr = ClientRepository.getInstance();
                                                                    ArrayList cList = cr.getClientList();
                                                                    for (int i = 0; i < cList.size(); i++) {
                                                                        if (((ClientDTO) cList.get(i)).getAccountStatus() != ClientDTO.CUSTOMER_STATUS_REMOVED) {
                                                                %>
                                                                <html:option value="<%=Long.toString(((ClientDTO) cList.get(i)).getUniqueID())%>"><%=((ClientDTO) cList.get(i)).getUserName()%></html:option>
                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </html:select></td>
                                                    </tr> --%>

                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Reference Letter No</td>
                                                        <td width="430" height="22" align="left"><html:text property="referenceLetterNo" size="10" /><label style="color:red; vertical-align: middle;" >*</label></td>
                                                    </tr>
<tr>
													<td width="150" align="left"></td>
													<td width="430" align="left"><html:errors property="removeReference" /></td>
												</tr>
                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Comment</td>
                                                        <td width="430" height="22" align="left"><html:textarea property="comment" /> </td>
                                                    </tr>
  <tr>
													<td width="150" align="left"></td>
													<td width="430" align="left"><html:errors property="removeComments" /></td>
												</tr>
                                                </table>
                                                <table border="0" cellpadding="0" cellspacing="0" width="89%" class="form1">

                                                    <tr>
                                                        <td width="100%" height="31" align="left"><br></td>
                                                    </tr>
                                                    <tr>
                                                        <td width="100%" align="left"  style="padding-left:70px">
                                                            <input type="reset" value="Reset" name="B1">
                                                            <input type="submit" value="Remove Client">
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

