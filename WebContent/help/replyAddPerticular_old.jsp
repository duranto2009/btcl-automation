<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../includes/checkLogin.jsp" %>
<%java.text.SimpleDateFormat help_date = new java.text.SimpleDateFormat ("yyyy-MM-dd"); %>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page errorPage="../common/failure.jsp" %>

<%@ page import="java.util.ArrayList,java.util.*,
				  java.util.Date,java.text.*,
 				 sessionmanager.SessionConstants,
				 help.*,java.sql.*,databasemanager.*" %>

<%
 String title = "Reply Form ";
 String submitCaption = "Send Reply";
 String actionName = "/AddHelpSolution";

%>
<%
    ArrayList helpList = (ArrayList) session.getAttribute("HelpList");
%>
<%
     String help_id=null;
     String sql = "select hshrdHelpID from adhelpsolution";

  java.util.HashMap replyMap = new java.util.HashMap();
      Connection connection = null;
      Statement stmt = null;
      connection = DatabaseManager.getInstance().getConnection();
      stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next())
      {
         help_id=rs.getString("hshrdHelpID");
         replyMap.put(help_id,help_id);
      }
      rs.close();
      stmt.close();
      DatabaseManager.getInstance().freeConnection(connection);


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

<body class="body_center_align" onload="document.forms[0].replyDesc.focus();">


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
          <tr>

            <td width="100%" align="center">

<br><br>
                  <!-- start of the form  -->
                  <html:form  action="<%=actionName %>" method="POST" onsubmit="return validate();">
                  <table  width="490" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                    <%
						String token = (String)request.getSession(true).getAttribute("id");
						String perticularID = (String)request.getSession(true).getAttribute("perticularID");
						
						String complain=null;							
						 Connection connection1 = DatabaseManager.getInstance().getConnection();
						 Statement stmt1 = connection1.createStatement();
						 ResultSet resultSet1 = stmt1.executeQuery("select hrdHelpDesc from adhelprequestdetail where hrdHelpID=" + perticularID);
						 while(resultSet1.next())
						 {
						   complain=resultSet1.getString("hrdHelpDesc");
						 }
						 resultSet1.close();
						 stmt1.close();
						 DatabaseManager.getInstance().freeConnection(connection1);

					%>

					<TR>
                      <td height="22" width="130">Token Code</td>
                      <td height="22" width="347"><input name="tokenCode" type ="hidden" value="<%=token %>" /> 
                        <input name="helpID" type ="hidden" value="<%=perticularID %>"  /><%=token %>
					  
                      </td>
                    </TR>
					<TR>
                      <TD height="22" width="130">Complain description</TD>
                      <td height="22" width="347"><input  name="complain"  type ="hidden" value="<%=complain %>" /><%=complain %></td>
					  
                    </TR>
                    <tr>
                      <td colspan="2"> <table >
                          <%
						    for( int i=0 ; i < helpList.size();i++)
						    {
						      HelpDTO dto = (HelpDTO) helpList.get(i);
						
							  SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
							  String date = formatter.format(new java.util.Date(dto.getRequestTime()));
							  
							 					 
							  %>
							  
                          <tr> 
                            <td class="td_viewdata1"  align="left"width="73">Date:<%=date%> 
                              <input name="helpID2" type ="hidden" value="<%=dto.m_helpID %>" /> 
                            </td>
                          </tr>
                          <tr> 
                            <td class="td_viewdata2"  align="left"width="73">Complain</td>
                            <td  align="left" class="td_viewdata2"><%=dto.m_helpDesc%></td>
                            <td  align="left" >&nbsp;</td>
                          </tr>
                          <tr> 
                            <td class="td_viewdata2"  align="left"width="73">Reply</td>
                            <td class="td_viewdata2"  align="left"width="333"><%=dto.m_replyDesc%></td>
                            <%if(replyMap.get(dto.m_helpID) == null)
							  {
							  %>
                            <td align="left"width="66"><a href="../GetReplyPerticular.do?id=<%=dto.m_helpID%>" ><font size="-2">Send 
                              Reply</font></a></td>
                            <%}%>
                          </tr>
                          <tr> 
                            <td   align="left"width="73"></td>
                            <td colspan="2"   align="left"></td>
                          </tr>
                          <%  }

%>
                        </table></td>
                    </tr>
                    <TR>
                      <TD height="20" width="130"></TD>
                      <TD height="20" width="347"></TD>
                    </TR>
                    <TR>
                      <TD height="35" colspan="2" valign="top"><strong>Reply the
                        complain :</strong>
                        <input name="userID" type ="hidden"   value="<%=loginDTO.getUserID()%>"/></TD>




                    <TR>
                      <TD valign="top" height="22">Reply Message</TD>
                      <TD height="22" colspan="2"><textarea cols="50" rows="6"  name="replyDesc" /></textarea></TD>
                    </TR>
                    <TR>
                      <TD valign="top" height="22" width="130"> </TD>
                      <TD height="22" colspan="2"><input name="replyTime" type ="hidden" value="<%=help_date.format(new java.util.Date())%>"   />
                      </TD>
                    </TR>

                    <TR>
                      <TD valign="top" height="10"></TD>
                      <TD height="10"></TD>
                    </TR>
                  </table>
                  <html:submit>Send Reply</html:submit> </html:form><br>


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
