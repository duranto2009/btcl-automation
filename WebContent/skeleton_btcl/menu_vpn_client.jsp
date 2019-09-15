<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<%@page import="vpn.constants.VpnStateConstants"%>


<%if (!registeredVpn){%>
	<li class="nav-item"><a href="${context}Client/Add.do?moduleID=<%=ModuleConstants.Module_ID_VPN%>" class="nav-link "><i class="fa fa-plus"></i><span class="title">VPN</span></a>
	</li>
<%}else{%>
	<%if (statusClientVpn == VpnStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED){%>
		<li class="nav-item"><a	href="${context}GetClientForEdit.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_VPN%>&edit"	class="nav-link nav-toggle"> 
			<i class="fa fa-chain"></i> <span class="title">VPN</span> <span class="fa fa-exclamation-circle" style="color: red"></span></a>
		</li>
	<%} else if(activationStatusVpn != EntityTypeConstant.STATUS_ACTIVE) {%>
			<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-chain"></i> <span class="title">VPN</span> <span class="arrow"></span></a>
				<ul class="sub-menu">			
					<li class="nav-item"><a	href="${context}GetClientForView.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_VPN %>"	class="nav-link nav-toggle"> 
						<i class="fa fa-user"></i> <span class="title">Profile</span> <span class="arrow"></span></a>
					</li>					
					<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Request</span> <span class="arrow"></span></a>
						<ul class="sub-menu">
							<li class="nav-item  "><a href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_VPN%>"	class="nav-link "><i class="fa fa-search"></i>
								<span class="title">Search </span></a>
							</li>
						</ul>
					</li>
				</ul>
			</li>			
	<% }else if(activationStatusVpn == EntityTypeConstant.STATUS_ACTIVE) {%>
		<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-chain"></i> <span class="title">VPN</span> <span class="arrow"></span></a>
			<ul class="sub-menu">			
				<li class="nav-item"><a	href="${context}GetClientForView.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_VPN %>"	class="nav-link nav-toggle"> 
					<i class="fa fa-user"></i> <span class="title">Profile</span>
					<%--<span class="arrow"></span>--%>
				</a>
				</li>					
				<%--<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Request</span> <span class="arrow"></span></a>--%>
					<%--<ul class="sub-menu">--%>
						<%--<li class="nav-item  "><a href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_VPN%>"	class="nav-link "><i class="fa fa-search"></i>--%>
							<%--<span class="title">Search </span></a>--%>
						<%--</li>--%>
					<%--</ul>--%>
				<%--</li>--%>
				
				<!-- Add Necessary List Items according to module HERE. Thanks. -->
				
				<li class="nav-item"><a href="javascript:;"	class="nav-link nav-toggle"> <i class="fa fa-external-link"></i>
							<span class="title"><%=EntityTypeConstant.entityNameMap.get(EntityTypeConstant.VPN_LINK) %></span> <span class="arrow"></span></a>
					<ul class="sub-menu">
						<li class="nav-item  " id="addVpnLink"><a
								href="${context}vpn/link/add.do" class="nav-link ">
								<i class="fa fa-plus"></i><span class="title">Add</span>
						</a></li>
						<li class="nav-item  "  id="searchVpnLink" ><a href="${context}vpn/link/search.do"
							class="nav-link "> <i class="fa fa-search"></i><span
								class="title">Search</span>
						</a></li>						
						<%--<li class="nav-item  " id="updateBandwidth">--%>
							<%--<a href="${context}vpn/link/bandwidthChange.jsp" class="nav-link "> --%>
								<%--<i class="fa fa-edit"></i>--%>
								<%--<span class="title"> Bandwidth Change</span>--%>
							<%--</a>--%>
						<%--</li>--%>
						<li class="nav-item" id="updateBandwidth">
							<a href="${context}vpn/network/bandwidth-change.do" class="nav-link "> <i class="fa  fa-edit"></i><span class="title">Bandwidth Change</span></a>
						</li>

						<%-- <li class="nav-item" id="shiftVPNLink">
							<a href="${context}VPN/Link/Shift/Add.do" class="nav-link "> 
								<i class="fa fa-exchange"></i>
								<span class="title"> Shift Link</span>
							</a>
						</li> --%>
						<%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_LINK_SHIFT) != -1) {%>--%>
						<li class="nav-item" id="shiftVpnLink" >
							<a href="${context}vpn/link/shift.do" class="nav-link "> <i class="fa fa-search"></i><span class="title">Shift Link</span></a>
						</li>
						<%--<%}%>--%>
						<li class="nav-item" id="VpnTdClose">
						<a href="${context}vpn/td/search.do" class="nav-link "><i class="fa  fa-window-close"></i><span class="title"> TD Link</span></a>
						</li>
						<li class="nav-item" id="VpnEnable">
						<a href="${context}vpn/reconnect/application.do" class="nav-link "><i class="fa  fa-exclamation"></i><span class="title"> Enable Link</span></a>
						</li>
						<li class="nav-item" id="VpnLinkClose"><a
							href="${context}vpn/close/application.do" class="nav-link "> <i
								class="fa  fa-window-close"></i><span class="title"> Close Link</span>
							</a>
						</li>
						<li class="nav-item" id="VpnOwnerChange"><a
								href="${context}vpn/ownerchange/application.do" class="nav-link "> <i
								class="fa fa-exchange"></i><span class="title"> Owner Change</span>
						</a>
						</li>
					</ul>
				</li>
				<%--<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Request</span> <span class="arrow"></span></a>--%>
					<%--<ul class="sub-menu">--%>
						<%--<li class="nav-item  "><a href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_VPN%>"--%>
							<%--class="nav-link "><i class="fa fa-search"></i><span	class="title">Search </span></a>--%>
					<%--</ul>--%>
				<%--</li>--%>
				<li class="nav-item" id="common"><a href="javascript:;"	class="nav-link nav-toggle"> <i class="fa fa-money"></i> <span class="title">Bill & Payment</span> <span class="arrow"></span></a>
					<ul class="sub-menu">
						<li class="nav-item" id="searchBill"><a href="${context}bill/search.do?moduleID=<%=ModuleConstants.Module_ID_VPN%>" class="nav-link "> <i
							class="fa fa-search"></i><span class="title"> Bill Search</span></a></li>
						<%--<li class="nav-item" id="commonBankPayment"><a--%>
							<%--href="${context}common/payment/bankPayment.jsp?moduleID=<%=ModuleConstants.Module_ID_VPN %>" class="nav-link "> <i--%>
								<%--class="fa fa-search"></i><span class="title">Bank Payment</span></a></li>--%>
					</ul>
				</li>
				<%-- <li class="nav-item " id="complainMenu<%=ModuleConstants.Module_ID_VPN%>"><a href="javascript:;"
					class="nav-link nav-toggle"> <i class="fa fa-bug"></i> <span
						class="title">Complain</span> <span class="arrow"></span>
				</a>
					<ul class="sub-menu">
						<li class="nav-item  " id="complainAddMenu<%=ModuleConstants.Module_ID_VPN%>"><a
							href="${context}complain/addComplain.jsp?moduleID=<%=ModuleConstants.Module_ID_VPN %>" class="nav-link "><i
								class="fa fa-plus"></i> <span class="title">Add </span> </a></li>
						<li class="nav-item  " id="complainSearchMenu<%=ModuleConstants.Module_ID_VPN%>"><a href="${context}ComplainSearch.do?moduleID=<%=ModuleConstants.Module_ID_VPN %>"
							class="nav-link "><i class="fa fa-search"></i> <span
								class="title">Search </span> </a></li>
					</ul>
				</li> --%>
			</ul>
		</li>	
	<%
	}
}%>
