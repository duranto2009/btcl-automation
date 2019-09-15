<%@page import="common.ModuleConstants"%>
<%@page import="login.PermissionConstants"%>

<!-- LLI -->
<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI) != -1) {%>
<li class="nav-item" id="lliMenu">
	<a href="javascript:void(0);" class="nav-link nav-toggle">
	<i class="fa fa-retweet"></i>
	<span>LLI</span>
	<span class="arrow"></span>
</a>
	<ul class="sub-menu">

		<!-- 	Client    -->
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CLIENT) != -1) {%>
		<li class="nav-item" id="lliClientMenu">
			<a href="javascript:void(0);" class="nav-link nav-toggle">
			   <i class="fa fa-user"></i>
			   <span>Client</span>
			   <span class="arrow"></span>
			</a>
			<ul class="sub-menu">
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CLIENT_ADD) >= PermissionConstants.PERMISSION_FULL) {%>
					<li class="nav-item" id="addLliClientMenu">
						<a href="${context}Client/Add.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link ">
						   <i class="fa fa-plus"></i>
						   <span>Add</span>
						</a>
					</li>
				<%}%>

				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CLIENT_SEARCH) != -1) {%>
				<li class="nav-item" id="searchLliClientMenu">
					<a href="${context}SearchClient.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link ">
					   <i class="fa fa-search"></i>
					   <span>Search</span>
					</a>
				</li>
				<%}%>

			</ul>
		</li>

		<%}%>
		<!-- 	Client   Ends -->

		<!-- 	Application    -->
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_APPLICATION) != -1) {%>
		<li class="nav-item" id="lli-application">
			<a href="javascript:void(0);" class="nav-link nav-toggle">
				<i class="fa fa-external-link"></i>
				<span>Apply</span>
				<span class="arrow"></span>
			</a>
			<ul class="sub-menu">

				<!-- New Connection -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_NEW_CONNECTION_APPLICATION) != -1) {%>

				<!-- New Connection -->
				<li class="nav-item" id="lli-application-new-connection">
					<a href="${context}lli/application/new-connection.do" class="nav-link ">
						<i class="fa fa-plus"></i>
						<span>New Connection</span>
					</a>
				</li>
				<!-- New Connection End-->
				<%} %>
				<!-- New Connection End-->

				<!-- Upgrade -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_UPGRADE_CONNECTION_APPLICATION) != -1){%>
				<li class="nav-item" id="lli-application-upgrade-connection">
					<a href="${context}lli/application/upgrade-bandwidth.do" class="nav-link ">
						<i class="fa fa-chevron-up"></i>
						<span>Upgrade Bandwidth</span>
					</a>
				</li>
				<%}%>
				<!-- Upgrade End-->

				<!-- Downgrade -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_DOWNGRADE_CONNECTION_APPLICATION) != -1){%>
				<li class="nav-item"
					id="lli-application-downgrade-connection">
					<a href="${context}lli/application/downgrade-bandwidth.do"
					   class="nav-link ">
						<i class="fa fa-chevron-down"></i>
						<span>Downgrade Bandwidth</span>
					</a>
				</li>
				<%}%>
				<!-- Downgrade End-->

				<!-- Shift BW -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_SHIFT_BW_APPLICATION) != -1){%>
				<li class="nav-item" id="lli-shift-bandwidth">
					<a href="${context}lli/application/shift-bandwidth.do" class="nav-link ">
						<i class="fa fa-code-fork"></i>
						<span>Shift Bandwidth</span>
					</a>
				</li>
				<%}%>
				<!-- Shift BW End-->

				<!-- Shift POP -->
				<%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_SHIFT_POP_APPLICATION) != -1){%>--%>
				<%--<li class="nav-item" id="lli-shift-pop">--%>
					<%--<a href="${context}lli/application/shift-pop.do" class="nav-link ">--%>
						<%--<i class="fa fa-code-fork"></i>--%>
						<%--<span>Shift PoP</span>--%>
					<%--</a>--%>
				<%--</li>--%>
				<%--<%}%>--%>
				<!-- Shift POP End -->

				<!-- Additional IP -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_ADDITIONAL_IP_APPLICATION) != -1){%>
				<li class="nav-item" id="lli-additional-ip">
					<a href="${context}lli/application/additional-ip.do" class="nav-link ">
						<i class="fa fa-plus"></i>
						<span>Additional IP</span>
					</a>
				</li>
				<%}%>
				<!-- Additional IP End -->

				<!-- Additional Port/Local Loop -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_ADDITIONAL_LOCAL_LOOP_APPLICATION) != -1){%>
				<li class="nav-item" id="lli-additional-port-local-loop">
					<a href="${context}lli/application/additional-local-loop.do" class="nav-link ">
						<i class="fa fa-plus"></i>
						<span>Additional Port/Local Loop</span>
					</a>
				</li>
				<%}%>
				<!-- Additional Port/Local Loop End -->

				<%--Change port ip --%>
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CHANGE_IP_PORT_APPLICATION) != -1){%>
				<li class="nav-item" id="lli-change-ip-port">
					<a href="${context}lli/changeIPPort/get-change-ip-port.do" class="nav-link ">
						<i class="fa fa-exchange"></i>
						<span>Change IP & Port</span>
					</a>
				</li>
				<%}%>
				<%--Chagne port ip end--%>

				<!-- Close Connection -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CLOSE_CONNECTION_APPLICATION) != -1){%>
				<li class="nav-item" id="lli-close-connection">
					<a href="${context}lli/application/close-connection.do" class="nav-link ">
						<i class="fa fa-window-close"></i>
						<span>Close Connection</span>
					</a>
				</li>
				<%}%>
				<!-- Close Connection End -->

				<!-- Owner Change -->
				<%if (menuLoginDTO.getMenuPermission(PermissionConstants.LLI_OWNER_CHANGE_APPLICATION) != -1) {%>
				<li class="nav-item" id="lli-change-ownership">
					<a href="${context}lli/ownershipChange/application-insert.do" class="nav-link ">
						<i class="fa fa-user-plus"></i>
						<span class="title">Change Owner</span>
					</a>
				</li>
				<%}%>
				<!-- Owner Change End-->

				<!-- New LT Contract -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_NEW_LT_CONTRACT) != -1){%>
				<li class="nav-item" id="lli-new-long-term">
					<a href="${context}lli/application/new-long-term.do" class="nav-link ">
						<i class="fa fa-handshake-o"></i>
						<span>LT Contract</span>
					</a>
				</li>
				<%}%>
				<!-- New LT Contract End-->

				<!-- Terminate LT Contract -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_TERMINATE_LT_CONTRACT) != -1){%>
				<li class="nav-item" id="lli-terminate-long-term">
					<a href="${context}lli/application/break-long-term.do" class="nav-link ">
						<i class="fa fa-remove"></i>
						<span>Terminate LT Contract</span>
					</a>
				</li>
				<%}%>
				<!-- Terminate LT Contract End-->

				<!-- Reconnect -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_RECONNECT_APPLICATION) != -1){%>
				<li class="nav-item" id="lli-reconnect">
					<a href="${context}lli/application/reconnect.do" class="nav-link ">
						<i class="fa fa-refresh"></i>
						<span>Reconnection</span>
					</a>
				</li>
				<%}%>
				<!-- Reconnect End -->
			</ul>
		</li>
		<%}%>
		<!-- 	Application Ends    -->

		<!-- 	Search    -->
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_SEARCH) != -1) {%>
		<li class="nav-item" id="lli-search">
			<a href="javascript:void(0);" class="nav-link nav-toggle">
				<i class="fa fa-search"></i>
				<span>Search</span>
				<span class="arrow"></span>
			</a>
			<ul class="sub-menu">

				<!-- Application -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_SEARCH_APPLICATION) != -1) {%>
				<!-- Application -->
				<li class="nav-item" id="lli-search-application">
					<a href="javascript:void(0);" class="nav-link nav-toggle">
						<i class="fa fa-file"></i>
						<span>Application</span>
						<span class="arrow"></span>
					</a>
					<ul class="sub-menu">
						<!-- Connection Application Search -->
						<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_SEARCH_LLI_APPLICATION) != -1) {%>
						<li class="nav-item" id="lli-search-application-connection">
							<a href="${context}lli/application/search.do" class="nav-link ">
								<i class="fa fa-retweet"></i>
								<span>LLI</span>
							</a>
						</li>
						<%}%>
						<!-- Connection Application Search End-->

						<!-- Client Application Search-->
						<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_SEARCH_LLI_CLIENT_APPLICATION) != -1) {%>
						<li class="nav-item" id="lli-search-application-client">
							<a href="${context}lli/revise/search.do" class="nav-link ">
								<i class="fa fa-user"></i>
								<span>Client</span>
							</a>
						</li>
						<%}%>
						<!-- Client Application Search End -->

						<!-- Owner Change Application Search -->
						<%if (menuLoginDTO.getMenuPermission(PermissionConstants.LLI_OWNER_CHANGE_SEARCH) != -1) {%>
						<li class="nav-item" id="lli-change-ownership-search">
							<a href="${context}lli/ownershipChange/search.do" class="nav-link ">
								<i class="fa fa-user-times"></i>
								<span class="title">Owner Change</span>
							</a>
						</li>
						<%}%>
						<!-- Owner Change Application Search End-->
					</ul>
				</li>
				<%}%>
				<!-- Application End-->

				<!-- Connection -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_SEARCH_CONNECTION) != -1) {%>
				<li class="nav-item" id="lli-connection-search">
					<a href="${context}lli/connection/search.do" class="nav-link ">
						<i class="fa fa-external-link"></i>
						<span>Search Connection</span>
					</a>
				</li>
				<%}%>
				<!-- Connection End-->

				<!-- Probable TD Clients -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_SEARCH_TD_CLIENTS) != -1) {%>
				<li class="nav-item" id="lli-td-client-search">
					<a href="${context}lli/revise/searchprobable.do" class="nav-link ">
						<i class="fa fa-user-times"></i>
						<span>Probable TD Clients</span>
					</a>
				</li>
				<%}%>
				<!-- Probable TD Clients End-->

				<!-- Long Term Contract Search -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_SEARCH_LONG_TERM) != -1) {%>
				<li class="nav-item" id="lli-long-term-search">
					<a href="${context}lli/longterm/search.do" class="nav-link ">
						<i class="fa fa-handshake-o"></i>
						<span>LT Contract Search</span>
					</a>
				</li>
				<%}%>
				<!-- Long Term Contract Search End-->

			</ul>
		</li>
		<%}%>
		<!-- 	Search Ends    -->

		<!-- 	Inventory    -->
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_INVENTORY) != -1) {%>
		<li class="nav-item" id="inventorySubmenuLLI">
			<a href="javascript:void(0);" class="nav-link nav-toggle">
			   <i class="fa fa-bank"></i>
			   <span>Inventory</span>
			   <span class="arrow"></span>
			</a>
			<ul class="sub-menu">

				<!-- 	Inventory Add  -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_INVENTORY_ADD) != -1) {%>
				<li class="nav-item" id="addInventorySubmenuLLI">
					<a href="${context}inventory/add.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link ">
					   <i class="fa fa-plus"></i>
					   <span>Add </span>
					</a>
				</li>
				<%}%>
				<!-- 	Inventory Add End -->

				<!-- 	Inventory Search   -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_INVENTORY_SEARCH) != -1) {%>
				<li class="nav-item "
					id="searchInventorySubmenuLLI">
					<a href="${context}inventory/search.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>"
					   class="nav-link ">
					   <i class="fa fa-search"></i>
					   <span>Search</span>
					</a>
				</li>
				<%}%>
				<!-- 	Inventory Search End   -->
			</ul>
		</li>
		<%}%>
		<!-- 	Inventory Ends    -->

		<!-- Bill -->
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL) != -1) {%>
		<li class="nav-item" id="lliBillAndPayment">
			<a href="javascript:void(0);" class="nav-link nav-toggle">
				<b style="color: #97b1c3">&#2547;&nbsp;&nbsp;</b>
			   <span>Bill & Payment</span>
			   <span class="arrow"></span>
			</a>
			<ul class="sub-menu">
				<!-- Monthly Bill -->
				<%if (menuLoginDTO.getMenuPermission(PermissionConstants.LLI_MONTHLY_BILL) != -1) {%>
				<li class="nav-item" id="monthly-bill">
					<a href="javascript:void(0);" class="nav-link nav-toggle">
						<b style="color: #97b1c3">&#2547;&nbsp;&nbsp;</b>
						<span>Monthly Bill</span>
						<span class="arrow"></span>
					</a>
					<ul class="sub-menu">
						<!-- Monthly Bill search -->
						<%if (menuLoginDTO.getMenuPermission(PermissionConstants.LLI_MONTHLY_BILL_SEARCH) != -1) {%>
						<li class="nav-item" id="lli-monthly-bill-search">
							<a href="${context}lli/monthly-bill/searchPage.do" class="nav-link ">
								<i class="fa fa-search"></i>
								<span>Search</span>
							</a>
						</li>
						<%}%>
						<!-- Monthly Bill search Ends -->

						<!--Monthly usage search -->
						<%if (menuLoginDTO.getMenuPermission(PermissionConstants.LLI_MONTHLY_BILL_USAGE_SEARCH) != -1) {%>
						<li class="nav-item" id="lli-monthly-usage-search">
							<a href="${context}lli/monthly-usage/search.do" class="nav-link ">
								<i class="fa fa-search"></i>
								<span>Usage Search</span>
							</a>
						</li>
						<%}%>
						<!-- Monthly Usage search Ends -->

						<!--Monthly Bill Summary Search -->
						<%if (menuLoginDTO.getMenuPermission(PermissionConstants.LLI_MONTHLY_BILL_SUMMARY_SEARCH) != -1) {%>
						<li class="nav-item" id="lli-monthly-bill-summary-search">
							<a href="${context}lli/monthly-bill-summary/searchPage.do" class="nav-link ">
								<i class="fa fa-search"></i>
								<span>Summary Search</span>
							</a>
						</li>
						<%}%>
						<!--Monthly Bill Summary Search Ends-->

						<!--Monthly Bill Summary Generate -->
						<%if (menuLoginDTO.getMenuPermission(PermissionConstants.LLI_MONTHLY_BILL_SUMMARY_GENERATE) != -1) {%>
						<li class="nav-item" id="lli-monthly-bill-check">
							<a href="${context}lli/monthly-bill/check.do" class="nav-link ">
								<i class="fa  fa-cog"></i>
								<span>Summary Generate</span>
							</a>
						</li>
						<%}%>
						<!--Monthly Bill Summary Generate Ends-->

						<!--Monthly Bill Summary Generate All-->
						<%if (menuLoginDTO.getMenuPermission(PermissionConstants.LLI_MONTHLY_BILL_SUMMARY_GENERATE_ALL) != -1) {%>
						<li class="nav-item" id="lli-monthly-bill-generate">
							<a href="${context}lli/monthly-bill/goGenerate.do" class="nav-link ">
								<i class="fa fa-cogs"></i>
								<span>Summary Generate All</span>
							</a>
						</li>
						<%}%>
						<!--Monthly Bill Summary Generate All Ends-->

						<!--Monthly Outsource Bill  -->
						<%if (menuLoginDTO.getMenuPermission(PermissionConstants.LLI_MONTHLY_OUTSOURCE_BILL_SEARCH) != -1) {%>
						<li class="nav-item" id="lli-monthly-outsource-Bill-search">
							<a href="${context}lli/monthly-outsource-bill/search.do" class="nav-link ">
								<i class="fa fa-search"></i>
								<span>O/C Bill Search</span>
							</a>
						</li>
						<%}%>
						<!-- Monthly Outsource bill Ends-->
					</ul>
				</li>
				<%}%>
				<!-- Monthly Bill Ends -->

				<!-- Manual Bill  -->
				<%if (menuLoginDTO.getMenuPermission(PermissionConstants.LLI_MANUAL_BILL) != -1) {%>
				<li class="nav-item" id="manual-bill">
					<a href="${context}lli/bill/manual.do" class="nav-link ">
						<b style="color: #97b1c3">&#2547;&nbsp;&nbsp;</b>
						<span>Manual Bill</span>
					</a>
				</li>
				<%}%>
				<!-- Manual Bill Ends -->

				<!--All Bill  -->
				<%if (menuLoginDTO.getMenuPermission(PermissionConstants.LLI_ALL_BILL_SEARCH) != -1) {%>
				<li class="nav-item" id="lliBillSearch">
					<a href="${context}bill/search.do?moduleID=<%=ModuleConstants.Module_ID_LLI%>"
					   class="nav-link ">
						<i class="fa fa-search"></i>
						<span>All Bill Search</span>
					</a>
				</li>
				<%}%>
				<!--All Bill Ends  -->

				<!--Payment Search -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL_PAYMENT_SEARCH) != -1) {%>
				<li class="nav-item"
					id="lliPaymentSearch">
					<a href="${context}payment/search.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link ">
						<i class="fa fa-search"></i>
						<span>All Payment Search</span>
					</a>
				</li>
				<%}%>
				<!--Payment Search Ends-->

				<!-- Bank Add -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL_BANK_ADD) != -1) {%>
				<li class="nav-item"
					id="addBankLLI">
					<a href="${context}Bank/AddNewBank.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>"
					   class="nav-link ">
						<i class="fa fa-money"></i>
						<span>Add Bank</span>
					</a>
				</li>
				<%}%>
				<!-- Bank Add End-->

				<!-- Bank Search -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL_BANK_SEARCH) != -1) {%>
				<li class="nav-item" id="searchBankLLI">
					<a href="${context}BankSearch/Banks.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>"
					   class="nav-link ">
						<i class="fa fa-money"></i>
						<span>Bank Search</span>
					</a>
				</li>
				<%}%>
				<!-- Bank Search End -->

				<!-- Accounting Incident-->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL_ACCOUNTING_INCIDENT) != -1) {%>
				<li class="nav-item" id="accounting-incident-search">
					<a href="${context}accounting/incident/search.do" class="nav-link ">
						<i class="fa fa-search"></i>
						<span>Search Incidents</span>
					</a>
				</li>
				<%}%>
				<!-- Accounting Incident End -->

				<!-- Ledger -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL_LEDGER) != -1) {%>
				<li class="nav-item" id="accounting-ledger-search">
					<a href="${context}accounting/ledger/search.do" class="nav-link ">
						<i class="fa fa-search"></i>
						<span>Ledger</span>
					</a>
				</li>
				<%}%>
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL_LEDGER) != -1) {%>
				<li class="nav-item" id="accounting-subscriber-ledger">
					<a href="${context}accounting/ledger/subscriber.do" class="nav-link ">
						<i class="fa fa-book"></i>
						<span>Subscriber Ledger</span>
					</a>
				</li>
				<%}%>
				<!-- Ledger End-->

				<%--Reminder Letter--%>

				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL_LEDGER) != -1) {%>
				<li class="nav-item" id="search-request-letter">
					<a href="${context}request-letter/get-search-page.do" class="nav-link ">
						<i class="fa fa-search"></i>
						<span>Reminder Letter</span>
					</a>
				</li>
				<%}%>

				<%--Reminder Letter End--%>

				<%--Clearance Certificate--%>

				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL_LEDGER) != -1) {%>
				<li class="nav-item" id="search-clearance-certificate">
					<a href="${context}clearance-certificate/get-search-page.do" class="nav-link ">
						<i class="fa fa-search"></i>
						<span>Clearance Certificate</span>
					</a>
				</li>
				<%}%>

				<%--Clearance Certificate End--%>

				<%--Final Bill --%>

				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL_LEDGER) != -1) {%>
				<li class="nav-item" id="search-final-bill">
					<a href="${context}common/bill/searchFinalPage.do" class="nav-link ">
						<i class="fa fa-search"></i>
						<span>Final Bill</span>
					</a>
				</li>
				<%}%>

				<%--Final Bill End--%>


				<%--Multiple Duplicate Bill --%>

				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL_LEDGER) != -1) {%>
				<li class="nav-item" id="search-multiple-bill">
					<a href="${context}common/bill/searchMultiplePage.do" class="nav-link ">
						<i class="fa fa-search"></i>
						<span>Multiple Month Bill</span>
					</a>
				</li>
				<%}%>

				<%--Multiple Duplicate Bill End--%>



			</ul>
		</li>
		<%}%>
		<!-- Bill Ends -->

		<!-- Report begins -->
		<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.GENERAL_REPORT_LLI ) != -1) {%>
		<li class="nav-item" id="lliReportMenu">
			<a href="javascript:void(0);" class="nav-link nav-toggle">
				<i class="fa fa-book"></i>
				<span>Report</span>
				<span class="arrow"></span>
			</a>
			<ul class="sub-menu">

				<!-- Client -->
				<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_CLIENT_LLI ) != -1) {%>
				  <li class="nav-item" id="lliClientReportMenu">
					<a href="${context}report/client.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link">
						<i class="fa fa-id-card"></i>
						<span>Client Report</span>
					</a>
				</li>
				<%} %>
				<!-- Client End -->

				<!-- Bill -->
				<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_BILL_LLI ) != -1) {%>
				 <li class="nav-item" id="lliBillReportMenu">
					<a href="${context}report/bill.do?moduleID=<%=ModuleConstants.Module_ID_LLI%>" class="nav-link">
						<b style="color: #97b1c3">&#2547;&nbsp;&nbsp;</b>
						<span>Bill Report</span>
					</a>
				</li>
				<%} %>
				<!-- Bill End-->

				<!-- Payment -->
				<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_PAYMENT_LLI ) != -1) {%>
				<li class="nav-item" id="lliPaymentReportMenu">
					<a href="${context}report/payment.do?moduleID=<%=ModuleConstants.Module_ID_LLI%>" class="nav-link">
						<b style="color: #97b1c3">&#2547;&nbsp;&nbsp;</b>
						<span>Payment Report</span>
					</a>
				</li>
				<%} %>
				<!-- Payment End -->

				<!-- Connection -->
				<%if( menuLoginDTO.getMenuPermission( PermissionConstants.REPORT_CONNECTION_LLI ) != -1) {%>

				<li class="nav-item" id="lliConnectionReportMenu">
					<a href="${context}report/connection.do?module=<%=ModuleConstants.Module_ID_LLI%>" class="nav-link">
						<i class="fa fa-link"></i>
						<span>Connection Report</span>
					</a>
				</li>
				<%} %>
				<!-- Connection End -->

				<!-- Application -->
				<%if( menuLoginDTO.getMenuPermission( PermissionConstants.REPORT_APPLICATION_LLI ) != -1) {%>

				<li class="nav-item"
						 id="lliApplicationReportMenu">
				<a href="${context}report/application.do?module=<%=ModuleConstants.Module_ID_LLI%>"
				   class="nav-link">
					<i class="fa fa-file"></i>
					<span>Application Report</span>
				</a>
				</li>
				<%} %>
				<!-- Application -->
			</ul>
		</li>
		<%} %>
		<!-- Report ends -->

		<!-- Configuration Begins-->
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CONFIGURATION) != -1) {%>
		<li class="nav-item" id="lli-configuration">
			<a href="javascript:void(0);" class="nav-link nav-toggle">
			   <i class="fa fa-gear"></i>
				<span>Configuration</span>
				<span class="arrow"></span>
			</a>
			<ul class="sub-menu">
				<!-- BW Cost -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CONFIGURATION_COST) != -1) {%>
				<li class="nav-item  " id="lliCostConfigMenu">
					<a href="${context}GetCostConfig.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>&categoryID=1" class="nav-link ">
						<b style="color: #97b1c3">&#2547;&nbsp;</b>
						<span> BW Cost</span>
					</a>
				</li>
				<%} %>
				<!-- BW Cost End-->

				<!-- Fixed Cost -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CONFIGURATION_FIXED_COST) != -1){%>
				<li class="nav-item" id="lli-configuration-fixed-cost">
					<a href="${context}lli/configuration/fixed-cost/get.do" class="nav-link ">
						<b style="color: #97b1c3">&#2547;&nbsp;</b>
					   <span> Fixed Costs </span>
					</a>
				</li>
				<%} %>
				<!-- Fixed Cost End-->

				<!-- OFC -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST) != -1) {%>
				<li class="nav-item" id="districtOfcInstallationCostSubmenu2LLI">
					<a href="${context}DistrictOfcInstallation/UpdateDistrictOfcInstallationCost.do?module=7" class="nav-link ">
						<b style="color: #97b1c3">&#2547;&nbsp;</b>
						<span>OFC Installation Cost</span>
					</a>
				</li>
				<%}%>
				<!-- OFC End -->

				<!-- Client Category -->
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CONFIGURATION_CLIENT_CATEGORY) != -1){%>
				<li class="nav-item  " id="lli-configuration-category">
					<a href="${context}lli/configuration/new-category-ui/get.do"
					   class="nav-link ">
					   <i class="fa fa-cogs"></i>
					   <span> Category </span>
					</a>
				</li>
				<%} %>
				<!-- Client Category End -->

			</ul>
		</li>
		<%}%>
		<!-- Configuration Ends -->

		<!--ASN  -->
		<%if (menuLoginDTO.getMenuPermission(PermissionConstants.LLI_ASN) != -1) {%>
		<li class="nav-item" id="lli-asn">
			<a href="javascript:void(0);" class="nav-link nav-toggle">
				<i class="fa fa-external-link"></i>
				<span>ASN</span>
				<span class="arrow"></span>
			</a>
			<ul class="sub-menu">

				<!-- ASN Apply -->
				<%if (menuLoginDTO.getMenuPermission(PermissionConstants.LLI_ASN_ADD) != -1) {%>
				<li class="nav-item" id="lli-asn-add">
					<a href="${context}asn/add.do" class="nav-link ">
						<i class="fa fa-plus"></i>
						<span>Apply</span>
					</a>
				</li>
				<%}%>
				<!-- ASN Apply End-->

				<!-- ASN Application Search -->
				<%if (menuLoginDTO.getMenuPermission(PermissionConstants.LLI_ASN_SEARCH_APP) != -1) {%>
				<li class="nav-item" id="lli-asn-app-search">
					<a href="${context}asn/search.do" class="nav-link ">
						<i class="fa fa-search"></i>
						<span>Search Application</span>
					</a>
				</li>
				<%}%>
				<!-- ASN Application Search End-->

				<!-- ASN Search -->
				<%if (menuLoginDTO.getMenuPermission(PermissionConstants.LLI_ASN_SEARCH) != -1) {%>
				<li class="nav-item"
					id="lli-asn-search">
					<a href="${context}asn/search-asn.do"
					   class="nav-link ">
						<i class="fa fa-search"></i>
						<span>Search ASN</span>
					</a>
				</li>
				<%}%>
				<!-- ASN Search End -->
			</ul>

		</li>
		<%}%>
		<!-- ASN -->
	</ul>
</li>
<%}%>
<!-- LLI End -->
			