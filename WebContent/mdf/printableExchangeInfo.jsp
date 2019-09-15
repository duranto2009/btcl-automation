<%@page import="shifting.ShiftClientService"%>
<%@page import="shifting.ShiftClientDTO"%>
<%@page import="mdf.MDFDTO"%>
<%@page import="mdf.MDFService"%>
<%@page import="packages.PackageRepository"%>
<%@page import="regiontype.RegionService"%>
<%@ include file="../includes/checkLogin.jsp" %>

<%@page errorPage="../common/failure.jsp" %>
<%@page import = "client.*" %>
<%@page import = "java.util.*" %>
<%@page import = "java.text.*" %>
<%@page import = "client.*" %>
<%@ page import="java.util.ArrayList,
         java.util.*,
         java.util.Date,java.text.*,
         sessionmanager.SessionConstants,
         help.*,java.sql.*,databasemanager.*,java.lang.Integer,java.lang.*,user.*,dslm.*,exchange.*" %>


<%-- <%@ page errorPage="../common/failure.jsp" %> --%>

<%
    String accountID = request.getParameter("id");
    /*==== Parameters needed to be configured  =====================================*/

    String windowTitle = "Information Details";
    

%>
<html>
    <head>
        <title><%=windowTitle%></title>
        <script language="JavaScript">
            function printPage() {
                if(document.all) {
                    document.all.divButtons.style.visibility = 'hidden';
                    window.print();
                    document.all.divButtons.style.visibility = 'visible';
                } else {
                    document.getElementById('divButtons').style.visibility = 'hidden';
                    window.print();
                    document.getElementById('divButtons').style.visibility = 'visible';
                }
            }
        </script>

        <%
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
           
            MDFService fservice = new MDFService();
            ArrayList<MDFDTO> data1 = (ArrayList<MDFDTO>) fservice.getToBeConnected(loginDTO);
            ArrayList<MDFDTO> data2 = (ArrayList<MDFDTO>) fservice.getToBeDisconnected(loginDTO);

        %>
    </head>

    <body>
    <center>

        <table id="pageTable" height="600" border="0" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0" width="410">
            <tr>
                <td valign="top" width="410">
                    <table style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0" width="100%">
                        <!-- Start of Page Header -->
                        <tr><td>
                            </td></tr>
                        <!-- End of Page Header -->




                        <!-- Start of Report Header -->
                        <tr><td>
                                <table border="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%">
                                    <tr>
                                        <td width="100%" style="padding:0; font-family: Arial (fantasy); font-size: 2pt; font-style: italic; font-weight: bold"> 
                                            <p align="center"> <font size="3" face="Courier New, Courier, mono">Bangladesh Telecommunication Company Limited</font><br/> <font size="2" face="Courier New, Courier, mono"><em>ADSL Connection Disconnection Information</em></font> </p>

                                        </td>
                                    </tr>
                                </table>
                            </td></tr>
                        <!-- End of Report Header -->
                        <tr>
                            <td>&nbsp;</td>
                        </tr>


                        <!-- Start of Data Header -->
                        <tr><td>
                                <table  style="font-family: Arial; font-size: 10pt; color: #000000; border-collapse:collapse" cellpadding="3" cellspacing="0" border="1" width="409" bordercolor="#000000" >
                                    <tr>
                                        <td width="275" height="26" align="left" valign="top" ><font size="2" face="Courier New, Courier, mono">ADSL Phone No</font></td>
                                        <td width="275" height="26" align="left" valign="top" ><div align="left"><font size="2" face="Courier New, Courier, mono">Customer Name</font></div></td>
                                        <td width="275" height="26" align="left" valign="top" >Address </font></td>
                                        <td width="275" height="26" align="left" valign="top" >Port No </font></td>
                                       <td width="275" height="26" align="left" valign="top" >Exchange Name </font></td> 
                                        <td width="275" height="26" align="left" valign="top" >DSLM Name </font></td>
                                        <td width="275" height="26" align="left" valign="top" >Job Type </font></td>
                                        <td width="275" height="26" align="left" valign="top" >Assignment Time </font></td>
                                    </tr>
                                    <% for (int i = 0; i < data1.size(); i++) {%>

                                    <tr>


                                        <td width="275" height="26" align="left" valign="top" ><font size="2" face="Courier New, Courier, mono"><%=ClientRepository.getInstance().getClient(data1.get(i).getClientID()).getPhoneNo()%></font></font></td>
                                        <td width="275" height="26" align="left" valign="top"> <font size="2" face="Courier New, Courier, mono"><%=ClientRepository.getInstance().getClient(data1.get(i).getClientID()).getCustomerName()%></div></font></font></td>
                                        <td width="275" height="26" align="left" valign="top" ><font size="2" face="Courier New, Courier, mono"></font><font face="Courier New, Courier, mono"><%=ClientRepository.getInstance().getClient(data1.get(i).getClientID()).getCustomerAddress()%></font></td>
                                        <td width="275" height="26" align="left" valign="top" ><font size="2" face="Courier New, Courier, mono"></font><font face="Courier New, Courier, mono"><%=data1.get(i).getPort()%></font></td>   
                                        <td width="275" height="26" align="left" valign="top" ><font size="2" face="Courier New, Courier, mono"></font><font face="Courier New, Courier, mono"><%=ExchangeRepository.getInstance().getExchange(DslmRepository.getInstance().getDslm(data1.get(i).getDslmID()).getDslmExchangeNo()).getExName()%></font></td>                                   
                                        <td width="275" height="26" align="left" valign="top" ><font size="2" face="Courier New, Courier, mono"></font><font face="Courier New, Courier, mono"><%=DslmRepository.getInstance().getDslm(data1.get(i).getDslmID()).getDslmName()%></font></td>                                  
                                        <td width="275" height="26" align="left" valign="top" ><font size="2" face="Courier New, Courier, mono"></font><font face="Courier New, Courier, mono"><%=MDFDTO.JOB[data1.get(i).getJobType()-1]%></font></td>
                                        <td width="275" height="26" align="left" valign="top" ><font size="2" face="Courier New, Courier, mono"></font><font face="Courier New, Courier, mono"><%=formatter.format(new Date(data1.get(i).getEntryTime()))%></font></td>                                    
                                    </tr>
                                    <%}%>
                                    
                                    
                                     <% for (int i = 0; i < data2.size(); i++) {%>

                                    <tr>
                                    <%if(data2.get(i).getRefID()!= -1) 
                                        {
                                        	ShiftClientDTO sdto = (new ShiftClientService()).getShiftClientDetail(""+data2.get(i).getRefID());
                                       %>
                                       
                                            <td class="td_viewdata1" align="center" width="110" height="15"><%=sdto.getOldTelephone()%> </td>
                                            <td class="td_viewdata1" align="center" width="170" height="15"><%=ClientRepository.getInstance().getClient(data2.get(i).getClientID()).getCustomerName()%> </td>
                                            <td class="td_viewdata1" align="center" width="170" height="15"><%=sdto.getOldAddress()%> </td>
                                       
                                       
                                       
                                       <%}else{ %>
                                        
                                            <td class="td_viewdata1" align="center" width="110" height="15"><%=ClientRepository.getInstance().getClient(data2.get(i).getClientID()).getPhoneNo()%> </td>
                                            <td class="td_viewdata1" align="center" width="170" height="15"><%=ClientRepository.getInstance().getClient(data2.get(i).getClientID()).getCustomerName()%> </td>
                                            <td class="td_viewdata1" align="center" width="170" height="15"><%=ClientRepository.getInstance().getClient(data2.get(i).getClientID()).getCustomerAddress()%> </td>
                                        <%}%>
                                         <td width="275" height="26" align="left" valign="top" ><font size="2" face="Courier New, Courier, mono"></font><font face="Courier New, Courier, mono"><%=data2.get(i).getPort()%></font></td>                         
                                          
                                        <td width="275" height="26" align="left" valign="top" ><font size="2" face="Courier New, Courier, mono"></font><font face="Courier New, Courier, mono"><%=ExchangeRepository.getInstance().getExchange(DslmRepository.getInstance().getDslm(data2.get(i).getDslmID()).getDslmExchangeNo()).getExName()%></font></td>                                   
                           
                                        <td width="275" height="26" align="left" valign="top" ><font size="2" face="Courier New, Courier, mono"></font><font face="Courier New, Courier, mono"><%=DslmRepository.getInstance().getDslm(data2.get(i).getDslmID()).getDslmName()%></font></td>                                  
                                        <td width="275" height="26" align="left" valign="top" ><font size="2" face="Courier New, Courier, mono"></font><font face="Courier New, Courier, mono"><%=MDFDTO.JOB[data2.get(i).getJobType()-1]%></font></td>
                                        <td width="275" height="26" align="left" valign="top" ><font size="2" face="Courier New, Courier, mono"></font><font face="Courier New, Courier, mono"><%=formatter.format(new Date(data2.get(i).getEntryTime()))%></font></td>                                    
                                    </tr>
                                    <%}%>

                                </table>
                            </td></tr>
                        <!-- End of Data -->
                    </table></td>
            </tr>

            <tr>
                <td width="410"> <p>&nbsp;</p>
                    <p><font face="Courier New, Courier, mono"><br>
                        <!-- Start of Page Footer -->
                        </font> </p>
            <center>
                <table border="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%">
                    <tr>
                        <td align="left" colspan ="2" style="padding:0; font-family: Arial; font-size: 10pt; "><font face="Courier New, Courier, mono">Print 
                            Date:<%=new java.util.Date()%> </font></td>
                            
                            
                        <td align="right" colspan ="4"; font-family: Arial; font-size: 10pt; "><font face="Courier New, Courier, mono"> 
                            ............</font> </td>
                    </tr>
                   
                    <tr>
                    <td align="left" colspan = "2" style="padding:0; font-family: Arial; font-size: 10pt; "><font face="Courier New, Courier, mono">
                            </font></td>
                    <td align="right" style="padding-left: 100;" colspan="4"; font-family: Arial; font-size: 10pt; "><font face="Courier New, Courier, mono"> 
                            <div align ="right"> Authorized Signature</font></div> </td>
                    </tr>
                    </table>
                    
                     <tr> 
                        <td align="center" width = 300 px; style="padding:0; colspan ="2" font-family: Arial; font-size: 10pt; "><font face="Courier New, Courier, mono"> 
                            <div align = "center"> Copyright© REVE Systems. All rights reserved </div></font> </td>
                            
                            
                    </tr>
                    
                    <tr>
                        <td width="100%" style="padding:0; font-family: Arial (fantasy); font-size: 2pt; font-style: italic; font-weight: bold"> 
                            <p align="center"> <font size="2" face="Courier New, Courier, mono">


                            <div id="divButtons" name="divButtons" align = "center">
                                <input type="button" value = "Print" onclick="printPage()" >
                            </div>






                            </font> </p>

                        </td>
                    </tr>

                </table>
            </center>
            <font face="Courier New, Courier, mono">
            <!-- End of Page Footer -->
            </font></td>
            </tr>
        </table><!-- end of page table -->


        <!--Summery/Running totals -->

    </table>


</center>
</body>
</html>
