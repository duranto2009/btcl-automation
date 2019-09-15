<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page errorPage="../common/failure.jsp" %>


<%@ page import="java.util.ArrayList,
sessionmanager.SessionConstants,

java.sql.*,
databasemanager.*,client.*,invoiceorder.*,user.*,role.*,invoice.*" %>				 
				 

<%
 String title = "Add New Invoice Order";
 String submitCaption2 = "Add";
 String actionName = "/AddInvoiceOrder";

%>

<html>
<head>
<html:base/>
<meta name="GENERATOR" content="Microsoft FrontPage 5.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title><%=title %></title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>

<script language="JavaScript">
	 
	 function validate()
      {
        var f = document.forms[0];

        var ob = f.dslmName;
        if( isEmpty(ob.value))
        {
          alert("Please enter InvoiceOrder Name");
          ob.value = "";
          ob.focus();
          return false;
        }

       
        return true;
      }
	 
	
	 
	 function handleClick(value)
	 {
		// alert(value);
		 if(value == 5)
		 {		 	
	 		document.getElementsByName("invoicePerUnitCost")[0].disabled = true;
		 }
	 		
	 	 else
	 	 {
	 		document.getElementsByName("invoicePerUnitCost")[0].disabled = false;
	 	 }
		
	 }
	 
	 function init()
	 {
		 document.getElementsByName("invoiceOrderType")[0].checked = true;
	 }

</script>

</head>

<body class="body_center_align" onload="init();">


<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="1024" id="AutoNumber1">

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/header.jsp"  %>
    </td>
  </tr>

  <!--center-->
  <tr>
    <td width="100%">
    <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="1024" id="AutoNumber2">
      <tr>
	    <!--left menu-->
       
	    <!--main -->
        <td width="1024" valign="top" class="td_main" align="center">

        <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber3">
          <tr>
            <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
            <div class="div_title"><%=title %></div>
			</td>
          </tr>
		 
            <td width="100%" align="center">

<br><br>
<!-- start of the form  -->

<html:form  action="<%=actionName %>" method="POST" onsubmit="return validate();">
                  <table  width="402" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                    <%
  String msg = null;
  if( (msg = (String)session.getAttribute(util.ConformationMessage.INVOICE_ORDER_ADD))!= null)
  {
    session.removeAttribute(util.ConformationMessage.INVOICE_ORDER_ADD);
    %>
                    <tr> 
                      <td colspan="3" align="center" valign="top" height="50"><b><%=msg%></b></td>
                    </tr>
                    <%}%>
                    
                    <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="invoiceOrderOperator"  /></TD>
                    </TR>
					 <TR> 
                      <TD valign="top" height="25" align="left">Order Operator</TD>
                      <TD height="25" colspan="2" align="left">
                      <html:select size="1" property="invoiceOrderOperator">
					 
					 <%					
					    RoleService rservice = util.ServiceDAOFactory.getService(RoleService.class);
					    ArrayList userList = user.UserRepository.getInstance().getUserList();
					    for(int i=0;i<userList.size();i++)
					    {
					      UserDTO userDTO = (UserDTO)userList.get(i);
					      out.println(rservice.getRole(""+userDTO.getRoleID()).getRestrictedtoOwn());
					      if(rservice.getRole(""+userDTO.getRoleID()).getRestrictedtoOwn())
					      {
					 %>
					               <html:option value="<%=Long.toString(userDTO.getUserID())%>" ><%=userDTO.getUsername()%></html:option>
					 <%   }
					    }
					 %>
 
 
</html:select>
                      </TD>
                    </TR>
                   
                    <tr>
                      <td width="200" style="padding-right: 0" height="25" align="left">Invoice Type</td>
                      <td height="25" align="left"> 
                          
                          <%					          
					        
					          for(int i=0;i<InvoiceConstants.INVOICE_TYPE_VALUE.length;i++)
					          {
					            
					            %>
                          <html:radio value="<%=Integer.toString(InvoiceConstants.INVOICE_TYPE_VALUE[i])%>" property = "invoiceOrderType"  onclick = "handleClick(this.value);" /><%=InvoiceConstants.INVOICE_TYPE_NAME[i]%>
                          <% } %>
                       </td>
                    </tr> 
                    <tr>
                        <td width="194" style="padding-right: 0" height="25" align="left">Total Number of Invoice</td>
                        <td width="292" height="25" align="left"> <html:text property="invoiceOrderCount"/><label style="color:red; vertical-align: middle;" >*</label></td>
                      </tr>
                      
                       <tr>
                        <td width="194" align="left"></td>
                        <td width="292"align="left"> <html:errors property="invoiceOrderCount"/></td>
                      </tr>
                      
                       <TR> 
                      <TD valign="top" height="25" align="left">Invoice Per Unit Cost</TD>
                      <TD height="25" colspan="2" align="left"><html:text property="invoicePerUnitCost"  /></TD>
                    </TR>    
                    
                     <tr>
                        <td width="194" align="left"></td>
                        <td width="292"align="left"> <html:errors property="invoicePerUnitCost"/></td>
                      </tr>
                    
					 <TR> 
                      <TD valign="top" height="25" align="left">Description</TD>
                      <TD height="25" colspan="2" align="left"><html:textarea property="invoiceOrderDescription"  /></TD>
                    </TR> 
                    
                    <tr>
                        <td width="194" align="left"></td>
                        <td width="292"align="left"> <html:errors property="invoiceOrderDescription"/></td>
                      </tr>    
                      
                       <tr>
                        <td width="194" align="left"></td>
                        <td width="292"align="left"> <html:errors property="notEnoughBalance"/></td>
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
