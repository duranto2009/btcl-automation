<%@page import="login.PermissionConstants"%>
<%@page import="common.ModuleConstants" %>
<li class="nav-item" id="domainMenu"><a href="javascript:;"
                                        class="nav-link nav-toggle"> <i class="fa fa-location-arrow"></i> <span
        span>Domain</span> <span class="arrow"></span>
</a>

    <ul class="sub-menu">
        <%
            if (menuLoginDTO.getMenuPermission(login.PermissionConstants.DOMAIN_CLIENT) != -1) {
        %>
        <li class="nav-item" id="domainClientMenu">

            <a href="javascript:;" class="nav-link nav-toggle">
                <i class="fa fa-user"></i> <span>Client</span> <span class="arrow"></span>
            </a>

            <ul class="sub-menu">
                <%
                    if (menuLoginDTO.getMenuPermission(login.PermissionConstants.DOMAIN_CLIENT_ADD)>= PermissionConstants.PERMISSION_FULL) {%>
                <li class="nav-item" id="addDomainClientMenu">
                    <a href="${context}Client/Add.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>" class="nav-link ">
                        <i class="fa fa-plus"></i><span>Add</span>
                    </a>
                </li>
                <%
                    }
                    if (menuLoginDTO.getMenuPermission(login.PermissionConstants.DOMAIN_CLIENT_SEARCH) != -1) {
                %>
                <li class="nav-item" id="searchDomainClientMenu">
                    <a href="${context}SearchClient.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>"
                       class="nav-link ">
                        <i class="fa fa-search"></i><span>Search</span>
                    </a>
                </li>
                <% } %>
            </ul>
            <% } %>
        </li>


        <li class="nav-item" id="domainAllMenu">

            <%
                if (menuLoginDTO.getMenuPermission(login.PermissionConstants.DOMAIN_MARKET) != -1) {
            %>
            <a href="javascript:;" class="nav-link nav-toggle">
                <i class="fa fa-globe"></i> <span>Domain-Market</span> <span class="arrow"></span>
            </a>

            <ul class="sub-menu">

                <%
                    if (menuLoginDTO.getMenuPermission(login.PermissionConstants.BUY_DOMAIN) != -1) {
                %>
                <li class="nav-item" id="buyDomainMenu">
                    <a href="${context}domain/domainQueryForBuy/domainQueryForBuy.jsp" class="nav-link ">
                        <i class="fa fa-shopping-bag"></i><span>Buy Domain</span>
                    </a>
                </li>

                <%
                    }
                    if (menuLoginDTO.getMenuPermission(login.PermissionConstants.BUY_DOMAIN_SEARCH) != -1) {
                %>
                <li class="nav-item" id="searchBuyDomainMenu">
                    <a href="${context}SearchDomain.do" class="nav-link ">
                        <i class="fa fa-search"></i><span>Search</span>
                    </a>
                </li>
                <% } %>
                
                <%
                    if (menuLoginDTO.getMenuPermission(login.PermissionConstants.DOMAIN_OWNERSHIP_CHANGE) != -1) {
                %>
                <li class="nav-item  " id="requestDomainOwnershipMenu"><a
                        href="${context}domain/ownership/requestForOwnership.jsp"
                        class="nav-link ">  <i class="fa  fa-exchange"> </i> <span
                        span>Ownership Change </span>
                </a></li>
				<%}%>
				 <%
                    if (menuLoginDTO.getMenuPermission(login.PermissionConstants.DOMAIN_TRANSFER) != -1) {
                %>
				 <li class="nav-item  " id="transferDomainMenu"><a
                        href="${context}domain/transfer/transferDomain.jsp"
                        class="nav-link ">  <i class="fa  fa-exchange"> </i> <span
                        span>Transfer Domain </span>
                </a></li>
                <%} %>
            </ul>
            <% } %>

        </li>
        
        
        <!-- dhrubo dns -->
        <li class="nav-item hidden" id="dnsManageMenu">

            <a href="javascript:;" class="nav-link nav-toggle">
                <i class="fa fa-globe"></i> <span>DNS Management</span> <span class="arrow"></span>
            </a>

            <ul class="sub-menu">

                <li class="nav-item" id="addDNSMenu">
                    <a href="${context}dnsdomain/add_dns/dnsAdd.jsp" class="nav-link ">
                        <i class="fa fa-shopping-bag"></i><span>Add DNS</span>
                    </a>
                </li>

                <li class="nav-item" id="searchDNSMenu">
                    <a href="${context}SearchDNS.do" class="nav-link ">
                        <i class="fa fa-search"></i><span>Search</span>
                    </a>
                </li>
            </ul>

        </li>
		<!-- /dhrubo dns -->
	

        <li class="nav-item" id="configDomainMenu">

            <%
                if (menuLoginDTO.getMenuPermission(login.PermissionConstants.CONFIGURATION) != -1) {
            %>

            <a href="javascript:;" class="nav-link nav-toggle">
                <i class="fa fa-gear"></i><span>Configuration</span> <span class="arrow"></span>
            </a>

            <ul class="sub-menu">

                <%
                    if (menuLoginDTO.getMenuPermission(login.PermissionConstants.PACKAGE_TYPE) != -1) {
                %>
                <li class="nav-item" id="packageTypeDomainMenu">

                    <a href="javascript:;" class="nav-link nav-toggle">
                        <i class="fa fa-briefcase"></i> <span>Package Type</span> <span
                            class="arrow"></span>
                    </a>
                    <ul class="sub-menu">

                        <%
                            if (menuLoginDTO.getMenuPermission(login.PermissionConstants.PACKAGE_TYPE_ADD) != -1) {
                        %>
                        <li class="nav-item" id="addPackageTypeDomainMenu">
                            <a href="${context}domain/package/addPackageType.jsp" class="nav-link ">
                                <i class="fa fa-plus"></i><span>Add</span>
                            </a>
                        </li>

                        <%
                            }
                            if (menuLoginDTO.getMenuPermission(login.PermissionConstants.PACKAGE_TYPE_SEARCH) != -1) {
                        %>
                        <li class="nav-item" id="packageTypeSearchDomainMenu">
                            <a href="${context}SearchPackageType.do" class="nav-link ">
                                <i class=" fa fa-search"></i> <span>Search</span>
                            </a>
                        </li>
                        <%} %>
                    </ul>
                    <%} %>
                </li>


                <li class="nav-item" id="packageDomainMenu">

                    <%
                        if (menuLoginDTO.getMenuPermission(login.PermissionConstants.PACKAGE) != -1) {
                    %>
                    <a href="javascript:;" class="nav-link nav-toggle">
                        <i class="fa fa-ticket"></i> <span>Package</span> <span class="arrow"></span>
                    </a>

                    <ul class="sub-menu">

                        <%
                            if (menuLoginDTO.getMenuPermission(login.PermissionConstants.PACKAGE_ADD) != -1) {
                        %>
                        <li class="nav-item" id="addPackageDomainMenu">
                            <a href="${context}domain/package/addDomainPackage.jsp" class="nav-link ">
                                <i class="fa fa-plus"></i><span>Add</span>
                            </a>
                        </li>

                        <%
                            }
                            if (menuLoginDTO.getMenuPermission(login.PermissionConstants.PACKAGE_SEARCH) != -1) {
                        %>
                        <li class="nav-item" id="searchPackageDomainMenu">
                            <a href="${context}SearchDomainPackage.do" class="nav-link ">
                                <i class="fa fa-search"></i><span>Search</span>
                            </a>
                        </li>

                        <%} %>
                    </ul>

                    <%} %>
                </li>


                <li class="nav-item" id="filterDomainMenu">

                    <%
                        if (menuLoginDTO.getMenuPermission(login.PermissionConstants.FILTER_DOMAIN) != -1) {
                    %>
                    <a href="javascript:;" class="nav-link nav-toggle">
                        <i class="fa fa-chain-broken"></i> <span>Filter Domain</span> <span
                            class="arrow"></span>
                    </a>

                    <ul class="sub-menu">

                        <%
                            if (menuLoginDTO.getMenuPermission(login.PermissionConstants.FILTER_DOMAIN_ADD) != -1) {
                        %>
                        <li class="nav-item" id="addClassifyDomainMenu">
                            <a href="${context}domain/newDomain/addDomain.jsp" class="nav-link ">
                                <i class="fa fa-plus"></i><span>Add</span>
                            </a>
                        </li>

                        <%
                            }
                            if (menuLoginDTO.getMenuPermission(login.PermissionConstants.FILTER_DOMAIN_SEARCH) != -1) {
                        %>
                        <li class="nav-item" id="searchClassifyDomainMenu">
                            <a href="${context}SearchDomainName.do" class="nav-link ">
                                <i class="fa fa-search"></i><span>Search</span>
                            </a>
                        </li>
                        <% } %>
                    </ul>
                    <% } %>
                </li>

                <li class="nav-item" id="forbiddenDomainnMenu">

                    <%
                        if (menuLoginDTO.getMenuPermission(login.PermissionConstants.FORBIDDEN_WORD) != -1) {
                    %>
                    <a href="javascript:;" class="nav-link nav-toggle">
                        <i class="fa fa-chain-broken"></i> <span>Forbidden Word</span> <span
                            class="arrow"></span>
                    </a>

                    <ul class="sub-menu">

                        <%
                            if (menuLoginDTO.getMenuPermission(login.PermissionConstants.FORBIDDEN_WORD_ADD) != -1) {
                        %>
                        <li class="nav-item" id="">
                            <a href="${context}domain/forbiddenDomain/addForbiddenWord.jsp" class="nav-link ">
                                <i class="fa fa-plus"></i><span>Add</span>
                            </a>
                        </li>

                        <%
                            }
                            if (menuLoginDTO.getMenuPermission(login.PermissionConstants.FORBIDDEN_WORD_SEARCH) != -1) {
                        %>
                        <li class="nav-item" id="">
                            <a href="${context}SearchForbiddenWord.do" class="nav-link ">
                                <i class="fa fa-search"></i><span>Search</span>
                            </a>
                        </li>
                        <% } %>
                    </ul>
                    <% } %>
                </li>

                <%
                    if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COMMON_CHARGE) != -1) {
                %>
                <li class="nav-item" id="commonChargeDomainMenu">
                    <a href="${context}DomainChargeConfig.do?mode=getdomainChargeConfig" class="nav-link nav-toggle">
                        <i class="fa fa-cogs"></i> <span>Common Charge</span>
                    </a>
                </li>
                <%} %>
            </ul>
            <%} %>
        </li>

        <li class="nav-item" id="domainBillAndPayment">

            <%
                if (menuLoginDTO.getMenuPermission(login.PermissionConstants.BILL) != -1) {
            %>
            <a href="javascript:;" class="nav-link nav-toggle">
                <i class="fa fa-money"></i> <span>Bill & Payment</span> <span class="arrow"></span>
            </a>

            <ul class="sub-menu">

                <%
                    if (menuLoginDTO.getMenuPermission(login.PermissionConstants.BILL_SEARCH) != -1) {
                %>
                <li class="nav-item" id="domainBillSearch">
                    <a href="${context}SearchBill.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>" class="nav-link ">
                        <i class="fa fa-search"></i><span> Bill Search</span>
                    </a>
                </li>

                <%
                    }
                    if (menuLoginDTO.getMenuPermission(login.PermissionConstants.BILL_BANK_PAYMENT) != -1) {
                %>
                <li class="nav-item" id="domainBankPayment">
                    <a href="${context}common/payment/bankPayment.jsp?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>"
                       class="nav-link ">
                        <i class="fa fa-money"></i><span>Bank Payment</span>
                    </a>
                </li>

                <%
                    }
                    if (menuLoginDTO.getMenuPermission(login.PermissionConstants.BILL_PAYMENT_SEARCH) != -1) {
                %>
                <li class="nav-item" id="domainPaymentSearch">
                    <a href="${context}SearchPayment.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>"
                       class="nav-link ">
                        <i class="fa fa-search"></i><span>Payment Search</span>
                    </a>
                </li>
                <%} %>
                 <li class="nav-item hidden" id="domainDisputeResolver">
                    <a href="${context}DomainDisputeResolver.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>"
                       class="nav-link ">
                        <i class="fa fa-search"></i><span>Dispute Resolver</span>
                    </a>
                </li>
                 <li class="nav-item" id="manualDemandNote">
                    <a href="${context}domain/NIXDemandNote/manualDemandNote.jsp?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>"
                       class="nav-link ">
                        <i class="fa fa-search"></i><span>Manual Demand Note </span>
                    </a>
                </li>
            </ul>
            <%} %>
        </li>

        <li id="domRequestSubMenu" class="nav-item">

            <%
                if (menuLoginDTO.getMenuPermission(login.PermissionConstants.REQUEST) != -1) {
            %>
            <a href="javascript:;" class="nav-link nav-toggle">
                <i class="fa fa-envelope-o"></i> <span>Request</span> <span class="arrow"></span>
            </a>
            <ul class="sub-menu">

                <%
                    if (menuLoginDTO.getMenuPermission(login.PermissionConstants.REQUEST_SEARCH) != -1) {
                %>
                <li class="nav-item" id="domSearchRequestSubMenu">
                    <a href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>"
                       class="nav-link ">
                        <i class="fa fa-search"></i><span>Search </span>
                    </a>
                </li>
                <%} %>
            </ul>
            <%} %>
        </li>

        <%
            if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COMPLAIN) != -1) {
        %>

        <li class="nav-item  " id="complainMenu<%=ModuleConstants.Module_ID_DOMAIN%>">

            <a href="javascript:;" class="nav-link nav-toggle">
                <i class="fa fa-bug"></i> <span>Complain</span> <span class="arrow"></span>
            </a>
            <ul class="sub-menu">
                <%
                    if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COMPLAIN_ADD) != -1 && false) {
                %>
                <li class="nav-item  " id="complainAddMenu<%=ModuleConstants.Module_ID_DOMAIN%>">
                    <a href="${context}complain/addComplain.jsp?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>"
                       class="nav-link ">
                        <i class="fa fa-plus"></i> <span>Add </span>
                    </a>
                </li>
                <%
                    }
                    if (menuLoginDTO.getMenuPermission(login.PermissionConstants.COMPLAIN_SEARCH) != -1) {
                %>
                <li class="nav-item  " id="complainSearchMenu<%=ModuleConstants.Module_ID_DOMAIN%>">
                    <a href="${context}ComplainSearch.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>"
                       class="nav-link ">
                        <i class="fa fa-search"></i> <span>Search </span>
                    </a>
                </li>
                <% } %>
                <li class="nav-item" id="summary-report">
                    <a href="${context}domain/report/payment/summary.do"
                       class="nav-link">
                        <i class="fa fa-book"></i> <span>Monthly Payment Summary</span>
                    </a>
                </li>
            </ul>
        </li>
        <% } %>
        <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.GENERAL_REPORT_DOMAIN ) != -1) {%>
        <li class="nav-item  " id="domainReportMenu">
            <a href="javascript:;" class="nav-link nav-toggle">
                <i class="fa fa-file"></i> <span>Report</span> <span class="arrow"></span>
            </a>
            <ul class="sub-menu">
            <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_MODULE_DOMAIN  ) != -1) {%>
                <li class="nav-item" id="domainReportNewMenu">
                    <a href="${context}Report/GetNewReport.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span>Domain Report</span>
                    </a>
                </li>
                <%} %>
                <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_CLIENT_DOMAIN  ) != -1) {%>
                  <li class="nav-item" id="domainClientReportMenu">
                    <a href="${context}Report/GetClientReport.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span>Client Report</span>
                    </a>
                </li>
                <%} %>
                <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_BILL_DOMAIN  ) != -1) {%>
                 <li class="nav-item" id="domainBillReportMenu">
                    <a href="${context}Report/GetBillReport.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span>Bill Report</span>
                    </a>
                </li>
                <%} %>
                <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_PAYMENT_DOMAIN  ) != -1) {%>
                <li class="nav-item" id="domainPaymentReportMenu">
                    <a href="${context}PaymentReport/GetNewPaymentReport.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span>Payment Report</span>
                    </a>
                </li>
                <%} %>
                <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_SEARCH_LOG_DOMAIN) != -1) {%>
                <li class="nav-item" id="domainSearchLogReportMenu">
                    <a href="${context}DomainSearchLog/SearchLog.do"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span>Domain Search Log</span>
                    </a>
                </li>
                <%} %>
<%--                 <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_SEARCH_LOG_DOMAIN) != -1) {%> --%>
                
<%--                 <%} %> --%>
            </ul>
        </li>
        <%} %>
    </ul>
</li>