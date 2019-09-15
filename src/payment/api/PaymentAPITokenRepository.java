package payment.api;

import java.util.*;

import org.apache.log4j.Logger;


public class PaymentAPITokenRepository {
	Logger logger = Logger.getLogger(getClass());
	static PaymentAPITokenRepository instance = new PaymentAPITokenRepository();
	
	Map<String,String> mapOfTokenToBankCode = new HashMap<>();
	Map<String,String> mapOfBankCodeToToken = new HashMap<>();
	
	
	public String getNewToken(String bankCode){
		
		if(mapOfTokenToBankCode.containsKey(bankCode)){
			String prevToken = mapOfTokenToBankCode.get(bankCode);
			mapOfBankCodeToToken.remove(prevToken);
			mapOfTokenToBankCode.remove(bankCode);
		}
		String uniqueToken = createUniqueToken();
		mapOfTokenToBankCode.put(bankCode, uniqueToken);
		mapOfBankCodeToToken.put(uniqueToken, bankCode);
		return mapOfTokenToBankCode.get(bankCode);
	}
	
	public String getBankCode(String token){
		if(!mapOfBankCodeToToken.containsKey(token)){
			throw new PaymentApiException(ResponseCode.INVALID_CREDENTIAL, "Invalid Token");
		}
		return mapOfBankCodeToToken.get(token);
	}
	
	
	private PaymentAPITokenRepository(){
		
	}
	private String createUniqueToken(){
		return UUID.randomUUID().toString();
	}


	public static PaymentAPITokenRepository getInstance(){
		if(instance == null){
			instance = new PaymentAPITokenRepository();
		}
		
		return instance;
	}
}
