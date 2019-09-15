<%@page import="packages.PackageConstants"%>
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%String valuePackageType = (String)session.getAttribute("pTariffType");
if(valuePackageType == null)valuePackageType="-1";
%>
<tr>
	<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Package Plan Type</td>
	<td bgcolor="#deede6" align="left" height="25"> 
		<select size="1" name="pTariffType">
			<option value="-1" <%=(valuePackageType.equals("-1")?"selected=\"selected\"":"")%>>All</option>
			<option value="<%=PackageConstants.PACKAGE_PLAN_TYPE_VOLUME_BASED%>" <%=(valuePackageType.equals(""+PackageConstants.PACKAGE_PLAN_TYPE_VOLUME_BASED)?"selected=\"selected\"":"")%>>Volume Based</option>
			<option value="<%=PackageConstants.PACKAGE_PLAN_TYPE_TIME_BASED%>" <%=(valuePackageType.equals(""+PackageConstants.PACKAGE_PLAN_TYPE_TIME_BASED)?"selected=\"selected\"":"")%>>Time Based</option>
			<option value="<%=PackageConstants.PACKAGE_PLAN_TYPE_VPN%>" <%=(valuePackageType.equals(""+PackageConstants.PACKAGE_PLAN_TYPE_VPN)?"selected=\"selected\"":"")%>>VPN</option>
		</select>
	</td>
</tr>