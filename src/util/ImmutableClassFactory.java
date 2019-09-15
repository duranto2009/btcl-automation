package util;

import accounting.AccountingEntry;
import accounting.AccountingIncident;
import annotation.ColumnName;
import application.Application;
import client.classification.*;
import client.module.ClientModuleSubscriptionDTO;
import client.temporaryClient.TemporaryClient;
import clientdocument.ClientDocumentTypeDTO;
import coLocation.accounts.VariableCost.VariableCostDTO;
import coLocation.accounts.commonCost.CommonCostDTO;
import coLocation.accounts.commonCost.CommonCostTemplateDTO;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.connection.CoLocationConnectionDTO;
import coLocation.demandNote.CoLocationDemandNote;
import coLocation.demandNote.CoLocationDemandNoteAdjustment;
import coLocation.ifr.CoLocationApplicationIFRDTO;
import coLocation.inventory.CoLocationInventoryDTO;
import coLocation.inventory.CoLocationInventoryInUseDTO;
import coLocation.inventory.CoLocationInventoryTemplateDTO;
import coLocation.td.CoLocationProbableTDDTO;
import common.ClientDTO;
import common.ClientDTOConditionBuilder;
import common.StringUtils;
import common.bill.BillDTO;
import common.bill.MultipleBillMappingDTO;
import common.client.Client;
import common.client.ClientContactDetails;
import common.client.ClientDetails;
import costConfig.CategoryDTO;
import entity.comment.Comment;
import entity.localloop.LocalLoop;
import entity.localloop.LocalLoopConsumerMap;
import entity.office.Office;
import file.FileDTO;
import file.upload.FileForm;
import file.upload.FileVsState;
import flow.entity.*;
import inventory.InventoryAllocationHistory;
import inventory.InventoryAttributeValue;
import inventory.InventoryItem;
import ip.IPInventory.IPBlockInventory;
import ip.ipRegion.IPRegion;
import ip.ipRegion.IPRegionToDistrict;
import ip.ipUsage.IPBlockUsage;
import ip.ipVsLLIConnection.IPvsConnection;
import ip.ipVsNIXConnection.NIXIPvsConnection;
import javassist.*;
import lli.Application.AdditionalIP.LLIAdditionalIP;
import lli.Application.AdditionalPort.AdditionalPort;
import lli.Application.ChangeBillingAddress.LLINewBillingAddressApplication;
import lli.Application.EFR.EFR;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.IFR.IFR;
import lli.Application.LLIApplication;
import lli.Application.NewLocalLoop.NewLocalLoop;
import lli.Application.ReviseClient.ReviseDTO;
import lli.Application.newOffice.NewOffice;
import lli.Application.ownership.LLIOnProcessConnection;
import lli.Application.ownership.LLIOwnerShipChangeApplication;
import lli.Comments.Comments;
import lli.Comments.RevisedComment;
import lli.*;
import lli.asn.ASN;
import lli.asn.ASNApplication;
import lli.asn.ASNmapToIP;
import lli.client.td.LLIClientTD;
import lli.client.td.LLIProbableTDClient;
import lli.configuration.LLIFixedCostConfigurationDTO;
import lli.configuration.ofc.cost.OfcInstallationCostDTO;
import lli.demandNote.adjustment.LLIDemandNoteAdjustment;
import lli.longTerm.LLILongTermBenefit;
import lli.monthlyBill.ItemForManualBill;
import lli.monthlyBill.LLIMonthlyBillByClient;
import lli.monthlyBill.LLIMonthlyBillByConnection;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryByClient;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryByItem;
import lli.monthlyUsage.LLIMonthlyUsageByClient;
import lli.monthlyUsage.LLIMonthlyUsageByConnection;
import lli.outsourceBill.LLIMonthlyOutsourceBill;
import lli.outsourceBill.LLIMonthlyOutsourceBillByConnection;
import location.Area;
import location.District;
import location.Division;
import location.Zone;
import nix.application.NIXApplication;
import nix.application.close.NIXCloseApplication;
import nix.application.downgrade.NIXDowngradeApplication;
import nix.application.localloop.NIXApplicationLocalLoop;
import nix.application.office.NIXApplicationOffice;
import nix.application.upgrade.NIXUpgradeApplication;
import nix.connection.NIXConnection;
import nix.demandnote.NIXDemandNoteAdjustment;
import nix.efr.NIXEFR;
import nix.ifr.NIXIFR;
import nix.localloop.NIXLocalLoop;
import nix.monthlybill.NIXMonthlyBillByClient;
import nix.monthlybill.NIXMonthlyBillByConnection;
import nix.monthlybillsummary.NIXMonthlyBillSummaryByClient;
import nix.monthlybillsummary.NIXMonthlyBillSummaryByItem;
import nix.monthlyusage.NIXMonthlyUsageByClient;
import nix.monthlyusage.NIXMonthlyUsageByConnection;
import nix.nixportconfig.NIXPortConfig;
import nix.office.NIXOffice;
import nix.outsourcebill.NIXMonthlyOutsourceBill;
import nix.outsourcebill.NIXMonthlyOutsourceBillByConnection;
import nix.revise.NIXClientTD;
import nix.revise.NIXProbableTDClient;
import nix.revise.NIXReviseDTO;
import notification.NotificationDTO;
import upstream.application.UpstreamApplication;
import upstream.circuitInfo.CircuitInformationDTO;
import upstream.contract.UpstreamContract;
import upstream.inventory.UpstreamInventoryItem;
import upstream.vendor.UpstreamVendor;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationLink;
import vpn.clientContactDetails.ClientContactDetailsDTO;
import vpn.demandNote.VPNDemandNoteAdjustment;
import vpn.demandNote.VPNLoopChargeDiscountEligibility;
import vpn.monthlyBill.VPNMonthlyBillByClient;
import vpn.monthlyBill.VPNMonthlyBillByLink;
import vpn.monthlyBillSummary.VPNMonthlyBillSummaryByClient;
import vpn.monthlyBillSummary.VPNMonthlyBillSummaryByItem;
import vpn.monthlyOutsourceBill.VPNMonthlyOutsourceBill;
import vpn.monthlyOutsourceBill.VPNMonthlyOutsourceBillByLink;
import vpn.monthlyUsage.VPNMonthlyUsageByClient;
import vpn.monthlyUsage.VPNMonthlyUsageByLink;
import vpn.network.VPNNetwork;
import vpn.network.VPNNetworkLink;
import vpn.ownerchange.VPNOnProcessLink;
import vpn.td.VPNClientTD;
import vpn.td.VPNProbableTD;
import vpn.td.VPNProbableTDClient;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ImmutableClassFactory {
	
	private static Map<Class, Class> immutableClassMap = new ConcurrentHashMap<>();
	private static Set<Class> allowedParameterClass = new HashSet<Class>(){{
		add(Integer.class);
		add(Integer.TYPE);
		add(Double.class);
		add(Double.TYPE);
		add(String.class);
		add(Long.class);
		add(Long.TYPE);
		add(Boolean.class);
		add(Boolean.TYPE);
		add(Character.class);
		add(Character.TYPE);
	}};
	
	
	synchronized public static<T>  Class<? extends T> getImmutableClass(Class<T> classObject){
		if(!immutableClassMap.containsKey(classObject)){
			try{
				immutableClassMap.put(classObject, createImmutableProxyClass(classObject));
			}catch(Exception ex){
				throw new RuntimeException(ex);
			}
		}
		return immutableClassMap.get(classObject);
	}
	

	

	private static synchronized Class createImmutableProxyClass(Class classObject) throws Exception{
		ClassPool classPool = ClassPool.getDefault();
		classPool.insertClassPath(new ClassClassPath(classObject));
		CtClass ctClass = classPool.makeClass(classObject.getCanonicalName()+"Proxy");
		ctClass.setSuperclass(classPool.get(classObject.getCanonicalName()));
		
		for(Method method: classObject.getDeclaredMethods()){
			if(method.getReturnType().equals(void.class) 
					&& method.getName().startsWith("set")
					&& method.getParameterTypes().length == 1
					&& allowedParameterClass.contains(method.getGenericParameterTypes()[0])){
				
				String parameterTypeName = method.getGenericParameterTypes()[0].getTypeName();
				if(parameterTypeName.contains(".")){
					int lastIndex = parameterTypeName.lastIndexOf(".")+1;
					parameterTypeName = parameterTypeName.substring(lastIndex);
				}
				CtMethod ctMethod = CtNewMethod.make("public void "+method.getName()+"("+parameterTypeName+" tmp ){throw new RuntimeException(\"No modification allowed on a object returned by repository\");}", ctClass);
				ctClass.addMethod(ctMethod);
			}
		}
		Class newClass = ctClass.toClass();
		//ctClass.writeFile("WebContent/WEB-INF/classes/");
		return newClass;
	}
	public static String createColumnNameGetterMethodString(String propertyName) throws Exception{
		String methodString = "public String get"+propertyName+"(){"
								+"return util.SqlGenerator.getColumnName("+propertyName+",classObject);"
								+"}";
		
		return methodString;
	}

	public static void createConditionBuilderClass(Class<?> classObject) throws Exception{

		final String classFileLocation = "WebContent/WEB-INF/classes/";
		ClassPool classPool = ClassPool.getDefault();
		classPool.insertClassPath(new ClassClassPath(classObject));
		String newClassName = classObject.getCanonicalName()+"ConditionBuilder";
		CtClass ctClass = classPool.makeClass(newClassName);
		File file = new File(classFileLocation+newClassName.replaceAll("\\.", "/")+".class");
		if(file.exists()){
			boolean deleted = file.delete();
			System.out.println("Previous file " + file.getName()+ "deletion : "+deleted);
		}
		ctClass.addField(new CtField(ClassPool.getDefault().get("java.lang.StringBuilder"), "conditionBuilder", ctClass));
		ctClass.addField(new CtField(ClassPool.getDefault().get("java.lang.Class"), "classObject", ctClass));
		ctClass.addField(new CtField(CtPrimitiveType.booleanType, "isConditionAdded",ctClass));
		ctClass.addField(new CtField(CtPrimitiveType.booleanType, "isWhereAdded",ctClass));
		ctClass.addField(new CtField(CtPrimitiveType.booleanType, "isOrderByAdded",ctClass));
		ctClass.addField(new CtField(CtPrimitiveType.booleanType, "isSelectAdded",ctClass));
		ctClass.addField(new CtField(ClassPool.getDefault().get("java.util.List"),"objectList",ctClass));
		List<Field> annotatedColumnFields = getAnnotatedFields(classObject);
		ctClass.addMethod(createCheckMethod(annotatedColumnFields, ctClass, classObject));
		ctClass.addConstructor(createConstructor(ctClass, classObject));
		List<CtMethod> columnNameGetterMethods = createColumnNameGetterMethods(annotatedColumnFields
				, ctClass, classObject);
		for(CtMethod ctMethod: columnNameGetterMethods){
			ctClass.addMethod(ctMethod);
		}
		List<CtMethod> conditionBuilderMethods = createConditionBuilderMethods(annotatedColumnFields, ctClass);
		conditionBuilderMethods.add(createOrMethod(ctClass));
		conditionBuilderMethods.add(createBracketStartMethod(ctClass));
		conditionBuilderMethods.add(createBracketEndMethod(ctClass));
		for(CtMethod ctMethod: conditionBuilderMethods){
			ctClass.addMethod(ctMethod);
		}
		String lastMethod = "public java.lang.Object[] getCondition(){java.util.List ret = new java.util.ArrayList();ret.add(conditionBuilder.toString());ret.addAll(objectList);return ret.toArray();}";
		ctClass.addMethod(CtNewMethod.make(lastMethod, ctClass));
		ctClass.addMethod(createWhereMethod(ctClass));
		ctClass.addMethod(createGetSqlPairMethod(ctClass));
		ctClass.addMethod(createGetNullableSqlPairMethod(ctClass));
		ctClass.addMethod(createFromMethod(classObject, ctClass));
		String limitMethod = "public "+ctClass.getName()+" limit(int param){conditionBuilder.append(\" limit \"+param+\" \");return this;}";
		ctClass.addMethod(CtMethod.make(limitMethod, ctClass));
		//Class newClass = ctClass.toClass();
		ctClass.writeFile(classFileLocation);
	}

	private static CtMethod createBracketStartMethod(CtClass ctClass) throws Exception {
		String bracketStartMethod = "public "+ctClass.getName()+" bracketStart(){"

				+"conditionBuilder.append(\" ( \");"
				+"return this;}";

		return CtNewMethod.make(bracketStartMethod, ctClass);
	}


	private static CtMethod createBracketEndMethod(CtClass ctClass) throws Exception {
		String bracketStartMethod = "public "+ctClass.getName()+" bracketEnd(){"

				+"conditionBuilder.append(\" ) \");"
				+"return this;}";

		return CtNewMethod.make(bracketStartMethod, ctClass);
	}
	private static List<Field> getAnnotatedFields(Class<?> classObject) throws Exception{
		List<Field> annotatedColumnFields = new ArrayList<>();
		for(Field field: classObject.getDeclaredFields()){
			if(SqlGenerator.hasAnnotation(classObject, field.getName(), ColumnName.class)){
				annotatedColumnFields.add(field);
			}
		}
		return annotatedColumnFields;
	}
	
	
	private static CtConstructor createConstructor(CtClass ctClass,Class<?> classObject) throws Exception{
		String constructBodyString = "public "+classObject.getSimpleName() +"ConditionBuilder(){\n"
									+ "		conditionBuilder = new java.lang.StringBuilder(\"    \");\n"
									+ "		classObject = "+classObject.getCanonicalName()+".class;\n"
									+ "		objectList = new java.util.ArrayList();"
									+ "		isWhereAdded = false;\n"
									+ "		isSelectAdded = false;\n"
									+ "		check();\n"
									+ " }";

		return CtNewConstructor.make(constructBodyString, ctClass);
	}
	
	private static CtMethod createCheckMethod(List<Field> annotatedColumnFields,CtClass ctClass,Class classObject) 
			throws Exception{

		String compileTimeCheckMethod =  "public void check(){\n"
										+"	"+classObject.getCanonicalName()+" "+classObject.getSimpleName().toLowerCase()+" = new "+classObject.getCanonicalName()+"();\n";
		
		for(Field annotatedColumnField: annotatedColumnFields){
			String propertyName = annotatedColumnField.getName();
			compileTimeCheckMethod+="	"+classObject.getSimpleName().toLowerCase()
										+"."+propertyName+" = "+classObject.getSimpleName().toLowerCase()
										+"."+propertyName+";\n";
		}
		
		compileTimeCheckMethod+="}";
		return CtNewMethod.make(compileTimeCheckMethod, ctClass);
		
	}
	
	
	public void createOrderByConditionBuilder(Class<?> classObject) throws Exception{

		final String classFileLocation = "WebContent/WEB-INF/classes/"; 
		
		ClassPool classPool = ClassPool.getDefault();
		classPool.insertClassPath(new ClassClassPath(classObject));
		String newClassName = classObject.getCanonicalName()+"OrderConditionBuilder";
		CtClass ctClass = classPool.makeClass(newClassName);
		
		
		File file = new File(classFileLocation+newClassName.replaceAll("\\.", "/")+".class");
		if(file.exists()){
			boolean deleted = file.delete();
			System.out.println("Previous file deletion : "+deleted);
		}
		
		ctClass.addField(new CtField(ClassPool.getDefault().get("java.lang.StringBuilder"), "conditionBuilder", ctClass));
		ctClass.addField(new CtField(ClassPool.getDefault().get("java.lang.Class"), "classObject", ctClass));
		
		
	}
	
	
	private static List<CtMethod> createColumnNameGetterMethods(List<Field> annotatedColumnFields
			,CtClass ctClass,Class<?> classObject) throws Exception{
		
		List<CtMethod> columnNameGetterMethods = new ArrayList<>();
		
		for(Field annotatedColumnField: annotatedColumnFields){
			String propertyName = annotatedColumnField.getName();
			
			String parameterTypeName = annotatedColumnField.getType().getName();
			
			if(parameterTypeName.contains(".")){
				int lastIndex = parameterTypeName.lastIndexOf(".")+1;
				parameterTypeName = parameterTypeName.substring(lastIndex);
			}
			String capitalizedPropertyName = (""+propertyName.charAt(0)).toUpperCase()+propertyName.substring(1);
			String columnNameGetterMethodName =  "public String get"+capitalizedPropertyName+"ColumnName() throws Exception{\n"
												+"		return util.ModifiedSqlGenerator.getColumnName("+classObject.getCanonicalName()
												+".class,\""+propertyName+"\");\n"
												+"}";
			CtMethod ctMethod = CtNewMethod.make(columnNameGetterMethodName, ctClass); 
			
			columnNameGetterMethods.add(ctMethod);
		}
		
		
		return columnNameGetterMethods;
	}
	
	
	private static List<CtMethod> createConditionBuilderMethods(List<Field> annotatedFieldList
			,CtClass ctClass) throws Exception{

		
		List<CtMethod> conditionBuilderMethods = new ArrayList<>();
		
		for(Field annotatedField: annotatedFieldList){
			conditionBuilderMethods.addAll(createConditionBuilderMethods(annotatedField,ctClass));
		}
		
		
		return conditionBuilderMethods;
	}
	
	private static CtMethod createGetSqlPairMethod(CtClass ctClass) throws Exception{
		String getSqlPairMethodString = "public util.SqlPair getSqlPair() {"
				+ "util.SqlPair sqlPair = new util.SqlPair();"
				+ "sqlPair.sql = conditionBuilder.toString();sqlPair.objectList = objectList;"
				+ "return sqlPair;}";
		return CtNewMethod.make(getSqlPairMethodString, ctClass);
	}
	
	private static CtMethod createGetNullableSqlPairMethod(CtClass ctClass) throws Exception{
		String nullableSqlPairMethodString = "public util.SqlPair getNullableSqlPair(){"
				+ "if(!isConditionAdded){"
				+ "	return null;"
				+ "}"
				+ " return getSqlPair();}";
		return CtNewMethod.make(nullableSqlPairMethodString, ctClass);
	}
	
	private static CtMethod createWhereMethod(CtClass ctClass) throws Exception{
		String whereMethodString = "public "+ctClass.getName()+" Where(){isWhereAdded = true;return this;}";
		return CtNewMethod.make(whereMethodString,ctClass);
	}
	
	private static CtMethod createOrMethod(CtClass ctClass) throws Exception{
		String orMethod = "public "+ctClass.getName()+" Or(util.SqlPair sqlPair){"
							+"if(sqlPair == null){return this;}"
							+"conditionBuilder.append(\" OR(\").append(sqlPair.sql).append(\")\");"
							+"for(int i =0;i<sqlPair.objectList.size();i++){objectList.add(sqlPair.objectList.get(i));}"
				+"return this;}";
		
		return CtNewMethod.make(orMethod, ctClass);
	}
	
	private static CtMethod createFromMethod(Class classObject,CtClass ctClass) throws Exception{
		String fromMethodString = " public "+ctClass.getName()+" from"+StringUtils.getCamelCase(classObject.getSimpleName())+"(){"
				+ "conditionBuilder.append(\" from \")"
				+ ".append(util.ModifiedSqlGenerator.getTableName(classObject));"
				+ "return this;"
				+ "}";
		return CtNewMethod.make(fromMethodString, ctClass);
	}
	
	
	private static List<CtMethod> createConditionBuilderMethods(Field annotatedColumnField,CtClass ctClass) 
			throws Exception{
		List<CtMethod> ctMethods = new ArrayList<>();
		
		
		
		String selectMethodString = "public "+ctClass.getName()+" select"+StringUtils.getCamelCase(annotatedColumnField.getName())+"(){"
				+"if(!isSelectAdded){conditionBuilder.append(\" Select \");}"
				+"else {conditionBuilder.append(\" , \");}"
				+ "isSelectAdded = true;"
				+"conditionBuilder.append(util.ModifiedSqlGenerator.getColumnName(classObject,\""+annotatedColumnField.getName()+"\"));"
																+ "return this;}";
		
		
		ctMethods.add(CtNewMethod.make(selectMethodString, ctClass));
		
		String orderByMethodString = "public "+ctClass.getName()+" orderBy"+annotatedColumnField.getName()+"Asc(){"
										+"if(!isOrderByAdded){conditionBuilder.append(\" order by \");}"
										+"else{conditionBuilder.append(\",\");}"
										+"conditionBuilder.append(util.ModifiedSqlGenerator.getColumnName(classObject,\""+annotatedColumnField.getName()+"\"))"
										+".append(\" asc \");"
										+"isOrderByAdded = true;"
										+"return this;}";
		
		
		ctMethods.add(CtNewMethod.make(orderByMethodString, ctClass));
		
		
		orderByMethodString = "public "+ctClass.getName()+" orderBy"+annotatedColumnField.getName()+"Desc(){"
				+"if(!isOrderByAdded){conditionBuilder.append(\" order by \");}"
				+"else{conditionBuilder.append(\",\");}"
				+"conditionBuilder.append(util.ModifiedSqlGenerator.getColumnName(classObject,\""+annotatedColumnField.getName()+"\"))"
				+".append(\" desc \");"
				+"isOrderByAdded = true;"
				+"return this;}";


		ctMethods.add(CtNewMethod.make(orderByMethodString, ctClass));
		
		String[] methodNames = {"Equals","GreaterThan","GreaterThanEquals","LessThan","LessThanEquals", "NotEquals"};
		String[] operators   = {"="		,">"		  ,">="				  ,"<"		 ,"<="            , "<>"};
		
		
		
		
		Map<Class,String> mapOfClassToClassName = new HashMap<>();
		
		mapOfClassToClassName.put(Integer.class, "Integer");
		mapOfClassToClassName.put(Integer.TYPE, "Integer");
		mapOfClassToClassName.put(Long.class, "Long");
		mapOfClassToClassName.put(Long.TYPE, "Long");
		mapOfClassToClassName.put(Boolean.class, "Boolean");
		mapOfClassToClassName.put(Boolean.TYPE, "Boolean");
		mapOfClassToClassName.put(Double.class, "Double");
		mapOfClassToClassName.put(Double.TYPE, "Double");
		mapOfClassToClassName.put(String.class, "String");
		mapOfClassToClassName.put(Enum.class, "Enum");
		
		
		
		String methodBodyTemplate = " if(param == null){return this;}"
				+"if(!isConditionAdded && isWhereAdded){\n"
				+"	conditionBuilder.append(\" where \");\n"
				+"}else if(isConditionAdded){\n"
				+"	conditionBuilder.append(\" and \");\n"
				+"}\n"
				+"isConditionAdded = true;\n"
				+"conditionBuilder.append(util.ModifiedSqlGenerator.getColumnName(classObject,\""+annotatedColumnField.getName()+"\"))"
				+".append(\" %s ?\");"
				+" objectList.add(%s);"
				+"return this;";

		String methodBodyTemplateWithStartingBracket = " if(param == null){return this;}"
				+"if(!isConditionAdded && isWhereAdded){\n"
				+"	conditionBuilder.append(\"where ( \");\n"
				+"}else if(isConditionAdded){\n"
				+"	conditionBuilder.append(\" and (\");\n"
				+"}\n"
				+"isConditionAdded = true;\n"
				+"conditionBuilder.append(util.ModifiedSqlGenerator.getColumnName(classObject,\""+annotatedColumnField.getName()+"\"))"
				+".append(\" %s ?\");"
				+" objectList.add(%s);"
				+"return this;";
		
		
		
		Class<?> classOfPropertyTYpe = annotatedColumnField.getType();
		boolean nonComparable = classOfPropertyTYpe.equals(Boolean.class) ||
									classOfPropertyTYpe.equals(Boolean.TYPE) ||
										classOfPropertyTYpe.isEnum();
		if(!nonComparable){
			
			for(int i = 0;i<methodNames.length;i++){
				String methodName = methodNames[i];
				String operator = operators[i];
				String methodString =   "public "+ctClass.getName()+" "+annotatedColumnField.getName()+methodName+"("
						+mapOfClassToClassName.get(classOfPropertyTYpe)+" param) throws Exception{ "
									
									+String.format(methodBodyTemplate,operator,"param" )
									+"}";
				String methodStringWithStartingBracket =   "public "+ctClass.getName()+" "+annotatedColumnField.getName()+methodName+"WithStartingBracket("
						+mapOfClassToClassName.get(classOfPropertyTYpe)+" param) throws Exception{ "

						+String.format(methodBodyTemplateWithStartingBracket,operator,"param" )
						+"}";
			
				ctMethods.add(CtNewMethod.make(methodString, ctClass));
				ctMethods.add(CtNewMethod.make(methodStringWithStartingBracket, ctClass));
				
				String stringParamMethodString = "public "+ctClass.getName()+" "+annotatedColumnField.getName()+methodName+"String(java.lang.String param) throws Exception{ if(param!=null && param.trim().length()==0){return this;}"
						
						+String.format(methodBodyTemplate,operator,"param" )
						+"}"; 
				
				ctMethods.add(CtNewMethod.make(stringParamMethodString, ctClass));
				
				String sqlPairMethodString = "public "+ctClass.getName()+" "+annotatedColumnField.getName()+methodName+"SqlPair(util.SqlPair param) throws Exception{ \n"
						+" if(param == null){return this;}"
						+"if(!isConditionAdded && isWhereAdded){\n"
						+"	conditionBuilder.append(\" where \");\n"
						+"}else if(isConditionAdded){\n"
						+"	conditionBuilder.append(\" and \");\n"
						+"}\n"
						+"isConditionAdded = true;\n"
						+"conditionBuilder.append(util.ModifiedSqlGenerator.getColumnName(classObject,\""+  annotatedColumnField.getName()+  "\"));"
						+"conditionBuilder.append(\""+operator+"\");"
						+"conditionBuilder.append(\"(\");"
						+"conditionBuilder.append(param.sql);"
						+"conditionBuilder.append(\")\");"
						+"for(int i =0;i<param.objectList.size();i++){\n"
						+ "objectList.add(param.objectList.get(i));\n"
						+ "}\n"
						+"return this;}\n";
				ctMethods.add(CtNewMethod.make(sqlPairMethodString, ctClass));
			
			}
			
		}
		
		String likeMethodNames[] = {"LeftLike","RightLike","BothLike","Like"};
		String leftPaddings[]    = {""		 ,"%"		 ,"%"		,""};
		String rightPaddings[]   = {"%"		 ,""		 ,"%"		,""};
		
		for(int i = 0;i<likeMethodNames.length;i++){
			String likeMethodName = likeMethodNames[i];
			String leftPadding = leftPaddings[i];
			String rightPadding = rightPaddings[i];
			

			String likeMethodString = "public "+ctClass.getName()+" "+annotatedColumnField.getName()+likeMethodName+"(String param) {"
					+" if(param == null||param.trim().length()==0){return this;}"
					+"if(!isConditionAdded && isWhereAdded){\n"
					+"	conditionBuilder.append(\" where \");\n"
					+"}else if(isConditionAdded){\n"
					+"	conditionBuilder.append(\" and \");\n"
					+"}\n"
					+"isConditionAdded = true;\n"
					+"conditionBuilder.append(util.ModifiedSqlGenerator.getColumnName(classObject,\""+annotatedColumnField.getName()+"\"))"
					+".append(\" LIKE ?\");"
					+" objectList.add(\""+ leftPadding+"\"+param+\""+rightPadding +"\");"
					+"return this;}";
			
			ctMethods.add(CtNewMethod.make(likeMethodString, ctClass));
			
			
			
			
			
			
			String sqlPairMethodString = "public "+ctClass.getName()+" "+annotatedColumnField.getName()+likeMethodName+"SqlPair(util.SqlPair param) throws Exception{ \n"
					+" if(param == null){return this;}"
					+"if(!isConditionAdded && isWhereAdded){\n"
					+"	conditionBuilder.append(\" where \");\n"
					+"}else if(isConditionAdded){\n"
					+"	conditionBuilder.append(\" and \");\n"
					+"}\n"
					+"isConditionAdded = true;\n"
					+"conditionBuilder.append(util.ModifiedSqlGenerator.getColumnName(classObject,\""+  annotatedColumnField.getName()+  "\"));"
					+"conditionBuilder.append(\" LIKE \");"
					+"conditionBuilder.append(\"(\");"
					+"conditionBuilder.append(param.sql);"
					+"conditionBuilder.append(\")\");"
					+"for(int i =0;i<param.objectList.size();i++){\n"
					+ "objectList.add(param.objectList.get(i));\n"
					+ "}\n"
					+"return this;}\n";
			ctMethods.add(CtNewMethod.make(sqlPairMethodString, ctClass));
		}
		
		
		
		if(classOfPropertyTYpe.equals(Boolean.class) || classOfPropertyTYpe.equals(Boolean.TYPE)){
			String methodString =   "public "+ctClass.getName()+" "+annotatedColumnField.getName()+"("+mapOfClassToClassName.get(classOfPropertyTYpe)+" param) throws Exception{ "
									+String.format(methodBodyTemplate, " = ","new java.lang.Integer( Boolean.TRUE.equals(param)?1:0 )")
									+"}";
			
				ctMethods.add(CtNewMethod.make(methodString, ctClass));
		}
		
		String nullCheckMethodString = "public "+ctClass.getName()+" "+annotatedColumnField.getName()+"IsNull(Boolean param) throws Exception{ "
										+String.format(methodBodyTemplate, " IS "," Boolean.TRUE.equals(param)? null : \"NOT\"+ null ")
										+"}";
		
		//System.out.println(nullCheckMethodString);
		

		ctMethods.add(CtNewMethod.make(nullCheckMethodString, ctClass));
		
		
		String inQueryMethodString = "public "+ctClass.getName()+" "+annotatedColumnField.getName()+"In(java.util.List params){ "
				+"if(params!=null && !params.isEmpty()){"
				+"if(!isConditionAdded && isWhereAdded){\n"
				+"	conditionBuilder.append(\" where \");\n"
				+"}else if(isConditionAdded){\n"
				+"	conditionBuilder.append(\" and \");\n"
				+"}\n"
				+"isConditionAdded = true;\n"
				+"String columnName = util.ModifiedSqlGenerator.getColumnName(classObject,\""+annotatedColumnField.getName()+"\");"
				+"conditionBuilder.append(columnName).append(\" \").append(\" IN (\");"
				+"for(int i=0;i<params.size();i++){"
				+"if(i!=0){conditionBuilder.append(\",\");}"
				+"conditionBuilder.append(\"?\");"
				+"objectList.add(params.get(i));}"
				+"conditionBuilder.append(\")\");"
				+"}"
				+"return this;}";
		
		ctMethods.add(CtNewMethod.make(inQueryMethodString, ctClass));
		
		String sqlPairMethodString = "public "+ctClass.getName()+" "+annotatedColumnField.getName()+"InSqlPair(util.SqlPair param) throws Exception{ \n"
				+" if(param == null){return this;}"
				+"if(!isConditionAdded && isWhereAdded){\n"
				+"	conditionBuilder.append(\" where \");\n"
				+"}else if(isConditionAdded){\n"
				+"	conditionBuilder.append(\" and \");\n"
				+"}\n"
				+"isConditionAdded = true;\n"
				+"conditionBuilder.append(util.ModifiedSqlGenerator.getColumnName(classObject,\""+  annotatedColumnField.getName()+  "\"));"
				+"conditionBuilder.append(\" IN \");"
				+"conditionBuilder.append(\"(\");"
				+"conditionBuilder.append(param.sql);"
				+"conditionBuilder.append(\")\");"
				+"for(int i =0;i<param.objectList.size();i++){\n"
				+ "objectList.add(param.objectList.get(i));\n"
				+ "}\n"
				+"return this;}\n";
		ctMethods.add(CtNewMethod.make(sqlPairMethodString, ctClass));
		
		return ctMethods;
	}
	
	
	private String createMethodStringByMethodNameByMethodNameANdParameterTypeAndOperator(String methodName
			,String parameterType,String parameterName,String operator) throws Exception{
		String methodString = "public void "+methodName+"( "+parameterType+" "+parameterName+"){"
				
				
				+ "}";
		
		
		return methodString;
	}

	public static void main(String[] atgs) throws Exception{
		
		Class<?>[] classList = {

				TemporaryClient.class,


				//doc upload

				FileVsState.class,


				//region upstream
				FileForm.class,
				CircuitInformationDTO.class,
				UpstreamContract.class,
				UpstreamVendor.class,
				UpstreamApplication.class,
				UpstreamInventoryItem.class,
				//endregion
				//VPN space
				VPNMonthlyOutsourceBillByLink.class,
				VPNMonthlyOutsourceBill.class,
				VPNProbableTDClient.class,
				VPNDemandNoteAdjustment.class,
				VPNMonthlyBillSummaryByClient.class,
				VPNMonthlyBillSummaryByItem.class,
				VPNNetwork.class,
				VPNNetworkLink.class,
				VPNMonthlyBillByClient.class,
				VPNMonthlyBillByLink.class,
				VPNMonthlyUsageByClient.class,
				VPNMonthlyUsageByLink.class,
				// region colocation
				CoLocationInventoryTemplateDTO.class,
				CoLocationInventoryDTO.class,
				CoLocationInventoryInUseDTO.class,
				CoLocationApplicationDTO.class,
				CoLocationDemandNote.class,
				CommonCostTemplateDTO.class,
				CommonCostDTO.class,
				VariableCostDTO.class,
				CoLocationApplicationIFRDTO.class,
				CoLocationConnectionDTO.class,
				CoLocationProbableTDDTO.class,
				CoLocationDemandNoteAdjustment.class,

				//endregion

				IPBlockInventory.class,
				IPBlockUsage.class,
				IPvsConnection.class,

				RegistrantTypeDTO.class,
				RequiredDocsInACategoryDTO.class,
				ClientDocumentTypeDTO.class,
				RegistrantCategoriesInATypeDTO.class,
				ClientModuleSubscriptionDTO.class,
				RegistrantTypesInAModuleDTO.class,
				RegistrantCategoryDTO.class,
				ClientDTO.class,
				lli.Application.Office.Office.class,
				NewOffice.class,
				ReviseDTO.class,
				LLIConnectionInstance.class,
				LLIConnection.class,
				LLIOffice.class,
				lli.Application.LocalLoop.LocalLoop.class,
				NewLocalLoop.class,
				Comments.class,
				RevisedComment.class,
				LLILocalLoop.class,
				//NewLocalLoop.class,
				LLILongTermContract.class,
				LLIApplicationInstance.class,
				AccountingEntry.class,
				AccountingIncident.class,
				LLIApplication.class,
				LLIClientTD.class,
				LLIProbableTDClient.class,
				InventoryAllocationHistory.class,
				InventoryItem.class,
				LLIFixedCostConfigurationDTO.class,
				BillDTO.class,
				LLIMonthlyBillByClient.class,
				LLIMonthlyBillByConnection.class,
				LLIMonthlyUsageByClient.class,
				LLIMonthlyUsageByConnection.class,
				OfcInstallationCostDTO.class,
				CategoryDTO.class,
				District.class,
				Division.class,
				Zone.class,
				Area.class,
				IFR.class,

				Module.class,
				Component.class,
                Flow.class,
                FlowState.class,
                FlowStateTransition.class,
                FlowStateTransitionRole.class,
                Role.class,
				EFR.class,
				Client.class,
				ClientDetails.class,
				ClientContactDetails.class,
				ClientContactDetailsDTO.class,
				LLIMonthlyBillSummaryByItem.class,
				LLIMonthlyBillSummaryByClient.class,
				//InventoryItem.class,
				//LLIConnectionInstance.class,
				ItemForManualBill.class,
				IPRegion.class,
				IPRegionToDistrict.class,
				LLIDemandNoteAdjustment.class,
				LLINewBillingAddressApplication.class,
				LLIAdditionalIP.class,
				AdditionalPort.class,
				LLIMonthlyOutsourceBill.class,
				LLIMonthlyOutsourceBillByConnection.class,
				LLILongTermBenefit.class,
				ASN.class,
				ASNApplication.class,
				ASNmapToIP.class,
				//ownerchange
				LLIOwnerShipChangeApplication.class,
				LLIOnProcessConnection.class,
				//region nix
				NIXApplication.class,
				NIXApplicationOffice.class,
				NIXApplicationLocalLoop.class,
				NIXEFR.class,
				NIXIFR.class,
				InventoryAttributeValue.class,
				NIXConnection.class,
				NIXOffice.class,
				NIXLocalLoop.class,
				NIXUpgradeApplication.class,
				NIXDowngradeApplication.class,
				NIXPortConfig.class,
				NIXIPvsConnection.class,
				NIXMonthlyBillByConnection.class,
				NIXMonthlyBillByClient.class,
				NIXProbableTDClient.class,
				NIXClientTD.class,
				NIXReviseDTO.class,
				NIXMonthlyUsageByConnection.class,
				NIXMonthlyUsageByClient.class,
				NIXMonthlyBillSummaryByItem.class,
				NIXMonthlyBillSummaryByClient.class,
				NIXCloseApplication.class,
				NIXMonthlyOutsourceBill.class,
				NIXMonthlyOutsourceBillByConnection.class,

				NIXDemandNoteAdjustment.class,
				//endregion

				//region vpn
				Application.class,
				VPNApplication.class,
				VPNApplicationLink.class,
				entity.efr.EFR.class,
				Office.class,
				LocalLoop.class,
				LocalLoopConsumerMap.class,
				VPNClientTD.class,
				VPNProbableTD.class,
				VPNOnProcessLink.class,
				//endregion

				//region comment
				Comment.class,
				//endregion

				//region Notification
				NotificationDTO.class,


				//endregion
				MultipleBillMappingDTO.class,

				//client-file
				FileDTO.class,
				VPNLoopChargeDiscountEligibility.class,
				};
		
		
		for(Class<?> classObject: classList){
			createConditionBuilderClass(classObject);
		}




	}
	
}
