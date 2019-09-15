<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="login.PermissionConstants"%>
<%@include file="../includes/checkLogin.jsp"%>
<%@page language="java"%>
<%@taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="java.util.ArrayList,sessionmanager.SessionConstants,discount.*"%>
<%@page errorPage="../common/failure.jsp"%>
<html>
<head>
<html:base/>
<title>Discount</title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>
<script language="JavaScript">

function CheckAll()
{
  var form = document.forms[2];
  if  (form.Check_All.checked == true)
  {
	  form.Check_All_App.checked = false;	  
	  form.Check_All_Den.checked = false;
	  CheckAllApp();
	  CheckAllDen();
	  
    for(i=0; i<form.deletedIDs.length; i++)
    	if(form.deletedIDs[i].disabled == false)
    			form.deletedIDs[i].checked = true;
  }
  else
  {
    for(i=0; i<form.deletedIDs.length; i++)
    	if(form.deletedIDs[i].disabled == false)
    		form.deletedIDs[i].checked = false;
  }
  return true;
}

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
          
          <td width="780" valign="top" class="td_main" align="center">
            <table border="0" cellpadding="0" cellspacing="0" width="100%" id="AutoNumber3">
              <tr>
                <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
                  <div class="div_title">
                    <!--Client-->
Discount                </div>
                </td>
              </tr>
            <%
              String msg = null;
              if ((msg = (String) session.getAttribute(util.ConformationMessage.DISCOUNT_UPDATE)) != null)
			    {
                session.removeAttribute(util.ConformationMessage.DISCOUNT_UPDATE);
            %>
              <tr>
                <td width="100%" align="center" valign="top" height="25">
                  <b><%=msg%> </b>
                </td>
              </tr>
            <%  }  %>
              <tr>
                <td width="100%" align="center">
                  <br/>
                  <!-- search and navigation control-->
                <%
                  String url = "ViewDiscount";
                  String navigator = SessionConstants.NAV_DISCOUNT;
                %>
                  <jsp:include page="../includes/nav.jsp" flush="true">
                  <jsp:param name="url" value="<%=url %>"/>
                  <jsp:param name="navigator" value="<%=navigator %>"/>
                  </jsp:include>
                  <!-- search and navigation control -->
                  <!-- start of the form -->
                  <html:form action="/DropDiscount" method="POST">
                    <table width="595" class="table_view">
                      <tr>
                        <td class="td_viewheader" align="center" valign="top" width="110" height="25">
                          <!--Name-->
						Discount Name</td>
                        
                        <td class="td_viewheader" align="center" valign="top" width="70" height="25">
                          <!--Discount Category-->
                         Discount Category </td>                      
                        
                         
                      
                          <td class="td_viewheader" align="center" valign="top" width="50" height="25">
                          <!--Edit-->
						Edit</td>                        
                        <td class="td_viewheader"  align="center" width="58"  >
	                <input type="submit"  value="Delete" >
                </td>
                      </tr>
                    <%
                      ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_DISCOUNT);
                      if (data != null) {
                        int size = data.size();
                        System.out.println("size:"+size);
                        DiscountService fservice = new DiscountService();
                        
                        for (int i = 0; i < size; i++)
						{
                          DiscountDTO row = (DiscountDTO) data.get(i);
                    %>
                      <tr>
                        <td class="td_viewdata1" align="center" width="110" height="15"><%=row.getDiscountName() %> </td>                         
                        <td class="td_viewdata1" align="center" width="70" height="15"><%=DiscountConstants.DISCOUNT_CATEGORY_NAMES[(int)row.getDiscountCategoryId() -1]  %> </td>  
                        
                        
                        <td class="td_viewdata1" align="center" width="50" height="15">
                          <a href="../GetDiscount.do?id=<%=row.getDiscountID() %>">
                            <!--Edit-->
						Edit </a></td>
                       <%--  </td>
                        <td class="td_viewdata2" align="center" width="50" height="15">
                          <a target="_blank" href="../clients/printableViewClientDetail.jsp?id=<%=row.getUniqueID() %>">View</a>
                        </td> --%>
                        
                        <%if((fservice.hasPackage(""+row.getDiscountID())&& row.getDiscountActivationDate()<System.currentTimeMillis()) || row.getDiscountStatus() == DiscountConstants.DISCOUNT_STATE_DELETED){ %>
				
		                <td class="td_viewdata2"  align="center" width="58" >
			                <input type="checkbox" name="deletedIDs" value="<%=row.getDiscountID() %>" disabled = "true"/>
		                </td>
		               <%}else{ %>
		               <td class="td_viewdata2"  align="center" width="58" >
			                <input type="checkbox" name="deletedIDs" value="<%=row.getDiscountID() %>" >
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
