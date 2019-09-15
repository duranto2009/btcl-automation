<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<%@page import="lli.constants.LliStateConstants"%>


<%if (!registeredLli){%>
	<li class="nav-item"><a href="${context}Client/Add.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link "><i class="fa fa-plus"></i><span class="title">LLI</span></a>
	</li>
<%}else{%>
	<%if (statusClientLli == LliStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED){%>
		<li class="nav-item"><a	href="${context}GetClientForEdit.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_LLI%>&edit"	class="nav-link nav-toggle"> 			 
			<i class="fa fa-chain"></i> <span class="title">LLI</span> <span class="fa fa-exclamation-circle" style="color: red"></span></a>
		</li>
	<%} else if(activationStatusLli != EntityTypeConstant.STATUS_ACTIVE) {%>
			<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-chain"></i> <span class="title">LLI</span> <span class="arrow"></span></a>
				<ul class="sub-menu">			
					<li class="nav-item"><a	href="${context}GetClientForView.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_LLI %>"	class="nav-link nav-toggle"> 
						<i class="fa fa-user"></i> <span class="title">Profile</span> <span class="arrow"></span></a>
					</li>					
					<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Request</span> <span class="arrow"></span></a>
						<ul class="sub-menu">
							<li class="nav-item  "><a href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_LLI%>"	class="nav-link "><i class="fa fa-search"></i>
								<span class="title">Search </span></a>
							</li>
						</ul>
					</li>
				</ul>
			</li>			
	<% }else if(activationStatusLli == EntityTypeConstant.STATUS_ACTIVE) {%>
		<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-chain"></i> <span class="title">LLI</span> <span class="arrow"></span></a>
			<ul class="sub-menu">			
				<li class="nav-item"><a	href="${context}GetClientForView.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_LLI %>"	class="nav-link nav-toggle"> 
					<i class="fa fa-user"></i> <span class="title">Profile</span> <span class="arrow"></span></a>
				</li>					
				<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Request</span> <span class="arrow"></span></a>
					<ul class="sub-menu">
						<li class="nav-item  "><a href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_LLI%>"	class="nav-link "><i class="fa fa-search"></i>
							<span class="title">Search </span></a>
						</li>
					</ul>
				</li>
				
				<!-- Add Necessary List Items according to module HERE. Thanks. -->
				<!-- Doya kore bisri kore HTML paste korben na keu. -->
				
				<li class="nav-item"><a href="javascript:;"	class="nav-link nav-toggle"> <i class="fa fa-external-link"></i>
							<span class="title"><%=EntityTypeConstant.entityNameMap.get(EntityTypeConstant.LLI_LINK) %></span> <span class="arrow"></span></a>
					<ul class="sub-menu">
						<li class="nav-item  " id="addLliLink"><a
							href="${context}LLI/Connection/Add.do" class="nav-link ">
								<i class="fa fa-plus"></i><span class="title">Add</span>
						</a></li>
						<li class="nav-item  "  id="searchLliLink" ><a href="${context}LliLinkSearch.do"
							class="nav-link "> <i class="fa fa-search"></i><span
								class="title">Search</span>
						</a></li>
						<li class="nav-item  " id="updateBandwidth">
							<a href="${context}lli/link/bandwidthChange.jsp" class="nav-link "> 
							<i class="fa fa-edit"></i>
							<span class="title"> Bandwidth Change</span>
							</a>
						</li>
						<li class="nav-item" id="lliLinkRequestIpAddressSubmenu2" >
							<a href="${context}LliLinkIpaddress/new.do" class="nav-link "> <i class="fa fa-search"></i><span	class="title">IP Address Request</span></a>
						</li>

				<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI_LINK_REQUEST_IPADDRESS) != -1) {%>
				<li class="nav-item" id="lliLinkRequestIpAddressSubmenu2" >
					<a href="${context}LliLinkIpaddress/new.do" class="nav-link "> <i class="fa fa-search"></i><span	class="title">IP Address Request</span></a>
				</li>
				<%}%>

				<%-- <li class="nav-item" id="shiftLliLink" >
					<a href="${context}lli/link/linkShift.jsp" class="nav-link "> <i class="fa fa-search"></i><span	class="title">Shift Connection</span></a>
				</li> --%>
				
				<li class="nav-item" id="LliLinkClose"><a
					href="${context}LliLinkClose.do"" class="nav-link "> <i
						class="fa  fa-window-close"></i><span class="title"> Close Connection</span>
				</a></li>
					</ul>
				</li>
				<li class="nav-item" id="common"><a href="javascript:;"	class="nav-link nav-toggle"> <i class="fa fa-money"></i> <span class="title">Bill & Payment</span> <span class="arrow"></span></a>
					<ul class="sub-menu">
						<li class="nav-item" id="searchBill"><a href="${context}SearchBill.do?moduleID=<%=ModuleConstants.Module_ID_LLI%>" class="nav-link "> <i
							class="fa fa-search"></i><span class="title">Bill Search</span></a></li>
						<li class="nav-item" id="commonBankPayment"><a
							href="${context}common/bill/bankPayment.jsp?moduleID=<%=ModuleConstants.Module_ID_LLI %>" class="nav-link "> <i
								class="fa fa-search"></i><span class="title">Bank Payment</span></a></li>
						<li class="nav-item" id="lliInstructionPayment"><a href="${context}LLI/Instruction/Payment.do" class="nav-link "> 
							<i class="fa fa-question"></i><span class="title">How to Pay</span></a>
						</li>
					</ul>
				</li>
				<%-- <li class="nav-item " id="complainMenu<%=ModuleConstants.Module_ID_LLI%>"><a href="javascript:;"
					class="nav-link nav-toggle"> <i class="fa fa-bug"></i> <span
						class="title">Complain</span> <span class="arrow"></span>
				</a>
					<ul class="sub-menu">
						<li class="nav-item  " id="complainAddMenu<%=ModuleConstants.Module_ID_LLI%>"><a
							href="${context}complain/addComplain.jsp?moduleID=<%=ModuleConstants.Module_ID_LLI%>" class="nav-link "><i
								class="fa fa-plus"></i> <span class="title">Add </span> </a></li>
						<li class="nav-item  " id="complainSearchMenu<%=ModuleConstants.Module_ID_LLI%>"><a href="${context}ComplainSearch.do?moduleID=<%=ModuleConstants.Module_ID_LLI %>"
							class="nav-link "><i class="fa fa-search"></i> <span
								class="title">Search </span> </a></li>
					</ul>
				</li> --%>
			</ul>
		</li>	
	<%
	}
}%>
