<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../includes/checkLogin.jsp" %>
<%java.text.SimpleDateFormat help_date = new java.text.SimpleDateFormat ("yyyy-MM-dd"); %>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page errorPage="../common/failure.jsp" %>

<%@ page import="java.util.ArrayList, java.util.*,java.util.Date,java.text.*,
sessionmanager.SessionConstants,help.*,java.sql.*,databasemanager.*" %>

<%
 String title = "Search and Add new Complain ";
 String submitCaption = "Send Complain ";
 String actionName = "/AddHelpRequest";

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

<body class="body_center_align" onload="document.forms[0].helpDesc.focus();">


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
	    <!--left menu-->
       
	    <!--main -->
        <td width="1024" valign="top" class="td_main" align="center">

        <table border="0" cellpadding="0" cellspacing="0" width="100%" id="AutoNumber3">
          <tr>
            <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
            <div class="div_title"><%=title %></div>
			</td>
          </tr>
          <tr>

            <td width="100%" align="center">

<br><br>
                  <!-- start of the form  -->
                  <html:form  action="<%=actionName %>" method="POST" onsubmit="return validate();">
                  <table  width="472" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                    <%
                    String token = (String)request.getSession(true).getAttribute("id");
                    String subject = (String)request.getSession(true).getAttribute("subject");
                    %>
                    <TR>
                      <TD height="22" width="96"><strong>Token</strong></TD>
                      <TD width="11"><strong>:</strong></TD>
                      <TD height="22" colspan="6"><%=token%><input type="hidden" name="tokenCode" value="<%=token%>"/> </TD>
                    </TR>
					<TR>
                      <TD height="22" width="96"><strong>Subject</strong></TD>
                      <TD width="11"><strong>:</strong></TD>
                      <TD height="22" colspan="6"><%=subject %><input type="hidden" name="helpSub" value="<%=subject%>"/>
                        <input type="hidden" name="accountID" value="<%=Long.toString(loginDTO.getAccountID())%>"/></TD>
                    
					</TR>
                    <TR>
                      <TD height="22" colspan="3"></TD>
                      <TD height="22" colspan="5"></TD>
                    </TR>
                    <tr>
                      <td colspan="8"> <table >
                          <tr>
                            <td class="td_viewheader"  align="left"width="75"><div align="center"><strong>Date
                                </strong></div></td>
                            <td class="td_viewheader"  align="left"width="75"><div align="center"><strong>Com/Rep</strong></div></td>
                            <td class="td_viewheader"  align="left"width="301"><div align="center"><strong>Messege</strong></div></td>
                          </tr>
                          <%
                          for( int i=0 ; i < helpList.size();i++)
                          {
                            HelpDTO dto = (HelpDTO) helpList.get(i);
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
                            String date = formatter.format(new java.util.Date(dto.getRequestTime()));
                            String date2 = formatter.format(new java.util.Date(dto.getReplyTime()));
                            if(dto.m_replyDesc==null)
                            {
                              dto.m_replyDesc=" ";
							  date2 =" ";
                            }
	  %>
                          <tr>
                            <td class="td_viewdata2"  align="left"width="75"><strong>Complain</strong></td>
                            <td class="td_viewdata2"  align="left"width="75"><%=date%></td>
                            <td class="td_viewdata2"  align="left"width="301"><%=dto.m_helpDesc%></td>
                          </tr>
                          <tr>
                            <td class="td_viewdata1"  align="left"width="75"><strong>Reply
                              <input name="hrID" type ="hidden" value="<%=dto.m_hrID %>" />
                              </strong></td>
                            <td class="td_viewdata1"  align="left"width="75"><%=date2%></td>
                            <td class="td_viewdata1"  align="left"width="301"><%=dto.m_replyDesc%>
                            </td>
                          </tr>
                          <%  }

%>
                        </table></td>
                    </tr>
                    <TR>
                      <TD height="20" colspan="3"></TD>
                      <TD height="20" colspan="5"></TD>
                    </TR>
                   <%--  <TR>
                      <TD height="20" colspan="6" valign="top"><strong>Do you
                        want to add again a new complain?</strong></TD>
                      <TD width="28" height="20" valign="top"><a href="../GetHelpYes.do?id=<%=token%>" >Yes</a></TD>
                      <TD width="96" height="20" valign="top"><a href="../ViewHelp.do" >No</a></TD>
                    </TR> --%>
                    <TR>
                      <TD height="35" colspan="8" valign="top"><strong>Add your
                        new complain here:</strong></TD>
                    </TR>
                    <TR>
                      <TD height="22" colspan="2" valign="top"><strong>Complain
                        Message</strong></TD>
                      <TD width="229" height="22" valign="top"><textarea cols="50" rows="6"  name="helpDesc" /></textarea></TD>
                      <TD height="22" colspan="6"></TD>
                    </TR>
                    <TR>
                      <TD height="22" colspan="3" valign="top"> </TD>
                      <TD height="22" colspan="6"><input name="requestTime" type ="hidden" value="<%=help_date.format(new java.util.Date())%>"   />
                      </TD>
                    </TR>
                    <TR>
                      <TD height="15" colspan="3" valign="top"></TD>
                      <TD height="15" colspan="6">&nbsp;</TD>
                    </TR>
                    <TR>
                      <TD height="10" colspan="3" valign="top"></TD>
                      <TD height="10" colspan="5"></TD>
                    </TR>
                  </table>
                  <html:submit><%=submitCaption %></html:submit> </html:form><br>


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
