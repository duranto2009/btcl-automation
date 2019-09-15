<%@page import="login.PermissionConstants"%>
<%@page import="common.ModuleConstants"%>

<li class="nav-item" id="nixMenu">
	<a href="javascript:;" class="nav-link nav-toggle">
		<i class="fa fa-random"></i> <span class="title">NIX</span> <span class="arrow"></span>
	</a>
	<ul class="sub-menu">

		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.NIX_CLIENT) != -1) {%>
			<li class="nav-item" id="nixClientMenu">
				<a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-user"></i>
					<span class="title">Client</span> <span class="arrow"></span>
				</a>
				<ul class="sub-menu">

					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.NIX_CLIENT_ADD) >= PermissionConstants.PERMISSION_FULL) {%>
						<li class="nav-item" id="addNixClientMenu">
							<a href="${context}Client/Add.do?moduleID=<%=ModuleConstants.Module_ID_NIX %>" class="nav-link ">
								<i class="fa fa-plus"></i><span class="title">Add</span>
							</a>
						</li>
					<%}%>

					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.NIX_CLIENT_SEARCH) != -1) {%>
					<li class="nav-item" id="searchNixClientMenu">
						<a href="${context}SearchClient.do?moduleID=<%=ModuleConstants.Module_ID_NIX %>"	class="nav-link ">
							<i class="fa fa-search"></i><span class="title">Search</span>
						</a>
					</li>
					<%}	%>

				</ul>
			</li>
		<%}%>

		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.NIX_CONNECTION_MANAGEMENT) != -1) {%>
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
				<%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_NEW_CONNECTION_APPLICATION) != -1) {%>

				<li class="nav-item"
					id="nix-application-new-connection">
					<a href="${context}nix/application/new-connection.do"
					   class="nav-link ">
						<i class="fa fa-plus"></i>
						<span>New Connection</span>
					</a>
				</li>
				<%}%>

				<%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_EXISTING_CONNECTION_MANAGEMENT) != -1) {%>

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
							<%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_REVISE_CONNECTION_MANAGEMENT) != -1) {%>

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
									<%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_UPGRADE_APPLICATION) != -1) {%>

									<li class="nav-item"
										id="nix-application-upgrade-connection">
										<a href="${context}nix/application/upgrade-port.do"
										   class="nav-link ">
											<i class="fa fa-plus"></i>
											<span>Upgrade</span>
										</a>
									</li>
									<%}%>
									<%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_DOWNGRADE_APPLICATION) != -1) {%>
									<!-- 3 -->
									<li class="nav-item"
										id="nix-application-downgrade-connection">
										<a href="${context}nix/application/downgrade-port.do"
										   class="nav-link ">
											<i class="fa fa-plus"></i>
											<span>Downgrade</span>
										</a>
									</li>
									<%}%>

									<%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_CLOSE_APPLICATION) != -1) {%>
									<!-- 4 -->
									<li class="nav-item"
										id="nix-application-close">
										<a href="${context}nix/application/close-port.do"
										   class="nav-link ">
											<i class="fa fa-plus"></i>
											<span>Close</span>
										</a>
									</li>
									<%}%>

								</ul>
							</li>

							<%}%>

							<!-- TD & reconnect Management -->
							<%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_CONNECTED_CLIENT_MANAGEMENT) != -1) {%>
							<li class="nav-item"
								id="nix-td-reconnect-connection">
								<a href="javascript:;"
								   class="nav-link nav-toggle">
									<i class="fa fa-external-link"></i>
									<span>Connected Client Management</span>
									<span class="arrow"></span>
								</a>
								<ul class="sub-menu">
									<%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_REVISE_SEARCH) != -1) {%>

									<li class="nav-item"
										id="nix-td-list-search">
										<a href="${context}nix/revise/search.do"
										   class="nav-link ">
											<i class="fa fa-search"></i>
											<span>Application List</span>
										</a>
									</li>
									<%}%>

									<%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_PROBABLE_TD) != -1) {%>
									<li class="nav-item"
										id="nix-td-client-search">
										<a href="${context}nix/revise/searchprobable.do"
										   class="nav-link ">
											<i class="fa fa-search"></i>
											<span>Probable TD Clients</span>
										</a>
									</li>
									<%}%>

									<%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_RECONNECT_APPLICATION) != -1) {%>
									<li class="nav-item"
										id="nix-reconnect">
										<a href="${context}nix/application/reconnect.do"
										   class="nav-link ">
											<i class="fa  fa-plus"></i>
											<span>Apply for Reconnection</span>
										</a>
									</li>
									<%}%>

								</ul>
							</li>
							<%}%>
						</ul>
					</li>
				<%}%>

				<%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_SEARCH_APPLICATION) != -1) {%>

				<li class="nav-item"
					id="nix-application-search">
					<a href="${context}nix/application/search.do"
					   class="nav-link ">
						<i class="fa  fa-search"></i>
						<span>Search Application</span>
					</a>
				</li>
				<%}%>

				<%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_SEARCH_CONNECTION) != -1) {%>

				<li class="nav-item"
					id="nix-connection-search">
					<a href="${context}nix/connection/search.do"
					   class="nav-link ">
						<i class="fa  fa-search"></i>
						<span>Search Connection</span>
					</a>
				</li>
				<%}%>
			</ul>
		</li>
		<!-- 	Connection Ends 	-->
		<%}%>

		<!-- Report begins -->
		<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.GENERAL_REPORT_NIX ) != -1) {%>
		<li class="nav-item  "
			id="nixReportMenu">
			<a href="javascript:;"
			   class="nav-link nav-toggle">
				<i class="fa fa-file"></i>
				<span>Report</span>
				<span class="arrow"></span>
			</a>
			<ul class="sub-menu">
				<%--<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_MODULE_NIX ) != -1) {%>
				<li class="nav-item"
					id="nixReportNewMenu">
					<a href="${context}Report/GetServiceEntityReport.do?moduleID=<%=ModuleConstants.Module_ID_NIX %>"
					   class="nav-link">
						<i class="fa fa-plus"></i>
						<span>NIX Report</span>
					</a>
				</li>
				<%} %>--%>
				<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_CLIENT_NIX ) != -1) {%>
				<li class="nav-item"
					id="nixClientReportMenu">
					<a href="${context}Report/GetClientReport.do?moduleID=<%=ModuleConstants.Module_ID_NIX %>"
					   class="nav-link">
						<i class="fa fa-plus"></i>
						<span>Client Report</span>
					</a>
				</li>
				<%} %>
				<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_BILL_NIX ) != -1) {%>
				<li class="nav-item"
					id="nixBillReportMenu">
					<a href="${context}Report/GetBillReport.do?moduleID=<%=ModuleConstants.Module_ID_NIX%>"
					   class="nav-link">
						<i class="fa fa-plus"></i>
						<span>Bill Report</span>
					</a>
				</li>
				<%} %>
				<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_PAYMENT_NIX ) != -1) {%>
				<li class="nav-item"
					id="nixPaymentReportMenu">
					<a href="${context}Report/GetPaymentReport.do?moduleID=<%=ModuleConstants.Module_ID_NIX%>"
					   class="nav-link">
						<i class="fa fa-plus"></i>
						<span>Payment Report</span>
					</a>
				</li>
				<%} %>
				<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_CONNECTION_NIX ) != -1) {%>

				<li class="nav-item"
					id="nixConnectionReportMenu">
					<a href="${context}report/connection.do?module=<%=ModuleConstants.Module_ID_NIX%>"
					   class="nav-link">
						<i class="fa fa-plus"></i>
						<span>Connection Report</span>
					</a>
				</li>
				<%} %>

				<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_APPLICATION_NIX ) != -1) {%>

				<li class="nav-item"
					id="nixApplicationReportMenu">
					<a href="${context}report/application.do?module=<%=ModuleConstants.Module_ID_NIX%>"
					   class="nav-link">
						<i class="fa fa-plus"></i>
						<span>Application Report</span>
					</a>
				</li>
				<%} %>

			</ul>
		</li>
		<%} %>
		<!-- Report ends -->

        <%--Monthly Bill starts--%>
        <%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_MONTHLY_BILL) != -1) {%>
        <li class="nav-item"
            id="nix-monthly-bill">
            <a href="javascript:;"
               class="nav-link nav-toggle">
                <i class="fa fa-external-link"></i>
                <span>Monthly Bill</span>
                <span class="arrow"></span>
            </a>

            <ul class="sub-menu">
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_MONTHLY_BILL_SEARCH) != -1) {%>
                <li class="nav-item"
                    id="nix-monthly-bill-search">
                    <a href="${context}nix/monthly-bill/searchPage.do"
                       class="nav-link ">
                        <i class="fa fa-search"></i>
                        <span>Search</span>
                    </a>
                </li>
                <%}%>
            </ul>

        </li>
        <%}%>

		<!--Monthly usage  -->
        <%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_MONTHLY_BILL_USAGE) != -1) {%>
        <li class="nav-item"
            id="nix-monthly-usage">
            <a href="javascript:;"
               class="nav-link nav-toggle">
                <i class="fa fa-external-link"></i>
                <span>Monthly Usage</span>
                <span class="arrow"></span>
            </a>

            <ul class="sub-menu">
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_MONTHLY_BILL_USAGE_SEARCH) != -1) {%>
                <li class="nav-item"
                    id="nix-monthly-usage-search">
                    <a href="${context}nix/monthly-bill/monthly-bill-usage-searchPage.do"
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
        <%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_MONTHLY_BILL_SUMMARY) != -1) {%>
        <li class="nav-item"
            id="nix-monthly-Bill-summary">
            <a href="javascript:;"
               class="nav-link nav-toggle">
                <i class="fa fa-external-link"></i>
                <span>Monthly Bill Summary</span>
                <span class="arrow"></span>
            </a>

            <ul class="sub-menu">
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_MONTHLY_BILL_SUMMARY_SEARCH) != -1) {%>
                <li class="nav-item"
                    id="nix-monthly-bill-summary-search">
                    <a href="${context}nix/monthly-bill/monthly-bill-summary-searchPage.do"
                       class="nav-link ">
                        <i class="fa fa-search"></i>
                        <span>Search</span>
                    </a>
                </li>
                <%}%>


                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_MONTHLY_BILL_SUMMARY_CHECK) != -1) {%>
                <li class="nav-item"
                    id="nix-monthly-bill-check">
                    <a href="${context}nix/monthly-bill/check.do"
                       class="nav-link ">
                        <i class="fa  fa-question"></i>
                        <span>Check</span>
                    </a>
                </li>
                <%}%>



                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_MONTHLY_BILL_SUMMARY_GENERATE) != -1) {%>
                <li class="nav-item"
                    id="nix-monthly-bill-generate">
                    <a href="${context}nix/monthly-bill/goGenerate.do"
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
        <%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_MONTHLY_OUTSOURCE_BILL) != -1) {%>
        <li class="nav-item"
            id="nix-monthly-outsource-Bill">
            <a href="javascript:;"
               class="nav-link nav-toggle">
                <i class="fa fa-external-link"></i>
                <span>Outsource Bill</span>
                <span class="arrow"></span>
            </a>

            <ul class="sub-menu">
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.NIX_MONTHLY_OUTSOURCE_BILL_SEARCH) != -1) {%>
                <li class="nav-item"
                    id="nix-monthly-outsource-Bill-search">
                    <a href="${context}nix/monthly-bill/monthly-outsource-bill-search.do"
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

		<!-- Bill Begins-->

		<!-- 	BILL AND PAYMENT    -->
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.NIX_BILL) != -1){%>
		<li class="nav-item" id="vpnBillAndPayment">
			<a href="javascript:;" class="nav-link nav-toggle">
				<i class="fa fa-money"></i>
				<span class="title">Bill & Payment</span>
				<span class="arrow"></span>
			</a>

			<ul class="sub-menu">
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.NIX_BILL_SEARCH) != -1){%>
				<li class="nav-item" id="vpnBillSearch">
					<a href="${context}bill/search.do?moduleID=<%=ModuleConstants.Module_ID_NIX%>"
					   class="nav-link ">
						<i	class="fa fa-search"></i>
						<span class="title"> Bill Search</span>
					</a>
				</li>
				<%}%>

				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.NIX_BILL_PAYMENT_SEARCH) != -1){ %>
				<li class="nav-item" id="vpnPaymentSearch">
					<a href="${context}payment/search.do?moduleID=<%=ModuleConstants.Module_ID_NIX %>"
					   class="nav-link ">
						<i	class="fa fa-search"></i>
						<span class="title">Payment Search</span>
					</a>
				</li>
				<%}%>

				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.NIX_BILL_BANK_ADD) != -1){ %>
				<li class="nav-item" id="addBankVPN">
					<a href="${context}Bank/AddNewBank.do?moduleID=<%=ModuleConstants.Module_ID_NIX%>"
					   class="nav-link ">
						<i class="fa fa-plus"></i>
						<span class="title">Add Bank</span>
					</a>
				</li>
				<%}%>

				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.NIX_BILL_BANK_SEARCH) != -1) {%>
				<li class="nav-item" id="searchBankVPN">
					<a href="${context}BankSearch/Banks.do?moduleID=<%=ModuleConstants.Module_ID_NIX%>"
					   class="nav-link ">
						<i class="fa fa-search"></i>
						<span class="title">Bank Search</span>
					</a>
				</li>
				<%}%>
				<li class="nav-item" id="billConfigurationVPN">
					<a href="${context}BillConfiguration.do?moduleID=<%=ModuleConstants.Module_ID_NIX%>"
					   class="nav-link ">
						<i class="fa fa-plus"></i>
						<span class="title">Add Bill Configuration</span>
					</a>
				</li>
				<li class="nav-item" id="splitBillVPN">
					<a href="${context}SplitBill.do?method=splitBill&moduleID=<%=ModuleConstants.Module_ID_NIX%>"
					   class="nav-link ">
						<i class="fa fa-plus"></i>
						<span class="title">Split Bill</span>
					</a>
				</li>
			</ul>
		</li>
		<%}%>		<!-- Bill Ends -->

	</ul>

</li>