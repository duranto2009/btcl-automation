<%-- <%@page import="dslm.DslmRepository"%>
<%@page import="user.UserRepository"%>
<%@page import="dslm.ExchangeRepository"%>
<%@page import="mdf.MDFService"%>
<%@page import="login.PermissionConstants"%>
<%@include file="../includes/checkLogin.jsp"%>
<%@page language="java"%>
<%@taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="java.util.ArrayList,sessionmanager.SessionConstants,client.*,mdf.*"%>

<html>
<head>
<html:base/>
<title>Clients</title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>
<script language="JavaScript">


</script>
</head>
<body class="body_center_align">
<table border="0" cellpadding="0" cellspacing="0" width="780" id="AutoNumber1">
  <tr>
    <td width="100%">
      <%@include file="../includes/header.jsp"%>
    </td>
  </tr>
  <tr>
    <td width="100%">
      <table border="0" cellpadding="0" cellspacing="0" width="780" id="AutoNumber2">
        <tr>
          <td class="td_menu">
            
        			<%@ include file="../includes/menu.jsp"%>
        	
        	
            &nbsp;
          </td>
          <td width="600" valign="top" class="td_main" align="center">
            
                  <html:form action="/ConnectClient" method="POST">
                    <table width="595" class="table_view">
                      <tr><td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0" colspan ="4">
                      <div class="div_title"><%=ExchangeRepository.getInstance().getExchange(UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID()).getDslmExchangeNo()).exName %></div>
            </td></tr>
                      <tr>
                        <td class="td_viewheader" align="center" valign="top" width="110" height="25">
                          <!--Customer ID-->
						ADSL Phone No</td>
                        <td class="td_viewheader" align="center" valign="top" width="170" height="25">
                          <!--Name-->
						Port No</td>
                        <td class="td_viewheader" align="center" valign="top" width="70" height="25">
                          <!--Payment Type-->
                        DSLM Name</td>
                            
                         <td class="td_viewheader" align="center" valign="top" width="70" height="25"><input type="submit" name="submitType" value="Connect"></td>
                       
                                          
                         
                      </tr>
                    <%
                     MDFService fservice = new MDFService();
                      ArrayList<ClientDTO> data = (ArrayList<ClientDTO>)fservice.getIDs(loginDTO) ;
                      if (data != null) {
                        int size = data.size();
                        System.out.println("size:"+size);
                        
                        for (int i = 0; i < size; i++)
						{
                          ClientDTO row = (ClientDTO) data.get(i);
                    %>
                      <tr>
                        <td class="td_viewdata1" align="center" width="110" height="15"><%=row.getPhoneNo() %> </td>
                        <td class="td_viewdata2" align="center" width="170" height="15"><%=row.getPortNo() %> </td>
                         <td class="td_viewdata1" align="center" width="70" height="15"><%=DslmRepository.getInstance().getDslm(row.getDslmNo()).getDslmName()%> </td>
                       
                         <%if(row.getConnected() == 1){ %>
                        <td class="td_viewdata1" align="center" width="55" height="15">
                          <input type="checkbox" name="deletedIDs" id = "<%="deleted" + row.getUniqueID()%>"  value="<%=row.getUniqueID() %>" disabled = "true">
                        </td>
                        <%}else{ %>
                         <td class="td_viewdata1" align="center" width="55" height="15">
                          <input type="checkbox" name="deletedIDs" id = "<%="deleted" + row.getUniqueID()%>"  value="<%=row.getUniqueID() %>" >
                        </td>
                        <%} %>
                        
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
 --%>