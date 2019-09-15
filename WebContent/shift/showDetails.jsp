<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="common.repository.AllClientRepository"%>
<%@page errorPage="../common/failure.jsp" %>
<%@page import="regiontype.RegionRepository"%>
<%@page import="exchange.ExchangeRepository"%>
<%@page import="dslm.DslmRepository"%>
<%@page import="util.TimeFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="shifting.form.ShiftClientForm"%>
<%@ include file="../includes/checkLogin.jsp"%>
<%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*,sessionmanager.SessionConstants,java.io.*,packages.*"%>
<html>
    <head>
        <html:base />
        <title>Shift Client Detail</title>
        <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css" />
        <link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css"/>
        
        <script type="text/javascript" src="../scripts/util.js"></script>
        <script type="text/javascript">
            function back(){
                window.history.back();
            }
        </script>

    </head>

    <body class="body_center_align"
          onload="init();">
        &nbsp;
        <table border="0" cellpadding="0" cellspacing="0" width="780">
            <tr>
                <td width="100%"><%@ include file="../includes/header.jsp"%></td>
            </tr>
            <tr>
                <td width="100%">
                    <table border="0" cellpadding="0" cellspacing="0" width="780">
                        <tr>
                            
                            <td width="780" valign="top" class="td_main" align="center">

                                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                    <tr>
                                        <td width="100%" align="right"
                                            style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
                                            <div class="div_title">Shift Client Detail</div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="100%" align="center" style="padding-left:210px"><br /> 
                                            <!--<html:form action="/shiftClient" method="POST" onsubmit="return validate(this);">-->
                                                <table border="0" cellpadding="1" class="form1" width="534">
                                                    <%
                                                        ShiftClientForm form = (ShiftClientForm) session.getAttribute("shifting_details");
                                                    %>
                                                    <tr>
                                                        <td width="150" style="padding-right: 0" align="left">Client Id</td>
                                                        <td width="430" height="22" align="left"> : <%=AllClientRepository.getInstance().getClientByClientID(form.getClientID()).getUserName()%></td>
                                                    </tr>

                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Old Telephone</td>
                                                        <td width="430" height="22" align="left"> : <%=form.getOldTelephone()%></td>
                                                    </tr>

                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">New Telephone</td>
                                                        <td width="430" height="22" align="left"> : <%=form.getNewTelephone()%></td>
                                                    </tr>

                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Old DSLM</td>
                                                        <td width="430" height="22" align="left"> : <%=DslmRepository.getInstance().getDslm(form.getOldDslm()).getDslmName()%> </td>
                                                    </tr>

                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">New DSLM</td>
                                                        <td width="430" height="22" align="left"> : <%=DslmRepository.getInstance().getDslm(form.getNewDslm()).getDslmName()%> </td>
                                                    </tr>

                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Old Port</td>
                                                        <td width="430" height="22" align="left"> : <%=form.getOldPort()%> </td>
                                                    </tr>

                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">New Port</td>
                                                        <td width="430" height="22" align="left"> : <%=form.getNewPort()%> </td>
                                                    </tr>


                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Previously Bound to Port</td>
                                                        <td width="430" height="22" align="left"> : <%=form.isOldBindToPort() ? "Yes" : "No"%> </td>
                                                    </tr>

                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Bind to Port</td>
                                                        <td width="430" height="22" align="left"> : <%=form.isBindToPort() ? "Yes" : "No"%> </td>
                                                    </tr>

                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Old Exchange</td>
                                                        <td width="430" height="22" align="left"> : <%=ExchangeRepository.getInstance().getExchange(DslmRepository.getInstance().getDslm(form.getOldDslm()).getDslmExchangeNo()).getExName()%> </td>
                                                    </tr>

                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">New Exchange</td>
                                                        <td width="430" height="22" align="left"> : <%=ExchangeRepository.getInstance().getExchange(DslmRepository.getInstance().getDslm(form.getNewDslm()).getDslmExchangeNo()).getExName()%> </td>
                                                    </tr>



                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Old Area Code</td>
                                                        <td width="430" height="22" align="left"> : <%=RegionRepository.getInstance().getRegionID(form.getOldAreaCode()).getRegionName()%> </td>
                                                    </tr>

                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">New Area Code</td>
                                                        <td width="430" height="22" align="left"> : <%=RegionRepository.getInstance().getRegionID(form.getAreaCode()).getRegionName()%> </td>
                                                    </tr>









                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Old Address</td>
                                                        <td width="430" height="22" align="left"> : <%=form.getOldAddress()%> </td>
                                                    </tr>

                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">New Address</td>
                                                        <td width="430" height="22" align="left"> : <%=form.getNewAddress()%> </td>
                                                    </tr>




                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Invoice No</td>
                                                        <td width="430" height="22" align="left"> : <%=form.getInvoiceNo()%> </td>
                                                    </tr>


                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Shifting Charge</td>
                                                        <td width="430" height="22" align="left"> : <%=form.getCost()%> </td>
                                                    </tr>



                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Shifting Date</td>
                                                        <td width="430" height="22" align="left"> : <%=TimeFormat.getSpecificDate(form.getShiftingDate())%> </td>
                                                    </tr>

                                                    <tr>
                                                        <td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">Description</td>
                                                        <td width="430" height="22" align="left"> : <%=form.getDescription()%> </td>
                                                    </tr>

                                                </table>
                                                <table border="0" cellpadding="0" cellspacing="0" width="89%" class="form1">

                                                    <tr>
                                                        <td width="100%" height="31" align="left"><br></td>
                                                    </tr>
                                                    <tr>
                                                        <td width="100%" align="center" style="padding-right:220px">
                                                            <input type="button" value="Back" onclick="back();">
                                                            <html:form method="POST" action="/Download">
                                                                <input type="submit" name="downloadShiftingDetails" value="Download">
                                                            </html:form>
                                                        </td>
                                                        <td  align="left">
                                                            
                                                        </td>
                                                    </tr>
                                                </table>
                                                <br>
                                                <br>
                                                <!--</html:form>--> 
                                                <br></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td width="100%"><%@ include file="../includes/footer.jsp"%></td>
            </tr>
        </table>
    </body>
</html>
