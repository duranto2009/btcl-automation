package common;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionRedirect;

public class BtclRedirectUtility {
	public static String getTarget(String modduleEntityStr, String status) {
		return modduleEntityStr + status;
	}

	public static String getTarget(int entityID, String action, String status) {
		if (EntityTypeConstant.DOMAIN_CLIENT == entityID) {
			return "domainClient" + action + status;
		} else if (EntityTypeConstant.WEBHOSTING_CLIENT == entityID) {
			return "webHostingClient" + action + status;
		} else if (EntityTypeConstant.IPADDRESS_CLIENT == entityID) {
			return "ipaddressClient" + action + status;
		} else if (EntityTypeConstant.COLOCATION_CLIENT == entityID) {
			return "colocationClient" + action + status;
		} else if (EntityTypeConstant.IIG_CLIENT == entityID) {
			return "iigClient" + action + status;
		} else if (EntityTypeConstant.VPN_CLIENT == entityID) {
			return "vpnClient" + action + status;
		} else if (EntityTypeConstant.LLI_CLIENT == entityID) {
			return "lliClient" + action + status;
		} else if (EntityTypeConstant.ADSL_CLIENT == entityID) {
			return "adslClient" + action + status;
		} else if (EntityTypeConstant.NIX_CLIENT == entityID) {
			return "nixClient" + action + status;
		} else if (EntityTypeConstant.DNSHOSTING_CLIENT == entityID) {
			return "dnshostingClient" + action + status;
		}
		return "login";
	}

	public static String getTarget(int moduleID, String entity, String action, String status) {
		if (ModuleConstants.Module_ID_DOMAIN == moduleID) {
			return "domain" + entity + action + status;
		} else if (ModuleConstants.Module_ID_WEBHOSTING == moduleID) {
			return "webHosting" + entity + action + status;
		} else if (ModuleConstants.Module_ID_IPADDRESS == moduleID) {
			return "ipaddress" + entity + action + status;
		} else if (ModuleConstants.Module_ID_COLOCATION == moduleID) {
			return "colocation" + entity + action + status;
		} else if (ModuleConstants.Module_ID_IIG == moduleID) {
			return "iig" + entity + action + status;
		} else if (ModuleConstants.Module_ID_VPN == moduleID) {
			return "vpn" + entity + action + status;
		} else if (ModuleConstants.Module_ID_LLI == moduleID) {
			return "lli" + entity + action + status;
		} else if (ModuleConstants.Module_ID_ADSL == moduleID) {
			return "adsl" + entity + action + status;
		} else if (ModuleConstants.Module_ID_NIX == moduleID) {
			return "nix" + entity + action + status;
		} else if (ModuleConstants.Module_ID_DNSHOSTING == moduleID) {
			return "dnshosting" + entity + action + status;
		} 
		return "login";
	}

	public static void setForwardParameters(ActionRedirect forward, int moduleID, long entityID) {
		forward.addParameter("entityID", entityID);
		forward.addParameter("moduleID", moduleID);
		forward.addParameter("currentTab", 1);
	}

	public static void setForwardParameters(ActionRedirect forward, int moduleID, long entityID, int entityTypeID,
			int currentTab) {
		forward.addParameter("moduleID", moduleID);
		forward.addParameter("entityID", entityID);
		forward.addParameter("entityTypeID", entityTypeID);
		forward.addParameter("currentTab", currentTab);
	}

	public static String getCommentTarget(int moduleID, String entity, String action, String status) {
		if (ModuleConstants.Module_ID_DOMAIN == moduleID) {
			return "domain" + entity + action + status;
		} else if (ModuleConstants.Module_ID_WEBHOSTING == moduleID) {
			return "webHosting" + entity + action + status;
		} else if (ModuleConstants.Module_ID_IPADDRESS == moduleID) {
			return "ipaddress" + entity + action + status;
		} else if (ModuleConstants.Module_ID_COLOCATION == moduleID) {
			return "colocation" + entity + action + status;
		} else if (ModuleConstants.Module_ID_IIG == moduleID) {
			return "iig" + entity + action + status;
		} else if (ModuleConstants.Module_ID_VPN == moduleID) {
			return "vpn" + entity + action + status;
		} else if (ModuleConstants.Module_ID_LLI == moduleID) {
			return "lli" + entity + action + status;
		} else if (ModuleConstants.Module_ID_ADSL == moduleID) {
			return "adsl" + entity + action + status;
		} else if (ModuleConstants.Module_ID_NIX == moduleID) {
			return "nix" + entity + action + status;
		} else if (ModuleConstants.Module_ID_DNSHOSTING == moduleID) {
			return "dnshosting" + entity + action + status;
		}
		return "login";
	}

	public static String getCommentTarget(int moduleID, int entityTypeID) {
		String moduleStr = "";
		String entityStr = "";

		if (ModuleConstants.Module_ID_DOMAIN == moduleID) {
			moduleStr = "domain";
		} else if (ModuleConstants.Module_ID_WEBHOSTING== moduleID) {
			moduleStr = "webHosting";
		} else if (ModuleConstants.Module_ID_IPADDRESS== moduleID) {
			moduleStr = "ipaddress";
		} else if (ModuleConstants.Module_ID_COLOCATION== moduleID) {
			moduleStr = "colocation";
		} else if (ModuleConstants.Module_ID_IIG== moduleID) {
			moduleStr = "iig";
		} else if (ModuleConstants.Module_ID_VPN == moduleID) {
			moduleStr = "vpn";
		} else if (ModuleConstants.Module_ID_LLI== moduleID) {
			moduleStr = "lli";
		} else if (ModuleConstants.Module_ID_ADSL== moduleID) {
			moduleStr = "adsl";
		} else if (ModuleConstants.Module_ID_NIX== moduleID) {
			moduleStr = "nix";
		} else if (ModuleConstants.Module_ID_DNSHOSTING== moduleID) {
			moduleStr = "dnshosting";
		}
		
		

		if ((EntityTypeConstant.DOMAIN_CLIENT == entityTypeID) || (EntityTypeConstant.WEBHOSTING_CLIENT == entityTypeID) || (EntityTypeConstant.IPADDRESS_CLIENT == entityTypeID)
				|| (EntityTypeConstant.COLOCATION_CLIENT == entityTypeID) || (EntityTypeConstant.IIG_CLIENT == entityTypeID) || (EntityTypeConstant.VPN_CLIENT == entityTypeID)
				|| (EntityTypeConstant.LLI_CLIENT == entityTypeID) || (EntityTypeConstant.ADSL_CLIENT == entityTypeID) || (EntityTypeConstant.NIX_CLIENT == entityTypeID)
				|| (EntityTypeConstant.DNSHOSTING_CLIENT == entityTypeID)) {
			entityStr = "Client";
		} else if ((EntityTypeConstant.VPN_LINK == entityTypeID) || (EntityTypeConstant.VPN_LINK == entityTypeID)) {
			entityStr = "Link";
		} else if ((EntityTypeConstant.DOMAIN == entityTypeID) || (EntityTypeConstant.WEBHOSTING == entityTypeID)) {
			entityStr = "View";
		}
		String forwardPath = moduleStr + entityStr;

		if (StringUtils.isNotBlank(forwardPath)) {
			return forwardPath;
		}
		return "login";
	}

}
