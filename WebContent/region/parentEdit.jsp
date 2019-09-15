<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%-- 
    Document   : parentEdit
    Created on : Nov 27, 2012, 8:50:31 AM
    Author     : Dhiman
--%>

<%@page import="regiontype.form.RegionForm"%>
<%@page import="regiontype.RegionConstants"%>

<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page errorPage="failure.jsp" %>

<%@ page import="java.util.ArrayList,
         sessionmanager.SessionConstants,
         java.sql.*,databasemanager.*,regiontype.*" %>

<%
    String title = "Update Region";
    String submitCaption = "Update";
    String actionName = "/UpdateRegion";
    RegionService rService = new RegionService();
%>

<html>
    <head>
        <html:base/>
        <title><%=title%></title>
        <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
        <script language="JavaScript" src="../scripts/util.js"></script>

        <script language="JavaScript">
           
        </script>

    </head>

    <body class="body_center_align" >


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
                            <!--left menu-->
                            
                            <!--main -->
                            <td width="600" valign="top" class="td_main" align="center">

                                <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber3">
                                    <tr>
                                        <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
                                            <div class="div_title"><%=title%></div>
                                        </td>
                                    </tr>
                                    <tr>

                                        <td width="100%" align="center">

                                            <br><br>
                                            <!-- start of the form  -->

                                            <html:form  action="/ChangeParentRegion" method="POST" onsubmit="return validate();">
                                            <%
RegionForm rf= (RegionForm) request.getAttribute("regionForm");
long regionID = Long.parseLong(rf.getRegionID());
rf.setStatus(RegionRepository.getInstance().getRegionID(regionID,"t").getStatus());
rf.setParentCode((RegionRepository.getInstance().getRegionID(regionID,"t").getParentCode()));
rf.setPrefixCode((RegionRepository.getInstance().getRegionID(regionID,"t").getPrefixCode()));



%>
                                                <table  width="500" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                                                <html:hidden property="regionID" />

                                                    <TR>
                                                        <TD height="22" width="154" align = "left" style= "padding-left:80px">Region Name</TD>
                                                        <TD height="22" width="259" align = "left" style= "padding-left:80px"><bean:write name="regionForm" property="regionName"/>
                                                            <html:hidden property="regionID"  />
                                                            <html:hidden property="regionName"  />
                                                    </TR>

                                                    <TR>
                                                        <TD height="22" width="154" align = "left" style= "padding-left:80px">Code</TD>
                                                        <TD height="22" width="259" align = "left" style= "padding-left:80px"><html:hidden property = "regionDesc"/><bean:write name="regionForm" property="regionDesc" />
                                                        </TD>
                                                    </TR>
                                                    <TR>
                                                        <TD height="22" width="154" align = "left" style= "padding-left:80px">Parent Region Code</TD>
                                                        <TD height="22" width="259" align = "left" style= "padding-left:80px"><% 
                                                        out.print(rf.getParentCode()==-1?"None":rf.getParentCode()+"");%>
                                                        </TD>
                                                    </TR>
                                                    <TR> 
                                                        <TD valign="top" height="22" align = "left" style= "padding-left:80px">New Parent Code</TD>
                                                        <TD height="22" colspan="2" align = "left" style= "padding-left:80px">
                                                            <html:select property="parentCode">
                                                                <%
                                                                ArrayList <RegionDTO> rList = RegionRepository.getInstance().getRegionList();
                                                                for(int i=0;i<rList.size();i++){
                                                                    if(rList.get(i).getParentCode() == -1){
                                                                %>
                                                                <html:option value="<%=Long.toString(rList.get(i).getRegionDesc())%>"><%=rList.get(i).getRegionName()%></html:option>
                                                                <%  }
                                                                }%>
                                                            </html:select>
                                                        </TD>
                                                    </TR>
                                                    
                                                     <TR>
                                                        <TD height="22" width="154" align = "left" style= "padding-left:80px">Prefix</TD>
                                                        <TD height="22" width="259" align = "left" style= "padding-left:80px"><html:text property = "prefixCode"/><label style="color:red; vertical-align: middle;" >*</label></TD>
                                                            
                                                    </TR>
                                                    
                                                      <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="prefixCode"  /></TD>
                    </TR>


                                                    
                                                    <TR>
                                                        <TD valign="top" height="22" width="154">

                                                        </TD>
                                                        <TD height="22" width="259">
                                                        </TD>
                                                    </TR>

                                                </table>
                                                <html:reset>Reset</html:reset>

                                                <html:submit><%=submitCaption%></html:submit>


                                            </html:form>
                                            <!-- end of the form -->

                                            <br>

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
                    <%@ include file="../includes/footer.jsp"  %>
                </td>
            </tr>

        </table>
    </body>
</html>

