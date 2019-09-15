<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>


<%
  request.setAttribute("menu","crmMenu");
  request.setAttribute("subMenu1","crmDepartmentSubmenu1");
  request.setAttribute("subMenu2","crmDepartmentSubmenu2"); 

%> 

<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="New Department Add" />
	<jsp:param name="body" value="../crm/department/departmentAddBody.jsp" />
</jsp:include>