<%@page import="common.repository.AllClientRepository" %>
<%@page import="lli.Application.LLIApplication" %>
<%@page import="lli.connection.LLIConnectionConstants" %>
<%@page import="sessionmanager.SessionConstants" %>
<%@page import="util.TimeConverter" %>
<%@page import="java.util.ArrayList" %>
<%
    String url = "lli/application/search";
    String navigator = SessionConstants.NAV_LLI_APPLICATION;
    String context = "../../.." + request.getContextPath() + "/";
%>


<jsp:include page="../../../includes/nav.jsp" flush="true">
    <jsp:param name="url" value="<%=url%>"/>
    <jsp:param name="navigator" value="<%=navigator%>"/>
</jsp:include>


<div class="portlet box">
    <div class="portlet-body">
        <div class="table-responsive">
            <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                    <th>Application ID</th>
                    <th>Client</th>
                    <th>Application Type</th>
                    <th>Status</th>
                    <th>App Date</th>
                    <th>Details</th>
                </tr>
                </thead>
                <tbody>
                <%
                    ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_LLI_APPLICATION);

                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            LLIApplication lliApplication = (LLIApplication) data.get(i);
                %>
                <tr>
                    <td style="font-weight: bold">
                        <%if (lliApplication.getHasPermission()) {%>
                        <a href="<%=context%>lli/application/newview.do?id=<%=lliApplication.getApplicationID() %>"><%=lliApplication.getApplicationID()%>
                        </a>
                        <% } else { %>
                        <p><%=lliApplication.getApplicationID()%>
                        </p>
                        <%}%>

                    </td>
                    <td> <a href="<%=context%>lli/client/board.do?id=<%=lliApplication.getClientID()%>"><%=AllClientRepository.getInstance().getClientByClientID(lliApplication.getClientID()).getLoginName()%>
                    </a></td>
                    <td><%=LLIConnectionConstants.applicationTypeNameMap.get(lliApplication.getApplicationType())%>
                    </td>
                    <td style="color: <%=lliApplication.getColor()%>;font-weight: bold"><%=lliApplication.getStateDescription()%>
                    </td>
                    <td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd-MM-YYYY")%>
                    </td>
                    <td><a href="<%=context%>lli/application/new-connection-details.do?id=<%=lliApplication.getApplicationID() %>">
                        <%--<%=lliApplication.getApplicationID()%>--%>
                            <span class="glyphicon glyphicon-list-alt"></span>
                    </a></td>
                </tr>
                <%
                    }
                %>
                </tbody>
                <% } %>
            </table>
        </div>
    </div>
</div>
