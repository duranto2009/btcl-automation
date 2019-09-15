<%@page import="login.PermissionConstants"%>
<%@page import="common.ModuleConstants"%>

<li class="nav-item" id="lliMenu">
	<a href="javascript:;" class="nav-link nav-toggle"> 
		<i class="fa fa-retweet"></i> <span class="title">LLI</span> <span class="arrow"></span>
	</a>
	<ul class="sub-menu">
	
	
		<!-- 	CLIENT    -->
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CLIENT) != -1) {%>
		<li class="nav-item" id="lliClientMenu">
			<a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-user"></i> 
				<span class="title">Client</span> <span class="arrow"></span>
			</a>
			<ul class="sub-menu">
			
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CLIENT_ADD) >= PermissionConstants.PERMISSION_FULL) {%>
					<li class="nav-item" id="addLliClientMenu">
						<a href="${context}Client/Add.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link ">
							<i class="fa fa-plus"></i><span class="title">Add</span>
						</a>
					</li>	
				<%}%>
				
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CLIENT_SEARCH) != -1) {%>
				<li class="nav-item" id="searchLliClientMenu">
					<a href="${context}SearchClient.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>"	class="nav-link "> 
						<i class="fa fa-search"></i><span class="title">Search</span>
					</a>
				</li>
				<%}	%>
				
			</ul>
		</li>
		<%}%>
		
		
		<!-- 	Connection    -->
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_LINK) != -1) {%>
		<li class="nav-item" id="lliLink">
			<a href="javascript:;" class="nav-link nav-toggle"> 
				<i class="fa fa-external-link"></i>
				<span class="title">Connection</span> <span class="arrow"></span>
			</a>
			
			<ul class="sub-menu">
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_LINK_ADD) != -1) {%>
				<li class="nav-item" id="addLliLink">
					<a href="${context}LLI/Connection/Add.do" class="nav-link "><i class="fa fa-plus"></i><span class="title">Add</span></a>
				</li>
				<%}%>
				
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_LINK_SEARCH) != -1) {%>
				<li class="nav-item  " id="searchlliLink">
					<a href="${context}LliLinkSearch.do" class="nav-link "><i class="fa fa-search"></i><span class="title">Search</span></a>
				</li>
				<%}%>
				
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_LINK_BANDWIDTH_CHANGE) != -1) {%>
				<li class="nav-item" id="lliLinkUpdateBandwidthSubmenu2">
					<a href="${context}LliLinkBandwidthChange.do" class="nav-link "> <i class="fa  fa-edit"></i><span class="title">Bandwidth Change</span></a>
				</li>
				<%}%>								

				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_LINK_REQUEST_IPADDRESS) != -1) {%>
				<li class="nav-item" id="lliLinkRequestIpAddressSubmenu2" >
					<a href="${context}LliLinkIpaddress/new.do" class="nav-link "> <i class="fa fa-search"></i><span	class="title">IP Address Request</span></a>
				</li>
				<%}%>

				<%-- <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_LINK_SHIFT) != -1) {%>
				<li class="nav-item" id="shiftLliLink" >
					<a href="${context}lli/link/linkShift.jsp" class="nav-link "> <i class="fa fa-search"></i><span	class="title">Shift Connection</span></a>
				</li>
				<%}%> --%>
				
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_LINK_TDLINK) != -1) {%>
				<li class="nav-item" id="lliLinkTDLinkSubmenu2">
					<a href="${context}LLILinkTD.do" class="nav-link "><i class="fa  fa-exclamation"></i><span class="title"> TD Connection </span></a>
				</li>
				<%}%>
				
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_LINK_TDLINK) != -1) {%>
				<li class="nav-item" id="lliLinkEnable">
					<a href="${context}LLILinkEnable.do" class="nav-link "><i class="fa  fa-exclamation"></i><span class="title"> Enable Connection </span></a>
				</li>
				<%}%>
								
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_LINK_CLOSE) != -1) {%>
				<li class="nav-item" id="lliLinkCloseSubmenu2">
					<a href="${context}LliLinkClose.do" class="nav-link "> <i class="fa  fa-ban"></i><span class="title">Close Connection </span></a>
				</li>
				<%}%>				
				
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_LINK_BALANCE_TRANSFER) != -1) {%>
				<li class="nav-item" id="lliLinkBalanceTransferSubmenu2">
					<a href="${context}lli/link/bandwidthChange.jsp" class="nav-link "> <i	class="fa fa-exchange"></i><span class="title">Transfer Balance</span></a>
				</li>
				<%}%>
				
			</ul>
		</li>
		<%}%>
		
		<!-- 	BILL AND PAYMENT    -->
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL) != -1) {%>
		<li class="nav-item" id="lliBillAndPayment">
			<a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-money"></i> <span class="title">Bill & Payment</span> <span class="arrow"></span></a>
			
			<ul class="sub-menu">
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL_SEARCH) != -1) {%>
				<li class="nav-item" id="lliBillSearch">
					<a href="${context}SearchBill.do?moduleID=<%=ModuleConstants.Module_ID_LLI%>&categoryID=1" class="nav-link "><i class="fa fa-search"></i><span class="title"> Bill Search</span></a>
				</li>
				<%}%>
				
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL_PAYMENT_SEARCH) != -1) {%>
				<li class="nav-item" id="lliPaymentSearch">
					<a href="${context}SearchPayment.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link "><i class="fa fa-money"></i><span class="title">Payment Search</span></a>
				</li>	
				<%}%>
				
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL_BANK_ADD) != -1) {%>
				<li class="nav-item" id="addBankLLI">
					<a href="${context}Bank/AddNewBank.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link "><i class="fa fa-money"></i><span class="title">Add Bank</span></a>
				</li>		
				<%}%>
				
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL_BANK_SEARCH) != -1) {%>
				<li class="nav-item" id="searchBankLLI">
					<a href="${context}BankSearch/Banks.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link "><i class="fa fa-money"></i><span class="title">Bank Search</span></a>
				</li>	
				<%}%>	
				
				<li class="nav-item" id="billConfigurationLLI">
					<a href="${context}BillConfiguration.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link "> <i class="fa fa-plus"></i><span class="title">Add Bill Configuration</span></a>
				</li>	
				<li class="nav-item" id="splitBillLLI">
					<a href="${context}SplitBill.do?method=splitBill&moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link "> <i class="fa fa-plus"></i><span class="title">Split Bill</span></a>
				</li>
				
				<li class="nav-item" id="create-lli-monthly-bill">
					<a href="${context}lli/bill/create-monthly-bill.do" class="nav-link "> <i class="fa fa-money"></i><span class="title">Create Monthly Bill</span></a>
				</li>	
				
			</ul>
		</li>
		<%}%>
		
		
		
		
		<!-- 	INVENTORY    -->
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_INVENTORY) != -1) {%>
		<li class="nav-item" id="inventorySubmenuLLI">
			<a href="javascript:;" class="nav-link nav-toggle"><i class="fa fa-bank"></i><span class="title">Inventory</span><span class="arrow"></span></a>
			
			<ul class="sub-menu">
			
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_INVENTORY_ADD) != -1) {%>
				<li class="nav-item" id="addInventorySubmenuLLI">
					<a href="${context}VpnInventoryAdd.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link "><i class="fa fa-plus-circle"></i><span class="title">Add Item</span></a>
				</li>
				<%}%>

				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_INVENTORY_SEARCH) != -1) {%>
				<li class="nav-item " id="searchInventorySubmenuLLI">
					<a href="${context}SearchInventoryItemAll.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link "><i class="fa fa-search"></i> <span class="title">Search</span></a>
				</li>
				<%}%>
			
				<%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_INVENTORY_IPADDRESS) != -1) {%>--%>
				<%--<li class="nav-item" id="ipAddressLLI">--%>
					<%--<a href="javascript:;" class="nav-link nav-toggle"><i class="fa fa-server"></i><span class="title">IP Address</span><span class="arrow"></span></a>--%>
					<%----%>
					<%--<ul class="sub-menu">--%>
					<%----%>
						<%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_INVENTORY_IPADDRESS_ADD) != -1) {%>--%>
						<%--<li class="nav-item " id="addIPAddressLLI">--%>
							<%--<a href="${context}lli/inventory/ipAddress/add.do" class="nav-link"><i class="fa fa-plus"></i><span class="title">Add</span></a>--%>
						<%--</li>--%>
						<%----%>
						<%--<li class="nav-item " id="IPBlockTesterLLI">--%>
							<%--<a href="${context}lli/inventory/ipAddress/getIPBlockTester.do" class="nav-link"><i class="fa fa-link"></i><span class="title">IPBlock Tester</span></a>--%>
						<%--</li>--%>
						<%----%>
						<%----%>
						<%--<%}%>--%>
						<%----%>
						<%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_INVENTORY_IPADDRESS_SEARCH) != -1) {%>--%>
						<%--<li class="nav-item" id="search-original-ip">--%>
							<%--<a href="${context }lli/inventory/ipAddress/original/search.do" class=nav-link>--%>
								<%--<i class="fa fa-search"></i><span class="title">Search Original IP</span>--%>
							<%--</a>--%>
						<%--</li>--%>
						<%--<li class="nav-item" id="search-allocated-ip">--%>
							<%--<a href="${context }lli/inventory/ipAddress/allocated/search.do" class=nav-link>--%>
								<%--<i class="fa fa-search"></i><span class="title">Search Allocated IP</span>--%>
							<%--</a>--%>
						<%--</li>--%>
							<%----%>
						<%--<%}%>--%>
						<%----%>
					<%--</ul>--%>
				<%--</li>--%>
				<%--<%}%>--%>
			</ul>
		</li>
		<%}%>
		
		
		
		<!-- 	CONFIGURATION    -->
		<%if(menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CONFIGURATION) != -1){%>
		<li class="nav-item" id="lliConfigurationSubmenu1">
			<a href="javascript:;" class="nav-link nav-toggle"><i class="fa fa-gear"></i> <span class="title">Configuration</span><span class="arrow"></span></a>
			
			<ul class="sub-menu">
				
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CONFIGURATION_INVENTORY) != -1) {%>
				<li class="nav-item  " id="lliInventoryConfigSubmenu2">
					<a href="${context}vpn/inventory/editCategory.jsp" class="nav-link "><i class="fa fa-gg"></i> <span class="title">Inventory</span> </a>
				</li>
				<%}%>
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CONFIGURATION_COST) != -1) {%>
				<li class="nav-item " id="lliCostConfigSubmenu2">
					<a href="javascript:;" class="nav-link nav-toggle"><i class="fa fa-gear"></i> <span class="title">Cost</span><span class="arrow"></span></a>
					
					<ul class="sub-menu">
						<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CONFIGURATION_COST) != -1) {%>
						<li class="nav-item  " id="lliCostConfigMenu">
							<a href="${context}GetCostConfig.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>&categoryID=1" class="nav-link "> <i class="fa fa-money"></i><span class="title"> BW Cost</span></a>
						</li>
						<%} %>
						<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CONFIGURATION_FIXED_COST) != -1){%>
						<li class="nav-item  " id="lliCommonChargeMenu">
							<a href="${context}GetCommonChargeConfigLLI.do" class="nav-link "><i class="fa fa-cogs"></i><span class="title"> Common Costs </span></a>
						</li>
						<%} %>
						<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST) != -1) {%>
						<li class="nav-item" id="districtOfcInstallationCostSubmenu2LLI">
							<a href="${context}DistrictOfcInstallation/UpdateDistrictOfcInstallationCost.do" class="nav-link "><i class="fa fa-map-marker"></i><span class="title">OFC Installation Cost</span></a>
						</li>
						<%}%>
					</ul>
				</li>
				
				
<!-- 				<li class="nav-item " id="lliCostConfigSubmenu2"> -->
<%-- 					<a href="${context}GetCostConfig.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>&categoryID=1" class="nav-link "> <i class="fa fa-money"></i><span class="title">Cost</span></a> --%>
<!-- 				</li> -->
				<%}%>
				
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST) != -1) {%>
				<li class="nav-item" id="lliInventorySubmenu1">
					<a href="javascript:;" class="nav-link nav-toggle"><i class="fa fa-bank"></i><span class="title">Distance</span><span class="arrow"></span></a>
					<ul class="sub-menu">
						<li class="nav-item" id="lliDistrictDistanceSubmenu2">
							<a href="${context}Distance/UpdateDistrictDistance.do" class="nav-link "><i class="fa fa-map-marker"></i> <span class="title">District Distance</span></a>
						</li>
						<li class="nav-item  " id="lliUpazilaDistanceSubmenu2">
							<a href="${context}Distance/UpdateUpazilaDistance.do" class="nav-link "><i class="fa fa-map-marker"></i> <span class="title">Upazila Distance</span></a>
						</li>
						<li class="nav-item  " id="lliUnionDistanceSubmenu2">
							<a href="${context}Distance/UpdateUnionDistance.do" class="nav-link "><i class="fa fa-map-marker"></i> <span class="title">Union Distance</span></a>
						</li>
					</ul>
				</li>
				<%}%>
			</ul>
		</li>
		<%}%>
		
		
		
		
		<!-- 	REQUEST    -->
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_REQUEST) != -1) {%>
		<li class="nav-item" id="lliRequestSubMenu">
			<a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Request</span> <span class="arrow"></span></a>
			
			<ul class="sub-menu">
				
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_REQUEST_SEARCH) != -1) {%>
				<li class="nav-item" id="lliSearchRequestSubMenu">
					<a href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link "><i class="fa fa-search"></i><span class="title">Search</span></a>
				</li>
				<%}%>
				
			</ul>
		</li>
		<%}%>
		
		<!-- 	NOTE    -->
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_NOTE) != -1) {%>
		<li class="nav-item" id="lliNoteSubMenu">
			<a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Note</span> <span class="arrow"></span></a>
			
			<ul class="sub-menu">
				
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_NOTE_SEARCH) != -1) {%>
				<li class="nav-item" id="lliSearchNoteSubMenu">
					<a href="${context}SearchNote.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link "><i class="fa fa-search"></i><span class="title">Search </span></a>
				</li>
				<%}%>
				
			</ul>
		</li>
		<%}%>
		
		<!-- 	Preview For temp purpose    -->
		
		<li class="nav-item" id="lliPreview">
			<a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Preview</span> <span class="arrow"></span></a>
			<ul class="sub-menu">
				<li class="nav-item" id="lliDemandNotePreview">
					<a href="${context}preview.do" class="nav-link "><i class="fa fa-book"></i><span class="title">Demand Note </span></a>
				</li>
			</ul>
		</li>
		
		
		
		<!-- 	MIGRATION    -->
		<%if(migrationEnabled && menuLoginDTO.getMenuPermission( login.PermissionConstants.LLI_MIGRATION ) != -1) {%>
		<li class="nav-item" id="lliMigration">
			<a href="javascript:;" class="nav-link nav-toggle"><i class="fa fa-external-link"></i><span class="title">Migration</span><span class="arrow"></span></a>
			
			<ul class="sub-menu">
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_LINK_UPDATE_MIGRATED_LINK) != -1) {%>
				<li class="nav-item" id="lliConnectionMigration" >
					<a href="${context}lli/link/updateMigratedLLI.jsp" class="nav-link "> <i class="fa fa-plus"></i><span	class="title">Connection Migration</span></a>
				</li>
				<%}%>
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_BILL_MIGRATION) != -1){%>
				<li class="nav-item" id="lliBillMigration">
					<a href="${context}lli/link/generateDemandNoteMigration.jsp" class="nav-link "><i class="fa fa-money"></i><span class="title">Bill Migration</span></a>
				</li>
				<%}%>
				
				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_CLIENT_MIGRATION) != -1){%>
				<li class="nav-item" id="lliClientMigration">
					<a href="${context}/lli/client/migratedClientUpdate.jsp" class="nav-link "><i class="fa fa-money"></i><span class="title">Client Migration</span></a>
				</li>
				<%}%>
			</ul>
		</li>
		<%}%>
		<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.GENERAL_REPORT_LLI ) != -1) {%>
		<li class="nav-item  " id="lliReportMenu">
            <a href="javascript:;" class="nav-link nav-toggle">
                <i class="fa fa-file"></i> <span class="title">Report</span> <span class="arrow"></span>
            </a>
            <ul class="sub-menu">
                <%--<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_MODULE_LLI ) != -1) {%>--%>
                <%--<li class="nav-item" id="lliReportNewMenu">--%>
                    <%--<a href="${context}Report/GetNewReport.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>"--%>
                       <%--class="nav-link">--%>
                        <%--<i class="fa fa-plus"></i> <span class="title">LLI Report</span>--%>
                    <%--</a>--%>
                <%--</li>--%>
                <%--<%} %>--%>
                <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_CLIENT_LLI ) != -1) {%>
                  <li class="nav-item" id="lliClientReportMenu">
                    <a href="${context}Report/GetClientReport.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span class="title">Client Report</span>
                    </a>
                </li>
                <%} %>
                <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_BILL_LLI ) != -1) {%>
                 <li class="nav-item" id="lliBillReportMenu">
                    <a href="${context}Report/GetBillReport.do?moduleID=<%=ModuleConstants.Module_ID_LLI%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span class="title">Bill Report</span>
                    </a>
                </li>
                <%} %>
                <%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_PAYMENT_LLI ) != -1) {%>
                <li class="nav-item" id="lliPaymentReportMenu">
                    <a href="${context}PaymentReport/GetNewPaymentReport.do?moduleID=<%=ModuleConstants.Module_ID_LLI%>"
                       class="nav-link">
                        <i class="fa fa-plus"></i> <span class="title">Payment Report</span>
                    </a>
                </li>
                <%} %>
<!--                 <li class="nav-item  " id="lliReportTemplateSearchMenu"> -->
<%--                     <a href="${context}ReportTemplateSearch.do" --%>
<!--                        class="nav-link "> -->
<!--                         <i class="fa fa-search"></i> <span class="title">Templates</span> -->
<!--                     </a> -->
<!--                 </li> -->
            </ul>
        </li>
        <%} %>
		
		
		
	</ul>
</li>
			