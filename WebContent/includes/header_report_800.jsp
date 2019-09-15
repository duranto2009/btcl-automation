<%//if(loginDTO.getWebRoot()== null){ %>

<%java.text.SimpleDateFormat header_report_datefmt = new java.text.SimpleDateFormat ("dd-MMM-yyyy HH:mm:ss zzz"); %>
<table width="800" cellpadding="0" cellspacing="0" bgcolor="rgb(130,168,230)">
  <tr>
    <td width="250" height="50">
      <img border="0" src="../images/common/report_title.png" width="250" height="50" style="padding: 0;margin:0" />
    </td>
    <td width="550" height="50" style="color:white" valign="top" align="right">
      <%="<b>Server Time:&nbsp;</b> "+header_report_datefmt.format(new java.util.Date())+"&nbsp;"%>
      </td>
    </tr>
  </table>
<%//}else{ %>

<table width="800" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="100%">
     <!-- <img border="0" src="<%//=loginDTO.getWebRoot()+"/header_800.jpg"%>" style="padding: 0;margin:0" width="800"/> -->
    </td>
  </tr>
</table>

<%//} %>
