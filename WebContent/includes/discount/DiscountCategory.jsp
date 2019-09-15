<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,discount.*"%>
<%String discountCategoryID = (String)session.getAttribute("discountCategoryID");
if(discountCategoryID == null)discountCategoryID="-1";
%>
<tr >

<%-- <html:hidden property="discountCategoryID" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Discount Category</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="discountCategoryID">
 <option value="-1" <%=(discountCategoryID.equals("-1")?"selected=\"selected\"":"")%>>All</option>
 <%					          
   
    for(int i=0;i<DiscountConstants.DISCOUNT_CATEGORY_VALUES.length;i++)
    {
     
 %>
               <option value="<%=DiscountConstants.DISCOUNT_CATEGORY_VALUES[i]%>" <%=(discountCategoryID.equals(""+DiscountConstants.DISCOUNT_CATEGORY_VALUES[i])?"selected=\"selected\"":"")%>><%=DiscountConstants.DISCOUNT_CATEGORY_NAMES[i]%></option>
 <% } %>
 
  
</select>
</td>
</tr>