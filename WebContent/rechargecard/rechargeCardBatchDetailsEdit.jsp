<%@page import="language.LanguageConstants"%>
<%@page import="language.LanguageManager"%>
<%@ include file="../includes/checkLogin.jsp"%>
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
int permission=login.PermissionConstants.MANAGE_PREPAID_CARD_SEARCH;
%>
<%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="../common/failure.jsp"%>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,utildao.*,rechargecard.*"%>
<%
	String submitCaption = "Update Card Number";
	String actionName = "/UpdateRechargeCardNumber";
	String cardID = request.getParameter("CardID");
	String globalReset = language.LanguageManager.getInstance().getString(language.LanguageConstants.Global_reset,loginDTO);

	if( cardID == null)
	{
  		cardID = (String)session.getAttribute("CardID");
  		session.removeAttribute("CardID");
	}

	String cardBatchID = request.getParameter("CardBatchID");
	if( cardBatchID == null)
	{
  		cardBatchID = (String)session.getAttribute("CardBatchID");
  		session.removeAttribute("CardBatchID");
	}

	CardDTO dto = null;
	long batchID=-1;
	if(cardBatchID!=null)
	{
		batchID=Long.valueOf(cardBatchID);
	}
	
	if(batchID==-1)
	{
		request.getSession(true).setAttribute(SessionConstants.FAILURE_MESSAGE,"Invalid BatchID!");
		response.sendRedirect("../common/failure.jsp");
	}
	RechargeCardBatchDAO cardbatchDao=new RechargeCardBatchDAO();
	RechargeCardBatchDTO cardBatchDTO=cardbatchDao.getRechargeCardBatch(cardBatchID);
	if(cardBatchDTO.m_parent!=loginDTO.getAccountID())
	{
		request.getSession(true).setAttribute(SessionConstants.FAILURE_MESSAGE,"Unauthorize Batch ID!");
		response.sendRedirect("../common/failure.jsp");
	}
	CardBatchDetailsDAO dao = new CardBatchDetailsDAO(batchID);

	if(	cardID != null)
	{
  		dto = dao.getCard(cardID);
  		if(dto.cardBatchID!=batchID)
  		{
  			 request.getSession(true).setAttribute(SessionConstants.FAILURE_MESSAGE,"Invalid BatchID!!");
  			 response.sendRedirect("../common/failure.jsp");
  		}
  		
	}
	
%>

<html><head><html:base/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><%=LanguageManager.getInstance().getString(LanguageConstants.GLOBAL_UPDATE, loginDTO) %> <%=LanguageManager.getInstance().getString(LanguageConstants.RECHARGE_CARD, loginDTO) %></title>
    <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
      <script language="JavaScript" src="../scripts/util.js"></script>
      <script language="JavaScript">

      function validate()
      {
        var f = document.forms[0];
        var ob = f.cardNO;
        
        if( isEmpty(ob.value))
        {
          alert("Please Enter Card Number");
          ob.value = "";
          ob.focus();
          return false;
        }
        if( !validateInteger(ob.value))
        {
          alert("Card  Must be a Number");
          ob.value = "";
          ob.focus();
          return false;
        }
        return true;
      }

      </script>
      </head>
      <body class="body_center_align" onLoad="showPullDown('pullDownRechargeCard')">
        <table border="0" cellpadding="0" cellspacing="0" width="1024">
          <tr><td width="100%"><%@ include file="../includes/header.jsp"%></td></tr>
          <tr><td width="100%">
              <table border="0" cellpadding="0" cellspacing="0" width="1024">
                <tr><td class="td_menu"><%@ include file="../includes/menu.jsp"%>&nbsp;</td>
                  <td width="820" valign="top" class="td_main" align="center">

                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                       <tr><td width="100%" align="center">
                      
                      <table border="0" cellpadding="0" cellspacing="0" class="form1"  width="700" align="center">
               <tr><td width="100%" align="center" style="padding-bottom: 20px;">
                 <div class="div_title"><span style="vertical-align: sub;"><%=LanguageManager.getInstance().getString(LanguageConstants.Global_edit, loginDTO) %> <%=LanguageManager.getInstance().getString(LanguageConstants.RECHARGE_CARD, loginDTO) %></span></div></td></tr></table>
                      
                          <!-- start of the form  -->
                          <html:form  action="<%=actionName%>" method="POST" onsubmit="return validate();">
                            <table border="0" cellpadding="0" cellspacing="0" class="form1"  width="386">

                              <tr>
                                <td colspan="2">
                                  <input type="hidden" name="CardBatchID" value="<%=cardBatchID%>" />
                                    <input type="hidden" name="cardID" value="<%=cardID%>" />
                                    </td>
                                  </tr>

                                  <tr>
                                    <td width="167" height="22"><!--Serial NO-->
										<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_edit_pinNo_Serial,loginDTO)%>
                                    </td>
                                    <td width="219" height="22"><%=dto.cardSerialNO%></td>
                                  </tr>


                                  <tr>
                                    <td width="167" height="22"><%=LanguageManager.getInstance().getString(LanguageConstants.CURRENT_CARD_NUMBER, loginDTO) %></td>
                                    <td width="219" height="22"><%=dto.cardNO%></td>
                                  </tr>

                                  <tr>
                                    <td width="167">&nbsp;</td>
                                    <td width="219"><html:errors property="cardNumber"  /></td>
                                  </tr>

                                  <tr>
                                    <td width="167" height="22"><%=LanguageManager.getInstance().getString(LanguageConstants.NEW_CARD_NUMBER, loginDTO) %></td>
                                    <td width="219" height="22">
                                      <input type ="text" name="cardNO" size="20"/>
                                    </td>
                                  </tr>
                                  <tr>
                                    <td width="167" height="22"></td>
                                    <td width="219" height="22"></td>
                                  </tr>

                                  <tr>
                                    <td align="center" colspan="2">
                                      <input class="cmd" style="width:100;height:22"  type="reset" value="<%=globalReset%>" name="B1"/>&nbsp;
                                      <%if(permission>=2){ %>
                                      <input class="cmd" style="width:150;height:22"  type="submit" value="<%=LanguageManager.getInstance().getString(LanguageConstants.Global_submit, loginDTO) %> " name="B2"/>
                                      <%} %>
                                      </td>
                                    </tr>
                                  </table>
                                </html:form>
                                <!-- end of the form -->
                              </td></tr></table></td></tr></table></td></tr>
<tr><td width="100%"><%@ include file="../includes/footer.jsp"%></td></tr></table></body></html>
