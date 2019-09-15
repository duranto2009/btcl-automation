<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="complain.ComplainService"%>
<%@ include file="../includes/checkLogin.jsp" %>
<%java.text.SimpleDateFormat help_date = new java.text.SimpleDateFormat ("yyyy-MM-dd"); %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page errorPage="../common/failure.jsp" %>


<%@ page import="java.util.ArrayList,
sessionmanager.SessionConstants,

java.sql.*,
databasemanager.*,complain.*" %>


<%
 String title = "Add New Complain";
 String submitCaption = "Add another complain";
 String submitCaption2 = "Submit";
 String actionName = "/AddHelp";

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

<body class="body_center_align" onLoad="document.forms[0].helpSub.focus();">


<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="1024" id="AutoNumber1">

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/header_client.jsp"  %>
    </td>
  </tr>

  <!--center-->
  <tr>
    <td width="100%">
    <table border="0" cellpadding="0" cellspacing="0" width="1024" id="AutoNumber2">
      <tr>
	    <!--left menu-->
        
	    <!--main -->
        <td width="1024" valign="top" class="td_main" align="center">

        <table border="0" cellpadding="0" cellspacing="0" width="100%" id="AutoNumber3">
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
                  <table  width="500" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                    <%
  String msg = null;
  if( (msg = (String)session.getAttribute(util.ConformationMessage.HELP_ADD))!= null)
  {
    session.removeAttribute(util.ConformationMessage.HELP_ADD);
    %>
                    <tr>
                      <td colspan="3" align="center" valign="top" height="50"><b><%=msg%></b></td>
                    </tr>
                    <%}%>
                   <%--  <TR>
                      <TD height="22" width="118">Token Code</TD>
                      <TD height="22" colspan="2" >
<html:text property="tokenCode" value="<%=Long.toString(DatabaseManager.getInstance().getNextSequenceId(\"hrTokenCode\"))%>"  size="8" readonly="true"/>
<html:text property="accountID" value="<%=Long.toString(loginDTO.getAccountID())%>"/></TD>
                    </TR> --%>
                    <TR>
                      <TD width="118"></TD>
                      <TD colspan="2"><html:errors property="helpName"  /></TD>
                    </TR>
                    <TR>
                      <TD valign="top" height="22" align="left"></TD>
                      <TD height="22" colspan="2" align="left" style="padding-left: 50px">&nbsp;</TD>
                    </TR>
                    <TR>
                      <TD height="35" colspan="3" valign="top" align="center"><strong>Describe
                        your Complain:</strong></TD>
                    </TR>
                    <TR>
                      <TD valign="top" height="22" align="left">Subject</TD>
                      <TD height="22" colspan="2" align="left" style="padding-left: 50px"><html:text property="helpSub" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
					<TR>
                      <TD valign="top" height="22" align="left">Complain Category</TD>
                      <TD height="22" colspan="2" align="left" style="padding-left: 50px"> <html:select property="categoryID" >
                          <%
                                   ComplainService complainService = new ComplainService();
                          		   ArrayList<ComplainDTO> complainList = complainService.getComplains();
                                   for(int i = 0; i<complainList.size();i++)
                                    {
                          %>
                          				<html:option value="<%=Long.toString(complainList.get(i).getComplainID())%>"><%=complainList.get(i).getComplainType() %> </html:option>
                          <%
                                     }
                                    
                          %>
                        </html:select></TD>
                    </TR>
                    <TR>
                      <TD valign="top" height="22" align="left">Complain Message</TD>
                      <TD height="22" colspan="2" align="left" style="padding-left: 50px"><html:textarea cols="50" rows="6"  property="helpDesc" /></TD>
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
