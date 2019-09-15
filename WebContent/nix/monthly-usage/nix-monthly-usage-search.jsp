<%@ include file="../../includes/checkLogin.jsp" %>
<%
    request.setAttribute("menu","nixMenu");
    request.setAttribute("subMenu1","nix-monthly-usage");
    request.setAttribute("subMenu2","nix-monthly-usage-search");
%>
<%
boolean isAdmin = false;
if( loginDTO.getUserID()>0 )
{ 	
        isAdmin=true;
}
%>

<script>var isAdmin= <%=isAdmin %></script>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="NIX monthly usage" />
    <jsp:param name="body" value="../nix/monthly-usage/nix-monthly-usage-search-body.jsp" />
	<jsp:param name="helpers" value="/common/datePickerHelper.jsp" />
</jsp:include>
