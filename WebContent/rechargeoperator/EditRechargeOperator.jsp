<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../includes/checkLogin.jsp" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page errorPage="failure.jsp" %>


<%@ page import="java.util.ArrayList,java.util.Date,
sessionmanager.SessionConstants,

java.sql.*,
databasemanager.*,user.*" %>				 
				 

<%
 String title = "Recharge Details";
 String submitCaption2 = "Back";
 String actionName = "/ViewRechargeOperator";
 
 String idForDownloadOperatorRecharge = request.getParameter("id");
 session.setAttribute("idForDownloadOperatorRechargeDetails", idForDownloadOperatorRecharge);

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
	 
	

</script>

</head>

<body class="body_center_align" onload="document.forms[0].operatorName.focus();">


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
        <td width="780" valign="top" class="td_main" align="center">

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
                   
                    <bean:define id="operator" scope="request"> <bean:write name="rechargeOperatorForm" property = "rechargeOperator"/></bean:define>  
                     <bean:define id="id" scope="request"> <bean:write name="rechargeOperatorForm" property = "rechargerID"/></bean:define>
                                      
                     
                     <bean:define id="time" scope="request"> <bean:write name="rechargeOperatorForm" property = "rechargeTime"/></bean:define>  
                     
                    
                    <%
                    	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    	String date = format.format(new Date(Long.parseLong(time)));
                     
                    
                    %>
                    <tr>
                      <td  align="left" style="padding-left: 80px" height="25" width = "200">Recharged By</td>
                      <td height="25" align="left" style="padding-left: 10px"> <%=UserRepository.getInstance().getUserDTOByUserID(Long.parseLong(id)).getUsername()%></td>
                    </tr>  
                    
                    
                    
                    <tr>
                      <td align="left" style="padding-left: 80px" height="25" width = "200">Operator</td>
                      <td height="25" align="left" style="padding-left: 10px"> <%=UserRepository.getInstance().getUserDTOByUserID(Long.parseLong(operator)).getUsername() %></td>
                    </tr>  
                    
                    
					 <TR> 
                      <TD valign="top" height="25" align="left" style="padding-left: 80px">Recharge Amount</TD>
                      <TD height="25" colspan="2" align="left" style="padding-left: 10px"><bean:write  name = "rechargeOperatorForm" property ="rechargeAmount"/></TD>
                    </TR>
                   
                   
					   
                    
                     <TR> 
                      <TD valign="top" height="25" align="left" style="padding-left: 80px">Voucher No</TD>
                      <TD height="25" colspan="2" align="left" style="padding-left: 10px"><bean:write  name = "rechargeOperatorForm" property ="rechargeVoucherNo"/></TD>
                    </TR>                    
                    
                    
                    
                    
                    
                    <TR> 
                      <TD valign="top" height="25" align="left" style="padding-left: 80px">Date</TD>
                      <TD height="25" colspan="2" align="left" style="padding-left: 10px"><%=date %></TD>
                    </TR>  
                    
                    
                     <TR> 
                      <TD valign="top" height="25" align="left" style="padding-left: 80px">Recharge Description</TD>
                      <TD height="25" colspan="2" align="left" style="padding-left: 10px"><bean:write  name = "rechargeOperatorForm" property ="rechargeDescription"/></TD>
                    </TR>  
                    
                    <TR> 
                      <TD valign="top" height="15"></TD>
                      <TD height="15" colspan="2">&nbsp;</TD>
                    </TR>
                  </table>
                 <html:submit><%=submitCaption2 %></html:submit> 
                  </html:form> 
                  <html:form method="POST" action="/Download">
                  	<input type ="submit" name="downloadRechargeOperatorDetails" value="Download">
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
