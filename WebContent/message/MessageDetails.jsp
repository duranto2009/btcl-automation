<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="client.ClientRepository"%>
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page errorPage="failure.jsp" %>


<%@ page import="java.util.ArrayList,java.text.SimpleDateFormat,
sessionmanager.SessionConstants,

java.sql.*,
databasemanager.*,migration.*,client.*,message.*,java.util.Date" %>				 
				 

<%
 String title = "Message";
 String submitCaption2 = "Back";
 String actionName = "/ViewMessage";
 
 SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

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
		 
            <td width="100%" align="center">

<br><br>
<!-- start of the form  -->

<html:form  action="<%=actionName %>" method="POST" onsubmit="return validate();">
                  <table  width="402" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                    
                    
                    <bean:define id="time" scope="request"> <bean:write name="messageForm" property = "msgSendTime"/></bean:define> 
                    <bean:define id="type" scope="request"> <bean:write name="messageForm" property = "msgType"/></bean:define>  
                    <bean:define id="status" scope="request"> <bean:write name="messageForm" property = "msgStatus"/></bean:define> 
                   
                   <TR> 
                      <TD valign="top" height="22" align= "left" style="padding-left: 60px">Message Type</TD>
                      <TD height="22" colspan="2" align= "left" style="padding-left: 60px">
                      <%=Integer.parseInt(type)==1?"SMS":"Mail"%>
                      </TD>
                    </TR>
                    
                    
					 <TR> 
                      <TD valign="top" height="22" align= "left" style="padding-left: 60px">Sender</TD>
                      <TD height="22" colspan="2" align= "left" style="padding-left: 60px">
                      <bean:write name="messageForm" property = "msgFrom"/>
                      </TD>
                    </TR>
                   
                     <TR> 
                      <TD valign="top" height="22" align= "left" style="padding-left: 60px">Receiver</TD>
                      <TD height="22" colspan="2" align= "left" style="padding-left: 60px">
                      <bean:write name="messageForm" property = "msgTo"/>
                      </TD>
                    </TR>
                    
                    
                    
                     <TR> 
                      <TD valign="top" height="22" align= "left" style="padding-left: 60px">Message</TD>
                      <TD height="22" colspan="2" align= "left" style="padding-left: 60px">
                      <bean:write name="messageForm" property = "msgText"/>
                      </TD>
                    </TR>
                   
                     <TR> 
                      <TD valign="top" height="22" align= "left" style="padding-left: 60px">Sending Time</TD>
                      <TD height="22" colspan="2" align= "left" style="padding-left: 60px">
                      <%= format.format(new Date(Long.parseLong(time)))%>
                      </TD>
                    </TR>
                    
                    <TR> 
                      <TD valign="top" height="22" align= "left" style="padding-left: 60px">Status</TD>
                      <TD height="22" colspan="2" align= "left" style="padding-left: 60px">
                      <%= Integer.parseInt(status) == 1?"Sent":"Sending Failed"%>
                      </TD>
                    </TR>
                    
					
                    <TR> 
                      <TD valign="top" height="15"></TD>
                      <TD height="15" colspan="2">&nbsp;</TD>
                    </TR>
                  </table>
                 <html:submit><%=submitCaption2 %></html:submit> 
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
