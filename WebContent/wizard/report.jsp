
<%@ include file="../includes/checkLogin.jsp" %>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import = "report.*" %>
<%@page import = "java.util.*" %>

<%@page import = "client.*" %>
<%@ page import="
                  java.util.*,
				
 				 sessionmanager.SessionConstants,
				help.*,databasemanager.*,java.lang.*" %>


<%@ page errorPage="/common/failure.jsp" %>

<%

ReportData report = (ReportData)session.getAttribute("reportData");
String title = report.getTitle();

%>
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />


<title><%=title %></title>
</head>

<body>
<center>
      <%@ include file="../includes/header_report.jsp"  %>

<table id="pageTable" height="600" border="0" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0" width="900">
<tr>
<td valign="top" width="857">
<table style="border-collapse: collapse" bordercolor="#111111" border="0px" cellpadding="0" cellspacing="0" width="103%">
<!-- Start of Page Header -->
<tr><td>
<table border="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%">
  <tr>
    <td style="padding:0; font-family: Arial; font-size: 10pt; " align="left">
    </td>
    <td  style="padding:0; font-family: Arial; font-size: 10pt; " align="right">
    </td>
  </tr>
</table>
</td></tr>
<!-- End of Page Header -->

<tr>
 <td>&nbsp;</td>
</tr>



<!-- Start of Report Header -->
<tr><td>
<table border="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" align="center">
<%
              String msg = null;
              if ((msg = (String) session.getAttribute(SessionConstants.REPORT_CONFIGURATION_SAVED)) != null)
			    {
                session.removeAttribute(SessionConstants.REPORT_CONFIGURATION_SAVED);
            %>
              <tr>
                <td width="100%" align="center" valign="top" height="25">
                  <b><%=msg%> </b>
                </td>
              </tr>
            <%  }  %>
  <tr>
                  <td width="100%" style="padding:0; font-family: Arial (fantasy); font-size: 24pt; font-style: italic; font-weight: bold"> 
                    <font size="5"><%=title %></font> 
                    <hr>
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
<table width = "500" style="font-family: Arial; font-size: 10pt; color: #000000; border-collapse:collapse" cellpadding="3" cellspacing="0" border="0" width="898" bordercolor="#000000" align="center">
                <tr> 
                <%
                String[] headerData = report.getColumnHeaders();
                ArrayList<ReportDTO> reportData = report.getReportData();
                //System.out.println("data size : "+reportData.size());
                if(reportData.size()<=0)
                {%>
         			<td><p><h2>No Data Found</h2></p></td></tr>
                <%}
                else
                {
                	for(int i=0;i<headerData.length;i++)
                	{%>
                  <td width="17" style="font-weight: bold; text-align: center" valign="top"><div align="center"><font size="2"> 
                     <%=headerData[i] %></font></div></td>
               
                <%	
                	}
                %>
                </tr>
                <%
                for(int j = 0; j<reportData.size();j++)
                {%>
                <tr> 
                <%
                ReportDTO reportDto = reportData.get(j);
                for(int k = 0;k<reportDto.size();k++)
                	{
                		String cellElement = "-";
                		if(reportDto.getAtIndex(k) != null)
                		{
                			cellElement = reportDto.getAtIndex(k).toString();
                		}
%>
                  <td width="17" height="26" align="center" valign="top" style="font-family: Arial; font-size: 10pt" ><font size="2"><%=cellElement %></font></td>
                  <%} %>
                </tr>
                <%
                }
                %>

              </table>
              <table width="500" align="center">
              <tr align="center">
              <td align="center">
              
                	<html:form action ="/SaveOrDownloadReport" method = "POST">
	<table width= "500" align="center">
		<tr align="center">
			<td align="left"><input type = "submit" name ="saveOptions" value = "Save Options"></td>
			<td align="right"><input type = "submit" name = "downloadReport" value = "Download PDF"></td>
			<td align="right"><input type = "submit" name = "downloadReportXls" value = "Download XLS"></td>
		</tr>
	</table>
</html:form>
                <%
                }

%>
              
</td>
</tr>
</table>
</td></tr>
<!-- End of Data -->

</table><br></td>
</tr>

</table><!-- end of page table -->
</center>
</body>
</html>
