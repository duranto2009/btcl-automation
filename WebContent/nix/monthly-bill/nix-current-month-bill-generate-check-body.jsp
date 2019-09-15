<%@page import="java.text.SimpleDateFormat" %>
<%@page import="util.TimeConverter" %>
<%@page import="vpn.client.ClientDetailsDTO" %>
<%@page import="common.repository.AllClientRepository" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Calendar" %>
<%@page import="java.util.Date" %>
<%@page import="sessionmanager.SessionConstants" %>


<%
    String msg = null;
    String url = "nix/monthly-bill/check";
    String navigator = SessionConstants.NAV_NIX_BILL_GENERATION_CHECK;
    String context = "../../.." + request.getContextPath() + "/";
    System.out.println((String) request.getParameter("title"));
    boolean isGeneration = true;
%>

<jsp:include page="../../includes/nav.jsp" flush="true">
    <jsp:param name="url" value="<%=url%>"/>
    <jsp:param name="navigator" value="<%=navigator%>"/>
</jsp:include>

<%
    String fromTime = (String) session.getAttribute("Month");
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat dayMonthYearDateFormat = new SimpleDateFormat("yyyy-MM");

    if (fromTime != null && fromTime.length() > 0) {
        System.out.print("fromTime: " + fromTime);
        Date date = dayMonthYearDateFormat.parse(fromTime);
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        cal.setTime(new Date());
        int curmonth = cal.get(Calendar.MONTH);
        int curyear = cal.get(Calendar.YEAR);
        if (curyear >= year && curmonth > month) {
            isGeneration = false;
        } else isGeneration = true;
    } else {
        fromTime = dayMonthYearDateFormat.format(new Date());
    }
%>

<div id="monthlybillGenerateCheckBody" class="portlet box">
    <div class="portlet-body">
        <div class="table-responsive">
            <table class="table table-bordered table-striped">
                <thead>
                <tr>
                    <th>ClientID</th>
                    <th>Name</th>
                    <th>Activation Date</th>
                    <th>Bill of Month</th>
                    <%if (isGeneration) {%>
                    <th class="text-center">
                        <input type="checkbox"
                               name="checkAll"
                               value="CheckAll"
                               @click="isCheckALl"
                               class="group-checkable pull-left"/>
                    </th>
                    <%} %>

                </tr>
                </thead>
                <tbody>
                <%
                    ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_NIX_BILL_GENERATION_CHECK);
                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            ClientDetailsDTO clientDetailsDTO = (ClientDetailsDTO) data.get(i);
                %>
                <tr>
                    <td>
                        <a href="<%=context%>nix/monthly-bill/view.do?id=<%=clientDetailsDTO.getClientID() %>"><%=clientDetailsDTO.getClientID()%>
                        </a>

                    </td>
                    <td><%=clientDetailsDTO.getName()%>
                    </td>
                    <td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(clientDetailsDTO.getActivationDate(), "dd-MM-YYYY")%>
                    </td>
                    <td><%=fromTime%>
                    </td>

                    <%if (isGeneration) {%>
                    <td class="text-center">
                        <input class="checkboxes" type="checkbox" name="cIDs"
                               data-client-id="<%=clientDetailsDTO.getClientID() %>"
                               value="<%=clientDetailsDTO.getClientID() %>">
                    </td>
                    <%} %>

                </tr>
                <%
                    }
                %>
                </tbody>
                <% } %>
            </table>

            <%if (data != null && data.size() > 0 && isGeneration) {%>
            <div>
                <button id=" generateButton"
                        class="btn btn-sm  btn-circle green-meadow uppercase"
                        @click="genrateBillOfTheMonth()" :disabled="clicked">Generate Bill
                </button>
            </div>
            <%} %>
        </div>
    </div>
</div>

<script src=nix-monthly-bill-generateCheck.js></script>

<script type="text/javascript">
    var month = $("input[name='Month']").val();
</script> 


