package file;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.SystemUtils;

import client.IdentityTypeConstants;
import common.ModuleConstants;

public class FileTypeConstants // extends VpnSubRequestTypeConstants{
{
	public static final String BASE_PATH =SystemUtils.IS_OS_LINUX?"/srv/btclautomation/resources/":"C:/btclautomation/resources/";
	public static final String TEMP_UPLOAD_DIR = "temp-uploads/";
	public static final String FINAL_UPLOAD_DIR = "uploads/";
	public static final String BILL_DIRECTORY = "bills/";
	public static final String VPN_BILL_DIRECTORY = "bills/vpn/";
	public static final String DOMAIN_BILL_DIRECTORY = "bills/domain/";
	public static final String LLI_BILL_DIRECTORY = "bills/lli/";
	public static final String COLOCATION_BILL_DIRECTORY = "bills/colocation/";
	public static final String NIX_BILL_DIRECTORY = "bills/nix";
	public static final String DOMAIN_NAME_TO_IMAGE_DIR = "domain/name/";
	public static final int DOMAIN_NAME_IMG_FONT_SIZE = 35;
	public static final int DOMAIN_NAME_IMG_WIDTH = 700;
	public static final int DOMAIN_NAME_IMG_HEIGHT = 45;
	
	public static final String DEFAULT_PROFILE_PIC = "avatar.png";
	public static final String DEFAULT_PROFILE_PATH = "assets/layouts/layout4/img/"+DEFAULT_PROFILE_PIC;
	public static final int ENTITY_TYPE_PROFILE_PICTURE=-1; 
	public static final int BUFFER_SIZE = 4096;
	public static final int MAXIMUM_DIRECOTRY_NUMBER=1000;
	public static final long MAXIMUM_FILE_SIZE=5242880L; //5MB
	
	public static final int VAT_DOC = 5;
	public static final int IT_DEDUCTION_DOC = 6;
	
	public static final HashSet<String> allowedContentTypes = new HashSet<String>();
    public static HashSet<String> allowedExtensions=new HashSet<String>();
	
	static {
		allowedContentTypes.add("image/png");
		allowedContentTypes.add("image/gif");
		allowedContentTypes.add("image/jpg");
		allowedContentTypes.add("image/jpeg");
		allowedContentTypes.add("application/pdf");
		//allowedContentTypes.add("application/zip");
		//allowedContentTypes.add("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		//allowedContentTypes.add("application/msword");
		
		allowedExtensions.add("pdf");
        allowedExtensions.add("jpg");
        allowedExtensions.add("jpeg");
        allowedExtensions.add("png");
        allowedExtensions.add("gif");
	}

	public static final int PROFILE_PICTURE = 100;

	public static final int NID_SUFFIX = 101;
	public static final int PASSPORT_SUFFIX = 102;
	public static final int TIN_SUFFIX = 103;
	public static final int TRADE_SUFFIX = 104;
	public static final int FORWARDING_LETTER_SUFFIX = 105;
	public static final int SIGNATURE_SUFFIX = 106;
	public static final int VAT_SUFFIX = 107;
	public static final int REGISTRATION_SUFFIX = 108;
	public static final int TAX_SUFFIX = 109;
	
	public static final int VPN_BILL_SUFFIX = 601;
	

	public static final int COMMENT_SUFFIX = 201;
	public static final int DOMIAN_REQUIREMENT = 301;
	public static final int OWNERSHIP_CHANGE = 302;
	public static final int TRANSFER = 303;
	
	public static final int BANK_PAYMENT = 500;

	public static class CLIENT {
		public static final int CLIENT_PROFILE_PICTURE = ModuleConstants.Module_ID_CLIENT * ModuleConstants.MULTIPLIER + PROFILE_PICTURE;
		
		public static final int COMMON_NID=ModuleConstants.Module_ID_CLIENT * ModuleConstants.MULTIPLIER+NID_SUFFIX;
		public static final int COMMON_PASSPORT=ModuleConstants.Module_ID_CLIENT * ModuleConstants.MULTIPLIER+PASSPORT_SUFFIX;
		public static final int COMMON_TIN=ModuleConstants.Module_ID_CLIENT * ModuleConstants.MULTIPLIER+TIN_SUFFIX;
		public static final int COMMON_TRADE=ModuleConstants.Module_ID_CLIENT * ModuleConstants.MULTIPLIER+TRADE_SUFFIX;
		public static final int COMMON_FORWARDING_LETTER=ModuleConstants.Module_ID_CLIENT * ModuleConstants.MULTIPLIER+FORWARDING_LETTER_SUFFIX;
		public static final int COMMON_SIGNATURE = ModuleConstants.Module_ID_CLIENT * ModuleConstants.MULTIPLIER+SIGNATURE_SUFFIX;
		public static final int COMMON_VAT = ModuleConstants.Module_ID_CLIENT * ModuleConstants.MULTIPLIER+VAT_SUFFIX;
		public static final int COMMON_REGISTRATION = ModuleConstants.Module_ID_CLIENT * ModuleConstants.MULTIPLIER+REGISTRATION_SUFFIX;
		public static final int COMMON_TAX = ModuleConstants.Module_ID_CLIENT * ModuleConstants.MULTIPLIER+TAX_SUFFIX;
	}

	public static class DOMAIN_CLIENT_ADD {//1
		public static final int DOMAIN_CLIENT_ADD_NID = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + NID_SUFFIX;
		public static final int DOMAIN_CLIENT_ADD_PASSPORT = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + PASSPORT_SUFFIX;
		public static final int DOMAIN_CLIENT_ADD_TIN = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + TIN_SUFFIX;
		public static final int DOMAIN_CLIENT_ADD_TRADE = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + TRADE_SUFFIX;
		public static final int DOMAIN_CLIENT_ADD_FORWARDING_LETTER = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + FORWARDING_LETTER_SUFFIX;
		public static final int DOMAIN_CLIENT_ADD_SIGNATURE = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + SIGNATURE_SUFFIX;
		public static final int DOMAIN_CLIENT_ADD_VAT = ModuleConstants.Module_ID_CLIENT * ModuleConstants.MULTIPLIER+VAT_SUFFIX;
		public static final int DOMAIN_CLIENT_ADD_REGISTRATION = ModuleConstants.Module_ID_CLIENT * ModuleConstants.MULTIPLIER+REGISTRATION_SUFFIX;
		public static final int DOMAIN_CLIENT_ADD_TAX = ModuleConstants.Module_ID_CLIENT * ModuleConstants.MULTIPLIER+TAX_SUFFIX;
		
		public static final HashMap<Integer, Integer> COMMON_TO_DOMAIN_CLIENT= new HashMap<Integer, Integer>();
		static{
			COMMON_TO_DOMAIN_CLIENT.put(CLIENT.COMMON_NID, DOMAIN_CLIENT_ADD_NID);
			COMMON_TO_DOMAIN_CLIENT.put(CLIENT.COMMON_PASSPORT, DOMAIN_CLIENT_ADD_PASSPORT);
			COMMON_TO_DOMAIN_CLIENT.put(CLIENT.COMMON_TIN, DOMAIN_CLIENT_ADD_TIN);
			COMMON_TO_DOMAIN_CLIENT.put(CLIENT.COMMON_TRADE, DOMAIN_CLIENT_ADD_TRADE);
			COMMON_TO_DOMAIN_CLIENT.put(CLIENT.COMMON_FORWARDING_LETTER, DOMAIN_CLIENT_ADD_FORWARDING_LETTER);
			COMMON_TO_DOMAIN_CLIENT.put(CLIENT.COMMON_SIGNATURE, DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_SIGNATURE);
			COMMON_TO_DOMAIN_CLIENT.put(CLIENT.COMMON_VAT, DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_VAT);
			COMMON_TO_DOMAIN_CLIENT.put(CLIENT.COMMON_REGISTRATION, DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_REGISTRATION);
			COMMON_TO_DOMAIN_CLIENT.put(CLIENT.COMMON_TAX, DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_TAX);
		}

	}
	
	public static class WEBHOSTING_CLIENT_ADD {//2
		public static final int WEBHOSTING_CLIENT_ADD_NID = ModuleConstants.Module_ID_WEBHOSTING * ModuleConstants.MULTIPLIER + NID_SUFFIX;
		public static final int WEBHOSTING_CLIENT_ADD_PASSPORT = ModuleConstants.Module_ID_WEBHOSTING * ModuleConstants.MULTIPLIER + PASSPORT_SUFFIX;
		public static final int WEBHOSTING_CLIENT_ADD_TIN = ModuleConstants.Module_ID_WEBHOSTING * ModuleConstants.MULTIPLIER+ TIN_SUFFIX;
		public static final int WEBHOSTING_CLIENT_ADD_TRADE = ModuleConstants.Module_ID_WEBHOSTING * ModuleConstants.MULTIPLIER + TRADE_SUFFIX;
		public static final int WEBHOSTING_CLIENT_ADD_FORWARDING_LETTER = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + FORWARDING_LETTER_SUFFIX;
		
		public static final HashMap<Integer, Integer> COMMON_TO_WEBHOSTING_CLIENT= new HashMap<Integer, Integer>();
		static{
			COMMON_TO_WEBHOSTING_CLIENT.put(CLIENT.COMMON_NID, WEBHOSTING_CLIENT_ADD_NID);
			COMMON_TO_WEBHOSTING_CLIENT.put(CLIENT.COMMON_PASSPORT, WEBHOSTING_CLIENT_ADD_PASSPORT);
			COMMON_TO_WEBHOSTING_CLIENT.put(CLIENT.COMMON_TIN, WEBHOSTING_CLIENT_ADD_TIN);
			COMMON_TO_WEBHOSTING_CLIENT.put(CLIENT.COMMON_TRADE, WEBHOSTING_CLIENT_ADD_TRADE);
			COMMON_TO_WEBHOSTING_CLIENT.put(CLIENT.COMMON_FORWARDING_LETTER, WEBHOSTING_CLIENT_ADD_FORWARDING_LETTER);
		}
	}
	
	public static class IPADDRESS_CLIENT_ADD {//3
		public static final int IPADDRESS_CLIENT_ADD_NID = ModuleConstants.Module_ID_IPADDRESS * ModuleConstants.MULTIPLIER + NID_SUFFIX;
		public static final int IPADDRESS_CLIENT_ADD_PASSPORT = ModuleConstants.Module_ID_IPADDRESS * ModuleConstants.MULTIPLIER + PASSPORT_SUFFIX;
		public static final int IPADDRESS_CLIENT_ADD_TIN = ModuleConstants.Module_ID_IPADDRESS * ModuleConstants.MULTIPLIER+ TIN_SUFFIX;
		public static final int IPADDRESS_CLIENT_ADD_TRADE = ModuleConstants.Module_ID_IPADDRESS * ModuleConstants.MULTIPLIER + TRADE_SUFFIX;
		public static final int IPADDRESS_CLIENT_ADD_FORWARDING_LETTER = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + FORWARDING_LETTER_SUFFIX;
		
		public static final HashMap<Integer, Integer> COMMON_TO_IPADDRESS_CLIENT= new HashMap<Integer, Integer>();
		static{
			COMMON_TO_IPADDRESS_CLIENT.put(CLIENT.COMMON_NID, IPADDRESS_CLIENT_ADD_NID);
			COMMON_TO_IPADDRESS_CLIENT.put(CLIENT.COMMON_PASSPORT, IPADDRESS_CLIENT_ADD_PASSPORT);
			COMMON_TO_IPADDRESS_CLIENT.put(CLIENT.COMMON_TIN, IPADDRESS_CLIENT_ADD_TIN);
			COMMON_TO_IPADDRESS_CLIENT.put(CLIENT.COMMON_TRADE, IPADDRESS_CLIENT_ADD_TRADE);
			COMMON_TO_IPADDRESS_CLIENT.put(CLIENT.COMMON_FORWARDING_LETTER, IPADDRESS_CLIENT_ADD_FORWARDING_LETTER);
		}
	}
	
	public static class COLOCATION_CLIENT_ADD {//4
		public static final int COLOCATION_CLIENT_ADD_NID = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + NID_SUFFIX;
		public static final int COLOCATION_CLIENT_ADD_PASSPORT = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + PASSPORT_SUFFIX;
		public static final int COLOCATION_CLIENT_ADD_TIN = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER+ TIN_SUFFIX;
		public static final int COLOCATION_CLIENT_ADD_TRADE = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + TRADE_SUFFIX;
		public static final int COLOCATION_CLIENT_ADD_FORWARDING_LETTER = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + FORWARDING_LETTER_SUFFIX;
		
		public static final HashMap<Integer, Integer> COMMON_TO_COLOCATION_CLIENT= new HashMap<Integer, Integer>();
		static{
			COMMON_TO_COLOCATION_CLIENT.put(CLIENT.COMMON_NID, COLOCATION_CLIENT_ADD_NID);
			COMMON_TO_COLOCATION_CLIENT.put(CLIENT.COMMON_PASSPORT, COLOCATION_CLIENT_ADD_PASSPORT);
			COMMON_TO_COLOCATION_CLIENT.put(CLIENT.COMMON_TIN, COLOCATION_CLIENT_ADD_TIN);
			COMMON_TO_COLOCATION_CLIENT.put(CLIENT.COMMON_TRADE, COLOCATION_CLIENT_ADD_TRADE);
			COMMON_TO_COLOCATION_CLIENT.put(CLIENT.COMMON_FORWARDING_LETTER, COLOCATION_CLIENT_ADD_FORWARDING_LETTER);
		}
	}
	
	public static class IIG_CLIENT_ADD {//5
		public static final int IIG_CLIENT_ADD_NID = ModuleConstants.Module_ID_IIG * ModuleConstants.MULTIPLIER + NID_SUFFIX;
		public static final int IIG_CLIENT_ADD_PASSPORT = ModuleConstants.Module_ID_IIG * ModuleConstants.MULTIPLIER + PASSPORT_SUFFIX;
		public static final int IIG_CLIENT_ADD_TIN = ModuleConstants.Module_ID_IIG * ModuleConstants.MULTIPLIER+ TIN_SUFFIX;
		public static final int IIG_CLIENT_ADD_TRADE = ModuleConstants.Module_ID_IIG * ModuleConstants.MULTIPLIER + TRADE_SUFFIX;
		public static final int IIG_CLIENT_ADD_FORWARDING_LETTER = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + FORWARDING_LETTER_SUFFIX;
		
		public static final HashMap<Integer, Integer> COMMON_TO_IIG_CLIENT= new HashMap<Integer, Integer>();
		static{
			COMMON_TO_IIG_CLIENT.put(CLIENT.COMMON_NID, IIG_CLIENT_ADD_NID);
			COMMON_TO_IIG_CLIENT.put(CLIENT.COMMON_PASSPORT, IIG_CLIENT_ADD_PASSPORT);
			COMMON_TO_IIG_CLIENT.put(CLIENT.COMMON_TIN, IIG_CLIENT_ADD_TIN);
			COMMON_TO_IIG_CLIENT.put(CLIENT.COMMON_TRADE, IIG_CLIENT_ADD_TRADE);
			COMMON_TO_IIG_CLIENT.put(CLIENT.COMMON_FORWARDING_LETTER, IIG_CLIENT_ADD_FORWARDING_LETTER);
		}
	}
	
	public static class VPN_CLIENT_ADD {//6
		public static final int VPN_CLIENT_ADD_NID = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + NID_SUFFIX;
		public static final int VPN_CLIENT_ADD_PASSPORT = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + PASSPORT_SUFFIX;
		public static final int VPN_CLIENT_ADD_TIN = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER+ TIN_SUFFIX;
		public static final int VPN_CLIENT_ADD_TRADE = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + TRADE_SUFFIX;
		public static final int VPN_CLIENT_ADD_FORWARDING_LETTER = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + FORWARDING_LETTER_SUFFIX;
		
		public static final HashMap<Integer, Integer> COMMON_TO_VPN_CLIENT= new HashMap<Integer, Integer>();
		static{
			COMMON_TO_VPN_CLIENT.put(CLIENT.COMMON_NID, VPN_CLIENT_ADD_NID);
			COMMON_TO_VPN_CLIENT.put(CLIENT.COMMON_PASSPORT, VPN_CLIENT_ADD_PASSPORT);
			COMMON_TO_VPN_CLIENT.put(CLIENT.COMMON_TIN, VPN_CLIENT_ADD_TIN);
			COMMON_TO_VPN_CLIENT.put(CLIENT.COMMON_TRADE, VPN_CLIENT_ADD_TRADE);
			COMMON_TO_VPN_CLIENT.put(CLIENT.COMMON_FORWARDING_LETTER, VPN_CLIENT_ADD_FORWARDING_LETTER);
		}
	}
	
	public static class LLI_CLIENT_ADD {//7
		public static final int LLI_CLIENT_ADD_NID = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + NID_SUFFIX;
		public static final int LLI_CLIENT_ADD_PASSPORT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + PASSPORT_SUFFIX;
		public static final int LLI_CLIENT_ADD_TIN = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER+ TIN_SUFFIX;
		public static final int LLI_CLIENT_ADD_TRADE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + TRADE_SUFFIX;
		public static final int LLI_CLIENT_ADD_FORWARDING_LETTER = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + FORWARDING_LETTER_SUFFIX;
		
		public static final HashMap<Integer, Integer> COMMON_TO_LLI_CLIENT= new HashMap<Integer, Integer>();
		static{
			COMMON_TO_LLI_CLIENT.put(CLIENT.COMMON_NID, LLI_CLIENT_ADD_NID);
			COMMON_TO_LLI_CLIENT.put(CLIENT.COMMON_PASSPORT, LLI_CLIENT_ADD_PASSPORT);
			COMMON_TO_LLI_CLIENT.put(CLIENT.COMMON_TIN, LLI_CLIENT_ADD_TIN);
			COMMON_TO_LLI_CLIENT.put(CLIENT.COMMON_TRADE, LLI_CLIENT_ADD_TRADE);
			COMMON_TO_LLI_CLIENT.put(CLIENT.COMMON_FORWARDING_LETTER, LLI_CLIENT_ADD_FORWARDING_LETTER);
		}
	}
	
	public static class ADSL_CLIENT_ADD {//8
		public static final int ADSL_CLIENT_ADD_NID = ModuleConstants.Module_ID_ADSL * ModuleConstants.MULTIPLIER + NID_SUFFIX;
		public static final int ADSL_CLIENT_ADD_PASSPORT = ModuleConstants.Module_ID_ADSL * ModuleConstants.MULTIPLIER + PASSPORT_SUFFIX;
		public static final int ADSL_CLIENT_ADD_TIN = ModuleConstants.Module_ID_ADSL * ModuleConstants.MULTIPLIER+ TIN_SUFFIX;
		public static final int ADSL_CLIENT_ADD_TRADE = ModuleConstants.Module_ID_ADSL * ModuleConstants.MULTIPLIER + TRADE_SUFFIX;
		public static final int ADSL_CLIENT_ADD_FORWARDING_LETTER = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + FORWARDING_LETTER_SUFFIX;
		
		public static final HashMap<Integer, Integer> COMMON_TO_ADSL_CLIENT= new HashMap<Integer, Integer>();
		static{
			COMMON_TO_ADSL_CLIENT.put(CLIENT.COMMON_NID, ADSL_CLIENT_ADD_NID);
			COMMON_TO_ADSL_CLIENT.put(CLIENT.COMMON_PASSPORT, ADSL_CLIENT_ADD_PASSPORT);
			COMMON_TO_ADSL_CLIENT.put(CLIENT.COMMON_TIN, ADSL_CLIENT_ADD_TIN);
			COMMON_TO_ADSL_CLIENT.put(CLIENT.COMMON_TRADE, ADSL_CLIENT_ADD_TRADE);
			COMMON_TO_ADSL_CLIENT.put(CLIENT.COMMON_FORWARDING_LETTER, ADSL_CLIENT_ADD_FORWARDING_LETTER);
		}
	}
	
	public static class NIX_CLIENT_ADD {//9
		public static final int NIX_CLIENT_ADD_NID = ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER + NID_SUFFIX;
		public static final int NIX_CLIENT_ADD_PASSPORT = ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER + PASSPORT_SUFFIX;
		public static final int NIX_CLIENT_ADD_TIN = ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER+ TIN_SUFFIX;
		public static final int NIX_CLIENT_ADD_TRADE = ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER + TRADE_SUFFIX;
		public static final int NIX_CLIENT_ADD_FORWARDING_LETTER = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + FORWARDING_LETTER_SUFFIX;
		
		public static final HashMap<Integer, Integer> COMMON_TO_NIX_CLIENT= new HashMap<Integer, Integer>();
		static{
			COMMON_TO_NIX_CLIENT.put(CLIENT.COMMON_NID, NIX_CLIENT_ADD_NID);
			COMMON_TO_NIX_CLIENT.put(CLIENT.COMMON_PASSPORT, NIX_CLIENT_ADD_PASSPORT);
			COMMON_TO_NIX_CLIENT.put(CLIENT.COMMON_TIN, NIX_CLIENT_ADD_TIN);
			COMMON_TO_NIX_CLIENT.put(CLIENT.COMMON_TRADE, NIX_CLIENT_ADD_TRADE);
			COMMON_TO_NIX_CLIENT.put(CLIENT.COMMON_FORWARDING_LETTER, NIX_CLIENT_ADD_FORWARDING_LETTER);
		}
	}
	
	public static class DNSHOSTING_CLIENT_ADD {//9
		public static final int DNSHOSTING_CLIENT_ADD_NID = ModuleConstants.Module_ID_DNSHOSTING * ModuleConstants.MULTIPLIER + NID_SUFFIX;
		public static final int DNSHOSTING_CLIENT_ADD_PASSPORT = ModuleConstants.Module_ID_DNSHOSTING * ModuleConstants.MULTIPLIER + PASSPORT_SUFFIX;
		public static final int DNSHOSTING_CLIENT_ADD_TIN = ModuleConstants.Module_ID_DNSHOSTING * ModuleConstants.MULTIPLIER+ TIN_SUFFIX;
		public static final int DNSHOSTING_CLIENT_ADD_TRADE = ModuleConstants.Module_ID_DNSHOSTING * ModuleConstants.MULTIPLIER + TRADE_SUFFIX;
		public static final int DNSHOSTING_CLIENT_ADD_FORWARDING_LETTER = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + FORWARDING_LETTER_SUFFIX;
		
		public static final HashMap<Integer, Integer> COMMON_TO_DNSHOSTING_CLIENT= new HashMap<Integer, Integer>();
		static{
			COMMON_TO_DNSHOSTING_CLIENT.put(CLIENT.COMMON_NID, DNSHOSTING_CLIENT_ADD_NID);
			COMMON_TO_DNSHOSTING_CLIENT.put(CLIENT.COMMON_PASSPORT, DNSHOSTING_CLIENT_ADD_PASSPORT);
			COMMON_TO_DNSHOSTING_CLIENT.put(CLIENT.COMMON_TIN, DNSHOSTING_CLIENT_ADD_TIN);
			COMMON_TO_DNSHOSTING_CLIENT.put(CLIENT.COMMON_TRADE, DNSHOSTING_CLIENT_ADD_TRADE);
			COMMON_TO_DNSHOSTING_CLIENT.put(CLIENT.COMMON_FORWARDING_LETTER, DNSHOSTING_CLIENT_ADD_FORWARDING_LETTER);
		}
	}
	
	
	public static class DOMAIN_CLIENT_VIEW {//2
		public static final int DOMAIN_CLIENT_COMMENT = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + COMMENT_SUFFIX;
	}
	public static class VPN_CLIENT_VIEW {//2
		public static final int VPN_CLIENT_COMMENT = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER	+ COMMENT_SUFFIX;
	}
	public static class VPN_LINK_VIEW {//2
		public static final int VPN_LINK_COMMENT = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + COMMENT_SUFFIX;
	}
	
	public static class DOMAIN_BUY {//3
		public static final int DOMAIN_BUY_DOC = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + DOMIAN_REQUIREMENT;
		public static final int DOMAIN_OWNERSHIP_CHANGE = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER	+ OWNERSHIP_CHANGE;
		public static final int DOMAIN_TRANSFER = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER	+ TRANSFER;
	}
	
	public static class VPN_LINK_ADD {//3
		public static final int VPN_LINK_ADD_NID = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 301;
		public static final int VPN_LINK_ADD_PASSPORT = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 302;
		public static final int VPN_LINK_ADD_FORWARDING_LETTER = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 303;
		public static final int VPN_LINK_ADD_DOC2 = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 304;
		public static final int VPN_LINK_CLOSE = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 305;
		public static final int VPN_LINK_ADD_GENERAL_DOCUMENT = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 306;
	}
	
	public static class LLI_LINK_ADD {//3
		public static final int LLI_LINK_ADD_NID = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 301;
		public static final int LLI_LINK_ADD_PASSPORT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 302;
		public static final int LLI_LINK_ADD_FORWARDING_LETTER = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 303;
		public static final int LLI_LINK_ADD_DOC2 = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 304;
		public static final int LLI_LINK_CLOSE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 305;
		public static final int LLI_LINK_GENERAL_DOCUMENT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 306;
	}
	
	public static class GLOBAL {//5
//		public static final int VPN_BANK_PAYMENT = ModuleConstants.Module_ID_GENERAL * ModuleConstants.MULTIPLIER + BANK_PAYMENT;
		public static final int BANK_PAYMENT_VAT = ModuleConstants.Module_ID_GENERAL * ModuleConstants.MULTIPLIER + VAT_DOC;		
		public static final int BANK_PAYMENT_IT = ModuleConstants.Module_ID_GENERAL * ModuleConstants.MULTIPLIER + IT_DEDUCTION_DOC;		
		public static final int BILL = ModuleConstants.Module_ID_GENERAL * ModuleConstants.MULTIPLIER + VPN_BILL_SUFFIX;
	}
	
	public static class CRM {
		public static final int CLIENT_COMPLAIN = ModuleConstants.Module_ID_CRM * ModuleConstants.MULTIPLIER+2200;
		public static final int CRM_COMPLAIN = ModuleConstants.Module_ID_CRM * ModuleConstants.MULTIPLIER+2201;
	}
	
	/**
	 * @author dhrubo
	 */
	private static void populateModuleSpecificFileTypeConstants(int moduleID, int identityTypeConstant) {
		TYPE_ID_NAME.put(moduleID * 10000 + identityTypeConstant, IdentityTypeConstants.IdentityTypeName.get(identityTypeConstant));
	}

	public static HashMap<Integer, String> TYPE_ID_NAME = new HashMap<Integer, String>();

	static {
		
		TYPE_ID_NAME.put(CLIENT.COMMON_NID, "NID");
		TYPE_ID_NAME.put(CLIENT.COMMON_PASSPORT, "Passport");
		TYPE_ID_NAME.put(CLIENT.COMMON_TIN, "TIN");
		TYPE_ID_NAME.put(CLIENT.COMMON_TRADE, "Trade Licence");
		TYPE_ID_NAME.put(CLIENT.COMMON_FORWARDING_LETTER, "Forwarding Letter");
		
		
		TYPE_ID_NAME.put(DOMAIN_CLIENT_VIEW.DOMAIN_CLIENT_COMMENT, "DOC");
		
		TYPE_ID_NAME.put(DOMAIN_BUY.DOMAIN_BUY_DOC, "DOC");
		TYPE_ID_NAME.put(DOMAIN_BUY.DOMAIN_OWNERSHIP_CHANGE, "Documents for Ownership Change");
		TYPE_ID_NAME.put(DOMAIN_BUY.DOMAIN_TRANSFER, "Documents for Domain Transfer");
		
		
		TYPE_ID_NAME.put(VPN_LINK_ADD.VPN_LINK_ADD_NID, "NID");
		TYPE_ID_NAME.put(VPN_LINK_ADD.VPN_LINK_ADD_PASSPORT, "Passport");
		TYPE_ID_NAME.put(VPN_LINK_ADD.VPN_LINK_ADD_FORWARDING_LETTER, "Forwarding Letter");
		TYPE_ID_NAME.put(VPN_LINK_ADD.VPN_LINK_ADD_DOC2, "DOC");
		TYPE_ID_NAME.put(VPN_LINK_ADD.VPN_LINK_CLOSE, "Documents");
		TYPE_ID_NAME.put(VPN_LINK_ADD.VPN_LINK_ADD_GENERAL_DOCUMENT, "Documents");

		TYPE_ID_NAME.put(VPN_CLIENT_VIEW.VPN_CLIENT_COMMENT, "DOC");
		TYPE_ID_NAME.put(VPN_LINK_VIEW.VPN_LINK_COMMENT, "DOC");
		
		TYPE_ID_NAME.put(GLOBAL.BANK_PAYMENT_VAT, "VAT");
		TYPE_ID_NAME.put(GLOBAL.BANK_PAYMENT_IT, "IT Deduction");
		TYPE_ID_NAME.put(GLOBAL.BILL, "Bill" );
		
		TYPE_ID_NAME.put(LLI_LINK_ADD.LLI_LINK_CLOSE, "Documents");
		TYPE_ID_NAME.put(LLI_LINK_ADD.LLI_LINK_GENERAL_DOCUMENT,"Documents");
		
		TYPE_ID_NAME.put(CRM.CLIENT_COMPLAIN, "Client Uploads");
		TYPE_ID_NAME.put(CRM.CRM_COMPLAIN, "Admin");
	}
	
	
	/**
	 * @author dhrubo
	 */
	static {
		for(int moduleID : ModuleConstants.ModuleMap.keySet()) {
			for(int identityTypeConstant : IdentityTypeConstants.IdentityTypeName.keySet()) {
				if (identityTypeConstant != 0) {
					populateModuleSpecificFileTypeConstants(moduleID, identityTypeConstant);
				}
			}
		}
	}

}