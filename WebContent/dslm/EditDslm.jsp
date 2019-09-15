<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="dslm.form.DslmForm"%>
<%@page import="exchange.ExchangeDTO"%>
<%@page import="exchange.ExchangeRepository"%>
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page errorPage="../common/failure.jsp" %>


<%@ page import="java.util.ArrayList,
sessionmanager.SessionConstants,

java.sql.*,
databasemanager.*,dslm.*,regiontype.*,exchange.*" %>				 
				 

<%
 String title = "Edit DSLM";
 String submitCaption2 = "Update";
 String actionName = "/UpdateDslm";
 DslmService fservice = new DslmService();
%>

<html>
<head>
<html:base/>
<meta name="GENERATOR" content="Microsoft FrontPage 5.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title><%=title %></title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>

<script language="JavaScript">
	 
	 
if (!String.prototype.trim) {
	 String.prototype.trim = function() {
	  return this.replace(/^\s+|\s+$/g,'');
	 }
}


function goBack()
{
	window.history.back()
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


	function changeExchange(v)
	{
		  
		  xmlHttp = GetXmlHttpObject();
		  if(xmlHttp == null)
		  {
			  alert("Browser is not supported.");
			  return false;
		  }    	  	
		  
		  v = v.trim(v);
		
		  	var url="../dslm/GetExchange.do?areaCode="+v;
		  	//alert(url);
		  	xmlHttp.onreadystatechange = ExchangeChanged;
		  	xmlHttp.open("GET",url,false);    	  	
		  	xmlHttp.send(null);    	  	
		  	return true;
	}

	function ExchangeChanged() 
	{ 
	  // alert(xmlHttp.responseText);
	   if (xmlHttp.readyState==4)
	   { 
	     //alert(document.getElementById("previous").innerHTML);
	     document.getElementById("previous").innerHTML=xmlHttp.responseText;
		  
	   }
	   
	   else
	 	 {
	 	 	//alert("Request failed.");
	 	 }
	}


	 function validate()
      {
        var f = document.forms[0];

        var ob = f.dslmName;
        if( isEmpty(ob.value))
        {
          alert("Please enter Dslm Name");
          ob.value = "";
          ob.focus();
          return false;
        }

       
        return true;
      }

</script>

</head>

<body class="body_center_align" onload="document.forms[0].dslmName.focus();">


<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="780" id="AutoNumber1">

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/header.jsp"  %>
    </td>
  </tr>

  <!--center-->
  <tr>
    <td width="100%">
    <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="780" id="AutoNumber2">
      <tr>
	    <!--left menu-->
       
	    <!--main -->
        <td width="600" valign="top" class="td_main" align="center">

        <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber3">
          <tr>
            <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
            <div class="div_title"><%=title %></div>
			</td>
          </tr>
		 
            <td width="100%" align="center">

<br><br>
<!-- start of the form  -->

<html:form  action="<%=actionName %>" method="POST" onsubmit="return validate();">
                  <table  width="500" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                    <%
  String msg = null;
  if( (msg = (String)session.getAttribute(util.ConformationMessage.DSLM_UPDATE))!= null)
  {
    session.removeAttribute(util.ConformationMessage.DSLM_UPDATE);
    %>				 
                    <tr> 
                      <td colspan="3" align="center" valign="top" height="50"><b><%=msg%></b></td>
                    </tr>
                    <%}%>
                    <html:hidden property="dslmID" />
                    
                    
                    
                    
                     <%
                     	 DslmForm dform = ((DslmForm)request.getAttribute("dslmForm"));
                     	 long dslmID = ((DslmForm)request.getAttribute("dslmForm")).getDslmID();
                     	
                         String  bstatus = ""+DslmRepository.getInstance().getDslm(dslmID).getDslmStatus();
                         dform.setDslmStatus(Integer.parseInt(bstatus));
                        
                         
                         
                     %>
                   
                     <bean:define id="da" scope="request"> <bean:write name="dslmForm" property = "dslmAreaCode"/></bean:define>
                     <bean:define id="de" scope="request"> <bean:write name="dslmForm" property = "dslmExchangeNo"/></bean:define> 
                    
                    
                   <%if(fservice.hasClient(""+dslmID)) {%> 
					 <TR> 
                      <TD valign="top" height="22" align = "left" style="padding-left: 50px">DSLM Name</TD>
                      <TD height="22" colspan="2" align = "left" style="padding-left: 50px"><html:text property="dslmName" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                   
                    <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="dslmName"  /></TD>
                    </TR>
                     
                     <tr>
                        <td width="194" style="padding-left: 50px" height="21" align = "left">Area Code</td>
                        <td width="292" height="22" align = "left" style="padding-left: 50px"><html:hidden property="dslmAreaCode" /> <%=RegionRepository.getInstance().getRegionID(Long.parseLong(da)).getRegionName() %></td>
                       
                      </tr>
                      
                     
                     <tr>
                        <td width="194" style="padding-left: 50px" height="21" align = "left">Exchange</td>
                        <td width="292" height="22" align = "left" style="padding-left: 50px"><html:hidden property = "dslmExchangeNo"/> <%=ExchangeRepository.getInstance().getExchange(Integer.parseInt(de)).getExName() %></td>
                       
                      </tr>
                    
                    
                   
					 <tr>
                      <TD valign="top" height="22" align = "left" style="padding-left: 50px">Dslm Description</TD>
                      <TD height="22" colspan="2" align = "left" style="padding-left: 50px"><html:textarea property="dslmDescription"  /></TD>
                    </TR>                    
                    
                     <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="dslmDescription"  /></TD>
                    </TR>
                                       
                    <%}else{ %>
                    
                      
					 <TR> 
                      <TD valign="top" height="22" align = "left" style="padding-left: 50px">DSLM Name</TD>
                      <TD height="22" colspan="2" align = "left" style="padding-left: 50px"><html:text property="dslmName" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                   
                    <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="dslmName"  /></TD>
                    </TR>
                   
                    <tr>
                        <td width="194" style="padding-left: 50px" height="21" align = "left">Area Code</td>
                        <td width="292" height="22" align = "left" style="padding-left: 50px"> <html:select property="dslmAreaCode" onchange="changeExchange(this.value);">
                           
                            <%
					          RegionService rService = new RegionService();
					          ArrayList regionList = rService.getAllRegion();
					          for(int i=0;i<regionList.size();i++)
					          {
					            RegionDTO regionDTO = (RegionDTO)regionList.get(i);
					            %>
                            <html:option value="<%=Long.toString(regionDTO.getRegionDesc())%>"><%=regionDTO.getRegionName()%></html:option>
                            <% } %>
                          </html:select> </td>
                      </tr>
					 
					 
					  <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="dslmAreaCode"  /></TD>
                    </TR>
					 
					 
					 <tr>
                        <td width="194" style="padding-left: 50px" height="21" align = "left">Exchange</td>
                        <td width="292" height="22" align = "left" style="padding-left: 50px"><div id ="previous"> <html:select property="dslmExchangeNo">
                           
                            <%
                            RegionService rService = new RegionService();
					          
					            for(int i = 0; i<ExchangeRepository.getInstance().getExchangeList().size();i++)
					            {
					            	ExchangeDTO exchange = (ExchangeDTO)ExchangeRepository.getInstance().getExchangeList().get(i);
					            	if(exchange.getExNWD() == Long.parseLong(da))
					            	{
					            
					            %>
                            <html:option value="<%=Integer.toString(exchange.getExCode())%>"><%=exchange.getExName()%></html:option>
                            <%} } %>
                          </html:select> </div></td>
                      </tr>
					 
					 
					  <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="dslmExchangeNo"  /></TD>
                    </TR>
					 
					 <tr>
                      <TD valign="top" height="22" align = "left" style="padding-left: 50px">Dslm Description</TD>
                      <TD height="22" colspan="2" align = "left" style="padding-left: 50px"><html:textarea property="dslmDescription"  /></TD>
                    </TR>                    
                    
                     <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="dslmDslmDescription"  /></TD>
                    </TR>
                    
                    
                    <%} %>
                   
                    <TR> 
                      <TD valign="top" height="22" align = "left" style="padding-left: 50px">Dslm Status</TD>
                      <TD height="22" colspan="2" align = "left" style="padding-left: 50px">
                      <% 
                      
                      if(fservice.hasClient(""+dslmID)||bstatus.equals(""+DslmConstants.DSLM_ACTIVE))
                      {
                      %>
                      <html:hidden property = "dslmStatus"/>
                      <html:select property = "dslmStatus" disabled = "true">
                     <html:option value="<%=Integer.toString(DslmConstants.DSLM_ACTIVE) %>"><%=DslmConstants.ACTIVE %></html:option>
                      <html:option value="<%=Integer.toString(DslmConstants.DSLM_DELETED) %>"><%=DslmConstants.DELETED %></html:option>
                      </html:select>
                      <%}else {%>
                      
                       <html:select property = "dslmStatus" >
                     <html:option value="<%=Integer.toString(DslmConstants.DSLM_ACTIVE) %>"><%=DslmConstants.ACTIVE %></html:option>
                      <html:option value="<%=Integer.toString(DslmConstants.DSLM_DELETED) %>"><%=DslmConstants.DELETED %></html:option>
                      </html:select>
                      <%} %>
                      </TD>
                    </TR>   
                    
                     <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="dslmStatus"  /></TD>
                    </TR>
                    
                    
                    <TR> 
                      <TD valign="top" height="15"></TD>
                      <TD height="15" colspan="2">&nbsp;</TD>
                    </TR>
                  </table>
                  <%if(!fservice.hasClient(""+dslmID)) {%> 
                  <html:reset> Reset </html:reset><html:submit><%=submitCaption2 %></html:submit> 
                  <%}
                  else{       
                	  
                	  
                  %>
                  
                 <html:reset> Reset </html:reset><html:submit><%=submitCaption2 %></html:submit> 
                  
                  <%} %>
                  </html:form> 
                  <!-- end of the form -->
                  <br>

            <br>
            </td>
          </tr>
        </table>

        </td>
      </tr>
    </table>
    </td>
  </tr>

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/footer.jsp"  %>
    </td>
  </tr>

</table>
</body>
</html>
