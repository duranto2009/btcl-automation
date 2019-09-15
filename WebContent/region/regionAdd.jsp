<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page errorPage="failure.jsp" %>


<%@ page import="java.util.ArrayList,
sessionmanager.SessionConstants,

java.sql.*,
databasemanager.*" %>				 
				 

<%
 String title = "Add New Region";
 String submitCaption2 = "Add";
 String actionName = "/AddRegion";

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
	 
	 function validate()
      {
        var f = document.forms[0];

        var ob = f.regionName;
        if( isEmpty(ob.value))
        {
          alert("Please enter Region Name");
          ob.value = "";
          ob.focus();
          return false;
        }

        var ob = f.regionDesc;
        if( isEmpty(ob.value))
        {
          alert("Please enter Region Description");
          ob.value = "";
          ob.focus();
          return false;
        }

        return true;
      }

</script>

</head>

<body class="body_center_align" onload="document.forms[0].regionName.focus();">


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
                  <table  width="402" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                    <%
  String msg = null;
  if( (msg = (String)session.getAttribute(util.ConformationMessage.REGION_ADD))!= null)
  {
    session.removeAttribute(util.ConformationMessage.REGION_ADD);
    %>
                    <tr> 
                      <td colspan="3" align="center" valign="top" height="50"><b><%=msg%></b></td>
                    </tr>
                    <%}%>
                    
                   
					 <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left: 50px">Name</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left: 50px"><html:text property="regionName" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                    <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="regionName"  /></TD>
                    </TR>
					 <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left: 50px">Code</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left: 50px"><html:text property="regionDesc"  size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                    
                     <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="regionDesc"  /></TD>
                    </TR>
                    
                    <%-- <TR> 
                      <TD valign="top" height="22">Parent Region</TD>
                      <TD height="22" colspan="2"><html:text property="parentRegionName"  size="48"   /></TD>
                    </TR> --%>
                    <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left: 50px">Description</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left: 50px"><html:textarea property="description"/></TD>
                    </TR>
                     <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="description"  /></TD>
                    </TR>
                    <TR> 
                      <TD valign="top" height="15"></TD>
                      <TD height="15" colspan="2">&nbsp;</TD>
                    </TR>
                  </table>
                  <html:reset> Reset </html:reset><html:submit><%=submitCaption2 %></html:submit> 
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
