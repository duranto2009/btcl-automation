package common.constants;

import coLocation.ColocationRequestTypeConstants;
import common.ModuleConstants;
import dnshosting.constants.DnshostingRequestTypeConstants;
import iig.constants.IigRequestTypeConstants;
import lli.constants.LliRequestTypeConstants;
import nix.constants.NixRequestTypeConstants;
import webHosting.constants.WebHostingRequestTypeConstants;

import java.util.*;

//import coLocation.ColocationRequestTypeConstants;

public class RequestTypeConstants {
	public static HashMap<Integer, Class> RequestTypeMapByModuleID = new HashMap<Integer, Class>();
	static
	{
//		RequestTypeMapByModuleID.put(ModuleConstants.Module_ID_DOMAIN, DomainRequestTypeConstants.class);
		RequestTypeMapByModuleID.put(ModuleConstants.Module_ID_WEBHOSTING, WebHostingRequestTypeConstants.class);
//		RequestTypeMapByModuleID.put(ModuleConstants.Module_ID_IPADDRESS, IpaddressRequestTypeConstants.class);
		RequestTypeMapByModuleID.put(ModuleConstants.Module_ID_COLOCATION, ColocationRequestTypeConstants.class);
		RequestTypeMapByModuleID.put(ModuleConstants.Module_ID_IIG, IigRequestTypeConstants.class);
//		RequestTypeMapByModuleID.put(ModuleConstants.Module_ID_VPN, VpnRequestTypeConstants.class);
		RequestTypeMapByModuleID.put(ModuleConstants.Module_ID_LLI, LliRequestTypeConstants.class);
		RequestTypeMapByModuleID.put(ModuleConstants.Module_ID_NIX, NixRequestTypeConstants.class);
		RequestTypeMapByModuleID.put(ModuleConstants.Module_ID_DNSHOSTING, DnshostingRequestTypeConstants.class);
	}
	
	public static Map<Integer, List<Integer>> filteredOutRequestTypeIDByModuleID = new HashMap<>();
	
	public static List<Integer> domainFilteredOutList = new ArrayList<Integer>();
	public static List<Integer> vpnFilteredOutList = new ArrayList<Integer>();
	public static List<Integer> lliFilteredOutList = new ArrayList<Integer>();
	
	static {
//		vpnFilteredOutList.add(VpnRequestTypeConstants.REQUEST_NEW_CONNECTION.CLIENT_APPLY);
		lliFilteredOutList.add(LliRequestTypeConstants.REQUEST_NEW_CONNECTION.CLIENT_APPLY);
	}
	
	static
	{
		filteredOutRequestTypeIDByModuleID.put(ModuleConstants.Module_ID_DOMAIN, domainFilteredOutList);
		filteredOutRequestTypeIDByModuleID.put(ModuleConstants.Module_ID_VPN, vpnFilteredOutList);
		filteredOutRequestTypeIDByModuleID.put(ModuleConstants.Module_ID_LLI, lliFilteredOutList);
		filteredOutRequestTypeIDByModuleID.put(ModuleConstants.Module_ID_NIX, Collections.emptyList());
		filteredOutRequestTypeIDByModuleID.put(ModuleConstants.Module_ID_COLOCATION, Collections.emptyList());
	}
	
	public static Set<Integer> migrationActionSet = new HashSet();
	static {
//		migrationActionSet.add(VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_SKIP_DEMAND_NOTE_DEMAND_NOTE_GENERATION);
		migrationActionSet.add(LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_SKIP_DEMAND_NOTE_DEMAND_NOTE_GENERATION);
	}
}
