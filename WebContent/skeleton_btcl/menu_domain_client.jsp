<%@page import="common.ModuleConstants" %>
<%@page import="domain.constants.DomainStateConstants"%>

<%if (!registeredDomain){%>
	<li class="nav-item"><a href="${context}Client/Add.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>" class="nav-link "><i class="fa fa-plus"></i><span class="title">DOMAIN</span></a>
	</li>
<%}else{%>
	<%if (statusClientDomain == DomainStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED){%>
		<li class="nav-item"><a	href="${context}GetClientForEdit.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_DOMAIN%>&edit"	class="nav-link nav-toggle"> 
			<i class="fa fa-chain"></i> <span class="title">DOMAIN</span> <span class="fa fa-exclamation-circle" style="color: red"></span></a>
		</li>
	<%} else{%>
		<li class="nav-item" id="domainMenu"><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-location-arrow"></i> <span class="title">Domain</span> <span class="arrow"></span></a>
		    <ul class="sub-menu">
		        <li class="nav-item" id="domainClientMenu"><a href="${context}GetClientForView.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>" class="nav-link nav-toggle"> <i class="fa fa-user"></i> 
		        	<span class="title">Profile</span></a>
		        </li>
		        <li class="nav-item" id="domainAllMenu"><a href="javascript:;"  class="nav-link nav-toggle"> <i class="fa fa-globe "></i>
		        		<span  class="title">Domain-Market</span> <span class="arrow"></span></a>
		            <ul class="sub-menu">
		                <li class="nav-item" id="buyDomainMenu"><a
		                        href="${context}domain/domainQueryForBuy/domainQueryForBuy.jsp"
		                        class="nav-link "> <i class="fa fa-shopping-bag"></i><span
		                        class="title">Buy Domain</span></a></li>
		                <li class="nav-item" id="searchBuyDomainMenu"><a
		                        href="${context}SearchDomain.do" class="nav-link "> <i
		                        class="fa fa-search"></i><span class="title">Search</span></a></li>
		                <li class="nav-item  " id="requestDomainOwnership"><a
		                        href="${context}domain/ownership/requestForOwnership.jsp"
		                        class="nav-link "> <i class="fa  fa-exchange"></i> <span
		                        class="title">Ownership Change </span>
		                </a></li>
		            </ul>
		        </li>
		
		        <li class="nav-item " id="domRequestSubMenu">
		        	<a href="javascript:;" class="nav-link nav-toggle"> 
		        		<i  class="fa fa-envelope-o"></i> 
		        		<span class="title">Request</span> 
		        		<span  class="arrow"></span>
		        	</a>
		            <ul class="sub-menu">
		                <li class="nav-item " id="domSearchRequestSubMenu"><a
		                        href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>"
		                        class="nav-link "><i class="fa fa-search"></i><span
		                        class="title">Search </span></a>
		            </ul>
		        </li>
		        <li class="nav-item" id="common"><a href="javascript:;"
		                                            class="nav-link nav-toggle"> <i class="fa fa-money"></i> <span
		                class="title">Bill & Payment</span> <span class="arrow"></span>
		        </a>
		            <ul class="sub-menu">
		                <li class="nav-item" id="searchBill"><a
		                        href="${context}SearchBill.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>"
		                        class="nav-link "> <i class="fa fa-search"></i><span
		                        class="title"> Bill Search</span></a></li>
		                <li class="nav-item" id="domainPaymentSearch"><a
		                        href="${context}SearchPayment.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>"
		                        class="nav-link "> <i class="fa fa-money"></i><span
		                        class="title">Payment Search</span></a></li>
		            </ul>
		        </li>
		        <li class="nav-item  "
		            id="complainMenu<%=ModuleConstants.Module_ID_DOMAIN%>"><a
		                href="javascript:;" class="nav-link nav-toggle"> <i
		                class="fa fa-bug"></i> <span class="title">Complain</span> <span
		                class="arrow"></span>
		        </a>
		            <ul class="sub-menu">
		
		                <li class="nav-item  "
		                    id="complainAddMenu<%=ModuleConstants.Module_ID_DOMAIN%>"><a
		                        href="${context}complain/addComplain.jsp?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>"
		                        class="nav-link "><i class="fa fa-plus"></i> <span
		                        class="title">Add </span> </a></li>
		                <li class="nav-item  "
		                    id="complainSearchMenu<%=ModuleConstants.Module_ID_DOMAIN%>"><a
		                        href="${context}ComplainSearch.do?moduleID=<%=ModuleConstants.Module_ID_DOMAIN %>"
		                        class="nav-link "><i class="fa fa-search"></i> <span
		                        class="title">Search </span> </a></li>
		            </ul>
		        </li>
		    </ul>
		</li>
	<%}
}%>