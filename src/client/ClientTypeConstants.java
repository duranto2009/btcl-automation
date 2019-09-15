package client;

import common.ModuleConstants;
import common.ObjectPair;
import common.RequestFailureException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class FieldType {
	public static final int BOTH = 1;
	public static final int FILE = 2;
	public static final int NUMBER = 3;
}



class ClientRegistrationDocumentDescrptor{
	public List<ObjectPair<Integer,Boolean>> getDocumentValidityList(){
		return new ArrayList<>();
	}
	
	ClientRegistrationDocumentDescrptor(Integer moduleID, Integer suffix, Integer fieldType, Boolean isMandatory, List<Integer> alternativeDocumentSuffixList){
		this.id = suffix;
		//this.name = IdentityTypeConstants.IdentityTypeName.get(suffix);
		this.fieldType = fieldType;
		this.isMandatory = isMandatory;
		
		this.alternativeDocumentIdList = new ArrayList<>();
		for(Integer alternativeDocumentSuffix : alternativeDocumentSuffixList) {
			this.alternativeDocumentIdList.add(moduleID * 10000 + alternativeDocumentSuffix);
		}
	}
	
	public Integer id;
	public String name;
	public String description;
	public Integer fieldType;
	public Boolean isMandatory;
	public List<Integer> alternativeDocumentIdList;
}


public class ClientTypeConstants {

	public static final int INVALID_DOCUMENT = 1;
	public static final int OPTIONAL_DOCUMENT = 2;
	public static final int MANDATORY_DOCUMENT = 3;
	

	public static boolean isValidDocumentID(int moduleID,int registrantType,int registrantCategory,int documentID) {
		
		if(!mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID.containsKey(moduleID)) {
			return false;
		}
		
		if(!mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID.get(moduleID).containsKey(registrantType)) {
			return false;
		}
		if(!mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID.get(moduleID).get(registrantType).containsKey(registrantCategory)) {
			return false;
		}
		if(!mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID.get(moduleID).get(registrantType).get(registrantCategory).containsKey(documentID)) {
			return false;
		}
		return true;
	}
		
		
	public static int getDocumentType(int moduleID,int registrantType,int registrantCategory,int documentID) {
		if(!isValidDocumentID(moduleID, registrantType, registrantCategory, documentID)) {
			return INVALID_DOCUMENT;
		}
		return mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID.get(moduleID).get(registrantType)
				.get(registrantCategory).get(documentID)?MANDATORY_DOCUMENT:OPTIONAL_DOCUMENT;
	} 
		
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<ObjectPair<Integer,String>> getRegistrantTypeListByModuleID(int moduleID) {
		if(!mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID.containsKey(moduleID)) {
			throw new  RequestFailureException("No such module exists");
		}
		
		List<ObjectPair<Integer,String>> objectPairs = new ArrayList<ObjectPair<Integer,String>>();
		for(int registrantType: mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID.get(moduleID).keySet()) {
			ObjectPair< Integer, String> objectPair = new ObjectPair(registrantType, RegistrantTypeConstants.RegistrantTypeName.get(registrantType)); 
			objectPairs.add(objectPair);
		}
		return objectPairs;
	}
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<ObjectPair<Integer, String>> getRegistrationCategoryByModuleIDAndRegistrationType(int moduleID,int registrantType){
		List<ObjectPair<Integer, String>> objectPairs = new ArrayList<>();
		
		for(long registrantCategory:mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID.get(moduleID).get(registrantType).keySet()) {
			objectPairs.add(new ObjectPair(registrantCategory, client.RegistrantCategoryConstants.RegistrantCategoryName.get(registrantCategory)));
		}
		return objectPairs;
	}
	
	
	public static List<ClientRegistrationDocumentDescrptor> getClientRegistrationDocumentDescriptorByModuleIDAndRegTypeAndRegCat(int moduleID,int registrantTypeID,long registrantCategoryId){
		
		return DocumentDescriptorToCatIDToRegTypeIDToModuleID.get(moduleID).get(registrantTypeID).get(registrantCategoryId);
	}
	public static void populateValidDocumentsForCompany(int moduleID,int registrantType,long registrantCategory, int categoryID, ClientRegistrationDocumentDescrptor clientRegistrationDocumentDescrptor,ClientRegistrationDocumentDescrptor... clientRegistrationDocumentDescrptors) {
		populateValidDocumentsForCompany(moduleID, registrantType, registrantCategory, clientRegistrationDocumentDescrptor);
		
		for(ClientRegistrationDocumentDescrptor clientRegistrationDocumentDescrptortm: clientRegistrationDocumentDescrptors) {
			populateValidDocumentsForCompany(moduleID, registrantType, registrantCategory, clientRegistrationDocumentDescrptortm);
		}
		
		populateClientCategoryToCategoryToTypeToModuleMap(moduleID, registrantType, registrantCategory, categoryID);
	}
		
	private static void populateClientCategoryToCategoryToTypeToModuleMap(int moduleID, int registrantType,	long registrantCategory, int categoryID) {
		if(!ClientCategoryIDToCatIDToRegTypeIDToModuleID.containsKey(moduleID)) {
			ClientCategoryIDToCatIDToRegTypeIDToModuleID.put(moduleID, new HashMap<>());
		}
		
		if(!ClientCategoryIDToCatIDToRegTypeIDToModuleID.get(moduleID).containsKey(registrantType)) {
			ClientCategoryIDToCatIDToRegTypeIDToModuleID.get(moduleID).put(registrantType, new HashMap<>());
		}

		if(!ClientCategoryIDToCatIDToRegTypeIDToModuleID.get(moduleID).get(registrantType).containsKey(registrantCategory)) {
			ClientCategoryIDToCatIDToRegTypeIDToModuleID.get(moduleID).get(registrantType).put(registrantCategory,categoryID);
		}
	}


	private static void populateValidDocumentsForCompany(int moduleID,int registrantType,long registrantCategory, ClientRegistrationDocumentDescrptor clientRegistrationDocumentDescrptor) {
		if(!mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID.containsKey(moduleID)) {
			mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID.put(moduleID, new HashMap<>());
		}
		if(!DocumentDescriptorToCatIDToRegTypeIDToModuleID.containsKey(moduleID)) {
			DocumentDescriptorToCatIDToRegTypeIDToModuleID.put(moduleID, new HashMap<>());
		}
		
		if(!mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID.get(moduleID).containsKey(registrantType)) {
			mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID.get(moduleID).put(registrantType, new HashMap<>());
		}

		if(!DocumentDescriptorToCatIDToRegTypeIDToModuleID.get(moduleID).containsKey(registrantType)) {
			DocumentDescriptorToCatIDToRegTypeIDToModuleID.get(moduleID).put(registrantType, new HashMap<>());
		}
		if(!mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID.get(moduleID).get(registrantType).containsKey(registrantCategory)) {
			mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID.get(moduleID).get(registrantType).put(registrantCategory,new HashMap<>());
		}
		

		if(!DocumentDescriptorToCatIDToRegTypeIDToModuleID.get(moduleID).get(registrantType).containsKey(registrantCategory)) {
			DocumentDescriptorToCatIDToRegTypeIDToModuleID.get(moduleID).get(registrantType).put(registrantCategory,new ArrayList<>());
		}
		
		DocumentDescriptorToCatIDToRegTypeIDToModuleID.get(moduleID).get(registrantType).get(registrantCategory).add(clientRegistrationDocumentDescrptor);
		
		for(ObjectPair<Integer, Boolean> objectPair: clientRegistrationDocumentDescrptor.getDocumentValidityList()) {
			mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID.get(moduleID).get(registrantType).get(registrantCategory).put(objectPair.key,objectPair.value);
		}
	}
		
	//Company Registrant
	// moduleID->registrantType->registrantCategory->documentID->isEssential
	public static Map<Integer,Map<Integer,Map<Long,Map<Integer,Boolean>>>> mapOfValidityToDocIDToCatIDToRegTypeIDToModuleID = new HashMap<>();
	// moduleID->registrantType->registrantCategory->documentID->Descriptor
	public static Map<Integer,Map<Integer,Map<Long,List<ClientRegistrationDocumentDescrptor>>>> DocumentDescriptorToCatIDToRegTypeIDToModuleID = new HashMap<>();
	// moduleID->registrantType->registrantCategory->CategoryID
	public static Map<Integer,Map<Integer,Map<Long,Integer>>> ClientCategoryIDToCatIDToRegTypeIDToModuleID = new HashMap<>();
	
	
	
	public static List<ClientRegistrationDocumentDescrptor> getClientRegistrationDocumentDescriptorListByModuleID(Integer moduleID) {
		return DocumentDescriptorToModuleID.get(moduleID);
	}
	
	
	public static void populateValidDocumentsForIndividual(int moduleID, ClientRegistrationDocumentDescrptor... clientRegistrationDocumentDescrptors) {
		if(!mapOfValidityToModuleID.containsKey(moduleID)) {
			mapOfValidityToModuleID.put(moduleID, true);
		}

		if(!DocumentDescriptorToModuleID.containsKey(moduleID)) {
			DocumentDescriptorToModuleID.put(moduleID,new ArrayList<>());
		}
		
		for(ClientRegistrationDocumentDescrptor clientRegistrationDocumentDescrptor: clientRegistrationDocumentDescrptors) {
			DocumentDescriptorToModuleID.get(moduleID).add(clientRegistrationDocumentDescrptor);
		}
	}
	
	//Individual Registrant
	public static Map<Integer,List<ClientRegistrationDocumentDescrptor>> DocumentDescriptorToModuleID = new HashMap<>();
	public static Map<Integer,Boolean> mapOfValidityToModuleID = new HashMap<>();


	static {
		Integer moduleID;
		List<Integer> alternativeDocumentSuffixList = new ArrayList<>();


		//LLI
		moduleID = ModuleConstants.Module_ID_LLI;

		//Company
		ClientRegistrationDocumentDescrptor mandatoryForwardingLetter = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Forwarding_Letter, FieldType.BOTH, true, alternativeDocumentSuffixList);
		ClientRegistrationDocumentDescrptor mandatoryTradeLicense = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Trade_License, FieldType.BOTH, true, alternativeDocumentSuffixList);
		ClientRegistrationDocumentDescrptor mandatoryMemorandumOfArticle = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Memorandum_Of_Article, FieldType.BOTH, true, alternativeDocumentSuffixList);

		alternativeDocumentSuffixList.add(IdentityTypeConstants.Passport);
		ClientRegistrationDocumentDescrptor mandatoryNIDwithPassport = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.NID, FieldType.BOTH, true, alternativeDocumentSuffixList);
		alternativeDocumentSuffixList = new ArrayList<>();

		alternativeDocumentSuffixList.add(IdentityTypeConstants.NID);
		ClientRegistrationDocumentDescrptor mandatoryPassportwithNID = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Passport, FieldType.BOTH, true, alternativeDocumentSuffixList);
		alternativeDocumentSuffixList = new ArrayList<>();

		ClientRegistrationDocumentDescrptor mandatoryPhotograph = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Photograph, FieldType.FILE, true, alternativeDocumentSuffixList);
		ClientRegistrationDocumentDescrptor mandatoryTIN = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.TIN, FieldType.BOTH, true, alternativeDocumentSuffixList);
		ClientRegistrationDocumentDescrptor mandatoryUpdatedCompanyTax = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Updated_Company_Tax_Certificate, FieldType.BOTH, true, alternativeDocumentSuffixList);
		ClientRegistrationDocumentDescrptor mandatoryValidBTRC = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Valid_BTRC_License, FieldType.BOTH, true, alternativeDocumentSuffixList);
		ClientRegistrationDocumentDescrptor mandatoryDeedOfHome = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Deed_Of_Home_Office_Rent, FieldType.FILE, true, alternativeDocumentSuffixList);
		ClientRegistrationDocumentDescrptor mandatoryHoldingTax = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Holding_Tax_Of_Home_Office, FieldType.FILE, true, alternativeDocumentSuffixList);


		ClientRegistrationDocumentDescrptor optionalNID = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.NID, FieldType.BOTH, false, alternativeDocumentSuffixList);
		ClientRegistrationDocumentDescrptor optionalPhotograph = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Photograph, FieldType.FILE, false, alternativeDocumentSuffixList);
		ClientRegistrationDocumentDescrptor optionalVATCertificate = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Updated_VAT_Registration_And_VAT_Certificate, FieldType.FILE, false, alternativeDocumentSuffixList);
		ClientRegistrationDocumentDescrptor optionalValidBTRC = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Valid_BTRC_License, FieldType.FILE, false, alternativeDocumentSuffixList);



		//Category 1
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.Educational_Institute, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.Training_Institute, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.Educational_Institute, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.Training_Institute, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.Educational_Research_Network, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);


		//Category 2
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.Organization, ClientCategoryConstants.Category_2, mandatoryForwardingLetter, optionalNID, optionalPhotograph);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.Educational_Institute, ClientCategoryConstants.Category_2, mandatoryForwardingLetter, optionalNID, optionalPhotograph);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.International_University, ClientCategoryConstants.Category_2, mandatoryForwardingLetter, optionalNID, optionalPhotograph);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.International_Training_And_Research_Center, ClientCategoryConstants.Category_2, mandatoryForwardingLetter, optionalNID, optionalPhotograph);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.International_Organization, ClientCategoryConstants.Category_2, mandatoryForwardingLetter, optionalNID, optionalPhotograph);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.Organization, ClientCategoryConstants.Category_2, mandatoryForwardingLetter);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.Call_Center, ClientCategoryConstants.Category_2, mandatoryForwardingLetter, mandatoryTradeLicense, mandatoryMemorandumOfArticle, mandatoryNIDwithPassport, mandatoryPassportwithNID, mandatoryPhotograph, mandatoryTIN, mandatoryUpdatedCompanyTax, mandatoryValidBTRC, mandatoryDeedOfHome, mandatoryHoldingTax, optionalVATCertificate);;
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.Software_Company, ClientCategoryConstants.Category_2, mandatoryForwardingLetter, mandatoryTradeLicense, mandatoryMemorandumOfArticle, mandatoryNIDwithPassport, mandatoryPassportwithNID, mandatoryPhotograph, mandatoryTIN, mandatoryUpdatedCompanyTax, mandatoryValidBTRC, mandatoryDeedOfHome, mandatoryHoldingTax, optionalVATCertificate);


		//Category 3
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.Corporate_Office, ClientCategoryConstants.Category_3, mandatoryForwardingLetter, mandatoryTradeLicense, mandatoryMemorandumOfArticle, mandatoryNIDwithPassport, mandatoryPassportwithNID, mandatoryPhotograph, mandatoryTIN, mandatoryUpdatedCompanyTax, mandatoryDeedOfHome, mandatoryHoldingTax, optionalVATCertificate);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.Organization, ClientCategoryConstants.Category_3, mandatoryForwardingLetter, mandatoryTradeLicense, mandatoryMemorandumOfArticle, mandatoryNIDwithPassport, mandatoryPassportwithNID, mandatoryPhotograph, mandatoryTIN, mandatoryUpdatedCompanyTax, mandatoryDeedOfHome, mandatoryHoldingTax, optionalVATCertificate);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.Bank_Insurance_Company, ClientCategoryConstants.Category_3, mandatoryForwardingLetter, mandatoryTradeLicense, mandatoryMemorandumOfArticle, mandatoryNIDwithPassport, mandatoryPassportwithNID, mandatoryPhotograph, mandatoryTIN, mandatoryUpdatedCompanyTax, mandatoryDeedOfHome, mandatoryHoldingTax, optionalVATCertificate);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.Limited_Company, ClientCategoryConstants.Category_3, mandatoryForwardingLetter, mandatoryTradeLicense, mandatoryMemorandumOfArticle, mandatoryNIDwithPassport, mandatoryPassportwithNID, mandatoryPhotograph, mandatoryTIN, mandatoryUpdatedCompanyTax, mandatoryDeedOfHome, mandatoryHoldingTax, optionalVATCertificate);

		//Category 4
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.ISP, ClientCategoryConstants.Category_4, mandatoryForwardingLetter, mandatoryTradeLicense, mandatoryMemorandumOfArticle, mandatoryNIDwithPassport, mandatoryPassportwithNID, mandatoryPhotograph, mandatoryTIN, mandatoryUpdatedCompanyTax, mandatoryValidBTRC, mandatoryDeedOfHome, mandatoryHoldingTax, optionalVATCertificate);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.IIG, ClientCategoryConstants.Category_4, mandatoryForwardingLetter, mandatoryTradeLicense, mandatoryMemorandumOfArticle, mandatoryNIDwithPassport, mandatoryPassportwithNID, mandatoryPhotograph, mandatoryTIN, mandatoryUpdatedCompanyTax, mandatoryValidBTRC, mandatoryDeedOfHome, mandatoryHoldingTax, optionalVATCertificate);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.BWA, ClientCategoryConstants.Category_4, mandatoryForwardingLetter, mandatoryTradeLicense, mandatoryMemorandumOfArticle, mandatoryNIDwithPassport, mandatoryPassportwithNID, mandatoryPhotograph, mandatoryTIN, mandatoryUpdatedCompanyTax, mandatoryValidBTRC, mandatoryDeedOfHome, mandatoryHoldingTax, optionalVATCertificate);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.Mobile_Operator, ClientCategoryConstants.Category_4, mandatoryForwardingLetter, mandatoryTradeLicense, mandatoryMemorandumOfArticle, mandatoryNIDwithPassport, mandatoryPassportwithNID, mandatoryPhotograph, mandatoryTIN, mandatoryUpdatedCompanyTax, mandatoryValidBTRC, mandatoryDeedOfHome, mandatoryHoldingTax, optionalVATCertificate);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.ISP, ClientCategoryConstants.Category_4, mandatoryForwardingLetter, mandatoryTradeLicense, mandatoryMemorandumOfArticle, mandatoryNIDwithPassport, mandatoryPassportwithNID, mandatoryPhotograph, mandatoryTIN, mandatoryUpdatedCompanyTax, mandatoryDeedOfHome, mandatoryHoldingTax, optionalVATCertificate, optionalValidBTRC);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.IIG, ClientCategoryConstants.Category_4, mandatoryForwardingLetter, mandatoryTradeLicense, mandatoryMemorandumOfArticle, mandatoryNIDwithPassport, mandatoryPassportwithNID, mandatoryPhotograph, mandatoryTIN, mandatoryUpdatedCompanyTax, mandatoryDeedOfHome, mandatoryHoldingTax, optionalVATCertificate, optionalValidBTRC);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.BWA, ClientCategoryConstants.Category_4, mandatoryForwardingLetter, mandatoryTradeLicense, mandatoryMemorandumOfArticle, mandatoryNIDwithPassport, mandatoryPassportwithNID, mandatoryPhotograph, mandatoryTIN, mandatoryUpdatedCompanyTax, mandatoryDeedOfHome, mandatoryHoldingTax, optionalVATCertificate, optionalValidBTRC);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.Mobile_Operator, ClientCategoryConstants.Category_4, mandatoryForwardingLetter, mandatoryTradeLicense, mandatoryMemorandumOfArticle, mandatoryNIDwithPassport, mandatoryPassportwithNID, mandatoryPhotograph, mandatoryTIN, mandatoryUpdatedCompanyTax, mandatoryDeedOfHome, mandatoryHoldingTax, optionalVATCertificate, optionalValidBTRC);


		//Individual
		populateValidDocumentsForIndividual(moduleID, mandatoryNIDwithPassport);
		populateValidDocumentsForIndividual(moduleID, mandatoryPassportwithNID);

	}

	static {
		Integer moduleID;
		List<Integer> alternativeDocumentSuffixList = new ArrayList<>();


		//Domain
		moduleID = ModuleConstants.Module_ID_DOMAIN;

		//Individual
		alternativeDocumentSuffixList.add(IdentityTypeConstants.Passport);
		ClientRegistrationDocumentDescrptor mandatoryNIDwithPassport = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.NID, FieldType.BOTH, true, alternativeDocumentSuffixList);
		alternativeDocumentSuffixList = new ArrayList<>();

		alternativeDocumentSuffixList.add(IdentityTypeConstants.NID);
		ClientRegistrationDocumentDescrptor mandatoryPassportwithNID = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Passport, FieldType.BOTH, true, alternativeDocumentSuffixList);
		alternativeDocumentSuffixList = new ArrayList<>();

		populateValidDocumentsForIndividual(moduleID, mandatoryNIDwithPassport);
		populateValidDocumentsForIndividual(moduleID, mandatoryPassportwithNID);

		//Company
		alternativeDocumentSuffixList.add(IdentityTypeConstants.Trade_License);
		ClientRegistrationDocumentDescrptor mandatoryTINwithTrade = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.TIN, FieldType.BOTH, true, alternativeDocumentSuffixList);
		alternativeDocumentSuffixList = new ArrayList<>();

		alternativeDocumentSuffixList.add(IdentityTypeConstants.TIN);
		ClientRegistrationDocumentDescrptor mandatoryTradeLicensewithTIN = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Trade_License, FieldType.BOTH, true, alternativeDocumentSuffixList);
		alternativeDocumentSuffixList = new ArrayList<>();

		ClientRegistrationDocumentDescrptor optionalForwardingLetter = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Forwarding_Letter, FieldType.BOTH, false, alternativeDocumentSuffixList);

		//
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.Educational, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.Financial, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.Organization, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.NGO, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.Institute, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.PROJECT, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.IT, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.Company, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.OTHERS, ClientCategoryConstants.Category_1, optionalForwardingLetter);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.Educational, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.Financial, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.Organization, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.NGO, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.Institute, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.PROJECT, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.IT, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.Company, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.OTHERS, ClientCategoryConstants.Category_1, optionalForwardingLetter);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.Educational, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.Financial, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.Organization, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.NGO, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.Institute, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.PROJECT, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.IT, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.Company, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.OTHERS, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.Educational, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.Financial, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.Organization, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.NGO, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.Institute, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.PROJECT, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.IT, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.Company, ClientCategoryConstants.Category_1, optionalForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.OTHERS, ClientCategoryConstants.Category_1, optionalForwardingLetter);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.Educational, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.Financial, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.Organization, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.NGO, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.Institute, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.PROJECT, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.IT, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.Company, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.OTHERS, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
	}


	static {
		Integer moduleID;
		List<Integer> alternativeDocumentSuffixList = new ArrayList<>();


		//VPN
		moduleID = ModuleConstants.Module_ID_VPN;

		//Company
		alternativeDocumentSuffixList.add(IdentityTypeConstants.Trade_License);
		ClientRegistrationDocumentDescrptor mandatoryTINwithTrade = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.TIN, FieldType.BOTH, true, alternativeDocumentSuffixList);
		alternativeDocumentSuffixList = new ArrayList<>();

		alternativeDocumentSuffixList.add(IdentityTypeConstants.TIN);
		ClientRegistrationDocumentDescrptor mandatoryTradeLicensewithTIN = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Trade_License, FieldType.BOTH, true, alternativeDocumentSuffixList);
		alternativeDocumentSuffixList = new ArrayList<>();

		ClientRegistrationDocumentDescrptor mandatoryForwardingLetter = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Forwarding_Letter, FieldType.BOTH, true, alternativeDocumentSuffixList);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.IIG, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.ISP, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.PROJECT, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.CORPORATE_BODY, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.OTHERS, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.IIG, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.ISP, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.PROJECT, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.CORPORATE_BODY, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.OTHERS, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.IIG, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.ISP, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.PROJECT, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.CORPORATE_BODY, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.OTHERS, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.IIG, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.ISP, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.PROJECT, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.CORPORATE_BODY, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.OTHERS, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.IIG, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.ISP, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.PROJECT, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.CORPORATE_BODY, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.OTHERS, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
	}

	static {
		Integer moduleID;
		List<Integer> alternativeDocumentSuffixList = new ArrayList<>();


		//COLOCATION
		moduleID = ModuleConstants.Module_ID_COLOCATION;

		//Company
		alternativeDocumentSuffixList.add(IdentityTypeConstants.Trade_License);
		ClientRegistrationDocumentDescrptor mandatoryTINwithTrade = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.TIN, FieldType.BOTH, true, alternativeDocumentSuffixList);
		alternativeDocumentSuffixList = new ArrayList<>();

		alternativeDocumentSuffixList.add(IdentityTypeConstants.TIN);
		ClientRegistrationDocumentDescrptor mandatoryTradeLicensewithTIN = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Trade_License, FieldType.BOTH, true, alternativeDocumentSuffixList);
		alternativeDocumentSuffixList = new ArrayList<>();

		ClientRegistrationDocumentDescrptor mandatoryForwardingLetter = new ClientRegistrationDocumentDescrptor(moduleID, IdentityTypeConstants.Forwarding_Letter, FieldType.BOTH, true, alternativeDocumentSuffixList);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.IIG, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.ISP, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.PROJECT, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.CORPORATE_BODY, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.GOVT, RegistrantCategoryConstants.OTHERS, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.IIG, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.ISP, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.PROJECT, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.CORPORATE_BODY, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.MILITARY, RegistrantCategoryConstants.OTHERS, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.IIG, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.ISP, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.PROJECT, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.CORPORATE_BODY, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.PRIVATE, RegistrantCategoryConstants.OTHERS, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.IIG, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.ISP, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.PROJECT, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.CORPORATE_BODY, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.FOREIGN, RegistrantCategoryConstants.OTHERS, ClientCategoryConstants.Category_1, mandatoryForwardingLetter);

		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.IIG, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.ISP, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.PROJECT, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.CORPORATE_BODY, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
		populateValidDocumentsForCompany(moduleID, RegistrantTypeConstants.OTHERS, RegistrantCategoryConstants.OTHERS, ClientCategoryConstants.Category_1, mandatoryTINwithTrade, mandatoryTradeLicensewithTIN);
	}


	public static void main(String[] args){
		System.out.println(ClientCategoryIDToCatIDToRegTypeIDToModuleID.get(7).get(3).get(2L));
	}

}
