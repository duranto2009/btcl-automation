<%@page import="login.PermissionConstants" %>
<%@page import="common.ModuleConstants" %>
<li class="nav-item" id="colocationMenu">

    <a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-server" aria-hidden="true"></i> <span
            class="title">CoLocation</span> <span class="arrow"></span></a>

    <ul class="sub-menu">

        <!-- Client -->
        <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COLOCATION_CLIENT) != -1) {%>
        <li class="nav-item" id="colocationClientMenu">
            <a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-user"></i>
                <span class="title">Client</span> <span class="arrow"></span>
            </a>
            <ul class="sub-menu">

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COLOCATION_CLIENT_ADD) >= PermissionConstants.PERMISSION_FULL) {%>
                <li class="nav-item" id="addColocationClientMenu">
                    <a href="${context}client/client/clientAdd.jsp?moduleID=<%=ModuleConstants.Module_ID_COLOCATION%>"
                       class="nav-link ">
                        <i class="fa fa-plus"></i><span class="title">Add</span>
                    </a>
                </li>
                <%}%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COLOCATION_CLIENT_SEARCH) != -1) {%>
                <li class="nav-item" id="searchColocationClientMenu">
                    <a href="${context}SearchClient.do?moduleID=<%=ModuleConstants.Module_ID_COLOCATION %>"
                       class="nav-link ">
                        <i class="fa fa-search"></i><span class="title">Search</span>
                    </a>
                </li>
                <%} %>

            </ul>
        </li>
        <%}%>


        <!-- Colocation -->
        <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COLOCATION_COLOCATION) != -1) {%>
        <li class="nav-item" id="colocationColocationSubmenu1">
            <a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-server"></i>
                <span class="title">Manage CoLocation</span> <span class="arrow"></span>
            </a>
            <ul class="sub-menu">

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COLOCATION_COLOCATION_ADD) != -1) { %>
                <li class="nav-item" id="new-connection">
                    <a href="${context}co-location/new-connection-application.do" class="nav-link ">
                        <i class="fa fa-plus"></i><span class="title">New Connection</span>
                    </a>
                </li>
                <%}%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COLOCATION_COLOCATION_ADD) != -1) { %>
                <li class="nav-item" id="revise-connection">
                    <a href="${context}co-location/revise-application.do" class="nav-link ">
                        <i class="fa fa-plus"></i><span class="title">Revise Connection</span>
                    </a>
                </li>
                <%}%>




                <%--TD--%>
                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COLOCATION_COLOCATION_TD) != -1) { %>
                <li class="nav-item" id="coloc-td">
                    <a href="${context}co-location/probable-td.do" class="nav-link ">
                        <i class="fa  fa-plug"></i><span class="title">TD</span>
                    </a>
                </li>
                <%}%>

                <%--RECONNECT--%>
                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COLOCATION_COLOCATION_RECONNECT) != -1) { %>
                <li class="nav-item" id="coloc-reconnect">
                    <a href="${context}co-location/reconnect.do" class="nav-link ">
                        <i class="fa fa-recycle"></i><span class="title">Reconnect</span>
                    </a>
                </li>
                <%}%>

                <%--Close--%>
                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COLOCATION_COLOCATION_CLOSE) != -1) { %>
                <li class="nav-item" id="coloc-close">
                    <a href="${context}co-location/close-connection.do" class="nav-link ">
                        <i class="fa fa-trash"></i><span class="title">Close and Dismantle</span>
                    </a>
                </li>
                <%}%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COLOCATION_COLOCATION_SEARCH) != -1) {%>
                <li class="nav-item" id="search-application">
                    <a href="${context}co-location/search.do" class="nav-link ">
                        <i class="fa fa-search"></i><span class="title">Search Application</span>
                    </a>
                </li>
                <%} %>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COLOCATION_COLOCATION_SEARCH) != -1) {%>
                <li class="nav-item" id="search-connection">
                    <a href="${context}co-location/connection-search.do" class="nav-link ">
                        <i class="fa fa-search"></i><span class="title">Search Connection</span>
                    </a>
                </li>
                <%} %>




            </ul>
        </li>
        <%}%>


        <!-- Bill and Payment -->


        <!-- Inventory -->
        <%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COLOCATION_INVENTORY) != -1) {%>--%>
        <li class="nav-item" id="colocationInventorySubmenu1">
            <a href="javascript:;" class="nav-link nav-toggle"><i class="fa fa-bank"></i><span
                    class="title">Inventory</span><span class="arrow"></span></a>

            <ul class="sub-menu">


                <%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COLOCATION_INVENTORY_ADD) != -1) {%>--%>
                <li class="nav-item " id="colocationInventoryAdd">
                    <a href="${context}co-location/inventory-add.do" class="nav-link "><i
                            class="fa fa-plus"></i><span class="title">Add</span></a>
                </li>
                <%--<%}%>--%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COLOCATION_INVENTORY_SEARCH) != -1) {%>
                <li class="nav-item " id="colocationInventorySearch">
                    <a href="${context}co-location/inventory-search.do" class="nav-link "><i
                            class="fa fa-search"></i><span class="title">Search</span></a>
                </li>
                <%}%>


            </ul>
        </li>
        <%--<%}%>--%>

        <%if(menuLoginDTO.getMenuPermission(PermissionConstants.COLOCATION_BILL)!=-1) {
        %>
        <li class="nav-item" id="colocationBillPaymentSubmenu1">
            <a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-money"></i>
                <span class="title">Bill & Payment</span> <span class="arrow"></span>
            </a>
            <ul class="sub-menu">

                <%if(menuLoginDTO.getMenuPermission(PermissionConstants.COLOCATION_BILL_SEARCH)!=-1) {
                %>
                <li class="nav-item" id="billSearchColocationBillPaymentSubmenu2">
                    <a href="${context}bill/search.do?moduleID=<%=ModuleConstants.Module_ID_COLOCATION %>"
                       class="nav-link ">
                        <i class="fa fa-search"></i><span class="title">Bill Search</span>
                    </a>
                </li>
                <%}%>

                <%if(menuLoginDTO.getMenuPermission(PermissionConstants.COLOCATION_BILL_PAYMENT_SEARCH)!=-1) {
                %>
                <li class="nav-item" id="paymentSearchColocationBillPaymentSubmenu2">
                    <a href="${context}payment/search.do?moduleID=<%=ModuleConstants.Module_ID_COLOCATION %>"
                       class="nav-link ">
                        <i class="fa fa-search"></i><span class="title">Payment Search</span>
                    </a>
                </li>
                <%} %>
                <%if(menuLoginDTO.getMenuPermission(PermissionConstants.COLOCATION_MANUAL_DEMAND_NOTE)!=-1) {
                %>
                <li class="nav-item" id="manualDemandNoteColocationBillPaymentSubmenu2">
                    <a href="${context}coLocation/NIXDemandNote/manualDemandNote.jsp?moduleID=<%=ModuleConstants.Module_ID_COLOCATION %>"
                       class="nav-link ">
                        <i class="fa fa-search"></i><span class="title">Manual Demand Note</span>
                    </a>
                </li>
                <%} %>

            </ul>
        </li>
        <%}%>


        <!-- 	CONFIGURATION    -->
        <%if(menuLoginDTO.getMenuPermission(PermissionConstants.COLOCATION_CONFIGURATION)!=-1) {
        %>
        <li class="nav-item" id="colocationConfigurationSubmenu1">
            <a href="javascript:;" class="nav-link nav-toggle"><i class="fa fa-gear"></i> <span class="title">Configuration</span><span
                    class="arrow"></span></a>

            <ul class="sub-menu">

                <%if(menuLoginDTO.getMenuPermission(PermissionConstants.COLOCATION_CONFIGURATION_COST)!=-1) {
                %>
                <li class="nav-item" id="costConfigureColocationColocationSubmenu2">
                    <a href="${context}co-location/inventory-cost-config.do" class="nav-link "><i
                            class="fa fa-money"></i><span
                            class="title">Cost Configuration</span></a>
                </li>
                <%}%>

            </ul>
        </li>
        <%}%>


        <!-- 	REQUEST    -->
        <%if(menuLoginDTO.getMenuPermission(PermissionConstants.COLOCATION_REQUEST)!=-1) {
        %>
        <li class="nav-item" id="colocationRequestSubMenu">
            <a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Request</span>
                <span class="arrow"></span></a>

            <ul class="sub-menu">

                <%if(menuLoginDTO.getMenuPermission(PermissionConstants.COLOCATION_REQUEST_SEARCH)!=-1) {
                %>
                <li class="nav-item" id="colocationSearchRequestSubMenu">
                    <a href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_COLOCATION %>"
                       class="nav-link "><i class="fa fa-search"></i><span class="title">Search </span></a>
                </li>
                <%}%>

            </ul>
        </li>
        <%}%>


        <!-- 	MIGRATION    -->
        <%if(menuLoginDTO.getMenuPermission(PermissionConstants.COLOCATION_MIGRATION)!=-1) {
        %>
        <li class="nav-item" id="colocationMigration">
            <a href="javascript:;" class="nav-link nav-toggle"><i class="fa fa-external-link"></i><span class="title">Migration</span><span
                    class="arrow"></span></a>

            <ul class="sub-menu">

                <%if(menuLoginDTO.getMenuPermission(PermissionConstants.COLOCATION_MIGRATION_BILL)!=-1) {
                %>
                <li class="nav-item" id="colocationBillMigration">
                    <a href="${context}colocation/colocation/generateDemandNoteMigration.jsp" class="nav-link "><i
                            class="fa fa-money"></i><span class="title">Bill Migration</span></a>
                </li>
                <%}%>

                <%if(menuLoginDTO.getMenuPermission(PermissionConstants.COLOCATION_MIGRATION_CLIENT)!=-1) {
                %>
                <li class="nav-item" id="colocationBillMigration">
                    <a href="${context}colocation/colocation/generateDemandNoteMigration.jsp" class="nav-link "><i
                            class="fa fa-money"></i><span class="title">Client Migration</span></a>
                </li>
                <%}%>

            </ul>
        </li>
        <%}%>
        <%if(menuLoginDTO.getMenuPermission(PermissionConstants.GENERAL_REPORT)!=-1) {
        %>
        <li class="nav-item  " id="colocationReportMenu">
            <a href="javascript:;" class="nav-link nav-toggle">
                <i class="fa fa-file"></i> <span class="title">Report</span> <span class="arrow"></span>
            </a>
            <ul class="sub-menu">
                <%
                    if(menuLoginDTO.getMenuPermission(login.PermissionConstants.REPORT_CLIENT)!=-1) {
                %>
                <li class="nav-item" id="colocationClientReportMenu">
                    <a href="${context}Report/GetClientReport.do?moduleID=<%=ModuleConstants.Module_ID_COLOCATION%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span class="title">Client Report</span>
                    </a>
                </li>
                <%} %>
                <%if(menuLoginDTO.getMenuPermission(PermissionConstants.REPORT_BILL)!=-1) {
                %>
                <li class="nav-item" id="colocationBillReportMenu">
                    <a href="${context}Report/GetBillReport.do?moduleID=<%=ModuleConstants.Module_ID_COLOCATION%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span class="title">Bill Report</span>
                    </a>
                </li>
                <%} %>
                <%if(menuLoginDTO.getMenuPermission(login.PermissionConstants.REPORT_PAYMENT)!=-1) {
                %>
                <li class="nav-item" id="colocationPaymentReportMenu">
                    <a href="${context}Report/GetPaymentReport.do?moduleID=<%=ModuleConstants.Module_ID_COLOCATION%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span class="title">Payment Report</span>
                    </a>
                </li>
                <%} %>
                <%if(menuLoginDTO.getMenuPermission(PermissionConstants.REPORT_CONNECTION)!=-1) {
                %>
                <li class="nav-item" id="colocationConnectionReportMenu">
                    <a href="${context}report/connection.do?module=<%=ModuleConstants.Module_ID_COLOCATION%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span class="title">Connection Report</span>
                    </a>
                </li>
                <%} %>
                <%if(menuLoginDTO.getMenuPermission(PermissionConstants.REPORT_APPLICATION)!=-1) {
                %>
                <li class="nav-item" id="colocationApplicationReportMenu">
                    <a href="${context}report/application.do?module=<%=ModuleConstants.Module_ID_COLOCATION%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span class="title">Application Report</span>
                    </a>
                </li>
                <%} %>
            </ul>
            <%} %>
        </li>
    </ul>
</li>							
					