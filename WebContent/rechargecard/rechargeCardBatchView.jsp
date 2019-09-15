<%@ include file="../includes/checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %><%@ page errorPage="../common/failure.jsp" %>
<%@ page import="java.util.*,sessionmanager.SessionConstants,utildao.*,java.sql.*,util.*,databasemanager.*,rechargecard.*" %>
<%
boolean hasPermission=false;
if( (loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_PREPAID_CARD )!=-1&&(loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_PREPAID_CARD_SEARCH) !=-1)))
{
	hasPermission=true;
}
else if(isAgent && loginDTO.getRoleID()==-1)
{
	hasPermission=true;
}
if(!hasPermission)
{
	 response.sendRedirect("../");
}
String batchID = (String)session.getAttribute(util.ConformationMessage.CARD_BATCH_ID);
session.removeAttribute(util.ConformationMessage.CARD_BATCH_ID);

if( batchID == null )
{
  batchID = (String) request.getParameter("id");
}
ArrayList cardList=null;
rechargecard.CardDTO cardDTO=null;
if(batchID!=null)
{	
	
 RechargeCardBatchDAO dao=new RechargeCardBatchDAO();
   RechargeCardBatchDTO dto=dao.getRechargeCardBatch(batchID);
   if(dto.m_parent!=loginDTO.getAccountID())
   {

		 request.getSession(true).setAttribute(SessionConstants.FAILURE_MESSAGE,"You do not have permission to view this recharge card batch");
		 response.sendRedirect("../common/failure.jsp");
  
   }

}



%>

<html><head><html:base/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.RECHARGE_CARD,loginDTO)%></title>
    <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
      <script language="JavaScript" src="../scripts/util.js"></script>
      <script language="JavaScript"></script>
    </head>
    <body class="body_center_align" onload="showPullDown('pullDownRechargeCard');">
    <table border="0" cellpadding="0" cellspacing="0"  width="1024">
        <tr><td width="100%"><%@ include file="../includes/header.jsp"%></td></tr>
        <tr><td width="100%">
            <table border="0" cellpadding="0" cellspacing="0" width="1024">
              <tr><td class="td_menu">  <%if(isAgent && loginDTO.getRoleID()==-1){ %>
      <%@ include file="../includes/resMenu.jsp"%>
      <%} else { %>
      <%@ include file="../includes/menu.jsp"%>
      <%}%></td>
                <td width="820" valign="top" class="td_main" align="center">
                  <table border="0" cellpadding="0" cellspacing="0" width="100%">
                     <tr>
                      <td width="100%" align="center">
                        <table border="0" cellpadding="0" cellspacing="0" class="form1"  width="700" align="center">
                          <tr>
                      		<td width="100%" align="center" style="padding-bottom: 20px;">
                        		<div class="div_title"><span style="vertical-align: sub;"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.RECHARGE_CARD,loginDTO)%></span></div>
                      		</td>
                    	</tr>
                    	</table>
                      </td>
                    </tr>
<tr><td width="100%" align="center">
  <table align="center" cellspacing="0">
  <%
  String msg = null;
  if( (msg = (String)session.getAttribute(util.ConformationMessage.CARD_ADD))!= null)
  {
    session.removeAttribute(util.ConformationMessage.CARD_ADD);
  }
   if( batchID == null)
  {%>
  <tr>
    <td>
      <table align="center">
        <tr>
          <td width="100%" colspan="2" align="center" valign="top" height="30"><b><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_pinReport_batch_id,loginDTO)%></b></td>
        </tr>
      </table>
    </td>
  </tr><%
}
else
{
  StringBuffer pinListBuffer = null;
  cardList = rechargecard.RechargeCardRepository.getInstance().getCardListByBatchID(Long.parseLong(batchID));
  if( cardList != null)
  {
    for( int i=0;i<cardList.size();i++)
    {

       cardDTO = (rechargecard.CardDTO) cardList.get(i);

      if( pinListBuffer == null)
      {
        pinListBuffer = new StringBuffer(cardList.size()*24);
        pinListBuffer.append("Serial NO\t\tCard NO\n");
      }
      else
      {
        pinListBuffer.append("\n");
      }
      pinListBuffer.append(cardDTO.cardSerialNO +"\t\t"+cardDTO.cardNO);
    }
    %>
    <tr><td><table align="center">

    <%if( msg != null)
    {%>
    <tr><td width="100%" colspan="2" align="center" valign="top" height="30"><b><%=msg%></b></td></tr>
    <tr><td width="100%" colspan="2" align="left" valign="bottom" height="20"><b><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_pinReport_gen_pin,loginDTO)%></b></td></tr>
    <%}else {%>
    <tr><td width="100%" colspan="2" align="left" valign="bottom" height="20"><b><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_pinReport_serial_num,loginDTO)%></b></td></tr>

    <%}%>
    <tr><td width="100%" colspan="2" align="center"  ><textarea  readonly="readonly" cols="60" rows="<%=cardList.size() < 30 ? cardList.size() + 2 : 30%>" ><%=pinListBuffer.toString()%></textarea></td></tr>
    <tr><td colspan="2"  height="30">&nbsp;</td></tr>
  </table>
</td>
</tr>
<%}
  else
  {%>
  <tr><td><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_pinReport_no_pin_found,loginDTO)%></td></tr>
  <%}
}%>

  </table></td></tr></table></td></tr></table></td></tr>
  <tr><td width="100%"><%@ include file="../includes/footer.jsp"%></td></tr></table></body></html>
