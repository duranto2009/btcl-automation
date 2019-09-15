<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<%@page import="dnshosting.constants.DnshostingStateConstants"%>

<%if (!registeredDnshosting){%>
	<li class="nav-item"><a href="${context}dnshosting/client/clientAdd.jsp" class="nav-link "><i class="fa fa-plus"></i><span class="title">DNS Hosting</span></a>
	</li>
<%}else{%>
	<%if (statusClientDnshosting == DnshostingStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED){%>
		<li class="nav-item"><a	href="${context}GetClientForEdit.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_DNSHOSTING%>&edit"	class="nav-link nav-toggle"> 
			<i class="fa fa-chain"></i> <span class="title">DNS Hosting</span> <span class="fa fa-exclamation-circle" style="color: red"></span></a>
		</li>
	<%} else if(activationStatusDnshosting != EntityTypeConstant.STATUS_ACTIVE) {%>
			<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-chain"></i> <span class="title">DNS Hosting</span> <span class="arrow"></span></a>
				<ul class="sub-menu">			
					<li class="nav-item"><a	href="${context}GetClientForView.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_DNSHOSTING %>"	class="nav-link nav-toggle"> 
						<i class="fa fa-user"></i> <span class="title">Profile</span> <span class="arrow"></span></a>
					</li>					
					<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Request</span> <span class="arrow"></span></a>
						<ul class="sub-menu">
							<li class="nav-item  "><a href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_DNSHOSTING%>"	class="nav-link "><i class="fa fa-search"></i>
								<span class="title">Search </span></a>
							</li>
						</ul>
					</li>
				</ul>
			</li>			
	<% }else if(activationStatusDnshosting == EntityTypeConstant.STATUS_ACTIVE) {%>
		<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-chain"></i> <span class="title">DNS Hosting</span> <span class="arrow"></span></a>
			<ul class="sub-menu">			
				<li class="nav-item"><a	href="${context}GetClientForView.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_DNSHOSTING %>"	class="nav-link nav-toggle"> 
					<i class="fa fa-user"></i> <span class="title">Profile</span> <span class="arrow"></span></a>
				</li>					
				<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Request</span> <span class="arrow"></span></a>
					<ul class="sub-menu">
						<li class="nav-item  "><a href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_DNSHOSTING%>"	class="nav-link "><i class="fa fa-search"></i>
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