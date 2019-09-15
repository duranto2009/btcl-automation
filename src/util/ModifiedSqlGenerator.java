package util;

import annotation.*;
import common.RequestFailureException;
import common.StringUtils;
import connection.DatabaseConnection;
import dataConverter.IDataConverter;
import org.apache.log4j.Logger;
import org.jdom.IllegalTargetException;
import repository.RepositoryManager;
import validator.DTOValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static common.StringUtils.*;

public class ModifiedSqlGenerator {
	static Logger logger = Logger.getLogger(SqlGenerator.class);
	static Map<Class<?>,List<Field>> mapOfCurrentTimeAnnotatedFieldListToClassObject = new ConcurrentHashMap<>();
	static private List<Field> getCurrentTimeAnnotatedFieldList(Class<?> classObject) throws Exception{
		if(!mapOfCurrentTimeAnnotatedFieldListToClassObject.containsKey(classObject)){
			// synchronization is not used here intentionally
			Field[] fields = classObject.getDeclaredFields();
			List<Field> fieldList = new ArrayList<>();
			for(Field field: fields){
				if(field.getAnnotation(CurrentTime.class)!=null){
					field.setAccessible(true);
					fieldList.add(field);
				}
			}
			mapOfCurrentTimeAnnotatedFieldListToClassObject.put(classObject, fieldList);
		}
		return mapOfCurrentTimeAnnotatedFieldListToClassObject.get(classObject);
	}
	static void populateObjectWithCurrentTime(Object object,Class<?> classObject) throws Exception{
		List<Field> currentTimeAnnotatedFieldList = getCurrentTimeAnnotatedFieldList(classObject);
		for(Field currentTimeAnnotatedField: currentTimeAnnotatedFieldList){
			currentTimeAnnotatedField.set(object, CurrentTimeFactory.getCurrentTime());
		}
	}
	
	static  public<T> List<T> getAllObjectForRepository(Class<T> classObject, boolean isFirstReload) throws Exception{
		
		String conditionString = getConditionStringForRepository(classObject,  isFirstReload);
		return getAllObjectList(classObject,  conditionString);
	}
	static public Object getPrimaryKeyValue(Object entity) throws Exception {
		Field field = getPrimaryField(entity.getClass());
		field.setAccessible(true);
		return field.get(entity);
	}
	static public ResultSet getAllResultSetForRepository(Class<?> classObject, boolean isFirstReload) throws Exception{
		String conditionString = getConditionStringForRepository(classObject,  isFirstReload);
		return getAllResultSetOfTable(classObject,  conditionString);
	}
	
	static private String getConditionStringForRepository(Class<?> classObject,boolean isFirstReload) throws Exception{
		String conditionString = isFirstReload?"":" where "+getColumnName(classObject, "lastModificationTime")+" >= "+RepositoryManager.lastModifyTime;
		return conditionString;
	}
	
	
	static public int deleteEntityByIDList(Class<?> classObject,List<Long> idList) throws Exception{

		int numberOfAffectedRow;
		String sql = "update "+getTableName(classObject)+" set "+getColumnName(classObject, "isDeleted")+
				" = 1 , "+getLastModificationTimeColumnName(classObject)+"="+CurrentTimeFactory.getCurrentTime()
				+" where "+getPrimaryKeyColumnName(classObject)+" in "+StringUtils.getCommaSeparatedString(idList);		
		Statement stmt = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement();
		numberOfAffectedRow = stmt.executeUpdate(sql);
		stmt.close();
		updateSequencerTable(classObject);
		return numberOfAffectedRow;
	}
	
	

	static public int deleteEntityByID(Class<?> classObject,  long ID) throws Exception{


		int numberOfAffectedRow;
		String sql = "update "+getTableName(classObject)+" set "+getColumnName(classObject, "isDeleted")+
				" = 1 , "+getLastModificationTimeColumnName(classObject)+"="+CurrentTimeFactory.getCurrentTime()+" where "+getPrimaryKeyColumnName(classObject)+" = "+ID;		
		Statement stmt = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement();
		numberOfAffectedRow = stmt.executeUpdate(sql);
		stmt.close();
		updateSequencerTable(classObject);
		return numberOfAffectedRow;
	} 
	

	static public int deleteHardEntityByID(Class<?> classObject,  long ID) throws Exception{

		int numberOfAffectedRow;
		String sql = "delete from "+getTableName(classObject)+
				"  where "+getPrimaryKeyColumnName(classObject)+" = "+ID;
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		Statement stmt = databaseConnection.getNewStatement();
		numberOfAffectedRow = stmt.executeUpdate(sql);
		stmt.close();
		return numberOfAffectedRow;
		
	}
	static private Object handleEnum (Field field, Object object) throws Exception {
		Object value = null;
		if(field.getAnnotation(Enumerated.class) != null){
			EnumType enumType =  field.getAnnotation(Enumerated.class).value();
			value = (enumType == EnumType.STRING) ? ((Enum)field.get(object)).name() : ((Enum)field.get(object)).ordinal();
		}else {
			value = ((Enum)field.get(object)).name();
		}
		return value;
	}

	static public ColumnNameValuePair getColumnNameValuePair(Field field,Object object) throws Exception{
		field.setAccessible(true);
		boolean isEnum = field.getType().isEnum() ;
		String columnName = field.getAnnotation(ColumnName.class).value();
		Object value = null;
		if(isEnum){
			value = handleEnum(field, object);
		}else {
			value = field.get(object);
		}

		return new ColumnNameValuePair(columnName, value);
	}
	
	public static<T> T getObjectByID(Class<T> classObject,long ID) throws Exception{
		return getObjectByID(classObject, classObject, ID);
		
	}

	public static<T> T getObjectByID(Class<T> classObject,Class<? extends T> subClassObject,long ID) throws Exception{
		String conditionString = " where "+getPrimaryKeyColumnName(classObject)+" = "+ID+ 
				(hasColumn(classObject, "isDeleted", false)?  (" and "+getColumnName(classObject, "isDeleted")+"=0"):""); 
		List<? extends T> resultList = getAllObjectList(classObject,subClassObject,conditionString);
		
		return resultList.size()>0? resultList.get(0):null;
		
	}

	public static<T> T getObjectByIDForUpdate(Class<T> classObject,Class<? extends T> subClassObject,long ID) throws Exception{
		String conditionString = " where "+getPrimaryKeyColumnName(classObject)+" = "+ID+ 
				(hasColumn(classObject, "isDeleted", false)?  (" and "+getColumnName(classObject, "isDeleted")+"=0"):"")
				+" FOR UPDATE"; 
		List<? extends T> resultList = getAllObjectList(classObject,subClassObject,conditionString);
		
		return resultList.size()>0? resultList.get(0):null;
		
	}


	
	public static<T> List<T> getObjectListByIDList(Class<T> classObject,Class<? extends T> subClassObject,Collection<Long> IDList,Object... conditionString) throws Exception{

		if(conditionString.length>1){
			throw new Exception("Invalid method call");
		}
		List<T> objectList = new ArrayList<>();
		if(IDList.isEmpty()){
			return objectList;
		}
		String primaryKeyColumnName = getPrimaryKeyColumnName(classObject);
		StringBuilder conditionBuilder = new StringBuilder(" where ");
		conditionBuilder.append(" ").append(primaryKeyColumnName).append(" IN (");
		
		int i = 0;
		for(Long id: IDList)
		{
			conditionBuilder.append(id);
			i++;
			if(i < IDList.size())
			{
				conditionBuilder.append(",");
			}
		}
		
		conditionBuilder.append(")");
		if(conditionString.length==1){
			conditionBuilder.append("  and "+conditionString[0]);
		}
		return getAllObjectList(classObject,  conditionBuilder.toString());
	}
	
	
	
	
	
	public static<T> List<T> getObjectListByIDList(Class<T> classObject,Collection<Long> IDList,Object... conditionString) throws Exception{

		return getObjectListByIDList(classObject, classObject, IDList, conditionString);
	}
	
	public static<T> T getObjectFullyPopulatedByID(Class<T> classObject,long ID ) throws Exception{
		List<T> list = getAllObjectListFullyPopulated(classObject,  " where "+getPrimaryKeyColumnName(classObject)+" = "+ID);
		return list.isEmpty()?null:list.get(0);
	}
	public static<T> List<T> getObjectFullyPopulatedByString(Class<T> classObject,Object[] values, String[] columnNames ,Object... conditionString) throws Exception{
		
		if(values.length!=columnNames.length){
			throw new Exception("Invalid method call. Column names and values length should be same");
		}
		
		StringBuilder conditionBuilder = new StringBuilder(" where ");
		boolean conditionAdded = false;
		for(int i =0;i<values.length;i++){
			Object value = values[i];
			String columnName = columnNames[i];
			
			Field field = classObject.getDeclaredField(columnName);
			field.setAccessible(true);
			if(conditionAdded){
				conditionBuilder.append(" and ");
			}
			conditionAdded = true;
			if(field.getType().equals(String.class)){
				conditionBuilder.append(getColumnName(classObject, columnName)).append(" like '%").append(value).append("%'");
			}else{
				conditionBuilder.append(getColumnName(classObject, columnName)).append(" = ").append(value);
			}
		}
		if(conditionString.length>0){
			conditionBuilder.append(conditionString[0]);
		}
		
		List<T> list = getAllObjectListFullyPopulated(classObject,  conditionBuilder.toString());
		return list;
	}
	public static<T> List<T> getAllObjectListFullyPopulatedByIDList(Class<T> classObject,List<Long> IDList) throws Exception{
		return getAllObjectListFullyPopulatedByIDList(classObject,classObject, IDList);
	}
	public static<T,S extends T> List<S> getAllObjectListFullyPopulatedByIDList(Class<T> classObject,Class<S> subClassObject,List<Long> IDList) throws Exception{
		String conditionString = " where "+getPrimaryKeyColumnName(subClassObject)+" IN "+StringUtils.getCommaSeparatedString(IDList);
		return getAllObjectListFullyPopulated(classObject, subClassObject, conditionString);
	}
	public static<U,T extends U,S extends T> List<S> getAllObjectListFullyPopulated(Class<T> classObject,Class<S> subClassObject, Object ...conditionString) throws Exception{
		if(conditionString.length>1){
			throw new Exception("Invalid number of arguments .");
		}
		@SuppressWarnings("unchecked")
		List<S> subObjectList = (List<S>)getAllObjectList(classObject, subClassObject, conditionString);
		List<S> returnObjectList = new ArrayList<>();
		if(!superClassExists(classObject)){
			returnObjectList = subObjectList;
		}
		if(superClassExists(classObject) && !subObjectList.isEmpty()){
			
			Field primaryFieldOfSuperClass = getPrimaryField(classObject.getSuperclass());
			
			//Field primaryField = getPrimaryField(classObject);
			primaryFieldOfSuperClass.setAccessible(true);
			StringBuilder conditionStringBuilder = new StringBuilder(" where "+getColumnName(classObject.getSuperclass(), primaryFieldOfSuperClass.getName())+" IN ( ");
			for(int i=0;i<subObjectList.size();i++){
				Object element = subObjectList.get(i);
				long primaryKeyValue = new Long(primaryFieldOfSuperClass.get(element).toString());
				conditionStringBuilder.append(primaryKeyValue);
				if(i!=subObjectList.size()-1){
					conditionStringBuilder.append(",");
				}
			}
			conditionStringBuilder.append(" )");
			
			
			
			List<U> superObjectList = (List<U>)getAllObjectListFullyPopulated(classObject.getSuperclass(),conditionStringBuilder.toString());
			
			
			
			
			Map<Long,U> superObjectMapToPrimaryKey = new HashMap<>();
			for(U superObject: superObjectList){
				Field superObjectPrimaryField = getPrimaryField(superObject.getClass());
				superObjectPrimaryField.setAccessible(true);
				Object superObjectPrimaryFieldValue = superObjectPrimaryField.get(superObject);
				long primaryKey = new Long(superObjectPrimaryFieldValue.toString());
				superObjectMapToPrimaryKey.put(primaryKey, superObject);
			}
			for(S returnObject:subObjectList){
				Long foreignKeyValue = new Long(getForeignKeyColumnNameValuePair(returnObject, classObject).value.toString());
				if(superObjectMapToPrimaryKey.containsKey(foreignKeyValue)){
					//populateObjectFromOtherObject(returnObject, classObject.getSuperclass(), superObjectMapToPrimaryKey.get(foreignKeyValue), classObject.getSuperclass());
					
					populateObjectFromOtherObject(superObjectMapToPrimaryKey.get(foreignKeyValue),returnObject,  (Class<U>)classObject.getSuperclass());
					returnObjectList.add(returnObject);
				}
			}
			
		}
		return returnObjectList;
	}
	public static<T> List<T> getAllObjectListFullyPopulated(Class<T> classObject,Object ...conditionString ) throws Exception{
		return getAllObjectListFullyPopulated(classObject, classObject, conditionString);
	}
	
	public static<T,U extends T> List<U> getAllObjectList(Class<T> classObject,Class<U> subClassObject,Object ...conditionString) throws Exception{
		/*if(conditionString.length>1){
			throw new Exception("Invalid number of arguments .");
		}*/
		
		ResultSet rs = getAllResultSetOfTable(classObject, conditionString);
		
		return getObjectListByResultSet(classObject, subClassObject, rs);
	}
	
	
	public static<T> List<T> getObjectListByResultSet(Class<T> classObject,ResultSet rs) throws Exception{
		return getObjectListByResultSet(classObject, classObject, rs);
	}
	
	public static<T,U extends T> List<U> getObjectListByResultSet(Class<T> classObject,Class<U> subClassObject,ResultSet rs) 
			throws Exception{
		List<U> returnObjectList = new ArrayList<>();
		
		
		while(rs.next()){
			U object = subClassObject.newInstance();
			try{
				populateObjectFromDB(object, rs, classObject);
				returnObjectList.add(object);
			}catch(Exception ex){
				logger.info("fatal:",ex);
				logger.info("primary key = "+rs.getObject(getPrimaryKeyColumnName(classObject)));
				
			}
		}

		return returnObjectList;
	}
	
	public static<T> List<T> getAllObjectList(Class<T> classObject,Object ...conditionString) throws Exception{
		
		return (List<T>)getAllObjectList(classObject, classObject, conditionString);
	}
	
	public static List<Long> getAllIDList(Class<?> classObject,Object ...conditionString) throws Exception{
		List<Long> returnIDList = new ArrayList<Long>();
		ResultSet rs = null;
		if(conditionString.length == 0){
			rs = getIDResultSetOfTable(classObject);
		}else{
			rs = getIDResultSetOfTable(classObject,  conditionString[0]);
		}
		
		while(rs.next()){
			Long ID = rs.getLong(1);
			returnIDList.add(ID);
		}
		return returnIDList;
	}
	
	public static ResultSet getIDResultSetOfTable(Class<?> classObject,Object ...conditionString) throws Exception{
		if(conditionString.length>1){
			throw new Exception("Invalid number of arguments .");
		}
		List<Field> columnFieldList = new ArrayList<Field>();
		columnFieldList.add(getPrimaryField(classObject));
		ResultSet rs = null; 
		if(conditionString.length == 0 ){
			rs = getResultSetFromColumnFieldList(classObject, columnFieldList);
		}else{
			rs = getResultSetFromColumnFieldList(classObject, columnFieldList,  conditionString[0]);
		}		
		return rs;
	}
	
	public static ResultSet getAllResultSetOfTable(Class<?> classObject,Object ...conditionString) throws Exception{
		/*if(conditionString.length>1){
			throw new Exception("Invalid number of arguments .");
		}*/
		List<String> columnNameList = getColumnNameListForSelectQuery(classObject);
		ResultSet rs = null; 
		/*if(conditionString.length == 0 ){*/
			rs = getResultSetFromColumnNameList(classObject, columnNameList,conditionString);
		/*}else{
			rs = getResultSetFromColumnNameList(classObject, columnNameList,  conditionString[0]);
		}*/		
		return rs;
	}
	
	
	private static ResultSet getResultSetFromColumnNameList(Class<?> classObject,List<String> columnNameList,Object ...conditionString ) throws Exception{
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		StringBuilder stringBuilder = new StringBuilder("select ");
		for(int i = 0;i<columnNameList.size();i++){
			if(i!=0){
				stringBuilder.append(",");
			}
			stringBuilder.append(columnNameList.get(i));
		}
		stringBuilder.append(" from ").append(getTableName(classObject));
		
		
		if(conditionString.length > 0){
			stringBuilder.append(conditionString[0]);
		}
		
		
		
		String sql = stringBuilder.toString();
		logger.debug("sql: "+sql);
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		if(conditionString.length>1){
			for(int i=1;i<conditionString.length;i++){
				ps.setObject(i, conditionString[i]);
			}
		}
		logger.info(ps);
		
		ResultSet rs = ps.executeQuery();
		
		return rs;
	}
	
	private static ResultSet getResultSetFromColumnFieldList(Class<?> classObject,List<Field> fieldList,Object ...conditionString ) throws Exception{

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		StringBuilder stringBuilder = new StringBuilder("select ");
		for(int i = 0;i<fieldList.size();i++){
			if(i!=0){
				stringBuilder.append(",");
			}
			Field field = fieldList.get(i);
			field.setAccessible(true);
			String columnName = "";
			try{
				columnName = getColumnName(classObject, field.getName());
			}catch(Exception ex){
				columnName = getColumnName(classObject.getSuperclass(), field.getName());
			}
			if(!columnName.equals("")){
				stringBuilder.append(columnName);
			}
		}
		stringBuilder.append(" from ").append(getTableName(classObject));
		if(conditionString.length == 1){
			conditionString[0] = getParitialSqlFromString(classObject, (String)conditionString[0]);
			stringBuilder.append(conditionString[0]);
		}
		String sql = stringBuilder.toString();
		logger.debug("sql: "+sql);
		Statement stmt = databaseConnection.getNewStatement();
		ResultSet rs = stmt.executeQuery(sql);
		return rs;
	}
	static private List<String> getColumnNameListForSelectQuery(Class<?> classObject) throws Exception{
		List<String> columNameList = new ArrayList<String>();
		Field primaryField = getPrimaryField(classObject);
		for(Field field: classObject.getDeclaredFields()){
			if(!field.equals(primaryField) && field.getAnnotation(ColumnName.class)!=null){
				ColumnName columnName = field.getAnnotation(ColumnName.class);
				columNameList.add(columnName.value());
			}
		}
		if(classObject.getAnnotation(ForeignKeyName.class)!=null){
			String columName = classObject.getAnnotation(ForeignKeyName.class).value();
			columNameList.add(columName);
		}
		columNameList.add(primaryField.getAnnotation(ColumnName.class).value());
		return columNameList;
	}
	static Field getForeignKeyField(Class<?> classObject) throws Exception{
		return getPrimaryField(classObject.getSuperclass());
	}
	
	/*
	 *Primary key will be at the last of the list
	 * */	
	
	
	@SuppressWarnings("unchecked")
	static private List<ColumnNameValuePair> getColumnNameValuePairList(Object object,Class<?> classObject,boolean discardNonEditableColumns,Object... objects) throws Exception{
		List<ColumnNameValuePair> columnNameValuePairs = new ArrayList<ColumnNameValuePair>();
		if(objects.length>1){
			throw new Exception("Invalid method call. Number of optional argument should be less than 2");
		}
		boolean isCustomEditEnabled = (objects.length == 1);
		
		Set<String> columnNameSet = null;
		if(objects.length == 1){
			columnNameSet=(Set<String>)(objects[0]);
		}
		
		
		Field primaryField = getPrimaryField(classObject);
		primaryField.setAccessible(true);
		for(Field field: classObject.getDeclaredFields()){
			field.setAccessible(true);
			if(!isCustomEditEnabled || isCustomEditEnabled && columnNameSet.contains(field.getName())){
				if(!primaryField.equals(field) && field.getAnnotation(ColumnName.class)!=null){
					if(!discardNonEditableColumns || field.getAnnotation(ColumnName.class).editable()){
						columnNameValuePairs.add(getColumnNameValuePair(field, object));
					}
				}
			}
		}
		
		if(superClassExists(classObject)){
			Field foreignKeyField = getForeignKeyField(classObject);
			if(!isCustomEditEnabled||isCustomEditEnabled && columnNameSet.contains(foreignKeyField.getName())){
				if(!discardNonEditableColumns || classObject.getAnnotation(ForeignKeyName.class).editable()){
					ColumnNameValuePair foreignKeyColumnNameValuePair = getForeignKeyColumnNameValuePair(object, classObject);
					if(classObject.getAnnotation(ForeignKeyName.class).editable()){
						columnNameValuePairs.add(foreignKeyColumnNameValuePair);
					}
				}
			}
		}
		columnNameValuePairs.add(getColumnNameValuePair(primaryField, object));
		return columnNameValuePairs;
	}
	
	/*
	 *Primary key will be at the last of the list
	 * */	
	static private List<ColumnNameValuePair> getColumnNameValuePairList(Object object,Class<?> classObject) throws Exception{
		List<ColumnNameValuePair> columnNameValuePairs = new ArrayList<ColumnNameValuePair>();
		if(superClassExists(classObject)){
			ColumnNameValuePair columnNameValuePair = getForeignKeyColumnNameValuePair(object, classObject);
			columnNameValuePairs.add(columnNameValuePair);
		}
		Field primaryField = getPrimaryField(classObject);
		primaryField.setAccessible(true);
		for(Field field: classObject.getDeclaredFields()){
			field.setAccessible(true);
			if(!field.equals(primaryField)  && field.getAnnotation(ColumnName.class) != null && field.getAnnotation(ColumnName.class) instanceof ColumnName  ){
				ColumnNameValuePair columnNameValuePair = getColumnNameValuePair(field, object);
				columnNameValuePairs.add(columnNameValuePair);				
			}
		}

		ColumnNameValuePair columnNameValuePair = getColumnNameValuePair(primaryField, object);
		columnNameValuePairs.add(columnNameValuePair);	
		return columnNameValuePairs;
	}
	static public String getTableName(Class<?> classObject) throws Exception{
		TableName tableNameAnnotation = classObject.getAnnotation(TableName.class);
		if(tableNameAnnotation instanceof TableName){
			return tableNameAnnotation.value();
		}else{
			throw new Exception("Table Name Annotation Not defined");
		}
	}
	static public String getColumnName(Class<?> classObject,String propertyName) throws Exception{
		if(hasColumn(classObject, propertyName, true) && !hasColumn(classObject, propertyName, false)){
			ForeignKeyName foreignKeyName = classObject.getDeclaredAnnotation(ForeignKeyName.class);
			return foreignKeyName.value();
		}
		Field field = classObject.getDeclaredField(propertyName);
		field.setAccessible(true);
		return field.getAnnotation(ColumnName.class).value();
	} 
	static public boolean isDeletedColumnName(Object object) throws Exception{
		Field field = object.getClass().getDeclaredField("isDeleted");
		field.setAccessible(true);
		boolean isDeleted = field.getBoolean(object);
		return isDeleted;
	}
	static public String getLastModificationTimeColumnName(Class<?> classObject) throws Exception{
		return getColumnName(classObject, "lastModificationTime");
	}
	
	static public int updateEntity(Object object) throws Exception{
		return updateEntity(object, object.getClass(), true, false);
	}
	
	static public int updateEntity(Object object,Class<?> classObject,boolean allowNestedUpdate,boolean applyOptimisticLocking) throws Exception{

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		DTOValidator.validate(classObject);
		if(!isEntity(classObject)){
			throw new Exception("Not an document type.");
		}

		populateObjectWithCurrentTime(object, classObject);
		long lastModificationTime = getLastModificationTime(object, classObject);
		
		long currentTime = CurrentTimeFactory.getCurrentTime();
		
		if(applyOptimisticLocking){
			Field lastModificationTimeField = classObject.getDeclaredField("lastModificationTime");
			lastModificationTimeField.setAccessible(true);
			lastModificationTimeField.set(object, currentTime);
		}
		
		if(superClassExists(classObject) && allowNestedUpdate){

			if(hasColumn(classObject.getSuperclass(), "lastModificationTime", true)) {
				Field lastModificationTimeFieldOfSuperClass = classObject.getSuperclass().getDeclaredField("lastModificationTime");
				lastModificationTimeFieldOfSuperClass.setAccessible(true);
				lastModificationTimeFieldOfSuperClass.set(object, lastModificationTime);
			}
			updateEntity(object, classObject.getSuperclass(),  true,applyOptimisticLocking);
		}
		
		List<ColumnNameValuePair> columnNameValuePairs = getColumnNameValuePairList(object, classObject,true);
		String tableName = getTableName(classObject);
		StringBuilder sqlBuilder = new StringBuilder();
		if(columnNameValuePairs.size()>=2){
			sqlBuilder.append("update ").append(tableName).append(" set ");
		}
		for(int i =0 ;i+1<columnNameValuePairs.size();i++){
			ColumnNameValuePair columnNameValuePair = columnNameValuePairs.get(i);
			sqlBuilder.append(columnNameValuePair.columnName).append(" = ? ");
			if(i!=columnNameValuePairs.size()-2){
				sqlBuilder.append(",");
			}
		}
		ColumnNameValuePair lastColumnNameValuePair = columnNameValuePairs.get(columnNameValuePairs.size()-1); 
		sqlBuilder.append(" where ").append(lastColumnNameValuePair.columnName).append(" = ?");
		
		
		List<Object> valueList = new ArrayList<>();
		for(ColumnNameValuePair columnNameValuePair : columnNameValuePairs){
			valueList.add(columnNameValuePair.value);
		}
		if(applyOptimisticLocking){
			sqlBuilder.append(" and ").append(getLastModificationTimeColumnName(classObject))
			  .append(" = ? ");
	
			valueList.add(lastModificationTime);
		}

		String sql = sqlBuilder.toString();
		logger.debug(sql);
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		int parameterIndex = 1;
		
		for(Object value: valueList){
			ps.setObject(parameterIndex++, value);
		}
		
		logger.debug("values : "+valueList);
		logger.info("update sql: "+ps);
		int cnt = ps.executeUpdate();
		ps.close();
		updateSequencerTable(classObject);
		return cnt;
	}
	
	
	
	
	static public int deleteEntity(Object object,Class<?> classObject,boolean allowNestedDelete) throws Exception{

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		if(!isEntity(classObject)){
			throw new Exception("This is not a database document");
		}

		populateObjectWithCurrentTime(object, classObject);
		if(superClassExists(classObject) && allowNestedDelete){
			deleteEntity(object, classObject.getSuperclass(),  allowNestedDelete);
		}
		String tableName = classObject.getAnnotation(TableName.class).value();
		String deleteColumnName = getColumnName(classObject, "isDeleted");
		Field primaryKeyField = getPrimaryField(classObject);
		primaryKeyField.setAccessible(true);
		ColumnNameValuePair columnNameValuePair = getColumnNameValuePair(primaryKeyField, object);
		
		String primaryKeyColumnName = columnNameValuePair.columnName;
		Object primaryKeyValue = columnNameValuePair.value;
		String sql = "update "+tableName + " set "+deleteColumnName+" = true where "+primaryKeyColumnName +" = "+primaryKeyValue;
		logger.debug(sql);
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		
		int numOfAffectedRows = ps.executeUpdate();
		ps.close();
		updateSequencerTable(classObject);
		return numOfAffectedRows;
	}
	
	static private boolean isEntity(Class<?> classObject) throws Exception{
		Annotation annotation = classObject.getAnnotation(TableName.class);
		return annotation instanceof TableName;
	}
	
	static private long getLastModificationTime(Object domainObject,Class<?> classObject) throws Exception{
		long lastModificationTime = System.currentTimeMillis();
		Field field = null;
		try {
			field = classObject.getDeclaredField("lastModificationTime");
			field.setAccessible(true);
		} catch (Exception ex) {
			logger.debug("lastModificationTime is not found in dto of class type "+classObject);
			//logger.debug("Exception " ,ex);
		}	
			
			
		if(field != null){	
			if (field.getLong(domainObject) == 0) {
				throw new Exception("lastModificationTime is not set in dto of class type "+classObject.getName());
			}else{
				lastModificationTime = field.getLong(domainObject);
			}
		}
		return lastModificationTime;
	}
	
	
	
	static public void insert(Object object) throws Exception{
		 insert(object,object.getClass(),true);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static public void insert(Object domainObject,Class<?> classObject, boolean allowNestedInsert) throws Exception{

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		
		Annotation annotation = classObject.getAnnotation(TableName.class);
		
		if( (annotation instanceof TableName )== false){
			return ;
		}
		
		populateObjectWithCurrentTime(domainObject, classObject);
		DTOValidator.validate(domainObject, classObject);
		long lastModificationTime = 0;		
		lastModificationTime = getLastModificationTime(domainObject, classObject);
		
		String tableName = ((TableName)annotation).value();
		if(superClassExists(classObject) && allowNestedInsert){
			try{
				Field lastModificationTimeFieldOfSuperClass = classObject.getSuperclass().getDeclaredField("lastModificationTime");
				lastModificationTimeFieldOfSuperClass.setAccessible(true);
				lastModificationTimeFieldOfSuperClass.set(domainObject, lastModificationTime);
			}catch(Exception ex){
				logger.debug("lastModificationTime is not found in dto of class type "+classObject.getSuperclass());
			}
			insert(domainObject,classObject.getSuperclass(), true);
		}
		
		Object primaryKey = databaseConnection.getNextID(tableName);
		setPrimaryKey(domainObject, classObject, primaryKey);
		List<ColumnNameValuePair> columnValuePairs = getColumnNameValuePairList(domainObject, classObject);
		StringBuilder sqlBuilder = new StringBuilder("");
		sqlBuilder.append("insert into ").append(tableName).append(" (");
		StringBuilder valuePart = new StringBuilder("  values(");
		for(int i = 0;i<columnValuePairs.size();i++){
			if(i!=0){
				sqlBuilder.append(",");
				valuePart.append(",");
			}
			
			ColumnNameValuePair columnNameValuePair = columnValuePairs.get(i);
			sqlBuilder.append(columnNameValuePair.columnName);
			valuePart.append("?");
		}
		sqlBuilder.append(")");
		valuePart.append(")");
		String sql = sqlBuilder.append(valuePart).toString();		
		List<Object> valueList = new ArrayList();
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		for(int i = 1;i<=columnValuePairs.size();i++){

			ps.setObject(i, columnValuePairs.get(i-1).value);
			valueList.add(columnValuePairs.get(i-1).value);
		}
		if(!ps.toString().contains("at_user_activity"))logger.info(ps);
		ps.execute();
		ps.close();
		
		updateSequencerTable(classObject);
		
	}
	
	static private boolean superClassExists(Class<?> classObject){
		if( classObject.getSuperclass()==null){
			return false;
		}
		if((classObject.getAnnotation(ForeignKeyName.class) instanceof ForeignKeyName)==false){
			return false;
		}
		if((classObject.getSuperclass().getAnnotation(TableName.class) instanceof TableName ) == false){
			return false;
		}
		return true;
	}


	
	static public Object createObject(Class<?> classObject,String value) throws Exception{
		if(Integer.TYPE.equals(classObject)){
			return new Integer(value);
		}
		if(Long.TYPE.equals(classObject)){
			return new Long(value);
		}
		return classObject.getConstructor(String.class).newInstance(value);
	}
	
	static public void setPrimaryKey(Object domainObject,Class<?> classObject,Object primaryKey) throws Exception{
		Field field  = getPrimaryField(classObject);
		if(field == null) {
			throw new Exception("No primary key annotation found");
		}
		field.setAccessible(true);
		field.set(domainObject, createObject(field.getType(), primaryKey.toString()));

//		for(Field field : classObject.getDeclaredFields()){
//			field.setAccessible(true);
//			if(field.getAnnotation(PrimaryKey.class) instanceof PrimaryKey){
//				field.set(domainObject, createObject(field.getType(), primaryKey.toString()));
//				return;
//			}
//		}
//
//		if(classObject.getSuperclass() != null){
//			setPrimaryKey(domainObject, classObject.getSuperclass(), primaryKey);
//			return;
//		}
//
//		throw new Exception("No primary key annotation found");
	}
	
	static private ColumnNameValuePair getForeignKeyColumnNameValuePair(Object object,Class<?> classObject) throws Exception{
		ColumnNameValuePair columnNameValuePair = new ColumnNameValuePair();
		String columnName;
		Object columnValue;
		columnName = classObject.getAnnotation(ForeignKeyName.class).value();
		Field field = getPrimaryField(classObject.getSuperclass());
		field.setAccessible(true);
		columnValue = field.get(object);
		columnNameValuePair.columnName = columnName;
		columnNameValuePair.value = columnValue;
		return columnNameValuePair;
	}
	static public String getPrimaryKeyColumnName(Class<?> classObject) throws Exception{
		Field field = getPrimaryField(classObject);
		String primaryKeyColumnName = field.getAnnotation(ColumnName.class).value();
		return primaryKeyColumnName;
	}
	static private Field getPrimaryField(Class<?> classObject) throws Exception{
		for(Field field :classObject.getDeclaredFields()){
			if(field.getAnnotation(PrimaryKey.class) instanceof PrimaryKey){
				return field;
			}
		}
		if(classObject.getSuperclass() != null){
			return getPrimaryField(classObject.getSuperclass());
		}
		throw new Exception("No primary key found ");
	}
	
	
	
	static String getInsertSql(Object objectInstance) throws Exception{
		Class<?> classObject = objectInstance.getClass();
		StringBuffer sql = new StringBuffer("");
		List<String> columList = new ArrayList<String>();
		List<Object> valueList = new ArrayList<Object>();
		
		Annotation tableNameAnnotation = classObject.getAnnotation(TableName.class);
		String tableName;
		if(tableNameAnnotation  instanceof TableName){
			tableName =  ((TableName)tableNameAnnotation).value();
		}else{
			throw new Exception();
		}
		
		Field fields[] = classObject.getDeclaredFields();
		
		for (Field field : fields) {
			field.setAccessible(true);
			Annotation annotation = field.getAnnotation(ColumnName.class);
			if (annotation instanceof ColumnName) {
				ColumnName columnNameAnnotation = (ColumnName) annotation;
				columList.add(columnNameAnnotation.value());
				valueList.add(field.get(objectInstance));
			}

		}
		
		sql.append("insert into ").append(tableName).append("(");
		for(int i = 0 ;i<columList.size();i++){
			if(i!=0){
				sql.append(",");
			}
			sql.append(columList.get(i));
		}
		sql.append(") values ( ");
		
		for(int i =0;i<valueList.size();i++){
			if(i!=0){
				sql.append(",");
			}
			sql.append(valueList.get(i));
		}
		sql.append(")");
		return sql.toString();
	}
	
	
	static public void populateObjectFromDB(Object objectInstance,ResultSet rs,Object ...optionalClassObject) throws Exception{
		Class<?> classObject;
		if(optionalClassObject.length > 1){
			throw new Exception("Invalid number of arguments");
		}
		if(optionalClassObject.length == 1){
			classObject = (Class<?>)optionalClassObject[0];
		}else{
			classObject = objectInstance.getClass();
		}
		
		Set<String> resultSetColumnNames = getColumnNamesFromResultSet(rs);
		
		for(Field field : classObject.getDeclaredFields()){
			Annotation annotation = field.getAnnotation(ColumnName.class);
			field.setAccessible(true);
			if(annotation instanceof ColumnName){
				String columnName = (field.getAnnotation(ColumnName.class)).value();
				if(resultSetColumnNames.contains(columnName)){
					populateField(objectInstance,field,rs,columnName);
				}
			}
		}
		if(superClassExists(classObject)){
			String databaseColumnName = getForeignKeyColumnNameValuePair(objectInstance, classObject).columnName;
			populatePrimaryKeyOfSuperClass(objectInstance, 
					classObject,rs,databaseColumnName);
			populateObjectFromDB(objectInstance, rs, classObject.getSuperclass());
		}

        injectManipulatedData(objectInstance);
	}

	private static void injectManipulatedData(Object objectInstance)
	{
        for(Field field : objectInstance.getClass().getDeclaredFields())
        {
            if (field.isAnnotationPresent(ManipulateData.class))
            {
                field.setAccessible(true);
                ManipulateData annotation = field.getAnnotation(ManipulateData.class);


                try {
                    Class<? extends IDataConverter> converterClass = annotation.value();
                    Method method = converterClass.getDeclaredMethod("convert", Object.class);
                    if(method == null)
                        continue;
                    method.setAccessible(true);
                    Object convertedValue = method.invoke(converterClass.newInstance(), field.get(objectInstance));//field.get(objectInstance));
                    field.set(objectInstance, convertedValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
	}
	
	
	private static void populatePrimaryKeyOfSuperClass(Object objectInstance,Class<?> classObject,
			ResultSet rs,String databaseColumnName) throws Exception{
		Field field = getPrimaryField(classObject.getSuperclass());
		field.setAccessible(true);
		populateField(objectInstance, field, rs, databaseColumnName);
	}
	
	static private String filterName(String name){
		String tokens[] = name.split("\\.");
		
		if(tokens.length==0){
			return name;
		}else {
			return  tokens[tokens.length-1];
		}
	} 
	
	
	static private Set<String> getColumnNamesFromResultSet(ResultSet rs) throws Exception{
		
		Set<String> columnNameHashSet = new HashSet<>();
		ResultSetMetaData rsMetaData = rs.getMetaData();
		for(int i = 1;i<=rsMetaData.getColumnCount();i++){
			columnNameHashSet.add(rsMetaData.getColumnLabel(i));
		}
		return columnNameHashSet;
	}
	
	static private void populateField(Object domainObject ,Field field , ResultSet rs, String databaseColumnName) throws Exception{
		if(field.getType().isEnum()){
			Enum enumVal = Enum.valueOf((Class<? extends Enum>) field.getType(),  (rs.getString(databaseColumnName)));
			field.set(domainObject, enumVal);
			return;
		}
		String typeName = field.getType().getName();
		typeName = filterName(typeName);
		String methodName = "get"+ typeName.toUpperCase().substring(0,1) +typeName.toLowerCase().substring(1);
		methodName = methodName.replaceAll("Integer", "Int");
		Method method = rs.getClass().getMethod(methodName, new Class[]{String.class});
		
		try{
			Object returnValueObject = method.invoke(rs, databaseColumnName);
			if(!field.getType().isPrimitive() && rs.wasNull()){
				returnValueObject = null;
			}
			try{
				field.set(domainObject, returnValueObject);
			}catch(Exception ex){
				
				if(field.getType().equals(Long.class) || field.getType().equals(Integer.class)){
					field.set(domainObject, 0);
				}else{
					throw ex;
				}
			}
		}catch(IllegalTargetException ex){
			logger.debug("exception thrown while invoking method "+methodName+"("+databaseColumnName+")");
			throw ex;
		}catch (InvocationTargetException e) {
			logger.debug("problem populating "+field.getName());
			throw e;
		}
		
	}
	public static void updateSequencerTable(Class<?> classObject) throws Exception{
 		long lastModificationTime = CurrentTimeFactory.getCurrentTime();
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		String tableName = getTableName(classObject);
		databaseConnection.addSequencerSql(tableName, lastModificationTime);
	}
	/*
	public static void populateObjectFromOtherObject(Object destinationObject,Class<?> destinationClassObject,Object sourceObject,Class<?> sourceClassObject) throws Exception{
		if(!destinationClassObject.equals(sourceClassObject)){
			throw new Exception("Source class "+sourceClassObject+"  and Destination class "+destinationClassObject+" type does not match");
		}
		Field[] sourceFields = sourceClassObject.getDeclaredFields();
		for(Field sourceField: sourceFields){
			sourceField.setAccessible(true);
			Object sourceValue = sourceField.get(sourceObject);
			Field destinationField = destinationClassObject.getDeclaredField(sourceField.getName());
			destinationField.setAccessible(true);
			try{
				if(!Modifier.isFinal(destinationField.getModifiers())){
					destinationField.set(destinationObject, sourceValue);
				}
			}catch(Exception ex){
				logger.debug("can not set the value of field "+destinationField.getName()+" of class"+destinationClassObject);
			}
		}
		if(destinationClassObject.getSuperclass()!=Object.class){
			populateObjectFromOtherObject(destinationObject, destinationClassObject.getSuperclass(), sourceObject, sourceClassObject.getSuperclass());
		}
	}*/
	
	
	public static<T,U extends T,V extends T> void populateObjectFromOtherObject(U srcObject,V descObject,Class<T> classObject){
		if(classObject.equals(Object.class)){
			return;
		}
		populateObjectFromOtherObjectNonRecursively(srcObject, descObject, classObject);
		populateObjectFromOtherObject(srcObject, descObject, classObject.getSuperclass());
	}
	
	
	
	public static String getParitialSqlFromString(Class<?> classObject,String conditionString){
		
		String tokens[] = conditionString.split("[,| |\\.||=]");
		
		for(String token: tokens){
			try{
				String columnName = getColumnName(classObject, token);
				conditionString = conditionString.replaceAll(token, columnName);
			}catch(Exception ex){
				//ex.printStackTrace();
			}
		}
		
		return conditionString;
	}
	
	public static boolean isActivated(Class<?> classObject,long ID,DatabaseConnection databaseConnection) throws Exception{
		if(classObject.equals(Object.class)){
			throw new Exception("No method named isActivated() founed");
		}
		Object returnvalue ;
		try{
			Method isActivatedMethod = classObject.getDeclaredMethod( "isActivated");
			Object domainObject = getObjectByID(classObject, ID);
			if(domainObject == null){
				throw new Exception("No dto found with class type "+classObject+" where id = "+ID);
			}
			returnvalue = isActivatedMethod.invoke(domainObject);
		}catch(NoSuchMethodException noSuchMethodException){
			returnvalue = isActivated(classObject.getSuperclass(), ID,databaseConnection);
		}
		return (Boolean)returnvalue;
	}
	public static String getSelectQueryForNestedQuery(Class<?> classObject,String columnName) throws Exception{
		SearchFieldFromReferenceTable searchFieldFromReferenceTable = null;
		try{
			Field field = classObject.getDeclaredField(columnName);
			field.setAccessible(true);
			searchFieldFromReferenceTable = (SearchFieldFromReferenceTable)field.getDeclaredAnnotation(SearchFieldFromReferenceTable.class);
		}catch(NoSuchFieldException ex){
			searchFieldFromReferenceTable = (SearchFieldFromReferenceTable)classObject.getDeclaredAnnotation(SearchFieldFromReferenceTable.class);
		}
		
		Class<?> referenceTableClassObject = searchFieldFromReferenceTable.entityClass();
		String referenceTableOperator = searchFieldFromReferenceTable.operator();
		String referenceTableColumnName = searchFieldFromReferenceTable.entityColumnName();
		Field primaryFieldOfReferenceTable = getPrimaryField(referenceTableClassObject);
		primaryFieldOfReferenceTable.setAccessible(true);
		
		StringBuilder selectQueryBuilder =new StringBuilder();
		selectQueryBuilder.append("select ").append(getColumnName(referenceTableClassObject, primaryFieldOfReferenceTable.getName()))
							.append(" from ").append(getTableName(referenceTableClassObject)).append(" where ").append(getColumnName(referenceTableClassObject, referenceTableColumnName))
							.append(" ").append(referenceTableOperator).append(" ?");
		
		if(hasColumn(referenceTableClassObject, "isDeleted", false)){
			selectQueryBuilder.append(" and ")
								.append(getColumnName(referenceTableClassObject, "isDeleted"))
								.append("=0");
		}
		
		if(!StringUtils.isBlank(searchFieldFromReferenceTable.fixedCondition())){
			selectQueryBuilder.append(" ")
								.append(searchFieldFromReferenceTable.fixedCondition());
		}
		String nestedQueryString = selectQueryBuilder.toString();
		logger.debug("nested query: "+nestedQueryString);
		return nestedQueryString;
	}
	
	public static boolean hasColumn(Class<?> classObject,String columnName,boolean isNestedField){
		if(classObject.equals(Object.class)){
			return false;
		}
		try{
			classObject.getDeclaredField(columnName);
			return true;
		}catch(Exception ex){
			if(isNestedField){
				return hasColumn(classObject.getSuperclass(), columnName, true);
			}else{
				return false;
			}
		}
		
	}
	
	public static boolean hasAnnotation(Class<?> classObject,String columnName,Class<? extends Annotation> annotationClass) throws Exception{
		if(!hasColumn(classObject, columnName,false)){
			return false;
		}
		Field field = classObject.getDeclaredField(columnName);
		
		field.setAccessible(true);
		return field.getDeclaredAnnotation(annotationClass)!=null; 
	}
	
	public static String getInnerSqlOperator(Class<?> classObject,String columnName) throws Exception{
		Class<SearchFieldFromReferenceTable> annotationClass = SearchFieldFromReferenceTable.class;
		if(!hasAnnotation(classObject, columnName, annotationClass)){
			throw new Exception("Invalid Method call.");
		}
		Field field = classObject.getDeclaredField(columnName);
		field.setAccessible(true);
		
		return ((SearchFieldFromReferenceTable)field.getDeclaredAnnotation(annotationClass)).operator();
	}
	

	public static List<?> getSelectedColumnListFromSearchCriteria(Class<?> classObject,String[] keys,String[] operators,String[] dtoColumnNames,
			Map<String, String> criteriaMap,String fixedCondition,String columnName,Object... conditionString) throws Exception{

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		if(keys.length != operators.length || keys.length!=dtoColumnNames.length){
			throw new Exception("Invalid method call. keys.length,operators.length,dtoColumnNames.length should be same");
		}
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select ").append(getColumnName(classObject, columnName)).append(" from ")
					.append(getTableName(classObject)).append(" ");
		List<Object> values = new ArrayList<Object>();
		boolean conditionAdded = false;
		for(int i=0;i<keys.length;i++){
			String key = keys[i];
			String userInput = trim(criteriaMap.get(key));
			String operator = toUpperCase(operators[i]);
			String dtoColumnName = trim(dtoColumnNames[i]);
			if(!isBlank(userInput)){
				if(!conditionAdded){
					sqlBuilder.append(" WHERE ");
					conditionAdded = true;
				}else{
					sqlBuilder.append(" AND ");
				}
				
				
				if(operator.equals("NULL")){
					sqlBuilder.append(" ").append(getColumnName(classObject, dtoColumnName))
								.append(" IS ").append(userInput.equals("0")?" NULL ":" NOT NULL ");
					
				}else if(hasAnnotation(classObject, dtoColumnName, SearchFieldFromMethod.class)){
					Field field = classObject.getDeclaredField(dtoColumnName);
					field.setAccessible(true);
					SearchFieldFromMethod searchFieldFromMethod = (SearchFieldFromMethod)field.getDeclaredAnnotation(SearchFieldFromMethod.class);
					Class<?> serviceClass = searchFieldFromMethod.objectClass();
					String methodName = searchFieldFromMethod.methodName();
					Class<?> parameterType = searchFieldFromMethod.parameterType();
					Method method = serviceClass.getDeclaredMethod(methodName, parameterType);
					List<Number> list = (List<Number>)method.invoke(null, parameterType.equals(Hashtable.class)?criteriaMap: createObject(parameterType, userInput));
					if(list.isEmpty()){
						return new ArrayList<Long>();
					}
					sqlBuilder.append(" ").append(getColumnName(classObject, dtoColumnName)).append(" ").append(operator).append(" ").append(common.StringUtils.getCommaSeparatedString(list));
				}else if(operator.equals("IN")){
					sqlBuilder.append(" ").append(getColumnName(classObject, dtoColumnName)).append(" ").append(operator).append("(").
					append(getSelectQueryForNestedQuery(classObject, dtoColumnName)).append(")");
					userInput = userInput.replaceAll("_", "\\\\_");
					values.add(userInput+"%");
					
				}else{
					sqlBuilder.append(" "+getColumnName(classObject, dtoColumnName)+" "+operator+" ? ");
					if(operator.equals("LIKE")){
						userInput = userInput.replaceAll("_", "\\\\_");
						values.add(userInput+"%");
					}else{
						values.add(userInput.matches("[0-9]{1,2}/[0-9]{1,2}/[0-9]{2,4}")?TimeConverter.getTimeFromString(userInput):userInput);
					}
				}
				/*
				if(hasAnnotation(classObject, dtoColumnName, SearchFieldFromMethod.class)){
					// do nothing
				}else if(StringUtils.containsIgnoreCase(StringUtils.trimToEmpty(operator), "like")||
						hasAnnotation(classObject, dtoColumnName, SearchFieldFromReferenceTable.class) && StringUtils.containsIgnoreCase(StringUtils.trimToEmpty(getInnerSqlOperator(classObject, dtoColumnName)), "like")){
					values.add("%"+userInput+"%");
				}else{
					values.add(userInput.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")?TimeConverter.getTimeFromString(userInput):userInput);
				}*/
			}
		}
		if(!isBlank(fixedCondition)){

			if(!conditionAdded){
				sqlBuilder.append(" WHERE ");
				conditionAdded = true;
			}else{
				sqlBuilder.append(" AND ");
			}
			sqlBuilder.append(" "+fixedCondition);
		
		}
		logger.debug("sql: "+sqlBuilder);
		logger.debug("values: "+values);
		
		if(conditionString.length>0){
			sqlBuilder.append(conditionString[0]);
		}
		
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sqlBuilder.toString());
		for(int i=0;i<values.size();i++){
			ps.setObject(i+1, values.get(i));
		}
//		logger.info("PreparedStatement: "+ps);
		ResultSet rs = ps.executeQuery();
		List<Object> selectedColumnList = new ArrayList<Object>();
		while(rs.next()){
			selectedColumnList.add(rs.getLong(1));
		}
		rs.close();
		ps.close();
		return selectedColumnList;
	}
	

	public static List<Long> getIDListFromSearchCriteria(Class<?> classObject,String[] keys,String[] operators,String[] dtoColumnNames,
			Map<String, String> criteriaMap,String fixedCondition) throws Exception{
		Field primaryField = getPrimaryField(classObject);
		primaryField.setAccessible(true);
		return (List<Long>)getSelectedColumnListFromSearchCriteria(classObject, keys, operators, dtoColumnNames, criteriaMap, fixedCondition,  primaryField.getName());
	}
	
	
	public static<T> int updateEntityByPropertyList(T object,Class<T> classObject,boolean allowNestedUpdate,boolean applyOptimisticLocking,
			String[] propertyNames,Object ...currentTimeOption) throws Exception{

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		
		if(!isEntity(classObject)){
			throw new Exception("Not an document type.");
		}

		populateObjectWithCurrentTime(object, classObject);
		long lastModificationTime = getLastModificationTime(object, classObject);
		
		long currentTime = (currentTimeOption.length==1?(Long)currentTimeOption[0]:System.currentTimeMillis());
		
		if(applyOptimisticLocking){
			// setLastModificationTime
			Field lastModificationTimeField = classObject.getDeclaredField("lastModificationTime");
			lastModificationTimeField.setAccessible(true);
			lastModificationTimeField.set(object, currentTime);
		}
		
		Set<String> propertyNameSet = new HashSet<String>();
		
		for(String propertyName: propertyNames){
			propertyNameSet.add(propertyName);
		}
		
		List<ColumnNameValuePair> columnNameValuePairs = getColumnNameValuePairList(object, classObject,true,propertyNameSet);
		String tableName = getTableName(classObject);
		StringBuilder sqlBuilder = new StringBuilder();
		if(columnNameValuePairs.size()>=2){
			sqlBuilder.append("update ").append(tableName).append(" set ");
		}
		for(int i =0 ;i+1<columnNameValuePairs.size();i++){
			ColumnNameValuePair columnNameValuePair = columnNameValuePairs.get(i);
			sqlBuilder.append(columnNameValuePair.columnName).append(" = ? ");
			if(i!=columnNameValuePairs.size()-2){
				sqlBuilder.append(",");
			}
		}
		ColumnNameValuePair lastColumnNameValuePair = columnNameValuePairs.get(columnNameValuePairs.size()-1); 
		sqlBuilder.append(" where ").append(lastColumnNameValuePair.columnName).append(" = ?");
		
		
		List<Object> valueList = new ArrayList<>();
		for(ColumnNameValuePair columnNameValuePair : columnNameValuePairs){
			valueList.add(columnNameValuePair.value);
		}
		if(applyOptimisticLocking){
			sqlBuilder.append(" and ").append(getLastModificationTimeColumnName(classObject))
			  .append(" = ? ");
	
			valueList.add(lastModificationTime);
		}

		String sql = sqlBuilder.toString();
		logger.debug(sql);
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		int parameterIndex = 1;
		
		for(Object value: valueList){
			ps.setObject(parameterIndex++, value);
		}
		
		logger.debug("values : "+valueList);
		int cnt = ps.executeUpdate();
		ps.close();
		updateSequencerTable(classObject);
		return cnt;
	}
	public static Object getPropertyValueByPropertyName(Object object, String propertyName) throws Exception{
		Field field = object.getClass().getDeclaredField(propertyName);
		field.setAccessible(true);
		return  field.get(object);
	}
	public static<T> void populateObjectFromOtherObjectNonRecursively(T sourceObj,T descObj,Class<T> classObject){
		
		if(sourceObj == null){
			throw new RequestFailureException("Source obj can not be null");
		}
		if(descObj == null){
			throw new RequestFailureException("Desc obj can not be null");
		}
		
		Field[] fields = classObject.getDeclaredFields();
		for(Field field: fields){
			field.setAccessible(true);
			if(!Modifier.isFinal(field.getModifiers())){
				try{
					field.set(descObj, field.get(sourceObj));
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}
	public static ResultSet getResultSetBySqlPair(SqlPair sqlPair) throws Exception{
//		logger.info("SqlPair: " + sqlPair);
		PreparedStatement ps = DatabaseConnectionFactory.getCurrentDatabaseConnection()
								.getNewPrepareStatement(sqlPair.sql);

		for(int index = 1;index<=sqlPair.objectList.size();index++){
			ps.setObject(index, sqlPair.objectList.get(index-1));
		}
		logger.info(ps);
		return ps.executeQuery();
	}
	@SuppressWarnings("unchecked")
	public static<T> List<T> getSingleColumnListByResultSet(ResultSet rs,Class<T> classObject) throws Exception{
		List<T> resultList = new ArrayList<>();
		while(rs.next()){
			resultList.add((T)rs.getObject(1));
		}
		return resultList;
	}
}
