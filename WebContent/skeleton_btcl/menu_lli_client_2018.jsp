<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<%@page import="lli.constants.LliStateConstants"%>


<%if (!registeredLli){%>
	<li class="nav-item" id="lliMenu">
		<a href="${context}Client/Add.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link ">
			<i class="fa fa-plus"></i>
		   <span class="title">LLI</span>
		 </a>
	</li>
<%}
else{%>
	<%if (statusClientLli == LliStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED){%>
	<li class="nav-item" id="lliMenu">
		<a href="${context}GetClientForEdit.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_LLI%>&edit"
		   class="nav-link nav-toggle">
			<i class="fa fa-chain"></i>
			<span class="title">LLI</span>
			<span class="fa fa-exclamation-circle"
				  style="color: red"></span>
		</a>
	</li>
	<%}
	else if(activationStatusLli != EntityTypeConstant.STATUS_ACTIVE) {%>
	<li class="nav-item" id="lliMenu">
		<a href="javascript:void(0);" class="nav-link nav-toggle">
			<i class="fa fa-chain"></i>
			<span class="title">LLI</span>
			<span class="arrow"></span>
		</a>
		<ul class="sub-menu">
			<li class="nav-item">
				<a	href="${context}GetClientForView.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_LLI %>"
					class="nav-link nav-toggle">
					<i class="fa fa-user"></i>
					<span class="title">Profile</span>
					<span class="arrow"></span></a>
			</li>
		</ul>
	</li>
	<% }
	else if(activationStatusLli == EntityTypeConstant.STATUS_ACTIVE) {%>
	<li class="nav-item" id="lliMenu">
		<a href="javascript:void(0);" class="nav-link nav-toggle">
		   <i class="fa fa-chain"></i>
		   <span class="title">LLI</span>
		   <span class="arrow"></span>
		</a>
		<ul class="sub-menu">
			<!-- profile  -->
			<li class="nav-item">
				<a	href="${context}GetClientForView.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_LLI %>"
					class="nav-link nav-toggle">
					<i class="fa fa-user"></i>
					<span class="title">Profile</span>
				</a>
			</li>
			<!-- profile ends -->

			<!-- board  -->
			<li class="nav-item">
				<a	href="${context}lli/client/board.do?id=<%=menuLoginDTO.getAccountID()%>"
					class="nav-link nav-toggle">
					<i class="fa fa-tv"></i>
					<span class="title">Board</span>
				</a>
			</li>
			<!-- board ends -->

			<!-- 	Application    -->
			<li class="nav-item" id="lli-application">
				<a href="javascript:void(0);" class="nav-link nav-toggle">
					<i class="fa fa-external-link"></i>
					<span>Apply</span>
					<span class="arrow"></span>
				</a>
				<ul class="sub-menu">


					<!-- New Connection -->
					<li class="nav-item" id="lli-application-new-connection">
						<a href="${context}lli/application/new-connection.do" class="nav-link ">
							<i class="fa fa-plus"></i>
							<span>New Connection</span>
						</a>
					</li>
					<!-- New Connection End-->

					<!-- Upgrade -->
					<li class="nav-item" id="lli-application-upgrade-connection">
						<a href="${context}lli/application/upgrade-bandwidth.do" class="nav-link ">
							<i class="fa fa-chevron-up"></i>
							<span>Upgrade Bandwidth</span>
						</a>
					</li>
					<!-- Upgrade End-->

					<!-- Downgrade -->
					<li class="nav-item"
						id="lli-application-downgrade-connection">
						<a href="${context}lli/application/downgrade-bandwidth.do"
						   class="nav-link ">
							<i class="fa fa-chevron-down"></i>
							<span>Downgrade Bandwidth</span>
						</a>
					</li>
					<!-- Downgrade End-->

					<!-- Shift BW -->
					<li class="nav-item" id="lli-shift-bandwidth">
						<a href="${context}lli/application/shift-bandwidth.do" class="nav-link ">
							<i class="fa fa-code-fork"></i>
							<span>Shift Bandwidth</span>
						</a>
					</li>
					<!-- Shift BW End-->

					<!-- Shift POP -->
					<%--<li class="nav-item" id="lli-shift-pop">--%>
						<%--<a href="${context}lli/application/shift-pop.do" class="nav-link ">--%>
							<%--<i class="fa fa-code-fork"></i>--%>
							<%--<span>Shift PoP</span>--%>
						<%--</a>--%>
					<%--</li>--%>
					<!-- Shift POP End -->

					<!-- Additional IP -->
					<li class="nav-item" id="lli-additional-ip">
						<a href="${context}lli/application/additional-ip.do" class="nav-link ">
							<i class="fa fa-plus"></i>
							<span>Additional IP</span>
						</a>
					</li>
					<!-- Additional IP End -->

					<!-- Additional Port/Local Loop -->
					<li class="nav-item" id="lli-additional-port-local-loop">
						<a href="${context}lli/application/additional-local-loop.do" class="nav-link ">
							<i class="fa fa-plus"></i>
							<span>Additional Port/Local Loop</span>
						</a>
					</li>
					<!-- Additional Port/Local Loop End -->

					<!-- Close Connection -->
					<li class="nav-item" id="lli-close-connection">
						<a href="${context}lli/application/close-connection.do" class="nav-link ">
							<i class="fa fa-window-close"></i>
							<span>Close Connection</span>
						</a>
					</li>
					<!-- Close Connection End -->

					<!-- Owner Change -->
					<li class="nav-item" id="lli-change-ownership">
						<a href="${context}lli/ownershipChange/application-insert.do" class="nav-link ">
							<i class="fa fa-user-plus"></i>
							<span class="title">Change Owner</span>
						</a>
					</li>
					<!-- Owner Change End-->

					<!-- New LT Contract -->
					<li class="nav-item" id="lli-new-long-term">
						<a href="${context}lli/application/new-long-term.do" class="nav-link ">
							<i class="fa fa-handshake-o"></i>
							<span>LT Contract</span>
						</a>
					</li>
					<!-- New LT Contract End-->

					<!-- Terminate LT Contract -->
					<li class="nav-item" id="lli-terminate-long-term">
						<a href="${context}lli/application/break-long-term.do" class="nav-link ">
							<i class="fa fa-remove"></i>
							<span>Terminate LT Contract</span>
						</a>
					</li>
					<!-- Terminate LT Contract End-->

					<!-- Reconnect -->
					<li class="nav-item" id="lli-reconnect">
						<a href="${context}lli/application/reconnect.do" class="nav-link ">
							<i class="fa fa-refresh"></i>
							<span>Reconnection</span>
						</a>
					</li>
					<!-- Reconnect End -->
				</ul>
			</li>
			<!-- 	Application Ends    -->

			<!-- 	Search    -->
			<li class="nav-item" id="lli-search">
				<a href="javascript:void(0);" class="nav-link nav-toggle">
					<i class="fa fa-search"></i>
					<span>Search</span>
					<span class="arrow"></span>
				</a>
				<ul class="sub-menu">

					<!-- Application -->
					<li class="nav-item" id="lli-search-application">
						<a href="javascript:void(0);" class="nav-link nav-toggle">
							<i class="fa fa-file"></i>
							<span>Application</span>
							<span class="arrow"></span>
						</a>
						<ul class="sub-menu">
							<!-- Connection Application Search -->
							<li class="nav-item" id="lli-search-application-connection">
								<a href="${context}lli/application/search.do" class="nav-link ">
									<i class="fa fa-retweet"></i>
									<span>LLI</span>
								</a>
							</li>
							<!-- Connection Application Search End-->

							<!-- Client Application Search-->
							<li class="nav-item" id="lli-search-application-client">
								<a href="${context}lli/revise/search.do" class="nav-link ">
									<i class="fa fa-user"></i>
									<span>Client</span>
								</a>
							</li>
							<!-- Client Application Search End -->

							<!-- Owner Change Application Search -->
							<li class="nav-item" id="lli-change-ownership-search">
								<a href="${context}lli/ownershipChange/search.do" class="nav-link ">
									<i class="fa fa-user-times"></i>
									<span class="title">Owner Change</span>
								</a>
							</li>
							<!-- Owner Change Application Search End-->
						</ul>
					</li>
					<!-- Application End-->

					<!-- Connection -->
					<li class="nav-item" id="lli-connection-search">
						<a href="${context}lli/connection/search.do" class="nav-link ">
							<i class="fa fa-external-link"></i>
							<span>Search Connection</span>
						</a>
					</li>
					<!-- Connection End-->

					<!-- Long Term Contract Search -->
					<li class="nav-item" id="lli-long-term-search">
						<a href="${context}lli/longterm/search.do" class="nav-link ">
							<i class="fa fa-handshake-o"></i>
							<span>LT Contract Search</span>
						</a>
					</li>
					<!-- Long Term Contract Search End-->

				</ul>
			</li>
			<!-- 	Search Ends    -->

			<!-- Bill -->
			<li class="nav-item" id="lliBillAndPayment">
				<a href="javascript:void(0);" class="nav-link nav-toggle">
					<b style="color: #97b1c3">&#2547;&nbsp;&nbsp;</b>
					<span>Bill & Payment</span>
					<span class="arrow"></span>
				</a>
				<ul class="sub-menu">
					<!-- Monthly Bill -->
					<li class="nav-item" id="monthly-bill">
						<a href="javascript:void(0);" class="nav-link nav-toggle">
							<b style="color: #97b1c3">&#2547;&nbsp;&nbsp;</b>
							<span>Monthly Bill</span>
							<span class="arrow"></span>
						</a>
						<ul class="sub-menu">
							<!-- Monthly Bill search -->
							<li class="nav-item" id="lli-monthly-bill-search">
								<a href="${context}lli/monthly-bill/searchPage.do" class="nav-link ">
									<i class="fa fa-search"></i>
									<span>Search</span>
								</a>
							</li>
							<!-- Monthly Bill search Ends -->

							<!--Monthly usage search -->
							<li class="nav-item" id="lli-monthly-usage-search">
								<a href="${context}lli/monthly-usage/search.do" class="nav-link ">
									<i class="fa fa-search"></i>
									<span>Usage Search</span>
								</a>
							</li>
							<!-- Monthly Usage search Ends -->

							<!--Monthly Bill Summary Search -->
							<li class="nav-item" id="lli-monthly-bill-summary-search">
								<a href="${context}lli/monthly-bill-summary/searchPage.do" class="nav-link ">
									<i class="fa fa-search"></i>
									<span>Summary Search</span>
								</a>
							</li>
							<!--Monthly Bill Summary Search Ends-->
						</ul>
					</li>

					<!-- Monthly Bill Ends -->

					<!--All Bill  -->
					<li class="nav-item" id="lliBillSearch">
						<a href="${context}bill/search.do?moduleID=<%=ModuleConstants.Module_ID_LLI%>"
						   class="nav-link ">
							<i class="fa fa-search"></i>
							<span>All Bill Search</span>
						</a>
					</li>
					<!--All Bill Ends  -->
				</ul>
			</li>
			<!-- Bill Ends -->
			<!--ASN  -->
			<li class="nav-item" id="lli-asn">
				<a href="javascript:void(0);" class="nav-link nav-toggle">
					<i class="fa fa-external-link"></i>
					<span>ASN</span>
					<span class="arrow"></span>
				</a>
				<ul class="sub-menu">

					<!-- ASN Apply -->
					<li class="nav-item" id="lli-asn-add">
						<a href="${context}asn/add.do" class="nav-link ">
							<i class="fa fa-plus"></i>
							<span>Apply</span>
						</a>
					</li>
					<!-- ASN Apply End-->

					<!-- ASN Application Search -->
					<li class="nav-item" id="lli-asn-app-search">
						<a href="${context}asn/search.do" class="nav-link ">
							<i class="fa fa-search"></i>
							<span>Search Application</span>
						</a>
					</li>
					<!-- ASN Application Search End-->

					<!-- ASN Search -->
					<li class="nav-item"
						id="lli-asn-search">
						<a href="${context}asn/search-asn.do"
						   class="nav-link ">
							<i class="fa fa-search"></i>
							<span>Search ASN</span>
						</a>
					</li>
					<!-- ASN Search End -->
				</ul>
			</li>
			<!-- ASN -->
		</ul>
	</li>
	<%}%>
<%}%>


				

				


			