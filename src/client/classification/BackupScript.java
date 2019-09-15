package client.classification;

import common.ModuleConstants;

/**
 * @author Touhid
 *
 */

//DO NOT MODIFY OR RUN THE MAIN METHOD OF THIS CLASS WITHOUT LETTING ME KNOW
public class BackupScript {

	public static void main(String[] args) throws Exception {

		int moduleId = ModuleConstants.Module_ID_COLOCATION;

//		ClientClassificationService service = ServiceDAOFactory.getService(ClientClassificationService.class);
//
//
//		//Govt
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Trade_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Memorandum_Of_Article, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.NID, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.TIN, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Deed_Of_Home_Office_Rent, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Holding_Tax_Of_Home_Office, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Updated_Company_Tax_Certificate, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Updated_VAT_Registration_And_VAT_Certificate, false);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Valid_BTRC_License, false);
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Trade_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Memorandum_Of_Article, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.NID, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.TIN, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Deed_Of_Home_Office_Rent, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Holding_Tax_Of_Home_Office, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Updated_Company_Tax_Certificate, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Updated_VAT_Registration_And_VAT_Certificate, false);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Valid_BTRC_License, false);
//
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Trade_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Memorandum_Of_Article, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.NID, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.TIN, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Deed_Of_Home_Office_Rent, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Holding_Tax_Of_Home_Office, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Updated_Company_Tax_Certificate, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Updated_VAT_Registration_And_VAT_Certificate, false);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Valid_BTRC_License, false);
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Trade_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Memorandum_Of_Article, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.NID, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.TIN, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Deed_Of_Home_Office_Rent, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Holding_Tax_Of_Home_Office, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Updated_Company_Tax_Certificate, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Updated_VAT_Registration_And_VAT_Certificate, false);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Valid_BTRC_License, false);
//
//
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.Educational_Institute, IdentityTypeConstants.Forwarding_Letter, true);
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.Training_Institute, IdentityTypeConstants.Forwarding_Letter, true);
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.Organization, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON,
//				RegistrantCategoryConstants.Organization, IdentityTypeConstants.NID, false);
//
//
//
//
//
//		//Military
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_MILITARY,
//				RegistrantCategoryConstants.Educational_Institute, IdentityTypeConstants.Forwarding_Letter, true);
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_MILITARY,
//				RegistrantCategoryConstants.Training_Institute, IdentityTypeConstants.Forwarding_Letter, true);
//
//
//
//
//
//		//Private
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Limited_Company, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Limited_Company, IdentityTypeConstants.Trade_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Limited_Company, IdentityTypeConstants.Memorandum_Of_Article, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Limited_Company, IdentityTypeConstants.NID, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Limited_Company, IdentityTypeConstants.TIN, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Limited_Company, IdentityTypeConstants.Updated_Company_Tax_Certificate, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Limited_Company, IdentityTypeConstants.Deed_Of_Home_Office_Rent, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Limited_Company, IdentityTypeConstants.Holding_Tax_Of_Home_Office, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Limited_Company, IdentityTypeConstants.Updated_VAT_Registration_And_VAT_Certificate, false);
//
//
//
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Trade_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Memorandum_Of_Article, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.NID, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.TIN, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Updated_Company_Tax_Certificate, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Valid_BTRC_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Deed_Of_Home_Office_Rent, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Holding_Tax_Of_Home_Office, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.IIG, IdentityTypeConstants.Updated_VAT_Registration_And_VAT_Certificate, false);
//
//
//
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Trade_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Memorandum_Of_Article, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.NID, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.TIN, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Updated_Company_Tax_Certificate, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Valid_BTRC_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Deed_Of_Home_Office_Rent, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Holding_Tax_Of_Home_Office, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.ISP, IdentityTypeConstants.Updated_VAT_Registration_And_VAT_Certificate, false);
//
//
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Trade_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Memorandum_Of_Article, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.NID, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.TIN, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Updated_Company_Tax_Certificate, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Valid_BTRC_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Deed_Of_Home_Office_Rent, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Holding_Tax_Of_Home_Office, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.BWA, IdentityTypeConstants.Updated_VAT_Registration_And_VAT_Certificate, false);
//
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Trade_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Memorandum_Of_Article, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.NID, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.TIN, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Updated_Company_Tax_Certificate, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Valid_BTRC_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Deed_Of_Home_Office_Rent, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Holding_Tax_Of_Home_Office, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Mobile_Operator, IdentityTypeConstants.Updated_VAT_Registration_And_VAT_Certificate, false);
//
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Educational_Institute, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Educational_Institute, IdentityTypeConstants.NID, false);
//
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Organization, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Organization, IdentityTypeConstants.Trade_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Organization, IdentityTypeConstants.Memorandum_Of_Article, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Organization, IdentityTypeConstants.NID, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Organization, IdentityTypeConstants.TIN, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Organization, IdentityTypeConstants.Updated_Company_Tax_Certificate, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Organization, IdentityTypeConstants.Deed_Of_Home_Office_Rent, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Organization, IdentityTypeConstants.Holding_Tax_Of_Home_Office, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Organization, IdentityTypeConstants.Updated_VAT_Registration_And_VAT_Certificate, false);
//
//
//
//
//
//
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Call_Center, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Call_Center, IdentityTypeConstants.Trade_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Call_Center, IdentityTypeConstants.Memorandum_Of_Article, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Call_Center, IdentityTypeConstants.NID, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Call_Center, IdentityTypeConstants.TIN, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Call_Center, IdentityTypeConstants.Updated_Company_Tax_Certificate, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Call_Center, IdentityTypeConstants.Valid_BTRC_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Call_Center, IdentityTypeConstants.Deed_Of_Home_Office_Rent, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Call_Center, IdentityTypeConstants.Holding_Tax_Of_Home_Office, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Call_Center, IdentityTypeConstants.Updated_VAT_Registration_And_VAT_Certificate, false);
//
//
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Software_Company, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Software_Company, IdentityTypeConstants.Trade_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Software_Company, IdentityTypeConstants.Memorandum_Of_Article, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Software_Company, IdentityTypeConstants.NID, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Software_Company, IdentityTypeConstants.TIN, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Software_Company, IdentityTypeConstants.Updated_Company_Tax_Certificate, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Software_Company, IdentityTypeConstants.Valid_BTRC_License, false);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Software_Company, IdentityTypeConstants.Deed_Of_Home_Office_Rent, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Software_Company, IdentityTypeConstants.Holding_Tax_Of_Home_Office, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Software_Company, IdentityTypeConstants.Updated_VAT_Registration_And_VAT_Certificate, false);
//
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Corporate_Office, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Corporate_Office, IdentityTypeConstants.Trade_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Corporate_Office, IdentityTypeConstants.Memorandum_Of_Article, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Corporate_Office, IdentityTypeConstants.NID, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Corporate_Office, IdentityTypeConstants.TIN, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Corporate_Office, IdentityTypeConstants.Updated_Company_Tax_Certificate, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Corporate_Office, IdentityTypeConstants.Deed_Of_Home_Office_Rent, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Corporate_Office, IdentityTypeConstants.Holding_Tax_Of_Home_Office, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Corporate_Office, IdentityTypeConstants.Updated_VAT_Registration_And_VAT_Certificate, false);
//
//
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Bank_Insurance_Company, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Bank_Insurance_Company, IdentityTypeConstants.Trade_License, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Bank_Insurance_Company, IdentityTypeConstants.Memorandum_Of_Article, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Bank_Insurance_Company, IdentityTypeConstants.NID, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Bank_Insurance_Company, IdentityTypeConstants.TIN, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Bank_Insurance_Company, IdentityTypeConstants.Updated_Company_Tax_Certificate, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Bank_Insurance_Company, IdentityTypeConstants.Deed_Of_Home_Office_Rent, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Bank_Insurance_Company, IdentityTypeConstants.Holding_Tax_Of_Home_Office, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_PRIVATE,
//				RegistrantCategoryConstants.Bank_Insurance_Company, IdentityTypeConstants.Updated_VAT_Registration_And_VAT_Certificate, false);
//
//
//
//
//
//		//foreign
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_FOREIGN,
//				RegistrantCategoryConstants.International_University, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_FOREIGN,
//				RegistrantCategoryConstants.International_University, IdentityTypeConstants.NID, false);
//
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_FOREIGN,
//				RegistrantCategoryConstants.International_Training_And_Research_Center, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_FOREIGN,
//				RegistrantCategoryConstants.International_Training_And_Research_Center, IdentityTypeConstants.NID, false);
//
//
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_FOREIGN,
//				RegistrantCategoryConstants.International_Organization, IdentityTypeConstants.Forwarding_Letter, true);
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_FOREIGN,
//				RegistrantCategoryConstants.International_Organization, IdentityTypeConstants.NID, false);
//
//
//		//Others
//
//		service.addRequiredDocInARegistrantCategory(moduleId, RegistrantTypeConstants.REGISTRANT_TYPE_OTHERS,
//				RegistrantCategoryConstants.Educational_Research_Network, IdentityTypeConstants.Forwarding_Letter, true);



		System.out.println("done");
	}
}