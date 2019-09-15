package report;

import java.lang.reflect.Field;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import annotation.ColumnName;
import common.RequestFailureException;
import common.StringUtils;



// output column format     		display.{ClassName}.{FieldName}=value
class DisplayColumn{
	String databaseColumnName;
	String caption;
	ColumnConvertor convertor = new DefaultConvertor();
	
	
	public static boolean isOutputColumn(String key){
		return key!=null && key.startsWith("display.");
	}
	public DisplayColumn(String key,String value,Map<String,Class> classMap){
		if(StringUtils.isBlank(key)){
			throw new RuntimeException("output key can not be empty string");
		}
		if(StringUtils.isBlank(value)){
			throw new RuntimeException("output value can not be empty string");
		}
		String[] tokens = key.split("\\.",-1);
		if(tokens.length!=3){
			throw new RuntimeException("the output column must have two parts");
		}
		
		String className = tokens[1];
		String fieldName = tokens[2];
		

		if(!classMap.containsKey(className)){
			throw new RuntimeException("The requested className is not allowed "+className);
		}
		Class classObject = classMap.get(className);
		
		try {
			Field field = classObject.getDeclaredField(fieldName);
			field.setAccessible(true);
			ColumnName columnName = (ColumnName)field.getDeclaredAnnotation(ColumnName.class);
			if(columnName==null){
				throw new RuntimeException("No column name annotation found on the specified field. key name "+key);
			}
			if(StringUtils.isBlank(value)){
				throw new RuntimeException("The output caption can not be empty: "+key);
			}
			
			this.caption = value;
			this.databaseColumnName = columnName.value();
			
			Display convert = (Display) field.getAnnotation(Display.class);
			if(convert != null){
				this.convertor = convert.value().newInstance();
			}
			
			
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
		
	}
}


// input column format   [criteria.{ClassName}.{FieldName}.{Operator}]
class CriteriaColumn{
	String databaseColumnName;
	String criteriaValue;
	String operator;
	Integer moduleID;
	public static boolean isInputColumn(String key){
		return key.startsWith("criteria.");
	}
	
	
	public static String convertToValidOperator(String oprtr){
		
		Map<String,String> map = new HashMap<String,String>(){{
			put("eq", "=");
			put("like","like");
			put("eq","=");
			put("geq",">=");
			put("leq","<=");
			put("g",">");
			put("l","<");
			put("null","null");
			put("neq","!=");
			put("in","in");
			
		}};
		if(map.containsKey(oprtr)){
			return map.get(oprtr);
		}
		throw new RequestFailureException("Invalid operator "+oprtr);
	}

	CriteriaColumn(String key,String value,Map<String,Class> classMap){
		if(!isInputColumn(key)){
			throw new RuntimeException(key+" is not a input column");
		}
		String[] tokens = key.split("\\.",-1);
		if(tokens.length!=4){
			throw new RuntimeException("Invalid criteria name format: "+key);
		}
		String className = tokens[1];
		
		if(!classMap.containsKey(className)){
			throw new RuntimeException("Invalid className. The requested class name is not"
					+ " allowed for this report : "+className);
		}
		Class classObject = classMap.get(className);
		
		String fieldName = tokens[2];
		String operator = tokens[3];
		try {
			
			Field field = classObject.getDeclaredField(fieldName);
			field.setAccessible(true);
			this.databaseColumnName = ((ColumnName)field.getAnnotation(ColumnName.class)).value();
			this.criteriaValue = value;
			
			if(StringUtils.isBlank(value)){
				throw new RuntimeException("invalid request for input column. Since no value is found");
			}
			
			if(StringUtils.isBlank(operator)){
				throw new RuntimeException("invalid request for input column. Since no operator is found");
			}
			
			operator = CriteriaColumn.convertToValidOperator(operator);
			
			if(operator.toLowerCase().equals("like")){
				this.criteriaValue = "%"+this.criteriaValue+"%";
			}
			
			ReportCriteria reportCriteria = (ReportCriteria)field.getAnnotation(ReportCriteria.class);
			if(reportCriteria!=null){
				try{
					this.moduleID = reportCriteria.moduleID();
					if(moduleID > 0)
						this.criteriaValue =  reportCriteria.value().newInstance().createSubquery(value,this.moduleID );
					else
						this.criteriaValue =  reportCriteria.value().newInstance().createSubquery(value);
				}catch(Exception ex){
					throw new RuntimeException(ex);
				}
			}
			
			
			this.operator = operator;
		}  catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
	}
}

public class ReportMetadata {
	private List<DisplayColumn> outputColumnNames = new ArrayList<>();
	private List<CriteriaColumn> inputColumnNames = new ArrayList<>();
	private List<String> orderByColumnNames = new ArrayList<>();
	
	
	public List<DisplayColumn> getOutputColumnNames() {
		return outputColumnNames;
	}


	public void setOutputColumnNames(List<DisplayColumn> outputColumnNames) {
		this.outputColumnNames = outputColumnNames;
	}


	public List<CriteriaColumn> getInputColumnNames() {
		return inputColumnNames;
	}


	public void setInputColumnNames(List<CriteriaColumn> inputColumnNames) {
		this.inputColumnNames = inputColumnNames;
	}


	public List<String> getOrderByColumnNames() {
		return orderByColumnNames;
	}


	public void setOrderByColumnNames(List<String> orderByColumnNames) {
		this.orderByColumnNames = orderByColumnNames;
	}


	public ReportMetadata(Map<String,Class> classMap,HttpServletRequest request){
		
		Enumeration<String> enumeration = request.getParameterNames();
		while(enumeration.hasMoreElements()){
			String key = enumeration.nextElement();
			if(CriteriaColumn.isInputColumn(key) && !StringUtils.isBlank(request.getParameter(key))){
				inputColumnNames.add(new CriteriaColumn(key, request.getParameter(key), classMap));
			}else if(DisplayColumn.isOutputColumn(key) && !StringUtils.isBlank(request.getParameter(key))){
				outputColumnNames.add(new DisplayColumn(key, request.getParameter(key),classMap));
			}
		}
		if(request.getParameterValues("orderByColumns")!=null){
			for(String orderByColumnName:request.getParameterValues("orderByColumns")){
				
				String[] keys = orderByColumnName.split("\\.");
				if(keys.length!=2){
					throw new RequestFailureException("Format for order by column is {className}.{fieldName}");
				}
				String classname = keys[0];
				String fieldName = keys[1];
				Class<?> classObject = classMap.get(classname);
				if(classObject==null){
					throw new RequestFailureException("Invalid class name : "+classname );
				}
				String databaseColumnName = null;
				
				try {
					Field field = classObject.getDeclaredField(fieldName);
					field.setAccessible(true);
					ColumnName columnName = (ColumnName) field.getAnnotation(ColumnName.class);
					if(columnName == null){
						throw new RequestFailureException("Invalid order by column name "+fieldName);
					}
					databaseColumnName = columnName.value();
				} catch (NoSuchFieldException | SecurityException e) {
					throw new RequestFailureException("Invalid field name : "+fieldName);
				}
				orderByColumnNames.add(databaseColumnName);
			}
		}
	}
}
