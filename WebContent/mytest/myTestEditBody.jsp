<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="mytest.MyTestDTO"%>
<%@page import="mytest.MyTestService"%>
<%@page import="mytest.form.MyTestForm"%>
<%@page import="org.apache.struts.Globals"%>
<%@page import="mytest.MyTestConstants"%>
<%@ include file="../includes/checkLogin.jsp" %>
<%@page errorPage="../common/failure.jsp" %>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>



<%@ page import="java.util.ArrayList,
 				 sessionmanager.SessionConstants,
				 java.sql.*,databasemanager.*" %>
				
<%
 String title = "Edit MyTest Type";
 String submitCaption = "Update Type";
 String actionName = "/UpdateMyTest";
 MyTestForm form = (MyTestForm)request.getAttribute("mytestForm");
 MyTestDTO cDTO = (new MyTestService()).getMyTest(form.getMyTestID()+"");
 String cs = cDTO.getMyTestStatus()+"";
 form.setMyTestStatus(cDTO.getMyTestStatus());
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
		


		return true;
	}
	
function goBack()
{
	window.history.back()
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
	                  <TD height="22" width="154">MyTest Category</TD>
	                  <TD height="22" width="259"><html:text property="mytestType"  size="48"  />
                        <html:hidden property="mytestID" />
</TR>

 <tr>
													<td width="150"></td>
													<td width="430"><html:errors property="mytestType" /></td>
												</tr>

<TR>
	                  <TD height="22" width="154">Mail To</TD>
	<TD height="22" width="259"><html:textarea cols="50" rows="3"  property="permitUser" />
	</TD>
</TR>
 <tr>
													<td width="150"></td>
													<td width="430"><html:errors property="permitUser" /></td>
												</tr>
<TR>


<%if(Integer.parseInt(cs) == 1){ %>
<TR>

	                  <TD height="22" width="154">Category Status</TD>
	<TD height="22" width="259"><html:select   property="mytestStatus"  disabled = "true">
	<html:option value="<%=Integer.toString(MyTestConstants.MYTEST_STATUS_ACTIVE_VALUE) %>"><%=MyTestConstants.MYTEST_STATUS_ACTIVE_NAME %></html:option>
	<html:option value="<%=Integer.toString(MyTestConstants.MYTEST_STATUS_DELETED_VALUE) %>"><%=MyTestConstants.MYTEST_STATUS_DELETED_NAME %></html:option>
	</html:select>
	</TD>
</TR>
<%}else{ %>


<TR>

	                  <TD height="22" width="154">Category Status</TD>
	<TD height="22" width="259" align="left"><html:select   property="mytestStatus" >
	<html:option value="<%=Integer.toString(MyTestConstants.MYTEST_STATUS_ACTIVE_VALUE) %>"><%=MyTestConstants.MYTEST_STATUS_ACTIVE_NAME %></html:option>
	<html:option value="<%=Integer.toString(MyTestConstants.MYTEST_STATUS_DELETED_VALUE) %>"><%=MyTestConstants.MYTEST_STATUS_DELETED_NAME %></html:option>
	</html:select>
	</TD>
</TR>
<%} %>
<TR>
	<TD  height="22" width="154">

	</TD>
	<TD height="22" width="259">
	</TD>
</TR>

</table>

<%if(Integer.parseInt(cs) == 1){ %>
<html:reset> Reset Form </html:reset>
		
			<html:submit><%=submitCaption %></html:submit>
<%}else{ %>

<input type="button"
															value="Back" onclick="goBack();"/>
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
<%
session.removeAttribute("prevMyTestError");
session.removeAttribute("errorMyTestForm");
%>