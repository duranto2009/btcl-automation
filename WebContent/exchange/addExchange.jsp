<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="lot.LotRepository"%>
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
 String title = "Add New Exchange";
 String submitCaption2 = "Add";
 String actionName = "/AddExchange";

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
  if( (msg = (String)session.getAttribute(util.ConformationMessage.EXCHANGE_ADD))!= null)
  {
    session.removeAttribute(util.ConformationMessage.EXCHANGE_ADD);
    %>
                    <tr> 
                      <td colspan="3" align="center" valign="top" height="50"><b><%=msg%></b></td>
                    </tr>
                    <%}%>
                    
                   
                    
                    <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:30px">Exchange Code</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:80px"><html:text property="exCode" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                    <html:hidden property="exAdd" value = "1" />
                    
                    <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="exCode"  /></TD>
                    </TR>
                    
                    
					 <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:30px">Exchange Name</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:80px"><html:text property="exName" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                   
                   <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="exName"  /></TD>
                    </TR>
                    <tr>
                        <td width="194"  align = "left" style= "padding-left:30px" height="21">Area Code</td>
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
                        <td width="194" align = "left" style= "padding-left:30px"" height="21"> Original Area Code</td>
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
                        <td width="194" align = "left" style= "padding-left:30px" height="21">Exchange Zone</td>
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
                        <td width="194" align = "left" style= "padding-left:30px" height="21">Lot</td>
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
                      <TD valign="top" height="22" align = "left" style= "padding-left:30px">Exchange Phone Range Start</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:80px"><html:text property="exStartPhone" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                    
                    <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="exStartPhone"  /></TD>
                    </TR>
                    
                    
                      <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:30px">Exchange Phone Range End</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:80px"><html:text property="exEndPhone" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                    
					  <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="exEndPhone"  /></TD>
                    </TR>                
                    
                    <html:hidden property="exStatus" value = '<%=""+ExchangeConstants.EXCHANGE_ACTIVE %>' />
                    
                    <TR> 
                      <TD valign="top" height="15" align = "left" style= "padding-left:30px"></TD>
                      <TD height="15" colspan="2" align = "left" style= "padding-left:80px">&nbsp;</TD>
                    </TR>
                  </table>
                  <html:reset> Reset </html:reset><html:submit><%=submitCaption2%></html:submit> 
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
