<%@page import="topuprateplan.TopUpRatePlanRepository"%>
<%@page import="language.LanguageConstants"%>
<%@page import="javax.security.auth.callback.LanguageCallback"%>
<%@page import="language.LanguageManager"%>
<%@include file="../includes/checkLogin.jsp"%>
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
int  permission=loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_PREPAID_CARD_SEARCH);
%>
<%@page language="java"%>
<%@taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="java.util.*,java.text.*,sessionmanager.SessionConstants, rechargecard.*,java.text.*"%>
<%@page errorPage="../common/failure.jsp"%>
<%
  String rechargeCardBatchID = (String) session.getAttribute("RechargeCardBatchID");
  RechargeCardBatchDAO dao = new RechargeCardBatchDAO();
  
  rechargecard.RechargeCardBatchDTO cardBatchDTO = dao.getRechargeCardBatch(rechargeCardBatchID);
	if(cardBatchDTO.m_parent!=loginDTO.getAccountID())
	{
		request.getSession(true).setAttribute(SessionConstants.FAILURE_MESSAGE,"Unauthorize Batch ID!");
		response.sendRedirect("../common/failure.jsp");
	}
  
	
	 String ratePlanName = rateplan.RatePlanRepository.getInstance().getRatePlanName(cardBatchDTO.m_ratePlanID);
     if(ratePlanName == null)ratePlanName = "";
     String mtuRatePlan="";
		 long  topupRatePlanID=cardBatchDTO.m_TopUpRatePlan;
		if(topupRatePlanID!=-1)
		{
			mtuRatePlan= TopUpRatePlanRepository.getInstance().getRatePlanName(topupRatePlanID);
			if(mtuRatePlan==null)
				mtuRatePlan="";
		}
		
	
  DecimalFormat dfTwoDigit = new DecimalFormat("0.00");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><%=LanguageManager.getInstance().getString(LanguageConstants.CARD_BATCH, loginDTO)%>&nbsp;<%=LanguageManager.getInstance().getString(LanguageConstants.GLOBAL_DETAILS, loginDTO) %></title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js" type=""></script>
<script language="JavaScript" type=""></script>
</head>
<body class="body_center_align" onLoad="showPullDown('pullDownRechargeCard');">
<table border="0" cellpadding="0" cellspacing="0" width="1024">
  <tr>
    <td width="100%">
      <%@include file="../includes/header.jsp"%>
    </td>
  </tr>
  <tr>
    <td width="100%">
      <table border="0" cellpadding="0" cellspacing="0" width="1024">
        <tr>
          <td class="td_menu">
                        <%if(isAgent && loginDTO.getRoleID()==-1){ %>
      <%@ include file="../includes/resMenu.jsp"%>
      <%} else { %>
      <%@ include file="../includes/menu.jsp"%>
      <%}%>
          </td>
          <td width="820" valign="top" class="td_main" align="center">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
              <tr>
              
              <td width="100%" align="center">
                  <table border="0" cellpadding="0" cellspacing="0" class="form1"  width="700" align="center">
               <tr><td width="100%" align="center" style="padding-bottom: 20px;">
                 <div class="div_title"><span style="vertical-align: sub;"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.GENERATE_RECHARGE_CARD,loginDTO)%></span></div></td></tr></table>
  				 </td>
  				
               
              </tr>
              <tr>
                <td  style="padding-right: 60;"  align="right">
                  <a href="blockUnblockCardSeries.jsp?id=<%=Long.toString(cardBatchDTO.getBatchID())%>">Block/Unblock Card Series</a>
                </td>
              </tr>
            <%
              String msg = null;
              if ((msg = (String) session.getAttribute(util.ConformationMessage.CARD_BATCH_DETAILS_EDIT)) != null) {
                session.removeAttribute(util.ConformationMessage.CARD_BATCH_DETAILS_EDIT);
            %>
              <tr>
                <td width="100%" colspan="2" align="center" valign="top" style="padding-top:  20px;" height="25" class="green_text">
                  <b><%=msg%></b>
                </td>
              </tr>
            <%}%>
              <tr>
                <td width="100%" align="center">
                  <br/>
                <%
                  String url = "ViewRechargeCardBatchDetails";
                  String navigator = SessionConstants.NAV_CARD_BATCH_DETAILS;
                %>
                  <jsp:include page="../includes/navDeleteOption.jsp" flush="true">
                  <jsp:param name="url" value="<%=url %>"/>
                  <jsp:param name="navigator" value="<%=navigator %>"/>
                  </jsp:include>
                  <table class="table_view" width="650">
                    <tr align="center">
                      <td class="td_viewheader"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_pin_history_serial,loginDTO)%></td>
                      <td class="td_viewheader"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.CARD,loginDTO)%></td>
                      <td class="td_viewheader"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_ADD_CLIENT_Status,loginDTO)%></td>
                      <td class="td_viewheader"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.CARD_BALANCE,loginDTO)%></td>
                      <td class="td_viewheader"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.Global_custID,loginDTO)%></td>
                      <td class="td_viewheader"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.DATE,loginDTO)%></td>
                      
                       <td class="td_viewheader"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_ADD_CLIENT_Rate_Plan,loginDTO)%></td>
						     <%if(mtuEnabled){%>
						     <td class="td_viewheader" > <%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Top_UP_RatePlan_Add_Title,loginDTO)%></td>
						     <%} %>
                      <%if(permission>=2){ %>
                      <td class="td_viewheader"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.Global_edit,loginDTO)%>  </td>
                      <%} %>
                    </tr>
                  <%
                    ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_CARD_BATCH_DETAILS);
                    if (data != null) {
                      int size = data.size();
                      int c=0;
                      for (int i = 0; i < size; i++) {
                        CardDTO cardDTO = (CardDTO) data.get(i);
                       
                        String customerID = "";
                        String rechargeDate = "";
                        if (cardDTO.cardAccountID != -1) {
                          client.ClientDTO clientDTO = client.ClientRepository.getInstance().getClient(cardDTO.cardAccountID);
                          if (clientDTO != null) {
                            customerID = clientDTO.getCustomerID();
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                            rechargeDate = sdf.format(new Date(cardDTO.cardRefillTime));
                          }
                        }
						
                        String style="";
                        if(cardDTO.isDeleted)
						{
                        	style="style=\"color:#666666;\"";
						}
                        else  if(cardDTO.cardStatus==CardDTO.CARD_STATUS_DISABLE)
                        {
                        	style="style=\"color:#ff0000;\"";
                        }
					%>
                    <tr align="center" <%=style%>  class="td_viewdata<%=(c++%2+1)%>">
                      <td ><%=cardDTO.cardSerialNO%></td>
                      <td ><%=cardDTO.cardNO%></td>
                      <td ><%=cardDTO.getStatusStr()%></td>
                      <td><%=dfTwoDigit.format(cardBatchDTO.getCardBalance())%></td>
                      <td ><%=customerID%></td>
                      <td><%=rechargeDate%></td>
                      <td >
          				<a  class="view_menu"  href="../ViewRatePlanDetails.do?RatePlanID=<%=cardBatchDTO.m_ratePlanID%>"><%=ratePlanName%></a>
          				</td>
                            <%if(mtuEnabled){%>
           						<td  > <a  class="view_menu"  href="../ViewTopUpRatePlanDetails.do?TopUpRatePlanID=<%=cardBatchDTO.m_TopUpRatePlan%>"><%=mtuRatePlan %></a></td>
          					 <%} %>
                      
                      <%if(permission>=2){ %>
                      <%if(  cardDTO.isDeleted == false ) {%>
                      <td >
                        <a  class="view_menu" href="rechargeCardBatchDetailsEdit.jsp?CardID=<%=cardDTO.cardID%>&CardBatchID=<%=cardDTO.cardBatchID%>">
						<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.Global_edit,loginDTO)%></a>
                      </td>
                      <%} else {%>
                      <td></td>
                      <%}}%>
                    </tr>
                  <%}}%>
                  </table>
                  <br/>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td width="100%">
      <%@include file="../includes/footer.jsp"%>
    </td>
  </tr>
</table>
</body>
</html>
