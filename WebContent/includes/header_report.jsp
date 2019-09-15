<%//if(loginDTO.getWebRoot()== null){ %>

<%java.text.SimpleDateFormat header_report_datefmt = new java.text.SimpleDateFormat ("dd-MMM-yyyy HH:mm:ss zzz"); %>
<table width="780" cellpadding="0" cellspacing="0" bgcolor="white">
  <tr>
    <td align="center" width="780" height="100">
      <img border="0" src="<%=request.getContextPath().toString()%>/images/common/BTCL-Logo.jpg" width="250" height="100" style="padding: 0;margin:0" />
    </td>
   
    </tr>
    <tr>
    	 <td width="780" height="50" bgcolor="white" valign="bottom" align="right">
      <%="<b>Server Time:&nbsp;</b> "+header_report_datefmt.format(new java.util.Date())+"&nbsp;"%>
    </td>
    </tr>
  </table>
<%//}else{ %>

<table width="600" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="100%">
      <!--   <img border="0" src="<%//=loginDTO.getWebRoot()+"/header_600.jpg"%>" style="padding: 0;margin:0" width="600"/> -->
	      </td>
  </tr>
</table>

<%//} %>
