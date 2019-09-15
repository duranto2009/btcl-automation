package util;

import common.ModuleConstants;
 
public class ForwardView {
	public static String getTarget(int moduleID, String status) {

		if (ModuleConstants.Module_ID_DOMAIN == moduleID) {
			return "domain" + status;
		} else if (ModuleConstants.Module_ID_WEBHOSTING == moduleID) {
			return "webHosting" + status;
		} else if (ModuleConstants.Module_ID_IPADDRESS == moduleID) {
			return "ipaddress" + status;
		} else if (ModuleConstants.Module_ID_COLOCATION == moduleID) {
			return "colocation" + status;
		} else if (ModuleConstants.Module_ID_IIG == moduleID) {
			return "iig" + status;
		} else if (ModuleConstants.Module_ID_VPN == moduleID) {
			return "vpn" + status;
		} else if (ModuleConstants.Module_ID_LLI == moduleID) {
			return "lli" + status;
		} else if (ModuleConstants.Module_ID_ADSL == moduleID) {
			return "adsl" + status;
		} else if (ModuleConstants.Module_ID_NIX == moduleID) {
			return "nix" + status;
		} else if (ModuleConstants.Module_ID_DNSHOSTING == moduleID) {
			return "dnshosting" + status;
		} 
		return null;

	}

	public static String getTarget(int moduleID) {

		if (ModuleConstants.Module_ID_DOMAIN == moduleID) {
			return "domain";
		} else {
			return "view";
		} 
	}

}
