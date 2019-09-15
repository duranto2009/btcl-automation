package client;

import java.util.HashMap;
import java.util.Map;

public class RegistrantCategoryConstants {
	public static final long IIG = 1;
	public static final long ISP = 2;
	public static final long PROJECT = 3;
	public static final long CORPORATE_BODY = 4;
	public static final long OTHERS = 30;
	
	public static final long Educational_Institute = 5;
	public static final long Training_Institute = 6;
	public static final long Educational_Research_Network = 7;
	public static final long Organization = 8;
	public static final long International_University = 9;
	public static final long International_Training_And_Research_Center = 10;
	public static final long International_Organization = 11;
	public static final long Call_Center = 12;
	public static final long Software_Company = 13;
	public static final long Corporate_Office = 14;
	public static final long Bank_Insurance_Company = 15;
	public static final long Limited_Company = 16;
	public static final long BWA = 17;
	public static final long Mobile_Operator = 18;
	
	
	//Added Later for Domain Module
	public static final long Educational = 19;
	public static final long Financial = 20;
	public static final long NGO = 21;
	public static final long Institute = 22;
	public static final long IT = 23;
	public static final long Company = 24;
	
	/*
	domainRegTypeMap.put(REG_TYPE_EDUCATIONAL, "Educational");	//New Value 19
	domainRegTypeMap.put(REG_TYPE_FINANCIAL, "Financial");		//New Value 20
	domainRegTypeMap.put(REG_TYPE_ORGANIZATION, "Organization");//Already exists in 8
	domainRegTypeMap.put(REG_TYPE_NGO, "NGO");					//New Value 21
	domainRegTypeMap.put(REG_TYPE_INSTITUTE, "Institute");		//New Value 22
	domainRegTypeMap.put(REG_TYPE_PROJECT, "Project");			//Already exists in 3
	domainRegTypeMap.put(REG_TYPE_IT, "IT");					//New Value 23
	domainRegTypeMap.put(REG_TYPE_COMPANY, "Company");			//New Value 24
	domainRegTypeMap.put(REG_TYPE_OTHERS, "Others");			//Already exists in 30
	*/
	
	
	public static final long ANY = -1; //Extremely sensitive; Handle with Care.
	
	
	public static Map<Long, String> RegistrantCategoryName = new HashMap<>();
	
	static {
		RegistrantCategoryName.put(IIG, "IIG");
		RegistrantCategoryName.put(ISP, "ISP");
		RegistrantCategoryName.put(PROJECT, "Project");
		RegistrantCategoryName.put(CORPORATE_BODY, "Corporate Body");
		RegistrantCategoryName.put(OTHERS, "Others");
		
		RegistrantCategoryName.put(Educational_Institute, "Educational Institute");
		RegistrantCategoryName.put(Training_Institute, "Training Institute");
		RegistrantCategoryName.put(Educational_Research_Network, "Educational Research Network");
		RegistrantCategoryName.put(Organization, "Organization");
		RegistrantCategoryName.put(International_University, "International University");
		RegistrantCategoryName.put(International_Training_And_Research_Center, "International Training and Research Center");
		RegistrantCategoryName.put(International_Organization, "International Organization");
		RegistrantCategoryName.put(Call_Center, "Call Center");
		RegistrantCategoryName.put(Software_Company, "Software Company");
		RegistrantCategoryName.put(Corporate_Office, "Corporate Office");
		RegistrantCategoryName.put(Bank_Insurance_Company, "Bank/Insurance Company");
		RegistrantCategoryName.put(Limited_Company, "Limited Company");
		RegistrantCategoryName.put(BWA, "BWA");
		RegistrantCategoryName.put(Mobile_Operator, "Mobile Operator");
		
		//Added Later for Domain Module
		RegistrantCategoryName.put(Educational, "Educational");
		RegistrantCategoryName.put(Financial, "Financial");
		RegistrantCategoryName.put(NGO, "NGO");
		RegistrantCategoryName.put(Institute, "Institute");
		RegistrantCategoryName.put(IT, "IT");
		RegistrantCategoryName.put(Company, "Company");
	}
}
