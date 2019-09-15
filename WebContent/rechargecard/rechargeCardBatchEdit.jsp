<%@ include file="../includes/checkLogin.jsp" %>
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



%>
<%@ page language="java" %><%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %><%@ page errorPage="../common/failure.jsp" %>
<%@ page import="java.util.*,sessionmanager.SessionConstants,utildao.*,java.sql.*,util.*,databasemanager.*,rechargecard.*.*,rate.*" %><%

String submitCaption = "Update Prepaid Card";
String actionName = "/UpdatePrepaidCardBatch";

String months[] ={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec",};
RechargeCardBatchDTO dto = (RechargeCardBatchDTO)session.getAttribute("PrepaidCardBatchDTO");
session.removeAttribute("PrepaidCardBatchDTO");

GregorianCalendar calendar = new java.util.GregorianCalendar();
calendar.setTime(new java.util.Date(dto.getActivationTime()));
int year = calendar.get(GregorianCalendar.YEAR);
int month = calendar.get(GregorianCalendar.MONTH);
int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
int hour = calendar.get(GregorianCalendar.HOUR_OF_DAY);
int min = calendar.get(GregorianCalendar.MINUTE);
int sec = calendar.get(GregorianCalendar.SECOND);

GregorianCalendar expCalendar = new java.util.GregorianCalendar();

long at=dto.getActivationTime();




int expirationTypeIndex = 0;
long  afterFirstUseDay = 0;
if( dto.getCardExpirationSpecifiedBy() == PrepaidCardBatchDTO.EXPIRATION_TYPE_BY_DATE)
{
  expCalendar.setTime(new java.util.Date(dto.getCardExpirationTime()));
}
else
{
  expirationTypeIndex = 1;
  afterFirstUseDay = (dto.getCardExpirationTime()/(1000L*60*60*24));
}

int expYear = expCalendar.get(GregorianCalendar.YEAR);
int expMonth = expCalendar.get(GregorianCalendar.MONTH);
int expDay = expCalendar.get(GregorianCalendar.DAY_OF_MONTH);
int expHour = expCalendar.get(GregorianCalendar.HOUR_OF_DAY);


%><html><head><html:base/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_add_batch,loginDTO)%></title>
    <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
    <script language="JavaScript" src="../scripts/util.js"></script>
    <script language="JavaScript">
    function init()
    {
      var form = document.forms[0];
      form.Activation_year.value = <%=year%>;
      form.Activation_month.value = <%=month%>;
      form.Activation_day.value = <%=day%>;
      form.Activation_hour.value = <%=hour%>;

      form.year.value = <%=expYear%>;
      form.month.value = <%=expMonth%>;
      form.day.value = <%=expDay%>;
      form.hour.value = <%=expHour%>;
      form.cardStatus[0].click();
      form.expirationType[<%=expirationTypeIndex%>].click();
      form.expirationTimeSpecifiedByDaysAfterFirstUse.value= <%=afterFirstUseDay%>
    }

    function isInteger(str)
    {
      if(str.length == 0) return false;

      for(var c = 0; c < str.length; c++)
      {
        digit = str.substring(c, c+1);

        if(digit < "0" || digit > "9") return false;
      }

      return true;
    }

    function isSignedInteger(str)
    {
      if(str.length == 0) return false;
      var c = 0;
      digit = str.substring(c, c+1);
      if( digit == "-")
      c = c + 1;
      for(; c < str.length; c++)
      {
        digit = str.substring(c, c+1);

        if(digit < "0" || digit > "9") return false;
      }

      return true;
    }

    function setExpirationType()
    {
      var form = document.forms[0];

      form.year.disabled=false;
      form.month.disabled=false;
      form.day.disabled=false;
      form.hour.disabled=false;
      form.expirationTimeSpecifiedByDaysAfterFirstUse.disabled=true;
    }


    function setExpirationDay()
    {
      var form = document.forms[0];

      form.year.disabled=true;
      form.month.disabled=true;
      form.day.disabled=true;
      form.hour.disabled=true;
      form.expirationTimeSpecifiedByDaysAfterFirstUse.disabled=false;
      form.expirationTimeSpecifiedByDaysAfterFirstUse.value=90;
    }

    function validate()
    {
      var f = document.forms[0];

      var ob = f.batchName;
      if( isEmpty(ob.value))
      {
        alert("Please enter Batch Name");
        ob.value = "";
        ob.focus();
        return false;
      }
      var ob = f.cardPrefix;
      if( isEmpty(ob.value))
      {
        alert("Please enter Card Prefix");
        ob.value = "";
        ob.focus();
        return false;
      }

      if( !isNum(ob.value))
      {
        alert(" Card Prefix Must be a  Number");
        ob.value = "";
        ob.focus();
        return false;
      }
      var ob = f.totalCard;
      if( isEmpty(ob.value))
      {
        alert("Please enter Total Number Of Cards");
        ob.value = "";
        ob.focus();
        return false;
      }

      if( !isNum(ob.value))
      {
        alert("Total Number of Cards Must be a Positive Integer");
        ob.value = "";
        ob.focus();
        return false;
      }

      if( ob.value < 1)
      {
        alert("Total Number of Cards Must be a Positive Integer");
        ob.value = "";
        ob.focus();
        return false;

      }
      var ob = f.cardDigitLength;
      if( isEmpty(ob.value))
      {
        alert("Please enter the length of card number");
        ob.value = "";
        ob.focus();
        return false;
      }

      if( !isNum(ob.value))
      {
        alert("Card Digit Length Must be a Positive Integer");
        ob.value = "";
        ob.focus();
        return false;
      }

      if( ob.value < 1)
      {
        alert("Card Digit Length Must be a Positive Integer");
        ob.value = "";
        ob.focus();
        return false;

      }

      if( ob.value > 50)
      {
        alert("Card Digit length must be less than 50");
        ob.value = "";
        ob.focus();
        return false;
      }

      var ob = f.cardBalance;
      if( isEmpty(ob.value))
      {
        alert("Please enter Card Balance");
        ob.value = "";
        ob.focus();
        return false;
      }
      if( !isNum(ob.value))
      {
        alert("Card Balance Must be a Positive Number");
        ob.value = "";
        ob.focus();
        return false;
      }

      var ob = f.bonusBalance;
      if( isEmpty(ob.value))
      {
        ob.value = "0.0";
      }
      if( !isNum(ob.value))
      {
        alert("Bonus Balance Must be a Positive Number");
        ob.value = "";
        ob.focus();
        return false;
      }

      selectAllOptions();
      return true;
    }

    function selectAllOptions()
    {
      return true;
    }
    </script>
  </head>

  <body class="body_center_align" onload="showPullDown('pullDownPrepaidCard');init();">
  <table border="0" cellpadding="0" cellspacing="0"  width="780">
    <tr><td width="100%"><%@ include file="../includes/header.jsp"%></td></tr>
    <tr><td width="100%">
    <table border="0" cellpadding="0" cellspacing="0"  width="780">
      <tr><td class="td_menu"><%@ include file="../includes/menu.jsp"%>&nbsp;</td>
        <td width="600" valign="top" class="td_main" align="center"><table border="0" cellpadding="0" cellspacing="0" width="100%">
      <tr><td width="100%" align="center">
    	
          <table border="0" cellpadding="0" cellspacing="0" class="form1"  width="700" align="center">
               <tr><td width="100%" align="center" style="padding-bottom: 20px;">
                 <div class="div_title"><span style="vertical-align: sub;">View PIn Batch</span></div></td></tr></table>
  
  
  <html:form action="<%=actionName%>"  method="POST" onsubmit="return validate();">
    <table align="center" cellspacing="0">
<tr><td width="100%"><table width="100%" align="center" cellspacing="10" cellpadding="0">
  <tr><td width="50%" align="center" rowspan="3">
    <table border="0" cellpadding="0" cellspacing="0" class="form1"  width="100%">
                                <tr>
                                  <td width="40%" height="22"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_Batch_Name,loginDTO)%></td>
                                  <td width="60%" height="22"><html:text property="batchName" size="20"/></td>
                                </tr>
                                
                                <tr> 
                                  <td  height="22"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_prepaidcard_prefix,loginDTO)%></td>
                                  <td  height="22"><html:text property="cardPrefix" size="20" /></td>
                                </tr>
                                <tr> 
                                  <td  height="22"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_tot_num,loginDTO)%></td>
                                  <td  height="22"><html:text property="totalCard" size="20" /></td>
                                </tr>
                                <tr> 
                                  <td  height="22"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_Digit_Length,loginDTO)%></td>
                                  <td  height="22"><html:text property="cardDigitLength" size="20" /></td>
                                </tr>
                                <tr> 
                                  <td  height="22"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_Initial_Balance,loginDTO)%></td>
                                  <td  height="22"><html:text property="cardBalance" size="20" /></td>
                                </tr>
                                <tr> 
                                  <td  height="22">Bonus Balance</td>
                                  <td  height="22"><html:text property="bonusBalance" size="20" /></td>
                                </tr>
                                <tr>
                                  <td height="10"></td>
                                </tr>
                                <tr> 
                                  <td width="100%" colspan="2"> <table width="100%" class="form1" border="0" cellpadding="0" cellspacing="0">
                                      <tr> 
                                        <td width="40%" style="padding-right: 0;font-weight:bold" height="22" rowspan="2"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_Pin_Status,loginDTO)%></td>
                                        <td width="60%"> <html:radio property="cardStatus" value="1"/><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_ADD_CLIENT_Active,loginDTO)%> 
                                        </td>
                                      </tr>
                                      <tr> 
                                        <td width="60%" align="left"><html:radio property="cardStatus" value="2"/><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_Disabled,loginDTO)%></td>
                                      </tr>
                                    </table></td>
                                </tr>
                              </table>
</td>

<td width="50%">
  <table border="0" cellpadding="0" cellspacing="0" class="form1"  width="100%" align="right">
    <tr><td align="center" width="100%" style="font-weight:bold" ><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_Activation_Time,loginDTO)%></td></tr>
    <tr><td><table border="0" class="form1"  width="200" align="center">
      <tr><td width="100%">
        <table border="0" class="form1" width="100%">
          <tr>
            <td height="20" width="33%" align="center" ><!--Year--><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Value_Global_Year,loginDTO)%>
              <select size="1" name="Activation_year"><%
              for (int i = 2004; i <= 2020; i++) { %>
                <option	value="<%=i%>" ><%=i%></option><% } %>
              </select>
            </td>
            <td height="20" width="33%" align="center" ><!--Month--><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Value_Global_Month,loginDTO)%>
              <select size="1"  name="Activation_month"> <%
              for (int i = 0; i <= 11; i++) { %>
                <option	value="<%=i%>" ><%=months[i]%></option><% } %>
              </select>
            </td>
            <td height="20" width="33%" align="center" ><!--Day--><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Value_Global_Day,loginDTO)%>
              <select size="1" name="Activation_day"><%
              for (int i = 1; i <= 31; i++) { %>
                <option	value="<%=i%>" ><%=i%></option><% } %>
              </select>
            </td>
                  <td height="20" width="33%" align="center" ><!--Hour--><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Value_Global_Hour,loginDTO)%>
                    <select size="1" name="Activation_hour"> <%
                      for (int i = 0; i <= 23; i++) { %>
                      <option value="<%=i%>" ><%=i%></option><% } %>
                    </select>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>

</td></tr>

<tr><td width="300">
  <table border="0"  align="center" cellpadding="0" cellspacing="0" class="form1"  width="100%">
    <tr><td style="font-weight:bold" colspan="2"><input type="radio" name="expirationType" value="1" checked="checked"  onclick="setExpirationType();"/><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_Expire_Date,loginDTO)%></td></tr>
    <tr>
      <td  colspan="2">
        <table border="0" class="form1" width="200" align="center">
          <tr align="center">
            <td height="20" width="25%"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Value_Global_Year,loginDTO)%>
              <select size="1" name="year" ><%
              for (int i = 2004; i <= 2020; i++) { %>
                <option	value="<%=i%>" ><%=i%></option><%}%>
              </select>
            </td>
            <td height="20" width="25%"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Value_Global_Month,loginDTO)%>
              <select size="1"  name="month"> <%
              for (int i = 0; i <= 11; i++) {%>
                <option	value="<%=i%>" ><%=months[i]%></option><%}%>
              </select>
            </td>
            <td height="20" width="25%"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Value_Global_Day,loginDTO)%>
              <select size="1" name="day"><%
              for (int i = 1; i <= 31; i++) {%>
                <option	value="<%=i%>" ><%=i%></option><%}%>
              </select>
            </td>
            <td height="20" width="25%"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Value_Global_Hour,loginDTO)%>
              <select size="1" name="hour"> <%
              for (int i = 0; i <= 23; i++) {%>
                <option value="<%=i%>" ><%=i%></option><%}%>
              </select>
            </td>
          </tr>
        </table>
      </td>
    </tr>

    <tr><td style="font-weight:bold"><input type="radio" name="expirationType" value="2" onclick="setExpirationDay();"/><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_First_Use,loginDTO)%></td>
      <td><input type="text" align="left" name="expirationTimeSpecifiedByDaysAfterFirstUse" size="5" /><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_days,loginDTO)%></td>
    </tr>
</table></td></tr>
</table></td></tr>
<tr><td><table class="form1"><tr><td width="100">&nbsp;&nbsp;<b>Rate</b></td><td width="387">
  <html:select property="rateID" size="1"><%
  RateService rateService = new RateService();
  ArrayList list = (ArrayList)rateService.getDTOs(rateService.getIDs(loginDTO));
  for(int i = 0; i<list.size();i++)
  {
    RateDTO rateDTO = (RateDTO)list.get(i);

    	String rateName=rateDTO.m_firstInterval+"/"+ rateDTO.m_incrementInterval+"/"+rateDTO.m_flatRate;
    %><html:option value="<%=((rateDTO.getRateID()))%>"><%=rateName%></html:option><%
  }
   %></html:select></td></tr></table></td></tr>
<tr><td height="40"></td></tr>
</table></html:form>
</td></tr></table>
</td></tr></table></td></tr>
  <tr><td width="100%"><%@ include file="../includes/footer.jsp"%></td></tr>
</table></body></html>
