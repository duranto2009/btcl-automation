<%@page import="common.CommonDAO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="config.GlobalConfigConstants"%>
<%@page import="config.GlobalConfigurationRepository"%>
<%@page import="login.LoginDTO"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="request.StateDTO"%>
<%@page import="request.StateRepository"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="vpn.client.ClientDetailsDTO"%>
<%
	LoginDTO menuLoginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	Logger loggerMenuClient = Logger.getLogger("loggerMenuClient");

    boolean moduleDomainEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_DOMAIN).getValue() > 0;
    boolean moduleWebhostingEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_WEBHOSTING).getValue() > 0;
    boolean moduleIpaddressEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_IPADDRESS).getValue() > 0;
    boolean moduleColocationEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_COLOCATION).getValue() > 0;
    boolean moduleIigEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_IIG).getValue() > 0;
    boolean moduleVPNEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_VPN).getValue() > 0;
    boolean moduleLliEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_LLI).getValue() > 0;
    boolean moduleAdslEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_ADSL).getValue() > 0;
    boolean moduleNixEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_NIX).getValue() > 0;
    boolean moduleDnshostingEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_DNSHOSTING).getValue() > 0;
    
    boolean crmEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.Module_ID_CRM).getValue() > 0;
	
    CommonDAO commonDAO = new CommonDAO();
	
	//Check if client has accounts in any modules
	StateDTO sdto = null;
	//DOMAIN
	int statusClientDomain = 0;	
	int activationStatusDomain = -1;
	boolean registeredDomain = false;
	ClientDetailsDTO domainClientDTOMenu = AllClientRepository.getInstance().getVpnClientByClientID(menuLoginDTO.getAccountID(), ModuleConstants.Module_ID_DOMAIN);
	loggerMenuClient.debug("domainClientDTOMenu " + domainClientDTOMenu);
	if(moduleDomainEnabled){
		if(domainClientDTOMenu != null){
			statusClientDomain = domainClientDTOMenu.getCurrentStatus();
			sdto = StateRepository.getInstance().getStateDTOByStateID(statusClientDomain);
			if(sdto != null){
				activationStatusDomain = sdto.getActivationStatus();
			}
			if(activationStatusDomain != EntityTypeConstant.STATUS_NOT_ACTIVE){
				registeredDomain =true;	
			}
		}
	}
	loggerMenuClient.debug("statusClientDomain " + statusClientDomain);
	loggerMenuClient.debug("sdto " + sdto);	
	//WEBHOSTING
	int statusClientWebhosting = 0;	
	int activationStatusWebhosting = -1;
	boolean registeredWebhosting = false;
	ClientDetailsDTO webhostingClientDTOMenu = AllClientRepository.getInstance().getVpnClientByClientID(menuLoginDTO.getAccountID(), ModuleConstants.Module_ID_WEBHOSTING);
	loggerMenuClient.debug("webhostingnClientDTOMenu " + webhostingClientDTOMenu);
	if(moduleWebhostingEnabled){
		if(webhostingClientDTOMenu != null){
			statusClientWebhosting = webhostingClientDTOMenu.getCurrentStatus();
			sdto = StateRepository.getInstance().getStateDTOByStateID(statusClientWebhosting);
			if(sdto != null){
				activationStatusWebhosting = sdto.getActivationStatus();
			}
			if(activationStatusWebhosting != EntityTypeConstant.STATUS_NOT_ACTIVE){
				registeredWebhosting =true;	
			}
		}
	}
	loggerMenuClient.debug("statusClientWebhosting " + statusClientWebhosting);
	loggerMenuClient.debug("sdto " + sdto);	
	//IPADDRESS
	int statusClientIpaddress = 0;	
	int activationStatusIpaddress = -1;
	boolean registeredIpaddress = false;
	ClientDetailsDTO ipaddressClientDTOMenu = AllClientRepository.getInstance().getVpnClientByClientID(menuLoginDTO.getAccountID(), ModuleConstants.Module_ID_IPADDRESS);
	loggerMenuClient.debug("ipaddressClientDTOMenu " + ipaddressClientDTOMenu);
	if(moduleIpaddressEnabled){
		if(ipaddressClientDTOMenu != null){
			statusClientIpaddress = ipaddressClientDTOMenu.getCurrentStatus();
			sdto = StateRepository.getInstance().getStateDTOByStateID(statusClientIpaddress);
			if(sdto != null){
				activationStatusIpaddress = sdto.getActivationStatus();
			}
			if(activationStatusIpaddress != EntityTypeConstant.STATUS_NOT_ACTIVE){
				registeredIpaddress =true;	
			}
		}
	}
	loggerMenuClient.debug("statusClientIpaddress " + statusClientIpaddress);
	loggerMenuClient.debug("sdto " + sdto);	
	//COLOCATION
	int statusClientColocation = 0;	
	int activationStatusColocation = -1;
	boolean registeredColocation = false;
	ClientDetailsDTO colocationClientDTOMenu = AllClientRepository.getInstance().getVpnClientByClientID(menuLoginDTO.getAccountID(), ModuleConstants.Module_ID_COLOCATION);
	loggerMenuClient.debug("colocationClientDTOMenu " + colocationClientDTOMenu);
	if(moduleColocationEnabled){
		if(colocationClientDTOMenu != null){
			statusClientColocation = colocationClientDTOMenu.getCurrentStatus();
			sdto = StateRepository.getInstance().getStateDTOByStateID(statusClientColocation);
			if(sdto != null){
				activationStatusColocation = sdto.getActivationStatus();
			}
			if(activationStatusColocation != EntityTypeConstant.STATUS_NOT_ACTIVE){
				registeredColocation =true;	
			}
		}
	}
	loggerMenuClient.debug("statusClientColocation " + statusClientColocation);
	loggerMenuClient.debug("sdto " + sdto);	
	//IIG
	int statusClientIig = 0;	
	int activationStatusIig = -1;
	boolean registeredIig = false;
	ClientDetailsDTO iigClientDTOMenu = AllClientRepository.getInstance().getVpnClientByClientID(menuLoginDTO.getAccountID(), ModuleConstants.Module_ID_IIG);
	loggerMenuClient.debug("iigClientDTOMenu " + iigClientDTOMenu);
	if(moduleIigEnabled){
		if(iigClientDTOMenu != null){
			statusClientIig = iigClientDTOMenu.getCurrentStatus();
			sdto = StateRepository.getInstance().getStateDTOByStateID(statusClientIig);
			if(sdto != null){
				activationStatusIig = sdto.getActivationStatus();
			}
			if(activationStatusIig != EntityTypeConstant.STATUS_NOT_ACTIVE){
				registeredIig =true;	
			}
		}
	}
	loggerMenuClient.debug("statusClientIig " + statusClientIig);
	loggerMenuClient.debug("sdto " + sdto);	
	//VPN
    int statusClientVpn = 0;
	int activationStatusVpn = -1;
	boolean registeredVpn = false;
	if(moduleVPNEnabled){
		ClientDetailsDTO clientDetailsDTOMenu = AllClientRepository.getInstance().getVpnClientByClientID(menuLoginDTO.getAccountID(), ModuleConstants.Module_ID_VPN);
		if(clientDetailsDTOMenu != null){
			statusClientVpn = clientDetailsDTOMenu.getCurrentStatus();
			sdto = StateRepository.getInstance().getStateDTOByStateID(statusClientVpn);
			if(sdto != null){
				activationStatusVpn = sdto.getActivationStatus();
			}
			if(activationStatusVpn != EntityTypeConstant.STATUS_NOT_ACTIVE){
				registeredVpn =true;	
			}
		}
	}
	loggerMenuClient.debug("statusClientVpn " + statusClientVpn);
	loggerMenuClient.debug("sdto " + sdto);
	//LLI
	int statusClientLli = 0;	
	int activationStatusLli = -1;
	boolean registeredLli = false;
	ClientDetailsDTO lliClientDTOMenu = AllClientRepository.getInstance().getVpnClientByClientID(menuLoginDTO.getAccountID(), ModuleConstants.Module_ID_LLI);
	loggerMenuClient.debug("lliClientDTOMenu " + lliClientDTOMenu);
	if(moduleLliEnabled){
		if(lliClientDTOMenu != null){
			statusClientLli = lliClientDTOMenu.getCurrentStatus();
			sdto = StateRepository.getInstance().getStateDTOByStateID(statusClientLli);
			if(sdto != null){
				activationStatusLli = sdto.getActivationStatus();
			}
			if(activationStatusLli != EntityTypeConstant.STATUS_NOT_ACTIVE){
				registeredLli =true;	
			}
		}
	}
	loggerMenuClient.debug("statusClientLli " + statusClientLli);
	loggerMenuClient.debug("sdto " + sdto);	
	//ADSL
	int statusClientAdsl = 0;	
	int activationStatusAdsl = -1;
	boolean registeredAdsl = false;
	ClientDetailsDTO adslClientDTOMenu = AllClientRepository.getInstance().getVpnClientByClientID(menuLoginDTO.getAccountID(), ModuleConstants.Module_ID_ADSL);
	loggerMenuClient.debug("adslClientDTOMenu " + adslClientDTOMenu);
	if(moduleAdslEnabled){
		if(adslClientDTOMenu != null){
			statusClientAdsl = adslClientDTOMenu.getCurrentStatus();
			sdto = StateRepository.getInstance().getStateDTOByStateID(statusClientAdsl);
			if(sdto != null){
				activationStatusAdsl = sdto.getActivationStatus();
			}
			if(activationStatusAdsl != EntityTypeConstant.STATUS_NOT_ACTIVE){
				registeredAdsl =true;	
			}
		}
	}
	loggerMenuClient.debug("statusClientAdsl " + statusClientAdsl);
	loggerMenuClient.debug("sdto " + sdto);	
	//NIX
	int statusClientNix = 0;	
	int activationStatusNix = -1;
	boolean registeredNix = false;
	ClientDetailsDTO nixClientDTOMenu = AllClientRepository.getInstance().getVpnClientByClientID(menuLoginDTO.getAccountID(), ModuleConstants.Module_ID_NIX);
	loggerMenuClient.debug("nixClientDTOMenu " + nixClientDTOMenu);
	if(moduleNixEnabled){
		if(nixClientDTOMenu != null){
			statusClientNix = nixClientDTOMenu.getCurrentStatus();
			sdto = StateRepository.getInstance().getStateDTOByStateID(statusClientNix);
			if(sdto != null){
				activationStatusNix = sdto.getActivationStatus();
			}
			if(activationStatusNix != EntityTypeConstant.STATUS_NOT_ACTIVE){
				registeredNix =true;	
			}
		}
	}
	loggerMenuClient.debug("statusClientNix " + statusClientNix);
	loggerMenuClient.debug("sdto " + sdto);	
	//DNSHOSTING
	int statusClientDnshosting = 0;	
	int activationStatusDnshosting = -1;
	boolean registeredDnshosting = false;
	ClientDetailsDTO dnshostingClientDTOMenu = AllClientRepository.getInstance().getVpnClientByClientID(menuLoginDTO.getAccountID(), ModuleConstants.Module_ID_DNSHOSTING);
	loggerMenuClient.debug("dnshostingClientDTOMenu " + dnshostingClientDTOMenu);
	if(moduleDnshostingEnabled){
		if(dnshostingClientDTOMenu != null){
			statusClientDnshosting = dnshostingClientDTOMenu.getCurrentStatus();
			sdto = StateRepository.getInstance().getStateDTOByStateID(statusClientDnshosting);
			if(sdto != null){
				activationStatusDnshosting = sdto.getActivationStatus();
			}
			if(activationStatusDnshosting != EntityTypeConstant.STATUS_NOT_ACTIVE){
				registeredDnshosting =true;	
			}
		}
	}
	loggerMenuClient.debug("statusClientDnshosting " + statusClientDnshosting);
	loggerMenuClient.debug("sdto " + sdto);	
	
	
	request.setAttribute("isClientRegisteredToDomain", registeredDomain);
	request.setAttribute("isClientRegisteredToWebhosting", registeredWebhosting);
	request.setAttribute("isClientRegisteredToIpaddress", registeredIpaddress);
	request.setAttribute("isClientRegisteredToColocation", registeredColocation);
	request.setAttribute("isClientRegisteredToIig", registeredIig);
	request.setAttribute("isClientRegisteredToVpn", registeredVpn);
	request.setAttribute("isClientRegisteredToLli", registeredLli);
	request.setAttribute("isClientRegisteredToAdsl", registeredAdsl);
	request.setAttribute("isClientRegisteredToNix", registeredNix);
	request.setAttribute("isClientRegisteredToDnshosting", registeredDnshosting);
	
	
%>
<div class="page-sidebar-wrapper">
	<!-- BEGIN SIDEBAR -->
	<!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
	<!-- DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed -->
	<div class="page-sidebar navbar-collapse collapse">
		<!-- BEGIN SIDEBAR MENU -->
		<!-- DOC: Apply "page-sidebar-menu-light" class right after "page-sidebar-menu" to enable light sidebar menu style(without borders) -->
		<!-- DOC: Apply "page-sidebar-menu-hover-submenu" class right after "page-sidebar-menu" to enable hoverable(hover vs accordion) sub menu mode -->
		<!-- DOC: Apply "page-sidebar-menu-closed" class right after "page-sidebar-menu" to collapse("page-sidebar-closed" class must be applied to the body element) the sidebar sub menu mode -->
		<!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
		<!-- DOC: Set data-keep-expand="true" to keep the submenues expanded -->
		<!-- DOC: Set data-auto-speed="200" to adjust the sub menu slide up/down speed -->
		<%
			try {
		%>
		<ul class="page-sidebar-menu   " data-keep-expanded="false"
			data-auto-scroll="true" data-slide-speed="200">
			<li class="nav-item start "><a href="${context}Welcome.do"
				class="nav-link"> <i class="icon-home"></i> <span class="title">Dashboard</span>
			</a></li>
			<li class="heading">
				<h3 class="uppercase">Modules</h3>
			</li>
			
				<%--<%if (moduleDomainEnabled){%><%@ include file="menu_domain_client.jsp"%><%}%>--%>
				
				<%if (moduleWebhostingEnabled){%><%@ include file="menu_webhosting_client.jsp"%><%}%>
				
				<%if (moduleDnshostingEnabled){%><%@ include file="menu_dnshosting_client.jsp"%><%}%>
				
				<%-- <%if (moduleIpaddressEnabled){%><%@ include file="menu_ipaddress_client.jsp"%><%}%> --%>
				
				<%if (moduleColocationEnabled){%><%@ include file="menu_coloc_client.jsp"%><%}%>
				
				<%-- <%if (moduleIigEnabled){%><%@ include file="menu_iig_client.jsp"%><%}%> --%>
				
				<%if (moduleVPNEnabled){%><%@ include file="menu_vpn_client.jsp"%><%}%>
				
				<%if (moduleLliEnabled){%><%@ include file="menu_lli_client_2018.jsp"%><%}%>
				
				<%if (moduleAdslEnabled){%><%@ include file="menu_adsl_client.jsp"%><%}%>
				
				<%if (moduleNixEnabled){%><%@ include file="menu_nix_client.jsp"%><%}%>


				<%if(crmEnabled) {%><%@ include file="menu_crm_client.jsp"%><%} %>
		</ul>
		<%
			} catch (Exception ex) {
				loggerMenuClient.debug("Exception ", ex);
			}
		%>
		<!-- END SIDEBAR MENU -->
	</div>
	<!-- END SIDEBAR -->
</div>
