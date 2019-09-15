
<map name="FPMap0">
  <area href="../Welcome.do" shape="rect" coords="70, 0, 130, 30"/>
  <area href="../Logout.do"      shape="rect" coords="0, 0, 65, 30"  />
</map>
<div style="position:relative">    
  <img border="0" src="../images/common/banner_hosting.jpg" width="100%" style="padding: 0;margin:0" />
  <div style="position:absolute;right:0;top:0;">
      <ul class="menu">
      <li ><a href="../Welcome.do" style="font-weight:bold;font-size:110%">HOME</a></li>
      	<li ><a href="../Logout.do" style="font-weight:bold;font-size:110%">LOGOUT</a></li>      	
      </ul>
  </div>
</div>
<table width="1024" border="0" cellpadding="0" cellspacing="0" bgcolor="white">
<%--   <tr>
    <td width="519" height="85" rowspan="3">
      <img border="0" src="../images/common/banner_hosting.jpg" width="300" height="85" style="padding: 0;margin:0" />
    </td>
  </tr>
  <tr>
  <td width="200" height="55">&nbsp;</td>
  <!-- <td width="535" height="55" style="color:white" colspan="2" valign="top" align="right"> <img border="0" src="../images/common/banner.jpg" width="535" height="55" style="padding: 0;margin:0"/> </td> -->
    <td width="535" height="45" style="color:white" colspan="2" valign="top" align="right"> <%="<b>Server Time:&nbsp;</b>"+header_datefmt.format(new java.util.Date())+"&nbsp;"%> </td>
  </tr>

  <tr>
    <td align="center" width="200" height="25" bgcolor= "#13bb8d">
      <ul class="menu">
      	<li style="width:100px"><a href="../Logout.do" class="menulink">Log Out</a></li>
      	<li style="width:100px"><a href="../home/index.jsp" class="menulink">Home</a></li>
      </ul>
    </td>
  </tr>--%>
  <tr> 
  	<td width="1024" height="32" colspan="1" bgcolor="#04692F" rowspan="1" align="center">
  		<%@ include file="../includes/menu_client.jsp" %>
  	</td>
  </tr>
</table>
