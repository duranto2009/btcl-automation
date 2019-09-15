
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%String rcType = (String)session.getAttribute("rcType");
if(rcType == null)rcType="-1";
%>
<tr>
	<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25">Report Configuration Type</td>
	<td bgcolor="#deede6" align="left" height="25"> 
		<select size="1" name="rcType">
			<option value="" <%=(rcType.equals("")?"selected=\"selected\"":"")%>>All</option>
			<option value="<%="Type of Connectivity Services" %>" <%=(rcType.equals(""+"Type of Connectivity Services")?"selected=\"selected\"":"")%>>Type of Connectivity Services</option>
			<option value="<%="Bandwidth Consumption and Data Transfer"%>" <%=(rcType.equals(""+"Bandwidth Consumption and Data Transfer")?"selected=\"selected\"":"")%>>Bandwidth Consumption and Data Transfer</option>
			<option value="<%="Billing Packages" %>" <%=(rcType.equals(""+"Billing Packages")?"selected=\"selected\"":"")%>>Billing Packages</option>	
			<option value="<%="Region Wise" %>" <%=(rcType.equals(""+"Region Wise")?"selected=\"selected\"":"")%>>Region Wise</option>
			<option value="<%="Customer Details" %>" <%=(rcType.equals(""+"Customer Details")?"selected=\"selected\"":"")%>>Customer Details</option>
			<option value="<%="System Admin" %>" <%=(rcType.equals(""+"System Admin")?"selected=\"selected\"":"")%>>System Admin</option>
			<option value="<%="Tariff Details" %>" <%=(rcType.equals(""+"Tariff Details")?"selected=\"selected\"":"")%>>Tariff Details</option>
			<option value="<%="Traffic Details" %>" <%=(rcType.equals(""+"Traffic Details")?"selected=\"selected\"":"")%>>Traffic Details</option>
			<option value="<%="Usage Details" %>" <%=(rcType.equals(""+"Usage Details")?"selected=\"selected\"":"")%>>Usage Details</option>
			<option value="<%="Package Wise Customer Details" %>" <%=(rcType.equals(""+"Package Wise Customer Details")?"selected=\"selected\"":"")%>>Package Wise Customer Details</option>
			<option value="<%="Login Status" %>" <%=(rcType.equals(""+"Login Status")?"selected=\"selected\"":"")%>>Login Status</option>
			<option value="<%="Client Status" %>" <%=(rcType.equals(""+"Client Status")?"selected=\"selected\"":"")%>>Client Status</option>
			<option value="<%="Password Change" %>" <%=(rcType.equals(""+"Password Change")?"selected=\"selected\"":"")%>>Password Change</option>
		</select>
	</td>
</tr>