package common;

import coLocation.CoLocationConstants;
import coLocation.connection.CoLocationConnectionDTO;
import common.bill.BillDTO;
import crm.CrmActivityLog;
import lli.LLIConnectionInstance;
import lli.connection.LLIConnectionConstants;
import nix.connection.NIXConnection;
import nix.constants.NIXConstants;
import request.CommonRequestDTO;
import vpn.VPNConstants;
import vpn.bill.VpnBillDTO;
import vpn.client.ClientDetailsDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityTypeConstant {
	public static final int MULTIPLIER2 = 100;
	
	public static final int COMMENT = 3;
	public static final int REQUEST = 50;/*OK*/
	public static final int COMMON_REQUEST = 101;
	
	public static final int STATUS_CURRENT = 1;
	public static final int STATUS_LATEST = 2;
	public static final int STATUS_CURRENT_AND_LATEST = 10;
	
	
	public static final int DOMAIN = ModuleConstants.Module_ID_DOMAIN * MULTIPLIER2 + 1;
	public static final int DOMAIN_CLIENT = ModuleConstants.Module_ID_DOMAIN * MULTIPLIER2 + 51;
	
	public static final int WEBHOSTING = ModuleConstants.Module_ID_WEBHOSTING * MULTIPLIER2 + 1;
	public static final int WEBHOSTING_CLIENT = ModuleConstants.Module_ID_WEBHOSTING * MULTIPLIER2 + 51;
	
	public static final int IPADDRESS = ModuleConstants.Module_ID_IPADDRESS * MULTIPLIER2 + 1;
	public static final int IPADDRESS_CLIENT = ModuleConstants.Module_ID_IPADDRESS * MULTIPLIER2 + 51;
	
	public static final int COLOCATION = ModuleConstants.Module_ID_COLOCATION * MULTIPLIER2 + 1;
	public static final int COLOCATION_CLIENT = ModuleConstants.Module_ID_COLOCATION * MULTIPLIER2 + 51;
	
	public static final int IIG = ModuleConstants.Module_ID_IIG * MULTIPLIER2 + 1;
	public static final int IIG_CLIENT = ModuleConstants.Module_ID_IIG * MULTIPLIER2 + 51;
	
	public static final int VPN_CONNECTION = ModuleConstants.Module_ID_VPN * MULTIPLIER2 +  1;
	public static final int VPN_LINK = ModuleConstants.Module_ID_VPN * MULTIPLIER2 + 2;
	public static final int VPN_LINK_NEAR_END = ModuleConstants.Module_ID_VPN * MULTIPLIER2 + 3;
	public static final int VPN_LINK_FAR_END = ModuleConstants.Module_ID_VPN * MULTIPLIER2 + 4;
	public static final int VPN_CLIENT = ModuleConstants.Module_ID_VPN * MULTIPLIER2 + 51;
	
	//public static final int LLI_CONNECTION = ModuleConstants.Module_ID_LLI * MULTIPLIER2 +  1;
	public static final int LLI_LINK = ModuleConstants.Module_ID_LLI * MULTIPLIER2 + 2;
	public static final int LLI_LINK_FAR_END = ModuleConstants.Module_ID_LLI * MULTIPLIER2 + 4;
	public static final int LLI_CLIENT = ModuleConstants.Module_ID_LLI * MULTIPLIER2 + 51;
	
	public static final int ADSL = ModuleConstants.Module_ID_ADSL * MULTIPLIER2 + 1;
	public static final int ADSL_CLIENT = ModuleConstants.Module_ID_ADSL * MULTIPLIER2 + 51;
	
	public static final int NIX = ModuleConstants.Module_ID_NIX * MULTIPLIER2 + 1;
	public static final int NIX_CLIENT = ModuleConstants.Module_ID_NIX * MULTIPLIER2 + 51;
	
	public static final int DNSHOSTING = ModuleConstants.Module_ID_DNSHOSTING * MULTIPLIER2 + 1;
	public static final int DNSHOSTING_CLIENT = ModuleConstants.Module_ID_DNSHOSTING * MULTIPLIER2 + 51;
	
	
	public static final int INVENTORY = 99 * MULTIPLIER2;
	public static final int INVENTORY_ROUTER = 99 * MULTIPLIER2 + CategoryConstants.CATEGORY_ID_ROUTER;
	public static final int INVENTORY_PORT = 99 * MULTIPLIER2 + CategoryConstants.CATEGORY_ID_PORT;
	
	
	public static final int COMPLAIN = ModuleConstants.Module_ID_COMPLAIN * MULTIPLIER2 + 1;
	public static final int CRM = ModuleConstants.Module_ID_CRM * MULTIPLIER2 + 1;
	public static final int CRM_CLIENT_COMPLAIN = ModuleConstants.Module_ID_CRM * MULTIPLIER2 + 2;
	public static final int CRM_COMPLAIN = ModuleConstants.Module_ID_CRM * MULTIPLIER2 + 3;
	public static HashMap<Integer, Class<? extends EntityDTO>> entityClassMap = new HashMap<>();
	static {
//		entityClassMap.put(VPN_LINK, VpnLinkDTO.class);
//		entityClassMap.put(VPN_CONNECTION, VpnConnectionDTO.class);
//		entityClassMap.put(VPN_LINK_NEAR_END, VpnNearEndDTO.class);
//		entityClassMap.put(VPN_LINK_FAR_END, VpnFarEndDTO.class);
		entityClassMap.put(VPN_CLIENT, ClientDetailsDTO.class);
//		entityClassMap.put(LLI_LINK_FAR_END, VpnFarEndDTO.class);
		entityClassMap.put(LLI_CLIENT, ClientDetailsDTO.class);		


	}
	
	public static HashMap<Integer, Class<? extends BillDTO>> entityBillClassMap = new HashMap<>();
	static {
		entityBillClassMap.put(VPN_LINK, VpnBillDTO.class);
//		entityBillClassMap.put(LLI_LINK, LliBillDTO.class);
	}
	
	public static HashMap<Integer, Class<? extends CommonRequestDTO>> requestTypeExtentendedRequestClassMap = new HashMap<>();
	static {
//		requestTypeExtentendedRequestClassMap.put(VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_INTERNAL_FR, VpnFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(- VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_INTERNAL_FR, VpnFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(VpnRequestTypeConstants.REQUEST_NEW_LINK.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, VpnFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(- VpnRequestTypeConstants.REQUEST_NEW_LINK.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, VpnFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_NEAR_END, VpnFRResponseExternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_NEAR_END, VpnFRResponseExternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END, VpnFRResponseExternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END, VpnFRResponseExternalDTO.class);
//
//		requestTypeExtentendedRequestClassMap.put(VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR, VpnFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(- VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR, VpnFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(VpnRequestTypeConstants.REQUEST_UPGRADE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, VpnFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(- VpnRequestTypeConstants.REQUEST_UPGRADE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, VpnFRResponseInternalDTO.class);
//
//		requestTypeExtentendedRequestClassMap.put(VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR, VpnFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(- VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR, VpnFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(VpnRequestTypeConstants.REQUEST_DOWNGRADE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, VpnFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(- VpnRequestTypeConstants.REQUEST_DOWNGRADE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, VpnFRResponseInternalDTO.class);

		
//		requestTypeExtentendedRequestClassMap.put(LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_INTERNAL_FR, LliFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(- LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_INTERNAL_FR, LliFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(LliRequestTypeConstants.REQUEST_NEW_LINK.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, LliFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(- LliRequestTypeConstants.REQUEST_NEW_LINK.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, LliFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_NEAR_END, LliFRResponseExternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_NEAR_END, LliFRResponseExternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END, LliFRResponseExternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END, LliFRResponseExternalDTO.class);
//
//		requestTypeExtentendedRequestClassMap.put(LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR, LliFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(- LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR, LliFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(LliRequestTypeConstants.REQUEST_UPGRADE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, LliFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(- LliRequestTypeConstants.REQUEST_UPGRADE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, LliFRResponseInternalDTO.class);
//
//		requestTypeExtentendedRequestClassMap.put(LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR, LliFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(- LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR, LliFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(LliRequestTypeConstants.REQUEST_DOWNGRADE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, LliFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(- LliRequestTypeConstants.REQUEST_DOWNGRADE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, LliFRResponseInternalDTO.class);
//
//		requestTypeExtentendedRequestClassMap.put(LliRequestTypeConstants.REQUEST_IPADDRESS.SYSTEM_RESPONSE_WITH_INTERNAL_FR, LliFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(- LliRequestTypeConstants.REQUEST_IPADDRESS.SYSTEM_RESPONSE_WITH_INTERNAL_FR, LliFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(LliRequestTypeConstants.REQUEST_IPADDRESS.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, LliFRResponseInternalDTO.class);
//		requestTypeExtentendedRequestClassMap.put(- LliRequestTypeConstants.REQUEST_IPADDRESS.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, LliFRResponseInternalDTO.class);

	}

	public static HashMap<Integer, Class> hasSuperClassMap = new HashMap<Integer, Class>();
	static {
//		hasSuperClassMap.put(VPN_LINK_NEAR_END, VpnEndPointDTO.class);
//		hasSuperClassMap.put(VPN_LINK_FAR_END, VpnEndPointDTO.class);
	}
	

	
//	static
//	{
//		entityClassMap.put(LLI_LINK, LliLinkDTO.class);
//		entityClassMap.put(LLI_LINK_FAR_END, LliFarEndDTO.class);
//	}
	
	public static Map<Class,Integer> mapOfModuleIDToEntityClass =  new HashMap<Class, Integer>();
	static{

//		mapOfModuleIDToEntityClass.put(VpnLinkDTO.class, ModuleConstants.Module_ID_VPN);
//		mapOfModuleIDToEntityClass.put(VpnConnectionDTO.class, ModuleConstants.Module_ID_VPN);
		mapOfModuleIDToEntityClass.put(ClientDetailsDTO.class, ModuleConstants.Module_ID_CLIENT);
		
//		mapOfModuleIDToEntityClass.put(LliLinkDTO.class, ModuleConstants.Module_ID_LLI);
		


	}
	public static HashMap<Integer, String> entityNameMap = new HashMap<Integer, String>();

	static
	{
		entityNameMap.put(DOMAIN,"Domain");
		entityNameMap.put(DOMAIN_CLIENT,"Domain Client");
		entityNameMap.put(WEBHOSTING,"Web Hosting");
		entityNameMap.put(WEBHOSTING_CLIENT,"Web Hosting Client");
		entityNameMap.put(IPADDRESS,"IP Address");
		entityNameMap.put(IPADDRESS_CLIENT,"IP Address Client");
		entityNameMap.put(COLOCATION,"Co Location");
		entityNameMap.put(COLOCATION_CLIENT,"Co Location Client");
		entityNameMap.put(IIG,"IIG");
		entityNameMap.put(IIG_CLIENT,"IIG Client");
		entityNameMap.put(VPN_CONNECTION,"VPN Connection");
		entityNameMap.put(VPN_LINK,"VPN Link");
		entityNameMap.put(VPN_LINK_NEAR_END,"VPN Link Near End");
		entityNameMap.put(VPN_LINK_FAR_END,"VPN Link Far End");
		entityNameMap.put(VPN_CLIENT,"VPN Client");
		entityNameMap.put(LLI_LINK,"LLI Connection");
		entityNameMap.put(LLI_LINK_FAR_END,"LLI Connection End");
		entityNameMap.put(LLI_CLIENT,"LLI Client");
		entityNameMap.put(ADSL,"ADSL");
		entityNameMap.put(ADSL_CLIENT,"ADSL Client");
		entityNameMap.put(NIX,"NIX");
		entityNameMap.put(NIX_CLIENT,"NIX Client");
		entityNameMap.put(DNSHOSTING,"DNS Hosting");
		entityNameMap.put(DNSHOSTING_CLIENT,"DNS Hosting Client");
		entityNameMap.put(CRM_CLIENT_COMPLAIN, "CRM Client Complain");
		entityNameMap.put(CRM_COMPLAIN, "CRM Complain");
		
	}
	public static HashMap<Integer, String> entityStrutsActionMap = new HashMap<Integer, String>();
	static
	{
		entityStrutsActionMap.put(DOMAIN,"ViewDomain");
		entityStrutsActionMap.put(DOMAIN_CLIENT,"GetClientForView");
		
		entityStrutsActionMap.put(WEBHOSTING,"ViewWebHosting");
		entityStrutsActionMap.put(WEBHOSTING_CLIENT,"GetClientForView");
		
		entityStrutsActionMap.put(IPADDRESS,"ViewIpaddress");
		entityStrutsActionMap.put(IPADDRESS_CLIENT,"GetClientForView");
		
		entityStrutsActionMap.put(COLOCATION,"ColocationView");
		entityStrutsActionMap.put(COLOCATION_CLIENT,"GetClientForView");
		
		entityStrutsActionMap.put(IIG,"ViewIig");
		entityStrutsActionMap.put(IIG_CLIENT,"GetClientForView");
		
		entityStrutsActionMap.put(VPN_CONNECTION,"VpnConnectionAction");
		entityStrutsActionMap.put(VPN_LINK,"VpnLinkAction");
		entityStrutsActionMap.put(VPN_LINK_NEAR_END,"VpnLinkAction");
		entityStrutsActionMap.put(VPN_LINK_FAR_END,"VpnLinkAction");
		entityStrutsActionMap.put(VPN_CLIENT,"GetClientForView");
		
		entityStrutsActionMap.put(LLI_LINK,"LliLinkAction");
		entityStrutsActionMap.put(LLI_LINK_FAR_END,"LliLinkAction");
		entityStrutsActionMap.put(LLI_CLIENT,"GetClientForView");
		
		entityStrutsActionMap.put(ADSL,"ViewAdsl");
		entityStrutsActionMap.put(ADSL_CLIENT,"GetClientForView");
		
		entityStrutsActionMap.put(NIX,"ViewNix");
		entityStrutsActionMap.put(NIX_CLIENT,"GetClientForView");
		
		entityStrutsActionMap.put(DNSHOSTING,"ViewDnshosting");
		entityStrutsActionMap.put(DNSHOSTING_CLIENT,"GetClientForView");
		
	}
	public static HashMap<Integer, String> entityStrutsActionMapForEdit = new HashMap<Integer, String>();
	static
	{
		entityStrutsActionMapForEdit.put(DOMAIN,"ViewDomain");
		entityStrutsActionMapForEdit.put(DOMAIN_CLIENT,"GetClientForEdit");
		
		entityStrutsActionMapForEdit.put(WEBHOSTING,"ViewWebHosting");
		entityStrutsActionMapForEdit.put(WEBHOSTING_CLIENT,"GetClientForEdit");
		
		entityStrutsActionMapForEdit.put(IPADDRESS,"ViewIpaddress");
		entityStrutsActionMapForEdit.put(IPADDRESS_CLIENT,"GetClientForEdit");
		
		entityStrutsActionMapForEdit.put(COLOCATION,"ViewColocation");
		entityStrutsActionMapForEdit.put(COLOCATION_CLIENT,"GetClientForEdit");
		
		entityStrutsActionMapForEdit.put(IIG,"ViewIig");
		entityStrutsActionMapForEdit.put(IIG_CLIENT,"GetClientForEdit");
		
		entityStrutsActionMapForEdit.put(VPN_CONNECTION,"VpnConnectionAction");
		entityStrutsActionMapForEdit.put(VPN_LINK,"VpnLinkAction");
		entityStrutsActionMapForEdit.put(VPN_LINK_NEAR_END,"VpnLinkAction");
		entityStrutsActionMapForEdit.put(VPN_LINK_FAR_END,"VpnLinkAction");
		entityStrutsActionMapForEdit.put(VPN_CLIENT,"GetClientForEdit");
		
		entityStrutsActionMapForEdit.put(LLI_LINK,"LliLinkAction");
		entityStrutsActionMapForEdit.put(LLI_CLIENT,"GetClientForEdit");
		
		entityStrutsActionMapForEdit.put(ADSL,"ViewAdsl");
		entityStrutsActionMapForEdit.put(ADSL_CLIENT,"GetClientForEdit");
		
		entityStrutsActionMapForEdit.put(NIX,"ViewNix");
		entityStrutsActionMapForEdit.put(NIX_CLIENT,"GetClientForEdit");
		
		entityStrutsActionMapForEdit.put(DNSHOSTING,"ViewDnshosting");
		entityStrutsActionMapForEdit.put(DNSHOSTING_CLIENT,"GetClientForEdit");
		
	}

	public static Map<Class, String> mapOfExpiryDateColumnNameToEntityClass = new HashMap<Class, String>();
	static {
		mapOfExpiryDateColumnNameToEntityClass.put(BillDTO.class, "lastPaymentDate");

	}

	public static HashMap<Integer, Integer> entityModuleIDMap = new HashMap<Integer, Integer>();

	static
	{
		entityModuleIDMap.put(DOMAIN,ModuleConstants.Module_ID_DOMAIN);
		entityModuleIDMap.put(DOMAIN_CLIENT,ModuleConstants.Module_ID_DOMAIN);
		entityModuleIDMap.put(WEBHOSTING,ModuleConstants.Module_ID_WEBHOSTING);
		entityModuleIDMap.put(WEBHOSTING_CLIENT,ModuleConstants.Module_ID_WEBHOSTING);
		entityModuleIDMap.put(IPADDRESS,ModuleConstants.Module_ID_IPADDRESS);
		entityModuleIDMap.put(IPADDRESS_CLIENT,ModuleConstants.Module_ID_IPADDRESS);
		entityModuleIDMap.put(COLOCATION,ModuleConstants.Module_ID_COLOCATION);
		entityModuleIDMap.put(COLOCATION_CLIENT,ModuleConstants.Module_ID_COLOCATION);
		entityModuleIDMap.put(IIG,ModuleConstants.Module_ID_IIG);
		entityModuleIDMap.put(IIG_CLIENT,ModuleConstants.Module_ID_IIG);
		entityModuleIDMap.put(VPN_CONNECTION, ModuleConstants.Module_ID_VPN);
		entityModuleIDMap.put(VPN_LINK,ModuleConstants.Module_ID_VPN);
		entityModuleIDMap.put(VPN_LINK_NEAR_END,ModuleConstants.Module_ID_VPN);
		entityModuleIDMap.put(VPN_LINK_FAR_END,ModuleConstants.Module_ID_VPN);
		entityModuleIDMap.put(VPN_CLIENT,ModuleConstants.Module_ID_VPN);
		entityModuleIDMap.put(LLI_LINK,ModuleConstants.Module_ID_LLI);
		entityModuleIDMap.put(LLI_LINK_FAR_END,ModuleConstants.Module_ID_LLI);
		entityModuleIDMap.put(LLI_CLIENT,ModuleConstants.Module_ID_LLI);
		entityModuleIDMap.put(ADSL,ModuleConstants.Module_ID_ADSL);
		entityModuleIDMap.put(ADSL_CLIENT,ModuleConstants.Module_ID_ADSL);
		entityModuleIDMap.put(NIX,ModuleConstants.Module_ID_NIX);
		entityModuleIDMap.put(NIX_CLIENT,ModuleConstants.Module_ID_NIX);
		entityModuleIDMap.put(DNSHOSTING,ModuleConstants.Module_ID_DNSHOSTING);
		entityModuleIDMap.put(DNSHOSTING_CLIENT,ModuleConstants.Module_ID_DNSHOSTING);
	}

	public static HashMap<Integer, Integer> moduleIDClientTypeIDMap = new HashMap<Integer, Integer>();
	static
	{
		moduleIDClientTypeIDMap.put(ModuleConstants.Module_ID_DOMAIN, DOMAIN_CLIENT);
		moduleIDClientTypeIDMap.put(ModuleConstants.Module_ID_WEBHOSTING, WEBHOSTING_CLIENT);
		moduleIDClientTypeIDMap.put(ModuleConstants.Module_ID_IPADDRESS, IPADDRESS_CLIENT);
		moduleIDClientTypeIDMap.put(ModuleConstants.Module_ID_COLOCATION, COLOCATION_CLIENT);
		moduleIDClientTypeIDMap.put(ModuleConstants.Module_ID_IIG, IIG_CLIENT);
		moduleIDClientTypeIDMap.put(ModuleConstants.Module_ID_VPN, VPN_CLIENT);
		moduleIDClientTypeIDMap.put(ModuleConstants.Module_ID_LLI, LLI_CLIENT);
		moduleIDClientTypeIDMap.put(ModuleConstants.Module_ID_ADSL, ADSL_CLIENT);
		moduleIDClientTypeIDMap.put(ModuleConstants.Module_ID_NIX, NIX_CLIENT);
		moduleIDClientTypeIDMap.put(ModuleConstants.Module_ID_DNSHOSTING, DNSHOSTING_CLIENT);
		
	}
	
	public static HashMap<Integer, Integer> mapMainEntityToModuleID = new HashMap<Integer, Integer>();
	static
	{
		mapMainEntityToModuleID.put(ModuleConstants.Module_ID_DOMAIN, DOMAIN);
		mapMainEntityToModuleID.put(ModuleConstants.Module_ID_VPN, VPN_LINK);
		mapMainEntityToModuleID.put(ModuleConstants.Module_ID_LLI, LLI_LINK);
		
	}


	public static HashMap<String, Integer> moduleNameModuleIDMap = new HashMap<String, Integer>();
	static
	{
		moduleNameModuleIDMap.put("domain",ModuleConstants.Module_ID_DOMAIN);
		moduleNameModuleIDMap.put("webhosting", ModuleConstants.Module_ID_WEBHOSTING);
		moduleNameModuleIDMap.put("ipaddress",ModuleConstants.Module_ID_IPADDRESS);
		moduleNameModuleIDMap.put("colocation", ModuleConstants.Module_ID_COLOCATION);
		moduleNameModuleIDMap.put("iig",ModuleConstants.Module_ID_IIG);
		moduleNameModuleIDMap.put("vpn", ModuleConstants.Module_ID_VPN);
		moduleNameModuleIDMap.put("lli",ModuleConstants.Module_ID_LLI);
		moduleNameModuleIDMap.put("adsl", ModuleConstants.Module_ID_ADSL);
		moduleNameModuleIDMap.put("nix", ModuleConstants.Module_ID_NIX);
		moduleNameModuleIDMap.put("dnshosting", ModuleConstants.Module_ID_DNSHOSTING);
	}	
	
	/*
	 * 
	 * Entity Status
	 * 
	 */
	public static final int STATUS_NOT_ACTIVE = 0;
	public static final int STATUS_SEMI_ACTIVE = 2;
	public static final int STATUS_ACTIVE = 1;
	public static final int STATUS_TD = 4;

	public static final String STATUS_NOT_ACTIVE_STR = "<label class='label label-danger'>Not active</label>";
	public static final String STATUS_SEMI_ACTIVE_STR = "<label class='label label-info'>Ongoing</label>";
	public static final String STATUS_ACTIVE_STR = "<label class='label label-success'>Active</label>";

	public static HashMap<Integer, String> statusStrMap = new HashMap<>();
	static {
		statusStrMap.put(STATUS_NOT_ACTIVE, "Not Active");
		statusStrMap.put(STATUS_SEMI_ACTIVE, "Ongoing");
		statusStrMap.put(STATUS_ACTIVE, "Approved");
	}
	public static HashMap<Integer, String> statusMap = new HashMap<Integer, String>();
	static {
		statusMap.put(STATUS_NOT_ACTIVE, STATUS_NOT_ACTIVE_STR);
		statusMap.put(STATUS_SEMI_ACTIVE, STATUS_SEMI_ACTIVE_STR);
		statusMap.put(STATUS_ACTIVE, STATUS_ACTIVE_STR);
	}

	public static final int BANDWIDTH_TYPE_MB = 1;
	public static final int BANDWIDTH_TYPE_GB = 2;
	public static HashMap<Integer, String> linkBandwidthTypeMap = new HashMap<Integer, String>();
	static {
		linkBandwidthTypeMap.put(BANDWIDTH_TYPE_MB, "Mb");
		linkBandwidthTypeMap.put(BANDWIDTH_TYPE_GB, "Gb");
	}

	public static HashMap<Integer, String> requestNonEditablePageLinkmap = new HashMap<Integer, String>();
	static {
//		requestNonEditablePageLinkmap.put(VpnRequestTypeConstants.REQUEST_NEW_LINK.CLIENT_APPLY, "viewPage.do?");
	}

	public static HashMap<Integer, String> mapOfModuleNameToMainEntityTypeIdForCrm = new HashMap<Integer, String>();
	static {
		mapOfModuleNameToMainEntityTypeIdForCrm.put(VPN_LINK,
				ModuleConstants.mapOfModuleNameToActiveModuleID.get(ModuleConstants.Module_ID_VPN));
		mapOfModuleNameToMainEntityTypeIdForCrm.put(DOMAIN,
				ModuleConstants.mapOfModuleNameToActiveModuleID.get(ModuleConstants.Module_ID_DOMAIN));
		mapOfModuleNameToMainEntityTypeIdForCrm.put(LLI_LINK,
				ModuleConstants.mapOfModuleNameToActiveModuleID.get(ModuleConstants.Module_ID_LLI));
	}
	public static HashMap<Integer, Integer> mapOfModulesToEntityTypeID = new HashMap<Integer, Integer>();
	static {
		mapOfModulesToEntityTypeID.put(VPNConstants.ENTITY_TYPE, ModuleConstants.Module_ID_VPN);
		mapOfModulesToEntityTypeID.put(DOMAIN,ModuleConstants.Module_ID_DOMAIN);
		mapOfModulesToEntityTypeID.put(LLIConnectionConstants.ENTITY_TYPE,ModuleConstants.Module_ID_LLI);
		mapOfModulesToEntityTypeID.put(CoLocationConstants.ENTITY_TYPE,ModuleConstants.Module_ID_COLOCATION);
		mapOfModulesToEntityTypeID.put(NIXConstants.ENTITY_TYPE,ModuleConstants.Module_ID_NIX);

	}
	public static HashMap<Integer, Integer> mapOfModuleIDToMainEntityTypeIdForCrm = new HashMap<Integer, Integer>();
	static {
		mapOfModuleIDToMainEntityTypeIdForCrm.put(VPN_LINK,ModuleConstants.Module_ID_VPN);
		mapOfModuleIDToMainEntityTypeIdForCrm.put(DOMAIN,ModuleConstants.Module_ID_DOMAIN);
	}
	
	/***
	 * For DNS hosting : Record Type For DNS SubDomain.
	 */
	public static final String RECORD_TYPE_A = "A";
	public static final String RECORD_TYPE_AAAA= "AAAA";
	public static final String RECORD_TYPE_CNAME = "CNAME";
	public static final String RECORD_TYPE_NS = "NS";
	public static final String RECORD_TYPE_MX = "MX";
	
	
	public static final int A_TYPE_RECORD = 1;
	public static final int AAAA_TYPE_RECORD = 2;
	public static final int CNAME_TYPE_RECORD = 3;
	public static final int NS_TYPE_RECORD = 4;
	public static final int MX_TYPE_RECORD = 5;
	
	public static HashMap <Integer, String> mapOfRecordTypeToRecordTypeStr = new HashMap<>();
	static {
		mapOfRecordTypeToRecordTypeStr.put(A_TYPE_RECORD, RECORD_TYPE_A);
		mapOfRecordTypeToRecordTypeStr.put(AAAA_TYPE_RECORD, RECORD_TYPE_AAAA);
		mapOfRecordTypeToRecordTypeStr.put(CNAME_TYPE_RECORD, RECORD_TYPE_CNAME);
		mapOfRecordTypeToRecordTypeStr.put(NS_TYPE_RECORD, RECORD_TYPE_NS);
		mapOfRecordTypeToRecordTypeStr.put(MX_TYPE_RECORD, RECORD_TYPE_MX);
	}
	
	/***
	 * Crm Activity Log
	 * 
	 */
	public static final String PASS_COMPLAIN = "Pass";
	public static final String ASSIGN_COMPLAIN = "Assign";
	public static final String FEEDBACK_COMPLAIN = "Feedback";
	public static final String REJECT_COMPLAIN = "Reject";
	public static final String STATUS_CHANGE_COMPLAIN = "Status Change";
	public static final String ATTEMPT_TO_COMPLETE_COMPLAIN = "Attempt to Complete";
	public static final String COMPLETE_COMPLAIN = "Complete";
	
	public static HashMap< Integer, String> mapOfActionTypeStrToActionTypeCrm = new HashMap<>();
	static {
		mapOfActionTypeStrToActionTypeCrm.put(CrmActivityLog.COMPLAIN_PASS	, PASS_COMPLAIN);
		mapOfActionTypeStrToActionTypeCrm.put(CrmActivityLog.COMPLAIN_ASSIGN, ASSIGN_COMPLAIN);
		mapOfActionTypeStrToActionTypeCrm.put(CrmActivityLog.COMPLAIN_FEEDBACK, FEEDBACK_COMPLAIN);
		mapOfActionTypeStrToActionTypeCrm.put(CrmActivityLog.COMPLAIN_REJECT	, REJECT_COMPLAIN);
		mapOfActionTypeStrToActionTypeCrm.put(CrmActivityLog.COMPLAIN_STATUS_CHANGE	, STATUS_CHANGE_COMPLAIN);
		mapOfActionTypeStrToActionTypeCrm.put(CrmActivityLog.ATTEMP_TO_COMPLETE_COMPLAIN	, ATTEMPT_TO_COMPLETE_COMPLAIN);
		mapOfActionTypeStrToActionTypeCrm.put(CrmActivityLog.COMPLETE_COMPLAIN	, COMPLETE_COMPLAIN);
		
	}
	
	/***
	 * CrmNotification purpose
	 */
	public enum ActionTypeForCrmComplain {
		COMPLETE_TYPE,
		FEEDBACK_TYPE,
		PASS_TYPE,
		REJECT_TYPE,
		ASSIGN_TYPE
	}
	
	/**
	 * @author dhrubo
	 */
	public static HashMap< Integer, Integer> childEntityTypeParentEntityTypeMap = new HashMap<>();
	static {
		childEntityTypeParentEntityTypeMap.put(VPN_LINK, null);
		childEntityTypeParentEntityTypeMap.put(VPN_LINK_FAR_END, VPN_LINK);
		childEntityTypeParentEntityTypeMap.put(VPN_LINK_NEAR_END, VPN_LINK);
		
		childEntityTypeParentEntityTypeMap.put(LLI_LINK, null);
		childEntityTypeParentEntityTypeMap.put(LLI_LINK_FAR_END, LLI_LINK);
	}
	
	/**
	 * @author dhrubo
	 */
	public static HashMap<Integer, List<Integer>> parentEntityToChildEntityList = new HashMap<>();
	static {
		List<Integer> vpnLinkToChildEntityList = new ArrayList<Integer>();
		vpnLinkToChildEntityList.add(VPN_LINK_FAR_END);
		vpnLinkToChildEntityList.add(VPN_LINK_NEAR_END);
		parentEntityToChildEntityList.put(VPN_LINK, vpnLinkToChildEntityList);
		
		List<Integer> lliLinkToChildEntityList = new ArrayList<Integer>();
		lliLinkToChildEntityList.add(LLI_LINK_FAR_END);
		parentEntityToChildEntityList.put(LLI_LINK, lliLinkToChildEntityList);
	}
}
