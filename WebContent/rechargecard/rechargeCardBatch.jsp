<%@page import="topuprateplan.TopUpRatePlanRepository"%>
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

int permission=loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_PREPAID_CARD_SEARCH);
%>
<%@page language="java"%>
<%@taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="java.util.ArrayList,sessionmanager.SessionConstants,rechargecard.*,java.text.*, language.*"%>
<%@page errorPage="../common/failure.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><%=LanguageManager.getInstance().getString(LanguageConstants.RECHARGE_CARD, loginDTO) %></title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js" ></script>
<script language="JavaScript" >
function CheckAll()
{
  var form = document.forms[2];
  if  (form.Check_All.checked == true)
  {
    if(form.deleteIDs.length!=undefined)
    {
      for(i=0; i<form.deleteIDs.length; i++)
      form.deleteIDs[i].checked = true;
    }
    else{
      form.deleteIDs.checked = true;
    }
  }
  else
  {
    if(form.deleteIDs.length!=undefined)
    {
      for(i=0; i<form.deleteIDs.length; i++)
      form.deleteIDs[i].checked = false;
    }
    else{
      form.deleteIDs.checked = false;
    }
  }
   return true;

}
function confirmDelete()
{
  var form = document.forms[2];
  var selected=false;

  if(form.deleteIDs==null)
  {
	  	
	  	return false;
  }
  
  if(form.deleteIDs.length!=undefined)
  {	 
    for(i=0;i<form.deleteIDs.length;i++)
    {
	  if(form.deleteIDs[i].checked==true)
		  selected=true;
	}
  }
  else if(form.deleteIDs.checked==true)
  {
	  selected=true;
  }

	if(selected==false)
	{
		alert("select Recharge Card Batch to be deleted");
		return false;
	}

   var answer = confirm("Are you sure you want to delete the selected Recharge Card Batchs?");
   if (answer)
   {
	  return true;
   }
    return false;
  
}
</script>
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
              <td width="100%">
         	<table border="0" cellpadding="0" cellspacing="0" class="form1"  width="700" align="center">
               <tr><td width="100%" align="center" style="padding-bottom: 20px;">
                 <div class="div_title"><span style="vertical-align: sub;"> <%=language.LanguageManager.getInstance().getString(language.LanguageConstants.Global_search,loginDTO)%> Recharge Card Batch</span></div></td></tr></table>
  
                </td>
              </tr>
            <%
              java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
              DecimalFormat dfTwoDigit = new DecimalFormat("0.00");
              String msg = null;
              if ((msg = (String) session.getAttribute(util.ConformationMessage.CARD_BATCH_EDIT)) != null) {
                session.removeAttribute(util.ConformationMessage.CARD_BATCH_EDIT);
            %>
              <tr>
                <td width="100%" colspan="2" align="center" valign="top" height="25" class="green_text">
                  <b><%=msg%></b>
                </td>
              </tr>
            <%}%>
              <tr>
                <td width="100%" align="center">
                  <br/>
                <%
                  String url = "ViewRechargeCardBatch";
                  String navigator = SessionConstants.NAV_CARD_BATCH;
                %>
                  <jsp:include page="../includes/navDeleteOption.jsp" flush="true">
                  <jsp:param name="url" value="<%=url %>"/>
                  <jsp:param name="navigator" value="<%=navigator %>"/>
                  </jsp:include>
                  <html:form action="/DropRechargeCardBatch" method="POST">
                    <table class="table_view" width="750">
                      <tr align="center">
                        <td class="td_viewheader" width="150"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_Batch_Name,loginDTO)%></td>
                        <td class="td_viewheader" width="50"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Search_pin_Prefix2,loginDTO)%></td>
                        <td class="td_viewheader" width="100"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.TOTAL_CARDS,loginDTO)%></td>
                        <td class="td_viewheader" width="80"> Allow All Client</td>
                        <td class="td_viewheader" width="100"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.CARD_BALANCE,loginDTO)%></td>
                        <td class="td_viewheader" width="100">Ext.Pin Expiration <br/> in days</td>
                        <td class="td_viewheader" width="120"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_Activation_Time,loginDTO)%></td>
                        <td class="td_viewheader" width="120"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.EXPIRATION_TIME,loginDTO)%></td>
                        <td class="td_viewheader" valign="top" width="122" height="25"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_ADD_CLIENT_Rate_Plan,loginDTO)%></td>
						     <%if(mtuEnabled){%>
						     <td class="td_viewheader" valign="top" width="122" height="25"> <%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Top_UP_RatePlan_Add_Title,loginDTO)%></td>
						     <%} %>
                       
                        
                        <%if(permission==3){ %>
                        <td class="td_viewheader" width="100">
                          <input type="submit" value="<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.Global_delete,loginDTO)%>" onclick="return confirmDelete()"/><input type="checkbox" name="Check_All" value="CheckAll" onClick="CheckAll();">
                        </td><% }%>
                      </tr>
                    <%
                      ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_CARD_BATCH);
                      if (data != null) {
                        int size = data.size();
                        int c=0;
                        for (int i = 0; i < size; i++) {
                          RechargeCardBatchDTO row = (RechargeCardBatchDTO) data.get(i);
	
						String style="";
                        if(row.isDeleted)
						{
                        	style="style=\"color:#666666;\"";
						}
                        
                      
                        String ratePlanName = rateplan.RatePlanRepository.getInstance().getRatePlanName(row.m_ratePlanID);
                        if(ratePlanName == null)ratePlanName = "";
                        String mtuRatePlan="";
                   		 long  topupRatePlanID=row.m_TopUpRatePlan;
                   		if(topupRatePlanID!=-1)
                   		{
                   			mtuRatePlan= TopUpRatePlanRepository.getInstance().getRatePlanName(topupRatePlanID);
                   			if(mtuRatePlan==null)
                   				mtuRatePlan="";
                   		}
                        
                    %>
                      <tr align="center" <%=style%> class="td_viewdata<%=(c++%2+1)%>">
                        <td  align="center">
                          <a  class="view_menu" href="../ViewRechargeCardBatchDetails.do?RechargeCardBatchID=<%=row.getBatchID()%>"><%=row.getBatchName()%></a>
                        </td>
                        <td ><%=row.getCardPrefix()%></td>
                        <td ><%=row.getTotalCard()%></td>
                         <td ><%=row.allowAllClient==1?"Yes":"No"%></td>
                        <td ><%=dfTwoDigit.format(row.getCardBalance())%></td>
                        <td ><%=row.getExtendExpireDays()%></td>
                        <td ><%=sdf.format(new java.util.Date(row.getActivationTime()))%></td>
                        <td ><%=sdf.format(new java.util.Date(row.getCardExpirationTime()))%></td>
                        <td  width="122">
          				<a  class="view_menu"  href="../ViewRatePlanDetails.do?RatePlanID=<%=row.m_ratePlanID%>"><%=ratePlanName%></a>
          				</td>
                            <%if(mtuEnabled){%>
           						<td  > <a  class="view_menu"  href="../ViewTopUpRatePlanDetails.do?TopUpRatePlanID=<%=topupRatePlanID%>"><%=mtuRatePlan %></a></td>
          					 <%} %>
                        <%if(permission==3){ %>
                        <td align="center" >
                          <input type="checkbox" name="deleteIDs" value="<%=row.getBatchID()%>">
                        </td><%} %>
                      </tr>
                    <%
                      }
                    }
                    %>
                    </table>
                  </html:form>
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
