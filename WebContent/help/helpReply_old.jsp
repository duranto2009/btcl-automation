<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../includes/checkLogin.jsp" %>
<%java.text.SimpleDateFormat help_date = new java.text.SimpleDateFormat ("yyyy-MM-dd"); %>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page errorPage="../common/failure.jsp" %>

 <%@ page import="java.util.ArrayList,java.util.*,
				  java.util.Date,java.text.*,sessionmanager.SessionConstants,
		 		  help.*,java.sql.*,databasemanager.*,java.lang.Integer,java.lang.*" %>

<%
 String title = "Search and Add new Complain ";
 String submitCaption = "Add new Complain ";
 String actionName = "/AddHelpRequest";
 String token = (String)session.getAttribute("id");
 ArrayList helpList = (ArrayList) session.getAttribute("HelpList");
%>

<html>
<head>
<html:base/>
<title><%=title %></title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>

<script language="JavaScript">
	 function validate()
      {
        var f = document.forms[0];

        var ob = f.helpSub;
        if( isEmpty(ob.value))
        {
          alert("Please enter Subject");
          ob.value = "";
          ob.focus();
          return false;
        }

        var ob = f.helpDesc;
        if( isEmpty(ob.value))
        {
          alert("Please enter your Message");
          ob.value = "";
          ob.focus();
          return false;
        }

        return true;
      }
</script>

</head>

<body class="body_center_align" >


<table border="0" cellpadding="0" cellspacing="0" width="1024" id="AutoNumber1">

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/header_client.jsp"  %>
    </td>
  </tr>

  <!--center-->
  <tr>
    <td width="100%">
    <table border="0" cellpadding="0" cellspacing="0" width="1024" id="AutoNumber2">
      <tr>
        
        <td width="1024" valign="top" class="td_main" align="center">

        <table border="0" cellpadding="0" cellspacing="0" width="100%" id="AutoNumber3">
          <tr>
            <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
            <div class="div_title"><%=title %></div></td>
          </tr>

          <tr><td width="100%" align="center">

<br><br>
                  <!-- start of the form  -->
                  <html:form  action="<%=actionName %>" method="POST" onsubmit="return validate();">
                  <table  width="479" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                    <TR>
                      <TD height="22" width="119">Token Code</TD>
                      <TD height="22" colspan="5"><strong>:</strong>  <%=token%>
                      <input type="hidden" name="tokenCode" value="<%=token%>"/>
                      </TD>
                    </TR>
					<%
                    String subject = (String)request.getSession(true).getAttribute("subject");
                    %>
					<TR>
                      <TD height="22" width="119">Subject</TD>
                      <TD height="22" colspan="5"><strong>:</strong>  <%=subject%>
                      </TD>
                    </TR>
                    <TR>
                      <TD height="22" width="119"></TD>
                      <TD height="22" colspan="5"></TD>
                    </TR>
                    <tr>
                      <td colspan="6"> <table >
                      <%
                      String prevTokenCode = "";
                      for( int i=0 ; i < helpList.size();i++)
                      {
                        HelpDTO dto = (HelpDTO) helpList.get(i);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String date = formatter.format(new java.util.Date(dto.getRequestTime()));
						String date2 = formatter.format(new java.util.Date(dto.getReplyTime()));
						 String tokenCode = dto.m_helpID;
                        if(dto.m_replyDesc==null)
                        {
                          dto.m_replyDesc=" ";
						  date2 =" ";
                        }
                        
                        if(!tokenCode.equalsIgnoreCase(prevTokenCode))
						  {
							  prevTokenCode = tokenCode;
	  %>
                          
                           <tr>
                            <td class="td_viewdata1"  align="left" colspan = "3" height="10"></td>
                            
                          </tr>
                          <tr>
						    <td class="td_viewdata2"  align="left"width="75"><strong>Complain
                              <input name="hrID" type ="hidden" value="<%=dto.m_hrID %>" />
                              </strong></td>
                            <td class="td_viewdata2"  align="left"width="75"><%=date%></td>
                            <td class="td_viewdata2"  align="left"width="390"><%=dto.m_helpDesc%></td>
                           </tr><%}%>
                          <tr>
						  <td class="td_viewdata1"  align="left"width="75"><strong>Reply</strong></td>
                            <td class="td_viewdata1"  align="left"width="75"><%=date2%></td>
                            <td class="td_viewdata1"  align="left"width="390"><%=dto.m_replyDesc%></td>
                          </tr>
                          <%  }

%>
                        </table></td>
                    </tr>
                    <TR>
                      <TD height="20" width="119"></TD>
                      <TD height="20" colspan="5"></TD>
                    </TR>
					<%
					String status = (String)session.getAttribute("status");

					int st=Integer.valueOf(status).intValue();
					 if(st==1)
					{%>
                    <TR>
                      <TD height="20" colspan="2" valign="top"><strong>Do you
                        want to add new complain?</strong></TD>
                      <TD width="47" valign="top"><a href="../GetHelpYes.do?id=<%=token%>" >Yes</a></TD>
                      <TD width="131" valign="top"><a href="../ViewHelp.do" >No</a></TD>
                      <TD width="46" valign="top">&nbsp;</TD>
                      <TD width="45" valign="top">&nbsp;</TD>
                    </TR>
                   <%}%>
                    <TR>
                      <TD valign="top" height="15"></TD>
                      <TD height="15" colspan="6">&nbsp;</TD>
                    </TR>
                    <TR>
                      <TD valign="top" height="10"></TD>
                      <TD height="10" colspan="5"></TD>
                    </TR>
                  </table>
                  </html:form><br>


            </td>
          </tr>
        </table>

        </td>
      </tr>
    </table>
    </td>
  </tr>

  <tr><td width="100%"><%@ include file="../includes/footer.jsp"%></td></tr>
</table></body></html>
