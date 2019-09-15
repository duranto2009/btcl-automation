package client;

import java.util.HashMap;
import java.util.Map;

public class IdentityTypeConstants {
	
	public static final int NID = 101;
	public static final int Passport = 102;
	public static final int TIN = 103;
	public static final int Trade_License = 104;
	public static final int Forwarding_Letter = 105;
	public static final int Signature = 106;
	public static final int VAT = 107;
	public static final int Registration = 108;
	public static final int TAX = 109;

	public static final int Memorandum_Of_Article = 110;
	public static final int Photograph = 111;
	public static final int Updated_Company_Tax_Certificate = 112;
	public static final int Valid_BTRC_License = 113;
	public static final int Deed_Of_Home_Office_Rent = 114;
	public static final int Holding_Tax_Of_Home_Office = 115;
	public static final int Updated_VAT_Registration_And_VAT_Certificate = 116;
	
	
	public static Map<Integer, String> IdentityTypeName = new HashMap<Integer,String>();
	
	static {
		IdentityTypeName.put(NID, "NID");
		IdentityTypeName.put(Passport, "Passport");
		IdentityTypeName.put(TIN, "TIN");
		IdentityTypeName.put(Trade_License, "Trade License");
		IdentityTypeName.put(Forwarding_Letter, "Forwarding Letter");
		IdentityTypeName.put(Signature, "Signature");
		IdentityTypeName.put(VAT, "VAT");
		IdentityTypeName.put(Registration, "Registration");
		IdentityTypeName.put(TAX, "TAX");
		
		IdentityTypeName.put(Memorandum_Of_Article, "Memorandum of Article");
		IdentityTypeName.put(Photograph, "Photograph");
		IdentityTypeName.put(Updated_Company_Tax_Certificate, "Updated Company Tax Certificate");
		IdentityTypeName.put(Valid_BTRC_License, "Valid BTRC License");
		IdentityTypeName.put(Deed_Of_Home_Office_Rent, "Deed Of Home Office Rent");
		IdentityTypeName.put(Holding_Tax_Of_Home_Office, "Holding Tax of Home Office");
		IdentityTypeName.put(Updated_VAT_Registration_And_VAT_Certificate, "Updated VAT Registration and VAT Certificate");
		
		IdentityTypeName.put(0, "Corrupted Entry");
	}
}
