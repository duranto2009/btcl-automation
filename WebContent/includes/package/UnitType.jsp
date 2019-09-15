<%@page import="packages.PackageConstants"%>
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%String valuePackageType = (String)session.getAttribute("pUnitType");
if(valuePackageType == null)valuePackageType="-1";
%>
<tr>
	<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Unit Type</td>
	<td bgcolor="#deede6" align="left" height="25"> 
		<select size="1" name="pUnitType">
			<option value="-1" <%=(valuePackageType.equals("-1")?"selected=\"selected\"":"")%>>All</option>
			<option value="<%=PackageConstants.UNIT_TYPE_PER_MB%>" <%=(valuePackageType.equals(""+PackageConstants.UNIT_TYPE_PER_MB)?"selected=\"selected\"":"")%>>Per MB</option>
			<option value="<%=PackageConstants.UNIT_TYPE_PER_KB%>" <%=(valuePackageType.equals(""+PackageConstants.UNIT_TYPE_PER_KB)?"selected=\"selected\"":"")%>>Per KB</option>
			<option value="<%=PackageConstants.UNIT_TYPE_PER_MIN%>" <%=(valuePackageType.equals(""+PackageConstants.UNIT_TYPE_PER_MIN)?"selected=\"selected\"":"")%>>Per Min</option>
		</select>
	</td>
</tr>