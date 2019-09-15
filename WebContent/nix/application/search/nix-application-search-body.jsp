<%@page import="util.TimeConverter" %>
<%@page import="common.repository.AllClientRepository" %>
<%@page import="nix.application.NIXApplication" %>
<%@page import="java.util.ArrayList" %>
<%@page import="sessionmanager.SessionConstants" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="nix.constants.NIXConstants" %>
<%
    String msg = null;
    String url = "nix/application/search";
    String navigator = SessionConstants.NAV_NIX_APPLICATION;
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
                    ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_NIX_APPLICATION);




                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            NIXApplication nixApplication = (NIXApplication) data.get(i);
                %>
                <tr>
                    <td style="font-weight: bold">
                        <%if (nixApplication.isHasPermission()) {%>
                        <a href="<%=context%>nix/application/newview.do?id=<%=nixApplication.getId() %>"><%=nixApplication.getId()%>
                        </a>
                        <% } else { %>
                        <p><%=nixApplication.getId()%>
                        </p>
                        <%}%>

                    </td>
                    <td> <a href="<%=context%>nix/client/board.do?id=<%=nixApplication.getClient()%>"><%=AllClientRepository.getInstance().getClientByClientID(nixApplication.getClient()).getLoginName()%>
                    </a></td>
                    <td><%=NIXConstants.nixapplicationTypeNameMap.get(nixApplication.getType())%>
                    </td>
                    <td style="color: <%=nixApplication.getColor()%>;font-weight: bold">
                        <%=nixApplication.getStateDescription()%>
                    </td>
                    <td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(
                            nixApplication.getSubmissionDate(), "dd-MM-YYYY")%>
                    </td>
                    <td>
                        <a href="<%=context%>nix/application/new-connection-details.do?id=<%=nixApplication.getId()%>">
                        <%--<%=nixApplication.getId()%>--%>
                            <span class="glyphicon glyphicon-list-alt"></span>
                        </a>
                    </td>
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
