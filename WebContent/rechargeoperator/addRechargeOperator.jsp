<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="user.UserRepository"%>
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page errorPage="failure.jsp" %>


<%@ page import="java.util.*,sessionmanager.SessionConstants,databasemanager.*,user.*,rechargeoperator.*,role.*" %>				 
				 

<%
 String title = "Recharge operator";
 String submitCaption2 = "Recharge";
 String actionName = "/AddRechargeOperator";
 RoleService rservice = util.ServiceDAOFactory.getService(RoleService.class);
 SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
%>

<html>
<head>
<html:base/>
<title><%=title %></title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css"/>
<link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css"/>

<script language="JavaScript" src="../scripts/util.js"></script>

<script type="text/javascript">
	 
function validate(f)
{
	
	var eDate = f.rechargeScrollTime;
	if (isEmpty(eDate.value)) {
		eDate.value = "";
	} else if (!checkDate2(eDate.value)) {
		eDate.value = "";
		eDate.focus();
		return false;
	}
}

</script>

</head>

<body class="body_center_align" onload="document.forms[0].dslmName.focus();">


<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="780" id="AutoNumber1">

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/header.jsp"  %>
    </td>
  </tr>

  <!--center-->
  <tr>
    <td width="100%">
    <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="780" id="AutoNumber2">
      <tr>
	    <!--left menu-->
       
	    <!--main -->
        <td width="780" valign="top" class="td_main" align="center">

        <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber3">
          <tr>
            <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
            <div class="div_title"><%=title %></div>
			</td>
          </tr>
		 
            <td width="100%" align="center">

<br><br>
<!-- start of the form  -->

<html:form  action="<%=actionName %>" method="POST" onsubmit="return validate(this);">
                  <table  width="402" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                    <%
  String msg = null;
  if( (msg = (String)session.getAttribute(util.ConformationMessage.RECHARGE_OPERATOR_ADD))!= null)
  {
    session.removeAttribute(util.ConformationMessage.RECHARGE_OPERATOR_ADD);
    %>
    
    
                    <tr> 
                      <td colspan="3" align="center" valign="top" height="50"><b><%=msg%></b></td>
                    </tr>
                    <%}%>
                    
                    
                    <html:hidden property="rechargerID" value = "<%=Long.toString(loginDTO.getUserID())%>"  />
                 
                    
                    <tr>
                      <td  align="left" style="padding-left: 80px" height="25" width = 200>Operator</td>
                      <td height="25" align="left" style="padding-left: 80px"> <html:select property="rechargeOperator">
                          
                          <%					          
					          ArrayList operatorList = UserRepository.getInstance().getUserList();
					          for(int i=0;i<operatorList.size();i++)
					          {
					           user.UserDTO operatorDTO = (user.UserDTO)operatorList.get(i);
					           
					            if(rservice.getRole(""+operatorDTO.getRoleID()).getRestrictedtoOwn())
					            {
					            %>
                          <html:option value="<%=Long.toString(operatorDTO.getUserID())%>"><%=operatorDTO.getUsername()%></html:option>
                          <% }} %>
                        </html:select></td>
                    </tr>  
                    
                    
					 <tr> 
                      <td valign="top" height="25" align="left" style="padding-left: 80px">Recharge Amount</td>
                      <td height="25" colspan="2" align="left" style="padding-left: 80px"><html:text property="rechargeAmount" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></td>
                    </tr>
                    
                    <tr> 
                      <td valign="top" align="left" style="padding-left: 80px"></td>
                      <td align="left" style="padding-left: 80px"><html:errors property="rechargeAmount"/></td>
                    </tr>
                   
                   
					 
                     <tr> 
                      <td valign="top" height="25" align="left" style="padding-left: 80px">Voucher No</td>
                      <td height="25" colspan="2" align="left" style="padding-left: 80px"><html:text property="rechargeVoucherNo"  /><label style="color:red; vertical-align: middle;" >*</label></td>
                    </tr>                      
					<tr>
						<td align="left" style="padding-left: 80px">Date</td>
						<td height="25" colspan="2" align="left" style="padding-left: 80px">
						<html:text property = "rechargeScrollTime" size="21" readonly="readonly" value="<%=new String(format.format(new Date())) %>"/>
                             <script type="text/javascript" >
                             new tcal ({
                               // form name
                               'formname': 'rechargeOperatorForm',
                               // input name
                               'controlname': 'rechargeScrollTime'
                             });
                             </script>                           
						</td>
					</tr>
                    <tr> 
                      <td valign="top" align="left" style="padding-left: 80px"></td>
                      <td align="left" style="padding-left: 80px"><html:errors property="rechargeVoucherNo"/></td>
                    </tr>  
                    
                    
                    <TR> 
                      <TD valign="top" height="25" align="left" style="padding-left: 80px">Recharge Description</TD>
                      <TD height="25" colspan="2" align="left" style="padding-left: 80px"><html:textarea property="rechargeDescription"  /></TD>
                    </TR>     
                    
                    <tr> 
                      <td valign="top" align="left" style="padding-left: 80px"></td>
                      <td align="left" style="padding-left: 80px"><html:errors property="rechargeDescription"/></td>
                    </tr>
                                 
                    
                    <TR> 
                      <TD valign="top" height="15"></TD>
                      <TD height="15" colspan="2">&nbsp;</TD>
                    </TR>
                  </table>
                  <html:reset> Reset </html:reset><html:submit><%=submitCaption2 %></html:submit> 
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
