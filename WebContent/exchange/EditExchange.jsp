<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="lot.LotRepository"%>
<%@page import="exchange.form.ExchangeForm"%>
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page errorPage="failure.jsp" %>


<%@ page import="java.util.ArrayList,
sessionmanager.SessionConstants,

java.sql.*,
databasemanager.*,exchange.*,regiontype.*" %>				 
				 

<%
 String title = "Edit Exchange";
 String submitCaption2 = "Update";
 String actionName = "/UpdateExchange";
 ExchangeService fservice = new ExchangeService();
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
		
		  	var url="../exchange/GetExchange.do?areaCode="+v;
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

        var ob = f.exchangeName;
        if( isEmpty(ob.value))
        {
          alert("Please enter Exchange Name");
          ob.value = "";
          ob.focus();
          return false;
        }

       
        return true;
      }

</script>

</head>

<body class="body_center_align" onload="document.forms[0].exchangeName.focus();">


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
  if( (msg = (String)session.getAttribute(util.ConformationMessage.EXCHANGE_UPDATE))!= null)
  {
    session.removeAttribute(util.ConformationMessage.EXCHANGE_UPDATE);
    %>				 
                    <tr> 
                      <td colspan="3" align="center" valign="top" height="50"><b><%=msg%></b></td>
                    </tr>
                    <%}%>
                    
                    
                    
                    <%
                                                    ExchangeForm cf = (ExchangeForm) request.getAttribute("exchangeForm");
                                                    
                                                    String bname = cf.getExName();
                                                    int exCode = cf.getExCode();
                                                    String btatus = ExchangeRepository.getInstance().getExchange(exCode).getExStatus() + "";
                                                    cf.setExStatus(Integer.parseInt(btatus));
                                                    
                                                %>
                                                
                                                 <html:hidden property="exCode"/>
                                                  <html:hidden property="exAdd" value = "0" />
                                                 
                                                 
                                                 
                                                 
                                                 
                                                 
                    
                   <%if(fservice.hasDslm(exCode)) {%> 
                  
                   <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:50px">Exchange Code</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:80px"><bean:write name = "exchangeForm" property="exCode"/></TD>
                    </TR>
                    
                    
					 <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:50px">Exchange Name</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:80px"><html:text property="exName" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                    
                    <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="exName"  /></TD>
                    </TR>
                    
                    <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:50px">Zone</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:80px"><html:hidden property="exZone"/><bean:write name = "exchangeForm" property="exZone"/></TD>
                    </TR>
                     
                     <tr>
                        <td width="194" align = "left" style= "padding-left:50px" height="21">Area Code</td>
                        <td width="292" height="22" align = "left" style= "padding-left:80px"><html:hidden property="exNWD"/> <%=RegionRepository.getInstance().getRegionID((long)cf.getExNWD()).getRegionName() %></td>
                       
                      </tr>
                      
                      <tr>
                        <td width="194" align = "left" style= "padding-left:50px" height="21">Original Area Code</td>
                        <td width="292" height="22" align = "left" style= "padding-left:80px"><html:hidden property="exOriginalNWD"/> <%=RegionRepository.getInstance().getRegionID((long)cf.getExOriginalNWD()).getRegionName() %></td>
                       
                      </tr>
                      
                       <tr>
                        <td width="194" align = "left" style= "padding-left:50px" height="21">Lot</td>
                        <td width="292" height="22" align = "left" style= "padding-left:80px"> <html:select property="lotCode">
                           
                            <%
					         
					          for(int i=1;i<8;i++)
					          {
					            
					            %>
                            <html:option value="<%=Integer.toString(i)%>"><%=LotRepository.getInstance().getLot(i).getLotName() %></html:option>
                            <% } %>
                          </html:select> </td>
                      </tr>	 
					  <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="lotCode"  /></TD>
                    </TR>
                      
                     
                    <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:50px">Exchange Phone Range Start</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:80px"><html:text property="exStartPhone" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                    
                     <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="exStartPhone"  /></TD>
                    </TR>
                    
                    
                      <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:50px">Exchange Phone Range End</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:80px"><html:text property="exEndPhone" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                     <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="exEndPhone"  /></TD>
                    </TR>
                    
                    
                                    
                    <%}else{ %>
                    
                      <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:50px">Exchange Code</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:80px"><bean:write name = "exchangeForm" property="exCode"/></TD>
                    </TR>
                      <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:50px">Exchange Name</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:80px"><html:text property="exName" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                    
                    <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="exName"  /></TD>
                    </TR>
                   
                    <tr>
                        <td width="194" align = "left" style= "padding-left:50px" height="21">Area Code</td>
                        <td width="292" height="22" align = "left" style= "padding-left:80px"> <html:select property="exNWD" onchange="changeExchange(this.value);">
                           
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
                      <TD colspan="2"><html:errors property="exNWD"  /></TD>
                    </TR>
					 
					 <tr>
                        <td width="194" align = "left" style= "padding-left:50px" height="21"> Original Area Code</td>
                        <td width="292" height="22" align = "left" style= "padding-left:80px"> <html:select property="exOriginalNWD" onchange="changeExchange(this.value);">
                           
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
                      <TD colspan="2"><html:errors property="exOriginalNWD"  /></TD>
                    </TR>
                      
                      <tr>
                        <td width="194" align = "left" style= "padding-left:50px" height="21">Exchange Zone</td>
                        <td width="292" height="22" align = "left" style= "padding-left:80px"> <html:select property="exZone" onchange="changeExchange(this.value);">
                           
                            <%
					         
					          for(int i=1;i<11;i++)
					          {
					            
					            %>
                            <html:option value="<%=Integer.toString(i)%>"><%=Integer.toString(i)%></html:option>
                            <% } %>
                          </html:select> </td>
                      </tr>	 
                       <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="exZone"  /></TD>
                    </TR>
                      
                       <tr>
                        <td width="194" align = "left" style= "padding-left:50px" height="21">Lot</td>
                        <td width="292" height="22" align = "left" style= "padding-left:80px"> <html:select property="lotCode">
                           
                            <%
					         
					          for(int i=1;i<8;i++)
					          {
					            
					            %>
                            <html:option value="<%=Integer.toString(i)%>"><%=LotRepository.getInstance().getLot(i).getLotName() %></html:option>
                            <% } %>
                          </html:select> </td>
                      </tr>	 
					  <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="lotCode"  /></TD>
                    </TR>
					 
					 
					  <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:50px">Exchange Phone Range Start</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:80px"><html:text property="exStartPhone" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                    
                     <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="exStartPhone"  /></TD>
                    </TR>
                    
                    
                      <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:50px">Exchange Phone Range End</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:80px"><html:text property="exEndPhone" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                     <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="exEndPhone"  /></TD>
                    </TR>
					                  
                    
                    
                    <%} %>
                   
                    <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:50px">Exchange Status</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:80px">
                      <% 
                      
                      if(fservice.hasDslm(cf.getExCode())||cf.getExStatus()==ExchangeConstants.EXCHANGE_ACTIVE)
                      {
                      %>
                      <html:hidden property="exStatus"/>
                      <html:select property = "exStatus" disabled = "true">
                     <html:option value="<%=Integer.toString(ExchangeConstants.EXCHANGE_ACTIVE) %>"><%=ExchangeConstants.ACTIVE %></html:option>
                      <html:option value="<%=Integer.toString(ExchangeConstants.EXCHANGE_DELETED) %>"><%=ExchangeConstants.DELETED %></html:option>
                      </html:select>
                      <%}else {%>
                      
                       <html:select property = "exStatus" >
                     <html:option value="<%=Integer.toString(ExchangeConstants.EXCHANGE_ACTIVE) %>"><%=ExchangeConstants.ACTIVE %></html:option>
                      <html:option value="<%=Integer.toString(ExchangeConstants.EXCHANGE_DELETED) %>"><%=ExchangeConstants.DELETED %></html:option>
                      </html:select>
                      <%} %>
                      </TD>
                    </TR>   
                     <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="exStatus"  /></TD>
                    </TR>
                    
                    <TR> 
                      <TD valign="top" height="15"></TD>
                      <TD height="15" colspan="2">&nbsp;</TD>
                    </TR>
                  </table>
                  <%if(fservice.hasDslm(cf.getExCode()))
                  {%>
                 <html:reset> Reset </html:reset><html:submit><%=submitCaption2 %></html:submit> 
                  <%}else{ %>
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
