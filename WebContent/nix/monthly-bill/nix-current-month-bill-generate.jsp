<%@ include file="../../includes/checkLogin.jsp" %>
<%
    request.setAttribute("menu","nixMenu");
    request.setAttribute("subMenu1","nix-monthly-Bill-summary");
    request.setAttribute("subMenu2","nix-monthly-bill-generate");
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
        <jsp:param name="title" value="NIX monthly bill generate" />
        <jsp:param name="body" value="../nix/monthly-bill/nix-current-month-bill-generate-body.jsp" />
</jsp:include>
