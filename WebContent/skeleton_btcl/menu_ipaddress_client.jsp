<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<%@page import="ipaddress.constants.IpaddressStateConstants"%>


<%if (!registeredIpaddress){%>
	<li class="nav-item"><a href="${context}ipaddress/client/clientAdd.jsp" class="nav-link "><i class="fa fa-plus"></i><span class="title">IP Address</span></a>
	</li>
<%}else{%>
	<%if (statusClientIpaddress == IpaddressStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED){%>
		<li class="nav-item"><a	href="${context}GetClientForEdit.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_IPADDRESS%>&edit"	class="nav-link nav-toggle"> 
			<i class="fa fa-chain"></i> <span class="title">IP Address</span> <span class="fa fa-exclamation-circle" style="color: red"></span></a>
		</li>
	<%} else if(activationStatusIpaddress != EntityTypeConstant.STATUS_ACTIVE) {%>
			<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-chain"></i> <span class="title">IP Address</span> <span class="arrow"></span></a>
				<ul class="sub-menu">			
					<li class="nav-item"><a	href="${context}GetClientForView.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_IPADDRESS %>"	class="nav-link nav-toggle"> 
						<i class="fa fa-user"></i> <span class="title">Profile</span> <span class="arrow"></span></a>
					</li>					
					<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Request</span> <span class="arrow"></span></a>
						<ul class="sub-menu">
							<li class="nav-item  "><a href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_IPADDRESS%>"	class="nav-link "><i class="fa fa-search"></i>
								<span class="title">Search </span></a>
							</li>
						</ul>
					</li>
				</ul>
			</li>			
	<% }else if(activationStatusIpaddress == EntityTypeConstant.STATUS_ACTIVE) {%>
		<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-chain"></i> <span class="title">IP Address</span> <span class="arrow"></span></a>
			<ul class="sub-menu">			
				<li class="nav-item"><a	href="${context}GetClientForView.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_IPADDRESS %>"	class="nav-link nav-toggle"> 
					<i class="fa fa-user"></i> <span class="title">Profile</span> <span class="arrow"></span></a>
				</li>					
				<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Request</span> <span class="arrow"></span></a>
					<ul class="sub-menu">
						<li class="nav-item  "><a href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_IPADDRESS%>"	class="nav-link "><i class="fa fa-search"></i>
							<span class="title">Search </span></a>
						</li>
					</ul>
				</li>
				
				<!-- Add Necessary List Items according to module HERE. Thanks. -->
				
				
				
				
			</ul>
		</li>	
	<%
	}
}%>