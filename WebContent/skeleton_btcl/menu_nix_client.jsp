<%@page import="common.EntityTypeConstant" %>
<%@page import="common.ModuleConstants" %>
<%@page import="nix.constants.NixStateConstants" %>

<%if (!registeredNix) {%>
<li class="nav-item">
    <a href="${context}Client/Add.do?moduleID=<%=ModuleConstants.Module_ID_NIX %>"
       class="nav-link ">
        <i class="fa fa-plus"></i>
        <span class="title">NIX</span>
    </a>
</li>
<%} else {%>
<%if (statusClientNix == NixStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED) {%>
<li class="nav-item">
    <a href="${context}GetClientForEdit.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_NIX%>&edit"
       class="nav-link nav-toggle">
        <i class="fa fa-chain"></i>
        <span class="title">NIX</span>
        <span class="fa fa-exclamation-circle" style="color: red"></span>
    </a>
</li>
<%} else if (activationStatusNix != EntityTypeConstant.STATUS_ACTIVE) {%>
<li class="nav-item  ">
    <a href="javascript:;"
       class="nav-link nav-toggle">
        <i class="fa fa-chain"></i>
        <span class="title">NIX</span>
        <span class="arrow"></span>
    </a>
    <ul class="sub-menu">
        <li class="nav-item">
            <a href="${context}GetClientForView.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_NIX %>"
               class="nav-link nav-toggle">
                <i class="fa fa-user"></i>
                <span class="title">Profile</span>
                <span class="arrow"></span>
            </a>
        </li>

    </ul>
</li>
<% } else if (activationStatusNix == EntityTypeConstant.STATUS_ACTIVE) {%>
<li class="nav-item  ">
    <a href="javascript:;"
       class="nav-link nav-toggle">
        <i class="fa fa-chain"></i>
        <span class="title">NIX</span>
        <span class="arrow"></span>
    </a>
    <ul class="sub-menu">
        <li class="nav-item">
            <a href="${context}GetClientForView.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_NIX %>"
               class="nav-link nav-toggle">
                <i class="fa fa-user"></i>
                <span class="title">Profile</span>

            </a>
        </li>

        <!-- Add Necessary List Items according to module HERE. Thanks. -->
        <!-- 	Connection Begins 	-->
        <li class="nav-item"
            id="nix-connection">
            <a href="javascript:;"
               class="nav-link nav-toggle">
                <i class="fa fa-external-link"></i>
                <span>Connection</span>
                <span class="arrow"></span>
            </a>

            <ul class="sub-menu">
                <!-- New Connection Management -->
                <!-- 1 -->
                <li class="nav-item"
                    id="lli-application-new-connection">
                    <a href="${context}nix/application/new-connection.do"
                       class="nav-link ">
                        <i class="fa fa-plus"></i>
                        <span>New Connection</span>
                    </a>
                </li>
                <!-- New Connection Management -->
                <!-- Existing Connection Management -->
                <li class="nav-item"
                    id="nix-application-existing-connection">
                    <a href="javascript:;"
                       class="nav-link nav-toggle">
                        <i class="fa fa-external-link"></i>
                        <span>Manage Existing Connection</span>
                        <span class="arrow"></span>
                    </a>

                    <ul class="sub-menu">

                        <!-- Revise Connection -->
                        <li class="nav-item"
                            id="nix-application-revise-connection">
                            <a href="javascript:;"
                               class="nav-link nav-toggle">
                                <i class="fa fa-external-link"></i>
                                <span>Revise Connection</span>
                                <span class="arrow"></span>
                            </a>
                            <ul class="sub-menu">
                                <!-- 2 -->
                                <li class="nav-item"
                                    id="nix-application-upgrade-connection">
                                    <a href="${context}nix/application/upgrade-port.do"
                                       class="nav-link ">
                                        <i class="fa fa-plus"></i>
                                        <span>Upgrade</span>
                                    </a>
                                </li>
                                <!-- 3 -->
                                <li class="nav-item"
                                    id="nix-application-downgrade-connection">
                                    <a href="${context}nix/application/downgrade-port.do"
                                       class="nav-link ">
                                        <i class="fa fa-plus"></i>
                                        <span>Downgrade</span>
                                    </a>
                                </li>
                                <!-- 4 -->
                                <li class="nav-item"
                                    id="lli-application-close">
                                    <a href="${context}nix/application/close-port.do"
                                       class="nav-link ">
                                        <i class="fa fa-plus"></i>
                                        <span>Close</span>
                                    </a>
                                </li>

                            </ul>
                        </li>
                        <!-- Revise Connection -->
                    </ul>
                </li>
                <!-- Existing Connection Management -->
                <!-- TD & reconnect Management -->
                <li class="nav-item"
                    id="nix-td-reconnect-connection">
                    <a href="javascript:;"
                       class="nav-link nav-toggle">
                        <i class="fa fa-external-link"></i>
                        <span>Connected Client Management</span>
                        <span class="arrow"></span>
                    </a>
                    <ul class="sub-menu">
                        <li class="nav-item"
                            id="nix-td-list-search">
                            <a href="${context}nix/revise/search.do"
                               class="nav-link ">
                                <i class="fa fa-search"></i>
                                <span>Application List</span>
                            </a>
                        </li>
                        <li class="nav-item"
                            id="nix-td-client-search">
                            <a href="${context}nix/revise/searchprobable.do"
                               class="nav-link ">
                                <i class="fa fa-search"></i>
                                <span>Probable TD Clients</span>
                            </a>
                        </li>


                        <li class="nav-item"
                            id="reconnect">
                            <a href="${context}nix/application/reconnect.do"
                               class="nav-link ">
                                <i class="fa  fa-plus"></i>
                                <span>Apply for Reconnection</span>
                            </a>
                        </li>
                    </ul>
                </li>
                <!-- TD & reconnect Management -->
                <li class="nav-item"
                    id="lli-application-search">
                    <a href="${context}nix/application/search.do"
                       class="nav-link ">
                        <i class="fa  fa-search"></i>
                        <span>Search Application</span>
                    </a>
                </li>
            </ul>
        </li>
        <!-- 	Connection Ends 	-->
        <%--Monthly Bill starts--%>

        <li class="nav-item"
            id="nix-monthly-bill">
            <a href="javascript:;"
               class="nav-link nav-toggle">
                <i class="fa fa-external-link"></i>
                <span>Monthly Bill</span>
                <span class="arrow"></span>
            </a>

            <ul class="sub-menu">

                <li class="nav-item"
                    id="nix-monthly-bill-search">
                    <a href="${context}nix/monthly-bill/searchPage.do"
                       class="nav-link ">
                        <i class="fa fa-search"></i>
                        <span>Search</span>
                    </a>
                </li>

            </ul>

        </li>
        <li class="nav-item" id="common">
            <a href="javascript:;"
               class="nav-link nav-toggle">
                <i class="fa fa-money"></i>
                <span class="title">Bill & Payment</span>
                <span class="arrow"></span>
            </a>
            <ul class="sub-menu">
                <li class="nav-item" id="searchBill">
                    <a href="${context}bill/search.do?moduleID=<%=ModuleConstants.Module_ID_NIX%>"
                       class="nav-link ">
                        <i class="fa fa-search"></i>
                        <span class="title"> bill Search</span>
                    </a>
                </li>
                <%--<li class="nav-item"--%>
                    <%--id="commonBankPayment">--%>
                    <%--<a href="${context}common/payment/bankPayment.jsp?moduleID=<%=ModuleConstants.Module_ID_NIX %>"--%>
                       <%--class="nav-link ">--%>
                        <%--<i class="fa fa-search"></i>--%>
                        <%--<span class="title">Bank Payment</span>--%>
                    <%--</a>--%>
                <%--</li>--%>
            </ul>
        </li>
    </ul>
</li>
<%}
}%>