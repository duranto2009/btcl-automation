<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
<title><%=title %></title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>

<script language="JavaScript">
function goBack()
{
	window.history.back()
}

function validate()
	{
		var f = document.forms[0];

		var ob = f.regionName;
		if( isEmpty(ob.value))
		{
			alert("Please enter Region Name");
			ob.value = "";
			ob.focus();
			return false;
		}

		var ob = f.regionDesc;
		if( isEmpty(ob.value))
		{
			alert("Please enter Description");
			ob.value = "";
			ob.focus();
			return false;
		}


		return true;
	}

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
            <div class="div_title"><%=title %></div>
			</td>
          </tr>
          <tr>

            <td width="100%" align="center">

<br><br>
<!-- start of the form  -->

<html:form  action="<%=actionName %>" method="POST" onsubmit="return validate();">
<table  width="413" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
<%
RegionForm rf= (RegionForm) request.getAttribute("regionForm");
long regionID = Long.parseLong(rf.getRegionID());
rf.setStatus(RegionRepository.getInstance().getRegionID(regionID,"t").getStatus());
rf.setParentCode((RegionRepository.getInstance().getRegionID(regionID,"t").getParentCode()));
rf.setPrefixCode((RegionRepository.getInstance().getRegionID(regionID,"t").getPrefixCode()));



%>
<html:hidden property="regionID"/>
<html:hidden property="parentRegionName"/>
<html:hidden property="prefixCode"/>

<bean:define id="rc" scope="request"> <bean:write name="regionForm" property = "regionDesc"/></bean:define>  

<%if(rService.hasExchange(Long.parseLong(rc))) {%>
<TR>
	                  <TD height="22" width="154" align = "left" style= "padding-left:70px">Region Name</TD>
	                  <TD height="22" width="259" align = "left" style= "padding-left:70px"><html:text property="regionName"/><label style="color:red; vertical-align: middle;" >*</label></TD>
                        <html:hidden property="regionID"  />
</TR>

 <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="regionName"  /></TD>
                    </TR>

<TR>
	                  <TD height="22" width="154" align = "left" style= "padding-left:70px">Code</TD>
	<TD height="22" width="259" align = "left" style= "padding-left:70px"><html:hidden property = "regionDesc"/><bean:write name="regionForm" property="regionDesc" />
	</TD>
	</TR>
	<TR>
	 <TD height="22" width="154" align = "left" style= "padding-left:70px">Parent Region</TD>
	<TD height="22" width="259" align = "left" style= "padding-left:70px"><bean:write name="regionForm" property="parentRegionName"/>
	</TD>
</TR>

 <TR> 
 
 <%if(rf.getPrefixCode()!= 0){ %>

			  <TR>
	 <TD height="22" width="154" align = "left" style= "padding-left:70px">PrefixCode</TD>
	<TD height="22" width="259" align = "left" style= "padding-left:70px"><bean:write name="regionForm" property="prefixCode"/>
	</TD>
</TR>
<%} %>
                      <TD valign="top" height="22" align = "left" style= "padding-left:70px">Description</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:70px"><html:textarea property="description"  /></TD>
                    </TR>
                    
                     <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="description"  /></TD>
                    </TR>
                    <%}else{ %>
                    
                   
					 <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:70px">Name</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:70px"><html:text property="regionName" size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                    
                     <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="regionName"  /></TD>
                    </TR>
                   
					 <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:70px">Code</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:70px"><html:text property="regionDesc"  size="48"   /><label style="color:red; vertical-align: middle;" >*</label></TD>
                    </TR>
                     <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="regionDesc"  /></TD>
                    </TR>
                   <TR>
	 <TD height="22" width="154" align = "left" style= "padding-left:70px">Parent Region</TD>
	<TD height="22" width="259" align = "left" style= "padding-left:70px"><bean:write name="regionForm" property="parentRegionName"/>
	</TD>
</TR>



<%if(rf.getPrefixCode()!= 0){ %>

			  <TR>
	 <TD height="22" width="154" align = "left" style= "padding-left:70px">PrefixCode</TD>
	<TD height="22" width="259" align = "left" style= "padding-left:70px"><bean:write name="regionForm" property="prefixCode"/>
	</TD>
</TR>
<%} %>
                    <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:70px">Description</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:70px"><html:textarea property="description"/></TD>
                    </TR>
                    
                     <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="description"  /></TD>
                    </TR>
                    
                    
                    <%} %>
<TR>
<bean:define id="rstatus" scope="request"> <bean:write name="regionForm" property = "status"/></bean:define> 
<%if(rstatus.equals(""+RegionConstants.REGION_STATUS_ACTIVE)) {%>
 <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:70px">Status</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:70px"><html:hidden property="status"/><html:select property="status"  disabled = "true" >
                      <html:option value="<%=Integer.toString(RegionConstants.REGION_STATUS_ACTIVE) %>">Active</html:option>
                      <html:option value="<%=Integer.toString(RegionConstants.REGION_STATUS_DELETED) %>">DELETED</html:option>
                      
                      </html:select>
                      </TD>
                    </TR>
                    <%}else{ %>
                     <TR> 
                      <TD valign="top" height="22" align = "left" style= "padding-left:70px">Status</TD>
                      <TD height="22" colspan="2" align = "left" style= "padding-left:70px"><html:select property="status"  size ="1" >
                      <html:option value="<%=Integer.toString(RegionConstants.REGION_STATUS_ACTIVE) %>">Active</html:option>
                      <html:option value="<%=Integer.toString(RegionConstants.REGION_STATUS_DELETED) %>">DELETED</html:option>
                      
                      </html:select>
                      </TD>
                    </TR>
                    <%} %>
                    
                     <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="status"  /></TD>
                    </TR>
<TR>
	<TD valign="top" height="22" width="154">

	</TD>
	<TD height="22" width="259">
	</TD>
</TR>

</table>

<%if(rService.hasExchange(Long.parseLong(rc))) {%>

<html:reset>Reset</html:reset>		
<html:submit><%=submitCaption %></html:submit>

<%}else{ %>
<html:reset>Reset</html:reset>		
<html:submit><%=submitCaption %></html:submit>
<%} %>

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
