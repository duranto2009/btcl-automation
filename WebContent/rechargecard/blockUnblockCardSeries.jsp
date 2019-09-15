<%@ include file="../includes/checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page errorPage="../common/failure.jsp" %>
<%@ page import="java.util.*,sessionmanager.SessionConstants,utildao.*,java.sql.*,util.*,databasemanager.*,rechargecard.*" %>

<%
if((loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_PREPAID_CARD )!=-1&&(loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_PREPAID_CARD_SEARCH) !=-1)));
else
{
	 response.sendRedirect("../");
}

%>

<%
int cardDigitLength=0;
ArrayList serialList= new ArrayList();
%>
<html><head><html:base/>
  <title>Block/Unblock Card Series</title>
  <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
  <script language="JavaScript" src="../scripts/util.js"></script>
</head>
<body class="body_center_align" onload="showPullDown('pullDownRechargeCard');">
<table border="0" cellpadding="0" cellspacing="0"  width="1024">
  <tr><td width="100%"><%@ include file="../includes/header.jsp"%></td></tr>

  <tr><td width="100%">
    <table border="0" cellpadding="0" cellspacing="0"  width="100%">
      <tr><td class="td_menu"><%@ include file="../includes/menu.jsp"%>&nbsp;</td>
        <td width="820" valign="top" class="td_main" align="center">
          <table border="0" cellpadding="0" cellspacing="0" width="700">
            <tr>
            <td width="100%" align="center" style="padding-bottom: 20px;">
                <div class="div_title">
                	<span style="vertical-align: sub;">Block/Unblock Card Series</span>
                </div>
              </td></tr>
<tr><td width="100%" align="center">
  <table align="center" cellspacing="0">
  <%
  String batchID = (String)session.getAttribute(util.ConformationMessage.CARD_BATCH_ID);
  session.removeAttribute(util.ConformationMessage.CARD_BATCH_ID);

  if( batchID == null )
  {
    batchID = (String) request.getParameter("id");
  }
  if( batchID == null){%>
<tr><td><table align="center"><tr><td width="100%" colspan="2" align="center" valign="top" height="30"><b><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_pinReport_Error,loginDTO)%></b></td></tr></table></td></tr>
<%}else{
	
	RechargeCardBatchDTO cardBatchDTO=new RechargeCardBatchDAO().getRechargeCardBatch(batchID);
	if(cardBatchDTO.m_parent!=loginDTO.getAccountID())
	{
		request.getSession(true).setAttribute(SessionConstants.FAILURE_MESSAGE,"Unauthorize Batch ID!");
		response.sendRedirect("../common/failure.jsp");
	}
	
  ArrayList cardList = RechargeCardRepository.getInstance().getCardListByBatchID(Long.parseLong(batchID));
  StringBuffer cardListBuffer = null;
  if( cardList != null)
  {
    for( int i=0;i<cardList.size();i++)
    {
      CardDTO cardDTO = (CardDTO) cardList.get(i);
      serialList.add(cardDTO.cardSerialNO);
      if( cardListBuffer == null)
      {
        cardListBuffer = new StringBuffer(cardList.size()*24);
        cardListBuffer.append("Serial\tCard Number \tStatus\n");
      }
      else
      {
        cardListBuffer.append("\n");
      }
      cardListBuffer.append(cardDTO.cardSerialNO +"\t"+cardDTO.cardNO+"\t"

      +(cardDTO.cardStatus == CardDTO.CARD_STATUS_ACTIVE?"Active":"Disable"));


      cardDigitLength = cardDTO.cardNO.length();
    }

    %>
<tr><td><table width="41%" align="center"><tr><td width="100%" colspan="2" align="center" valign="bottom" height="20"><b>Serial Number and Card Number List</b></td></tr>
  <tr><td width="100%" colspan="2" align="center"  ><textarea  readonly="readonly" cols=<%=(38+cardDigitLength) %> rows=<%=cardList.size() < 20 ? cardList.size() + 5 : 20%> > <%=cardListBuffer.toString()%> </textarea></td></tr>
    <tr><td colspan="2"  height="10">&nbsp;</td></tr></table>

    <html:form  action="/BlockUnblockCardSeries" method="POST" onsubmit="return validate();">
      <table align="center">
        <tr><td><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_pinReport_Srfrom,loginDTO)%></td>
          <td><input type="text" name="serialFrom" size="20" /></td></tr>
          <tr><td><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_pinReport_Srto,loginDTO)%></td>
            <td><input type="text" name="serialTo" size="20" /></td>
</tr>
  <tr><td><input type="radio" name="BLOCK_DECISION" value="2" checked="checked" /><!--Block--><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_pinReport_block,loginDTO)%></td>
    <td><input type="radio" name="BLOCK_DECISION" value="1" /><!--Unblock--><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_pinReport_unblock,loginDTO)%></td></tr>

  <tr><td height="50">&nbsp;</td><td>
      <input class="cmd"  type="reset" value="<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.Global_reset,loginDTO)%>"><input type="hidden" name="CardBatchID" value="<%=batchID%>" />
      &nbsp;
      <input class="cmd" type="submit" value="<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.Global_submit,loginDTO)%>">
    </td>
  </tr>

</table></html:form></td></tr>
<%}else{%>
<tr><td><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_pinReport_no_pin,loginDTO)%></td></tr>
<%}}%></table></td></tr></table></td></tr></table></td></tr>
<tr><td width="100%"><%@ include file="../includes/footer.jsp"%></td></tr></table></body></html>

<script language="JavaScript">

function init()
{
}


function validate()
{
  var f = document.forms[0];

  var ob = f.serialFrom;
  if( isEmpty(ob.value))
  {
    alert("Please enter Serial From");
    ob.value = "";
    ob.focus();
    return false;
  }
  var ob = f.serialTo;
  if( isEmpty(ob.value))
  {
    alert("Please enter Serial To");
    ob.value = "";
    ob.focus();
    return false;
  }

  if( parseInt(f.serialFrom.value) > parseInt(f.serialTo.value))
  {
    alert("Serial From cannot be greater than Serial To");
    return false;
  }
  var match1 = 0;
  var match2 = 0;
  <%
  for (int i=0; i < serialList.size(); i++)
  {
    String serial = (String) serialList.get(i);
    %>
    if( f.serialFrom.value == <%=serial%>)
    {
      match1 = 1;
    }
    if(f.serialTo.value == <%=serial%>)
    {
      match2=1;
    }
    <%}%>

    if( match1 == 0)
    {
      alert("Serial From is not valid")
      f.serialFrom.value="";
      f.serialFrom.focus();
      return false;
    }

    if( match2 == 0)
    {
      alert("Serial To is not valid")
      f.serialTo.value="";
      f.serialTo.focus();
      return false;
    }
    return true;
  }

  </script>
