<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="login.PermissionConstants"%>
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,
         sessionmanager.SessionConstants,
         regiontype.*" %>
<%@ page errorPage="failure.jsp" %>
<html>
    <head>
        <title>Region</title>
        <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
        <script language="JavaScript" src="../scripts/util.js"></script>

        <script language="JavaScript">
        </script>

    </head>

    <body class="body_center_align" >


        <table border="0" cellpadding="0" cellspacing="0"  width="780" id="AutoNumber1">

            <!--header-->
            <tr>
                <td width="100%">
                    <%@ include file="../includes/header.jsp"  %>
                </td>
            </tr>

            <!--center-->
            <tr>
                <td width="100%">
                    <table border="0" cellpadding="0" cellspacing="0"  width="780" id="AutoNumber2">
                        <tr>
                            <!--left menu-->
                           
                            <!--main -->
                            <td width="600" valign="top" class="td_main" align="center">

                                <table border="0" cellpadding="0" cellspacing="0" width="100%" id="AutoNumber3">
                                    <tr>
                                        <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
                                            <div class="div_title">Region</div>
                                        </td>
                                    </tr>
                                    <%
                                        String msg = null;
                                        RegionService rService = new RegionService();
                                        if ((msg = (String) session.getAttribute(util.ConformationMessage.REGION_EDIT)) != null) {
                                            session.removeAttribute(util.ConformationMessage.REGION_EDIT);
                                    %>
                                    <tr>
                                        <td width="100%" align="center" valign="top" height="25"><b><%=msg%></b></td>
                                    </tr>
                                    <%}%>


                                    <tr>
                                        <td width="100%" align="center">
                                            <br>
                                            <!-- search and navigation control-->
                                            <%
                                                String url = "ViewRegion";
                                                String navigator = SessionConstants.NAV_REGION;
                                            %>
                                            <jsp:include page="../includes/nav.jsp" flush="true">
                                                <jsp:param name="url" value="<%=url%>"/>
                                                <jsp:param name="navigator" value="<%=navigator%>" />
                                            </jsp:include>
                                            <!-- search and navigation control -->


                                            <!-- start of the form -->
                                            <html:form action="/DropRegion" method="POST" >

                                                <table  class="table_view"  width="550">
                                                    <tr>
                                                        <td class="td_viewheader"  align="center" width="217"  >Region 
                                                            Name</td>
                                                        <td class="td_viewheader"  align="center" width="159"  >Region 
                                                            Code</td>
                                                        <td class="td_viewheader"  align="center" width="96"  >Edit</td>
                                                        <%if(loginDTO.getColumnPermission(PermissionConstants.COLUMN_CHANGE_PARENT_REGION)){%>
                                                        <td class="td_viewheader"  align="center" width="96"  >Change Parent Code</td>
                                                        <%}%>
                                                        <td class="td_viewheader"  align="center" width="58"  >
                                                            <input type="submit"  value="Delete" >
                                                        </td>

                                                    </tr>

                                                    <%

                                                        ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_REGION);

                                                        if (data != null) {
                                                            int size = data.size();

                                                            for (int i = 0; i < size; i++) {

                                                                RegionDTO row = (RegionDTO) data.get(i);

                                                    %>
                                                    <tr>
                                                        <td class="td_viewdata1"  align="center" width="217" ><%=row.getRegionName()%>&nbsp;</td>
                                                        <td class="td_viewdata2 " align="center" width="159" ><%=row.getRegionDesc()%>&nbsp;</td>

                                                        <%if (rService.hasExchange(row.getRegionDesc()) || row.getStatus() == RegionConstants.REGION_STATUS_DELETED) {%>
                                                        <td class="td_viewdata1"  align="center" width="96" ><a href="../GetRegion.do?id=<%=row.getRegionID()%>" >Edit</a></td>
                                                        <%} else {%>
                                                        <td class="td_viewdata1"  align="center" width="96" ><a href="../GetRegion.do?id=<%=row.getRegionID()%>" >Edit</a></td>
                                                        <%}%>
                                                        
                                                        <%if(loginDTO.getColumnPermission(PermissionConstants.COLUMN_CHANGE_PARENT_REGION)){%>
                                                        <%if ( row.getStatus() == RegionConstants.REGION_STATUS_DELETED) {%>
                                                        <td class="td_viewdata2"  align="center" width="96" >Change</td>
                                                        <%} else {%>
                                                        <td class="td_viewdata2"  align="center" width="96" ><a href="../ParentRegionChange.do?id=<%=row.getRegionID()%>" >Change</a></td>
                                                        <%}%>
                                                        <%}%>
                                                        <%if (rService.hasExchange(row.getRegionDesc()) || row.getStatus() == RegionConstants.REGION_STATUS_DELETED) {%>
                                                        <td class="td_viewdata1"  align="center" width="58" >
                                                            <input type="checkbox" name="deleteIDs" value="<%=row.getRegionID()%>" disabled = "true" >
                                                        </td>
                                                        <%} else {%>
                                                        <td class="td_viewdata1"  align="center" width="58" >
                                                            <input type="checkbox" name="deleteIDs" value="<%=row.getRegionID()%>"  >
                                                        </td>
                                                        <%}%>
                                                    </tr>
                                                    <%
                                                            }
                                                        }
                                                    %>

                                                </table>
                                            </html:form>

                                            <!-- end of the form -->
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
                    <%@ include file="../includes/footer.jsp"  %></td></tr></table></body></html>
