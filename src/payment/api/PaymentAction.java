package payment.api;

import java.io.PrintWriter;
import java.lang.reflect.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static payment.api.ServiceResponseConstants.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import com.google.gson.*;
import static common.StringUtils.*;

import common.StringUtils;
import common.bill.*;
import common.payment.PaymentService;
import requestMapping.RequestParameter;
import util.FormPopulator;
import util.TimeConverter;
import static org.apache.commons.lang3.ClassUtils.*;
import org.apache.log4j.Logger;




class BankPaymentApiResponse{
	int responseCode;
	Object payload;
	String msg;
	public BankPaymentApiResponse(){}
	public BankPaymentApiResponse(int responseCode,Object payload,String msg){
		this.responseCode = responseCode;
		this.payload = payload;
		this.msg = msg;
	}
}



class BankPaidBillDTOAdapter implements JsonSerializer<BillDTOForBankPayment>{

	@Override
	public JsonElement serialize(BillDTOForBankPayment paramT, Type paramType,
			JsonSerializationContext paramJsonSerializationContext) {
		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("billID", paramT.getID());
		jsonObject.addProperty("serviceType", paramT.getServiceType());
		jsonObject.addProperty("customerID", paramT.getClientID());
		jsonObject.addProperty("customerUserName", paramT.customerUserName());
		jsonObject.addProperty("billPaymentGateway",paramT.getPaymentPortal()==null? null: BillDTO.paymentGatewayMap.get(paramT.getPaymentPortal()));
		jsonObject.addProperty("netPayable", paramT.getNetPayableCeiled());
		jsonObject.addProperty("billToken", paramT.getLastModificationTime());
		jsonObject.addProperty("paymentStatus", paramT.isPayable()?"UnPaid":"Paid");				
		jsonObject.addProperty("paymentTime", !paramT.isPayable()?TimeConverter.getTimeWithTimeStringFromLong(paramT.getPaymentTime()):"");
		jsonObject.addProperty("payerBankName", paramT.getBankCode());
		jsonObject.addProperty("payerBranchCode", paramT.getBranchCode());
		
		return jsonObject;
	}
	
}


class BillDTOAdapter implements JsonSerializer<BillDTO>{

	@Override
	public JsonElement serialize(BillDTO paramT, Type paramType,
			JsonSerializationContext paramJsonSerializationContext) {
		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("billID", paramT.getID());
		jsonObject.addProperty("billAmount", paramT.getNetPayableCeiled());
		jsonObject.addProperty("billToken", paramT.getLastModificationTime());
		jsonObject.addProperty("paymentStatus", paramT.isPayable()?"UnPaid":"Paid");			
		jsonObject.addProperty("paymentTime", paramT.isPaid()?TimeConverter.getTimeWithTimeStringFromLong(new PaymentService().getPaymentDTObyID(paramT.getPaymentID()).getPaymentTime()):"");
		return jsonObject;
	}
	
}


class BankTransactionHistoryAdapter implements JsonSerializer<BankTransactionHistory>{

	@Override
	public JsonElement serialize(BankTransactionHistory bankTransactionHistory, Type arg1, JsonSerializationContext arg2) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("txID", bankTransactionHistory.getID());
		jsonObject.addProperty("billID", bankTransactionHistory.getBillID());
		jsonObject.addProperty("bankCode", bankTransactionHistory.getBankCode());
		jsonObject.addProperty("branchCode", bankTransactionHistory.getBranchCode());
		jsonObject.addProperty("txType", bankTransactionHistory.getTxType());
		jsonObject.addProperty("txTime", TimeConverter.getTimeWithTimeStringFromLong(bankTransactionHistory.getTime()));
		return jsonObject;
	}
	
}



public class PaymentAction extends Action{
	
	
	private Map<String,Method> mapOfMethodToMethodName = null;
	
	
	private Map<String,Method> createMethodMap(){
		Map<String,Method> methodMap = new HashMap<>();
		Class<?> classObject = getClass();
		for(Method method:classObject.getMethods()){
			methodMap.put(method.getName(), method);
		}
		return methodMap;
	}
	
	private Method getMethodByMethodName(String methodName){
		if(mapOfMethodToMethodName==null){
			mapOfMethodToMethodName = createMethodMap();
		}
		return mapOfMethodToMethodName.get(methodName);
	}
	
	
	private Object createObject(Class<?> classObject,String stringValue,String parameterName){
		if(isBlank(stringValue)&&classObject.isPrimitive()){
			throw new PaymentApiException(ResponseCode.INVALID_REQUEST_FORMAT, "Invalid format for "+parameterName);
		}
		if(isBlank(stringValue)){
			return null;
		}
		if(classObject.isPrimitive()){
			classObject = primitiveToWrapper(classObject);
		}
		try{
			if(Date.class.equals(classObject)){
				return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(stringValue);
			}else{
				return classObject.getConstructor(String.class).newInstance(stringValue);
			}
		}catch(Exception ex){
			throw new PaymentApiException(ResponseCode.INVALID_REQUEST_FORMAT, "Invalid format for "+parameterName);
		}
	}
	
	private String getStringValueFromRequest(HttpServletRequest request,String key){
		String stringValue = request.getParameter(key);
		if(stringValue == null){
			stringValue = (String)request.getAttribute(key);
		}
		return stringValue;
	}
	
	private Object[] getMethodParameters(Method method,HttpServletRequest request,Map<Class<?>,Object> parameterMap) throws Exception{
		
		
		
		Parameter[] parameterTypes = method.getParameters();
		Object[] methodParameters = new Object[parameterTypes.length];
		logger.debug("parameter length "+methodParameters.length);
		for(int i=0;i<parameterTypes.length;i++){
			logger.debug("parameter type "+parameterTypes[i].getType());
			
			Parameter parameter = parameterTypes[i];
			Class<?> parameterClass = parameter.getType();
			
			if(parameterMap.containsKey(parameterClass)){
				methodParameters[i] = parameterMap.get(parameterClass);
			}else{
				RequestParameter requestParameter = (RequestParameter)parameter.getAnnotation(RequestParameter.class);
				if(requestParameter == null && (parameterClass.isPrimitive() || isPrimitiveOrWrapper(parameterClass))){
					throw new PaymentApiException(ResponseCode.SERVER_ERROR, "Invalid API design. Please contact the API designer");
				}
					
					
				if(requestParameter != null){
					String parameterName = requestParameter.value();
					String stringValue = getStringValueFromRequest(request, parameterName);
					methodParameters[i] = createObject(parameterClass, stringValue, parameterName);
				}else{
					methodParameters[i] = parameterClass.newInstance();
					FormPopulator.populate(methodParameters[i], request);
				}
				
			}
			logger.debug("methodParameters[i] "+methodParameters[i]);
		}
		return methodParameters;
	}
	
	
	GsonBuilder gsonBuilder = new GsonBuilder();
	BillDTOAdapter billDTOAdapter = new BillDTOAdapter();
	BankPaidBillDTOAdapter bankPaidBillDTOAdapter = new BankPaidBillDTOAdapter();
	BankTransactionHistoryAdapter bankTransactionHistoryAdapter = new BankTransactionHistoryAdapter();
	
	Logger logger = Logger.getLogger(getClass());
	PaymentAPIService paymentAPIService = new PaymentAPIService();
	BillService billService = new BillService();
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.debug(request.getRequestURI());
		String requestedURI = request.getRequestURI();
		int startIndex = requestedURI.lastIndexOf("/")+1;
		int endIndex = requestedURI.lastIndexOf(".");
		String methodName = requestedURI.substring(startIndex, endIndex);
		logger.debug("method name : "+methodName);
		PrintWriter writer = response.getWriter();
		
		@SuppressWarnings("serial")
		Map<Class<?>,Object> parameterMap = new HashMap<Class<?>,Object>(){{ 
			put(ActionMapping.class,mapping);
			put(ActionForm.class,form);
			put(HttpServletRequest.class,request);
			put(HttpServletResponse.class,response);
		}};

		
		try{
			
			
			BankPaymentApiResponse bankPaymentApiResponse = null;
			try{
				if(!methodName.equals("getToken")){
					String bankCode = getBankCode(request);
					request.setAttribute("bankCode", bankCode);
				}
				Method method = getMethodByMethodName(methodName);
				Object[] methodParameters= getMethodParameters(method, request, parameterMap);
				
				
				Object methodReturnObject = null;
				if(!method.getReturnType().equals(Void.class)){
					methodReturnObject = method.invoke(this, methodParameters);
				}
				bankPaymentApiResponse = new BankPaymentApiResponse(ResponseCode.SUCCESS, methodReturnObject, "Success");
			}catch(PaymentApiException ex){
				bankPaymentApiResponse = new BankPaymentApiResponse(ex.errorCode,null,ex.getMessage());
			}catch(InvocationTargetException ex){
				if(ex.getCause()!=null && ex.getCause() instanceof PaymentApiException){
					PaymentApiException paymentApiException = (PaymentApiException)ex.getCause();
					bankPaymentApiResponse = new BankPaymentApiResponse(paymentApiException.errorCode, null, paymentApiException.getMessage());
					if(paymentApiException.errorCode == ResponseCode.SERVER_ERROR){
						logger.debug("fatal",ex.getCause());
					}
				}else{
					logger.debug("fatal",ex.getCause());
					bankPaymentApiResponse = new BankPaymentApiResponse(ResponseCode.SERVER_ERROR,null,"Server Error");
				}
			}catch(Exception ex){
				logger.debug("fatal",ex);
				bankPaymentApiResponse = new BankPaymentApiResponse(ResponseCode.SERVER_ERROR,null,"Server Error");
			}
			
			
			
			gsonBuilder.registerTypeHierarchyAdapter(BillDTO.class, billDTOAdapter);
			gsonBuilder.registerTypeHierarchyAdapter(BillDTOForBankPayment.class, bankPaidBillDTOAdapter);
			gsonBuilder.registerTypeAdapter(BankTransactionHistory.class, bankTransactionHistoryAdapter);
			Gson gson =   gsonBuilder.create();
			try{
				gson.toJson(bankPaymentApiResponse, writer);
			}catch(Exception ex){
				logger.debug("fatal", ex);
			}
			
		}catch(InvalidCredentialException ex){
			FailureResponse failureResponse = new FailureResponse();
			failureResponse.responseCode = TOKEN_EXPIRED_OR_INVALID_USER;
			writer.print(failureResponse);
			writer.flush();
		}
		return null;
	}
	
	private String getBankCode(HttpServletRequest request){
		String token = request.getParameter("token");
		if(token==null){
			throw new PaymentApiException(ResponseCode.INVALID_CREDENTIAL, "Invalid credential");
		}
		String bankCode = PaymentAPITokenRepository.getInstance().getBankCode(token);
		if(bankCode == null){
			throw new PaymentApiException(ResponseCode.INVALID_CREDENTIAL, "Invalid credential");
		}
		
		
		
		return bankCode;
	}
	
	public String getToken(@RequestParameter("userID") String userID,@RequestParameter("password")String password){
		String token = paymentAPIService.GetTokenByUser(userID, password);
		return token;
	}

	public String changePassword(@RequestParameter("userID")String userID,@RequestParameter("token")String token,@RequestParameter("oldPassword")String oldPassword,@RequestParameter("newPassword")String newPassword) {
		String newToken = paymentAPIService.ChangePasswordRequest(userID, token, oldPassword, newPassword);
		return newToken;
	}
	
	public BillDTOForBankPayment getBill(@RequestParameter("billID") long billID,@RequestParameter("bankCode")String bankCode) throws Exception{
		BillDTOForBankPayment billDTOForBankPayment = paymentAPIService.getBillForBankPayment(billID);
		if(!StringUtils.isEqual(bankCode, billDTOForBankPayment.getBankCode())){
			billDTOForBankPayment.setBankCode(null);
			billDTOForBankPayment.setBranchCode(null);
		}
		return billDTOForBankPayment;
	}

	
	public PaidBillDetailsDTO getPaidBills(HttpServletRequest request,@RequestParameter(value = "fromDate",required = false) Date fromDate,@RequestParameter(value = "toDate", required = false) Date toDate){
		Map<String,String> criteriaMap = getParameterMap(request);
		if(fromDate!=null){
			criteriaMap.remove("fromDate");
			criteriaMap.put("fromDate", ""+fromDate.getTime());
		}
		if(toDate!=null){
			criteriaMap.remove("toDate");
			criteriaMap.put("toDate", ""+toDate.getTime());
		}
		
		List<BillDTOForBankPayment> billDTOs = paymentAPIService.getPaidBills(criteriaMap);
		long total = 0;
		
		for(BillDTOForBankPayment billDTOForBankPayment: billDTOs){
			total+= billDTOForBankPayment.getNetPayableCeiled();
		}
		
		PaidBillDetailsDTO paidBillDetailsDTO = new PaidBillDetailsDTO();
		
		paidBillDetailsDTO.setBillDTOForBankPayment(billDTOs);
		paidBillDetailsDTO.setTotal(total);
		
		return paidBillDetailsDTO;
	}
	
	public Map<String,String> getParameterMap(HttpServletRequest request){
		Map<String,String> resultantMap = new HashMap<>();
		
		Enumeration<String> parameteKeys = request.getParameterNames();
		while(parameteKeys.hasMoreElements()){
			String parameterKey = parameteKeys.nextElement();
			String parameterValue = request.getParameter(parameterKey);
			resultantMap.put(parameterKey, parameterValue);
		}
		String token = request.getParameter("token");
		String bankCode = PaymentAPITokenRepository.getInstance().getBankCode(token);
		resultantMap.put("bankCode", bankCode);
		return resultantMap;
	}
	
	// not done
	public Reconciliation getReconsulation(HttpServletRequest request,@RequestParameter("fromDate") Date fromDate, @RequestParameter("toDate") Date toDate){
		Map<String,String> criteriaMap = getParameterMap(request);
		if(fromDate!=null){
			criteriaMap.remove("fromDate");
			criteriaMap.put("fromDate", ""+fromDate.getTime());
		}
		if(toDate!=null){
			criteriaMap.remove("toDate");
			criteriaMap.put("toDate", ""+toDate.getTime());
		}
		return paymentAPIService.getReconsutationAmount(criteriaMap);
	}
	
	public BillDTOForBankPayment payBill(@RequestParameter("billToken") long billToken,@RequestParameter("billID") long billID,@RequestParameter("netPayable")long netPayable,
			@RequestParameter("bankCode") String bankCode,@RequestParameter("branchCode")String branchCode) throws Exception{
		paymentAPIService.payBill(billID,billToken, netPayable, bankCode, branchCode);
		return getBill(billID, bankCode);
	}
	
	public void cancelBillPayment(@RequestParameter("billID") long billID,@RequestParameter("bankCode") String bankCode,@RequestParameter("branchCode") String branchCode){
		paymentAPIService.cancelBillPayment(billID, bankCode, branchCode);
	}
	/*
	public BankTransactionHistory getTransactionHistory(HttpServletResponse response,@RequestParameter("txID") long txID,@RequestParameter("bankCode") String bankCode){
		BankTransactionHistory bankTransactionHistory = paymentAPIService.getTransactionHistoryByID(txID, bankCode);
		return bankTransactionHistory;
	}*/
	
}
