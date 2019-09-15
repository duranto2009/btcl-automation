package accounting;

import java.util.*;


enum Type{DEBIT,CREDIT}

public enum AccountType {

	ASSET(1,"Asset",Type.DEBIT,null),
	EQUITY(2,"Equity",Type.CREDIT,null),
	LIABILITY(3,"Liability Account",Type.CREDIT,null),
	
	
	ACCOUNT_RECEIVABLE(4,"Account Receivable",Type.DEBIT,ASSET),
	ACCOUNT_RECEIVABLE_TD(5,"Account Receivable TD",Type.DEBIT, ACCOUNT_RECEIVABLE),
	ACCOUNT_RECEIVABLE_NOT_TD(6,"Account Receivable Not TD",Type.DEBIT, ACCOUNT_RECEIVABLE),
	//BTCL_RECEIVEABLE_TD(7,"Btcl",Type.DEBIT,ACCOUNT_RECEIVABLE_TD),
	//VAT_RECEIVEABLE_TD(8,"Vat Receiveable",Type.DEBIT,ACCOUNT_RECEIVABLE_TD),
	//BTCL_RECEIVEABLE_NOT_TD(9,"Btcl",Type.DEBIT,ACCOUNT_RECEIVABLE_NOT_TD),
	//VAT_RECEIVEABLE_NOT_TD(10,"Vat Receiveable",Type.DEBIT,ACCOUNT_RECEIVABLE_NOT_TD),
	CASH(11,"Cash Account",Type.DEBIT,ASSET),
	UNVERIFIED_CASH(12,"Temp Cash",Type.DEBIT,ASSET),
	DISCOUNT(13,"Discount",Type.DEBIT,ASSET),
	SECURITY(14,"Security Account",Type.CREDIT,LIABILITY),
	ADJUSTABLE(15,"Adjustable Account",Type.CREDIT,LIABILITY),
	UPGRADATION_CHARGE(16,"Upgradation Charge",Type.CREDIT,EQUITY),
	VAT_PAYABLE_TO_NBR(17,"VAT",Type.CREDIT,LIABILITY),
	FIBRE_OTC(18,"Fibre OTC",Type.CREDIT,EQUITY),
	NEW_CONNECTION_CHARGE(19,"New Connection Charge",Type.CREDIT,EQUITY),
	OTHER_CHARGES(20,"Other Charges",Type.CREDIT,EQUITY),
	LOCAL_LOOP_CHARGE(21,"Local Loop Charge",Type.CREDIT,EQUITY),
	DOWNGRADE_CHARGE(22,"Downgrade Charge",Type.CREDIT,EQUITY),
	PORT_CHARGE(23,"Port Charge",Type.CREDIT,EQUITY),
	CORE_CHARGE(24,"Core Charge",Type.CREDIT,EQUITY),
	IP_COST(25,"Ip Cost",Type.CREDIT,EQUITY),
	SHIFT_COST(26,"Shift Cost",Type.CREDIT,EQUITY),
	CLOSING_OTC(27,"Closing OTC",Type.CREDIT,EQUITY),
	BANDWIDTH_SHIFT_CHARGE(28,"Bandwidth Shift Charge",Type.CREDIT,EQUITY),
	OWNERSHIP_CHANGE_CHARGE(29,"Ownership Change Charge",Type.CREDIT,EQUITY),
	LONG_TERM_BREAK_FINE(30,"Long Term Break Fine",Type.CREDIT,EQUITY),
	RECONNECT(31, "reconnect Connection Charge", Type.CREDIT, EQUITY),
	BANDWIDTH_COST(32, "Bandwidth Charge", Type.CREDIT, EQUITY),

	NIX_DISCOUNT(33, "Discount(NIX)", Type.DEBIT, ASSET),
	NIX_SECURITY(34, "Security Account(NIX)", Type.CREDIT, LIABILITY),
	NIX_NEW_CONNECTION_CHARGE (35, "New Connection Charge(NIX)", Type.CREDIT, EQUITY),
	NIX_ADJUSTABLE(36,"Adjustable Account(NIX)",Type.CREDIT,LIABILITY),
	NIX_LOCAL_LOOP(37,"Local Loop Charge(NIX)",Type.CREDIT,EQUITY),
	NIX_PORT_UPGRADE(38,"Port Upgrade Charge(NIX)",Type.CREDIT,EQUITY),
	NIX_PORT_DOWNGRADE(39,"Port Downgrade Charge(NIX)",Type.CREDIT,EQUITY),
	NIX_PORT(40,"Port Charge(NIX)",Type.CREDIT,EQUITY),
	NIX_INSTANT_DEGRADATION(41,"Instant Degradation Charge(NIX)",Type.CREDIT,EQUITY),
	NIX_RECONNECT(42, "reconnect Charge(NIX)", Type.CREDIT, EQUITY),
	NIX_CLOSING_OTC(43,"Closing Charge(NIX)",Type.CREDIT,EQUITY),
	NIX_CASH(44,"Cash Account(NIX)",Type.DEBIT,ASSET),
	NIX_ACCOUNT_RECEIVABLE_TD(45,"Account Receivable TD(NIX)",Type.DEBIT, ACCOUNT_RECEIVABLE),
	NIX_ACCOUNT_RECEIVABLE_NOT_TD(46,"Account Receivable Not TD(NIX)",Type.DEBIT, ACCOUNT_RECEIVABLE),
	NIX_VAT_PAYABLE_TO_NBR(47,"VAT(NIX)",Type.CREDIT,LIABILITY),


	COLOCATION_DISCOUNT(400, "Discount(COLOCATION)", Type.DEBIT, ASSET),
	COLOCATION_ADJUSTABLE(401,"Adjustable Account(COLOCATION)",Type.CREDIT,LIABILITY),
	COLOCATION_RECONNECT(402, "reconnect Charge(COLOCATION)", Type.CREDIT, EQUITY),
	COLOCATION_CLOSING_OTC(403,"Closing Charge(COLOCATION)",Type.CREDIT,EQUITY),
	COLOCATION_CASH(404,"Cash Account(COLOCATION)",Type.DEBIT,ASSET),
	COLOCATION_ACCOUNT_RECEIVABLE_TD(405,"Account Receivable TD(COLOCATION)",Type.DEBIT, ACCOUNT_RECEIVABLE),
	COLOCATION_ACCOUNT_RECEIVABLE_NOT_TD(406,"Account Receivable Not TD(COLOCATION)",Type.DEBIT, ACCOUNT_RECEIVABLE),
	COLOCATION_RACK_COST(407, "Space Cost(CoLocation)", Type.CREDIT, EQUITY),
	COLOCATION_OFC_COST(408, "Fiber Cost(CoLocation)", Type.CREDIT, EQUITY),
	COLOCATION_POWER_COST(409, "Power Cost(CoLocation)", Type.CREDIT, EQUITY),
	COLOCATION_UPGRADE_COST(410, "Upgrade Cost(CoLocation)", Type.CREDIT, EQUITY),
	COLOCATION_DOWNGRADE_COST(411, "Downgrade Cost(CoLocation)", Type.CREDIT, EQUITY),
	COLOCATION_VAT_PAYABLE_TO_NBR(412,"VAT(CoLocation)",Type.CREDIT,LIABILITY),
	COLOCATION_YEARLY_ADJUSTMENT(413,"CoLocation Yearly Adjustment",Type.DEBIT,EQUITY),
	COLOCATION_FLOOR_SPACE_COST(414, "Floor Space Cost(CoLocation)", Type.CREDIT, EQUITY),



	VPN_ACCOUNT_RECEIVABLE_TD (601, "Account Receivable TD (VPN)", Type.DEBIT, ACCOUNT_RECEIVABLE),
	VPN_CASH(602, "Cash (VPN)", Type.DEBIT, ASSET),
    VPN_SECURITY(603, "Security(VPN)", Type.CREDIT, LIABILITY),
    VPN_NEW_NETWORK_COST (604, "New Network Cost(VPN)", Type.CREDIT, EQUITY),
	VPN_OTC_LOCAL_LOOP_BTCL(618, "OTC Local Loop BTCL (VPN)", Type.CREDIT, EQUITY),
    VPN_BANDWIDTH_COST (605, "Bandwidth Cost(VPN)[with discount]", Type.CREDIT, EQUITY),
    VPN_LOCAL_LOOP_COST (607, "Local Loop Cost(VPN)", Type.CREDIT, EQUITY),
    VPN_DEGRADATION_COST (608, "Degradation Cost(VPN)", Type.CREDIT, EQUITY),
    VPN_RECONNECT_COST (609, "Reconnect Cost(VPN)", Type.CREDIT, EQUITY),
    VPN_CLOSING_COST (610, "Closing Cost(VPN)", Type.CREDIT, EQUITY),
    VPN_SHIFTING_COST (611, "Shifting Cost(VPN)", Type.CREDIT, EQUITY),
    VPN_OWNERSHIP_CHANGE_COST (612, "Ownership Change Cost(VPN)", Type.CREDIT, EQUITY),
    VPN_OTHER_COST (613, "Other Cost(VPN)", Type.CREDIT, EQUITY),
    VPN_ADJUSTABLE_COST (614, "Advance Adjustable(VPN)", Type.CREDIT, EQUITY),
    VPN_DISCOUNT(615, "Discount(VPN)", Type.DEBIT, ASSET),
    VPN_ACCOUNT_RECEIVABLE_NOT_TD(616,"Account Receivable Not TD(VPN)",Type.DEBIT, ACCOUNT_RECEIVABLE),
    VPN_VAT_PAYABLE_TO_NBR(617,"VAT(VPN)",Type.CREDIT,LIABILITY),


	;


    private static int accountIDCounter = 0;
	
	private int accountID;
	
	private String accountName;
	
	private static Map<AccountType,List<AccountType>> mapOfChildListToParentType; 
	
	private static Map<Integer,AccountType> mapOfAccountTypeToAccountID;
	
	private Type type;
	
	
	private static void populateParentChildRelationship(AccountType parentAccountType,AccountType accountType){
		if(mapOfChildListToParentType==null) {
			mapOfChildListToParentType = new HashMap<>();
		}
		if(!mapOfChildListToParentType.containsKey(parentAccountType)){
			mapOfChildListToParentType.put(parentAccountType, new ArrayList<>());
		}
		mapOfChildListToParentType.get(parentAccountType).add(accountType);
	}
	
	
	private static void populateAccountIDMapByAccountTYpe(AccountType accountType){
		if(mapOfAccountTypeToAccountID==null) {
			mapOfAccountTypeToAccountID = new HashMap<>();
		}
		if(mapOfAccountTypeToAccountID.containsKey(accountType.getID())){
			AccountType oldAccountType = mapOfAccountTypeToAccountID.get(accountType.getID());
			throw new RuntimeException("Two Account Type ("+accountType.getName()+","+oldAccountType.getName()
			+") exists with same account ID "+accountType.getID());
		}
		mapOfAccountTypeToAccountID.put(accountType.getID(), accountType);
	}
	
	private static int getNextAccountIDCounter(){
		return ++accountIDCounter;
	}


	AccountType(int accountID,String accountName,Type type,AccountType parentAccountType){
		this.accountID = accountID;
		this.accountName = accountName;
		this.type = type;
		populateAccountIDMapByAccountTYpe(this);
		populateParentChildRelationship(parentAccountType,this);
		
	}
	public boolean isDebitAccount(){
		return type == Type.DEBIT;
	}
	public boolean isCreditAccount(){
		return type == Type.CREDIT;
	}
	
	public int getID(){
		return accountID;
	}
	
	public String getName(){
		return accountName;
	}
	
	public boolean isLeafAccount(){
		return !mapOfChildListToParentType.containsKey(this);
	}
	
	public static List<AccountType> getFirstLevelAccountTypes () {
		return mapOfChildListToParentType.get(null);
	}
	public static AccountType getAccountTypeByAccountID(int accountID){
		return mapOfAccountTypeToAccountID.get(accountID);
	}
	
	public static boolean isValidAccountID(int accountID){
		return mapOfAccountTypeToAccountID.containsKey(accountID);
	} 
	
	
	public  List<AccountType> getLeafAccountTypeList(){
		List<AccountType> resultList = new ArrayList<>();
		resultList.add(this);
		if(this.isLeafAccount()){
			return resultList;
		}
		
		for(AccountType childAccountType: AccountType.mapOfChildListToParentType.getOrDefault(this,Collections.emptyList())){
			resultList.addAll(childAccountType.getLeafAccountTypeList());
		}
		
		return resultList;
	}
}
