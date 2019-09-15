<%
    request.setAttribute("menu","global_search");
    request.setAttribute("subMenu1","official_letter");
//    request.setAttribute("subMenu2","search-official-letter");
%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="Official Letter" />
    <jsp:param name="body" value="../officialLetter/search-official-letter-body.jsp" />
    <jsp:param name="helpers" value="../common/datePickerHelper.jsp" />
</jsp:include>