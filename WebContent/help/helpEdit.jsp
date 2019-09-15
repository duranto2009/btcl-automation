<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page errorPage="../common/failure.jsp" %>

<%@ page import="java.util.ArrayList,
 				 sessionmanager.SessionConstants,
				  help.*" %>

<%
 String title = "Edit the Description of the Message";
 String submitCaption = "Update";
 String actionName = "/UpdateHelpReply";

%>

<html>
<head>
<html:base/>
<title><%=title %></title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>

<script language="JavaScript">
	 function validate()
      {
        var f = document.forms[0];

        var ob = f.helpSub;
        if( isEmpty(ob.value))
        {
          alert("Please enter Subject");
          ob.value = "";
          ob.focus();
          return false;
        }

        var ob = f.helpDesc;
        if( isEmpty(ob.value))
        {
          alert("Please enter Complain Message");
          ob.value = "";
          ob.focus();
          return false;
        }

        return true;
      }

</script>

</head>

<body class="body_center_align" >


<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="1024" id="AutoNumber1">

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/header.jsp"  %>
    </td>
  </tr>

  <!--center-->
  <tr>
    <td width="100%">
    <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="1024" id="AutoNumber2">
      <tr>
	    <!--left menu-->
        
	    <!--main -->
        <td width="1024" valign="top" class="td_main" align="center">

        <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber3">
          <tr>
            <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
            <div class="div_title"><%=title %></div>
			</td>
          </tr>
          <tr>

            <td width="100%" align="center">

<br><br>
                  <!-- start of the form  -->
                  <html:form  action="<%=actionName %>" method="POST" onsubmit="return validate();"> 
                  <table  width="413" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                    
                    <TR> 
                      <TD height="22" width="120">Token Code</TD>
                      <TD height="22" width="291"><html:text property="tokenCode" size="8" maxlength="20"  readonly="true" /> 
                        <html:hidden property="helpID" /></TD>
                    </TR>
                    <TR> 
                      <TD height="22" width="120"></TD>
                      <TD height="22" width="291"><html:errors property="helpName2"  /> 
                        <html:hidden property="userID" /></TD>
                    </TR>
                    <TR> 
                      <TD height="35" colspan="2" valign="top"><strong>Describe 
                        your Complain:</strong></TD>
                    </TR>
                    <TR> 
                      <TD valign="top" height="22">Subject</TD>
                      <TD height="22"><html:text property="helpSub" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                    <TR> 
                      <TD valign="top" height="22">Complain Message</TD>
                      <TD height="22"><html:textarea property="helpDesc" rows="6" cols="50" /></TD>
                    </TR>
                    <TR> 
                      <TD valign="top" height="22"> </TD>
                      <TD height="22"><html:hidden property="helpDate"  /> <html:hidden property="helpStatus"  /> 
                      </TD>
                    </TR>
                    
                  </table>
                  <html:reset> Reset</html:reset> 
                  <!--need permision-->
                  <html:submit><%=submitCaption %></html:submit> </html:form><br>


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
