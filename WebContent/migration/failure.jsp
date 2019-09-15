<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../includes/checkLogin.jsp" %>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="sessionmanager.SessionConstants" %>
<%@ page isErrorPage="true" %>

<%
String message = "";
HttpSession sess  = request.getSession(true);
message = (String)sess.getValue(SessionConstants.FAILURE_MESSAGE);

if(message == null)
{
  message = "";
  if(exception != null)
  message = exception.getMessage();
}

if(message == null)message = "";

%>

<html>
  <head>
    <html:base/>
    <title>Failure</title>
    <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
      <script language="JavaScript" src="../scripts/util.js"></script>

      <script language="JavaScript">
      </script>

    </head>

    <body class="body_center_align">


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
                <!--main -->
                <td width="100%" valign="top" class="td_main"class="td_main" align="center">

                  <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber3">
                    <tr>
                      <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
                        <div class="div_title" style="color:red;">Failure!!</div>
                      </td>
                    </tr>
                    <tr>
                      <td width="100%" align="center" height="200">
                        <br />
                        <br />
                        <div class="div_error_message">
                          <b>Failure Message</b>
                          <br />
                          <br />
                          <%=message %>
                        </div>
                        <br />
                        <br />
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
