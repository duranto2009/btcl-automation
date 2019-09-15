<%@ page import="scheduler.common.SchedulerConstants" %>
<%
    //set up the menu
    request.setAttribute("menu", "global_search");
    String requstURI = (String)request.getAttribute(SchedulerConstants.REQUESTED_URI);
    String path = "";
    if(SchedulerConstants.SCHEDULER_ALL.equalsIgnoreCase(requstURI)){
        request.setAttribute("subMenu1","scheduler");
        request.setAttribute("subMenu2","scheduler-view-all");
        path = "../scheduler/scheduler-all.jsp";
    }


%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="Scheduler" />
    <jsp:param name="body" value="<%=path%>" />
</jsp:include>