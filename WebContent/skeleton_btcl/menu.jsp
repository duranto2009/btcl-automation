<%@page import="config.GlobalConfigConstants"%>
<%@page import="config.GlobalConfigurationRepository"%>
<%@page import="login.LoginDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%
	LoginDTO menuLoginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	Logger menuLogger = Logger.getLogger("menu_jsp");
	
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
    
    boolean migrationEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MIGRATION_ENABLED).getValue() > 0;
%>
<div class="page-sidebar-wrapper">
	<!-- BEGIN SIDEBAR -->
	<div class="page-sidebar navbar-collapse collapse">
		<!-- BEGIN SIDEBAR MENU -->
		<ul class="page-sidebar-menu   " data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200">
			
			<%if ((menuLoginDTO.getMenuPermission(login.PermissionConstants.WEBHOSTING) != -1) && moduleWebhostingEnabled){%><%@ include file="menu_webhosting_admin.jsp"%><%}%>
			<%if ((menuLoginDTO.getMenuPermission(login.PermissionConstants.DNSHOSTING) != -1) && moduleDnshostingEnabled){%><%@ include file="menu_dnshosting_admin.jsp"%><%}%>
			<%if ((menuLoginDTO.getMenuPermission(login.PermissionConstants.COLOCATION) != -1) && moduleColocationEnabled){%><%@ include file="menu_coloc_admin.jsp"%><%}%>
			<%if ((menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN) != -1) && moduleVPNEnabled){%><%@ include file="menu_vpn_admin.jsp"%><%}%>
			<%if ((menuLoginDTO.getMenuPermission(login.PermissionConstants.LLI) != -1) && moduleLliEnabled){%><%@ include file="menu_lli_admin_2018.jsp"%><%}%>
			<%if ((menuLoginDTO.getMenuPermission(login.PermissionConstants.ADSL) != -1) && moduleAdslEnabled){%><%@ include file="menu_adsl_admin.jsp"%><%}%>
			<%if ((menuLoginDTO.getMenuPermission(login.PermissionConstants.NIX) != -1) && moduleNixEnabled){%><%@ include file="menu_nix_admin.jsp"%><%}%>
			<%if ((menuLoginDTO).getMenuPermission(login.PermissionConstants.IPADDRESS) != -1 && moduleIpaddressEnabled){%><%@ include file="menu_ipaddress_admin.jsp"%><%}%>
			<%if (crmEnabled){%><%@ include file="menu_crm_admin.jsp"%><%}%>
			<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.USER) != -1){%><%@ include file="menu_user_admin.jsp"%><%}%>
			<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.GENERAL_REPORT) != -1){%><%@ include file="menu_global_report.jsp"%><%} %>
			<%if (menuLoginDTO.getMenuPermission(PermissionConstants.UPSTREAM) != -1) {%><%@ include file="menu_upstream_admin.jsp"%><%}%>
			<%if (menuLoginDTO.getMenuPermission(PermissionConstants.ADVANCED_SEARCH)!=-1){%><%@ include file="menu_global_search.jsp"%><%}%>
		</ul>
		<!-- END SIDEBAR MENU -->
	</div>
	<!-- END SIDEBAR -->
</div>
