<%@ include file="../includes/checkLogin.jsp" %><%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %><%@page import = "java.util.*, client.*" %><%@page import = "java.text.*" %><%@ page errorPage="../common/failure.jsp"%><%
String windowTitle =  "Balance Transfer";
%>
  <script language="JavaScript" src="../scripts/util.js"></script>
 <script language="JavaScript">
 var submit=false;
 function validate()
 {

if(submit==false)
{

 var form = document.forms[0];

var  ob=form.pinNo;

 if(isEmpty(ob.value))
 {
	 alert("Insert the Target PIN to transfer Balance");
	 ob.value="";
	 ob.focus();
	 return false;
 }

 ob=form.transferAmount;
	 	 
 if(isEmpty(ob.value))
 {
	 alert("Insert the Amount to transfer");
	 ob.value="";
	 ob.focus();
	 return false;
 }

 if( !isNum(ob.value))
 {
   alert("Transfer Amount Must be a Number");
   ob.value = "";
   ob.focus();
   return false;
 }

 if( ob.value<=0)
 {
   alert("Transfer Amount Must be a Greater than Zero");
   ob.value = "";
   ob.focus();
   return false;
 }
 ob=form.password;
 if(isEmpty(ob.value))
 {
	 alert("Enter your password");
	 ob.value="";
	 ob.focus();
	 return false;
 }
 
submit=true;
 return true;
}

return false;
 }
 </script>
<html><head><html:base/><title><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.BalanceTransfer,loginDTO)%></title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css"></head>
<body class="body_center_align">
<table border="0" cellpadding="0" cellspacing="0" width="1024">
  <tr><td width="100%"><%@ include file="../includes/header.jsp"%></td></tr>
  <tr><td width="100%">
    <table border="0" cellpadding="0" cellspacing="0" width="1024">
      <tr><%if(loginDTO.getWebRoot()== null){
    	  
    	  if(menuAtLeftForPin)
        	{
              %>
        <td class="td_menu"><%@ include file="../includes/menu_pin.jsp"%>&nbsp;</td>
       
              <%	
        	} 
        %><%}else{
          %><td valign="top"><%java.net.URL url = new java.net.URL(loginDTO.getWebRoot()+"/left.htm");

                  java.io.InputStream inputStream = url.openStream();
                  java.io.BufferedReader reader = new java.io.BufferedReader( new java.io.InputStreamReader(inputStream));


                  String str = null;
                  while((str = reader.readLine()) != null)
                  {
                    out.println(str);
                  }
                  reader.close();
                  %></td><%}
%><td width="820" valign="top" class="td_main" style="background-color:white;" align="center">

  <table width="100%" align="center"><tr><td width="100%">
    <table width="100%" align="right" style="font-family: Arial; font-size: 10pt; ; color: #000000 " border="0"><%if(loginDTO.getWebRoot() != null){
      %><tr><td width="100%" align="center"><table style="font-family: Arial; font-size: 10pt; ; color: #000000 " width="90%" align="center">
<tr>
  <td><a href="../pin/PinRateList.jsp"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_pin_history_call,loginDTO)%></a></td>
  <td><a href="../pin/CallHistory.jsp?type=ThisMonth"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_pin_history_this,loginDTO)%></a></td>
  <td><a href="../pin/CallHistory.jsp?type=LastMonth"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_pin_history_last,loginDTO)%></a></td>
  <td><a href="../pin/CallRecordParams.jsp"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_pin_history_detail,loginDTO)%></a></td>
</tr>
</table></td></tr><%}%></table></td></tr>
<tr><td>
<br/>
 <html:form method="POST" onsubmit="return validate();" action="/BalanceTransfer">
<table width="50%" align="center" style="font-family: Arial; font-size: 10pt; ; color: #000000">
<%String message = (String)session.getAttribute(util.ConformationMessage.BALANCE_TRANSFER);
if(message != null)
{
  session.removeAttribute(util.ConformationMessage.BALANCE_TRANSFER);
  %><tr><td colspan="2"><b><%=message%></b></td></tr><%
}

%>
<tr><td> <%=language.LanguageManager.getInstance().getString(language.LanguageConstants.BalanceTransfer_TargetedPin,loginDTO)%> </td><td><input type="text" name="pinNo"/></td></tr>
<tr><td><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.BalanceTransfer_TransferAmount,loginDTO)%>  </td><td><input type="text" name="transferAmount"/></td></tr>
<tr><td><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.PASSWORD,loginDTO)%>  </td><td><input type="password" name="password"/></td></tr>
<tr><td align="center" colspan="2" height="60">
<input type="button" onclick="init();" value="<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.Global_reset,loginDTO)%>"/>
<input type="submit" value="<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.BalanceTransfer_Transfer,loginDTO)%> "/></td>
</tr>
</table></html:form></td></tr></table></td></tr></table></td></tr>
<tr><td width="100%"><%@ include file="../includes/footer.jsp"%></td></tr></table></body></html>
