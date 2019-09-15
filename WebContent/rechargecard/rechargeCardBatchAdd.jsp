<%@page import="topuprateplan.TopUpRatePlanDTO"%>
<%@page import="topuprateplan.TopUpRatePlanRepository"%>
<%@page import="rateplan.RatePlanService"%>
<%@ include file="../includes/checkLogin.jsp"%>
<%
int permission=-1;
boolean hasPermission=false;
boolean hasIncreaseByPermission=false;
if( (loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_PREPAID_CARD )!=-1&&(loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_PREPAID_CARD_GENERATE) !=-1)))
{
	hasPermission=true;
	permission=loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_PREPAID_CARD_GENERATE);
	if(loginDTO.getUserID()>0)
	hasIncreaseByPermission=loginDTO.getColumnPermission(PermissionConstants.INCREASE_BY);
}
else if(isAgent && loginDTO.getRoleID()==-1)
{
	hasPermission=true;
	permission=3;
}
if(!hasPermission)
{
	 response.sendRedirect("../");
}

if(loginDTO.getAccountID()>0 && !isAgent)
{
	ClientDTO  loginClientDTO= ClientRepository.getInstance().getClient(loginDTO.getAccountID());
		if(loginClientDTO.getDisableRatePlanCreationPermission()==0)
			hasIncreaseByPermission=loginDTO.getColumnPermission(PermissionConstants.INCREASE_BY);
}


%>
<%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page errorPage="../common/failure.jsp" %>
<%@ page import="java.util.*,sessionmanager.SessionConstants,utildao.*,java.sql.*,util.*,databasemanager.*,rechargecard.*,rate.*" %><%

String submitCaption = "Generate Recharge Card";
String actionName = "/AddRechargeCardBatch";

String months[] ={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec",};

GregorianCalendar calendar = new java.util.GregorianCalendar();

int year = calendar.get(GregorianCalendar.YEAR);
int month = calendar.get(GregorianCalendar.MONTH);
int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
int hour = calendar.get(GregorianCalendar.HOUR_OF_DAY);
int min = calendar.get(GregorianCalendar.MINUTE);
int sec = calendar.get(GregorianCalendar.SECOND);%>

<html>
  <head>
    <html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.GENERATE_RECHARGE_CARD,loginDTO)%></title>
    <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
    <script language="JavaScript" src="../scripts/util.js"></script>
    <script type="text/javascript" src="../scripts/datetimepicker.js"></script>
    <script language="JavaScript">
    
    function clearValue(ob, rateplan){
  	  if(ob.value == '0.0')
  		  ob.value = '';
  	  if(parseFloat(ob.value) > 0.0)
  			document.getElementById(rateplan).style.display="table-row";	
  		else
  			 document.getElementById(rateplan).style.display="none";
  	}
    
    function checkRatePlanName(ob){

    	xmlHttp=GetXmlHttpObject();
    	if (xmlHttp==null)
    	{
    	 alert ("Your browser does not support AJAX!");
    	 return false;
    	}      
    	focusElement = ob;
    	var url = "../rateplan/checkname.jsp?name="+ob.value;
    	xmlHttp.onreadystatechange = isAvailable;
    	xmlHttp.open("GET", url, true);
    	xmlHttp.send(null);
    	return true;
    	
    }

    function checkTopUpRatePlanName(ob){
    	
    	xmlHttp=GetXmlHttpObject();
    	  if (xmlHttp==null)
    	  {
    	     alert ("Your browser does not support AJAX!");
    	     return false;
    	  }      
    	  focusElement = ob;
    		var url = "../rateplan/checkname.jsp?topup=yes&name="+ob.value;
    		xmlHttp.onreadystatechange = isAvailable;
    		xmlHttp.open("GET", url, true);
    		xmlHttp.send(null);
    		return true;
    		
    	}
    function isAvailable() 
    { 
    if (xmlHttp.readyState==4)
    {
     if(xmlHttp.responseText.replace(/^\s+|\s+$/g,"")=="true"){
    	   //alert("");
     }
     else{
    	   alert("RatePlan Name already Exists, Please choose a different name");
    	   focusElement.value="";
     }
     

    }
    }

    function GetXmlHttpObject()
    {
    var xmlHttp=null;
    try
    {
    //Firefox, Opera 8.0+, Safari
    xmlHttp=new XMLHttpRequest();
    }
    catch (e)
    {
    //Internet Explorer
    try
    {
     xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
    }
    catch (e)
    {
     xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    }
    return xmlHttp;
    }	
    
    function showTopUpRatePlan(){
    	  var form = document.forms[0];
    		var topUpRatePlanID = -1;
    		topUpRatePlanID = form.topupRatePlanID.options[form.topupRatePlanID.selectedIndex].value;
    		if(topUpRatePlanID == -1)
    		{
    			alert("Please select a Top Up Rate plan first");
    			return false;
    		}else{
    		window.open ("<%=request.getContextPath().toString()%>/ViewTopUpRatePlanDetails.do?TopUpRatePlanID=" +topUpRatePlanID ,"","location=1,status=1,scrollbars=1, addressbars = 1, width=800,height=700,resizable=1");
    		return false;
    		}
    		 
    	}
    function showRatePlan(){
    	var form = document.forms[0];
    	var ratePlanID = -1;

    	
    		ratePlanID = form.ratePlanID.options[form.ratePlanID.selectedIndex].value;
    	
    	if(ratePlanID == -1)
    	{
    		alert("Please select a rate plan first");
    		return false;
    	}else{
    		window.open ("<%=request.getContextPath().toString()%>/ViewRatePlanDetails.do?RatePlanID=" +ratePlanID ,"","location=1,status=1,scrollbars=1, addressbars = 1, width=600,height=600,resizable=1");
    		return false;
    	} 
    }
    function init()
    {
      var form = document.forms[0];
      form.Activation_year.value = <%=year%>;
      form.Activation_month.value = <%=month%>;
      form.Activation_day.value = <%=day%>;
      form.Activation_hour.value = <%=hour%>;
      form.year.value = <%=year+2%>;
      form.month.value = <%=month%>;
      form.day.value = <%=day%>;
      form.hour.value = <%=hour%>;
      form.cardStatus[0].click();
      form.allowAllClient[1].click();
      
     
<%--       document.getElementById("ratePlanModificationFactor").style.display="block";
      document.getElementById("orgNewRatePlan").style.display="block";
      //document.getElementById("topUpModificationFactor").style.display="table-row";
      <%}%> --%>
      
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

        if(digit !="." &&(digit < "0" || digit > "9")) return false;
      }

      return true;
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

      if( !validateInteger(ob.value))
      {
        alert("Card Prefix Must be a  integer");
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
        alert("Please enter the digit length of card number");
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

      var ob = f.extendExpireDays;
      if( isEmpty(ob.value))
      {
        alert("Please enter Expiration extend days");
        ob.value = "";
        ob.focus();
        return false;
      }
      if( !isInteger(ob.value))
      {
        alert("Pin expiration extension must be a positive days");
        ob.value = "";
        ob.focus();
        return false;
      }
      
      
      var ob=f.ratePlanModificationFactor;
      var rpID = f.ratePlanID;
      var newrpName = f.orgNewRP;
     
      if((rpID.options[rpID.selectedIndex].value=="-1") && (parseFloat(ob.value)!=0.0)){
      	alert("Please Select a rateplan to apply modification factor");
      	rpID.focus();
      	return false;
      }else if((rpID.options[rpID.selectedIndex].value!="-1") && (parseFloat(ob.value)!=0.0) && newrpName.value==""){
      	alert("Please give a new rateplan name");
      	newrpName.focus();
      	return false;
      }
      if(!isSignedInteger(ob.value))
      {
    	  alert(" Rate plan modification percentage Must be a Number");
    	   ob.value = "0.0";
    	   ob.focus();
    	  return false;
    		  
       }
      else if(ob.value<0)
      {
        var answer = confirm("Negative Rate Plan Increase By Percentange. Would you like to Continue?");
    	   if (answer)
    	   {
    		  return true;
    	   }
    	   else
    	   {
    		  return false;
    	   }
      }
      <%if(mtuEnabled){%>
      var ob=f.topUpRatePlanModificationFactor;
      var rpID = f.topupRatePlanID;
      var newrpName = f.muNewRP;

      if((rpID.options[rpID.selectedIndex].value=="-1") && (parseFloat(ob.value)!=0.0)){
      	alert("Please Select a rateplan to apply modification factor");
      	rpID.focus();
      	return false;
      }else if((rpID.options[rpID.selectedIndex].value!="-1") && (parseFloat(ob.value)!=0.0) && newrpName.value==""){
      	alert("Please give a new rateplan name");
      	newrpName.focus();
      	return false;
      }
      if(!isSignedInteger(ob.value))
      {
       alert(" Top up Rate plan modification percentage Must be a Number");
        ob.value = "0.0";
        ob.focus();
       return false;
        
       }
      else
          if(ob.value<0)
          {
            var answer = confirm("Negative Top Up Rate plan Descrease By Percentange. Would you like to Continue?");
        	   if (answer)
        	   {
        		  return true;
        	   }
        	   else
        	   {
        		  return false;
        	   }
          }

      <%}%>
      selectAllOptions();

      setFromDate();
      setToDate();

      
      return true;
    }

    function selectAllOptions()
    {
      return true;
    }


    function setFromDate()
    {
    	 var form = document.forms[0];
    	 var activationTime=form.date_from.value;

    	 var dayAndTime=activationTime.split(" ");
		 var day=dayAndTime[0].split("-");
		 var time=dayAndTime[1].split(":");
    	 
         form.Activation_year.value = day[2];
         form.Activation_month.value = parseInt(day[1])-1;
         form.Activation_day.value = day[0];
         form.Activation_hour.value = time[0];

    }

    function setToDate()
    {
    	 var form = document.forms[0];
    	 var expireTime=form.date_to.value;


    	 var dayAndTime=expireTime.split(" ");
		 var day=dayAndTime[0].split("-");
		 var time=dayAndTime[1].split(":");
		 
         form.year.value = day[2];
         form.month.value = parseInt(day[1])-1;
         form.day.value = day[0];
         form.hour.value = time[0];    

    }
    
    </script>
  </head>

  <body class="body_center_align" onload="showPullDown('pullDownRechargeCard');init();">
  <table border="0" cellpadding="0" cellspacing="0"  width="1024">
    <tr><td width="100%"><%@ include file="../includes/header.jsp"%></td></tr>
    <tr><td width="100%">
    <table border="0" cellpadding="0" cellspacing="0"  width="1024">
      <tr><td class="td_menu">
      <%if(isAgent && loginDTO.getRoleID()==-1){ %>
      <%@ include file="../includes/resMenu.jsp"%>
      <%} else { %>
      <%@ include file="../includes/menu.jsp"%>
      <%}%>
      </td>
      
      
        <td width="820" valign="top" class="td_main" align="center">
          <table border="0" cellpadding="0" cellspacing="0" width="100%">
          <tr><td width="100%" align="center">
        
          <table border="0" cellpadding="0" cellspacing="0" class="form1"  width="700" align="center">
               <tr><td width="100%" align="center" style="padding-bottom: 20px;">
                 <div class="div_title"><span style="vertical-align: sub;"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.GENERATE_RECHARGE_CARD,loginDTO)%></span></div></td></tr></table>
  
     
    <html:form action="<%=actionName%>"  method="POST" onsubmit="return validate();">
      <table align="center" cellspacing="0" width="800">
	<tr><td width="100%"><table width="100%" align="center" cellspacing="10" cellpadding="0">
  <tr>
  <td width="790" align="center" rowspan="3">
    <table border="0" cellpadding="0" cellspacing="0" class="form1"  width="790">
    <tr><td width="170" height="22"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_Batch_Name,loginDTO)%></td>
      <td width="620" height="22"><html:text property="batchName" size="20" /></td>
  </tr>
  <%
  String message = (String)session.getAttribute(util.ConformationMessage.CARD_RECHARGE);
  if(message!= null)
  {
    session.removeAttribute(util.ConformationMessage.CARD_RECHARGE);%>
  <tr>
  <td class="green_text"><b><%=message %></b></td>
  </tr>
  <%}
  %>
  <tr>
    <td  height="22" width="170"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.CARD_PREFIX,loginDTO)%></td>
    <td  height="22" width="620"><html:text property="cardPrefix" size="20" /></td></tr>
      <tr>
        <td  height="22" width="170"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.TOTAL_CARDS,loginDTO)%></td>
        <td  height="22" width="620"><html:text property="totalCard" size="20" /></td>
      </tr>

      <tr>
        <td  height="22" width="170"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.CARD_DIGIT_LENGTH,loginDTO)%></td>
        <td  height="22" width="620"><html:text property="cardDigitLength" size="20" /></td>
      </tr>

      <tr>
        <td  height="22" width="170"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.CARD_BALANCE,loginDTO)%></td>
        
        <td  height="22" width="620"><html:select property="cardBalance" size="1" style="width: 145px;">
        <html:option value="10">10</html:option>
        	<html:option value="20">20</html:option>
        	<html:option value="50">50</html:option>
        	<html:option value="100">100</html:option>
        	<html:option value="500">500</html:option>
        </html:select></td>
      </tr>
      
       <tr>
        <td  height="22" width="170">Extend Pin Expiration</td>
        <td  height="22" width="620"><html:text property="extendExpireDays" size="20" />Days</td>
      </tr>
      
         <tr>
        <td  height="22" width="170"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.CARD_STATUS,loginDTO)%></td>
        <td width="620">
          <html:radio property="cardStatus" value="1"/><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_ADD_CLIENT_Active,loginDTO)%><html:radio property="cardStatus" value="2"/><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_Disabled,loginDTO)%>
        </td>
      </tr>
	<%if(loginDTO.getUserID()>0 ||isAgent){ %>
 		<tr>
        <td  height="22" width="170">Allowed Client</td>
        <td width="620">
          <input type="radio" name="allowAllClient" value="1" />All Client    <%if(!isAgent) {%> <input type="radio" name="allowAllClient" value="0" />Own Client Only<%} %>
        </td>
      </tr>
	<%} %>

<tr>
	<td height="22"  width="170">
		<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_Activation_Time,loginDTO)%>
	</td>
	<td height="22" width="620">
	<input type="text" style="width: 138"  name="date_from" id="from_date" value="<%=day+"-"+(month+1)+"-"+year+ " "+hour+":00:00" %>" />
      <a href="javascript:NewCal('from_date','ddmmyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0"  alt="Pick a date"></a>
      <input type="hidden" name="Activation_year" id="Activation_year">
      <input type="hidden" name="Activation_month" id="Activation_month">
      <input type="hidden" name="Activation_day" id="Activation_day">
      <input type="hidden" name="Activation_hour" id="Activation_hour">
	</td>
</tr>

<tr>
	<td height="22" width="170">
	<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Generate_pin_Expire_Date,loginDTO)%>
</td>
	<td height="22" width="620">
	<input type="text" name="date_to" id="to_date" style="width: 138" value="<%=day+"-"+(month+1)+"-"+(year+2)+ " "+hour+":00:00"  %>" />
    <a href="javascript:NewCal('to_date','ddmmyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date"></a>
     <input type="hidden" name="year" id="year">
      <input type="hidden" name="month" id="month">
      <input type="hidden" name="day" id="day">
      <input type="hidden" name="hour" id="hour">
</td>
</tr>



<tr>
	<td width="170"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.VOICE,loginDTO)%>
	<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_ADD_CLIENT_Rate_Plan,loginDTO)%></td>
	<td width="620" >
	<div style="width: 235px; float: left;">
		<select name="ratePlanID" size="1"
			style="width: 165px;">
			<%if(loginDTO.getAccountID()==-1){ %>
			<option value="-1">[None]</option>
			<%}else{ long ratePlanID=client.ClientRepository.getInstance().getClient(loginDTO.getAccountID()).getRatePlanID();
  String rateplanName=ratePlanID==-1?"[None]":rateplan.RatePlanRepository.getInstance().getRatePlanName(ratePlanID);
  %><option value="<%=Long.toString(ratePlanID)%>"><%=rateplanName%></option><% }
			
			RatePlanService ratePlanservice=new RatePlanService();
  java.util.ArrayList ratePlans = ratePlanservice.getAllRatePlans(loginDTO);
  for( int i=0;i<ratePlans.size();i++)
  {
    rateplan.RatePlanDTO ratePlanDTO = (rateplan.RatePlanDTO)ratePlans.get(i);
    if(ratePlanDTO.getIsDeleted())
    continue;%>
<option
	value="<%=Long.toString(ratePlanDTO.m_ratePlanID)%>"><%=ratePlanDTO.m_ratePlanName%></option>
<%}%>
</select><a href="#" class="view_menu"
	onclick="showRatePlan();"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.VIEW_RATE,loginDTO)%></a>
</div>
<%  if(hasIncreaseByPermission){ %> 
<div id="ratePlanModificationFactor"
	style=" width: 140px; float: left;"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MODIFY_BY,loginDTO)%>&nbsp;<input
type="text" name="ratePlanModificationFactor"
value="0.0" style="width: 35px;"
onkeyup="clearValue(this,'orgNewRatePlan');"
onclick="clearValue(this,'orgNewRatePlan');" />%&nbsp;&nbsp;
</div>
<div id="orgNewRatePlan"
	style=" width: 240px; float: left;">
<span style="width: 100px;">Rate Plan Name</span> <input
type="text" onchange="checkRatePlanName(this);"
name="orgNewRP" style="width: 120px;" />
		</div><%} %>
	</td>

</tr>

<%if(mtuEnabled) {%>
<tr id="trTopUpRatePlan">
	<td width="170"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Recharge_Agent_Top_Up_Rate_Plan,loginDTO)%>
</td>
<td width="620">
	<div style="width: 235px; float: left;">
<select name="topupRatePlanID" size="1"
	style="width: 165px;">
<%if(loginDTO.getAccountID()==-1){ %>
<option value="-1">[None]</option>
<%}else{ long topUpRatePlanID=client.ClientRepository.getInstance().getClient(loginDTO.getAccountID()).getTopUpRatePlan();
			if(topUpRatePlanID!=-1)
			{	
			String topUpRateplanName=TopUpRatePlanRepository.getInstance().getRatePlanName(topUpRatePlanID);
	 		%><option value="<%=Long.toString(topUpRatePlanID)%>"><%=topUpRateplanName%></option>
<%}%>
<option value="-1">[None]</option>
<%}

java.util.ArrayList<TopUpRatePlanDTO> topUpRatePlans = TopUpRatePlanRepository.getInstance().getAllTopUpRatePlan(loginDTO);
for( int i=0;i<topUpRatePlans.size();i++)
  {
		 		TopUpRatePlanDTO topUpRatePlanDTO =topUpRatePlans.get(i);
		  if(!topUpRatePlanDTO.getIsDeleted()){%>
<option
	value="<%=Long.toString(topUpRatePlanDTO.m_ratePlanID)%>"><%=topUpRatePlanDTO.m_ratePlanName%></option>
<%}}%>
</select><a href="#" class="view_menu"
	onclick="showTopUpRatePlan();"><%=language.LanguageManager.getInstance().getString(language.LanguageConstants.VIEW_RATE,loginDTO)%></a>
</div>
<%  if(hasIncreaseByPermission){ %> 
<div
	style=" width: 140px; float: left;"
id="topUpModificationFactor">
<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.MENU_Top_Up_Rate_Plan_Decrease,loginDTO)%>&nbsp;<input
type="text" name="topUpRatePlanModificationFactor"
value="0.0" style="width: 35px;"
onkeyup="clearValue(this,'muNewRatePlan');"
onclick="clearValue(this,'muNewRatePlan');" />%&nbsp;&nbsp;
</div>
<div id="muNewRatePlan"
	style=" width: 240px; float: left;">
<span style="width: 100px;">Rate Plan Name</span> <input
type="text" name="muNewRP"
onchange="checkTopUpRatePlanName(this);"
style="width: 120px;" />
		</div><%} %>
	</td>
</tr>
<%} %>

</table>
</td>

</tr>


</table>
</td></tr>

<tr><td style="padding-left: 170px">
<input class="cmd" style="width:70;height:25"  type="reset"" value="<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.Global_reset,loginDTO)%>"/>
<%if(permission==3){ %>
<input class="cmd" style="width:120;height:25"  type="submit" value="<%=language.LanguageManager.getInstance().getString(language.LanguageConstants.RECHARGE_CARD_GENERATE,loginDTO)%>"/>
<%} %>
</td></tr>
</table></html:form>
</td></tr></table>
</td></tr></table></td></tr>
  <tr><td width="100%"><%@ include file="../includes/footer.jsp"%></td></tr>
</table></body></html>
