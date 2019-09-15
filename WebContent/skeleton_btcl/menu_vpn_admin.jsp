<%@page import="login.PermissionConstants"%>
<%@page import="common.ModuleConstants"%>

<li class="nav-item" id="vpnMenu">

    <a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-shield"></i> <span class="title">VPN</span> <span class="arrow"></span></a>

    <ul class="sub-menu">


        <!-- 	CLIENT    -->
        <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_CLIENT) != -1) {%>
        <li class="nav-item" id="vpnClient">
            <a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-user"></i><span class="title">Client</span> <span class="arrow"></span></a>

            <ul class="sub-menu">
                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_CLIENT_ADD) >= PermissionConstants.PERMISSION_FULL) {%>
                <li class="nav-item" id="addVpnClient">
                    <a href="${context}Client/Add.do?moduleID=<%=ModuleConstants.Module_ID_VPN %>" class="nav-link "><i class="fa fa-plus"></i><span class="title">Add</span></a>
                </li>
                <%}%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_CLIENT_SEARCH) != -1) {%>
                <li class="nav-item" id="searchVpnClient">
                    <a href="${context}SearchClient.do?moduleID=<%=ModuleConstants.Module_ID_VPN %>" class="nav-link "> <i class="fa fa-search"></i><span class="title">Search</span></a>
                </li>
                <%}%>

            </ul>
        </li>
        <%}%>


        <!-- 	LINK    -->
        <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_LINK) != -1) {%>
        <li class="nav-item" id="vpnLink">
            <a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-external-link"></i>
                <span class="title">Link</span> <span class="arrow"></span>
            </a>

            <ul class="sub-menu">
                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_LINK_ADD) != -1) {%>
                <li class="nav-item" id="addVpnLink">
                    <a href="${context}vpn/link/add.do" class="nav-link"><i class="fa fa-plus"></i><span class="title">Add</span></a>
                </li>
                <%}%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_LINK_SEARCH) != -1) {%>
                <li class="nav-item" id="searchVpnLink" >
                    <a href="${context}vpn/link/search.do" class="nav-link"> <i class="fa fa-search"></i><span class="title">Search</span></a>
                </li>
                <%}%>

                <li class="nav-item" id="searchVpnNetworkLink" >
                    <a href="${context}vpn/network/search.do" class="nav-link"> <i class="fa fa-search"></i><span class="title">Link Search</span></a>
                </li>
                <%--<%}%>--%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_LINK_BANDWIDTH_CHANGE) != -1) {%>
                <li class="nav-item" id="updateBandwidth">
                    <a href="${context}vpn/network/bandwidth-change.do" class="nav-link "> <i class="fa  fa-edit"></i><span class="title">Bandwidth Change</span></a>
                </li>
                <%}%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_LINK_SHIFT) != -1) {%>
                <li class="nav-item" id="shiftVpnLink" >
                    <a href="${context}vpn/link/shift.do" class="nav-link "> <i class="fa fa-search"></i><span class="title">Shift Link</span></a>
                </li>
                <%}%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_LINK_TDLINK) != -1) {%>
                <li class="nav-item" id="VpnTdClose">
                    <a href="${context}vpn/td/search.do" class="nav-link "><i class="fa  fa-exclamation"></i><span class="title"> TD Link</span></a>
                </li>
                <%}%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_LINK_TDLINK) != -1) {%>
                <li class="nav-item" id="VpnEnable">
                    <a href="${context}vpn/reconnect/application.do" class="nav-link "><i class="fa  fa-exclamation"></i><span class="title"> Enable Link</span></a>
                </li>
                <%}%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_LINK_CLOSE) != -1) {%>
                <li class="nav-item" id="VpnLinkClose">
                    <a href="${context}vpn/close/application.do" class="nav-link "> <i class="fa  fa-ban"></i><span class="title">Close Link</span></a>
                </li>
                <%}%>
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.VPN_LINK_OWNER_CHANGE) != -1) {%>
                <li class="nav-item" id="VpnLinkOwnerChange">
                    <a href="${context}vpn/ownerchange/application.do" class="nav-link "> <i class="fa fa-exchange"></i><span class="title">Owner Change</span></a>
                </li>
                <%}%>
                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_LINK_BALANCE_TRANSFER) != -1) {%>
                <li class="nav-item" id="vpnBalanceTransfer">
                    <a href="${context}VpnBalanceTransfer.do" class="nav-link "> <i	class="fa fa-exchange"></i><span class="title">Transfer Balance</span></a>
                </li>
                <%}%>
            </ul>
        </li>
        <%}%>



        <!-- 	BILL AND PAYMENT    -->
        <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_BILL) != -1){%>
        <li class="nav-item" id="vpnBillAndPayment">
            <a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-money"></i> <span class="title">Bill & Payment</span> <span class="arrow"></span></a>

            <ul class="sub-menu">
                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_BILL_SEARCH) != -1){%>
                <li class="nav-item" id="vpnBillSearch">
                    <a href="${context}bill/search.do?moduleID=<%=ModuleConstants.Module_ID_VPN%>" class="nav-link "> <i	class="fa fa-search"></i><span class="title"> Bill Search</span></a>
                </li>
                <%}%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_BILL_PAYMENT_SEARCH) != -1){ %>
                <li class="nav-item" id="vpnPaymentSearch">
                    <a href="${context}payment/search.do?moduleID=<%=ModuleConstants.Module_ID_VPN %>" class="nav-link "> <i	class="fa fa-search"></i><span class="title">Payment Search</span></a>
                </li>
                <%}%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_BILL_BANK_ADD) != -1){ %>
                <li class="nav-item" id="addBankVPN">
                    <a href="${context}Bank/AddNewBank.do?moduleID=<%=ModuleConstants.Module_ID_VPN%>" class="nav-link "> <i class="fa fa-plus"></i><span class="title">Add Bank</span></a>
                </li>
                <%}%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_BILL_BANK_SEARCH) != -1) {%>
                <li class="nav-item" id="searchBankVPN">
                    <a href="${context}BankSearch/Banks.do?moduleID=<%=ModuleConstants.Module_ID_VPN%>" class="nav-link "> <i class="fa fa-search"></i><span class="title">Bank Search</span></a>
                </li>
                <%}%>
                <li class="nav-item" id="billConfigurationVPN">
                    <a href="${context}BillConfiguration.do?moduleID=<%=ModuleConstants.Module_ID_VPN%>" class="nav-link "> <i class="fa fa-plus"></i><span class="title">Add Bill Configuration</span></a>
                </li>
                <li class="nav-item" id="splitBillVPN">
                    <a href="${context}SplitBill.do?method=splitBill&moduleID=<%=ModuleConstants.Module_ID_VPN%>" class="nav-link "> <i class="fa fa-plus"></i><span class="title">Split Bill</span></a>
                </li>
            </ul>
        </li>
        <%}%>



        <!-- 	INVENTORY    -->
        <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_INVENTORY) != -1) {%>
        <li class="nav-item" id="inventorySubmenu">
            <a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-bank"></i> <span class="title">Inventory</span> <span class="arrow"></span></a>

            <ul class="sub-menu">
                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_INVENTORY_ADD) != -1) {%>
                <li class="nav-item" id="addInventorySubmenu">
                    <a href="${context}inventory/add.do?moduleID=<%=ModuleConstants.Module_ID_VPN %>" class="nav-link ">
                        <i class="fa fa-plus"></i><span class="title">Add</span></a>
                </li>
                <%}%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_INVENTORY_SEARCH) != -1) {%>
                <li class="nav-item " id="searchInventorySubmenu">
                    <a href="${context}inventory/search.do?moduleID=<%=ModuleConstants.Module_ID_VPN %>" class="nav-link ">
                        <i class="fa fa-search"></i> <span class="title">Search</span></a>
                </li>
                <%}%>
            </ul>
        </li>
        <%}%>



        <!-- 	CONFIGURATION    -->
        <%if(menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_CONFIGURATION) != -1){%>
        <li class="nav-item" id="vpnConfigurationSubmenu1">
            <a href="javascript:;" class="nav-link nav-toggle"><i class="fa fa-gear"></i> <span class="title">Configuration</span><span class="arrow"></span></a>

            <ul class="sub-menu">
                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_CONFIGURATION_INVENTORY) != -1) {%>
                <li class="nav-item  " id="editInventorySubmenu">
                    <a href="${context}vpn/inventory/editCategory.jsp" class="nav-link "><i class="fa fa-gg"></i> <span class="title">Inventory</span> </a>
                </li>
                <%}%>


                <li class="nav-item " id="vpnCostConfigSubmenu2">
                    <a href="javascript:;" class="nav-link nav-toggle"><i class="fa fa-gear"></i> <span class="title">Cost</span><span class="arrow"></span></a>

                    <ul class="sub-menu">
                        <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_CONFIGURATION_COST) != -1) {%>
                        <li class="nav-item  " id="vpnCostConfigMenu">
                            <a href="${context}GetCostConfig.do?moduleID=<%=ModuleConstants.Module_ID_VPN %>" class="nav-link "> <i class="fa fa-money"></i><span class="title"> Dist-BW Cost</span></a>
                        </li>
                        <%} %>
                        <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_CONFIGURATION_BILLING) != -1){%>
                        <li class="nav-item  " id="vpnCommonChargeMenu">
                            <a href="${context}GetCommonChargeConfig.do" class="nav-link "><i class="fa fa-cogs"></i><span class="title"> Common Costs </span></a>
                        </li>
                        <%} %>
                        <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_CONFIGURATION_OFC_INSTALL_COST) != -1) {%>
                        <li class="nav-item  " id="vpnOFCInstallationChargeMenu">
                            <a href="${context}DistrictOfcInstallation/UpdateDistrictOfcInstallationCost.do?module=6" class="nav-link "><i class="fa fa-map-marker"></i><span class="title"> OFC Installation Cost</span></a>
                        </li>
                        <%} %>
                    </ul>
                </li>
                <%--
                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_CONFIGURATION_BILLING) != -1){%>
                <li class="nav-item  " id="commonConfigSubmenu2">
                    <a href="${context}GetCommonChargeConfig.do" class="nav-link "><i class="fa fa-cogs"></i><span class="title">Billing </span></a>
                </li>
                <%}%>
                 --%>
                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_CONFIGURATION_DISTANCE) != -1) {%>
                <li class="nav-item" id="vpnDistanceConfigSubmenu2">
                    <a href="javascript:;" class="nav-link nav-toggle"><i class="fa fa-bank"></i><span class="title">Distance</span><span class="arrow"></span></a>
                    <ul class="sub-menu">
                        <li class="nav-item" id="districtDistanceSubmenu2">
                            <a href="${context}Distance/UpdateDistrictDistance.do" class="nav-link "><i class="fa fa-map-marker"></i> <span class="title">District Distance</span></a>
                        </li>
                        <li class="nav-item  " id="upazilaDistanceSubmenu2">
                            <a href="${context}Distance/UpdateUpazilaDistance.do" class="nav-link "><i class="fa fa-map-marker"></i> <span class="title">Upazila Distance</span></a>
                        </li>
                        <li class="nav-item  " id="unionDistanceSubmenu2">
                            <a href="${context}Distance/UpdateUnionDistance.do" class="nav-link "><i class="fa fa-map-marker"></i> <span class="title">Union Distance</span></a>
                        </li>
                    </ul>
                </li>
                <%}%>

                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_CONFIGURATION_OFC_INSTALL_COST) != -1) {%>
                <li class="nav-item" id="districtOfcInstallationCostSubmenu2">
                    <a href="${context}DistrictOfcInstallation/UpdateDistrictOfcInstallationCost.do" class="nav-link "><i class="fa fa-map-marker"></i><span class="title">OFC Installation Cost</span></a>
                </li>
                <%}%>

            </ul>
        </li>
        <%}%>

        <%--MONTHLY BILL --%>

        <!-- Monthly Bill -->
        <%if (menuLoginDTO.getMenuPermission(PermissionConstants.VPN_MONTHLY_BILL) != -1) {%>
        <li class="nav-item"
            id="vpn-monthly-bill">
            <a href="javascript:;"
               class="nav-link nav-toggle">
                <i class="fa fa-external-link"></i>
                <span>Monthly Bill</span>
                <span class="arrow"></span>
            </a>
            <ul class="sub-menu">
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.VPN_MONTHLY_BILL_SEARCH) != -1) {%>
                <li class="nav-item"
                    id="vpn-monthly-bill-search">
                    <a href="${context}vpn/monthly-bill/searchPage.do"
                       class="nav-link ">
                        <i class="fa fa-search"></i>
                        <span>Search</span>
                    </a>
                </li>
                <%}%>
            </ul>
        </li>
        <%}%>
        <!-- Monthly Bill Ends -->


        <!--Monthly usage  -->
        <%if (menuLoginDTO.getMenuPermission(PermissionConstants.VPN_MONTHLY_BILL_USAGE) != -1) {%>
        <li class="nav-item"
            id="vpn-monthly-usage">
            <a href="javascript:;"
               class="nav-link nav-toggle">
                <i class="fa fa-external-link"></i>
                <span>Monthly Usage</span>
                <span class="arrow"></span>
            </a>
            <ul class="sub-menu">
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.VPN_MONTHLY_BILL_USAGE_SEARCH) != -1) {%>
                <li class="nav-item"
                    id="vpn-monthly-usage-search">
                    <a href="${context}vpn/monthly-usage/search.do"
                       class="nav-link ">
                        <i class="fa fa-search"></i>
                        <span>Search</span>
                    </a>
                </li>
                <%}%>
            </ul>
        </li>
        <%}%>
        <!-- Monthly Usage Ends -->

        <!--Monthly Bill Summary  -->
        <%if (menuLoginDTO.getMenuPermission(PermissionConstants.VPN_MONTHLY_BILL_SUMMARY) != -1) {%>
        <li class="nav-item"
            id="vpn-monthly-Bill-summary">
            <a href="javascript:;"
               class="nav-link nav-toggle">
                <i class="fa fa-external-link"></i>
                <span>Monthly Bill Summary</span>
                <span class="arrow"></span>
            </a>

            <ul class="sub-menu">
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.VPN_MONTHLY_BILL_SUMMARY_SEARCH) != -1) {%>
                <li class="nav-item"
                    id="vpn-monthly-bill-summary-search">
                    <a href="${context}vpn/monthly-bill-summary/searchPage.do"
                       class="nav-link ">
                        <i class="fa fa-search"></i>
                        <span>Search</span>
                    </a>
                </li>
                <%}%>
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.VPN_MONTHLY_BILL_SUMMARY_CHECK) != -1) {%>
                <li class="nav-item"
                    id="vpn-monthly-bill-check">
                    <a href="${context}vpn/monthly-bill/check.do"
                       class="nav-link ">
                        <i class="fa  fa-question"></i>
                        <span>Check</span>
                    </a>
                </li>
                <%}%>
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.VPN_MONTHLY_BILL_SUMMARY_GENERATE) != -1) {%>
                <li class="nav-item"
                    id="vpn-monthly-bill-generate">
                    <a href="${context}vpn/monthly-bill/goGenerate.do"
                       class="nav-link ">
                        <i class="fa fa-plus"></i>
                        <span>Generate</span>
                    </a>
                </li>
                <%}%>
            </ul>
        </li>
        <%}%>
        <!-- Monthly Bill Summary Ends -->

        <!--Monthly Outsource Bill  -->
        <%if (menuLoginDTO.getMenuPermission(PermissionConstants.VPN_MONTHLY_OUTSOURCE_BILL) != -1) {%>
        <li class="nav-item"
            id="vpn-monthly-outsource-Bill">
            <a href="javascript:;"
               class="nav-link nav-toggle">
                <i class="fa fa-external-link"></i>
                <span>Outsource Bill</span>
                <span class="arrow"></span>
            </a>
            <ul class="sub-menu">
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.VPN_MONTHLY_OUTSOURCE_BILL_SEARCH) != -1) {%>
                <li class="nav-item"
                    id="vpn-monthly-outsource-Bill-search">
                    <a href="${context}vpn/monthly-outsource-bill/search.do"
                       class="nav-link ">
                        <i class="fa fa-search"></i>
                        <span>Search</span>
                    </a>
                </li>
                <%}%>
            </ul>
        </li>
        <%}%>
        <!-- Monthly outsouce bill -->






        <%--<!-- 	REQUEST    -->--%>
        <%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_REQUEST) != -1) {%>--%>
        <%--<li class="nav-item" id="vpnRequestSubMenu">--%>
            <%--<a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Request</span> <span class="arrow"></span></a>--%>

            <%--<ul class="sub-menu">--%>

                <%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_REQUEST_SEARCH) != -1) {%>--%>
                <%--<li class="nav-item" id="vpnSearchRequestSubMenu">--%>
                    <%--<a href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_VPN %>" class="nav-link "><i class="fa fa-search"></i><span class="title">Search </span></a>--%>
                <%--</li>--%>
                <%--<%}%>--%>

            <%--</ul>--%>
        <%--</li>--%>
        <%--<%}%>--%>

        <%--<!-- 	NOTE    -->--%>
        <%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_NOTE) != -1) {%>--%>
        <%--<li class="nav-item" id="vpnNoteSubMenu">--%>
            <%--<a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Note</span> <span class="arrow"></span></a>--%>

            <%--<ul class="sub-menu">--%>

                <%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_NOTE_SEARCH) != -1) {%>--%>
                <%--<li class="nav-item" id="vpnSearchNoteSubMenu">--%>
                    <%--<a href="${context}SearchNote.do?moduleID=<%=ModuleConstants.Module_ID_VPN %>" class="nav-link "><i class="fa fa-search"></i><span class="title">Search </span></a>--%>
                <%--</li>--%>
                <%--<%}%>--%>

            <%--</ul>--%>
        <%--</li>--%>
        <%--<%}%>--%>



        <%--<!-- 	MIGRATION    -->--%>
        <%--<%if(migrationEnabled && menuLoginDTO.getMenuPermission( login.PermissionConstants.VPN_MIGRATION ) != -1) {%>--%>
        <%--<li class="nav-item" id="vpnMigration">--%>
            <%--<a href="javascript:;" class="nav-link nav-toggle"><i class="fa fa-external-link"></i><span class="title">Migration</span><span class="arrow"></span></a>--%>

            <%--<ul class="sub-menu">--%>
                <%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_LINK_UPDATE_MIGRATED_VPN) != -1) {%>--%>
                <%--<li class="nav-item" id="vpnLinkMigration" >--%>
                    <%--<a href="${context}vpn/link/updateMigratedVPN.jsp" class="nav-link "> <i class="fa fa-plus"></i><span	class="title">Link Migration</span></a>--%>
                <%--</li>--%>
                <%--<%}%>--%>
                <%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_BILL_MIGRATION) != -1){%>--%>
                <%--<li class="nav-item" id="vpnBillMigration">--%>
                    <%--<a href="${context}vpn/link/generateDemandNoteMigration.jsp" class="nav-link "><i class="fa fa-money"></i><span class="title">Bill Migration</span></a>--%>
                <%--</li>--%>
                <%--<%}%>--%>

                <%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_CLIENT_MIGRATION) != -1) {%>--%>
                <%--<li class="nav-item" id="vpnClientMigration">--%>
                    <%--<a href="${context}vpn/client/migratedClientUpdate.jsp" class="nav-link "> <i class="fa fa-search"></i><span class="title">Client Migration</span></a>--%>
                <%--</li>--%>
                <%--<%}%>--%>

            <%--</ul>--%>
        <%--</li>--%>
        <%--<%}%>--%>
        <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.GENERAL_REPORT_VPN ) != -1) {%>
        <li class="nav-item  " id="vpnReportMenu">
            <a href="javascript:;" class="nav-link nav-toggle">
                <i class="fa fa-file"></i> <span class="title">Report</span> <span class="arrow"></span>
            </a>
            <ul class="sub-menu">
               <%-- <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_MODULE_VPN ) != -1) {%>
                <li class="nav-item" id="vpnReportNewMenu">
                    <a href="${context}Report/GetNewReport.do?moduleID=<%=ModuleConstants.Module_ID_VPN%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span class="title">VPN Report</span>
                    </a>
                </li>
                <%} %>--%>
                <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_CLIENT_VPN) != -1) {%>
                <li class="nav-item" id="vpnClientReportMenu">
                    <a href="${context}Report/GetClientReport.do?moduleID=<%=ModuleConstants.Module_ID_VPN%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span class="title">Client Report</span>
                    </a>
                </li>
                <%} %>
                <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_BILL_VPN ) != -1) {%>
                <li class="nav-item" id="vpnBillReportMenu">
                    <a href="${context}Report/GetBillReport.do?moduleID=<%=ModuleConstants.Module_ID_VPN%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span class="title">Bill Report</span>
                    </a>
                </li>
                <%} %>
                <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_PAYMENT_VPN ) != -1) {%>
                <li class="nav-item" id="vpnPaymentReportMenu">
                    <a href="${context}PaymentReport/GetNewPaymentReport.do?moduleID=<%=ModuleConstants.Module_ID_VPN%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span class="title">Payment Report</span>
                    </a>
                </li>
                <%} %>

                <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_CONNECTION_VPN ) != -1) {%>

                <li class="nav-item" id="vpnReportMenuNetworkLink">
                    <a href="${context}report/connection.do?module=<%=ModuleConstants.Module_ID_VPN%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span class="title">Link Report</span>
                    </a>
                </li>
                <%} %>

                <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_APPLICATION_VPN ) != -1) {%>

                <li class="nav-item" id="vpnReportMenuApplication">
                    <a href="${context}report/application.do?module=<%=ModuleConstants.Module_ID_VPN%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span class="title">Application Report</span>
                    </a>
                </li>
                <%} %>


            </ul>
        </li>
      <%} %>


    </ul>
</li>