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

<body class="body_center_align" >


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
						String subject = (String)request.getSession(true).getAttribute("subject");
						String perticularID = (String)request.getSession(true).getAttribute("perticularID");
					%>
                    <TR>
                      <TD height="22"><strong>Token Code</strong></TD>
                      <TD height="22"><strong>:</strong></TD>
                      <TD height="22" width="368"><%=token%> <input name="helpID" type ="hidden" value="<%=perticularID %>" readonly="true" />
                      </TD>
                    </TR>
                    <TR>
                      <TD height="22" width="109"><strong>Subject</strong></TD>
                      <TD width="9"><strong>:</strong></TD>
                      <TD width="368"><%=subject%></TD>
                      <TD width="2" height="22" colspan="5">&nbsp; </TD>
                    </TR>
                    <TR>
                      <TD height="22" colspan="2"></TD>
                      <TD height="22" width="368"></TD>
                    </TR>
                    <tr>
                      <td colspan="3"> <table >
                          <tr>
                            <td class="td_viewheader"  align="left"width="73"><div align="center">Date</div></td>
                            <td class="td_viewheader"  align="left"width="73"><div align="center">Com/Rep</div></td>
                            <td  align="left" class="td_viewheader"><div align="center">Messege</div></td>
                            <td  align="left" class="td_viewheader"><div align="center">Reply</div></td>							
                          </tr>
                          <%
                         	 String prevTokenCode = "";
						    for( int i=0 ; i < helpList.size();i++)
						    {
						      HelpDTO dto = (HelpDTO) helpList.get(i);
						     
							  String tokenCode = dto.m_helpID;
							 
							  

							  SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
							  String date = formatter.format(new java.util.Date(dto.getRequestTime()));
							  String date2 = formatter.format(new java.util.Date(dto.getReplyTime()));
							  if(dto.m_replyDesc==null)
							  {
							  	dto.m_replyDesc=" ";
							  }
							  
							  if(!tokenCode.equalsIgnoreCase(prevTokenCode))
							  {
								  prevTokenCode = tokenCode;
								 // out.println("code:"+tokenCode);
							 

							  %>
							  
							    <tr>
                            <td class="td_viewdata1"  align="left" colspan = "3" height="10"></td>
                            
                          </tr>
                          <tr>
                            <td class="td_viewdata2"  align="left"width="73"><%=date%></td>
                            <td class="td_viewdata2"  align="left"width="73">Complain</td>
                            <td  align="left" class="td_viewdata2"><%=dto.m_helpDesc%></td>
                            <%
								String status = (String)request.getSession(true).getAttribute("status");
								int st=Integer.valueOf(status).intValue();
								 if(st==1)
								{	%>
                            <td align="left"width="66"><div align="center"><a href="../GetReplyPerticular.do?id=<%=dto.m_helpID%>" ><font size="-2">Reply</font></a></div></td>
                            <%}%></tr><%}%>
                          
                          <tr>
                            <td class="td_viewdata1"  align="left"width="73"><%=date2%></td>
                            <td class="td_viewdata1"  align="left"width="73">Reply
                              <input name="helpID2" type ="hidden" value="<%=dto.m_helpID %>" /></td>
                            <td class="td_viewdata1"  align="left"width="333"><%=dto.m_replyDesc%></td>
                          </tr>
                          <%  }
						  %>
                        </table></td>
                    </tr>
                    <TR>
                      <TD height="20" colspan="2"></TD>
                      <TD height="20" width="368"></TD>
                    </TR>
                    <TR>
                      <TD height="35" colspan="3" valign="top"> <input name="userID" type ="hidden"   value="<%=loginDTO.getUserID()%>"/></TD>
                    <TR>
                      <TD height="22" colspan="2" valign="top">&nbsp;</TD>
                      <TD height="22" colspan="2"></textarea></TD>
                    </TR>
                    <TR>
                      <TD height="22" colspan="2" valign="top"> </TD>
                      <TD height="22" colspan="2"><input name="replyTime" type ="hidden" value="<%=help_date.format(new java.util.Date())%>"   />
                      </TD>
                    </TR>
                    <TR>
                      <TD height="10" colspan="2" valign="top"></TD>
                      <TD height="10"></TD>
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

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/footer.jsp"  %>
    </td>
  </tr>

</table>
</body>
</html>
