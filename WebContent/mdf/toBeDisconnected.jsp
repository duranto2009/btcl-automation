<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="shifting.ShiftClientDTO"%>
<%@page import="shifting.ShiftClientService"%>
<%@page import="dslm.DslmRepository"%>
<%@page import="user.UserRepository"%>
<%@page import="exchange.ExchangeRepository"%>
<%@page import="mdf.MDFService"%>
<%@page import="login.PermissionConstants"%>
<%@include file="../includes/checkLogin.jsp"%>
<%@page language="java"%>
<%@taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="java.util.ArrayList,sessionmanager.SessionConstants,client.*,mdf.*"%>
<%@page errorPage="../common/failure.jsp" %>
<html>
    <head>
        <html:base/>
        <title>Clients</title>
        <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
        <script language="JavaScript" src="../scripts/util.js"></script>
        <script language="JavaScript">


        </script>
    </head>
    <body class="body_center_align" >
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
                            
                           <td width="600" valign="top" class="td_main" align="center">
                            

        <table border="0" cellpadding="0" cellspacing="0" width="100%" id="AutoNumber3">
          <tr>
            <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
            <div class="div_title">Exchange Information</div>
            </td>
          </tr>
          
          <%int permission = loginDTO.getMenuPermission(login.PermissionConstants.MDF); %> 
                                <html:form action="/DisconnectClient" method="POST">
                                    <table width="595" class="table_view">
                                       
                                        <tr>
                                            <td class="td_viewheader" align="center" valign="top" width="110" height="25">
                                                <!--Customer ID-->
                                                ADSL Phone No</td>
                                            <td class="td_viewheader" align="center" valign="top" width="170" height="25">
                                                <!--Name-->
                                                Customer Name</td>
                                            <td class="td_viewheader" align="center" valign="top" width="170" height="25">
                                                <!--Name-->
                                                Address</td>
                                            <td class="td_viewheader" align="center" valign="top" width="110" height="25">
                                                <!--Name-->
                                                Port No</td>
                                                
                                                <td class="td_viewheader" align="center" valign="top" width="110" height="25">
                                                <!--Payment Type-->
                                               Exchange Name</td>
                                            <td class="td_viewheader" align="center" valign="top" width="110" height="25">
                                                <!--Payment Type-->
                                                DSLM Name</td>
                                                
                                             <td class="td_viewheader" align="center" valign="top" width="110" height="25">
                                                <!--Payment Type-->
                                                Job Type</td>
                                                
                                                <%if(permission==3){ %>
                                            <td class="td_viewheader" align="center" valign="top" width="70" height="25">
                                                <input type="submit" name="submitType" value="Disconnect">
                                            </td>
                                            <%} %>

                                        </tr>
                                        <%
                                            MDFService fservice = new MDFService();
                                            ArrayList<MDFDTO> data = (ArrayList<MDFDTO>) fservice.getToBeDisconnected(loginDTO);
                                            if (data != null) {
                                                int size = data.size();
                                                System.out.println("size:" + size);

                                                for (int i = 0; i < size; i++) {
                                                    MDFDTO row = (MDFDTO) data.get(i);
                                        %>
                                        <tr>
                                        
                                        <%if(row.getRefID()!= -1) 
                                        {
                                        	ShiftClientDTO sdto = (new ShiftClientService()).getShiftClientDetail(""+row.getRefID());
                                       %>
                                       
                                            <td class="td_viewdata1" align="center" width="110" height="15"><%=sdto.getOldTelephone()%> </td>
                                            <td class="td_viewdata1" align="center" width="170" height="15"><%=ClientRepository.getInstance().getClient(row.getClientID()).getCustomerName()%> </td>
                                            <td class="td_viewdata1" align="center" width="170" height="15"><%=sdto.getOldAddress()%> </td>
                                       
                                       
                                       
                                       <%}else{ %>
                                        
                                            <td class="td_viewdata1" align="center" width="110" height="15"><%=ClientRepository.getInstance().getClient(row.getClientID()).getPhoneNo()%> </td>
                                            <td class="td_viewdata1" align="center" width="170" height="15"><%=ClientRepository.getInstance().getClient(row.getClientID()).getCustomerName()%> </td>
                                            <td class="td_viewdata1" align="center" width="170" height="15"><%=ClientRepository.getInstance().getClient(row.getClientID()).getCustomerAddress()%> </td>
                                        <%}%>
                                            <td class="td_viewdata2" align="center" width="110" height="15"><%=row.getPort()%> </td>
                                            
                                              <td class="td_viewdata1" align="center" width="110" height="15"><%=ExchangeRepository.getInstance().getExchange(DslmRepository.getInstance().getDslm(row.getDslmID()).getDslmExchangeNo()).getExName()%> </td>
                                            
                                            
                                            <td class="td_viewdata1" align="center" width="110" height="15"><%=DslmRepository.getInstance().getDslm(row.getDslmID()).getDslmName()%> </td>
                                           <td class="td_viewdata1" align="center" width="110" height="15"><%=MDFDTO.JOB[row.getJobType()-1]%> </td>
                                            <%if(permission==3){ %>
                                            <td class="td_viewdata1" align="center" width="55" height="15">
                                                <input type="checkbox" name="deletedIDs" id="<%="deleted" + row.getMdfID()%>"  value="<%=row.getMdfID()%>" >
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
