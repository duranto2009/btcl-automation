
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page errorPage="../common/failure.jsp" %>
<html>
<head>
<html:base/>
<title>Billing System::Mail Password</title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
</head>
<script language="JavaScript">
function checkValue()
{
	document.getElementById("reqType").value = "2";
	
	alert(document.getElementById("reqType").value);
}
</script>
<body class="body_center_align"  onload="document.forms[0].username.focus();">
<center>
<br>
<br>
<table bgcolor="#EFF7FF" style="border-collapse: collapse" bordercolor="#C0C0C0" cellpadding="0" cellspacing="0" border="1" width="366" height="207">
<tr>
  <td align="center" style="font-family: Arial; color: #000084; font-size:14pt" width="364" height="207">
  <br>
  Password Mailing System
  <br>
<!----login Form -------------------------------->
  <html:form method="POST" action="/PasswordMail">
  <input type="hidden" value="1" id="reqType" name="reqType"/>
  <table border="0" cellpadding="2" style="border-collapse: collapse; font-family: Arial; color: #000084; font-size: 10pt" bordercolor="#111111" width="260" >
    <tr>
      <td width="108" height="16"></td>
      <td width="233" height="16"></td>
    </tr>
    <tr>
      <td width="108" height="16">Username </td>
      <td width="233" height="16"><html:text property="username" size="20" style="height:20;border: 1px solid #C0C0C0"/><label style="color:red; vertical-align: middle;" >*</label></td>
    </tr>
    <tr>
      <td width="108" height="16">or Mail Address</td>
      <td width="233" height="16"><html:text property="mailAddress" size="20" style="height:20;border: 1px solid #C0C0C0"/><label style="color:red; vertical-align: middle;" >*</label></td>
    </tr>
    <tr>
      <td width="108" height="25" colspan="2">&nbsp;</td>
    </tr>

    <tr>
      <td width="50%" height="26" align="right" >
      <input type="reset" value="Reset" name="B1" style="font-weight: bold; border: 1px solid #C0C0C0; font-family:Arial; font-size:10pt; color:#000084; background-color:#FFEECA">
        </td>
      <td width="50%" height="26"  align="left">
      <input type="submit" value="Mail Password" name="B2" style="font-weight: bold; border: 1px solid #C0C0C0; font-family:Arial; font-size:10pt; color:#000084; background-color:#FFEECA">
      </td>    
      </tr>
       
		<tr>
      <td width="50%" height="26" align="right" >
      OR
        </td>
      <td width="50%" height="26"  align="center" colspan="2">
      <input type="submit" value="SMS Password" name="B3" style="font-weight: bold; border: 1px solid #C0C0C0; font-family:Arial; font-size:10pt; color:#000084; background-color:#FFEECA" onclick="checkValue()">
      </td>
      </tr>  
  </html:form >
<!----login Form -------------------------------->

  </td>
</tr>
</table>
<center>
<p></p>
</body>
</html>
